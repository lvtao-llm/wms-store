/**
 * 销毁|释放覆盖物
 * @param overlayGroup  覆盖物组
 * @param map           地图实例
 */
function disposeOverlayGroup(overlayGroup, map) {
  if (!overlayGroup) return false;
  function handle(overlays) {
    overlays.forEach(ol => {
      if (ol["_overlays"]) handle(ol["_overlays"]);
      ol.destroy && ol.destroy();
    });
  }
  map.remove(overlayGroup);
  handle(overlayGroup.getOverlays());
  overlayGroup.clearOverlays();
}
export {
  disposeOverlayGroup
};