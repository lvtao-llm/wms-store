<template>
  <div class="PlayerHikHc">
    <div
      ref="player"
      class="vps-player"
    >
    </div>
    <VphState
      :error="error"
      :tip="tip"
      class="vps-state"
    />
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/11/21 13:05
import VphState from "./components/VphState.vue";
import { uuid } from "@vci/helper/src/string";

/**
 * 海康视频播放器 直连版本
 * 需要引入相关SDK 具体信息查询官方文档 WebSDK3.3.2
 */
const IpPoolLogined = [];
export default {
  name: "PlayerHikHc",
  components: { VphState },
  props: {
    monitorParams: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      isInitPlugin: false,
      isInPlay: false,
      tip: null,
      error: null,
      inter: {
        initPlugin: -1
      }
    };
  },
  watch: {
    monitorParams: {
      immediate: true,
      handler: "render"
    }
  },
  beforeDestroy() {
    this.stop();
  },
  methods: {
    initPlugin(reInitPlugin = false) {
      return new Promise((resolve, reject) => {
        this.$nextTick(() => {
          if (!reInitPlugin && this.isInitPlugin) resolve();
          else {
            this.isInitPlugin = false;
            // 检查插件是否已经安装过
            if (global.WebVideoCtrl.I_CheckPluginInstall && global.WebVideoCtrl.I_CheckPluginInstall() === -1) reject("您还未安装过插件，双击开发包目录里的HCWebSDKPlugin.exe安装");
            // if (global.WebVideoCtrl.I_SupportNoPlugin && !global.WebVideoCtrl.I_SupportNoPlugin()) reject("当前浏览器不支持视频无插件播放");
            const el = this.$refs.player;
            el.setAttribute("id", uuid());
            this.inter.initPlugin = setTimeout(() => {
              global.WebVideoCtrl.I_InitPlugin({
                iWndowType: 1,
                bWndFull: true,
                // bDebugMode: true,
                cbInitPluginComplete: () => {
                  global.WebVideoCtrl.I_InsertOBJECTPlugin(el.getAttribute("id"));
                  this.isInitPlugin = true;
                  resolve();
                }
              });
            }, .35e3);
          }
        });
      });
    },
    play(params) {
      this.resetState();
      this.tip = "准备中...";
      if (!params.ip) this.error = "缺少必要参数,ip";
      if (!params.port) this.error = "缺少必要参数,port";
      if (!params.loginName) this.error = "缺少必要参数,loginName";
      if (!params.loginPwd) this.error = "缺少必要参数,loginPwd";
      if (this.error) return false;
      const monitorParams = {
        protocol: /^https/.test(window.location.protocol) ? 2 : 1,// protocol 1：http, 2:https
        ip: params.ip,
        port: params.port,
        loginName: params.loginName,
        loginPwd: params.loginPwd,
        streamType: params.streamType || 1,// stream 1：main stream  2：sub-stream  3：third stream  4：transcode stream
        channelID: params.channelID,// channel no
        zeroChannel: false// zero channel
      };
      // 登录设备
      this.tip = "登录中...";
      const playHandle = async () => {
        this.tip = "即将播放实时画面...";
        global.WebVideoCtrl.I_StartRealPlay(`${monitorParams.ip}_${monitorParams.port}`, {
          iWndIndex: 0,
          iStreamType: monitorParams.streamType,
          iChannelID: monitorParams.channelID,
          bZeroChannel: monitorParams.bZeroChannel,
          success: () => {
            this.isInPlay = true;
            this.resetState();
            this.$emit("onPlayStart");
          },
          error: e => {
            console.error(e);
            this.error = "播放异常";
          }
        });
      };
      if (IpPoolLogined.includes(monitorParams.ip)) playHandle();
      else global.WebVideoCtrl.I_Login(monitorParams.ip, monitorParams.protocol, monitorParams.port, monitorParams.loginName, monitorParams.loginPwd, {
        success: () => {
          IpPoolLogined.push(monitorParams.ip);
          playHandle();
        },
        error: e => {
          console.error(e);
          this.error = "登录异常";
        }
      });
    },
    stop() {
      return new Promise(resolve => {
        if (this.isInPlay) global.WebVideoCtrl.I_Stop({
          success: () => resolve(),
          error: () => resolve()
        }); else resolve();
      });
    },
    resetState() {
      this.tip = null;
      this.error = null;
    },
    async render(reInitPlugin = false) {
      clearTimeout(this.inter.initPlugin);
      clearTimeout(this.inter.play);
      this.resetState();
      this.tip = "插件初始化中...";
      await this.stop();
      this.initPlugin(reInitPlugin)
        .then(() => this.inter.play = setTimeout(() => this.play(this.monitorParams), 1e3))
        .catch(e => {
          console.error(e);
          this.error = "插件初始化异常";
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.PlayerHikHc {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  .vps-player {
    position: relative;
    z-index: 1;
    width: 100%;
    height: 100%;
  }
  .vps-state {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 2;
  }
}
</style>