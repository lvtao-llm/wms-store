<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="调拨明细编号" prop="allotDetailId">
        <el-input
          v-model="queryParams.allotDetailId"
          placeholder="请输入调拨明细编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调拨编码" prop="allotCode">
        <el-input
          v-model="queryParams.allotCode"
          placeholder="请输入调拨编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_material_out:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      height="500"
      v-loading="loading"
      :data="wms_material_outList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="调拨明细编号"
        align="center"
        prop="allotDetailId"
      />
      <el-table-column label="库房编号" align="center" prop="storehouseId" />
      <el-table-column label="单据类型" align="center" prop="documentType" />
      <el-table-column label="车号" align="center" prop="carNumber" />
      <el-table-column label="车型" align="center" prop="carModel" />
      <el-table-column label="毛重" align="center" prop="grossWeight" />
      <el-table-column label="皮重" align="center" prop="tareWeight" />
      <el-table-column label="净重" align="center" prop="netWeight" />
      <el-table-column label="仪表编号" align="center" prop="meterId" />
      <el-table-column label="部门" align="center" prop="department" />
      <el-table-column label="门卫" align="center" prop="guard" />
      <el-table-column
        label="出库时间"
        align="center"
        prop="outboundTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.outboundTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计量员" align="center" prop="measurer" />
      <el-table-column
        label="计量时间"
        align="center"
        prop="measureTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.measureTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据状态" align="center" prop="documentStatus" />
      <el-table-column
        label="操作时间"
        align="center"
        prop="operateTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operateTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="调整数量" align="center" prop="adjustQuantity" />
      <el-table-column label="出库编号" align="center" prop="outboundId" />
      <el-table-column label="计量类型" align="center" prop="measureType" />
      <el-table-column label="调拨编码" align="center" prop="allotCode" />
      <el-table-column label="调拨类型" align="center" prop="allotType" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column label="物料组" align="center" prop="materialGroup" />
      <el-table-column label="物资编码" align="center" prop="wzbm" />
      <el-table-column label="物资名称" align="center" prop="wzmc" />
      <el-table-column label="计量单位" align="center" prop="jldw" />
      <el-table-column label="销售公司" align="center" prop="salesCompany" />
      <el-table-column label="库房" align="center" prop="storehouse" />
      <el-table-column label="料场" align="center" prop="materialYard" />
      <el-table-column label="调拨数量" align="center" prop="allotQuantity" />
      <el-table-column label="实发数量" align="center" prop="actualQuantity" />
      <el-table-column label="车数" align="center" prop="carCount" />
      <el-table-column label="计划单价" align="center" prop="plannedPrice" />
      <el-table-column label="销售单价" align="center" prop="salesPrice" />
      <el-table-column label="税率" align="center" prop="taxRate" />
      <el-table-column label="调拨员" align="center" prop="allotOperator" />
      <el-table-column
        label="调拨时间"
        align="center"
        prop="allotTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.allotTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="余额" align="center" prop="balance" />
      <el-table-column label="尾料" align="center" prop="tailMaterial" />
      <el-table-column label="余料" align="center" prop="surplusMaterial" />
      <el-table-column label="车数1" align="center" prop="carCount1" />
      <el-table-column label="是否多车" align="center" prop="isMultiCar" />
      <el-table-column label="首次皮重" align="center" prop="firstTareWeight" />
      <el-table-column label="部门1" align="center" prop="department1" />
      <el-table-column label="料场编码" align="center" prop="yardCode" />
      <el-table-column label="报号人" align="center" prop="reporter" />
      <el-table-column label="调整备注" align="center" prop="adjustRemark" />
      <el-table-column label="净重1" align="center" prop="netWeight1" />
      <el-table-column
        label="业务年月"
        align="center"
        prop="businessYearMonth"
      />
      <el-table-column label="中队" align="center" prop="squad" />
      <el-table-column label="物料组1" align="center" prop="materialGroup1" />
      <el-table-column label="简码" align="center" prop="shortCode" />
      <el-table-column
        label="自合并分类"
        align="center"
        prop="selfMergeCategory"
      />
      <el-table-column label="保管员姓名" align="center" prop="custodianName" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope"> </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改调拨视图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="调拨明细编号" prop="allotDetailId">
          <el-input
            v-model="form.allotDetailId"
            placeholder="请输入调拨明细编号"
          />
        </el-form-item>
        <el-form-item label="库房编号" prop="storehouseId">
          <el-input v-model="form.storehouseId" placeholder="请输入库房编号" />
        </el-form-item>
        <el-form-item label="车号" prop="carNumber">
          <el-input v-model="form.carNumber" placeholder="请输入车号" />
        </el-form-item>
        <el-form-item label="车型" prop="carModel">
          <el-input v-model="form.carModel" placeholder="请输入车型" />
        </el-form-item>
        <el-form-item label="毛重" prop="grossWeight">
          <el-input v-model="form.grossWeight" placeholder="请输入毛重" />
        </el-form-item>
        <el-form-item label="皮重" prop="tareWeight">
          <el-input v-model="form.tareWeight" placeholder="请输入皮重" />
        </el-form-item>
        <el-form-item label="净重" prop="netWeight">
          <el-input v-model="form.netWeight" placeholder="请输入净重" />
        </el-form-item>
        <el-form-item label="仪表编号" prop="meterId">
          <el-input v-model="form.meterId" placeholder="请输入仪表编号" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="form.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="计量图片文件" prop="measureImageFile">
          <file-upload v-model="form.measureImageFile" />
        </el-form-item>
        <el-form-item label="计量录像文件" prop="measureVideoFile">
          <file-upload v-model="form.measureVideoFile" />
        </el-form-item>
        <el-form-item label="门卫" prop="guard">
          <el-input v-model="form.guard" placeholder="请输入门卫" />
        </el-form-item>
        <el-form-item label="出库时间" prop="outboundTime">
          <el-date-picker
            clearable
            v-model="form.outboundTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出库时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计量员" prop="measurer">
          <el-input v-model="form.measurer" placeholder="请输入计量员" />
        </el-form-item>
        <el-form-item label="计量时间" prop="measureTime">
          <el-date-picker
            clearable
            v-model="form.measureTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择计量时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="皮重图片文件" prop="tareImageFile">
          <file-upload v-model="form.tareImageFile" />
        </el-form-item>
        <el-form-item label="皮重录像文件" prop="tareVideoFile">
          <file-upload v-model="form.tareVideoFile" />
        </el-form-item>
        <el-form-item label="保管员" prop="custodian">
          <el-input v-model="form.custodian" placeholder="请输入保管员" />
        </el-form-item>
        <el-form-item label="操作时间" prop="operateTime">
          <el-date-picker
            clearable
            v-model="form.operateTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择操作时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="调整数量" prop="adjustQuantity">
          <el-input
            v-model="form.adjustQuantity"
            placeholder="请输入调整数量"
          />
        </el-form-item>
        <el-form-item label="出库编号" prop="outboundId">
          <el-input v-model="form.outboundId" placeholder="请输入出库编号" />
        </el-form-item>
        <el-form-item label="件数" prop="pieceCount">
          <el-input v-model="form.pieceCount" placeholder="请输入件数" />
        </el-form-item>
        <el-form-item label="原单编号" prop="originalDocumentId">
          <el-input
            v-model="form.originalDocumentId"
            placeholder="请输入原单编号"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号" />
        </el-form-item>
        <el-form-item label="调拨编号" prop="allotId">
          <el-input v-model="form.allotId" placeholder="请输入调拨编号" />
        </el-form-item>
        <el-form-item label="调拨编码" prop="allotCode">
          <el-input v-model="form.allotCode" placeholder="请输入调拨编码" />
        </el-form-item>
        <el-form-item label="客户编码" prop="customerCode">
          <el-input v-model="form.customerCode" placeholder="请输入客户编码" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="物料组" prop="materialGroup">
          <el-input v-model="form.materialGroup" placeholder="请输入物料组" />
        </el-form-item>
        <el-form-item label="物资编码" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="请输入物资编码" />
        </el-form-item>
        <el-form-item label="物资名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="计量单位" prop="measureUnit">
          <el-input v-model="form.measureUnit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="销售公司" prop="salesCompany">
          <el-input v-model="form.salesCompany" placeholder="请输入销售公司" />
        </el-form-item>
        <el-form-item label="库房" prop="storehouse">
          <el-input v-model="form.storehouse" placeholder="请输入库房" />
        </el-form-item>
        <el-form-item label="料场" prop="materialYard">
          <el-input v-model="form.materialYard" placeholder="请输入料场" />
        </el-form-item>
        <el-form-item label="调拨数量" prop="allotQuantity">
          <el-input v-model="form.allotQuantity" placeholder="请输入调拨数量" />
        </el-form-item>
        <el-form-item label="实发数量" prop="actualQuantity">
          <el-input
            v-model="form.actualQuantity"
            placeholder="请输入实发数量"
          />
        </el-form-item>
        <el-form-item label="车数" prop="carCount">
          <el-input v-model="form.carCount" placeholder="请输入车数" />
        </el-form-item>
        <el-form-item label="计划单价" prop="plannedPrice">
          <el-input v-model="form.plannedPrice" placeholder="请输入计划单价" />
        </el-form-item>
        <el-form-item label="销售单价" prop="salesPrice">
          <el-input v-model="form.salesPrice" placeholder="请输入销售单价" />
        </el-form-item>
        <el-form-item label="税率" prop="taxRate">
          <el-input v-model="form.taxRate" placeholder="请输入税率" />
        </el-form-item>
        <el-form-item label="调拨员" prop="allotOperator">
          <el-input v-model="form.allotOperator" placeholder="请输入调拨员" />
        </el-form-item>
        <el-form-item label="调拨时间" prop="allotTime">
          <el-date-picker
            clearable
            v-model="form.allotTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择调拨时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="余额" prop="balance">
          <el-input v-model="form.balance" placeholder="请输入余额" />
        </el-form-item>
        <el-form-item label="尾料" prop="tailMaterial">
          <el-input v-model="form.tailMaterial" placeholder="请输入尾料" />
        </el-form-item>
        <el-form-item label="余料" prop="surplusMaterial">
          <el-input v-model="form.surplusMaterial" placeholder="请输入余料" />
        </el-form-item>
        <el-form-item label="车数1" prop="carCount1">
          <el-input v-model="form.carCount1" placeholder="请输入车数1" />
        </el-form-item>
        <el-form-item label="是否多车" prop="isMultiCar">
          <el-input v-model="form.isMultiCar" placeholder="请输入是否多车" />
        </el-form-item>
        <el-form-item label="报号备注" prop="reportRemark">
          <el-input v-model="form.reportRemark" placeholder="请输入报号备注" />
        </el-form-item>
        <el-form-item label="首次皮重" prop="firstTareWeight">
          <el-input
            v-model="form.firstTareWeight"
            placeholder="请输入首次皮重"
          />
        </el-form-item>
        <el-form-item label="部门1" prop="department1">
          <el-input v-model="form.department1" placeholder="请输入部门1" />
        </el-form-item>
        <el-form-item label="料场编码" prop="yardCode">
          <el-input v-model="form.yardCode" placeholder="请输入料场编码" />
        </el-form-item>
        <el-form-item label="报号人" prop="reporter">
          <el-input v-model="form.reporter" placeholder="请输入报号人" />
        </el-form-item>
        <el-form-item label="调整备注" prop="adjustRemark">
          <el-input v-model="form.adjustRemark" placeholder="请输入调整备注" />
        </el-form-item>
        <el-form-item label="库房图片" prop="storehouseImage">
          <image-upload v-model="form.storehouseImage" />
        </el-form-item>
        <el-form-item label="图片路径" prop="imagePath">
          <el-input v-model="form.imagePath" placeholder="请输入图片路径" />
        </el-form-item>
        <el-form-item label="多调拨单明细" prop="multiAllotDetail">
          <el-input
            v-model="form.multiAllotDetail"
            placeholder="请输入多调拨单明细"
          />
        </el-form-item>
        <el-form-item label="净重1" prop="netWeight1">
          <el-input v-model="form.netWeight1" placeholder="请输入净重1" />
        </el-form-item>
        <el-form-item label="业务年月" prop="businessYearMonth">
          <el-input
            v-model="form.businessYearMonth"
            placeholder="请输入业务年月"
          />
        </el-form-item>
        <el-form-item label="中队" prop="squad">
          <el-input v-model="form.squad" placeholder="请输入中队" />
        </el-form-item>
        <el-form-item label="物料组1" prop="materialGroup1">
          <el-input v-model="form.materialGroup1" placeholder="请输入物料组1" />
        </el-form-item>
        <el-form-item label="保管员id" prop="custodianId">
          <el-input v-model="form.custodianId" placeholder="请输入保管员id" />
        </el-form-item>
        <el-form-item label="简码" prop="shortCode">
          <el-input v-model="form.shortCode" placeholder="请输入简码" />
        </el-form-item>
        <el-form-item label="自合并分类" prop="selfMergeCategory">
          <el-input
            v-model="form.selfMergeCategory"
            placeholder="请输入自合并分类"
          />
        </el-form-item>
        <el-form-item label="保管员姓名" prop="custodianName">
          <el-input
            v-model="form.custodianName"
            placeholder="请输入保管员姓名"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listWms_material_out,
  getWms_material_out,
  delWms_material_out,
  addWms_material_out,
  updateWms_material_out,
} from "@/api/system/wms_material_out";

