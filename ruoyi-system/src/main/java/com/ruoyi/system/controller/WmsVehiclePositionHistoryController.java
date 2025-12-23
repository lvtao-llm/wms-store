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
import com.ruoyi.system.domain.WmsVehiclePositionHistory;
import com.ruoyi.system.service.IWmsVehiclePositionHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆路径点历史Controller
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/system/wms_vehicle_position_history")
public class WmsVehiclePositionHistoryController extends BaseController
{
    @Autowired
    private IWmsVehiclePositionHistoryService wmsVehiclePositionHistoryService;

    /**
     * 查询车辆路径点历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        startPage();
        List<WmsVehiclePositionHistory> list = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(wmsVehiclePositionHistory);
        return getDataTable(list);
    }

    /**
     * 导出车辆路径点历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:export')")
    @Log(title = "车辆路径点历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        List<WmsVehiclePositionHistory> list = wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryList(wmsVehiclePositionHistory);
        ExcelUtil<WmsVehiclePositionHistory> util = new ExcelUtil<WmsVehiclePositionHistory>(WmsVehiclePositionHistory.class);
        util.exportExcel(response, list, "车辆路径点历史数据");
    }

    /**
     * 获取车辆路径点历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsVehiclePositionHistoryService.selectWmsVehiclePositionHistoryById(id));
    }

    /**
     * 新增车辆路径点历史
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:add')")
    @Log(title = "车辆路径点历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        return toAjax(wmsVehiclePositionHistoryService.insertWmsVehiclePositionHistory(wmsVehiclePositionHistory));
    }

    /**
     * 修改车辆路径点历史
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:edit')")
    @Log(title = "车辆路径点历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehiclePositionHistory wmsVehiclePositionHistory)
    {
        return toAjax(wmsVehiclePositionHistoryService.updateWmsVehiclePositionHistory(wmsVehiclePositionHistory));
    }

    /**
     * 删除车辆路径点历史
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_position_history:remove')")
    @Log(title = "车辆路径点历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsVehiclePositionHistoryService.deleteWmsVehiclePositionHistoryByIds(ids));
    }
}
