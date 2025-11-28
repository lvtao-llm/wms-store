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
      <el-form-item label="区域名称" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入区域名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域功能" prop="areaFunction">
        <el-input
          v-model="queryParams.areaFunction"
          placeholder="请输入区域功能"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="enabled">
        <el-input
          v-model="queryParams.enabled"
          placeholder="请输入启用状态"
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
          v-hasPermi="['system:area:add']"
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
          v-hasPermi="['system:area:edit']"
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
          v-hasPermi="['system:area:remove']"
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
          v-hasPermi="['system:area:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="text"
          plain
          icon="el-icon-download"
          size="mini"
          @click="getList(handleShow)"
          v-hasPermi="['system:area:export']"
        >展示全部
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="areaList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="区域名称" align="center" prop="areaName"/>
      <el-table-column label="区域类型" align="center" prop="areaType"/>
      <el-table-column label="风险等级" align="center" prop="areaRiskLevel"/>
      <el-table-column label="启用状态" align="center" prop="enabled"/>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="照片" align="center" prop="photos">
        <template slot-scope="scope">
          <div v-if="scope.row.photos">
            <image-preview
              v-for="(photo, index) in scope.row.photos.split(',')"
              :key="index"
              :src="photo.trim()"
              :width="50"
              :height="50"
              style="margin-right: 5px; margin-bottom: 5px"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="全景" align="center" prop="photo360">
        <template slot-scope="scope">
          <div
            @click="handlePanorama(scope.row.photo360)"
            v-if="scope.row.photo360"
          >
            <img
              :src="baseUrl + scope.row.photo360"
              :width="50"
              :height="50"
              style="margin-right: 5px; margin-bottom: 5px"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="摄像头" align="center" prop="cameras">
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:area:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-no-smoking"
            @click="handleAreaPolygon(scope.row)"
            v-hasPermi="['system:area:edit']"
          >绘制
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:area:remove']"
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

    <!-- 添加或修改区域对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区域名称"/>
        </el-form-item>
        <el-form-item label="区域类型" prop="areaFunction">
          <el-select v-model="form.areaType" placeholder="请选择区域类型">
            <el-option label="办公区域" value="办公区域"/>
            <el-option label="高危区域" value="高危区域"/>
            <el-option label="料场区域" value="料场区域"/>
          </el-select>
        </el-form-item>
        <el-form-item label="安全提示" prop="areaSafetyNotice">
          <el-input
            v-model="form.areaSafetyNotice"
            placeholder="请输入安全提示"
          />
        </el-form-item>
        <el-form-item label="风险等级" prop="areaRiskLevel">
          <el-select v-model="form.areaRiskLevel" placeholder="请选择区域类型">
            <el-option label="低" value="低"/>
            <el-option label="中" value="中"/>
            <el-option label="高" value="高"/>
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态" prop="enabled">
          <el-select v-model="form.enabled" placeholder="请选择启用状态">
            <el-option label="启用" value="启用"/>
            <el-option label="禁用" value="禁用"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="区域照片" prop="photos">
          <image-upload v-model="form.photos"/>
        </el-form-item>
        <el-form-item label="全景照片" prop="photo360">
          <image-upload :fileSize="30" v-model="form.photo360" :limit="1"/>
        </el-form-item>
        <el-form-item label="摄像头" prop="cameras">
          <el-select
            v-model="form.cameras"
            multiple
            placeholder="请选择摄像头"
          >
            <el-option
              v-for="dict in devices"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <mapDetail ref="maps"></mapDetail>
    <panorama ref="panorama"></panorama>
  </div>
</template>

<script>
import panorama from "@/components/Panorama/index";
import mapDetail from "./childView/detail3d.vue";
import {
  listArea,
  getArea,
  delArea,
  addArea,
  updateArea,
} from "@/api/system/wms_area";
import {
  listWms_device,
} from "@/api/system/wms_device";
import item from "@/layout/components/Sidebar/Item.vue";

export default {
  name: "Area",
  components: {
    mapDetail,
    panorama,
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
      // 区域表格数据
      areaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        areaCode: null,
        areaName: null,
        areaType: null,
        areaFunction: null,
        areaSafetyNotice: null,
        areaPolygon: null,
        areaRiskLevel: null,
        enabled: null,
        photos: null,
        photo360: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        areaName: [
          {required: true, message: "区域名称不能为空", trigger: "blur"},
        ],
        areaType: [
          {required: true, message: "区域类型不能为空", trigger: "blur"},
        ],
      },
      baseUrl: process.env.VUE_APP_BASE_API,
      devices: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询区域列表 */
    getList(callback) {
      this.loading = true;
      listWms_device({}).then((response) => {
        this.devices = response.rows.map((item) => {
          return {label: item.deviceName, value: item.deviceName}
        });
        listArea(this.queryParams).then((response) => {
          this.areaList = response.rows.map(row => {
            return {
              ...row,
            }
          });
          console.log(this.areaList, this.devices)
          this.total = response.total;
          this.loading = false;
          if (callback) {
            callback(response.rows.map((row) => row.areaPolygon));
          }
        });
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        areaId: null,
        areaCode: null,
        areaName: null,
        areaType: "办公区域",
        areaFunction: null,
        areaSafetyNotice: null,
        areaPolygon: null,
        areaRiskLevel: "低",
        enabled: "启用",
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
        photos: null,
        photo360: null,
      };
      this.resetForm("form");
    },
    handlePanorama(src) {
      this.$refs.panorama.openDia(src);
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
      this.ids = selection.map((item) => item.areaId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加区域";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const areaId = row.areaId || this.ids;
      getArea(areaId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改区域";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.cameras = this.form.cameras.join(",");
          if (this.form.areaId != null) {
            updateArea(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addArea(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleAreaPolygon(row) {
      //TODO 弹出层中绘制多边形及颜色
      this.$refs.maps.openDia(row, "风险区域", row.areaId);
    },
    handleAreaPolygonAll(row) {
      //TODO 弹出层中绘制多边形及颜色
      this.$refs.maps.openDiaAll(row);
    },
    handleShow(areaPolygons) {
      const arr = areaPolygons;
      const result = [];
      arr.forEach((i) => {
        if (i) {
          result.push(JSON.parse(i)[0]);
        }
      });
      this.handleAreaPolygonAll(result);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const areaIds = row.areaId || this.ids;
      this.$modal
        .confirm('是否确认删除区域编号为"' + areaIds + '"的数据项？')
        .then(function () {
          return delArea(areaIds);
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
        "system/area/export",
        {
          ...this.queryParams,
        },
        `area_${new Date().getTime()}.xlsx`
      );
    }
  },
};
</script>
