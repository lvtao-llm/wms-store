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

    @Value("${media-server.ip:10.61.102.150}")
    private String NVRIp;
    @Value("${media-server.username:admin}")
    private String NVRUsername;
    @Value("${media-server.password:Ll112233}")
    private String NVRPassword;
    @Value("${media-server.port:556}")
    private String NVRPort;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * 启动摄像头流
     */
    @GetMapping("/stream/start/{type}/{id}/{num}")
    public ResponseEntity<Map<String, Object>> startStream(@PathVariable String type, @PathVariable String id, @PathVariable String num) {
        try {
            if (!"1".equals(num) && !"2".equals(num)) {
                Map<String, Object> res = new HashMap<>();
                res.put("success", false);
                res.put("message", "channel只能是1或2");
                return ResponseEntity.status(500).body(res);
            }

            String streamId = UUID.randomUUID().toString().replace("-", "");
            String rtspUrl;
            String channelNum = "";

            if ("area".equals(type)) {
                JSONObject data = getCameraInfo(id);
                if ("1".equals(num)) {
                    channelNum = data.getString("channel1");
                }
                if ("2".equals(num)) {
                    channelNum = data.getString("channel2");
                }
            }
            if ("device".equals(type)) {
                JSONObject data = getCameraInfo(Long.parseLong(id));
                if ("1".equals(num)) {
                    channelNum = data.getString("channel1");
                }
                if ("2".equals(num)) {
                    channelNum = data.getString("channel2");
                }
            }

            rtspUrl = String.format("rtsp://%s:%s@%s:%s/h264/%s/sub/av_stream",
                    NVRUsername,
                    NVRPassword,
                    NVRIp,
                    NVRPort,
                    channelNum);

            String mpegCommand = String.format(ffmpegCommand + " http://localhost:%s/api/camera/stream/receive/%s", rtspUrl, serverPort, streamId).replace("  ", " ");
            FFmpegWrap fFmpegWrap = new FFmpegWrap(streamId, id + "-" + num, rtspUrl, mpegCommand);
            cameraServeice.activeWrap.put(streamId, fFmpegWrap);

            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("id", streamId);
            res.put("message", "流启动成功");
            res.put("port", port);
            res.put("rtspUrl", rtspUrl);
            res.put("mpeg-command", mpegCommand);
            res.put("code", 200);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("data", res);
            response.put("success", true);
            response.put("id", streamId);
            response.put("message", "流启动成功");
            response.put("port", port);
            response.put("rtspUrl", rtspUrl);
            response.put("mpeg-command", mpegCommand);
            response.put("code", 200);
            log.info("启动流成功: {}", response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "启动流失败: " + e.getMessage());
            res.put("code", 500);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("data", res);
            response.put("code", 500);
            response.put("message", "启动流失败: " + e.getMessage());
            response.put("success", false);

            log.error("启动流失败: {}", response);
            return ResponseEntity.status(500).body(response);
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
                log.error("媒体服务器启动失败: {}", e.getMessage(), e);
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


    private JSONObject getCameraInfo(Long cameraId) throws Exception {
        WmsDevice wmsDevice = wmsDeviceService.selectWmsDeviceById(cameraId);
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

    private JSONObject getCameraInfo(String cameraId) throws Exception {
        WmsDevice wmsDevice = new WmsDevice();
        wmsDevice.setDeviceName(cameraId);
        wmsDevice.setDeviceType("摄像头");
        List<WmsDevice> wmsDevices = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        if (wmsDevices.isEmpty()) {
            throw new Exception("设备不存在");
        }
        for (WmsDevice d : wmsDevices) {
            if (d.getDeviceName().equals(cameraId)) {
                wmsDevice = d;
                break;
            }
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
