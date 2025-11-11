import Animator from "@vci/quick-three/src/core/Animator";
import { AnimationClip, Group, InterpolateLinear, VectorKeyframeTrack } from "three";
import { mergeDeep } from "@vci/helper/src/object";
import Trajectory from "./Trajectory";
import { resourceRootAliyun } from "@vci/vci/config/api";

const NameAnimation = "trajectory";
export default class TrajectoryHistory extends Animator {
  constructor(option) {
    super(mergeDeep({
      dataTrajectory: [],
      thingCW: null,
      type: "car"
    }, option));
  }

  init() {
    super.init();
    this.object = new Group();
    this.isTrajectoryHistory = true;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.createTrajectoryHistoriesWithRisk();
    this.createTrajectoryHistory();
  }

  createTrajectoryHistoriesWithRisk() {
    const { qt } = this;
    const { dataTrajectory, type } = this.option;
    this.thingsTrajectory = [];
    // TODO 暂时手动模拟与风险区域有交集的轨迹
    const dataTrajectoryWithRisk = this.dataTrajectoryWithRisk = [
      {
        isRisk: false,
        path: dataTrajectory.slice(0, 4)
      },
      {
        isRisk: true,
        path: dataTrajectory.slice(3, 8)
      },
      {
        isRisk: false,
        path: dataTrajectory.slice(7)
      }
    ];
    dataTrajectoryWithRisk.forEach(dtr => {
      const { isRisk, path } = dtr;
      const color = isRisk ? "red" : (type === "car" ? "green" : "blue");
      const thingTrajectory = new Trajectory({
        qt,
        arrow: { urlTexture: `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/trajectory/arrow.${color}.png` },
        line: { urlTexture: `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/trajectory/line.${color}.png` }
      });
      thingTrajectory.updateTrajectory(path.map(v => v.position.map((v, i) => v + (i === 1 ? .1 : 0))));
      this.thingsTrajectory.push(thingTrajectory);
    });
    return this;
  }

  clearTrajectoryHistoriesWithRisk() {
    this.thingsTrajectory.forEach(thing => thing.fadeOut().then(() => thing.destroy()));
    delete this.thingsTrajectory;
    return this;
  }

  createTrajectoryHistory() {
    const { dataTrajectory, thingCW } = this.option;
    this.object.add(thingCW.object);
    const timeStart = dataTrajectory[0].time;
    const timeEnd = dataTrajectory[dataTrajectory.length - 1].time;
    const duration = (timeEnd - timeStart) / 1e3;
    const tracks = [
      new VectorKeyframeTrack(
        ".position",
        dataTrajectory.map(v => (v.time - timeStart) / 1e3),
        dataTrajectory.map(v => v.position).flat(1),
        InterpolateLinear
      )
    ];
    const clip = new AnimationClip(NameAnimation, duration, tracks);
    this.object.animations = [clip];
    this.bootAnimation();
    this.play({
      name: NameAnimation,
      loop: { times: Infinity }
    });
    this.action = this.getAction(NameAnimation);
    return this;
  }

  clearTrajectoryHistory() {
    const { thingCW } = this.option;
    thingCW.parentObject.add(thingCW.object);
  }

  animate(delta) {
    super.animate(delta);
    if (this.action && !this.action.paused) {
      const { dataTrajectory } = this.option;
      this.dispatchEvent("playing", {
        startedAt: dataTrajectory[0].time,
        current: this.action.time,
        duration: this.action.getClip().duration
      });
    }
  }

  destroy(force = false) {
    this.clearTrajectoryHistoriesWithRisk();
    this.clearTrajectoryHistory();
    return super.destroy(force);
  }
}