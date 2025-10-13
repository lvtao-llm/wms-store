package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.system.domain.WmsCardRecord;
import com.ruoyi.system.service.IWmsCardRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 发卡记录Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/system/record")
public class WmsCardRecordController extends BaseController
{
    @Autowired
    private IWmsCardRecordService wmsCardRecordService;

    /**
     * 查询发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsCardRecord wmsCardRecord)
    {
        startPage();
        List<WmsCardRecord> list = wmsCardRecordService.selectWmsCardRecordList(wmsCardRecord);
        return getDataTable(list);
    }

    /**
     * 导出发卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "发卡记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsCardRecord wmsCardRecord)
    {
        List<WmsCardRecord> list = wmsCardRecordService.selectWmsCardRecordList(wmsCardRecord);
        ExcelUtil<WmsCardRecord> util = new ExcelUtil<WmsCardRecord>(WmsCardRecord.class);
        util.exportExcel(response, list, "发卡记录数据");
    }

    /**
     * 获取发卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{cardRecordId}")
    public AjaxResult getInfo(@PathVariable("cardRecordId") Long cardRecordId)
    {
        return success(wmsCardRecordService.selectWmsCardRecordByCardRecordId(cardRecordId));
    }

    /**
     * 新增发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "发卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsCardRecord wmsCardRecord)
    {
        return toAjax(wmsCardRecordService.insertWmsCardRecord(wmsCardRecord));
    }

    /**
     * 修改发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "发卡记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsCardRecord wmsCardRecord)
    {
        return toAjax(wmsCardRecordService.updateWmsCardRecord(wmsCardRecord));
    }

    /**
     * 删除发卡记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "发卡记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cardRecordIds}")
    public AjaxResult remove(@PathVariable Long[] cardRecordIds)
    {
        return toAjax(wmsCardRecordService.deleteWmsCardRecordByCardRecordIds(cardRecordIds));
    }
}
