<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="报警类型" prop="alarmRuleType">
        <el-select v-model="queryParams.alarmRuleType" placeholder="请选择报警类型" clearable>
          <el-option
            v-for="dict in dict.type.wms_alarm_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['system:wms_alarm_rule:add']"-->
      <!--        >新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:wms_alarm_rule:edit']"
        >修改
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['system:wms_alarm_rule:remove']"-->
      <!--        >删除-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:wms_alarm_rule:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wms_alarm_ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="规则名" align="center" prop="alarmRuleName"/>-->
      <el-table-column label="报警类型" align="center" prop="alarmRuleType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_alarm_type" :value="scope.row.alarmRuleType"/>
        </template>
      </el-table-column>
      <el-table-column label="周期开始时间" align="center" prop="alarmRuleEffectivePeriodS"/>
      <el-table-column label="周期结束时间" align="center" prop="alarmRuleEffectivePeriodE"/>
      <el-table-column label="时间阈值" align="center" prop="alarmRuleTimeThreshold"/>
      <el-table-column label="距离阈值" align="center" prop="alarmRuleDistThreshold"/>
      <el-table-column label="目标区域" align="center" prop="alarmRuleTargetAreaName">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_area_names" :value="scope.row.alarmRuleTargetAreaName"/>
        </template>
      </el-table-column>

      <!--      <el-table-column label="是否启用" align="center" prop="alarmRuleEnabled">-->
      <!--        <template slot-scope="scope">-->
      <!--          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.alarmRuleEnabled"/>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="授权人员" align="center" prop="alarmRuleRelatedPeople">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_internal_people" :value="scope.row.alarmRuleRelatedPeople"/>
        </template>
      </el-table-column>
      <el-table-column label="授权岗位" align="center" prop="alarmRuleRelatedDept">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_internal_post" :value="scope.row.alarmRuleRelatedDept"/>
        </template>
      </el-table-column>
      <el-table-column label="通知方式" align="center" prop="alarmRuleNoticeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_notice_type" :value="scope.row.alarmRuleNoticeType"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:wms_alarm_rule:edit']"
          >修改
          </el-button>
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-delete"-->
          <!--            @click="handleDelete(scope.row)"-->
          <!--            v-hasPermi="['system:wms_alarm_rule:remove']"-->
          <!--          >删除-->
          <!--          </el-button>-->
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

    <!-- 添加或修改报警信息规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="报警类型" prop="alarmRuleType">
          <el-select v-model="form.alarmRuleType" placeholder="请选择报警类型" disabled="disabled" style="width: 100%">
            <el-option
              v-for="dict in dict.type.wms_alarm_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="周期开始时间" prop="alarmRuleEffectivePeriodS">
          <el-time-picker
            v-model="form.alarmRuleEffectivePeriodS"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择生效周期开始时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="周期结束时间" prop="alarmRuleEffectivePeriodE">
          <el-time-picker
            v-model="form.alarmRuleEffectivePeriodE"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择生效周期结束时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="时间阈值" prop="alarmRuleTimeThreshold">
          <el-input v-model="form.alarmRuleTimeThreshold" placeholder="请输入时间阈值" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="距离阈值" prop="alarmRuleDistThreshold">
          <el-input v-model="form.alarmRuleDistThreshold" placeholder="请输入距离阈值" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="目标区域" prop="alarmRuleTargetAreaCode">
          <el-select v-model="form.alarmRuleTargetAreaCode"
                     multiple
                     placeholder="请选择目标区域">
            <el-option
              v-for="dict in dict.type.wms_area_names"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>


        <!--        <el-form-item label="是否启用" prop="alarmRuleEnabled">-->
        <!--          <el-select v-model="form.alarmRuleEnabled" placeholder="请选择是否启用">-->
        <!--            <el-option-->
        <!--              v-for="dict in dict.type.sys_normal_disable"-->
        <!--              :key="dict.value"-->
        <!--              :label="dict.label"-->
        <!--              :value="dict.value"-->
        <!--            ></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="关联人员" prop="alarmRuleRelatedPeople">
          <el-select
            multiple
            v-model="form.alarmRuleRelatedPeople"
            placeholder="请选择关联人员">
            <el-option
              v-for="dict in dict.type.wms_internal_people"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关联岗位" prop="alarmRuleRelatedDept">
          <el-select multiple v-model="form.alarmRuleRelatedDept" placeholder="请选择关联岗位">
            <el-option
              v-for="dict in dict.type.sys_post"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="短信通知人" prop="alarmRuleNoticeType">
          <el-select
            multiple
            v-model="form.smsNoticeUsers"
            placeholder="请选择短信通知人">
            <el-option
              v-for="dict in dict.type.sys_users"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="即时通通知人" prop="alarmRuleNoticeType">
          <el-select
            multiple
            v-model="form.imNoticeUsers"
            placeholder="请选择即时通通知人">
            <el-option
              v-for="dict in dict.type.sys_users"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="系统通知人" prop="alarmRuleNoticeType">
          <el-select
            multiple
            v-model="form.sysNoticeUsers"
            placeholder="请选择系统通知人">
            <el-option
              v-for="dict in dict.type.sys_users"
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
  </div>
