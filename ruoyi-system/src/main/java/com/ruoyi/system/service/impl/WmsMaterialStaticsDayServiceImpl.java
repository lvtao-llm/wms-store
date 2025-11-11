package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialStaticsDayMapper;
import com.ruoyi.system.domain.WmsMaterialStaticsDay;
import com.ruoyi.system.service.IWmsMaterialStaticsDayService;

/**
 * 物料日统计Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-09
 */
@Service
public class WmsMaterialStaticsDayServiceImpl implements IWmsMaterialStaticsDayService 
{
    @Autowired
    private WmsMaterialStaticsDayMapper wmsMaterialStaticsDayMapper;

    /**
     * 查询物料日统计
     * 
     * @param day 物料日统计主键
     * @return 物料日统计
     */
    @Override
    public WmsMaterialStaticsDay selectWmsMaterialStaticsDayByDay(String day)
    {
        return wmsMaterialStaticsDayMapper.selectWmsMaterialStaticsDayByDay(day);
    }

    /**
     * 查询物料日统计列表
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 物料日统计
     */
    @Override
    public List<WmsMaterialStaticsDay> selectWmsMaterialStaticsDayList(WmsMaterialStaticsDay wmsMaterialStaticsDay)
    {
        return wmsMaterialStaticsDayMapper.selectWmsMaterialStaticsDayList(wmsMaterialStaticsDay);
    }

    /**
     * 新增物料日统计
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 结果
     */
    @Override
    public int insertWmsMaterialStaticsDay(WmsMaterialStaticsDay wmsMaterialStaticsDay)
    {
        return wmsMaterialStaticsDayMapper.insertWmsMaterialStaticsDay(wmsMaterialStaticsDay);
    }

    /**
     * 修改物料日统计
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 结果
     */
    @Override
    public int updateWmsMaterialStaticsDay(WmsMaterialStaticsDay wmsMaterialStaticsDay)
    {
        return wmsMaterialStaticsDayMapper.updateWmsMaterialStaticsDay(wmsMaterialStaticsDay);
    }

    /**
     * 批量删除物料日统计
     * 
     * @param days 需要删除的物料日统计主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStaticsDayByDays(String[] days)
    {
        return wmsMaterialStaticsDayMapper.deleteWmsMaterialStaticsDayByDays(days);
    }

    /**
     * 删除物料日统计信息
     * 
     * @param day 物料日统计主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialStaticsDayByDay(String day)
    {
        return wmsMaterialStaticsDayMapper.deleteWmsMaterialStaticsDayByDay(day);
    }
}
