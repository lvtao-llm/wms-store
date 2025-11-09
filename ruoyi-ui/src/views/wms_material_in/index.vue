<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="接料编号" prop="jlBh">
        <el-input
          v-model="queryParams.jlBh"
          placeholder="请输入接料编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库房" prop="kf">
        <el-input
          v-model="queryParams.kf"
          placeholder="请输入库房"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车号" prop="ch">
        <el-input
          v-model="queryParams.ch"
          placeholder="请输入车号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="到货数量" prop="dhsl">
        <el-input
          v-model="queryParams.dhsl"
          placeholder="请输入到货数量"
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
      <el-form-item label="业务年月" prop="yynY">
        <el-input
          v-model="queryParams.yynY"
          placeholder="请输入业务年月"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物资名称" prop="wzmc">
        <el-input
          v-model="queryParams.wzmc"
          placeholder="请输入物资名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物资类别" prop="wzlb">
        <el-input
          v-model="queryParams.wzlb"
          placeholder="请输入物资类别"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_material_in:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_material_inList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="接料编号" align="center" prop="jlBh" />
      <el-table-column label="到货编号" align="center" prop="dhBh" />
      <el-table-column label="货票编号" align="center" prop="hpBh" />
      <el-table-column label="到货类型" align="center" prop="dhLx" />
      <el-table-column label="库房" align="center" prop="kf" />
      <el-table-column label="单据类型" align="center" prop="djLx" />
      <el-table-column label="车号" align="center" prop="ch" />
      <el-table-column label="车数" align="center" prop="cs" />
      <el-table-column label="AS运单号" align="center" prop="asYdh" />
      <el-table-column label="发运数量" align="center" prop="fysl" />
      <el-table-column label="到货数量" align="center" prop="dhsl" />
      <el-table-column label="计量员" align="center" prop="jly" />
      <el-table-column label="计量时间" align="center" prop="jlsj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jlsj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="厂商名称" align="center" prop="csMc" />
      <el-table-column label="物资编码" align="center" prop="wzbm" />
      <el-table-column label="计量类型" align="center" prop="jllx" />
      <el-table-column label="交料人" align="center" prop="jlr" />
      <el-table-column label="交料时间" align="center" prop="jlsjR" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jlsjR, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保管员" align="center" prop="bgy" />
      <el-table-column label="中队" align="center" prop="zd" />
      <el-table-column label="料组" align="center" prop="lz" />
      <el-table-column label="接料时间" align="center" prop="jlsjT" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jlsjT, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="验收人" align="center" prop="ysr" />
      <el-table-column label="验收时间" align="center" prop="yssj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.yssj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="稽核人" align="center" prop="jhr" />
      <el-table-column label="稽核时间" align="center" prop="jhsj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jhsj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到库时间" align="center" prop="dksj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dksj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发运时间" align="center" prop="fysj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.fysj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发运方式" align="center" prop="fyfs" />
      <el-table-column label="发站" align="center" prop="fz" />
      <el-table-column label="到站" align="center" prop="dz" />
      <el-table-column label="业务年月" align="center" prop="yynY" />
      <el-table-column label="状态" align="center" prop="zt" />
      <el-table-column label="卸车编号" align="center" prop="xcbh" />
      <el-table-column label="品种编码" align="center" prop="pzbm" />
      <el-table-column label="物资名称" align="center" prop="wzmc" />
      <el-table-column label="计量单位" align="center" prop="jldw" />
      <el-table-column label="物资类别" align="center" prop="wzlb" />
      <el-table-column label="验收人编码" align="center" prop="ysrBm" />
      <el-table-column label="接料人编码" align="center" prop="jlrBm" />
      <el-table-column label="品种名称" align="center" prop="pzmc" />
      <el-table-column label="业务分公司" align="center" prop="ywfgs" />
      <el-table-column label="装卸队名称" align="center" prop="zxzdMc" />
      <el-table-column label="卸车方式" align="center" prop="xcfs" />
      <el-table-column label="卸车作业起始时间" align="center" prop="xczysjQ" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.xczysjQ, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="卸车作业截止时间" align="center" prop="xczysjZ" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.xczysjZ, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出库时间" align="center" prop="cksj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.cksj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现场员姓名" align="center" prop="xcyxm" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">

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

    <!-- 添加或修改接料视图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="接料编号" prop="jlBh">
          <el-input v-model="form.jlBh" placeholder="请输入接料编号" />
        </el-form-item>
        <el-form-item label="到货编号" prop="dhBh">
          <el-input v-model="form.dhBh" placeholder="请输入到货编号" />
        </el-form-item>
        <el-form-item label="货票编号" prop="hpBh">
          <el-input v-model="form.hpBh" placeholder="请输入货票编号" />
        </el-form-item>
        <el-form-item label="到货类型" prop="dhLx">
          <el-input v-model="form.dhLx" placeholder="请输入到货类型" />
        </el-form-item>
        <el-form-item label="库房" prop="kf">
          <el-input v-model="form.kf" placeholder="请输入库房" />
        </el-form-item>
        <el-form-item label="单据类型" prop="djLx">
          <el-input v-model="form.djLx" placeholder="请输入单据类型" />
        </el-form-item>
        <el-form-item label="车号" prop="ch">
          <el-input v-model="form.ch" placeholder="请输入车号" />
        </el-form-item>
        <el-form-item label="车数" prop="cs">
          <el-input v-model="form.cs" placeholder="请输入车数" />
        </el-form-item>
        <el-form-item label="AS运单号" prop="asYdh">
          <el-input v-model="form.asYdh" placeholder="请输入AS运单号" />
        </el-form-item>
        <el-form-item label="发运数量" prop="fysl">
          <el-input v-model="form.fysl" placeholder="请输入发运数量" />
        </el-form-item>
        <el-form-item label="到货数量" prop="dhsl">
          <el-input v-model="form.dhsl" placeholder="请输入到货数量" />
        </el-form-item>
        <el-form-item label="计量员" prop="jly">
          <el-input v-model="form.jly" placeholder="请输入计量员" />
        </el-form-item>
        <el-form-item label="计量时间" prop="jlsj">
          <el-date-picker clearable
            v-model="form.jlsj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择计量时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="厂商名称" prop="csMc">
          <el-input v-model="form.csMc" placeholder="请输入厂商名称" />
        </el-form-item>
        <el-form-item label="物资编码" prop="wzbm">
          <el-input v-model="form.wzbm" placeholder="请输入物资编码" />
        </el-form-item>
        <el-form-item label="计量类型" prop="jllx">
          <el-input v-model="form.jllx" placeholder="请输入计量类型" />
        </el-form-item>
        <el-form-item label="交料人" prop="jlr">
          <el-input v-model="form.jlr" placeholder="请输入交料人" />
        </el-form-item>
        <el-form-item label="交料时间" prop="jlsjR">
          <el-date-picker clearable
            v-model="form.jlsjR"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择交料时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="保管员" prop="bgy">
          <el-input v-model="form.bgy" placeholder="请输入保管员" />
        </el-form-item>
        <el-form-item label="中队" prop="zd">
          <el-input v-model="form.zd" placeholder="请输入中队" />
        </el-form-item>
        <el-form-item label="料组" prop="lz">
          <el-input v-model="form.lz" placeholder="请输入料组" />
        </el-form-item>
        <el-form-item label="接料时间" prop="jlsjT">
          <el-date-picker clearable
            v-model="form.jlsjT"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择接料时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="验收人" prop="ysr">
          <el-input v-model="form.ysr" placeholder="请输入验收人" />
        </el-form-item>
        <el-form-item label="验收时间" prop="yssj">
          <el-date-picker clearable
            v-model="form.yssj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择验收时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="稽核人" prop="jhr">
          <el-input v-model="form.jhr" placeholder="请输入稽核人" />
        </el-form-item>
        <el-form-item label="稽核时间" prop="jhsj">
          <el-date-picker clearable
            v-model="form.jhsj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择稽核时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="到库时间" prop="dksj">
          <el-date-picker clearable
            v-model="form.dksj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择到库时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发运时间" prop="fysj">
          <el-date-picker clearable
            v-model="form.fysj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发运时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发运方式" prop="fyfs">
          <el-input v-model="form.fyfs" placeholder="请输入发运方式" />
        </el-form-item>
        <el-form-item label="发站" prop="fz">
          <el-input v-model="form.fz" placeholder="请输入发站" />
        </el-form-item>
        <el-form-item label="到站" prop="dz">
          <el-input v-model="form.dz" placeholder="请输入到站" />
        </el-form-item>
        <el-form-item label="业务年月" prop="yynY">
          <el-input v-model="form.yynY" placeholder="请输入业务年月" />
        </el-form-item>
        <el-form-item label="状态" prop="zt">
          <el-input v-model="form.zt" placeholder="请输入状态" />
        </el-form-item>
        <el-form-item label="卸车编号" prop="xcbh">
          <el-input v-model="form.xcbh" placeholder="请输入卸车编号" />
        </el-form-item>
        <el-form-item label="品种编码" prop="pzbm">
          <el-input v-model="form.pzbm" placeholder="请输入品种编码" />
        </el-form-item>
        <el-form-item label="物资名称" prop="wzmc">
          <el-input v-model="form.wzmc" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="计量单位" prop="jldw">
          <el-input v-model="form.jldw" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="物资类别" prop="wzlb">
          <el-input v-model="form.wzlb" placeholder="请输入物资类别" />
        </el-form-item>
        <el-form-item label="验收人编码" prop="ysrBm">
          <el-input v-model="form.ysrBm" placeholder="请输入验收人编码" />
        </el-form-item>
        <el-form-item label="接料人编码" prop="jlrBm">
          <el-input v-model="form.jlrBm" placeholder="请输入接料人编码" />
        </el-form-item>
        <el-form-item label="品种名称" prop="pzmc">
          <el-input v-model="form.pzmc" placeholder="请输入品种名称" />
        </el-form-item>
        <el-form-item label="业务分公司" prop="ywfgs">
          <el-input v-model="form.ywfgs" placeholder="请输入业务分公司" />
        </el-form-item>
        <el-form-item label="装卸队名称" prop="zxzdMc">
          <el-input v-model="form.zxzdMc" placeholder="请输入装卸队名称" />
        </el-form-item>
        <el-form-item label="卸车方式" prop="xcfs">
          <el-input v-model="form.xcfs" placeholder="请输入卸车方式" />
        </el-form-item>
        <el-form-item label="卸车作业起始时间" prop="xczysjQ">
          <el-date-picker clearable
            v-model="form.xczysjQ"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择卸车作业起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="卸车作业截止时间" prop="xczysjZ">
          <el-date-picker clearable
            v-model="form.xczysjZ"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择卸车作业截止时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出库时间" prop="cksj">
          <el-date-picker clearable
            v-model="form.cksj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出库时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="现场员姓名" prop="xcyxm">
          <el-input v-model="form.xcyxm" placeholder="请输入现场员姓名" />
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
import { listWms_material_in, getWms_material_in, delWms_material_in, addWms_material_in, updateWms_material_in } from "@/api/system/wms_material_in"

