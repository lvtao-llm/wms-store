package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsArea360;

/**
 * 区域点位全景Service接口
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
public interface IWmsArea360Service 
{
    /**
     * 查询区域点位全景
     * 
     * @param id 区域点位全景主键
     * @return 区域点位全景
     */
    public WmsArea360 selectWmsArea360ById(Long id);

    /**
     * 查询区域点位全景列表
     * 
     * @param wmsArea360 区域点位全景
     * @return 区域点位全景集合
     */
    public List<WmsArea360> selectWmsArea360List(WmsArea360 wmsArea360);

    /**
     * 新增区域点位全景
     * 
     * @param wmsArea360 区域点位全景
     * @return 结果
     */
    public int insertWmsArea360(WmsArea360 wmsArea360);

    /**
     * 修改区域点位全景
     * 
     * @param wmsArea360 区域点位全景
     * @return 结果
     */
    public int updateWmsArea360(WmsArea360 wmsArea360);

    /**
     * 批量删除区域点位全景
     * 
     * @param ids 需要删除的区域点位全景主键集合
     * @return 结果
     */
    public int deleteWmsArea360ByIds(Long[] ids);

    /**
     * 删除区域点位全景信息
     * 
     * @param id 区域点位全景主键
     * @return 结果
     */
    public int deleteWmsArea360ById(Long id);
}
