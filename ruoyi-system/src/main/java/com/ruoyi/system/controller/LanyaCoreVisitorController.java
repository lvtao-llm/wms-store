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
import com.ruoyi.system.domain.LanyaCoreVisitor;
import com.ruoyi.system.service.ILanyaCoreVisitorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客记录Controller
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@Api(value = "访客记录", tags = {"卡机端", "访客记录"})
@RestController
@RequestMapping("/system/lanya_core_visitor")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaCoreVisitorController extends BaseController {
    @Autowired
    private ILanyaCoreVisitorService lanyaCoreVisitorService;

    /**
     * 查询访客记录列表
     */
    @ApiOperation("查询访客记录列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaCoreVisitor lanyaCoreVisitor) {
        startPage();
        List<LanyaCoreVisitor> list = lanyaCoreVisitorService.selectLanyaCoreVisitorList(lanyaCoreVisitor);
        return getDataTable(list);
    }

    /**
     * 导出访客记录列表
     */
    @ApiOperation("导出访客记录列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:export')")
    @Log(title = "访客记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaCoreVisitor lanyaCoreVisitor) {
        List<LanyaCoreVisitor> list = lanyaCoreVisitorService.selectLanyaCoreVisitorList(lanyaCoreVisitor);
        ExcelUtil<LanyaCoreVisitor> util = new ExcelUtil<LanyaCoreVisitor>(LanyaCoreVisitor.class);
        util.exportExcel(response, list, "访客记录数据");
    }

    /**
     * 获取访客记录详细信息
     */
    @ApiOperation("获取访客记录详细信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:query')")
    @GetMapping(value = "/{visitorId}")
    public AjaxResult getInfo(@PathVariable("visitorId") Long visitorId) {
        return success(lanyaCoreVisitorService.selectLanyaCoreVisitorByVisitorId(visitorId));
    }

    /**
     * 新增访客记录
     */
    @ApiOperation("新增访客记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:add')")
    @Log(title = "访客记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaCoreVisitor lanyaCoreVisitor) {
        return toAjax(lanyaCoreVisitorService.insertLanyaCoreVisitor(lanyaCoreVisitor));
    }

    /**
     * 修改访客记录
     */
    @ApiOperation("修改访客记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:edit')")
    @Log(title = "访客记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaCoreVisitor lanyaCoreVisitor) {
        return toAjax(lanyaCoreVisitorService.updateLanyaCoreVisitor(lanyaCoreVisitor));
    }

    /**
     * 删除访客记录
     */
    @ApiOperation("删除访客记录")
    @PreAuthorize("@ss.hasPermi('system:lanya_core_visitor:remove')")
    @Log(title = "访客记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{visitorIds}")
    public AjaxResult remove(@PathVariable Long[] visitorIds) {
        return toAjax(lanyaCoreVisitorService.deleteLanyaCoreVisitorByVisitorIds(visitorIds));
    }
}
