package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆黑名单wms_vehicle_blacklist对象 wms_vehicle_record
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public class WmsVehicleRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehiclePlateNo;

    /** 进入或出去 */
    @Excel(name = "进入或出去")
    private String type;

    /** 照片 */
    @Excel(name = "照片")
    private String photos;

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

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setPhotos(String photos) 
    {
        this.photos = photos;
    }

    public String getPhotos() 
    {
        return photos;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehiclePlateNo", getVehiclePlateNo())
            .append("type", getType())
            .append("photos", getPhotos())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
