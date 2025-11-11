import { now } from "./browser";

/**
 * 动画帧启动
 * @param update // TWEEN.update
 * @param call   // 动画帧回调函数
 */
const fnsAnimate = [];
function animation(update, call) {
  function animate() {
    const delta = now();
    fnsAnimate.forEach(fn => fn(delta));
    call && call(delta);
    update(delta);
    requestAnimationFrame(animate);
  }
  animate();
}
// 动画帧过程函数|添加
function faAdd(fn) {
  fnsAnimate.push(fn);
}
// 动画帧过程函数|移除
function faRemove(fn) {
  fnsAnimate.splice(fnsAnimate.indexOf(fn), 1);
}
// 动画帧过程函数|清空
function faClear() {
  fnsAnimate.length = 0;
}
export {
  animation,
  faAdd,
  faRemove,
  faClear
};