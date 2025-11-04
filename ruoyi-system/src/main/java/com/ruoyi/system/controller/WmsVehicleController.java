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
import com.ruoyi.system.domain.WmsVehicle;
import com.ruoyi.system.service.IWmsVehicleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆档案Controller
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@RestController
@RequestMapping("/system/wms_vehicle")
public class WmsVehicleController extends BaseController
{
    @Autowired
    private IWmsVehicleService wmsVehicleService;

    /**
     * 查询车辆档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehicle wmsVehicle)
    {
        startPage();
        List<WmsVehicle> list = wmsVehicleService.selectWmsVehicleList(wmsVehicle);
        return getDataTable(list);
    }

    /**
     * 导出车辆档案列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:export')")
    @Log(title = "车辆档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehicle wmsVehicle)
    {
        List<WmsVehicle> list = wmsVehicleService.selectWmsVehicleList(wmsVehicle);
        ExcelUtil<WmsVehicle> util = new ExcelUtil<WmsVehicle>(WmsVehicle.class);
        util.exportExcel(response, list, "车辆档案数据");
    }

    /**
     * 获取车辆档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:query')")
    @GetMapping(value = "/{vehicleId}")
    public AjaxResult getInfo(@PathVariable("vehicleId") Long vehicleId)
    {
        return success(wmsVehicleService.selectWmsVehicleByVehicleId(vehicleId));
    }

    /**
     * 新增车辆档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:add')")
    @Log(title = "车辆档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehicle wmsVehicle)
    {
        return toAjax(wmsVehicleService.insertWmsVehicle(wmsVehicle));
    }

    /**
     * 修改车辆档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:edit')")
    @Log(title = "车辆档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehicle wmsVehicle)
    {
        return toAjax(wmsVehicleService.updateWmsVehicle(wmsVehicle));
    }

    /**
     * 删除车辆档案
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle:remove')")
    @Log(title = "车辆档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vehicleIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleIds)
    {
        return toAjax(wmsVehicleService.deleteWmsVehicleByVehicleIds(vehicleIds));
    }
}