export default {
  name: "Wms_material_in",
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
      // 接料视图表格数据
      wms_material_inList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jlBh: null,
        dhBh: null,
        hpBh: null,
        dhLx: null,
        kf: null,
        djLx: null,
        ch: null,
        cs: null,
        asYdh: null,
        fysl: null,
        dhsl: null,
        jly: null,
        jlsj: null,
        csMc: null,
        wzbm: null,
        jllx: null,
        jlr: null,
        jlsjR: null,
        bgy: null,
        zd: null,
        lz: null,
        jlsjT: null,
        ysr: null,
        yssj: null,
        jhr: null,
        jhsj: null,
        dksj: null,
        fysj: null,
        fyfs: null,
        fz: null,
        dz: null,
        yynY: null,
        zt: null,
        xcbh: null,
        pzbm: null,
        wzmc: null,
        jldw: null,
        wzlb: null,
        ysrBm: null,
        jlrBm: null,
        pzmc: null,
        ywfgs: null,
        zxzdMc: null,
        xcfs: null,
        xczysjQ: null,
        xczysjZ: null,
        cksj: null,
        xcyxm: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询接料视图列表 */
    getList() {
      this.loading = true
      listWms_material_in(this.queryParams).then(response => {
        this.wms_material_inList = response.rows
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
        jlBh: null,
        dhBh: null,
        hpBh: null,
        dhLx: null,
        kf: null,
        djLx: null,
        ch: null,
        cs: null,
        asYdh: null,
        fysl: null,
        dhsl: null,
        jly: null,
        jlsj: null,
        csMc: null,
        wzbm: null,
        jllx: null,
        jlr: null,
        jlsjR: null,
        bgy: null,
        zd: null,
        lz: null,
        jlsjT: null,
        ysr: null,
        yssj: null,
        jhr: null,
        jhsj: null,
        dksj: null,
        fysj: null,
        fyfs: null,
        fz: null,
        dz: null,
        yynY: null,
        zt: null,
        xcbh: null,
        pzbm: null,
        wzmc: null,
        jldw: null,
        wzlb: null,
        ysrBm: null,
        jlrBm: null,
        pzmc: null,
        ywfgs: null,
        zxzdMc: null,
        xcfs: null,
        xczysjQ: null,
        xczysjZ: null,
        cksj: null,
        xcyxm: null
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
      this.ids = selection.map(item => item.jlBh)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加接料视图"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const jlBh = row.jlBh || this.ids
      getWms_material_in(jlBh).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改接料视图"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.jlBh != null) {
            updateWms_material_in(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_material_in(this.form).then(response => {
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
      const jlBhs = row.jlBh || this.ids
      this.$modal.confirm('是否确认删除接料视图编号为"' + jlBhs + '"的数据项？').then(function() {
        return delWms_material_in(jlBhs)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_material_in/export', {
        ...this.queryParams
      }, `wms_material_in_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
