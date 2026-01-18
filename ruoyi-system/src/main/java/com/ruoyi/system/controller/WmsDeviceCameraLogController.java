package com.ruoyi.system.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.GraphPath;

/**
 * 摄像头识别日志Controller
 *
 * @author ruoyi
 * @date 2025-11-11
 */
@Api(value = "摄像头识别日志管理", tags = {"系统端", "摄像头识别日志管理"})
@RestController
@RequestMapping("/system/wms_device_camera_log")
public class WmsDeviceCameraLogController extends BaseController {
    @Autowired
    private IWmsDeviceCameraLogService wmsDeviceCameraLogService;

    @Autowired
    private IWmsDeviceService wmsDeviceService;

    @Autowired
    private IWmsPathsDefinetionService wmsPathsDefinetionService;

    @Autowired
    private IWmsVehiclePositionHistoryService wmsVehiclePositionHistoryService;

    @Autowired
    private WebSocketServer webSocketServer;

    private static Map<String, String> ipMapDeviceName = new HashMap<>();
    private static Map<String, List<String>> neighbors = new HashMap<>();

    private SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");
    // 1. 创建图对象 (顶点和边都用 String 表示名字)
    private SimpleGraph<String, DefaultEdge> mapGraph = new SimpleGraph<>(DefaultEdge.class);

    @Autowired
    private IWmsTrajectoryService wmsTrajectoryService;

    private Map<String, PathDetection> pathDetectionCache = new HashMap<>();

    /**
     * 入口闸机
     */
    private WmsDevice entranceGate = new WmsDevice();

    /**
     * 出口闸机
     */
    private WmsDevice exitGate = new WmsDevice();

    /**
     * 入口车牌号列表
     */
    private Map<String, WmsDeviceCameraLog> entrancePlateNumbers = new HashMap<>();

    /**
     * 是否播放从当前点虚拟一个未来的路径还是从上一个点到当前点的路径
     */
    @Value("${is-play-future-path:false}")
    boolean isPlayFuturePath;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        WmsDevice wmsDevice = new WmsDevice();
        wmsDevice.setDeviceType("摄像头");
        List<WmsDevice> wmsDevices = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        for (WmsDevice device : wmsDevices) {
            String data = device.getData();
            JSONObject jsonObject = JSONObject.parseObject(data);
            if (jsonObject.containsKey("ip1")) {
                ipMapDeviceName.put(jsonObject.getString("ip1"), device.getDeviceName());
            }
            if (jsonObject.containsKey("ip2")) {
                ipMapDeviceName.put(jsonObject.getString("ip2"), device.getDeviceName());
            }
            // 2. 添加点（独立描述数据）
            mapGraph.addVertex(device.getDeviceName());
        }

        // 获取闸机
        wmsDevice.setDeviceType("闸机");
        wmsDevices = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        for (WmsDevice device : wmsDevices) {
            if (device.getDeviceName().equals("入口闸机")) {
                entranceGate = device;
            }
            if (device.getDeviceName().equals("出口闸机")) {
                exitGate = device;
            }
        }


