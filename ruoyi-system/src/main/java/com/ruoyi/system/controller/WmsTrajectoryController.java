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
import com.ruoyi.system.domain.WmsTrajectory;
import com.ruoyi.system.service.IWmsTrajectoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 轨迹Controller
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Api(value = "轨迹管理", tags = {"系统端", "轨迹管理"})
@RestController
@RequestMapping("/system/trajectory")
public class WmsTrajectoryController extends BaseController {
    @Autowired
    private IWmsTrajectoryService wmsTrajectoryService;

    /**
     * 查询轨迹列表
     */
    @ApiOperation("轨迹列表")
    @PreAuthorize("@ss.hasPermi('system:trajectory:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsTrajectory wmsTrajectory) {
        startPage();
        List<WmsTrajectory> list = wmsTrajectoryService.selectWmsTrajectoryList(wmsTrajectory);
        return getDataTable(list);
    }

    /**
     * 查询轨迹列表
     */
    @ApiOperation("轨迹列表")
    @GetMapping("/personFuzzyList")
    public TableDataInfo personFuzzyList(String key) {
        startPage();
        WmsTrajectory wmsTrajectory = new WmsTrajectory();
        wmsTrajectory.setFuzzy(key);
        List<WmsTrajectory> list = wmsTrajectoryService.selectWmsTrajectoryList(wmsTrajectory);
        return getDataTable(list);
    }

    /**
     * 查询轨迹列表
     */
    @ApiOperation("轨迹列表")
    @GetMapping("/vehicleFuzzyList")
    public TableDataInfo vehicleFuzzyList(String key) {
        startPage();
        WmsTrajectory wmsTrajectory = new WmsTrajectory();
        wmsTrajectory.setTrajectoryType("车辆");
        wmsTrajectory.setFuzzy(key);
        List<WmsTrajectory> list = wmsTrajectoryService.selectWmsTrajectoryList(wmsTrajectory);
        return getDataTable(list);
    }

    /**
     * 导出轨迹列表
     */
    @ApiOperation("轨迹列表")
    @PreAuthorize("@ss.hasPermi('system:trajectory:export')")
    @Log(title = "轨迹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsTrajectory wmsTrajectory) {
        List<WmsTrajectory> list = wmsTrajectoryService.selectWmsTrajectoryList(wmsTrajectory);
        ExcelUtil<WmsTrajectory> util = new ExcelUtil<WmsTrajectory>(WmsTrajectory.class);
        util.exportExcel(response, list, "轨迹数据");
    }

    /**
     * 获取轨迹详细信息
     */
    @ApiOperation("获取轨迹详细信息")
    @PreAuthorize("@ss.hasPermi('system:trajectory:query')")
    @GetMapping(value = "/{trajectoryId}")
    public AjaxResult getInfo(@PathVariable("trajectoryId") Long trajectoryId) {
        return success(wmsTrajectoryService.selectWmsTrajectoryByTrajectoryId(trajectoryId));
    }

    /**
     * 新增轨迹
     */
    @ApiOperation("新增轨迹")
    @PreAuthorize("@ss.hasPermi('system:trajectory:add')")
    @Log(title = "轨迹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsTrajectory wmsTrajectory) {
        return toAjax(wmsTrajectoryService.insertWmsTrajectory(wmsTrajectory));
    }

    /**
     * 修改轨迹
     */
    @ApiOperation("修改轨迹")
    @PreAuthorize("@ss.hasPermi('system:trajectory:edit')")
    @Log(title = "轨迹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsTrajectory wmsTrajectory) {
        return toAjax(wmsTrajectoryService.updateWmsTrajectory(wmsTrajectory));
    }

    /**
     * 删除轨迹
     */
    @ApiOperation("删除轨迹")
    @PreAuthorize("@ss.hasPermi('system:trajectory:remove')")
    @Log(title = "轨迹", businessType = BusinessType.DELETE)
    @DeleteMapping("/{trajectoryIds}")
    public AjaxResult remove(@PathVariable Long[] trajectoryIds) {
        return toAjax(wmsTrajectoryService.deleteWmsTrajectoryByTrajectoryIds(trajectoryIds));
    }
}
