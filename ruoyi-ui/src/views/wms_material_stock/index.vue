<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="库存编号" prop="inventoryId">
        <el-input
          v-model="queryParams.inventoryId"
          placeholder="请输入库存编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物资编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物资编码"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_material_stock:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_material_stockList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库存编号" align="center" prop="inventoryId" />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="实存重量" align="center" prop="actualWeight" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="物资名称" align="center" prop="materialName" />
      <el-table-column label="计量单位" align="center" prop="unit" />
      <el-table-column label="库房" align="center" prop="warehouse" />
      <el-table-column label="账存重量" align="center" prop="bookWeight" />
      <el-table-column label="品种编码" align="center" prop="varietyCode" />
      <el-table-column label="品种名称" align="center" prop="varietyName" />
      <el-table-column label="物资类别" align="center" prop="materialCategory" />
      <el-table-column label="预拨重量" align="center" prop="reservedWeight" />
      <el-table-column label="可拨重量" align="center" prop="availableWeight" />
      <el-table-column label="中队" align="center" prop="squadron" />
      <el-table-column label="班组" align="center" prop="team" />
      <el-table-column label="所属公司" align="center" prop="company" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
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

    <!-- 添加或修改库存视图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="库存编号" prop="inventoryId">
          <el-input v-model="form.inventoryId" placeholder="请输入库存编号" />
        </el-form-item>
        <el-form-item label="物资编码" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="请输入物资编码" />
        </el-form-item>
        <el-form-item label="实存重量" prop="actualWeight">
          <el-input v-model="form.actualWeight" placeholder="请输入实存重量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="物资名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="库房" prop="warehouse">
          <el-input v-model="form.warehouse" placeholder="请输入库房" />
        </el-form-item>
        <el-form-item label="账存重量" prop="bookWeight">
          <el-input v-model="form.bookWeight" placeholder="请输入账存重量" />
        </el-form-item>
        <el-form-item label="品种编码" prop="varietyCode">
          <el-input v-model="form.varietyCode" placeholder="请输入品种编码" />
        </el-form-item>
        <el-form-item label="品种名称" prop="varietyName">
          <el-input v-model="form.varietyName" placeholder="请输入品种名称" />
        </el-form-item>
        <el-form-item label="物资类别" prop="materialCategory">
          <el-input v-model="form.materialCategory" placeholder="请输入物资类别" />
        </el-form-item>
        <el-form-item label="预拨重量" prop="reservedWeight">
          <el-input v-model="form.reservedWeight" placeholder="请输入预拨重量" />
        </el-form-item>
        <el-form-item label="可拨重量" prop="availableWeight">
          <el-input v-model="form.availableWeight" placeholder="请输入可拨重量" />
        </el-form-item>
        <el-form-item label="中队" prop="squadron">
          <el-input v-model="form.squadron" placeholder="请输入中队" />
        </el-form-item>
        <el-form-item label="班组" prop="team">
          <el-input v-model="form.team" placeholder="请输入班组" />
        </el-form-item>
        <el-form-item label="所属公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入所属公司" />
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
import { listWms_material_stock, getWms_material_stock, delWms_material_stock, addWms_material_stock, updateWms_material_stock } from "@/api/system/wms_material_stock"

export default {
  name: "Wms_material_stock",
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
      // 库存视图表格数据
      wms_material_stockList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inventoryId: null,
        materialCode: null,
        actualWeight: null,
        status: null,
        materialName: null,
        unit: null,
        warehouse: null,
        bookWeight: null,
        varietyCode: null,
        varietyName: null,
        materialCategory: null,
        reservedWeight: null,
        availableWeight: null,
        squadron: null,
        team: null,
        company: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询库存视图列表 */
    getList() {
      this.loading = true
      listWms_material_stock(this.queryParams).then(response => {
        this.wms_material_stockList = response.rows
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
        inventoryId: null,
        materialCode: null,
        actualWeight: null,
        status: null,
        remark: null,
        materialName: null,
        unit: null,
        warehouse: null,
        bookWeight: null,
        varietyCode: null,
        varietyName: null,
        materialCategory: null,
        reservedWeight: null,
        availableWeight: null,
        squadron: null,
        team: null,
        company: null
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
      this.ids = selection.map(item => item.inventoryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加库存视图"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const inventoryId = row.inventoryId || this.ids
      getWms_material_stock(inventoryId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改库存视图"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inventoryId != null) {
            updateWms_material_stock(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_material_stock(this.form).then(response => {
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
      const inventoryIds = row.inventoryId || this.ids
      this.$modal.confirm('是否确认删除库存视图编号为"' + inventoryIds + '"的数据项？').then(function() {
        return delWms_material_stock(inventoryIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_material_stock/export', {
        ...this.queryParams
      }, `wms_material_stock_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
