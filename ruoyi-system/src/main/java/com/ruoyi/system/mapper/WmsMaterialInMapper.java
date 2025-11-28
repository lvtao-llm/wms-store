package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.WmsMaterialIn;
import org.apache.ibatis.annotations.Param;

/**
 * 接料视图Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-08
 */
public interface WmsMaterialInMapper {
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
     * 删除接料视图
     *
     * @param jlBh 接料视图主键
     * @return 结果
     */
    public int deleteWmsMaterialInByJlBh(String jlBh);

    /**
     * 批量删除接料视图
     *
     * @param jlBhs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsMaterialInByJlBhs(String[] jlBhs);

    List<WmsMaterialIn> selectWmsMaterialInListByAreaNames(@Param("wmsMaterialIn") WmsMaterialIn wmsMaterialIn, @Param("areaNames") List<String> areaNames);
}
