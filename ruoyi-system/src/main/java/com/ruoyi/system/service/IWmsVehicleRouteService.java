package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVehicleRoute;

/**
 * 车辆路线规划Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsVehicleRouteService 
{
    /**
     * 查询车辆路线规划
     * 
     * @param routeId 车辆路线规划主键
     * @return 车辆路线规划
     */
    public WmsVehicleRoute selectWmsVehicleRouteByRouteId(Long routeId);

    /**
     * 查询车辆路线规划列表
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 车辆路线规划集合
     */
    public List<WmsVehicleRoute> selectWmsVehicleRouteList(WmsVehicleRoute wmsVehicleRoute);

    /**
     * 新增车辆路线规划
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 结果
     */
    public int insertWmsVehicleRoute(WmsVehicleRoute wmsVehicleRoute);

    /**
     * 修改车辆路线规划
     * 
     * @param wmsVehicleRoute 车辆路线规划
     * @return 结果
     */
    public int updateWmsVehicleRoute(WmsVehicleRoute wmsVehicleRoute);

    /**
     * 批量删除车辆路线规划
     * 
     * @param routeIds 需要删除的车辆路线规划主键集合
     * @return 结果
     */
    public int deleteWmsVehicleRouteByRouteIds(Long[] routeIds);

    /**
     * 删除车辆路线规划信息
     * 
     * @param routeId 车辆路线规划主键
     * @return 结果
     */
    public int deleteWmsVehicleRouteByRouteId(Long routeId);
}
