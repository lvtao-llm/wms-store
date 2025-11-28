package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialOutMapper;
import com.ruoyi.system.domain.WmsMaterialOut;
import com.ruoyi.system.service.IWmsMaterialOutService;

/**
 * 调拨视图Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
@Service
public class WmsMaterialOutServiceImpl implements IWmsMaterialOutService 
{
    @Autowired
    private WmsMaterialOutMapper wmsMaterialOutMapper;

    /**
     * 查询调拨视图
     * 
     * @param allotDetailId 调拨视图主键
     * @return 调拨视图
     */
    @Override
    public WmsMaterialOut selectWmsMaterialOutByAllotDetailId(String allotDetailId)
    {
        return wmsMaterialOutMapper.selectWmsMaterialOutByAllotDetailId(allotDetailId);
    }

    /**
     * 查询调拨视图列表
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 调拨视图
     */
    @Override
    public List<WmsMaterialOut> selectWmsMaterialOutList(WmsMaterialOut wmsMaterialOut)
    {
        return wmsMaterialOutMapper.selectWmsMaterialOutList(wmsMaterialOut);
    }

    /**
     * 新增调拨视图
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 结果
     */
    @Override
    public int insertWmsMaterialOut(WmsMaterialOut wmsMaterialOut)
    {
        return wmsMaterialOutMapper.insertWmsMaterialOut(wmsMaterialOut);
    }

    /**
     * 修改调拨视图
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 结果
     */
    @Override
    public int updateWmsMaterialOut(WmsMaterialOut wmsMaterialOut)
    {
        return wmsMaterialOutMapper.updateWmsMaterialOut(wmsMaterialOut);
    }

    /**
     * 批量删除调拨视图
     * 
     * @param allotDetailIds 需要删除的调拨视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialOutByAllotDetailIds(String[] allotDetailIds)
    {
        return wmsMaterialOutMapper.deleteWmsMaterialOutByAllotDetailIds(allotDetailIds);
    }

    /**
     * 删除调拨视图信息
     * 
     * @param allotDetailId 调拨视图主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialOutByAllotDetailId(String allotDetailId)
    {
        return wmsMaterialOutMapper.deleteWmsMaterialOutByAllotDetailId(allotDetailId);
    }

    @Override
    public List<WmsMaterialOut> selectWmsMaterialOutListByDateRange(Date start, Date end) {
        return wmsMaterialOutMapper.selectWmsMaterialOutListByDateRange(start, end);
    }

    @Override
    public List<WmsMaterialOut> selectWmsMaterialOutListByAreaNames(WmsMaterialOut wmsMaterialOut, List<String> wzbm) {
        return wmsMaterialOutMapper.selectWmsMaterialOutListByAreaNames(wmsMaterialOut, wzbm);
    }
}
