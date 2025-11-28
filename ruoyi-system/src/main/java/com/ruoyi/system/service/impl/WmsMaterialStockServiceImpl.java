package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ruoyi.system.domain.WmsArea;
import com.ruoyi.system.service.IWmsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialStockMapper;
import com.ruoyi.system.domain.WmsMaterialStock;
import com.ruoyi.system.service.IWmsMaterialStockService;

/**
 * 库存视图Service业务层处理
 *
 * @author ruoyi
 * @date 2025-11-08
 */
@Service
public class WmsMaterialStockServiceImpl implements IWmsMaterialStockService {
    @Autowired
    private WmsMaterialStockMapper wmsMaterialStockMapper;
    @Autowired
    private IWmsAreaService wmsAreaService;

    /**
     * 查询库存视图
     *
     * @param inventoryId 库存视图主键
     * @return 库存视图
     */
    @Override
    public WmsMaterialStock selectWmsMaterialStockByInventoryId(String inventoryId) {
        return wmsMaterialStockMapper.selectWmsMaterialStockByInventoryId(inventoryId);
    }

    /**
     * 查询库存视图列表
     *
     * @param wmsMaterialStock 库存视图
     * @return 库存视图
     */
    @Override
    public List<WmsMaterialStock> selectWmsMaterialStockList(WmsMaterialStock wmsMaterialStock) {
        List<WmsArea> wmsAreas = wmsAreaService.selectWmsAreaList(new WmsArea());
        List<WmsMaterialStock> wmsMaterialStocks = wmsMaterialStockMapper.selectWmsMaterialStockList(wmsMaterialStock);
        for (WmsMaterialStock stock : wmsMaterialStocks) {
            List<String> areaName = new ArrayList<>();
            if (stock.getAreaCodes() != null) {
                for (String areaCode : stock.getAreaCodes().split(",")) {
                    for (WmsArea wmsArea : wmsAreas) {
                        if (wmsArea.getAreaId() != null && areaCode != null) {
                            if (areaCode.equals(wmsArea.getAreaId() + "")) {
                                areaName.add(wmsArea.getAreaName());
                            }
                        }
                    }
                }
            }
            stock.setAreaNames(areaName);
        }
        return wmsMaterialStocks;
    }

    /**
     * 新增库存视图
     *
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    @Override
    public int insertWmsMaterialStock(WmsMaterialStock wmsMaterialStock) {
        return wmsMaterialStockMapper.insertWmsMaterialStock(wmsMaterialStock);
    }

    /**
     * 修改库存视图
     *
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    @Override
    public int updateWmsMaterialStock(WmsMaterialStock wmsMaterialStock) {
        return wmsMaterialStockMapper.updateWmsMaterialStock(wmsMaterialStock);
    }

    /**
     * 批量删除库存视图
     *
     * @param inventoryIds 需要删除的库存视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStockByInventoryIds(String[] inventoryIds) {
        return wmsMaterialStockMapper.deleteWmsMaterialStockByInventoryIds(inventoryIds);
    }

    /**
     * 删除库存视图信息
     *
     * @param inventoryId 库存视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStockByInventoryId(String inventoryId) {
        return wmsMaterialStockMapper.deleteWmsMaterialStockByInventoryId(inventoryId);
    }

    @Override
    public int deleteWmsMaterialStockAll() {
        return wmsMaterialStockMapper.deleteWmsMaterialStockAll();
    }

    @Override
    public List<WmsMaterialStock> selectWmsMaterialStockListByAreaNames(WmsMaterialStock wmsMaterialStock, List<String> wzbm) {
        return wmsMaterialStockMapper.selectWmsMaterialStockListByAreaNames(wmsMaterialStock, wzbm);
    }
}
