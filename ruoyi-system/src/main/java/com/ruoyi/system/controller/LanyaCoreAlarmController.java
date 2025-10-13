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
import com.ruoyi.system.domain.LanyaCoreAlarm;
import com.ruoyi.system.service.ILanyaCoreAlarmService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报警记录Controller
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_core_alarm")
@DataSource(DataSourceType.SLAVE)
public class LanyaCoreAlarmController extends BaseController
{
    @Autowired
    private ILanyaCoreAlarmService lanyaCoreAlarmService;

    /**
     * 查询报警记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaCoreAlarm lanyaCoreAlarm)
    {
        startPage();
        List<LanyaCoreAlarm> list = lanyaCoreAlarmService.selectLanyaCoreAlarmList(lanyaCoreAlarm);
        return getDataTable(list);
    }

    /**
     * 导出报警记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:export')")
    @Log(title = "报警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaCoreAlarm lanyaCoreAlarm)
    {
        List<LanyaCoreAlarm> list = lanyaCoreAlarmService.selectLanyaCoreAlarmList(lanyaCoreAlarm);
        ExcelUtil<LanyaCoreAlarm> util = new ExcelUtil<LanyaCoreAlarm>(LanyaCoreAlarm.class);
        util.exportExcel(response, list, "报警记录数据");
    }

    /**
     * 获取报警记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:query')")
    @GetMapping(value = "/{alarmId}")
    public AjaxResult getInfo(@PathVariable("alarmId") Long alarmId)
    {
        return success(lanyaCoreAlarmService.selectLanyaCoreAlarmByAlarmId(alarmId));
    }

    /**
     * 新增报警记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:add')")
    @Log(title = "报警记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaCoreAlarm lanyaCoreAlarm)
    {
        return toAjax(lanyaCoreAlarmService.insertLanyaCoreAlarm(lanyaCoreAlarm));
    }

    /**
     * 修改报警记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:edit')")
    @Log(title = "报警记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaCoreAlarm lanyaCoreAlarm)
    {
        return toAjax(lanyaCoreAlarmService.updateLanyaCoreAlarm(lanyaCoreAlarm));
    }

    /**
     * 删除报警记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm:remove')")
    @Log(title = "报警记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alarmIds}")
    public AjaxResult remove(@PathVariable Long[] alarmIds)
    {
        return toAjax(lanyaCoreAlarmService.deleteLanyaCoreAlarmByAlarmIds(alarmIds));
    }
}
