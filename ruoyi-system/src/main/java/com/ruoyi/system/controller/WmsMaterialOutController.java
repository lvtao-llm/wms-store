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
import com.ruoyi.system.domain.WmsMaterialOut;
import com.ruoyi.system.service.IWmsMaterialOutService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 调拨视图Controller
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
@RestController
@RequestMapping("/system/wms_material_out")
public class WmsMaterialOutController extends BaseController
{
    @Autowired
    private IWmsMaterialOutService wmsMaterialOutService;

    /**
     * 查询调拨视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialOut wmsMaterialOut)
    {
        startPage();
        List<WmsMaterialOut> list = wmsMaterialOutService.selectWmsMaterialOutList(wmsMaterialOut);
        return getDataTable(list);
    }

    /**
     * 导出调拨视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:export')")
    @Log(title = "调拨视图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialOut wmsMaterialOut)
    {
        List<WmsMaterialOut> list = wmsMaterialOutService.selectWmsMaterialOutList(wmsMaterialOut);
        ExcelUtil<WmsMaterialOut> util = new ExcelUtil<WmsMaterialOut>(WmsMaterialOut.class);
        util.exportExcel(response, list, "调拨视图数据");
    }

    /**
     * 获取调拨视图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:query')")
    @GetMapping(value = "/{allotDetailId}")
    public AjaxResult getInfo(@PathVariable("allotDetailId") String allotDetailId)
    {
        return success(wmsMaterialOutService.selectWmsMaterialOutByAllotDetailId(allotDetailId));
    }

    /**
     * 新增调拨视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:add')")
    @Log(title = "调拨视图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialOut wmsMaterialOut)
    {
        return toAjax(wmsMaterialOutService.insertWmsMaterialOut(wmsMaterialOut));
    }

    /**
     * 修改调拨视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:edit')")
    @Log(title = "调拨视图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialOut wmsMaterialOut)
    {
        return toAjax(wmsMaterialOutService.updateWmsMaterialOut(wmsMaterialOut));
    }

    /**
     * 删除调拨视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out:remove')")
    @Log(title = "调拨视图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{allotDetailIds}")
    public AjaxResult remove(@PathVariable String[] allotDetailIds)
    {
        return toAjax(wmsMaterialOutService.deleteWmsMaterialOutByAllotDetailIds(allotDetailIds));
    }
}
