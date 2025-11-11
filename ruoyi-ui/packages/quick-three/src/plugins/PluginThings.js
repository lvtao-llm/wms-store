import { QtPlugin } from "./QtPlugin";
import Thing from "../core/Thing";
import { isEmpty } from "@vci/helper/src/other";

class PluginThings extends QtPlugin {
  static namespace = "pluginThings";

  /**
   * 更新事物
   * @param optionsThing  配置
   * @param fnFilter      限定事物更新的范围
   * @param things        自定义实例池
   */
  updateThings(optionsThing = [], fnFilter = null, things) {

    const { qt } = this;
    const isCustomThingsPool = !isEmpty(things);
    if (!things) things = fnFilter ? qt.things.filter(thing => fnFilter(thing)) : qt.things;
    else things = fnFilter ? things.filter(thing => fnFilter(thing)) : things;
    // 移出配置中已不存在的模型
    const thingsNeedRemove = [];
    things.forEach(thing => {
      if (
        !optionsThing.some(thingOption => thingOption.id === thing.id)
        && thing.option.enableRemovedWhenDestroyOnUpdate
        && !thing.isImmortal
      ) {
        if (
          qt.editor
          && qt.editor.isEdit
          && qt.editor.controlTransform.object
          && qt.editor.controlTransform.object.thing === thing
        ) qt.editor.controlTransform.detach();
        if (isCustomThingsPool) thingsNeedRemove.push(thing);
        else {
          if (thing.option.enableFadeOutWhenDestroyOnUpdate) thing.fadeOut().then(() => thing.destroy());
          else thing.destroy();
        }
      }
    });
    isCustomThingsPool && thingsNeedRemove.forEach(thing => {
      const index = things.indexOf(thing);
      index !== -1 && (things[index] = null);
      if (thing.option.enableFadeOutWhenDestroyOnUpdate) thing.fadeOut().then(() => thing.destroy());
      else thing.destroy();
    });
    isCustomThingsPool && (things = things.filter(thing => thing));
    // 更新或者添加新事物
    optionsThing.forEach(thingOption => {
      const targetThing = things.find(thing => thing.id === thingOption.id);
      if (targetThing) targetThing.update(thingOption);
      else {
        const thing = new (thingOption.creator || Thing)({ ...thingOption, qt });
        isCustomThingsPool && things.push(thing);
      }
    });
    return things;
  }

  // 添加事物
  addThings(optionsThing = []) {
    optionsThing.forEach(thingOption => new (thingOption.creator || Thing)({
      ...thingOption,
      qt: this.qt
    }));
  }
}

export { PluginThings };
