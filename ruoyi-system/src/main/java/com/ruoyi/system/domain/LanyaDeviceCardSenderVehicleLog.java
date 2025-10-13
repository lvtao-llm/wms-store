package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆发卡记录对象 device_card_sender_vehicle_log
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public class LanyaDeviceCardSenderVehicleLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 车辆类别 */
    @Excel(name = "车辆类别")
    private String vehicleCategory;

    /** 车辆名称 */
    @Excel(name = "车辆名称")
    private String vehicleName;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String vehicleType;

    /** 车载卡号 */
    @Excel(name = "车载卡号")
    private Long cardId;

    /** 车辆id */
    @Excel(name = "车辆id")
    private Long vehicleId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 司机姓名 */
    @Excel(name = "司机姓名")
    private String driverName;

    /** 司机身份证号码 */
    @Excel(name = "司机身份证号码")
    private String idNumber;

    /** 人员IC号 */
    @Excel(name = "人员IC号")
    private String personIc;

    /** 司机手机号 */
    @Excel(name = "司机手机号")
    private String driverTel;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String companyName;

    /** 发卡机SN */
    @Excel(name = "发卡机SN")
    private String deviceSn;

    /** 柜组 */
    @Excel(name = "柜组")
    private Integer deviceAims;

    /** 柜号 */
    @Excel(name = "柜号")
    private Integer deviceNum;

    /** 类型 */
    @Excel(name = "类型")
    private Integer cardSenderType;

    /** 结果 */
    @Excel(name = "结果")
    private String result;

    /** 认证方式 */
    @Excel(name = "认证方式")
    private String identifyType;

    /** 认证照片 */
    @Excel(name = "认证照片")
    private String identifyPhoto;

    /** 发卡方式 */
    @Excel(name = "发卡方式")
    private String rentType;

    /** 认证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "认证时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date identifyTime;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 下发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date commandTime;

    /** 闭环时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "闭环时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closedTime;

    /** 三方平台通知结果 */
    @Excel(name = "三方平台通知结果")
    private String notifyStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVehicleCategory(String vehicleCategory) 
    {
        this.vehicleCategory = vehicleCategory;
    }

    public String getVehicleCategory() 
    {
        return vehicleCategory;
    }

    public void setVehicleName(String vehicleName) 
    {
        this.vehicleName = vehicleName;
    }

    public String getVehicleName() 
    {
        return vehicleName;
    }

    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() 
    {
        return vehicleType;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setVehicleId(Long vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() 
    {
        return vehicleId;
    }

    public void setLicensePlateNumber(String licensePlateNumber) 
    {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateNumber() 
    {
        return licensePlateNumber;
    }

    public void setDriverName(String driverName) 
    {
        this.driverName = driverName;
    }

    public String getDriverName() 
    {
        return driverName;
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

    public void setDriverTel(String driverTel) 
    {
        this.driverTel = driverTel;
    }

    public String getDriverTel() 
    {
        return driverTel;
    }

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setDeviceSn(String deviceSn) 
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() 
    {
        return deviceSn;
    }

    public void setDeviceAims(Integer deviceAims) 
    {
        this.deviceAims = deviceAims;
    }

    public Integer getDeviceAims() 
    {
        return deviceAims;
    }

    public void setDeviceNum(Integer deviceNum) 
    {
        this.deviceNum = deviceNum;
    }

    public Integer getDeviceNum() 
    {
        return deviceNum;
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

    public void setIdentifyType(String identifyType) 
    {
        this.identifyType = identifyType;
    }

    public String getIdentifyType() 
    {
        return identifyType;
    }

    public void setIdentifyPhoto(String identifyPhoto) 
    {
        this.identifyPhoto = identifyPhoto;
    }

    public String getIdentifyPhoto() 
    {
        return identifyPhoto;
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

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setCommandTime(Date commandTime) 
    {
        this.commandTime = commandTime;
    }

    public Date getCommandTime() 
    {
        return commandTime;
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
            .append("vehicleCategory", getVehicleCategory())
            .append("vehicleName", getVehicleName())
            .append("vehicleType", getVehicleType())
            .append("cardId", getCardId())
            .append("vehicleId", getVehicleId())
            .append("licensePlateNumber", getLicensePlateNumber())
            .append("driverName", getDriverName())
            .append("idNumber", getIdNumber())
            .append("personIc", getPersonIc())
            .append("driverTel", getDriverTel())
            .append("companyName", getCompanyName())
            .append("deviceSn", getDeviceSn())
            .append("deviceAims", getDeviceAims())
            .append("deviceNum", getDeviceNum())
            .append("cardSenderType", getCardSenderType())
            .append("result", getResult())
            .append("identifyType", getIdentifyType())
            .append("identifyPhoto", getIdentifyPhoto())
            .append("rentType", getRentType())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("identifyTime", getIdentifyTime())
            .append("deviceName", getDeviceName())
            .append("commandTime", getCommandTime())
            .append("closedTime", getClosedTime())
            .append("updateTime", getUpdateTime())
            .append("notifyStatus", getNotifyStatus())
            .toString();
    }
}
