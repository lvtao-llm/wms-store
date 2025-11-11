<template>
  <div class="DateRange vci-flex-rml vci-pst-rlv">
    <div class="icon-calendar vci-pst-m vci-bg-fill"></div>
    <div class="icon-calendar vci-pst-m vci-bg-fill"></div>
    <ElDatePicker
      v-model="mapHistorySearch.startedAt"
      :picker-options="pickerOptions"
      end-placeholder="结束月份"
      format="yyyy-MM-dd HH:mm:ss"
      range-separator="-"
      start-placeholder="开始月份"
      type="datetime" />
    <span class="hor"> - </span>
    <ElDatePicker
      v-model="mapHistorySearch.endedAt"
      :picker-options="pickerOptions"
      end-placeholder="结束月份"
      format="yyyy-MM-dd HH:mm:ss"
      range-separator="-"
      start-placeholder="开始月份"
      type="datetime" />
  </div>
</template>

<script>
import { mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";

export default {
  name: "DateRange",
  data() {
    return {
      pickerOptions: { disabledDate: (time) => time.getTime() > Date.now() },
    };
  },
  computed: {
    ...mapWritableState(storeCore, ["mapHistorySearch"]),
  },
};
</script>

<style lang="scss" scoped>
.DateRange {
  pointer-events: auto;
  cursor: pointer;
  .icon-calendar {
    pointer-events: none;
    z-index: 4;
    width: 17px;
    height: 15px;
    background-image: url("./img/icon.date.png");
    &:nth-child(1) {
      left: 8px;
    }
    &:nth-child(2) {
      left: 9vw;
    }
  }
  .hor {
    margin: 0 4px;
  }
  :deep(.el-date-editor) {
    font-size: 1rem !important;
    width: 8vw;
    .el-input__icon {
      display: none;
    }
    .el-input__inner {
      width: 100%;
      height: 35px;
      background-color: transparent;
      border: none;
      background-image: url("./img/bg.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-position: center;
      color: #fff;
      font-size: 0.7rem !important;
      font-family: "oppo.m", sans-serif;
      line-height: 35px;
      padding-right: 0;
    }
    & + .el-date-editor {
      margin-left: 12px;
    }
  }
}
</style>
