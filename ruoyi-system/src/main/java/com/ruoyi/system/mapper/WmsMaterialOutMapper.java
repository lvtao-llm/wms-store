package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialOut;

/**
 * 调拨视图Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
public interface WmsMaterialOutMapper 
{
    /**
     * 查询调拨视图
     * 
     * @param allotDetailId 调拨视图主键
     * @return 调拨视图
     */
    public WmsMaterialOut selectWmsMaterialOutByAllotDetailId(String allotDetailId);

    /**
     * 查询调拨视图列表
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 调拨视图集合
     */
    public List<WmsMaterialOut> selectWmsMaterialOutList(WmsMaterialOut wmsMaterialOut);

    /**
     * 新增调拨视图
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 结果
     */
    public int insertWmsMaterialOut(WmsMaterialOut wmsMaterialOut);

    /**
     * 修改调拨视图
     * 
     * @param wmsMaterialOut 调拨视图
     * @return 结果
     */
    public int updateWmsMaterialOut(WmsMaterialOut wmsMaterialOut);

    /**
     * 删除调拨视图
     * 
     * @param allotDetailId 调拨视图主键
     * @return 结果
     */
    public int deleteWmsMaterialOutByAllotDetailId(String allotDetailId);

    /**
     * 批量删除调拨视图
     * 
     * @param allotDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsMaterialOutByAllotDetailIds(String[] allotDetailIds);
}
