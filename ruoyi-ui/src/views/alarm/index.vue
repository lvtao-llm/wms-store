<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="卡ID" prop="cardRecordId">
        <el-input
          v-model="queryParams.cardRecordId"
          placeholder="请输入发卡记录ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="personName">
        <el-input
          v-model="queryParams.personName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="personJobNo">
        <el-input
          v-model="queryParams.personJobNo"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属部门" prop="personDept">
        <el-input
          v-model="queryParams.personDept"
          placeholder="请输入所属部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="vehiclePlateNo">
        <el-input
          v-model="queryParams.vehiclePlateNo"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在区域" prop="areaCode">
        <el-input
          v-model="queryParams.areaCode"
          placeholder="请输入所在区域CODE"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="进入时间" prop="alarmEnterTime">
        <el-date-picker clearable
          v-model="queryParams.alarmEnterTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择进入时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="异常行为" prop="alarmBehavior">
        <el-input
          v-model="queryParams.alarmBehavior"
          placeholder="请输入异常行为"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理结果" prop="alarmResult">
        <el-input
          v-model="queryParams.alarmResult"
          placeholder="请输入处理结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理人员" prop="alarmHandler">
        <el-input
          v-model="queryParams.alarmHandler"
          placeholder="请输入处理人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理时间" prop="alarmHandleTime">
        <el-date-picker clearable
          v-model="queryParams.alarmHandleTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间">
        </el-date-picker>
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
          v-hasPermi="['system:alarm:add']"
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
          v-hasPermi="['system:alarm:edit']"
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
          v-hasPermi="['system:alarm:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:alarm:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="alarmList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="alarmId" />
      <el-table-column label="报警类型" align="center" prop="alarmType" />
      <el-table-column label="卡ID" align="center" prop="cardRecordId" />
      <el-table-column label="姓名" align="center" prop="personName" />
      <el-table-column label="工号" align="center" prop="personJobNo" />
      <el-table-column label="所属部门" align="center" prop="personDept" />
      <el-table-column label="车牌号" align="center" prop="vehiclePlateNo" />
      <el-table-column label="车型" align="center" prop="vehicleType" />
      <el-table-column label="所在位置" align="center" prop="alarmLocation" />
      <el-table-column label="所在区域" align="center" prop="areaCode" />
      <el-table-column label="进入时间" align="center" prop="alarmEnterTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmEnterTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常行为" align="center" prop="alarmBehavior" />
      <el-table-column label="处理结果" align="center" prop="alarmResult" />
      <el-table-column label="处理人员" align="center" prop="alarmHandler" />
      <el-table-column label="处理时间" align="center" prop="alarmHandleTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmHandleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:alarm:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:alarm:remove']"
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

    <!-- 添加或修改报警信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发卡记录ID" prop="cardRecordId">
          <el-input v-model="form.cardRecordId" placeholder="请输入发卡记录ID" />
        </el-form-item>
        <el-form-item label="姓名" prop="personName">
          <el-input v-model="form.personName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="工号" prop="personJobNo">
          <el-input v-model="form.personJobNo" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="所属部门" prop="personDept">
          <el-input v-model="form.personDept" placeholder="请输入所属部门" />
        </el-form-item>
        <el-form-item label="车牌号" prop="vehiclePlateNo">
          <el-input v-model="form.vehiclePlateNo" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="所在位置{x,y}" prop="alarmLocation">
          <el-input v-model="form.alarmLocation" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="所在区域CODE" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入所在区域CODE" />
        </el-form-item>
        <el-form-item label="进入时间" prop="alarmEnterTime">
          <el-date-picker clearable
            v-model="form.alarmEnterTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择进入时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="异常行为" prop="alarmBehavior">
          <el-input v-model="form.alarmBehavior" placeholder="请输入异常行为" />
        </el-form-item>
        <el-form-item label="处理结果" prop="alarmResult">
          <el-input v-model="form.alarmResult" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="处理人员" prop="alarmHandler">
          <el-input v-model="form.alarmHandler" placeholder="请输入处理人员" />
        </el-form-item>
        <el-form-item label="处理时间" prop="alarmHandleTime">
          <el-date-picker clearable
            v-model="form.alarmHandleTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择处理时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="${comment}" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入${comment}" />
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
import { listAlarm, getAlarm, delAlarm, addAlarm, updateAlarm } from "@/api/system/alarm"

export default {
  name: "Alarm",
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
      // 报警信息表格数据
      alarmList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmType: null,
        cardRecordId: null,
        personName: null,
        personJobNo: null,
        personDept: null,
        vehiclePlateNo: null,
        vehicleType: null,
        alarmLocation: null,
        areaCode: null,
        alarmEnterTime: null,
        alarmBehavior: null,
        alarmResult: null,
        alarmHandler: null,
        alarmHandleTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        alarmType: [
          { required: true, message: "报警类型：0人员，1车辆不能为空", trigger: "change" }
        ],
        cardRecordId: [
          { required: true, message: "发卡记录ID不能为空", trigger: "blur" }
        ],
        personName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        personJobNo: [
          { required: true, message: "工号不能为空", trigger: "blur" }
        ],
        vehiclePlateNo: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        alarmLocation: [
          { required: true, message: "所在位置{x,y}不能为空", trigger: "blur" }
        ],
        areaCode: [
          { required: true, message: "所在区域CODE不能为空", trigger: "blur" }
        ],
        alarmEnterTime: [
          { required: true, message: "进入时间不能为空", trigger: "blur" }
        ],
        alarmBehavior: [
          { required: true, message: "异常行为不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报警信息列表 */
    getList() {
      this.loading = true
      listAlarm(this.queryParams).then(response => {
        this.alarmList = response.rows
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
        alarmId: null,
        alarmType: null,
        cardRecordId: null,
        personName: null,
        personJobNo: null,
        personDept: null,
        vehiclePlateNo: null,
        vehicleType: null,
        alarmLocation: null,
        areaCode: null,
        alarmEnterTime: null,
        alarmBehavior: null,
        alarmResult: null,
        alarmHandler: null,
        alarmHandleTime: null,
        createTime: null,
        updateTime: null,
        delFlag: null
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
      this.ids = selection.map(item => item.alarmId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报警信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const alarmId = row.alarmId || this.ids
      getAlarm(alarmId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改报警信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.alarmId != null) {
            updateAlarm(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAlarm(this.form).then(response => {
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
      const alarmIds = row.alarmId || this.ids
      this.$modal.confirm('是否确认删除报警信息编号为"' + alarmIds + '"的数据项？').then(function() {
        return delAlarm(alarmIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/alarm/export', {
        ...this.queryParams
      }, `alarm_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
