package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsSystemLog;

/**
 * 定时任务调度日志Service接口
 * 
 * @author ruoyi
 * @date 2025-11-24
 */
public interface IWmsSystemLogService 
{
    /**
     * 查询定时任务调度日志
     * 
     * @param jobLogId 定时任务调度日志主键
     * @return 定时任务调度日志
     */
    public WmsSystemLog selectWmsSystemLogByJobLogId(Long jobLogId);

    /**
     * 查询定时任务调度日志列表
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
    public List<WmsSystemLog> selectWmsSystemLogList(WmsSystemLog wmsSystemLog);

    /**
     * 新增定时任务调度日志
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 结果
     */
    public int insertWmsSystemLog(WmsSystemLog wmsSystemLog);

    /**
     * 修改定时任务调度日志
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 结果
     */
    public int updateWmsSystemLog(WmsSystemLog wmsSystemLog);

    /**
     * 批量删除定时任务调度日志
     * 
     * @param jobLogIds 需要删除的定时任务调度日志主键集合
     * @return 结果
     */
    public int deleteWmsSystemLogByJobLogIds(Long[] jobLogIds);

    /**
     * 删除定时任务调度日志信息
     * 
     * @param jobLogId 定时任务调度日志主键
     * @return 结果
     */
    public int deleteWmsSystemLogByJobLogId(Long jobLogId);
}
