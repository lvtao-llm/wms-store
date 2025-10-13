package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人脸发卡记录对象 device_card_sender_log
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public class LanyaDeviceCardSenderLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备SN */
    @Excel(name = "设备SN")
    private String deviceSn;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 充电口号 */
    @Excel(name = "充电口号")
    private Integer deviceNum;

    /** 定位卡号 */
    @Excel(name = "定位卡号")
    private Long cardId;

    /** 人脸识别照片 */
    @Excel(name = "人脸识别照片")
    private String personPhoto;

    /** 下发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date commandTime;

    /** 0:还卡 1：发卡 */
    @Excel(name = "0:还卡 1：发卡")
    private Integer cardSenderType;

    /** 结果 */
    @Excel(name = "结果")
    private String result;

    /** 人员id */
    @Excel(name = "人员id")
    private Long personId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String realName;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 工号 */
    @Excel(name = "工号")
    private String jobNumber;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String idNumber;

    /** 人员IC号 */
    @Excel(name = "人员IC号")
    private String personIc;

    /** 设备主板 */
    @Excel(name = "设备主板")
    private Integer deviceAims;

    /** 认证方式(人脸/扫码/身份证/刷卡) */
    @Excel(name = "认证方式(人脸/扫码/身份证/刷卡)")
    private String identifyType;

    /** 发卡方式(固定/随机) */
    @Excel(name = "发卡方式(固定/随机)")
    private String rentType;

    /** 认证时间(人脸机) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "认证时间(人脸机)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date identifyTime;

    /** 闭环时间(发卡机) */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "闭环时间(发卡机)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closedTime;

    /** 三方平台通知结果：0:未通知 1：通知成功 2：通知失败 */
    @Excel(name = "三方平台通知结果：0:未通知 1：通知成功 2：通知失败")
    private String notifyStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDeviceSn(String deviceSn) 
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() 
    {
        return deviceSn;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceNum(Integer deviceNum) 
    {
        this.deviceNum = deviceNum;
    }

    public Integer getDeviceNum() 
    {
        return deviceNum;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setPersonPhoto(String personPhoto) 
    {
        this.personPhoto = personPhoto;
    }

    public String getPersonPhoto() 
    {
        return personPhoto;
    }

    public void setCommandTime(Date commandTime) 
    {
        this.commandTime = commandTime;
    }

    public Date getCommandTime() 
    {
        return commandTime;
    }

    public void setCardSenderType(Integer cardSenderType) 
    {
        this.cardSenderType = cardSenderType;
    }

    public Integer getCardSenderType() 
    {
        return cardSenderType;
    }

    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }

    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setJobNumber(String jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() 
    {
        return jobNumber;
    }

    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }

    public void setPersonIc(String personIc) 
    {
        this.personIc = personIc;
    }

    public String getPersonIc() 
    {
        return personIc;
    }

    public void setDeviceAims(Integer deviceAims) 
    {
        this.deviceAims = deviceAims;
    }

    public Integer getDeviceAims() 
    {
        return deviceAims;
    }

    public void setIdentifyType(String identifyType) 
    {
        this.identifyType = identifyType;
    }

    public String getIdentifyType() 
    {
        return identifyType;
    }

    public void setRentType(String rentType) 
    {
        this.rentType = rentType;
    }

    public String getRentType() 
    {
        return rentType;
    }

    public void setIdentifyTime(Date identifyTime) 
    {
        this.identifyTime = identifyTime;
    }

    public Date getIdentifyTime() 
    {
        return identifyTime;
    }

    public void setClosedTime(Date closedTime) 
    {
        this.closedTime = closedTime;
    }

    public Date getClosedTime() 
    {
        return closedTime;
    }

    public void setNotifyStatus(String notifyStatus) 
    {
        this.notifyStatus = notifyStatus;
    }

    public String getNotifyStatus() 
    {
        return notifyStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceSn", getDeviceSn())
            .append("deviceName", getDeviceName())
            .append("deviceNum", getDeviceNum())
            .append("cardId", getCardId())
            .append("personPhoto", getPersonPhoto())
            .append("commandTime", getCommandTime())
            .append("cardSenderType", getCardSenderType())
            .append("result", getResult())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("personId", getPersonId())
            .append("realName", getRealName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("jobNumber", getJobNumber())
            .append("idNumber", getIdNumber())
            .append("personIc", getPersonIc())
            .append("deviceAims", getDeviceAims())
            .append("identifyType", getIdentifyType())
            .append("rentType", getRentType())
            .append("identifyTime", getIdentifyTime())
            .append("closedTime", getClosedTime())
            .append("updateTime", getUpdateTime())
            .append("notifyStatus", getNotifyStatus())
            .toString();
    }
}
