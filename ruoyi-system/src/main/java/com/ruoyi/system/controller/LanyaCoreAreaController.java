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
import com.ruoyi.system.domain.LanyaCoreArea;
import com.ruoyi.system.service.ILanyaCoreAreaService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 区域信息Controller
 *
 * @author 吕涛
 * @date 2025-10-10
 */
@Api(value = "区域信息", tags = {"卡机端", "区域管理"})
@RestController
@RequestMapping("/system/lanya_core_area")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaCoreAreaController extends BaseController {
    @Autowired
    private ILanyaCoreAreaService lanyaCoreAreaService;

    /**
     * 查询区域信息列表
     */
    @ApiOperation(value = "查询区域信息列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaCoreArea lanyaCoreArea) {
        startPage();
        List<LanyaCoreArea> list = lanyaCoreAreaService.selectLanyaCoreAreaList(lanyaCoreArea);
        return getDataTable(list);
    }

    /**
     * 导出区域信息列表
     */
    @ApiOperation(value = "导出区域信息列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:export')")
    @Log(title = "区域信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaCoreArea lanyaCoreArea) {
        List<LanyaCoreArea> list = lanyaCoreAreaService.selectLanyaCoreAreaList(lanyaCoreArea);
        ExcelUtil<LanyaCoreArea> util = new ExcelUtil<LanyaCoreArea>(LanyaCoreArea.class);
        util.exportExcel(response, list, "区域信息数据");
    }

    /**
     * 获取区域信息详细信息
     */
    @ApiOperation(value = "获取区域信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:query')")
    @GetMapping(value = "/{areaId}")
    public AjaxResult getInfo(@PathVariable("areaId") Long areaId) {
        return success(lanyaCoreAreaService.selectLanyaCoreAreaByAreaId(areaId));
    }

    /**
     * 新增区域信息
     */
    @ApiOperation(value = "新增区域信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:add')")
    @Log(title = "区域信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaCoreArea lanyaCoreArea) {
        return toAjax(lanyaCoreAreaService.insertLanyaCoreArea(lanyaCoreArea));
    }

    /**
     * 修改区域信息
     */
    @ApiOperation(value = "修改区域信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:edit')")
    @Log(title = "区域信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaCoreArea lanyaCoreArea) {
        return toAjax(lanyaCoreAreaService.updateLanyaCoreArea(lanyaCoreArea));
    }

    /**
     * 删除区域信息
     */
    @ApiOperation(value = "删除区域信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_area:remove')")
    @Log(title = "区域信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{areaIds}")
    public AjaxResult remove(@PathVariable Long[] areaIds) {
        return toAjax(lanyaCoreAreaService.deleteLanyaCoreAreaByAreaIds(areaIds));
    }
}
