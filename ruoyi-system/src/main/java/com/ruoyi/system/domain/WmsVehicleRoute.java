package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆路线规划对象 wms_vehicle_route
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class WmsVehicleRoute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long routeId;

    /** 路线编号 业务唯一 */
    @Excel(name = "路线编号")
    private String routeNo;

    @Excel(name = "途经点")
    private String routeWaypoints;

    /** $column.columnComment */
    private String delFlag;

    public void setRouteId(Long routeId) 
    {
        this.routeId = routeId;
    }

    public Long getRouteId() 
    {
        return routeId;
    }

    public void setRouteNo(String routeNo) 
    {
        this.routeNo = routeNo;
    }

    public String getRouteNo() 
    {
        return routeNo;
    }

    public void setRouteWaypoints(String routeWaypoints) 
    {
        this.routeWaypoints = routeWaypoints;
    }

    public String getRouteWaypoints() 
    {
        return routeWaypoints;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("routeId", getRouteId())
            .append("routeNo", getRouteNo())
            .append("routeWaypoints", getRouteWaypoints())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