        List<WmsPathsDefinetion> wmsPathsDefinetions = wmsPathsDefinetionService.selectWmsPathsDefinetionList(new WmsPathsDefinetion());
        for (WmsPathsDefinetion pathsDefinetion : wmsPathsDefinetions) {
            // 3. 添加边（路径描述数据）
            mapGraph.addEdge(pathsDefinetion.getFromName(), pathsDefinetion.getToName());
            if (!neighbors.containsKey(pathsDefinetion.getFromName())) {
                neighbors.put(pathsDefinetion.getFromName(), new ArrayList<>());
            }
            if (!neighbors.containsKey(pathsDefinetion.getToName())) {
                neighbors.put(pathsDefinetion.getToName(), new ArrayList<>());
            }
            if (!neighbors.get(pathsDefinetion.getFromName()).contains(pathsDefinetion.getToName())) {
                neighbors.get(pathsDefinetion.getFromName()).add(pathsDefinetion.getToName());
            }
            if (!neighbors.get(pathsDefinetion.getToName()).contains(pathsDefinetion.getFromName())) {
                neighbors.get(pathsDefinetion.getToName()).add(pathsDefinetion.getFromName());
            }
        }
        logger.info("摄像头设备名称：{}", ipMapDeviceName);
    }

    /**
     * 查询摄像头识别日志列表
     */
    @ApiOperation("查询摄像头识别日志列表")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsDeviceCameraLog wmsDeviceCameraLog) {
        startPage();
        List<WmsDeviceCameraLog> list = wmsDeviceCameraLogService.selectWmsDeviceCameraLogList(wmsDeviceCameraLog);
        for (WmsDeviceCameraLog cameraLog : list) {
            if (ipMapDeviceName.containsKey(cameraLog.getDwmc())) {
                cameraLog.setDwmc(ipMapDeviceName.get(cameraLog.getDwmc()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出摄像头识别日志列表
     */
    @ApiOperation("导出摄像头识别日志列表")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:export')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsDeviceCameraLog wmsDeviceCameraLog) {
        List<WmsDeviceCameraLog> list = wmsDeviceCameraLogService.selectWmsDeviceCameraLogList(wmsDeviceCameraLog);
        ExcelUtil<WmsDeviceCameraLog> util = new ExcelUtil<WmsDeviceCameraLog>(WmsDeviceCameraLog.class);
        util.exportExcel(response, list, "摄像头识别日志数据");
    }

    /**
     * 获取摄像头识别日志详细信息
     */
    @ApiOperation("获取摄像头识别日志详细信息")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(id));
    }

    /**
     * 新增摄像头识别日志
     */
    @ApiOperation("新增摄像头识别日志")
    @Log(title = "摄像头识别日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsDeviceCameraLog currLog) {
        if (currLog.getSj() == null) {
            // 如果没有设置时间，则设置为当前时间
            currLog.setSj(new Date());
        }

        // 日志是闸机的数据
        boolean isGate = false;
        // 日志时出口闸机数据
        boolean isExitGate = false;
        // log是入口闸机的数据，则记录车牌号
        if (entranceGate.getData().contains(currLog.getDwmc())) {
            entrancePlateNumbers.put(currLog.getCph(), currLog);
            isGate = true;
        }

        // log是出口闸机的数据，则完成车辆轨迹
        if (exitGate.getData().contains(currLog.getDwmc())) {
            completePosition(currLog.getCph(), currLog.getSj());
            entrancePlateNumbers.remove(currLog.getCph());
            isGate = true;
            isExitGate = true;
        }

        currLog.setSrcCph(currLog.getCph());
        currLog.setCph(null);


        // 如果日志不是闸机数据，则尝试匹配车牌号
        WmsDeviceCameraLog bestMatchingPlateWithQuality = null;
        if (!isGate) {
            bestMatchingPlateWithQuality = findBestMatchingPlateWithQuality(currLog);
            if (bestMatchingPlateWithQuality != null) {
                currLog.setCph(bestMatchingPlateWithQuality.getCph());
            }
        }

        // 插入新的wms_device_camera_log
        int newLog = wmsDeviceCameraLogService.insertWmsDeviceCameraLog(currLog);

        if (!isGate && bestMatchingPlateWithQuality == null) {
            // 如果不是入口闸机数据并且没有匹配到入口闸机车牌号则不在地图上显示，直接跳出接口。
            return toAjax(newLog);
        }

        if (isExitGate) {
            // 如果是出口闸机数据则不在地图上显示，直接跳出接口。
            return toAjax(newLog);
        }

        /// 按天创建一个车牌号的虚拟路径 ///

        // 根据当前log的摄像头ip查询当前设备
        WmsDevice currDevice = wmsDeviceService.selectWmsDeviceByIp(currLog.getDwmc());
        WmsDevice prevDevice = null;
        List<List<String>> playPaths = new ArrayList<>();
        playPaths.add(new ArrayList<>());
        playPaths.add(new ArrayList<>());
        if (currDevice == null) {
            // 当前输入没有找到对应的摄像头设备
            return toAjax(newLog);
        }

        // 当前天的时间字符串
        String ymd = sdfTableSuffix.format(new Date());

        // 查询当前天和车牌号的记录
        WmsVehiclePositionHistory query = new WmsVehiclePositionHistory();
        query.setYmd(sdfTableSuffix.format(new Date()));
        query.setCph(currLog.getCph());
        List<WmsVehiclePositionHistory> res = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(query);

        // 当前新WmsDeviceCameraLog是否与记录中最后一个摄像头设备是同一个设备,默认是false，
        // 如果是第一次的车辆数据接下来会或者不是上一个设备会创建虚拟路径发送给websocket服务器，页面会绘制车辆的虚拟路径。
        boolean isSameDevice = false;

        // 判断缓存中是否有车牌号
        if (!pathDetectionCache.containsKey(currLog.getCph())) {
            // 创建一个路径发现对象
            PathDetection pathDetection = new PathDetection();
            pathDetection.begin = currLog.getSj();
            pathDetection.cph = currLog.getCph();
            pathDetectionCache.put(currLog.getCph(), pathDetection);
        }

        // 当前车辆的路径点发现类对象
        PathDetection pathDetection = pathDetectionCache.get(currLog.getCph());
        pathDetection.cameraNames.add(currDevice.getDeviceName());
        pathDetection.cameraIps.add(currLog.getDwmc());

        // 判断车牌号今天是否有记录
        if (res.isEmpty()) {
            // 今天没有记录，新增
            WmsVehiclePositionHistory positionHistory = new WmsVehiclePositionHistory();
            positionHistory.setCph(currLog.getCph());
            positionHistory.setYmd(ymd);
            positionHistory.setKssj(new Date());
            positionHistory.setLogIds(currLog.getId().toString());
            wmsVehiclePositionHistoryService.insertWmsVehiclePositionHistory(positionHistory);
        } else {
            // 今天有记录，更新
            WmsVehiclePositionHistory positionHistory = res.get(0);
            // 今天记录中记录的wms_device_camera_log的ID
            List<String> logIds = new ArrayList<>(Arrays.asList(positionHistory.getLogIds().split(",")));
            // 更具今天已记录的WmsVehiclePositionHistory中最后一个wms_device_camera_log id 取WmsDeviceCameraLog对象
            WmsDeviceCameraLog prevLog = wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(Long.parseLong(logIds.get(logIds.size() - 1)));
            // 当前车辆的上一个摄像头设备
            prevDevice = wmsDeviceService.selectWmsDeviceByIp(prevLog.getDwmc());

            // 车牌机端会发送在同一车牌机会发送重复的识别记录，所以最后一个识别记录的IP和当前识别记录的IP不一致，则添加。如果相同和忽略
            if (prevDevice != null && !prevDevice.getDeviceName().equals(currDevice.getDeviceName())) {
                // 获取当前记录的经纬度
                String historyPointsStr = positionHistory.getPoints();
                // 转成List
                List<String> historyPoints = historyPointsStr != null && !historyPointsStr.isEmpty() ? new ArrayList<>(Arrays.asList(historyPointsStr.split(";"))) : new ArrayList<>();
                // 查询当前车辆所在摄像头设备与上一个摄像头设备之间的路径点
                List<String> path = findPath(prevDevice.getDeviceName(), currDevice.getDeviceName());
                // 遍历路径点
                for (int i = 0; i < path.size() - 1; i++) {
                    // 获取两个摄像头设备之间的路径点
                    String name1 = path.get(i);
                    String name2 = path.get(i + 1);
                    List<List<String>> wmsPathsDefinition = getWmsPathsDefinition(name1, name2);

                    // 将路径点添加到历史路径点中
                    for (int j = 0; j < wmsPathsDefinition.get(0).size(); j++) {
                        String point = wmsPathsDefinition.get(0).get(j) + "," + wmsPathsDefinition.get(1).get(j);
                        historyPoints.add(point);
                        pathDetection.pathsAll.add(point);
                        playPaths.get(0).addAll(wmsPathsDefinition.get(0));
                        playPaths.get(1).addAll(wmsPathsDefinition.get(1));
                    }
                }
                // 重新赋值虚拟路径点
                positionHistory.setPoints(String.join(";", historyPoints));
                // 添加wms_device_camera_log的ID
                logIds.add(currLog.getId().toString());
                positionHistory.setLogIds(String.join(",", logIds));

                // 修改结束时间
                positionHistory.setJssj(new Date());

                // 更新车辆路径点历史记录
                wmsVehiclePositionHistoryService.updateWmsVehiclePositionHistory(positionHistory);
            } else {
                // 当前车辆所在的摄像头设备与上一个设备是同一个设备，不创建虚拟路径给前端。
                isSameDevice = true;
            }
        }

        if (!isSameDevice) {

            // 是播放虚拟未来路径还是播放从上一个点到当前点的路径;
            if (isPlayFuturePath) {
                // 获取当前摄像头设备的相邻摄像头设备名称列表
                List<String> strings = neighbors.get(currDevice.getDeviceName());

                // 下一个虚拟要到达的摄像头设备的名称
                String nextDeviceName = null;
                if (strings != null) {
                    nextDeviceName = strings.get(0);
                }
                if (nextDeviceName != null) {
                    List<List<String>> wmsPathsDefinition = getWmsPathsDefinition(currDevice.getDeviceName(), nextDeviceName);
                    // 添加虚拟路径点
                    pathDetection.setFutureLongitudeAndLatitude(wmsPathsDefinition.get(0), wmsPathsDefinition.get(1));


                }
            } else {
                pathDetection.setFutureLongitudeAndLatitude(playPaths.get(0), playPaths.get(1));
            }
            // 更新车辆路径点数据给前端浏览器
            webSocketServer.updateVehiclePositionHistoryData(pathDetection);
        }

        return toAjax(newLog);
    }

    /**
     * 移除摄像头识别车辆从缓存中
     */
    @ApiOperation("移除摄像头识别车辆从缓存中")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:remove_cache')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.REMOVE_CACHE)
    @GetMapping("/remove_cache/{cph}")
    public AjaxResult removeCache(@PathVariable("cph") String cph) {
        return toAjax(webSocketServer.removeVehiclePositionHistoryData(cph));
    }

    /**
     * 查询车辆轨迹缓存列表
     */
    @ApiOperation("查询车辆轨迹缓存列表")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:list')")
    @GetMapping("/trajectory/cache/{cph}")
    public TableDataInfo trajectoryCache(@PathVariable("cph") String cph) {
        // 获取分页参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        Collection<PathDetection> res = new ArrayList<>();
        if ("list".equals(cph)) {
            res = webSocketServer.vehicleCurrents.values();
        } else {
            Collection<String> keys = webSocketServer.vehicleCurrents.keySet()
                    .stream()
                    .filter(key -> key.toLowerCase().contains(cph.toLowerCase()))
                    .collect(Collectors.toList());
            for (String key : keys) {
                res.add(webSocketServer.vehicleCurrents.get(key));
            }
        }

        // 分页处理
        List<PathDetection> resList = new ArrayList<>(res);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, resList.size());

        if (startIndex >= resList.size()) {
            return getDataTable(new ArrayList<>());
        }

        List<PathDetection> paginatedList = resList.subList(startIndex, endIndex);
        return getDataTable(paginatedList);
    }

    /**
     * 修改摄像头识别日志
     */
    @ApiOperation("修改摄像头识别日志")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:edit')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsDeviceCameraLog wmsDeviceCameraLog) {
        return toAjax(wmsDeviceCameraLogService.updateWmsDeviceCameraLog(wmsDeviceCameraLog));
    }

    /**
     * 删除摄像头识别日志
     */
    @ApiOperation("删除摄像头识别日志")
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:remove')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wmsDeviceCameraLogService.deleteWmsDeviceCameraLogByIds(ids));
    }

    @ApiOperation("查询车辆历史轨迹")
    @PostMapping("/findVehicleHistoryList")
    public TableDataInfo findVehicleHistoryList(@RequestBody JSONObject jsonObject) {
        String beginTime = jsonObject.getString("beginTime");
        String endTime = jsonObject.getString("endTime");
        Date begin;
        Date end;
        String ymd;
        String cph = jsonObject.getString("cph");
        try {
            begin = DateUtils.parseDate(beginTime);
            end = DateUtils.parseDate(endTime);
            ymd = sdfTableSuffix.format(begin);
            if (!ymd.equals(sdfTableSuffix.format(end))) {
                throw new RuntimeException("日期必须在同一天");
            }
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误");
        }

        WmsVehiclePositionHistory wmsVehiclePositionHistory = new WmsVehiclePositionHistory();
        wmsVehiclePositionHistory.setYmd(ymd);
        wmsVehiclePositionHistory.setCph(cph);
        List<WmsVehiclePositionHistory> wmsVehiclePositionHistories = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(wmsVehiclePositionHistory);
        if (wmsVehiclePositionHistories.isEmpty()) {
            throw new RuntimeException("无此车辆历史轨迹");
        }

        WmsVehiclePositionHistory history = wmsVehiclePositionHistories.get(0);
        String[] logIds = history.getLogIds().split(",");

        List<WmsDeviceCameraLog> list = wmsDeviceCameraLogService.selectWmsDeviceCameraLogInId(logIds, begin, end);

        if (list.size() < 2) {
            throw new RuntimeException("无此车辆历史轨迹");
        }

        WmsDevice prev = wmsDeviceService.selectWmsDeviceByIp(list.get(0).getDwmc());
        JSONArray array = new JSONArray();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 1; i < list.size() - 1; i++) {
            WmsDevice next = wmsDeviceService.selectWmsDeviceByIp(list.get(i).getDwmc());
            List<String> path = findPath(prev.getDeviceName(), next.getDeviceName());
            for (int j = 0; j < path.size() - 1; j++) {
                String name1 = path.get(j);
                String name2 = path.get(j + 1);
                List<List<String>> wmsPathsDefinition = getWmsPathsDefinition(name1, name2);
                for (int k = 0; k < wmsPathsDefinition.get(0).size(); k++) {
                    JSONObject object = new JSONObject();
                    object.put("acceptTime", calendar.getTime());
                    object.put("cardId", cph);
                    object.put("personId", cph);
                    object.put("longitude", Double.parseDouble(wmsPathsDefinition.get(0).get(k)));
                    object.put("latitude", Double.parseDouble(wmsPathsDefinition.get(1).get(k)));
                    object.put("layerId", "全图");
                    object.put("layerHeight", 0);
                    object.put("prevTimeDifference", 0);
                    object.put("lineFlag", "beginToEnd");
                    object.put("createTime", calendar.getTime());
                    array.add(object);
                    // 每次循环后将start时间增加1秒
                    calendar.add(Calendar.SECOND, 1);
                }
            }
            prev = next;
        }

        return getDataTable(array);
    }

    /**
     * 192.168.10.162 入口
     * 192.168.10.163 出口
     * 192.168.10.171 出口摄像头
     *
     * @param name1
     * @param name2
     * @return
     */
    private List<List<String>> getWmsPathsDefinition(String name1, String name2) {

        List<String> longitudes = new ArrayList<>();
        List<String> latitudes = new ArrayList<>();

        // 查询虚拟路径定义表
        WmsPathsDefinetion wmsPathsDefinetion = new WmsPathsDefinetion();
        wmsPathsDefinetion.setFromName(name1);
        wmsPathsDefinetion.setToName(name2);
        List<WmsPathsDefinetion> forwardDefinition = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
        if (!forwardDefinition.isEmpty()) {
            // 取出虚拟路径定义的经纬度
            longitudes = Arrays.asList(forwardDefinition.get(0).getPathLongitude().split(","));
            latitudes = Arrays.asList(forwardDefinition.get(0).getPathLatitude().split(","));
        }
        wmsPathsDefinetion.setFromName(name2);
        wmsPathsDefinetion.setToName(name1);
        List<WmsPathsDefinetion> backwardDefinition = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
        if (!backwardDefinition.isEmpty()) {
            // 取出虚拟路径定义的经纬度
            longitudes = Arrays.asList(backwardDefinition.get(0).getPathLongitude().split(","));
            latitudes = Arrays.asList(backwardDefinition.get(0).getPathLatitude().split(","));
            Collections.reverse(longitudes);
            Collections.reverse(latitudes);
        }

        return Arrays.asList(longitudes, latitudes);
    }

    public static class PathDetection {
        /**
         * 车牌号
         */
        public String cph;
        /**
         * 路径开始时间
         */
        public Date begin;
        /**
         * 途经摄像头设备名称
         */
        public List<String> cameraNames = new ArrayList<>();
        /**
         * 途经摄像头设备IP
         */
        public List<String> cameraIps = new ArrayList<>();
        /**
         * 所有的路径点
         */
        public List<String> pathsAll = new ArrayList<>();
        /**
         * 未来的虚拟路径点， 目的是从最后一个摄像头设备开始，预估车辆会到达下一个摄像头设备的路径点， 给前端一个虚拟的展示， 防止车辆在地图上静止。
         */
        public List<String> futurePaths = new ArrayList<>();
        /**
         * 发送给前端用的虚拟路径点的数据结构体，表示最后一个虚拟的路径点数据
         */
        public WmsVehiclePositionCurrent futurePositionCurrent;
        /**
         * 发送给前端用的虚拟路径的经纬度坐标数组，提前预制好，带前端发送时到数组中取对应虚拟路径点的经纬度
         */
        private List<Double> futureLongitudes;
        /**
         * 发送给前端用的虚拟路径的经纬度坐标数组，提前预制好，带前端发送时到数组中取对应虚拟路径点的经纬度
         */
        private List<Double> futureLatitudes;
        /**
         * 车辆要走到下一个虚拟的摄像头设备需要花费的时间（秒）
         */
        private final int futureSeconds = 60;
        /**
         * 每秒生成多少个点
         */
        private final int futurePointsPerSecond = 10;
        /**
         * 生产的实际点数
         */
        private final int futurePoints = futureSeconds * futurePointsPerSecond;
        /**
         * 开始播放的时间
         */
        private final Date futureStartTime = new Date();
        /**
         * 结束播放的时间
         */
        private final Date futureEndTime;

        /**
         * 构造函数
         */
        public PathDetection() {
            this.futurePositionCurrent = new WmsVehiclePositionCurrent();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(futureStartTime);
            calendar.add(Calendar.SECOND, futureSeconds);
            this.futureEndTime = calendar.getTime();
        }

        /**
         * 获取下一个虚拟路径点数据
         *
         * @return 下一个虚拟路径点数据
         */
        public WmsVehiclePositionCurrent getNextPositionCurrent() {
            Date now = new Date();
            int pointNum = nextPoint();
            WmsVehiclePositionCurrent nextPositionCurrent = new WmsVehiclePositionCurrent();
            nextPositionCurrent.setAcceptTime(now);
            nextPositionCurrent.setRealName(cph);
            nextPositionCurrent.setLongitude(futureLongitudes.get(pointNum));
            nextPositionCurrent.setLatitude(futureLatitudes.get(pointNum));
            return nextPositionCurrent;
        }

        /**
         * 设置经纬度， 使用插值算法平滑路径点及调整路径点的点数
         *
         * @param longitude 经度，类型List<String>
         * @param latitude  纬度, 类型List<String>
         */
        public void setFutureLongitudeAndLatitude(List<String> longitude, List<String> latitude) {
            // 将输入的经纬度转换为double类型
            List<Double> longitudes = longitude.stream()
                    .filter(s -> !s.isEmpty())
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            // 将输入的纬度转换为double类型
            List<Double> latitudes = latitude.stream()
                    .filter(s -> !s.isEmpty())
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            // 拟合成this.futurePoints个点
            this.futureLongitudes = this.fitToPoints(longitudes, this.futurePoints);
            // 拟合成this.futurePoints个点
            this.futureLatitudes = this.fitToPoints(latitudes, this.futurePoints);
        }

        /**
         * 将输入的点序列拟合成指定数量的点
         *
         * @param input        输入点序列
         * @param targetPoints 目标点数
         * @return 拟合后的点序列
         */
        private List<Double> fitToPoints(List<Double> input, int targetPoints) {
            if (input.size() <= 1) {
                return input;
            }

            List<Double> result = new ArrayList<>();

            if (input.size() == targetPoints) {
                // 如果输入点数等于目标点数，直接返回
                return new ArrayList<>(input);
            } else if (input.size() < targetPoints) {
                // 如果输入点数少于目标点数，使用线性插值补充
                for (int i = 0; i < targetPoints; i++) {
                    double ratio = (double) i / (targetPoints - 1);
                    double value = interpolate(input, ratio);
                    result.add(value);
                }
            } else {
                // 如果输入点数多于目标点数，进行采样
                for (int i = 0; i < targetPoints; i++) {
                    double ratio = (double) i / (targetPoints - 1);
                    int index = (int) (ratio * (input.size() - 1));
                    result.add(input.get(index));
                }
            }

            return result;
        }

        /**
         * 线性插值计算
         *
         * @param values 点序列
         * @param ratio  插值比例 (0.0 - 1.0)
         * @return 插值结果
         */
        private double interpolate(List<Double> values, double ratio) {
            if (values.size() <= 1) {
                return values.isEmpty() ? 0.0 : values.get(0);
            }

            double targetIndex = ratio * (values.size() - 1);
            int lowerIndex = (int) Math.floor(targetIndex);
            int upperIndex = (int) Math.ceil(targetIndex);

            if (lowerIndex >= values.size() - 1) {
                return values.get(values.size() - 1);
            }

            if (upperIndex <= 0) {
                return values.get(0);
            }

            double fraction = targetIndex - lowerIndex;
            double lowerValue = values.get(lowerIndex);
            double upperValue = values.get(upperIndex);

            return lowerValue + fraction * (upperValue - lowerValue);
        }

        /**
         * 获取下一个虚拟路径点在路径中的位置（index）
         *
         * @return 下一个虚拟路径点index
         */
        private int nextPoint() {
            Date nextDate = new Date();
            // 计算nextDate在startTime和endTime之间的比例
            long timeDiff = futureEndTime.getTime() - futureStartTime.getTime();
            long nextTimeDiff = nextDate.getTime() - futureStartTime.getTime();

            // 计算比例（0.0到1.0之间）
            double ratio = (double) nextTimeDiff / timeDiff;

            // 确保比例在0.0到1.0之间
            ratio = Math.max(0.0, Math.min(1.0, ratio));

            // 根据比例计算pointNum
            // 当前点数
            int pointNum = (int) (ratio * (futurePoints - 1));

            // 确保pointNum在有效范围内
            pointNum = Math.max(0, Math.min(futurePoints - 1, pointNum));
            return pointNum;
        }
    }

    /**
     * 根据起点、终点，寻找最短路径
     *
     * @param startNode 开始摄像头设备名称
     * @param endNode   结束摄像头设备名称
     * @return
     */
    public List<String> findPath(String startNode, String endNode) {
        // 使用 Dijkstra 算法寻找最短路径
        DijkstraShortestPath<String, DefaultEdge> dijkstra = new DijkstraShortestPath<>(mapGraph);
        GraphPath<String, DefaultEdge> path = dijkstra.getPath(startNode, endNode);

        if (path != null) {
            // 如果能联通，返回连续的点列表
            return path.getVertexList();
        } else {
            // 无法联通
            return new ArrayList<>();
        }
    }

    /**
     * 定时任务，清除车辆当前位置缓存
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void clearVehicleCurrents() {
        for (String cph : webSocketServer.vehicleCurrents.keySet()) {
            completePosition(cph, new Date());
        }
    }

    public void completePosition(String cph, Date endTime) {
        PathDetection pathDetection = webSocketServer.vehicleCurrents.remove(cph);
        WmsTrajectory wmsTrajectory = new WmsTrajectory();
        wmsTrajectory.setTrajectoryType("车辆");
        wmsTrajectory.setFuzzy(cph + "-" + pathDetection.begin + "-外部车辆");
        wmsTrajectory.setTrajectoryBegin(pathDetection.begin);
        wmsTrajectory.setTrajectoryEnd(endTime);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < pathDetection.pathsAll.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            String[] s = pathDetection.pathsAll.get(i).split(",");
            jsonObject.put("longitude", s[0]);
            jsonObject.put("latitude", s[1]);
            jsonArray.add(jsonObject);
        }
        wmsTrajectory.setTrajectoryPoints(jsonArray.toJSONString());
        wmsTrajectoryService.insertWmsTrajectory(wmsTrajectory);
    }

    /**
     * 最长公共子序列算法计算匹配字符数
     */
    private int longestCommonSubsequenceLength(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    /**
     * 查找最佳匹配的车牌号，并考虑匹配质量和相似度
     *
     * @param log 输入的车牌号字符串
     * @return 匹配的键值对，如果没有合适的匹配则返回 null
     */
    private WmsDeviceCameraLog findBestMatchingPlateWithQuality(WmsDeviceCameraLog log) {
        if (log.getCph() == null || entrancePlateNumbers.isEmpty()) {
            return null;
        }

        Map.Entry<String, WmsDeviceCameraLog> bestMatch = null;
        int maxMatchCount = 0;
        double bestMatchRatio = 0.0;

        for (Map.Entry<String, WmsDeviceCameraLog> entry : entrancePlateNumbers.entrySet()) {
            int matchCount = longestCommonSubsequenceLength(log.getCph(), entry.getKey());

            // 计算匹配率（避免短字符串的高匹配率问题）
            double matchRatio = (double) matchCount / Math.max(log.getCph().length(), entry.getKey().length());

            // 如果匹配数更高，或者匹配数相同但匹配率更高的情况下更新最佳匹配
            if (matchCount > maxMatchCount || (matchCount == maxMatchCount && matchRatio > bestMatchRatio)) {
                maxMatchCount = matchCount;
                bestMatchRatio = matchRatio;
                bestMatch = entry;
            }
        }

        // 只有在匹配数达到一定阈值时才返回匹配结果
        if (bestMatch == null || maxMatchCount < 4) {  // 至少匹配5个字符
            return null;
        }

        return bestMatch.getValue();
    }
}
