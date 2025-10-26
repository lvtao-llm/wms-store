<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入巡检规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务时间" prop="taskTime">
        <el-date-picker clearable
                        v-model="queryParams.taskTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择任务时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="巡检结果" prop="inspectionResult">
        <el-select v-model="queryParams.inspectionResult" placeholder="请选择巡检结果" clearable>
          <el-option
            v-for="dict in dict.type.wms_inspection_result"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择任务状态" clearable>
          <el-option
            v-for="dict in dict.type.wms_create_status"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_inspection_task:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_inspection_taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键ID" align="center" prop="id"/>
      <el-table-column label="巡检规则ID" align="center" prop="ruleId"/>
      <el-table-column label="巡检规则名称" align="center" prop="ruleName"/>
      <el-table-column label="任务时间" align="center" prop="taskTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际巡检数据" align="center" prop="actualInspectionData"/>
      <el-table-column label="巡检结果" align="center" prop="inspectionResult">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_inspection_result" :value="scope.row.inspectionResult"/>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_create_status" :value="scope.row.taskStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_inspection_task:edit']"
          >上报
          </el-button>
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

    <!-- 添加或修改巡检任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
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
  listWms_inspection_task,
  getWms_inspection_task,
  delWms_inspection_task,
  addWms_inspection_task,
  updateWms_inspection_task
} from "@/api/system/wms_inspection_task"

export default {
  name: "Wms_inspection_task",
  dicts: ['wms_create_status', 'wms_inspection_result'],
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
      // 巡检任务表格数据
      wms_inspection_taskList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        ruleName: null,
        taskTime: null,
        actualInspectionData: null,
        inspectionResult: null,
        taskStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleId: [
          {required: true, message: "巡检规则ID不能为空", trigger: "blur"}
        ],
        ruleName: [
          {required: true, message: "巡检规则名称不能为空", trigger: "blur"}
        ],
        taskTime: [
          {required: true, message: "任务时间不能为空", trigger: "blur"}
        ],
        inspectionResult: [
          {required: true, message: "巡检结果不能为空", trigger: "change"}
        ],
        taskStatus: [
          {required: true, message: "任务状态不能为空", trigger: "change"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询巡检任务列表 */
    getList() {
      this.loading = true
      listWms_inspection_task(this.queryParams).then(response => {
        this.wms_inspection_taskList = response.rows
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
        ruleId: null,
        ruleName: null,
        taskTime: null,
        actualInspectionData: null,
        inspectionResult: null,
        taskStatus: null,
        createTime: null,
        updateTime: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加巡检任务"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // 跳转到巡检问题页面，传递任务ID和返回路径
      this.$router.push({
        path: '/inspection/issue',
        query: {
          taskId: row.id,
          ruleId: row.ruleId,
          from: 'task' // 标记来源页面
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_inspection_task(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_inspection_task(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除巡检任务编号为"' + ids + '"的数据项？').then(function () {
        return delWms_inspection_task(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_inspection_task/export', {
        ...this.queryParams
      }, `wms_inspection_task_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
