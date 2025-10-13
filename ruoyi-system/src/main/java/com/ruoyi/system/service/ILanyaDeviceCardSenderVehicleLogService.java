package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaDeviceCardSenderVehicleLog;

/**
 * 车辆发卡记录Service接口
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public interface ILanyaDeviceCardSenderVehicleLogService 
{
    /**
     * 查询车辆发卡记录
     * 
     * @param id 车辆发卡记录主键
     * @return 车辆发卡记录
     */
    public LanyaDeviceCardSenderVehicleLog selectLanyaDeviceCardSenderVehicleLogById(Long id);

    /**
     * 查询车辆发卡记录列表
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 车辆发卡记录集合
     */
    public List<LanyaDeviceCardSenderVehicleLog> selectLanyaDeviceCardSenderVehicleLogList(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog);

    /**
     * 新增车辆发卡记录
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 结果
     */
    public int insertLanyaDeviceCardSenderVehicleLog(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog);

    /**
     * 修改车辆发卡记录
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 结果
     */
    public int updateLanyaDeviceCardSenderVehicleLog(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog);

    /**
     * 批量删除车辆发卡记录
     * 
     * @param ids 需要删除的车辆发卡记录主键集合
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderVehicleLogByIds(Long[] ids);

    /**
     * 删除车辆发卡记录信息
     * 
     * @param id 车辆发卡记录主键
     * @return 结果
     */
    public int deleteLanyaDeviceCardSenderVehicleLogById(Long id);
}
