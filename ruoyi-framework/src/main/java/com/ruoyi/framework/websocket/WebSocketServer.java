package com.ruoyi.framework.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.ThirdPartyAuth;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.lanya.data.LanyaDataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
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

    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
    private void setBaseUrl(String baseUrl) {
        WebSocketServer.baseUrl = baseUrl;
    }

    // 目标服务器地址
    private static String baseUrl;

    // 存储客户端会话和对应的目标WebSocket连接
    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();

    // 目标WebSocket会话
    private WebSocketSession targetSession = null;

    // Spring WebSocket客户端
    private WebSocketClient webSocketClient;

    @PostConstruct
    public void init() {
        try {
            thirdPartyAuth = SpringUtils.getBean(ThirdPartyAuth.class);
            webSocketClient = SpringUtils.getBean(WebSocketClient.class);
            String userId = thirdPartyAuth.getUserInfo().get("userId");
            String id = "XR_" + System.currentTimeMillis() + "_" + userId;
            // 构建目标WebSocket地址
            String targetUrl = "ws://" + baseUrl + "/websocket/ws/" + id;

            // 使用Spring WebSocket客户端连接到目标服务器
            WebSocketServer.TargetWebSocketHandler targetHandler = new WebSocketServer.TargetWebSocketHandler();
            targetSession = webSocketClient.doHandshake(targetHandler, targetUrl).get();

        } catch (Exception e) {
            LOGGER.error("初始化失败", e);
        }

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
        } catch (Exception e) {
            LOGGER.error("连接异常:", e);
        }
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(Session session, @PathParam("requestId") String requestId) {
        // 清理资源
        clientSessions.remove(requestId);
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {
        // 清理资源
        clientSessions.remove(requestId);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {
//        try {
//            // 转发消息到目标服务器
//            if (targetSessions != null && targetSessions.isOpen()) {
//                targetSessions.sendMessage(new TextMessage(message));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void mockData() {
        for (Session clientSession : clientSessions.values()) {
            if (clientSession.isOpen()) {
                // 使用异步发送替代阻塞发送
                JSONObject materialLog = new JSONObject();
                JSONArray materialLogData = new JSONArray();
                materialLog.put("msgType", "materialLog");
                materialLog.put("data", materialLogData);
                materialLog.put("total", 10);
                for(int i = 0; i < 10; i++) {
                    JSONObject materialLogDataItem = new JSONObject();
                    materialLogDataItem.put("sort", i);
                    materialLogDataItem.put("materialName", "MATERIAL_" + i);
                    materialLogDataItem.put("materialCode", "MATERIAL_CODE_" + i);
                    materialLogDataItem.put("materialType", "MATERIAL_TYPE_" + i);
                    materialLogDataItem.put("stockIn", i);
                    materialLogDataItem.put("stockOut", 0);
                    materialLogDataItem.put("stock", 100);
                    materialLogDataItem.put("areaName", "钢铁区");
                    materialLogData.add(materialLogDataItem);
                }

                clientSession.getAsyncRemote().sendText(materialLog.toString(), result -> {
                    if (!result.isOK()) {
                        LOGGER.error("异步发送消息失败: {}", result.getException().getMessage());
                    }
                });


                // 使用异步发送替代阻塞发送
                JSONObject areaLog = new JSONObject();
                JSONArray areaLogData = new JSONArray();
                areaLog.put("msgType", "areaLog");
                areaLog.put("total", 10);
                areaLog.put("data", areaLogData);
                for(int i = 0; i < 10; i++) {
                    JSONObject areaLogDataItem = new JSONObject();
                    areaLogDataItem.put("sort", i);
                    areaLogDataItem.put("areaCode", "area_CODE_" + i);
                    areaLogDataItem.put("areaType", "area_TYPE_" + i);
                    areaLogDataItem.put("personCount", i);
                    areaLogDataItem.put("vehicleCount", 0);
                    areaLogDataItem.put("areaName", "钢铁区");
                    areaLogData.add(areaLogDataItem);
                }

                clientSession.getAsyncRemote().sendText(areaLog.toString(), result -> {
                    if (!result.isOK()) {
                        LOGGER.error("异步发送消息失败: {}", result.getException().getMessage());
                    }
                });
            }
        }
    }

    /**
     * 目标WebSocket服务器的消息处理器
     */
    public static class TargetWebSocketHandler extends TextWebSocketHandler {

        public TargetWebSocketHandler() {
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
                for (Session clientSession : clientSessions.values()) {
                    if (clientSession.isOpen()) {
                        // 使用异步发送替代阻塞发送
                        clientSession.getAsyncRemote().sendText(message.getPayload(), result -> {
                            if (!result.isOK()) {
                                LOGGER.error("异步发送消息失败: {}", result.getException().getMessage());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.error("处理目标服务器消息异常", e);
            }
        }

        /**
         * 处理目标服务器连接建立
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            LOGGER.info("与目标服务器连接建立成功");
        }

        /**
         * 处理目标服务器连接关闭
         */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            LOGGER.info("与目标服务器连接关闭,状态: {}", status);
        }

        /**
         * 处理传输错误
         */
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            LOGGER.error("与目标服务器传输错误:", exception);
        }
    }
}
