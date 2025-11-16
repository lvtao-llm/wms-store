package com.ruoyi.system.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@Component
@ServerEndpoint("/api/camera/ws/{id}")
public class CameraWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(CameraWebSocketServer.class);


    private static CameraServeice cameraServeice;


    @Autowired
    public void setCameraStreamService(CameraServeice cameraServeice) {
        CameraWebSocketServer.cameraServeice = cameraServeice;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        LocalDateTime time = LocalDateTime.now();
                        Set<Map.Entry<String, FFmpegFrameGrabberWrap>> entries = cameraServeice.activeStreams1.entrySet();
                        List<String> key = new ArrayList<>();
                        for (Map.Entry<String, FFmpegFrameGrabberWrap> entry : entries) {
                            long duration = Duration.between(time, entry.getValue().getTime()).abs().getSeconds();
                            if (duration > 1000) {
                                log.info("关闭不活跃的连接:{}({})", entry.getKey(), entry.getValue().getId());
                                entry.getValue().stop();
                                key.add(entry.getKey());
                            }
                        }
                        for (String k : key) {
                            FFmpegFrameGrabberWrap remove = cameraServeice.activeStreams1.remove(k);
                            if (remove != null) {
                                for (Session s : remove.getSessions()) {
                                    try {
                                        s.close();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    remove.getSessions().remove(s);
                                }
                                remove.stop();
                            }
                        }
                        Thread.sleep(1000); // 使
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("打开连接:{}", id);
        Set<Map.Entry<String, FFmpegFrameGrabberWrap>> entries = cameraServeice.activeStreams1.entrySet();
        for (Map.Entry<String, FFmpegFrameGrabberWrap> entry : entries) {
            if (entry.getValue().getId().equals(id)) {
                entry.getValue().getSessions().add(session);
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        log.info("关闭连接:{}", id);
        Set<Map.Entry<String, FFmpegFrameGrabberWrap>> entries = cameraServeice.activeStreams1.entrySet();
        String key = null;
        for (Map.Entry<String, FFmpegFrameGrabberWrap> entry : entries) {
            if (entry.getValue().getId().equals(id)) {
                entry.getValue().getSessions().remove(session);
                if (entry.getValue().getSessions().isEmpty()) {
                    entry.getValue().stop();
                    key = entry.getKey();
                }
            }
        }
        if (key != null) {
            FFmpegFrameGrabberWrap remove = cameraServeice.activeStreams1.remove(key);
            for (Session s : remove.getSessions()) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                remove.getSessions().remove(s);
            }
            remove.stop();
        }
    }
}
