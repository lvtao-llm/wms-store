package com.ruoyi.quartz.controller;

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
import com.ruoyi.quartz.domain.MlPositionHistory;
import com.ruoyi.quartz.service.IMlPositionHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 历史轨迹Controller
 * 
 * @author ruoyi
 * @date 2025-09-29
 */
@RestController
@RequestMapping("/quartz/ml_point_histor")
public class MlPositionHistoryController extends BaseController
{
    @Autowired
    private IMlPositionHistoryService mlPositionHistoryService;

    /**
     * 查询历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:list')")
    @GetMapping("/list")
    public TableDataInfo list(MlPositionHistory mlPositionHistory)
    {
        startPage();
        List<MlPositionHistory> list = mlPositionHistoryService.selectMlPositionHistoryList(mlPositionHistory);
        return getDataTable(list);
    }

    /**
     * 导出历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:export')")
    @Log(title = "历史轨迹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MlPositionHistory mlPositionHistory)
    {
        List<MlPositionHistory> list = mlPositionHistoryService.selectMlPositionHistoryList(mlPositionHistory);
        ExcelUtil<MlPositionHistory> util = new ExcelUtil<MlPositionHistory>(MlPositionHistory.class);
        util.exportExcel(response, list, "历史轨迹数据");
    }

    /**
     * 获取历史轨迹详细信息
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mlPositionHistoryService.selectMlPositionHistoryById(id));
    }

    /**
     * 新增历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:add')")
    @Log(title = "历史轨迹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MlPositionHistory mlPositionHistory)
    {
        return toAjax(mlPositionHistoryService.insertMlPositionHistory(mlPositionHistory));
    }

    /**
     * 修改历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:edit')")
    @Log(title = "历史轨迹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MlPositionHistory mlPositionHistory)
    {
        return toAjax(mlPositionHistoryService.updateMlPositionHistory(mlPositionHistory));
    }

    /**
     * 删除历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('quartz:ml_point_histor:remove')")
    @Log(title = "历史轨迹", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mlPositionHistoryService.deleteMlPositionHistoryByIds(ids));
    }
}
