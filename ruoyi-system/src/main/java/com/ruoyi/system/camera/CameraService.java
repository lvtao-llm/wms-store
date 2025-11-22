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

    public CameraService() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopAllProcesses));
    }

    private void stopAllProcesses() {
        log.info("开始停止所有摄像头流进程...");
        for (Map.Entry<String, FFmpegWrap> entry : activeWrap.entrySet()) {
            try {
                FFmpegWrap wrap = entry.getValue();
                wrap.stop();
                log.info("已停止摄像头流: {}", wrap.getId());
            } catch (Exception e) {
                log.error("停止摄像头流失败: {}", entry.getKey(), e);
            }
        }
        activeWrap.clear();
        log.info("所有摄像头流已停止");
    }
}
