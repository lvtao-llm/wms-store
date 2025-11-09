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
import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.service.IWmsMaterialDescService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料描述档案Controller
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/system/wms_material_desc")
public class WmsMaterialDescController extends BaseController
{
    @Autowired
    private IWmsMaterialDescService wmsMaterialDescService;

    /**
     * 查询物料描述档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialDesc wmsMaterialDesc)
    {
        startPage();
        List<WmsMaterialDesc> list = wmsMaterialDescService.selectWmsMaterialDescList(wmsMaterialDesc);
        return getDataTable(list);
    }

    /**
     * 导出物料描述档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:export')")
    @Log(title = "物料描述档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialDesc wmsMaterialDesc)
    {
        List<WmsMaterialDesc> list = wmsMaterialDescService.selectWmsMaterialDescList(wmsMaterialDesc);
        ExcelUtil<WmsMaterialDesc> util = new ExcelUtil<WmsMaterialDesc>(WmsMaterialDesc.class);
        util.exportExcel(response, list, "物料描述档案数据");
    }

    /**
     * 获取物料描述档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:query')")
    @GetMapping(value = "/{materialDescId}")
    public AjaxResult getInfo(@PathVariable("materialDescId") Long materialDescId)
    {
        return success(wmsMaterialDescService.selectWmsMaterialDescByMaterialDescId(materialDescId));
    }

    /**
     * 新增物料描述档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:add')")
    @Log(title = "物料描述档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialDesc wmsMaterialDesc)
    {
        return toAjax(wmsMaterialDescService.insertWmsMaterialDesc(wmsMaterialDesc));
    }

    /**
     * 修改物料描述档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:edit')")
    @Log(title = "物料描述档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialDesc wmsMaterialDesc)
    {
        return toAjax(wmsMaterialDescService.updateWmsMaterialDesc(wmsMaterialDesc));
    }

    /**
     * 删除物料描述档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_desc:remove')")
    @Log(title = "物料描述档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{materialDescIds}")
    public AjaxResult remove(@PathVariable Long[] materialDescIds)
    {
        return toAjax(wmsMaterialDescService.deleteWmsMaterialDescByMaterialDescIds(materialDescIds));
    }
}
