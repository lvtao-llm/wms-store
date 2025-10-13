package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料档案对象 wms_material
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class WmsMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long materialId;

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

    /** 批次 */
    @Excel(name = "批次")
    private String materialBatchNo;

    /** 存放区域 */
    @Excel(name = "存放区域")
    private String areaCode;

    /** 库存件数 */
    @Excel(name = "库存件数")
    private Long materialStockQty;

    /** 变更的上一记录 */
    @Excel(name = "变更的上一记录")
    private Long materialChangePrevId;

    /** 变更的下一记录 */
    @Excel(name = "变更的下一记录")
    private Long materialChangeNextId;

    /** 是否是变更记录：0初始入库，1变更记录 */
    @Excel(name = "是否是变更记录：0初始入库，1变更记录")
    private String materialIsChange;

    /** 变更类型：0入库，1出库，2：借入更正，3：贷出更正 */
    @Excel(name = "变更类型：0入库，1出库，2：借入更正，3：贷出更正")
    private String materialChangeType;

    /** 变更原因 */
    @Excel(name = "变更原因")
    private String materialChangeCause;

    /** 单位 */
    @Excel(name = "单位")
    private String materialUnit;

    /** 存放位置：json{x,y} */
    @Excel(name = "存放位置：json{x,y}")
    private String materialPos;

    /** 存放条件 */
    @Excel(name = "存放条件")
    private String materialRequirements;

    /** 安全注意事项 */
    @Excel(name = "安全注意事项")
    private String materialSafetyNotice;

    /** 物料照片 */
    @Excel(name = "物料照片")
    private String materialPhoto;

    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
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

    public void setMaterialBatchNo(String materialBatchNo) 
    {
        this.materialBatchNo = materialBatchNo;
    }

    public String getMaterialBatchNo() 
    {
        return materialBatchNo;
    }

    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }

    public void setMaterialStockQty(Long materialStockQty) 
    {
        this.materialStockQty = materialStockQty;
    }

    public Long getMaterialStockQty() 
    {
        return materialStockQty;
    }

    public void setMaterialChangePrevId(Long materialChangePrevId) 
    {
        this.materialChangePrevId = materialChangePrevId;
    }

    public Long getMaterialChangePrevId() 
    {
        return materialChangePrevId;
    }

    public void setMaterialChangeNextId(Long materialChangeNextId) 
    {
        this.materialChangeNextId = materialChangeNextId;
    }

    public Long getMaterialChangeNextId() 
    {
        return materialChangeNextId;
    }

    public void setMaterialIsChange(String materialIsChange) 
    {
        this.materialIsChange = materialIsChange;
    }

    public String getMaterialIsChange() 
    {
        return materialIsChange;
    }

    public void setMaterialChangeType(String materialChangeType) 
    {
        this.materialChangeType = materialChangeType;
    }

    public String getMaterialChangeType() 
    {
        return materialChangeType;
    }

    public void setMaterialChangeCause(String materialChangeCause) 
    {
        this.materialChangeCause = materialChangeCause;
    }

    public String getMaterialChangeCause() 
    {
        return materialChangeCause;
    }

    public void setMaterialUnit(String materialUnit) 
    {
        this.materialUnit = materialUnit;
    }

    public String getMaterialUnit() 
    {
        return materialUnit;
    }

    public void setMaterialPos(String materialPos) 
    {
        this.materialPos = materialPos;
    }

    public String getMaterialPos() 
    {
        return materialPos;
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

    public void setMaterialPhoto(String materialPhoto) 
    {
        this.materialPhoto = materialPhoto;
    }

    public String getMaterialPhoto() 
    {
        return materialPhoto;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("materialId", getMaterialId())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("materialSpec", getMaterialSpec())
            .append("materialModel", getMaterialModel())
            .append("materialBatchNo", getMaterialBatchNo())
            .append("areaCode", getAreaCode())
            .append("materialStockQty", getMaterialStockQty())
            .append("materialChangePrevId", getMaterialChangePrevId())
            .append("materialChangeNextId", getMaterialChangeNextId())
            .append("materialIsChange", getMaterialIsChange())
            .append("materialChangeType", getMaterialChangeType())
            .append("materialChangeCause", getMaterialChangeCause())
            .append("materialUnit", getMaterialUnit())
            .append("materialPos", getMaterialPos())
            .append("materialRequirements", getMaterialRequirements())
            .append("materialSafetyNotice", getMaterialSafetyNotice())
            .append("materialPhoto", getMaterialPhoto())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
