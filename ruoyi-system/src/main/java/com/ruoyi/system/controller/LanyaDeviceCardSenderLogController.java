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
import com.ruoyi.system.domain.LanyaDeviceCardSenderLog;
import com.ruoyi.system.service.ILanyaDeviceCardSenderLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人脸发卡记录Controller
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@RestController
@RequestMapping("/system/lanya_device_card_sender_log")
@DataSource(value = DataSourceType.SLAVE)
public class LanyaDeviceCardSenderLogController extends BaseController
{
    @Autowired
    private ILanyaDeviceCardSenderLogService lanyaDeviceCardSenderLogService;

    /**
     * 查询人脸发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog)
    {
        startPage();
        List<LanyaDeviceCardSenderLog> list = lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogList(lanyaDeviceCardSenderLog);
        return getDataTable(list);
    }

    /**
     * 查询人脸发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:list')")
    @GetMapping("/list-by-name-card-type")
    public TableDataInfo listByNameCardType(String param)
    {
        startPage();
        List<LanyaDeviceCardSenderLog> list = lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogListByNameCardType(param);
        return getDataTable(list);
    }

    /**
     * 导出人脸发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:export')")
    @Log(title = "人脸发卡记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog)
    {
        List<LanyaDeviceCardSenderLog> list = lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogList(lanyaDeviceCardSenderLog);
        ExcelUtil<LanyaDeviceCardSenderLog> util = new ExcelUtil<LanyaDeviceCardSenderLog>(LanyaDeviceCardSenderLog.class);
        util.exportExcel(response, list, "人脸发卡记录数据");
    }

    /**
     * 获取人脸发卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lanyaDeviceCardSenderLogService.selectLanyaDeviceCardSenderLogById(id));
    }

    /**
     * 新增人脸发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:add')")
    @Log(title = "人脸发卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog)
    {
        return toAjax(lanyaDeviceCardSenderLogService.insertLanyaDeviceCardSenderLog(lanyaDeviceCardSenderLog));
    }

    /**
     * 修改人脸发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:edit')")
    @Log(title = "人脸发卡记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LanyaDeviceCardSenderLog lanyaDeviceCardSenderLog)
    {
        return toAjax(lanyaDeviceCardSenderLogService.updateLanyaDeviceCardSenderLog(lanyaDeviceCardSenderLog));
    }

    /**
     * 删除人脸发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:lanya_device_card_sender_log:remove')")
    @Log(title = "人脸发卡记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lanyaDeviceCardSenderLogService.deleteLanyaDeviceCardSenderLogByIds(ids));
    }
}
