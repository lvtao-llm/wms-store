<template>
  <div class="camera-view-container">
    <el-dialog
      title="摄像头实时画面"
      :visible.sync="dialogVisible"
      width="800px"
      :before-close="handleClose"
      append-to-body
    >
      <div class="video-container">
        <!-- 方案1: 使用 MJPEG 流 -->
        <img
          v-if="imageUrl1 && streamMode === 'mjpeg'"
          :src="imageUrl1"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />
        <img
          v-if="imageUrl2 && streamMode === 'mjpeg'"
          :src="imageUrl2"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />

        <!-- 方案2: 使用 Canvas 绘制 -->
        <canvas
          v-if="streamMode === 'canvas'"
          ref="videoCanvas1"
          width="768"
          height="500"
          style="width: 100%; height: 500px;"
        ></canvas>
        <canvas
          v-if="streamMode === 'canvas'"
          ref="videoCanvas2"
          width="768"
          height="500"
          style="width: 100%; height: 500px;"
        ></canvas>

        <!-- 方案3: 使用 WebSocket -->
        <img
          v-if="websocketImageUrl1 && streamMode === 'websocket'"
          :src="websocketImageUrl1"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />
        <img
          v-if="websocketImageUrl2 && streamMode === 'websocket'"
          :src="websocketImageUrl2"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />

        <div v-if="!imageUrl1 && !websocketImageUrl1" class="no-video">
          <p>正在加载视频流...</p>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-select v-model="streamMode" size="small" @change="changeStreamMode">
          <el-option label="MJPEG流" value="mjpeg"></el-option>
          <el-option label="WebSocket" value="websocket"></el-option>
          <el-option label="Canvas绘制" value="canvas"></el-option>
        </el-select>
        <el-button @click="closeDialog">关 闭</el-button>
        <el-button type="primary" @click="refreshStream">刷 新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {streamStart} from "@/api/system/wms_camera"

export default {
  name: "CameraView",
  data() {
    return {
      dialogVisible: false,
      imageUrl1: null,
      imageUrl2: null,
      websocketImageUrl1: null,
      websocketImageUrl2: null,
      streamMode: 'mjpeg',
      streamInterval: null,
      websocket: null,
      canvasContext: null,
      deviceId: null
    };
  },
  methods: {
    // 打开摄像头画面弹窗
    openCamera(device, info) {
      this.deviceId = device.id;
      this.dialogVisible = true;
      streamStart(device.id).then(response => {
        if (response.success) {
          this.changeStreamMode(this.streamMode);
        }
      }).catch(error => {
        console.error('启动摄像头流失败:', error);
      })
    },
    // 切换流模式
    changeStreamMode(mode) {
      this.clearStreamInterval();

      switch (mode) {
        case 'mjpeg':
          this.startMjpegStream();
          break;
        case 'websocket':
          this.startWebsocketStream();
          break;
        case 'canvas':
          this.startCanvasStream();
          break;
      }
    },

    // MJPEG 流模式
    startMjpegStream() {
      const token = this.$store.getters.token || localStorage.getItem('token');
      this.imageUrl1 = `${process.env.VUE_APP_BASE_API}/api/camera/stream/frame/${this.deviceId}/1?token=${token}&t=${Date.now()}`;
      this.imageUrl2 = `${process.env.VUE_APP_BASE_API}/api/camera/stream/frame/${this.deviceId}/2?token=${token}&t=${Date.now()}`;
      // this.streamInterval = setInterval(() => {
      //   this.refreshStream();
      // }, 100); // 10 FPS
    },

    // WebSocket 流模式
    startWebsocketStream() {
      if (this.websocket) {
        this.websocket.close();
      }

      const wsUrl = `ws://${window.location.host}/api/camera/ws/${this.deviceId}`;
      this.websocket = new WebSocket(wsUrl);

      this.websocket.onmessage = (event) => {
        if (event.data instanceof Blob) {
          const reader = new FileReader();
          reader.onload = (e) => {
            this.websocketImageUrl = e.target.result;
          };
          reader.readAsDataURL(event.data);
        }
      };

      this.websocket.onclose = () => {
        this.websocket = null;
      };
    },

    // Canvas 绘制模式
    startCanvasStream() {
      if (!this.canvasContext) {
        const canvas = this.$refs.videoCanvas;
        this.canvasContext = canvas.getContext('2d');
      }

      this.streamInterval = setInterval(async () => {
        try {
          const response = await fetch(`/api/camera/stream/frame/${this.deviceId}`);
          if (response.ok) {
            const blob = await response.blob();
            const imageUrl = URL.createObjectURL(blob);
            const img = new Image();
            img.onload = () => {
              this.canvasContext.drawImage(img, 0, 0, 768, 500);
              URL.revokeObjectURL(imageUrl);
            };
            img.src = imageUrl;
          }
        } catch (error) {
          console.error('获取帧失败:', error);
        }
      }, 100); // 10 FPS
    },

    // 刷新视频流
    refreshStream() {
      if (this.streamMode === 'mjpeg') {
        this.imageUrl = `/api/camera/stream/frame/${this.deviceId}?t=${Date.now()}`;
      }
    },

    // 关闭弹窗
    closeDialog() {
      this.dialogVisible = false;
      this.clearStreamInterval();
      if (this.websocket) {
        this.websocket.close();
        this.websocket = null;
      }
    },

    // 清除定时器
    clearStreamInterval() {
      if (this.streamInterval) {
        clearInterval(this.streamInterval);
        this.streamInterval = null;
      }
    },

    // 处理弹窗关闭
    handleClose(done) {
      this.clearStreamInterval();
      if (this.websocket) {
        this.websocket.close();
        this.websocket = null;
      }
      done();
    }
  },

  beforeDestroy() {
    this.clearStreamInterval();
    if (this.websocket) {
      this.websocket.close();
    }
  }
};
</script>
