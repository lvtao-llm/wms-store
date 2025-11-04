package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVehicleBlacklist;

/**
 * 车辆黑名单Service接口
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public interface IWmsVehicleBlacklistService 
{
    /**
     * 查询车辆黑名单
     * 
     * @param id 车辆黑名单主键
     * @return 车辆黑名单
     */
    public WmsVehicleBlacklist selectWmsVehicleBlacklistById(Long id);

    /**
     * 查询车辆黑名单列表
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 车辆黑名单集合
     */
    public List<WmsVehicleBlacklist> selectWmsVehicleBlacklistList(WmsVehicleBlacklist wmsVehicleBlacklist);

    /**
     * 新增车辆黑名单
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 结果
     */
    public int insertWmsVehicleBlacklist(WmsVehicleBlacklist wmsVehicleBlacklist);

    /**
     * 修改车辆黑名单
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 结果
     */
    public int updateWmsVehicleBlacklist(WmsVehicleBlacklist wmsVehicleBlacklist);

    /**
     * 批量删除车辆黑名单
     * 
     * @param ids 需要删除的车辆黑名单主键集合
     * @return 结果
     */
    public int deleteWmsVehicleBlacklistByIds(Long[] ids);

    /**
     * 删除车辆黑名单信息
     * 
     * @param id 车辆黑名单主键
     * @return 结果
     */
    public int deleteWmsVehicleBlacklistById(Long id);
}
