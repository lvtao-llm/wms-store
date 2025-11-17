package com.ruoyi.system.camera;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@Slf4j
@RestController
@RequestMapping("/api/camera")
public class CameraController implements CommandLineRunner {

//    private static final Logger log = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    private IWmsDeviceService wmsDeviceService;

    @Autowired
    private CameraServeice cameraServeice;

    @Autowired
    MediaServer mediaServer;

    @Value("${flv.port:10041}")
    private Integer port;

    @Value("${server.port:10030}")
    private Integer serverPort;

    /**
     * 启动摄像头流
     */
    @GetMapping("/stream/start/{cameraId}/{channel}")
    public ResponseEntity<Map<String, Object>> startStream(@PathVariable String cameraId, @PathVariable String channel) {
        try {
            if (!"1".equals(channel) && !"2".equals(channel)) {
                Map<String, Object> res = new HashMap<>();
                res.put("success", false);
                res.put("message", "channel只能是1或2");
                return ResponseEntity.status(500).body(res);
            }

            JSONObject data = getCameraInfo(cameraId);
            String id = UUID.randomUUID().toString().replace("-", "");
            String rtspUrl = null;

            if ("1".equals(channel)) {
                rtspUrl = String.format("rtsp://%s:%s@%s:%s/h264/%s/sub/av_stream",
                        data.getString("username1"),
                        data.getString("password1"),
                        data.getString("ip1"),
                        data.getString("port1"),
                        data.getString("channel1")
                );
            }
            if ("2".equals(channel)) {
                rtspUrl = String.format("rtsp://%s:%s@%s:%s/h264/%s/sub/av_stream",
                        data.getString("username2"),
                        data.getString("password2"),
                        data.getString("ip2"),
                        data.getString("port2"),
                        data.getString("channel2")
                );
            }
            String[] f = new String[]{"D:\\吕胤希\\wx_camera_1731195787660.mp4", "D:\\吕胤希\\WeChat_20240819093216.mp4"};
            rtspUrl = f[(int) (Math.random() * 2)];

//                mpegCommand = String.format("ffmpeg -rtsp_transport tcp -max_delay 500000 -use_wallclock_as_timestamps 1 -i " + rtspUrl + " -vf fps=2 -c:v libx264 -f flv ");
//                mpegCommand = String.format("ffmpeg -rtsp_transport tcp -i %s -c:v copy -an -f flv ", rtspUrl);
            String mpegCommand = String.format("ffmpeg -stream_loop -1 -re -i %s -c:v libx264 -preset ultrafast -tune zerolatency -g 25 -c:a aac -f flv http://localhost:%s/api/camera/stream/receive/%s", rtspUrl, serverPort, id);

            FFmpegWrap fFmpegWrap = new FFmpegWrap(id, cameraId + "-" + channel, rtspUrl, mpegCommand);
            cameraServeice.activeWrap.put(id, fFmpegWrap);


            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("id", id);
            res.put("message", "流启动成功");
            res.put("port", port);
            res.put("rtspUrl", rtspUrl);
            res.put("mpeg-command", mpegCommand);
            log.info("启动流成功: {}", res);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "启动流失败: " + e.getMessage());
            log.error("启动流失败: {}", res);
            return ResponseEntity.status(500).body(res);
        }
    }

    @RequestMapping("/stream/receive/{id}")
    @ResponseBody
    public String receive(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            ByteBuf tempCache = cameraServeice.activeWrap.get(id).getTempCache();
            ServletInputStream inputStream = request.getInputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                tempCache.writeBytes(buffer, 0, len);
                while (tempCache.readableBytes() >= 15) {
                    int dataSize = ((tempCache.getByte(1) & 0xFF) << 16)
                            | ((tempCache.getByte(2) & 0xFF) << 8)
                            | (tempCache.getByte(3) & 0xFF);
                    int fullTagSize = 11 + dataSize + 4;
                    if (tempCache.readableBytes() < fullTagSize) break;
                    ByteBuf tagBuf = tempCache.readBytes(fullTagSize);
                    cameraServeice.activeWrap.get(id).broadcast(tagBuf);
                }
            }
        } catch (Exception e) {
            log.error("接收数据失败: {}", e.getMessage());
        }
        return "1";
    }


    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        LocalDateTime time = LocalDateTime.now();
                        Set<Map.Entry<String, FFmpegWrap>> entries = cameraServeice.activeWrap.entrySet();
                        List<String> key = new ArrayList<>();
                        for (Map.Entry<String, FFmpegWrap> entry : entries) {
                            long duration = Duration.between(time, entry.getValue().getTime()).abs().getSeconds();
                            if (duration > 100) {
                                log.info("关闭不活跃的FFmpegWrap:{}({})", entry.getKey(), entry.getValue().getId());
                                entry.getValue().stop();
                                key.add(entry.getKey());
                            }
                        }
                        for (String k : key) {
                            FFmpegWrap remove = cameraServeice.activeWrap.remove(k);
                            if (remove != null) {
                                for (Channel s : remove.getSessions()) {
                                    s.close();
                                    remove.getSessions().remove(s);
                                }
                                remove.stop();
                            }
                        }
                        Thread.sleep(1000); // 使
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        InetSocketAddress socketAddress = new InetSocketAddress(port);
        mediaServer.start(socketAddress);
    }


    private JSONObject getCameraInfo(String cameraId) throws Exception {
        WmsDevice wmsDevice = wmsDeviceService.selectWmsDeviceById(Long.parseLong(cameraId));
        if (wmsDevice == null) {
            throw new Exception("设备不存在");
        }
        if (wmsDevice.getData() == null) {
            throw new Exception("设备数据为空");
        }
        JSONObject cameraInfo;
        try {
            cameraInfo = JSONObject.parseObject(wmsDevice.getData());
        } catch (Exception e) {
            throw new Exception("设备数据格式错误:" + e.getMessage());
        }
        return cameraInfo;
    }
}
