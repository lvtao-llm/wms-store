package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaVehicleAlarmMapper;
import com.ruoyi.system.domain.LanyaVehicleAlarm;
import com.ruoyi.system.service.ILanyaVehicleAlarmService;

/**
 * 车辆报警记录Service业务层处理
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@Service
public class LanyaVehicleAlarmServiceImpl implements ILanyaVehicleAlarmService 
{
    @Autowired
    private LanyaVehicleAlarmMapper lanyaVehicleAlarmMapper;

    /**
     * 查询车辆报警记录
     * 
     * @param id 车辆报警记录主键
     * @return 车辆报警记录
     */
    @Override
    public LanyaVehicleAlarm selectLanyaVehicleAlarmById(Long id)
    {
        return lanyaVehicleAlarmMapper.selectLanyaVehicleAlarmById(id);
    }

    /**
     * 查询车辆报警记录列表
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 车辆报警记录
     */
    @Override
    public List<LanyaVehicleAlarm> selectLanyaVehicleAlarmList(LanyaVehicleAlarm lanyaVehicleAlarm)
    {
        return lanyaVehicleAlarmMapper.selectLanyaVehicleAlarmList(lanyaVehicleAlarm);
    }

    /**
     * 新增车辆报警记录
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 结果
     */
    @Override
    public int insertLanyaVehicleAlarm(LanyaVehicleAlarm lanyaVehicleAlarm)
    {
        lanyaVehicleAlarm.setCreateTime(DateUtils.getNowDate());
        return lanyaVehicleAlarmMapper.insertLanyaVehicleAlarm(lanyaVehicleAlarm);
    }

    /**
     * 修改车辆报警记录
     * 
     * @param lanyaVehicleAlarm 车辆报警记录
     * @return 结果
     */
    @Override
    public int updateLanyaVehicleAlarm(LanyaVehicleAlarm lanyaVehicleAlarm)
    {
        lanyaVehicleAlarm.setUpdateTime(DateUtils.getNowDate());
        return lanyaVehicleAlarmMapper.updateLanyaVehicleAlarm(lanyaVehicleAlarm);
    }

    /**
     * 批量删除车辆报警记录
     * 
     * @param ids 需要删除的车辆报警记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaVehicleAlarmByIds(Long[] ids)
    {
        return lanyaVehicleAlarmMapper.deleteLanyaVehicleAlarmByIds(ids);
    }

    /**
     * 删除车辆报警记录信息
     * 
     * @param id 车辆报警记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaVehicleAlarmById(Long id)
    {
        return lanyaVehicleAlarmMapper.deleteLanyaVehicleAlarmById(id);
    }
}
