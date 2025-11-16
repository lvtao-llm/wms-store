<template>
  <div class="panorama-creator-container">
    <el-card>
      <template slot="header">
        <div class="card-header">
          <span>全景图制作</span>
        </div>
      </template>

      <!-- 步骤指示器 -->
      <el-steps :active="currentStep" finish-status="success">
        <el-step title="上传照片"></el-step>
        <el-step title="拼接全景图"></el-step>
        <el-step title="预览全景图"></el-step>
        <el-step title="保存结果"></el-step>
      </el-steps>

      <div class="steps-content" style="margin-top: 30px">
        <!-- 步骤1：上传照片 -->
        <div v-if="currentStep === 0" class="upload-step">
          <el-alert
            title="请上传多张照片用于拼接全景图（建议6-8张，覆盖360度视角）"
            type="info"
            show-icon
          ></el-alert>
          <ImageUpload
            v-model="imageList"
            :limit="10"
            :file-size="10"
            :file-type="['jpg', 'jpeg', 'png']"
            @input="handleImageInput"
          />

          <!-- 上传图片列表展示 -->
          <div v-if="uploadedImages.length > 0" class="uploaded-images-preview">
            <h4>已上传照片（{{ uploadedImages.length }}张）</h4>
            <div class="image-grid">
              <div
                v-for="(image, index) in uploadedImages"
                :key="index"
                class="image-item"
              >
                <el-image
                  :src="image.url"
                  :preview-src-list="[image.url]"
                  style="width: 100%; height: 100%"
                  fit="cover"
                ></el-image>
                <div class="image-index">{{ index + 1 }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤2：拼接全景图 -->
        <div v-if="currentStep === 1" class="stitch-step">
          <el-alert
            title="正在拼接全景图，请稍候..."
            type="info"
            show-icon
          ></el-alert>
          <div class="loading-container">
            <el-loading
              v-loading="isStitching"
              :text="'正在处理 ' + uploadedImages.length + ' 张照片...'"
            ></el-loading>
          </div>
        </div>

        <!-- 步骤3：预览全景图 -->
        <div v-if="currentStep === 2" class="preview-step">
          <el-alert title="全景图预览" type="success" show-icon></el-alert>
          <div class="panorama-preview">
            <div id="panorama-preview" ref="panoramaPreview"></div>
          </div>
        </div>

        <!-- 步骤4：保存结果 -->
        <div v-if="currentStep === 3" class="save-step">
          <el-form :model="saveForm" label-width="80px">
            <el-form-item label="全景图名称">
              <el-input
                v-model="saveForm.name"
                placeholder="请输入全景图名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="全景图描述">
              <el-input
                v-model="saveForm.description"
                type="textarea"
                :rows="3"
                placeholder="请输入全景图描述"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave" :loading="isSaving"
                >保存全景图</el-button
              >
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="steps-action">
        <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
        <el-button
          v-if="currentStep < 3"
          type="primary"
          @click="nextStep"
          :disabled="!canNextStep()"
        >
          {{ currentStep === 0 ? "开始拼接" : "下一步" }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { ElMessage } from "element-ui";

export default {
  name: "PanoramaCreator",
  components: {
    // ImageUpload组件应该已经在全局注册，这里可以直接使用
  },
  data() {
    return {
      currentStep: 0,
      imageList: [],
      uploadedImages: [], // 存储上传成功的图片信息
      panoramaResult: null, // 存储拼接后的全景图URL
      isStitching: false,
      isSaving: false,
      panoramaViewer: null,
      upload: {
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/common/upload",
        // 上传的请求头
        headers: {
          Authorization: "Bearer " + getToken(),
        },
      },
      saveForm: {
        name: "",
        description: "",
      },
      // 全景图配置
      panoramaConfig: {
        default: {
          firstScene: "panorama",
          sceneFadeDuration: 1000,
          autoLoad: true,
          autoRotate: -2,
          showControls: true,
          compass: true,
        },
        scenes: {
          panorama: {
            title: "全景图预览",
            hfov: 110,
            pitch: 0,
            yaw: 0,
            type: "equirectangular",
            panorama: "",
          },
        },
      },
    };
  },
  mounted() {
    window.addEventListener("resize", this.adjustPanoramaSize);
  },
  beforeDestroy() {
    if (this.panoramaViewer) {
      this.panoramaViewer.destroy();
    }
    window.removeEventListener("resize", this.adjustPanoramaSize);
  },
  methods: {
    // 检查是否可以进入下一步
    canNextStep() {
      if (this.currentStep === 0) {
        return this.uploadedImages.length >= 2;
      }
      return true;
    },

    // 上一步
    prevStep() {
      if (this.currentStep === 3) {
        this.resetForm();
      }
      this.currentStep--;
    },

    // 下一步
    nextStep() {
      if (this.currentStep === 0) {
        this.startStitching();
      } else if (this.currentStep === 1) {
        this.previewPanorama();
      } else if (this.currentStep === 2) {
        this.currentStep++;
      }
    },

    // 处理图片上传成功
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.uploadedImages.push({
          name: file.name,
          url: response.url,
          size: file.size,
        });
        ElMessage.success("图片上传成功");
      } else {
        ElMessage.error("图片上传失败：" + response.msg);
      }
    },

    // 处理图片上传失败
    handleUploadError(error) {
      ElMessage.error("上传失败，请重试");
    },

    // 处理图片移除
    handleRemove(file, fileList) {
      const index = this.uploadedImages.findIndex(
        (img) => img.name === file.name
      );
      if (index !== -1) {
        this.uploadedImages.splice(index, 1);
      }
    },

    // 上传前的校验
    beforeUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isJPG) {
        ElMessage.error("上传图片只能是 JPG/PNG 格式!");
        return false;
      }
      if (!isLt10M) {
        ElMessage.error("上传图片大小不能超过 10MB!");
        return false;
      }
      return true;
    },

    // 开始拼接全景图
    startStitching() {
      this.isStitching = true;
      this.currentStep++;

      // 模拟全景图拼接过程
      // 在实际应用中，这里应该调用后端API进行真实的全景图拼接
      setTimeout(() => {
        // 模拟拼接结果，使用第一张图片作为预览（实际应用中应该使用真实的拼接结果）
        // 注意：在实际生产环境中，这里应该替换为后端返回的真实全景图URL
        if (this.uploadedImages.length > 0) {
          // 这里只是演示，实际应该使用后端拼接后的全景图URL
          this.panoramaResult = this.uploadedImages[0].url;
          this.panoramaConfig.scenes.panorama.panorama = this.panoramaResult;
        }
        this.isStitching = false;
      }, 3000);
    },

    // 预览全景图
    previewPanorama() {
      this.currentStep++;
      this.$nextTick(() => {
        this.initPannellumViewer();
      });
    },

    // 初始化Pannellum查看器
    initPannellumViewer() {
      try {
        // 确保容器已经渲染
        if (this.$refs.panoramaPreview) {
          // 销毁之前的实例
          if (this.panoramaViewer) {
            this.panoramaViewer.destroy();
          }

          // 创建新的查看器实例
          this.panoramaViewer = pannellum.viewer(
            this.$refs.panoramaPreview,
            this.panoramaConfig
          );
        }
      } catch (error) {
        console.error("初始化全景图预览失败:", error);
        ElMessage.error("全景图预览初始化失败，请重试");
      }
    },

    // 调整全景图大小
    adjustPanoramaSize() {
      if (this.panoramaViewer) {
        this.panoramaViewer.resize();
      }
    },

    // 保存全景图
    handleSave() {
      if (!this.saveForm.name) {
        ElMessage.warning("请输入全景图名称");
        return;
      }

      this.isSaving = true;

      // 模拟保存过程
      // 实际应用中，这里应该调用后端API保存全景图信息
      setTimeout(() => {
        // 构造保存数据
        const panoramaData = {
          name: this.saveForm.name,
          description: this.saveForm.description,
          imageUrl: this.panoramaResult,
          sourceImages: this.uploadedImages.map((img) => img.url),
        };

        console.log("保存全景图数据:", panoramaData);
        ElMessage.success("全景图保存成功");
        this.isSaving = false;

        // 重置表单，准备制作新的全景图
        this.resetAll();
      }, 2000);
    },

    // 重置表单
    resetForm() {
      this.saveForm = {
        name: "",
        description: "",
      };
    },

    // 重置所有状态，准备制作新的全景图
    resetAll() {
      this.currentStep = 0;
      this.imageList = [];
      this.uploadedImages = [];
      this.panoramaResult = null;
      this.resetForm();

      if (this.panoramaViewer) {
        this.panoramaViewer.destroy();
        this.panoramaViewer = null;
      }
    },
  },
};
</script>

<style scoped>
.panorama-creator-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.steps-content {
  min-height: 300px;
}

.upload-step {
  padding: 20px 0;
}

.uploaded-images-preview {
  margin-top: 20px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
  margin-top: 10px;
}

.image-item {
  position: relative;
  width: 150px;
  height: 100px;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.image-index {
  position: absolute;
  top: 5px;
  left: 5px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 12px;
}

.stitch-step,
.preview-step,
.save-step {
  padding: 20px 0;
}

.loading-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.panorama-preview {
  width: 100%;
  height: 600px;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

#panorama-preview {
  width: 100%;
  height: 100%;
}

.steps-action {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
