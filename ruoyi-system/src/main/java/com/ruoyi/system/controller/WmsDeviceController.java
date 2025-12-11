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
import com.ruoyi.system.domain.WmsDevice;
import com.ruoyi.system.service.IWmsDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author ruoyi
 * @date 2025-10-27
 */
@Api(tags = "设备管理")
@RestController
@RequestMapping("/system/wms_device")
public class WmsDeviceController extends BaseController
{
    @Autowired
    private IWmsDeviceService wmsDeviceService;

    /**
     * 查询设备列表
     */
    @ApiOperation("设备列表")
    @GetMapping("/list")
    public TableDataInfo list(WmsDevice wmsDevice)
    {
        startPage();
        List<WmsDevice> list = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @ApiOperation("设备列表")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsDevice wmsDevice)
    {
        List<WmsDevice> list = wmsDeviceService.selectWmsDeviceList(wmsDevice);
        ExcelUtil<WmsDevice> util = new ExcelUtil<WmsDevice>(WmsDevice.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @ApiOperation("设备列表")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsDeviceService.selectWmsDeviceById(id));
    }

    /**
     * 新增设备
     */
    @ApiOperation("设备列表")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsDevice wmsDevice)
    {
        return toAjax(wmsDeviceService.insertWmsDevice(wmsDevice));
    }

    /**
     * 修改设备
     */
    @ApiOperation("设备列表")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsDevice wmsDevice)
    {
        return toAjax(wmsDeviceService.updateWmsDevice(wmsDevice));
    }

    /**
     * 删除设备
     */
    @ApiOperation("设备列表")
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsDeviceService.deleteWmsDeviceByIds(ids));
    }
}
