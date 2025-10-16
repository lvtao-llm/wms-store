<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工编号" prop="personEmpNo">
        <el-input
          v-model="queryParams.personEmpNo"
          placeholder="请输入员工编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="personIdentityNo">
        <el-input
          v-model="queryParams.personIdentityNo"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="personName">
        <el-input
          v-model="queryParams.personName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年龄" prop="personAge">
        <el-input
          v-model="queryParams.personAge"
          placeholder="请输入年龄"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="personPhone">
        <el-input
          v-model="queryParams.personPhone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职位" prop="personPost">
        <el-input
          v-model="queryParams.personPost"
          placeholder="请输入职位"
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
      <el-form-item label="部门ID" prop="personDeptId">
        <el-input
          v-model="queryParams.personDeptId"
          placeholder="请输入部门ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位卡号" prop="personCardNo">
        <el-input
          v-model="queryParams.personCardNo"
          placeholder="请输入定位卡号"
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
          v-hasPermi="['system:person:add']"
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
          v-hasPermi="['system:person:edit']"
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
          v-hasPermi="['system:person:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:person:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="personId" />
      <el-table-column label="员工编号" align="center" prop="personEmpNo" />
      <el-table-column label="身份证号" align="center" prop="personIdentityNo" />
      <el-table-column label="姓名" align="center" prop="personName" />
      <el-table-column label="性别" align="center" prop="personSex" />
      <el-table-column label="年龄" align="center" prop="personAge" />
      <el-table-column label="手机号" align="center" prop="personPhone" />
      <el-table-column label="职位" align="center" prop="personPost" />
      <el-table-column label="工号" align="center" prop="personJobNo" />
      <el-table-column label="部门ID" align="center" prop="personDeptId" />
      <el-table-column label="定位卡号" align="center" prop="personCardNo" />
      <el-table-column label="授权区域" align="center" prop="personAuthArea" />
      <el-table-column label="启用" align="center" prop="personStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:person:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:person:remove']"
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

    <!-- 添加或修改人员档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工编号" prop="personEmpNo">
          <el-input v-model="form.personEmpNo" placeholder="请输入员工编号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="personIdentityNo">
          <el-input v-model="form.personIdentityNo" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="姓名" prop="personName">
          <el-input v-model="form.personName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="年龄" prop="personAge">
          <el-input v-model="form.personAge" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="手机号" prop="personPhone">
          <el-input v-model="form.personPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="职位" prop="personPost">
          <el-input v-model="form.personPost" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="工号" prop="personJobNo">
          <el-input v-model="form.personJobNo" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="部门ID" prop="personDeptId">
          <el-input v-model="form.personDeptId" placeholder="请输入部门ID" />
        </el-form-item>
        <el-form-item label="人脸照片Base64" prop="personFaceImg">
          <el-input v-model="form.personFaceImg" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="定位卡号" prop="personCardNo">
          <el-input v-model="form.personCardNo" placeholder="请输入定位卡号" />
        </el-form-item>
        <el-form-item label="授权区域JSON数组" prop="personAuthArea">
          <el-input v-model="form.personAuthArea" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="${comment}" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入${comment}" />
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
import { listPerson, getPerson, delPerson, addPerson, updatePerson } from "@/api/system/wms_person"

export default {
  name: "Person",
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
      // 人员档案表格数据
      personList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        personType: null,
        personEmpNo: null,
        personIdentityNo: null,
        personName: null,
        personSex: null,
        personAge: null,
        personPhone: null,
        personPost: null,
        personJobNo: null,
        personDeptId: null,
        personFaceImg: null,
        personCardNo: null,
        personAuthArea: null,
        personStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        personType: [
          { required: true, message: "1员工 2访客不能为空", trigger: "change" }
        ],
        personIdentityNo: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        personName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        personSex: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        personAge: [
          { required: true, message: "年龄不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询人员档案列表 */
    getList() {
      this.loading = true
      listPerson(this.queryParams).then(response => {
        this.personList = response.rows
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
        personId: null,
        personType: null,
        personEmpNo: null,
        personIdentityNo: null,
        personName: null,
        personSex: null,
        personAge: null,
        personPhone: null,
        personPost: null,
        personJobNo: null,
        personDeptId: null,
        personFaceImg: null,
        personCardNo: null,
        personAuthArea: null,
        personStatus: null,
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
      this.ids = selection.map(item => item.personId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加人员档案"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const personId = row.personId || this.ids
      getPerson(personId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改人员档案"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.personId != null) {
            updatePerson(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPerson(this.form).then(response => {
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
      const personIds = row.personId || this.ids
      this.$modal.confirm('是否确认删除人员档案编号为"' + personIds + '"的数据项？').then(function() {
        return delPerson(personIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/person/export', {
        ...this.queryParams
      }, `person_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
