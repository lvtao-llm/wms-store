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
import com.ruoyi.system.domain.LanyaVehicleInfoVisitor;
import com.ruoyi.system.service.ILanyaVehicleInfoVisitorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客车辆Controller
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya-vehicleInfo-visitor")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaVehicleInfoVisitorController extends BaseController
{
    @Autowired
    private ILanyaVehicleInfoVisitorService lanyaVehicleInfoVisitorService;

    /**
     * 查询访客车辆列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        startPage();
        List<LanyaVehicleInfoVisitor> list = lanyaVehicleInfoVisitorService.selectLanyaVehicleInfoVisitorList(lanyaVehicleInfoVisitor);
        return getDataTable(list);
    }

    /**
     * 导出访客车辆列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:export')")
    @Log(title = "访客车辆", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        List<LanyaVehicleInfoVisitor> list = lanyaVehicleInfoVisitorService.selectLanyaVehicleInfoVisitorList(lanyaVehicleInfoVisitor);
        ExcelUtil<LanyaVehicleInfoVisitor> util = new ExcelUtil<LanyaVehicleInfoVisitor>(LanyaVehicleInfoVisitor.class);
        util.exportExcel(response, list, "访客车辆数据");
    }

    /**
     * 获取访客车辆详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lanyaVehicleInfoVisitorService.selectLanyaVehicleInfoVisitorById(id));
    }

    /**
     * 新增访客车辆
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:add')")
    @Log(title = "访客车辆", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        return toAjax(lanyaVehicleInfoVisitorService.insertLanyaVehicleInfoVisitor(lanyaVehicleInfoVisitor));
    }

    /**
     * 修改访客车辆
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:edit')")
    @Log(title = "访客车辆", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaVehicleInfoVisitor lanyaVehicleInfoVisitor)
    {
        return toAjax(lanyaVehicleInfoVisitorService.updateLanyaVehicleInfoVisitor(lanyaVehicleInfoVisitor));
    }

    /**
     * 删除访客车辆
     */
    @PreAuthorize("@ss.hasPermi('system:lanya-vehicleInfo-visitor:remove')")
    @Log(title = "访客车辆", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lanyaVehicleInfoVisitorService.deleteLanyaVehicleInfoVisitorByIds(ids));
    }
}
