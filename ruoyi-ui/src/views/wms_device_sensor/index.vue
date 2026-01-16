<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="98px"
    >
      <el-form-item label="传感器名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入传感器名称"
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
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置
        </el-button>
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
          v-hasPermi="['system:wms_device:edit']"
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
          v-hasPermi="['system:wms_device:remove']"
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
          v-hasPermi="['system:wms_device:export']"
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
      :data="wms_deviceList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="传感器名称" align="center" prop="deviceName" />
      <el-table-column label="传感器类型" align="center" prop="sensorType">
        <template slot-scope="{ row }">
          {{
            row.sensorType == 1
              ? "龙门吊"
              : row.sensorType == 2
              ? "厂房"
              : "地秤"
          }}
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="传感器描述"
        align="center"
        prop="deviceDescription"
      /> -->
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="380"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_device:edit']"
            >修改
          </el-button>
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

    <!-- 添加或修改车牌机对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="传感器名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入传感器名称" />
        </el-form-item>
        <el-form-item label="传感器类型" prop="sensorType">
          <el-select style="width: 100%" v-model="form.sensorType">
            <el-option label="龙门吊" value="1"></el-option>
            <el-option label="厂房" value="2"></el-option>
            <el-option label="地秤" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="传感器描述" prop="remark">
          <editor v-model="form.remark" :min-height="192" />
        </el-form-item>

        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入传感器经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入传感器纬度" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

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
import maps from "../wms_area/childView/detail3d";

export default {
  name: "Wms_device",
  dicts: ["wms_device_type", "wms_deleted"],
  components: {
    maps,
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
      // 车牌机表格数据
      wms_deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceType: "传感器",
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
      // 传感器信息
      info: {
        ip1: null,
        port1: null,
        username1: null,
        password1: null,
        channel1: null,
        ip2: null,
        port2: null,
        username2: null,
        password2: null,
        channel2: null,
      },
      dialogName: "",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车牌机列表 */
    getList() {
      this.loading = true;
      listWms_device(this.queryParams).then((response) => {
        this.wms_deviceList = response.rows.map((item) => {
          let info = item.data ? JSON.parse(item.data) : {};
          return {
            ...item,
            sensorType: info.sensorType,
            remark: info.remark,
          };
        });
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
        deviceType: "传感器",
        longitude: null,
        latitude: null,
        altitude: null,
        deviceName: null,
      };
      this.info = {};
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
      this.title = "添加传感器";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getWms_device(id).then((response) => {
        this.form = response.data;
        this.info = response.data.data ? JSON.parse(response.data.data) : {};
        this.form.sensorType = this.info.sensorType;
        this.form.remark = decodeURIComponent(this.info.remark);
        this.open = true;
        this.title = "修改传感器";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          console.warn("this.form")
          this.form.data = JSON.stringify({
            sensorType: this.form.sensorType,
            remark: encodeURIComponent(this.form.remark),
          });
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
        .confirm("是否确认删除？")
        .then(function () {
          return delWms_device(ids);
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
        "system/wms_device/export",
        {
          ...this.queryParams,
        },
        `wms_device_${new Date().getTime()}.xlsx`
      );
    },
    handlePosition(row) {
      // this.reset();
      // const id = row.id || this.ids;
      // getWms_device(id).then((response) => {
      //   this.form = response.data;
      //   this.$refs.maps.openDia(row);
      // });
      this.$refs.maps.openDia(row, "传感器", row.id);
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
    // 添加查看传感器画面的方法
    viewCameraStream(row, index) {
      // 打开传感器画面弹窗
      this.dialogName = row.deviceName;
      this.$refs.cameraView.openCamera(row, JSON.parse(row.data));
    },
  },
};
</script>
