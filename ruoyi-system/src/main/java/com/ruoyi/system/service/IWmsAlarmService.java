package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsAlarm;

/**
 * 报警信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IWmsAlarmService 
{
    /**
     * 查询报警信息
     * 
     * @param alarmId 报警信息主键
     * @return 报警信息
     */
    public WmsAlarm selectWmsAlarmByAlarmId(Long alarmId);

    /**
     * 查询报警信息列表
     * 
     * @param wmsAlarm 报警信息
     * @return 报警信息集合
     */
    public List<WmsAlarm> selectWmsAlarmList(WmsAlarm wmsAlarm);

    /**
     * 新增报警信息
     * 
     * @param wmsAlarm 报警信息
     * @return 结果
     */
    public int insertWmsAlarm(WmsAlarm wmsAlarm);

    /**
     * 修改报警信息
     * 
     * @param wmsAlarm 报警信息
     * @return 结果
     */
    public int updateWmsAlarm(WmsAlarm wmsAlarm);

    /**
     * 批量删除报警信息
     * 
     * @param alarmIds 需要删除的报警信息主键集合
     * @return 结果
     */
    public int deleteWmsAlarmByAlarmIds(Long[] alarmIds);

    /**
     * 删除报警信息信息
     * 
     * @param alarmId 报警信息主键
     * @return 结果
     */
    public int deleteWmsAlarmByAlarmId(Long alarmId);
}
