package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料日统计对象 wms_material_statics_day
 * 
 * @author ruoyi
 * @date 2025-11-09
 */
public class WmsMaterialStaticsDay extends WmsMaterialDesc
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String day;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String wzbm;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String wzmc;

    /** 规格 */
    @Excel(name = "品种名称")
    private String pzmc;

    /** 型号 */
    @Excel(name = "物资类别")
    private String wzlb;

    /** 存放区域 */
    @Excel(name = "存放区域")
    private String areaCodes;

    /** 单位 */
    @Excel(name = "计量单位")
    private String jldw;

    /** 接料 */
    @Excel(name = "接料")
    private Double jl;

    /** 调拨 */
    @Excel(name = "调拨")
    private Double db;

    /** 库存 */
    @Excel(name = "库存")
    private Double kc;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String getWzbm() {
        return wzbm;
    }

    @Override
    public void setWzbm(String wzbm) {
        this.wzbm = wzbm;
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

    @Override
    public String getAreaCodes() {
        return areaCodes;
    }

    @Override
    public void setAreaCodes(String areaCodes) {
        this.areaCodes = areaCodes;
    }

    @Override
    public String getJldw() {
        return jldw;
    }

    @Override
    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public Double getJl() {
        return jl;
    }

    public void setJl(Double jl) {
        this.jl = jl;
    }

    public Double getDb() {
        return db;
    }

    public void setDb(Double db) {
        this.db = db;
    }

    public Double getKc() {
        return kc;
    }

    public void setKc(Double kc) {
        this.kc = kc;
    }
}
