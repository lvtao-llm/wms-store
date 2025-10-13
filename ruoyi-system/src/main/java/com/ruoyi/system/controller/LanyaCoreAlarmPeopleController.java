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
import com.ruoyi.system.domain.LanyaCoreAlarmPeople;
import com.ruoyi.system.service.ILanyaCoreAlarmPeopleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人员报警Controller
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_core_alarm_people")
@DataSource(DataSourceType.SLAVE)
public class LanyaCoreAlarmPeopleController extends BaseController
{
    @Autowired
    private ILanyaCoreAlarmPeopleService lanyaCoreAlarmPeopleService;

    /**
     * 查询人员报警列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        startPage();
        List<LanyaCoreAlarmPeople> list = lanyaCoreAlarmPeopleService.selectLanyaCoreAlarmPeopleList(lanyaCoreAlarmPeople);
        return getDataTable(list);
    }

    /**
     * 导出人员报警列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:export')")
    @Log(title = "人员报警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        List<LanyaCoreAlarmPeople> list = lanyaCoreAlarmPeopleService.selectLanyaCoreAlarmPeopleList(lanyaCoreAlarmPeople);
        ExcelUtil<LanyaCoreAlarmPeople> util = new ExcelUtil<LanyaCoreAlarmPeople>(LanyaCoreAlarmPeople.class);
        util.exportExcel(response, list, "人员报警数据");
    }

    /**
     * 获取人员报警详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lanyaCoreAlarmPeopleService.selectLanyaCoreAlarmPeopleById(id));
    }

    /**
     * 新增人员报警
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:add')")
    @Log(title = "人员报警", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        return toAjax(lanyaCoreAlarmPeopleService.insertLanyaCoreAlarmPeople(lanyaCoreAlarmPeople));
    }

    /**
     * 修改人员报警
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:edit')")
    @Log(title = "人员报警", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaCoreAlarmPeople lanyaCoreAlarmPeople)
    {
        return toAjax(lanyaCoreAlarmPeopleService.updateLanyaCoreAlarmPeople(lanyaCoreAlarmPeople));
    }

    /**
     * 删除人员报警
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_alarm_people:remove')")
    @Log(title = "人员报警", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lanyaCoreAlarmPeopleService.deleteLanyaCoreAlarmPeopleByIds(ids));
    }
}
