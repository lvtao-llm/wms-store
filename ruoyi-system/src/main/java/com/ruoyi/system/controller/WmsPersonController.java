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
import com.ruoyi.system.domain.WmsPerson;
import com.ruoyi.system.service.IWmsPersonService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人员档案Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Api("人员档案")
@RestController
@RequestMapping("/system/person")
public class WmsPersonController extends BaseController
{
    @Autowired
    private IWmsPersonService wmsPersonService;

    /**
     * 查询人员档案列表
     */
    @ApiOperation("查询人员档案列表")
    @PreAuthorize("@ss.hasPermi('system:person:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsPerson wmsPerson)
    {
        startPage();
        List<WmsPerson> list = wmsPersonService.selectWmsPersonList(wmsPerson);
        return getDataTable(list);
    }

    /**
     * 导出人员档案列表
     */
    @ApiOperation("导出人员档案列表")
    @PreAuthorize("@ss.hasPermi('system:person:export')")
    @Log(title = "人员档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsPerson wmsPerson)
    {
        List<WmsPerson> list = wmsPersonService.selectWmsPersonList(wmsPerson);
        ExcelUtil<WmsPerson> util = new ExcelUtil<WmsPerson>(WmsPerson.class);
        util.exportExcel(response, list, "人员档案数据");
    }

    /**
     * 获取人员档案详细信息
     */
    @ApiOperation("获取人员档案详细信息")
    @PreAuthorize("@ss.hasPermi('system:person:query')")
    @GetMapping(value = "/{personId}")
    public AjaxResult getInfo(@PathVariable("personId") Long personId)
    {
        return success(wmsPersonService.selectWmsPersonByPersonId(personId));
    }

    /**
     * 新增人员档案
     */
    @ApiOperation("新增人员档案")
    @PreAuthorize("@ss.hasPermi('system:person:add')")
    @Log(title = "人员档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsPerson wmsPerson)
    {
        return toAjax(wmsPersonService.insertWmsPerson(wmsPerson));
    }

    /**
     * 修改人员档案
     */
    @ApiOperation("修改人员档案")
    @PreAuthorize("@ss.hasPermi('system:person:edit')")
    @Log(title = "人员档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsPerson wmsPerson)
    {
        return toAjax(wmsPersonService.updateWmsPerson(wmsPerson));
    }

    /**
     * 删除人员档案
     */
    @ApiOperation("删除人员档案")
    @PreAuthorize("@ss.hasPermi('system:person:remove')")
    @Log(title = "人员档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{personIds}")
    public AjaxResult remove(@PathVariable Long[] personIds)
    {
        return toAjax(wmsPersonService.deleteWmsPersonByPersonIds(personIds));
    }
}
