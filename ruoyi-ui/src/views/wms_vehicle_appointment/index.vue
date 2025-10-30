<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车辆" prop="vehicleId">
        <el-select v-model="queryParams.vehicleId" placeholder="请选择车辆ID" clearable>
          <el-option
            v-for="dict in dict.type.wms_external_vehicle"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预约时间" prop="gatepassAppointmentTime">
        <el-date-picker clearable
                        v-model="queryParams.gatepassAppointmentTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择预约时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="驾驶员姓名" prop="vehicleDriverName">
        <el-input
          v-model="queryParams.vehicleDriverName"
          placeholder="请输入驾驶员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="驾驶员手机" prop="vehicleDriverPhone">
        <el-input
          v-model="queryParams.vehicleDriverPhone"
          placeholder="请输入驾驶员手机"
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
          v-hasPermi="['system:gatepass:add']"
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
          v-hasPermi="['system:gatepass:edit']"
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
          v-hasPermi="['system:gatepass:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:gatepass:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="gatepassList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车辆" align="center" prop="vehicleId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_external_vehicle" :value="scope.row.vehicleId"/>
        </template>
      </el-table-column>
      <el-table-column label="预约时间" align="center" prop="gatepassAppointmentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gatepassAppointmentTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约区域" align="center" prop="areaCodes">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_area_name" :value="scope.row.areaCodes"/>
        </template>
      </el-table-column>
      <el-table-column label="驾驶员姓名" align="center" prop="vehicleDriverName" />
      <el-table-column label="驾驶员手机" align="center" prop="vehicleDriverPhone" />
      <el-table-column label="预约物资" align="center" prop="plans" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:gatepass:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:gatepass:remove']"
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

    <!-- 添加或修改车辆预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车辆" prop="vehicleId">
          <el-select v-model="form.vehicleId" placeholder="请选择车辆">
            <el-option
              v-for="dict in dict.type.wms_external_vehicle"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="gatepassAppointmentTime">
          <el-date-picker clearable
                          v-model="form.gatepassAppointmentTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择预约时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预约区域" prop="areaCodes">
          <el-select v-model="form.areaCodes" placeholder="请选择预约区域">
            <el-option
              v-for="dict in dict.type.wms_area_name"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="驾驶员姓名" prop="vehicleDriverName">
          <el-input v-model="form.vehicleDriverName" placeholder="请输入驾驶员姓名" />
        </el-form-item>
        <el-form-item label="驾驶员手机" prop="vehicleDriverPhone">
          <el-input v-model="form.vehicleDriverPhone" placeholder="请输入驾驶员手机" />
        </el-form-item>
        <el-form-item label="预约物资" prop="plans">
          <el-input v-model="form.plans" type="textarea" placeholder="请输入内容" />
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
import { listGatepass, getGatepass, delGatepass, addGatepass, updateGatepass } from "@/api/system/gatepass"

export default {
  name: "Gatepass",
  dicts: ['wms_area_name', 'wms_external_vehicle'],
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
      // 车辆预约表格数据
      gatepassList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleId: null,
        gatepassAppointmentTime: null,
        areaCodes: null,
        cardRecordId: null,
        cardId: null,
        vehicleDriverName: null,
        vehicleDriverPhone: null,
        actuals: null,
        plans: null,
        gatepassGateInTime: null,
        gatepassGateInPhoto: null,
        gatepassGateOutTime: null,
        gatepassGateOutPhoto: null,
        gatepassDwellTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehicleId: [
          { required: true, message: "车辆ID不能为空", trigger: "change" }
        ],
        gatepassAppointmentTime: [
          { required: true, message: "预约时间不能为空", trigger: "blur" }
        ],
        areaCodes: [
          { required: true, message: "预约区域编码的json数组[]不能为空", trigger: "change" }
        ],
        vehicleDriverName: [
          { required: true, message: "驾驶员姓名不能为空", trigger: "blur" }
        ],
        vehicleDriverPhone: [
          { required: true, message: "驾驶员手机不能为空", trigger: "blur" }
        ],
        plans: [
          { required: true, message: "预约物资", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询车辆预约列表 */
    getList() {
      this.loading = true
      listGatepass(this.queryParams).then(response => {
        this.gatepassList = response.rows
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
        gatepassId: null,
        vehicleId: null,
        gatepassAppointmentTime: null,
        areaCodes: null,
        cardRecordId: null,
        cardId: null,
        vehicleDriverName: null,
        vehicleDriverPhone: null,
        actuals: null,
        plans: null,
        gatepassGateInTime: null,
        gatepassGateInPhoto: null,
        gatepassGateOutTime: null,
        gatepassGateOutPhoto: null,
        gatepassDwellTime: null,
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
      this.ids = selection.map(item => item.gatepassId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加车辆预约"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const gatepassId = row.gatepassId || this.ids
      getGatepass(gatepassId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改车辆预约"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.gatepassId != null) {
            updateGatepass(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGatepass(this.form).then(response => {
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
      const gatepassIds = row.gatepassId || this.ids
      this.$modal.confirm('是否确认删除车辆预约编号为"' + gatepassIds + '"的数据项？').then(function() {
        return delGatepass(gatepassIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/gatepass/export', {
        ...this.queryParams
      }, `gatepass_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
