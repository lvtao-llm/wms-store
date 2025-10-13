<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="区域名称" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入区域名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生效开始日期" prop="validBeginDate">
        <el-date-picker clearable
          v-model="queryParams.validBeginDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生效开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="生效结束日期" prop="validEndDate">
        <el-date-picker clearable
          v-model="queryParams.validEndDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生效结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="生效开始时段" prop="validBeginTime">
        <el-date-picker clearable
          v-model="queryParams.validBeginTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生效开始时段">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="生效结束时段" prop="validEndTime">
        <el-date-picker clearable
          v-model="queryParams.validEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生效结束时段">
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
          v-hasPermi="['system:lanya_core_area:add']"
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
          v-hasPermi="['system:lanya_core_area:edit']"
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
          v-hasPermi="['system:lanya_core_area:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_core_area:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_core_areaList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="区域id" align="center" prop="areaId" />
      <el-table-column label="区域名称" align="center" prop="areaName" />
      <el-table-column label="启用状态" align="center" prop="areaEnable" />
      <el-table-column label="报警类型" align="center" prop="alarmType" />
      <el-table-column label="报警规则" align="center" prop="rule" />
      <el-table-column label="生效开始日期" align="center" prop="validBeginDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.validBeginDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效结束日期" align="center" prop="validEndDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.validEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效开始时段" align="center" prop="validBeginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.validBeginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效结束时段" align="center" prop="validEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.validEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="通知配置id" align="center" prop="alarmNoticeId" width="100"/>
      <el-table-column label="2级聚集报警" align="center" prop="secondAlarmRule" width="100"/>
      <el-table-column label="2级报警通知id" align="center" prop="secondAlarmNoticeId" width="120"/>
      <el-table-column label="3级聚集报警" align="center" prop="thirdAlarmRule" width="100"/>
      <el-table-column label="3级报警通知id" align="center" prop="thirdAlarmNoticeId" width="120"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_core_area:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_core_area:remove']"
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

    <!-- 添加或修改区域信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区域名称" />
        </el-form-item>
        <el-form-item label="启用状态Y|N" prop="areaEnable">
          <el-input v-model="form.areaEnable" placeholder="请输入启用状态Y|N" />
        </el-form-item>
        <el-form-item label="报警规则(人/秒/1级聚集报警:聚集半径,持续时长,人数)" prop="rule">
          <el-input v-model="form.rule" placeholder="请输入报警规则(人/秒/1级聚集报警:聚集半径,持续时长,人数)" />
        </el-form-item>
        <el-form-item label="生效开始日期" prop="validBeginDate">
          <el-date-picker clearable
            v-model="form.validBeginDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生效开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="生效结束日期" prop="validEndDate">
          <el-date-picker clearable
            v-model="form.validEndDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生效结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="生效开始时段" prop="validBeginTime">
          <el-date-picker clearable
            v-model="form.validBeginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生效开始时段">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="生效结束时段" prop="validEndTime">
          <el-date-picker clearable
            v-model="form.validEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生效结束时段">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="通知配置id(表core_alarm_notice)" prop="alarmNoticeId">
          <el-input v-model="form.alarmNoticeId" placeholder="请输入通知配置id(表core_alarm_notice)" />
        </el-form-item>
        <el-form-item label="2级聚集报警:聚集半径,持续时长,人数" prop="secondAlarmRule">
          <el-input v-model="form.secondAlarmRule" placeholder="请输入2级聚集报警:聚集半径,持续时长,人数" />
        </el-form-item>
        <el-form-item label="2级报警通知id" prop="secondAlarmNoticeId">
          <el-input v-model="form.secondAlarmNoticeId" placeholder="请输入2级报警通知id" />
        </el-form-item>
        <el-form-item label="3级聚集报警:聚集半径,持续时长,人数" prop="thirdAlarmRule">
          <el-input v-model="form.thirdAlarmRule" placeholder="请输入3级聚集报警:聚集半径,持续时长,人数" />
        </el-form-item>
        <el-form-item label="3级报警通知id" prop="thirdAlarmNoticeId">
          <el-input v-model="form.thirdAlarmNoticeId" placeholder="请输入3级报警通知id" />
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
import { listLanya_core_area, getLanya_core_area, delLanya_core_area, addLanya_core_area, updateLanya_core_area } from "@/api/system/lanya_core_area"

export default {
  name: "Lanya_core_area",
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
      // 区域信息表格数据
      lanya_core_areaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        areaName: null,
        areaEnable: null,
        alarmType: null,
        rule: null,
        validBeginDate: null,
        validEndDate: null,
        validBeginTime: null,
        validEndTime: null,
        alarmNoticeId: null,
        secondAlarmRule: null,
        secondAlarmNoticeId: null,
        thirdAlarmRule: null,
        thirdAlarmNoticeId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        areaName: [
          { required: true, message: "区域名称不能为空", trigger: "blur" }
        ],
        areaEnable: [
          { required: true, message: "启用状态Y|N不能为空", trigger: "blur" }
        ],
        alarmType: [
          { required: true, message: "报警类型不能为空", trigger: "change" }
        ],
        validBeginTime: [
          { required: true, message: "生效开始时段不能为空", trigger: "blur" }
        ],
        validEndTime: [
          { required: true, message: "生效结束时段不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询区域信息列表 */
    getList() {
      this.loading = true
      listLanya_core_area(this.queryParams).then(response => {
        this.lanya_core_areaList = response.rows
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
        areaId: null,
        areaName: null,
        areaEnable: null,
        alarmType: null,
        rule: null,
        validBeginDate: null,
        validEndDate: null,
        validBeginTime: null,
        validEndTime: null,
        alarmNoticeId: null,
        secondAlarmRule: null,
        secondAlarmNoticeId: null,
        thirdAlarmRule: null,
        thirdAlarmNoticeId: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
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
      this.ids = selection.map(item => item.areaId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加区域信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const areaId = row.areaId || this.ids
      getLanya_core_area(areaId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改区域信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.areaId != null) {
            updateLanya_core_area(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_core_area(this.form).then(response => {
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
      const areaIds = row.areaId || this.ids
      this.$modal.confirm('是否确认删除区域信息编号为"' + areaIds + '"的数据项？').then(function() {
        return delLanya_core_area(areaIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_core_area/export', {
        ...this.queryParams
      }, `lanya_core_area_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
