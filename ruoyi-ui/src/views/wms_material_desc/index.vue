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
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.wzbm"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="wzmc">
        <el-input
          v-model="queryParams.wzmc"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品种名称" prop="pzmc">
        <el-input
          v-model="queryParams.pzmc"
          placeholder="请输入规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物资类别" prop="wzlb">
        <el-input
          v-model="queryParams.wzlb"
          placeholder="请输入型号"
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
          v-hasPermi="['system:wms_material_desc:add']"
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
          v-hasPermi="['system:wms_material_desc:edit']"
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
          v-hasPermi="['system:wms_material_desc:remove']"
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
          v-hasPermi="['system:wms_material_desc:export']"
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
      :data="wms_material_descList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物料编码" align="center" prop="wzbm" />
      <el-table-column label="物料名称" align="center" prop="wzmc" />
      <el-table-column label="规格" align="center" prop="pzmc" />
      <el-table-column label="型号" align="center" prop="wzlb" />
      <el-table-column label="存放区域" align="center" prop="areaCodes">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.wms_area_name"
            :value="scope.row.areaCodes"
          />
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center" prop="materialUnit" />
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
            v-hasPermi="['system:wms_material_desc:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleKeyPoints(scope.row)"
            v-hasPermi="['system:wms_material_desc:edit']"
            >关键点位
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_material_desc:remove']"
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

    <!-- 添加或修改物料描述档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="物料编码" prop="wzbm">
          <el-input v-model="form.wzbm" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="wzmc">
          <el-input v-model="form.wzmc" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="规格" prop="pzmc">
          <el-input v-model="form.pzmc" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="型号" prop="wzlb">
          <el-input v-model="form.wzlb" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="存放区域" prop="areaCodes">
          <el-select
            v-model="form.areaCodes"
            multiple
            placeholder="请选择存放区域"
          >
            <el-option
              v-for="dict in dict.type.wms_area_name"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单位" prop="materialUnit">
          <el-input v-model="form.materialUnit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="存放条件" prop="materialRequirements">
          <el-input
            v-model="form.materialRequirements"
            placeholder="请输入存放条件"
          />
        </el-form-item>
        <el-form-item label="安全注意事项" prop="materialSafetyNotice">
          <el-input
            v-model="form.materialSafetyNotice"
            placeholder="请输入安全注意事项"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <maps @save="savePoints" ref="map"></maps>
  </div>
</template>

<script>
import {
  listWms_material_desc,
  getWms_material_desc,
  delWms_material_desc,
  addWms_material_desc,
  updateWms_material_desc,
} from "@/api/system/wms_material_desc";
import {
  listArea,
  getArea,
  delArea,
  addArea,
  updateArea,
} from "@/api/system/wms_area";
import maps from "./childView/map";

export default {
  name: "Wms_material_desc",
  dicts: ["wms_area_name"],
  components: { maps },
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
      // 物料描述档案表格数据
      wms_material_descList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wzbm: null,
        wzmc: null,
        pzmc: null,
        wzlb: null,
        areaCodes: null,
        materialUnit: null,
        materialRequirements: null,
        materialSafetyNotice: null,
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
    /** 查询物料描述档案列表 */
    getList() {
      this.loading = true;
      listArea(this.queryParams).then((response) => {
        this.dict.type.wms_area_name = response.rows.map((item) => {
          return {
            value: item.areaId + "", // 确保是字符串
            label: item.areaName,
            raw: { ...item, listClass: "primary" },
          };
        });
        listWms_material_desc(this.queryParams).then((response) => {
          this.wms_material_descList = response.rows.map((item) => {
            return {
              ...item,
              areaCodes: item.areaCodes ? item.areaCodes.split(",") : [],
            };
          });
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
        materialDescId: null,
        wzbm: null,
        wzmc: null,
        pzmc: null,
        wzlb: null,
        areaCodes: null,
        materialUnit: null,
        materialRequirements: null,
        materialSafetyNotice: null,
        createBy: null,
        createTime: null,
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
      this.ids = selection.map((item) => item.materialDescId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料描述档案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const materialDescId = row.materialDescId || this.ids;
      getWms_material_desc(materialDescId).then((response) => {
        this.form = response.data;
        this.form.areaCodes = response.data.areaCodes
          ? response.data.areaCodes.split(",")
          : [];
        this.open = true;
        this.title = "修改物料描述档案";
      });
    },
    handleKeyPoints(row) {
      this.reset();
      const materialDescId = row.materialDescId || this.ids;
      getWms_material_desc(materialDescId).then((response) => {
        this.form = response.data;
        // TODO: 添加修改关键点位 @伟兴
        this.title = "修改物料关键点位";
        this.$refs.map.openDia(this.form);
      });
    },
    savePoints(params) {
      updateWms_material_desc(params).then((response) => {
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.areaCodes = this.form.areaCodes
            ? this.form.areaCodes.join(",")
            : "";
          if (this.form.materialDescId != null) {
            updateWms_material_desc(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWms_material_desc(this.form).then((response) => {
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
      const materialDescIds = row.materialDescId || this.ids;
      this.$modal
        .confirm(
          '是否确认删除物料描述档案编号为"' + materialDescIds + '"的数据项？'
        )
        .then(function () {
          return delWms_material_desc(materialDescIds);
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
        "system/wms_material_desc/export",
        {
          ...this.queryParams,
        },
        `wms_material_desc_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
