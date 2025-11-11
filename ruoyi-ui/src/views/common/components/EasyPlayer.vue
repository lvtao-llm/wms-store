<template>
  <div
    v-silky.enable-error-tip="state"
    class="EasyPlayer"
  >
    <div
      ref="easy-player"
      class="easy-player-dom"
    />
  </div>
</template>

<script>
export default {
  name: "EasyPlayer",
  props: {
    url: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      video: null,
      state: 1,
      config: {
        stretch: true,
        hasAudio: false
      }
    };
  },
  mounted() {
    this.createdVideo();
  },
  methods: {
    createdVideo() {
      const { EasyPlayerPro } = window;
      this.state = 1;
      this.video = new EasyPlayerPro(this.$refs["easy-player"], this.config);
      this.video && this.video.play(this.url)
        .then(() => this.state = 2)
        .catch(e => {
          console.error(e);
          this.state = e;
        });
      this.$once("hook:beforeDestroy", () => {
        this.video && this.video.destroy();
      });
    }
  }
};
</script>


<style lang="scss" scoped>
.EasyPlayer {
  width: 100%;
  height: 100%;
  .easy-player-dom {
    width: 100%;
    height: 100%;
  }
}
</style>
