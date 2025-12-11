package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.service.IWmsAreaService;
import com.ruoyi.system.service.IWmsMaterialDescService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@Api(value = "接料视图", tags = {"系统端", "接料视图"})
@RestController
@RequestMapping("/system/wms_material_in")
public class WmsMaterialInController extends BaseController {
    @Autowired
    private IWmsMaterialInService wmsMaterialInService;

    @Autowired
    private IWmsAreaService wmsAreaService;

    @Autowired
    private IWmsMaterialDescService wmsMaterialDescService;

    /**
     * 查询接料视图列表
     */
    @ApiOperation("查询接料视图列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialIn wmsMaterialIn) {
        startPage();

        List<WmsMaterialIn> wmsMaterialIns = wmsMaterialInService.selectWmsMaterialInList(wmsMaterialIn);

        return getDataTable(wmsMaterialIns);
    }

    /**
     * 查询接料视图列表
     */
    @ApiOperation("查询接料视图列表")
    @GetMapping("/list/{areaName}")
    public TableDataInfo list(WmsMaterialIn wmsMaterialIn, @PathVariable(value = "areaName", required = false) String areaName) {
        startPage();
        List<WmsMaterialIn> list = new ArrayList<>();
        if (areaName != null) {
            WmsArea wmsArea = new WmsArea();
            wmsArea.setAreaName(areaName);
            List<WmsArea> wmsAreas = wmsAreaService.selectWmsAreaList(wmsArea);
            if (wmsAreas.isEmpty()) {
                return getDataTable(list);
            }
            wmsArea = wmsAreas.get(0);
            List<WmsMaterialDesc> wmsMaterialDescs = wmsMaterialDescService.selectWmsMaterialDescList(new WmsMaterialDesc());
            List<String> wzbm = new ArrayList<>();
            for (WmsMaterialDesc desc : wmsMaterialDescs) {
                if (desc.getAreaCodes() == null) {
                    continue;
                }
                String[] ids = desc.getAreaCodes().split(",");
                String id = wmsArea.getAreaId() + "";
                if (Arrays.asList(ids).contains(id)) {
                    wzbm.add(desc.getWzmc());
                }
            }
            if (wzbm.isEmpty()) {
                return getDataTable(list);
            }
            list = wmsMaterialInService.selectWmsMaterialInListByAreaNames(wmsMaterialIn, wzbm);
        } else {
            list = wmsMaterialInService.selectWmsMaterialInList(wmsMaterialIn);
        }

        return getDataTable(list);
    }

    /**
     * 导出接料视图列表
     */
    @ApiOperation("导出接料视图列表")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:export')")
    @Log(title = "接料视图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialIn wmsMaterialIn) {
        List<WmsMaterialIn> list = wmsMaterialInService.selectWmsMaterialInList(wmsMaterialIn);
        ExcelUtil<WmsMaterialIn> util = new ExcelUtil<WmsMaterialIn>(WmsMaterialIn.class);
        util.exportExcel(response, list, "接料视图数据");
    }

    /**
     * 获取接料视图详细信息
     */
    @ApiOperation("获取接料视图详细信息")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:query')")
    @GetMapping(value = "/{jlBh}")
    public AjaxResult getInfo(@PathVariable("jlBh") String jlBh) {
        return success(wmsMaterialInService.selectWmsMaterialInByJlBh(jlBh));
    }

    /**
     * 新增接料视图
     */
    @ApiOperation("新增接料视图")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:add')")
    @Log(title = "接料视图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialIn wmsMaterialIn) {
        return toAjax(wmsMaterialInService.insertWmsMaterialIn(wmsMaterialIn));
    }

    /**
     * 修改接料视图
     */
    @ApiOperation("修改接料视图")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:edit')")
    @Log(title = "接料视图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialIn wmsMaterialIn) {
        return toAjax(wmsMaterialInService.updateWmsMaterialIn(wmsMaterialIn));
    }

    /**
     * 删除接料视图
     */
    @ApiOperation("删除接料视图")
    @PreAuthorize("@ss.hasPermi('system:wms_material_in:remove')")
    @Log(title = "接料视图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jlBhs}")
    public AjaxResult remove(@PathVariable String[] jlBhs) {
        return toAjax(wmsMaterialInService.deleteWmsMaterialInByJlBhs(jlBhs));
    }
}
