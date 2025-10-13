package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
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
import com.ruoyi.system.domain.LanyaPositionHistory;
import com.ruoyi.system.service.ILanyaPositionHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 历史轨迹Controller
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_position_history")
@DataSource(DataSourceType.SLAVE)
public class LanyaPositionHistoryController extends BaseController
{
    @Autowired
    private ILanyaPositionHistoryService lanyaPositionHistoryService;

    /**
     * 查询历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaPositionHistory lanyaPositionHistory)
    {
        startPage();
        List<LanyaPositionHistory> list = lanyaPositionHistoryService.selectLanyaPositionHistoryList(lanyaPositionHistory);
        return getDataTable(list);
    }

    /**
     * 导出历史轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:export')")
    @Log(title = "历史轨迹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaPositionHistory lanyaPositionHistory)
    {
        List<LanyaPositionHistory> list = lanyaPositionHistoryService.selectLanyaPositionHistoryList(lanyaPositionHistory);
        ExcelUtil<LanyaPositionHistory> util = new ExcelUtil<LanyaPositionHistory>(LanyaPositionHistory.class);
        util.exportExcel(response, list, "历史轨迹数据");
    }

    /**
     * 获取历史轨迹详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lanyaPositionHistoryService.selectLanyaPositionHistoryById(id));
    }

    /**
     * 新增历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:add')")
    @Log(title = "历史轨迹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaPositionHistory lanyaPositionHistory)
    {
        return toAjax(lanyaPositionHistoryService.insertLanyaPositionHistory(lanyaPositionHistory));
    }

    /**
     * 修改历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:edit')")
    @Log(title = "历史轨迹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaPositionHistory lanyaPositionHistory)
    {
        return toAjax(lanyaPositionHistoryService.updateLanyaPositionHistory(lanyaPositionHistory));
    }

    /**
     * 删除历史轨迹
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_history:remove')")
    @Log(title = "历史轨迹", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lanyaPositionHistoryService.deleteLanyaPositionHistoryByIds(ids));
    }
}
