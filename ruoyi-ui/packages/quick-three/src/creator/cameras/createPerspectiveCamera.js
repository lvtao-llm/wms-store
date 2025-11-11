import { mergeDeep } from "@vci/helper/src/object";
import { PerspectiveCamera } from "three";

function createPerspectiveCamera(option) {
  const { qt, name, position, aspect, fov, near, far } = mergeDeep(
    {
      qt: null,
      name: null,
      position: [0, 100, 0],
      aspect: 1,
      fov: 50,
      near: 0.001,
      far: 1e3
    },
    option
  );
  const camera = new PerspectiveCamera(fov, aspect, near, far);
  camera.position.fromArray(position);
  camera.lookAt(0, 0, 0);
  if (qt) {
    qt.cameras.set(camera.uuid, camera);
    if (qt.debug && qt.gui) {
      const key = `camera@${camera.uuid}`;
      const gui = qt.gui.guis[key] = qt.gui.guis.camera.addFolder(name || key).close();
      gui.add(camera, "visible").listen();
      gui.add(camera, "fov").onChange(() => camera.updateProjectionMatrix()).listen();
      gui.add(camera, "near").onChange(() => camera.updateProjectionMatrix()).listen();
      gui.add(camera, "far").onChange(() => camera.updateProjectionMatrix()).listen();
    }
  }
  return camera;
}
export { createPerspectiveCamera };
