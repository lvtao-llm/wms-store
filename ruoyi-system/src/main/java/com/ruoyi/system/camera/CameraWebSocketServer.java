package com.ruoyi.system.camera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/11
 */
@Component
@ServerEndpoint("/api/camera/ws/{cameraId}/{channel}")
public class CameraWebSocketServer {

    private static CameraStreamService cameraStreamService;

    @Autowired
    public void setCameraStreamService(CameraStreamService service) {
        CameraWebSocketServer.cameraStreamService = service;
    }

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("cameraId") String cameraId, @PathParam("channel") String channel) {
        sessions.put(cameraId, session);
        startStreaming(cameraId, channel);
    }

    @OnClose
    public void onClose(@PathParam("cameraId") String cameraId) {
        sessions.remove(cameraId);
    }

    private void startStreaming(String cameraId, String channel) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                Session session = sessions.get(cameraId);
                if (session != null && session.isOpen()) {
                    byte[] frame = cameraStreamService.getCameraFrame(cameraId, channel);
                    if (frame != null) {
                        session.getBasicRemote().sendBinary(ByteBuffer.wrap(frame));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // 10 FPS
    }
}
