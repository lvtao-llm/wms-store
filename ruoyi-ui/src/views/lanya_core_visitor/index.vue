<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="140px">
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
      <el-form-item label="照片" prop="personPhoto">
        <el-input
          v-model="queryParams.personPhoto"
          placeholder="请输入照片"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生日" prop="birth">
        <el-date-picker clearable
          v-model="queryParams.birth"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="籍贯" prop="nativePlace">
        <el-input
          v-model="queryParams.nativePlace"
          placeholder="请输入籍贯"
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
      <el-form-item label="人员IC号" prop="personIc">
        <el-input
          v-model="queryParams.personIc"
          placeholder="请输入人员IC号"
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
      <el-form-item label="管理人员姓名" prop="administratorName">
        <el-input
          v-model="queryParams.administratorName"
          placeholder="请输入管理人员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="管理人员号码" prop="administratorPhone">
        <el-input
          v-model="queryParams.administratorPhone"
          placeholder="请输入管理人员号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="jobNumber">
        <el-input
          v-model="queryParams.jobNumber"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在单位" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入所在单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="办公电话" prop="officePhone">
        <el-input
          v-model="queryParams.officePhone"
          placeholder="请输入办公电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约进入开始时间" prop="reservationEntryStartTime">
        <el-date-picker clearable
          v-model="queryParams.reservationEntryStartTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预约进入开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预约进入结束时间" prop="reservationEntryEndTime">
        <el-date-picker clearable
          v-model="queryParams.reservationEntryEndTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预约进入结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="来访时间" prop="visitTime">
        <el-date-picker clearable
          v-model="queryParams.visitTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择来访时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="离开时间" prop="leaveTime">
        <el-date-picker clearable
          v-model="queryParams.leaveTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择离开时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="来访原因" prop="visitReason">
        <el-input
          v-model="queryParams.visitReason"
          placeholder="请输入来访原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来访地点" prop="visitLocation">
        <el-input
          v-model="queryParams.visitLocation"
          placeholder="请输入来访地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联人员id" prop="personId">
        <el-input
          v-model="queryParams.personId"
          placeholder="请输入关联人员id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接待人" prop="receiveLeader">
        <el-input
          v-model="queryParams.receiveLeader"
          placeholder="请输入接待人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接待人电话" prop="receivePhone">
        <el-input
          v-model="queryParams.receivePhone"
          placeholder="请输入接待人电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="闸机通行" prop="gateThrough">
        <el-input
          v-model="queryParams.gateThrough"
          placeholder="请输入闸机通行 Y：允许 N：禁止"
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
          v-hasPermi="['system:lanya_core_visitor:add']"
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
          v-hasPermi="['system:lanya_core_visitor:edit']"
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
          v-hasPermi="['system:lanya_core_visitor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_core_visitor:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_core_visitorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访客id" align="center" prop="visitorId" width="200"/>
      <el-table-column label="卡号" align="center" prop="cardId" />
      <el-table-column label="定位图标类型" align="center" prop="positionIconType" width="100"/>
      <el-table-column label="卡类型" align="center" prop="cardType" />
      <el-table-column label="姓名" align="center" prop="realName" />
      <el-table-column label="手机号" align="center" prop="phone" width="120"/>
      <el-table-column label="性别" align="center" prop="sex" />
      <el-table-column label="照片" align="center" prop="personPhoto" />
      <el-table-column label="生日" align="center" prop="birth" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birth, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="籍贯" align="center" prop="nativePlace" />
      <el-table-column label="政治面貌" align="center" prop="politicsStatus" />
      <el-table-column label="健康状况" align="center" prop="healthStatus" />
      <el-table-column label="证件类型" align="center" prop="idType" />
      <el-table-column label="证件号码" align="center" prop="idNumber" width="200"/>
      <el-table-column label="人员IC号" align="center" prop="personIc" />
      <el-table-column label="部门id" align="center" prop="deptId" />
      <el-table-column label="岗位id" align="center" prop="postId" />
      <el-table-column label="管理人员姓名" align="center" prop="administratorName" width="100"/>
      <el-table-column label="管理人员号码" align="center" prop="administratorPhone" width="100"/>
      <el-table-column label="工号" align="center" prop="jobNumber" />
      <el-table-column label="所在单位" align="center" prop="companyName" />
      <el-table-column label="办公电话" align="center" prop="officePhone" />
      <el-table-column label="预约进入开始时间" align="center" prop="reservationEntryStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reservationEntryStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约进入结束时间" align="center" prop="reservationEntryEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reservationEntryEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来访时间" align="center" prop="visitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="离开时间" align="center" prop="leaveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.leaveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来访状态" align="center" prop="visitorStatus" />
      <el-table-column label="来访原因" align="center" prop="visitReason" />
      <el-table-column label="来访地点" align="center" prop="visitLocation" />
      <el-table-column label="接待人" align="center" prop="receiveLeader" />
      <el-table-column label="接待人电话" align="center" prop="receivePhone" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="闸机通行" align="center" prop="gateThrough" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_core_visitor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_core_visitor:remove']"
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

    <!-- 添加或修改访客记录对话框 -->
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
        <el-form-item label="证件号码" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入证件号码" />
        </el-form-item>
        <el-form-item label="人员IC号" prop="personIc">
          <el-input v-model="form.personIc" placeholder="请输入人员IC号" />
        </el-form-item>
        <el-form-item label="部门id" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入部门id" />
        </el-form-item>
        <el-form-item label="岗位id" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入岗位id" />
        </el-form-item>
        <el-form-item label="管理人员姓名" prop="administratorName">
          <el-input v-model="form.administratorName" placeholder="请输入管理人员姓名" />
        </el-form-item>
        <el-form-item label="管理人员号码" prop="administratorPhone">
          <el-input v-model="form.administratorPhone" placeholder="请输入管理人员号码" />
        </el-form-item>
        <el-form-item label="工号" prop="jobNumber">
          <el-input v-model="form.jobNumber" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="所在单位" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入所在单位" />
        </el-form-item>
        <el-form-item label="办公电话" prop="officePhone">
          <el-input v-model="form.officePhone" placeholder="请输入办公电话" />
        </el-form-item>
        <el-form-item label="预约进入开始时间" prop="reservationEntryStartTime">
          <el-date-picker clearable
            v-model="form.reservationEntryStartTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预约进入开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预约进入结束时间" prop="reservationEntryEndTime">
          <el-date-picker clearable
            v-model="form.reservationEntryEndTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预约进入结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="来访时间" prop="visitTime">
          <el-date-picker clearable
            v-model="form.visitTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择来访时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="离开时间" prop="leaveTime">
          <el-date-picker clearable
            v-model="form.leaveTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择离开时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="来访原因" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入来访原因" />
        </el-form-item>
        <el-form-item label="来访地点" prop="visitLocation">
          <el-input v-model="form.visitLocation" placeholder="请输入来访地点" />
        </el-form-item>
        <el-form-item label="关联人员id" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入关联人员id" />
        </el-form-item>
        <el-form-item label="接待人" prop="receiveLeader">
          <el-input v-model="form.receiveLeader" placeholder="请输入接待人" />
        </el-form-item>
        <el-form-item label="接待人电话" prop="receivePhone">
          <el-input v-model="form.receivePhone" placeholder="请输入接待人电话" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="闸机通行 Y：允许 N：禁止" prop="gateThrough">
          <el-input v-model="form.gateThrough" placeholder="请输入闸机通行 Y：允许 N：禁止" />
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
import { listLanya_core_visitor, getLanya_core_visitor, delLanya_core_visitor, addLanya_core_visitor, updateLanya_core_visitor } from "@/api/system/lanya_core_visitor"

