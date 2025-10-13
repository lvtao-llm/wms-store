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
import com.ruoyi.system.domain.LanyaInteralEmployee;
import com.ruoyi.system.service.ILanyaInteralEmployeeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 内部员工Controller
 *
 * @author 吕涛
 * @date 2025-10-12
 */
@RestController
@RequestMapping("/system/lanya_internal_employee")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaInteralEmployeeController extends BaseController {
    @Autowired
    private ILanyaInteralEmployeeService lanyaInteralEmployeeService;

    /**
     * 查询内部员工列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaInteralEmployee lanyaInteralEmployee) {
        startPage();
        List<LanyaInteralEmployee> list = lanyaInteralEmployeeService.selectLanyaInteralEmployeeList(lanyaInteralEmployee);
        return getDataTable(list);
    }

    /**
     * 导出内部员工列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:export')")
    @Log(title = "内部员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaInteralEmployee lanyaInteralEmployee) {
        List<LanyaInteralEmployee> list = lanyaInteralEmployeeService.selectLanyaInteralEmployeeList(lanyaInteralEmployee);
        ExcelUtil<LanyaInteralEmployee> util = new ExcelUtil<LanyaInteralEmployee>(LanyaInteralEmployee.class);
        util.exportExcel(response, list, "内部员工数据");
    }

    /**
     * 获取内部员工详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:query')")
    @GetMapping(value = "/{personId}")
    public AjaxResult getInfo(@PathVariable("personId") Long personId) {
        return success(lanyaInteralEmployeeService.selectLanyaInteralEmployeeByPersonId(personId));
    }

    /**
     * 新增内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:add')")
    @Log(title = "内部员工", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaInteralEmployee lanyaInteralEmployee) {
        return toAjax(lanyaInteralEmployeeService.insertLanyaInteralEmployee(lanyaInteralEmployee));
    }

    /**
     * 修改内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:edit')")
    @Log(title = "内部员工", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaInteralEmployee lanyaInteralEmployee) {
        return toAjax(lanyaInteralEmployeeService.updateLanyaInteralEmployee(lanyaInteralEmployee));
    }

    /**
     * 删除内部员工
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_internal_employee:remove')")
    @Log(title = "内部员工", businessType = BusinessType.DELETE)
    @DeleteMapping("/{personIds}")
    public AjaxResult remove(@PathVariable Long[] personIds) {
        return toAjax(lanyaInteralEmployeeService.deleteLanyaInteralEmployeeByPersonIds(personIds));
    }
}
