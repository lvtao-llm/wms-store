package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 虚拟路径点对象 wms_paths_definetion
 * 
 * @author ruoyi
 * @date 2025-12-19
 */
public class WmsPathsDefinetion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 开始时摄像头 */
    @Excel(name = "开始时摄像头")
    private String from;

    /** 开始时摄像头名称 */
    @Excel(name = "开始时摄像头名称")
    private String fromName;

    /** 开始时摄像头IP */
    @Excel(name = "开始时摄像头IP")
    private String fromIp;

    /** 结束时摄像头 */
    @Excel(name = "结束时摄像头")
    private String to;

    /** 结束时摄像头名称 */
    @Excel(name = "结束时摄像头名称")
    private String toName;

    /** 结束时摄像头IP */
    @Excel(name = "结束时摄像头IP")
    private String toIp;

    /** 虚拟点（经度） */
    @Excel(name = "虚拟点", readConverterExp = "经=度")
    private String pathLongitude;

    /** 虚拟点（纬度） */
    @Excel(name = "虚拟点", readConverterExp = "纬=度")
    private String pathLatitude;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFrom(String from) 
    {
        this.from = from;
    }

    public String getFrom() 
    {
        return from;
    }

    public void setFromName(String fromName) 
    {
        this.fromName = fromName;
    }

    public String getFromName() 
    {
        return fromName;
    }

    public void setFromIp(String fromIp) 
    {
        this.fromIp = fromIp;
    }

    public String getFromIp() 
    {
        return fromIp;
    }

    public void setTo(String to) 
    {
        this.to = to;
    }

    public String getTo() 
    {
        return to;
    }

    public void setToName(String toName) 
    {
        this.toName = toName;
    }

    public String getToName() 
    {
        return toName;
    }

    public void setToIp(String toIp) 
    {
        this.toIp = toIp;
    }

    public String getToIp() 
    {
        return toIp;
    }

    public void setPathLongitude(String pathLongitude) 
    {
        this.pathLongitude = pathLongitude;
    }

    public String getPathLongitude() 
    {
        return pathLongitude;
    }

    public void setPathLatitude(String pathLatitude) 
    {
        this.pathLatitude = pathLatitude;
    }

    public String getPathLatitude() 
    {
        return pathLatitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("from", getFrom())
            .append("fromName", getFromName())
            .append("fromIp", getFromIp())
            .append("to", getTo())
            .append("toName", getToName())
            .append("toIp", getToIp())
            .append("pathLongitude", getPathLongitude())
            .append("pathLatitude", getPathLatitude())
            .toString();
    }
}
