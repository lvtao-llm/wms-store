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
import com.ruoyi.system.domain.WmsAlarm;
import com.ruoyi.system.service.IWmsAlarmService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报警信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/system/alarm")
public class WmsAlarmController extends BaseController
{
    @Autowired
    private IWmsAlarmService wmsAlarmService;

    /**
     * 查询报警信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsAlarm wmsAlarm)
    {
        startPage();
        List<WmsAlarm> list = wmsAlarmService.selectWmsAlarmList(wmsAlarm);
        return getDataTable(list);
    }

    /**
     * 导出报警信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:export')")
    @Log(title = "报警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsAlarm wmsAlarm)
    {
        List<WmsAlarm> list = wmsAlarmService.selectWmsAlarmList(wmsAlarm);
        ExcelUtil<WmsAlarm> util = new ExcelUtil<WmsAlarm>(WmsAlarm.class);
        util.exportExcel(response, list, "报警信息数据");
    }

    /**
     * 获取报警信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:query')")
    @GetMapping(value = "/{alarmId}")
    public AjaxResult getInfo(@PathVariable("alarmId") Long alarmId)
    {
        return success(wmsAlarmService.selectWmsAlarmByAlarmId(alarmId));
    }

    /**
     * 新增报警信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:add')")
    @Log(title = "报警信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsAlarm wmsAlarm)
    {
        return toAjax(wmsAlarmService.insertWmsAlarm(wmsAlarm));
    }

    /**
     * 修改报警信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:edit')")
    @Log(title = "报警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsAlarm wmsAlarm)
    {
        return toAjax(wmsAlarmService.updateWmsAlarm(wmsAlarm));
    }

    /**
     * 删除报警信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarm:remove')")
    @Log(title = "报警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alarmIds}")
    public AjaxResult remove(@PathVariable Long[] alarmIds)
    {
        return toAjax(wmsAlarmService.deleteWmsAlarmByAlarmIds(alarmIds));
    }
}
