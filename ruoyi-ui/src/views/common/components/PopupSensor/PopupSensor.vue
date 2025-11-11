<template>
  <div class="PopupSensor vci-bg-fill">
    <div class="title vci-flex-rmb">
      <div class="text vci-fs-20 font-pm-zd">
        {{ title }}
      </div>
      <VciClose @click="$emit('close')" />
    </div>
    <div
      v-if="list"
      class="info-frame font-oppo-m"
    >
      <div
        v-for="item in list"
        :key="item.id"
        class="the-info"
      >
        <span class="name">{{ item["name"] }}:</span>
        <span class="value">{{ item["value"] }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import VciClose from "@vci/vci/vue/v2/components/other/VciClose.vue";

export default {
  name: "PopupSensor",
  components: { VciClose },
  props: {
    title: {
      type: String,
      default: null
    },
    list: {
      type: Array,
      default: null
    }
  },
  data() {
    return {};
  }
};
</script>


<style lang="scss" scoped>
.PopupSensor {
  width: 229px;
  height: 84px;
  padding-top: 3px;
  background-image: url("./img/bg.png");
  pointer-events: auto;
  cursor: default;
  .title {
    height: 27px;
    .text {
      margin-left: 18px;
    }
    .VciClose {
      margin-right: 18px;
    }
  }
  .info-frame {
    padding-top: 16px;
    padding-left: 18px;
    .name {
      color: #c7e7fd;
    }
    .value {
      padding-left: 4px;
      color: #9bb9cd;
    }
  }
}
</style>
