//package com.ruoyi.framework.websocket;
//
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//import javax.annotation.PostConstruct;
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import javax.websocket.server.ServerEndpointConfig;
//
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ruoyi.common.utils.ThirdPartyAuth;
//import com.ruoyi.common.utils.spring.SpringUtils;
//import com.ruoyi.system.domain.*;
//import com.ruoyi.system.lanya.data.AlarmDetection;
//import com.ruoyi.system.service.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
///**
// * websocket 消息处理
// *
// * @author ruoyi
// */
//@Component
//@ServerEndpoint(value = "/system/lanya-transfer/ws/{requestId}", configurator = ServerEndpointConfig.Configurator.class)
//public class WebSocketServer {
//    /**
//     * WebSocketServer 日志控制器
//     */
//    private static final Logger log = LoggerFactory.getLogger("websocket-data");
//
//    /**
//     * 第三方授权
//     */
//    @Autowired
//    private ThirdPartyAuth thirdPartyAuth;
//
//    /**
//     * 服务器是否已经就绪
//     */
//    private static boolean serverAlreadyStarted = false;
//
//    /**
//     * 定时同步Lanya设备卡发送日志开关
//     */
//    @Value("${lanya.enabled-lanya-websocket-subscribe:false}")
//    private boolean enabledLanyaWebsocketSubscribe;
//
//    /**
//     * 定时同步Lanya设备卡发送日志开关
//     */
//    @Value("${lanya.forward-url}")
//    private String forwardUrl;
//
//    /**
//     * 定时同步Lanya设备卡发送日志开关
//     */
//    @Value("${lanya.forward-enable:false}")
//    private boolean forwardEnable;
//
//    /**
//     * 定时同步Lanya设备卡发送日志开关
//     */
//    @Value("${lanya.position.mock.enabled:false}")
//    private boolean enableMock;
//
//    /**
//     * 模拟人员数据: 人员ID
//     */
//    @Value("${lanya.position.mock.personIds:1988583160362352641,1925015210049478657}")
//    private Long[] mockPersonIds;
//
//    @Value("${lanya.position.mock.personChangeIds:1988583160362352641,1925015210049478657}")
//    private Long[] mockPersonChangeIds;
//    @Value("${lanya.position.mock.personRealNames:吕涛,于俊春,张伟星,张平}")
//    private String[] mockPersonRealNames;
//    /**
//     * 模拟人员数据: 车辆ID
//     */
//    @Value("${lanya.position.mock.vehicleIds:1925015210049478657,1925015210049478657}")
//    private Long[] mockVehicleIds;
//
//    @Value("${lanya.position.mock.vehicleChangeIds:1925015210049478657,1925015210049478657}")
//    private Long[] mockVehicleChangeIds;
//
//    /**
//     * 模拟人员数据: 日期表达式
//     */
//    @Value("${lanya.position.mock.personDates:2025-11-11 10:00:00,2025-11-12 10:00:00}")
//    private String[] mockPersonDateExps;
//
//    /**
//     * 模拟人员数据: 日期
//     */
//    private Date[] mockPersonDates;
//
//    /**
//     * 模拟人员数据: 表名称
//     */
//    private String[] mockPersonTable;
//
//    /**
//     * 模拟车辆数据: 日期表达式
//     */
//    @Value("${lanya.position.mock.vehicleDates:2025-11-11 10:00:00,2025-11-12 10:00:00}")
//    private String[] mockVehicleDateExps;
//
//    /**
//     * 模拟车辆数据: 日期
//     */
//    private Date[] mockVehicleDates;
//
//    /**
//     * 模拟车辆数据: 表名称
//     */
//    private String[] mockVehicleTable;
//
//    /**
//     * 模拟车辆数据: 车牌
//     */
//    @Value("${lanya.position.mock.vehicleNumbers:黑E98977,黑E34934}")
//    private String[] mockVehicleNumbers;
//
//    /**
//     * 是否保存人员位置
//     */
//    @Value("${lanya.save-person-location:false}")
//    private boolean savePersonLocation;
//
//    /**
//     * 目标服务器基础地址
//     */
//    private static String baseUrl;
//
//    @Value("${lanya.base-url:112.98.110.101:8091/gateway-service}")
//    private void setBaseUrl(String baseUrl) {
//        WebSocketServer.baseUrl = baseUrl;
//    }
//
//    /**
//     * 存储浏览器会话队列
//     */
//    // 存储客户端会话和对应的目标WebSocket连接
//    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();
//
//    /**
//     * 目标服务器会话
//     */
//    private WebSocketClient thirdWebSocketClient;
//
//    /**
//     * 报警检测服务
//     */
//    private static AlarmDetection alarmDetection;
//
//    @Autowired
//    public void setAlarmDetection(AlarmDetection alarmDetection) {
//        WebSocketServer.alarmDetection = alarmDetection;
//    }
//
//
//    /**
//     * 报警规则服务
//     */
//    private static IWmsAlarmRuleService wmsAlarmRuleService;
//
//    @Autowired
//    public void setWmsAlarmRuleService(IWmsAlarmRuleService wmsAlarmRuleService) {
//        WebSocketServer.wmsAlarmRuleService = wmsAlarmRuleService;
//    }
//
//    /**
//     * 设备服务
//     */
//    private static IWmsDeviceService wmsDeviceService;
//
//    @Autowired
//    public void setWmsDeviceService(IWmsDeviceService wmsDeviceService) {
//        WebSocketServer.wmsDeviceService = wmsDeviceService;
//    }
//
//    /**
//     * 存储消息队列
//     */
//    public BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(10000);
//
//    /**
//     * 物料日统计服务
//     */
//    private static IWmsMaterialStaticsDayService wmsMaterialStaticsDayService;
//
//    @Autowired
//    public void setWmsMaterialStaticsDayService(IWmsMaterialStaticsDayService wmsMaterialStaticsDayService) {
//        WebSocketServer.wmsMaterialStaticsDayService = wmsMaterialStaticsDayService;
//    }
//
//    /**
//     * 区域服务
//     */
//    private static IWmsAreaService wmsAreaService;
//
//    @Autowired
//    public void setWmsAreaService(IWmsAreaService wmsAreaService) {
//        WebSocketServer.wmsAreaService = wmsAreaService;
//    }
//
//    private static IWmsMaterialInService wmsMaterialInService;
//
//    @Autowired
//    public void setWmsMaterialInService(IWmsMaterialInService wmsMaterialInService) {
//        WebSocketServer.wmsMaterialInService = wmsMaterialInService;
//    }
//
//    private static IWmsMaterialOutService wmsMaterialOutService;
//
//    @Autowired
//    public void setWmsMaterialOutService(IWmsMaterialOutService wmsMaterialOutService) {
//        WebSocketServer.wmsMaterialOutService = wmsMaterialOutService;
//    }
//
//    private static IWmsMaterialStockService wmsMaterialStockService;
//
//    @Autowired
//    public void setWmsMaterialStockService(IWmsMaterialStockService wmsMaterialStockService) {
//        WebSocketServer.wmsMaterialStockService = wmsMaterialStockService;
//    }
//
//    private static ILanyaCorePersonService lanyaCorePersonService;
//
//    @Autowired
//    public void setLanyaCorePersonService(ILanyaCorePersonService lanyaCorePersonService) {
//        WebSocketServer.lanyaCorePersonService = lanyaCorePersonService;
//    }
//
//    private static IWmsArea360Service wmsArea360Service;
//
//    @Autowired
//    public void setWmsArea360Service(IWmsArea360Service wmsArea360Service) {
//        WebSocketServer.wmsArea360Service = wmsArea360Service;
//    }
//
//    /**
//     * 时间格式 年月日
//     */
//    SimpleDateFormat sdfYearMonDay = new SimpleDateFormat("yyyy-MM-dd");
//
//    /**
//     * 时间格式 年月日 时分秒
//     */
//    SimpleDateFormat sdfYearMondayHourMinSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    // 初始化时间格式
//    SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMdd");
//
//    /**
//     * Lanya定位数据数据服务
//     */
//    private static ILanyaPositionHistoryService lanyaPositionHistoryService;
//
//    @Autowired
//    public void setLanyaPositionHistoryService(ILanyaPositionHistoryService lanyaPositionHistoryService) {
//        WebSocketServer.lanyaPositionHistoryService = lanyaPositionHistoryService;
//    }
//
//
//    /**
//     * 初始化
//     */
//    @PostConstruct
//    public void init() {
//        try {
//            // 初始化模拟人员数据时间
//            mockPersonDates = new Date[mockPersonDateExps.length];
//            // 模拟人员数据表名称
//            mockPersonTable = new String[mockPersonDateExps.length];
//            for (int i = 0; i < mockPersonDateExps.length; i++) {
//                // 模拟人员数据时间
//                mockPersonDates[i] = sdfYearMondayHourMinSec.parse(mockPersonDateExps[i]);
//                // 模拟人员数据表名称
//                mockPersonTable[i] = "position_history_" + YMD.format(mockPersonDates[i]);
//            }
//
//            // 初始化模拟车辆数据时间
//            mockVehicleDates = new Date[mockVehicleDateExps.length];
//            // 模拟车辆数据表名称
//            mockVehicleTable = new String[mockVehicleDateExps.length];
//            for (int i = 0; i < mockVehicleDateExps.length; i++) {
//                // 模拟车辆数据时间
//                mockVehicleDates[i] = sdfYearMondayHourMinSec.parse(mockVehicleDateExps[i]);
//                // 模拟车辆数据表名称
//                mockVehicleTable[i] = "position_history_" + YMD.format(mockVehicleDates[i]);
//            }
//        } catch (Exception e) {
//            log.error("初始化模拟数据参数失败", e);
//        }
//
//        try {
//            // 启动消息处理线程
//            startMessageProcessor();
//        } catch (Exception e) {
//            log.error("初始化消息发送线程失败", e);
//        }
//
//        if (!enabledLanyaWebsocketSubscribe) {
//            log.info("Lanya WebSocket 订阅未启用");
//            return;
//        }
//
//        try {
//            // 三方接口
//            thirdPartyAuth = SpringUtils.getBean(ThirdPartyAuth.class);
//            // 三方WebSocket客户端
//            thirdWebSocketClient = SpringUtils.getBean(WebSocketClient.class);
//            // 三方用户ID
//            String thirdUserId = thirdPartyAuth.getUserInfo().get("userId");
//            // 三方socket id
//            String thirdId = "XR_" + System.currentTimeMillis() + "_" + thirdUserId;
//            // 三方目标WebSocket地址
//            String thirdTargetUrl = "ws://" + baseUrl + "/websocket/ws/" + thirdId;
//            // 连接三方目标WebSocket
//
//
//            thirdWebSocketClient.doHandshake(new ThirdWebSocketHandler(messageQueue, forwardEnable, forwardUrl, savePersonLocation, lanyaPositionHistoryService), thirdTargetUrl).get();
//
//        } catch (Exception e) {
//            log.error("Lanya WebSocket 订阅失败", e);
//        }
//        log.info("Lanya WebSocket 订阅成功");
//    }
//
//    /**
//     * 启动消息处理线程
//     */
//    private void startMessageProcessor() {
//        // 创建一个线程
//        Thread messageProcessorThread = new Thread(() -> {
//            while (true) {
//                try {
//                    // 从队列中取出消息
//                    String message = messageQueue.take(); // 阻塞式获取
//                    // 发送给所有连接的客户端
//                    broadcastMessage(message);
//                } catch (InterruptedException e) {
//                    log.info("消息处理线程被中断");
//                    Thread.currentThread().interrupt();
//                    break;
//                } catch (Exception e) {
//                    log.error("处理消息时发生异常", e);
//                }
//            }
//        });
//        messageProcessorThread.setDaemon(true);
//        messageProcessorThread.setName("WebSocket-Message-Processor");
//        messageProcessorThread.start();
//    }
//
//    /**
//     * 广播消息给所有客户端
//     *
//     * @param message 消息
//     */
//    private void broadcastMessage(String message) {
//        for (Session clientSession : clientSessions.values()) {
//            if (clientSession.isOpen()) {
//                try {
//                    synchronized (clientSession) {
//                        clientSession.getBasicRemote().sendText(message);
//                    }
//                } catch (Exception e) {
//                    log.error("发送消息给客户端时发生异常", e);
//                }
//            }
//        }
//    }
//
//    /**
//     * 客户端连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("requestId") String requestId) throws Exception {
//        try {
//            log.info("客户端连接成功:{}", requestId);
//            // 将客户端会话存储到Map中
//            if (clientSessions.containsKey(requestId)) {
//                Session sess = clientSessions.remove(requestId);
//                if (sess.isOpen()) {
//                    sess.close();
//                }
//            }
//            clientSessions.put(requestId, session);
//
//            // 首次连接时向客户端发送消息
//            this.firstData(session);
//
//        } catch (Exception e) {
//            log.error("连接异常:", e);
//        }
//    }
//
//    private void firstData(Session session) {
//        // 查询条件
//        WmsDevice wmsDeviceQuery = new WmsDevice();
//
//        // 摄像头设备
//        wmsDeviceQuery.setDeviceType("摄像头");
//        List<WmsDevice> wmsDevicesCamera = wmsDeviceService.selectWmsDeviceList(wmsDeviceQuery);
//
//        // 传感器设备
//        wmsDeviceQuery.setDeviceType("传感器");
//        List<WmsDevice> wmsDevicesSensor = wmsDeviceService.selectWmsDeviceList(wmsDeviceQuery);
//
//        // 区域点位全景数据
//        List<WmsArea360> wmsArea360s = wmsArea360Service.selectWmsArea360List(new WmsArea360());
//        List<JSONObject> wmsArea360sBody = new ArrayList<>();
//        for (WmsArea360 wmsArea360 : wmsArea360s) {
//            JSONObject wmsArea360Body = new JSONObject();
//            wmsArea360Body.put("id", wmsArea360.getId());
//            wmsArea360Body.put("deviceName", wmsArea360.getName());
//            wmsArea360Body.put("deviceType", "传感器");
//            wmsArea360Body.put("longitude", wmsArea360.getLongitude());
//            wmsArea360Body.put("latitude", wmsArea360.getLatitude());
//            wmsArea360Body.put("altitude", 0);
//            wmsArea360Body.put("type", "watch");
//            wmsArea360Body.put("photo360", wmsArea360.getImage());
//            wmsArea360sBody.add(wmsArea360Body);
//        }
//
//        // 向客户端发送的消息体
//        Map<String, Object> deviceBody = new HashMap<String, Object>() {
//            {
//                put("msgType", "摄像头与传感器");
//                put("rules", new ArrayList<Object>() {{
//                    addAll(wmsDevicesCamera);
//                    addAll(wmsDevicesSensor);
//                    addAll(wmsArea360sBody);
//                }});
//            }
//        };
//        // 向当前连接发送初始摄像头与传感器数据
//        log.info("向客户端发送初始数据:{}", deviceBody);
//        session.getAsyncRemote().sendText(new JSONObject(deviceBody).toJSONString());
//    }
//
//    /**
//     * 客户端连接关闭时处理
//     */
//    @OnClose
//    public void onClose(Session session, @PathParam("requestId") String requestId) throws IOException {
//        log.info("客户端连接关闭:{}", requestId);
//        // 清理资源
//        if (clientSessions.containsKey(requestId)) {
//            Session sess = clientSessions.remove(requestId);
//            if (sess.isOpen()) {
//                sess.close();
//            }
//        }
//    }
//
//    /**
//     * 客户端抛出异常时处理
//     */
//    @OnError
//    public void onError(Session session, @PathParam("requestId") String requestId, Throwable exception) throws Exception {
//        log.error("客户端抛出异常:{}", requestId, exception);
//        // 清理资源
//        if (clientSessions.containsKey(requestId)) {
//            Session sess = clientSessions.remove(requestId);
//            if (sess.isOpen()) {
//                sess.close();
//            }
//        }
//    }
//
//    /**
//     * 接收到客户端消息时调用的方法
//     */
//    @OnMessage
//    public void onMessage(String message, Session session, @PathParam("requestId") String requestId) {
//        log.info("收到客户端消息:{}-{}", requestId, message);
//    }
//
//    /**
//     * 车辆统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.vehicle-alarm:0/10 * * * * ?}")
//    public void vehicleAlarmData() {
//        Map<String, Object> vehicleAlarm = new HashMap<String, Object>() {
//            {
//                put("msgType", "vehicleAlarm");
//                put("rules", new ArrayList<WmsVehicleAlarmRule>() {{
//                    add(new WmsVehicleAlarmRule("未授权进入", 0, 0));
//                    add(new WmsVehicleAlarmRule("未预约进入", 0, 0));
//                    add(new WmsVehicleAlarmRule("轨迹异常", 0, 0));
//                }});
//            }
//        };
//        String json = new JSONObject(vehicleAlarm).toJSONString();
//        messageQueue.add(json);
//    }
//
//    /**
//     * 人员报警数据
//     */
//    @Scheduled(cron = "${wms.ws-data.person-alarm:0/10 * * * * ?}")
//    public void personAlarmData() {
//        List<WmsAlarmRule> rules = alarmDetection.getWmsAlarmRules();
//        Map<String, Object> personAlarm = new HashMap<String, Object>() {
//            {
//                put("msgType", "personAlarm");
//                put("rules", rules);
//            }
//        };
//        String json = new JSONObject(personAlarm).toJSONString();
//        messageQueue.add(json);
//    }
//
//    /**
//     * 物料统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.material-statics:0/10 * * * * ?}")
//    public void materialStaticsData() {
//
//        // 使用异步发送替代阻塞发送
//        WmsMaterialStaticsDay wmsMaterialStaticsDay = new WmsMaterialStaticsDay();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        String ymd = sdfYearMonDay.format(calendar.getTime());
//        wmsMaterialStaticsDay.setDay(ymd);
//        List<WmsMaterialStaticsDay> wmsMaterialStaticsDays = wmsMaterialStaticsDayService.selectWmsMaterialStaticsDayList(wmsMaterialStaticsDay);
//        JSONObject materialLog = new JSONObject();
//        JSONArray materialLogData = new JSONArray();
//        materialLog.put("msgType", "materialLog");
//        materialLog.put("data", materialLogData);
//        materialLog.put("total", wmsMaterialStaticsDays.size());
//        for (int i = 0; i < wmsMaterialStaticsDays.size(); i++) {
//            WmsMaterialStaticsDay d = wmsMaterialStaticsDays.get(i);
//            JSONObject materialLogDataItem = new JSONObject();
//            materialLogDataItem.put("sort", i);
//            materialLogDataItem.put("materialName", d.getWzmc());
//            materialLogDataItem.put("materialCode", d.getWzbm());
//            materialLogDataItem.put("materialType", d.getWzlb());
//            materialLogDataItem.put("stockIn", d.getJl());
//            materialLogDataItem.put("stockOut", d.getDb());
//            materialLogDataItem.put("stock", d.getKc());
//            materialLogDataItem.put("areaName", d.getAreaCodes());
//            materialLogData.add(materialLogDataItem);
//        }
////        messageQueue.add(materialLog.toString());
//    }
//
//    /**
//     * 物料统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.material-jl:0/10 * * * * ?}")
//    public void materialJlData() {
//        // 使用异步发送替代阻塞发送
//        WmsMaterialIn wmsMaterialIn = new WmsMaterialIn();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        String ymd = sdfYearMonDay.format(calendar.getTime());
//        wmsMaterialIn.setJlsj(calendar.getTime());
//        List<WmsMaterialIn> wmsMaterialIns = wmsMaterialInService.selectWmsMaterialInList(new WmsMaterialIn());
//        JSONObject materialLog = new JSONObject();
//        JSONArray materialLogData = new JSONArray();
//        materialLog.put("msgType", "materialJlLog");
//        materialLog.put("data", materialLogData);
//        materialLog.put("total", wmsMaterialIns.size());
//        for (int i = 0; i < wmsMaterialIns.size(); i++) {
//            WmsMaterialIn d = wmsMaterialIns.get(i);
//            JSONObject materialLogDataItem = new JSONObject();
//            materialLogDataItem.put("sort", i);
//            materialLogDataItem.put("materialName", d.getWzmc());
//            materialLogDataItem.put("materialCode", d.getWzbm());
//            materialLogDataItem.put("materialType", d.getWzlb());
//            materialLogDataItem.put("stockIn", d.getDhsl());
//            materialLogDataItem.put("areaName", d.getAreaCodes());
//            materialLogData.add(materialLogDataItem);
//        }
//        messageQueue.add(materialLog.toString());
//    }
//
//    /**
//     * 物料统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.material-db:0/10 * * * * ?}")
//    public void materialDbData() throws ParseException {
//        // 使用异步发送替代阻塞发送
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        String start = sdfYearMonDay.format(calendar.getTime()) + " 00:00:00";
//        String end = sdfYearMonDay.format(calendar.getTime()) + " 23:59:59";
//
//        List<WmsMaterialOut> wmsMaterialOuts = wmsMaterialOutService.selectWmsMaterialOutListByDateRange(sdfYearMondayHourMinSec.parse(start), sdfYearMondayHourMinSec.parse(end));
//        JSONObject materialLog = new JSONObject();
//        JSONArray materialLogData = new JSONArray();
//        materialLog.put("msgType", "materialDbLog");
//        materialLog.put("data", materialLogData);
//        materialLog.put("total", wmsMaterialOuts.size());
//        for (int i = 0; i < wmsMaterialOuts.size(); i++) {
//            WmsMaterialOut d = wmsMaterialOuts.get(i);
//            if (d.getActualQuantity() > 0) {
//                JSONObject materialLogDataItem = new JSONObject();
//                materialLogDataItem.put("sort", i);
//                materialLogDataItem.put("materialName", d.getWzmc());
//                materialLogDataItem.put("materialCode", d.getWzbm());
//                materialLogDataItem.put("materialType", d.getWzlb());
//                materialLogDataItem.put("stockOut", d.getActualQuantity());
//                materialLogDataItem.put("areaName", d.getAreaCodes());
//                materialLogData.add(materialLogDataItem);
//            }
//        }
//        messageQueue.add(materialLog.toString());
//    }
//
//    /**
//     * 物料统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.material-kc:0/10 * * * * ?}")
//    public void materialKcData() {
//        // 使用异步发送替代阻塞发送
//        WmsMaterialStock wmsMaterialStock = new WmsMaterialStock();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -1);
//        String ymd = sdfYearMonDay.format(calendar.getTime());
//        wmsMaterialStock.setCreateTime(calendar.getTime());
//        List<WmsMaterialStock> wmsMaterialStocks = wmsMaterialStockService.selectWmsMaterialStockList(wmsMaterialStock);
//        JSONObject materialLog = new JSONObject();
//        JSONArray materialLogData = new JSONArray();
//        materialLog.put("msgType", "materialKcLog");
//        materialLog.put("data", materialLogData);
//        materialLog.put("total", wmsMaterialStocks.size());
//        for (int i = 0; i < wmsMaterialStocks.size(); i++) {
//            WmsMaterialStock d = wmsMaterialStocks.get(i);
//            if (d.getActualWeight() >= 0) {
//                JSONObject materialLogDataItem = new JSONObject();
//                materialLogDataItem.put("sort", i);
//                materialLogDataItem.put("materialName", d.getWzmc());
//                materialLogDataItem.put("materialCode", d.getWzbm());
//                materialLogDataItem.put("materialType", d.getWzlb());
//                materialLogDataItem.put("stock", d.getActualWeight());
//                materialLogDataItem.put("areaNames", d.getAreaNames());
//                materialLogDataItem.put("areaCodes", d.getAreaCodes());
//                materialLogData.add(materialLogDataItem);
//            }
//        }
//        messageQueue.add(materialLog.toString());
//    }
//
//    /**
//     * 区域统计数据
//     */
//    @Scheduled(cron = "${wms.ws-data.area-statics:0/10 * * * * ?}")
//    public void areaStaticsData() {
//        Map<Long, WmsArea> wmsAreas = wmsAreaService.getAreaMap();
//        // 使用异步发送替代阻塞发送
//        JSONObject areaLog = new JSONObject();
//        JSONArray areaLogData = new JSONArray();
//        areaLog.put("msgType", "areaLog");
//        areaLog.put("total", wmsAreas.size());
//        areaLog.put("data", areaLogData);
//        int i = 0;
//        for (Map.Entry<Long, WmsArea> entry : wmsAreas.entrySet()) {
//            WmsArea a = entry.getValue();
//            JSONObject areaLogDataItem = new JSONObject();
//            areaLogDataItem.put("sort", i);
//            areaLogDataItem.put("areaCode", a.getAreaCode());
//            areaLogDataItem.put("areaType", a.getAreaType());
//            areaLogDataItem.put("personCount", a.getStaffCount());
//            areaLogDataItem.put("vehicleCount", a.getVehicleCount());
//            areaLogDataItem.put("areaName", a.getAreaName());
//            areaLogData.add(areaLogDataItem);
//            i++;
//        }
//        messageQueue.add(areaLog.toString());
//    }
//
//    /**
//     * 创建LanyaPositionHistory数据模拟器
//     */
//    @Scheduled(cron = "0/2 * * * * ?")
//    public void realtimeVehiclePositionHistoryData() throws ParseException {
//        DecimalFormat df = new DecimalFormat("#.##");
//        String tableName = "position_history_" + YMD.format(new Date());
//        // 创建所需的JSONObject
//        JSONObject locationData = new JSONObject();
//        locationData.put("msgType", "currentVehicleLocation");
//        locationData.put("total", 1);
//
//        // 创建personTypeStatistics数组
//        JSONArray personTypeStats = new JSONArray();
//
//        // 添加staff类型统计
//        JSONObject staffStat = new JSONObject();
//        staffStat.put("count", mockVehicleIds.length);
//        staffStat.put("personType", "staff");
//        staffStat.put("personTypeName", "员工");
//        staffStat.put("ratio", df.format((float) mockVehicleIds.length / mockVehicleIds.length * 100));
//        personTypeStats.add(staffStat);
//
//        locationData.put("personTypeStatistics", personTypeStats);
//        JSONArray dataArray = new JSONArray();
//
//        List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectNewLanyaPositionHistoryListByTable(tableName);
//        for (LanyaPositionHistory history : trajectory) {
//            JSONObject locationDataItem = JSONObject.from(history);
//            dataArray.add(locationDataItem);
//        }
//
//        locationData.put("data", dataArray);
//        messageQueue.add(locationData.toString());
//    }
//
//    /**
//     * 创建LanyaPositionHistory数据模拟器
//     */
//    @Scheduled(cron = "0/2 * * * * ?")
//    public void realtimePersonPositionHistoryData() throws ParseException {
//        DecimalFormat df = new DecimalFormat("#.##");
//        String tableName = "position_history_" + YMD.format(new Date());
//        Map<Long, WmsArea> wmsAreas = wmsAreaService.getAreaMap();
//        int staffCount = 0;
//        int visitorCount = 0;
//        for (Map.Entry<Long, WmsArea> entry : wmsAreas.entrySet()) {
//            WmsArea a = entry.getValue();
//            staffCount += a.getStaffCount();
//            visitorCount += a.getVisitorCount();
//        }
//
//        // 创建所需的JSONObject
//        JSONObject locationData = new JSONObject();
//        locationData.put("msgType", "currentPersonLocation");
//        locationData.put("total", 1);
//
//
//        // 创建personTypeStatistics数组
//        JSONArray personTypeStats = new JSONArray();
//
//        // 添加staff类型统计
//        JSONObject staffStat = new JSONObject();
//        staffStat.put("count", mockPersonIds.length);
//        staffStat.put("personType", "staff");
//        staffStat.put("personTypeName", "内部员工");
//        staffStat.put("ratio", df.format((float) mockPersonIds.length / mockPersonIds.length * 100));
//        personTypeStats.add(staffStat);
//
//        // 添加visitor类型统计
//        JSONObject visitorStat = new JSONObject();
//        visitorStat.put("count", 0);
//        visitorStat.put("personType", "visitor");
//        visitorStat.put("personTypeName", "临时访客");
//        visitorStat.put("ratio", 0);
//        personTypeStats.add(visitorStat);
//
//        locationData.put("personTypeStatistics", personTypeStats);
//
//        JSONArray dataArray = new JSONArray();
//        List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectNewLanyaPositionHistoryListByTable(tableName);
//        for (LanyaPositionHistory history : trajectory) {
//            dataArray.add(history);
//        }
//        locationData.put("data", dataArray);
//        messageQueue.add(locationData.toString());
//    }
//
//    /**
//     * 创建LanyaPositionHistory数据模拟器
//     */
//    @Scheduled(cron = "0/2 * * * * ?")
//    public void generateMockVehiclePositionHistoryData() throws ParseException {
//        if (!enableMock) {
//            return;
//        }
//
//        DecimalFormat df = new DecimalFormat("#.##");
//
//        // 创建所需的JSONObject
//        JSONObject locationData = new JSONObject();
//        locationData.put("msgType", "currentVehicleLocation");
//        locationData.put("total", 1);
//
//        // 创建personTypeStatistics数组
//        JSONArray personTypeStats = new JSONArray();
//
//        // 添加staff类型统计
//        JSONObject staffStat = new JSONObject();
//        staffStat.put("count", mockVehicleIds.length);
//        staffStat.put("personType", "staff");
//        staffStat.put("personTypeName", "员工");
//        staffStat.put("ratio", df.format((float) mockVehicleIds.length / mockVehicleIds.length * 100));
//        personTypeStats.add(staffStat);
//
//        locationData.put("personTypeStatistics", personTypeStats);
//        JSONArray dataArray = new JSONArray();
//        for (int i = 0; i < mockVehicleIds.length; i++) {
//            List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTable(mockVehicleTable[i], mockVehicleIds[i], mockVehicleDates[i]);
//            for (LanyaPositionHistory history : trajectory) {
//                JSONObject locationDataItem = JSONObject.from(history);
//                locationDataItem.put("vehicleNumber", mockVehicleNumbers[i]);
//                locationDataItem.put("realName", mockVehicleNumbers[i]);
//                locationDataItem.put("personId", mockVehicleChangeIds[i]);
//                // 创建空的data数组
//                mockVehicleDates[i] = history.getCreateTime();
//                dataArray.add(locationDataItem);
//            }
//        }
//        locationData.put("data", dataArray);
//        messageQueue.add(locationData.toString());
//    }
//
//    /**
//     * 创建LanyaPositionHistory数据模拟器
//     */
//    @Scheduled(cron = "0/2 * * * * ?")
//    public void generateMockPersonPositionHistoryData() throws ParseException {
//        if (!enableMock) {
//            return;
//        }
//
//        DecimalFormat df = new DecimalFormat("#.##");
//        List<LanyaCorePerson> lanyaCorePeople = lanyaCorePersonService.selectLanyaCorePersonList(new LanyaCorePerson());
//        Map<Long, WmsArea> wmsAreas = wmsAreaService.getAreaMap();
//        int staffCount = 0;
//        int visitorCount = 0;
//        for (Map.Entry<Long, WmsArea> entry : wmsAreas.entrySet()) {
//            WmsArea a = entry.getValue();
//            staffCount += a.getStaffCount();
//            visitorCount += a.getVisitorCount();
//        }
//
//        // 创建所需的JSONObject
//        JSONObject locationData = new JSONObject();
//        locationData.put("msgType", "currentPersonLocation");
//        locationData.put("total", 1);
//
//
//        // 创建personTypeStatistics数组
//        JSONArray personTypeStats = new JSONArray();
//
//        // 添加staff类型统计
//        JSONObject staffStat = new JSONObject();
//        staffStat.put("count", mockPersonIds.length);
//        staffStat.put("personType", "staff");
//        staffStat.put("personTypeName", "内部员工");
//        staffStat.put("ratio", df.format((float) mockPersonIds.length / mockPersonIds.length * 100));
//        personTypeStats.add(staffStat);
//
//        // 添加visitor类型统计
//        JSONObject visitorStat = new JSONObject();
//        visitorStat.put("count", 0);
//        visitorStat.put("personType", "visitor");
//        visitorStat.put("personTypeName", "临时访客");
//        visitorStat.put("ratio", 0);
//        personTypeStats.add(visitorStat);
//
//        locationData.put("personTypeStatistics", personTypeStats);
//
//        JSONArray dataArray = new JSONArray();
//        for (int i = 0; i < mockPersonIds.length; i++) {
//            List<LanyaPositionHistory> trajectory = lanyaPositionHistoryService.selectLanyaPositionHistoryListByTable(mockPersonTable[i], mockPersonIds[i], mockPersonDates[i]);
//            for (LanyaPositionHistory history : trajectory) {
//                // 创建空的data数组
//                mockPersonDates[i] = history.getCreateTime();
//                history.setPersonId(mockPersonChangeIds[i]);
//                history.setRealName(mockPersonRealNames[i]);
//                dataArray.add(history);
//            }
//        }
//        locationData.put("data", dataArray);
//        messageQueue.add(locationData.toString());
//    }
//
//    public void setServerAlreadyStarted() {
//        serverAlreadyStarted = false;
//    }
//
//    /**
//     * 三方WebSocket服务器的消息处理器
//     */
//    public static class ThirdWebSocketHandler extends TextWebSocketHandler {
//
//        private final BlockingQueue<String> messageQueue;
//        private final boolean forwardEnable;
//        private final String forwardUrl;
//        private final boolean savePersonLocation;
//        private final ILanyaPositionHistoryService lanyaPositionHistoryService;
//        public RestTemplate restTemplate = new RestTemplate();
//        private ObjectMapper mapper = new ObjectMapper();
//        HttpHeaders headers = new HttpHeaders();
//
//
//        public ThirdWebSocketHandler(BlockingQueue<String> messageQueue, boolean forwardEnable, String forwardUrl, boolean savePersonLocation, ILanyaPositionHistoryService lanyaPositionHistoryService) {
//            this.messageQueue = messageQueue;
//            this.forwardEnable = forwardEnable;
//            this.forwardUrl = forwardUrl;
//            this.savePersonLocation = savePersonLocation;
//            this.lanyaPositionHistoryService = lanyaPositionHistoryService;
//            this.headers.setContentType(MediaType.APPLICATION_JSON);
//            this.headers.set("Authorization", "fei@#%joie@#&*joijo1234%^567AABB");
//        }
//
//        /**
//         * 处理来自目标服务器的消息
//         */
//        @Override
//        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//            try {
//                // 检查消息大小
//                if (message.getPayloadLength() > 1024 * 1024) { // 1MB限制
//                    log.warn("接收 Lanya WebSocket 消息过大，大小: {} 字节", message.getPayloadLength());
//                    // 可以选择丢弃或分批处理
//                }
//                String payload = message.getPayload();
//                JSONObject jsonObject = JSONObject.parseObject(payload);
//                if (jsonObject.get("msgType").equals("cardSenderView")) {
//                    return;
//                }
//                if (jsonObject.get("msgType").equals("alarmCount")) {
//                    return;
//                }
//                if (jsonObject.get("msgType").equals("vehicleAlarmCount")) {
//                    return;
//                }
//                if (jsonObject.get("msgType").equals("currentVehicleLocation")) {
//                    return;
//                }
//                if (jsonObject.get("msgType").equals("personCount")) {
//                    return;
//                }
//                if (jsonObject.get("msgType").equals("currentPersonLocation")) {
//                    return;
//                }
//                messageQueue.add(payload);
//
//                if (forwardEnable && serverAlreadyStarted) {
//                    String sendBody = mapper.writeValueAsString(new HashMap<String, String>() {{
//                        put("message", payload);
//                    }});
//                    HttpEntity<String> entity = new HttpEntity<>(sendBody, this.headers);
//                    ResponseEntity<String> resp = restTemplate.exchange(this.forwardUrl, HttpMethod.POST, entity, String.class);
//                    HashMap respMap = this.mapper.readValue(resp.getBody(), HashMap.class);
//                }
//
//                if (savePersonLocation && serverAlreadyStarted) {
//                    LanyaPositionHistory history = new LanyaPositionHistory();
//
//                    lanyaPositionHistoryService.insertLanyaPositionHistory(history);
//                }
//            } catch (Exception e) {
//                log.error("处理 Lanya WebSocket 消息异常", e);
//            }
//        }
//
//        /**
//         * 处理目标服务器连接建立
//         */
//        @Override
//        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//            log.info("与 Lanya WebSocket 连接建立成功");
//        }
//
//        /**
//         * 处理目标服务器连接关闭
//         */
//        @Override
//        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//            log.info("与 Lanya WebSocket 连接关闭,状态: {}", status);
//        }
//
//        /**
//         * 处理传输错误
//         */
//        @Override
//        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//            log.error("与 Lanya WebSocket 传输错误:", exception);
//        }
//    }
//}