export default {
  name: "Wms_material_out",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调拨视图表格数据
      wms_material_outList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        allotDetailId: null,
        storehouseId: null,
        documentType: null,
        carNumber: null,
        carModel: null,
        grossWeight: null,
        tareWeight: null,
        netWeight: null,
        meterId: null,
        department: null,
        measureImageFile: null,
        measureVideoFile: null,
        guard: null,
        outboundTime: null,
        measurer: null,
        measureTime: null,
        tareImageFile: null,
        tareVideoFile: null,
        documentStatus: null,
        custodian: null,
        operateTime: null,
        adjustQuantity: null,
        measureCurvature: null,
        outboundId: null,
        measureType: null,
        pieceCount: null,
        originalDocumentId: null,
        cardId: null,
        allotId: null,
        allotCode: null,
        allotType: null,
        customerCode: null,
        customerName: null,
        materialGroup: null,
        materialCode: null,
        materialName: null,
        measureUnit: null,
        salesCompany: null,
        storehouse: null,
        materialYard: null,
        allotQuantity: null,
        actualQuantity: null,
        carCount: null,
        plannedPrice: null,
        salesPrice: null,
        taxRate: null,
        allotOperator: null,
        allotTime: null,
        balance: null,
        tailMaterial: null,
        surplusMaterial: null,
        carCount1: null,
        isMultiCar: null,
        reportRemark: null,
        firstTareWeight: null,
        department1: null,
        yardCode: null,
        reporter: null,
        adjustRemark: null,
        storehouseImage: null,
        imagePath: null,
        multiAllotDetail: null,
        netWeight1: null,
        businessYearMonth: null,
        squad: null,
        materialGroup1: null,
        custodianId: null,
        shortCode: null,
        selfMergeCategory: null,
        custodianName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询调拨视图列表 */
    getList() {
      this.loading = true;
      listWms_material_out(this.queryParams).then((response) => {
        this.wms_material_outList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        allotDetailId: null,
        storehouseId: null,
        documentType: null,
        carNumber: null,
        carModel: null,
        grossWeight: null,
        tareWeight: null,
        netWeight: null,
        meterId: null,
        department: null,
        measureImageFile: null,
        measureVideoFile: null,
        guard: null,
        outboundTime: null,
        measurer: null,
        measureTime: null,
        tareImageFile: null,
        tareVideoFile: null,
        documentStatus: null,
        custodian: null,
        operateTime: null,
        adjustQuantity: null,
        measureCurvature: null,
        outboundId: null,
        measureType: null,
        pieceCount: null,
        originalDocumentId: null,
        remark: null,
        cardId: null,
        allotId: null,
        allotCode: null,
        allotType: null,
        customerCode: null,
        customerName: null,
        materialGroup: null,
        materialCode: null,
        materialName: null,
        measureUnit: null,
        salesCompany: null,
        storehouse: null,
        materialYard: null,
        allotQuantity: null,
        actualQuantity: null,
        carCount: null,
        plannedPrice: null,
        salesPrice: null,
        taxRate: null,
        allotOperator: null,
        allotTime: null,
        balance: null,
        tailMaterial: null,
        surplusMaterial: null,
        carCount1: null,
        isMultiCar: null,
        reportRemark: null,
        firstTareWeight: null,
        department1: null,
        yardCode: null,
        reporter: null,
        adjustRemark: null,
        storehouseImage: null,
        imagePath: null,
        multiAllotDetail: null,
        netWeight1: null,
        businessYearMonth: null,
        squad: null,
        materialGroup1: null,
        custodianId: null,
        shortCode: null,
        selfMergeCategory: null,
        custodianName: null,
        status: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.allotDetailId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加调拨视图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const allotDetailId = row.allotDetailId || this.ids;
      getWms_material_out(allotDetailId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改调拨视图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.allotDetailId != null) {
            updateWms_material_out(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWms_material_out(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const allotDetailIds = row.allotDetailId || this.ids;
      this.$modal
        .confirm('是否确认删除调拨视图编号为"' + allotDetailIds + '"的数据项？')
        .then(function () {
          return delWms_material_out(allotDetailIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/wms_material_out/export",
        {
          ...this.queryParams,
        },
        `wms_material_out_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
