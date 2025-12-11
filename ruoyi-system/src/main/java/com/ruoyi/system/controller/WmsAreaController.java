package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
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
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAreaService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 区域Controller
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Api(value = "区域接口", tags = {"系统端", "区域管理"})
@RestController
@RequestMapping("/system/area")
public class WmsAreaController extends BaseController {
    @Autowired
    private IWmsAreaService wmsAreaService;

    @Autowired
    private ISysDictDataService dictDataService;

    String dictType = "wms_area_name";

    /**
     * 查询区域列表
     */
    @ApiOperation("区域列表")
    @GetMapping("/list")
    public TableDataInfo list(WmsArea wmsArea) {
        startPage();
        List<WmsArea> list = wmsAreaService.selectWmsAreaList(wmsArea);
        return getDataTable(list);
    }

    /**
     * 导出区域列表
     */
    @ApiOperation("区域列表")
    @Log(title = "区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsArea wmsArea) {
        List<WmsArea> list = wmsAreaService.selectWmsAreaList(wmsArea);
        ExcelUtil<WmsArea> util = new ExcelUtil<WmsArea>(WmsArea.class);
        util.exportExcel(response, list, "区域数据");
    }

    /**
     * 获取区域详细信息
     */
    @ApiOperation("区域列表")
    @GetMapping(value = "/{areaId}")
    public AjaxResult getInfo(@PathVariable("areaId") Long areaId) {
        return success(wmsAreaService.selectWmsAreaByAreaId(areaId));
    }

    /**
     * 新增区域
     */
    @ApiOperation("区域列表")
    @Log(title = "区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsArea wmsArea) {
        return toAjax(wmsAreaService.insertWmsArea(wmsArea));
    }

    /**
     * 修改区域
     */
    @ApiOperation("区域列表")
    @Log(title = "区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsArea wmsArea) {
        return toAjax(wmsAreaService.updateWmsArea(wmsArea));
    }

    /**
     * 删除区域
     */
    @ApiOperation("区域列表")
    @Log(title = "区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/{areaIds}")
    public AjaxResult remove(@PathVariable Long[] areaIds) {
        return toAjax(wmsAreaService.deleteWmsAreaByAreaIds(areaIds));
    }
}
