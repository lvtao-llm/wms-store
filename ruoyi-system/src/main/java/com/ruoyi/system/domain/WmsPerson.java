package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人员档案对象 wms_person
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@ApiModel("人员档案对象")
public class WmsPerson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long personId;

    /**
     * 1员工 2访客
     */
    @Excel(name = "1员工 2访客")
    @ApiModelProperty("1员工 2访客")
    private String personType;

    /**
     * 员工编号（访客空）
     */
    @Excel(name = "员工编号", readConverterExp = "访=客空")
    @ApiModelProperty("员工编号（访客空）")
    private String personEmpNo;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    @ApiModelProperty("身份证号")
    private String personIdentityNo;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String personName;

    /**
     * 性别
     */
    @Excel(name = "性别")
    @ApiModelProperty("性别")
    private String personSex;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    @ApiModelProperty("年龄")
    private String personAge;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    @ApiModelProperty("手机号")
    private String personPhone;

    /**
     * 职位
     */
    @Excel(name = "职位")
    @ApiModelProperty("职位")
    private String personPost;

    /**
     * 工号
     */
    @Excel(name = "工号")
    @ApiModelProperty("工号")
    private String personJobNo;

    /**
     * 部门ID（ruoyi系统表）
     */
    @Excel(name = "部门ID", readConverterExp = "r=uoyi系统表")
    @ApiModelProperty("部门ID")
    private Long personDeptId;

    /**
     * 人脸照片Base64
     */
    @Excel(name = "人脸照片Base64")
    @ApiModelProperty("人脸照片Base64")
    private String personFaceImg;

    /**
     * 定位卡号
     */
    @Excel(name = "定位卡号")
    @ApiModelProperty("定位卡号")
    private String personCardNo;

    /**
     * 授权区域JSON数组
     */
    @Excel(name = "授权区域JSON数组")
    @ApiModelProperty("授权区域JSON数组")
    private String personAuthArea;

    /**
     * 状态 0禁用 1启用
     */
    @Excel(name = "状态 0禁用 1启用")
    @ApiModelProperty("状态 0禁用 1启用")
    private String personStatus;

    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志")
    private String delFlag;

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

    public void setPersonEmpNo(String personEmpNo) {
        this.personEmpNo = personEmpNo;
    }

    public String getPersonEmpNo() {
        return personEmpNo;
    }

    public void setPersonIdentityNo(String personIdentityNo) {
        this.personIdentityNo = personIdentityNo;
    }

    public String getPersonIdentityNo() {
        return personIdentityNo;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonSex(String personSex) {
        this.personSex = personSex;
    }

    public String getPersonSex() {
        return personSex;
    }

    public void setPersonAge(String personAge) {
        this.personAge = personAge;
    }

    public String getPersonAge() {
        return personAge;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPost(String personPost) {
        this.personPost = personPost;
    }

    public String getPersonPost() {
        return personPost;
    }

    public void setPersonJobNo(String personJobNo) {
        this.personJobNo = personJobNo;
    }

    public String getPersonJobNo() {
        return personJobNo;
    }

    public void setPersonDeptId(Long personDeptId) {
        this.personDeptId = personDeptId;
    }

    public Long getPersonDeptId() {
        return personDeptId;
    }

    public void setPersonFaceImg(String personFaceImg) {
        this.personFaceImg = personFaceImg;
    }

    public String getPersonFaceImg() {
        return personFaceImg;
    }

    public void setPersonCardNo(String personCardNo) {
        this.personCardNo = personCardNo;
    }

    public String getPersonCardNo() {
        return personCardNo;
    }

    public void setPersonAuthArea(String personAuthArea) {
        this.personAuthArea = personAuthArea;
    }

    public String getPersonAuthArea() {
        return personAuthArea;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    public String getPersonStatus() {
        return personStatus;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("personId", getPersonId())
                .append("personType", getPersonType())
                .append("personEmpNo", getPersonEmpNo())
                .append("personIdentityNo", getPersonIdentityNo())
                .append("personName", getPersonName())
                .append("personSex", getPersonSex())
                .append("personAge", getPersonAge())
                .append("personPhone", getPersonPhone())
                .append("personPost", getPersonPost())
                .append("personJobNo", getPersonJobNo())
                .append("personDeptId", getPersonDeptId())
                .append("personFaceImg", getPersonFaceImg())
                .append("personCardNo", getPersonCardNo())
                .append("personAuthArea", getPersonAuthArea())
                .append("personStatus", getPersonStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
