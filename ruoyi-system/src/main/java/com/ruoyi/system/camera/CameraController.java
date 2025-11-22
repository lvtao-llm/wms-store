package com.ruoyi.system.camera;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private static final Logger log = LoggerFactory.getLogger("camera-stream");

    @Autowired
    private IWmsDeviceService wmsDeviceService;

    @Autowired
    private CameraService cameraServeice;

    @Autowired
    MediaServer mediaServer;

    @Value("${media-server.port:10041}")
    private Integer port;

    @Value("${media-server.ffmpeg-command:ffmpeg -rtsp_transport udp -i %s -c:v copy -an -fflags +genpts -use_wallclock_as_timestamps 1 -flags +global_header -flush_packets 1 -f flv}")
    private String ffmpegCommand;


    @Value("${server.port:10030}")
    private Integer serverPort;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

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
            String mpegCommand = String.format(ffmpegCommand + " http://localhost:%s/api/camera/stream/receive/%s", rtspUrl, serverPort, id).replace("  ", " ");
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
    public void receive(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                ByteBuf tempBuf = Unpooled.copiedBuffer(buffer, 0, len);
                cameraServeice.activeWrap.get(id).broadcast(tempBuf);
                tempBuf.release();
            }
        } catch (Exception e) {
            log.error("{}-接收数据失败: {}", id, e.getMessage());
        }
    }

    @PostConstruct
    public void run() {
        scheduler.scheduleAtFixedRate(this::checkInactiveWraps, 0, 1, TimeUnit.SECONDS);
        // 改为使用scheduler执行一次
        scheduler.schedule(() -> {
            try {
                InetSocketAddress socketAddress = new InetSocketAddress(port);
                mediaServer.start(socketAddress);
            } catch (Exception e) {
                log.error("媒体服务器启动失败: {}", e.getMessage());
            }
        }, 0, TimeUnit.SECONDS);
    }

    private void checkInactiveWraps() {
        try {
            LocalDateTime time = LocalDateTime.now();
            Set<Map.Entry<String, FFmpegWrap>> entries = cameraServeice.activeWrap.entrySet();
            List<String> key = new ArrayList<>();
            for (Map.Entry<String, FFmpegWrap> entry : entries) {
                long duration = Duration.between(time, entry.getValue().getTime()).abs().getSeconds();
                if (duration > 100) {
                    log.info("{}-关闭不活跃的ffmpegWrap:{}", entry.getKey(), entry.getValue().getId());
                    entry.getValue().stop();
                    key.add(entry.getKey());
                }
            }
            for (String k : key) {
                FFmpegWrap remove = cameraServeice.activeWrap.remove(k);
                if (remove != null) {
                    for (Channel s : remove.getChannels()) {
                        s.close();
                        remove.getChannels().remove(s);
                    }
                    remove.stop();
                }
            }
        } catch (Exception e) {
            log.error("关闭不活跃的ffmpegWrap失败: {}", e.getMessage());
        }
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
