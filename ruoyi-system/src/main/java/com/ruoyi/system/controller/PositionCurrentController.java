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
import com.ruoyi.system.domain.PositionCurrent;
import com.ruoyi.system.service.IPositionCurrentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 实时定位Controller
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
@Api(tags = "实时定位管理")
@RestController
@RequestMapping("/system/position_current")
public class PositionCurrentController extends BaseController
{
    @Autowired
    private IPositionCurrentService positionCurrentService;

    /**
     * 查询实时定位列表
     */
    @ApiOperation("查询实时定位列表")
    @PreAuthorize("@ss.hasPermi('system:position_current:list')")
    @GetMapping("/list")
    public TableDataInfo list(PositionCurrent positionCurrent)
    {
        startPage();
        List<PositionCurrent> list = positionCurrentService.selectPositionCurrentList(positionCurrent);
        return getDataTable(list);
    }

    /**
     * 导出实时定位列表
     */
    @ApiOperation("导出实时定位列表")
    @PreAuthorize("@ss.hasPermi('system:position_current:export')")
    @Log(title = "实时定位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PositionCurrent positionCurrent)
    {
        List<PositionCurrent> list = positionCurrentService.selectPositionCurrentList(positionCurrent);
        ExcelUtil<PositionCurrent> util = new ExcelUtil<PositionCurrent>(PositionCurrent.class);
        util.exportExcel(response, list, "实时定位数据");
    }

    /**
     * 获取实时定位详细信息
     */
    @ApiOperation("获取实时定位详细信息")
    @PreAuthorize("@ss.hasPermi('system:position_current:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(positionCurrentService.selectPositionCurrentById(id));
    }

    /**
     * 新增实时定位
     */
    @ApiOperation("新增实时定位")
    @PreAuthorize("@ss.hasPermi('system:position_current:add')")
    @Log(title = "实时定位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PositionCurrent positionCurrent)
    {
        return toAjax(positionCurrentService.insertPositionCurrent(positionCurrent));
    }

    /**
     * 修改实时定位
     */
    @ApiOperation("修改实时定位")
    @PreAuthorize("@ss.hasPermi('system:position_current:edit')")
    @Log(title = "实时定位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PositionCurrent positionCurrent)
    {
        return toAjax(positionCurrentService.updatePositionCurrent(positionCurrent));
    }

    /**
     * 删除实时定位
     */
    @ApiOperation("删除实时定位")
    @PreAuthorize("@ss.hasPermi('system:position_current:remove')")
    @Log(title = "实时定位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(positionCurrentService.deletePositionCurrentByIds(ids));
    }
}
