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
import com.ruoyi.system.domain.WmsVehicleGatepass;
import com.ruoyi.system.service.IWmsVehicleGatepassService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆预约Controller
 *
 * @author ruoyi
 * @date 2025-10-30
 */
@Api(value = "车辆预约", tags = {"系统端", "车辆预约管理"})
@RestController
@RequestMapping("/system/gatepass")
public class WmsVehicleGatepassController extends BaseController {
    @Autowired
    private IWmsVehicleGatepassService wmsVehicleGatepassService;

    /**
     * 查询车辆预约列表
     */
    @ApiOperation("车辆预约列表")
    @PreAuthorize("@ss.hasPermi('system:gatepass:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehicleGatepass wmsVehicleGatepass) {
        startPage();
        List<WmsVehicleGatepass> list = wmsVehicleGatepassService.selectWmsVehicleGatepassList(wmsVehicleGatepass);
        return getDataTable(list);
    }

    /**
     * 导出车辆预约列表
     */
    @ApiOperation("车辆预约列表")
    @PreAuthorize("@ss.hasPermi('system:gatepass:export')")
    @Log(title = "车辆预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehicleGatepass wmsVehicleGatepass) {
        List<WmsVehicleGatepass> list = wmsVehicleGatepassService.selectWmsVehicleGatepassList(wmsVehicleGatepass);
        ExcelUtil<WmsVehicleGatepass> util = new ExcelUtil<WmsVehicleGatepass>(WmsVehicleGatepass.class);
        util.exportExcel(response, list, "车辆预约数据");
    }

    /**
     * 获取车辆预约详细信息
     */
    @ApiOperation("获取车辆预约详细信息")
    @PreAuthorize("@ss.hasPermi('system:gatepass:query')")
    @GetMapping(value = "/{gatepassId}")
    public AjaxResult getInfo(@PathVariable("gatepassId") Long gatepassId) {
        return success(wmsVehicleGatepassService.selectWmsVehicleGatepassByGatepassId(gatepassId));
    }

    /**
     * 新增车辆预约
     */
    @ApiOperation("新增车辆预约")
    @PreAuthorize("@ss.hasPermi('system:gatepass:add')")
    @Log(title = "车辆预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehicleGatepass wmsVehicleGatepass) {
        return toAjax(wmsVehicleGatepassService.insertWmsVehicleGatepass(wmsVehicleGatepass));
    }

    /**
     * 修改车辆预约
     */
    @ApiOperation("修改车辆预约")
    @PreAuthorize("@ss.hasPermi('system:gatepass:edit')")
    @Log(title = "车辆预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehicleGatepass wmsVehicleGatepass) {
        return toAjax(wmsVehicleGatepassService.updateWmsVehicleGatepass(wmsVehicleGatepass));
    }

    /**
     * 删除车辆预约
     */
    @ApiOperation("删除车辆预约")
    @PreAuthorize("@ss.hasPermi('system:gatepass:remove')")
    @Log(title = "车辆预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{gatepassIds}")
    public AjaxResult remove(@PathVariable Long[] gatepassIds) {
        return toAjax(wmsVehicleGatepassService.deleteWmsVehicleGatepassByGatepassIds(gatepassIds));
    }
}
