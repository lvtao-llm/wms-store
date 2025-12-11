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
 * 报警记录对象 core_alarm
 *
 * @author ruoyi
 * @date 2025-10-10
 */
@ApiModel(description = "报警记录对象 core_alarm")
public class LanyaCoreAlarm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 报警id
     */
    @ApiModelProperty("报警id")
    private Long alarmId;

    /**
     * 信号接收时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "信号接收时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "信号接收时间")
    @ApiModelProperty("信号接收时间")
    private Date acceptTime;

    /**
     * 报警类型
     */
    @Excel(name = "报警类型")
    @ApiModelProperty("报警类型")
    private String alarmType;

    /**
     * 报警类型名称
     */
    @Excel(name = "报警类型名称")
    @ApiModelProperty("报警类型名称")
    private String alarmTypeName;

    /**
     * 报警名称
     */
    @Excel(name = "报警名称")
    @ApiModelProperty("报警名称")
    private String alarmName;

    /**
     * 报警描述
     */
    @Excel(name = "报警描述")
    @ApiModelProperty("报警描述")
    private String alarmDesc;

    /**
     * 报警状态（未处理15;已处理20；25无效）
     */
    @Excel(name = "报警状态", readConverterExp = "未=处理15;已处理20；25无效")
    @ApiModelProperty("报警状态")
    private String alarmStatus;

    /**
     * 报警区域ID
     */
    @Excel(name = "报警区域ID")
    @ApiModelProperty("报警区域ID")
    private Long areaId;

    /**
     * 报警区域名称
     */
    @Excel(name = "报警区域名称")
    @ApiModelProperty("报警区域名称")
    private String areaName;

    /**
     * 围栏id
     */
    @Excel(name = "围栏id")
    @ApiModelProperty("围栏id")
    private Long railId;

    /**
     * 围栏名称
     */
    @Excel(name = "围栏名称")
    @ApiModelProperty("围栏名称")
    private String railName;

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
     * 围栏坐标
     */
    @Excel(name = "围栏坐标")
    @ApiModelProperty("围栏坐标")
    private String railScope;

    /**
     * 围栏高度
     */
    @Excel(name = "围栏高度")
    @ApiModelProperty("围栏高度")
    private Long railHeight;

    /**
     * 绘制类型:0圆;1线;2多边形
     */
    @Excel(name = "绘制类型:0圆;1线;2多边形")
    @ApiModelProperty("绘制类型:0圆;1线;2多边形")
    private Integer drawType;

    /**
     * 经度
     */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 信标id
     */
    @Excel(name = "信标id")
    @ApiModelProperty("信标id")
    private String beaconId;

    /**
     * 定位卡号
     */
    @Excel(name = "定位卡号")
    @ApiModelProperty("定位卡号")
    private Long cardId;

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
     * 报警人
     */
    @Excel(name = "报警人")
    @ApiModelProperty("报警人")
    private String realName;

    /**
     * 工号
     */
    @Excel(name = "工号")
    @ApiModelProperty("工号")
    private String jobNumber;

    /**
     * 管理人员姓名
     */
    @Excel(name = "管理人员姓名")
    @ApiModelProperty("管理人员姓名")
    private String administratorName;

    /**
     * 管理人员号码
     */
    @Excel(name = "管理人员号码")
    @ApiModelProperty("管理人员号码")
    private String administratorPhone;

    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    @ApiModelProperty("部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 岗位id
     */
    @Excel(name = "岗位id")
    @ApiModelProperty("岗位id")
    private Long postId;

    /**
     * 岗位名称
     */
    @Excel(name = "岗位名称")
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 所属承包商
     */
    @Excel(name = "所属承包商")
    @ApiModelProperty("所属承包商")
    private Long contractorId;

    /**
     * 承包商名称
     */
    @Excel(name = "承包商名称")
    @ApiModelProperty("承包商名称")
    private String contractorName;

    /**
     * 处理人
     */
    @Excel(name = "处理人")
    @ApiModelProperty("处理人")
    private String disposeBy;

    /**
     * 处理时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "处理时间")
    @ApiModelProperty("处理时间")
    private Date disposeTime;

    /**
     * 处理反馈
     */
    @Excel(name = "处理反馈")
    @ApiModelProperty("处理反馈")
    private String disposeFeedback;

    /**
     * 结束时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "结束时间")
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 报警级别: 1/2/3级报警
     */
    @Excel(name = "报警级别: 1/2/3级报警")
    @ApiModelProperty("报警级别: 1/2/3级报警")
    private Integer alarmLevel;

    /**
     * 最高报警级别
     */
    @Excel(name = "最高报警级别")
    @ApiModelProperty("最高报警级别")
    private Integer highestAlarmLevel;

    /**
     * 所属楼宇id
     */
    @Excel(name = "所属楼宇id")
    @ApiModelProperty("所属楼宇id")
    private Long buildingId;

    /**
     * 所属楼宇名称
     */
    @Excel(name = "所属楼宇名称")
    @ApiModelProperty("所属楼宇名称")
    private String buildingName;

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }

    public String getAlarmDesc() {
        return alarmDesc;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

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

    public void setRailId(Long railId) {
        this.railId = railId;
    }

    public Long getRailId() {
        return railId;
    }

    public void setRailName(String railName) {
        this.railName = railName;
    }

    public String getRailName() {
        return railName;
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

    public void setRailScope(String railScope) {
        this.railScope = railScope;
    }

    public String getRailScope() {
        return railScope;
    }

    public void setRailHeight(Long railHeight) {
        this.railHeight = railHeight;
    }

    public Long getRailHeight() {
        return railHeight;
    }

    public void setDrawType(Integer drawType) {
        this.drawType = drawType;
    }

    public Integer getDrawType() {
        return drawType;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
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

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorPhone(String administratorPhone) {
        this.administratorPhone = administratorPhone;
    }

    public String getAdministratorPhone() {
        return administratorPhone;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostName() {
        return postName;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setDisposeBy(String disposeBy) {
        this.disposeBy = disposeBy;
    }

    public String getDisposeBy() {
        return disposeBy;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeFeedback(String disposeFeedback) {
        this.disposeFeedback = disposeFeedback;
    }

    public String getDisposeFeedback() {
        return disposeFeedback;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setHighestAlarmLevel(Integer highestAlarmLevel) {
        this.highestAlarmLevel = highestAlarmLevel;
    }

    public Integer getHighestAlarmLevel() {
        return highestAlarmLevel;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("alarmId", getAlarmId())
                .append("acceptTime", getAcceptTime())
                .append("alarmType", getAlarmType())
                .append("alarmTypeName", getAlarmTypeName())
                .append("alarmName", getAlarmName())
                .append("alarmDesc", getAlarmDesc())
                .append("alarmStatus", getAlarmStatus())
                .append("areaId", getAreaId())
                .append("areaName", getAreaName())
                .append("railId", getRailId())
                .append("railName", getRailName())
                .append("layerId", getLayerId())
                .append("layerHeight", getLayerHeight())
                .append("railScope", getRailScope())
                .append("railHeight", getRailHeight())
                .append("drawType", getDrawType())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("beaconId", getBeaconId())
                .append("cardId", getCardId())
                .append("personId", getPersonId())
                .append("personType", getPersonType())
                .append("staffType", getStaffType())
                .append("realName", getRealName())
                .append("jobNumber", getJobNumber())
                .append("administratorName", getAdministratorName())
                .append("administratorPhone", getAdministratorPhone())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("postId", getPostId())
                .append("postName", getPostName())
                .append("contractorId", getContractorId())
                .append("contractorName", getContractorName())
                .append("disposeBy", getDisposeBy())
                .append("disposeTime", getDisposeTime())
                .append("disposeFeedback", getDisposeFeedback())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("endTime", getEndTime())
                .append("alarmLevel", getAlarmLevel())
                .append("highestAlarmLevel", getHighestAlarmLevel())
                .append("buildingId", getBuildingId())
                .append("buildingName", getBuildingName())
                .toString();
    }
}
