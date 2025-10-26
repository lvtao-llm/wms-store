<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上报人" prop="reporterId">
        <el-input
          v-model="queryParams.reporterName"
          placeholder="请输入上报人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="解决人姓名" prop="resolverName">
        <el-input
          v-model="queryParams.resolverName"
          placeholder="请输入解决人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否已解决" prop="isResolved">
        <el-select v-model="queryParams.isResolved" placeholder="请选择是否已解决" clearable>
          <el-option
            v-for="dict in dict.type.wms_issue_is_resolved"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_inspection_issue:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_inspection_issueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="规则名称" align="center" prop="ruleName"/>
      <el-table-column label="上报人" align="center" prop="reporterId">
        <template slot-scope="scope">
          {{ getStaffNameById(scope.row.reporterId) }}
        </template>
      </el-table-column>
      <el-table-column label="问题时间" align="center" prop="issueTime" width="180">
        <template slot-scope="scope">
          <div>{{ parseTime(scope.row.issueTime, '{y}-{m}-{d}') }}</div>
          <div>{{ parseTime(scope.row.issueTime, '{h}:{i}:{s}') }}</div>
        </template>
      </el-table-column>
      <el-table-column label="问题描述" align="center" prop="description"/>
      <el-table-column label="问题照片" align="center" prop="issuePhotos" width="100">
        <template slot-scope="scope">
          <div v-if="scope.row.issuePhotos">
            <image-preview
              v-for="(photo, index) in scope.row.issuePhotos.split(',')"
              :key="index"
              :src="photo.trim()"
              :width="50"
              :height="50"      style="margin-right: 5px; margin-bottom: 5px;"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="解决人" align="center" prop="resolverId">
        <template slot-scope="scope">
          {{ getStaffNameById(scope.row.resolverId) }}
        </template>
      </el-table-column>
      <el-table-column label="解决时间" align="center" prop="resolveTime" width="180">
        <template slot-scope="scope">
          <div>{{ parseTime(scope.row.resolveTime, '{y}-{m}-{d}') }}</div>
          <div>{{ parseTime(scope.row.resolveTime, '{h}:{i}:{s}') }}</div>
        </template>
      </el-table-column>
      <el-table-column label="解决描述" align="center" prop="resolveDescription"/>
      <el-table-column label="解决照片" align="center" prop="resolvePhotos" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.resolvePhotos" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="是否已解决" align="center" prop="isResolved">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_issue_is_resolved" :value="scope.row.isResolved"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_inspection_issue:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleResolve(scope.row)"
            v-hasPermi="['system:wms_inspection_issue:edit']"
          >处理
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_inspection_issue:remove']"
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

    <!-- 添加或修改巡检问题对话框 -->
    <el-dialog :title="title" :visible.sync="openCreate" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务" prop="taskId">
          {{ task.ruleName }}({{ task.taskTime }})
        </el-form-item>
        <el-form-item label="上报人" prop="reporterId">
          <el-select v-model="form.reporterId" placeholder="请输入上报人">
            <el-option
              v-for="dict in staffs"
              :key="dict.personId"
              :label="dict.realName"
              :value="dict.personId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="问题时间" prop="issueTime">
          <el-date-picker clearable
                          v-model="form.issueTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm"
                          placeholder="请选择问题时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="问题照片" prop="issuePhotos">
          <image-upload v-model="form.issuePhotos"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改巡检问题对话框 -->
    <el-dialog :title="title" :visible.sync="openResolved" width="500px" append-to-body>
      <el-form ref="form" :model="formResolved" :rules="rules" label-width="100px">
        <el-form-item label="任务" prop="taskId">
          {{ task.ruleName }}({{ task.taskTime }})
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          {{ formResolved.description }}
        </el-form-item>
        <el-form-item label="解决人" prop="resolverId">
          <el-select v-model="formResolved.resolverId" placeholder="请输入解决人">
            <el-option
              v-for="dict in staffs"
              :key="dict.personId"
              :label="dict.realName"
              :value="dict.personId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="解决时间" prop="resolveTime">
          <el-date-picker clearable
                          v-model="formResolved.resolveTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm"
                          placeholder="请选择解决时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="解决描述" prop="resolveDescription">
          <el-input v-model="formResolved.resolveDescription" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="解决照片" prop="resolvePhotos">
          <image-upload v-model="formResolved.resolvePhotos"/>
        </el-form-item>
        <el-form-item label="是否已解决" prop="isResolved">
          <el-select v-model="formResolved.isResolved" placeholder="请选择是否已解决">
            <el-option
              v-for="dict in dict.type.wms_issue_is_resolved"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormResolved">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listWms_inspection_issue,
  getWms_inspection_issue,
  delWms_inspection_issue,
  addWms_inspection_issue,
  updateWms_inspection_issue
} from "@/api/system/wms_inspection_issue"
import {
  listWms_inspection_task,
  getWms_inspection_task,
  delWms_inspection_task,
  addWms_inspection_task,
  updateWms_inspection_task
} from "@/api/system/wms_inspection_task"
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
  name: "Wms_inspection_issue",
  dicts: ['wms_issue_is_resolved'],
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
      // 巡检问题表格数据
      wms_inspection_issueList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openCreate: false,
      openResolved: false,
      isSelf: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        ruleId: null,
        ruleName: null,
        reporterId: null,
        cardId: null,
        issueTime: null,
        description: null,
        issuePhotos: null,
        resolverId: null,
        resolverName: null,
        resolveTime: null,
        resolveDescription: null,
        resolvePhotos: null,
        isResolved: null,
      },
      // 表单参数
      form: {},
      formResolved: {},
      task: {},
      staffs: [],
      // 表单校验
      rules: {
        taskId: [
          {required: true, message: "任务ID不能为空", trigger: "blur"}
        ],
        ruleId: [
          {required: true, message: "规则ID不能为空", trigger: "blur"}
        ],
        ruleName: [
          {required: true, message: "规则名称不能为空", trigger: "blur"}
        ],
        issueTime: [
          {required: true, message: "问题时间不能为空", trigger: "blur"}
        ],
        isResolved: [
          {required: true, message: "是否已解决不能为空", trigger: "change"}
        ],
      }
    }
  },
  created() {
    // 接收路由参数
    const taskId = this.$route.query.taskId;
    const ruleId = this.$route.query.ruleId;
    if (taskId) {
      // 如果有任务ID参数，自动打开创建对话框
      this.isSelf = true;
      this.handleCreateWithTask(taskId, ruleId);
    } else {
      this.isSelf = false;
      this.getList();
    }
  },
  methods: {
    /** 查询巡检问题列表 */
    getList() {
      this.loading = true
      listPersonStaffPage({
        pageNum: 1,
        pageSize: 10000  // 或其他合适的页面大小
      }).then(response => {
        this.staffs = response.data;
        listWms_inspection_issue(this.queryParams).then(response => {
          this.wms_inspection_issueList = response.rows
          this.total = response.total
          this.loading = false
        })
      })
    },
    // 取消按钮
    cancel() {
      this.openCreate = false
      this.openResolved = false
      this.reset()
      if (this.isSelf) {
        this.$router.go(-1)
      }
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        taskId: null,
        ruleId: null,
        ruleName: null,
        reporterId: null,
        cardId: null,
        issueTime: null,
        description: null,
        issuePhotos: null,
        createTime: null,
        updateTime: null
      }
      this.formResolved={
        id: null,
        resolverId: null,
        resolverName: null,
        resolveTime: null,
        resolveDescription: null,
        resolvePhotos: null,
        isResolved: null,
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
      this.openCreate = true
      this.title = "添加巡检问题"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      listPersonStaffPage({
        pageNum: 1,
        pageSize: 100  // 或其他合适的页面大小
      }).then(response => {
        this.staffs = response.data;
        getWms_inspection_task(row.taskId).then(response => {
          this.task = response.data;
          getWms_inspection_issue(id).then(response => {
            this.form = response.data
            this.openCreate = true
            this.title = "修改巡检问题"
          })
        })
      })
    },
    handleResolve(row){
      this.reset()
      const id = row.id || this.ids
      listPersonStaffPage({
        pageNum: 1,
        pageSize: 100  // 或其他合适的页面大小
      }).then(response => {
        this.staffs = response.data;
        getWms_inspection_task(row.taskId).then(response => {
          this.task = response.data;
          getWms_inspection_issue(id).then(response => {
            this.formResolved = response.data
            this.formResolved.resolveTime = new Date()
            this.openResolved = true
            this.title = "解决巡检问题"
          })
        })
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const formData = {...this.form};
          if (formData.issueTime) {
            formData.issueTime = this.parseTime(formData.issueTime, '{y}-{m}-{d} {h}:{i}:{s}');
          }
          if (formData.id != null) {
            updateWms_inspection_issue(formData).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.openCreate = false
              this.getList()
            })
          } else {
            addWms_inspection_issue(formData).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.openCreate = false
              this.getList()
            })
          }
          if (this.isSelf) {
            this.$router.go(-1)
          }
        }
      })
    },
    submitFormResolved() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const formData = {...this.formResolved};
          if (formData.resolveTime) {
            formData.resolveTime = this.parseTime(formData.resolveTime, '{y}-{m}-{d} {h}:{i}:{s}');
          }
          updateWms_inspection_issue(formData).then(response => {
            this.$modal.msgSuccess("修改成功")
            this.openResolved = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除巡检问题编号为"' + ids + '"的数据项？').then(function () {
        return delWms_inspection_issue(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_inspection_issue/export', {
        ...this.queryParams
      }, `wms_inspection_issue_${new Date().getTime()}.xlsx`)
    },
    handleCreateWithTask(taskId, ruleId) {
      this.reset();
      // 设置表单默认值
      listPersonStaffPage({
        pageNum: 1,
        pageSize: 100  // 或其他合适的页面大小
      }).then(response => {
        this.staffs = response.data;
        getWms_inspection_task(taskId).then(response => {
          this.task = response.data;
          this.form.taskId = response.data.id;
          this.form.ruleId = response.data.ruleId
          this.form.ruleName = response.data.ruleName
          this.form.issueTime = new Date();
          this.openCreate = true; // 打开创建对话框
          this.title = "上报问题";
        })
      })
    },
    getStaffNameById(staffId) {
      console.log(staffId, this.staffs)
      if (!staffId || !this.staffs || this.staffs.length === 0) {
        return '';
      }
      const staff = this.staffs.find(item => item.personId === (staffId + ''));
      return staff ? staff.realName : '';
    }
  }
}
</script>
