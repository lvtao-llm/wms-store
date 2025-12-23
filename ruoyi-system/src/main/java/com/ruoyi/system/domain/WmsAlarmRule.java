package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报警信息规则对象 wms_alarm_rule
 *
 * @author ruoyi
 * @date 2025-10-20
 */
@ApiModel("报警信息规则对象")
public class WmsAlarmRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Excel(name = "ID")
    @ApiModelProperty("主键")
    private Long alarmRuleId;

    /**
     * 规则名
     */
    @Excel(name = "规则名")
    @ApiModelProperty("规则名")
    private String alarmRuleName;

    /**
     * 时间阈值
     */
    @Excel(name = "时间阈值")
    @ApiModelProperty("时间阈值")
    private Double alarmRuleTimeThreshold;

    /**
     * 距离阈值
     */
    @Excel(name = "距离阈值")
    @ApiModelProperty("距离阈值")
    private Double alarmRuleDistThreshold;

    /**
     * 目标区域
     */
    @Excel(name = "目标区域")
    @ApiModelProperty("目标区域")
    private String alarmRuleTargetAreaCode;

    /**
     * 报警类型
     */
    @Excel(name = "报警类型")
    @ApiModelProperty("报警类型")
    private String alarmRuleType;

    /**
     * 生效周期开始时间cron
     */
    @Excel(name = "生效周期开始时间cron")
    @ApiModelProperty("生效周期开始时间cron")
    private String alarmRuleEffectivePeriodS;

    /**
     * 生效周期结束时间cron
     */
    @Excel(name = "生效周期结束时间cron")
    @ApiModelProperty("生效周期结束时间cron")
    private String alarmRuleEffectivePeriodE;

    /**
     * 是否启用
     */
    @Excel(name = "是否启用")
    @ApiModelProperty("是否启用")
    private String alarmRuleEnabled;

    /**
     * 关联人员
     */
    @Excel(name = "关联人员")
    @ApiModelProperty("关联人员")
    private String alarmRuleRelatedPeople;

    /**
     * 关联岗位
     */
    @Excel(name = "关联岗位")
    @ApiModelProperty("关联岗位")
    private String alarmRuleRelatedDept;

    /**
     * 通知方式
     */
    @Excel(name = "通知方式")
    @ApiModelProperty("通知方式")
    private String alarmRuleNoticeType;

    /**
     * 短信通知人员
     */
    @Excel(name = "短信通知人员")
    @ApiModelProperty("短信通知人员")
    private String smsNoticeUsers;

    /**
     * 即时通通知人员
     */
    @Excel(name = "即时通通知人员")
    @ApiModelProperty("即时通通知人员")
    private String imNoticeUsers;

    /**
     * 系统通知人员
     */
    @Excel(name = "系统通知人员")
    @ApiModelProperty("系统通知人员")
    private String sysNoticeUsers;

    /**
     * 报警类型
     */
    @Excel(name = "报警类型")
    @ApiModelProperty("报警类型")
    private String alarmType;

    /**
     * 最大人数
     */
    @ApiModelProperty("最大人数")
    private Integer maxPeopleCount;

    /**
     * 百分比
     */
    @ApiModelProperty("百分比")
    private double percentage = 0;

    /**
     * 统计数量
     */
    @ApiModelProperty("统计数量")
    private int count = 0;

    public void setAlarmRuleId(Long alarmRuleId) {
        this.alarmRuleId = alarmRuleId;
    }

    public Long getAlarmRuleId() {
        return alarmRuleId;
    }

    public void setAlarmRuleName(String alarmRuleName) {
        this.alarmRuleName = alarmRuleName;
    }

    public String getAlarmRuleName() {
        return alarmRuleName;
    }

    public void setAlarmRuleTimeThreshold(Double alarmRuleTimeThreshold) {
        this.alarmRuleTimeThreshold = alarmRuleTimeThreshold;
    }

    public Double getAlarmRuleTimeThreshold() {
        return alarmRuleTimeThreshold;
    }

    public void setAlarmRuleDistThreshold(Double alarmRuleDistThreshold) {
        this.alarmRuleDistThreshold = alarmRuleDistThreshold;
    }

    public Double getAlarmRuleDistThreshold() {
        return alarmRuleDistThreshold;
    }

    public void setAlarmRuleTargetAreaCode(String alarmRuleTargetAreaCode) {
        this.alarmRuleTargetAreaCode = alarmRuleTargetAreaCode;
    }

    public String getAlarmRuleTargetAreaCode() {
        return alarmRuleTargetAreaCode;
    }

    public void setAlarmRuleType(String alarmRuleType) {
        this.alarmRuleType = alarmRuleType;
    }

    public String getAlarmRuleType() {
        return alarmRuleType;
    }

    public void setAlarmRuleEffectivePeriodS(String alarmRuleEffectivePeriodS) {
        this.alarmRuleEffectivePeriodS = alarmRuleEffectivePeriodS;
    }

    public String getAlarmRuleEffectivePeriodS() {
        return alarmRuleEffectivePeriodS;
    }

    public void setAlarmRuleEffectivePeriodE(String alarmRuleEffectivePeriodE) {
        this.alarmRuleEffectivePeriodE = alarmRuleEffectivePeriodE;
    }

    public String getAlarmRuleEffectivePeriodE() {
        return alarmRuleEffectivePeriodE;
    }

    public void setAlarmRuleEnabled(String alarmRuleEnabled) {
        this.alarmRuleEnabled = alarmRuleEnabled;
    }

    public String getAlarmRuleEnabled() {
        return alarmRuleEnabled;
    }

    public void setAlarmRuleRelatedPeople(String alarmRuleRelatedPeople) {
        this.alarmRuleRelatedPeople = alarmRuleRelatedPeople;
    }

    public String getAlarmRuleRelatedPeople() {
        return alarmRuleRelatedPeople;
    }

    public void setAlarmRuleRelatedDept(String alarmRuleRelatedDept) {
        this.alarmRuleRelatedDept = alarmRuleRelatedDept;
    }

    public String getAlarmRuleRelatedDept() {
        return alarmRuleRelatedDept;
    }

    public void setAlarmRuleNoticeType(String alarmRuleNoticeType) {
        this.alarmRuleNoticeType = alarmRuleNoticeType;
    }

    public String getAlarmRuleNoticeType() {
        return alarmRuleNoticeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("alarmRuleId", getAlarmRuleId())
                .append("alarmRuleName", getAlarmRuleName())
                .append("alarmRuleTimeThreshold", getAlarmRuleTimeThreshold())
                .append("alarmRuleDistThreshold", getAlarmRuleDistThreshold())
                .append("alarmRuleTargetAreaCode", getAlarmRuleTargetAreaCode())
                .append("alarmRuleType", getAlarmRuleType())
                .append("alarmRuleEffectivePeriodS", getAlarmRuleEffectivePeriodS())
                .append("alarmRuleEffectivePeriodE", getAlarmRuleEffectivePeriodE())
                .append("alarmRuleEnabled", getAlarmRuleEnabled())
                .append("alarmRuleRelatedPeople", getAlarmRuleRelatedPeople())
                .append("alarmRuleRelatedDept", getAlarmRuleRelatedDept())
                .append("alarmRuleNoticeType", getAlarmRuleNoticeType())
                .toString();
    }

    public Integer getMaxPeopleCount() {
        return maxPeopleCount;
    }

    public void setMaxPeopleCount(Integer maxPeopleCount) {
        this.maxPeopleCount = maxPeopleCount;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getSmsNoticeUsers() {
        return smsNoticeUsers;
    }

    public void setSmsNoticeUsers(String smsNoticeUsers) {
        this.smsNoticeUsers = smsNoticeUsers;
    }

    public String getImNoticeUsers() {
        return imNoticeUsers;
    }

    public void setImNoticeUsers(String imNoticeUsers) {
        this.imNoticeUsers = imNoticeUsers;
    }

    public String getSysNoticeUsers() {
        return sysNoticeUsers;
    }

    public void setSysNoticeUsers(String sysNoticeUsers) {
        this.sysNoticeUsers = sysNoticeUsers;
    }
}
