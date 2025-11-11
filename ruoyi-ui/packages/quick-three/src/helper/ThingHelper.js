import { Box3, Euler, Group, Object3D, Quaternion, Vector2, Vector3 } from "three";
import { mergeDeep } from "@vci/helper/src/object";
import { clone } from "three/examples/jsm/utils/SkeletonUtils";

/**
 * 逆向转换得到物体在浏览器视口得定位
 * @param position [0,0,0]
 * @param camera   场景中使用的摄像机
 * @param element  场景容器元素
 */
const glCoords = new Vector3();
function getScreenCoordsFromWorldCoords(position, camera, element) {
  const { x, y } = glCoords.set(...position).project(camera);
  return {
    left: (x + 1) / 2 * element.clientWidth,
    top: (1 - y) / 2 * element.clientHeight
  };
}
/**
 * 获取居中模型
 * @param o3
 * @param verticalAlign
 * @returns {Group}
 */
export const VerticalAlignCentralization = {
  TOP: "TOP",
  MIDDLE: "MIDDLE",
  BOTTOM: "BOTTOM"
};
const _box3 = new Box3();
const _size = new Vector3();
const _center = new Vector3();
function getCentralizationO3(o3, verticalAlign = VerticalAlignCentralization.MIDDLE) {
  o3.position.set(0, 0, 0);
  _box3.setFromObject(o3);
  _box3.getSize(_size);
  _box3.getCenter(_center);
  const o3Grp = new Group();
  _center.multiplyScalar(-1);
  if (verticalAlign === VerticalAlignCentralization.TOP) _center.y += -_size.y / 2;
  else if (verticalAlign === VerticalAlignCentralization.BOTTOM) _center.y += _size.y / 2;
  o3.position.copy(_center);
  o3Grp.add(o3);
  return o3Grp;
}
/**
 * 获取模型中心点
 * @param o3
 * @param verticalAlign
 * @returns {Vector3}
 */
function getCenterCoordsFromO3(o3, verticalAlign = VerticalAlignCentralization.MIDDLE) {
  const _position = new Vector3().copy(o3.position);
  o3.position.set(0, 0, 0);
  _box3.setFromObject(o3);
  _box3.getSize(_size);
  _box3.getCenter(_center);
  if (verticalAlign === VerticalAlignCentralization.TOP) _center.y += _size.y / 2;
  else if (verticalAlign === VerticalAlignCentralization.BOTTOM) _center.y -= _size.y / 2;
  _center.add(_position);
  o3.position.copy(_position);
  return new Vector3().copy(_center);
}
// 通过屏幕坐标获取三维场景世界坐标
const vector2Raycaster = new Vector2();
function getWorldCoordsFromScreen(raycaster, camera, screenCoords, d3Width, d3Height, o3, isObjectResult = false) {
  screenCoords = {
    x: (screenCoords[0] / d3Width) * 2 - 1,
    y: -(screenCoords[1] / d3Height) * 2 + 1
  };
  vector2Raycaster.set(screenCoords.x, screenCoords.y);
  raycaster.setFromCamera(vector2Raycaster, camera);
  const intersects = raycaster.intersectObjects([o3]);
  if (intersects[0]) {
    return isObjectResult ? intersects[0].point : intersects[0].point.toArray();
  } else {
    const worldCoords = new Vector3(screenCoords.x, screenCoords.y, 0.5).unproject(camera);
    const cameraPosition = camera.getWorldPosition(new Vector3());
    const directionV3 = worldCoords.clone().sub(cameraPosition).normalize();
    const coords = cameraPosition.clone().add(directionV3.clone().multiplyScalar(100));
    if (isObjectResult) {
      coords.notInO3 = true;
      return coords;
    } else {
      const coordsArray = coords.toArray();
      coordsArray.notInO3 = true;
      return coordsArray;
    }
  }
}
/**
 * 克隆三维对象
 * @param o3  要被克隆的三维对象
 * @param cloneMaterial  是否克隆材质 默认不克隆
 * @returns {*}
 */
function cloneO3(o3, cloneMaterial = false) {
  let hasSkinnedMesh = false;
  o3.traverse(o => o.isSkinnedMesh && (hasSkinnedMesh = true));
  const o3Clone = hasSkinnedMesh ? clone(o3) : o3.clone(true);
  cloneMaterial && cloneO3Materials(o3Clone);
  return o3Clone;
}
function cloneO3Materials(o3) {
  o3.traverse(o => {
    if (o.isMesh) {
      if (Array.isArray(o.material)) o.material = o.material.map(material => material.clone());
      else o.material = o.material.clone();
    }
  });
  return o3;
}
/**
 * 从Object3D中获取观察位置
 * 位置相对世界坐标
 * 目前rotation仅支持旋转过Y轴的模型，其它轴无效
 * 默认观察点在O3z轴正方向
 * @param option
 * @returns {Vector3}
 */
function getWatchPositionFromO3(option) {
  const { o3, distance, offset } = option = mergeDeep({
    o3: new Object3D(),
    distance: 1,
    rotation: new Euler(0, 0, 0),
    offset: new Vector3()
  }, option);
  const position = o3.getWorldPosition(new Vector3());
  const quaternion = o3.getWorldQuaternion(new Quaternion());
  const rotation = new Euler().setFromQuaternion(quaternion);
  // rotation.x += option.rotation.x;
  rotation.y += option.rotation.y;
  // rotation.z += option.rotation.z;
  return new Vector3(
    position.x + Math.sin(rotation.y) * distance,
    position.y,
    position.z + Math.cos(rotation.y) * distance
  ).add(offset);
}
// 释放对象
export {
  getScreenCoordsFromWorldCoords,
  getCentralizationO3,
  getCenterCoordsFromO3,
  getWorldCoordsFromScreen,
  cloneO3,
  cloneO3Materials,
  getWatchPositionFromO3
};
