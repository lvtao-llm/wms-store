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
import com.ruoyi.system.domain.WmsInspectionRule;
import com.ruoyi.system.service.IWmsInspectionRuleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡检规则Controller
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
@RestController
@RequestMapping("/system/wms_inspection_rule")
public class WmsInspectionRuleController extends BaseController
{
    @Autowired
    private IWmsInspectionRuleService wmsInspectionRuleService;

    /**
     * 查询巡检规则列表
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsInspectionRule wmsInspectionRule)
    {
        startPage();
        List<WmsInspectionRule> list = wmsInspectionRuleService.selectWmsInspectionRuleList(wmsInspectionRule);
        return getDataTable(list);
    }

    /**
     * 导出巡检规则列表
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:export')")
    @Log(title = "巡检规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInspectionRule wmsInspectionRule)
    {
        List<WmsInspectionRule> list = wmsInspectionRuleService.selectWmsInspectionRuleList(wmsInspectionRule);
        ExcelUtil<WmsInspectionRule> util = new ExcelUtil<WmsInspectionRule>(WmsInspectionRule.class);
        util.exportExcel(response, list, "巡检规则数据");
    }

    /**
     * 获取巡检规则详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsInspectionRuleService.selectWmsInspectionRuleById(id));
    }

    /**
     * 新增巡检规则
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:add')")
    @Log(title = "巡检规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsInspectionRule wmsInspectionRule)
    {
        return toAjax(wmsInspectionRuleService.insertWmsInspectionRule(wmsInspectionRule));
    }

    /**
     * 修改巡检规则
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:edit')")
    @Log(title = "巡检规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsInspectionRule wmsInspectionRule)
    {
        return toAjax(wmsInspectionRuleService.updateWmsInspectionRule(wmsInspectionRule));
    }

    /**
     * 删除巡检规则
     */
//    @PreAuthorize("@ss.hasPermi('system:wms_inspection_rule:remove')")
    @Log(title = "巡检规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsInspectionRuleService.deleteWmsInspectionRuleByIds(ids));
    }
}
