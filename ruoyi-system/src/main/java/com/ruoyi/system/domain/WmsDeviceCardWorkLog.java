package com.ruoyi.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 发卡记录对象 wms_device_card_work_log
 *
 * @author ruoyi
 * @date 2025-10-19
 */
public class WmsDeviceCardWorkLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 定位卡号
     */
    @Excel(name = "定位卡号")
    private Long cardId;

    /**
     * 人脸识别照片
     */
    @Excel(name = "人脸识别照片")
    private String personPhoto;

    /**
     * 人员id
     */
    @Excel(name = "人员id")
    private Long personId;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String realName;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String deptName;

    /**
     * 证件号码
     */
    @Excel(name = "证件号码")
    private String idNumber;

    /**
     * 认证方式(人脸/扫码/身份证/刷卡)
     */
    @Excel(name = "认证方式(人脸/扫码/身份证/刷卡)")
    private String identifyType;

    /**
     * 发卡设备SN
     */
    @Excel(name = "发卡设备SN")
    private String senderDeviceSn;

    /**
     * 发卡设备名称
     */
    @Excel(name = "发卡设备名称")
    private String senderDeviceName;

    /**
     * 发卡充电口号
     */
    @Excel(name = "发卡充电口号")
    private Integer senderDeviceNum;

    /**
     * 发卡命令下发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发卡命令下发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date senderCommandTime;

    /**
     * 发卡方式(固定/随机)
     */
    @Excel(name = "发卡方式(固定/随机)")
    private String senderRentType;

    /**
     * 认证时间(人脸机)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "认证时间(人脸机)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date senderIdentifyTime;

    /**
     * 发卡日志ID
     */
    @Excel(name = "发卡日志ID")
    private Long senderLanyaLogId;

    /**
     * 还卡设备SN
     */
    @Excel(name = "还卡设备SN")
    private String returnDeviceSn;

    /**
     * 还卡设备名称
     */
    @Excel(name = "还卡设备名称")
    private String returnDeviceName;

    /**
     * 还卡充电口号
     */
    @Excel(name = "还卡充电口号")
    private Integer returnDeviceNum;

    /**
     * 还卡命令下发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "还卡命令下发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnCommandTime;

    /**
     * 还卡日志ID
     */
    @Excel(name = "还卡日志ID")
    private Long returnLanyaLogId;

    /**
     * 日志开始ID
     */
    @Excel(name = "日志开始ID")
    private Long positionHistoryBeginId;

    /**
     * 日志结束ID
     */
    @Excel(name = "日志结束ID")
    private Long positionHistoryEndId;

    @Transient
    @JsonIgnore
    private List<LanyaPositionHistory> trajectory = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setSenderDeviceSn(String senderDeviceSn) {
        this.senderDeviceSn = senderDeviceSn;
    }

    public String getSenderDeviceSn() {
        return senderDeviceSn;
    }

    public void setSenderDeviceName(String senderDeviceName) {
        this.senderDeviceName = senderDeviceName;
    }

    public String getSenderDeviceName() {
        return senderDeviceName;
    }

    public void setSenderDeviceNum(Integer senderDeviceNum) {
        this.senderDeviceNum = senderDeviceNum;
    }

    public Integer getSenderDeviceNum() {
        return senderDeviceNum;
    }

    public void setSenderCommandTime(Date senderCommandTime) {
        this.senderCommandTime = senderCommandTime;
    }

    public Date getSenderCommandTime() {
        return senderCommandTime;
    }

    public void setSenderRentType(String senderRentType) {
        this.senderRentType = senderRentType;
    }

    public String getSenderRentType() {
        return senderRentType;
    }

    public void setSenderIdentifyTime(Date senderIdentifyTime) {
        this.senderIdentifyTime = senderIdentifyTime;
    }

    public Date getSenderIdentifyTime() {
        return senderIdentifyTime;
    }

    public void setSenderLanyaLogId(Long senderLanyaLogId) {
        this.senderLanyaLogId = senderLanyaLogId;
    }

    public Long getSenderLanyaLogId() {
        return senderLanyaLogId;
    }

    public void setReturnDeviceSn(String returnDeviceSn) {
        this.returnDeviceSn = returnDeviceSn;
    }

    public String getReturnDeviceSn() {
        return returnDeviceSn;
    }

    public void setReturnDeviceName(String returnDeviceName) {
        this.returnDeviceName = returnDeviceName;
    }

    public String getReturnDeviceName() {
        return returnDeviceName;
    }

    public void setReturnDeviceNum(Integer returnDeviceNum) {
        this.returnDeviceNum = returnDeviceNum;
    }

    public Integer getReturnDeviceNum() {
        return returnDeviceNum;
    }

    public void setReturnCommandTime(Date returnCommandTime) {
        this.returnCommandTime = returnCommandTime;
    }

    public Date getReturnCommandTime() {
        return returnCommandTime;
    }

    public void setReturnLanyaLogId(Long returnLanyaLogId) {
        this.returnLanyaLogId = returnLanyaLogId;
    }

    public Long getReturnLanyaLogId() {
        return returnLanyaLogId;
    }

    public void setPositionHistoryBeginId(Long positionHistoryBeginId) {
        this.positionHistoryBeginId = positionHistoryBeginId;
    }

    public Long getPositionHistoryBeginId() {
        return positionHistoryBeginId;
    }

    public void setPositionHistoryEndId(Long positionHistoryEndId) {
        this.positionHistoryEndId = positionHistoryEndId;
    }

    public Long getPositionHistoryEndId() {
        return positionHistoryEndId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("cardId", getCardId())
                .append("personPhoto", getPersonPhoto())
                .append("personId", getPersonId())
                .append("deptId", getDeptId())
                .append("realName", getRealName())
                .append("deptName", getDeptName())
                .append("idNumber", getIdNumber())
                .append("identifyType", getIdentifyType())
                .append("senderDeviceSn", getSenderDeviceSn())
                .append("senderDeviceName", getSenderDeviceName())
                .append("senderDeviceNum", getSenderDeviceNum())
                .append("senderCommandTime", getSenderCommandTime())
                .append("senderRentType", getSenderRentType())
                .append("senderIdentifyTime", getSenderIdentifyTime())
                .append("senderLanyaLogId", getSenderLanyaLogId())
                .append("returnDeviceSn", getReturnDeviceSn())
                .append("returnDeviceName", getReturnDeviceName())
                .append("returnDeviceNum", getReturnDeviceNum())
                .append("returnCommandTime", getReturnCommandTime())
                .append("returnLanyaLogId", getReturnLanyaLogId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("positionHistoryBeginId", getPositionHistoryBeginId())
                .append("positionHistoryEndId", getPositionHistoryEndId())
                .toString();
    }

    public List<LanyaPositionHistory> getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(List<LanyaPositionHistory> trajectory) {
        this.trajectory = trajectory;
    }
}
