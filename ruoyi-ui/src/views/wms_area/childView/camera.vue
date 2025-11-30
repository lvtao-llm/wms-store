<template>
  <div class="map-container">
    <el-dialog
      :visible.sync="isShow"
      width="100%"
      @close="close"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="PopupCamera">
        <div class="camera-frame vci-bg-fill">
          <div class="video-frame">
            <div
              style="
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-start;
                gap: 20px;
                width: 100vw;
                height: 80vh;
                overflow-y: auto;
              "
            >
              <div v-for="(i, index) in players" :key="index">
                <video
                  :ref="i.tag"
                  autoplay
                  controls
                  muted
                  style="width: 23vw; height: 30vh"
                ></video>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { streamStarts } from "@/api/system/wms_area";
import flvjs from "flv.js";
export default {
  props: {
    cameras: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      isShow: false,
      indexKey: 0,
      imgSrc: "",
      players: [],
    };
  },
  created() {},
  methods: {
    openDia(row) {
      this.isShow = true;
      if (!row.cameras.length) return this.$message.warning("暂无绑定的摄像头");
      const arr = row.cameras.split(",");
      this.players = arr
        .map((i) => {
          return [
            {
              channel: 1,
              name: i,
              tag: i + "-1",
              player: null,
            },
            {
              channel: 2,
              name: i,
              tag: i + "-2",
              player: null,
            },
          ];
        })
        .flat();

      this.openCamera();
    },
    close() {
      this.isShow = false;
      this.closePlayers();
    },
    openCamera() {
      this.$nextTick(() => {
        if (flvjs.isSupported()) {
          if (!this.players.length)
            return this.$message.warning("暂无绑定的摄像头");
          else {
            setTimeout(() => {
              this.players.forEach(async (p) => {
                // 先销毁旧播放器
                if (p.player) {
                  try {
                    p.player.unload();
                    p.player.detachMediaElement();
                    p.player.destroy();
                  } catch (e) {
                    console.warn("销毁旧播放器出错:", e);
                  }
                  p.player = null;
                }

                const data = await streamStarts(p.name, p.channel);
                console.log(data);
                if (!data.success) {
                  console.error("启动摄像头流失败:", data.msg);
                  return;
                }
                let baseUrl = process.env.VUE_APP_BASE_API.replace(
                  "http://",
                  ""
                );
                baseUrl = baseUrl.slice(0, baseUrl.indexOf(":"));

                const flv = flvjs.createPlayer(
                  {
                    type: "flv",
                    isLive: true,
                    url: `ws://${baseUrl}:${data.port}/live/${data.id}`,
                  },
                  {
                    enableWorker: false,
                    enableStashBuffer: false,
                    stashInitialSize: 128,
                    connectionTimeout: 1000000,
                    keepAliveTimeout: 3000000,
                  }
                );

                let videoElement = this.$refs[p.tag];
                // 处理ref可能返回数组的情况
                if (Array.isArray(videoElement) && videoElement.length > 0) {
                  videoElement = videoElement[0];
                }
                // 确保是有效的DOM元素且有addEventListener方法
                if (
                  !videoElement ||
                  typeof videoElement.addEventListener !== "function"
                ) {
                  console.error(`视频元素 ${p.tag} 未找到或不是有效的DOM元素`);
                  return;
                }
                flv.attachMediaElement(videoElement);
                flv.load();
                flv.play().catch((e) => console.warn("play error", e));

                flv.on(flvjs.Events.ERROR, (err) => {
                  console.error("FLV播放器错误", err);
                  // 确保错误信息正确显示
                  if (err && typeof err === "object") {
                    console.error("错误详情:", JSON.stringify(err, null, 2));
                  }
                });

                p.player = flv;
              });
            }, 500);
          }
        }
      });
    },
    closeDialog() {
      this.dialogVisible = false;
      this.closePlayers();
    },
    closePlayers() {
      this.players.forEach((p) => {
        if (p.player) {
          try {
            p.player.unload();
            p.player.detachMediaElement();
            p.player.destroy();
          } catch (e) {
            console.warn("关闭播放器失败:", e);
          }
          p.player = null;
        }
      });
    },
    handleClose(done) {
      this.closeDialog();
      done();
    },
  },
  beforeDestroy() {
    this.closePlayers();
  },
};
</script>

<style lang="scss" scoped>
.PopupCamera {
  width: 100%;
  height: 90vh;
  pointer-events: auto;

  .camera-frame {
    width: 100%;
    height: 80vh;
    padding-top: 9px;
    // background-image: url("./img/bg.png");

    .title {
      height: 35px;
      font-size: 20px;

      .text {
        margin-left: 35px;
        line-height: 35px;
      }

      .VciClose {
        margin-right: 24px;
      }
    }

    .video-frame {
      width: 100%;
      height: 100%;
      margin: auto;
      padding-top: 5vh;
    }
  }
}
</style>
