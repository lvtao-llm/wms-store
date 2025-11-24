package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.WmsSystemLog;
import com.ruoyi.system.service.IWmsSystemLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 定时任务调度日志Controller
 * 
 * @author ruoyi
 * @date 2025-11-24
 */
@RestController
@RequestMapping("/system/wms_system_log")
public class WmsSystemLogController extends BaseController
{
    @Autowired
    private IWmsSystemLogService wmsSystemLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsSystemLog wmsSystemLog)
    {
        startPage();
        List<WmsSystemLog> list = wmsSystemLogService.selectWmsSystemLogList(wmsSystemLog);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:export')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsSystemLog wmsSystemLog)
    {
        List<WmsSystemLog> list = wmsSystemLogService.selectWmsSystemLogList(wmsSystemLog);
        ExcelUtil<WmsSystemLog> util = new ExcelUtil<WmsSystemLog>(WmsSystemLog.class);
        util.exportExcel(response, list, "定时任务调度日志数据");
    }

    /**
     * 获取定时任务调度日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:query')")
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable("jobLogId") Long jobLogId)
    {
        return success(wmsSystemLogService.selectWmsSystemLogByJobLogId(jobLogId));
    }

    /**
     * 新增定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:add')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsSystemLog wmsSystemLog)
    {
        return toAjax(wmsSystemLogService.insertWmsSystemLog(wmsSystemLog));
    }

    /**
     * 修改定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:edit')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsSystemLog wmsSystemLog)
    {
        return toAjax(wmsSystemLogService.updateWmsSystemLog(wmsSystemLog));
    }

    /**
     * 删除定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_system_log:remove')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable Long[] jobLogIds)
    {
        return toAjax(wmsSystemLogService.deleteWmsSystemLogByJobLogIds(jobLogIds));
    }
}
