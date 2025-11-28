package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.service.IWmsAreaService;
import com.ruoyi.system.service.IWmsMaterialDescService;
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
import com.ruoyi.system.domain.WmsMaterialStock;
import com.ruoyi.system.service.IWmsMaterialStockService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 库存视图Controller
 *
 * @author ruoyi
 * @date 2025-11-08
 */
@RestController
@RequestMapping("/system/wms_material_stock")
public class WmsMaterialStockController extends BaseController {
    @Autowired
    private IWmsMaterialStockService wmsMaterialStockService;

    @Autowired
    private IWmsAreaService wmsAreaService;

    @Autowired
    private IWmsMaterialDescService wmsMaterialDescService;

    /**
     * 查询库存视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsMaterialStock wmsMaterialStock) {

        startPage();
        List<WmsMaterialStock> list = wmsMaterialStockService.selectWmsMaterialStockList(wmsMaterialStock);
        return getDataTable(list);
    }

    @GetMapping("/list/{areaName}")
    public TableDataInfo list(WmsMaterialStock wmsMaterialStock, @PathVariable(value = "areaName", required = false) String areaName) {
        startPage();
        List<WmsMaterialStock> list = new ArrayList<>();
        if (areaName != null) {
            WmsArea wmsArea = new WmsArea();
            wmsArea.setAreaName(areaName);
            List<WmsArea> wmsAreas = wmsAreaService.selectWmsAreaList(wmsArea);
            if (wmsAreas.isEmpty()) {
                return getDataTable(list);
            }
            wmsArea = wmsAreas.get(0);
            List<WmsMaterialDesc> wmsMaterialDescs = wmsMaterialDescService.selectWmsMaterialDescList(new WmsMaterialDesc());
            List<String> wzbm = new ArrayList<>();
            for (WmsMaterialDesc desc : wmsMaterialDescs) {
                if (desc.getAreaCodes() == null) {
                    continue;
                }
                String[] ids = desc.getAreaCodes().split(",");
                String id = wmsArea.getAreaId() + "";
                if (Arrays.asList(ids).contains(id)) {
                    wzbm.add(desc.getWzmc());
                }
            }
            if(wzbm.isEmpty()){
                return getDataTable(list);
            }
            list = wmsMaterialStockService.selectWmsMaterialStockListByAreaNames(wmsMaterialStock, wzbm);
        } else {
            list = wmsMaterialStockService.selectWmsMaterialStockList(wmsMaterialStock);
        }

        return getDataTable(list);
    }

    /**
     * 导出库存视图列表
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:export')")
    @Log(title = "库存视图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsMaterialStock wmsMaterialStock) {
        List<WmsMaterialStock> list = wmsMaterialStockService.selectWmsMaterialStockList(wmsMaterialStock);
        ExcelUtil<WmsMaterialStock> util = new ExcelUtil<WmsMaterialStock>(WmsMaterialStock.class);
        util.exportExcel(response, list, "库存视图数据");
    }

    /**
     * 获取库存视图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:query')")
    @GetMapping(value = "/{inventoryId}")
    public AjaxResult getInfo(@PathVariable("inventoryId") String inventoryId) {
        return success(wmsMaterialStockService.selectWmsMaterialStockByInventoryId(inventoryId));
    }

    /**
     * 新增库存视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:add')")
    @Log(title = "库存视图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsMaterialStock wmsMaterialStock) {
        return toAjax(wmsMaterialStockService.insertWmsMaterialStock(wmsMaterialStock));
    }

    /**
     * 修改库存视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:edit')")
    @Log(title = "库存视图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsMaterialStock wmsMaterialStock) {
        return toAjax(wmsMaterialStockService.updateWmsMaterialStock(wmsMaterialStock));
    }

    /**
     * 删除库存视图
     */
    @PreAuthorize("@ss.hasPermi('system:wms_material_stock:remove')")
    @Log(title = "库存视图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inventoryIds}")
    public AjaxResult remove(@PathVariable String[] inventoryIds) {
        return toAjax(wmsMaterialStockService.deleteWmsMaterialStockByInventoryIds(inventoryIds));
    }
}
