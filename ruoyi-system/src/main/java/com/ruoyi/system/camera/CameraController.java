package com.ruoyi.system.camera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
* 
*
* @author 吕涛
* @version 1.0
* @since 2025/11/11
*/
@RestController
@RequestMapping("/api/camera")
public class CameraController {

    @Autowired
    private CameraStreamService cameraStreamService;

    /**
     * 启动摄像头流
     */
    @PostMapping("/stream/start/{cameraId}")
    public ResponseEntity<Map<String, Object>> startStream(
            @PathVariable String cameraId,
            @RequestBody CameraInfo cameraInfo) {

        try {
            String streamUrl = cameraStreamService.startCameraStream(cameraId, cameraInfo);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("streamUrl", streamUrl);
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
    @GetMapping(value = "/stream/frame/{cameraId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCameraFrame(@PathVariable String cameraId) {
        try {
            byte[] frameData = cameraStreamService.getCameraFrame(cameraId);
            if (frameData != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(frameData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
