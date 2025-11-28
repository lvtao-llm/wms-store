package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialIn;

/**
 * 接料视图Service接口
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
public interface IWmsMaterialInService 
{
    /**
     * 查询接料视图
     * 
     * @param jlBh 接料视图主键
     * @return 接料视图
     */
    public WmsMaterialIn selectWmsMaterialInByJlBh(String jlBh);

    /**
     * 查询接料视图列表
     * 
     * @param wmsMaterialIn 接料视图
     * @return 接料视图集合
     */
    public List<WmsMaterialIn> selectWmsMaterialInList(WmsMaterialIn wmsMaterialIn);

    /**
     * 新增接料视图
     * 
     * @param wmsMaterialIn 接料视图
     * @return 结果
     */
    public int insertWmsMaterialIn(WmsMaterialIn wmsMaterialIn);

    /**
     * 修改接料视图
     * 
     * @param wmsMaterialIn 接料视图
     * @return 结果
     */
    public int updateWmsMaterialIn(WmsMaterialIn wmsMaterialIn);

    /**
     * 批量删除接料视图
     * 
     * @param jlBhs 需要删除的接料视图主键集合
     * @return 结果
     */
    public int deleteWmsMaterialInByJlBhs(String[] jlBhs);

    /**
     * 删除接料视图信息
     * 
     * @param jlBh 接料视图主键
     * @return 结果
     */
    public int deleteWmsMaterialInByJlBh(String jlBh);

    List<WmsMaterialIn> selectWmsMaterialInListByAreaNames(WmsMaterialIn wmsMaterialIn, List<String> areaNames);
}
