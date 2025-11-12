package com.ruoyi.system.camera;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@RestController
@RequestMapping("/api/camera")
public class CameraController {

    @Autowired
    private CameraStreamService cameraStreamService;

    @Autowired
    private IWmsDeviceService wmsDeviceService;

    /**
     * 启动摄像头流
     */
    @GetMapping("/stream/start/{cameraId}")
    public ResponseEntity<Map<String, Object>> startStream(@PathVariable String cameraId) {
        try {
            JSONObject data = getCameraInfo(cameraId);

            String rtspUrl1 = String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/%s",
                    data.getString("username1"),
                    data.getString("password1"),
                    data.getString("ip1"),
                    data.getString("port1"),
                    data.getString("channel1")
            );

            String rtspUrl2 = String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/%s",
                    data.getString("username2"),
                    data.getString("password2"),
                    data.getString("ip2"),
                    data.getString("port2"),
                    data.getString("channel2")
            );

            // 启动FFmpeg流抓取器
            FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber(rtspUrl1);
            FFmpegFrameGrabber grabber2 = new FFmpegFrameGrabber(rtspUrl2);
            grabber1.setOption("stimeout", "5000000"); // 5秒超时
            grabber2.setOption("stimeout", "5000000"); // 5秒超时
            try {
                grabber1.start();
                grabber2.start();
                cameraStreamService.activeStreams1.put(cameraId, grabber1);
                cameraStreamService.activeStreams2.put(cameraId, grabber2);
            } catch (Exception e) {
                throw new RuntimeException("启动摄像头流失败", e);
            }

            // HTTP 流地址
            String httpStreamUrl = "/api/camera/stream/" + cameraId;
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("streamUrl", httpStreamUrl);
            result.put("message", "流启动成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "启动流失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取摄像头实时画面（JPEG 格式）
     */
    @GetMapping(value = "/stream/frame/{cameraId}/{channel}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCameraFrame(@PathVariable String cameraId, @PathVariable String channel) {
        try {
            byte[] frameData = cameraStreamService.getCameraFrame(cameraId, channel);
            if (frameData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(frameData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
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
