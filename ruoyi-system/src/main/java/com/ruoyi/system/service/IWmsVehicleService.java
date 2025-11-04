package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVehicle;

/**
 * 车辆档案Service接口
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public interface IWmsVehicleService 
{
    /**
     * 查询车辆档案
     * 
     * @param vehicleId 车辆档案主键
     * @return 车辆档案
     */
    public WmsVehicle selectWmsVehicleByVehicleId(Long vehicleId);

    /**
     * 查询车辆档案列表
     * 
     * @param wmsVehicle 车辆档案
     * @return 车辆档案集合
     */
    public List<WmsVehicle> selectWmsVehicleList(WmsVehicle wmsVehicle);

    /**
     * 新增车辆档案
     * 
     * @param wmsVehicle 车辆档案
     * @return 结果
     */
    public int insertWmsVehicle(WmsVehicle wmsVehicle);

    /**
     * 修改车辆档案
     * 
     * @param wmsVehicle 车辆档案
     * @return 结果
     */
    public int updateWmsVehicle(WmsVehicle wmsVehicle);

    /**
     * 批量删除车辆档案
     * 
     * @param vehicleIds 需要删除的车辆档案主键集合
     * @return 结果
     */
    public int deleteWmsVehicleByVehicleIds(Long[] vehicleIds);

    /**
     * 删除车辆档案信息
     * 
     * @param vehicleId 车辆档案主键
     * @return 结果
     */
    public int deleteWmsVehicleByVehicleId(Long vehicleId);
}
