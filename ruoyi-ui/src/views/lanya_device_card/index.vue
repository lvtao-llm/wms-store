<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="卡号" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入手机号"
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
          v-hasPermi="['system:lanya_device_card:add']"
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
          v-hasPermi="['system:lanya_device_card:edit']"
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
          v-hasPermi="['system:lanya_device_card:remove']"
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
          v-hasPermi="['system:lanya_device_card:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_device_cardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="卡号" align="center" prop="cardId"/>
      <el-table-column label="手机号" align="center"/>
      <el-table-column label="电量" align="center" prop="cardPower"/>
      <el-table-column label="使用状态" align="center" prop="useStatus"/>
      <el-table-column label="启用状态" align="center" prop="cardStatus"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lanya_device_card:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:lanya_device_card:remove']"
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

    <!-- 添加或修改定位卡管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="卡号" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡号"/>
        </el-form-item>
        <el-form-item label="定位卡型号" prop="cardModel">
          <el-input v-model="form.cardModel" placeholder="请输入定位卡型号"/>
        </el-form-item>
        <el-form-item label="IC卡号" prop="icCardId">
          <el-input v-model="form.icCardId" placeholder="请输入IC卡号"/>
        </el-form-item>
        <el-form-item label="电量百分比" prop="cardPower">
          <el-input v-model="form.cardPower" placeholder="请输入电量百分比"/>
        </el-form-item>
        <el-form-item label="版本" prop="cardVersion">
          <el-input v-model="form.cardVersion" placeholder="请输入版本"/>
        </el-form-item>
        <el-form-item label="频点" prop="cardFreq">
          <el-input v-model="form.cardFreq" placeholder="请输入频点"/>
        </el-form-item>
        <el-form-item label="更新状态时间" prop="statusTime">
          <el-date-picker clearable
                          v-model="form.statusTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择更新状态时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="" prop="cardEnable">
          <el-input v-model="form.cardEnable" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="卡片传输状态" prop="cardTransfer">
          <el-input v-model="form.cardTransfer" placeholder="请输入卡片传输状态"/>
        </el-form-item>
        <el-form-item label="心跳时间" prop="heartTime">
          <el-date-picker clearable
                          v-model="form.heartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择心跳时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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
  listLanya_device_card,
  getLanya_device_card,
  delLanya_device_card,
  addLanya_device_card,
  updateLanya_device_card
} from "@/api/system/lanya_device_card"

export default {
  name: "Lanya_device_card",
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
      // 定位卡管理表格数据
      lanya_device_cardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        cardType: null,
        cardModel: null,
        icCardId: null,
        cardPower: null,
        cardStatus: null,
        cardVersion: null,
        cardFreq: null,
        statusTime: null,
        cardEnable: null,
        useStatus: null,
        cardTransfer: null,
        heartTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardId: [
          {required: true, message: "卡号不能为空", trigger: "blur"}
        ],
        cardStatus: [
          {required: true, message: "卡状态不能为空", trigger: "change"}
        ],
        cardEnable: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        useStatus: [
          {required: true, message: "是否使用不能为空", trigger: "change"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询定位卡管理列表 */
    getList() {
      this.loading = true
      listLanya_device_card(this.queryParams).then(response => {
        this.lanya_device_cardList = response.rows
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
        id: null,
        cardId: null,
        cardType: null,
        cardModel: null,
        icCardId: null,
        cardPower: null,
        cardStatus: null,
        cardVersion: null,
        cardFreq: null,
        statusTime: null,
        cardEnable: null,
        useStatus: null,
        cardTransfer: null,
        heartTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null
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
      this.open = true
      this.title = "添加定位卡管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLanya_device_card(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改定位卡管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLanya_device_card(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_device_card(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除定位卡管理编号为"' + ids + '"的数据项？').then(function () {
        return delLanya_device_card(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_device_card/export', {
        ...this.queryParams
      }, `lanya_device_card_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
