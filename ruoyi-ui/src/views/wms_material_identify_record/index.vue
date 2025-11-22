<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年月日" prop="ymd">
        <el-input
          v-model="queryParams.ymd"
          placeholder="请输入年月日"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时分秒" prop="hms">
        <el-input
          v-model="queryParams.hms"
          placeholder="请输入时分秒"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物资编码" prop="wzbm">
        <el-input
          v-model="queryParams.wzbm"
          placeholder="请输入物资编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="识别结果" prop="wlsbjg">
        <el-input
          v-model="queryParams.wlsbjg"
          placeholder="请输入物料识别结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="cph">
        <el-input
          v-model="queryParams.cph"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车轴数" prop="czs">
        <el-input
          v-model="queryParams.czs"
          placeholder="请输入车轴数"
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
          v-hasPermi="['system:wms_material_identify_record:add']"
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
          v-hasPermi="['system:wms_material_identify_record:edit']"
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
          v-hasPermi="['system:wms_material_identify_record:remove']"
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
          v-hasPermi="['system:wms_material_identify_record:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_material_identify_recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="年月日" align="center" prop="ymd"/>
      <el-table-column label="物资编码" align="center" prop="wzbm"/>
      <el-table-column label="物料识别结果" align="center" prop="wlsbjg"/>
      <el-table-column label="车牌号" align="center" prop="cph"/>
      <el-table-column label="车轴数" align="center" prop="czs"/>
      <el-table-column label="图片1" align="center" prop="img1">
        <template slot-scope="scope">
          <div v-for="(img, index) in scope.row.img1" :key="index">
            <image-preview
              :src="img"
              :width="50"
              :height="50"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="图片2" align="center" prop="img2">
        <template slot-scope="scope">
          <div v-for="(img, index) in scope.row.img2" :key="index">
            <image-preview
              :src="img"
              :width="50"
              :height="50"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_material_identify_record:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:wms_material_identify_record:remove']"
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

    <!-- 添加或修改物料识别记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="年月日" prop="ymd">
          <el-input v-model="form.ymd" placeholder="请输入年月日"/>
        </el-form-item>
        <el-form-item label="时分秒" prop="hms">
          <el-input v-model="form.hms" placeholder="请输入时分秒"/>
        </el-form-item>
        <el-form-item label="物资编码" prop="wzbm">
          <el-input v-model="form.wzbm" placeholder="请输入物资编码"/>
        </el-form-item>
        <el-form-item label="物料识别结果" prop="wlsbjg">
          <el-input v-model="form.wlsbjg" placeholder="请输入物料识别结果"/>
        </el-form-item>
        <el-form-item label="车牌号" prop="cph">
          <el-input v-model="form.cph" placeholder="请输入车牌号"/>
        </el-form-item>
        <el-form-item label="车轴数" prop="czs">
          <el-input v-model="form.czs" placeholder="请输入车轴数"/>
        </el-form-item>
        <el-form-item label="图片1" prop="img1">
          <el-input v-model="form.img1" placeholder="请输入图片1"/>
        </el-form-item>
        <el-form-item label="图片2" prop="mg2">
          <el-input v-model="form.mg2" placeholder="请输入图片2"/>
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
  listWms_material_identify_record,
  getWms_material_identify_record,
  delWms_material_identify_record,
  addWms_material_identify_record,
  updateWms_material_identify_record
} from "@/api/system/wms_material_identify_record"
import path from 'path-browserify'

export default {
  name: "Wms_material_identify_record",
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
      // 物料识别记录表格数据
      wms_material_identify_recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ymd: null,
        hms: null,
        wzbm: null,
        wlsbjg: null,
        cph: null,
        czs: null,
        img1: null,
        mg2: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物料识别记录列表 */
    getList() {
      this.loading = true
      listWms_material_identify_record(this.queryParams).then(response => {
        this.wms_material_identify_recordList = response.rows.map(item => {
          return {
            ...item,
            wlsbjg: item.wlsbjg ? item.wlsbjg.split(' ').slice(0, 2).join(' ') : item.wlsbjg,
            img1: item.img1 ? item.img1.split(",").map(img => path.join("/profile/", item.imagePath, img)) : [],
            img2: item.mg2 ? item.mg2.split(",").map(img => path.join("/profile/", item.imagePath, img)) : []
          }
        })
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
        ymd: null,
        hms: null,
        wzbm: null,
        wlsbjg: null,
        cph: null,
        czs: null,
        img1: null,
        mg2: null
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
      this.ids = selection.map(item => item.ymd)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物料识别记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const ymd = row.ymd || this.ids
      getWms_material_identify_record(ymd).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物料识别记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.ymd != null) {
            updateWms_material_identify_record(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_material_identify_record(this.form).then(response => {
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
      const ymds = row.ymd || this.ids
      this.$modal.confirm('是否确认删除物料识别记录编号为"' + ymds + '"的数据项？').then(function () {
        return delWms_material_identify_record(ymds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_material_identify_record/export', {
        ...this.queryParams
      }, `wms_material_identify_record_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
