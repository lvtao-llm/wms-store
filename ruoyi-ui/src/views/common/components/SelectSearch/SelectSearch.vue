<template>
  <div class="SelectSearch">
    <el-select
      v-model="mapHistorySearch.target"
      :loading="loading"
      :remote-method="search"
      class="vci-bg-fill"
      clearable
      filterable
      placeholder="输入车牌号/姓名查询历史轨迹"
      remote
      reserve-keyword>
      <ElOption
        v-for="option in options"
        :key="option.value"
        :label="option.label"
        :value="option.value" />
    </el-select>
  </div>
</template>

<script>
import { isEmpty } from "@vci/helper/src/other";
import { delay } from "@/common/helper";
import { array } from "@vci/helper/src/array";
import { uuid } from "@vci/helper/src/string";
import { mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";

export default {
  name: "SelectSearch",
  data() {
    return {
      options: [],
      loading: false,
      states: [],
    };
  },
  computed: { ...mapWritableState(storeCore, ["mapHistorySearch"]) },
  methods: {
    async search(selector) {
      this.loading = false;
      if (!isEmpty(selector)) {
        this.loading = true;
        await delay(200);
        this.loading = false;
        this.options = array(6).map(() => ({
          value: `${
            selector.includes("car") ? "car" : "worker"
          }@${uuid().substring(0, 6)}`,
          label: `${selector}@${uuid().substring(0, 6)}`,
        }));
      } else this.options = [];
    },
  },
};
</script>

<style lang="scss" scoped>
.SelectSearch {
  pointer-events: auto;
  :deep(.el-select) {
    font-size: 1rem !important;
    width: 8vw;
    height: 35px;
    background-image: url("./img/bg.png");
    .el-select__input {
      color: #fff;
    }
    .el-input__inner {
      background-color: transparent;
      border: none;
      height: 35px;
      line-height: 35px;
      color: #fff;
      font-size: 1rem !important;
    }
    .el-input__icon {
      line-height: 35px;
      color: #fff;
    }
  }
}
</style>
