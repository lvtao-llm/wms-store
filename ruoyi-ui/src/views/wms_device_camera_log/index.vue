<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="点位名称" prop="dwmc">
        <el-input
          v-model="queryParams.dwmc"
          placeholder="请输入点位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间" prop="sj">
        <el-date-picker clearable
          v-model="queryParams.sj"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="是否有车" prop="yc">-->
<!--        <el-input-->
<!--          v-model="queryParams.yc"-->
<!--          placeholder="请输入是否有车"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="是否有人" prop="yr">-->
<!--        <el-input-->
<!--          v-model="queryParams.yr"-->
<!--          placeholder="请输入是否有人"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="车牌号" prop="cph">
        <el-input
          v-model="queryParams.cph"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="车轴" prop="cz">-->
<!--        <el-input-->
<!--          v-model="queryParams.cz"-->
<!--          placeholder="请输入车轴"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="车型" prop="cx">-->
<!--        <el-input-->
<!--          v-model="queryParams.cx"-->
<!--          placeholder="请输入车型"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="颜色" prop="ys">-->
<!--        <el-input-->
<!--          v-model="queryParams.ys"-->
<!--          placeholder="请输入颜色"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="是否穿戴工服" prop="sfcdgf">-->
<!--        <el-input-->
<!--          v-model="queryParams.sfcdgf"-->
<!--          placeholder="请输入是否穿戴工服"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
          v-hasPermi="['system:wms_device_camera_log:add']"
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
          v-hasPermi="['system:wms_device_camera_log:edit']"
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
          v-hasPermi="['system:wms_device_camera_log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_device_camera_log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_device_camera_logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="类别" align="center" prop="type" />
      <el-table-column label="点位名称" align="center" prop="dwmc" />
      <el-table-column label="时间" align="center" prop="sj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sj, '{y}-{m}-{d} {hh}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否有车" align="center" prop="yc" />-->
<!--      <el-table-column label="是否有人" align="center" prop="yr" />-->
      <el-table-column label="车牌号" align="center" prop="cph" />
<!--      <el-table-column label="车轴" align="center" prop="cz" />-->
<!--      <el-table-column label="车型" align="center" prop="cx" />-->
<!--      <el-table-column label="颜色" align="center" prop="ys" />-->
<!--      <el-table-column label="是否穿戴工服" align="center" prop="sfcdgf" />-->
      <el-table-column label="图片" align="center" prop="base64">
        <template slot-scope="scope">
            <image-preview
              :src="scope.row.base64"
              :width="50"
              :height="50"
              style="margin-right: 5px; margin-bottom: 5px"
            />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_device_camera_log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_device_camera_log:remove']"
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

    <!-- 添加或修改摄像头识别日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="点位名称" prop="dwmc">
          <el-input v-model="form.dwmc" placeholder="请输入点位名称" />
        </el-form-item>
        <el-form-item label="时间" prop="sj">
          <el-date-picker clearable
            v-model="form.sj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否有车" prop="yc">
          <el-input v-model="form.yc" placeholder="请输入是否有车" />
        </el-form-item>
        <el-form-item label="是否有人" prop="yr">
          <el-input v-model="form.yr" placeholder="请输入是否有人" />
        </el-form-item>
        <el-form-item label="车牌号" prop="cph">
          <el-input v-model="form.cph" placeholder="请输入车牌号" />
        </el-form-item>
<!--        <el-form-item label="车轴" prop="cz">-->
<!--          <el-input v-model="form.cz" placeholder="请输入车轴" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="车型" prop="cx">-->
<!--          <el-input v-model="form.cx" placeholder="请输入车型" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="颜色" prop="ys">-->
<!--          <el-input v-model="form.ys" placeholder="请输入颜色" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="是否穿戴工服" prop="sfcdgf">-->
<!--          <el-input v-model="form.sfcdgf" placeholder="请输入是否穿戴工服" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="颜色" prop="base64">-->
<!--          <el-input v-model="form.base64" type="textarea" placeholder="请输入内容" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWms_device_camera_log, getWms_device_camera_log, delWms_device_camera_log, addWms_device_camera_log, updateWms_device_camera_log } from "@/api/system/wms_device_camera_log"

export default {
  name: "Wms_device_camera_log",
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
      // 摄像头识别日志表格数据
      wms_device_camera_logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        dwmc: null,
        sj: null,
        yc: null,
        yr: null,
        cph: null,
        cz: null,
        cx: null,
        ys: null,
        sfcdgf: null,
        base64: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dwmc: [
          { required: true, message: "点位名称不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询摄像头识别日志列表 */
    getList() {
      this.loading = true
      listWms_device_camera_log(this.queryParams).then(response => {
        this.wms_device_camera_logList = response.rows
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
        type: null,
        dwmc: null,
        sj: null,
        yc: null,
        yr: null,
        cph: null,
        cz: null,
        cx: null,
        ys: null,
        sfcdgf: null,
        base64: null
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
      this.title = "添加摄像头识别日志"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWms_device_camera_log(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改摄像头识别日志"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_device_camera_log(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_device_camera_log(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除摄像头识别日志编号为"' + ids + '"的数据项？').then(function() {
        return delWms_device_camera_log(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_device_camera_log/export', {
        ...this.queryParams
      }, `wms_device_camera_log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
