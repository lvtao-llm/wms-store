package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialMapper;
import com.ruoyi.system.domain.WmsMaterial;
import com.ruoyi.system.service.IWmsMaterialService;

/**
 * 物料档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsMaterialServiceImpl implements IWmsMaterialService 
{
    @Autowired
    private WmsMaterialMapper wmsMaterialMapper;

    /**
     * 查询物料档案
     * 
     * @param materialId 物料档案主键
     * @return 物料档案
     */
    @Override
    public WmsMaterial selectWmsMaterialByMaterialId(Long materialId)
    {
        return wmsMaterialMapper.selectWmsMaterialByMaterialId(materialId);
    }

    /**
     * 查询物料档案列表
     * 
     * @param wmsMaterial 物料档案
     * @return 物料档案
     */
    @Override
    public List<WmsMaterial> selectWmsMaterialList(WmsMaterial wmsMaterial)
    {
        return wmsMaterialMapper.selectWmsMaterialList(wmsMaterial);
    }

    /**
     * 新增物料档案
     * 
     * @param wmsMaterial 物料档案
     * @return 结果
     */
    @Override
    public int insertWmsMaterial(WmsMaterial wmsMaterial)
    {
        wmsMaterial.setCreateTime(DateUtils.getNowDate());
        return wmsMaterialMapper.insertWmsMaterial(wmsMaterial);
    }

    /**
     * 修改物料档案
     * 
     * @param wmsMaterial 物料档案
     * @return 结果
     */
    @Override
    public int updateWmsMaterial(WmsMaterial wmsMaterial)
    {
        return wmsMaterialMapper.updateWmsMaterial(wmsMaterial);
    }

    /**
     * 批量删除物料档案
     * 
     * @param materialIds 需要删除的物料档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialByMaterialIds(Long[] materialIds)
    {
        return wmsMaterialMapper.deleteWmsMaterialByMaterialIds(materialIds);
    }

    /**
     * 删除物料档案信息
     * 
     * @param materialId 物料档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialByMaterialId(Long materialId)
    {
        return wmsMaterialMapper.deleteWmsMaterialByMaterialId(materialId);
    }
}
