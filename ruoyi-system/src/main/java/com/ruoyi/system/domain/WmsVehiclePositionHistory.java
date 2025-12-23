package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆路径点历史对象 wms_vehicle_position_history
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
public class WmsVehiclePositionHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String cph;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date kssj;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jssj;

    /** wms_device_camera_log的ID数组 */
    @Excel(name = "wms_device_camera_log的ID数组")
    private String logIds;

    /** 虚拟的路径点 */
    @Excel(name = "虚拟的路径点")
    private String points;

    /** 年月日 */
    @Excel(name = "年月日")
    private String ymd;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCph(String cph) 
    {
        this.cph = cph;
    }

    public String getCph() 
    {
        return cph;
    }

    public void setKssj(Date kssj) 
    {
        this.kssj = kssj;
    }

    public Date getKssj() 
    {
        return kssj;
    }

    public void setJssj(Date jssj) 
    {
        this.jssj = jssj;
    }

    public Date getJssj() 
    {
        return jssj;
    }

    public void setLogIds(String logIds) 
    {
        this.logIds = logIds;
    }

    public String getLogIds() 
    {
        return logIds;
    }

    public void setPoints(String points) 
    {
        this.points = points;
    }

    public String getPoints() 
    {
        return points;
    }

    public void setYmd(String ymd) 
    {
        this.ymd = ymd;
    }

    public String getYmd() 
    {
        return ymd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cph", getCph())
            .append("kssj", getKssj())
            .append("jssj", getJssj())
            .append("logIds", getLogIds())
            .append("points", getPoints())
            .append("ymd", getYmd())
            .toString();
    }
}
