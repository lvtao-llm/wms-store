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
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.service.IWmsAlarmRuleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 报警信息规则Controller
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
@RestController
@RequestMapping("/system/wms_alarm_rule")
public class WmsAlarmRuleController extends BaseController
{
    @Autowired
    private IWmsAlarmRuleService wmsAlarmRuleService;

    /**
     * 查询报警信息规则列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsAlarmRule wmsAlarmRule)
    {
        startPage();
        List<WmsAlarmRule> list = wmsAlarmRuleService.selectWmsAlarmRuleList(wmsAlarmRule);
        return getDataTable(list);
    }

    /**
     * 导出报警信息规则列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:export')")
    @Log(title = "报警信息规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsAlarmRule wmsAlarmRule)
    {
        List<WmsAlarmRule> list = wmsAlarmRuleService.selectWmsAlarmRuleList(wmsAlarmRule);
        ExcelUtil<WmsAlarmRule> util = new ExcelUtil<WmsAlarmRule>(WmsAlarmRule.class);
        util.exportExcel(response, list, "报警信息规则数据");
    }

    /**
     * 获取报警信息规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:query')")
    @GetMapping(value = "/{alarmRuleId}")
    public AjaxResult getInfo(@PathVariable("alarmRuleId") Long alarmRuleId)
    {
        return success(wmsAlarmRuleService.selectWmsAlarmRuleByAlarmRuleId(alarmRuleId));
    }

    /**
     * 新增报警信息规则
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:add')")
    @Log(title = "报警信息规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsAlarmRule wmsAlarmRule)
    {
        return toAjax(wmsAlarmRuleService.insertWmsAlarmRule(wmsAlarmRule));
    }

    /**
     * 修改报警信息规则
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:edit')")
    @Log(title = "报警信息规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsAlarmRule wmsAlarmRule)
    {
        return toAjax(wmsAlarmRuleService.updateWmsAlarmRule(wmsAlarmRule));
    }

    /**
     * 删除报警信息规则
     */
    @PreAuthorize("@ss.hasPermi('system:wms_alarm_rule:remove')")
    @Log(title = "报警信息规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alarmRuleIds}")
    public AjaxResult remove(@PathVariable Long[] alarmRuleIds)
    {
        return toAjax(wmsAlarmRuleService.deleteWmsAlarmRuleByAlarmRuleIds(alarmRuleIds));
    }
}
