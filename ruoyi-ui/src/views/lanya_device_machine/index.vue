<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备sn编码" prop="deviceSn">
        <el-input
          v-model="queryParams.deviceSn"
          placeholder="请输入设备sn编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备版本号" prop="deviceVersion">
        <el-input
          v-model="queryParams.deviceVersion"
          placeholder="请输入设备版本号"
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
      <el-form-item label="发卡机按钮租借(默认0否；1是)" prop="deviceBtnRent">
        <el-input
          v-model="queryParams.deviceBtnRent"
          placeholder="请输入发卡机按钮租借(默认0否；1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发卡机心跳时间" prop="deviceHeartTime">
        <el-date-picker clearable
          v-model="queryParams.deviceHeartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择发卡机心跳时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="设备配置" prop="deviceConfig">
        <el-input
          v-model="queryParams.deviceConfig"
          placeholder="请输入设备配置"
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
      <el-form-item label="楼层id" prop="layerId">
        <el-input
          v-model="queryParams.layerId"
          placeholder="请输入楼层id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼层高度" prop="layerHeight">
        <el-input
          v-model="queryParams.layerHeight"
          placeholder="请输入楼层高度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机用户名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入人脸机用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入人脸机密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机访问地址" prop="faceUrl">
        <el-input
          v-model="queryParams.faceUrl"
          placeholder="请输入人脸机访问地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸相机标识" prop="faceFlag">
        <el-input
          v-model="queryParams.faceFlag"
          placeholder="请输入人脸相机标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机mac地址" prop="faceMacAddr">
        <el-input
          v-model="queryParams.faceMacAddr"
          placeholder="请输入人脸机mac地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机SN码" prop="faceSn">
        <el-input
          v-model="queryParams.faceSn"
          placeholder="请输入人脸机SN码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人脸机心跳时间" prop="faceHeartTime">
        <el-date-picker clearable
          v-model="queryParams.faceHeartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择人脸机心跳时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="发卡机：是否启用 Y：启用 N：禁用" prop="deviceEnable">
        <el-input
          v-model="queryParams.deviceEnable"
          placeholder="请输入发卡机：是否启用 Y：启用 N：禁用"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位卡型号" prop="cardModel">
        <el-input
          v-model="queryParams.cardModel"
          placeholder="请输入定位卡型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划投放卡量" prop="plannedNumberCards">
        <el-input
          v-model="queryParams.plannedNumberCards"
          placeholder="请输入计划投放卡量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下发人脸(Y:是;N:否)" prop="pushFace">
        <el-input
          v-model="queryParams.pushFace"
          placeholder="请输入下发人脸(Y:是;N:否)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="白名单数量" prop="whiteListCount">
        <el-input
          v-model="queryParams.whiteListCount"
          placeholder="请输入白名单数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视图卡槽顺序" prop="viewSort">
        <el-input
          v-model="queryParams.viewSort"
          placeholder="请输入视图卡槽顺序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视图几排" prop="viewRows">
        <el-input
          v-model="queryParams.viewRows"
          placeholder="请输入视图几排"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视图几列" prop="viewColumns">
        <el-input
          v-model="queryParams.viewColumns"
          placeholder="请输入视图几列"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="视图数据" prop="viewData">
        <el-input
          v-model="queryParams.viewData"
          placeholder="请输入视图数据"
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
          v-hasPermi="['system:lanya_device_machine:add']"
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
          v-hasPermi="['system:lanya_device_machine:edit']"
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
          v-hasPermi="['system:lanya_device_machine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_device_machine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_device_machineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="发卡id" align="center" prop="cardSenderId" />
      <el-table-column label="设备sn编码" align="center" prop="deviceSn" />
      <el-table-column label="设备版本号" align="center" prop="deviceVersion" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="发卡机按钮租借(默认0否；1是)" align="center" prop="deviceBtnRent" />
      <el-table-column label="发卡机心跳时间" align="center" prop="deviceHeartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deviceHeartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备配置" align="center" prop="deviceConfig" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="楼层id" align="center" prop="layerId" />
      <el-table-column label="楼层高度" align="center" prop="layerHeight" />
      <el-table-column label="人脸机用户名" align="center" prop="userName" />
      <el-table-column label="人脸机密码" align="center" prop="password" />
      <el-table-column label="人脸机访问地址" align="center" prop="faceUrl" />
      <el-table-column label="人脸相机标识" align="center" prop="faceFlag" />
      <el-table-column label="人脸机mac地址" align="center" prop="faceMacAddr" />
      <el-table-column label="人脸机SN码" align="center" prop="faceSn" />
      <el-table-column label="人脸机心跳时间" align="center" prop="faceHeartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.faceHeartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="发卡机：是否启用 Y：启用 N：禁用" align="center" prop="deviceEnable" />
      <el-table-column label="定位卡型号" align="center" prop="cardModel" />
      <el-table-column label="计划投放卡量" align="center" prop="plannedNumberCards" />
      <el-table-column label="下发人脸(Y:是;N:否)" align="center" prop="pushFace" />
      <el-table-column label="白名单数量" align="center" prop="whiteListCount" />
      <el-table-column label="视图卡槽顺序" align="center" prop="viewSort" />
      <el-table-column label="视图几排" align="center" prop="viewRows" />
      <el-table-column label="视图几列" align="center" prop="viewColumns" />
      <el-table-column label="视图数据" align="center" prop="viewData" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_device_machine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_device_machine:remove']"
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

    <!-- 添加或修改发卡机对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备sn编码" prop="deviceSn">
          <el-input v-model="form.deviceSn" placeholder="请输入设备sn编码" />
        </el-form-item>
        <el-form-item label="设备版本号" prop="deviceVersion">
          <el-input v-model="form.deviceVersion" placeholder="请输入设备版本号" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="发卡机按钮租借(默认0否；1是)" prop="deviceBtnRent">
          <el-input v-model="form.deviceBtnRent" placeholder="请输入发卡机按钮租借(默认0否；1是)" />
        </el-form-item>
        <el-form-item label="发卡机心跳时间" prop="deviceHeartTime">
          <el-date-picker clearable
            v-model="form.deviceHeartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发卡机心跳时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备配置" prop="deviceConfig">
          <el-input v-model="form.deviceConfig" placeholder="请输入设备配置" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="楼层id" prop="layerId">
          <el-input v-model="form.layerId" placeholder="请输入楼层id" />
        </el-form-item>
        <el-form-item label="楼层高度" prop="layerHeight">
          <el-input v-model="form.layerHeight" placeholder="请输入楼层高度" />
        </el-form-item>
        <el-form-item label="人脸机用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入人脸机用户名" />
        </el-form-item>
        <el-form-item label="人脸机密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入人脸机密码" />
        </el-form-item>
        <el-form-item label="人脸机访问地址" prop="faceUrl">
          <el-input v-model="form.faceUrl" placeholder="请输入人脸机访问地址" />
        </el-form-item>
        <el-form-item label="人脸相机标识" prop="faceFlag">
          <el-input v-model="form.faceFlag" placeholder="请输入人脸相机标识" />
        </el-form-item>
        <el-form-item label="人脸机mac地址" prop="faceMacAddr">
          <el-input v-model="form.faceMacAddr" placeholder="请输入人脸机mac地址" />
        </el-form-item>
        <el-form-item label="人脸机SN码" prop="faceSn">
          <el-input v-model="form.faceSn" placeholder="请输入人脸机SN码" />
        </el-form-item>
        <el-form-item label="人脸机心跳时间" prop="faceHeartTime">
          <el-date-picker clearable
            v-model="form.faceHeartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择人脸机心跳时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="发卡机：是否启用 Y：启用 N：禁用" prop="deviceEnable">
          <el-input v-model="form.deviceEnable" placeholder="请输入发卡机：是否启用 Y：启用 N：禁用" />
        </el-form-item>
        <el-form-item label="定位卡型号" prop="cardModel">
          <el-input v-model="form.cardModel" placeholder="请输入定位卡型号" />
        </el-form-item>
        <el-form-item label="计划投放卡量" prop="plannedNumberCards">
          <el-input v-model="form.plannedNumberCards" placeholder="请输入计划投放卡量" />
        </el-form-item>
        <el-form-item label="下发人脸(Y:是;N:否)" prop="pushFace">
          <el-input v-model="form.pushFace" placeholder="请输入下发人脸(Y:是;N:否)" />
        </el-form-item>
        <el-form-item label="白名单数量" prop="whiteListCount">
          <el-input v-model="form.whiteListCount" placeholder="请输入白名单数量" />
        </el-form-item>
        <el-form-item label="视图卡槽顺序" prop="viewSort">
          <el-input v-model="form.viewSort" placeholder="请输入视图卡槽顺序" />
        </el-form-item>
        <el-form-item label="视图几排" prop="viewRows">
          <el-input v-model="form.viewRows" placeholder="请输入视图几排" />
        </el-form-item>
        <el-form-item label="视图几列" prop="viewColumns">
          <el-input v-model="form.viewColumns" placeholder="请输入视图几列" />
        </el-form-item>
        <el-form-item label="视图数据" prop="viewData">
          <el-input v-model="form.viewData" placeholder="请输入视图数据" />
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
import { listLanya_device_machine, getLanya_device_machine, delLanya_device_machine, addLanya_device_machine, updateLanya_device_machine } from "@/api/system/lanya_device_machine"

