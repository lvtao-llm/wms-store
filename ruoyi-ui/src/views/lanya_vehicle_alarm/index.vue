<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="报警时间" prop="alarmTime">
        <el-date-picker clearable
          v-model="queryParams.alarmTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择报警时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="车辆id" prop="vehicleId">
        <el-input
          v-model="queryParams.vehicleId"
          placeholder="请输入车辆id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车辆类别" prop="vehicleCategory">
        <el-input
          v-model="queryParams.vehicleCategory"
          placeholder="请输入车辆类别(见字典)"
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
      <el-form-item label="车辆名称" prop="vehicleName">
        <el-input
          v-model="queryParams.vehicleName"
          placeholder="请输入车辆名称"
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
      <el-form-item label="司机名称" prop="driverName">
        <el-input
          v-model="queryParams.driverName"
          placeholder="请输入司机名称"
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
      <el-form-item label="单位名称" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入单位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报警类型名称" prop="alarmTypeName">
        <el-input
          v-model="queryParams.alarmTypeName"
          placeholder="请输入报警类型名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报警名称" prop="alarmName">
        <el-input
          v-model="queryParams.alarmName"
          placeholder="请输入报警名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报警设置id" prop="settingId">
        <el-input
          v-model="queryParams.settingId"
          placeholder="请输入报警设置id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报警设置名称" prop="settingName">
        <el-input
          v-model="queryParams.settingName"
          placeholder="请输入报警设置名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="围栏id" prop="railId">
        <el-input
          v-model="queryParams.railId"
          placeholder="请输入围栏id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="围栏名称" prop="railName">
        <el-input
          v-model="queryParams.railName"
          placeholder="请输入围栏名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="围栏高度" prop="railHeight">
        <el-input
          v-model="queryParams.railHeight"
          placeholder="请输入围栏高度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼层" prop="layerId">
        <el-input
          v-model="queryParams.layerId"
          placeholder="请输入楼层"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼高" prop="layerHeight">
        <el-input
          v-model="queryParams.layerHeight"
          placeholder="请输入楼高"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经度" prop="longitude">
        <el-input
          v-model="queryParams.longitude"
          placeholder="请输入经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input
          v-model="queryParams.latitude"
          placeholder="请输入纬度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="速度" prop="speed">
        <el-input
          v-model="queryParams.speed"
          placeholder="请输入速度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方向" prop="direction">
        <el-input
          v-model="queryParams.direction"
          placeholder="请输入方向"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电量" prop="electricity">
        <el-input
          v-model="queryParams.electricity"
          placeholder="请输入电量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="信标编码" prop="beaconId">
        <el-input
          v-model="queryParams.beaconId"
          placeholder="请输入信标编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="信号接收时间" prop="acceptTime">
        <el-date-picker clearable
          v-model="queryParams.acceptTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择信号接收时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="处理人" prop="disposeBy">
        <el-input
          v-model="queryParams.disposeBy"
          placeholder="请输入处理人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理时间" prop="disposeTime">
        <el-date-picker clearable
          v-model="queryParams.disposeTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="处理反馈" prop="disposeFeedback">
        <el-input
          v-model="queryParams.disposeFeedback"
          placeholder="请输入处理反馈"
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
          v-hasPermi="['system:lanya_vehicle_alarm:add']"
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
          v-hasPermi="['system:lanya_vehicle_alarm:edit']"
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
          v-hasPermi="['system:lanya_vehicle_alarm:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_vehicle_alarm:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_vehicle_alarmList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车辆id" align="center" prop="vehicleId" />
      <el-table-column label="车辆类别" align="center" prop="vehicleCategory" />
      <el-table-column label="车载卡号" align="center" prop="cardId" />
      <el-table-column label="车辆名称" align="center" prop="vehicleName" />
      <el-table-column label="车牌号" align="center" prop="licensePlateNumber" />
      <el-table-column label="司机名称" align="center" prop="driverName" />
      <el-table-column label="司机手机号" align="center" prop="driverTel" width="100"/>
      <el-table-column label="单位名称" align="center" prop="companyName" />
      <el-table-column label="报警类型" align="center" prop="alarmType" />
      <el-table-column label="报警类型名称" align="center" prop="alarmTypeName" width="100"/>
      <el-table-column label="报警名称" align="center" prop="alarmName" />
      <el-table-column label="报警描述" align="center" prop="alarmDesc" />
      <el-table-column label="报警状态" align="center" prop="alarmStatus" />
      <el-table-column label="报警设置名称" align="center" prop="settingName" width="100"/>
      <el-table-column label="围栏id" align="center" prop="railId" />
      <el-table-column label="围栏名称" align="center" prop="railName" />
      <el-table-column label="绘制类型" align="center" prop="drawType" />
      <el-table-column label="围栏坐标" align="center" prop="railScope" />
      <el-table-column label="围栏高度" align="center" prop="railHeight" />
      <el-table-column label="楼层" align="center" prop="layerId" />
      <el-table-column label="楼高" align="center" prop="layerHeight" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="速度" align="center" prop="speed" />
      <el-table-column label="方向" align="center" prop="direction" />
      <el-table-column label="电量" align="center" prop="electricity" />
      <el-table-column label="信标编码" align="center" prop="beaconId" />
      <el-table-column label="信号接收时间" align="center" prop="acceptTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.acceptTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理人" align="center" prop="disposeBy" />
      <el-table-column label="处理时间" align="center" prop="disposeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.disposeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理反馈" align="center" prop="disposeFeedback" />
      <el-table-column label="comment" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_vehicle_alarm:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_vehicle_alarm:remove']"
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

    <!-- 添加或修改车辆报警记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="报警时间" prop="alarmTime">
          <el-date-picker clearable
            v-model="form.alarmTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择报警时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车辆id" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入车辆id" />
        </el-form-item>
        <el-form-item label="车辆类别(见字典)" prop="vehicleCategory">
          <el-input v-model="form.vehicleCategory" placeholder="请输入车辆类别(见字典)" />
        </el-form-item>
        <el-form-item label="车载卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入车载卡号" />
        </el-form-item>
        <el-form-item label="车辆名称" prop="vehicleName">
          <el-input v-model="form.vehicleName" placeholder="请输入车辆名称" />
        </el-form-item>
        <el-form-item label="车牌号" prop="licensePlateNumber">
          <el-input v-model="form.licensePlateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="司机名称" prop="driverName">
          <el-input v-model="form.driverName" placeholder="请输入司机名称" />
        </el-form-item>
        <el-form-item label="司机手机号" prop="driverTel">
          <el-input v-model="form.driverTel" placeholder="请输入司机手机号" />
        </el-form-item>
        <el-form-item label="单位名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="报警类型名称" prop="alarmTypeName">
          <el-input v-model="form.alarmTypeName" placeholder="请输入报警类型名称" />
        </el-form-item>
        <el-form-item label="报警名称" prop="alarmName">
          <el-input v-model="form.alarmName" placeholder="请输入报警名称" />
        </el-form-item>
        <el-form-item label="报警描述" prop="alarmDesc">
          <el-input v-model="form.alarmDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="报警设置id" prop="settingId">
          <el-input v-model="form.settingId" placeholder="请输入报警设置id" />
        </el-form-item>
        <el-form-item label="报警设置名称" prop="settingName">
          <el-input v-model="form.settingName" placeholder="请输入报警设置名称" />
        </el-form-item>
        <el-form-item label="围栏id" prop="railId">
          <el-input v-model="form.railId" placeholder="请输入围栏id" />
        </el-form-item>
        <el-form-item label="围栏名称" prop="railName">
          <el-input v-model="form.railName" placeholder="请输入围栏名称" />
        </el-form-item>
        <el-form-item label="围栏坐标" prop="railScope">
          <el-input v-model="form.railScope" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="围栏高度" prop="railHeight">
          <el-input v-model="form.railHeight" placeholder="请输入围栏高度" />
        </el-form-item>
        <el-form-item label="楼层" prop="layerId">
          <el-input v-model="form.layerId" placeholder="请输入楼层" />
        </el-form-item>
        <el-form-item label="楼高" prop="layerHeight">
          <el-input v-model="form.layerHeight" placeholder="请输入楼高" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="速度" prop="speed">
          <el-input v-model="form.speed" placeholder="请输入速度" />
        </el-form-item>
        <el-form-item label="方向" prop="direction">
          <el-input v-model="form.direction" placeholder="请输入方向" />
        </el-form-item>
        <el-form-item label="电量" prop="electricity">
          <el-input v-model="form.electricity" placeholder="请输入电量" />
        </el-form-item>
        <el-form-item label="信标编码" prop="beaconId">
          <el-input v-model="form.beaconId" placeholder="请输入信标编码" />
        </el-form-item>
        <el-form-item label="信号接收时间" prop="acceptTime">
          <el-date-picker clearable
            v-model="form.acceptTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择信号接收时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="处理人" prop="disposeBy">
          <el-input v-model="form.disposeBy" placeholder="请输入处理人" />
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
          <el-input v-model="form.disposeFeedback" placeholder="请输入处理反馈" />
        </el-form-item>
        <el-form-item label="${comment}" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入${comment}" />
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
import { listLanya_vehicle_alarm, getLanya_vehicle_alarm, delLanya_vehicle_alarm, addLanya_vehicle_alarm, updateLanya_vehicle_alarm } from "@/api/system/lanya_vehicle_alarm"

