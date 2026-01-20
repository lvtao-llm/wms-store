package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsMaterialType;

/**
 * 物料类型Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-20
 */
public interface WmsMaterialTypeMapper 
{
    /**
     * 查询物料类型
     * 
     * @param bm 物料类型主键
     * @return 物料类型
     */
    public WmsMaterialType selectWmsMaterialTypeByBm(String bm);

    /**
     * 查询物料类型列表
     * 
     * @param wmsMaterialType 物料类型
     * @return 物料类型集合
     */
    public List<WmsMaterialType> selectWmsMaterialTypeList(WmsMaterialType wmsMaterialType);

    /**
     * 新增物料类型
     * 
     * @param wmsMaterialType 物料类型
     * @return 结果
     */
    public int insertWmsMaterialType(WmsMaterialType wmsMaterialType);

    /**
     * 修改物料类型
     * 
     * @param wmsMaterialType 物料类型
     * @return 结果
     */
    public int updateWmsMaterialType(WmsMaterialType wmsMaterialType);

    /**
     * 删除物料类型
     * 
     * @param bm 物料类型主键
     * @return 结果
     */
    public int deleteWmsMaterialTypeByBm(String bm);

    /**
     * 批量删除物料类型
     * 
     * @param bms 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsMaterialTypeByBms(String[] bms);
}
