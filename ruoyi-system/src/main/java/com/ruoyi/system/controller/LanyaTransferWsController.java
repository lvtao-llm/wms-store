//package com.ruoyi.system.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import javax.websocket.ClientEndpoint;
//import javax.websocket.ContainerProvider;
//import javax.websocket.WebSocketContainer;
//import java.net.URI;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 蓝牙数据转发WebSocket控制器
// *
// * @author 吕涛
// * @version 1.0
// * @since 2025/10/27
// */
//@ServerEndpoint("/system/lanya-transfer/ws/{clientId}")
//public class LanyaTransferWsController {
//
//    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
//    private String baseUrl;
//
//    // 存储客户端会话和对应的目标WebSocket连接
//    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();
//    private static final Map<String, Session> targetSessions = new ConcurrentHashMap<>();
//
//    /**
//     * WebSocket连接建立
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("clientId") String clientId) {
//        try {
//            // 将客户端会话存储到Map中
//            clientSessions.put(clientId, session);
//
//            // 构建目标WebSocket地址
//            String targetUrl = "ws://" + baseUrl + "/websocket/ws/" + clientId;
//
//            // 创建WebSocket客户端并连接到目标服务器
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            Session targetSession = container.connectToServer(
//                    new TargetWebSocketHandler(session, clientId),
//                    new URI(targetUrl)
//            );
//
//            // 保存目标会话
//            targetSessions.put(clientId, targetSession);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 接收客户端消息
//     */
//    @OnMessage
//    public void onMessage(String message, @PathParam("clientId") String clientId) {
//        try {
//            // 转发消息到目标服务器
//            Session targetSession = targetSessions.get(clientId);
//            if (targetSession != null && targetSession.isOpen()) {
//                targetSession.getBasicRemote().sendText(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * WebSocket连接关闭
//     */
//    @OnClose
//    public void onClose(@PathParam("clientId") String clientId) {
//        // 清理资源
//        clientSessions.remove(clientId);
//
//        Session targetSession = targetSessions.get(clientId);
//        if (targetSession != null) {
//            try {
//                if (targetSession.isOpen()) {
//                    targetSession.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        targetSessions.remove(clientId);
//    }
//
//    /**
//     * 目标WebSocket服务器的消息处理器
//     */
//    private static class TargetWebSocketHandler {
//        private final Session clientSession;
//        private final String clientId;
//
//        public TargetWebSocketHandler(Session clientSession, String clientId) {
//            this.clientSession = clientSession;
//            this.clientId = clientId;
//        }
//
//        /**
//         * 处理来自目标服务器的消息
//         */
//        @OnMessage
//        public void onMessage(String message) {
//            try {
//                // 转发消息到客户端
//                if (clientSession.isOpen()) {
//                    clientSession.getBasicRemote().sendText(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        /**
//         * 处理目标服务器连接关闭
//         */
//        @OnClose
//        public void onClose() {
//            try {
//                // 关闭客户端连接
//                if (clientSession.isOpen()) {
//                    clientSession.close();
//                }
//
//                // 清理资源
//                targetSessions.remove(clientId);
//                clientSessions.remove(clientId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
