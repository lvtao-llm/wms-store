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
import com.ruoyi.system.domain.WmsVehicleRoute;
import com.ruoyi.system.service.IWmsVehicleRouteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆路线规划Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Api(tags = "车辆路线规划Controller")
@RestController
@RequestMapping("/system/route")
public class WmsVehicleRouteController extends BaseController
{
    @Autowired
    private IWmsVehicleRouteService wmsVehicleRouteService;

    /**
     * 查询车辆路线规划列表
     */
    @ApiOperation("查询车辆路线规划列表")
    @PreAuthorize("@ss.hasPermi('system:route:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehicleRoute wmsVehicleRoute)
    {
        startPage();
        List<WmsVehicleRoute> list = wmsVehicleRouteService.selectWmsVehicleRouteList(wmsVehicleRoute);
        return getDataTable(list);
    }

    /**
     * 导出车辆路线规划列表
     */
    @ApiOperation("导出车辆路线规划列表")
    @PreAuthorize("@ss.hasPermi('system:route:export')")
    @Log(title = "车辆路线规划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehicleRoute wmsVehicleRoute)
    {
        List<WmsVehicleRoute> list = wmsVehicleRouteService.selectWmsVehicleRouteList(wmsVehicleRoute);
        ExcelUtil<WmsVehicleRoute> util = new ExcelUtil<WmsVehicleRoute>(WmsVehicleRoute.class);
        util.exportExcel(response, list, "车辆路线规划数据");
    }

    /**
     * 获取车辆路线规划详细信息
     */
    @ApiOperation("获取车辆路线规划详细信息")
    @PreAuthorize("@ss.hasPermi('system:route:query')")
    @GetMapping(value = "/{routeId}")
    public AjaxResult getInfo(@PathVariable("routeId") Long routeId)
    {
        return success(wmsVehicleRouteService.selectWmsVehicleRouteByRouteId(routeId));
    }

    /**
     * 新增车辆路线规划
     */
    @ApiOperation("新增车辆路线规划")
    @PreAuthorize("@ss.hasPermi('system:route:add')")
    @Log(title = "车辆路线规划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehicleRoute wmsVehicleRoute)
    {
        return toAjax(wmsVehicleRouteService.insertWmsVehicleRoute(wmsVehicleRoute));
    }

    /**
     * 修改车辆路线规划
     */
    @ApiOperation("修改车辆路线规划")
    @PreAuthorize("@ss.hasPermi('system:route:edit')")
    @Log(title = "车辆路线规划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehicleRoute wmsVehicleRoute)
    {
        return toAjax(wmsVehicleRouteService.updateWmsVehicleRoute(wmsVehicleRoute));
    }

    /**
     * 删除车辆路线规划
     */
    @ApiOperation("删除车辆路线规划")
    @PreAuthorize("@ss.hasPermi('system:route:remove')")
    @Log(title = "车辆路线规划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{routeIds}")
    public AjaxResult remove(@PathVariable Long[] routeIds)
    {
        return toAjax(wmsVehicleRouteService.deleteWmsVehicleRouteByRouteIds(routeIds));
    }
}
