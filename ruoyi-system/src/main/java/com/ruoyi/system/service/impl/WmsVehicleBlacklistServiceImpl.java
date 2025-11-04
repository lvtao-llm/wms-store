package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehicleBlacklistMapper;
import com.ruoyi.system.domain.WmsVehicleBlacklist;
import com.ruoyi.system.service.IWmsVehicleBlacklistService;

/**
 * 车辆黑名单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@Service
public class WmsVehicleBlacklistServiceImpl implements IWmsVehicleBlacklistService 
{
    @Autowired
    private WmsVehicleBlacklistMapper wmsVehicleBlacklistMapper;

    /**
     * 查询车辆黑名单
     * 
     * @param id 车辆黑名单主键
     * @return 车辆黑名单
     */
    @Override
    public WmsVehicleBlacklist selectWmsVehicleBlacklistById(Long id)
    {
        return wmsVehicleBlacklistMapper.selectWmsVehicleBlacklistById(id);
    }

    /**
     * 查询车辆黑名单列表
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 车辆黑名单
     */
    @Override
    public List<WmsVehicleBlacklist> selectWmsVehicleBlacklistList(WmsVehicleBlacklist wmsVehicleBlacklist)
    {
        return wmsVehicleBlacklistMapper.selectWmsVehicleBlacklistList(wmsVehicleBlacklist);
    }

    /**
     * 新增车辆黑名单
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 结果
     */
    @Override
    public int insertWmsVehicleBlacklist(WmsVehicleBlacklist wmsVehicleBlacklist)
    {
        wmsVehicleBlacklist.setCreateTime(DateUtils.getNowDate());
        return wmsVehicleBlacklistMapper.insertWmsVehicleBlacklist(wmsVehicleBlacklist);
    }

    /**
     * 修改车辆黑名单
     * 
     * @param wmsVehicleBlacklist 车辆黑名单
     * @return 结果
     */
    @Override
    public int updateWmsVehicleBlacklist(WmsVehicleBlacklist wmsVehicleBlacklist)
    {
        wmsVehicleBlacklist.setUpdateTime(DateUtils.getNowDate());
        return wmsVehicleBlacklistMapper.updateWmsVehicleBlacklist(wmsVehicleBlacklist);
    }

    /**
     * 批量删除车辆黑名单
     * 
     * @param ids 需要删除的车辆黑名单主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleBlacklistByIds(Long[] ids)
    {
        return wmsVehicleBlacklistMapper.deleteWmsVehicleBlacklistByIds(ids);
    }

    /**
     * 删除车辆黑名单信息
     * 
     * @param id 车辆黑名单主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleBlacklistById(Long id)
    {
        return wmsVehicleBlacklistMapper.deleteWmsVehicleBlacklistById(id);
    }
}
