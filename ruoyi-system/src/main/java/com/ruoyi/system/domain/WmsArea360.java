package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 区域点位全景对象 wms_area_360
 * 
 * @author ruoyi
 * @date 2025-11-21
 */
@ApiModel("区域点位全景对象")
public class WmsArea360 extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty("主键")
    private Long id;

    /** 点位名称 */
    @Excel(name = "点位名称")
    @ApiModelProperty("点位名称")
    private String name;

    /** 经度 */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private String latitude;

    /** 全景照片 */
    @Excel(name = "全景照片")
    @ApiModelProperty("全景照片")
    private String image;

    private String type = "watch";

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }

    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("image", getImage())
            .toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
