<template>
  <div class="PopupCamera">
    <div class="camera-frame vci-bg-fill">
      <div class="title vci-pst-rlv vci-flex-rmb">
        <div class="text font-pm-zd">
          <span>{{ title }}</span>
        </div>
        <VciClose @click="$emit('close')" />
      </div>
      <div class="video-frame">
        <EasyPlayer url="http://spjk.tsipark.com:53372/live/ch5_0_1.flv" />
      </div>
    </div>
  </div>
</template>

<script>
import VciClose from "@vci/vci/vue/v2/components/other/VciClose.vue";
import EasyPlayer from "@/views/common/components/EasyPlayer.vue";

export default {
  name: "PopupCamera",
  components: { EasyPlayer, VciClose },
  props: {
    title: {
      type: String,
      default: null
    }
  },
  data() {
    return {};
  }
};
</script>


<style lang="scss" scoped>
.PopupCamera {
  width: 646px;
  height: 410px;
  pointer-events: auto;
  .camera-frame {
    width: 646px;
    height: 410px;
    padding-top: 9px;
    background-image: url("./img/bg.png");
    .title {
      height: 35px;
      .text {
        margin-left: 35px;
        line-height: 35px;
      }
      .VciClose {
        margin-right: 24px;
      }
    }
    .video-frame {
      width: 624px;
      height: 352px;
      margin: auto;
    }
  }
}
</style>
