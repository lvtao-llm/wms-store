package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterial;

/**
 * 物料档案Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface WmsMaterialMapper 
{
    /**
     * 查询物料档案
     * 
     * @param materialId 物料档案主键
     * @return 物料档案
     */
    public WmsMaterial selectWmsMaterialByMaterialId(Long materialId);

    /**
     * 查询物料档案列表
     * 
     * @param wmsMaterial 物料档案
     * @return 物料档案集合
     */
    public List<WmsMaterial> selectWmsMaterialList(WmsMaterial wmsMaterial);

    /**
     * 新增物料档案
     * 
     * @param wmsMaterial 物料档案
     * @return 结果
     */
    public int insertWmsMaterial(WmsMaterial wmsMaterial);

    /**
     * 修改物料档案
     * 
     * @param wmsMaterial 物料档案
     * @return 结果
     */
    public int updateWmsMaterial(WmsMaterial wmsMaterial);

    /**
     * 删除物料档案
     * 
     * @param materialId 物料档案主键
     * @return 结果
     */
    public int deleteWmsMaterialByMaterialId(Long materialId);

    /**
     * 批量删除物料档案
     * 
     * @param materialIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsMaterialByMaterialIds(Long[] materialIds);
}
