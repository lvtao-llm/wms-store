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
import com.ruoyi.system.domain.WmsMaterialIdentifyRecord;
import com.ruoyi.system.service.IWmsMaterialIdentifyRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料识别记录Controller
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@RestController
@RequestMapping("/system/wms_material_identify_record")
public class WmsMaterialIdentifyRecordController extends BaseController
{
    @Autowired
    private IWmsMaterialIdentifyRecordService wmsMaterialIdentifyRecordService;

    /**
     * 查询物料识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        startPage();
        List<WmsMaterialIdentifyRecord> list = wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordList(wmsMaterialIdentifyRecord);
        return getDataTable(list);
    }

    /**
     * 导出物料识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:export')")
    @Log(title = "物料识别记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        List<WmsMaterialIdentifyRecord> list = wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordList(wmsMaterialIdentifyRecord);
        ExcelUtil<WmsMaterialIdentifyRecord> util = new ExcelUtil<WmsMaterialIdentifyRecord>(WmsMaterialIdentifyRecord.class);
        util.exportExcel(response, list, "物料识别记录数据");
    }

    /**
     * 获取物料识别记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:query')")
    @GetMapping(value = "/{ymd}")
    public AjaxResult getInfo(@PathVariable("ymd") String ymd)
    {
        return success(wmsMaterialIdentifyRecordService.selectWmsMaterialIdentifyRecordByYmd(ymd));
    }

    /**
     * 新增物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:add')")
    @Log(title = "物料识别记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        return toAjax(wmsMaterialIdentifyRecordService.insertWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord));
    }

    /**
     * 修改物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:edit')")
    @Log(title = "物料识别记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialIdentifyRecord wmsMaterialIdentifyRecord)
    {
        return toAjax(wmsMaterialIdentifyRecordService.updateWmsMaterialIdentifyRecord(wmsMaterialIdentifyRecord));
    }

    /**
     * 删除物料识别记录
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_identify_record:remove')")
    @Log(title = "物料识别记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ymds}")
    public AjaxResult remove(@PathVariable String[] ymds)
    {
        return toAjax(wmsMaterialIdentifyRecordService.deleteWmsMaterialIdentifyRecordByYmds(ymds));
    }
}
