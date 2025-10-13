<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="140px">
      <el-form-item label="关联车辆id" prop="vehicleId">
        <el-input
          v-model="queryParams.vehicleId"
          placeholder="请输入关联车辆id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车辆名称" prop="vehicleName">
        <el-input
          v-model="queryParams.vehicleName"
          placeholder="请输入车辆名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车载卡号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入车载卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="licensePlateNumber">
        <el-input
          v-model="queryParams.licensePlateNumber"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="司机姓名" prop="driverName">
        <el-input
          v-model="queryParams.driverName"
          placeholder="请输入司机姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="司机手机号" prop="driverTel">
        <el-input
          v-model="queryParams.driverTel"
          placeholder="请输入司机手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="照片" prop="driverPhoto">
        <el-input
          v-model="queryParams.driverPhoto"
          placeholder="请输入照片"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="司机身份证号码" prop="idNumber">
        <el-input
          v-model="queryParams.idNumber"
          placeholder="请输入司机身份证号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人员IC号" prop="personIc">
        <el-input
          v-model="queryParams.personIc"
          placeholder="请输入人员IC号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位名称" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入单位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约进入开始时间" prop="reservationEntryStartTime">
        <el-date-picker clearable
          v-model="queryParams.reservationEntryStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预约进入开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预约进入结束时间" prop="reservationEntryEndTime">
        <el-date-picker clearable
          v-model="queryParams.reservationEntryEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预约进入结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="来访时间" prop="visitingTime">
        <el-date-picker clearable
          v-model="queryParams.visitingTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择来访时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="离开时间" prop="leaveTime">
        <el-date-picker clearable
          v-model="queryParams.leaveTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择离开时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="来访事由" prop="visitingReason">
        <el-input
          v-model="queryParams.visitingReason"
          placeholder="请输入来访事由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接待人" prop="receiver">
        <el-input
          v-model="queryParams.receiver"
          placeholder="请输入接待人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接待人联系方式" prop="receiverTel">
        <el-input
          v-model="queryParams.receiverTel"
          placeholder="请输入接待人联系方式"
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
          v-hasPermi="['system:lanya-vehicleInfo-visitor:add']"
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
          v-hasPermi="['system:lanya-vehicleInfo-visitor:edit']"
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
          v-hasPermi="['system:lanya-vehicleInfo-visitor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya-vehicleInfo-visitor:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_vehicleInfo_visitorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="车辆名称" align="center" prop="vehicleName" />
      <el-table-column label="车辆类型" align="center" prop="vehicleType" />
      <el-table-column label="车载卡号" align="center" prop="cardId" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
      <el-table-column label="司机姓名" align="center" prop="driverName" />
      <el-table-column label="司机手机号" align="center" prop="driverTel" width="100"/>
      <el-table-column label="照片" align="center" prop="driverPhoto" />
      <el-table-column label="司机身份证号码" align="center" prop="idNumber" width="180"/>
      <el-table-column label="人员IC号" align="center" prop="personIc" />
      <el-table-column label="单位名称" align="center" prop="companyName" />
      <el-table-column label="预约进入开始时间" align="center" prop="reservationEntryStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reservationEntryStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约进入结束时间" align="center" prop="reservationEntryEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reservationEntryEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来访时间" align="center" prop="visitingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="离开时间" align="center" prop="leaveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.leaveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来访状态" align="center" prop="visitingStatus" />
      <el-table-column label="来访事由" align="center" prop="visitingReason" />
      <el-table-column label="接待人" align="center" prop="receiver" />
      <el-table-column label="接待人联系方式" align="center" prop="receiverTel" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya-vehicleInfo-visitor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya-vehicleInfo-visitor:remove']"
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

    <!-- 添加或修改访客车辆对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联车辆id" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入关联车辆id" />
        </el-form-item>
        <el-form-item label="车辆名称" prop="vehicleName">
          <el-input v-model="form.vehicleName" placeholder="请输入车辆名称" />
        </el-form-item>
        <el-form-item label="车载卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入车载卡号" />
        </el-form-item>
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input v-model="form.licensePlateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="司机姓名" prop="driverName">
          <el-input v-model="form.driverName" placeholder="请输入司机姓名" />
        </el-form-item>
        <el-form-item label="司机手机号" prop="driverTel">
          <el-input v-model="form.driverTel" placeholder="请输入司机手机号" />
        </el-form-item>
        <el-form-item label="照片" prop="driverPhoto">
          <el-input v-model="form.driverPhoto" placeholder="请输入照片" />
        </el-form-item>
        <el-form-item label="司机身份证号码" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入司机身份证号码" />
        </el-form-item>
        <el-form-item label="人员IC号" prop="personIc">
          <el-input v-model="form.personIc" placeholder="请输入人员IC号" />
        </el-form-item>
        <el-form-item label="单位名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="预约进入开始时间" prop="reservationEntryStartTime">
          <el-date-picker clearable
            v-model="form.reservationEntryStartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预约进入开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预约进入结束时间" prop="reservationEntryEndTime">
          <el-date-picker clearable
            v-model="form.reservationEntryEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预约进入结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="来访时间" prop="visitingTime">
          <el-date-picker clearable
            v-model="form.visitingTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择来访时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="离开时间" prop="leaveTime">
          <el-date-picker clearable
            v-model="form.leaveTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择离开时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="来访事由" prop="visitingReason">
          <el-input v-model="form.visitingReason" placeholder="请输入来访事由" />
        </el-form-item>
        <el-form-item label="接待人" prop="receiver">
          <el-input v-model="form.receiver" placeholder="请输入接待人" />
        </el-form-item>
        <el-form-item label="接待人联系方式" prop="receiverTel">
          <el-input v-model="form.receiverTel" placeholder="请输入接待人联系方式" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listLanya_vehicleInfo_visitor, getLanya_vehicleInfo_visitor, delLanya_vehicleInfo_visitor, addLanya_vehicleInfo_visitor, updateLanya_vehicleInfo_visitor } from "@/api/system/lanya-vehicleInfo-visitor"

