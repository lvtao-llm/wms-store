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
import com.ruoyi.system.domain.LanyaPositionCurrent;
import com.ruoyi.system.service.ILanyaPositionCurrentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 实时定位Controller
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
@RestController
@RequestMapping("/system/lanya_position_current")
public class LanyaPositionCurrentController extends BaseController
{
    @Autowired
    private ILanyaPositionCurrentService lanyaPositionCurrentService;

    /**
     * 查询实时定位列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaPositionCurrent lanyaPositionCurrent)
    {
        startPage();
        List<LanyaPositionCurrent> list = lanyaPositionCurrentService.selectLanyaPositionCurrentList(lanyaPositionCurrent);
        return getDataTable(list);
    }

    /**
     * 导出实时定位列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:export')")
    @Log(title = "实时定位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaPositionCurrent lanyaPositionCurrent)
    {
        List<LanyaPositionCurrent> list = lanyaPositionCurrentService.selectLanyaPositionCurrentList(lanyaPositionCurrent);
        ExcelUtil<LanyaPositionCurrent> util = new ExcelUtil<LanyaPositionCurrent>(LanyaPositionCurrent.class);
        util.exportExcel(response, list, "实时定位数据");
    }

    /**
     * 获取实时定位详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lanyaPositionCurrentService.selectLanyaPositionCurrentById(id));
    }

    /**
     * 新增实时定位
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:add')")
    @Log(title = "实时定位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaPositionCurrent lanyaPositionCurrent)
    {
        return toAjax(lanyaPositionCurrentService.insertLanyaPositionCurrent(lanyaPositionCurrent));
    }

    /**
     * 修改实时定位
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:edit')")
    @Log(title = "实时定位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaPositionCurrent lanyaPositionCurrent)
    {
        return toAjax(lanyaPositionCurrentService.updateLanyaPositionCurrent(lanyaPositionCurrent));
    }

    /**
     * 删除实时定位
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_position_current:remove')")
    @Log(title = "实时定位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lanyaPositionCurrentService.deleteLanyaPositionCurrentByIds(ids));
    }
}
