package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访客记录对象 core_visitor
 * 
 * @author ruoyi
 * @date 2025-10-10
 */
public class LanyaCoreVisitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 访客id */
    private Long visitorId;

    /** 卡号 */
    @Excel(name = "卡号")
    private Long cardId;

    /** 定位图标类型（见字典） */
    @Excel(name = "定位图标类型", readConverterExp = "见=字典")
    private String positionIconType;

    /** 卡类型 */
    @Excel(name = "卡类型")
    private String cardType;

    /** 姓名 */
    @Excel(name = "姓名")
    private String realName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 性别：男/女 */
    @Excel(name = "性别：男/女")
    private String sex;

    /** 照片 */
    @Excel(name = "照片")
    private String personPhoto;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birth;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 籍贯 */
    @Excel(name = "籍贯")
    private String nativePlace;

    /** 政治面貌 */
    @Excel(name = "政治面貌")
    private String politicsStatus;

    /** 健康状况 */
    @Excel(name = "健康状况")
    private String healthStatus;

    /** 证件类型（见字典） */
    @Excel(name = "证件类型", readConverterExp = "见=字典")
    private String idType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String idNumber;

    /** 人员IC号 */
    @Excel(name = "人员IC号")
    private String personIc;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 岗位id */
    @Excel(name = "岗位id")
    private Long postId;

    /** 管理人员姓名 */
    @Excel(name = "管理人员姓名")
    private String administratorName;

    /** 管理人员号码 */
    @Excel(name = "管理人员号码")
    private String administratorPhone;

    /** 工号 */
    @Excel(name = "工号")
    private String jobNumber;

    /** 所在单位 */
    @Excel(name = "所在单位")
    private String companyName;

    /** 办公电话 */
    @Excel(name = "办公电话")
    private String officePhone;

    /** 预约进入开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约进入开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reservationEntryStartTime;

    /** 预约进入结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约进入结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reservationEntryEndTime;

    /** 来访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "来访时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitTime;

    /** 离开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveTime;

    /** 来访状态（N未离开；Y已离开） */
    @Excel(name = "来访状态", readConverterExp = "N=未离开；Y已离开")
    private String visitorStatus;

    /** 来访原因 */
    @Excel(name = "来访原因")
    private String visitReason;

    /** 来访地点 */
    @Excel(name = "来访地点")
    private String visitLocation;

    /** 关联人员id */
    @Excel(name = "关联人员id")
    private Long personId;

    /** 接待人 */
    @Excel(name = "接待人")
    private String receiveLeader;

    /** 接待人电话 */
    @Excel(name = "接待人电话")
    private String receivePhone;

    /** 闸机通行 Y：允许 N：禁止 */
    @Excel(name = "闸机通行 Y：允许 N：禁止")
    private String gateThrough;

    public void setVisitorId(Long visitorId) 
    {
        this.visitorId = visitorId;
    }

    public Long getVisitorId() 
    {
        return visitorId;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setPositionIconType(String positionIconType) 
    {
        this.positionIconType = positionIconType;
    }

    public String getPositionIconType() 
    {
        return positionIconType;
    }

    public void setCardType(String cardType) 
    {
        this.cardType = cardType;
    }

    public String getCardType() 
    {
        return cardType;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setPersonPhoto(String personPhoto) 
    {
        this.personPhoto = personPhoto;
    }

    public String getPersonPhoto() 
    {
        return personPhoto;
    }

    public void setBirth(Date birth) 
    {
        this.birth = birth;
    }

    public Date getBirth() 
    {
        return birth;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setNativePlace(String nativePlace) 
    {
        this.nativePlace = nativePlace;
    }

    public String getNativePlace() 
    {
        return nativePlace;
    }

    public void setPoliticsStatus(String politicsStatus) 
    {
        this.politicsStatus = politicsStatus;
    }

    public String getPoliticsStatus() 
    {
        return politicsStatus;
    }

    public void setHealthStatus(String healthStatus) 
    {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() 
    {
        return healthStatus;
    }

    public void setIdType(String idType) 
    {
        this.idType = idType;
    }

    public String getIdType() 
    {
        return idType;
    }

    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }

    public void setPersonIc(String personIc) 
    {
        this.personIc = personIc;
    }

    public String getPersonIc() 
    {
        return personIc;
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

    public void setAdministratorName(String administratorName) 
    {
        this.administratorName = administratorName;
    }

    public String getAdministratorName() 
    {
        return administratorName;
    }

    public void setAdministratorPhone(String administratorPhone) 
    {
        this.administratorPhone = administratorPhone;
    }

    public String getAdministratorPhone() 
    {
        return administratorPhone;
    }

    public void setJobNumber(String jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() 
    {
        return jobNumber;
    }

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setOfficePhone(String officePhone) 
    {
        this.officePhone = officePhone;
    }

    public String getOfficePhone() 
    {
        return officePhone;
    }

    public void setReservationEntryStartTime(Date reservationEntryStartTime) 
    {
        this.reservationEntryStartTime = reservationEntryStartTime;
    }

    public Date getReservationEntryStartTime() 
    {
        return reservationEntryStartTime;
    }

    public void setReservationEntryEndTime(Date reservationEntryEndTime) 
    {
        this.reservationEntryEndTime = reservationEntryEndTime;
    }

    public Date getReservationEntryEndTime() 
    {
        return reservationEntryEndTime;
    }

    public void setVisitTime(Date visitTime) 
    {
        this.visitTime = visitTime;
    }

    public Date getVisitTime() 
    {
        return visitTime;
    }

    public void setLeaveTime(Date leaveTime) 
    {
        this.leaveTime = leaveTime;
    }

    public Date getLeaveTime() 
    {
        return leaveTime;
    }

    public void setVisitorStatus(String visitorStatus) 
    {
        this.visitorStatus = visitorStatus;
    }

    public String getVisitorStatus() 
    {
        return visitorStatus;
    }

    public void setVisitReason(String visitReason) 
    {
        this.visitReason = visitReason;
    }

    public String getVisitReason() 
    {
        return visitReason;
    }

    public void setVisitLocation(String visitLocation) 
    {
        this.visitLocation = visitLocation;
    }

    public String getVisitLocation() 
    {
        return visitLocation;
    }

    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }

    public void setReceiveLeader(String receiveLeader) 
    {
        this.receiveLeader = receiveLeader;
    }

    public String getReceiveLeader() 
    {
        return receiveLeader;
    }

    public void setReceivePhone(String receivePhone) 
    {
        this.receivePhone = receivePhone;
    }

    public String getReceivePhone() 
    {
        return receivePhone;
    }

    public void setGateThrough(String gateThrough) 
    {
        this.gateThrough = gateThrough;
    }

    public String getGateThrough() 
    {
        return gateThrough;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("visitorId", getVisitorId())
            .append("cardId", getCardId())
            .append("positionIconType", getPositionIconType())
            .append("cardType", getCardType())
            .append("realName", getRealName())
            .append("phone", getPhone())
            .append("sex", getSex())
            .append("personPhoto", getPersonPhoto())
            .append("birth", getBirth())
            .append("email", getEmail())
            .append("nativePlace", getNativePlace())
            .append("politicsStatus", getPoliticsStatus())
            .append("healthStatus", getHealthStatus())
            .append("idType", getIdType())
            .append("idNumber", getIdNumber())
            .append("personIc", getPersonIc())
            .append("deptId", getDeptId())
            .append("postId", getPostId())
            .append("administratorName", getAdministratorName())
            .append("administratorPhone", getAdministratorPhone())
            .append("jobNumber", getJobNumber())
            .append("companyName", getCompanyName())
            .append("officePhone", getOfficePhone())
            .append("reservationEntryStartTime", getReservationEntryStartTime())
            .append("reservationEntryEndTime", getReservationEntryEndTime())
            .append("visitTime", getVisitTime())
            .append("leaveTime", getLeaveTime())
            .append("visitorStatus", getVisitorStatus())
            .append("visitReason", getVisitReason())
            .append("visitLocation", getVisitLocation())
            .append("personId", getPersonId())
            .append("receiveLeader", getReceiveLeader())
            .append("receivePhone", getReceivePhone())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("gateThrough", getGateThrough())
            .toString();
    }
}
