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
 * 轨迹对象 wms_trajectory
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@ApiModel("轨迹对象")
public class WmsTrajectory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键，轨迹ID */
    @ApiModelProperty("主键")
    private Long trajectoryId;

    /** 轨迹类型。1人员 2车辆 */
    @Excel(name = "轨迹类型。1人员 2车辆")
    @ApiModelProperty("轨迹类型")
    private String trajectoryType;

    /** 开始时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("开始时间")
    private Date trajectoryBegin;

    /** 结束时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("结束时间")
    private Date trajectoryEnd;

    /** 轨迹：[{x,y,t}] */
    @Excel(name = "轨迹：[{x,y,t}]")
    @ApiModelProperty("轨迹")
    private String trajectoryPoints;

    /** 发卡记录ID */
    @Excel(name = "发卡记录ID")
    @ApiModelProperty("发卡记录ID")
    private Long cardRecordId;

    /** 模糊查询字段 */
    @Excel(name = "模糊查询字段")
    @ApiModelProperty("模糊查询字段")
    private String fuzzy;

    /** 发卡记录对象ID */
    @Excel(name = "发卡记录对象ID")
    @ApiModelProperty("发卡记录对象ID")
    private Long workId;

    public void setTrajectoryId(Long trajectoryId) 
    {
        this.trajectoryId = trajectoryId;
    }

    public Long getTrajectoryId() 
    {
        return trajectoryId;
    }

    public void setTrajectoryType(String trajectoryType) 
    {
        this.trajectoryType = trajectoryType;
    }

    public String getTrajectoryType() 
    {
        return trajectoryType;
    }

    public void setTrajectoryBegin(Date trajectoryBegin) 
    {
        this.trajectoryBegin = trajectoryBegin;
    }

    public Date getTrajectoryBegin() 
    {
        return trajectoryBegin;
    }

    public void setTrajectoryEnd(Date trajectoryEnd) 
    {
        this.trajectoryEnd = trajectoryEnd;
    }

    public Date getTrajectoryEnd() 
    {
        return trajectoryEnd;
    }

    public void setTrajectoryPoints(String trajectoryPoints) 
    {
        this.trajectoryPoints = trajectoryPoints;
    }

    public String getTrajectoryPoints() 
    {
        return trajectoryPoints;
    }

    public void setCardRecordId(Long cardRecordId) 
    {
        this.cardRecordId = cardRecordId;
    }

    public Long getCardRecordId() 
    {
        return cardRecordId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("trajectoryId", getTrajectoryId())
            .append("trajectoryType", getTrajectoryType())
            .append("trajectoryBegin", getTrajectoryBegin())
            .append("trajectoryEnd", getTrajectoryEnd())
            .append("trajectoryPoints", getTrajectoryPoints())
            .append("cardRecordId", getCardRecordId())
            .toString();
    }

    public void setFuzzy(String fuzzy) {
        this.fuzzy = fuzzy;
    }

    public String getFuzzy() {
        return fuzzy;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getWorkId() {
        return workId;
    }
}
