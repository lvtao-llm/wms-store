package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
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
import com.ruoyi.system.domain.LanyaDeviceCardSenderVehicleLog;
import com.ruoyi.system.service.ILanyaDeviceCardSenderVehicleLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆发卡记录Controller
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_device_card_sender_vehicle_log")
@DataSource(DataSourceType.SLAVE)
public class LanyaDeviceCardSenderVehicleLogController extends BaseController {
    @Autowired
    private ILanyaDeviceCardSenderVehicleLogService lanyaDeviceCardSenderVehicleLogService;

    /**
     * 查询车辆发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog) {
        startPage();
        List<LanyaDeviceCardSenderVehicleLog> list = lanyaDeviceCardSenderVehicleLogService.selectLanyaDeviceCardSenderVehicleLogList(lanyaDeviceCardSenderVehicleLog);
        return getDataTable(list);
    }

    /**
     * 导出车辆发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:export')")
    @Log(title = "车辆发卡记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog) {
        List<LanyaDeviceCardSenderVehicleLog> list = lanyaDeviceCardSenderVehicleLogService.selectLanyaDeviceCardSenderVehicleLogList(lanyaDeviceCardSenderVehicleLog);
        ExcelUtil<LanyaDeviceCardSenderVehicleLog> util = new ExcelUtil<LanyaDeviceCardSenderVehicleLog>(LanyaDeviceCardSenderVehicleLog.class);
        util.exportExcel(response, list, "车辆发卡记录数据");
    }

    /**
     * 获取车辆发卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lanyaDeviceCardSenderVehicleLogService.selectLanyaDeviceCardSenderVehicleLogById(id));
    }

    /**
     * 新增车辆发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:add')")
    @Log(title = "车辆发卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog) {
        return toAjax(lanyaDeviceCardSenderVehicleLogService.insertLanyaDeviceCardSenderVehicleLog(lanyaDeviceCardSenderVehicleLog));
    }

    /**
     * 修改车辆发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:edit')")
    @Log(title = "车辆发卡记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog) {
        return toAjax(lanyaDeviceCardSenderVehicleLogService.updateLanyaDeviceCardSenderVehicleLog(lanyaDeviceCardSenderVehicleLog));
    }

    /**
     * 删除车辆发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_vehicle_log:remove')")
    @Log(title = "车辆发卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lanyaDeviceCardSenderVehicleLogService.deleteLanyaDeviceCardSenderVehicleLogByIds(ids));
    }
}
