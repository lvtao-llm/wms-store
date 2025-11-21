package com.ruoyi.framework.websocket;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.ThirdPartyAuth;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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
    private static final Logger log = LoggerFactory.getLogger("realtime-data-podcast");

    /**
     * 第三方授权
     */
    @Autowired
    private ThirdPartyAuth thirdPartyAuth;
    private static boolean serverAlreadyStarted = false;

    /**
     * 设置目标服务器基础地址
     */
    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
    private void setBaseUrl(String baseUrl) {
        WebSocketServer.baseUrl = baseUrl;
    }

    @Value("${lanya.enabled-lanya-websocket-subscribe:false}")
    private boolean enabledLanyaWebsocketSubscribe;

    @Value("${lanya.forward-url}")
    private String forwardUrl;

    @Value("${lanya.forward-enable:false}")
    private boolean forwardEnable;

    /**
     * 定时同步Lanya设备卡发送日志开关
     */
    @Value("${lanya.position.mock.enabled:false}")
    private boolean enableMock;

    @Value("${lanya.save-person-location:false}")
    private boolean savePersonLocation;

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

    @Autowired
    private IWmsMaterialInService wmsMaterialInService;

    @Autowired
    private IWmsMaterialOutService wmsMaterialOutService;

    @Autowired
    private IWmsMaterialStockService wmsMaterialStockService;

    @Autowired
    private ILanyaCorePersonService lanyaCorePersonService;

    /**
     * 时间格式 年月日
     */
    SimpleDateFormat sdfYearMonDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Lanya定位数据数据服务
     */
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    private Date mockPersonDate = null;
    private Date mockVehiclDate = null;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        // 启动消息处理线程
        startMessageProcessor();

        if (!enabledLanyaWebsocketSubscribe) {
            log.info("Lanya WebSocket 订阅未启用");
            return;
        }

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


            thirdWebSocketClient.doHandshake(new ThirdWebSocketHandler(messageQueue, forwardEnable, forwardUrl, savePersonLocation, lanyaPositionHistoryService), thirdTargetUrl).get();

        } catch (Exception e) {
            log.error("Lanya WebSocket 订阅失败", e);
        }
        log.info("Lanya WebSocket 订阅成功");
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
                    log.info("消息处理线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("处理消息时发生异常", e);
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
                    synchronized (clientSession) {
                        clientSession.getBasicRemote().sendText(message);
                    }
                } catch (Exception e) {
                    log.error("发送消息给客户端时发生异常", e);
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
            log.error("连接异常:", e);
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
                    add(new WmsVehicleAlarmRule("未授权进入", 0, 1));
                    add(new WmsVehicleAlarmRule("未预约进入", 0, 1));
                    add(new WmsVehicleAlarmRule("轨迹异常", 0, 1));
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
//        messageQueue.add(materialLog.toString());
    }

    /**
     * 物料统计数据
     */
    @Scheduled(cron = "${wms.ws-data.material-jl:0/10 * * * * ?}")
    public void materialJlData() {
        // 使用异步发送替代阻塞发送
        WmsMaterialIn wmsMaterialIn = new WmsMaterialIn();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String ymd = sdfYearMonDay.format(calendar.getTime());
        wmsMaterialIn.setJlsj(calendar.getTime());
        List<WmsMaterialIn> wmsMaterialIns = wmsMaterialInService.selectWmsMaterialInList(new WmsMaterialIn());
        JSONObject materialLog = new JSONObject();
        JSONArray materialLogData = new JSONArray();
        materialLog.put("msgType", "materialJlLog");
        materialLog.put("data", materialLogData);
        materialLog.put("total", wmsMaterialIns.size());
        for (int i = 0; i < wmsMaterialIns.size(); i++) {
            WmsMaterialIn d = wmsMaterialIns.get(i);
            JSONObject materialLogDataItem = new JSONObject();
            materialLogDataItem.put("sort", i);
            materialLogDataItem.put("materialName", d.getWzmc());
            materialLogDataItem.put("materialCode", d.getWzbm());
            materialLogDataItem.put("materialType", d.getWzlb());
            materialLogDataItem.put("stockIn", d.getDhsl());
            materialLogDataItem.put("areaName", d.getAreaCodes());
            materialLogData.add(materialLogDataItem);
        }
        messageQueue.add(materialLog.toString());
    }

    /**
     * 物料统计数据
     */
    @Scheduled(cron = "${wms.ws-data.material-db:0/10 * * * * ?}")
    public void materialDbData() {
        // 使用异步发送替代阻塞发送
        WmsMaterialOut wmsMaterialOut = new WmsMaterialOut();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String ymd = sdfYearMonDay.format(calendar.getTime());
        wmsMaterialOut.setOutboundTime(calendar.getTime());
        List<WmsMaterialOut> wmsMaterialOuts = wmsMaterialOutService.selectWmsMaterialOutList(wmsMaterialOut);
        JSONObject materialLog = new JSONObject();
        JSONArray materialLogData = new JSONArray();
        materialLog.put("msgType", "materialDbLog");
        materialLog.put("data", materialLogData);
        materialLog.put("total", wmsMaterialOuts.size());
        for (int i = 0; i < wmsMaterialOuts.size(); i++) {
            WmsMaterialOut d = wmsMaterialOuts.get(i);
            JSONObject materialLogDataItem = new JSONObject();
            materialLogDataItem.put("sort", i);
            materialLogDataItem.put("materialName", d.getWzmc());
            materialLogDataItem.put("materialCode", d.getWzbm());
            materialLogDataItem.put("materialType", d.getWzlb());
            materialLogDataItem.put("stockOut", d.getActualQuantity());
            materialLogDataItem.put("areaName", d.getAreaCodes());
            materialLogData.add(materialLogDataItem);
        }
        messageQueue.add(materialLog.toString());
    }

    /**
     * 物料统计数据
     */
    @Scheduled(cron = "${wms.ws-data.material-kc:0/10 * * * * ?}")
    public void materialKcData() {
        // 使用异步发送替代阻塞发送
        WmsMaterialStock wmsMaterialStock = new WmsMaterialStock();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String ymd = sdfYearMonDay.format(calendar.getTime());
        wmsMaterialStock.setCreateTime(calendar.getTime());
        List<WmsMaterialStock> wmsMaterialStocks = wmsMaterialStockService.selectWmsMaterialStockList(wmsMaterialStock);
        JSONObject materialLog = new JSONObject();
        JSONArray materialLogData = new JSONArray();
        materialLog.put("msgType", "materialKcLog");
        materialLog.put("data", materialLogData);
        materialLog.put("total", wmsMaterialStocks.size());
        for (int i = 0; i < wmsMaterialStocks.size(); i++) {
            WmsMaterialStock d = wmsMaterialStocks.get(i);
            JSONObject materialLogDataItem = new JSONObject();
            materialLogDataItem.put("sort", i);
            materialLogDataItem.put("materialName", d.getWzmc());
            materialLogDataItem.put("materialCode", d.getWzbm());
            materialLogDataItem.put("materialType", d.getWzlb());
            materialLogDataItem.put("stock", d.getBookWeight());
            materialLogDataItem.put("areaNames", d.getAreaNames());
            materialLogDataItem.put("areaCodes", d.getAreaCodes());
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
            areaLogDataItem.put("personCount", a.getStuffCount());
            areaLogDataItem.put("vehicleCount", a.getVehicleCount());
            areaLogDataItem.put("areaName", a.getAreaName());
            areaLogData.add(areaLogDataItem);
            i++;
        }
        messageQueue.add(areaLog.toString());
    }

    /**
     * 创建LanyaPositionHistory数据模拟器
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void generateMockVehiclePositionHistoryData() throws ParseException {
        if (!enableMock) {
            return;
        }

        // 创建所需的JSONObject
        JSONObject locationData = new JSONObject();
        locationData.put("msgType", "currentVehicleLocation");
        locationData.put("total", 1);


        // 创建personTypeStatistics数组
        JSONArray personTypeStats = new JSONArray();

        // 添加contractor类型统计
        JSONObject contractorStat = new JSONObject();
        contractorStat.put("count", 0);
        contractorStat.put("personType", "contractor");
        contractorStat.put("personTypeName", "承包商");
        contractorStat.put("ratio", 0);
        personTypeStats.add(contractorStat);

        // 添加staff类型统计
        JSONObject staffStat = new JSONObject();
        staffStat.put("count", 0);
        staffStat.put("personType", "staff");
        staffStat.put("personTypeName", "员工");
        staffStat.put("ratio", 0);
        personTypeStats.add(staffStat);

        // 添加visitor类型统计
        JSONObject visitorStat = new JSONObject();
        visitorStat.put("count", 0);
        visitorStat.put("personType", "visitor");
        visitorStat.put("personTypeName", "访客");
        visitorStat.put("ratio", 0);
        personTypeStats.add(visitorStat);

        locationData.put("personTypeStatistics", personTypeStats);

        if (mockVehiclDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mockVehiclDate = sdf.parse("2025-11-11 10:00:00");
        }

        List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTable("position_history_20251111", 1988037106810236930L, mockPersonDate);
        for (LanyaPositionHistory history : trajectory) {
            // 创建空的data数组
            mockVehiclDate = history.getCreateTime();
            JSONArray dataArray = new JSONArray();
            dataArray.add(history);
            locationData.put("data", dataArray);
            messageQueue.add(locationData.toString());
        }
    }

    /**
     * 创建LanyaPositionHistory数据模拟器
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void generateMockPersonPositionHistoryData() throws ParseException {
        if (!enableMock) {
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        List<LanyaCorePerson> lanyaCorePeople = lanyaCorePersonService.selectLanyaCorePersonList(new LanyaCorePerson());
        Map<Long, WmsArea> wmsAreas = wmsAreaService.getAreaMap();
        int staffCount = 0;
        int visitorCount = 0;
        for (Map.Entry<Long, WmsArea> entry : wmsAreas.entrySet()) {
            WmsArea a = entry.getValue();
            staffCount += a.getStuffCount();
            visitorCount += a.getVisitorCount();
        }

        // 创建所需的JSONObject
        JSONObject locationData = new JSONObject();
        locationData.put("msgType", "currentPersonLocation");
        locationData.put("total", 1);


        // 创建personTypeStatistics数组
        JSONArray personTypeStats = new JSONArray();

        // 添加staff类型统计
        JSONObject staffStat = new JSONObject();
        staffStat.put("count", staffCount);
        staffStat.put("personType", "staff");
        staffStat.put("personTypeName", "内部员工");
        staffStat.put("ratio", df.format(staffCount / lanyaCorePeople.size()));
        personTypeStats.add(staffStat);

        // 添加visitor类型统计
        JSONObject visitorStat = new JSONObject();
        visitorStat.put("count", visitorCount);
        visitorStat.put("personType", "visitor");
        visitorStat.put("personTypeName", "临时访客");
        visitorStat.put("ratio", 0);
        personTypeStats.add(visitorStat);

        locationData.put("personTypeStatistics", personTypeStats);

        if (mockPersonDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mockPersonDate = sdf.parse("2025-11-11 09:30:00");
        }

        List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTable("position_history_20251111", 1988037106810236930L, mockPersonDate);
        for (LanyaPositionHistory history : trajectory) {
            // 创建空的data数组
            mockPersonDate = history.getCreateTime();
            JSONArray dataArray = new JSONArray();
            dataArray.add(history);
            locationData.put("data", dataArray);
            messageQueue.add(locationData.toString());
        }
    }

    public void setServerAlreadyStarted() {
        serverAlreadyStarted = false;
    }

    /**
     * 三方WebSocket服务器的消息处理器
     */
    public static class ThirdWebSocketHandler extends TextWebSocketHandler {

        private final BlockingQueue<String> messageQueue;
        private final boolean forwardEnable;
        private final String forwardUrl;
        private final boolean savePersonLocation;
        private final ILanyaPositionHistoryService lanyaPositionHistoryService;
        public RestTemplate restTemplate = new RestTemplate();
        private ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();


        public ThirdWebSocketHandler(BlockingQueue<String> messageQueue, boolean forwardEnable, String forwardUrl, boolean savePersonLocation, ILanyaPositionHistoryService lanyaPositionHistoryService) {
            this.messageQueue = messageQueue;
            this.forwardEnable = forwardEnable;
            this.forwardUrl = forwardUrl;
            this.savePersonLocation = savePersonLocation;
            this.lanyaPositionHistoryService = lanyaPositionHistoryService;
            this.headers.setContentType(MediaType.APPLICATION_JSON);
            this.headers.set("Authorization", "fei@#%joie@#&*joijo1234%^567AABB");
        }

        /**
         * 处理来自目标服务器的消息
         */
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            try {
                // 检查消息大小
                if (message.getPayloadLength() > 1024 * 1024) { // 1MB限制
                    log.warn("接收 Lanya WebSocket 消息过大，大小: {} 字节", message.getPayloadLength());
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
                if (jsonObject.get("msgType").equals("currentPersonLocation")) {
                    return;
                }
                messageQueue.add(payload);

                if (forwardEnable && serverAlreadyStarted) {
                    String sendBody = mapper.writeValueAsString(new HashMap<String, String>() {{
                        put("message", payload);
                    }});
                    HttpEntity<String> entity = new HttpEntity<>(sendBody, this.headers);
                    ResponseEntity<String> resp = restTemplate.exchange(this.forwardUrl, HttpMethod.POST, entity, String.class);
                    HashMap respMap = this.mapper.readValue(resp.getBody(), HashMap.class);
                }

                if (savePersonLocation && serverAlreadyStarted) {
                    LanyaPositionHistory history = new LanyaPositionHistory();

                    lanyaPositionHistoryService.insertLanyaPositionHistory(history);
                }
            } catch (Exception e) {
                log.error("处理 Lanya WebSocket 消息异常", e);
            }
        }

        /**
         * 处理目标服务器连接建立
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            log.info("与 Lanya WebSocket 连接建立成功");
        }

        /**
         * 处理目标服务器连接关闭
         */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            log.info("与 Lanya WebSocket 连接关闭,状态: {}", status);
        }

        /**
         * 处理传输错误
         */
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            log.error("与 Lanya WebSocket 传输错误:", exception);
        }
    }
}
