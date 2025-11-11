package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CoreAlarmMapper;
import com.ruoyi.system.domain.CoreAlarm;
import com.ruoyi.system.service.ICoreAlarmService;

/**
 * 报警记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
@Service
public class CoreAlarmServiceImpl implements ICoreAlarmService 
{
    @Autowired
    private CoreAlarmMapper coreAlarmMapper;

    /**
     * 查询报警记录
     * 
     * @param alarmId 报警记录主键
     * @return 报警记录
     */
    @Override
    public CoreAlarm selectCoreAlarmByAlarmId(Long alarmId)
    {
        return coreAlarmMapper.selectCoreAlarmByAlarmId(alarmId);
    }

    /**
     * 查询报警记录列表
     * 
     * @param coreAlarm 报警记录
     * @return 报警记录
     */
    @Override
    public List<CoreAlarm> selectCoreAlarmList(CoreAlarm coreAlarm)
    {
        return coreAlarmMapper.selectCoreAlarmList(coreAlarm);
    }

    /**
     * 新增报警记录
     * 
     * @param coreAlarm 报警记录
     * @return 结果
     */
    @Override
    public int insertCoreAlarm(CoreAlarm coreAlarm)
    {
        coreAlarm.setCreateTime(DateUtils.getNowDate());
        return coreAlarmMapper.insertCoreAlarm(coreAlarm);
    }

    /**
     * 修改报警记录
     * 
     * @param coreAlarm 报警记录
     * @return 结果
     */
    @Override
    public int updateCoreAlarm(CoreAlarm coreAlarm)
    {
        coreAlarm.setUpdateTime(DateUtils.getNowDate());
        return coreAlarmMapper.updateCoreAlarm(coreAlarm);
    }

    /**
     * 批量删除报警记录
     * 
     * @param alarmIds 需要删除的报警记录主键
     * @return 结果
     */
    @Override
    public int deleteCoreAlarmByAlarmIds(Long[] alarmIds)
    {
        return coreAlarmMapper.deleteCoreAlarmByAlarmIds(alarmIds);
    }

    /**
     * 删除报警记录信息
     * 
     * @param alarmId 报警记录主键
     * @return 结果
     */
    @Override
    public int deleteCoreAlarmByAlarmId(Long alarmId)
    {
        return coreAlarmMapper.deleteCoreAlarmByAlarmId(alarmId);
    }
}
