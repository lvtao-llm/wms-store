import { defineStore } from "pinia";

export const storeCore = defineStore("core", {
  state: () => ({
    // 地图视角模式
    mapPerspectiveMode: "3D",
    // 地图风险区域
    mapRiskArea: {
      drawing: false,
      isUpdate: false,
      info: {
        name: "",
        idType: "",
        color: "#ff2020"
      },
      types: [
        {
          name: "避难所",
          value: "1",
          color: "#ffff00"
        },
        {
          name: "限速管控区",
          value: "2",
          color: "#5876f8"
        },
        {
          name: "高风险区域",
          value: "3",
          color: "#ff2020"
        }
      ]
    },
    // 地图历史搜索
    mapHistorySearch: {
      isDisplayTrajectoryHistory: false,
      target: "",
      startedAt: null,
      endedAt: null
    }
  }),
  actions: {
    // 重置风险区域信息
    resetMapRiskAreaInfo() {
      this.mapRiskArea.drawing = false;
      this.mapRiskArea.isUpdate = false;
      this.mapRiskArea.info = {
        name: "",
        idType: "",
        color: "#ff2020"
      };
    }
  }
});

