package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.WmsMaterialOut;

/**
 * 调拨视图Service接口
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
public interface IWmsMaterialOutService 
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
     * 批量删除调拨视图
     * 
     * @param allotDetailIds 需要删除的调拨视图主键集合
     * @return 结果
     */
    public int deleteWmsMaterialOutByAllotDetailIds(String[] allotDetailIds);

    /**
     * 删除调拨视图信息
     * 
     * @param allotDetailId 调拨视图主键
     * @return 结果
     */
    public int deleteWmsMaterialOutByAllotDetailId(String allotDetailId);

    List<WmsMaterialOut> selectWmsMaterialOutListByDateRange(Date parse, Date parse1);

    List<WmsMaterialOut> selectWmsMaterialOutListByAreaNames(WmsMaterialOut wmsMaterialIn, List<String> wzbm);
}
