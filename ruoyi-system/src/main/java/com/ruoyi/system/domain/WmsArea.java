package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 区域对象 wms_area
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class WmsArea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 区域ID */
    private Long areaId;

    /** 区域编码 */
    @Excel(name = "区域编码")
    private String areaCode;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String areaName;

    /** 1库区 2料场 3办公 4作业 5高风险 */
    @Excel(name = "1库区 2料场 3办公 4作业 5高风险")
    private String areaType;

    /** 区域功能 */
    @Excel(name = "区域功能")
    private String areaFunction;

    /** 安全提示 */
    @Excel(name = "安全提示")
    private String areaSafetyNotice;

    /** 多边形边界JSON */
    @Excel(name = "多边形边界JSON")
    private String areaPolygon;

    /** 风险等级 0低 1中 2高 */
    @Excel(name = "风险等级 0低 1中 2高")
    private String areaRiskLevel;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private String enabled;

    /** 删除标志 */
    private String delFlag;

    public void setAreaId(Long areaId) 
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }

    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }

    public void setAreaName(String areaName) 
    {
        this.areaName = areaName;
    }

    public String getAreaName() 
    {
        return areaName;
    }

    public void setAreaType(String areaType) 
    {
        this.areaType = areaType;
    }

    public String getAreaType() 
    {
        return areaType;
    }

    public void setAreaFunction(String areaFunction) 
    {
        this.areaFunction = areaFunction;
    }

    public String getAreaFunction() 
    {
        return areaFunction;
    }

    public void setAreaSafetyNotice(String areaSafetyNotice) 
    {
        this.areaSafetyNotice = areaSafetyNotice;
    }

    public String getAreaSafetyNotice() 
    {
        return areaSafetyNotice;
    }

    public void setAreaPolygon(String areaPolygon) 
    {
        this.areaPolygon = areaPolygon;
    }

    public String getAreaPolygon() 
    {
        return areaPolygon;
    }

    public void setAreaRiskLevel(String areaRiskLevel) 
    {
        this.areaRiskLevel = areaRiskLevel;
    }

    public String getAreaRiskLevel() 
    {
        return areaRiskLevel;
    }

    public void setEnabled(String enabled) 
    {
        this.enabled = enabled;
    }

    public String getEnabled() 
    {
        return enabled;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("areaId", getAreaId())
            .append("areaCode", getAreaCode())
            .append("areaName", getAreaName())
            .append("areaType", getAreaType())
            .append("areaFunction", getAreaFunction())
            .append("areaSafetyNotice", getAreaSafetyNotice())
            .append("areaPolygon", getAreaPolygon())
            .append("areaRiskLevel", getAreaRiskLevel())
            .append("enabled", getEnabled())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
