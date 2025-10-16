<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格" prop="materialSpec">
        <el-input
          v-model="queryParams.materialSpec"
          placeholder="请输入规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="型号" prop="materialModel">
        <el-input
          v-model="queryParams.materialModel"
          placeholder="请输入型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次" prop="materialBatchNo">
        <el-input
          v-model="queryParams.materialBatchNo"
          placeholder="请输入批次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存放区域" prop="areaCode">
        <el-input
          v-model="queryParams.areaCode"
          placeholder="请输入存放区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库存件数" prop="materialStockQty">
        <el-input
          v-model="queryParams.materialStockQty"
          placeholder="请输入库存件数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位" prop="materialUnit">
        <el-input
          v-model="queryParams.materialUnit"
          placeholder="请输入单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存放位置" prop="materialPos">
        <el-input
          v-model="queryParams.materialPos"
          placeholder="请输入存放位置：json{x,y}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:material:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:material:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:material:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:material:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="materialId" />
      <el-table-column label="物料编码" align="center" prop="materialCode" />
      <el-table-column label="物料名称" align="center" prop="materialName" />
      <el-table-column label="规格" align="center" prop="materialSpec" />
      <el-table-column label="型号" align="center" prop="materialModel" />
      <el-table-column label="批次" align="center" prop="materialBatchNo" />
      <el-table-column label="存放区域" align="center" prop="areaCode" />
      <el-table-column label="库存件数" align="center" prop="materialStockQty" />
      <el-table-column label="单位" align="center" prop="materialUnit" />
      <el-table-column label="存放位置" align="center" prop="materialPos" />
      <el-table-column label="存放条件" align="center" prop="materialRequirements" />
      <el-table-column label="安全注意事项" align="center" prop="materialSafetyNotice" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:material:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:material:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改物料档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="规格" prop="materialSpec">
          <el-input v-model="form.materialSpec" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="型号" prop="materialModel">
          <el-input v-model="form.materialModel" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="批次" prop="materialBatchNo">
          <el-input v-model="form.materialBatchNo" placeholder="请输入批次" />
        </el-form-item>
        <el-form-item label="存放区域" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入存放区域" />
        </el-form-item>
        <el-form-item label="库存件数" prop="materialStockQty">
          <el-input v-model="form.materialStockQty" placeholder="请输入库存件数" />
        </el-form-item>
        <el-form-item label="变更的上一记录" prop="materialChangePrevId">
          <el-input v-model="form.materialChangePrevId" placeholder="请输入变更的上一记录" />
        </el-form-item>
        <el-form-item label="变更的下一记录" prop="materialChangeNextId">
          <el-input v-model="form.materialChangeNextId" placeholder="请输入变更的下一记录" />
        </el-form-item>
        <el-form-item label="是否是变更记录：0初始入库，1变更记录" prop="materialIsChange">
          <el-input v-model="form.materialIsChange" placeholder="请输入是否是变更记录：0初始入库，1变更记录" />
        </el-form-item>
        <el-form-item label="变更原因" prop="materialChangeCause">
          <el-input v-model="form.materialChangeCause" placeholder="请输入变更原因" />
        </el-form-item>
        <el-form-item label="单位" prop="materialUnit">
          <el-input v-model="form.materialUnit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="存放位置：json{x,y}" prop="materialPos">
          <el-input v-model="form.materialPos" placeholder="请输入存放位置：json{x,y}" />
        </el-form-item>
        <el-form-item label="存放条件" prop="materialRequirements">
          <el-input v-model="form.materialRequirements" placeholder="请输入存放条件" />
        </el-form-item>
        <el-form-item label="安全注意事项" prop="materialSafetyNotice">
          <el-input v-model="form.materialSafetyNotice" placeholder="请输入安全注意事项" />
        </el-form-item>
        <el-form-item label="物料照片" prop="materialPhoto">
          <el-input v-model="form.materialPhoto" type="textarea" placeholder="请输入内容" />
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
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/system/wms_material"

export default {
  name: "Material",
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
      // 物料档案表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        materialSpec: null,
        materialModel: null,
        materialBatchNo: null,
        areaCode: null,
        materialStockQty: null,
        materialChangePrevId: null,
        materialChangeNextId: null,
        materialIsChange: null,
        materialChangeType: null,
        materialChangeCause: null,
        materialUnit: null,
        materialPos: null,
        materialRequirements: null,
        materialSafetyNotice: null,
        materialPhoto: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialCode: [
          { required: true, message: "物料编码不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        areaCode: [
          { required: true, message: "存放区域不能为空", trigger: "blur" }
        ],
        materialChangePrevId: [
          { required: true, message: "变更的上一记录不能为空", trigger: "blur" }
        ],
        materialChangeNextId: [
          { required: true, message: "变更的下一记录不能为空", trigger: "blur" }
        ],
        materialIsChange: [
          { required: true, message: "是否是变更记录：0初始入库，1变更记录不能为空", trigger: "blur" }
        ],
        materialChangeType: [
          { required: true, message: "变更类型：0入库，1出库，2：借入更正，3：贷出更正不能为空", trigger: "change" }
        ],
        materialPos: [
          { required: true, message: "存放位置：json{x,y}不能为空", trigger: "blur" }
        ],
        materialRequirements: [
          { required: true, message: "存放条件不能为空", trigger: "blur" }
        ],
        materialSafetyNotice: [
          { required: true, message: "安全注意事项不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物料档案列表 */
    getList() {
      this.loading = true
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        materialId: null,
        materialCode: null,
        materialName: null,
        materialSpec: null,
        materialModel: null,
        materialBatchNo: null,
        areaCode: null,
        materialStockQty: null,
        materialChangePrevId: null,
        materialChangeNextId: null,
        materialIsChange: null,
        materialChangeType: null,
        materialChangeCause: null,
        materialUnit: null,
        materialPos: null,
        materialRequirements: null,
        materialSafetyNotice: null,
        materialPhoto: null,
        createBy: null,
        createTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.materialId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物料档案"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const materialId = row.materialId || this.ids
      getMaterial(materialId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物料档案"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.materialId != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMaterial(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const materialIds = row.materialId || this.ids
      this.$modal.confirm('是否确认删除物料档案编号为"' + materialIds + '"的数据项？').then(function() {
        return delMaterial(materialIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
