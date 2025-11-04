package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsVehicleRecord;

/**
 * 车辆黑名单wms_vehicle_blacklistService接口
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public interface IWmsVehicleRecordService 
{
    /**
     * 查询车辆黑名单wms_vehicle_blacklist
     * 
     * @param id 车辆黑名单wms_vehicle_blacklist主键
     * @return 车辆黑名单wms_vehicle_blacklist
     */
    public WmsVehicleRecord selectWmsVehicleRecordById(Long id);

    /**
     * 查询车辆黑名单wms_vehicle_blacklist列表
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 车辆黑名单wms_vehicle_blacklist集合
     */
    public List<WmsVehicleRecord> selectWmsVehicleRecordList(WmsVehicleRecord wmsVehicleRecord);

    /**
     * 新增车辆黑名单wms_vehicle_blacklist
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 结果
     */
    public int insertWmsVehicleRecord(WmsVehicleRecord wmsVehicleRecord);

    /**
     * 修改车辆黑名单wms_vehicle_blacklist
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 结果
     */
    public int updateWmsVehicleRecord(WmsVehicleRecord wmsVehicleRecord);

    /**
     * 批量删除车辆黑名单wms_vehicle_blacklist
     * 
     * @param ids 需要删除的车辆黑名单wms_vehicle_blacklist主键集合
     * @return 结果
     */
    public int deleteWmsVehicleRecordByIds(Long[] ids);

    /**
     * 删除车辆黑名单wms_vehicle_blacklist信息
     * 
     * @param id 车辆黑名单wms_vehicle_blacklist主键
     * @return 结果
     */
    public int deleteWmsVehicleRecordById(Long id);
}
