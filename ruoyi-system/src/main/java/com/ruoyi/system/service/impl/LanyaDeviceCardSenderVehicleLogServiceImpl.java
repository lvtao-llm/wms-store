package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaDeviceCardSenderVehicleLogMapper;
import com.ruoyi.system.domain.LanyaDeviceCardSenderVehicleLog;
import com.ruoyi.system.service.ILanyaDeviceCardSenderVehicleLogService;

/**
 * 车辆发卡记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
public class LanyaDeviceCardSenderVehicleLogServiceImpl implements ILanyaDeviceCardSenderVehicleLogService 
{
    @Autowired
    private LanyaDeviceCardSenderVehicleLogMapper lanyaDeviceCardSenderVehicleLogMapper;

    /**
     * 查询车辆发卡记录
     * 
     * @param id 车辆发卡记录主键
     * @return 车辆发卡记录
     */
    @Override
    public LanyaDeviceCardSenderVehicleLog selectLanyaDeviceCardSenderVehicleLogById(Long id)
    {
        return lanyaDeviceCardSenderVehicleLogMapper.selectLanyaDeviceCardSenderVehicleLogById(id);
    }

    /**
     * 查询车辆发卡记录列表
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 车辆发卡记录
     */
    @Override
    public List<LanyaDeviceCardSenderVehicleLog> selectLanyaDeviceCardSenderVehicleLogList(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog)
    {
        return lanyaDeviceCardSenderVehicleLogMapper.selectLanyaDeviceCardSenderVehicleLogList(lanyaDeviceCardSenderVehicleLog);
    }

    /**
     * 新增车辆发卡记录
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 结果
     */
    @Override
    public int insertLanyaDeviceCardSenderVehicleLog(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog)
    {
        lanyaDeviceCardSenderVehicleLog.setCreateTime(DateUtils.getNowDate());
        return lanyaDeviceCardSenderVehicleLogMapper.insertLanyaDeviceCardSenderVehicleLog(lanyaDeviceCardSenderVehicleLog);
    }

    /**
     * 修改车辆发卡记录
     * 
     * @param lanyaDeviceCardSenderVehicleLog 车辆发卡记录
     * @return 结果
     */
    @Override
    public int updateLanyaDeviceCardSenderVehicleLog(LanyaDeviceCardSenderVehicleLog lanyaDeviceCardSenderVehicleLog)
    {
        lanyaDeviceCardSenderVehicleLog.setUpdateTime(DateUtils.getNowDate());
        return lanyaDeviceCardSenderVehicleLogMapper.updateLanyaDeviceCardSenderVehicleLog(lanyaDeviceCardSenderVehicleLog);
    }

    /**
     * 批量删除车辆发卡记录
     * 
     * @param ids 需要删除的车辆发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardSenderVehicleLogByIds(Long[] ids)
    {
        return lanyaDeviceCardSenderVehicleLogMapper.deleteLanyaDeviceCardSenderVehicleLogByIds(ids);
    }

    /**
     * 删除车辆发卡记录信息
     * 
     * @param id 车辆发卡记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaDeviceCardSenderVehicleLogById(Long id)
    {
        return lanyaDeviceCardSenderVehicleLogMapper.deleteLanyaDeviceCardSenderVehicleLogById(id);
    }
}