export default {
  name: "Lanya_core_visitor",
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
      // 访客记录表格数据
      lanya_core_visitorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        positionIconType: null,
        cardType: null,
        realName: null,
        phone: null,
        sex: null,
        personPhoto: null,
        birth: null,
        email: null,
        nativePlace: null,
        politicsStatus: null,
        healthStatus: null,
        idType: null,
        idNumber: null,
        personIc: null,
        deptId: null,
        postId: null,
        administratorName: null,
        administratorPhone: null,
        jobNumber: null,
        companyName: null,
        officePhone: null,
        reservationEntryStartTime: null,
        reservationEntryEndTime: null,
        visitTime: null,
        leaveTime: null,
        visitorStatus: null,
        visitReason: null,
        visitLocation: null,
        personId: null,
        receiveLeader: null,
        receivePhone: null,
        gateThrough: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
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
    /** 查询访客记录列表 */
    getList() {
      this.loading = true
      listLanya_core_visitor(this.queryParams).then(response => {
        this.lanya_core_visitorList = response.rows
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
        cardId: null,
        positionIconType: null,
        cardType: null,
        realName: null,
        phone: null,
        sex: null,
        personPhoto: null,
        birth: null,
        email: null,
        nativePlace: null,
        politicsStatus: null,
        healthStatus: null,
        idType: null,
        idNumber: null,
        personIc: null,
        deptId: null,
        postId: null,
        administratorName: null,
        administratorPhone: null,
        jobNumber: null,
        companyName: null,
        officePhone: null,
        reservationEntryStartTime: null,
        reservationEntryEndTime: null,
        visitTime: null,
        leaveTime: null,
        visitorStatus: null,
        visitReason: null,
        visitLocation: null,
        personId: null,
        receiveLeader: null,
        receivePhone: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        gateThrough: null
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
      this.title = "添加访客记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const visitorId = row.visitorId || this.ids
      getLanya_core_visitor(visitorId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改访客记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.visitorId != null) {
            updateLanya_core_visitor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_core_visitor(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除访客记录编号为"' + visitorIds + '"的数据项？').then(function() {
        return delLanya_core_visitor(visitorIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_core_visitor/export', {
        ...this.queryParams
      }, `lanya_core_visitor_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
