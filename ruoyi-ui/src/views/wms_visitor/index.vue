<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="身份证号" prop="visitorIdentityNo">
        <el-input
          v-model="queryParams.visitorIdentityNo"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="visitorName">
        <el-input
          v-model="queryParams.visitorName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="visitorPhone">
        <el-input
          v-model="queryParams.visitorPhone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来访事由" prop="visitorPurpose">
        <el-input
          v-model="queryParams.visitorPurpose"
          placeholder="请输入来访事由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预计停留时间" prop="estimatedTime">
        <el-input
          v-model="queryParams.estimatedTime"
          placeholder="请输入预计停留时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="被访人" prop="visitee">
        <el-input
          v-model="queryParams.visitee"
          placeholder="请输入被访人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发卡记录ID" prop="cardRecordId">
        <el-input
          v-model="queryParams.cardRecordId"
          placeholder="请输入发卡记录ID"
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
          v-hasPermi="['system:visitor:add']"
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
          v-hasPermi="['system:visitor:edit']"
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
          v-hasPermi="['system:visitor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:visitor:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="visitorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="visitorId" />
      <el-table-column label="身份证号" align="center" prop="visitorIdentityNo" />
      <el-table-column label="姓名" align="center" prop="visitorName" />
      <el-table-column label="手机号" align="center" prop="visitorPhone" />
      <el-table-column label="来访事由" align="center" prop="visitorPurpose" />
      <el-table-column label="预计停留时间" align="center" prop="estimatedTime" />
      <el-table-column label="被访人" align="center" prop="visitee" />
      <el-table-column label="人脸照片" align="center" prop="img" />
      <el-table-column label="发卡记录ID" align="center" prop="cardRecordId" />
      <el-table-column label="到访区域" align="center" prop="areaCode" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:visitor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:visitor:remove']"
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

    <!-- 添加或修改访客信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份证号" prop="visitorIdentityNo">
          <el-input v-model="form.visitorIdentityNo" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="姓名" prop="visitorName">
          <el-input v-model="form.visitorName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="来访事由" prop="visitorPurpose">
          <el-input v-model="form.visitorPurpose" placeholder="请输入来访事由" />
        </el-form-item>
        <el-form-item label="预计停留时间" prop="estimatedTime">
          <el-input v-model="form.estimatedTime" placeholder="请输入预计停留时间" />
        </el-form-item>
        <el-form-item label="被访人" prop="visitee">
          <el-input v-model="form.visitee" placeholder="请输入被访人" />
        </el-form-item>
        <el-form-item label="人脸照片Base64" prop="img">
          <el-input v-model="form.img" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="发卡记录ID" prop="cardRecordId">
          <el-input v-model="form.cardRecordId" placeholder="请输入发卡记录ID" />
        </el-form-item>
        <el-form-item label="到访区域:json数组" prop="areaCode">
          <el-input v-model="form.areaCode" type="textarea" placeholder="请输入内容" />
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
import { listVisitor, getVisitor, delVisitor, addVisitor, updateVisitor } from "@/api/system/wms_visitor"

export default {
  name: "Visitor",
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
      // 访客信息表格数据
      visitorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitorIdentityNo: null,
        visitorName: null,
        visitorPhone: null,
        visitorPurpose: null,
        estimatedTime: null,
        visitee: null,
        img: null,
        cardRecordId: null,
        areaCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        visitorIdentityNo: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        visitorName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        visitee: [
          { required: true, message: "被访人不能为空", trigger: "blur" }
        ],
        cardRecordId: [
          { required: true, message: "发卡记录ID不能为空", trigger: "blur" }
        ],
        areaCode: [
          { required: true, message: "到访区域:json数组不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询访客信息列表 */
    getList() {
      this.loading = true
      listVisitor(this.queryParams).then(response => {
        this.visitorList = response.rows
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
        visitorId: null,
        visitorIdentityNo: null,
        visitorName: null,
        visitorPhone: null,
        visitorPurpose: null,
        estimatedTime: null,
        visitee: null,
        img: null,
        cardRecordId: null,
        areaCode: null
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
      this.ids = selection.map(item => item.visitorId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加访客信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const visitorId = row.visitorId || this.ids
      getVisitor(visitorId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改访客信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.visitorId != null) {
            updateVisitor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVisitor(this.form).then(response => {
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
      const visitorIds = row.visitorId || this.ids
      this.$modal.confirm('是否确认删除访客信息编号为"' + visitorIds + '"的数据项？').then(function() {
        return delVisitor(visitorIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/visitor/export', {
        ...this.queryParams
      }, `visitor_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
