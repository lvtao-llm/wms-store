package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 定位卡管理对象 device_card
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
public class LanyaDeviceCard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 卡号 */
    @Excel(name = "卡号")
    private Long cardId;

    /** 卡类型 */
    @Excel(name = "卡类型")
    private String cardType;

    /** 定位卡型号 */
    @Excel(name = "定位卡型号")
    private String cardModel;

    /** IC卡号 */
    @Excel(name = "IC卡号")
    private String icCardId;

    /** 电量百分比 */
    @Excel(name = "电量百分比")
    private Integer cardPower;

    /** 卡状态（出厂默认-1） */
    @Excel(name = "卡状态", readConverterExp = "出=厂默认-1")
    private Integer cardStatus;

    /** 版本 */
    @Excel(name = "版本")
    private Integer cardVersion;

    /** 频点 */
    @Excel(name = "频点")
    private Integer cardFreq;

    /** 更新状态时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新状态时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date statusTime;

    /** （Y启用；N禁用） */
    @Excel(name = "", readConverterExp = "Y=启用；N禁用")
    private String cardEnable;

    /** 是否使用（Y使用中；N未使用） */
    @Excel(name = "是否使用", readConverterExp = "Y=使用中；N未使用")
    private String useStatus;

    /** 卡片传输状态 */
    @Excel(name = "卡片传输状态")
    private Integer cardTransfer;

    /** 心跳时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date heartTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
    }

    public void setCardType(String cardType) 
    {
        this.cardType = cardType;
    }

    public String getCardType() 
    {
        return cardType;
    }

    public void setCardModel(String cardModel) 
    {
        this.cardModel = cardModel;
    }

    public String getCardModel() 
    {
        return cardModel;
    }

    public void setIcCardId(String icCardId) 
    {
        this.icCardId = icCardId;
    }

    public String getIcCardId() 
    {
        return icCardId;
    }

    public void setCardPower(Integer cardPower) 
    {
        this.cardPower = cardPower;
    }

    public Integer getCardPower() 
    {
        return cardPower;
    }

    public void setCardStatus(Integer cardStatus) 
    {
        this.cardStatus = cardStatus;
    }

    public Integer getCardStatus() 
    {
        return cardStatus;
    }

    public void setCardVersion(Integer cardVersion) 
    {
        this.cardVersion = cardVersion;
    }

    public Integer getCardVersion() 
    {
        return cardVersion;
    }

    public void setCardFreq(Integer cardFreq) 
    {
        this.cardFreq = cardFreq;
    }

    public Integer getCardFreq() 
    {
        return cardFreq;
    }

    public void setStatusTime(Date statusTime) 
    {
        this.statusTime = statusTime;
    }

    public Date getStatusTime() 
    {
        return statusTime;
    }

    public void setCardEnable(String cardEnable) 
    {
        this.cardEnable = cardEnable;
    }

    public String getCardEnable() 
    {
        return cardEnable;
    }

    public void setUseStatus(String useStatus) 
    {
        this.useStatus = useStatus;
    }

    public String getUseStatus() 
    {
        return useStatus;
    }

    public void setCardTransfer(Integer cardTransfer) 
    {
        this.cardTransfer = cardTransfer;
    }

    public Integer getCardTransfer() 
    {
        return cardTransfer;
    }

    public void setHeartTime(Date heartTime) 
    {
        this.heartTime = heartTime;
    }

    public Date getHeartTime() 
    {
        return heartTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cardId", getCardId())
            .append("cardType", getCardType())
            .append("cardModel", getCardModel())
            .append("icCardId", getIcCardId())
            .append("cardPower", getCardPower())
            .append("cardStatus", getCardStatus())
            .append("cardVersion", getCardVersion())
            .append("cardFreq", getCardFreq())
            .append("statusTime", getStatusTime())
            .append("cardEnable", getCardEnable())
            .append("useStatus", getUseStatus())
            .append("cardTransfer", getCardTransfer())
            .append("heartTime", getHeartTime())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
