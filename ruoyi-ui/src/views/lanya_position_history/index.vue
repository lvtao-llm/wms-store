<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="接收时间" prop="acceptTime">
        <el-date-picker clearable
          v-model="queryParams.acceptTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择接收时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="卡号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入卡号"
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
      <el-form-item label="距离" prop="distance">
        <el-input
          v-model="queryParams.distance"
          placeholder="请输入距离"
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
      <el-form-item label="岗位id" prop="postId">
        <el-input
          v-model="queryParams.postId"
          placeholder="请输入岗位id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承包商id" prop="contractorId">
        <el-input
          v-model="queryParams.contractorId"
          placeholder="请输入承包商id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="海拔高度" prop="altitude">
        <el-input
          v-model="queryParams.altitude"
          placeholder="请输入海拔高度，单位米"
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
          v-hasPermi="['system:lanya_position_history:add']"
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
          v-hasPermi="['system:lanya_position_history:edit']"
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
          v-hasPermi="['system:lanya_position_history:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_position_history:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_position_historyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="接收时间" align="center" prop="acceptTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.acceptTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="卡号" align="center" prop="cardId" />
      <el-table-column label="卡类型" align="center" prop="cardType" />
      <el-table-column label="信标编码" align="center" prop="beaconId" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="距离" align="center" prop="distance" />
      <el-table-column label="楼层" align="center" prop="layerId" />
      <el-table-column label="楼高" align="center" prop="layerHeight" />
      <el-table-column label="人员id" align="center" prop="personId" />
      <el-table-column label="人员类型" align="center" prop="personType" />
      <el-table-column label="员工类型" align="center" prop="staffType" />
      <el-table-column label="姓名" align="center" prop="realName" />
      <el-table-column label="部门id" align="center" prop="deptId" />
      <el-table-column label="岗位id" align="center" prop="postId" />
      <el-table-column label="承包商id" align="center" prop="contractorId" />
      <el-table-column label="海拔高度" align="center" prop="altitude" />
      <el-table-column label="高精度定位模式" align="center" prop="rtkPositionMode" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_position_history:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_position_history:remove']"
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

    <!-- 添加或修改历史轨迹对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="接收时间" prop="acceptTime">
          <el-date-picker clearable
            v-model="form.acceptTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择接收时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号" />
        </el-form-item>
        <el-form-item label="信标编码" prop="beaconId">
          <el-input v-model="form.beaconId" placeholder="请输入信标编码" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="距离" prop="distance">
          <el-input v-model="form.distance" placeholder="请输入距离" />
        </el-form-item>
        <el-form-item label="楼层" prop="layerId">
          <el-input v-model="form.layerId" placeholder="请输入楼层" />
        </el-form-item>
        <el-form-item label="楼高" prop="layerHeight">
          <el-input v-model="form.layerHeight" placeholder="请输入楼高" />
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
        <el-form-item label="岗位id" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入岗位id" />
        </el-form-item>
        <el-form-item label="承包商id" prop="contractorId">
          <el-input v-model="form.contractorId" placeholder="请输入承包商id" />
        </el-form-item>
        <el-form-item label="海拔高度，单位米" prop="altitude">
          <el-input v-model="form.altitude" placeholder="请输入海拔高度，单位米" />
        </el-form-item>
        <el-form-item label="高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解" prop="rtkPositionMode">
          <el-input v-model="form.rtkPositionMode" placeholder="请输入高精度定位模式：1：单点 2：浮点 4：固定解 5浮点解" />
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
import { listLanya_position_history, getLanya_position_history, delLanya_position_history, addLanya_position_history, updateLanya_position_history } from "@/api/system/lanya_position_history"

export default {
  name: "Lanya_position_history",
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
      // 历史轨迹表格数据
      lanya_position_historyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        acceptTime: null,
        cardId: null,
        cardType: null,
        beaconId: null,
        longitude: null,
        latitude: null,
        distance: null,
        layerId: null,
        layerHeight: null,
        personId: null,
        personType: null,
        staffType: null,
        realName: null,
        deptId: null,
        postId: null,
        contractorId: null,
        altitude: null,
        rtkPositionMode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        acceptTime: [
          { required: true, message: "接收时间不能为空", trigger: "blur" }
        ],
        cardId: [
          { required: true, message: "卡号不能为空", trigger: "blur" }
        ],
        beaconId: [
          { required: true, message: "信标编码不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询历史轨迹列表 */
    getList() {
      this.loading = true
      listLanya_position_history(this.queryParams).then(response => {
        this.lanya_position_historyList = response.rows
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
        acceptTime: null,
        cardId: null,
        cardType: null,
        beaconId: null,
        longitude: null,
        latitude: null,
        distance: null,
        layerId: null,
        layerHeight: null,
        createTime: null,
        personId: null,
        personType: null,
        staffType: null,
        realName: null,
        deptId: null,
        postId: null,
        contractorId: null,
        altitude: null,
        rtkPositionMode: null
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
      this.title = "添加历史轨迹"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLanya_position_history(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改历史轨迹"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLanya_position_history(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_position_history(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除历史轨迹编号为"' + ids + '"的数据项？').then(function() {
        return delLanya_position_history(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_position_history/export', {
        ...this.queryParams
      }, `lanya_position_history_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
