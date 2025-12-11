package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 实时定位对象 position_current
 * 
 * @author ruoyi
 * @date 2025-12-05
 */
@ApiModel(description = "实时定位对象")
public class PositionCurrent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty("主键")
    private Long id;

    /** 接收时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("接收时间")
    private Date acceptTime;

    /** 卡号 */
    @Excel(name = "卡号")
    @ApiModelProperty("卡号")
    private Long cardId;

    /** 卡类型 */
    @Excel(name = "卡类型")
    @ApiModelProperty("卡类型")
    private String cardType;

    /** 信标编码 */
    @Excel(name = "信标编码")
    @ApiModelProperty("信标编码")
    private Long beaconId;

    /** 经度 */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /** 距离 */
    @Excel(name = "距离")
    @ApiModelProperty("距离")
    private BigDecimal distance;

    /** 层ID */
    @Excel(name = "层ID")
    @ApiModelProperty("层ID")
    private String layerId;

    /** 层高 */
    @Excel(name = "层高")
    @ApiModelProperty("层高")
    private Long layerHeight;

    /** 气压 */
    @Excel(name = "气压")
    @ApiModelProperty("气压")
    private Long pressure;

    /** 人员ID */
    @Excel(name = "人员ID")
    @ApiModelProperty("人员ID")
    private Long personId;

    /** 人员类型 */
    @Excel(name = "人员类型")
    @ApiModelProperty("人员类型")
    private String personType;

    /** 人员属性 */
    @Excel(name = "人员属性")
    @ApiModelProperty("人员属性")
    private String personAttribute;

    /** 员工类型 */
    @Excel(name = "员工类型")
    @ApiModelProperty("员工类型")
    private String staffType;

    /** 姓名 */
    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String realName;

    /** 照片 */
    @Excel(name = "照片")
    @ApiModelProperty("照片")
    private String personPhoto;

    /** 工号 */
    @Excel(name = "工号")
    @ApiModelProperty("工号")
    private String jobNumber;

    /** 部门ID */
    @Excel(name = "部门ID")
    @ApiModelProperty("部门ID")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;

    /** 岗位ID */
    @Excel(name = "岗位ID")
    @ApiModelProperty("岗位ID")
    private Long postId;

    /** 岗位名称 */
    @Excel(name = "岗位名称")
    @ApiModelProperty("岗位名称")
    private String postName;

    /** 承包商ID */
    @Excel(name = "承包商ID")
    @ApiModelProperty("承包商ID")
    private Long contractorId;

    /** 承包商名称 */
    @Excel(name = "承包商名称")
    @ApiModelProperty("承包商名称")
    private String contractorName;

    /** 卡静止状态(默认0移动，1静止) */
    @Excel(name = "卡静止状态(默认0移动，1静止)")
    @ApiModelProperty("卡静止状态(默认0移动，1静止)")
    private Long stillStatus;

    /** 定位模式 */
    @Excel(name = "定位模式")
    @ApiModelProperty("定位模式")
    private String positionType;

    /** 海拔高度 */
    @Excel(name = "海拔高度")
    @ApiModelProperty("海拔高度")
    private BigDecimal altitude;

    /** 高精度定位模式 */
    @Excel(name = "高精度定位模式")
    @ApiModelProperty("高精度定位模式")
    private String rtkPositionMode;

    /** 楼宇ID */
    @Excel(name = "楼宇ID")
    @ApiModelProperty("楼宇ID")
    private Long buildingId;

    /** 楼宇名称 */
    @Excel(name = "楼宇名称")
    @ApiModelProperty("楼宇名称")
    private String buildingName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAcceptTime(Date acceptTime) 
    {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() 
    {
        return acceptTime;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setCardType(String cardType) 
    {
        this.cardType = cardType;
    }

    public String getCardType() 
    {
        return cardType;
    }

    public void setBeaconId(Long beaconId) 
    {
        this.beaconId = beaconId;
    }

    public Long getBeaconId() 
    {
        return beaconId;
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

    public void setDistance(BigDecimal distance) 
    {
        this.distance = distance;
    }

    public BigDecimal getDistance() 
    {
        return distance;
    }

    public void setLayerId(String layerId) 
    {
        this.layerId = layerId;
    }

    public String getLayerId() 
    {
        return layerId;
    }

    public void setLayerHeight(Long layerHeight) 
    {
        this.layerHeight = layerHeight;
    }

    public Long getLayerHeight() 
    {
        return layerHeight;
    }

    public void setPressure(Long pressure) 
    {
        this.pressure = pressure;
    }

    public Long getPressure() 
    {
        return pressure;
    }

    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }

    public void setPersonType(String personType) 
    {
        this.personType = personType;
    }

    public String getPersonType() 
    {
        return personType;
    }

    public void setPersonAttribute(String personAttribute) 
    {
        this.personAttribute = personAttribute;
    }

    public String getPersonAttribute() 
    {
        return personAttribute;
    }

    public void setStaffType(String staffType) 
    {
        this.staffType = staffType;
    }

    public String getStaffType() 
    {
        return staffType;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setPersonPhoto(String personPhoto) 
    {
        this.personPhoto = personPhoto;
    }

    public String getPersonPhoto() 
    {
        return personPhoto;
    }

    public void setJobNumber(String jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() 
    {
        return jobNumber;
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

    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    public void setPostName(String postName) 
    {
        this.postName = postName;
    }

    public String getPostName() 
    {
        return postName;
    }

    public void setContractorId(Long contractorId) 
    {
        this.contractorId = contractorId;
    }

    public Long getContractorId() 
    {
        return contractorId;
    }

    public void setContractorName(String contractorName) 
    {
        this.contractorName = contractorName;
    }

    public String getContractorName() 
    {
        return contractorName;
    }

    public void setStillStatus(Long stillStatus) 
    {
        this.stillStatus = stillStatus;
    }

    public Long getStillStatus() 
    {
        return stillStatus;
    }

    public void setPositionType(String positionType) 
    {
        this.positionType = positionType;
    }

    public String getPositionType() 
    {
        return positionType;
    }

    public void setAltitude(BigDecimal altitude) 
    {
        this.altitude = altitude;
    }

    public BigDecimal getAltitude() 
    {
        return altitude;
    }

    public void setRtkPositionMode(String rtkPositionMode) 
    {
        this.rtkPositionMode = rtkPositionMode;
    }

    public String getRtkPositionMode() 
    {
        return rtkPositionMode;
    }

    public void setBuildingId(Long buildingId) 
    {
        this.buildingId = buildingId;
    }

    public Long getBuildingId() 
    {
        return buildingId;
    }

    public void setBuildingName(String buildingName) 
    {
        this.buildingName = buildingName;
    }

    public String getBuildingName() 
    {
        return buildingName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("acceptTime", getAcceptTime())
            .append("cardId", getCardId())
            .append("cardType", getCardType())
            .append("beaconId", getBeaconId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("distance", getDistance())
            .append("layerId", getLayerId())
            .append("layerHeight", getLayerHeight())
            .append("pressure", getPressure())
            .append("createTime", getCreateTime())
            .append("personId", getPersonId())
            .append("personType", getPersonType())
            .append("personAttribute", getPersonAttribute())
            .append("staffType", getStaffType())
            .append("realName", getRealName())
            .append("personPhoto", getPersonPhoto())
            .append("jobNumber", getJobNumber())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("postId", getPostId())
            .append("postName", getPostName())
            .append("contractorId", getContractorId())
            .append("contractorName", getContractorName())
            .append("stillStatus", getStillStatus())
            .append("positionType", getPositionType())
            .append("altitude", getAltitude())
            .append("rtkPositionMode", getRtkPositionMode())
            .append("buildingId", getBuildingId())
            .append("buildingName", getBuildingName())
            .toString();
    }
}
