<template>
  <div>
    <el-dialog
      :visible.sync="isShow"
      title="人员报警"
      width="800"
      append-to-body
    >
      <el-table
        height="500px"
        v-loading="loading"
        :data="wms_alarm_logList"
        @selection-change="handleSelectionChange"
      >
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <el-table-column label="姓名" align="center" prop="personName" />
        <el-table-column label="工号" align="center" prop="personJobNo" />
        <el-table-column label="所属部门" align="center" prop="personDept" />
        <el-table-column label="所在区域" align="center" prop="areaName" />
        <el-table-column
          label="进入时间"
          align="center"
          prop="alarmEnterTime"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{
              parseTime(scope.row.alarmEnterTime, "{y}-{m}-{d}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="异常行为" align="center" prop="alarmBehavior" />
        <el-table-column label="处理结果" align="center" prop="alarmResult" />
        <el-table-column label="处理人员" align="center" prop="alarmHandler" />
        <el-table-column
          label="处理时间"
          align="center"
          prop="alarmHandleTime"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{
              parseTime(scope.row.alarmHandleTime, "{y}-{m}-{d}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope"> </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listWms_alarm_log } from "@/api/system/wms_alarm_log";
import { listArea } from "@/api/system/wms_area";
export default {
  data() {
    return {
      isShow: false,
      loading: false,
      wms_alarm_logList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alarmType: null,
        cardRecordId: null,
        personName: null,
        personJobNo: null,
        personDept: null,
        vehiclePlateNo: null,
        vehicleType: null,
        alarmLocation: null,
        areaCode: null,
        alarmEnterTime: null,
        alarmBehavior: null,
        alarmResult: null,
        alarmHandler: null,
        alarmHandleTime: null,
      },
    };
  },
  methods: {
    openDia() {
      this.isShow = true;
      this.getList();
    },
    getList() {
      this.loading = true;
      listArea().then((response) => {
        this.areas = response.rows;
        listWms_alarm_log(this.queryParams).then((response) => {
          response.rows.forEach((row) => {
            if (row.areaCode && typeof row.areaCode === "string") {
              row.areaName = this.areas.find(
                (area) => area.areaId === row.areaCode
              );
            } else if (!row.areaCode) {
              row.areaName = [];
            }
          });

          this.wms_alarm_logList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      });
    },
    close() {
      this.isShow = false;
    },
  },
};
</script>
