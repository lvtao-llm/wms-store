package com.ruoyi.system.camera;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private static final Logger log = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    private IWmsDeviceService wmsDeviceService;

    @Autowired
    private CameraServeice cameraServeice;

    /**
     * 启动摄像头流
     */
    @GetMapping("/stream/start/{cameraId}/{channel}")
    public ResponseEntity<Map<String, Object>> startStream(@PathVariable String cameraId, @PathVariable String channel) {
        try {
            JSONObject data = getCameraInfo(cameraId);
            String id = UUID.randomUUID().toString().replace("-", "");
            String rtspUrl = null;
            String mpegCommand = null;
            if (!cameraServeice.activeStreams1.containsKey(cameraId + "-" + channel)) {
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
                String localSocketAddr = "tcp://127.0.0.1:9999"; // 可根据需要调整端口号
                rtspUrl = "rtsp://0.0.0.0:8554/live.sdp";
                mpegCommand = String.format("ffmpeg -rtsp_transport tcp -max_delay 500000 -use_wallclock_as_timestamps 1 -i " + rtspUrl + " -vf fps=2 -c:v libx264 -f flv ");
//                mpegCommand = String.format("ffmpeg -rtsp_transport tcp -i %s -c:v copy -an -f flv ", rtspUrl);
                if (rtspUrl == null) {
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", false);
                    res.put("message", "channel只能是1或2");
                    res.put("rtspUrl", rtspUrl);
                    res.put("mpeg-command", mpegCommand);
                    log.info("启动流失败: {}", res);
                    return ResponseEntity.status(500).body(res);
                }
                FFmpegFrameGrabberWrap fFmpegFrameGrabberWrap = new FFmpegFrameGrabberWrap(id, rtspUrl, mpegCommand);

                cameraServeice.activeStreams1.put(cameraId + "-" + channel, fFmpegFrameGrabberWrap);
            } else {
                id = cameraServeice.activeStreams1.get(cameraId + "-" + channel).getId();
                rtspUrl = cameraServeice.activeStreams1.get(cameraId + "-" + channel).getRtspUrl();
                mpegCommand = cameraServeice.activeStreams1.get(cameraId + "-" + channel).getMpegCommand();
            }

            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("id", id);
            res.put("message", "流启动成功");
            res.put("rtspUrl", rtspUrl);
            res.put("mpeg-command", mpegCommand);
            log.info("启动流成功: {}", res);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "启动流失败: " + e.getMessage());
            return ResponseEntity.status(500).body(res);
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
