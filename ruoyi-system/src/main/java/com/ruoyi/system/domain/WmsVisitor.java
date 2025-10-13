package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访客信息对象 wms_visitor
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class WmsVisitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long visitorId;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String visitorIdentityNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String visitorName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String visitorPhone;

    /** 来访事由 */
    @Excel(name = "来访事由")
    private String visitorPurpose;

    /** 预计停留时间 */
    @Excel(name = "预计停留时间")
    private String estimatedTime;

    /** 被访人 */
    @Excel(name = "被访人")
    private String visitee;

    /** 人脸照片Base64 */
    @Excel(name = "人脸照片Base64")
    private String img;

    /** 发卡记录ID */
    @Excel(name = "发卡记录ID")
    private Long cardRecordId;

    /** 到访区域:json数组 */
    @Excel(name = "到访区域:json数组")
    private String areaCode;

    public void setVisitorId(Long visitorId) 
    {
        this.visitorId = visitorId;
    }

    public Long getVisitorId() 
    {
        return visitorId;
    }

    public void setVisitorIdentityNo(String visitorIdentityNo) 
    {
        this.visitorIdentityNo = visitorIdentityNo;
    }

    public String getVisitorIdentityNo() 
    {
        return visitorIdentityNo;
    }

    public void setVisitorName(String visitorName) 
    {
        this.visitorName = visitorName;
    }

    public String getVisitorName() 
    {
        return visitorName;
    }

    public void setVisitorPhone(String visitorPhone) 
    {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorPhone() 
    {
        return visitorPhone;
    }

    public void setVisitorPurpose(String visitorPurpose) 
    {
        this.visitorPurpose = visitorPurpose;
    }

    public String getVisitorPurpose() 
    {
        return visitorPurpose;
    }

    public void setEstimatedTime(String estimatedTime) 
    {
        this.estimatedTime = estimatedTime;
    }

    public String getEstimatedTime() 
    {
        return estimatedTime;
    }

    public void setVisitee(String visitee) 
    {
        this.visitee = visitee;
    }

    public String getVisitee() 
    {
        return visitee;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    public void setCardRecordId(Long cardRecordId) 
    {
        this.cardRecordId = cardRecordId;
    }

    public Long getCardRecordId() 
    {
        return cardRecordId;
    }

    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("visitorId", getVisitorId())
            .append("visitorIdentityNo", getVisitorIdentityNo())
            .append("visitorName", getVisitorName())
            .append("visitorPhone", getVisitorPhone())
            .append("visitorPurpose", getVisitorPurpose())
            .append("estimatedTime", getEstimatedTime())
            .append("visitee", getVisitee())
            .append("img", getImg())
            .append("cardRecordId", getCardRecordId())
            .append("areaCode", getAreaCode())
            .toString();
    }
}
