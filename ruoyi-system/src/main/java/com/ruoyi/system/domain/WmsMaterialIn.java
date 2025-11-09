package com.ruoyi.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 接料视图对象 wms_material_in
 *
 * @author ruoyi
 * @date 2025-11-08
 */
public class WmsMaterialIn extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 接料编号
     */
    @Excel(name = "接料编号")
    private String jlBh;

    /**
     * 到货编号
     */
    @Excel(name = "到货编号")
    private String dhBh;

    /**
     * 货票编号
     */
    @Excel(name = "货票编号")
    private String hpBh;

    /**
     * 到货类型
     */
    @Excel(name = "到货类型")
    private String dhLx;

    /**
     * 库房
     */
    @Excel(name = "库房")
    private String kf;

    /**
     * 单据类型
     */
    @Excel(name = "单据类型")
    private String djLx;

    /**
     * 车号
     */
    @Excel(name = "车号")
    private String ch;

    /**
     * 车数
     */
    @Excel(name = "车数")
    private Double cs;

    /**
     * AS运单号
     */
    @Excel(name = "AS运单号")
    private String asYdh;

    /**
     * 发运数量
     */
    @Excel(name = "发运数量")
    private Double fysl;

    /**
     * 到货数量
     */
    @Excel(name = "到货数量")
    private Double dhsl;

    /**
     * 计量员
     */
    @Excel(name = "计量员")
    private String jly;

    /**
     * 计量时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计量时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jlsj;

    /**
     * 厂商名称
     */
    @Excel(name = "厂商名称")
    private String csMc;

    /**
     * 物资编码
     */
    @Excel(name = "物资编码")
    private String wzbm;

    /**
     * 计量类型
     */
    @Excel(name = "计量类型")
    private String jllx;

    /**
     * 交料人
     */
    @Excel(name = "交料人")
    private String jlr;

    /**
     * 交料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交料时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jlsjR;

    /**
     * 保管员
     */
    @Excel(name = "保管员")
    private String bgy;

    /**
     * 中队
     */
    @Excel(name = "中队")
    private String zd;

    /**
     * 料组
     */
    @Excel(name = "料组")
    private String lz;

    /**
     * 接料时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接料时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jlsjT;

    /**
     * 验收人
     */
    @Excel(name = "验收人")
    private String ysr;

    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "验收时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date yssj;

    /**
     * 稽核人
     */
    @Excel(name = "稽核人")
    private String jhr;

    /**
     * 稽核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "稽核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jhsj;

    /**
     * 到库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dksj;

    /**
     * 发运时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发运时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date fysj;

    /**
     * 发运方式
     */
    @Excel(name = "发运方式")
    private String fyfs;

    /**
     * 发站
     */
    @Excel(name = "发站")
    private String fz;

    /**
     * 到站
     */
    @Excel(name = "到站")
    private String dz;

    /**
     * 业务年月
     */
    @Excel(name = "业务年月")
    private String yynY;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String zt;

    /**
     * 卸车编号
     */
    @Excel(name = "卸车编号")
    private String xcbh;

    /**
     * 品种编码
     */
    @Excel(name = "品种编码")
    private String pzbm;

    /**
     * 物资名称
     */
    @Excel(name = "物资名称")
    private String wzmc;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    private String jldw;

    /**
     * 物资类别
     */
    @Excel(name = "物资类别")
    private String wzlb;

    /**
     * 验收人编码
     */
    @Excel(name = "验收人编码")
    private String ysrBm;

    /**
     * 接料人编码
     */
    @Excel(name = "接料人编码")
    private String jlrBm;

    /**
     * 品种名称
     */
    @Excel(name = "品种名称")
    private String pzmc;

    /**
     * 业务分公司
     */
    @Excel(name = "业务分公司")
    private String ywfgs;

    /**
     * 装卸队名称
     */
    @Excel(name = "装卸队名称")
    private String zxzdMc;

    /**
     * 卸车方式
     */
    @Excel(name = "卸车方式")
    private String xcfs;

    /**
     * 卸车作业起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卸车作业起始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date xczysjQ;

    /**
     * 卸车作业截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卸车作业截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date xczysjZ;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cksj;

    /**
     * 现场员姓名
     */
    @Excel(name = "现场员姓名")
    private String xcyxm;

    public void setMaterialDescId(Long id) {

    }

    public void setJlBh(String jlBh) {
        this.jlBh = jlBh;
    }

    public String getJlBh() {
        return jlBh;
    }

    public void setDhBh(String dhBh) {
        this.dhBh = dhBh;
    }

    public String getDhBh() {
        return dhBh;
    }

    public void setHpBh(String hpBh) {
        this.hpBh = hpBh;
    }

    public String getHpBh() {
        return hpBh;
    }

    public void setDhLx(String dhLx) {
        this.dhLx = dhLx;
    }

    public String getDhLx() {
        return dhLx;
    }

    public void setKf(String kf) {
        this.kf = kf;
    }

    public String getKf() {
        return kf;
    }

    public void setDjLx(String djLx) {
        this.djLx = djLx;
    }

    public String getDjLx() {
        return djLx;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getCh() {
        return ch;
    }

    public void setCs(Double cs) {
        this.cs = cs;
    }

    public Double getCs() {
        return cs;
    }

    public void setAsYdh(String asYdh) {
        this.asYdh = asYdh;
    }

    public String getAsYdh() {
        return asYdh;
    }

    public void setFysl(Double fysl) {
        this.fysl = fysl;
    }

    public Double getFysl() {
        return fysl;
    }

    public void setDhsl(Double dhsl) {
        this.dhsl = dhsl;
    }

    public Double getDhsl() {
        return dhsl;
    }

    public void setJly(String jly) {
        this.jly = jly;
    }

    public String getJly() {
        return jly;
    }

    public void setJlsj(Date jlsj) {
        this.jlsj = jlsj;
    }

    public Date getJlsj() {
        return jlsj;
    }

    public void setCsMc(String csMc) {
        this.csMc = csMc;
    }

    public String getCsMc() {
        return csMc;
    }

    public void setWzbm(String wzbm) {
        this.wzbm = wzbm;
    }

    public String getWzbm() {
        return wzbm;
    }

    public void setJllx(String jllx) {
        this.jllx = jllx;
    }

    public String getJllx() {
        return jllx;
    }

    public void setJlr(String jlr) {
        this.jlr = jlr;
    }

    public String getJlr() {
        return jlr;
    }

    public void setJlsjR(Date jlsjR) {
        this.jlsjR = jlsjR;
    }

    public Date getJlsjR() {
        return jlsjR;
    }

    public void setBgy(String bgy) {
        this.bgy = bgy;
    }

    public String getBgy() {
        return bgy;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getZd() {
        return zd;
    }

    public void setLz(String lz) {
        this.lz = lz;
    }

    public String getLz() {
        return lz;
    }

    public void setJlsjT(Date jlsjT) {
        this.jlsjT = jlsjT;
    }

    public Date getJlsjT() {
        return jlsjT;
    }

    public void setYsr(String ysr) {
        this.ysr = ysr;
    }

    public String getYsr() {
        return ysr;
    }

    public void setYssj(Date yssj) {
        this.yssj = yssj;
    }

    public Date getYssj() {
        return yssj;
    }

    public void setJhr(String jhr) {
        this.jhr = jhr;
    }

    public String getJhr() {
        return jhr;
    }

    public void setJhsj(Date jhsj) {
        this.jhsj = jhsj;
    }

    public Date getJhsj() {
        return jhsj;
    }

    public void setDksj(Date dksj) {
        this.dksj = dksj;
    }

    public Date getDksj() {
        return dksj;
    }

    public void setFysj(Date fysj) {
        this.fysj = fysj;
    }

    public Date getFysj() {
        return fysj;
    }

    public void setFyfs(String fyfs) {
        this.fyfs = fyfs;
    }

    public String getFyfs() {
        return fyfs;
    }

    public void setFz(String fz) {
        this.fz = fz;
    }

    public String getFz() {
        return fz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDz() {
        return dz;
    }

    public void setYynY(String yynY) {
        this.yynY = yynY;
    }

    public String getYynY() {
        return yynY;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getZt() {
        return zt;
    }

    public void setXcbh(String xcbh) {
        this.xcbh = xcbh;
    }

    public String getXcbh() {
        return xcbh;
    }

    public void setPzbm(String pzbm) {
        this.pzbm = pzbm;
    }

    public String getPzbm() {
        return pzbm;
    }

    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    public String getWzmc() {
        return wzmc;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getJldw() {
        return jldw;
    }

    public void setWzlb(String wzlb) {
        this.wzlb = wzlb;
    }

    public String getWzlb() {
        return wzlb;
    }

    public void setYsrBm(String ysrBm) {
        this.ysrBm = ysrBm;
    }

    public String getYsrBm() {
        return ysrBm;
    }

    public void setJlrBm(String jlrBm) {
        this.jlrBm = jlrBm;
    }

    public String getJlrBm() {
        return jlrBm;
    }

    public void setPzmc(String pzmc) {
        this.pzmc = pzmc;
    }

    public String getPzmc() {
        return pzmc;
    }

    public void setYwfgs(String ywfgs) {
        this.ywfgs = ywfgs;
    }

    public String getYwfgs() {
        return ywfgs;
    }

    public void setZxzdMc(String zxzdMc) {
        this.zxzdMc = zxzdMc;
    }

    public String getZxzdMc() {
        return zxzdMc;
    }

    public void setXcfs(String xcfs) {
        this.xcfs = xcfs;
    }

    public String getXcfs() {
        return xcfs;
    }

    public void setXczysjQ(Date xczysjQ) {
        this.xczysjQ = xczysjQ;
    }

    public Date getXczysjQ() {
        return xczysjQ;
    }

    public void setXczysjZ(Date xczysjZ) {
        this.xczysjZ = xczysjZ;
    }

    public Date getXczysjZ() {
        return xczysjZ;
    }

    public void setCksj(Date cksj) {
        this.cksj = cksj;
    }

    public Date getCksj() {
        return cksj;
    }

    public void setXcyxm(String xcyxm) {
        this.xcyxm = xcyxm;
    }

    public String getXcyxm() {
        return xcyxm;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("jlBh", getJlBh())
                .append("dhBh", getDhBh())
                .append("hpBh", getHpBh())
                .append("dhLx", getDhLx())
                .append("kf", getKf())
                .append("djLx", getDjLx())
                .append("ch", getCh())
                .append("cs", getCs())
                .append("asYdh", getAsYdh())
                .append("fysl", getFysl())
                .append("dhsl", getDhsl())
                .append("jly", getJly())
                .append("jlsj", getJlsj())
                .append("csMc", getCsMc())
                .append("wzbm", getWzbm())
                .append("jllx", getJllx())
                .append("jlr", getJlr())
                .append("jlsjR", getJlsjR())
                .append("bgy", getBgy())
                .append("zd", getZd())
                .append("lz", getLz())
                .append("jlsjT", getJlsjT())
                .append("ysr", getYsr())
                .append("yssj", getYssj())
                .append("jhr", getJhr())
                .append("jhsj", getJhsj())
                .append("dksj", getDksj())
                .append("fysj", getFysj())
                .append("fyfs", getFyfs())
                .append("fz", getFz())
                .append("dz", getDz())
                .append("yynY", getYynY())
                .append("zt", getZt())
                .append("xcbh", getXcbh())
                .append("pzbm", getPzbm())
                .append("wzmc", getWzmc())
                .append("jldw", getJldw())
                .append("wzlb", getWzlb())
                .append("ysrBm", getYsrBm())
                .append("jlrBm", getJlrBm())
                .append("pzmc", getPzmc())
                .append("ywfgs", getYwfgs())
                .append("zxzdMc", getZxzdMc())
                .append("xcfs", getXcfs())
                .append("xczysjQ", getXczysjQ())
                .append("xczysjZ", getXczysjZ())
                .append("cksj", getCksj())
                .append("xcyxm", getXcyxm())
                .toString();
    }
}
