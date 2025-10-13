package com.ruoyi.quartz.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 历史轨迹对象 position_history
 * 
 * @author ruoyi
 * @date 2025-09-29
 */
public class MlPositionHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 接收时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acceptTime;

    /** 卡号 */
    @Excel(name = "卡号")
    private Long cardId;

    /** 卡类型 */
    @Excel(name = "卡类型")
    private String cardType;

    /** 信标编码 */
    @Excel(name = "信标编码")
    private Long beaconId;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 距离 */
    @Excel(name = "距离")
    private BigDecimal distance;

    /** 楼层 */
    @Excel(name = "楼层")
    private String layerId;

    /** 楼高 */
    @Excel(name = "楼高")
    private Long layerHeight;

    /** 人员id */
    @Excel(name = "人员id")
    private Long personId;

    /** 人员类型 */
    @Excel(name = "人员类型")
    private String personType;

    /** 员工类型 */
    @Excel(name = "员工类型")
    private String staffType;

    /** 姓名 */
    @Excel(name = "姓名")
    private String realName;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 岗位id */
    @Excel(name = "岗位id")
    private Long postId;

    /** 承包商id */
    @Excel(name = "承包商id")
    private Long contractorId;

    /** 海拔高度，单位米 */
    @Excel(name = "海拔高度，单位米")
    private BigDecimal altitude;

    /** 高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解 */
    @Excel(name = "高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解")
    private String rtkPositionMode;

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

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    public void setContractorId(Long contractorId) 
    {
        this.contractorId = contractorId;
    }

    public Long getContractorId() 
    {
        return contractorId;
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
            .append("createTime", getCreateTime())
            .append("personId", getPersonId())
            .append("personType", getPersonType())
            .append("staffType", getStaffType())
            .append("realName", getRealName())
            .append("deptId", getDeptId())
            .append("postId", getPostId())
            .append("contractorId", getContractorId())
            .append("altitude", getAltitude())
            .append("rtkPositionMode", getRtkPositionMode())
            .toString();
    }
}
