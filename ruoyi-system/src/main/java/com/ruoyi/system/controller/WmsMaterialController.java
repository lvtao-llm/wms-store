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
import com.ruoyi.system.domain.WmsMaterial;
import com.ruoyi.system.service.IWmsMaterialService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料档案Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/system/material")
public class WmsMaterialController extends BaseController
{
    @Autowired
    private IWmsMaterialService wmsMaterialService;

    /**
     * 查询物料档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterial wmsMaterial)
    {
        startPage();
        List<WmsMaterial> list = wmsMaterialService.selectWmsMaterialList(wmsMaterial);
        return getDataTable(list);
    }

    /**
     * 导出物料档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:material:export')")
    @Log(title = "物料档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterial wmsMaterial)
    {
        List<WmsMaterial> list = wmsMaterialService.selectWmsMaterialList(wmsMaterial);
        ExcelUtil<WmsMaterial> util = new ExcelUtil<WmsMaterial>(WmsMaterial.class);
        util.exportExcel(response, list, "物料档案数据");
    }

    /**
     * 获取物料档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:material:query')")
    @GetMapping(value = "/{materialId}")
    public AjaxResult getInfo(@PathVariable("materialId") Long materialId)
    {
        return success(wmsMaterialService.selectWmsMaterialByMaterialId(materialId));
    }

    /**
     * 新增物料档案
     */
    @PreAuthorize("@ss.hasPermi('system:material:add')")
    @Log(title = "物料档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterial wmsMaterial)
    {
        return toAjax(wmsMaterialService.insertWmsMaterial(wmsMaterial));
    }

    /**
     * 修改物料档案
     */
    @PreAuthorize("@ss.hasPermi('system:material:edit')")
    @Log(title = "物料档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterial wmsMaterial)
    {
        return toAjax(wmsMaterialService.updateWmsMaterial(wmsMaterial));
    }

    /**
     * 删除物料档案
     */
    @PreAuthorize("@ss.hasPermi('system:material:remove')")
    @Log(title = "物料档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{materialIds}")
    public AjaxResult remove(@PathVariable Long[] materialIds)
    {
        return toAjax(wmsMaterialService.deleteWmsMaterialByMaterialIds(materialIds));
    }
}
