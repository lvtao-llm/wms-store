<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="报警名称" prop="alarmName">
        <el-input
          v-model="queryParams.alarmName"
          placeholder="请输入报警名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报警区域名称" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入报警区域名称"
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
      <el-form-item label="报警人" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入报警人"
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
          v-hasPermi="['system:lanya_core_alarm:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_core_alarmList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="信号接收时间" align="center" prop="acceptTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.acceptTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报警名称" align="center" prop="alarmName" width="200"/>
      <el-table-column label="报警描述" align="center" prop="alarmDesc" width="300"/>
      <el-table-column label="报警状态" align="center" prop="alarmStatus"/>
      <el-table-column label="报警区域名称" align="center" prop="areaName" width="100"/>
      <el-table-column label="经度" align="center" prop="longitude"/>
      <el-table-column label="纬度" align="center" prop="latitude"/>
      <el-table-column label="定位卡号" align="center" prop="cardId"/>
      <el-table-column label="报警人" align="center" prop="realName" width="180"/>
      <el-table-column label="处理人" align="center" prop="disposeBy"/>
      <el-table-column label="处理时间" align="center" prop="disposeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.disposeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理反馈" align="center" prop="disposeFeedback"/>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
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

    <!-- 添加或修改报警记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="信号接收时间" prop="acceptTime">
          <el-date-picker clearable
                          v-model="form.acceptTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择信号接收时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报警类型名称" prop="alarmTypeName">
          <el-input v-model="form.alarmTypeName" placeholder="请输入报警类型名称"/>
        </el-form-item>
        <el-form-item label="报警名称" prop="alarmName">
          <el-input v-model="form.alarmName" placeholder="请输入报警名称"/>
        </el-form-item>
        <el-form-item label="报警描述" prop="alarmDesc">
          <el-input v-model="form.alarmDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="报警区域ID" prop="areaId">
          <el-input v-model="form.areaId" placeholder="请输入报警区域ID"/>
        </el-form-item>
        <el-form-item label="报警区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入报警区域名称"/>
        </el-form-item>
        <el-form-item label="围栏id" prop="railId">
          <el-input v-model="form.railId" placeholder="请输入围栏id"/>
        </el-form-item>
        <el-form-item label="围栏名称" prop="railName">
          <el-input v-model="form.railName" placeholder="请输入围栏名称"/>
        </el-form-item>
        <el-form-item label="楼层" prop="layerId">
          <el-input v-model="form.layerId" placeholder="请输入楼层"/>
        </el-form-item>
        <el-form-item label="楼高" prop="layerHeight">
          <el-input v-model="form.layerHeight" placeholder="请输入楼高"/>
        </el-form-item>
        <el-form-item label="围栏坐标" prop="railScope">
          <el-input v-model="form.railScope" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="围栏高度" prop="railHeight">
          <el-input v-model="form.railHeight" placeholder="请输入围栏高度"/>
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度"/>
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度"/>
        </el-form-item>
        <el-form-item label="信标id" prop="beaconId">
          <el-input v-model="form.beaconId" placeholder="请输入信标id"/>
        </el-form-item>
        <el-form-item label="定位卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入定位卡号"/>
        </el-form-item>
        <el-form-item label="人员id" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入人员id"/>
        </el-form-item>
        <el-form-item label="报警人" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入报警人"/>
        </el-form-item>
        <el-form-item label="工号" prop="jobNumber">
          <el-input v-model="form.jobNumber" placeholder="请输入工号"/>
        </el-form-item>
        <el-form-item label="管理人员姓名" prop="administratorName">
          <el-input v-model="form.administratorName" placeholder="请输入管理人员姓名"/>
        </el-form-item>
        <el-form-item label="管理人员号码" prop="administratorPhone">
          <el-input v-model="form.administratorPhone" placeholder="请输入管理人员号码"/>
        </el-form-item>
        <el-form-item label="部门ID" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门ID"/>
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称"/>
        </el-form-item>
        <el-form-item label="岗位id" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入岗位id"/>
        </el-form-item>
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称"/>
        </el-form-item>
        <el-form-item label="所属承包商" prop="contractorId">
          <el-input v-model="form.contractorId" placeholder="请输入所属承包商"/>
        </el-form-item>
        <el-form-item label="承包商名称" prop="contractorName">
          <el-input v-model="form.contractorName" placeholder="请输入承包商名称"/>
        </el-form-item>
        <el-form-item label="处理人" prop="disposeBy">
          <el-input v-model="form.disposeBy" placeholder="请输入处理人"/>
        </el-form-item>
        <el-form-item label="处理时间" prop="disposeTime">
          <el-date-picker clearable
                          v-model="form.disposeTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择处理时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理反馈" prop="disposeFeedback">
          <el-input v-model="form.disposeFeedback" placeholder="请输入处理反馈"/>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报警级别: 1/2/3级报警" prop="alarmLevel">
          <el-input v-model="form.alarmLevel" placeholder="请输入报警级别: 1/2/3级报警"/>
        </el-form-item>
        <el-form-item label="最高报警级别" prop="highestAlarmLevel">
          <el-input v-model="form.highestAlarmLevel" placeholder="请输入最高报警级别"/>
        </el-form-item>
        <el-form-item label="所属楼宇id" prop="buildingId">
          <el-input v-model="form.buildingId" placeholder="请输入所属楼宇id"/>
        </el-form-item>
        <el-form-item label="所属楼宇名称" prop="buildingName">
          <el-input v-model="form.buildingName" placeholder="请输入所属楼宇名称"/>
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
  listLanya_core_alarm,
  getLanya_core_alarm,
  delLanya_core_alarm,
  addLanya_core_alarm,
  updateLanya_core_alarm
} from "@/api/system/lanya_core_alarm"

