<template>
  <div
    v-silky.enable-error-tip="state"
    class="PlayerFlv"
    data-loading-text="视频流处理中"
  >
    <video
      ref="video"
      :controls="controls"
      :loop="loop"
      :muted="muted"
      :poster="poster"
      :src="url"
      :style="{ 'object-fit': fit }"
      playsinline
      webkit-playsinline
      x5-playsinline
    ></video>
  </div>
</template>

<script>
import FlvJs from "flv.js";

FlvJs.LoggingControl.enableAll = true;
export default {
  name: "PlayerFlv",
  props: {
    type: {
      type: String,
      default: "flv"
    },
    url: {
      type: String,
      default: null
    },
    loop: {
      type: Boolean,
      default: true
    },
    controls: {
      type: Boolean,
      default: false
    },
    muted: {
      type: Boolean,
      default: false
    },
    // 适配方式 同css object-fit
    fit: {
      type: String,
      default: "fill"
    },
    // 是否自动播放
    autoplay: {
      type: Boolean,
      default: false
    },
    extData: {
      type: Object,
      default: null
    },
    poster: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      state: 1
    };
  },
  watch: {
    url: {
      immediate: true,
      handler: "createPlayer"
    }
  },
  beforeDestroy() {
    this.destroyPlayer();
  },
  methods: {
    createPlayer() {
      if (!FlvJs.isSupported()) {
        this.state = "暂不支持此浏览器播放";
        return false;
      }
      this.destroyPlayer();
      if (!this.url) {
        this.state = "等待资源载入...";
        return false;
      }
      this.state = 1;
      this.$nextTick(() => {
        this.player = FlvJs.createPlayer({
          type: this.type,
          url: this.url,
          cors: true,
          isLive: true
        });
        this.player.attachMediaElement(this.$refs.video);
        this.player.load();
        this.player.isPlayStarted = false;
        this.player.on(FlvJs.Events.STATISTICS_INFO, e => {
          if (e.decodedFrames && e.decodedFrames > 0) {
            if (!this.player.isPlayStarted) {
              this.player.isPlayStarted = true;
              this.state = 2;
              this.player.extData = this.extData;
              this.$emit("ready", this.player);
            }
          }
        });
        this.player.on(FlvJs.Events.ERROR, e => {
          console.error(e);
          this.state = e;
        });
        try {
          this.autoplay && this.player.play();
        } catch (e) {
          console.error(e);
        }
      });
    },
    destroyPlayer() {
      if (this.player) {
        this.player.pause();
        this.player.unload();
        this.player.detachMediaElement();
        this.player.destroy();
        this.player = null;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.PlayerFlv {
  position: relative;
  width: 100%;
  height: 100%;
  video {
    width: 100%;
    height: 100%;
    object-fit: fill;
  }
  /*video默认全屏按钮*/
  //video::-webkit-media-controls-fullscreen-button {
  //  display: none !important;
  //}
  /*video默认aduio音量按钮*/
  //video::-webkit-media-controls-mute-button {
  //  display: none !important;
  //}
  /*video默认setting按钮*/
  //video::-internal-media-controls-overflow-button {
  //  display: none !important;
  //}
  /*禁用video的controls（要慎重！不要轻易隐藏掉，会导致点击视频不能播放）*/
  //video::-webkit-media-controls {
  //  display: none !important;
  //}
}
</style>