export default {
  name: "Lanya_vehicle_alarm",
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
      // 车辆报警记录表格数据
      lanya_vehicle_alarmList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmTime: null,
        vehicleId: null,
        vehicleCategory: null,
        cardId: null,
        vehicleName: null,
        licensePlateNumber: null,
        driverName: null,
        driverTel: null,
        companyName: null,
        alarmType: null,
        alarmTypeName: null,
        alarmName: null,
        alarmDesc: null,
        alarmStatus: null,
        settingId: null,
        settingName: null,
        railId: null,
        railName: null,
        drawType: null,
        railScope: null,
        railHeight: null,
        layerId: null,
        layerHeight: null,
        longitude: null,
        latitude: null,
        speed: null,
        direction: null,
        electricity: null,
        beaconId: null,
        acceptTime: null,
        disposeBy: null,
        disposeTime: null,
        disposeFeedback: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        alarmTime: [
          { required: true, message: "报警时间不能为空", trigger: "blur" }
        ],
        alarmType: [
          { required: true, message: "报警类型不能为空", trigger: "change" }
        ],
        alarmStatus: [
          { required: true, message: "报警状态不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询车辆报警记录列表 */
    getList() {
      this.loading = true
      listLanya_vehicle_alarm(this.queryParams).then(response => {
        this.lanya_vehicle_alarmList = response.rows
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
        alarmTime: null,
        vehicleId: null,
        vehicleCategory: null,
        cardId: null,
        vehicleName: null,
        licensePlateNumber: null,
        driverName: null,
        driverTel: null,
        companyName: null,
        alarmType: null,
        alarmTypeName: null,
        alarmName: null,
        alarmDesc: null,
        alarmStatus: null,
        settingId: null,
        settingName: null,
        railId: null,
        railName: null,
        drawType: null,
        railScope: null,
        railHeight: null,
        layerId: null,
        layerHeight: null,
        longitude: null,
        latitude: null,
        speed: null,
        direction: null,
        electricity: null,
        beaconId: null,
        acceptTime: null,
        disposeBy: null,
        disposeTime: null,
        disposeFeedback: null,
        createBy: null,
        createTime: null,
        updateTime: null,
        updateBy: null,
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
      this.title = "添加车辆报警记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLanya_vehicle_alarm(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改车辆报警记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLanya_vehicle_alarm(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_vehicle_alarm(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除车辆报警记录编号为"' + ids + '"的数据项？').then(function() {
        return delLanya_vehicle_alarm(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_vehicle_alarm/export', {
        ...this.queryParams
      }, `lanya_vehicle_alarm_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
