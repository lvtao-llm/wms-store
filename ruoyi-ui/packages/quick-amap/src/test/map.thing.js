/**
 * 地图事物测试
 * @param mapThing
 */
import MapThing from "../core/MapThing";
import { log } from "../helper/log";

function testMapThing(mapThing, updateOption) {
  if (!(mapThing instanceof MapThing)) throw new Error(log("test: mapThing不是MapThing的实例化对象"));
  setTimeout(() => {
    mapThing.update(updateOption);
    setTimeout(() => {
      mapThing.hide();
      setTimeout(() => {
        mapThing.show();
        setTimeout(() => {
          mapThing.destroy();
          console.info("isDestroyed", mapThing);
        }, 1000);
      }, 1000);
    }, 1000);
  }, 1000);
}
export {
  testMapThing
};