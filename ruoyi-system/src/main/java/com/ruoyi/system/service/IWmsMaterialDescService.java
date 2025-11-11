package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.domain.WmsMaterialIn;

/**
 * 物料描述档案Service接口
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public interface IWmsMaterialDescService 
{
    /**
     * 查询物料描述档案
     * 
     * @param materialDescId 物料描述档案主键
     * @return 物料描述档案
     */
    public WmsMaterialDesc selectWmsMaterialDescByMaterialDescId(Long materialDescId);

    /**
     * 查询物料描述档案列表
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 物料描述档案集合
     */
    public List<WmsMaterialDesc> selectWmsMaterialDescList(WmsMaterialDesc wmsMaterialDesc);

    /**
     * 新增物料描述档案
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    public int insertWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc);

    /**
     * 修改物料描述档案
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    public int updateWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc);

    /**
     * 批量删除物料描述档案
     * 
     * @param materialDescIds 需要删除的物料描述档案主键集合
     * @return 结果
     */
    public int deleteWmsMaterialDescByMaterialDescIds(Long[] materialDescIds);

    /**
     * 删除物料描述档案信息
     * 
     * @param materialDescId 物料描述档案主键
     * @return 结果
     */
    public int deleteWmsMaterialDescByMaterialDescId(Long materialDescId);

    int insertNewMaterialDesc(WmsMaterialDesc w);

    Map<String, WmsMaterialDesc> getMaterialDescMap();
}