export default {
  name: "Lanya-vehicleInfo-visitor",
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
      // 访客车辆表格数据
      lanya_vehicleInfo_visitorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleId: null,
        vehicleName: null,
        vehicleType: null,
        cardId: null,
        licensePlateNumber: null,
        driverName: null,
        driverTel: null,
        driverPhoto: null,
        idNumber: null,
        personIc: null,
        companyName: null,
        reservationEntryStartTime: null,
        reservationEntryEndTime: null,
        visitingTime: null,
        leaveTime: null,
        visitingStatus: null,
        visitingReason: null,
        receiver: null,
        receiverTel: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehicleName: [
          { required: true, message: "车辆名称不能为空", trigger: "blur" }
        ],
        vehicleType: [
          { required: true, message: "车辆类型不能为空", trigger: "change" }
        ],
        licensePlateNumber: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        driverName: [
          { required: true, message: "司机姓名不能为空", trigger: "blur" }
        ],
        driverTel: [
          { required: true, message: "司机手机号不能为空", trigger: "blur" }
        ],
        visitingStatus: [
          { required: true, message: "来访状态不能为空", trigger: "change" }
        ],
        visitingReason: [
          { required: true, message: "来访事由不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询访客车辆列表 */
    getList() {
      this.loading = true
      listLanya_vehicleInfo_visitor(this.queryParams).then(response => {
        this.lanya_vehicleInfo_visitorList = response.rows
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
        id: null,
        vehicleId: null,
        vehicleName: null,
        vehicleType: null,
        cardId: null,
        licensePlateNumber: null,
        driverName: null,
        driverTel: null,
        driverPhoto: null,
        idNumber: null,
        personIc: null,
        companyName: null,
        reservationEntryStartTime: null,
        reservationEntryEndTime: null,
        visitingTime: null,
        leaveTime: null,
        visitingStatus: null,
        visitingReason: null,
        receiver: null,
        receiverTel: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加访客车辆"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLanya_vehicleInfo_visitor(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改访客车辆"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLanya_vehicleInfo_visitor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_vehicleInfo_visitor(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除访客车辆编号为"' + ids + '"的数据项？').then(function() {
        return delLanya_vehicleInfo_visitor(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya-vehicleInfo-visitor/export', {
        ...this.queryParams
      }, `lanya-vehicleInfo-visitor_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
