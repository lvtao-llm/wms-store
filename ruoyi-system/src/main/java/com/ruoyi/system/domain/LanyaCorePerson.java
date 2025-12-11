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
 * 人员（员工/访客/承包商人员）对象 core_person
 *
 * @author 吕涛
 * @date 2025-10-10
 */
@ApiModel(description = "人员（员工/访客/承包商人员）对象 core_person")
public class LanyaCorePerson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @ApiModelProperty("人员id")
    private Long personId;

    /**
     * 人员类型（见字典）
     */
    @Excel(name = "人员类型", readConverterExp = "见=字典")
    @ApiModelProperty("人员类型")
    private String personType;

    /**
     * 员工类型（见字典）
     */
    @Excel(name = "员工类型", readConverterExp = "见=字典")
    @ApiModelProperty("员工类型")
    private String staffType;

    /**
     * 定位图标类型（见字典）
     */
    @Excel(name = "定位图标类型", readConverterExp = "见=字典")
    @ApiModelProperty("定位图标类型")
    private String positionIconType;

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
     * 姓名
     */
    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String realName;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 性别：男/女
     */
    @Excel(name = "性别：男/女")
    @ApiModelProperty("性别：男/女")
    private String sex;

    /**
     * 照片
     */
    @Excel(name = "照片")
    @ApiModelProperty("照片")
    private String personPhoto;

    /**
     * 生日
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "生日")
    @ApiModelProperty("生日")
    private Date birth;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 籍贯
     */
    @Excel(name = "籍贯")
    @ApiModelProperty("籍贯")
    private String nativePlace;

    /**
     * 民族
     */
    @Excel(name = "民族")
    @ApiModelProperty("民族")
    private String nation;

    /**
     * 婚姻状况
     */
    @Excel(name = "婚姻状况")
    @ApiModelProperty("婚姻状况")
    private String maritalStatus;

    /**
     * 政治面貌
     */
    @Excel(name = "政治面貌")
    @ApiModelProperty("政治面貌")
    private String politicsStatus;

    /**
     * 健康状况
     */
    @Excel(name = "健康状况")
    @ApiModelProperty("健康状况")
    private String healthStatus;

    /**
     * 证件类型（见字典）
     */
    @Excel(name = "证件类型", readConverterExp = "见=字典")
    @ApiModelProperty("证件类型")
    private String idType;

    /**
     * 证件号码
     */
    @Excel(name = "证件号码")
    @ApiModelProperty("证件号码")
    private String idNumber;

    /**
     * 人员IC号
     */
    @Excel(name = "人员IC号")
    @ApiModelProperty("人员IC号")
    private String personIc;

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
     * 职责
     */
    @Excel(name = "职责")
    @ApiModelProperty("职责")
    private String duty;

    /**
     * 入职时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "入职时间")
    @ApiModelProperty("入职时间")
    private Date hireDate;

    /**
     * 工号
     */
    @Excel(name = "工号")
    @ApiModelProperty("工号")
    private String jobNumber;

    /**
     * 工作状态：在职/离职
     */
    @Excel(name = "工作状态：在职/离职")
    @ApiModelProperty("工作状态")
    private String jobStatus;

    /**
     * 办公电话
     */
    @Excel(name = "办公电话")
    @ApiModelProperty("办公电话")
    private String officePhone;

    /**
     * 职称
     */
    @Excel(name = "职称")
    @ApiModelProperty("职称")
    private String professionalTitle;

    /**
     * 职称证书编号
     */
    @Excel(name = "职称证书编号")
    @ApiModelProperty("职称证书编号")
    private String professionalTitleNumber;

    /**
     * 工龄
     */
    @Excel(name = "工龄")
    @ApiModelProperty("工龄")
    private String seniority;

    /**
     * 三年以上化工行业经验（是/否）
     */
    @Excel(name = "三年以上化工行业经验", readConverterExp = "是=/否")
    @ApiModelProperty("三年以上化工行业经验")
    private String yearPlusExperience;

    /**
     * 工作经历
     */
    @Excel(name = "工作经历")
    @ApiModelProperty("工作经历")
    private String experience;

    /**
     * 最高学历（见字典）
     */
    @Excel(name = "最高学历", readConverterExp = "见=字典")
    @ApiModelProperty("最高学历")
    private String highestEducation;

    /**
     * 最高学位（见字典）
     */
    @Excel(name = "最高学位", readConverterExp = "见=字典")
    @ApiModelProperty("最高学位")
    private String highestDegree;

    /**
     * 专业
     */
    @Excel(name = "专业")
    @ApiModelProperty("专业")
    private String profession;

    /**
     * 毕业院校
     */
    @Excel(name = "毕业院校")
    @ApiModelProperty("毕业院校")
    private String school;

    /**
     * 毕业证书编号
     */
    @Excel(name = "毕业证书编号")
    @ApiModelProperty("毕业证书编号")
    private String diplomaNumber;

    /**
     * 注册工程师证书编号
     */
    @Excel(name = "注册工程师证书编号")
    @ApiModelProperty("注册工程师证书编号")
    private String cengNumber;

    /**
     * 附件
     */
    @Excel(name = "附件")
    @ApiModelProperty("附件")
    private String accessory;

    /**
     * 员工离职日期/承包商人员入场到期日期
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "员工离职日期/承包商人员入场到期日期", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "员工离职日期/承包商人员入场到期日期")
    @ApiModelProperty("员工离职日期/承包商人员入场到期日期")
    private Date dimissionDate;

    /**
     * 所属承包商
     */
    @Excel(name = "所属承包商")
    @ApiModelProperty("所属承包商")
    private Long contractorId;

    /**
     * 人员编码
     */
    @Excel(name = "人员编码")
    @ApiModelProperty("人员编码")
    private String personCode;

    /**
     * 二道门状态(98进入;99离开)
     */
    @Excel(name = "二道门状态(98进入;99离开)")
    @ApiModelProperty("二道门状态(98进入;99离开)")
    private String gateStatus;

    /**
     * 二道门进入时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "二道门进入时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "二道门进入时间")
    @ApiModelProperty("二道门进入时间")
    private Date gateInTime;

    /**
     * 二道门出去时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "二道门出去时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "二道门出去时间")
    @ApiModelProperty("二道门出去时间")
    private Date gateOutTime;

    /**
     * 人员来源
     */
    @Excel(name = "人员来源")
    @ApiModelProperty("人员来源")
    private String personSource;

    /**
     * 闸机通行 Y：允许 N：禁止
     */
    @Excel(name = "闸机通行 Y：允许 N：禁止")
    @ApiModelProperty("闸机通行 Y：允许 N：禁止")
    private String gateThrough;

    /**
     * 同步第三方人员是否成功 1 成功 2 失败
     */
    @Excel(name = "同步第三方人员是否成功 1 成功 2 失败")
    @ApiModelProperty("同步第三方人员是否成功 1 成功 2 失败")
    private String updateFail;

    /**
     * 人脸照片唯一标识
     */
    @Excel(name = "人脸照片唯一标识")
    @ApiModelProperty("人脸照片唯一标识")
    private String photoSign;

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

    public void setPositionIconType(String positionIconType) {
        this.positionIconType = positionIconType;
    }

    public String getPositionIconType() {
        return positionIconType;
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

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Date getBirth() {
        return birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNation() {
        return nation;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setPoliticsStatus(String politicsStatus) {
        this.politicsStatus = politicsStatus;
    }

    public String getPoliticsStatus() {
        return politicsStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setPersonIc(String personIc) {
        this.personIc = personIc;
    }

    public String getPersonIc() {
        return personIc;
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

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getDuty() {
        return duty;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitleNumber(String professionalTitleNumber) {
        this.professionalTitleNumber = professionalTitleNumber;
    }

    public String getProfessionalTitleNumber() {
        return professionalTitleNumber;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setYearPlusExperience(String yearPlusExperience) {
        this.yearPlusExperience = yearPlusExperience;
    }

    public String getYearPlusExperience() {
        return yearPlusExperience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }

    public String getDiplomaNumber() {
        return diplomaNumber;
    }

    public void setCengNumber(String cengNumber) {
        this.cengNumber = cengNumber;
    }

    public String getCengNumber() {
        return cengNumber;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setDimissionDate(Date dimissionDate) {
        this.dimissionDate = dimissionDate;
    }

    public Date getDimissionDate() {
        return dimissionDate;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setGateStatus(String gateStatus) {
        this.gateStatus = gateStatus;
    }

    public String getGateStatus() {
        return gateStatus;
    }

    public void setGateInTime(Date gateInTime) {
        this.gateInTime = gateInTime;
    }

    public Date getGateInTime() {
        return gateInTime;
    }

    public void setGateOutTime(Date gateOutTime) {
        this.gateOutTime = gateOutTime;
    }

    public Date getGateOutTime() {
        return gateOutTime;
    }

    public void setPersonSource(String personSource) {
        this.personSource = personSource;
    }

    public String getPersonSource() {
        return personSource;
    }

    public void setGateThrough(String gateThrough) {
        this.gateThrough = gateThrough;
    }

    public String getGateThrough() {
        return gateThrough;
    }

    public void setUpdateFail(String updateFail) {
        this.updateFail = updateFail;
    }

    public String getUpdateFail() {
        return updateFail;
    }

    public void setPhotoSign(String photoSign) {
        this.photoSign = photoSign;
    }

    public String getPhotoSign() {
        return photoSign;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("personId", getPersonId())
                .append("personType", getPersonType())
                .append("staffType", getStaffType())
                .append("positionIconType", getPositionIconType())
                .append("cardId", getCardId())
                .append("cardType", getCardType())
                .append("realName", getRealName())
                .append("phone", getPhone())
                .append("sex", getSex())
                .append("personPhoto", getPersonPhoto())
                .append("birth", getBirth())
                .append("email", getEmail())
                .append("nativePlace", getNativePlace())
                .append("nation", getNation())
                .append("maritalStatus", getMaritalStatus())
                .append("politicsStatus", getPoliticsStatus())
                .append("healthStatus", getHealthStatus())
                .append("idType", getIdType())
                .append("idNumber", getIdNumber())
                .append("personIc", getPersonIc())
                .append("administratorName", getAdministratorName())
                .append("administratorPhone", getAdministratorPhone())
                .append("deptId", getDeptId())
                .append("postId", getPostId())
                .append("duty", getDuty())
                .append("hireDate", getHireDate())
                .append("jobNumber", getJobNumber())
                .append("jobStatus", getJobStatus())
                .append("officePhone", getOfficePhone())
                .append("professionalTitle", getProfessionalTitle())
                .append("professionalTitleNumber", getProfessionalTitleNumber())
                .append("seniority", getSeniority())
                .append("yearPlusExperience", getYearPlusExperience())
                .append("experience", getExperience())
                .append("highestEducation", getHighestEducation())
                .append("highestDegree", getHighestDegree())
                .append("profession", getProfession())
                .append("school", getSchool())
                .append("diplomaNumber", getDiplomaNumber())
                .append("cengNumber", getCengNumber())
                .append("accessory", getAccessory())
                .append("dimissionDate", getDimissionDate())
                .append("contractorId", getContractorId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("personCode", getPersonCode())
                .append("gateStatus", getGateStatus())
                .append("gateInTime", getGateInTime())
                .append("gateOutTime", getGateOutTime())
                .append("personSource", getPersonSource())
                .append("gateThrough", getGateThrough())
                .append("updateFail", getUpdateFail())
                .append("photoSign", getPhotoSign())
                .toString();
    }
}
