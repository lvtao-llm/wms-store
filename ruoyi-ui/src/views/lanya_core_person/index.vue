<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="卡号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入卡号"
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
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证件号码" prop="idNumber">
        <el-input
          v-model="queryParams.idNumber"
          placeholder="请输入证件号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属承包商" prop="contractorId">
        <el-input
          v-model="queryParams.contractorId"
          placeholder="请输入所属承包商"
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
          v-hasPermi="['system:lanya_core_person:add']"
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
          v-hasPermi="['system:lanya_core_person:edit']"
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
          v-hasPermi="['system:lanya_core_person:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_core_person:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_core_personList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="卡号" align="center" prop="cardId" />
      <el-table-column label="人员类型" align="center" prop="personType" />
      <el-table-column label="员工类型" align="center" prop="staffType" />
      <el-table-column label="卡类型" align="center" prop="cardType" />
      <el-table-column label="姓名" align="center" prop="realName" width="100" />
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="性别" align="center" prop="sex" />
      <el-table-column label="生日" align="center" prop="birth" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birth, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="籍贯" align="center" prop="nativePlace" />
      <el-table-column label="民族" align="center" prop="nation" />
      <el-table-column label="婚姻状况" align="center" prop="maritalStatus" />
      <el-table-column label="政治面貌" align="center" prop="politicsStatus" />
      <el-table-column label="健康状况" align="center" prop="healthStatus" />
      <el-table-column label="证件类型" align="center" prop="idType" />
      <el-table-column label="证件号码" align="center" prop="idNumber" width="180"/>
      <el-table-column label="人员IC号" align="center" prop="personIc" />
      <el-table-column label="部门id" align="center" prop="deptId" />
      <el-table-column label="岗位id" align="center" prop="postId" />
      <el-table-column label="职责" align="center" prop="duty" />
      <el-table-column label="入职时间" align="center" prop="hireDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.hireDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工号" align="center" prop="jobNumber" />
      <el-table-column label="工作状态" align="center" prop="jobStatus" />
      <el-table-column label="办公电话" align="center" prop="officePhone" />
      <el-table-column label="承包商" align="center" prop="contractorId" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="人员编码" align="center" prop="personCode" />
      <el-table-column label="人员来源" align="center" prop="personSource" />
      <el-table-column label="闸机通行" align="center" prop="gateThrough" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_core_person:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_core_person:remove']"
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

    <!-- 添加或修改人员（员工/访客/承包商人员）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="照片" prop="personPhoto">
          <el-input v-model="form.personPhoto" placeholder="请输入照片" />
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-date-picker clearable
            v-model="form.birth"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="籍贯" prop="nativePlace">
          <el-input v-model="form.nativePlace" placeholder="请输入籍贯" />
        </el-form-item>
        <el-form-item label="民族" prop="nation">
          <el-input v-model="form.nation" placeholder="请输入民族" />
        </el-form-item>
        <el-form-item label="证件号码" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入证件号码" />
        </el-form-item>
        <el-form-item label="人员IC号" prop="personIc">
          <el-input v-model="form.personIc" placeholder="请输入人员IC号" />
        </el-form-item>
        <el-form-item label="管理人员姓名" prop="administratorName">
          <el-input v-model="form.administratorName" placeholder="请输入管理人员姓名" />
        </el-form-item>
        <el-form-item label="管理人员号码" prop="administratorPhone">
          <el-input v-model="form.administratorPhone" placeholder="请输入管理人员号码" />
        </el-form-item>
        <el-form-item label="部门id" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门id" />
        </el-form-item>
        <el-form-item label="岗位id" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入岗位id" />
        </el-form-item>
        <el-form-item label="职责" prop="duty">
          <el-input v-model="form.duty" placeholder="请输入职责" />
        </el-form-item>
        <el-form-item label="入职时间" prop="hireDate">
          <el-date-picker clearable
            v-model="form.hireDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入职时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工号" prop="jobNumber">
          <el-input v-model="form.jobNumber" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="办公电话" prop="officePhone">
          <el-input v-model="form.officePhone" placeholder="请输入办公电话" />
        </el-form-item>
        <el-form-item label="职称" prop="professionalTitle">
          <el-input v-model="form.professionalTitle" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="职称证书编号" prop="professionalTitleNumber">
          <el-input v-model="form.professionalTitleNumber" placeholder="请输入职称证书编号" />
        </el-form-item>
        <el-form-item label="工龄" prop="seniority">
          <el-input v-model="form.seniority" placeholder="请输入工龄" />
        </el-form-item>
        <el-form-item label="三年以上化工行业经验" prop="yearPlusExperience">
          <el-input v-model="form.yearPlusExperience" placeholder="请输入三年以上化工行业经验" />
        </el-form-item>
        <el-form-item label="工作经历" prop="experience">
          <el-input v-model="form.experience" placeholder="请输入工作经历" />
        </el-form-item>
        <el-form-item label="最高学历" prop="highestEducation">
          <el-input v-model="form.highestEducation" placeholder="请输入最高学历" />
        </el-form-item>
        <el-form-item label="最高学位" prop="highestDegree">
          <el-input v-model="form.highestDegree" placeholder="请输入最高学位" />
        </el-form-item>
        <el-form-item label="专业" prop="profession">
          <el-input v-model="form.profession" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="毕业院校" prop="school">
          <el-input v-model="form.school" placeholder="请输入毕业院校" />
        </el-form-item>
        <el-form-item label="毕业证书编号" prop="diplomaNumber">
          <el-input v-model="form.diplomaNumber" placeholder="请输入毕业证书编号" />
        </el-form-item>
        <el-form-item label="注册工程师证书编号" prop="cengNumber">
          <el-input v-model="form.cengNumber" placeholder="请输入注册工程师证书编号" />
        </el-form-item>
        <el-form-item label="附件" prop="accessory">
          <el-input v-model="form.accessory" placeholder="请输入附件" />
        </el-form-item>
        <el-form-item label="员工离职日期/承包商人员入场到期日期" prop="dimissionDate">
          <el-date-picker clearable
            v-model="form.dimissionDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择员工离职日期/承包商人员入场到期日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="所属承包商" prop="contractorId">
          <el-input v-model="form.contractorId" placeholder="请输入所属承包商" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="人员编码" prop="personCode">
          <el-input v-model="form.personCode" placeholder="请输入人员编码" />
        </el-form-item>
        <el-form-item label="二道门进入时间" prop="gateInTime">
          <el-date-picker clearable
            v-model="form.gateInTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择二道门进入时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="二道门出去时间" prop="gateOutTime">
          <el-date-picker clearable
            v-model="form.gateOutTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择二道门出去时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="人员来源" prop="personSource">
          <el-input v-model="form.personSource" placeholder="请输入人员来源" />
        </el-form-item>
        <el-form-item label="闸机通行 Y：允许 N：禁止" prop="gateThrough">
          <el-input v-model="form.gateThrough" placeholder="请输入闸机通行 Y：允许 N：禁止" />
        </el-form-item>
        <el-form-item label="同步第三方人员是否成功 1 成功 2 失败" prop="updateFail">
          <el-input v-model="form.updateFail" placeholder="请输入同步第三方人员是否成功 1 成功 2 失败" />
        </el-form-item>
        <el-form-item label="人脸照片唯一标识" prop="photoSign">
          <el-input v-model="form.photoSign" placeholder="请输入人脸照片唯一标识" />
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
import { listLanya_core_person, getLanya_core_person, delLanya_core_person, addLanya_core_person, updateLanya_core_person } from "@/api/system/lanya_core_person"

