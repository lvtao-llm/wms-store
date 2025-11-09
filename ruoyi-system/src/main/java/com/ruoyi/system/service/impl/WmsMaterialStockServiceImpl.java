package com.ruoyi.system.service.impl;

import java.util.List;
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
public class WmsMaterialStockServiceImpl implements IWmsMaterialStockService 
{
    @Autowired
    private WmsMaterialStockMapper wmsMaterialStockMapper;

    /**
     * 查询库存视图
     * 
     * @param inventoryId 库存视图主键
     * @return 库存视图
     */
    @Override
    public WmsMaterialStock selectWmsMaterialStockByInventoryId(String inventoryId)
    {
        return wmsMaterialStockMapper.selectWmsMaterialStockByInventoryId(inventoryId);
    }

    /**
     * 查询库存视图列表
     * 
     * @param wmsMaterialStock 库存视图
     * @return 库存视图
     */
    @Override
    public List<WmsMaterialStock> selectWmsMaterialStockList(WmsMaterialStock wmsMaterialStock)
    {
        return wmsMaterialStockMapper.selectWmsMaterialStockList(wmsMaterialStock);
    }

    /**
     * 新增库存视图
     * 
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    @Override
    public int insertWmsMaterialStock(WmsMaterialStock wmsMaterialStock)
    {
        return wmsMaterialStockMapper.insertWmsMaterialStock(wmsMaterialStock);
    }

    /**
     * 修改库存视图
     * 
     * @param wmsMaterialStock 库存视图
     * @return 结果
     */
    @Override
    public int updateWmsMaterialStock(WmsMaterialStock wmsMaterialStock)
    {
        return wmsMaterialStockMapper.updateWmsMaterialStock(wmsMaterialStock);
    }

    /**
     * 批量删除库存视图
     * 
     * @param inventoryIds 需要删除的库存视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStockByInventoryIds(String[] inventoryIds)
    {
        return wmsMaterialStockMapper.deleteWmsMaterialStockByInventoryIds(inventoryIds);
    }

    /**
     * 删除库存视图信息
     * 
     * @param inventoryId 库存视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStockByInventoryId(String inventoryId)
    {
        return wmsMaterialStockMapper.deleteWmsMaterialStockByInventoryId(inventoryId);
    }

    @Override
    public int deleteWmsMaterialStockAll() {
        return wmsMaterialStockMapper.deleteWmsMaterialStockAll();
    }
}
