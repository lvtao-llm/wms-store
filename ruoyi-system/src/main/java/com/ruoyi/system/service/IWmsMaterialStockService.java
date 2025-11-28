package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialStock;

/**
 * 库存视图Service接口
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
public interface IWmsMaterialStockService 
{
    /**
     * 查询库存视图
     * 
     * @param inventoryId 库存视图主键
     * @return 库存视图
     */
    public WmsMaterialStock selectWmsMaterialStockByInventoryId(String inventoryId);

    /**
     * 查询库存视图列表
     * 
     * @param wmsMaterialStock 库存视图
     * @return 库存视图集合
     */
    public List<WmsMaterialStock> selectWmsMaterialStockList(WmsMaterialStock wmsMaterialStock);

    /**
     * 新增库存视图
     * 
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    public int insertWmsMaterialStock(WmsMaterialStock wmsMaterialStock);

    /**
     * 修改库存视图
     * 
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    public int updateWmsMaterialStock(WmsMaterialStock wmsMaterialStock);

    /**
     * 批量删除库存视图
     * 
     * @param inventoryIds 需要删除的库存视图主键集合
     * @return 结果
     */
    public int deleteWmsMaterialStockByInventoryIds(String[] inventoryIds);

    /**
     * 删除库存视图信息
     * 
     * @param inventoryId 库存视图主键
     * @return 结果
     */
    public int deleteWmsMaterialStockByInventoryId(String inventoryId);

    int deleteWmsMaterialStockAll();

    List<WmsMaterialStock> selectWmsMaterialStockListByAreaNames(WmsMaterialStock wmsMaterialStock, List<String> wzbm);
}
