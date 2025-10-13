<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备SN" prop="deviceSn">
        <el-input
          v-model="queryParams.deviceSn"
          placeholder="请输入设备SN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="充电口号" prop="deviceNum">
        <el-input
          v-model="queryParams.deviceNum"
          placeholder="请输入充电口号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位卡号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入定位卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下发时间" prop="commandTime">
        <el-date-picker clearable
          v-model="queryParams.commandTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择下发时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结果" prop="result">
        <el-input
          v-model="queryParams.result"
          placeholder="请输入结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人员id" prop="personId">
        <el-input
          v-model="queryParams.personId"
          placeholder="请输入人员id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门id" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入部门id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门名称" prop="deptName">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入部门名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="jobNumber">
        <el-input
          v-model="queryParams.jobNumber"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证件号码" prop="idNumber">
        <el-input
          v-model="queryParams.idNumber"
          placeholder="请输入证件号码"
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
      <el-form-item label="设备主板" prop="deviceAims">
        <el-input
          v-model="queryParams.deviceAims"
          placeholder="请输入设备主板"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="认证时间" prop="identifyTime">
        <el-date-picker clearable
          v-model="queryParams.identifyTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择认证时间(人脸机)">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="闭环时间" prop="closedTime">
        <el-date-picker clearable
          v-model="queryParams.closedTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择闭环时间(发卡机)">
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
          v-hasPermi="['system:lanya_device_card_sender_log:add']"
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
          v-hasPermi="['system:lanya_device_card_sender_log:edit']"
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
          v-hasPermi="['system:lanya_device_card_sender_log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_device_card_sender_log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_device_card_sender_logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" width="180"/>
      <el-table-column label="设备SN" align="center" prop="deviceSn"  width="180"/>
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="充电口号" align="center" prop="deviceNum" />
      <el-table-column label="定位卡号" align="center" prop="cardId" />
      <el-table-column label="下发时间" align="center" prop="commandTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.commandTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发卡类型" align="center" prop="cardSenderType" />
      <el-table-column label="结果" align="center" prop="result" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="姓名" align="center" prop="realName" />
      <el-table-column label="部门id" align="center" prop="deptId" />
      <el-table-column label="部门名称" align="center" prop="deptName" />
      <el-table-column label="工号" align="center" prop="jobNumber" />
      <el-table-column label="证件号码" align="center" prop="idNumber"  width="180"/>
      <el-table-column label="人员IC号" align="center" prop="personIc" />
      <el-table-column label="设备主板" align="center" prop="deviceAims" />
      <el-table-column label="认证方式" align="center" prop="identifyType" />
      <el-table-column label="发卡方式" align="center" prop="rentType" />
      <el-table-column label="认证时间" align="center" prop="identifyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.identifyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="闭环时间" align="center" prop="closedTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closedTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="三方平台通知结果" align="center" prop="notifyStatus"  width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_device_card_sender_log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_device_card_sender_log:remove']"
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

    <!-- 添加或修改人脸发卡记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备SN" prop="deviceSn">
          <el-input v-model="form.deviceSn" placeholder="请输入设备SN" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="充电口号" prop="deviceNum">
          <el-input v-model="form.deviceNum" placeholder="请输入充电口号" />
        </el-form-item>
        <el-form-item label="定位卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入定位卡号" />
        </el-form-item>
        <el-form-item label="人脸识别照片" prop="personPhoto">
          <el-input v-model="form.personPhoto" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="下发时间" prop="commandTime">
          <el-date-picker clearable
            v-model="form.commandTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择下发时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结果" prop="result">
          <el-input v-model="form.result" placeholder="请输入结果" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="人员id" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入人员id" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="部门id" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门id" />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="工号" prop="jobNumber">
          <el-input v-model="form.jobNumber" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="证件号码" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入证件号码" />
        </el-form-item>
        <el-form-item label="人员IC号" prop="personIc">
          <el-input v-model="form.personIc" placeholder="请输入人员IC号" />
        </el-form-item>
        <el-form-item label="设备主板" prop="deviceAims">
          <el-input v-model="form.deviceAims" placeholder="请输入设备主板" />
        </el-form-item>
        <el-form-item label="认证时间(人脸机)" prop="identifyTime">
          <el-date-picker clearable
            v-model="form.identifyTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择认证时间(人脸机)">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="闭环时间(发卡机)" prop="closedTime">
          <el-date-picker clearable
            v-model="form.closedTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择闭环时间(发卡机)">
          </el-date-picker>
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
import { listLanya_device_card_sender_log, getLanya_device_card_sender_log, delLanya_device_card_sender_log, addLanya_device_card_sender_log, updateLanya_device_card_sender_log } from "@/api/system/lanya_device_card_sender_log"

export default {
  name: "Lanya_device_card_sender_log",
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
      // 人脸发卡记录表格数据
      lanya_device_card_sender_logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceSn: null,
        deviceName: null,
        deviceNum: null,
        cardId: null,
        personPhoto: null,
        commandTime: null,
        cardSenderType: null,
        result: null,
        personId: null,
        realName: null,
        deptId: null,
        deptName: null,
        jobNumber: null,
        idNumber: null,
        personIc: null,
        deviceAims: null,
        identifyType: null,
        rentType: null,
        identifyTime: null,
        closedTime: null,
        notifyStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardSenderType: [
          { required: true, message: "0:还卡 1：发卡不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询人脸发卡记录列表 */
    getList() {
      this.loading = true
      listLanya_device_card_sender_log(this.queryParams).then(response => {
        this.lanya_device_card_sender_logList = response.rows
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
        deviceSn: null,
        deviceName: null,
        deviceNum: null,
        cardId: null,
        personPhoto: null,
        commandTime: null,
        cardSenderType: null,
        result: null,
        remark: null,
        createTime: null,
        personId: null,
        realName: null,
        deptId: null,
        deptName: null,
        jobNumber: null,
        idNumber: null,
        personIc: null,
        deviceAims: null,
        identifyType: null,
        rentType: null,
        identifyTime: null,
        closedTime: null,
        updateTime: null,
        notifyStatus: null
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
      this.title = "添加人脸发卡记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLanya_device_card_sender_log(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改人脸发卡记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLanya_device_card_sender_log(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_device_card_sender_log(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除人脸发卡记录编号为"' + ids + '"的数据项？').then(function() {
        return delLanya_device_card_sender_log(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_device_card_sender_log/export', {
        ...this.queryParams
      }, `lanya_device_card_sender_log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
