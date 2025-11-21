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
import com.ruoyi.system.domain.WmsMaterialOutFileSyncQueue;
import com.ruoyi.system.service.IWmsMaterialOutFileSyncQueueService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料出入库相关文件同步队列Controller
 * 
 * @author ruoyi
 * @date 2025-11-20
 */
@RestController
@RequestMapping("/system/wms_material_out_file_sync_queue")
public class WmsMaterialOutFileSyncQueueController extends BaseController
{
    @Autowired
    private IWmsMaterialOutFileSyncQueueService wmsMaterialOutFileSyncQueueService;

    /**
     * 查询物料出入库相关文件同步队列列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue)
    {
        startPage();
        List<WmsMaterialOutFileSyncQueue> list = wmsMaterialOutFileSyncQueueService.selectWmsMaterialOutFileSyncQueueList(wmsMaterialOutFileSyncQueue);
        return getDataTable(list);
    }

    /**
     * 导出物料出入库相关文件同步队列列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:export')")
    @Log(title = "物料出入库相关文件同步队列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue)
    {
        List<WmsMaterialOutFileSyncQueue> list = wmsMaterialOutFileSyncQueueService.selectWmsMaterialOutFileSyncQueueList(wmsMaterialOutFileSyncQueue);
        ExcelUtil<WmsMaterialOutFileSyncQueue> util = new ExcelUtil<WmsMaterialOutFileSyncQueue>(WmsMaterialOutFileSyncQueue.class);
        util.exportExcel(response, list, "物料出入库相关文件同步队列数据");
    }

    /**
     * 获取物料出入库相关文件同步队列详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:query')")
    @GetMapping(value = "/{调拨明细编号}")
    public AjaxResult getInfo(@PathVariable("调拨明细编号") String 调拨明细编号)
    {
        return success(wmsMaterialOutFileSyncQueueService.selectWmsMaterialOutFileSyncQueueBy调拨明细编号(调拨明细编号));
    }

    /**
     * 新增物料出入库相关文件同步队列
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:add')")
    @Log(title = "物料出入库相关文件同步队列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue)
    {
        return toAjax(wmsMaterialOutFileSyncQueueService.insertWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue));
    }

    /**
     * 修改物料出入库相关文件同步队列
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:edit')")
    @Log(title = "物料出入库相关文件同步队列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialOutFileSyncQueue wmsMaterialOutFileSyncQueue)
    {
        return toAjax(wmsMaterialOutFileSyncQueueService.updateWmsMaterialOutFileSyncQueue(wmsMaterialOutFileSyncQueue));
    }

    /**
     * 删除物料出入库相关文件同步队列
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_out_file_sync_queue:remove')")
    @Log(title = "物料出入库相关文件同步队列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{调拨明细编号s}")
    public AjaxResult remove(@PathVariable String[] 调拨明细编号s)
    {
        return toAjax(wmsMaterialOutFileSyncQueueService.deleteWmsMaterialOutFileSyncQueueBy调拨明细编号s(调拨明细编号s));
    }
}
