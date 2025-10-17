package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsAlarmLogMapper;
import com.ruoyi.system.domain.WmsAlarmLog;
import com.ruoyi.system.service.IWmsAlarmLogService;

/**
 * 报警信息记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
@Service
public class WmsAlarmLogServiceImpl implements IWmsAlarmLogService 
{
    @Autowired
    private WmsAlarmLogMapper wmsAlarmLogMapper;

    /**
     * 查询报警信息记录
     * 
     * @param alarmId 报警信息记录主键
     * @return 报警信息记录
     */
    @Override
    public WmsAlarmLog selectWmsAlarmLogByAlarmId(Long alarmId)
    {
        return wmsAlarmLogMapper.selectWmsAlarmLogByAlarmId(alarmId);
    }

    /**
     * 查询报警信息记录列表
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 报警信息记录
     */
    @Override
    public List<WmsAlarmLog> selectWmsAlarmLogList(WmsAlarmLog wmsAlarmLog)
    {
        return wmsAlarmLogMapper.selectWmsAlarmLogList(wmsAlarmLog);
    }

    /**
     * 新增报警信息记录
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 结果
     */
    @Override
    public int insertWmsAlarmLog(WmsAlarmLog wmsAlarmLog)
    {
        wmsAlarmLog.setCreateTime(DateUtils.getNowDate());
        return wmsAlarmLogMapper.insertWmsAlarmLog(wmsAlarmLog);
    }

    /**
     * 修改报警信息记录
     * 
     * @param wmsAlarmLog 报警信息记录
     * @return 结果
     */
    @Override
    public int updateWmsAlarmLog(WmsAlarmLog wmsAlarmLog)
    {
        wmsAlarmLog.setUpdateTime(DateUtils.getNowDate());
        return wmsAlarmLogMapper.updateWmsAlarmLog(wmsAlarmLog);
    }

    /**
     * 批量删除报警信息记录
     * 
     * @param alarmIds 需要删除的报警信息记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmLogByAlarmIds(Long[] alarmIds)
    {
        return wmsAlarmLogMapper.deleteWmsAlarmLogByAlarmIds(alarmIds);
    }

    /**
     * 删除报警信息记录信息
     * 
     * @param alarmId 报警信息记录主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmLogByAlarmId(Long alarmId)
    {
        return wmsAlarmLogMapper.deleteWmsAlarmLogByAlarmId(alarmId);
    }
}
