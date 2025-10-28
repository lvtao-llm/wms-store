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
      <el-table-column label="电量" align="center" prop="cardPower">
        <template slot-scope="scope">
          <span v-if="scope.row.cardPower">{{ scope.row.cardPower }}%</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="使用状态" align="center" prop="useStatus">
        <template slot-scope="scope">
          <span>{{ scope.row.useStatus === '-1' ? '使用' : '未使用' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="cardEnable">
        <template slot-scope="scope">
          <span>{{ scope.row.cardEnable === 'Y' ? '启用' : '未启用' }}</span>
        </template>
      </el-table-column>
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
        <el-form-item label="手机号" prop="cardModel">
          <el-input v-model="form.cardModel" placeholder="请输入定位卡手机号"/>
        </el-form-item>
        <el-form-item label="启用状态" prop="cardEnable">
          <el-select v-model="form.cardEnable" placeholder="请选择启用状态">
            <el-option label="启用" value="Y"/>
            <el-option label="未启用" value="N"/>
          </el-select>
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
  deviceCardListPage,
  deviceCardAddCard,
  deviceCardUpdateCard,
  deviceCardDelCard
} from "@/api/lanya_transfer"

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
      deviceCardListPage(this.queryParams).then(response => {
        this.lanya_device_cardList = response.data
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
      this.form = row
      this.open = true
      this.title = "修改定位卡管理"
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.cardModel = "GT01"
          this.form.cardType = "card"
          if (this.form.id != null) {
            deviceCardUpdateCard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            deviceCardAddCard(this.form).then(response => {
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
        return deviceCardDelCard({cardIds: [row.cardId]})
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
    },

  }
}
</script>
