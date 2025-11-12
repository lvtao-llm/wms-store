package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 摄像头识别日志对象 wms_device_camera_log
 * 
 * @author ruoyi
 * @date 2025-11-11
 */
public class WmsDeviceCameraLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 类别 */
    @Excel(name = "类别")
    private String type;

    /** 点位名称 */
    @Excel(name = "点位名称")
    private String dwmc;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sj;

    /** 是否有车 */
    @Excel(name = "是否有车")
    private String yc;

    /** 是否有人 */
    @Excel(name = "是否有人")
    private String yr;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String cph;

    /** 车轴 */
    @Excel(name = "车轴")
    private String cz;

    /** 车型 */
    @Excel(name = "车型")
    private String cx;

    /** 颜色 */
    @Excel(name = "颜色")
    private String ys;

    /** 是否穿戴工服 */
    @Excel(name = "是否穿戴工服")
    private String sfcdgf;

    /** 颜色 */
    @Excel(name = "颜色")
    private String base64;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setDwmc(String dwmc) 
    {
        this.dwmc = dwmc;
    }

    public String getDwmc() 
    {
        return dwmc;
    }

    public void setSj(Date sj) 
    {
        this.sj = sj;
    }

    public Date getSj() 
    {
        return sj;
    }

    public void setYc(String yc) 
    {
        this.yc = yc;
    }

    public String getYc() 
    {
        return yc;
    }

    public void setYr(String yr) 
    {
        this.yr = yr;
    }

    public String getYr() 
    {
        return yr;
    }

    public void setCph(String cph) 
    {
        this.cph = cph;
    }

    public String getCph() 
    {
        return cph;
    }

    public void setCz(String cz) 
    {
        this.cz = cz;
    }

    public String getCz() 
    {
        return cz;
    }

    public void setCx(String cx) 
    {
        this.cx = cx;
    }

    public String getCx() 
    {
        return cx;
    }

    public void setYs(String ys) 
    {
        this.ys = ys;
    }

    public String getYs() 
    {
        return ys;
    }

    public void setSfcdgf(String sfcdgf) 
    {
        this.sfcdgf = sfcdgf;
    }

    public String getSfcdgf() 
    {
        return sfcdgf;
    }

    public void setBase64(String base64) 
    {
        this.base64 = base64;
    }

    public String getBase64() 
    {
        return base64;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("dwmc", getDwmc())
            .append("sj", getSj())
            .append("yc", getYc())
            .append("yr", getYr())
            .append("cph", getCph())
            .append("cz", getCz())
            .append("cx", getCx())
            .append("ys", getYs())
            .append("sfcdgf", getSfcdgf())
            .append("base64", getBase64())
            .toString();
    }
}
