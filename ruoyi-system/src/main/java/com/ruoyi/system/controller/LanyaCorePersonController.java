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
import com.ruoyi.system.domain.LanyaCorePerson;
import com.ruoyi.system.service.ILanyaCorePersonService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人员（员工/访客/承包商人员）Controller
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_core_person")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaCorePersonController extends BaseController
{
    @Autowired
    private ILanyaCorePersonService lanyaCorePersonService;

    /**
     * 查询人员（员工/访客/承包商人员）列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaCorePerson lanyaCorePerson)
    {
        startPage();
        List<LanyaCorePerson> list = lanyaCorePersonService.selectLanyaCorePersonList(lanyaCorePerson);
        return getDataTable(list);
    }

    /**
     * 导出人员（员工/访客/承包商人员）列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:export')")
    @Log(title = "人员（员工/访客/承包商人员）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaCorePerson lanyaCorePerson)
    {
        List<LanyaCorePerson> list = lanyaCorePersonService.selectLanyaCorePersonList(lanyaCorePerson);
        ExcelUtil<LanyaCorePerson> util = new ExcelUtil<LanyaCorePerson>(LanyaCorePerson.class);
        util.exportExcel(response, list, "人员（员工/访客/承包商人员）数据");
    }

    /**
     * 获取人员（员工/访客/承包商人员）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:query')")
    @GetMapping(value = "/{personId}")
    public AjaxResult getInfo(@PathVariable("personId") Long personId)
    {
        return success(lanyaCorePersonService.selectLanyaCorePersonByPersonId(personId));
    }

    /**
     * 新增人员（员工/访客/承包商人员）
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:add')")
    @Log(title = "人员（员工/访客/承包商人员）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaCorePerson lanyaCorePerson)
    {
        return toAjax(lanyaCorePersonService.insertLanyaCorePerson(lanyaCorePerson));
    }

    /**
     * 修改人员（员工/访客/承包商人员）
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:edit')")
    @Log(title = "人员（员工/访客/承包商人员）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaCorePerson lanyaCorePerson)
    {
        return toAjax(lanyaCorePersonService.updateLanyaCorePerson(lanyaCorePerson));
    }

    /**
     * 删除人员（员工/访客/承包商人员）
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_core_person:remove')")
    @Log(title = "人员（员工/访客/承包商人员）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{personIds}")
    public AjaxResult remove(@PathVariable Long[] personIds)
    {
        return toAjax(lanyaCorePersonService.deleteLanyaCorePersonByPersonIds(personIds));
    }
}
