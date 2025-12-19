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
        String ymd = sdfTableSuffix.format(new Date());
        WmsVehiclePositionHistory query = new WmsVehiclePositionHistory();
        query.setYmd(sdfTableSuffix.format(new Date()));
        query.setCph(wmsDeviceCameraLog.getCph());
        List<WmsVehiclePositionHistory> wmsVehiclePositionHistories = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(query);
        if (wmsVehiclePositionHistories.isEmpty()) {
            WmsVehiclePositionHistory wmsVehiclePositionHistory = new WmsVehiclePositionHistory();
            wmsVehiclePositionHistory.setCph(wmsDeviceCameraLog.getCph());
            wmsVehiclePositionHistory.setYmd(ymd);
            wmsVehiclePositionHistory.setKssj(new Date());
            wmsVehiclePositionHistory.setLogIds(wmsDeviceCameraLog.getId().toString());
            wmsVehiclePositionHistoryService.insertWmsVehiclePositionHistory(wmsVehiclePositionHistory);
        } else {
            WmsVehiclePositionHistory wmsVehiclePositionHistory = wmsVehiclePositionHistories.get(0);
            // 修正：使用可修改的 ArrayList 替代 Arrays.asList()
            List<String> logIds = new ArrayList<>(Arrays.asList(wmsVehiclePositionHistory.getLogIds().split(",")));
            WmsDeviceCameraLog oldLog = wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(Long.parseLong(logIds.get(logIds.size() - 1)));
            if (!oldLog.getDwmc().equals(wmsDeviceCameraLog.getDwmc())) {
                WmsPathsDefinetion wmsPathsDefinetion = new WmsPathsDefinetion();
                wmsPathsDefinetion.setFromIp(oldLog.getDwmc());
                wmsPathsDefinetion.setToIp(oldLog.getDwmc());
                List<WmsPathsDefinetion> wmsPathsDefinetions = wmsPathsDefinetionService.selectWmsPathsDefinetionList(wmsPathsDefinetion);
                if (!wmsPathsDefinetions.isEmpty()) {
                    List<String> longitudes = Arrays.asList(wmsPathsDefinetions.get(0).getPathLongitude().split(","));
                    List<String> latitudes = Arrays.asList(wmsPathsDefinetions.get(0).getPathLatitude().split(","));
                    // 修正：使用可修改的 ArrayList 并添加空值检查
                    String pointsStr = wmsVehiclePositionHistory.getPoints();
                    List<String> points = pointsStr != null && !pointsStr.isEmpty() ?
                            new ArrayList<>(Arrays.asList(pointsStr.split(";"))) : new ArrayList<>();
                    for (int i = 0; i < longitudes.size(); i++) {
                        points.add(longitudes.get(i) + "," + latitudes.get(i));
                    }
                    wmsVehiclePositionHistory.setPoints(String.join(";", points));
                }
                // 修正：现在可以安全地调用 add 方法
                logIds.add(wmsDeviceCameraLog.getId().toString());
                wmsVehiclePositionHistory.setLogIds(String.join(",", logIds));
                wmsVehiclePositionHistory.setJssj(new Date());

                wmsVehiclePositionHistoryService.updateWmsVehiclePositionHistory(wmsVehiclePositionHistory);
            }
        }
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
