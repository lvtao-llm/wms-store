//package com.ruoyi.system.domain;
//
//import java.util.Date;
//
///**
// * @author 吕涛
// * @version 1.0
// * @since 2025/11/8
// */
//public class WzjtViewDbSk {
//    /** 调拨明细编号 */
//    private String allotDetailId;
//    /** 库房编号 */
//    private String storehouseId;
//    /** 单据类型 */
//    private String documentType;
//    /** 车号 */
//    private String carNumber;
//    /** 车型 */
//    private String carModel;
//    /** 毛重 */
//    private Double grossWeight;
//    /** 皮重 */
//    private Double tareWeight;
//    /** 净重 */
//    private Double netWeight;
//    /** 仪表编号 */
//    private String meterId;
//    /** 部门 */
//    private String department;
//    /** 计量图片文件 */
//    private String measureImageFile;
//    /** 计量录像文件 */
//    private String measureVideoFile;
//    /** 门卫 */
//    private String guard;
//    /** 出库时间 */
//    private Date outboundTime;
//    /** 计量员 */
//    private String measurer;
//    /** 计量时间 */
//    private Date measureTime;
//    /** 皮重图片文件 */
//    private String tareImageFile;
//    /** 皮重录像文件 */
//    private String tareVideoFile;
//    /** 单据状态 */
//    private String documentStatus;
//    /** 保管员 */
//    private String custodian;
//    /** 操作时间 */
//    private Date operateTime;
//    /** 调整数量 */
//    private Double adjustQuantity;
//    /** 计量曲率 */
//    private String measureCurvature;
//    /** 出库编号 */
//    private String outboundId;
//    /** 计量类型 */
//    private String measureType;
//    /** 件数 */
//    private String pieceCount;
//    /** 原单编号 */
//    private String originalDocumentId;
//    /** 备注 */
//    private String remark;
//    /** 卡号 */
//    private String cardId;
//    /** 调拨编号 */
//    private String allotId;
//    /** 调拨编码 */
//    private String allotCode;
//    /** 调拨类型 */
//    private String allotType;
//    /** 客户编码 */
//    private String customerCode;
//    /** 客户名称 */
//    private String customerName;
//    /** 物料组 */
//    private String materialGroup;
//    /** 物资编码 */
//    private String materialCode;
//    /** 物资名称 */
//    private String materialName;
//    /** 计量单位 */
//    private String measureUnit;
//    /** 销售公司 */
//    private String salesCompany;
//    /** 库房 */
//    private String storehouse;
//    /** 料场 */
//    private String materialYard;
//    /** 调拨数量 */
//    private Double allotQuantity;
//    /** 实发数量 */
//    private Double actualQuantity;
//    /** 车数 */
//    private Double carCount;
//    /** 计划单价 */
//    private Double plannedPrice;
//    /** 销售单价 */
//    private Double salesPrice;
//    /** 税率 */
//    private Double taxRate;
//    /** 调拨员 */
//    private String allotOperator;
//    /** 调拨时间 */
//    private Date allotTime;
//    /** 余额 */
//    private Double balance;
//    /** 尾料 */
//    private Double tailMaterial;
//    /** 余料 */
//    private Double surplusMaterial;
//    /** 车数1 */
//    private Double carCount1;
//    /** 是否多车 */
//    private String isMultiCar;
//    /** 报号备注 */
//    private String reportRemark;
//    /** 首次皮重 */
//    private Double firstTareWeight;
//    /** 部门1 */
//    private String department1;
//    /** 料场编码 */
//    private String yardCode;
//    /** 报号人 */
//    private String reporter;
//    /** 调整备注 */
//    private String adjustRemark;
//    /** 库房图片 */
//    private String storehouseImage;
//    /** 图片路径 */
//    private String imagePath;
//    /** 多调拨单明细 */
//    private String multiAllotDetail;
//    /** 净重1 */
//    private Double netWeight1;
//    /** 业务年月 */
//    private String businessYearMonth;
//    /** 中队 */
//    private String squad;
//    /** 物料组1 */
//    private String materialGroup1;
//    /** 保管员id */
//    private String custodianId;
//    /** 简码 */
//    private String shortCode;
//    /** 自合并分类 */
//    private String selfMergeCategory;
//    /** 保管员姓名 */
//    private String custodianName;
//    /** 状态 */
//    private String status;
//
//    // Getters and Setters
//    public String getAllotDetailId() {
//        return allotDetailId;
//    }
//
//    public void setAllotDetailId(String allotDetailId) {
//        this.allotDetailId = allotDetailId;
//    }
//
//    public String getStorehouseId() {
//        return storehouseId;
//    }
//
//    public void setStorehouseId(String storehouseId) {
//        this.storehouseId = storehouseId;
//    }
//
//    public String getDocumentType() {
//        return documentType;
//    }
//
//    public void setDocumentType(String documentType) {
//        this.documentType = documentType;
//    }
//
//    public String getCarNumber() {
//        return carNumber;
//    }
//
//    public void setCarNumber(String carNumber) {
//        this.carNumber = carNumber;
//    }
//
//    public String getCarModel() {
//        return carModel;
//    }
//
//    public void setCarModel(String carModel) {
//        this.carModel = carModel;
//    }
//
//    public Double getGrossWeight() {
//        return grossWeight;
//    }
//
//    public void setGrossWeight(Double grossWeight) {
//        this.grossWeight = grossWeight;
//    }
//
//    public Double getTareWeight() {
//        return tareWeight;
//    }
//
//    public void setTareWeight(Double tareWeight) {
//        this.tareWeight = tareWeight;
//    }
//
//    public Double getNetWeight() {
//        return netWeight;
//    }
//
//    public void setNetWeight(Double netWeight) {
//        this.netWeight = netWeight;
//    }
//
//    public String getMeterId() {
//        return meterId;
//    }
//
//    public void setMeterId(String meterId) {
//        this.meterId = meterId;
//    }
//
//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    public String getMeasureImageFile() {
//        return measureImageFile;
//    }
//
//    public void setMeasureImageFile(String measureImageFile) {
//        this.measureImageFile = measureImageFile;
//    }
//
//    public String getMeasureVideoFile() {
//        return measureVideoFile;
//    }
//
//    public void setMeasureVideoFile(String measureVideoFile) {
//        this.measureVideoFile = measureVideoFile;
//    }
//
//    public String getGuard() {
//        return guard;
//    }
//
//    public void setGuard(String guard) {
//        this.guard = guard;
//    }
//
//    public Date getOutboundTime() {
//        return outboundTime;
//    }
//
//    public void setOutboundTime(Date outboundTime) {
//        this.outboundTime = outboundTime;
//    }
//
//    public String getMeasurer() {
//        return measurer;
//    }
//
//    public void setMeasurer(String measurer) {
//        this.measurer = measurer;
//    }
//
//    public Date getMeasureTime() {
//        return measureTime;
//    }
//
//    public void setMeasureTime(Date measureTime) {
//        this.measureTime = measureTime;
//    }
//
//    public String getTareImageFile() {
//        return tareImageFile;
//    }
//
//    public void setTareImageFile(String tareImageFile) {
//        this.tareImageFile = tareImageFile;
//    }
//
//    public String getTareVideoFile() {
//        return tareVideoFile;
//    }
//
//    public void setTareVideoFile(String tareVideoFile) {
//        this.tareVideoFile = tareVideoFile;
//    }
//
//    public String getDocumentStatus() {
//        return documentStatus;
//    }
//
//    public void setDocumentStatus(String documentStatus) {
//        this.documentStatus = documentStatus;
//    }
//
//    public String getCustodian() {
//        return custodian;
//    }
//
//    public void setCustodian(String custodian) {
//        this.custodian = custodian;
//    }
//
//    public Date getOperateTime() {
//        return operateTime;
//    }
//
//    public void setOperateTime(Date operateTime) {
//        this.operateTime = operateTime;
//    }
//
//    public Double getAdjustQuantity() {
//        return adjustQuantity;
//    }
//
//    public void setAdjustQuantity(Double adjustQuantity) {
//        this.adjustQuantity = adjustQuantity;
//    }
//
//    public String getMeasureCurvature() {
//        return measureCurvature;
//    }
//
//    public void setMeasureCurvature(String measureCurvature) {
//        this.measureCurvature = measureCurvature;
//    }
//
//    public String getOutboundId() {
//        return outboundId;
//    }
//
//    public void setOutboundId(String outboundId) {
//        this.outboundId = outboundId;
//    }
//
//    public String getMeasureType() {
//        return measureType;
//    }
//
//    public void setMeasureType(String measureType) {
//        this.measureType = measureType;
//    }
//
//    public String getPieceCount() {
//        return pieceCount;
//    }
//
//    public void setPieceCount(String pieceCount) {
//        this.pieceCount = pieceCount;
//    }
//
//    public String getOriginalDocumentId() {
//        return originalDocumentId;
//    }
//
//    public void setOriginalDocumentId(String originalDocumentId) {
//        this.originalDocumentId = originalDocumentId;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public String getCardId() {
//        return cardId;
//    }
//
//    public void setCardId(String cardId) {
//        this.cardId = cardId;
//    }
//
//    public String getAllotId() {
//        return allotId;
//    }
//
//    public void setAllotId(String allotId) {
//        this.allotId = allotId;
//    }
//
//    public String getAllotCode() {
//        return allotCode;
//    }
//
//    public void setAllotCode(String allotCode) {
//        this.allotCode = allotCode;
//    }
//
//    public String getAllotType() {
//        return allotType;
//    }
//
//    public void setAllotType(String allotType) {
//        this.allotType = allotType;
//    }
//
//    public String getCustomerCode() {
//        return customerCode;
//    }
//
//    public void setCustomerCode(String customerCode) {
//        this.customerCode = customerCode;
//    }
//
//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }
//
//    public String getMaterialGroup() {
//        return materialGroup;
//    }
//
//    public void setMaterialGroup(String materialGroup) {
//        this.materialGroup = materialGroup;
//    }
//
//    public String getMaterialCode() {
//        return materialCode;
//    }
//
//    public void setMaterialCode(String materialCode) {
//        this.materialCode = materialCode;
//    }
//
//    public String getMaterialName() {
//        return materialName;
//    }
//
//    public void setMaterialName(String materialName) {
//        this.materialName = materialName;
//    }
//
//    public String getMeasureUnit() {
//        return measureUnit;
//    }
//
//    public void setMeasureUnit(String measureUnit) {
//        this.measureUnit = measureUnit;
//    }
//
//    public String getSalesCompany() {
//        return salesCompany;
//    }
//
//    public void setSalesCompany(String salesCompany) {
//        this.salesCompany = salesCompany;
//    }
//
//    public String getStorehouse() {
//        return storehouse;
//    }
//
//    public void setStorehouse(String storehouse) {
//        this.storehouse = storehouse;
//    }
//
//    public String getMaterialYard() {
//        return materialYard;
//    }
//
//    public void setMaterialYard(String materialYard) {
//        this.materialYard = materialYard;
//    }
//
//    public Double getAllotQuantity() {
//        return allotQuantity;
//    }
//
//    public void setAllotQuantity(Double allotQuantity) {
//        this.allotQuantity = allotQuantity;
//    }
//
//    public Double getActualQuantity() {
//        return actualQuantity;
//    }
//
//    public void setActualQuantity(Double actualQuantity) {
//        this.actualQuantity = actualQuantity;
//    }
//
//    public Double getCarCount() {
//        return carCount;
//    }
//
//    public void setCarCount(Double carCount) {
//        this.carCount = carCount;
//    }
//
//    public Double getPlannedPrice() {
//        return plannedPrice;
//    }
//
//    public void setPlannedPrice(Double plannedPrice) {
//        this.plannedPrice = plannedPrice;
//    }
//
//    public Double getSalesPrice() {
//        return salesPrice;
//    }
//
//    public void setSalesPrice(Double salesPrice) {
//        this.salesPrice = salesPrice;
//    }
//
//    public Double getTaxRate() {
//        return taxRate;
//    }
//
//    public void setTaxRate(Double taxRate) {
//        this.taxRate = taxRate;
//    }
//
//    public String getAllotOperator() {
//        return allotOperator;
//    }
//
//    public void setAllotOperator(String allotOperator) {
//        this.allotOperator = allotOperator;
//    }
//
//    public Date getAllotTime() {
//        return allotTime;
//    }
//
//    public void setAllotTime(Date allotTime) {
//        this.allotTime = allotTime;
//    }
//
//    public Double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(Double balance) {
//        this.balance = balance;
//    }
//
//    public Double getTailMaterial() {
//        return tailMaterial;
//    }
//
//    public void setTailMaterial(Double tailMaterial) {
//        this.tailMaterial = tailMaterial;
//    }
//
//    public Double getSurplusMaterial() {
//        return surplusMaterial;
//    }
//
//    public void setSurplusMaterial(Double surplusMaterial) {
//        this.surplusMaterial = surplusMaterial;
//    }
//
//    public Double getCarCount1() {
//        return carCount1;
//    }
//
//    public void setCarCount1(Double carCount1) {
//        this.carCount1 = carCount1;
//    }
//
//    public String getIsMultiCar() {
//        return isMultiCar;
//    }
//
//    public void setIsMultiCar(String isMultiCar) {
//        this.isMultiCar = isMultiCar;
//    }
//
//    public String getReportRemark() {
//        return reportRemark;
//    }
//
//    public void setReportRemark(String reportRemark) {
//        this.reportRemark = reportRemark;
//    }
//
//    public Double getFirstTareWeight() {
//        return firstTareWeight;
//    }
//
//    public void setFirstTareWeight(Double firstTareWeight) {
//        this.firstTareWeight = firstTareWeight;
//    }
//
//    public String getDepartment1() {
//        return department1;
//    }
//
//    public void setDepartment1(String department1) {
//        this.department1 = department1;
//    }
//
//    public String getYardCode() {
//        return yardCode;
//    }
//
//    public void setYardCode(String yardCode) {
//        this.yardCode = yardCode;
//    }
//
//    public String getReporter() {
//        return reporter;
//    }
//
//    public void setReporter(String reporter) {
//        this.reporter = reporter;
//    }
//
//    public String getAdjustRemark() {
//        return adjustRemark;
//    }
//
//    public void setAdjustRemark(String adjustRemark) {
//        this.adjustRemark = adjustRemark;
//    }
//
//    public String getStorehouseImage() {
//        return storehouseImage;
//    }
//
//    public void setStorehouseImage(String storehouseImage) {
//        this.storehouseImage = storehouseImage;
//    }
//
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
//
//    public String getMultiAllotDetail() {
//        return multiAllotDetail;
//    }
//
//    public void setMultiAllotDetail(String multiAllotDetail) {
//        this.multiAllotDetail = multiAllotDetail;
//    }
//
//    public Double getNetWeight1() {
//        return netWeight1;
//    }
//
//    public void setNetWeight1(Double netWeight1) {
//        this.netWeight1 = netWeight1;
//    }
//
//    public String getBusinessYearMonth() {
//        return businessYearMonth;
//    }
//
//    public void setBusinessYearMonth(String businessYearMonth) {
//        this.businessYearMonth = businessYearMonth;
//    }
//
//    public String getSquad() {
//        return squad;
//    }
//
//    public void setSquad(String squad) {
//        this.squad = squad;
//    }
//
//    public String getMaterialGroup1() {
//        return materialGroup1;
//    }
//
//    public void setMaterialGroup1(String materialGroup1) {
//        this.materialGroup1 = materialGroup1;
//    }
//
//    public String getCustodianId() {
//        return custodianId;
//    }
//
//    public void setCustodianId(String custodianId) {
//        this.custodianId = custodianId;
//    }
//
//    public String getShortCode() {
//        return shortCode;
//    }
//
//    public void setShortCode(String shortCode) {
//        this.shortCode = shortCode;
//    }
//
//    public String getSelfMergeCategory() {
//        return selfMergeCategory;
//    }
//
//    public void setSelfMergeCategory(String selfMergeCategory) {
//        this.selfMergeCategory = selfMergeCategory;
//    }
//
//    public String getCustodianName() {
//        return custodianName;
//    }
//
//    public void setCustodianName(String custodianName) {
//        this.custodianName = custodianName;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}