</template>

<script>
import {
  listWms_alarm_rule,
  getWms_alarm_rule,
  delWms_alarm_rule,
  addWms_alarm_rule,
  updateWms_alarm_rule
} from "@/api/system/wms_alarm_rule"
import {listArea} from "@/api/system/wms_area"
import {listUser} from "@/api/system/user"
import {listPost} from "@/api/system/post"

import {listPersonStaffPage} from "@/api/lanya_transfer"

export default {
  name: "Wms_alarm_rule",
  dicts: ['wms_alarm_type', 'wms_area_names', 'wms_internal_people', 'wms_notice_type', 'sys_normal_disable', 'wms_internal_post'],
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
      // 报警信息规则表格数据
      wms_alarm_ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmRuleId: null,
        alarmRuleName: null,
        alarmRuleTimeThreshold: null,
        alarmRuleDistThreshold: null,
        alarmRuleTargetAreaCode: null,
        alarmRuleType: null,
        alarmRuleEffectivePeriodS: null,
        alarmRuleEffectivePeriodE: null,
        alarmRuleEnabled: null,
        alarmRuleRelatedPeople: null,
        alarmRuleRelatedDept: null,
        alarmRuleNoticeType: null,
        alarmType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      areas: [],
    }
  },
  created() {
    this.queryParams.alarmType = this.$route.params.type;
    this.getList()
  },
  methods: {
    /** 查询报警信息规则列表 */
    getList() {

      // 获取内部人员列表
      listPersonStaffPage({"pageNum": 1, "pageSize": 1000}).then(response => {
        console.log(response)
        this.dict.type.wms_internal_people = response.data.map(item => ({
          label: item.realName,
          value: item.realName,
          raw: {...item, listClass: 'primary'}
        }));

        // 获取区域列表
        listArea({"pageNum": 2, "pageSize": 1000}).then(response => {
          this.areas = response.rows
          this.dict.type.wms_area_names = response.rows.map(item => ({
            label: item.areaName,
            value: item.areaId,
            raw: {...item, listClass: 'primary'}
          }));

          // 获取用户列表
          listUser({pageNum: 1, pageSize: 1000}).then(userResponse => {
            this.dict.type.sys_users = userResponse.rows.map(item => ({
              label: item.userName,
              value: item.userName,
              raw: {...item, listClass: 'primary'}
            }));

            // 获取岗位列表
            listPost({pageNum: 1, pageSize: 1000}).then(postResponse => {
              this.dict.type.sys_post = postResponse.rows.map(item => ({
                label: item.postName,
                value: item.postName,
                raw: {...item, listClass: 'primary'}
              }));

              // 获取规则列表
              listWms_alarm_rule(this.queryParams).then(response => {
                response.rows.forEach(row => {
                  if (row.alarmRuleTargetAreaCode && typeof row.alarmRuleTargetAreaCode === 'string') {
                    row.alarmRuleTargetAreaCode = row.alarmRuleTargetAreaCode.split(',').map(id => parseInt(id, 10))
                    row.alarmRuleTargetAreaName = row.alarmRuleTargetAreaCode.map(areaId => {
                      const area = this.areas.find(area => area.areaId === areaId)
                      return area ? area.areaName : ''
                    }).join(",")

                  } else if (!row.alarmRuleTargetAreaCode) {
                    row.alarmRuleTargetAreaCode = []
                    row.alarmRuleTargetAreaName = []

                  }

                  if (row.alarmRuleRelatedPeople && typeof row.alarmRuleRelatedPeople === 'string') {
                    row.alarmRuleRelatedPeople = row.alarmRuleRelatedPeople.split(",")
                  } else {
                    row.alarmRuleRelatedPeople = []
                  }
                })
                this.wms_alarm_ruleList = response.rows
                this.total = response.total
                this.loading = false
              })
            })
          })
        })
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
        alarmRuleId: null,
        alarmRuleName: null,
        alarmRuleTimeThreshold: null,
        alarmRuleDistThreshold: null,
        alarmRuleTargetAreaCode: null,
        alarmRuleType: null,
        alarmRuleEffectivePeriodS: null,
        alarmRuleEffectivePeriodE: null,
        alarmRuleEnabled: null,
        alarmRuleRelatedPeople: null,
        alarmRuleRelatedDept: null,
        alarmRuleNoticeType: null,
        smsNoticeUsers: null,
        imNoticeUsers: null,
        sysNoticeUsers: null,
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
      this.ids = selection.map(item => item.alarmRuleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报警信息规则"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const alarmRuleId = row.alarmRuleId || this.ids
      getWms_alarm_rule(alarmRuleId).then(response => {
        this.form = response.data

        // 处理报警区域数组
        if (this.form.alarmRuleTargetAreaCode && typeof this.form.alarmRuleTargetAreaCode === 'string') {
          this.form.alarmRuleTargetAreaCode = this.form.alarmRuleTargetAreaCode.split(',').map(id => parseInt(id, 10))
          this.form.alarmRuleTargetAreaName = this.form.alarmRuleTargetAreaCode.map(areaId => {
            const area = this.areas.find(area => area.areaId === areaId)
            return area ? area.areaName : ''
          }).join(",")

        } else if (!row.alarmRuleTargetAreaCode) {
          this.form.alarmRuleTargetAreaCode = []
          this.form.alarmRuleTargetAreaName = []

        }

        //  处理报警人员数组
        if (this.form.alarmRuleRelatedPeople && typeof this.form.alarmRuleRelatedPeople === 'string') {
          this.form.alarmRuleRelatedPeople = this.form.alarmRuleRelatedPeople.split(",")
        } else {
          this.form.alarmRuleRelatedPeople = []
        }

        // 处理通知方式数组
        if (this.form.alarmRuleNoticeType && typeof this.form.alarmRuleNoticeType === 'string') {
          this.form.alarmRuleNoticeType = this.form.alarmRuleNoticeType.split(",")
        } else {
          this.form.alarmRuleNoticeType = []
        }

        // 处理岗位数组
        if (this.form.alarmRuleRelatedDept && typeof this.form.alarmRuleRelatedDept === 'string') {
          this.form.alarmRuleRelatedDept = this.form.alarmRuleRelatedDept.split(",")
        }else{
          this.form.alarmRuleRelatedDept = []
        }

        // 处理短信通知人员数组
        if (this.form.smsNoticeUsers && typeof this.form.smsNoticeUsers === 'string') {
          this.form.smsNoticeUsers = this.form.smsNoticeUsers.split(",")
        }else{
          this.form.smsNoticeUsers = []
        }

        // 处理IM通知人员数组
        if (this.form.imNoticeUsers && typeof this.form.imNoticeUsers === 'string') {
          this.form.imNoticeUsers = this.form.imNoticeUsers.split(",")
        }else{
          this.form.imNoticeUsers = []
        }

        // 处理系统通知人员数组
        if (this.form.sysNoticeUsers && typeof this.form.sysNoticeUsers === 'string') {
          this.form.sysNoticeUsers = this.form.sysNoticeUsers.split(",")
        }else{
          this.form.sysNoticeUsers = []
        }

        this.open = true
        this.title = "修改报警信息规则"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.alarmRuleTargetAreaCode = this.form.alarmRuleTargetAreaCode ? this.form.alarmRuleTargetAreaCode.join(',') : ''
          this.form.alarmRuleRelatedPeople = this.form.alarmRuleRelatedPeople ? this.form.alarmRuleRelatedPeople.join(',') : ''
          this.form.alarmRuleNoticeType = this.form.alarmRuleNoticeType ? this.form.alarmRuleNoticeType.join(',') : ''
          this.form.alarmRuleRelatedDept = this.form.alarmRuleRelatedDept ? this.form.alarmRuleRelatedDept.join(',') : ''
          this.form.smsNoticeUsers = this.form.smsNoticeUsers ? this.form.smsNoticeUsers.join(',') : ''
          this.form.imNoticeUsers = this.form.imNoticeUsers ? this.form.imNoticeUsers.join(',') : ''
          this.form.sysNoticeUsers = this.form.sysNoticeUsers ? this.form.sysNoticeUsers.join(',') : ''
          if (this.form.alarmRuleId != null) {
            updateWms_alarm_rule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWms_alarm_rule(this.form).then(response => {
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
      const alarmRuleIds = row.alarmRuleId || this.ids
      this.$modal.confirm('是否确认删除报警信息规则编号为"' + alarmRuleIds + '"的数据项？').then(function () {
        return delWms_alarm_rule(alarmRuleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/wms_alarm_rule/export', {
        ...this.queryParams
      }, `wms_alarm_rule_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>

::v-deep .el-select {
  width: 100%;
}

::v-deep .el-date-editor {
  width: 100%;
}
</style>
