package com.ruoyi.framework.websocket;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * websocket 消息处理
 *
 * @author ruoyi
 */
@Component
@ServerEndpoint(value = "/system/lanya-transfer/ws/{requestId}", configurator = ServerEndpointConfig.Configurator.class)
public class WebSocketServer {
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
    private void setBaseUrl(String baseUrl) {
        WebSocketServer.baseUrl = baseUrl;
    }

    private static String baseUrl;

    // 存储客户端会话和对应的目标WebSocket连接
    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();
    private static final Map<String, WebSocketSession> targetSessions = new ConcurrentHashMap<>();
    // Spring WebSocket客户端
    private static WebSocketClient webSocketClient;

    @Autowired
    public void setWebSocketClient(WebSocketClient webSocketClient) {
        WebSocketServer.webSocketClient = webSocketClient;
    }

    public WebSocketServer() {
        LOGGER.info("\n WebSocketServer 初始化成功");
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("requestId") String requestId) throws Exception {
        try {
            // 将客户端会话存储到Map中
            clientSessions.put(requestId, session);

            // 构建目标WebSocket地址
            String targetUrl = "ws://" + baseUrl + "/websocket/ws/" + requestId;

            // 使用Spring WebSocket客户端连接到目标服务器
            WebSocketServer.TargetWebSocketHandler targetHandler = new WebSocketServer.TargetWebSocketHandler(session, requestId);
            WebSocketSession targetSession = webSocketClient.doHandshake(targetHandler, targetUrl).get();

            // 保存目标会话
            targetSessions.put(requestId, targetSession);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(Session session, @PathParam("requestId") String requestId) {
        // 清理资源
        clientSessions.remove(requestId);

        WebSocketSession targetSession = targetSessions.get(requestId);
        if (targetSession != null) {
            try {
                if (targetSession.isOpen()) {
                    targetSession.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        targetSessions.remove(requestId);
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {
        // 清理资源
        clientSessions.remove(requestId);

        WebSocketSession targetSession = targetSessions.get(requestId);
        if (targetSession != null) {
            try {
                if (targetSession.isOpen()) {
                    targetSession.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        targetSessions.remove(requestId);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {
        try {
            // 转发消息到目标服务器
            WebSocketSession targetSession = targetSessions.get(requestId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage( message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 目标WebSocket服务器的消息处理器
     */
    public static class TargetWebSocketHandler extends TextWebSocketHandler {
        private final Session clientSession;
        private final String clientId;

        public TargetWebSocketHandler(Session clientSession, String clientId) {
            this.clientSession = clientSession;
            this.clientId = clientId;
        }

        /**
         * 处理来自目标服务器的消息
         */
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            try {
                // 检查消息大小
                if (message.getPayloadLength() > 1024 * 1024) { // 1MB限制
                    LOGGER.warn("接收到过大的消息，大小: {} 字节", message.getPayloadLength());
                    // 可以选择丢弃或分批处理
                }
                if (clientSession.isOpen()) {
                    // 使用异步发送替代阻塞发送
                    clientSession.getAsyncRemote().sendText(message.getPayload(), result -> {
                        if (!result.isOK()) {
                            LOGGER.error("异步发送消息失败: " + result.getException().getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                LOGGER.error("处理目标服务器消息异常", e);
                e.printStackTrace();
            }
        }

        /**
         * 处理目标服务器连接建立
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            LOGGER.info("与目标服务器连接建立成功: {}", clientId);
        }

        /**
         * 处理目标服务器连接关闭
         */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            LOGGER.info("与目标服务器连接关闭: {}, 状态: {}", clientId, status);
            try {
                // 清理资源
                targetSessions.remove(clientId);
            } catch (Exception e) {
                LOGGER.error("处理目标服务器连接关闭异常", e);
                e.printStackTrace();
            }
        }

        /**
         * 处理传输错误
         */
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            LOGGER.error("与目标服务器传输错误: " + clientId, exception);
        }
    }
}
