package com.ruoyi.system.camera;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/13
 */
@Service
public class CameraServeice {
    public final Map<String, FFmpegFrameGrabberWrap> activeStreams1 = new ConcurrentHashMap<>();
}
