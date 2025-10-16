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
      <el-form-item label="姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人员类型" prop="realName">
        <el-input
          v-model="queryParams.personType"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="deptId">
        <el-input
          v-model="queryParams.personPhone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idNumber">
        <el-input
          v-model="queryParams.idNumber"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位卡号" prop="personIc">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入定位卡号"
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
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:lanya_device_card_sender_log:export']"
          >导出
        </el-button>
      </el-form-item>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-form>

    <el-table
      v-loading="loading"
      :data="lanya_device_card_sender_logList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="realName" />
      <el-table-column label="人员类型" align="center" prop="personType" />
      <el-table-column label="手机号" align="center" prop="personPhone" />
      <el-table-column
        label="身份证号码"
        align="center"
        prop="idNumber"
        width="180"
      />
      <el-table-column label="定位卡号" align="center" prop="cardId" />
      <el-table-column label="取卡时间" align="center" prop="sendCardTime" />
      <el-table-column label="还卡时间" align="center" prop="returnCardTime" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="100"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_device_card_sender_log:edit']"
            >行动轨迹
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

    <mapDetail ref="maps"></mapDetail>
  </div>
</template>

<script>
import { personInOutRecordPage } from "@/api/lanya_transfer";
import mapDetail from "./childView/map.vue";
export default {
  name: "Lanya_device_card_sender_log",
  components: {
    mapDetail,
  },
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
      // 人脸发卡记录表格数据
      lanya_device_card_sender_logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        deviceSn: null,
        deviceName: null,
        deviceNum: null,
        cardId: null,
        personPhoto: null,
        commandTime: null,
        cardSenderType: null,
        result: null,
        personId: null,
        realName: null,
        deptId: null,
        deptName: null,
        jobNumber: null,
        idNumber: null,
        personIc: null,
        deviceAims: null,
        identifyType: null,
        rentType: null,
        identifyTime: null,
        closedTime: null,
        notifyStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardSenderType: [
          {
            required: true,
            message: "0:还卡 1：发卡不能为空",
            trigger: "change",
          },
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询人脸发卡记录列表 */
    getList() {
      this.loading = true;
      personInOutRecordPage(this.queryParams).then((response) => {
        this.lanya_device_card_sender_logList = response.data;
        this.total = Number(response.total);
        this.loading = false;
      });
    },

    handleUpdate(row) {
      console.log(this.$refs.maps, 123);
      this.$refs.maps.handleSelectChange(row);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/lanya_device_card_sender_log/export",
        {
          ...this.queryParams,
        },
        `lanya_device_card_sender_log_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
