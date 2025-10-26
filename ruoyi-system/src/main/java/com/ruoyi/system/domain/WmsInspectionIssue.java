package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡检问题对象 wms_inspection_issue
 * 
 * @author ruoyi
 * @date 2025-10-25
 */
public class WmsInspectionIssue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 上报人ID */
    @Excel(name = "上报人ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long reporterId;

    /** 定位卡ID */
    @Excel(name = "定位卡ID")
    private Long cardId;

    /** 问题时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "问题时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date issueTime;

    /** 问题描述 */
    @Excel(name = "问题描述")
    private String description;

    /** 问题照片 */
    @Excel(name = "问题照片")
    private String issuePhotos;

    /** 解决人ID */
    @Excel(name = "解决人ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long resolverId;

    /** 解决人姓名 */
    @Excel(name = "解决人姓名")
    private String resolverName;

    /** 解决时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "解决时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date resolveTime;

    /** 解决描述 */
    @Excel(name = "解决描述")
    private String resolveDescription;

    /** 解决照片 */
    @Excel(name = "解决照片")
    private String resolvePhotos;

    /** 是否已解决 */
    @Excel(name = "是否已解决")
    private Integer isResolved;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
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

    public void setReporterId(Long reporterId) 
    {
        this.reporterId = reporterId;
    }

    public Long getReporterId() 
    {
        return reporterId;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setIssueTime(Date issueTime) 
    {
        this.issueTime = issueTime;
    }

    public Date getIssueTime() 
    {
        return issueTime;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setIssuePhotos(String issuePhotos) 
    {
        this.issuePhotos = issuePhotos;
    }

    public String getIssuePhotos() 
    {
        return issuePhotos;
    }

    public void setResolverId(Long resolverId) 
    {
        this.resolverId = resolverId;
    }

    public Long getResolverId() 
    {
        return resolverId;
    }

    public void setResolverName(String resolverName) 
    {
        this.resolverName = resolverName;
    }

    public String getResolverName() 
    {
        return resolverName;
    }

    public void setResolveTime(Date resolveTime) 
    {
        this.resolveTime = resolveTime;
    }

    public Date getResolveTime() 
    {
        return resolveTime;
    }

    public void setResolveDescription(String resolveDescription) 
    {
        this.resolveDescription = resolveDescription;
    }

    public String getResolveDescription() 
    {
        return resolveDescription;
    }

    public void setResolvePhotos(String resolvePhotos) 
    {
        this.resolvePhotos = resolvePhotos;
    }

    public String getResolvePhotos() 
    {
        return resolvePhotos;
    }

    public void setIsResolved(Integer isResolved) 
    {
        this.isResolved = isResolved;
    }

    public Integer getIsResolved() 
    {
        return isResolved;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("ruleId", getRuleId())
            .append("ruleName", getRuleName())
            .append("reporterId", getReporterId())
            .append("cardId", getCardId())
            .append("issueTime", getIssueTime())
            .append("description", getDescription())
            .append("issuePhotos", getIssuePhotos())
            .append("resolverId", getResolverId())
            .append("resolverName", getResolverName())
            .append("resolveTime", getResolveTime())
            .append("resolveDescription", getResolveDescription())
            .append("resolvePhotos", getResolvePhotos())
            .append("isResolved", getIsResolved())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
