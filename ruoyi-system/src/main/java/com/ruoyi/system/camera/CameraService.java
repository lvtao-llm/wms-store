package com.ruoyi.system.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/13
 */
@Service
public class CameraService {
    private static final Logger log = LoggerFactory.getLogger("camera-stream");
    public final Map<String, FFmpegWrap> activeWrap = new ConcurrentHashMap<>();
}
