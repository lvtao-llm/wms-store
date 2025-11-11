import { getElementMaxZIndex } from "@vci/helper/src/element";

/**
 * 置顶地图覆盖物
 * @param marker    覆盖物实例
 */
export function toppingMarker(marker) {
  if (marker) {
    const map = marker.getMap();
    marker.setOptions({ zIndex: getElementMaxZIndex(map.getContainer().querySelector(".amap-markers")) });
  }
}