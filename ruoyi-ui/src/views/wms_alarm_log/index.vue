<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['system:wms_alarm_log:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_alarm_logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="姓名" align="center" prop="personName"/>
      <el-table-column label="工号" align="center" prop="personJobNo"/>
      <el-table-column label="所属部门" align="center" prop="personDept"/>
      <el-table-column label="所在区域" align="center" prop="areaName"/>
      <el-table-column label="进入时间" align="center" prop="alarmEnterTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmEnterTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常行为" align="center" prop="alarmBehavior"/>
      <el-table-column label="处理结果" align="center" prop="alarmResult"/>
      <el-table-column label="处理人员" align="center" prop="alarmHandler"/>
      <el-table-column label="处理时间" align="center" prop="alarmHandleTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmHandleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
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

  </div>
</template>

<script>
import {listWms_alarm_log} from "@/api/system/wms_alarm_log"
import {listArea} from "@/api/system/wms_area"

export default {
  name: "Wms_alarm_log",
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
      // 报警信息记录表格数据
      wms_alarm_logList: [],
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
      // 表单校验
      areas: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报警信息记录列表 */
    getList() {
      this.loading = true
      listArea().then(response => {
        this.areas = response.rows
        listWms_alarm_log(this.queryParams).then(response => {
          response.rows.forEach(row => {
            if (row.areaCode && typeof row.areaCode === 'string') {
              row.areaName = this.areas.find(area => area.areaId === row.areaCode)
            } else if (!row.areaCode) {
              row.areaName = []
            }
          })

          this.wms_alarm_logList = response.rows
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_alarm_log/export', {
        ...this.queryParams
      }, `wms_alarm_log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
