package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LanyaVehicleAlarm;

/**
 * 车辆报警记录Service接口
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public interface ILanyaVehicleAlarmService 
{
    /**
     * 查询车辆报警记录
     * 
     * @param id 车辆报警记录主键
     * @return 车辆报警记录
     */
    public LanyaVehicleAlarm selectLanyaVehicleAlarmById(Long id);

    /**
     * 查询车辆报警记录列表
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 车辆报警记录集合
     */
    public List<LanyaVehicleAlarm> selectLanyaVehicleAlarmList(LanyaVehicleAlarm lanyaVehicleAlarm);

    /**
     * 新增车辆报警记录
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 结果
     */
    public int insertLanyaVehicleAlarm(LanyaVehicleAlarm lanyaVehicleAlarm);

    /**
     * 修改车辆报警记录
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 结果
     */
    public int updateLanyaVehicleAlarm(LanyaVehicleAlarm lanyaVehicleAlarm);

    /**
     * 批量删除车辆报警记录
     * 
     * @param ids 需要删除的车辆报警记录主键集合
     * @return 结果
     */
    public int deleteLanyaVehicleAlarmByIds(Long[] ids);

    /**
     * 删除车辆报警记录信息
     * 
     * @param id 车辆报警记录主键
     * @return 结果
     */
    public int deleteLanyaVehicleAlarmById(Long id);
}
