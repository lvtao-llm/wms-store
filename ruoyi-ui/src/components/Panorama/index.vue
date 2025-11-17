<template>
  <div id="app">
    <el-dialog
      :visible.sync="isShow"
      title="预览"
      width="80vw"
      append-to-body
      @close="closeDia"
    >
      <div id="panorama" ref="panorama"></div>
    </el-dialog>
  </div>
</template>

<script>
/* global pannellum */
export default {
  name: "Vpannellum",
  props: {
    config: {
      type: Object,
      default: () => ({
        // 默认配置
        default: {
          firstScene: "circle", // 页面初始化展示的场景id
          // author: "作者名称", // 左下角信息框中的作者名称，如果不需要，直接把这行注掉
          sceneFadeDuration: 1000, // 跳转的时候淡入淡出属性，时间为毫秒
          autoLoad: true, // 自动加载
          autoRotate: -2, // 自动旋转
        },
        // 全景图配置
        scenes: {
          circle: {
            // 场景id
            // title: "当前全景画面名称", // 左下角信息框中显示现在所在位置名称，如果不需要，直接把这行注掉
            hfov: 110, // 水平视角，根据调整来展示宽度，数值越大看的越窄，数值越小，看的越宽，正常默认110
            pitch: -3, // 俯仰角，数值大为仰，数值小为俯，默认值为0，即为平视
            yaw: 117, // 旋转角，默认为0，范围-180到180，或者0到360，-180到180从-1开始为左，1开始为右；0到360为右
            minPitch: -60, // 最小俯仰角，限制向下拖动的角度，防止看到脚下的内容（-90到90之间，负值越小越向下）
            minHfov: -30, // 最小视野 → 最大放大（视野最窄，缩放最大）
            maxHfov: 120, // 最大视野 → 最小放大（视野最宽，缩放最小）
            type: "equirectangular", // 全景图类型，equirectangular是已经做成完整的全景图，cubemap是由上下左右前后六个面的图片组成全景图的属性，multires是由多层级的图像瓦片组成的全景图属性，具体使用方法自己百度或者看pannellum文档
            panorama:
              "http://112.98.110.101:10030/profile/upload/2025/11/11/25-2_20251111191628A002.jpg", // 现在页面地址，最好做成url格式，我电脑有问题，不能用本地地址，到底能不能用本地地址，自己试
            // hotSpots: [
            //   {
            //     pitch: -2.1, // 跳转点所在的俯仰位置
            //     yaw: 132.9, // 跳转点所在的左右位置点
            //     type: "scene", // scene为跳转切换图标跟属性，info为显示信息热点，一般都是用于显示所在点位的一些信息，custom没用过，自己百度或者看pannellum文档
            //     text: "跳转箭头鼠标放置显示将要跳转名称", // 跳转点显示名称
            //     sceneId: "house", // 场景ID，sceneId后面的内容就是下一个场景的id，现在的id是house，那么跳转场景的id就要用house命名，如果跳转的场景id是puda，那么接下来跳转的场景id就要用puda来命名
            //   },
            // ],
          },
        },
      }),
    },
  },
  data() {
    return {
      isShow: false,
      baseUrl: process.env.VUE_APP_BASE_API,
      imgSrc: "",
      panoramaViewer: null, // 存储pannellum.viewer的实例
      panoramaWidth: "100%", // 初始化全景图宽度
    };
  },
  mounted() {
    this.initPannellum();
    window.addEventListener("resize", this.adjustPanoramaSize);
  },
  beforeDestroy() {
    if (this.panoramaViewer) {
      this.panoramaViewer.destroy(); // 调用Pannellum的销毁方法，清理资源
    }
    window.removeEventListener("resize", this.adjustPanoramaSize);
  },
  methods: {
    openDia(src) {
      this.imgSrc = this.baseUrl + src;
      this.isShow = true;
      this.$nextTick(() => {
        setTimeout(() => {
          // 先设置图片源并显示对话框

          this.initPannellum();
        }, 100); // 添加明确的延迟，确保对话框渲染完成
      });
    },
    closeDia() {
      this.isShow = false;
    },
    initPannellum() {
      console.log(this.config);
      this.config.scenes.circle.panorama = this.imgSrc;
      try {
        this.panoramaViewer = pannellum.viewer(
          this.$refs.panorama,
          this.config
        );
      } catch (error) {
        console.error("初始化全景图像:", error);
        // 这里可以添加用户友好的错误处理逻辑，比如显示错误信息
      }
    },
  },
};
</script>

<style scoped>
#panorama {
  width: 100%;
  height: 1400px;
}
</style>
