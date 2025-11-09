package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 调拨视图对象 wms_material_out
 *
 * @author ruoyi
 * @date 2025-11-08
 */
public class WmsMaterialOut extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 调拨明细编号 */
    @Excel(name = "调拨明细编号")
    private String allotDetailId;

    /** 库房编号 */
    @Excel(name = "库房编号")
    private String storehouseId;

    /** 单据类型 */
    @Excel(name = "单据类型")
    private String documentType;

    /** 车号 */
    @Excel(name = "车号")
    private String carNumber;

    /** 车型 */
    @Excel(name = "车型")
    private String carModel;

    /** 毛重 */
    @Excel(name = "毛重")
    private Double grossWeight;

    /** 皮重 */
    @Excel(name = "皮重")
    private Double tareWeight;

    /** 净重 */
    @Excel(name = "净重")
    private Double netWeight;

    /** 仪表编号 */
    @Excel(name = "仪表编号")
    private String meterId;

    /** 部门 */
    @Excel(name = "部门")
    private String department;

    /** 计量图片文件 */
    @Excel(name = "计量图片文件")
    private String measureImageFile;

    /** 计量录像文件 */
    @Excel(name = "计量录像文件")
    private String measureVideoFile;

    /** 门卫 */
    @Excel(name = "门卫")
    private String guard;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outboundTime;

    /** 计量员 */
    @Excel(name = "计量员")
    private String measurer;

    /** 计量时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计量时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date measureTime;

    /** 皮重图片文件 */
    @Excel(name = "皮重图片文件")
    private String tareImageFile;

    /** 皮重录像文件 */
    @Excel(name = "皮重录像文件")
    private String tareVideoFile;

    /** 单据状态 */
    @Excel(name = "单据状态")
    private String documentStatus;

    /** 保管员 */
    @Excel(name = "保管员")
    private String custodian;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateTime;

    /** 调整数量 */
    @Excel(name = "调整数量")
    private Double adjustQuantity;

    /** 计量曲率 */
    @Excel(name = "计量曲率")
    private String measureCurvature;

    /** 出库编号 */
    @Excel(name = "出库编号")
    private String outboundId;

    /** 计量类型 */
    @Excel(name = "计量类型")
    private String measureType;

    /** 件数 */
    @Excel(name = "件数")
    private String pieceCount;

    /** 原单编号 */
    @Excel(name = "原单编号")
    private String originalDocumentId;

    /** 卡号 */
    @Excel(name = "卡号")
    private String cardId;

    /** 调拨编号 */
    @Excel(name = "调拨编号")
    private String allotId;

    /** 调拨编码 */
    @Excel(name = "调拨编码")
    private String allotCode;

    /** 调拨类型 */
    @Excel(name = "调拨类型")
    private String allotType;

    /** 客户编码 */
    @Excel(name = "客户编码")
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 物料组 */
    @Excel(name = "物料组")
    private String materialGroup;

    /** 物资编码 */
    @Excel(name = "物资编码")
    private String materialCode;

    /** 物资名称 */
    @Excel(name = "物资名称")
    private String materialName;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String measureUnit;

    /** 销售公司 */
    @Excel(name = "销售公司")
    private String salesCompany;

    /** 库房 */
    @Excel(name = "库房")
    private String storehouse;

    /** 料场 */
    @Excel(name = "料场")
    private String materialYard;

    /** 调拨数量 */
    @Excel(name = "调拨数量")
    private Double allotQuantity;

    /** 实发数量 */
    @Excel(name = "实发数量")
    private Double actualQuantity;

    /** 车数 */
    @Excel(name = "车数")
    private Double carCount;

    /** 计划单价 */
    @Excel(name = "计划单价")
    private Double plannedPrice;

    /** 销售单价 */
    @Excel(name = "销售单价")
    private Double salesPrice;

    /** 税率 */
    @Excel(name = "税率")
    private Double taxRate;

    /** 调拨员 */
    @Excel(name = "调拨员")
    private String allotOperator;

    /** 调拨时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "调拨时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date allotTime;

    /** 余额 */
    @Excel(name = "余额")
    private Double balance;

    /** 尾料 */
    @Excel(name = "尾料")
    private Double tailMaterial;

    /** 余料 */
    @Excel(name = "余料")
    private Double surplusMaterial;

    /** 车数1 */
    @Excel(name = "车数1")
    private Double carCount1;

    /** 是否多车 */
    @Excel(name = "是否多车")
    private String isMultiCar;

    /** 报号备注 */
    @Excel(name = "报号备注")
    private String reportRemark;

    /** 首次皮重 */
    @Excel(name = "首次皮重")
    private Double firstTareWeight;

    /** 部门1 */
    @Excel(name = "部门1")
    private String department1;

    /** 料场编码 */
    @Excel(name = "料场编码")
    private String yardCode;

    /** 报号人 */
    @Excel(name = "报号人")
    private String reporter;

    /** 调整备注 */
    @Excel(name = "调整备注")
    private String adjustRemark;

    /** 库房图片 */
    @Excel(name = "库房图片")
    private String storehouseImage;

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String imagePath;

    /** 多调拨单明细 */
    @Excel(name = "多调拨单明细")
    private String multiAllotDetail;

    /** 净重1 */
    @Excel(name = "净重1")
    private Double netWeight1;

    /** 业务年月 */
    @Excel(name = "业务年月")
    private String businessYearMonth;

    /** 中队 */
    @Excel(name = "中队")
    private String squad;

    /** 物料组1 */
    @Excel(name = "物料组1")
    private String materialGroup1;

    /** 保管员id */
    @Excel(name = "保管员id")
    private String custodianId;

    /** 简码 */
    @Excel(name = "简码")
    private String shortCode;

    /** 自合并分类 */
    @Excel(name = "自合并分类")
    private String selfMergeCategory;

    /** 保管员姓名 */
    @Excel(name = "保管员姓名")
    private String custodianName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setAllotDetailId(String allotDetailId)
    {
        this.allotDetailId = allotDetailId;
    }

    public String getAllotDetailId()
    {
        return allotDetailId;
    }

    public void setStorehouseId(String storehouseId)
    {
        this.storehouseId = storehouseId;
    }

    public String getStorehouseId()
    {
        return storehouseId;
    }

    public void setDocumentType(String documentType)
    {
        this.documentType = documentType;
    }

    public String getDocumentType()
    {
        return documentType;
    }

    public void setCarNumber(String carNumber)
    {
        this.carNumber = carNumber;
    }

    public String getCarNumber()
    {
        return carNumber;
    }

    public void setCarModel(String carModel)
    {
        this.carModel = carModel;
    }

    public String getCarModel()
    {
        return carModel;
    }

    public void setGrossWeight(Double grossWeight)
    {
        this.grossWeight = grossWeight;
    }

    public Double getGrossWeight()
    {
        return grossWeight;
    }

    public void setTareWeight(Double tareWeight)
    {
        this.tareWeight = tareWeight;
    }

    public Double getTareWeight()
    {
        return tareWeight;
    }

    public void setNetWeight(Double netWeight)
    {
        this.netWeight = netWeight;
    }

    public Double getNetWeight()
    {
        return netWeight;
    }

    public void setMeterId(String meterId)
    {
        this.meterId = meterId;
    }

    public String getMeterId()
    {
        return meterId;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setMeasureImageFile(String measureImageFile)
    {
        this.measureImageFile = measureImageFile;
    }

    public String getMeasureImageFile()
    {
        return measureImageFile;
    }

    public void setMeasureVideoFile(String measureVideoFile)
    {
        this.measureVideoFile = measureVideoFile;
    }

    public String getMeasureVideoFile()
    {
        return measureVideoFile;
    }

    public void setGuard(String guard)
    {
        this.guard = guard;
    }

    public String getGuard()
    {
        return guard;
    }

    public void setOutboundTime(Date outboundTime)
    {
        this.outboundTime = outboundTime;
    }

    public Date getOutboundTime()
    {
        return outboundTime;
    }

    public void setMeasurer(String measurer)
    {
        this.measurer = measurer;
    }

    public String getMeasurer()
    {
        return measurer;
    }

    public void setMeasureTime(Date measureTime)
    {
        this.measureTime = measureTime;
    }

    public Date getMeasureTime()
    {
        return measureTime;
    }

    public void setTareImageFile(String tareImageFile)
    {
        this.tareImageFile = tareImageFile;
    }

    public String getTareImageFile()
    {
        return tareImageFile;
    }

    public void setTareVideoFile(String tareVideoFile)
    {
        this.tareVideoFile = tareVideoFile;
    }

    public String getTareVideoFile()
    {
        return tareVideoFile;
    }

    public void setDocumentStatus(String documentStatus)
    {
        this.documentStatus = documentStatus;
    }

    public String getDocumentStatus()
    {
        return documentStatus;
    }

    public void setCustodian(String custodian)
    {
        this.custodian = custodian;
    }

    public String getCustodian()
    {
        return custodian;
    }

    public void setOperateTime(Date operateTime)
    {
        this.operateTime = operateTime;
    }

    public Date getOperateTime()
    {
        return operateTime;
    }

    public void setAdjustQuantity(Double adjustQuantity)
    {
        this.adjustQuantity = adjustQuantity;
    }

    public Double getAdjustQuantity()
    {
        return adjustQuantity;
    }

    public void setMeasureCurvature(String measureCurvature)
    {
        this.measureCurvature = measureCurvature;
    }

    public String getMeasureCurvature()
    {
        return measureCurvature;
    }

    public void setOutboundId(String outboundId)
    {
        this.outboundId = outboundId;
    }

    public String getOutboundId()
    {
        return outboundId;
    }

    public void setMeasureType(String measureType)
    {
        this.measureType = measureType;
    }

    public String getMeasureType()
    {
        return measureType;
    }

    public void setPieceCount(String pieceCount)
    {
        this.pieceCount = pieceCount;
    }

    public String getPieceCount()
    {
        return pieceCount;
    }

    public void setOriginalDocumentId(String originalDocumentId)
    {
        this.originalDocumentId = originalDocumentId;
    }

    public String getOriginalDocumentId()
    {
        return originalDocumentId;
    }

    public void setCardId(String cardId)
    {
        this.cardId = cardId;
    }

    public String getCardId()
    {
        return cardId;
    }

    public void setAllotId(String allotId)
    {
        this.allotId = allotId;
    }

    public String getAllotId()
    {
        return allotId;
    }

    public void setAllotCode(String allotCode)
    {
        this.allotCode = allotCode;
    }

    public String getAllotCode()
    {
        return allotCode;
    }

    public void setAllotType(String allotType)
    {
        this.allotType = allotType;
    }

    public String getAllotType()
    {
        return allotType;
    }

    public void setCustomerCode(String customerCode)
    {
        this.customerCode = customerCode;
    }

    public String getCustomerCode()
    {
        return customerCode;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setMaterialGroup(String materialGroup)
    {
        this.materialGroup = materialGroup;
    }

    public String getMaterialGroup()
    {
        return materialGroup;
    }

    public void setMaterialCode(String materialCode)
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode()
    {
        return materialCode;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public String getMaterialName()
    {
        return materialName;
    }

    public void setMeasureUnit(String measureUnit)
    {
        this.measureUnit = measureUnit;
    }

    public String getMeasureUnit()
    {
        return measureUnit;
    }

    public void setSalesCompany(String salesCompany)
    {
        this.salesCompany = salesCompany;
    }

    public String getSalesCompany()
    {
        return salesCompany;
    }

    public void setStorehouse(String storehouse)
    {
        this.storehouse = storehouse;
    }

    public String getStorehouse()
    {
        return storehouse;
    }

    public void setMaterialYard(String materialYard)
    {
        this.materialYard = materialYard;
    }

    public String getMaterialYard()
    {
        return materialYard;
    }

    public void setAllotQuantity(Double allotQuantity)
    {
        this.allotQuantity = allotQuantity;
    }

    public Double getAllotQuantity()
    {
        return allotQuantity;
    }

    public void setActualQuantity(Double actualQuantity)
    {
        this.actualQuantity = actualQuantity;
    }

    public Double getActualQuantity()
    {
        return actualQuantity;
    }

    public void setCarCount(Double carCount)
    {
        this.carCount = carCount;
    }

    public Double getCarCount()
    {
        return carCount;
    }

    public void setPlannedPrice(Double plannedPrice)
    {
        this.plannedPrice = plannedPrice;
    }

    public Double getPlannedPrice()
    {
        return plannedPrice;
    }

    public void setSalesPrice(Double salesPrice)
    {
        this.salesPrice = salesPrice;
    }

    public Double getSalesPrice()
    {
        return salesPrice;
    }

    public void setTaxRate(Double taxRate)
    {
        this.taxRate = taxRate;
    }

    public Double getTaxRate()
    {
        return taxRate;
    }

    public void setAllotOperator(String allotOperator)
    {
        this.allotOperator = allotOperator;
    }

    public String getAllotOperator()
    {
        return allotOperator;
    }

    public void setAllotTime(Date allotTime)
    {
        this.allotTime = allotTime;
    }

    public Date getAllotTime()
    {
        return allotTime;
    }

    public void setBalance(Double balance)
    {
        this.balance = balance;
    }

    public Double getBalance()
    {
        return balance;
    }

    public void setTailMaterial(Double tailMaterial)
    {
        this.tailMaterial = tailMaterial;
    }

    public Double getTailMaterial()
    {
        return tailMaterial;
    }

    public void setSurplusMaterial(Double surplusMaterial)
    {
        this.surplusMaterial = surplusMaterial;
    }

    public Double getSurplusMaterial()
    {
        return surplusMaterial;
    }

    public void setCarCount1(Double carCount1)
    {
        this.carCount1 = carCount1;
    }

    public Double getCarCount1()
    {
        return carCount1;
    }

    public void setIsMultiCar(String isMultiCar)
    {
        this.isMultiCar = isMultiCar;
    }

    public String getIsMultiCar()
    {
        return isMultiCar;
    }

    public void setReportRemark(String reportRemark)
    {
        this.reportRemark = reportRemark;
    }

    public String getReportRemark()
    {
        return reportRemark;
    }

    public void setFirstTareWeight(Double firstTareWeight)
    {
        this.firstTareWeight = firstTareWeight;
    }

    public Double getFirstTareWeight()
    {
        return firstTareWeight;
    }

    public void setDepartment1(String department1)
    {
        this.department1 = department1;
    }

    public String getDepartment1()
    {
        return department1;
    }

    public void setYardCode(String yardCode)
    {
        this.yardCode = yardCode;
    }

    public String getYardCode()
    {
        return yardCode;
    }

    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }

    public String getReporter()
    {
        return reporter;
    }

    public void setAdjustRemark(String adjustRemark)
    {
        this.adjustRemark = adjustRemark;
    }

    public String getAdjustRemark()
    {
        return adjustRemark;
    }

    public void setStorehouseImage(String storehouseImage)
    {
        this.storehouseImage = storehouseImage;
    }

    public String getStorehouseImage()
    {
        return storehouseImage;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setMultiAllotDetail(String multiAllotDetail)
    {
        this.multiAllotDetail = multiAllotDetail;
    }

    public String getMultiAllotDetail()
    {
        return multiAllotDetail;
    }

    public void setNetWeight1(Double netWeight1)
    {
        this.netWeight1 = netWeight1;
    }

    public Double getNetWeight1()
    {
        return netWeight1;
    }

    public void setBusinessYearMonth(String businessYearMonth) 
    {
        this.businessYearMonth = businessYearMonth;
    }

    public String getBusinessYearMonth() 
    {
        return businessYearMonth;
    }

    public void setSquad(String squad) 
    {
        this.squad = squad;
    }

    public String getSquad() 
    {
        return squad;
    }

    public void setMaterialGroup1(String materialGroup1) 
    {
        this.materialGroup1 = materialGroup1;
    }

    public String getMaterialGroup1() 
    {
        return materialGroup1;
    }

    public void setCustodianId(String custodianId) 
    {
        this.custodianId = custodianId;
    }

    public String getCustodianId() 
    {
        return custodianId;
    }

    public void setShortCode(String shortCode) 
    {
        this.shortCode = shortCode;
    }

    public String getShortCode() 
    {
        return shortCode;
    }

    public void setSelfMergeCategory(String selfMergeCategory) 
    {
        this.selfMergeCategory = selfMergeCategory;
    }

    public String getSelfMergeCategory() 
    {
        return selfMergeCategory;
    }

    public void setCustodianName(String custodianName) 
    {
        this.custodianName = custodianName;
    }

    public String getCustodianName() 
    {
        return custodianName;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("allotDetailId", getAllotDetailId())
            .append("storehouseId", getStorehouseId())
            .append("documentType", getDocumentType())
            .append("carNumber", getCarNumber())
            .append("carModel", getCarModel())
            .append("grossWeight", getGrossWeight())
            .append("tareWeight", getTareWeight())
            .append("netWeight", getNetWeight())
            .append("meterId", getMeterId())
            .append("department", getDepartment())
            .append("measureImageFile", getMeasureImageFile())
            .append("measureVideoFile", getMeasureVideoFile())
            .append("guard", getGuard())
            .append("outboundTime", getOutboundTime())
            .append("measurer", getMeasurer())
            .append("measureTime", getMeasureTime())
            .append("tareImageFile", getTareImageFile())
            .append("tareVideoFile", getTareVideoFile())
            .append("documentStatus", getDocumentStatus())
            .append("custodian", getCustodian())
            .append("operateTime", getOperateTime())
            .append("adjustQuantity", getAdjustQuantity())
            .append("measureCurvature", getMeasureCurvature())
            .append("outboundId", getOutboundId())
            .append("measureType", getMeasureType())
            .append("pieceCount", getPieceCount())
            .append("originalDocumentId", getOriginalDocumentId())
            .append("remark", getRemark())
            .append("cardId", getCardId())
            .append("allotId", getAllotId())
            .append("allotCode", getAllotCode())
            .append("allotType", getAllotType())
            .append("customerCode", getCustomerCode())
            .append("customerName", getCustomerName())
            .append("materialGroup", getMaterialGroup())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("measureUnit", getMeasureUnit())
            .append("salesCompany", getSalesCompany())
            .append("storehouse", getStorehouse())
            .append("materialYard", getMaterialYard())
            .append("allotQuantity", getAllotQuantity())
            .append("actualQuantity", getActualQuantity())
            .append("carCount", getCarCount())
            .append("plannedPrice", getPlannedPrice())
            .append("salesPrice", getSalesPrice())
            .append("taxRate", getTaxRate())
            .append("allotOperator", getAllotOperator())
            .append("allotTime", getAllotTime())
            .append("balance", getBalance())
            .append("tailMaterial", getTailMaterial())
            .append("surplusMaterial", getSurplusMaterial())
            .append("carCount1", getCarCount1())
            .append("isMultiCar", getIsMultiCar())
            .append("reportRemark", getReportRemark())
            .append("firstTareWeight", getFirstTareWeight())
            .append("department1", getDepartment1())
            .append("yardCode", getYardCode())
            .append("reporter", getReporter())
            .append("adjustRemark", getAdjustRemark())
            .append("storehouseImage", getStorehouseImage())
            .append("imagePath", getImagePath())
            .append("multiAllotDetail", getMultiAllotDetail())
            .append("netWeight1", getNetWeight1())
            .append("businessYearMonth", getBusinessYearMonth())
            .append("squad", getSquad())
            .append("materialGroup1", getMaterialGroup1())
            .append("custodianId", getCustodianId())
            .append("shortCode", getShortCode())
            .append("selfMergeCategory", getSelfMergeCategory())
            .append("custodianName", getCustodianName())
            .append("status", getStatus())
            .toString();
    }
}