export default {
  name: "Lanya_device_machine",
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
      // 发卡机表格数据
      lanya_device_machineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceSn: null,
        deviceVersion: null,
        deviceName: null,
        deviceBtnRent: null,
        deviceHeartTime: null,
        deviceConfig: null,
        longitude: null,
        latitude: null,
        layerId: null,
        layerHeight: null,
        userName: null,
        password: null,
        faceUrl: null,
        faceFlag: null,
        faceMacAddr: null,
        faceSn: null,
        faceHeartTime: null,
        deviceEnable: null,
        cardModel: null,
        plannedNumberCards: null,
        pushFace: null,
        whiteListCount: null,
        viewSort: null,
        viewRows: null,
        viewColumns: null,
        viewData: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceSn: [
          { required: true, message: "设备sn编码不能为空", trigger: "blur" }
        ],
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        faceFlag: [
          { required: true, message: "人脸相机标识不能为空", trigger: "blur" }
        ],
        deviceEnable: [
          { required: true, message: "发卡机：是否启用 Y：启用 N：禁用不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询发卡机列表 */
    getList() {
      this.loading = true
      listLanya_device_machine(this.queryParams).then(response => {
        this.lanya_device_machineList = response.rows
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
        cardSenderId: null,
        deviceSn: null,
        deviceVersion: null,
        deviceName: null,
        deviceBtnRent: null,
        deviceHeartTime: null,
        deviceConfig: null,
        longitude: null,
        latitude: null,
        layerId: null,
        layerHeight: null,
        userName: null,
        password: null,
        faceUrl: null,
        faceFlag: null,
        faceMacAddr: null,
        faceSn: null,
        faceHeartTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null,
        deviceEnable: null,
        cardModel: null,
        plannedNumberCards: null,
        pushFace: null,
        whiteListCount: null,
        viewSort: null,
        viewRows: null,
        viewColumns: null,
        viewData: null
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
      this.ids = selection.map(item => item.cardSenderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加发卡机"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const cardSenderId = row.cardSenderId || this.ids
      getLanya_device_machine(cardSenderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改发卡机"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cardSenderId != null) {
            updateLanya_device_machine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_device_machine(this.form).then(response => {
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
      const cardSenderIds = row.cardSenderId || this.ids
      this.$modal.confirm('是否确认删除发卡机编号为"' + cardSenderIds + '"的数据项？').then(function() {
        return delLanya_device_machine(cardSenderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_device_machine/export', {
        ...this.queryParams
      }, `lanya_device_machine_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