export default {
  name: "Lanya_core_person",
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
      // 人员（员工/访客/承包商人员）表格数据
      lanya_core_personList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        personType: null,
        staffType: null,
        positionIconType: null,
        cardId: null,
        cardType: null,
        realName: null,
        phone: null,
        sex: null,
        personPhoto: null,
        birth: null,
        email: null,
        nativePlace: null,
        nation: null,
        maritalStatus: null,
        politicsStatus: null,
        healthStatus: null,
        idType: null,
        idNumber: null,
        personIc: null,
        administratorName: null,
        administratorPhone: null,
        deptId: null,
        postId: null,
        duty: null,
        hireDate: null,
        jobNumber: null,
        jobStatus: null,
        officePhone: null,
        professionalTitle: null,
        professionalTitleNumber: null,
        seniority: null,
        yearPlusExperience: null,
        experience: null,
        highestEducation: null,
        highestDegree: null,
        profession: null,
        school: null,
        diplomaNumber: null,
        cengNumber: null,
        accessory: null,
        dimissionDate: null,
        contractorId: null,
        personCode: null,
        gateStatus: null,
        gateInTime: null,
        gateOutTime: null,
        personSource: null,
        gateThrough: null,
        updateFail: null,
        photoSign: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        personType: [
          { required: true, message: "人员类型不能为空", trigger: "change" }
        ],
        realName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询人员（员工/访客/承包商人员）列表 */
    getList() {
      this.loading = true
      listLanya_core_person(this.queryParams).then(response => {
        this.lanya_core_personList = response.rows
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
        staffType: null,
        positionIconType: null,
        cardId: null,
        cardType: null,
        realName: null,
        phone: null,
        sex: null,
        personPhoto: null,
        birth: null,
        email: null,
        nativePlace: null,
        nation: null,
        maritalStatus: null,
        politicsStatus: null,
        healthStatus: null,
        idType: null,
        idNumber: null,
        personIc: null,
        administratorName: null,
        administratorPhone: null,
        deptId: null,
        postId: null,
        duty: null,
        hireDate: null,
        jobNumber: null,
        jobStatus: null,
        officePhone: null,
        professionalTitle: null,
        professionalTitleNumber: null,
        seniority: null,
        yearPlusExperience: null,
        experience: null,
        highestEducation: null,
        highestDegree: null,
        profession: null,
        school: null,
        diplomaNumber: null,
        cengNumber: null,
        accessory: null,
        dimissionDate: null,
        contractorId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        personCode: null,
        gateStatus: null,
        gateInTime: null,
        gateOutTime: null,
        personSource: null,
        gateThrough: null,
        updateFail: null,
        photoSign: null
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
      this.title = "添加人员（员工/访客/承包商人员）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const personId = row.personId || this.ids
      getLanya_core_person(personId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改人员（员工/访客/承包商人员）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.personId != null) {
            updateLanya_core_person(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_core_person(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除人员（员工/访客/承包商人员）编号为"' + personIds + '"的数据项？').then(function() {
        return delLanya_core_person(personIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_core_person/export', {
        ...this.queryParams
      }, `lanya_core_person_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
