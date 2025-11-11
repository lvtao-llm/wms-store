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
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
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
        >搜索
        </el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button
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
          v-hasPermi="['system:wms_device:add']"
        >新增
        </el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:wms_device:edit']"
        >修改
        </el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:wms_device:remove']"
        >删除
        </el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_device:export']"
        >导出
        </el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="wms_deviceList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备名称" align="center" prop="deviceName"/>
      <el-table-column label="序号SN" align="center" prop="serialNumber"/>
      <el-table-column
        label="设备描述"
        align="center"
        prop="deviceDescription"
      />
      <el-table-column label="经度" align="center" prop="longitude"/>
      <el-table-column label="纬度" align="center" prop="latitude"/>
      <el-table-column label="高度" align="center" prop="altitude"/>
      <el-table-column label="型号" align="center" prop="model"/>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="viewCameraStream(scope.row)"
            v-hasPermi="['system:wms_device:view']"
          >查看画面
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_device:edit']"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlePosition(scope.row)"
            v-hasPermi="['system:wms_device:edit']"
          >位置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_device:remove']"
          >删除
          </el-button
          >
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

    <!-- 添加或修改设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="设备描述" prop="deviceDescription">
          <el-input
            v-model="form.deviceDescription"
            placeholder="请输入设备描述"
          />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号"/>
        </el-form-item>
        <el-form-item label="序号SN" prop="serialNumber">
          <el-input v-model="form.serialNumber" placeholder="请输入序号SN"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 摄像头画面 -->
    <camera-view ref="cameraView" :device-id="currentDeviceId"></camera-view>

    <maps @editPoints="savePoints" ref="maps"></maps>
  </div>
</template>

<script>
import {
  listWms_device,
  getWms_device,
  delWms_device,
  addWms_device,
  updateWms_device,
} from "@/api/system/wms_device";
import maps from "./childView/map.vue";
import CameraView from './cameraView.vue';

export default {
  name: "Wms_device",
  dicts: ["wms_device_type", "wms_deleted"],
  components: {
    maps,
    CameraView
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
      // 设备表格数据
      wms_deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceType: "摄像头",
        deviceDescription: null,
        longitude: null,
        latitude: null,
        altitude: null,
        deviceName: null,
        model: null,
        specification: null,
        serialNumber: null,
        tableName: null,
        tableId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listWms_device(this.queryParams).then((response) => {
        this.wms_deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        deviceType: "摄像头",
        deviceDescription: null,
        longitude: null,
        latitude: null,
        altitude: null,
        deviceName: null,
        model: null,
        specification: null,
        serialNumber: null,
        tableName: null,
        tableId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
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
      this.title = "添加设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWms_device(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateWms_device(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWms_device(this.form).then((response) => {
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
        .confirm('是否确认删除设备编号为"' + ids + '"的数据项？')
        .then(function () {
          return delWms_device(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/wms_device/export",
        {
          ...this.queryParams,
        },
        `wms_device_${new Date().getTime()}.xlsx`
      );
    },
    handlePosition(row) {
      this.reset();
      const id = row.id || this.ids;
      getWms_device(id).then((response) => {
        this.form = response.data;
        this.$refs.maps.openDia(row);
      });
    },
    savePoints(row) {
      this.form.altitude = row.altitude;
      this.form.latitude = String(row.points[0].lat);
      this.form.longitude = String(row.points[0].lng);
      updateWms_device(this.form).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
    },
    // 添加查看摄像头画面的方法
    viewCameraStream(row) {
      this.currentDeviceId = row.id;

      // 构造摄像头连接参数（实际应从数据库或API获取）
      const cameraInfo = {
        ip: '192.168.1.64', // 摄像头IP地址
        port: '554',        // RTSP端口
        username: 'admin',  // 用户名
        password: '12345',  // 密码
        channel: '101'      // 通道号
      };

      // 打开摄像头画面弹窗
      this.$refs.cameraView.openCamera(cameraInfo);
    },
  },
};
</script>
