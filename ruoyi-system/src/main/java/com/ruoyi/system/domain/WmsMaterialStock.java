package com.ruoyi.system.domain;

import java.lang.Double;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 库存视图对象 wms_material_stock
 * 
 * @author ruoyi
 * @date 2025-11-08
 */
public class WmsMaterialStock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存编号 */
    @Excel(name = "库存编号")
    private String inventoryId;

    /** 物资编码 */
    @Excel(name = "物资编码")
    private String materialCode;

    /** 实存重量 */
    @Excel(name = "实存重量")
    private Double actualWeight;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 物资名称 */
    @Excel(name = "物资名称")
    private String materialName;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 库房 */
    @Excel(name = "库房")
    private String warehouse;

    /** 账存重量 */
    @Excel(name = "账存重量")
    private Double bookWeight;

    /** 品种编码 */
    @Excel(name = "品种编码")
    private String varietyCode;

    /** 品种名称 */
    @Excel(name = "品种名称")
    private String varietyName;

    /** 物资类别 */
    @Excel(name = "物资类别")
    private String materialCategory;

    /** 预拨重量 */
    @Excel(name = "预拨重量")
    private Double reservedWeight;

    /** 可拨重量 */
    @Excel(name = "可拨重量")
    private Double availableWeight;

    /** 中队 */
    @Excel(name = "中队")
    private String squadron;

    /** 班组 */
    @Excel(name = "班组")
    private String team;

    /** 所属公司 */
    @Excel(name = "所属公司")
    private String company;

    public void setInventoryId(String inventoryId) 
    {
        this.inventoryId = inventoryId;
    }

    public String getInventoryId() 
    {
        return inventoryId;
    }

    public void setMaterialCode(String materialCode) 
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() 
    {
        return materialCode;
    }

    public void setActualWeight(Double actualWeight)
    {
        this.actualWeight = actualWeight;
    }

    public Double getActualWeight() 
    {
        return actualWeight;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    public void setWarehouse(String warehouse) 
    {
        this.warehouse = warehouse;
    }

    public String getWarehouse() 
    {
        return warehouse;
    }

    public void setBookWeight(Double bookWeight) 
    {
        this.bookWeight = bookWeight;
    }

    public Double getBookWeight() 
    {
        return bookWeight;
    }

    public void setVarietyCode(String varietyCode) 
    {
        this.varietyCode = varietyCode;
    }

    public String getVarietyCode() 
    {
        return varietyCode;
    }

    public void setVarietyName(String varietyName) 
    {
        this.varietyName = varietyName;
    }

    public String getVarietyName() 
    {
        return varietyName;
    }

    public void setMaterialCategory(String materialCategory) 
    {
        this.materialCategory = materialCategory;
    }

    public String getMaterialCategory() 
    {
        return materialCategory;
    }

    public void setReservedWeight(Double reservedWeight) 
    {
        this.reservedWeight = reservedWeight;
    }

    public Double getReservedWeight() 
    {
        return reservedWeight;
    }

    public void setAvailableWeight(Double availableWeight) 
    {
        this.availableWeight = availableWeight;
    }

    public Double getAvailableWeight() 
    {
        return availableWeight;
    }

    public void setSquadron(String squadron) 
    {
        this.squadron = squadron;
    }

    public String getSquadron() 
    {
        return squadron;
    }

    public void setTeam(String team) 
    {
        this.team = team;
    }

    public String getTeam() 
    {
        return team;
    }

    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getCompany() 
    {
        return company;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("inventoryId", getInventoryId())
            .append("materialCode", getMaterialCode())
            .append("actualWeight", getActualWeight())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("materialName", getMaterialName())
            .append("unit", getUnit())
            .append("warehouse", getWarehouse())
            .append("bookWeight", getBookWeight())
            .append("varietyCode", getVarietyCode())
            .append("varietyName", getVarietyName())
            .append("materialCategory", getMaterialCategory())
            .append("reservedWeight", getReservedWeight())
            .append("availableWeight", getAvailableWeight())
            .append("squadron", getSquadron())
            .append("team", getTeam())
            .append("company", getCompany())
            .toString();
    }
}
