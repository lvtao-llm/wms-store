package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsAlarmMapper;
import com.ruoyi.system.domain.WmsAlarm;
import com.ruoyi.system.service.IWmsAlarmService;

/**
 * 报警信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class WmsAlarmServiceImpl implements IWmsAlarmService 
{
    @Autowired
    private WmsAlarmMapper wmsAlarmMapper;

    /**
     * 查询报警信息
     * 
     * @param alarmId 报警信息主键
     * @return 报警信息
     */
    @Override
    public WmsAlarm selectWmsAlarmByAlarmId(Long alarmId)
    {
        return wmsAlarmMapper.selectWmsAlarmByAlarmId(alarmId);
    }

    /**
     * 查询报警信息列表
     * 
     * @param wmsAlarm 报警信息
     * @return 报警信息
     */
    @Override
    public List<WmsAlarm> selectWmsAlarmList(WmsAlarm wmsAlarm)
    {
        return wmsAlarmMapper.selectWmsAlarmList(wmsAlarm);
    }

    /**
     * 新增报警信息
     * 
     * @param wmsAlarm 报警信息
     * @return 结果
     */
    @Override
    public int insertWmsAlarm(WmsAlarm wmsAlarm)
    {
        wmsAlarm.setCreateTime(DateUtils.getNowDate());
        return wmsAlarmMapper.insertWmsAlarm(wmsAlarm);
    }

    /**
     * 修改报警信息
     * 
     * @param wmsAlarm 报警信息
     * @return 结果
     */
    @Override
    public int updateWmsAlarm(WmsAlarm wmsAlarm)
    {
        wmsAlarm.setUpdateTime(DateUtils.getNowDate());
        return wmsAlarmMapper.updateWmsAlarm(wmsAlarm);
    }

    /**
     * 批量删除报警信息
     * 
     * @param alarmIds 需要删除的报警信息主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmByAlarmIds(Long[] alarmIds)
    {
        return wmsAlarmMapper.deleteWmsAlarmByAlarmIds(alarmIds);
    }

    /**
     * 删除报警信息信息
     * 
     * @param alarmId 报警信息主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmByAlarmId(Long alarmId)
    {
        return wmsAlarmMapper.deleteWmsAlarmByAlarmId(alarmId);
    }
}
