package com.ruoyi.system.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.LanyaCoreAlarm;

/**
 * 报警记录Service接口
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public interface ILanyaCoreAlarmService 
{
    /**
     * 查询报警记录
     * 
     * @param alarmId 报警记录主键
     * @return 报警记录
     */
    public LanyaCoreAlarm selectLanyaCoreAlarmByAlarmId(Long alarmId);

    /**
     * 查询报警记录列表
     * 
     * @param lanyaCoreAlarm 报警记录
     * @return 报警记录集合
     */
    public List<LanyaCoreAlarm> selectLanyaCoreAlarmList(LanyaCoreAlarm lanyaCoreAlarm);

    /**
     * 新增报警记录
     * 
     * @param lanyaCoreAlarm 报警记录
     * @return 结果
     */
    public int insertLanyaCoreAlarm(LanyaCoreAlarm lanyaCoreAlarm);

    /**
     * 修改报警记录
     * 
     * @param lanyaCoreAlarm 报警记录
     * @return 结果
     */
    public int updateLanyaCoreAlarm(LanyaCoreAlarm lanyaCoreAlarm);

    /**
     * 批量删除报警记录
     * 
     * @param alarmIds 需要删除的报警记录主键集合
     * @return 结果
     */
    public int deleteLanyaCoreAlarmByAlarmIds(Long[] alarmIds);

    /**
     * 删除报警记录信息
     * 
     * @param alarmId 报警记录主键
     * @return 结果
     */
    public int deleteLanyaCoreAlarmByAlarmId(Long alarmId);

    List<LanyaCoreAlarm> selectLanyaCoreAlarmGtTime(Date date);
}
