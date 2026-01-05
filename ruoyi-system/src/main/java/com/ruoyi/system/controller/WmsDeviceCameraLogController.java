package com.ruoyi.system.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.IWmsDeviceService;
import com.ruoyi.system.service.IWmsPathsDefinetionService;
import com.ruoyi.system.service.IWmsVehiclePositionHistoryService;
import com.ruoyi.system.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
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
import com.ruoyi.system.service.IWmsDeviceCameraLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

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

    SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");

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
        // 插入新的wms_device_camera_log
        int newLog = wmsDeviceCameraLogService.insertWmsDeviceCameraLog(currLog);

        // 按天创建一个车牌号的虚拟路径

        // 当前天的时间字符串
        String ymd = sdfTableSuffix.format(new Date());

        // 查询当前天和车牌号的记录
        WmsVehiclePositionHistory query = new WmsVehiclePositionHistory();
        query.setYmd(sdfTableSuffix.format(new Date()));
        query.setCph(currLog.getCph());
        List<WmsVehiclePositionHistory> res = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(query);

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

            // 今天的记录
            WmsVehiclePositionHistory positionHistory = res.get(0);
            // 今天记录中记录的wms_device_camera_log的ID
            List<String> logIds = new ArrayList<>(Arrays.asList(positionHistory.getLogIds().split(",")));
            // 最后一个wms_device_camera_log
            WmsDeviceCameraLog prevLog = wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(Long.parseLong(logIds.get(logIds.size() - 1)));

            WmsDevice prevDevice = wmsDeviceService.selectWmsDeviceByIp(prevLog.getDwmc());
            WmsDevice currDevice = wmsDeviceService.selectWmsDeviceByIp(currLog.getDwmc());

            // 车牌机端会发送在同一车牌机会发送重复的识别记录，所以最后一个识别记录的IP和当前识别记录的IP不一致，则添加。如果相同和忽略
            if (prevDevice != null && currDevice != null && !prevDevice.getDeviceName().equals(currDevice.getDeviceName())) {

                // 获取当前记录的经纬度
                String pointsStr = positionHistory.getPoints();
                // 转成List
                List<String> points = pointsStr != null && !pointsStr.isEmpty() ? new ArrayList<>(Arrays.asList(pointsStr.split(";"))) : new ArrayList<>();

                PathDetection pathDetection = getWmsPathsDefinition(prevDevice.getDeviceName(), currDevice.getDeviceName());
                pathDetection.cph = currLog.getCph();
                points.addAll(pathDetection.paths);
                // 重新赋值虚拟路径点
                positionHistory.setPoints(String.join(";", points));

                // 添加wms_device_camera_log的ID
                logIds.add(currLog.getId().toString());
                positionHistory.setLogIds(String.join(",", logIds));

                // 修改结束时间
                positionHistory.setJssj(new Date());

                // 更新车辆路径点历史记录
                wmsVehiclePositionHistoryService.updateWmsVehiclePositionHistory(positionHistory);

                // 更新车辆路径点数据给前端浏览器
                webSocketServer.updateVehiclePositionHistoryData(pathDetection);
            }
        }

        return toAjax(newLog);
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

    private PathDetection getWmsPathsDefinition(String name1, String name2) {

        PathDetection pathDetection = new PathDetection();
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
            pathDetection.fromName = forwardDefinition.get(0).getFromName();
            pathDetection.fromIp = forwardDefinition.get(0).getFromIp();
            pathDetection.toName = forwardDefinition.get(0).getToName();
            pathDetection.toIp = forwardDefinition.get(0).getToIp();
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
            pathDetection.fromName = backwardDefinition.get(0).getToName();
            pathDetection.fromIp = backwardDefinition.get(0).getToIp();
            pathDetection.toName = backwardDefinition.get(0).getFromName();
            pathDetection.toIp = backwardDefinition.get(0).getFromIp();
        }

        pathDetection.setLongitudeAndLatitude(longitudes, latitudes);
        // 添加虚拟路径点
        for (int i = 0; i < longitudes.size(); i++) {
            if (!Strings.isEmpty(longitudes.get(i)) && !Strings.isEmpty(latitudes.get(i))) {
                pathDetection.paths.add(longitudes.get(i) + "," + latitudes.get(i));
            }
        }

        return pathDetection;
    }

    public static class PathDetection {
        public String cph;
        public String fromName;
        public String toName;
        public String fromIp;
        public String toIp;
        public List<String> paths = new ArrayList<>();
        public WmsVehiclePositionCurrent positionCurrent;
        // 经度
        private List<Double> longitudes;
        // 纬度
        private List<Double> latitudes;
        // 秒数
        private final int seconds = 60;
        // 每秒点数
        private final int pointsPerSecond = 10;
        // 生产的点数
        private final int points = seconds * pointsPerSecond;
        // 开始时间
        private final Date startTime = new Date();
        // 结束时间
        private final Date endTime;

        public PathDetection() {
            this.positionCurrent = new WmsVehiclePositionCurrent();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.SECOND, seconds);
            this.endTime = calendar.getTime();
        }

        public WmsVehiclePositionCurrent getNextPositionCurrent(String cph) {
            Date now = new Date();
            int pointNum = nextPoint();
            WmsVehiclePositionCurrent nextPositionCurrent = new WmsVehiclePositionCurrent();
            nextPositionCurrent.setAcceptTime(now);
            nextPositionCurrent.setRealName(cph);
            nextPositionCurrent.setLongitude(longitudes.get(pointNum));
            nextPositionCurrent.setLatitude(latitudes.get(pointNum));
            return nextPositionCurrent;
        }

        /**
         * 设置经纬度
         *
         * @param longitude 经度，类型List<String>
         * @param latitude  纬度, 类型List<String>
         */
        public void setLongitudeAndLatitude(List<String> longitude, List<String> latitude) {
            List<Double> longitudes = longitude.stream()
                    .filter(s -> !s.isEmpty())
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());

            List<Double> latitudes = latitude.stream()
                    .filter(s -> !s.isEmpty())
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            // 拟合成100个点
            this.longitudes = this.fitToPoints(longitudes, this.points);
            this.latitudes = this.fitToPoints(latitudes, this.points);
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

        private int nextPoint() {
            Date nextDate = new Date();
            // 计算nextDate在startTime和endTime之间的比例
            long timeDiff = endTime.getTime() - startTime.getTime();
            long nextTimeDiff = nextDate.getTime() - startTime.getTime();

            // 计算比例（0.0到1.0之间）
            double ratio = (double) nextTimeDiff / timeDiff;

            // 确保比例在0.0到1.0之间
            ratio = Math.max(0.0, Math.min(1.0, ratio));

            // 根据比例计算pointNum
            // 当前点数
            int pointNum = (int) (ratio * (points - 1));

            // 确保pointNum在有效范围内
            pointNum = Math.max(0, Math.min(points - 1, pointNum));
            return pointNum;
        }
    }


}
