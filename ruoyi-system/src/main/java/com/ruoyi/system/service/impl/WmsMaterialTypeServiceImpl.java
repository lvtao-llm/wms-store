package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsMaterialTypeMapper;
import com.ruoyi.system.domain.WmsMaterialType;
import com.ruoyi.system.service.IWmsMaterialTypeService;

/**
 * 物料类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-20
 */
@Service
public class WmsMaterialTypeServiceImpl implements IWmsMaterialTypeService 
{
    @Autowired
    private WmsMaterialTypeMapper wmsMaterialTypeMapper;

    /**
     * 查询物料类型
     * 
     * @param bm 物料类型主键
     * @return 物料类型
     */
    @Override
    public WmsMaterialType selectWmsMaterialTypeByBm(String bm)
    {
        return wmsMaterialTypeMapper.selectWmsMaterialTypeByBm(bm);
    }

    /**
     * 查询物料类型列表
     * 
     * @param wmsMaterialType 物料类型
     * @return 物料类型
     */
    @Override
    public List<WmsMaterialType> selectWmsMaterialTypeList(WmsMaterialType wmsMaterialType)
    {
        return wmsMaterialTypeMapper.selectWmsMaterialTypeList(wmsMaterialType);
    }

    /**
     * 新增物料类型
     * 
     * @param wmsMaterialType 物料类型
     * @return 结果
     */
    @Override
    public int insertWmsMaterialType(WmsMaterialType wmsMaterialType)
    {
        return wmsMaterialTypeMapper.insertWmsMaterialType(wmsMaterialType);
    }

    /**
     * 修改物料类型
     * 
     * @param wmsMaterialType 物料类型
     * @return 结果
     */
    @Override
    public int updateWmsMaterialType(WmsMaterialType wmsMaterialType)
    {
        return wmsMaterialTypeMapper.updateWmsMaterialType(wmsMaterialType);
    }

    /**
     * 批量删除物料类型
     * 
     * @param bms 需要删除的物料类型主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialTypeByBms(String[] bms)
    {
        return wmsMaterialTypeMapper.deleteWmsMaterialTypeByBms(bms);
    }

    /**
     * 删除物料类型信息
     * 
     * @param bm 物料类型主键
     * @return 结果
     */
    @Override
    public int deleteWmsMaterialTypeByBm(String bm)
    {
        return wmsMaterialTypeMapper.deleteWmsMaterialTypeByBm(bm);
    }
}
