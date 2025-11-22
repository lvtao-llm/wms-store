<template>
  <div>
    <el-dialog
      :title="name + '摄像头实时画面'"
      :visible.sync="dialogVisible"
      width="1200px"
      :before-close="handleClose"
      append-to-body
    >
      <div style="display: flex; gap: 20px">
        <video
          ref="video_left"
          autoplay
          controls
          muted
          style="width: 50%; height: 400px"
        ></video>
        <video
          ref="video_right"
          autoplay
          controls
          muted
          style="width: 50%; height: 400px"
        ></video>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import flvjs from "flv.js";
import { streamStart } from "@/api/system/wms_camera";

export default {
  props: ["name"],
  data() {
    return {
      dialogVisible: false,
      deviceId: null,
      players: [],
    };
  },
  mounted() {
    this.players = [
      {
        channel: 1,
        tag: "video_left",
        player: null,
      }
    ];
  },
  methods: {
    openCamera(device) {
      this.deviceId = device.id;
      this.dialogVisible = true;
      this.$nextTick(() => {
        if (flvjs.isSupported()) {
          this.players.forEach((p) => {
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

            // 启动摄像头流
            streamStart(this.deviceId, p.channel)
              .then((res) => {
                if (!res.success) {
                  console.error("启动摄像头流失败:", res.msg);
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
                    url: `ws://localhost:10041/live/${res.id}`,
                  },
                  {
                    enableWorker: false,
                    enableStashBuffer: false,
                    stashInitialSize: 128,
                    connectionTimeout: 1000000,
                    keepAliveTimeout: 3000000,
                  }
                );

                flv.attachMediaElement(this.$refs[p.tag]);
                flv.load();
                flv.play().catch((e) => console.warn("play error", e));

                flv.on(flvjs.Events.ERROR, (err) => {
                  console.error("FLV播放器错误", err);
                });

                p.player = flv;
              })
              .catch((err) => {
                console.error("启动摄像头流异常:", err);
              });
          });
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
