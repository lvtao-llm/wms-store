import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import CptCar from "./CptCar/CptCar.vue";
import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { PluginLoading } from "@vci/vci/vue/v2/components/other/VpLoading";
import { delay } from "@/common/helper";
import TrajectoryRealtime from "@/views/common/components/DsMapD3/things/trajectory/TrajectoryRealtime";
import { cloneDeepByJSON } from "@vci/helper/src/object";
import { resourceRootAliyun } from "@vci/vci/config/api";
import { storeCore } from "@/store/store.core";

export default class Car extends Marker {
  init() {
    this.option.vm = instantiateComponentVm(CptCar);
    super.init();
    this.isCar = true;
    this.isDisplayTrajectoryReal = false;
    this.isDisplayTrajectoryHistory = false;
    this.mockDataTrajectory = [];
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.object.center.set(.5, 1);
    this.hide().fadeIn();
    this.vm.$on("display-trajectory-real", this.displayTrajectoryReal.bind(this));
    this.vm.$on("cancel-trajectory-real", this.cancelTrajectoryReal.bind(this));
    this.vm.$on("display-trajectory-history", () => {
      this.qt.things.filter(thing => thing.isCar || thing.isWorker).forEach(thing => {
        thing.isDisplayTrajectoryReal && thing.cancelTrajectoryReal();
        thing.isDisplayTrajectoryHistory && thing.cancelTrajectoryHistory();
      });
      storeCore().mapHistorySearch.target = "car-" + this.id;
      this.vm.isDisplayTrajectoryHistory = this.isDisplayTrajectoryHistory = true;
      storeCore().mapVisibleHistoryUI = true;
      storeCore().searchHistoryTrajectory(this);
    });
    this.vm.$on("cancel-trajectory-history", () => this.qt.thingPark.clearTrajectoryHistory());
  }

  async displayTrajectoryReal() {
    this.qt.things.filter(thing => thing.isCar || thing.isWorker).forEach(thing => {
      thing.isDisplayTrajectoryReal && thing.cancelTrajectoryReal();
      thing.isDisplayTrajectoryHistory && thing.cancelTrajectoryHistory();
    });
    PluginLoading.open({ text: "实时轨迹查询中" });
    await delay();
    const { qt } = this
    qt.thingPark.clearTrajectoryHistory();
    this.thingTrajectoryReal = new TrajectoryRealtime({
      qt,
      arrow: { urlTexture: `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/trajectory/arrow.green.png` },
      line: { urlTexture: `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/trajectory/line.green.png` }
    });
    this.thingTrajectoryReal.updateTrajectory(this.mockDataTrajectory);
    PluginLoading.close();
    this.vm.isDisplayTrajectoryReal = this.isDisplayTrajectoryReal = true;
    return this;
  }

  async cancelTrajectoryReal() {
    this.vm.isDisplayTrajectoryReal = this.isDisplayTrajectoryReal = false;
    this.thingTrajectoryReal && this.thingTrajectoryReal.destroy();
    delete this.thingTrajectoryReal;
    return this;
  }

  async cancelTrajectoryHistory() {
    this.vm.isDisplayTrajectoryHistory = this.isDisplayTrajectoryHistory = false;
    storeCore().mapVisibleHistoryUI = false;
    return this;
  }

  update(option) {
    super.update(option);
    this.vm.idCar = this.id;
    this.position.toString() !== (this.mockDataTrajectory[this.mockDataTrajectory.length - 1] || []).toString() && this.mockDataTrajectory.push(cloneDeepByJSON(this.position));
    this.isDisplayTrajectoryReal && this.thingTrajectoryReal && this.thingTrajectoryReal.updateTrajectory(this.mockDataTrajectory);
    return this;
  }

  toHistoryMode() {
    this.vm.isHistoryMode = true;
    this.sleep();
  }

  destroy(force = false, enableCr = true) {
    this.thingTrajectoryReal && this.thingTrajectoryReal.destroy();
    return super.destroy(force, enableCr);
  }
}