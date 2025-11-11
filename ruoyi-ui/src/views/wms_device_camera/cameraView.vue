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
          v-if="imageUrl && streamMode === 'mjpeg'"
          :src="imageUrl"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />

        <!-- 方案2: 使用 Canvas 绘制 -->
        <canvas
          v-if="streamMode === 'canvas'"
          ref="videoCanvas"
          width="768"
          height="500"
          style="width: 100%; height: 500px;"
        ></canvas>

        <!-- 方案3: 使用 WebSocket -->
        <img
          v-if="websocketImageUrl && streamMode === 'websocket'"
          :src="websocketImageUrl"
          alt="摄像头画面"
          style="width: 100%; height: 500px;"
        />

        <div v-if="!imageUrl && !websocketImageUrl" class="no-video">
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
export default {
  name: "CameraView",
  props: {
    deviceId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      dialogVisible: false,
      imageUrl: null,
      websocketImageUrl: null,
      streamMode: 'mjpeg',
      streamInterval: null,
      websocket: null,
      canvasContext: null
    };
  },
  methods: {
    // 打开摄像头画面弹窗
    openCamera(deviceInfo) {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.startCameraStream(deviceInfo);
      });
    },

    // 启动摄像头流
    async startCameraStream(deviceInfo) {
      try {
        const response = await fetch(`/api/camera/stream/start/${this.deviceId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(deviceInfo)
        });

        const result = await response.json();
        if (result.success) {
          this.changeStreamMode(this.streamMode);
        }
      } catch (error) {
        console.error('启动摄像头流失败:', error);
      }
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
      this.imageUrl = `/api/camera/stream/frame/${this.deviceId}?t=${Date.now()}`;
      this.streamInterval = setInterval(() => {
        this.refreshStream();
      }, 100); // 10 FPS
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
