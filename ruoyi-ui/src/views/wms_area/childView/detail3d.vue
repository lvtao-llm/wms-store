<template>
  <div class="map-container">
    <el-dialog
      :visible.sync="show"
      width="95%"
      @close="close"
      append-to-body
      :close-on-click-modal="false"
    >
      <iframe
        v-if="show"
        :key="indexKey"
        style="width: 100%; height: 100vh"
        id="contentFrame"
        :src="imgSrc"
      ></iframe>
    </el-dialog>
  </div>
</template>

<script>
import defaultSettings from "@/settings";
export default {
  data() {
    return {
      show: false,
      indexKey: 0,
      imgSrc: "",
    };
  },
  methods: {
    openDia(row, type, id, beginTime, endTime) {
      this.imgSrc = defaultSettings.iframeUrl + "?type=" + type + "&id=" + id;
      if (beginTime) this.imgSrc = this.imgSrc + "&beginTime=" + beginTime;
      if (endTime) this.imgSrc = this.imgSrc + "&endTime=" + endTime;
      this.show = true;
    },
    close() {
      this.show = false;
      this.imgSrc = ""; // 清空src，停止iframe的请求
    },
  },
};
</script>
