package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsSystemLogMapper;
import com.ruoyi.system.domain.WmsSystemLog;
import com.ruoyi.system.service.IWmsSystemLogService;

/**
 * 定时任务调度日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-24
 */
@Service
public class WmsSystemLogServiceImpl implements IWmsSystemLogService 
{
    @Autowired
    private WmsSystemLogMapper wmsSystemLogMapper;

    /**
     * 查询定时任务调度日志
     * 
     * @param jobLogId 定时任务调度日志主键
     * @return 定时任务调度日志
     */
    @Override
    public WmsSystemLog selectWmsSystemLogByJobLogId(Long jobLogId)
    {
        return wmsSystemLogMapper.selectWmsSystemLogByJobLogId(jobLogId);
    }

    /**
     * 查询定时任务调度日志列表
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 定时任务调度日志
     */
    @Override
    public List<WmsSystemLog> selectWmsSystemLogList(WmsSystemLog wmsSystemLog)
    {
        return wmsSystemLogMapper.selectWmsSystemLogList(wmsSystemLog);
    }

    /**
     * 新增定时任务调度日志
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 结果
     */
    @Override
    public int insertWmsSystemLog(WmsSystemLog wmsSystemLog)
    {
        wmsSystemLog.setCreateTime(DateUtils.getNowDate());
        return wmsSystemLogMapper.insertWmsSystemLog(wmsSystemLog);
    }

    /**
     * 修改定时任务调度日志
     * 
     * @param wmsSystemLog 定时任务调度日志
     * @return 结果
     */
    @Override
    public int updateWmsSystemLog(WmsSystemLog wmsSystemLog)
    {
        return wmsSystemLogMapper.updateWmsSystemLog(wmsSystemLog);
    }

    /**
     * 批量删除定时任务调度日志
     * 
     * @param jobLogIds 需要删除的定时任务调度日志主键
     * @return 结果
     */
    @Override
    public int deleteWmsSystemLogByJobLogIds(Long[] jobLogIds)
    {
        return wmsSystemLogMapper.deleteWmsSystemLogByJobLogIds(jobLogIds);
    }

    /**
     * 删除定时任务调度日志信息
     * 
     * @param jobLogId 定时任务调度日志主键
     * @return 结果
     */
    @Override
    public int deleteWmsSystemLogByJobLogId(Long jobLogId)
    {
        return wmsSystemLogMapper.deleteWmsSystemLogByJobLogId(jobLogId);
    }
}
