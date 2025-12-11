package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
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
import com.ruoyi.system.domain.LanyaVehicleAlarm;
import com.ruoyi.system.service.ILanyaVehicleAlarmService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆报警记录Controller
 *
 * @author 吕涛
 * @date 2025-10-10
 */
@Api(tags = "车辆报警记录管理")
@RestController
@RequestMapping("/system/lanya_vehicle_alarm")
@DataSource(DataSourceType.SLAVE)
public class LanyaVehicleAlarmController extends BaseController {
    @Autowired
    private ILanyaVehicleAlarmService lanyaVehicleAlarmService;

    /**
     * 查询车辆报警记录列表
     */
    @ApiOperation("查询车辆报警记录列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaVehicleAlarm lanyaVehicleAlarm) {
        startPage();
        List<LanyaVehicleAlarm> list = lanyaVehicleAlarmService.selectLanyaVehicleAlarmList(lanyaVehicleAlarm);
        return getDataTable(list);
    }

    /**
     * 导出车辆报警记录列表
     */
    @ApiOperation("导出车辆报警记录列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:export')")
    @Log(title = "车辆报警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaVehicleAlarm lanyaVehicleAlarm) {
        List<LanyaVehicleAlarm> list = lanyaVehicleAlarmService.selectLanyaVehicleAlarmList(lanyaVehicleAlarm);
        ExcelUtil<LanyaVehicleAlarm> util = new ExcelUtil<LanyaVehicleAlarm>(LanyaVehicleAlarm.class);
        util.exportExcel(response, list, "车辆报警记录数据");
    }

    /**
     * 获取车辆报警记录详细信息
     */
    @ApiOperation("获取车辆报警记录详细信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lanyaVehicleAlarmService.selectLanyaVehicleAlarmById(id));
    }

    /**
     * 新增车辆报警记录
     */
    @ApiOperation("新增车辆报警记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:add')")
    @Log(title = "车辆报警记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaVehicleAlarm lanyaVehicleAlarm) {
        return toAjax(lanyaVehicleAlarmService.insertLanyaVehicleAlarm(lanyaVehicleAlarm));
    }

    /**
     * 修改车辆报警记录
     */
    @ApiOperation("修改车辆报警记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:edit')")
    @Log(title = "车辆报警记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaVehicleAlarm lanyaVehicleAlarm) {
        return toAjax(lanyaVehicleAlarmService.updateLanyaVehicleAlarm(lanyaVehicleAlarm));
    }

    /**
     * 删除车辆报警记录
     */
    @ApiOperation("删除车辆报警记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_vehicle_alarm:remove')")
    @Log(title = "车辆报警记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lanyaVehicleAlarmService.deleteLanyaVehicleAlarmByIds(ids));
    }
}
