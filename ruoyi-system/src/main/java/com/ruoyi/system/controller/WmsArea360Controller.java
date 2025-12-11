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
import com.ruoyi.system.domain.WmsArea360;
import com.ruoyi.system.service.IWmsArea360Service;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 区域点位全景Controller
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@Api("区域点位全景接口")
@RestController
@RequestMapping("/system/wms_area_360")
public class WmsArea360Controller extends BaseController
{
    @Autowired
    private IWmsArea360Service wmsArea360Service;

    /**
     * 查询区域点位全景列表
     */
    @ApiOperation("查询区域点位全景列表")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsArea360 wmsArea360)
    {
        startPage();
        List<WmsArea360> list = wmsArea360Service.selectWmsArea360List(wmsArea360);
        return getDataTable(list);
    }

    /**
     * 导出区域点位全景列表
     */
    @ApiOperation("导出区域点位全景列表")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:export')")
    @Log(title = "区域点位全景", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsArea360 wmsArea360)
    {
        List<WmsArea360> list = wmsArea360Service.selectWmsArea360List(wmsArea360);
        ExcelUtil<WmsArea360> util = new ExcelUtil<WmsArea360>(WmsArea360.class);
        util.exportExcel(response, list, "区域点位全景数据");
    }

    /**
     * 获取区域点位全景详细信息
     */
    @ApiOperation("获取区域点位全景详细信息")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsArea360Service.selectWmsArea360ById(id));
    }

    /**
     * 新增区域点位全景
     */
    @ApiOperation("新增区域点位全景")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:add')")
    @Log(title = "区域点位全景", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsArea360 wmsArea360)
    {
        return toAjax(wmsArea360Service.insertWmsArea360(wmsArea360));
    }

    /**
     * 修改区域点位全景
     */
    @ApiOperation("修改区域点位全景")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:edit')")
    @Log(title = "区域点位全景", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsArea360 wmsArea360)
    {
        return toAjax(wmsArea360Service.updateWmsArea360(wmsArea360));
    }

    /**
     * 删除区域点位全景
     */
    @ApiOperation("删除区域点位全景")
    @PreAuthorize("@ss.hasPermi('system:wms_area_360:remove')")
    @Log(title = "区域点位全景", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsArea360Service.deleteWmsArea360ByIds(ids));
    }
}
