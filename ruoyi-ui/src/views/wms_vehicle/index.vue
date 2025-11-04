<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="vehiclePlateNo">
        <el-input
          v-model="queryParams.vehiclePlateNo"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车型" prop="vehicleType">
        <el-select v-model="queryParams.vehicleType" placeholder="请选择车型" clearable>
          <el-option
            v-for="dict in dict.type.wms_vehicle_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车轴数" prop="vehicleAxleNum">
        <el-input
          v-model="queryParams.vehicleAxleNum"
          placeholder="请输入车轴数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承运商" prop="vehicleCompany">
        <el-select v-model="queryParams.vehicleCompany" placeholder="请选择承运商" clearable>
          <el-option
            v-for="dict in dict.type.wms_carrier"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="驾驶员姓名" prop="vehicleDriverName">
        <el-select v-model="queryParams.vehicleDriverName" placeholder="请选择驾驶员姓名" clearable>
          <el-option
            v-for="dict in dict.type.wms_external_driver"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="驾驶员手机" prop="vehicleDriverPhone">
        <el-input
          v-model="queryParams.vehicleDriverPhone"
          placeholder="请输入驾驶员手机"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="核载(kg)" prop="maxWeight">
        <el-input
          v-model="queryParams.maxWeight"
          placeholder="请输入核载(kg)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可进入区域：json数组" prop="authArea">
        <el-select v-model="queryParams.authArea" placeholder="请选择可进入区域：json数组" clearable>
          <el-option
            v-for="dict in dict.type.wms_area_name"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_status"
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
          v-hasPermi="['system:wms_vehicle:add']"
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
          v-hasPermi="['system:wms_vehicle:edit']"
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
          v-hasPermi="['system:wms_vehicle:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_vehicle:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_vehicleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车辆ID" align="center" prop="vehicleId" />
      <el-table-column label="车牌号" align="center" prop="vehiclePlateNo" />
      <el-table-column label="车型" align="center" prop="vehicleType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_vehicle_type" :value="scope.row.vehicleType"/>
        </template>
      </el-table-column>
      <el-table-column label="车轴数" align="center" prop="vehicleAxleNum" />
      <el-table-column label="承运商" align="center" prop="vehicleCompany">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_carrier" :value="scope.row.vehicleCompany"/>
        </template>
      </el-table-column>
      <el-table-column label="驾驶员姓名" align="center" prop="vehicleDriverName">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_external_driver" :value="scope.row.vehicleDriverName"/>
        </template>
      </el-table-column>
      <el-table-column label="驾驶员手机" align="center" prop="vehicleDriverPhone" />
      <el-table-column label="核载(kg)" align="center" prop="maxWeight" />
      <el-table-column label="可进入区域：json数组" align="center" prop="authArea">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_area_name" :value="scope.row.authArea"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_vehicle:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_vehicle:remove']"
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

    <!-- 添加或修改车辆档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="vehiclePlateNo">
          <el-input v-model="form.vehiclePlateNo" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车型" prop="vehicleType">
          <el-select v-model="form.vehicleType" placeholder="请选择车型">
            <el-option
              v-for="dict in dict.type.wms_vehicle_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车轴数" prop="vehicleAxleNum">
          <el-input v-model="form.vehicleAxleNum" placeholder="请输入车轴数" />
        </el-form-item>
        <el-form-item label="承运商" prop="vehicleCompany">
          <el-select v-model="form.vehicleCompany" placeholder="请选择承运商">
            <el-option
              v-for="dict in dict.type.wms_carrier"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="驾驶员姓名" prop="vehicleDriverName">
          <el-select v-model="form.vehicleDriverName" placeholder="请选择驾驶员姓名">
            <el-option
              v-for="dict in dict.type.wms_external_driver"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="驾驶员手机" prop="vehicleDriverPhone">
          <el-input v-model="form.vehicleDriverPhone" placeholder="请输入驾驶员手机" />
        </el-form-item>
        <el-form-item label="核载(kg)" prop="maxWeight">
          <el-input v-model="form.maxWeight" placeholder="请输入核载(kg)" />
        </el-form-item>
        <el-form-item label="可进入区域：json数组" prop="authArea">
          <el-select v-model="form.authArea" placeholder="请选择可进入区域：json数组">
            <el-option
              v-for="dict in dict.type.wms_area_name"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.sys_common_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否删除" prop="delFlag">
          <el-select v-model="form.delFlag" placeholder="请选择是否删除">
            <el-option
              v-for="dict in dict.type.wms_deleted"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
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
import { listWms_vehicle, getWms_vehicle, delWms_vehicle, addWms_vehicle, updateWms_vehicle } from "@/api/system/wms_vehicle"

export default {
  name: "Wms_vehicle",
  dicts: ['wms_area_name', 'wms_external_driver', 'sys_common_status', 'wms_carrier', 'wms_vehicle_type', 'wms_deleted'],
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
      // 车辆档案表格数据
      wms_vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehiclePlateNo: null,
        vehicleType: null,
        vehicleAxleNum: null,
        vehicleCompany: null,
        vehicleDriverName: null,
        vehicleDriverPhone: null,
        maxWeight: null,
        authArea: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehiclePlateNo: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询车辆档案列表 */
    getList() {
      this.loading = true
      listWms_vehicle(this.queryParams).then(response => {
        this.wms_vehicleList = response.rows
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
        vehicleId: null,
        vehiclePlateNo: null,
        vehicleType: null,
        vehicleAxleNum: null,
        vehicleCompany: null,
        vehicleDriverName: null,
        vehicleDriverPhone: null,
        maxWeight: null,
        authArea: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
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
      this.ids = selection.map(item => item.vehicleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加车辆档案"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const vehicleId = row.vehicleId || this.ids
      getWms_vehicle(vehicleId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改车辆档案"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.vehicleId != null) {
            updateWms_vehicle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_vehicle(this.form).then(response => {
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
      const vehicleIds = row.vehicleId || this.ids
      this.$modal.confirm('是否确认删除车辆档案编号为"' + vehicleIds + '"的数据项？').then(function() {
        return delWms_vehicle(vehicleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_vehicle/export', {
        ...this.queryParams
      }, `wms_vehicle_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
