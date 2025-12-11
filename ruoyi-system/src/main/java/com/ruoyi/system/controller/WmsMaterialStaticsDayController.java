package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.system.domain.WmsMaterialStaticsDay;
import com.ruoyi.system.service.IWmsMaterialStaticsDayService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料日统计Controller
 *
 * @author ruoyi
 * @date 2025-11-09
 */
@Api(value = "物料日统计", tags = {"系统端", "物料日统计管理"})
@RestController
@RequestMapping("/system/wms_material_statics_day")
public class WmsMaterialStaticsDayController extends BaseController {
    @Autowired
    private IWmsMaterialStaticsDayService wmsMaterialStaticsDayService;

    /**
     * 查询物料日统计列表
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialStaticsDay wmsMaterialStaticsDay) {
        startPage();
        List<WmsMaterialStaticsDay> list = wmsMaterialStaticsDayService.selectWmsMaterialStaticsDayList(wmsMaterialStaticsDay);
        return getDataTable(list);
    }

    /**
     * 导出物料日统计列表
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:export')")
    @Log(title = "物料日统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialStaticsDay wmsMaterialStaticsDay) {
        List<WmsMaterialStaticsDay> list = wmsMaterialStaticsDayService.selectWmsMaterialStaticsDayList(wmsMaterialStaticsDay);
        ExcelUtil<WmsMaterialStaticsDay> util = new ExcelUtil<WmsMaterialStaticsDay>(WmsMaterialStaticsDay.class);
        util.exportExcel(response, list, "物料日统计数据");
    }

    /**
     * 获取物料日统计详细信息
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:query')")
    @GetMapping(value = "/{day}")
    public AjaxResult getInfo(@PathVariable("day") String day) {
        return success(wmsMaterialStaticsDayService.selectWmsMaterialStaticsDayByDay(day));
    }

    /**
     * 新增物料日统计
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:add')")
    @Log(title = "物料日统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialStaticsDay wmsMaterialStaticsDay) {
        return toAjax(wmsMaterialStaticsDayService.insertWmsMaterialStaticsDay(wmsMaterialStaticsDay));
    }

    /**
     * 修改物料日统计
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:edit')")
    @Log(title = "物料日统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialStaticsDay wmsMaterialStaticsDay) {
        return toAjax(wmsMaterialStaticsDayService.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay));
    }

    /**
     * 删除物料日统计
     */
    @ApiOperation("物料日统计列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_statics_day:remove')")
    @Log(title = "物料日统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{days}")
    public AjaxResult remove(@PathVariable String[] days) {
        return toAjax(wmsMaterialStaticsDayService.deleteWmsMaterialStaticsDayByDays(days));
    }
}
