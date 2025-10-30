package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆预约对象 wms_vehicle_gatepass
 * 
 * @author ruoyi
 * @date 2025-10-30
 */
public class WmsVehicleGatepass extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long gatepassId;

    /** 车辆ID */
    @Excel(name = "车辆ID")
    private Long vehicleId;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gatepassAppointmentTime;

    /** 预约区域编码的json数组[] */
    @Excel(name = "预约区域编码的json数组[]")
    private String areaCodes;

    /** 发卡记录ID */
    @Excel(name = "发卡记录ID")
    private Long cardRecordId;

    /** 定位卡ID */
    @Excel(name = "定位卡ID")
    private Long cardId;

    /** 驾驶员姓名 */
    @Excel(name = "驾驶员姓名")
    private String vehicleDriverName;

    /** 驾驶员手机 */
    @Excel(name = "驾驶员手机")
    private String vehicleDriverPhone;

    /** 实际物资,json:{
     material_id       // 关联物料档案表记录ID即可得到出入库
     quantity          // 实际数量
     datetime          // 时间
     gate_in_photo     // 进入区域照片
     gate_out_photo    // 离开区域照片
  } */
    @Excel(name = "实际物资")
    private String actuals;

    /** 预约物资，json：{
     material_id       // 关联物料档案表记录的第一条，目的是取物资的参数
     quantity          // 预约数量
     area_code         // 预约的区域
  } */
    @Excel(name = "预约物资")
    private String plans;

    /** 进入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gatepassGateInTime;

    /** 进入时料场门口照片 */
    @Excel(name = "进入时料场门口照片")
    private String gatepassGateInPhoto;

    /** 离开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gatepassGateOutTime;

    /** 离开时料场门口照片 */
    @Excel(name = "离开时料场门口照片")
    private String gatepassGateOutPhoto;

    /** 停留时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "停留时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gatepassDwellTime;

    public void setGatepassId(Long gatepassId) 
    {
        this.gatepassId = gatepassId;
    }

    public Long getGatepassId() 
    {
        return gatepassId;
    }

    public void setVehicleId(Long vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() 
    {
        return vehicleId;
    }

    public void setGatepassAppointmentTime(Date gatepassAppointmentTime) 
    {
        this.gatepassAppointmentTime = gatepassAppointmentTime;
    }

    public Date getGatepassAppointmentTime() 
    {
        return gatepassAppointmentTime;
    }

    public void setAreaCodes(String areaCodes) 
    {
        this.areaCodes = areaCodes;
    }

    public String getAreaCodes() 
    {
        return areaCodes;
    }

    public void setCardRecordId(Long cardRecordId) 
    {
        this.cardRecordId = cardRecordId;
    }

    public Long getCardRecordId() 
    {
        return cardRecordId;
    }

    public void setCardId(Long cardId) 
    {
        this.cardId = cardId;
    }

    public Long getCardId() 
    {
        return cardId;
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

    public void setActuals(String actuals) 
    {
        this.actuals = actuals;
    }

    public String getActuals() 
    {
        return actuals;
    }

    public void setPlans(String plans) 
    {
        this.plans = plans;
    }

    public String getPlans() 
    {
        return plans;
    }

    public void setGatepassGateInTime(Date gatepassGateInTime) 
    {
        this.gatepassGateInTime = gatepassGateInTime;
    }

    public Date getGatepassGateInTime() 
    {
        return gatepassGateInTime;
    }

    public void setGatepassGateInPhoto(String gatepassGateInPhoto) 
    {
        this.gatepassGateInPhoto = gatepassGateInPhoto;
    }

    public String getGatepassGateInPhoto() 
    {
        return gatepassGateInPhoto;
    }

    public void setGatepassGateOutTime(Date gatepassGateOutTime) 
    {
        this.gatepassGateOutTime = gatepassGateOutTime;
    }

    public Date getGatepassGateOutTime() 
    {
        return gatepassGateOutTime;
    }

    public void setGatepassGateOutPhoto(String gatepassGateOutPhoto) 
    {
        this.gatepassGateOutPhoto = gatepassGateOutPhoto;
    }

    public String getGatepassGateOutPhoto() 
    {
        return gatepassGateOutPhoto;
    }

    public void setGatepassDwellTime(Date gatepassDwellTime) 
    {
        this.gatepassDwellTime = gatepassDwellTime;
    }

    public Date getGatepassDwellTime() 
    {
        return gatepassDwellTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gatepassId", getGatepassId())
            .append("vehicleId", getVehicleId())
            .append("gatepassAppointmentTime", getGatepassAppointmentTime())
            .append("areaCodes", getAreaCodes())
            .append("cardRecordId", getCardRecordId())
            .append("cardId", getCardId())
            .append("vehicleDriverName", getVehicleDriverName())
            .append("vehicleDriverPhone", getVehicleDriverPhone())
            .append("actuals", getActuals())
            .append("plans", getPlans())
            .append("gatepassGateInTime", getGatepassGateInTime())
            .append("gatepassGateInPhoto", getGatepassGateInPhoto())
            .append("gatepassGateOutTime", getGatepassGateOutTime())
            .append("gatepassGateOutPhoto", getGatepassGateOutPhoto())
            .append("gatepassDwellTime", getGatepassDwellTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
