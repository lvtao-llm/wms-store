<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="vehiclePlateNo">
        <el-select v-model="queryParams.vehiclePlateNo" placeholder="请选择车牌号" clearable>
          <el-option
            v-for="dict in dict.type.wms_external_vehicle"
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
          v-hasPermi="['system:wms_vehicle_blacklist:add']"
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
          v-hasPermi="['system:wms_vehicle_blacklist:edit']"
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
          v-hasPermi="['system:wms_vehicle_blacklist:remove']"
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
          v-hasPermi="['system:wms_vehicle_blacklist:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_vehicle_blacklistList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="车牌号" align="center" prop="vehiclePlateNo">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_external_vehicle" :value="scope.row.vehiclePlateNo"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_vehicle_blacklist:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_vehicle_blacklist:remove']"
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

    <!-- 添加或修改车辆黑名单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="vehiclePlateNo">
          <el-autocomplete
            v-model="form.vehiclePlateNo"
            :fetch-suggestions="queryVehicleSuggestions"
            placeholder="请输入车辆名称"
            :trigger-on-focus="false"
            @select="handleVehicleSelect"
            clearable
          />
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
  listWms_vehicle_blacklist,
  getWms_vehicle_blacklist,
  delWms_vehicle_blacklist,
  addWms_vehicle_blacklist,
  updateWms_vehicle_blacklist
} from "@/api/system/wms_vehicle_blacklist"
import {
  listWms_vehicle,
  getWms_vehicle,
  delWms_vehicle,
  addWms_vehicle,
  updateWms_vehicle
} from "@/api/system/wms_vehicle"

export default {
  name: "Wms_vehicle_blacklist",
  dicts: ['wms_external_vehicle'],
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
      // 车辆黑名单表格数据
      wms_vehicle_blacklistList: [],
      vehicles: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehiclePlateNo: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehiclePlateNo: [
          {required: true, message: "车牌号不能为空", trigger: "change"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询车辆黑名单列表 */
    getList() {
      this.loading = true
      listWms_vehicle(this.queryParams).then(response => {
        this.vehicles = response.rows;
        listWms_vehicle_blacklist(this.queryParams).then(response => {
          this.wms_vehicle_blacklistList = response.rows
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
        id: null,
        vehiclePlateNo: null,
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
      this.title = "添加车辆黑名单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWms_vehicle_blacklist(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改车辆黑名单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_vehicle_blacklist(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_vehicle_blacklist(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除车辆黑名单编号为"' + ids + '"的数据项？').then(function () {
        return delWms_vehicle_blacklist(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_vehicle_blacklist/export', {
        ...this.queryParams
      }, `wms_vehicle_blacklist_${new Date().getTime()}.xlsx`)
    },
    queryVehicleSuggestions(queryString, cb) {
      // 这里需要调用获取车辆列表的API
      // 示例数据，需要替换为实际API调用
      const vehicles = this.vehicles;
      const results = queryString ? vehicles.filter(vehicle =>
        vehicle.vehiclePlateNo.toLowerCase().includes(queryString.toLowerCase())
      ) : vehicles;

      // 调用回调函数返回建议列表
      cb(results.map(item => ({
        value: item.vehiclePlateNo,
        id: item.vehiclePlateNo
      })));
    },
    // 选择车辆后的处理
    handleVehicleSelect(item) {
      // 将选中车辆的ID保存到表单中
      this.form.vehicleId = item.value;
    }
  }
}
</script>
