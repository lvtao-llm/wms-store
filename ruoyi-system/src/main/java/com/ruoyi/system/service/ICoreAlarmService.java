package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CoreAlarm;

/**
 * 报警记录Service接口
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
public interface ICoreAlarmService 
{
    /**
     * 查询报警记录
     * 
     * @param alarmId 报警记录主键
     * @return 报警记录
     */
    public CoreAlarm selectCoreAlarmByAlarmId(Long alarmId);

    /**
     * 查询报警记录列表
     * 
     * @param coreAlarm 报警记录
     * @return 报警记录集合
     */
    public List<CoreAlarm> selectCoreAlarmList(CoreAlarm coreAlarm);

    /**
     * 新增报警记录
     * 
     * @param coreAlarm 报警记录
     * @return 结果
     */
    public int insertCoreAlarm(CoreAlarm coreAlarm);

    /**
     * 修改报警记录
     * 
     * @param coreAlarm 报警记录
     * @return 结果
     */
    public int updateCoreAlarm(CoreAlarm coreAlarm);

    /**
     * 批量删除报警记录
     * 
     * @param alarmIds 需要删除的报警记录主键集合
     * @return 结果
     */
    public int deleteCoreAlarmByAlarmIds(Long[] alarmIds);

    /**
     * 删除报警记录信息
     * 
     * @param alarmId 报警记录主键
     * @return 结果
     */
    public int deleteCoreAlarmByAlarmId(Long alarmId);
}
