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
import com.ruoyi.system.domain.WmsVehicleRecord;
import com.ruoyi.system.service.IWmsVehicleRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆黑名单wms_vehicle_blacklistController
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@RestController
@RequestMapping("/system/wms_vehicle_record")
public class WmsVehicleRecordController extends BaseController
{
    @Autowired
    private IWmsVehicleRecordService wmsVehicleRecordService;

    /**
     * 查询车辆黑名单wms_vehicle_blacklist列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVehicleRecord wmsVehicleRecord)
    {
        startPage();
        List<WmsVehicleRecord> list = wmsVehicleRecordService.selectWmsVehicleRecordList(wmsVehicleRecord);
        return getDataTable(list);
    }

    /**
     * 导出车辆黑名单wms_vehicle_blacklist列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:export')")
    @Log(title = "车辆黑名单wms_vehicle_blacklist", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVehicleRecord wmsVehicleRecord)
    {
        List<WmsVehicleRecord> list = wmsVehicleRecordService.selectWmsVehicleRecordList(wmsVehicleRecord);
        ExcelUtil<WmsVehicleRecord> util = new ExcelUtil<WmsVehicleRecord>(WmsVehicleRecord.class);
        util.exportExcel(response, list, "车辆黑名单wms_vehicle_blacklist数据");
    }

    /**
     * 获取车辆黑名单wms_vehicle_blacklist详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsVehicleRecordService.selectWmsVehicleRecordById(id));
    }

    /**
     * 新增车辆黑名单wms_vehicle_blacklist
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:add')")
    @Log(title = "车辆黑名单wms_vehicle_blacklist", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVehicleRecord wmsVehicleRecord)
    {
        return toAjax(wmsVehicleRecordService.insertWmsVehicleRecord(wmsVehicleRecord));
    }

    /**
     * 修改车辆黑名单wms_vehicle_blacklist
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:edit')")
    @Log(title = "车辆黑名单wms_vehicle_blacklist", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVehicleRecord wmsVehicleRecord)
    {
        return toAjax(wmsVehicleRecordService.updateWmsVehicleRecord(wmsVehicleRecord));
    }

    /**
     * 删除车辆黑名单wms_vehicle_blacklist
     */
    @PreAuthorize("@ss.hasPermi('system:wms_vehicle_record:remove')")
    @Log(title = "车辆黑名单wms_vehicle_blacklist", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsVehicleRecordService.deleteWmsVehicleRecordByIds(ids));
    }
}
