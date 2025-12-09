package com.ruoyi.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报警信息记录对象 wms_alarm_log
 *
 * @author ruoyi
 * @date 2025-10-17
 */
public class WmsAlarmLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long alarmId;

    /**
     * 报警类型：0人员，1车辆
     */
    @Excel(name = "报警类型：0人员，1车辆")
    private String alarmType;

    /**
     * 发卡记录ID
     */
    @Excel(name = "发卡记录ID")
    private Long cardRecordId;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String personName;

    /**
     * 工号
     */
    @Excel(name = "工号")
    private String personJobNo;

    /**
     * 所属部门
     */
    @Excel(name = "所属部门")
    private String personDept;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String vehiclePlateNo;

    /**
     * 车型
     */
    @Excel(name = "车型")
    private String vehicleType;

    /**
     * 所在位置{x,y}
     */
    @Excel(name = "所在位置{x,y}")
    private String alarmLocation;

    /**
     * 所在区域CODE
     */
    @Excel(name = "所在区域CODE")
    private String areaCode;

    /**
     * 进入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date alarmEnterTime;

    /**
     * 异常行为
     */
    @Excel(name = "异常行为")
    private String alarmBehavior;

    /**
     * 处理结果
     */
    @Excel(name = "处理结果")
    private String alarmResult;

    /**
     * 处理人员
     */
    @Excel(name = "处理人员")
    private String alarmHandler;

    /**
     * 处理时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "处理时间")
    private Date alarmHandleTime;

    /**
     * 删除标志
     */
    @Excel(name = "删除标志")
    private String delFlag;

    /**
     * 工单ID
     */
    @Excel(name = "工单ID")
    private Long workId;

    /**
     * 已读标志
     */
    @Excel(name = "已读标志")
    private String readFlag;

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setCardRecordId(Long cardRecordId) {
        this.cardRecordId = cardRecordId;
    }

    public Long getCardRecordId() {
        return cardRecordId;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonJobNo(String personJobNo) {
        this.personJobNo = personJobNo;
    }

    public String getPersonJobNo() {
        return personJobNo;
    }

    public void setPersonDept(String personDept) {
        this.personDept = personDept;
    }

    public String getPersonDept() {
        return personDept;
    }

    public void setVehiclePlateNo(String vehiclePlateNo) {
        this.vehiclePlateNo = vehiclePlateNo;
    }

    public String getVehiclePlateNo() {
        return vehiclePlateNo;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setAlarmLocation(String alarmLocation) {
        this.alarmLocation = alarmLocation;
    }

    public String getAlarmLocation() {
        return alarmLocation;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAlarmEnterTime(Date alarmEnterTime) {
        this.alarmEnterTime = alarmEnterTime;
    }

    public Date getAlarmEnterTime() {
        return alarmEnterTime;
    }

    public void setAlarmBehavior(String alarmBehavior) {
        this.alarmBehavior = alarmBehavior;
    }

    public String getAlarmBehavior() {
        return alarmBehavior;
    }

    public void setAlarmResult(String alarmResult) {
        this.alarmResult = alarmResult;
    }

    public String getAlarmResult() {
        return alarmResult;
    }

    public void setAlarmHandler(String alarmHandler) {
        this.alarmHandler = alarmHandler;
    }

    public String getAlarmHandler() {
        return alarmHandler;
    }

    public void setAlarmHandleTime(Date alarmHandleTime) {
        this.alarmHandleTime = alarmHandleTime;
    }

    public Date getAlarmHandleTime() {
        return alarmHandleTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("alarmId", getAlarmId())
                .append("alarmType", getAlarmType())
                .append("cardRecordId", getCardRecordId())
                .append("personName", getPersonName())
                .append("personJobNo", getPersonJobNo())
                .append("personDept", getPersonDept())
                .append("vehiclePlateNo", getVehiclePlateNo())
                .append("vehicleType", getVehicleType())
                .append("alarmLocation", getAlarmLocation())
                .append("areaCode", getAreaCode())
                .append("alarmEnterTime", getAlarmEnterTime())
                .append("alarmBehavior", getAlarmBehavior())
                .append("alarmResult", getAlarmResult())
                .append("alarmHandler", getAlarmHandler())
                .append("alarmHandleTime", getAlarmHandleTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getWorkId() {
        return workId;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }
}
