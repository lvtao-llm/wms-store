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
import com.ruoyi.system.domain.WmsVisitor;
import com.ruoyi.system.service.IWmsVisitorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客信息Controller
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Api(value = "访客信息", tags = {"系统端", "访客信息"})
@RestController
@RequestMapping("/system/visitor")
public class WmsVisitorController extends BaseController {
    @Autowired
    private IWmsVisitorService wmsVisitorService;

    /**
     * 查询访客信息列表
     */
    @ApiOperation("查询访客信息列表")
    @PreAuthorize("@ss.hasPermi('system:visitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsVisitor wmsVisitor) {
        startPage();
        List<WmsVisitor> list = wmsVisitorService.selectWmsVisitorList(wmsVisitor);
        return getDataTable(list);
    }

    /**
     * 导出访客信息列表
     */
    @ApiOperation("导出访客信息列表")
    @PreAuthorize("@ss.hasPermi('system:visitor:export')")
    @Log(title = "访客信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsVisitor wmsVisitor) {
        List<WmsVisitor> list = wmsVisitorService.selectWmsVisitorList(wmsVisitor);
        ExcelUtil<WmsVisitor> util = new ExcelUtil<WmsVisitor>(WmsVisitor.class);
        util.exportExcel(response, list, "访客信息数据");
    }

    /**
     * 获取访客信息详细信息
     */
    @ApiOperation("获取访客信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:visitor:query')")
    @GetMapping(value = "/{visitorId}")
    public AjaxResult getInfo(@PathVariable("visitorId") Long visitorId) {
        return success(wmsVisitorService.selectWmsVisitorByVisitorId(visitorId));
    }

    /**
     * 新增访客信息
     */
    @ApiOperation("新增访客信息")
    @PreAuthorize("@ss.hasPermi('system:visitor:add')")
    @Log(title = "访客信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsVisitor wmsVisitor) {
        return toAjax(wmsVisitorService.insertWmsVisitor(wmsVisitor));
    }

    /**
     * 修改访客信息
     */
    @ApiOperation("修改访客信息")
    @PreAuthorize("@ss.hasPermi('system:visitor:edit')")
    @Log(title = "访客信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsVisitor wmsVisitor) {
        return toAjax(wmsVisitorService.updateWmsVisitor(wmsVisitor));
    }

    /**
     * 删除访客信息
     */
    @ApiOperation("删除访客信息")
    @PreAuthorize("@ss.hasPermi('system:visitor:remove')")
    @Log(title = "访客信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{visitorIds}")
    public AjaxResult remove(@PathVariable Long[] visitorIds) {
        return toAjax(wmsVisitorService.deleteWmsVisitorByVisitorIds(visitorIds));
    }
}
