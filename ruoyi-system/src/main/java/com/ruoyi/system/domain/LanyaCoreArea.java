package com.ruoyi.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 区域信息对象 core_area
 *
 * @author 吕涛
 * @date 2025-10-10
 */
@ApiModel(description = "区域信息对象")
public class LanyaCoreArea extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 区域id
     */
    @ApiModelProperty("区域id")
    private Long areaId;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    @ApiModelProperty("区域名称")
    private String areaName;

    /**
     * 启用状态Y|N
     */
    @Excel(name = "启用状态Y|N")
    @ApiModelProperty("启用状态Y|N")
    private String areaEnable;

    /**
     * 报警类型（见字典）
     */
    @Excel(name = "报警类型", readConverterExp = "见=字典")
    @ApiModelProperty("报警类型（见字典）")
    private String alarmType;

    /**
     * 报警规则(人/秒/1级聚集报警:聚集半径,持续时长,人数)
     */
    @Excel(name = "报警规则(人/秒/1级聚集报警:聚集半径,持续时长,人数)")
    @ApiModelProperty("报警规则(人/秒/1级聚集报警:聚集半径,持续时长,人数)")
    private String rule;

    /**
     * 生效开始日期
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生效开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "生效开始日期")
    @ApiModelProperty("生效开始日期")
    private Date validBeginDate;

    /**
     * 生效结束日期
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生效结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "生效结束日期")
    @ApiModelProperty("生效结束日期")
    private Date validEndDate;

    /**
     * 生效开始时段
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生效开始时段", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "生效开始时段")
    @ApiModelProperty("生效开始时段")
    private Date validBeginTime;

    /**
     * 生效结束时段
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生效结束时段", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "生效结束时段")
    @ApiModelProperty("生效结束时段")
    private Date validEndTime;

    /**
     * 通知配置id(表core_alarm_notice)
     */
    @Excel(name = "通知配置id(表core_alarm_notice)")
    @ApiModelProperty("通知配置id(表core_alarm_notice)")
    private Long alarmNoticeId;

    /**
     * 2级聚集报警:聚集半径,持续时长,人数
     */
    @Excel(name = "2级聚集报警:聚集半径,持续时长,人数")
    @ApiModelProperty("2级聚集报警:聚集半径,持续时长,人数")
    private String secondAlarmRule;

    /**
     * 2级报警通知id
     */
    @Excel(name = "2级报警通知id")
    @ApiModelProperty("2级报警通知id")
    private Long secondAlarmNoticeId;

    /**
     * 3级聚集报警:聚集半径,持续时长,人数
     */
    @Excel(name = "3级聚集报警:聚集半径,持续时长,人数")
    @ApiModelProperty("3级聚集报警:聚集半径,持续时长,人数")
    private String thirdAlarmRule;

    /**
     * 3级报警通知id
     */
    @Excel(name = "3级报警通知id")
    @ApiModelProperty("3级报警通知id")
    private Long thirdAlarmNoticeId;

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaEnable(String areaEnable) {
        this.areaEnable = areaEnable;
    }

    public String getAreaEnable() {
        return areaEnable;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }

    public void setValidBeginDate(Date validBeginDate) {
        this.validBeginDate = validBeginDate;
    }

    public Date getValidBeginDate() {
        return validBeginDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidBeginTime(Date validBeginTime) {
        this.validBeginTime = validBeginTime;
    }

    public Date getValidBeginTime() {
        return validBeginTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setAlarmNoticeId(Long alarmNoticeId) {
        this.alarmNoticeId = alarmNoticeId;
    }

    public Long getAlarmNoticeId() {
        return alarmNoticeId;
    }

    public void setSecondAlarmRule(String secondAlarmRule) {
        this.secondAlarmRule = secondAlarmRule;
    }

    public String getSecondAlarmRule() {
        return secondAlarmRule;
    }

    public void setSecondAlarmNoticeId(Long secondAlarmNoticeId) {
        this.secondAlarmNoticeId = secondAlarmNoticeId;
    }

    public Long getSecondAlarmNoticeId() {
        return secondAlarmNoticeId;
    }

    public void setThirdAlarmRule(String thirdAlarmRule) {
        this.thirdAlarmRule = thirdAlarmRule;
    }

    public String getThirdAlarmRule() {
        return thirdAlarmRule;
    }

    public void setThirdAlarmNoticeId(Long thirdAlarmNoticeId) {
        this.thirdAlarmNoticeId = thirdAlarmNoticeId;
    }

    public Long getThirdAlarmNoticeId() {
        return thirdAlarmNoticeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("areaId", getAreaId())
                .append("areaName", getAreaName())
                .append("areaEnable", getAreaEnable())
                .append("alarmType", getAlarmType())
                .append("rule", getRule())
                .append("validBeginDate", getValidBeginDate())
                .append("validEndDate", getValidEndDate())
                .append("validBeginTime", getValidBeginTime())
                .append("validEndTime", getValidEndTime())
                .append("alarmNoticeId", getAlarmNoticeId())
                .append("secondAlarmRule", getSecondAlarmRule())
                .append("secondAlarmNoticeId", getSecondAlarmNoticeId())
                .append("thirdAlarmRule", getThirdAlarmRule())
                .append("thirdAlarmNoticeId", getThirdAlarmNoticeId())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
