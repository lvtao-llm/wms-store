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
 * 访客车辆对象 vehicle_info_visitor
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
@ApiModel(description = "访客车辆对象")
public class LanyaVehicleInfoVisitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 关联车辆id */
    @Excel(name = "关联车辆id")
    @ApiModelProperty(value = "关联车辆id")
    private Long vehicleId;

    /** 车辆名称 */
    @Excel(name = "车辆名称")
    @ApiModelProperty(value = "车辆名称")
    private String vehicleName;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    @ApiModelProperty(value = "车辆类型")
    private String vehicleType;

    /** 车载卡号 */
    @Excel(name = "车载卡号")
    @ApiModelProperty(value = "车载卡号")
    private Long cardId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String licensePlateNumber;

    /** 司机姓名 */
    @Excel(name = "司机姓名")
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /** 司机手机号 */
    @Excel(name = "司机手机号")
    @ApiModelProperty(value = "司机手机号")
    private String driverTel;

    /** 照片 */
    @Excel(name = "照片")
    @ApiModelProperty(value = "照片")
    private String driverPhoto;

    /** 司机身份证号码 */
    @Excel(name = "司机身份证号码")
    @ApiModelProperty(value = "司机身份证号码")
    private String idNumber;

    /** 人员IC号 */
    @Excel(name = "人员IC号")
    @ApiModelProperty(value = "人员IC号")
    private String personIc;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String companyName;

    /** 预约进入开始时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约进入开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约进入开始时间")
    private Date reservationEntryStartTime;

    /** 预约进入结束时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约进入结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约进入结束时间")
    private Date reservationEntryEndTime;

    /** 来访时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "来访时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "来访时间")
    private Date visitingTime;

    /** 离开时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "离开时间")
    private Date leaveTime;

    /** 来访状态（0为已离开，1为未离开） */
    @Excel(name = "来访状态", readConverterExp = "0=为已离开，1为未离开")
    @ApiModelProperty(value = "来访状态（0为已离开，1为未离开）")
    private Integer visitingStatus;

    /** 来访事由 */
    @Excel(name = "来访事由")
    @ApiModelProperty(value = "来访事由")
    private String visitingReason;

    /** 接待人 */
    @Excel(name = "接待人")
    @ApiModelProperty(value = "接待人")
    private String receiver;

    /** 接待人联系方式 */
    @Excel(name = "接待人联系方式")
    @ApiModelProperty(value = "接待人联系方式")
    private String receiverTel;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVehicleId(Long vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() 
    {
        return vehicleId;
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

    public void setDriverTel(String driverTel) 
    {
        this.driverTel = driverTel;
    }

    public String getDriverTel() 
    {
        return driverTel;
    }

    public void setDriverPhoto(String driverPhoto) 
    {
        this.driverPhoto = driverPhoto;
    }

    public String getDriverPhoto() 
    {
        return driverPhoto;
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

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setReservationEntryStartTime(Date reservationEntryStartTime) 
    {
        this.reservationEntryStartTime = reservationEntryStartTime;
    }

    public Date getReservationEntryStartTime() 
    {
        return reservationEntryStartTime;
    }

    public void setReservationEntryEndTime(Date reservationEntryEndTime) 
    {
        this.reservationEntryEndTime = reservationEntryEndTime;
    }

    public Date getReservationEntryEndTime() 
    {
        return reservationEntryEndTime;
    }

    public void setVisitingTime(Date visitingTime) 
    {
        this.visitingTime = visitingTime;
    }

    public Date getVisitingTime() 
    {
        return visitingTime;
    }

    public void setLeaveTime(Date leaveTime) 
    {
        this.leaveTime = leaveTime;
    }

    public Date getLeaveTime() 
    {
        return leaveTime;
    }

    public void setVisitingStatus(Integer visitingStatus) 
    {
        this.visitingStatus = visitingStatus;
    }

    public Integer getVisitingStatus() 
    {
        return visitingStatus;
    }

    public void setVisitingReason(String visitingReason) 
    {
        this.visitingReason = visitingReason;
    }

    public String getVisitingReason() 
    {
        return visitingReason;
    }

    public void setReceiver(String receiver) 
    {
        this.receiver = receiver;
    }

    public String getReceiver() 
    {
        return receiver;
    }

    public void setReceiverTel(String receiverTel) 
    {
        this.receiverTel = receiverTel;
    }

    public String getReceiverTel() 
    {
        return receiverTel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehicleId", getVehicleId())
            .append("vehicleName", getVehicleName())
            .append("vehicleType", getVehicleType())
            .append("cardId", getCardId())
            .append("licensePlateNumber", getLicensePlateNumber())
            .append("driverName", getDriverName())
            .append("driverTel", getDriverTel())
            .append("driverPhoto", getDriverPhoto())
            .append("idNumber", getIdNumber())
            .append("personIc", getPersonIc())
            .append("companyName", getCompanyName())
            .append("reservationEntryStartTime", getReservationEntryStartTime())
            .append("reservationEntryEndTime", getReservationEntryEndTime())
            .append("visitingTime", getVisitingTime())
            .append("leaveTime", getLeaveTime())
            .append("visitingStatus", getVisitingStatus())
            .append("visitingReason", getVisitingReason())
            .append("receiver", getReceiver())
            .append("receiverTel", getReceiverTel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
