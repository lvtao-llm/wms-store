package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.controller.WmsDeviceCameraLogController;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 历史轨迹对象 position_history
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@ApiModel(description = "历史轨迹对象")
public class WmsVehiclePositionCurrent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 接收时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "接收时间")
    @ApiModelProperty("接收时间")
    private Date acceptTime;

    /**
     * 卡号
     */
    @Excel(name = "卡号")
    @ApiModelProperty("卡号")
    private Long cardId;

    /**
     * 卡类型
     */
    @Excel(name = "卡类型")
    @ApiModelProperty("卡类型")
    private String cardType;

    /**
     * 信标编码
     */
    @Excel(name = "信标编码")
    @ApiModelProperty("信标编码")
    private Long beaconId;

    /**
     * 经度
     */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private Double longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private Double latitude;

    /**
     * 距离
     */
    @Excel(name = "距离")
    @ApiModelProperty("距离")
    private Double distance;

    /**
     * 楼层
     */
    @Excel(name = "楼层")
    @ApiModelProperty("楼层")
    private String layerId;

    /**
     * 楼高
     */
    @Excel(name = "楼高")
    @ApiModelProperty("楼高")
    private Integer layerHeight;

    /**
     * 人员id
     */
    @Excel(name = "人员id")
    @ApiModelProperty("人员id")
    private Long personId;

    /**
     * 人员类型
     */
    @Excel(name = "人员类型")
    @ApiModelProperty("人员类型")
    private String personType;

    /**
     * 员工类型
     */
    @Excel(name = "员工类型")
    @ApiModelProperty("员工类型")
    private String staffType;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String realName;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 岗位id
     */
    @Excel(name = "岗位id")
    @ApiModelProperty("岗位id")
    private Long postId;

    /**
     * 承包商id
     */
    @Excel(name = "承包商id")
    @ApiModelProperty("承包商id")
    private Long contractorId;

    /**
     * 海拔高度，单位米
     */
    @Excel(name = "海拔高度，单位米")
    @ApiModelProperty("海拔高度，单位米")
    private BigDecimal altitude;

    /**
     * 高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解
     */
    @Excel(name = "高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解")
    @ApiModelProperty("高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解")
    private String rtkPositionMode;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setBeaconId(Long beaconId) {
        this.beaconId = beaconId;
    }

    public Long getBeaconId() {
        return beaconId;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerHeight(Integer layerHeight) {
        this.layerHeight = layerHeight;
    }

    public Integer getLayerHeight() {
        return layerHeight;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonType() {
        return personType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setAltitude(BigDecimal altitude) {
        this.altitude = altitude;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setRtkPositionMode(String rtkPositionMode) {
        this.rtkPositionMode = rtkPositionMode;
    }

    public String getRtkPositionMode() {
        return rtkPositionMode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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
