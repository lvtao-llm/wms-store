package com.ruoyi.framework.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.ThirdPartyAuth;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.domain.WmsMaterialStaticsDay;
import com.ruoyi.system.service.IWmsAlarmRuleService;
import com.ruoyi.system.service.IWmsAreaService;
import com.ruoyi.system.service.IWmsDeviceService;
import com.ruoyi.system.service.IWmsMaterialStaticsDayService;
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

    /**
     * 第三方授权
     */
    @Autowired
    private ThirdPartyAuth thirdPartyAuth;

    /**
     * 设置目标服务器基础地址
     */
    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
    private void setBaseUrl(String baseUrl) {
        WebSocketServer.baseUrl = baseUrl;
    }

    /**
     * 目标服务器基础地址
     */
    private static String baseUrl;

    /**
     * 存储浏览器会话队列
     */
    // 存储客户端会话和对应的目标WebSocket连接
    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();

    /**
     * 目标服务器会话
     */
    private WebSocketClient thirdWebSocketClient;

    /**
     * 报警规则服务
     */
    @Autowired
    private IWmsAlarmRuleService wmsAlarmRuleService;

    /**
     * 设备服务
     */
    @Autowired
    private static IWmsDeviceService wmsDeviceService;

    /**
     * 存储消息队列
     */
    public BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(10000);

    /**
     * 物料日统计服务
     */
    @Autowired
    private IWmsMaterialStaticsDayService wmsMaterialStaticsDayService;

    /**
     * 区域服务
     */
    @Autowired
    private IWmsAreaService wmsAreaService;

    /**
     * 时间格式 年月日
     */
    SimpleDateFormat sdfYearMonDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        try {
            // 三方接口
            thirdPartyAuth = SpringUtils.getBean(ThirdPartyAuth.class);
            // 三方WebSocket客户端
            thirdWebSocketClient = SpringUtils.getBean(WebSocketClient.class);
            // 三方用户ID
            String thirdUserId = thirdPartyAuth.getUserInfo().get("userId");
            // 三方socket id
            String thirdId = "XR_" + System.currentTimeMillis() + "_" + thirdUserId;
            // 三方目标WebSocket地址
            String thirdTargetUrl = "ws://" + baseUrl + "/websocket/ws/" + thirdId;
            // 连接三方目标WebSocket
            thirdWebSocketClient.doHandshake(new ThirdWebSocketHandler(messageQueue), thirdTargetUrl).get();

            // 启动消息处理线程
            startMessageProcessor();
        } catch (Exception e) {
            LOGGER.error("初始化失败", e);
        }

        LOGGER.info("\n WebSocketServer 初始化成功");


    }

    /**
     * 启动消息处理线程
     */
    private void startMessageProcessor() {
        // 创建一个线程
        Thread messageProcessorThread = new Thread(() -> {
            while (true) {
                try {
                    // 从队列中取出消息
                    String message = messageQueue.take(); // 阻塞式获取
                    // 发送给所有连接的客户端
                    broadcastMessage(message);
                } catch (InterruptedException e) {
                    LOGGER.info("消息处理线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    LOGGER.error("处理消息时发生异常", e);
                }
            }
        });
        messageProcessorThread.setDaemon(true);
        messageProcessorThread.setName("WebSocket-Message-Processor");
        messageProcessorThread.start();
    }

    /**
     * 广播消息给所有客户端
     *
     * @param message 消息
     */
    private void broadcastMessage(String message) {
        for (Session clientSession : clientSessions.values()) {
            if (clientSession.isOpen()) {
                try {
                    clientSession.getAsyncRemote().sendText(message, result -> {
                        if (!result.isOK()) {
                            LOGGER.error("给客户端异步发送消息失败: {}", result.getException().getMessage());
                        }
                    });
                } catch (Exception e) {
                    LOGGER.error("发送消息给客户端时发生异常", e);
                }
            }
        }
    }

    /**
     * 客户端连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("requestId") String requestId) throws Exception {
        try {
            // 将客户端会话存储到Map中
            if (clientSessions.containsKey(requestId)) {
                Session sess = clientSessions.remove(requestId);
                if (sess.isOpen()) {
                    sess.close();
                }
            }
            clientSessions.put(requestId, session);

            // 首次连接时向客户端发送消息

            // 设备服务
            IWmsDeviceService wmsDeviceService = SpringUtils.getBean(IWmsDeviceService.class);

            // 摄像头设备
            WmsDevice wmsDevice = new WmsDevice();
            wmsDevice.setDeviceType("摄像头");
            List<WmsDevice> wmsDevicesCamera = wmsDeviceService.selectWmsDeviceList(wmsDevice);

            // 传感器设备
            wmsDevice.setDeviceType("传感器");
            List<WmsDevice> wmsDevicesSensor = wmsDeviceService.selectWmsDeviceList(wmsDevice);
            wmsDevicesSensor.addAll(wmsDevicesCamera);

            // 向客户端发送的消息体
            Map<String, Object> personAlarm = new HashMap<String, Object>() {
                {
                    put("msgType", "摄像头与传感器");
                    put("rules", wmsDevicesSensor);
                }
            };

            // 转JSON字符串
            String json = new JSONObject(personAlarm).toJSONString();

            // 向当前连接发送初始数据
            session.getAsyncRemote().sendText(json);

        } catch (Exception e) {
            LOGGER.error("连接异常:", e);
        }
    }

    /**
     * 客户端连接关闭时处理
     */
    @OnClose
    public void onClose(Session session, @PathParam("requestId") String requestId) throws IOException {
        // 清理资源
        if (clientSessions.containsKey(requestId)) {
            Session sess = clientSessions.remove(requestId);
            if (sess.isOpen()) {
                sess.close();
            }
        }
    }

    /**
     * 客户端抛出异常时处理
     */
    @OnError
    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {
        // 清理资源
        if (clientSessions.containsKey(requestId)) {
            Session sess = clientSessions.remove(requestId);
            if (sess.isOpen()) {
                sess.close();
            }
        }
    }

    /**
     * 接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {
//        try {
//            // 转发消息到目标服务器
//            System.out.println("接收到[" + session.getId() + "]消息：" + message);
//            try {
//                JSONObject messageObject = JSONObject.parseObject(message);
//                Long timestamp = messageObject.getLong("timestamp");
//                if ("save".equals(messageObject.getString("method"))) {
//
//                    String type = messageObject.getString("type");
//                    switch (type) {
//                        case "摄像头": {
//                            IWmsDeviceService wmsDeviceService = SpringUtils.getBean(IWmsDeviceService.class);
//                            JSONObject jsonDevice = messageObject.getJSONObject("object");
//                            WmsDevice wmsDevice = jsonDevice.toJavaObject(WmsDevice.class);
//                            wmsDevice.setDeviceType("摄像头");
//                            if (wmsDeviceService.insertWmsDevice(wmsDevice) > 0) {
//                                session.getAsyncRemote().sendText("{code:200, msg:'保存成功', timestamp: " + timestamp + "}");
//                            } else {
//                                session.getAsyncRemote().sendText("{code:500, msg:'保存失败', timestamp: " + timestamp + "}");
//                            }
//                            break;
//                        }
//                        case "风险区域": {
//                            IWmsAreaService wmsAreaService = SpringUtils.getBean(IWmsAreaService.class);
//                            JSONObject jsonDevice = messageObject.getJSONObject("object");
//                            WmsArea wmsArea = jsonDevice.toJavaObject(WmsArea.class);
//                            if (wmsAreaService.insertWmsArea(wmsArea) > 0) {
//                                session.getAsyncRemote().sendText("{code:200, msg:'保存成功', timestamp: " + timestamp + "}");
//                            } else {
//                                session.getAsyncRemote().sendText("{code:500, msg:'保存失败', timestamp: " + timestamp + "}");
//                            }
//                            break;
//                        }
//                        case "传感器": {
//                            IWmsDeviceService wmsDeviceService = SpringUtils.getBean(IWmsDeviceService.class);
//                            JSONObject jsonDevice = messageObject.getJSONObject("object");
//                            WmsDevice wmsDevice = jsonDevice.toJavaObject(WmsDevice.class);
//                            wmsDevice.setDeviceType("传感器");
//                            if (wmsDeviceService.insertWmsDevice(wmsDevice) > 0) {
//                                session.getAsyncRemote().sendText("{code:200, msg:'保存成功', timestamp: " + timestamp + "}");
//                            } else {
//                                session.getAsyncRemote().sendText("{code:500, msg:'保存失败', timestamp: " + timestamp + "}");
//                            }
//                        }
//                    }
//                }
//                if ("delete".equals(messageObject.getString("method"))) {
//
//                    String type = messageObject.getString("type");
//                    switch (type) {
//                        case "风险区域": {
//                            IWmsAreaService wmsAreaService = SpringUtils.getBean(IWmsAreaService.class);
//                            JSONObject jsonDevice = messageObject.getJSONObject("object");
//                            WmsArea wmsArea = jsonDevice.toJavaObject(WmsArea.class);
//                            if (wmsAreaService.deleteWmsAreaByAreaId(wmsArea.getAreaId()) > 0) {
//                                session.getAsyncRemote().sendText("{code:200, msg:'保存成功', timestamp: " + timestamp + "}");
//                            } else {
//                                session.getAsyncRemote().sendText("{code:500, msg:'保存失败', timestamp: " + timestamp + "}");
//                            }
//                            break;
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                session.getAsyncRemote().sendText("{code:500, msg:'保存失败', exception:'" + e.getMessage() + "'}");
//                LOGGER.error("保存失败: " + e.getMessage());
//            }
//        } catch (Exception e) {
//            LOGGER.error("保存失败: " + e.getMessage());
//        }
    }

    /**
     * 车辆统计数据
     */
    @Scheduled(cron = "${wms.ws-data.vehicle-alarm:0/10 * * * * ?}")
    public void vehicleAlarmData() {
        Map<String, Object> vehicleAlarm = new HashMap<String, Object>() {
            {
                put("msgType", "vehicleAlarm");
                put("rules", new ArrayList<WmsVehicleAlarmRule>() {{
                    add(new WmsVehicleAlarmRule("未授权进入", 22.6, 10));
                    add(new WmsVehicleAlarmRule("未预约进入", 22.6, 10));
                    add(new WmsVehicleAlarmRule("轨迹异常", 22.6, 10));
                }});
            }
        };
        String json = new JSONObject(vehicleAlarm).toJSONString();
        messageQueue.add(json);
    }

    /**
     * 人员报警数据
     */
    @Scheduled(cron = "${wms.ws-data.person-alarm:0/10 * * * * ?}")
    public void personAlarmData() {
        List<WmsAlarmRule> wmsAlarmRules = wmsAlarmRuleService.selectWmsAlarmRuleList(new WmsAlarmRule());
        for (WmsAlarmRule wmsAlarmRule : wmsAlarmRules) {
            wmsAlarmRule.setPercentage(33.1);
            wmsAlarmRule.setCount(10);
        }
        Map<String, Object> personAlarm = new HashMap<String, Object>() {
            {
                put("msgType", "personAlarm");
                put("rules", wmsAlarmRules);
            }
        };
        String json = new JSONObject(personAlarm).toJSONString();
        messageQueue.add(json);
    }

    /**
     * 物料统计数据
     */
    @Scheduled(cron = "${wms.ws-data.material-statics:0/10 * * * * ?}")
    public void materialStaticsData() {
        // 使用异步发送替代阻塞发送
        WmsMaterialStaticsDay wmsMaterialStaticsDay = new WmsMaterialStaticsDay();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String ymd = sdfYearMonDay.format(calendar.getTime());
        wmsMaterialStaticsDay.setDay(ymd);
        List<WmsMaterialStaticsDay> wmsMaterialStaticsDays = wmsMaterialStaticsDayService.selectWmsMaterialStaticsDayList(wmsMaterialStaticsDay);
        JSONObject materialLog = new JSONObject();
        JSONArray materialLogData = new JSONArray();
        materialLog.put("msgType", "materialLog");
        materialLog.put("data", materialLogData);
        materialLog.put("total", wmsMaterialStaticsDays.size());
        for (int i = 0; i < wmsMaterialStaticsDays.size(); i++) {
            WmsMaterialStaticsDay d = wmsMaterialStaticsDays.get(i);
            JSONObject materialLogDataItem = new JSONObject();
            materialLogDataItem.put("sort", i);
            materialLogDataItem.put("materialName", d.getWzmc());
            materialLogDataItem.put("materialCode", d.getWzbm());
            materialLogDataItem.put("materialType", d.getWzlb());
            materialLogDataItem.put("stockIn", d.getJl());
            materialLogDataItem.put("stockOut", d.getDb());
            materialLogDataItem.put("stock", d.getKc());
            materialLogDataItem.put("areaName", d.getAreaCodes());
            materialLogData.add(materialLogDataItem);
        }
        messageQueue.add(materialLog.toString());
    }

    /**
     * 区域统计数据
     */
    @Scheduled(cron = "${wms.ws-data.area-statics:0/10 * * * * ?}")
    public void areaStaticsData() {
        Map<Long, WmsArea> wmsAreas = wmsAreaService.getAreaMap();
        // 使用异步发送替代阻塞发送
        JSONObject areaLog = new JSONObject();
        JSONArray areaLogData = new JSONArray();
        areaLog.put("msgType", "areaLog");
        areaLog.put("total", wmsAreas.size());
        areaLog.put("data", areaLogData);
        int i = 0;
        for (Map.Entry<Long, WmsArea> entry : wmsAreas.entrySet()) {
            WmsArea a = entry.getValue();
            JSONObject areaLogDataItem = new JSONObject();
            areaLogDataItem.put("sort", i);
            areaLogDataItem.put("areaCode", a.getAreaCode());
            areaLogDataItem.put("areaType", a.getAreaType());
            areaLogDataItem.put("personCount", a.getPersonCount());
            areaLogDataItem.put("vehicleCount", a.getVehicleCount());
            areaLogDataItem.put("areaName", a.getAreaName());
            areaLogData.add(areaLogDataItem);
            i++;
        }
        messageQueue.add(areaLog.toString());
    }

    /**
     * 三方WebSocket服务器的消息处理器
     */
    public static class ThirdWebSocketHandler extends TextWebSocketHandler {

        private final BlockingQueue<String> messageQueue;

        public ThirdWebSocketHandler(BlockingQueue<String> messageQueue) {
            this.messageQueue = messageQueue;
        }

        /**
         * 处理来自目标服务器的消息
         */
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            try {
                // 检查消息大小
                if (message.getPayloadLength() > 1024 * 1024) { // 1MB限制
                    LOGGER.warn("接收三方消息过大，大小: {} 字节", message.getPayloadLength());
                    // 可以选择丢弃或分批处理
                }
                String payload = message.getPayload();
                JSONObject jsonObject = JSONObject.parseObject(payload);
                if (jsonObject.get("msgType").equals("cardSenderView")) {
                    return;
                }
                if (jsonObject.get("msgType").equals("alarmCount")) {
                    return;
                }
                if (jsonObject.get("msgType").equals("vehicleAlarmCount")) {
                    return;
                }
                if (jsonObject.get("msgType").equals("currentVehicleLocation")) {
                    return;
                }
                if (jsonObject.get("msgType").equals("personCount")) {
                    return;
                }
                messageQueue.add(payload);
            } catch (Exception e) {
                LOGGER.error("处理三方服务器消息异常", e);
            }
        }

        /**
         * 处理目标服务器连接建立
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            LOGGER.error("与三方目标服务器连接建立成功");
        }

        /**
         * 处理目标服务器连接关闭
         */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            LOGGER.error("与三方目标服务器连接关闭,状态: {}", status);
        }

        /**
         * 处理传输错误
         */
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            LOGGER.error("与三方目标服务器传输错误:", exception);
        }
    }
}
