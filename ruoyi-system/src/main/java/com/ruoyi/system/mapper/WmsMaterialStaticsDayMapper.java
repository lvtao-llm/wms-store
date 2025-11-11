package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialStaticsDay;

/**
 * 物料日统计Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-09
 */
public interface WmsMaterialStaticsDayMapper 
{
    /**
     * 查询物料日统计
     * 
     * @param day 物料日统计主键
     * @return 物料日统计
     */
    public WmsMaterialStaticsDay selectWmsMaterialStaticsDayByDay(String day);

    /**
     * 查询物料日统计列表
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 物料日统计集合
     */
    public List<WmsMaterialStaticsDay> selectWmsMaterialStaticsDayList(WmsMaterialStaticsDay wmsMaterialStaticsDay);

    /**
     * 新增物料日统计
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 结果
     */
    public int insertWmsMaterialStaticsDay(WmsMaterialStaticsDay wmsMaterialStaticsDay);

    /**
     * 修改物料日统计
     * 
     * @param wmsMaterialStaticsDay 物料日统计
     * @return 结果
     */
    public int updateWmsMaterialStaticsDay(WmsMaterialStaticsDay wmsMaterialStaticsDay);

    /**
     * 删除物料日统计
     * 
     * @param day 物料日统计主键
     * @return 结果
     */
    public int deleteWmsMaterialStaticsDayByDay(String day);

    /**
     * 批量删除物料日统计
     * 
     * @param days 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsMaterialStaticsDayByDays(String[] days);
}
