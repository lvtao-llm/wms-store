import Trajectory from "./Trajectory";

export default class TrajectoryRealtime extends Trajectory {
  init() {
    super.init();
    this.isTrajectoryRealtime = true;
  }

  updateTrajectory(path) {
    super.updateTrajectory(path.map(v => [v[0], v[1] + .1, v[2]]));
  }
}