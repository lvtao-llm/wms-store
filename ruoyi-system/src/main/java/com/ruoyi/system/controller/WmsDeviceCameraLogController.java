package com.ruoyi.system.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.domain.WmsPathsDefinetion;
import com.ruoyi.system.domain.WmsVehiclePositionHistory;
import com.ruoyi.system.service.IWmsDeviceService;
import com.ruoyi.system.service.IWmsPathsDefinetionService;
import com.ruoyi.system.service.IWmsVehiclePositionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.system.domain.WmsDeviceCameraLog;
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

    private static Map<String, String> ipMapDeviceName = new HashMap<>();

    SimpleDateFormat sdfTableSuffix = new SimpleDateFormat("yyyyMMdd");

    @PostConstruct
    public void init() {
        WmsDevice wmsDevice = new WmsDevice();
        wmsDevice.setDeviceType("摄像头");
        List<WmsDevice> wmsDevices = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        for (WmsDevice wmsDevice1 : wmsDevices) {
            String data = wmsDevice1.getData();
            JSONObject jsonObject = JSONObject.parseObject(data);
            if (jsonObject.containsKey("ip1")) {
                ipMapDeviceName.put(jsonObject.getString("ip1"), wmsDevice1.getDeviceName());
            }
            if (jsonObject.containsKey("ip2")) {
                ipMapDeviceName.put(jsonObject.getString("ip2"), wmsDevice1.getDeviceName());
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
    public AjaxResult add(@RequestBody WmsDeviceCameraLog wmsDeviceCameraLog) {
        // 按天创建一个车牌号的虚拟路径

        // 当前天的时间字符串
        String ymd = sdfTableSuffix.format(new Date());
        // 查询当前天和车牌号的记录
        WmsVehiclePositionHistory query = new WmsVehiclePositionHistory();
        query.setYmd(sdfTableSuffix.format(new Date()));
        query.setCph(wmsDeviceCameraLog.getCph());
        List<WmsVehiclePositionHistory> wmsVehiclePositionHistories = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(query);

        // 判断车牌号今天是否有记录
        if (wmsVehiclePositionHistories.isEmpty()) {
            // 今天没有记录，新增
            WmsVehiclePositionHistory wmsVehiclePositionHistory = new WmsVehiclePositionHistory();
            wmsVehiclePositionHistory.setCph(wmsDeviceCameraLog.getCph());
            wmsVehiclePositionHistory.setYmd(ymd);
            wmsVehiclePositionHistory.setKssj(new Date());
            wmsVehiclePositionHistory.setLogIds(wmsDeviceCameraLog.getId().toString());
            wmsVehiclePositionHistoryService.insertWmsVehiclePositionHistory(wmsVehiclePositionHistory);
        } else {
            // 今天有记录，更新

            // 今天的记录
            WmsVehiclePositionHistory wmsVehiclePositionHistory = wmsVehiclePositionHistories.get(0);
            // 今天记录中记录的wms_device_camera_log的ID
            List<String> logIds = new ArrayList<>(Arrays.asList(wmsVehiclePositionHistory.getLogIds().split(",")));
            // 最后一个wms_device_camera_log
            WmsDeviceCameraLog oldLog = wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(Long.parseLong(logIds.get(logIds.size() - 1)));

            // 车牌机端会发送在同一车牌机会发送重复的识别记录，所以最后一个识别记录的IP和当前识别记录的IP不一致，则添加。如果相同和忽略
            if (!oldLog.getDwmc().equals(wmsDeviceCameraLog.getDwmc())) {

                // 查询虚拟路径定义表
                WmsPathsDefinetion wmsPathsDefinetion = new WmsPathsDefinetion();
                wmsPathsDefinetion.setFromIp(oldLog.getDwmc());
                wmsPathsDefinetion.setToIp(oldLog.getDwmc());
                List<WmsPathsDefinetion> wmsPathsDefinetions = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);

                // 如果存在虚拟路径定义，则添加虚拟路径点
                if (!wmsPathsDefinetions.isEmpty()) {

                    // 取出虚拟路径定义的经纬度
                    List<String> longitudes = Arrays.asList(wmsPathsDefinetions.get(0).getPathLongitude().split(","));
                    List<String> latitudes = Arrays.asList(wmsPathsDefinetions.get(0).getPathLatitude().split(","));

                    // 获取当前记录的经纬度
                    String pointsStr = wmsVehiclePositionHistory.getPoints();
                    // 转成List
                    List<String> points = pointsStr != null && !pointsStr.isEmpty() ? new ArrayList<>(Arrays.asList(pointsStr.split(";"))) : new ArrayList<>();

                    // 添加虚拟路径点
                    for (int i = 0; i < longitudes.size(); i++) {
                        points.add(longitudes.get(i) + "," + latitudes.get(i));
                    }

                    // 重新赋值虚拟路径点
                    wmsVehiclePositionHistory.setPoints(String.join(";", points));
                }

                // 添加wms_device_camera_log的ID
                logIds.add(wmsDeviceCameraLog.getId().toString());
                wmsVehiclePositionHistory.setLogIds(String.join(",", logIds));

                // 修改结束时间
                wmsVehiclePositionHistory.setJssj(new Date());

                // 更新车辆路径点历史记录
                wmsVehiclePositionHistoryService.updateWmsVehiclePositionHistory(wmsVehiclePositionHistory);
            }
        }

        // 插入新的wms_device_camera_log
        return toAjax(wmsDeviceCameraLogService.insertWmsDeviceCameraLog(wmsDeviceCameraLog));
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
}
