package com.ruoyi.system.camera;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@Service
public class CameraStreamService {

    private final Map<String, FFmpegFrameGrabber> activeStreams = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    /**
     * 启动摄像头流转换为 HTTP 流
     */
    public String startCameraStream(String cameraId, CameraInfo cameraInfo) {
        String rtspUrl = String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/%s",
                cameraInfo.getUsername(),
                cameraInfo.getPassword(),
                cameraInfo.getIp(),
                cameraInfo.getPort(),
                cameraInfo.getChannel());

        // HTTP 流地址
        String httpStreamUrl = "/api/camera/stream/" + cameraId;
        return httpStreamUrl;
    }

    /**
     * 获取摄像头帧数据
     */
    public byte[] getCameraFrame(String cameraId) throws Exception {
        FFmpegFrameGrabber grabber = activeStreams.get(cameraId);
        if (grabber == null) {
            throw new RuntimeException("摄像头流未启动");
        }

        Frame frame = grabber.grabImage();
        if (frame == null) return null;

        // 转换为 JPEG 格式
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage image = converter.convert(frame);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}
