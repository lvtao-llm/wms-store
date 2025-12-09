package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.system.domain.WmsAlarmLog;
import com.ruoyi.system.service.IWmsAlarmLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报警信息记录Controller
 *
 * @author ruoyi
 * @date 2025-10-17
 */
@RestController
@RequestMapping("/system/wms_alarm_log")
public class WmsAlarmLogController extends BaseController {
    @Autowired
    private IWmsAlarmLogService wmsAlarmLogService;

    /**
     * 查询报警信息记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsAlarmLog wmsAlarmLog) {
        startPage();
        List<WmsAlarmLog> list = wmsAlarmLogService.selectWmsAlarmLogList(wmsAlarmLog);
        return getDataTable(list);
    }

    /**
     * 查询报警信息记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:unread')")
    @GetMapping("/unread")
    public TableDataInfo unread() {
        startPage();
        WmsAlarmLog wmsAlarmLog = new WmsAlarmLog();
        wmsAlarmLog.setReadFlag("0");
        List<WmsAlarmLog> list = wmsAlarmLogService.selectWmsAlarmLogList(wmsAlarmLog);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:read')")
    @GetMapping("/read/{alarmIds}")
    public AjaxResult read(@PathVariable Long[] alarmIds) {
        return toAjax(wmsAlarmLogService.readWmsAlarmLogByAlarmIds(alarmIds));
    }

    /**
     * 导出报警信息记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:export')")
    @Log(title = "报警信息记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsAlarmLog wmsAlarmLog) {
        List<WmsAlarmLog> list = wmsAlarmLogService.selectWmsAlarmLogList(wmsAlarmLog);
        ExcelUtil<WmsAlarmLog> util = new ExcelUtil<WmsAlarmLog>(WmsAlarmLog.class);
        util.exportExcel(response, list, "报警信息记录数据");
    }

    /**
     * 获取报警信息记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:query')")
    @GetMapping(value = "/{alarmId}")
    public AjaxResult getInfo(@PathVariable("alarmId") Long alarmId) {
        return success(wmsAlarmLogService.selectWmsAlarmLogByAlarmId(alarmId));
    }

    /**
     * 新增报警信息记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:add')")
    @Log(title = "报警信息记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsAlarmLog wmsAlarmLog) {
        return toAjax(wmsAlarmLogService.insertWmsAlarmLog(wmsAlarmLog));
    }

    /**
     * 修改报警信息记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:edit')")
    @Log(title = "报警信息记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsAlarmLog wmsAlarmLog) {
        wmsAlarmLog.setAlarmHandleTime(new Date());
        return toAjax(wmsAlarmLogService.updateWmsAlarmLog(wmsAlarmLog));
    }

    /**
     * 删除报警信息记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_log:remove')")
    @Log(title = "报警信息记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{alarmIds}")
    public AjaxResult remove(@PathVariable Long[] alarmIds) {
        return toAjax(wmsAlarmLogService.deleteWmsAlarmLogByAlarmIds(alarmIds));
    }
}
