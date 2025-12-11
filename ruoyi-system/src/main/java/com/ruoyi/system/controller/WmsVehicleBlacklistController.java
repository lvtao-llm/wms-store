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
import com.ruoyi.system.domain.WmsVehicleBlacklist;
import com.ruoyi.system.service.IWmsVehicleBlacklistService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆黑名单Controller
 *
 * @author ruoyi
 * @date 2025-10-30
 */
@Api(value = "车辆黑名单管理", tags = {"系统端", "车辆黑名单管理"})
@RestController
@RequestMapping("/system/wms_vehicle_blacklist")
public class WmsVehicleBlacklistController extends BaseController {
    @Autowired
    private IWmsVehicleBlacklistService wmsVehicleBlacklistService;

    /**
     * 查询车辆黑名单列表
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehicleBlacklist wmsVehicleBlacklist) {
        startPage();
        List<WmsVehicleBlacklist> list = wmsVehicleBlacklistService.selectWmsVehicleBlacklistList(wmsVehicleBlacklist);
        return getDataTable(list);
    }

    /**
     * 导出车辆黑名单列表
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:export')")
    @Log(title = "车辆黑名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehicleBlacklist wmsVehicleBlacklist) {
        List<WmsVehicleBlacklist> list = wmsVehicleBlacklistService.selectWmsVehicleBlacklistList(wmsVehicleBlacklist);
        ExcelUtil<WmsVehicleBlacklist> util = new ExcelUtil<WmsVehicleBlacklist>(WmsVehicleBlacklist.class);
        util.exportExcel(response, list, "车辆黑名单数据");
    }

    /**
     * 获取车辆黑名单详细信息
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(wmsVehicleBlacklistService.selectWmsVehicleBlacklistById(id));
    }

    /**
     * 新增车辆黑名单
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:add')")
    @Log(title = "车辆黑名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehicleBlacklist wmsVehicleBlacklist) {
        return toAjax(wmsVehicleBlacklistService.insertWmsVehicleBlacklist(wmsVehicleBlacklist));
    }

    /**
     * 修改车辆黑名单
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:edit')")
    @Log(title = "车辆黑名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehicleBlacklist wmsVehicleBlacklist) {
        return toAjax(wmsVehicleBlacklistService.updateWmsVehicleBlacklist(wmsVehicleBlacklist));
    }

    /**
     * 删除车辆黑名单
     */
    @ApiOperation("车辆黑名单列表")
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_blacklist:remove')")
    @Log(title = "车辆黑名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wmsVehicleBlacklistService.deleteWmsVehicleBlacklistByIds(ids));
    }
}
