<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
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
          v-hasPermi="['system:lanya_device_machine:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lanya_device_machineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备名称" align="center" prop="deviceName"/>
      <el-table-column label="设备sn编码" align="center" prop="deviceSn"/>
      <el-table-column label="设备版本号" align="center" prop="deviceVersion"/>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="是否启用" align="center" prop="deviceEnable"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="showCardSlot(scope.row)"
            v-hasPermi="['system:lanya_device_card:edit']"
          >卡槽试图
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

    <!-- 绑定卡 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <div class="card-slot-container">
        <div v-for="(slot, index) in form.cardSenderDetailGroupList" :key="index" class="slot-group">
          <div class="slot-header">
            <span class="slot-number">{{ index + 1 }}</span>
            <span class="slot-id">{{ slot.cardId }}</span>
          </div>
          <div class="slot-content">
            <div v-for="(card, cardIndex) in slot.cardSenderDetails" :key="cardIndex" class="card-item">
              <div class="card-index">{{ cardIndex + 1 }}</div>
              <div class="card-status">
                <div v-if="card.cardId" class="status-bar full">
                  <div class="status-progress" :style="{ width: '100%' }"></div>
                  <span class="status-text">{{ card.electricity }}</span>
                </div>
                <div v-else-if="card.cardId === undefined" class="status-bar empty">
                  <div class="status-progress" :style="{ width: '0%' }"></div>
                  <span class="status-text">0%</span>
                </div>
                <div v-else class="status-bar partial">
                  <div class="status-progress" :style="{ width: card.progress + '%' }"></div>
                  <span class="status-text">{{ card.progress }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listLanya_device_machine,
  getLanya_device_machine,
  delLanya_device_machine,
  addLanya_device_machine,
  updateLanya_device_machine
} from "@/api/system/lanya_device_machine"
import {machine_detail_page} from '@/api/lanya_transfer'

export default {
  name: "Lanya_device_machine",
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
      // 发卡机表格数据
      lanya_device_machineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceSn: null,
        deviceVersion: null,
        deviceName: null,
        deviceBtnRent: null,
        deviceHeartTime: null,
        deviceConfig: null,
        longitude: null,
        latitude: null,
        layerId: null,
        layerHeight: null,
        userName: null,
        password: null,
        faceUrl: null,
        faceFlag: null,
        faceMacAddr: null,
        faceSn: null,
        faceHeartTime: null,
        deviceEnable: null,
        cardModel: null,
        plannedNumberCards: null,
        pushFace: null,
        whiteListCount: null,
        viewSort: null,
        viewRows: null,
        viewColumns: null,
        viewData: null,
        cardSenderDetailGroupList: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceSn: [
          {required: true, message: "设备sn编码不能为空", trigger: "blur"}
        ],
        deviceName: [
          {required: true, message: "设备名称不能为空", trigger: "blur"}
        ],
        faceFlag: [
          {required: true, message: "人脸相机标识不能为空", trigger: "blur"}
        ],
        deviceEnable: [
          {required: true, message: "发卡机：是否启用 Y：启用 N：禁用不能为空", trigger: "blur"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询发卡机列表 */
    getList() {
      this.loading = true
      listLanya_device_machine(this.queryParams).then(response => {
        this.lanya_device_machineList = response.rows
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
        cardSenderId: null,
        deviceSn: null,
        deviceVersion: null,
        deviceName: null,
        deviceBtnRent: null,
        deviceHeartTime: null,
        deviceConfig: null,
        longitude: null,
        latitude: null,
        layerId: null,
        layerHeight: null,
        userName: null,
        password: null,
        faceUrl: null,
        faceFlag: null,
        faceMacAddr: null,
        faceSn: null,
        faceHeartTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null,
        deviceEnable: null,
        cardModel: null,
        plannedNumberCards: null,
        pushFace: null,
        whiteListCount: null,
        viewSort: null,
        viewRows: null,
        viewColumns: null,
        viewData: null,
        cardSenderDetailGroupList: []
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
      this.ids = selection.map(item => item.cardSenderId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加发卡机"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const cardSenderId = row.cardSenderId || this.ids
      getLanya_device_machine(cardSenderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改发卡机"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cardSenderId != null) {
            updateLanya_device_machine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLanya_device_machine(this.form).then(response => {
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
      const cardSenderIds = row.cardSenderId || this.ids
      this.$modal.confirm('是否确认删除发卡机编号为"' + cardSenderIds + '"的数据项？').then(function () {
        return delLanya_device_machine(cardSenderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lanya_device_machine/export', {
        ...this.queryParams
      }, `lanya_device_machine_${new Date().getTime()}.xlsx`)
    },
    showCardSlot(row) {
      this.reset()
      machine_detail_page({'deviceSn': row.deviceSn}).then(response => {
        this.form = response.data
        this.title = '卡槽试图 '
        this.open = true
      })
    }
  }
}
</script>
<style scoped>
.card-slot-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  padding: 10px;
}

.slot-group {
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f5f5f5;
  padding: 10px;
}

.slot-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.slot-number {
  font-weight: bold;
  color: #333;
}

.slot-id {
  color: #666;
}

.slot-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.card-item {
  display: flex;
  align-items: center;
  padding: 5px 0;
  border-bottom: 1px solid #eee;
}

.card-index {
  width: 20px;
  text-align: center;
  color: #666;
  font-size: 12px;
}

.card-status {
  flex: 1;
  position: relative;
}

.status-bar {
  height: 8px;
  background-color: #eee;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.status-progress {
  height: 100%;
  background-color: #4CAF50;
  transition: width 0.3s ease;
}

.status-text {
  position: absolute;
  top: -15px;
  right: 0;
  color: #4CAF50;
  font-size: 12px;
}

.full .status-progress {
  background-color: #4CAF50;
}

.empty .status-progress {
  background-color: #f44336;
}

.partial .status-progress {
  background-color: #2196F3;
}

</style>
