package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人员报警对象 core_alarm_people
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public class LanyaCoreAlarmPeople extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 报警记录id */
    @Excel(name = "报警记录id")
    private Long coreAlarmId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String realName;

    /** 人员id */
    @Excel(name = "人员id")
    private Long personId;

    /** 卡号 */
    @Excel(name = "卡号")
    private Long cardId;

    /** 工号 */
    @Excel(name = "工号")
    private String jobNumber;

    /** 岗位 */
    @Excel(name = "岗位")
    private String postName;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 楼层 */
    @Excel(name = "楼层")
    private String layerId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acceptTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCoreAlarmId(Long coreAlarmId) 
    {
        this.coreAlarmId = coreAlarmId;
    }

    public Long getCoreAlarmId() 
    {
        return coreAlarmId;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setJobNumber(String jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() 
    {
        return jobNumber;
    }

    public void setPostName(String postName) 
    {
        this.postName = postName;
    }

    public String getPostName() 
    {
        return postName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
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

    public void setLayerId(String layerId) 
    {
        this.layerId = layerId;
    }

    public String getLayerId() 
    {
        return layerId;
    }

    public void setAcceptTime(Date acceptTime) 
    {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() 
    {
        return acceptTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("coreAlarmId", getCoreAlarmId())
            .append("realName", getRealName())
            .append("personId", getPersonId())
            .append("cardId", getCardId())
            .append("jobNumber", getJobNumber())
            .append("postName", getPostName())
            .append("phone", getPhone())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("layerId", getLayerId())
            .append("acceptTime", getAcceptTime())
            .append("endTime", getEndTime())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
