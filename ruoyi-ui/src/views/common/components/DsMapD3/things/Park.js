import Thing from "@vci/quick-three/src/core/Thing";
import MaterialPatch from "@vci/quick-three/src/helper/MaterialPatch";
import Car from "./car/Car";
import Worker from "./worker/Worker";
import Camera from "./camera/Camera";
import Sensor from "./sensor/Sensor";
import Custom from "./custom/Custom";
import { delay } from "@/common/helper";
import { DataMockTrajectory } from "@/constants";
import { isEmpty } from "@vci/helper/src/other";
import { PluginNotification } from "@vci/vci/vue/v2/components/other/VpNotification";
import TrajectoryHistory from "@/views/common/components/DsMapD3/things/trajectory/TrajectoryHistory";
import { storeCore } from "@/store/store.core";
import { uuid } from "@vci/helper/src/string";
import RiskArea from "@/views/common/components/DsMapD3/things/riskArea/RiskArea";

export default class Park extends Thing {
  init() {
    super.init();
    this.isPark = true;
    this.thingsCar = [];
    this.thingsWorker = [];
    this.thingsCamera = [];
    this.thingsSensor = [];
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { qt } = this;
    // 启用阴影投射与接收
    this.modifyShadowState(true);
    // 修改模型光泽度
    MaterialPatch.ModifyMtlProperties(this.object, {
      roughness: 1,
      metalness: 1
    });
    // 修改草地渲染顺序
    this.object.traverse(o3 => {
      if (o3.name === "caodi") {
        o3.renderOrder = -9;
      }
    });
    /// 创建风险区域实例
    qt.thingRiskArea = new RiskArea({ qt }).sleep();
  }

  /**
   * 通过数据更新车辆
   * @param data
   */
  updateCarFromData(data) {
    const { qt } = this;
    this.thingsCar = qt.pluginThings.updateThings(
      data.map(v => {
        return {
          id: "ic-" + v.id,
          name: v.name,
          position: qt.thingWorld.lngLatToCoords(v.position),
          creator: Car
        };
      }).filter(v => v),
      null,
      this.thingsCar
    );
  }

  /**
   * 通过数据更新人员
   * @param data
   */
  updateWorkerFromData(data) {
    const { qt } = this;
    this.thingsWorker = qt.pluginThings.updateThings(
      data.map(v => {
        return {
          id: "iw-" + v.id,
          name: v.name,
          position: qt.thingWorld.lngLatToCoords(v.position),
          creator: Worker
        };
      }).filter(v => v),
      null,
      this.thingsWorker
    );
  }

  /**
   * 通过数据更新摄像头
   * @param data
   */
  updateCameraFromData(data) {
    const { qt } = this;
    this.thingsCamera = qt.pluginThings.updateThings(
      data.map(v => {
        return {
          id: "camera-" + v.id,
          name: v.name,
          position: qt.thingWorld.lngLatToCoords(v.position, v.elevation || 0),
          creator: Camera
        };
      }).filter(v => v),
      null,
      this.thingsCamera
    );
  }

  /**
   * 通过数据更新传感器
   * @param data
   */
  updateSensorFromData(data) {
    const { qt } = this;
    this.thingsSensor = qt.pluginThings.updateThings(
      data.map(v => {
        return {
          id: "sensor-" + v.id,
          name: v.name,
          position: qt.thingWorld.lngLatToCoords(v.position, v.elevation || 0),
          creator: Custom
        };
      }).filter(v => v),

      null,
      this.thingsSensor
    );
    console.log(this.thingsSensor, 111);
  }

  /**
   * 展示历史轨迹
   * @param name  名称
   * @param type  类型 car|worker
   * @param dataTrajectory  轨迹数据
   * @param thingCW         车或人实例
   * @returns {Park}
   */
  async displayTrajectoryHistory(name, type, dataTrajectory = [], thingCW) {
    this.clearTrajectoryHistory();
    if (isEmpty(dataTrajectory)) {
      PluginNotification.error("缺少轨迹数据，无法展示");
      thingCW && thingCW.cancelTrajectoryHistory();
      return this;
    }
    if (dataTrajectory.length < 2) {
      PluginNotification.error("轨迹数据过少，无法展示");
      thingCW && thingCW.cancelTrajectoryHistory();
      return this;
    }
    await delay(.1e3);
    storeCore().mapHistorySearch.isDisplayTrajectoryHistory = true;
    const { qt } = this;
    this.thingTrajectoryHistoryCW = new (type === "car" ? Car : Worker)({
      qt,
      name
    });
    this.thingTrajectoryHistoryCW.toHistoryMode();
    this.thingTrajectoryHistory = new TrajectoryHistory({
      qt,
      dataTrajectory,
      thingCW: this.thingTrajectoryHistoryCW,
      type
    });
    this._thingCWDth = thingCW;
    return this;
  }

  clearTrajectoryHistory() {
    const thingTrajectoryHistoryCW = this.thingTrajectoryHistoryCW;
    thingTrajectoryHistoryCW && thingTrajectoryHistoryCW.fadeOut().then(() => thingTrajectoryHistoryCW.destroy());
    this.thingTrajectoryHistory && this.thingTrajectoryHistory.destroy();
    this._thingCWDth && this._thingCWDth.cancelTrajectoryHistory();
    delete this.thingTrajectoryHistoryCW;
    delete this.thingTrajectoryHistory;
    delete this._thingCWDth;
    storeCore().mapHistorySearch.isDisplayTrajectoryHistory = false;
  }

  updateRiskArea(data) {
    this.qt.thingRiskArea.updateFromData(data);
  }

