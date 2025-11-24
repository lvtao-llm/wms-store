<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="后台任务名称" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入后台任务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="后台任务类别" prop="jobGroup">
        <el-input
          v-model="queryParams.jobGroup"
          placeholder="请输入后台任务类别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择执行状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['system:wms_system_log:add']"
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
          v-hasPermi="['system:wms_system_log:edit']"
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
          v-hasPermi="['system:wms_system_log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_system_log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_system_logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志ID" align="center" prop="jobLogId" />
      <el-table-column label="后台任务名称" align="center" prop="jobName" />
      <el-table-column label="后台任务类别" align="center" prop="jobGroup" />
      <el-table-column label="调用目标字符串" align="center" prop="invokeTarget" />
      <el-table-column label="日志信息" align="center" prop="jobMessage" />
      <el-table-column label="执行状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_system_log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_system_log:remove']"
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

    <!-- 添加或修改定时任务调度日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="后台任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入后台任务名称" />
        </el-form-item>
        <el-form-item label="后台任务类别" prop="jobGroup">
          <el-input v-model="form.jobGroup" placeholder="请输入后台任务类别" />
        </el-form-item>
        <el-form-item label="调用目标字符串" prop="invokeTarget">
          <el-input v-model="form.invokeTarget" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="日志信息" prop="jobMessage">
          <el-input v-model="form.jobMessage" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="执行状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_common_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="异常信息" prop="exceptionInfo">
          <el-input v-model="form.exceptionInfo" type="textarea" placeholder="请输入内容" />
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
import { listWms_system_log, getWms_system_log, delWms_system_log, addWms_system_log, updateWms_system_log } from "@/api/system/wms_system_log"

export default {
  name: "Wms_system_log",
  dicts: ['sys_common_status'],
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
      // 定时任务调度日志表格数据
      wms_system_logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: null,
        jobGroup: null,
        invokeTarget: null,
        jobMessage: null,
        status: null,
        exceptionInfo: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        jobName: [
          { required: true, message: "后台任务名称不能为空", trigger: "blur" }
        ],
        jobGroup: [
          { required: true, message: "后台任务类别不能为空", trigger: "blur" }
        ],
        invokeTarget: [
          { required: true, message: "调用目标字符串不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询定时任务调度日志列表 */
    getList() {
      this.loading = true
      listWms_system_log(this.queryParams).then(response => {
        this.wms_system_logList = response.rows
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
        jobLogId: null,
        jobName: null,
        jobGroup: null,
        invokeTarget: null,
        jobMessage: null,
        status: null,
        exceptionInfo: null,
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
      this.ids = selection.map(item => item.jobLogId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加定时任务调度日志"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const jobLogId = row.jobLogId || this.ids
      getWms_system_log(jobLogId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改定时任务调度日志"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.jobLogId != null) {
            updateWms_system_log(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_system_log(this.form).then(response => {
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
      const jobLogIds = row.jobLogId || this.ids
      this.$modal.confirm('是否确认删除定时任务调度日志编号为"' + jobLogIds + '"的数据项？').then(function() {
        return delWms_system_log(jobLogIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_system_log/export', {
        ...this.queryParams
      }, `wms_system_log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
