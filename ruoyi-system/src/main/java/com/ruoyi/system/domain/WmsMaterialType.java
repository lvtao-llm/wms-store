package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料类型对象 wms_material_type
 * 
 * @author ruoyi
 * @date 2026-01-20
 */
public class WmsMaterialType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String bm;

    /** 目录名称 */
    @Excel(name = "目录名称")
    private String mc;

    /** 区域编码 */
    @Excel(name = "区域编码")
    private String areaCode;

    /** $column.columnComment */
    private Long id;

    public void setBm(String bm) 
    {
        this.bm = bm;
    }

    public String getBm() 
    {
        return bm;
    }

    public void setMc(String mc) 
    {
        this.mc = mc;
    }

    public String getMc() 
    {
        return mc;
    }

    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bm", getBm())
            .append("mc", getMc())
            .append("areaCode", getAreaCode())
            .append("id", getId())
            .toString();
    }
}
