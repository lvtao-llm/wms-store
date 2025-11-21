package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsArea360Mapper;
import com.ruoyi.system.domain.WmsArea360;
import com.ruoyi.system.service.IWmsArea360Service;

/**
 * 区域点位全景Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@Service
public class WmsArea360ServiceImpl implements IWmsArea360Service 
{
    @Autowired
    private WmsArea360Mapper wmsArea360Mapper;

    /**
     * 查询区域点位全景
     * 
     * @param id 区域点位全景主键
     * @return 区域点位全景
     */
    @Override
    public WmsArea360 selectWmsArea360ById(Long id)
    {
        return wmsArea360Mapper.selectWmsArea360ById(id);
    }

    /**
     * 查询区域点位全景列表
     * 
     * @param wmsArea360 区域点位全景
     * @return 区域点位全景
     */
    @Override
    public List<WmsArea360> selectWmsArea360List(WmsArea360 wmsArea360)
    {
        return wmsArea360Mapper.selectWmsArea360List(wmsArea360);
    }

    /**
     * 新增区域点位全景
     * 
     * @param wmsArea360 区域点位全景
     * @return 结果
     */
    @Override
    public int insertWmsArea360(WmsArea360 wmsArea360)
    {
        return wmsArea360Mapper.insertWmsArea360(wmsArea360);
    }

    /**
     * 修改区域点位全景
     * 
     * @param wmsArea360 区域点位全景
     * @return 结果
     */
    @Override
    public int updateWmsArea360(WmsArea360 wmsArea360)
    {
        return wmsArea360Mapper.updateWmsArea360(wmsArea360);
    }

    /**
     * 批量删除区域点位全景
     * 
     * @param ids 需要删除的区域点位全景主键
     * @return 结果
     */
    @Override
    public int deleteWmsArea360ByIds(Long[] ids)
    {
        return wmsArea360Mapper.deleteWmsArea360ByIds(ids);
    }

    /**
     * 删除区域点位全景信息
     * 
     * @param id 区域点位全景主键
     * @return 结果
     */
    @Override
    public int deleteWmsArea360ById(Long id)
    {
        return wmsArea360Mapper.deleteWmsArea360ById(id);
    }
}
