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
public class WmsMaterialStock extends WmsMaterialDesc
{
    private static final long serialVersionUID = 1L;

    /** 库存编号 */
    @Excel(name = "库存编号")
    private String inventoryId;

    /** 物资编码 */
    @Excel(name = "物资编码")
    private String wzbm;

    /** 实存重量 */
    @Excel(name = "实存重量")
    private Double actualWeight;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 物资名称 */
    @Excel(name = "物资名称")
    private String wzmc;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String jldw;

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
    private String pzmc;

    /** 物资类别 */
    @Excel(name = "物资类别")
    private String wzlb;

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

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public String getWzbm() {
        return wzbm;
    }

    @Override
    public void setWzbm(String wzbm) {
        this.wzbm = wzbm;
    }

    public Double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getWzmc() {
        return wzmc;
    }

    @Override
    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    @Override
    public String getJldw() {
        return jldw;
    }

    @Override
    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Double getBookWeight() {
        return bookWeight;
    }

    public void setBookWeight(Double bookWeight) {
        this.bookWeight = bookWeight;
    }

    public String getVarietyCode() {
        return varietyCode;
    }

    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }

    @Override
    public String getPzmc() {
        return pzmc;
    }

    @Override
    public void setPzmc(String pzmc) {
        this.pzmc = pzmc;
    }

    @Override
    public String getWzlb() {
        return wzlb;
    }

    @Override
    public void setWzlb(String wzlb) {
        this.wzlb = wzlb;
    }

    public Double getReservedWeight() {
        return reservedWeight;
    }

    public void setReservedWeight(Double reservedWeight) {
        this.reservedWeight = reservedWeight;
    }

    public Double getAvailableWeight() {
        return availableWeight;
    }

    public void setAvailableWeight(Double availableWeight) {
        this.availableWeight = availableWeight;
    }

    public String getSquadron() {
        return squadron;
    }

    public void setSquadron(String squadron) {
        this.squadron = squadron;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
