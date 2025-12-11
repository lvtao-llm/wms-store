package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 巡检规则对象 wms_inspection_rule
 *
 * @author ruoyi
 * @date 2025-10-25
 */
@ApiModel("巡检规则对象")
public class WmsInspectionRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    @ApiModelProperty("规则名称")
    private String name;

    /**
     * 巡检周期
     */
    @Excel(name = "巡检周期")
    @ApiModelProperty("巡检周期")
    private String cron;

    /**
     * 巡检时间误差
     */
    @Excel(name = "巡检时间误差")
    @ApiModelProperty("巡检时间误差")
    private Long timeTolerance;

    /**
     * 路径点
     */
    @Excel(name = "路径点")
    @ApiModelProperty("路径点")
    private String pathPoints;

    /**
     * 巡检点误差
     */
    @Excel(name = "巡检点误差")
    @ApiModelProperty("巡检点误差")
    private Long pointTolerance;

    /**
     * 是否按路径点顺序巡检
     */
    @Excel(name = "是否按路径点顺序巡检")
    @ApiModelProperty("是否按路径点顺序巡检")
    private String sequentialInspection;

    /**
     * 巡检人
     */
    @Excel(name = "巡检人")
    @ApiModelProperty("巡检人")
    private String inspectors;

    /**
     * 状态
     */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 下次任务ID
     */
    @Excel(name = "下次任务ID")
    @ApiModelProperty("下次任务ID")
    private Long nextTaskID;

    /**
     * 下次任务时间
     */
    @Excel(name = "下次任务时间")
    @ApiModelProperty("下次任务时间")
    private Date nextTaskTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return cron;
    }

    public void setTimeTolerance(Long timeTolerance) {
        this.timeTolerance = timeTolerance;
    }

    public Long getTimeTolerance() {
        return timeTolerance;
    }

    public void setPathPoints(String pathPoints) {
        this.pathPoints = pathPoints;
    }

    public String getPathPoints() {
        return pathPoints;
    }

    public void setPointTolerance(Long pointTolerance) {
        this.pointTolerance = pointTolerance;
    }

    public Long getPointTolerance() {
        return pointTolerance;
    }

    public void setSequentialInspection(String sequentialInspection) {
        this.sequentialInspection = sequentialInspection;
    }

    public String getSequentialInspection() {
        return sequentialInspection;
    }

    public void setInspectors(String inspectors) {
        this.inspectors = inspectors;
    }

    public String getInspectors() {
        return inspectors;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("cron", getCron())
                .append("timeTolerance", getTimeTolerance())
                .append("pathPoints", getPathPoints())
                .append("pointTolerance", getPointTolerance())
                .append("sequentialInspection", getSequentialInspection())
                .append("inspectors", getInspectors())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }

    public Long getNextTaskID() {
        return nextTaskID;
    }

    public void setNextTaskID(Long nextTaskID) {
        this.nextTaskID = nextTaskID;
    }

    public Date getNextTaskTime() {
        return nextTaskTime;
    }

    public void setNextTaskTime(Date nextTaskTime) {
        this.nextTaskTime = nextTaskTime;
    }
}
