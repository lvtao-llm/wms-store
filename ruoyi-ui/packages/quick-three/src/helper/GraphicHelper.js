import Drawer from "@vci/quick-drawer";
import { hexToRgba } from "@vci/helper/src/color";
import { Vector2 } from "three";

/**
 * 矩形边缘光晕
 * @param width       宽度
 * @param height      高度
 * @param color       颜色
 * @returns {Drawer}
 */
function rectangularEdgeHalo(width, height, color) {
  let drawer = new Drawer({
    dpr: 2,
    width,
    height
  });
  let edgeHaloSize = 50;
  drawer.render(ctx => {
    drawer.clear();
    const addColorStop = g => {
      g.addColorStop(0, color);
      g.addColorStop(0.2, hexToRgba(color, 0));
      g.addColorStop(1, hexToRgba(color, 0));
      return g;
    };
    // 上
    let lg = ctx.createLinearGradient(drawer.width / 2, edgeHaloSize, drawer.width / 2, 0);
    ctx.fillStyle = addColorStop(lg);
    ctx.fillRect(edgeHaloSize, 0, drawer.width - edgeHaloSize * 2, edgeHaloSize);
    // 下
    lg = ctx.createLinearGradient(drawer.width / 2, drawer.height - edgeHaloSize, drawer.width / 2, drawer.height);
    ctx.fillStyle = addColorStop(lg);
    ctx.fillRect(edgeHaloSize, drawer.height - edgeHaloSize, drawer.width - edgeHaloSize * 2, edgeHaloSize);
    // 左
    lg = ctx.createLinearGradient(edgeHaloSize, drawer.height * 0.5, 0, drawer.height * 0.5);
    ctx.fillStyle = addColorStop(lg);
    ctx.fillRect(0, edgeHaloSize, edgeHaloSize, drawer.height - edgeHaloSize * 2);
    // 右
    lg = ctx.createLinearGradient(drawer.width - edgeHaloSize, drawer.height * 0.5, drawer.width, drawer.height * 0.5);
    ctx.fillStyle = addColorStop(lg);
    ctx.fillRect(drawer.width - edgeHaloSize, edgeHaloSize, edgeHaloSize, drawer.height - edgeHaloSize * 2);
    // 左上
    let rg = ctx.createRadialGradient(edgeHaloSize, edgeHaloSize, 0, edgeHaloSize, edgeHaloSize, edgeHaloSize);
    ctx.fillStyle = addColorStop(rg);
    ctx.fillRect(0, 0, edgeHaloSize, edgeHaloSize);
    // 右上
    rg = ctx.createRadialGradient(drawer.width - edgeHaloSize, edgeHaloSize, 0, drawer.width - edgeHaloSize, edgeHaloSize, edgeHaloSize);
    ctx.fillStyle = addColorStop(rg);
    ctx.fillRect(drawer.width - edgeHaloSize, 0, edgeHaloSize, edgeHaloSize);
    // 右下
    rg = ctx.createRadialGradient(drawer.width - edgeHaloSize, drawer.height - edgeHaloSize, 0, drawer.width - edgeHaloSize, drawer.height - edgeHaloSize, edgeHaloSize);
    ctx.fillStyle = addColorStop(rg);
    ctx.fillRect(drawer.width - edgeHaloSize, drawer.height - edgeHaloSize, edgeHaloSize, edgeHaloSize);
    // 左下
    rg = ctx.createRadialGradient(edgeHaloSize, drawer.height - edgeHaloSize, 0, edgeHaloSize, drawer.height - edgeHaloSize, edgeHaloSize);
    ctx.fillStyle = addColorStop(rg);
    ctx.fillRect(0, drawer.height - edgeHaloSize, edgeHaloSize, edgeHaloSize);
  });
  return drawer;
}
/**
 * 获取包围盒 通过路径数组
 * @param path [...[x,y]]
 * @returns {{width: number, height: number}}
 */
function getBoundingFromPath(path = []) {
  const bounding = {};
  const max = new Vector2(
    Math.max(...path.map(v => v[0])),
    Math.max(...path.map(v => v[1]))
  );
  const min = new Vector2(
    Math.min(...path.map(v => v[0])),
    Math.min(...path.map(v => v[1]))
  );
  const diff = new Vector2().subVectors(max, min);
  bounding.min = min;
  bounding.max = max;
  bounding.center = new Vector2().addVectors(min, new Vector2().copy(diff).multiplyScalar(.5));
  bounding.width = diff.x;
  bounding.height = diff.y;
  bounding.toArray = () => [diff.x, diff.y];
  return bounding;
}
export {
  rectangularEdgeHalo,
  getBoundingFromPath
};
