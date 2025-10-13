package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 发卡机对象 device_card_sender
 * 
 * @author ruoyi
 * @date 2025-10-12
 */
public class LanyaDeviceCardMachine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 发卡id */
    private Long cardSenderId;

    /** 设备sn编码 */
    @Excel(name = "设备sn编码")
    private String deviceSn;

    /** 设备版本号 */
    @Excel(name = "设备版本号")
    private String deviceVersion;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 发卡机按钮租借(默认0否；1是) */
    @Excel(name = "发卡机按钮租借(默认0否；1是)")
    private Integer deviceBtnRent;

    /** 发卡机心跳时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发卡机心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deviceHeartTime;

    /** 设备配置 */
    @Excel(name = "设备配置")
    private String deviceConfig;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 楼层id */
    @Excel(name = "楼层id")
    private String layerId;

    /** 楼层高度 */
    @Excel(name = "楼层高度")
    private Integer layerHeight;

    /** 人脸机用户名 */
    @Excel(name = "人脸机用户名")
    private String userName;

    /** 人脸机密码 */
    @Excel(name = "人脸机密码")
    private String password;

    /** 人脸机访问地址 */
    @Excel(name = "人脸机访问地址")
    private String faceUrl;

    /** 人脸相机标识 */
    @Excel(name = "人脸相机标识")
    private String faceFlag;

    /** 人脸机mac地址（程序获取） */
    @Excel(name = "人脸机mac地址", readConverterExp = "程=序获取")
    private String faceMacAddr;

    /** 人脸机SN码 */
    @Excel(name = "人脸机SN码")
    private String faceSn;

    /** 人脸机心跳时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "人脸机心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date faceHeartTime;

    /** 发卡机：是否启用 Y：启用 N：禁用 */
    @Excel(name = "发卡机：是否启用 Y：启用 N：禁用")
    private String deviceEnable;

    /** 定位卡型号 */
    @Excel(name = "定位卡型号")
    private String cardModel;

    /** 计划投放卡量 */
    @Excel(name = "计划投放卡量")
    private Long plannedNumberCards;

    /** 下发人脸(Y:是;N:否) */
    @Excel(name = "下发人脸(Y:是;N:否)")
    private String pushFace;

    /** 白名单数量 */
    @Excel(name = "白名单数量")
    private Integer whiteListCount;

    /** 视图卡槽顺序 */
    @Excel(name = "视图卡槽顺序")
    private String viewSort;

    /** 视图几排 */
    @Excel(name = "视图几排")
    private Integer viewRows;

    /** 视图几列 */
    @Excel(name = "视图几列")
    private Integer viewColumns;

    /** 视图数据 */
    @Excel(name = "视图数据")
    private String viewData;

    public void setCardSenderId(Long cardSenderId) 
    {
        this.cardSenderId = cardSenderId;
    }

    public Long getCardSenderId() 
    {
        return cardSenderId;
    }

    public void setDeviceSn(String deviceSn) 
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() 
    {
        return deviceSn;
    }

    public void setDeviceVersion(String deviceVersion) 
    {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceVersion() 
    {
        return deviceVersion;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceBtnRent(Integer deviceBtnRent) 
    {
        this.deviceBtnRent = deviceBtnRent;
    }

    public Integer getDeviceBtnRent() 
    {
        return deviceBtnRent;
    }

    public void setDeviceHeartTime(Date deviceHeartTime) 
    {
        this.deviceHeartTime = deviceHeartTime;
    }

    public Date getDeviceHeartTime() 
    {
        return deviceHeartTime;
    }

    public void setDeviceConfig(String deviceConfig) 
    {
        this.deviceConfig = deviceConfig;
    }

    public String getDeviceConfig() 
    {
        return deviceConfig;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    public void setLayerId(String layerId) 
    {
        this.layerId = layerId;
    }

    public String getLayerId() 
    {
        return layerId;
    }

    public void setLayerHeight(Integer layerHeight) 
    {
        this.layerHeight = layerHeight;
    }

    public Integer getLayerHeight() 
    {
        return layerHeight;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setFaceUrl(String faceUrl) 
    {
        this.faceUrl = faceUrl;
    }

    public String getFaceUrl() 
    {
        return faceUrl;
    }

    public void setFaceFlag(String faceFlag) 
    {
        this.faceFlag = faceFlag;
    }

    public String getFaceFlag() 
    {
        return faceFlag;
    }

    public void setFaceMacAddr(String faceMacAddr) 
    {
        this.faceMacAddr = faceMacAddr;
    }

    public String getFaceMacAddr() 
    {
        return faceMacAddr;
    }

    public void setFaceSn(String faceSn) 
    {
        this.faceSn = faceSn;
    }

    public String getFaceSn() 
    {
        return faceSn;
    }

    public void setFaceHeartTime(Date faceHeartTime) 
    {
        this.faceHeartTime = faceHeartTime;
    }

    public Date getFaceHeartTime() 
    {
        return faceHeartTime;
    }

    public void setDeviceEnable(String deviceEnable) 
    {
        this.deviceEnable = deviceEnable;
    }

    public String getDeviceEnable() 
    {
        return deviceEnable;
    }

    public void setCardModel(String cardModel) 
    {
        this.cardModel = cardModel;
    }

    public String getCardModel() 
    {
        return cardModel;
    }

    public void setPlannedNumberCards(Long plannedNumberCards) 
    {
        this.plannedNumberCards = plannedNumberCards;
    }

    public Long getPlannedNumberCards() 
    {
        return plannedNumberCards;
    }

    public void setPushFace(String pushFace) 
    {
        this.pushFace = pushFace;
    }

    public String getPushFace() 
    {
        return pushFace;
    }

    public void setWhiteListCount(Integer whiteListCount) 
    {
        this.whiteListCount = whiteListCount;
    }

    public Integer getWhiteListCount() 
    {
        return whiteListCount;
    }

    public void setViewSort(String viewSort) 
    {
        this.viewSort = viewSort;
    }

    public String getViewSort() 
    {
        return viewSort;
    }

    public void setViewRows(Integer viewRows) 
    {
        this.viewRows = viewRows;
    }

    public Integer getViewRows() 
    {
        return viewRows;
    }

    public void setViewColumns(Integer viewColumns) 
    {
        this.viewColumns = viewColumns;
    }

    public Integer getViewColumns() 
    {
        return viewColumns;
    }

    public void setViewData(String viewData) 
    {
        this.viewData = viewData;
    }

    public String getViewData() 
    {
        return viewData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("cardSenderId", getCardSenderId())
            .append("deviceSn", getDeviceSn())
            .append("deviceVersion", getDeviceVersion())
            .append("deviceName", getDeviceName())
            .append("deviceBtnRent", getDeviceBtnRent())
            .append("deviceHeartTime", getDeviceHeartTime())
            .append("deviceConfig", getDeviceConfig())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("layerId", getLayerId())
            .append("layerHeight", getLayerHeight())
            .append("userName", getUserName())
            .append("password", getPassword())
            .append("faceUrl", getFaceUrl())
            .append("faceFlag", getFaceFlag())
            .append("faceMacAddr", getFaceMacAddr())
            .append("faceSn", getFaceSn())
            .append("faceHeartTime", getFaceHeartTime())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("deviceEnable", getDeviceEnable())
            .append("cardModel", getCardModel())
            .append("plannedNumberCards", getPlannedNumberCards())
            .append("pushFace", getPushFace())
            .append("whiteListCount", getWhiteListCount())
            .append("viewSort", getViewSort())
            .append("viewRows", getViewRows())
            .append("viewColumns", getViewColumns())
            .append("viewData", getViewData())
            .toString();
    }
}
