package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 发卡记录对象 wms_card_record
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@ApiModel("发卡记录")
public class WmsCardRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private Long cardRecordId;

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
            .append("cardRecordId", getCardRecordId())
            .toString();
    }
}
