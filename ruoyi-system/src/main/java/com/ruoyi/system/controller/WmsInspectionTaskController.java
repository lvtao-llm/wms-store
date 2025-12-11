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
import com.ruoyi.system.domain.WmsInspectionTask;
import com.ruoyi.system.service.IWmsInspectionTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡检任务Controller
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
@Api(tags = "巡检任务管理")
@RestController
@RequestMapping("/system/wms_inspection_task")
public class WmsInspectionTaskController extends BaseController
{
    @Autowired
    private IWmsInspectionTaskService wmsInspectionTaskService;

    /**
     * 查询巡检任务列表
     */
    @ApiOperation("查询巡检任务列表")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsInspectionTask wmsInspectionTask)
    {
        startPage();
        List<WmsInspectionTask> list = wmsInspectionTaskService.selectWmsInspectionTaskList(wmsInspectionTask);
        return getDataTable(list);
    }

    /**
     * 导出巡检任务列表
     */
    @ApiOperation("导出巡检任务列表")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:export')")
    @Log(title = "巡检任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInspectionTask wmsInspectionTask)
    {
        List<WmsInspectionTask> list = wmsInspectionTaskService.selectWmsInspectionTaskList(wmsInspectionTask);
        ExcelUtil<WmsInspectionTask> util = new ExcelUtil<WmsInspectionTask>(WmsInspectionTask.class);
        util.exportExcel(response, list, "巡检任务数据");
    }

    /**
     * 获取巡检任务详细信息
     */
    @ApiOperation("获取巡检任务详细信息")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsInspectionTaskService.selectWmsInspectionTaskById(id));
    }

    /**
     * 新增巡检任务
     */
    @ApiOperation("新增巡检任务")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:add')")
    @Log(title = "巡检任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsInspectionTask wmsInspectionTask)
    {
        return toAjax(wmsInspectionTaskService.insertWmsInspectionTask(wmsInspectionTask));
    }

    /**
     * 修改巡检任务
     */
    @ApiOperation("修改巡检任务")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:edit')")
    @Log(title = "巡检任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsInspectionTask wmsInspectionTask)
    {
        return toAjax(wmsInspectionTaskService.updateWmsInspectionTask(wmsInspectionTask));
    }

    /**
     * 删除巡检任务
     */
    @ApiOperation("删除巡检任务")
    @PreAuthorize("@ss.hasPermi('system:wms_inspection_task:remove')")
    @Log(title = "巡检任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsInspectionTaskService.deleteWmsInspectionTaskByIds(ids));
    }
}
