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
import com.ruoyi.system.domain.LanyaDeviceCard;
import com.ruoyi.system.service.ILanyaDeviceCardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 定位卡管理Controller
 *
 * @author ruoyi
 * @date 2025-10-12
 */
@Api(value = "定位卡管理", tags = {"卡机端", "定位卡管理", "设备管理"})
@RestController
@RequestMapping("/system/lanya_device_card")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaDeviceCardController extends BaseController {
    @Autowired
    private ILanyaDeviceCardService lanyaDeviceCardService;

    /**
     * 查询定位卡管理列表
     */
    @ApiOperation(value = "查询定位卡管理列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaDeviceCard lanyaDeviceCard) {
        startPage();
        List<LanyaDeviceCard> list = lanyaDeviceCardService.selectLanyaDeviceCardList(lanyaDeviceCard);
        return getDataTable(list);
    }

    /**
     * 导出定位卡管理列表
     */
    @ApiOperation(value = "导出定位卡管理列表")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:export')")
    @Log(title = "定位卡管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaDeviceCard lanyaDeviceCard) {
        List<LanyaDeviceCard> list = lanyaDeviceCardService.selectLanyaDeviceCardList(lanyaDeviceCard);
        ExcelUtil<LanyaDeviceCard> util = new ExcelUtil<LanyaDeviceCard>(LanyaDeviceCard.class);
        util.exportExcel(response, list, "定位卡管理数据");
    }

    /**
     * 获取定位卡管理详细信息
     */
    @ApiOperation(value = "获取定位卡管理详细信息")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lanyaDeviceCardService.selectLanyaDeviceCardById(id));
    }

    /**
     * 新增定位卡管理
     */
    @ApiOperation(value = "新增定位卡管理")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:add')")
    @Log(title = "定位卡管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaDeviceCard lanyaDeviceCard) {
        return toAjax(lanyaDeviceCardService.insertLanyaDeviceCard(lanyaDeviceCard));
    }

    /**
     * 修改定位卡管理
     */
    @ApiOperation(value = "修改定位卡管理")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:edit')")
    @Log(title = "定位卡管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaDeviceCard lanyaDeviceCard) {
        return toAjax(lanyaDeviceCardService.updateLanyaDeviceCard(lanyaDeviceCard));
    }

    /**
     * 删除定位卡管理
     */
    @ApiOperation(value = "删除定位卡管理")
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card:remove')")
    @Log(title = "定位卡管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lanyaDeviceCardService.deleteLanyaDeviceCardByIds(ids));
    }
}
