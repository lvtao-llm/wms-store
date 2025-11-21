package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料识别记录对象 wms_material_identify_record
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
public class WmsMaterialIdentifyRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 年月日 */
    @Excel(name = "年月日")
    private String ymd;

    /** 时分秒 */
    @Excel(name = "时分秒")
    private String hms;

    /** 物资编码 */
    @Excel(name = "物资编码")
    private String wzbm;

    /** 物料识别结果 */
    @Excel(name = "物料识别结果")
    private String wlsbjg;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String cph;

    /** 车轴数 */
    @Excel(name = "车轴数")
    private String czs;

    /** 图片1 */
    @Excel(name = "图片1")
    private String img1;

    /** 图片2 */
    @Excel(name = "图片2")
    private String mg2;

    public void setYmd(String ymd) 
    {
        this.ymd = ymd;
    }

    public String getYmd() 
    {
        return ymd;
    }

    public void setHms(String hms) 
    {
        this.hms = hms;
    }

    public String getHms() 
    {
        return hms;
    }

    public void setWzbm(String wzbm) 
    {
        this.wzbm = wzbm;
    }

    public String getWzbm() 
    {
        return wzbm;
    }

    public void setWlsbjg(String wlsbjg) 
    {
        this.wlsbjg = wlsbjg;
    }

    public String getWlsbjg() 
    {
        return wlsbjg;
    }

    public void setCph(String cph) 
    {
        this.cph = cph;
    }

    public String getCph() 
    {
        return cph;
    }

    public void setCzs(String czs) 
    {
        this.czs = czs;
    }

    public String getCzs() 
    {
        return czs;
    }

    public void setImg1(String img1) 
    {
        this.img1 = img1;
    }

    public String getImg1() 
    {
        return img1;
    }

    public void setMg2(String mg2) 
    {
        this.mg2 = mg2;
    }

    public String getMg2() 
    {
        return mg2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ymd", getYmd())
            .append("hms", getHms())
            .append("wzbm", getWzbm())
            .append("wlsbjg", getWlsbjg())
            .append("cph", getCph())
            .append("czs", getCzs())
            .append("img1", getImg1())
            .append("mg2", getMg2())
            .toString();
    }
}
