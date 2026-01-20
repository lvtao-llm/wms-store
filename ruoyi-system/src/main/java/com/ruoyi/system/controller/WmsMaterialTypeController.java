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
import com.ruoyi.system.domain.WmsMaterialType;
import com.ruoyi.system.service.IWmsMaterialTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料类型Controller
 * 
 * @author ruoyi
 * @date 2026-01-20
 */
@RestController
@RequestMapping("/system/wms_material_type")
public class WmsMaterialTypeController extends BaseController
{
    @Autowired
    private IWmsMaterialTypeService wmsMaterialTypeService;

    /**
     * 查询物料类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialType wmsMaterialType)
    {
        startPage();
        List<WmsMaterialType> list = wmsMaterialTypeService.selectWmsMaterialTypeList(wmsMaterialType);
        return getDataTable(list);
    }

    /**
     * 导出物料类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:export')")
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialType wmsMaterialType)
    {
        List<WmsMaterialType> list = wmsMaterialTypeService.selectWmsMaterialTypeList(wmsMaterialType);
        ExcelUtil<WmsMaterialType> util = new ExcelUtil<WmsMaterialType>(WmsMaterialType.class);
        util.exportExcel(response, list, "物料类型数据");
    }

    /**
     * 获取物料类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:query')")
    @GetMapping(value = "/{bm}")
    public AjaxResult getInfo(@PathVariable("bm") String bm)
    {
        return success(wmsMaterialTypeService.selectWmsMaterialTypeByBm(bm));
    }

    /**
     * 新增物料类型
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:add')")
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialType wmsMaterialType)
    {
        return toAjax(wmsMaterialTypeService.insertWmsMaterialType(wmsMaterialType));
    }

    /**
     * 修改物料类型
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:edit')")
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialType wmsMaterialType)
    {
        return toAjax(wmsMaterialTypeService.updateWmsMaterialType(wmsMaterialType));
    }

    /**
     * 删除物料类型
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_type:remove')")
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bms}")
    public AjaxResult remove(@PathVariable String[] bms)
    {
        return toAjax(wmsMaterialTypeService.deleteWmsMaterialTypeByBms(bms));
    }
}
