package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料描述档案对象 wms_material_desc
 * 
 * @author ruoyi
 * @date 2025-11-07
 */
public class WmsMaterialDesc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long materialDescId;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格 */
    @Excel(name = "规格")
    private String materialSpec;

    /** 型号 */
    @Excel(name = "型号")
    private String materialModel;

    /** 存放区域 */
    @Excel(name = "存放区域")
    private String areaCodes;

    /** 单位 */
    @Excel(name = "单位")
    private String materialUnit;

    /** 存放条件 */
    @Excel(name = "存放条件")
    private String materialRequirements;

    /** 安全注意事项 */
    @Excel(name = "安全注意事项")
    private String materialSafetyNotice;

    /** 关键点位 */
    @Excel(name = "关键点位")
    private String keyPoints;

    public void setMaterialDescId(Long materialDescId) 
    {
        this.materialDescId = materialDescId;
    }

    public Long getMaterialDescId() 
    {
        return materialDescId;
    }

    public void setMaterialCode(String materialCode) 
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() 
    {
        return materialCode;
    }

    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }

    public void setMaterialSpec(String materialSpec) 
    {
        this.materialSpec = materialSpec;
    }

    public String getMaterialSpec() 
    {
        return materialSpec;
    }

    public void setMaterialModel(String materialModel) 
    {
        this.materialModel = materialModel;
    }

    public String getMaterialModel() 
    {
        return materialModel;
    }

    public void setAreaCodes(String areaCodes) 
    {
        this.areaCodes = areaCodes;
    }

    public String getAreaCodes() 
    {
        return areaCodes;
    }

    public void setMaterialUnit(String materialUnit) 
    {
        this.materialUnit = materialUnit;
    }

    public String getMaterialUnit() 
    {
        return materialUnit;
    }

    public void setMaterialRequirements(String materialRequirements) 
    {
        this.materialRequirements = materialRequirements;
    }

    public String getMaterialRequirements() 
    {
        return materialRequirements;
    }

    public void setMaterialSafetyNotice(String materialSafetyNotice) 
    {
        this.materialSafetyNotice = materialSafetyNotice;
    }

    public String getMaterialSafetyNotice() 
    {
        return materialSafetyNotice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("materialDescId", getMaterialDescId())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("materialSpec", getMaterialSpec())
            .append("materialModel", getMaterialModel())
            .append("areaCodes", getAreaCodes())
            .append("materialUnit", getMaterialUnit())
            .append("materialRequirements", getMaterialRequirements())
            .append("materialSafetyNotice", getMaterialSafetyNotice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }

    public String getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(String keyPoints) {
        this.keyPoints = keyPoints;
    }
}
