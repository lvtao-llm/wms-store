//package com.ruoyi.system.domain;
//
//import java.util.Date;
//
///**
// * @author 吕涛
// * @version 1.0
// * @since 2025/11/8
// */
//public class WzjtViewJlSk {
//    private String jlBh; // 接料编号
//    private String dhBh; // 到货编号
//    private String hpBh; // 货票编号
//    private String dhLx; // 到货类型
//    private String kf;   // 库房
//    private String djLx; // 单据类型
//    private String ch;   // 车号
//    private Double cs; // 车数
//    private String asYdh; // AS运单号
//    private Double fysl; // 发运数量
//    private Double dhsl; // 到货数量
//    private String jly;  // 计量员
//    private Date jlsj;   // 计量时间
//    private String csMc; // 厂商名称
//    private String wzbm; // 物资编码
//    private String jllx; // 计量类型
//    private String jlr;  // 交料人
//    private Date jlsjR;  // 交料时间
//    private String bgy;  // 保管员
//    private String zd;   // 中队
//    private String lz;   // 料组
//    private Date jlsjT;  // 接料时间
//    private String ysr;  // 验收人
//    private Date yssj;   // 验收时间
//    private String jhr;  // 稽核人
//    private Date jhsj;   // 稽核时间
//    private Date dksj;   // 到库时间
//    private Date fysj;   // 发运时间
//    private String fyfs; // 发运方式
//    private String fz;   // 发站
//    private String dz;   // 到站
//    private String yynY; // 业务年月
//    private String zt;   // 状态
//    private String xcbh; // 卸车编号
//    private String pzbm; // 品种编码
//    private String wzmc; // 物资名称
//    private String jldw; // 计量单位
//    private String wzlb; // 物资类别
//    private String ysrBm;// 验收人编码
//    private String jlrBm;// 接料人编码
//    private String pzmc; // 品种名称
//    private String ywfgs;// 业务分公司
//    private String zxzdMc;// 装卸队名称
//    private String xcfs; // 卸车方式
//    private Date xczysjQ;// 卸车作业起始时间
//    private Date xczysjZ;// 卸车作业截止时间
//    private Date cksj;   // 出库时间
//    private String xcyxm; // 现场员姓名
//
//    public String getJlBh() {
//        return jlBh;
//    }
//
//    public void setJlBh(String jlBh) {
//        this.jlBh = jlBh;
//    }
//
//    public String getDhBh() {
//        return dhBh;
//    }
//
//    public void setDhBh(String dhBh) {
//        this.dhBh = dhBh;
//    }
//
//    public String getHpBh() {
//        return hpBh;
//    }
//
//    public void setHpBh(String hpBh) {
//        this.hpBh = hpBh;
//    }
//
//    public String getDhLx() {
//        return dhLx;
//    }
//
//    public void setDhLx(String dhLx) {
//        this.dhLx = dhLx;
//    }
//
//    public String getKf() {
//        return kf;
//    }
//
//    public void setKf(String kf) {
//        this.kf = kf;
//    }
//
//    public String getDjLx() {
//        return djLx;
//    }
//
//    public void setDjLx(String djLx) {
//        this.djLx = djLx;
//    }
//
//    public String getCh() {
//        return ch;
//    }
//
//    public void setCh(String ch) {
//        this.ch = ch;
//    }
//
//    public Double getCs() {
//        return cs;
//    }
//
//    public void setCs(Double cs) {
//        this.cs = cs;
//    }
//
//    public String getAsYdh() {
//        return asYdh;
//    }
//
//    public void setAsYdh(String asYdh) {
//        this.asYdh = asYdh;
//    }
//
//    public Double getFysl() {
//        return fysl;
//    }
//
//    public void setFysl(Double fysl) {
//        this.fysl = fysl;
//    }
//
//    public Double getDhsl() {
//        return dhsl;
//    }
//
//    public void setDhsl(Double dhsl) {
//        this.dhsl = dhsl;
//    }
//
//    public String getJly() {
//        return jly;
//    }
//
//    public void setJly(String jly) {
//        this.jly = jly;
//    }
//
//    public Date getJlsj() {
//        return jlsj;
//    }
//
//    public void setJlsj(Date jlsj) {
//        this.jlsj = jlsj;
//    }
//
//    public String getCsMc() {
//        return csMc;
//    }
//
//    public void setCsMc(String csMc) {
//        this.csMc = csMc;
//    }
//
//    public String getWzbm() {
//        return wzbm;
//    }
//
//    public void setWzbm(String wzbm) {
//        this.wzbm = wzbm;
//    }
//
//    public String getJllx() {
//        return jllx;
//    }
//
//    public void setJllx(String jllx) {
//        this.jllx = jllx;
//    }
//
//    public String getJlr() {
//        return jlr;
//    }
//
//    public void setJlr(String jlr) {
//        this.jlr = jlr;
//    }
//
//    public Date getJlsjR() {
//        return jlsjR;
//    }
//
//    public void setJlsjR(Date jlsjR) {
//        this.jlsjR = jlsjR;
//    }
//
//    public String getBgy() {
//        return bgy;
//    }
//
//    public void setBgy(String bgy) {
//        this.bgy = bgy;
//    }
//
//    public String getZd() {
//        return zd;
//    }
//
//    public void setZd(String zd) {
//        this.zd = zd;
//    }
//
//    public String getLz() {
//        return lz;
//    }
//
//    public void setLz(String lz) {
//        this.lz = lz;
//    }
//
//    public Date getJlsjT() {
//        return jlsjT;
//    }
//
//    public void setJlsjT(Date jlsjT) {
//        this.jlsjT = jlsjT;
//    }
//
//    public String getYsr() {
//        return ysr;
//    }
//
//    public void setYsr(String ysr) {
//        this.ysr = ysr;
//    }
//
//    public Date getYssj() {
//        return yssj;
//    }
//
//    public void setYssj(Date yssj) {
//        this.yssj = yssj;
//    }
//
//    public String getJhr() {
//        return jhr;
//    }
//
//    public void setJhr(String jhr) {
//        this.jhr = jhr;
//    }
//
//    public Date getJhsj() {
//        return jhsj;
//    }
//
//    public void setJhsj(Date jhsj) {
//        this.jhsj = jhsj;
//    }
//
//    public Date getDksj() {
//        return dksj;
//    }
//
//    public void setDksj(Date dksj) {
//        this.dksj = dksj;
//    }
//
//    public Date getFysj() {
//        return fysj;
//    }
//
//    public void setFysj(Date fysj) {
//        this.fysj = fysj;
//    }
//
//    public String getFyfs() {
//        return fyfs;
//    }
//
//    public void setFyfs(String fyfs) {
//        this.fyfs = fyfs;
//    }
//
//    public String getFz() {
//        return fz;
//    }
//
//    public void setFz(String fz) {
//        this.fz = fz;
//    }
//
//    public String getDz() {
//        return dz;
//    }
//
//    public void setDz(String dz) {
//        this.dz = dz;
//    }
//
//    public String getYynY() {
//        return yynY;
//    }
//
//    public void setYynY(String yynY) {
//        this.yynY = yynY;
//    }
//
//    public String getZt() {
//        return zt;
//    }
//
//    public void setZt(String zt) {
//        this.zt = zt;
//    }
//
//    public String getXcbh() {
//        return xcbh;
//    }
//
//    public void setXcbh(String xcbh) {
//        this.xcbh = xcbh;
//    }
//
//    public String getPzbm() {
//        return pzbm;
//    }
//
//    public void setPzbm(String pzbm) {
//        this.pzbm = pzbm;
//    }
//
//    public String getWzmc() {
//        return wzmc;
//    }
//
//    public void setWzmc(String wzmc) {
//        this.wzmc = wzmc;
//    }
//
//    public String getJldw() {
//        return jldw;
//    }
//
//    public void setJldw(String jldw) {
//        this.jldw = jldw;
//    }
//
//    public String getWzlb() {
//        return wzlb;
//    }
//
//    public void setWzlb(String wzlb) {
//        this.wzlb = wzlb;
//    }
//
//    public String getYsrBm() {
//        return ysrBm;
//    }
//
//    public void setYsrBm(String ysrBm) {
//        this.ysrBm = ysrBm;
//    }
//
//    public String getJlrBm() {
//        return jlrBm;
//    }
//
//    public void setJlrBm(String jlrBm) {
//        this.jlrBm = jlrBm;
//    }
//
//    public String getPzmc() {
//        return pzmc;
//    }
//
//    public void setPzmc(String pzmc) {
//        this.pzmc = pzmc;
//    }
//
//    public String getYwfgs() {
//        return ywfgs;
//    }
//
//    public void setYwfgs(String ywfgs) {
//        this.ywfgs = ywfgs;
//    }
//
//    public String getZxzdMc() {
//        return zxzdMc;
//    }
//
//    public void setZxzdMc(String zxzdMc) {
//        this.zxzdMc = zxzdMc;
//    }
//
//    public String getXcfs() {
//        return xcfs;
//    }
//
//    public void setXcfs(String xcfs) {
//        this.xcfs = xcfs;
//    }
//
//    public Date getXczysjQ() {
//        return xczysjQ;
//    }
//
//    public void setXczysjQ(Date xczysjQ) {
//        this.xczysjQ = xczysjQ;
//    }
//
//    public Date getXczysjZ() {
//        return xczysjZ;
//    }
//
//    public void setXczysjZ(Date xczysjZ) {
//        this.xczysjZ = xczysjZ;
//    }
//
//    public Date getCksj() {
//        return cksj;
//    }
//
//    public void setCksj(Date cksj) {
//        this.cksj = cksj;
//    }
//
//    public String getXcyxm() {
//        return xcyxm;
//    }
//
//    public void setXcyxm(String xcyxm) {
//        this.xcyxm = xcyxm;
//    }
//}
