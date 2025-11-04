package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsVehicleRecordMapper;
import com.ruoyi.system.domain.WmsVehicleRecord;
import com.ruoyi.system.service.IWmsVehicleRecordService;

/**
 * 车辆黑名单wms_vehicle_blacklistService业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
@Service
public class WmsVehicleRecordServiceImpl implements IWmsVehicleRecordService 
{
    @Autowired
    private WmsVehicleRecordMapper wmsVehicleRecordMapper;

    /**
     * 查询车辆黑名单wms_vehicle_blacklist
     * 
     * @param id 车辆黑名单wms_vehicle_blacklist主键
     * @return 车辆黑名单wms_vehicle_blacklist
     */
    @Override
    public WmsVehicleRecord selectWmsVehicleRecordById(Long id)
    {
        return wmsVehicleRecordMapper.selectWmsVehicleRecordById(id);
    }

    /**
     * 查询车辆黑名单wms_vehicle_blacklist列表
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 车辆黑名单wms_vehicle_blacklist
     */
    @Override
    public List<WmsVehicleRecord> selectWmsVehicleRecordList(WmsVehicleRecord wmsVehicleRecord)
    {
        return wmsVehicleRecordMapper.selectWmsVehicleRecordList(wmsVehicleRecord);
    }

    /**
     * 新增车辆黑名单wms_vehicle_blacklist
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 结果
     */
    @Override
    public int insertWmsVehicleRecord(WmsVehicleRecord wmsVehicleRecord)
    {
        wmsVehicleRecord.setCreateTime(DateUtils.getNowDate());
        return wmsVehicleRecordMapper.insertWmsVehicleRecord(wmsVehicleRecord);
    }

    /**
     * 修改车辆黑名单wms_vehicle_blacklist
     * 
     * @param wmsVehicleRecord 车辆黑名单wms_vehicle_blacklist
     * @return 结果
     */
    @Override
    public int updateWmsVehicleRecord(WmsVehicleRecord wmsVehicleRecord)
    {
        wmsVehicleRecord.setUpdateTime(DateUtils.getNowDate());
        return wmsVehicleRecordMapper.updateWmsVehicleRecord(wmsVehicleRecord);
    }

    /**
     * 批量删除车辆黑名单wms_vehicle_blacklist
     * 
     * @param ids 需要删除的车辆黑名单wms_vehicle_blacklist主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleRecordByIds(Long[] ids)
    {
        return wmsVehicleRecordMapper.deleteWmsVehicleRecordByIds(ids);
    }

    /**
     * 删除车辆黑名单wms_vehicle_blacklist信息
     * 
     * @param id 车辆黑名单wms_vehicle_blacklist主键
     * @return 结果
     */
    @Override
    public int deleteWmsVehicleRecordById(Long id)
    {
        return wmsVehicleRecordMapper.deleteWmsVehicleRecordById(id);
    }
}
