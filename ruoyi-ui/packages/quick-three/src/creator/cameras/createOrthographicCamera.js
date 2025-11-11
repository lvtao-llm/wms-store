import { mergeDeep } from "@vci/helper/src/object";
import { OrthographicCamera } from "three";

function createOrthographicCamera(option) {
  const { qt, name, position, left, right, top, bottom, near, far } = mergeDeep(
    {
      qt: null,
      name: null,
      position: [0, 100, 0],
      left: -100,
      right: 100,
      top: 100,
      bottom: -100,
      near: 0.001,
      far: 1e3
    },
    option
  );
  const camera = new OrthographicCamera(left, right, top, bottom, near, far);
  camera.position.fromArray(position);
  camera.lookAt(0, 0, 0);
  if (qt) {
    qt.cameras.set(camera.uuid, camera);
    if (qt.debug && qt.gui) {
      const key = `camera@${camera.uuid}`;
      const gui = qt.gui.guis[key] = qt.gui.guis.camera.addFolder(name || key).close();
      gui.add(camera, "visible").listen();
      gui.add(camera, "near").onChange(() => camera.updateProjectionMatrix()).listen();
      gui.add(camera, "far").onChange(() => camera.updateProjectionMatrix()).listen();
    }
  }
  return camera;
}
export { createOrthographicCamera };
