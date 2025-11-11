<template>
  <div class="PlayerHls">
    <video
      ref="video"
      playsinline
      webkit-playsinline
      x5-playsinline
    ></video>
    <div
      v-if="playerTip"
      class="ph-error"
    >
      {{ playerTip }}
    </div>
  </div>
</template>

<script>
import Hls from "hls.js";

/**
 * hls视频流播放组件
 * 测试地址 https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8
 */
export default {
  name: "PlayerHls",
  props: {
    url: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      isPlaying: false,
      playerTip: null,
      inter: {
        playerRecovery: -1,
        playerRefresh: -1
      }
    };
  },
  watch: {
    url: {
      immediate: true,
      async handler() {
        if (!Hls.isSupported()) {
          this.playerTip = "当前浏览器不支持该播放器，请使用最新的谷歌浏览器尝试";
          return false;
        }
        clearTimeout(this.inter.playerRefresh);
        clearTimeout(this.inter.playerRecovery);
        if (!this.url) {
          this.playerTip = "等待资源载入...";
          return false;
        }
        await this.$nextTick();
        this.createPlayer(() => {
          this.isPlaying = true;
          this.$refs.video.play();
        });
      }
    }
  },
  beforeDestroy() {
    this.destroyPlayer();
  },
  methods: {
    createPlayer(call) {
      clearTimeout(this.inter.playerRefresh);
      this.destroyPlayer();
      this.isPlaying = false;
      this.playerTip = "载入中...";
      this.player = new Hls({
        "enableWorker": true,
        "lowLatencyMode": true,
        "backBufferLength": 90
      });
      this.player.attachMedia(this.$refs.video);
      this.player.on(Hls.Events.MEDIA_ATTACHED, () => {
        this.player.loadSource(this.url);
        this.player.on(Hls.Events.MANIFEST_PARSED, () => {
          this.playerTip = null;
          this.play();
          call && call();
        });
      });
      this.player.on(Hls.Events.ERROR, (e, data) => {
        console.info("视频播放异常: ", e, data);
        this.player.stopLoad();
        clearTimeout(this.inter.playerRecovery);
        if (data.fatal) {
          switch (data.type) {
            case Hls.ErrorTypes.NETWORK_ERROR:
              this.playerTip = "视频流网络异常，正在尝试恢复";
              this.inter.playerRecovery = setTimeout(this.createPlayer.bind(this, call), 2e3);
              break;
            case Hls.ErrorTypes.MEDIA_ERROR:
              this.playerTip = "视频流媒体异常，正在尝试恢复";
              this.inter.playerRecovery = setTimeout(this.createPlayer.bind(this, call), 2e3);
              break;
            default:
              this.playerTip = "视频流异常，正在尝试恢复";
              this.inter.playerRecovery = setTimeout(this.createPlayer.bind(this, call), 2e3);
          }
        } else {
          this.playerTip = "视频流异常，正在尝试恢复";
          this.inter.playerRecovery = setTimeout(this.createPlayer.bind(this, call), 2e3);
        }
      });
      this.inter.playerRefresh = setTimeout(() => !this["_isDestroyed"] && this.createPlayer(), 60e3);
    },
    destroyPlayer() {
      if (!this.player) return false;
      this.player.stopLoad();
      this.player.detachMedia();
      this.player.destroy();
      this.player = null;
    },
    play() {
      this.isPlaying = true;
      this.$refs.video.play();
    },
    parse() {
      this.isPlaying = false;
      this.$refs.video.pause();
    },
    refresh() {
      this.createPlayer(() => this.play());
    }
  }
};
</script>

<style lang="scss" scoped>
.PlayerHls {
  position: relative;
  width: 100%;
  height: 100%;
  video {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1;
    display: block;
    width: 100%;
    height: 100%;
    object-fit: fill;
    overflow: hidden;
    white-space: normal;
    word-break: break-all;
  }
  .ph-error {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 4;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, .6);
    font-size: 16px;
    color: #fff;
    padding: 12px;
  }
}
</style>
