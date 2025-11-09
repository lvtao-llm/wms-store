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
import com.ruoyi.system.domain.WmsMaterialIn;
import com.ruoyi.system.service.IWmsMaterialInService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 接料视图Controller
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
@RestController
@RequestMapping("/system/wms_material_in")
public class WmsMaterialInController extends BaseController
{
    @Autowired
    private IWmsMaterialInService wmsMaterialInService;

    /**
     * 查询接料视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialIn wmsMaterialIn)
    {
        startPage();
        List<WmsMaterialIn> list = wmsMaterialInService.selectWmsMaterialInList(wmsMaterialIn);
        return getDataTable(list);
    }

    /**
     * 导出接料视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:export')")
    @Log(title = "接料视图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialIn wmsMaterialIn)
    {
        List<WmsMaterialIn> list = wmsMaterialInService.selectWmsMaterialInList(wmsMaterialIn);
        ExcelUtil<WmsMaterialIn> util = new ExcelUtil<WmsMaterialIn>(WmsMaterialIn.class);
        util.exportExcel(response, list, "接料视图数据");
    }

    /**
     * 获取接料视图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:query')")
    @GetMapping(value = "/{jlBh}")
    public AjaxResult getInfo(@PathVariable("jlBh") String jlBh)
    {
        return success(wmsMaterialInService.selectWmsMaterialInByJlBh(jlBh));
    }

    /**
     * 新增接料视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:add')")
    @Log(title = "接料视图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialIn wmsMaterialIn)
    {
        return toAjax(wmsMaterialInService.insertWmsMaterialIn(wmsMaterialIn));
    }

    /**
     * 修改接料视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:edit')")
    @Log(title = "接料视图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialIn wmsMaterialIn)
    {
        return toAjax(wmsMaterialInService.updateWmsMaterialIn(wmsMaterialIn));
    }

    /**
     * 删除接料视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:remove')")
    @Log(title = "接料视图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jlBhs}")
    public AjaxResult remove(@PathVariable String[] jlBhs)
    {
        return toAjax(wmsMaterialInService.deleteWmsMaterialInByJlBhs(jlBhs));
    }
}
