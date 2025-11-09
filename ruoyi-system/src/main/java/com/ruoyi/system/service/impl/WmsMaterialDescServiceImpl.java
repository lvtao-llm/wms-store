package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.WmsMaterialIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialDescMapper;
import com.ruoyi.system.domain.WmsMaterialDesc;
import com.ruoyi.system.service.IWmsMaterialDescService;

/**
 * 物料描述档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
@Service
public class WmsMaterialDescServiceImpl implements IWmsMaterialDescService 
{
    @Autowired
    private WmsMaterialDescMapper wmsMaterialDescMapper;

    /**
     * 查询物料描述档案
     * 
     * @param materialDescId 物料描述档案主键
     * @return 物料描述档案
     */
    @Override
    public WmsMaterialDesc selectWmsMaterialDescByMaterialDescId(Long materialDescId)
    {
        return wmsMaterialDescMapper.selectWmsMaterialDescByMaterialDescId(materialDescId);
    }

    /**
     * 查询物料描述档案列表
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 物料描述档案
     */
    @Override
    public List<WmsMaterialDesc> selectWmsMaterialDescList(WmsMaterialDesc wmsMaterialDesc)
    {
        return wmsMaterialDescMapper.selectWmsMaterialDescList(wmsMaterialDesc);
    }

    /**
     * 新增物料描述档案
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    @Override
    public int insertWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc)
    {
        wmsMaterialDesc.setCreateTime(DateUtils.getNowDate());
        return wmsMaterialDescMapper.insertWmsMaterialDesc(wmsMaterialDesc);
    }

    /**
     * 修改物料描述档案
     * 
     * @param wmsMaterialDesc 物料描述档案
     * @return 结果
     */
    @Override
    public int updateWmsMaterialDesc(WmsMaterialDesc wmsMaterialDesc)
    {
        return wmsMaterialDescMapper.updateWmsMaterialDesc(wmsMaterialDesc);
    }

    /**
     * 批量删除物料描述档案
     * 
     * @param materialDescIds 需要删除的物料描述档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialDescByMaterialDescIds(Long[] materialDescIds)
    {
        return wmsMaterialDescMapper.deleteWmsMaterialDescByMaterialDescIds(materialDescIds);
    }

    /**
     * 删除物料描述档案信息
     * 
     * @param materialDescId 物料描述档案主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialDescByMaterialDescId(Long materialDescId)
    {
        return wmsMaterialDescMapper.deleteWmsMaterialDescByMaterialDescId(materialDescId);
    }

    @Override
    public int insertNewMaterialDesc(WmsMaterialIn w) {
        return wmsMaterialDescMapper.insertNewMaterialDesc(w);
    }
}
