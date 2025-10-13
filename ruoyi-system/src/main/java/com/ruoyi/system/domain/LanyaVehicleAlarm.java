package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆报警记录对象 vehicle_alarm
 * 
 * @author 吕涛
 * @date 2025-10-10
 */
public class LanyaVehicleAlarm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 报警时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报警时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date alarmTime;

    /** 车辆id */
    @Excel(name = "车辆id")
    private Long vehicleId;

    /** 车辆类别(见字典) */
    @Excel(name = "车辆类别(见字典)")
    private String vehicleCategory;

    /** 车载卡号 */
    @Excel(name = "车载卡号")
    private Long cardId;

    /** 车辆名称 */
    @Excel(name = "车辆名称")
    private String vehicleName;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 司机名称 */
    @Excel(name = "司机名称")
    private String driverName;

    /** 司机手机号 */
    @Excel(name = "司机手机号")
    private String driverTel;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String companyName;

    /** 报警类型 */
    @Excel(name = "报警类型")
    private String alarmType;

    /** 报警类型名称 */
    @Excel(name = "报警类型名称")
    private String alarmTypeName;

    /** 报警名称 */
    @Excel(name = "报警名称")
    private String alarmName;

    /** 报警描述 */
    @Excel(name = "报警描述")
    private String alarmDesc;

    /** 报警状态（未处理15;已处理20；25无效） */
    @Excel(name = "报警状态", readConverterExp = "未=处理15;已处理20；25无效")
    private String alarmStatus;

    /** 报警设置id */
    @Excel(name = "报警设置id")
    private Long settingId;

    /** 报警设置名称 */
    @Excel(name = "报警设置名称")
    private String settingName;

    /** 围栏id */
    @Excel(name = "围栏id")
    private Long railId;

    /** 围栏名称 */
    @Excel(name = "围栏名称")
    private String railName;

    /** 绘制类型(0圆;1线;2多边形) */
    @Excel(name = "绘制类型(0圆;1线;2多边形)")
    private Integer drawType;

    /** 围栏坐标 */
    @Excel(name = "围栏坐标")
    private String railScope;

    /** 围栏高度 */
    @Excel(name = "围栏高度")
    private Integer railHeight;

    /** 楼层 */
    @Excel(name = "楼层")
    private String layerId;

    /** 楼高 */
    @Excel(name = "楼高")
    private Integer layerHeight;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 速度 */
    @Excel(name = "速度")
    private Integer speed;

    /** 方向 */
    @Excel(name = "方向")
    private Integer direction;

    /** 电量 */
    @Excel(name = "电量")
    private Integer electricity;

    /** 信标编码 */
    @Excel(name = "信标编码")
    private Long beaconId;

    /** 信号接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "信号接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acceptTime;

    /** 处理人 */
    @Excel(name = "处理人")
    private String disposeBy;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date disposeTime;

    /** 处理反馈 */
    @Excel(name = "处理反馈")
    private String disposeFeedback;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAlarmTime(Date alarmTime) 
    {
        this.alarmTime = alarmTime;
    }

    public Date getAlarmTime() 
    {
        return alarmTime;
    }

    public void setVehicleId(Long vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() 
    {
        return vehicleId;
    }

    public void setVehicleCategory(String vehicleCategory) 
    {
        this.vehicleCategory = vehicleCategory;
    }

    public String getVehicleCategory() 
    {
        return vehicleCategory;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setVehicleName(String vehicleName) 
    {
        this.vehicleName = vehicleName;
    }

    public String getVehicleName() 
    {
        return vehicleName;
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

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setAlarmType(String alarmType) 
    {
        this.alarmType = alarmType;
    }

    public String getAlarmType() 
    {
        return alarmType;
    }

    public void setAlarmTypeName(String alarmTypeName) 
    {
        this.alarmTypeName = alarmTypeName;
    }

    public String getAlarmTypeName() 
    {
        return alarmTypeName;
    }

    public void setAlarmName(String alarmName) 
    {
        this.alarmName = alarmName;
    }

    public String getAlarmName() 
    {
        return alarmName;
    }

    public void setAlarmDesc(String alarmDesc) 
    {
        this.alarmDesc = alarmDesc;
    }

    public String getAlarmDesc() 
    {
        return alarmDesc;
    }

    public void setAlarmStatus(String alarmStatus) 
    {
        this.alarmStatus = alarmStatus;
    }

    public String getAlarmStatus() 
    {
        return alarmStatus;
    }

    public void setSettingId(Long settingId) 
    {
        this.settingId = settingId;
    }

    public Long getSettingId() 
    {
        return settingId;
    }

    public void setSettingName(String settingName) 
    {
        this.settingName = settingName;
    }

    public String getSettingName() 
    {
        return settingName;
    }

    public void setRailId(Long railId) 
    {
        this.railId = railId;
    }

    public Long getRailId() 
    {
        return railId;
    }

    public void setRailName(String railName) 
    {
        this.railName = railName;
    }

    public String getRailName() 
    {
        return railName;
    }

    public void setDrawType(Integer drawType) 
    {
        this.drawType = drawType;
    }

    public Integer getDrawType() 
    {
        return drawType;
    }

    public void setRailScope(String railScope) 
    {
        this.railScope = railScope;
    }

    public String getRailScope() 
    {
        return railScope;
    }

    public void setRailHeight(Integer railHeight) 
    {
        this.railHeight = railHeight;
    }

    public Integer getRailHeight() 
    {
        return railHeight;
    }

    public void setLayerId(String layerId) 
    {
        this.layerId = layerId;
    }

    public String getLayerId() 
    {
        return layerId;
    }

    public void setLayerHeight(Integer layerHeight) 
    {
        this.layerHeight = layerHeight;
    }

    public Integer getLayerHeight() 
    {
        return layerHeight;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    public void setSpeed(Integer speed) 
    {
        this.speed = speed;
    }

    public Integer getSpeed() 
    {
        return speed;
    }

    public void setDirection(Integer direction) 
    {
        this.direction = direction;
    }

    public Integer getDirection() 
    {
        return direction;
    }

    public void setElectricity(Integer electricity) 
    {
        this.electricity = electricity;
    }

    public Integer getElectricity() 
    {
        return electricity;
    }

    public void setBeaconId(Long beaconId) 
    {
        this.beaconId = beaconId;
    }

    public Long getBeaconId() 
    {
        return beaconId;
    }

    public void setAcceptTime(Date acceptTime) 
    {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() 
    {
        return acceptTime;
    }

    public void setDisposeBy(String disposeBy) 
    {
        this.disposeBy = disposeBy;
    }

    public String getDisposeBy() 
    {
        return disposeBy;
    }

    public void setDisposeTime(Date disposeTime) 
    {
        this.disposeTime = disposeTime;
    }

    public Date getDisposeTime() 
    {
        return disposeTime;
    }

    public void setDisposeFeedback(String disposeFeedback) 
    {
        this.disposeFeedback = disposeFeedback;
    }

    public String getDisposeFeedback() 
    {
        return disposeFeedback;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("alarmTime", getAlarmTime())
            .append("vehicleId", getVehicleId())
            .append("vehicleCategory", getVehicleCategory())
            .append("cardId", getCardId())
            .append("vehicleName", getVehicleName())
            .append("licensePlateNumber", getLicensePlateNumber())
            .append("driverName", getDriverName())
            .append("driverTel", getDriverTel())
            .append("companyName", getCompanyName())
            .append("alarmType", getAlarmType())
            .append("alarmTypeName", getAlarmTypeName())
            .append("alarmName", getAlarmName())
            .append("alarmDesc", getAlarmDesc())
            .append("alarmStatus", getAlarmStatus())
            .append("settingId", getSettingId())
            .append("settingName", getSettingName())
            .append("railId", getRailId())
            .append("railName", getRailName())
            .append("drawType", getDrawType())
            .append("railScope", getRailScope())
            .append("railHeight", getRailHeight())
            .append("layerId", getLayerId())
            .append("layerHeight", getLayerHeight())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("speed", getSpeed())
            .append("direction", getDirection())
            .append("electricity", getElectricity())
            .append("beaconId", getBeaconId())
            .append("acceptTime", getAcceptTime())
            .append("disposeBy", getDisposeBy())
            .append("disposeTime", getDisposeTime())
            .append("disposeFeedback", getDisposeFeedback())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
