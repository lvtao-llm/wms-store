package com.ruoyi.system.camera;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
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


    public final Map<String, FFmpegFrameGrabber> activeStreams1 = new ConcurrentHashMap<>();
    public final Map<String, FFmpegFrameGrabber> activeStreams2 = new ConcurrentHashMap<>();

    /**
     * 获取摄像头帧数据
     */
    public byte[] getCameraFrame(String cameraInfo, String channel) throws Exception {

        FFmpegFrameGrabber grabber = null;
        if (channel.equals("1")) {
            grabber = activeStreams1.get(cameraInfo);
        }
        if (channel.equals("2")) {
            grabber = activeStreams2.get(cameraInfo);
        }
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
