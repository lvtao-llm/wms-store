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
public class WmsMaterialDesc extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long materialDescId;

    /**
     * 物料编码
     */
    @Excel(name = "物资编码")
    private String wzbm;

    /**
     * 物料名称
     */
    @Excel(name = "物资名称")
    private String wzmc;

    /**
     * 规格
     */
    @Excel(name = "品种名称")
    private String pzmc;

    /**
     * 型号
     */
    @Excel(name = "物资类别")
    private String wzlb;

    /**
     * 存放区域
     */
    @Excel(name = "存放区域")
    private String areaCodes;

    /**
     * 单位
     */
    @Excel(name = "计量单位")
    private String jldw;

    /**
     * 存放条件
     */
    @Excel(name = "存放条件")
    private String materialRequirements;

    /**
     * 安全注意事项
     */
    @Excel(name = "安全注意事项")
    private String materialSafetyNotice;

    /**
     * 关键点位
     */
    @Excel(name = "关键点位")
    private String keyPoints;

    /**
     * 实际重量
     */
    @Excel(name = "实际重量")
    private Double actualWeight;

    public Long getMaterialDescId() {
        return materialDescId;
    }

    public void setMaterialDescId(Long materialDescId) {
        this.materialDescId = materialDescId;
    }

    public String getWzbm() {
        return wzbm;
    }

    public void setWzbm(String wzbm) {
        this.wzbm = wzbm;
    }

    public String getWzmc() {
        return wzmc;
    }

    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    public String getPzmc() {
        return pzmc;
    }

    public void setPzmc(String pzmc) {
        this.pzmc = pzmc;
    }

    public String getWzlb() {
        return wzlb;
    }

    public void setWzlb(String wzlb) {
        this.wzlb = wzlb;
    }

    public String getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String areaCodes) {
        this.areaCodes = areaCodes;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getMaterialRequirements() {
        return materialRequirements;
    }

    public void setMaterialRequirements(String materialRequirements) {
        this.materialRequirements = materialRequirements;
    }

    public String getMaterialSafetyNotice() {
        return materialSafetyNotice;
    }

    public void setMaterialSafetyNotice(String materialSafetyNotice) {
        this.materialSafetyNotice = materialSafetyNotice;
    }

    public String getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(String keyPoints) {
        this.keyPoints = keyPoints;
    }

    public Double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Double actualWeight) {
        this.actualWeight = actualWeight;
    }
}
