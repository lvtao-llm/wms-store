<template>
  <div
    class="CptMarkerRaInfo vci-bg-fill vci-flex-ccbt"
    @click="$emit('blur')"
  >
    <div
      v-for="(info, i) in infos"
      :key="i"
      class="the-line vci-w-100 vci-fs-16 font-oppo-m"
    >
      <span class="name">{{ info["name"] }}</span>
      <span
        :style="{color: info['color']}"
        class="value"
      >
        {{ info["value"] }}
      </span>
    </div>
  </div>
</template>

<script>

export default {
  name: "CptMarkerRaInfo",
  props: {
    infos: {
      type: Array,
      default: () => ([])
    }
  },
  data() {
    return {};
  }
};
</script>


<style lang="scss" scoped>
.CptMarkerRaInfo {
  pointer-events: auto;
  z-index: 10;
  min-width: 163px;
  height: 74px;
  padding: 12px 20px;
  background-image: url("./img/bg.png");
  .name {
    color: #c7e7fd;
  }
  .value {
    color: #9bb9cd;
  }
}
</style>
