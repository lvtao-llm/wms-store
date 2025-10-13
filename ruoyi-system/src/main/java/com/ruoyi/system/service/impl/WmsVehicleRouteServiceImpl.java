package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehicleRouteMapper;
import com.ruoyi.system.domain.WmsVehicleRoute;
import com.ruoyi.system.service.IWmsVehicleRouteService;

/**
 * 车辆路线规划Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsVehicleRouteServiceImpl implements IWmsVehicleRouteService 
{
    @Autowired
    private WmsVehicleRouteMapper wmsVehicleRouteMapper;

    /**
     * 查询车辆路线规划
     * 
     * @param routeId 车辆路线规划主键
     * @return 车辆路线规划
     */
    @Override
    public WmsVehicleRoute selectWmsVehicleRouteByRouteId(Long routeId)
    {
        return wmsVehicleRouteMapper.selectWmsVehicleRouteByRouteId(routeId);
    }

    /**
     * 查询车辆路线规划列表
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 车辆路线规划
     */
    @Override
    public List<WmsVehicleRoute> selectWmsVehicleRouteList(WmsVehicleRoute wmsVehicleRoute)
    {
        return wmsVehicleRouteMapper.selectWmsVehicleRouteList(wmsVehicleRoute);
    }

    /**
     * 新增车辆路线规划
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 结果
     */
    @Override
    public int insertWmsVehicleRoute(WmsVehicleRoute wmsVehicleRoute)
    {
        wmsVehicleRoute.setCreateTime(DateUtils.getNowDate());
        return wmsVehicleRouteMapper.insertWmsVehicleRoute(wmsVehicleRoute);
    }

    /**
     * 修改车辆路线规划
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 结果
     */
    @Override
    public int updateWmsVehicleRoute(WmsVehicleRoute wmsVehicleRoute)
    {
        wmsVehicleRoute.setUpdateTime(DateUtils.getNowDate());
        return wmsVehicleRouteMapper.updateWmsVehicleRoute(wmsVehicleRoute);
    }

    /**
     * 批量删除车辆路线规划
     * 
     * @param routeIds 需要删除的车辆路线规划主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleRouteByRouteIds(Long[] routeIds)
    {
        return wmsVehicleRouteMapper.deleteWmsVehicleRouteByRouteIds(routeIds);
    }

    /**
     * 删除车辆路线规划信息
     * 
     * @param routeId 车辆路线规划主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleRouteByRouteId(Long routeId)
    {
        return wmsVehicleRouteMapper.deleteWmsVehicleRouteByRouteId(routeId);
    }
}
