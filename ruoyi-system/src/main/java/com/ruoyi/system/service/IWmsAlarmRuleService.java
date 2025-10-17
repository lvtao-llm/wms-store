package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WmsAlarmRule;

/**
 * 报警信息规则Service接口
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
public interface IWmsAlarmRuleService 
{
    /**
     * 查询报警信息规则
     * 
     * @param alarmRuleId 报警信息规则主键
     * @return 报警信息规则
     */
    public WmsAlarmRule selectWmsAlarmRuleByAlarmRuleId(Long alarmRuleId);

    /**
     * 查询报警信息规则列表
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 报警信息规则集合
     */
    public List<WmsAlarmRule> selectWmsAlarmRuleList(WmsAlarmRule wmsAlarmRule);

    /**
     * 新增报警信息规则
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 结果
     */
    public int insertWmsAlarmRule(WmsAlarmRule wmsAlarmRule);

    /**
     * 修改报警信息规则
     * 
     * @param wmsAlarmRule 报警信息规则
     * @return 结果
     */
    public int updateWmsAlarmRule(WmsAlarmRule wmsAlarmRule);

    /**
     * 批量删除报警信息规则
     * 
     * @param alarmRuleIds 需要删除的报警信息规则主键集合
     * @return 结果
     */
    public int deleteWmsAlarmRuleByAlarmRuleIds(Long[] alarmRuleIds);

    /**
     * 删除报警信息规则信息
     * 
     * @param alarmRuleId 报警信息规则主键
     * @return 结果
     */
    public int deleteWmsAlarmRuleByAlarmRuleId(Long alarmRuleId);
}
