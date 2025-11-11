import "@vci/style";
import App from "./App";
// import { adapt } from "../../other/adapt";
import VciLoading from "./components/other/VciLoading.vue";
import { DirectiveSilky } from "./components/other/VpSilky/directive";
import { PluginLoading } from "./components/other/VpLoading";
import { PluginNotification } from "./components/other/VpNotification";
import VciNotification from "./components/other/VciNotification.vue";
import FilterIcu from "./filters/filter.icu";
import { PluginPopupQuick } from "./components/popup/PopupQuick";
import { PluginPopupDrag } from "./components/popup/PopupDrag";

/**
 * 初始化Vue
 * @param Vue         Vue类
 * @param option      Vue初始化配置
 * @param pkg         package.json
 * @return vm
 */
export default function VciInit(Vue, option = {},) {
  //pkg = {}
  // 常用过滤器
  Vue.use(FilterIcu);
  // 加载业务逻辑
  DirectiveSilky();
  // 弹框插件
  Vue.use(PluginPopupQuick);
  // 弹框插件(可拖拽)
  Vue.use(PluginPopupDrag);
  // 加载状态
  Vue.use(PluginLoading, { component: VciLoading });
  // 通知
  Vue.use(PluginNotification, { component: VciNotification });
  // 实例化Vue
  Vue.config.productionTip = false;
  const vm = new Vue({
    render: h => h(App),
    ...option
  }).$mount("#app");
  // 屏幕适配
  // adapt(pkg, vm);
  return vm;
}