  // 场景模拟
  mock() {
    // 模拟风险区域
    this.mockRiskArea();
    // 模拟摄像头
    this.mockCamera();
    // 模拟传感器
    this.mockSensor();
    // 模拟车辆
    this.mockCar();
    // 模拟人员
    delay().then(this.mockWorker.bind(this));
  }

  // 模拟人员
  mockWorker() {
    const mockData = [
      {
        id: 1,
        name: "Worker 1",
        positions: DataMockTrajectory.map(v => [v[0] + 0.00001, v[1] + 0.0001])
      }
    ];
    const max = Math.max(...mockData.map(v => v.positions.length));
    this.indexMockWorker = 0;
    this.inter.mockWorker = setInterval(() => {
      const data = [];
      for (let i = 0; i < mockData.length; i++) {
        const id = mockData[i].id;
        const name = mockData[i].name;
        const positions = mockData[i].positions;
        positions[this.indexMockWorker] && data.push({
          id,
          name,
          position: positions[this.indexMockWorker]
        });
      }
      this.updateWorkerFromData(data);
      this.indexMockWorker++;
      if (this.indexMockWorker >= max) clearInterval(this.inter.mockWorker);
    }, 3e3);
  }

  // 模拟车辆
  mockCar() {
    const mockData = [
      {
        id: 1,
        name: "Car 1",
        positions: DataMockTrajectory
      }
    ];
    const max = Math.max(...mockData.map(v => v.positions.length));
    this.indexMockCar = 0;
    this.inter.mockCar = setInterval(() => {
      const data = [];
      for (let i = 0; i < mockData.length; i++) {
        const id = mockData[i].id;
        const name = mockData[i].name;
        const positions = mockData[i].positions;
        positions[this.indexMockCar] && data.push({
          id,
          name,
          position: positions[this.indexMockCar]
        });
      }
      this.updateCarFromData(data);
      this.indexMockCar++;
      if (this.indexMockCar >= max) clearInterval(this.inter.mockCar);
    }, 3e3);
  }

  // 模拟传感器
  mockSensor() {
    this.updateSensorFromData([
      {
        id: "sensor-" + uuid().substring(6),
        name: "Sensor 1",
        position: [125.040608921928, 46.583347974660796],
        elevation: 3
      },
      {
        id: "sensor-" + uuid().substring(6),
        name: "Sensor 2",
        position: [125.03875543942591, 46.58290029010827],
        elevation: 3
      },
      {
        id: "sensor-" + uuid().substring(6),
        name: "Sensor 3",
        position: [125.01875543942511, 46.58290029010897],
        elevation: 6
      }
    ]);
  }

  // 模拟摄像头
  mockCamera() {
    this.updateCameraFromData([
      {
        id: "camera-" + uuid().substring(6),
        name: "Camera 1",
        position: [125.04024333946414, 46.58358181609939],
        elevation: 42
      },
      {
        id: "camera-" + uuid().substring(6),
        name: "Camera 2",
        position: [125.03948355466143, 46.58433070726409],
        elevation: 8
      }
    ]);
  }

  // 模拟风险区域
  mockRiskArea() {
    this.updateRiskArea([
      {
        "name": "限速管控区",
        "idType": "2",
        "color": "#5876f8",
        "path": [[125.03535568964362, 46.58532735774574], [125.03560626760142, 46.58545874516047], [125.03588018866374, 46.58525737147844], [125.03575908107965, 46.58501734309976]],
        "id": "ra-f8-2c3a-4bb2-b45d-67f0e47ba4b0"
      },
      {
        "name": "避难所",
        "idType": "1",
        "color": "#ffff00",
        "path": [[125.04066713987243, 46.58280017047028], [125.0418197311545, 46.582539079275804], [125.04165654262715, 46.58219591432348], [125.04051171523581, 46.58245511324711], [125.04052681763888, 46.58248437982805]],
        "id": "ra-18-111f-419f-a19e-dad6b5e0d013"
      },
      {
        "name": "高风险区域",
        "idType": "3",
        "color": "#ff2020",
        "path": [[125.0398044609553, 46.58411984543123], [125.04103159344554, 46.583888849364534], [125.04065416890941, 46.5834006174583], [125.04041528267915, 46.58303543530316], [125.03970335119268, 46.583512040649666], [125.03952233394361, 46.58375181077847], [125.03884548598349, 46.58387547699256], [125.03888538860846, 46.584067351681476], [125.03954627222886, 46.58403061455127]],
        "id": "ra-72-0c3a-4658-9d49-f3850e45b95a"
      },
      {
        "name": "严重危险作业区",
        "idType": "3",
        "color": "#ff2020",
        "path": [[125.04207425510745, 46.58319047665748], [125.04273527749015, 46.583086809904245], [125.04331194610117, 46.58291160128086], [125.0431188410088, 46.58245252553401], [125.04213115736478, 46.582633146290625], [125.04220001650003, 46.582855074779204], [125.0416071096099, 46.582979968643755], [125.04101868801341, 46.58309309160856], [125.04121703211295, 46.58325412762347], [125.04173744518143, 46.583113871706814], [125.04213223242236, 46.58305815098112], [125.04195953034117, 46.58314229413672], [125.04191537993992, 46.583218659708784], [125.04208368154572, 46.583627356821424]],
        "id": "ra-e3-c0b3-406a-a921-115d63cdb586"
      },
      {
        "name": "严重危险作业区-2",
        "idType": "3",
        "color": "#ff2020",
        "path": [[125.03982865640612, 46.58314395186561], [125.04080622321065, 46.58294377268481], [125.04071899635407, 46.58283131923209], [125.03935611709444, 46.583097304200884]],
        "id": "ra-4d-bf8f-46bf-a4aa-aac0d222ba16"
      }
    ]);
  }

  destroy(force = false, enableCr = true) {
    this.clearTrajectoryHistory();
    return super.destroy(force, enableCr);
  }
}
