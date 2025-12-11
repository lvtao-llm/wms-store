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
 * 巡检任务对象 wms_inspection_task
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
@ApiModel("巡检任务对象")
public class WmsInspectionTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键")
    private Long id;

    /** 巡检规则ID */
    @Excel(name = "巡检规则ID")
    @ApiModelProperty("巡检规则ID")
    private Long ruleId;

    /** 巡检规则名称 */
    @Excel(name = "巡检规则名称")
    @ApiModelProperty("巡检规则名称")
    private String ruleName;

    /** 任务时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "任务时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("任务时间")
    private Date taskTime;

    /** 实际巡检数据 */
    @Excel(name = "实际巡检数据")
    @ApiModelProperty("实际巡检数据")
    private String actualInspectionData;

    /** 巡检结果 */
    @Excel(name = "巡检结果")
    @ApiModelProperty("巡检结果")
    private String inspectionResult;

    /** 任务状态 */
    @Excel(name = "任务状态")
    @ApiModelProperty("任务状态")
    private String taskStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRuleId(Long ruleId) 
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId() 
    {
        return ruleId;
    }

    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }

    public void setTaskTime(Date taskTime) 
    {
        this.taskTime = taskTime;
    }

    public Date getTaskTime() 
    {
        return taskTime;
    }

    public void setActualInspectionData(String actualInspectionData) 
    {
        this.actualInspectionData = actualInspectionData;
    }

    public String getActualInspectionData() 
    {
        return actualInspectionData;
    }

    public void setInspectionResult(String inspectionResult) 
    {
        this.inspectionResult = inspectionResult;
    }

    public String getInspectionResult() 
    {
        return inspectionResult;
    }

    public void setTaskStatus(String taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() 
    {
        return taskStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ruleId", getRuleId())
            .append("ruleName", getRuleName())
            .append("taskTime", getTaskTime())
            .append("actualInspectionData", getActualInspectionData())
            .append("inspectionResult", getInspectionResult())
            .append("taskStatus", getTaskStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
