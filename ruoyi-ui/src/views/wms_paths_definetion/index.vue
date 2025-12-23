<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="168px">
      <el-form-item label="开始时摄像头名称" prop="fromName">
        <el-input
          v-model="queryParams.fromName"
          placeholder="请输入开始时摄像头名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时摄像头IP" prop="fromIp">
        <el-input
          v-model="queryParams.fromIp"
          placeholder="请输入开始时摄像头IP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束时摄像头名称" prop="toName">
        <el-input
          v-model="queryParams.toName"
          placeholder="请输入结束时摄像头名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束时摄像头IP" prop="toIp">
        <el-input
          v-model="queryParams.toIp"
          placeholder="请输入结束时摄像头IP"
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
          v-hasPermi="['system:wms_paths_definetion:add']"
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
          v-hasPermi="['system:wms_paths_definetion:edit']"
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
          v-hasPermi="['system:wms_paths_definetion:remove']"
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
          v-hasPermi="['system:wms_paths_definetion:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_paths_definetionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键" align="center" prop="id"/>
      <el-table-column label="开始时摄像头名称" align="center" prop="fromName"/>
      <el-table-column label="开始时摄像头IP" align="center" prop="fromIp"/>
      <el-table-column label="结束时摄像头名称" align="center" prop="toName"/>
      <el-table-column label="结束时摄像头IP" align="center" prop="toIp"/>
      <el-table-column label="虚拟点" align="center" prop="pathLongitude"/>
      <el-table-column label="虚拟点" align="center" prop="pathLatitude"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_paths_definetion:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_paths_definetion:remove']"
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

    <!-- 添加或修改虚拟路径点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="开始摄像头IP" prop="fromIp">
          <el-input v-model="form.fromIp" placeholder="请输入开始时摄像头IP"/>
        </el-form-item>
        <el-form-item label="结束摄像头IP" prop="toIp">
          <el-input v-model="form.toIp" placeholder="请输入结束时摄像头IP"/>
        </el-form-item>
        <el-form-item label="卡号" prop="pathLongitude">
          <el-input v-model="form.cardId" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="pathLatitude">
          <el-date-picker
            clearable
            v-model="form.beginTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择开始时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="pathLatitude">
          <el-date-picker
            clearable
            v-model="form.finishTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择结束时间"
          >
          </el-date-picker>
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
  listWms_paths_definetion,
  getWms_paths_definetion,
  delWms_paths_definetion,
  addWms_paths_definetion,
  updateWms_paths_definetion
} from "@/api/system/wms_paths_definetion"

export default {
  name: "Wms_paths_definetion",
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
      // 虚拟路径点表格数据
      wms_paths_definetionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fromName: null,
        fromIp: null,
        toName: null,
        toIp: null,
        pathLongitude: null,
        pathLatitude: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询虚拟路径点列表 */
    getList() {
      this.loading = true
      listWms_paths_definetion(this.queryParams).then(response => {
        this.wms_paths_definetionList = response.rows.map(item => {
          return{
            ...item,
            pathLongitude: item.pathLongitude ? item.pathLongitude.split( ",").length : "0",
            pathLatitude: item.pathLatitude ? item.pathLatitude.split( ",").length : "0"
          }
        })
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
        fromName: null,
        fromIp: null,
        toName: null,
        toIp: null,
        cardId: null,
        beginTime: null,
        finishTime: null
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
      this.title = "添加虚拟路径点"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getWms_paths_definetion(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改虚拟路径点"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_paths_definetion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_paths_definetion(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除虚拟路径点编号为"' + ids + '"的数据项？').then(function () {
        return delWms_paths_definetion(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_paths_definetion/export', {
        ...this.queryParams
      }, `wms_paths_definetion_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
