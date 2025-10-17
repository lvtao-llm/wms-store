package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WmsAlarmRuleMapper;
import com.ruoyi.system.domain.WmsAlarmRule;
import com.ruoyi.system.service.IWmsAlarmRuleService;

/**
 * 报警信息规则Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
@Service
public class WmsAlarmRuleServiceImpl implements IWmsAlarmRuleService 
{
    @Autowired
    private WmsAlarmRuleMapper wmsAlarmRuleMapper;

    /**
     * 查询报警信息规则
     * 
     * @param alarmRuleId 报警信息规则主键
     * @return 报警信息规则
     */
    @Override
    public WmsAlarmRule selectWmsAlarmRuleByAlarmRuleId(Long alarmRuleId)
    {
        return wmsAlarmRuleMapper.selectWmsAlarmRuleByAlarmRuleId(alarmRuleId);
    }

    /**
     * 查询报警信息规则列表
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 报警信息规则
     */
    @Override
    public List<WmsAlarmRule> selectWmsAlarmRuleList(WmsAlarmRule wmsAlarmRule)
    {
        return wmsAlarmRuleMapper.selectWmsAlarmRuleList(wmsAlarmRule);
    }

    /**
     * 新增报警信息规则
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 结果
     */
    @Override
    public int insertWmsAlarmRule(WmsAlarmRule wmsAlarmRule)
    {
        return wmsAlarmRuleMapper.insertWmsAlarmRule(wmsAlarmRule);
    }

    /**
     * 修改报警信息规则
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 结果
     */
    @Override
    public int updateWmsAlarmRule(WmsAlarmRule wmsAlarmRule)
    {
        return wmsAlarmRuleMapper.updateWmsAlarmRule(wmsAlarmRule);
    }

    /**
     * 批量删除报警信息规则
     * 
     * @param alarmRuleIds 需要删除的报警信息规则主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmRuleByAlarmRuleIds(Long[] alarmRuleIds)
    {
        return wmsAlarmRuleMapper.deleteWmsAlarmRuleByAlarmRuleIds(alarmRuleIds);
    }

    /**
     * 删除报警信息规则信息
     * 
     * @param alarmRuleId 报警信息规则主键
     * @return 结果
     */
    @Override
    public int deleteWmsAlarmRuleByAlarmRuleId(Long alarmRuleId)
    {
        return wmsAlarmRuleMapper.deleteWmsAlarmRuleByAlarmRuleId(alarmRuleId);
    }
}
