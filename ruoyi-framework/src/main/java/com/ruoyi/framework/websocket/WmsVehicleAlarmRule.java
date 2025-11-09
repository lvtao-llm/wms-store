package com.ruoyi.framework.websocket;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/11/6
 */
public class WmsVehicleAlarmRule {
    private String alarmRuleId;
    private String alarmRuleName;
    private final double percentage;
    private final int count;

    public WmsVehicleAlarmRule(String alarmRuleName, double percentage, int count) {
        this.alarmRuleName = alarmRuleName;
        this.percentage = percentage;
        this.count = count;
    }

    public String getAlarmRuleId() {
        return alarmRuleId;
    }

    public void setAlarmRuleId(String alarmRuleId) {
        this.alarmRuleId = alarmRuleId;
    }

    public String getAlarmRuleName() {
        return alarmRuleName;
    }

    public void setAlarmRuleName(String alarmRuleName) {
        this.alarmRuleName = alarmRuleName;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getCount() {
        return count;
    }
}
