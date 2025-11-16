<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="规则名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['system:wms_inspection_rule:add']"
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
          v-hasPermi="['system:wms_inspection_rule:edit']"
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
          v-hasPermi="['system:wms_inspection_rule:remove']"
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
          v-hasPermi="['system:wms_inspection_rule:export']"
          >导出
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="wms_inspection_ruleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="规则名称" align="center" prop="name" />
      <el-table-column label="巡检周期" align="center" prop="cron" />
      <el-table-column
        label="巡检时间范围"
        align="center"
        prop="timeTolerance"
      />
      <el-table-column
        label="巡检点距离"
        align="center"
        prop="pointTolerance"
      />
      <el-table-column
        label="按顺序巡检"
        align="center"
        prop="sequentialInspection"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.sequentialInspection"
          />
        </template>
      </el-table-column>
      <el-table-column label="巡检角色" align="center" prop="inspectors">
        <template slot-scope="scope">
          <dict-tag :options="roles" :value="scope.row.inspectors" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_job_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="centerr"
        class-name="small-padding fixed-width"
        width="200px"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_inspection_rule:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdateInspection(scope.row)"
            v-hasPermi="['system:wms_inspection_rule:edit']"
            >巡检点
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_inspection_rule:remove']"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改巡检规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="规则名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="巡检周期" prop="cron">
          <el-input
            v-model="form.cron"
            placeholder="请输入巡检周期cron执行表达式"
          >
            <template slot="append">
              <el-button type="primary" @click="handleShowCron">
                生成表达式
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="巡检时间范围" prop="timeTolerance">
          <el-input
            v-model="form.timeTolerance"
            placeholder="请输入巡检时间范围"
          >
            <template slot="append">
              <span>分钟</span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="巡检点距离范围" prop="pointTolerance">
          <el-input
            v-model="form.pointTolerance"
            placeholder="请输入巡检点误差"
          >
            <template slot="append">
              <span>米</span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="按顺序巡检" prop="sequentialInspection">
          <el-select
            v-model="form.sequentialInspection"
            placeholder="请选择是否按顺序巡检"
          >
            <el-option
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="巡检角色" prop="inspectors">
          <el-select v-model="form.inspectors" placeholder="请选择角色">
            <el-option
              v-for="dict in roles"
              :key="dict.roleName"
              :label="dict.roleName"
              :value="dict.roleName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.sys_job_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="Cron表达式生成器"
      :visible.sync="openCron"
      append-to-body
      destroy-on-close
      class="scrollbar"
    >
      <crontab
        @hide="openCron = false"
        @fill="crontabFill"
        :expression="expression"
      ></crontab>
    </el-dialog>
    <maps @editPoints="savePoints" ref="maps"></maps>
  </div>
</template>

<script>
import {
  listWms_inspection_rule,
  getWms_inspection_rule,
  delWms_inspection_rule,
  addWms_inspection_rule,
  updateWms_inspection_rule,
} from "@/api/system/wms_inspection_rule";
import maps from "../wms_area/childView/detail3d";
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  dataScope,
  changeRoleStatus,
  deptTreeSelect,
} from "@/api/system/role";
import Crontab from "@/components/Crontab/index.vue";

export default {
  components: { Crontab, maps },
  name: "Wms_inspection_rule",
  dicts: ["sys_job_status", "sys_yes_no"],
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
      // 巡检规则表格数据
      wms_inspection_ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 显示生成表达式
      openCron: false,
      expression: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        cron: null,
        timeTolerance: null,
        pathPoints: null,
        pointTolerance: null,
        sequentialInspection: null,
        inspectors: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 角色列表
      roles: [],
      // 表单校验
      rules: {
        name: [
          { required: true, message: "规则名称不能为空", trigger: "blur" },
        ],
        cron: [
          { required: true, message: "巡检周期不能为空", trigger: "blur" },
        ],
        timeTolerance: [
          { required: true, message: "巡检时间误差不能为空", trigger: "blur" },
        ],
        pointTolerance: [
          { required: true, message: "巡检点误差不能为空", trigger: "blur" },
        ],
        sequentialInspection: [
          {
            required: true,
            message: "是否按路径点顺序巡检不能为空",
            trigger: "change",
          },
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询巡检规则列表 */
    getList() {
      this.loading = true;
      listRole({}).then((response) => {
        this.roles = response.rows;
        listWms_inspection_rule(this.queryParams).then((response) => {
          this.wms_inspection_ruleList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        cron: null,
        timeTolerance: null,
        pathPoints: null,
        pointTolerance: null,
        sequentialInspection: null,
        inspectors: null,
        status: null,
        createTime: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加巡检规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWms_inspection_rule(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改巡检规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_inspection_rule(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWms_inspection_rule(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除巡检规则编号为"' + ids + '"的数据项？')
        .then(function () {
          return delWms_inspection_rule(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/wms_inspection_rule/export",
        {
          ...this.queryParams,
        },
        `wms_inspection_rule_${new Date().getTime()}.xlsx`
      );
    },
    /** cron表达式按钮操作 */
    handleShowCron() {
      this.expression = this.form.cron;
      this.openCron = true;
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.form.cron = value;
    },
    /** 修改巡检点 */
    handleUpdateInspection(row) {
      // this.reset();
      // const id = row.id || this.ids;
      // getWms_inspection_rule(id).then((response) => {
      //   this.form = response.data;
      //   //TODO 打开巡检点的地图标注窗体标注巡检点
      //   //巡检点数据格式:{"巡检顺序的序号":{经纬度坐标， 巡检点半径}}

      // });
      this.$refs.maps.openDia(row, "巡检点", row.id);
    },
    savePoints(data) {
      // 更新巡检点 -----------------------------
      this.form.pathPoints = JSON.stringify(data);
      updateWms_inspection_rule(this.form).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.open = false;
        this.getList();
      });
    },
  },
};
</script>
