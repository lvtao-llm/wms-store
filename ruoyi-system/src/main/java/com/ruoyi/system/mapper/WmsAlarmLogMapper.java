package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WmsAlarmLog;

/**
 * 报警信息记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
public interface WmsAlarmLogMapper 
{
    /**
     * 查询报警信息记录
     * 
     * @param alarmId 报警信息记录主键
     * @return 报警信息记录
     */
    public WmsAlarmLog selectWmsAlarmLogByAlarmId(Long alarmId);

    /**
     * 查询报警信息记录列表
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 报警信息记录集合
     */
    public List<WmsAlarmLog> selectWmsAlarmLogList(WmsAlarmLog wmsAlarmLog);

    /**
     * 新增报警信息记录
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 结果
     */
    public int insertWmsAlarmLog(WmsAlarmLog wmsAlarmLog);

    /**
     * 修改报警信息记录
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 结果
     */
    public int updateWmsAlarmLog(WmsAlarmLog wmsAlarmLog);

    /**
     * 删除报警信息记录
     * 
     * @param alarmId 报警信息记录主键
     * @return 结果
     */
    public int deleteWmsAlarmLogByAlarmId(Long alarmId);

    /**
     * 批量删除报警信息记录
     * 
     * @param alarmIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsAlarmLogByAlarmIds(Long[] alarmIds);
}
