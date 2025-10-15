<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
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
      <el-form-item label="即时通工号">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入即时通工号"
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
      <el-form-item label="岗位">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入即时通工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入即时通工号"
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
          v-hasPermi="['system:lanya_internal_employee:add']"
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
          v-hasPermi="['system:lanya_internal_employee:edit']"
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
          v-hasPermi="['system:lanya_internal_employee:remove']"
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
          v-hasPermi="['system:lanya_internal_employee:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_internal_employeeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="卡号" align="center" prop="cardId"/>
      <el-table-column label="姓名" align="center" prop="realName"/>
      <el-table-column label="性别" align="center" prop="sex"/>
      <el-table-column label="即时通功耗" align="center"/>
      <el-table-column label="手机号" align="center" prop="phone"/>
      <el-table-column label="部门" align="center" prop="deptId"/>
      <el-table-column label="岗位" align="center" prop="postId"/>
      <el-table-column label="人员状态" align="center"/>
      <el-table-column label="发卡机" align="center"/>
      <el-table-column label="照片" align="center" width="100">
        <template slot-scope="scope">
          <el-image v-if="scope.row.personPhoto"
                    style="width: 40px; height: 40px; border-radius: 5%;"
                    :src="imgSrc(scope.row)"
                    fit="cover">
            <!-- 加载失败时显示默认图 -->
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"/>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleBindDialog(scope.row)"
            v-hasPermi="['system:lanya_internal_employee:remove']"
          >绑定
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_internal_employee:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_internal_employee:remove']"
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

    <!-- 绑定卡 -->
    <el-dialog :title="title" :visible.sync="openBind" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="发卡类型" prop="userName">
          <el-select v-model="bindForm.cardModelMulti" placeholder="请选择卡号" multiple>
            <el-option
              v-for="dict in lanyaDict.card_model"
              :key="dict.dictValue"
              :label="dict.dictValue"
              :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="发卡方式" prop="userName">
          <el-select v-model="bindForm.sendCardMode" placeholder="请选择卡号">
            <el-option
              v-for="dict in lanyaDict.send_card_mode"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="指定发卡机" prop="userName">
          <el-select v-model="bindForm.cardSenderIdList" placeholder="请选择卡号" multiple>
            <el-option
              v-for="dict in lanyaMachineListPage"
              :key="dict.cardSenderId"
              :label="dict.deviceName"
              :value="dict.cardSenderId"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleMachineConfigRelationEditByPerson">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改内部员工对话框 -->
    <el-dialog :title="title" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择">
            <el-option label="男" value="男"/>
            <el-option label="女" value="女"/>
          </el-select>
        </el-form-item>
        <el-form-item label="即时通工号">
          <el-input placeholder="请输入即时通工号"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="部门id" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门id"/>
        </el-form-item>
        <el-form-item label="岗位id" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入岗位id"/>
        </el-form-item>
        <el-form-item label="人员状态" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门id"/>
        </el-form-item>
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号"/>
        </el-form-item>
        <el-form-item label="照片" prop="personPhoto">
          <el-upload
            class="avatar-uploader"
            :action="uploadImageUrl"
            name="files"
            :data="uploadData"
            :show-file-list="false"
            :on-success="handleUploadImage">
            <el-image
              v-if="form.personPhoto"
              style="width: 40px; height: 40px; border-radius: 5%;"
              :src="imgSrc(form)"
              fit="cover">
              <!-- 加载失败时显示默认图 -->
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"/>
              </div>
            </el-image>
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
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
  list_dict_findData,
  machine_list_page,
  machineConfigRelationDetailByPerson,
  machineConfigRelationEditByPerson,
  listPersonStaffPage,
  personStaffUpdateStaff,
  personStaffAddStaff,
  personStaffDelStaff
} from "@/api/lanya_transfer"

export default {
  name: "Lanya_internal_employee",
  data() {
    return {
      imgPrefix: process.env.VUE_APP_BASE_API,
      uploadImageUrl: process.env.VUE_APP_BASE_API + '/system/lanya-transfer/files/upload',
      uploadData: {
        compress: true,
        module: 'xrkc'
      },
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
      // 内部员工表格数据
      lanya_internal_employeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDialog: false,
      // 是否显示卡弹出层
      openBind: false,
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
      cardIdList: [],
      machineIdList: [],
      bindForm: {},
      // 表单校验
      rules: {
        personType: [
          {required: true, message: "人员类型不能为空", trigger: "change"}
        ],
        realName: [
          {required: true, message: "姓名不能为空", trigger: "blur"}
        ],
      },
      lanyaDict: {},
      lanyaMachineListPage: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    imgSrc(row) {
      console.log(process)
      return `${process.env.VUE_APP_BASE_URL}/system/lanya-transfer/files/image?path=${
        encodeURIComponent(row.personPhoto)
      }`;
    },
    /** 查询内部员工列表 */
    getList() {
      this.loading = true
      list_dict_findData().then(response => {
        this.lanyaDict = response.data
        listPersonStaffPage(this.queryParams).then(response => {
          this.lanya_internal_employeeList = response.data
          console.log(this.lanya_internal_employeeList)
          this.total = response.total
          this.loading = false
        })
      })
    },
    // 取消按钮
    cancel() {
      this.openDialog = false
      this.openBind = false
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.form = {}
      this.openDialog = true
      this.title = "添加内部员工"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.form = row
      this.openDialog = true
      this.title = "修改内部员工"
    },
    /** 绑定卡 */
    handleBindDialog(row) {
      // this.reset()
      machineConfigRelationDetailByPerson({sourceId: row.personId + ""}).then(resDetail => {
        this.bindForm = resDetail.data
        if (this.bindForm) {
          this.bindForm.cardModelMulti = this.bindForm.cardModelMulti.split(",")
          this.bindForm.sourceId = row.personId
        }
        machine_list_page().then(response => {
          this.lanyaMachineListPage = response.data
          this.openBind = true
          this.title = "绑定定位卡"
        })
      }).catch(e => {
        this.bindForm = {sourceId: row.personId}
        machine_list_page().then(response => {
          this.lanyaMachineListPage = response.data
          this.openBind = true
          this.title = "绑定定位卡"
        })
      })

    },
    /** 绑定发卡机 */
    handleMachineConfigRelationEditByPerson(row) {
      let data = this.bindForm
      data.cardModelMulti = data.cardModelMulti.join(",")
      console.log(data)
      machineConfigRelationEditByPerson(data).then(response => {
        this.$modal.msgSuccess(response.data.msg)
        this.openBind = false
        this.getList()
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.personId != null) {
            personStaffUpdateStaff(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.openDialog = false
              this.getList()
            })
          } else {
            personStaffAddStaff(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.openDialog = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const personIds = row.personId || this.ids
      this.$modal.confirm('是否确认删除内部员工编号为"' + personIds + '"的数据项？').then(function () {
        return personStaffDelStaff({personIds: [row.personId]})
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_internal_employee/export', {
        ...this.queryParams
      }, `lanya_internal_employee_${new Date().getTime()}.xlsx`)
    },
    handleUploadImage(res, file) {
      // res 就是后端返回的 JSON
      console.log(res)
      if (res.code === 200) {
        this.form.personPhoto = res.data[0].filePath
        this.$modal.msgSuccess('上传成功')
      } else {
        this.$modal.msgError(res.msg || '上传失败')
      }
    }
  }
}
</script>
