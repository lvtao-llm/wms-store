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
import com.ruoyi.system.domain.LanyaDeviceCardMachine;
import com.ruoyi.system.service.ILanyaDeviceCardMachineService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 发卡机Controller
 *
 * @author ruoyi
 * @date 2025-10-12
 */
@Api(value = "发卡机管理", tags = {"卡机端", "发卡机管理", "设备管理"})
@RestController
@RequestMapping("/system/lanya_device_machine")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaDeviceCardMachineController extends BaseController {
    @Autowired
    private ILanyaDeviceCardMachineService lanyaDeviceCardMachineService;

    /**
     * 查询发卡机列表
     */
    @ApiOperation("查询发卡机列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaDeviceCardMachine lanyaDeviceCardMachine) {
        startPage();
        List<LanyaDeviceCardMachine> list = lanyaDeviceCardMachineService.selectLanyaDeviceCardMachineList(lanyaDeviceCardMachine);
        return getDataTable(list);
    }

    /**
     * 导出发卡机列表
     */
    @ApiOperation("导出发卡机列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:export')")
    @Log(title = "发卡机", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaDeviceCardMachine lanyaDeviceCardMachine) {
        List<LanyaDeviceCardMachine> list = lanyaDeviceCardMachineService.selectLanyaDeviceCardMachineList(lanyaDeviceCardMachine);
        ExcelUtil<LanyaDeviceCardMachine> util = new ExcelUtil<LanyaDeviceCardMachine>(LanyaDeviceCardMachine.class);
        util.exportExcel(response, list, "发卡机数据");
    }

    /**
     * 获取发卡机详细信息
     */
    @ApiOperation("获取发卡机详细信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:query')")
    @GetMapping(value = "/{cardSenderId}")
    public AjaxResult getInfo(@PathVariable("cardSenderId") Long cardSenderId) {
        return success(lanyaDeviceCardMachineService.selectLanyaDeviceCardMachineByCardSenderId(cardSenderId));
    }

    /**
     * 新增发卡机
     */
    @ApiOperation("新增发卡机")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:add')")
    @Log(title = "发卡机", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaDeviceCardMachine lanyaDeviceCardMachine) {
        return toAjax(lanyaDeviceCardMachineService.insertLanyaDeviceCardMachine(lanyaDeviceCardMachine));
    }

    /**
     * 修改发卡机
     */
    @ApiOperation("修改发卡机")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:edit')")
    @Log(title = "发卡机", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaDeviceCardMachine lanyaDeviceCardMachine) {
        return toAjax(lanyaDeviceCardMachineService.updateLanyaDeviceCardMachine(lanyaDeviceCardMachine));
    }

    /**
     * 删除发卡机
     */
    @ApiOperation("删除发卡机")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_machine:remove')")
    @Log(title = "发卡机", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardSenderIds}")
    public AjaxResult remove(@PathVariable Long[] cardSenderIds) {
        return toAjax(lanyaDeviceCardMachineService.deleteLanyaDeviceCardMachineByCardSenderIds(cardSenderIds));
    }
}
