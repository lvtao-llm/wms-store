<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则名称" prop="alarmRuleName">
        <el-input
          v-model="queryParams.alarmRuleName"
          placeholder="请输入规则名称"
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
          v-hasPermi="['system:wms_alarm_rule:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:wms_alarm_rule:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:wms_alarm_rule:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_alarm_rule:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_alarm_ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="规则名称" align="center" prop="alarmRuleName"/>
      <el-table-column label="时间阈值" align="center" prop="alarmRuleTimeThreshold"/>
      <el-table-column label="距离阈值" align="center" prop="alarmRuleDistThreshold"/>
      <el-table-column label="目标区域" align="center" prop="alarmRuleTargetAreaName"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_alarm_rule:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_alarm_rule:remove']"
          >删除
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

    <!-- 添加或修改报警信息规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则名称" prop="alarmRuleName">
          <el-input v-model="form.alarmRuleName" placeholder="请输入规则名称"/>
        </el-form-item>
        <el-form-item label="时间阈值" prop="alarmRuleTimeThreshold">
          <el-input v-model="form.alarmRuleTimeThreshold" placeholder="请输入时间阈值"/>
        </el-form-item>
        <el-form-item label="距离阈值" prop="alarmRuleDistThreshold">
          <el-input v-model="form.alarmRuleDistThreshold" placeholder="请输入距离阈值"/>
        </el-form-item>
        <el-form-item label="目标区域" prop="alarmRuleTargetAreaCode">
          <el-select v-model="form.alarmRuleTargetAreaCode" placeholder="请选择目标区域" multiple>
            <el-option
              v-for="area in areas"
              :key="area.areaId"
              :label="area.areaName"
              :value="area.areaId"/>
          </el-select>
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
  listWms_alarm_rule,
  getWms_alarm_rule,
  delWms_alarm_rule,
  addWms_alarm_rule,
  updateWms_alarm_rule
} from "@/api/system/wms_alarm_rule"
import {listArea} from "@/api/system/wms_area"

export default {
  name: "Wms_alarm_rule",
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
      // 报警信息规则表格数据
      wms_alarm_ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmRuleId: null,
        alarmRuleName: null,
        alarmRuleTimeThreshold: null,
        alarmRuleDistThreshold: null,
        alarmRuleTargetAreaCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 区域列表
      areas: [],
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报警信息规则列表 */
    getList() {
      listArea().then(response => {
        this.areas = response.rows
        listWms_alarm_rule(this.queryParams).then(response => {
          response.rows.forEach(row => {
            if (row.alarmRuleTargetAreaCode && typeof row.alarmRuleTargetAreaCode === 'string') {
              row.alarmRuleTargetAreaCode = row.alarmRuleTargetAreaCode.split(',').map(id => parseInt(id, 10))
              row.alarmRuleTargetAreaName = row.alarmRuleTargetAreaCode.map(areaId => {
                const area = this.areas.find(area => area.areaId === areaId)
                return area ? area.areaName : ''
              }).join(",")
            } else if (!row.alarmRuleTargetAreaCode) {
              row.alarmRuleTargetAreaCode = []
              row.alarmRuleTargetAreaName = []
            }
          })
          this.wms_alarm_ruleList = response.rows
          console.log(this.wms_alarm_ruleList)
          this.total = response.total
          this.loading = false
        })
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
        alarmRuleId: null,
        alarmRuleName: null,
        alarmRuleTimeThreshold: null,
        alarmRuleDistThreshold: null,
        alarmRuleTargetAreaCode: null
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
      this.ids = selection.map(item => item.alarmRuleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报警信息规则"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const alarmRuleId = row.alarmRuleId || this.ids
      console.log(row, alarmRuleId)
      getWms_alarm_rule(alarmRuleId).then(response => {
        if (response.data.alarmRuleTargetAreaCode && typeof response.data.alarmRuleTargetAreaCode === 'string') {
          response.data.alarmRuleTargetAreaCode = response.data.alarmRuleTargetAreaCode.split(',').map(id => parseInt(id, 10))
        } else if (!response.data.alarmRuleTargetAreaCode) {
          response.data.alarmRuleTargetAreaCode = []
          response.data.alarmRuleTargetAreaName = []
        }
        this.form = response.data
        this.open = true
        this.title = "修改报警信息规则"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log('bbbbbbbbbb', this.form.alarmRuleTargetAreaCode)
          this.form.alarmRuleTargetAreaCode = this.form.alarmRuleTargetAreaCode.join(",")
          if (this.form.alarmRuleId != null) {
            updateWms_alarm_rule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_alarm_rule(this.form).then(response => {
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
      const alarmRuleIds = row.alarmRuleId || this.ids
      this.$modal.confirm('是否确认删除报警信息规则编号为"' + alarmRuleIds + '"的数据项？').then(function () {
        return delWms_alarm_rule(alarmRuleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_alarm_rule/export', {
        ...this.queryParams
      }, `wms_alarm_rule_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
