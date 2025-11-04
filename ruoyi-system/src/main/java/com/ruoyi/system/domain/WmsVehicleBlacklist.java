package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆黑名单对象 wms_vehicle_blacklist
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public class WmsVehicleBlacklist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehiclePlateNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVehiclePlateNo(String vehiclePlateNo) 
    {
        this.vehiclePlateNo = vehiclePlateNo;
    }

    public String getVehiclePlateNo() 
    {
        return vehiclePlateNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehiclePlateNo", getVehiclePlateNo())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
