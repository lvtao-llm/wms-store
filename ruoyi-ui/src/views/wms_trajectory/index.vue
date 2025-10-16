<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="开始时间" prop="trajectoryBegin">
        <el-date-picker clearable
          v-model="queryParams.trajectoryBegin"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="trajectoryEnd">
        <el-date-picker clearable
          v-model="queryParams.trajectoryEnd"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="卡ID" prop="cardRecordId">
        <el-input
          v-model="queryParams.cardRecordId"
          placeholder="请输入发卡记录ID"
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
          v-hasPermi="['system:trajectory:add']"
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
          v-hasPermi="['system:trajectory:edit']"
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
          v-hasPermi="['system:trajectory:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:trajectory:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="trajectoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="trajectoryId" />
      <el-table-column label="轨迹类型" align="center" prop="trajectoryType" />
      <el-table-column label="开始时间" align="center" prop="trajectoryBegin" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trajectoryBegin, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="trajectoryEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trajectoryEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="轨迹" align="center" prop="trajectoryPoints" />
      <el-table-column label="卡ID" align="center" prop="cardRecordId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:trajectory:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:trajectory:remove']"
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

    <!-- 添加或修改轨迹对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="开始时间" prop="trajectoryBegin">
          <el-date-picker clearable
            v-model="form.trajectoryBegin"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="trajectoryEnd">
          <el-date-picker clearable
            v-model="form.trajectoryEnd"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="轨迹：[{x,y,t}]" prop="trajectoryPoints">
          <el-input v-model="form.trajectoryPoints" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="发卡记录ID" prop="cardRecordId">
          <el-input v-model="form.cardRecordId" placeholder="请输入发卡记录ID" />
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
import { listTrajectory, getTrajectory, delTrajectory, addTrajectory, updateTrajectory } from "@/api/system/wms_trajectory"

export default {
  name: "Trajectory",
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
      // 轨迹表格数据
      trajectoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        trajectoryType: null,
        trajectoryBegin: null,
        trajectoryEnd: null,
        trajectoryPoints: null,
        cardRecordId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        trajectoryType: [
          { required: true, message: "轨迹类型。1人员 2车辆不能为空", trigger: "change" }
        ],
        trajectoryBegin: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        cardRecordId: [
          { required: true, message: "发卡记录ID不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询轨迹列表 */
    getList() {
      this.loading = true
      listTrajectory(this.queryParams).then(response => {
        this.trajectoryList = response.rows
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
        trajectoryId: null,
        trajectoryType: null,
        trajectoryBegin: null,
        trajectoryEnd: null,
        trajectoryPoints: null,
        cardRecordId: null
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
      this.ids = selection.map(item => item.trajectoryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加轨迹"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const trajectoryId = row.trajectoryId || this.ids
      getTrajectory(trajectoryId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改轨迹"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.trajectoryId != null) {
            updateTrajectory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTrajectory(this.form).then(response => {
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
      const trajectoryIds = row.trajectoryId || this.ids
      this.$modal.confirm('是否确认删除轨迹编号为"' + trajectoryIds + '"的数据项？').then(function() {
        return delTrajectory(trajectoryIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/trajectory/export', {
        ...this.queryParams
      }, `trajectory_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
