package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆档案对象 wms_vehicle
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public class WmsVehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 车辆ID */
    private Long vehicleId;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehiclePlateNo;

    /** 车型 */
    @Excel(name = "车型")
    private String vehicleType;

    /** 车轴数 */
    @Excel(name = "车轴数")
    private String vehicleAxleNum;

    /** 承运商 */
    @Excel(name = "承运商")
    private String vehicleCompany;

    /** 驾驶员姓名 */
    @Excel(name = "驾驶员姓名")
    private String vehicleDriverName;

    /** 驾驶员手机 */
    @Excel(name = "驾驶员手机")
    private String vehicleDriverPhone;

    /** 核载(kg) */
    @Excel(name = "核载(kg)")
    private String maxWeight;

    /** 可进入区域：json数组 */
    @Excel(name = "可进入区域：json数组")
    private String authArea;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 是否删除 */
    private String delFlag;

    public void setVehicleId(Long vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId()
    {
        return vehicleId;
    }

    public void setVehiclePlateNo(String vehiclePlateNo) 
    {
        this.vehiclePlateNo = vehiclePlateNo;
    }

    public String getVehiclePlateNo() 
    {
        return vehiclePlateNo;
    }

    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() 
    {
        return vehicleType;
    }

    public void setVehicleAxleNum(String vehicleAxleNum) 
    {
        this.vehicleAxleNum = vehicleAxleNum;
    }

    public String getVehicleAxleNum() 
    {
        return vehicleAxleNum;
    }

    public void setVehicleCompany(String vehicleCompany) 
    {
        this.vehicleCompany = vehicleCompany;
    }

    public String getVehicleCompany() 
    {
        return vehicleCompany;
    }

    public void setVehicleDriverName(String vehicleDriverName) 
    {
        this.vehicleDriverName = vehicleDriverName;
    }

    public String getVehicleDriverName() 
    {
        return vehicleDriverName;
    }

    public void setVehicleDriverPhone(String vehicleDriverPhone) 
    {
        this.vehicleDriverPhone = vehicleDriverPhone;
    }

    public String getVehicleDriverPhone() 
    {
        return vehicleDriverPhone;
    }

    public void setMaxWeight(String maxWeight) 
    {
        this.maxWeight = maxWeight;
    }

    public String getMaxWeight() 
    {
        return maxWeight;
    }

    public void setAuthArea(String authArea) 
    {
        this.authArea = authArea;
    }

    public String getAuthArea() 
    {
        return authArea;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("vehicleId", getVehicleId())
            .append("vehiclePlateNo", getVehiclePlateNo())
            .append("vehicleType", getVehicleType())
            .append("vehicleAxleNum", getVehicleAxleNum())
            .append("vehicleCompany", getVehicleCompany())
            .append("vehicleDriverName", getVehicleDriverName())
            .append("vehicleDriverPhone", getVehicleDriverPhone())
            .append("maxWeight", getMaxWeight())
            .append("authArea", getAuthArea())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