export default {
  name: "Lanya_core_alarm",
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
      // 报警记录表格数据
      lanya_core_alarmList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        acceptTime: null,
        alarmType: 95,
        alarmTypeName: null,
        alarmName: null,
        alarmDesc: null,
        alarmStatus: null,
        areaId: null,
        areaName: null,
        railId: null,
        railName: null,
        layerId: null,
        layerHeight: null,
        railScope: null,
        railHeight: null,
        drawType: null,
        longitude: null,
        latitude: null,
        beaconId: null,
        cardId: null,
        personId: null,
        personType: null,
        staffType: null,
        realName: null,
        jobNumber: null,
        administratorName: null,
        administratorPhone: null,
        deptId: null,
        deptName: null,
        postId: null,
        postName: null,
        contractorId: null,
        contractorName: null,
        disposeBy: null,
        disposeTime: null,
        disposeFeedback: null,
        endTime: null,
        alarmLevel: null,
        highestAlarmLevel: null,
        buildingId: null,
        buildingName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        acceptTime: [
          {required: true, message: "信号接收时间不能为空", trigger: "blur"}
        ],
        alarmType: [
          {required: true, message: "报警类型不能为空", trigger: "change"}
        ],
        alarmName: [
          {required: true, message: "报警名称不能为空", trigger: "blur"}
        ],
        alarmDesc: [
          {required: true, message: "报警描述不能为空", trigger: "blur"}
        ],
        alarmStatus: [
          {required: true, message: "报警状态不能为空", trigger: "change"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报警记录列表 */
    getList() {
      this.loading = true
      listLanya_core_alarm({
        pageNum: 1,
        pageSize: 1000
      }).then(response => {
        this.lanya_core_alarmList = response.rows
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
        acceptTime: null,
        alarmType: null,
        alarmTypeName: null,
        alarmName: null,
        alarmDesc: null,
        alarmStatus: null,
        areaId: null,
        areaName: null,
        railId: null,
        railName: null,
        layerId: null,
        layerHeight: null,
        railScope: null,
        railHeight: null,
        drawType: null,
        longitude: null,
        latitude: null,
        beaconId: null,
        cardId: null,
        personId: null,
        personType: null,
        staffType: null,
        realName: null,
        jobNumber: null,
        administratorName: null,
        administratorPhone: null,
        deptId: null,
        deptName: null,
        postId: null,
        postName: null,
        contractorId: null,
        contractorName: null,
        disposeBy: null,
        disposeTime: null,
        disposeFeedback: null,
        createBy: null,
        createTime: null,
        updateTime: null,
        updateBy: null,
        endTime: null,
        alarmLevel: null,
        highestAlarmLevel: null,
        buildingId: null,
        buildingName: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报警记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const alarmId = row.alarmId || this.ids
      getLanya_core_alarm(alarmId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改报警记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.alarmId != null) {
            updateLanya_core_alarm(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_core_alarm(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除报警记录编号为"' + alarmIds + '"的数据项？').then(function () {
        return delLanya_core_alarm(alarmIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_core_alarm/export', {
        ...this.queryParams
      }, `lanya_core_alarm_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
