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
import com.ruoyi.system.domain.CoreAlarm;
import com.ruoyi.system.service.ICoreAlarmService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报警记录Controller
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
@RestController
@RequestMapping("/lanya_core_alarm/alarm")
public class CoreAlarmController extends BaseController
{
    @Autowired
    private ICoreAlarmService coreAlarmService;

    /**
     * 查询报警记录列表
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(CoreAlarm coreAlarm)
    {
        startPage();
        List<CoreAlarm> list = coreAlarmService.selectCoreAlarmList(coreAlarm);
        return getDataTable(list);
    }

    /**
     * 导出报警记录列表
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:export')")
    @Log(title = "报警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoreAlarm coreAlarm)
    {
        List<CoreAlarm> list = coreAlarmService.selectCoreAlarmList(coreAlarm);
        ExcelUtil<CoreAlarm> util = new ExcelUtil<CoreAlarm>(CoreAlarm.class);
        util.exportExcel(response, list, "报警记录数据");
    }

    /**
     * 获取报警记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:query')")
    @GetMapping(value = "/{alarmId}")
    public AjaxResult getInfo(@PathVariable("alarmId") Long alarmId)
    {
        return success(coreAlarmService.selectCoreAlarmByAlarmId(alarmId));
    }

    /**
     * 新增报警记录
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:add')")
    @Log(title = "报警记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CoreAlarm coreAlarm)
    {
        return toAjax(coreAlarmService.insertCoreAlarm(coreAlarm));
    }

    /**
     * 修改报警记录
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:edit')")
    @Log(title = "报警记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CoreAlarm coreAlarm)
    {
        return toAjax(coreAlarmService.updateCoreAlarm(coreAlarm));
    }

    /**
     * 删除报警记录
     */
    @PreAuthorize("@ss.hasPermi('lanya_core_alarm:alarm:remove')")
    @Log(title = "报警记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alarmIds}")
    public AjaxResult remove(@PathVariable Long[] alarmIds)
    {
        return toAjax(coreAlarmService.deleteCoreAlarmByAlarmIds(alarmIds));
    }
}
