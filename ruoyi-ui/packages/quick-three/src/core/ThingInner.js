import { mergeDeep } from "@vci/helper/src/object";
import Animator from "../core/Animator";
// 模型内部子模型事物化类
export default class ThingInner extends Animator {
  constructor(option) {
    super(mergeDeep({
      // 功能性配置
      enableEdit: false,
      enableEvent: false,
      enableUpdateMatrix: false,
      enableDisposeO3OnUpdate: false,
      // 功能性配置|销毁
      enableDisposeO3OnDestroy: false,
      enableRemoveO3FromParentOnDestroy: false,
      // 功能性配置|qt.updateThings
      enableRemovedWhenDestroyOnUpdate: false,
      enableAddObjectToParent: false
    }, option));
  }

  init() {
    super.init();
    this.isThingInner = true;
  }
}
