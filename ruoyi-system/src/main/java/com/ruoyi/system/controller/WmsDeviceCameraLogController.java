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
import com.ruoyi.system.domain.WmsDeviceCameraLog;
import com.ruoyi.system.service.IWmsDeviceCameraLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 摄像头识别日志Controller
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
@RestController
@RequestMapping("/system/wms_device_camera_log")
public class WmsDeviceCameraLogController extends BaseController
{
    @Autowired
    private IWmsDeviceCameraLogService wmsDeviceCameraLogService;

    /**
     * 查询摄像头识别日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        startPage();
        List<WmsDeviceCameraLog> list = wmsDeviceCameraLogService.selectWmsDeviceCameraLogList(wmsDeviceCameraLog);
        return getDataTable(list);
    }

    /**
     * 导出摄像头识别日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:export')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        List<WmsDeviceCameraLog> list = wmsDeviceCameraLogService.selectWmsDeviceCameraLogList(wmsDeviceCameraLog);
        ExcelUtil<WmsDeviceCameraLog> util = new ExcelUtil<WmsDeviceCameraLog>(WmsDeviceCameraLog.class);
        util.exportExcel(response, list, "摄像头识别日志数据");
    }

    /**
     * 获取摄像头识别日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsDeviceCameraLogService.selectWmsDeviceCameraLogById(id));
    }

    /**
     * 新增摄像头识别日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:add')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        return toAjax(wmsDeviceCameraLogService.insertWmsDeviceCameraLog(wmsDeviceCameraLog));
    }

    /**
     * 修改摄像头识别日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:edit')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsDeviceCameraLog wmsDeviceCameraLog)
    {
        return toAjax(wmsDeviceCameraLogService.updateWmsDeviceCameraLog(wmsDeviceCameraLog));
    }

    /**
     * 删除摄像头识别日志
     */
    @PreAuthorize("@ss.hasPermi('system:wms_device_camera_log:remove')")
    @Log(title = "摄像头识别日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsDeviceCameraLogService.deleteWmsDeviceCameraLogByIds(ids));
    }
}
