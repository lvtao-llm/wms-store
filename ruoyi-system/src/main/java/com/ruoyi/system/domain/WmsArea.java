package com.ruoyi.system.domain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域对象 wms_area
 *
 * @author ruoyi
 * @date 2025-09-26
 */
public class WmsArea extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 区域编码
     */
    @Excel(name = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    private String areaName;

    /**
     * 1库区 2料场 3办公 4作业 5高风险
     */
    @Excel(name = "1库区 2料场 3办公 4作业 5高风险")
    private String areaType;

    /**
     * 区域颜色
     */
    @Excel(name = "区域颜色")
    private String areaColor;

    /**
     * 区域功能
     */
    @Excel(name = "区域功能")
    private String areaFunction;

    /**
     * 安全提示
     */
    @Excel(name = "安全提示")
    private String areaSafetyNotice;

    /**
     * 多边形边界JSON
     */
    @Excel(name = "多边形边界JSON")
    private String areaPolygon;

    /**
     * 风险等级 0低 1中 2高
     */
    @Excel(name = "风险等级 0低 1中 2高")
    private String areaRiskLevel;

    /**
     * 启用状态
     */
    @Excel(name = "启用状态")
    private String enabled;

    /**
     * geometry
     */
    @JsonIgnore
    private Geometry geometry;

    /**
     * 区域图片
     */
    @Excel(name = "区域图片")
    private String photos;

    /**
     * 全景图片
     */
    @Excel(name = "全景图片")
    private String photo360;

    /**
     * 删除标志
     */
    private String delFlag;

    /**
     * 人数
     */
    @JsonIgnore
    private int stuffCount = 0;

    @JsonIgnore
    private int visitorCount = 0;

    /**
     * 相机
     */
    @Excel(name = "相机")
    private String cameras;

    /**
     * 车辆数
     */
    @JsonIgnore
    private int vehicleCount = 0;

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaType() {
        return areaType;
    }

    public String getAreaColor() {
        return areaColor;
    }

    public void setAreaColor(String areaColor) {
        this.areaColor = areaColor;
    }

    public void setAreaFunction(String areaFunction) {
        this.areaFunction = areaFunction;
    }

    public String getAreaFunction() {
        return areaFunction;
    }

    public void setAreaSafetyNotice(String areaSafetyNotice) {
        this.areaSafetyNotice = areaSafetyNotice;
    }

    public String getAreaSafetyNotice() {
        return areaSafetyNotice;
    }

    public void setAreaPolygon(String areaPolygon) {
        this.areaPolygon = areaPolygon;
    }

    public String getAreaPolygon() {
        return areaPolygon;
    }

    public void setAreaRiskLevel(String areaRiskLevel) {
        this.areaRiskLevel = areaRiskLevel;
    }

    public String getAreaRiskLevel() {
        return areaRiskLevel;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("areaId", getAreaId())
                .append("areaCode", getAreaCode())
                .append("areaName", getAreaName())
                .append("areaType", getAreaType())
                .append("areaColor", getAreaColor())
                .append("areaFunction", getAreaFunction())
                .append("areaSafetyNotice", getAreaSafetyNotice())
                .append("areaPolygon", getAreaPolygon())
                .append("areaRiskLevel", getAreaRiskLevel())
                .append("enabled", getEnabled())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }


    public List<double[]> getAreaPolygonDouble() {
        List<double[]> list = new ArrayList<>();
        if (areaPolygon != null) {
            JSONObject array = JSONObject.parse(areaPolygon);
            if (array.containsKey("path")) {
                JSONArray points = array.getJSONArray("path");
                for (int i = 0; i < points.size(); i++) {
                    if (!points.getJSONObject(i).containsKey("longitude") || !points.getJSONObject(i).containsKey("latitude")) {
                        return list;
                    }
                    list.add(new double[]{points.getJSONObject(i).getDouble("longitude"), points.getJSONObject(i).getDouble("latitude")});
                }
            }
        }
        return list;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPhoto360() {
        return photo360;
    }

    public void setPhoto360(String photo360) {
        this.photo360 = photo360;
    }

    public int getStuffCount() {
        return stuffCount;
    }

    public void setStuffCount(int stuffCount) {
        this.stuffCount = stuffCount;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }

    public String getCameras() {
        return cameras;
    }

    public void setCameras(String cameras) {
        this.cameras = cameras;
    }
}
