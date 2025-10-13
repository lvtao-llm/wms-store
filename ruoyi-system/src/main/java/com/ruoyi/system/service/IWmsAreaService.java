package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsArea;

/**
 * 区域Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsAreaService 
{
    /**
     * 查询区域
     * 
     * @param areaId 区域主键
     * @return 区域
     */
    public WmsArea selectWmsAreaByAreaId(Long areaId);

    /**
     * 查询区域列表
     * 
     * @param wmsArea 区域
     * @return 区域集合
     */
    public List<WmsArea> selectWmsAreaList(WmsArea wmsArea);

    /**
     * 新增区域
     * 
     * @param wmsArea 区域
     * @return 结果
     */
    public int insertWmsArea(WmsArea wmsArea);

    /**
     * 修改区域
     * 
     * @param wmsArea 区域
     * @return 结果
     */
    public int updateWmsArea(WmsArea wmsArea);

    /**
     * 批量删除区域
     * 
     * @param areaIds 需要删除的区域主键集合
     * @return 结果
     */
    public int deleteWmsAreaByAreaIds(Long[] areaIds);

    /**
     * 删除区域信息
     * 
     * @param areaId 区域主键
     * @return 结果
     */
    public int deleteWmsAreaByAreaId(Long areaId);
}
