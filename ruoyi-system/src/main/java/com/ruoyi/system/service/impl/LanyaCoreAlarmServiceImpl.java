package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LanyaCoreAlarmMapper;
import com.ruoyi.system.domain.LanyaCoreAlarm;
import com.ruoyi.system.service.ILanyaCoreAlarmService;

/**
 * 报警记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@Service
@DataSource(DataSourceType.SLAVE)
public class LanyaCoreAlarmServiceImpl implements ILanyaCoreAlarmService {
    @Autowired
    private LanyaCoreAlarmMapper lanyaCoreAlarmMapper;

    /**
     * 查询报警记录
     *
     * @param alarmId 报警记录主键
     * @return 报警记录
     */
    @Override
    public LanyaCoreAlarm selectLanyaCoreAlarmByAlarmId(Long alarmId) {
        return lanyaCoreAlarmMapper.selectLanyaCoreAlarmByAlarmId(alarmId);
    }

    /**
     * 查询报警记录列表
     *
     * @param lanyaCoreAlarm 报警记录
     * @return 报警记录
     */
    @Override
    public List<LanyaCoreAlarm> selectLanyaCoreAlarmList(LanyaCoreAlarm lanyaCoreAlarm) {
        return lanyaCoreAlarmMapper.selectLanyaCoreAlarmList(lanyaCoreAlarm);
    }

    /**
     * 新增报警记录
     *
     * @param lanyaCoreAlarm 报警记录
     * @return 结果
     */
    @Override
    public int insertLanyaCoreAlarm(LanyaCoreAlarm lanyaCoreAlarm) {
        lanyaCoreAlarm.setCreateTime(DateUtils.getNowDate());
        return lanyaCoreAlarmMapper.insertLanyaCoreAlarm(lanyaCoreAlarm);
    }

    /**
     * 修改报警记录
     *
     * @param lanyaCoreAlarm 报警记录
     * @return 结果
     */
    @Override
    public int updateLanyaCoreAlarm(LanyaCoreAlarm lanyaCoreAlarm) {
        lanyaCoreAlarm.setUpdateTime(DateUtils.getNowDate());
        return lanyaCoreAlarmMapper.updateLanyaCoreAlarm(lanyaCoreAlarm);
    }

    /**
     * 批量删除报警记录
     *
     * @param alarmIds 需要删除的报警记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAlarmByAlarmIds(Long[] alarmIds) {
        return lanyaCoreAlarmMapper.deleteLanyaCoreAlarmByAlarmIds(alarmIds);
    }

    /**
     * 删除报警记录信息
     *
     * @param alarmId 报警记录主键
     * @return 结果
     */
    @Override
    public int deleteLanyaCoreAlarmByAlarmId(Long alarmId) {
        return lanyaCoreAlarmMapper.deleteLanyaCoreAlarmByAlarmId(alarmId);
    }

    @Override
    public List<LanyaCoreAlarm> selectLanyaCoreAlarmGtTime(Date date) {
        return lanyaCoreAlarmMapper.selectLanyaCoreAlarmGtTime(date);
    }
}
