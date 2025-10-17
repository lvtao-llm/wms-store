package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报警信息规则对象 wms_alarm_rule
 * 
 * @author ruoyi
 * @date 2025-10-17
 */
public class WmsAlarmRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则IDt */
    @Excel(name = "规则ID", readConverterExp = "规则ID.readConverterExp()")
    private Long alarmRuleId;

    /** 规则名称 */
    @Excel(name = "规则名称", readConverterExp = "规则名称.readConverterExp()")
    private String alarmRuleName;

    /** 时间阈值 */
    @Excel(name = "时间阈值", readConverterExp = "时间阈值.readConverterExp()")
    private String alarmRuleTimeThreshold;

    /** 距离阈值 */
    @Excel(name = "距离阈值", readConverterExp = "距离阈值.readConverterExp()")
    private String alarmRuleDistThreshold;

    /** 目标区域 */
    @Excel(name = "目标区域", readConverterExp = "目标区域.readConverterExp()")
    private String alarmRuleTargetAreaCode;

    public void setAlarmRuleId(Long alarmRuleId) 
    {
        this.alarmRuleId = alarmRuleId;
    }

    public Long getAlarmRuleId() 
    {
        return alarmRuleId;
    }

    public void setAlarmRuleName(String alarmRuleName) 
    {
        this.alarmRuleName = alarmRuleName;
    }

    public String getAlarmRuleName() 
    {
        return alarmRuleName;
    }

    public void setAlarmRuleTimeThreshold(String alarmRuleTimeThreshold) 
    {
        this.alarmRuleTimeThreshold = alarmRuleTimeThreshold;
    }

    public String getAlarmRuleTimeThreshold() 
    {
        return alarmRuleTimeThreshold;
    }

    public void setAlarmRuleDistThreshold(String alarmRuleDistThreshold) 
    {
        this.alarmRuleDistThreshold = alarmRuleDistThreshold;
    }

    public String getAlarmRuleDistThreshold()
    {
        return alarmRuleDistThreshold;
    }

    public void setAlarmRuleTargetAreaCode(String alarmRuleTargetAreaCode) 
    {
        this.alarmRuleTargetAreaCode = alarmRuleTargetAreaCode;
    }

    public String getAlarmRuleTargetAreaCode() 
    {
        return alarmRuleTargetAreaCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("alarmRuleId", getAlarmRuleId())
            .append("alarmRuleName", getAlarmRuleName())
            .append("alarmRuleTimeThreshold", getAlarmRuleTimeThreshold())
            .append("alarmRuleDistThreshold", getAlarmRuleDistThreshold())
            .append("alarmRuleTargetAreaCode", getAlarmRuleTargetAreaCode())
            .toString();
    }
}
