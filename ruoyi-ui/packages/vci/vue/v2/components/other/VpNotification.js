import Vue from "vue";
import VciNotification from "./VciNotification.vue";
import { mergeDeep } from "@vci/helper/src/object";
import { addCss, getElementBody, getElementMaxZIndex } from "@vci/helper/src/element";

const vms = [];
const ConfigVpNotification = {
  component: VciNotification,
  propsComponent: null,
  style: {
    bottom: 32,
    right: 20,
    margin: 20
  }
};
const VpNotification = Vue.extend({
  functional: true,
  props: {
    duration: {
      type: Number,
      default: 2e3
    },
    alwaysPresent: {
      type: Boolean,
      default: false
    },
    transitionDuration: {
      type: Number,
      default: 350
    },
    transitionTimingFunction: {
      type: String,
      default: "cubic-bezier(0.65, 0.05, 0.36, 1)"
    },
    component: {
      type: Object,
      default: null
    },
    title: {
      type: String,
      default: "提示"
    },
    message: {
      type: String,
      default: "来自勤奋的vci"
    },
    theme: {
      type: String,
      default: "#000"
    }
  },
  data() {
    return {};
  },
  methods: {
    open() {
      addCss(this.$el, {
        transform: `translateX(0)`,
        opacity: 1
      });
      !this.alwaysPresent && setTimeout(this.close, this.duration);
    },
    close() {
      addCss(this.$el, {
        opacity: 0,
        transform: "translateX(100%) skewX(15deg)"
      });
      this.isDestroyed = true;
      const indexInVms = vms.findIndex(v => this === v);
      vms.forEach((v, i) => {
        if (v) {
          !this.mark && (this.mark = v);
          const isFirstVm = this.mark === v;
          if (v !== this) {
            if (i > indexInVms) {
              v.bottom -= (this.$el.clientHeight + (isFirstVm ? 0 : ConfigVpNotification.style.margin));
              addCss(v.$el, { bottom: `${v.bottom}px` });
            }
          }
        }
      });
      setTimeout(() => {
        vms.splice(vms.findIndex(v => v === this), 1, null);
        this.$el.parentNode && this.$el.parentNode.removeChild(this.$el);
        this.$destroy();
      }, this.transitionDuration);
    }
  },
  render(h) {
    const component = this.component || ConfigVpNotification.component || "div";
    return h(
      component,
      {
        props: mergeDeep(ConfigVpNotification.propsComponent, this.$props),
        on: { close: this.close }
      }
    );
  }
});
function open(propsData) {
  const vm = new VpNotification({ propsData });
  vm.$mount();
  const style = {
    position: "fixed",
    right: `${ConfigVpNotification.style.right}px`,
    bottom: ConfigVpNotification.style.bottom,
    zIndex: getElementMaxZIndex(),
    transition: `${vm.transitionDuration / 1e3}s ${vm.transitionTimingFunction}`,
    transform: `translateX(100%) skewX(-15deg)`,
    opacity: 0
  };
  vms.forEach(v => v && !v.isDestroyed && (style.bottom += (v.$el.clientHeight + ConfigVpNotification.style.margin)));
  vm.bottom = style.bottom;
  style.bottom += "px";
  addCss(vm.$el, style);
  getElementBody().appendChild(vm.$el);
  setTimeout(() => vm.open(), 50);
  vms.push(vm);
  return vm;
}
const PluginNotification = {
  open,
  install: (Vue, option = {}) => {
    const { component, propsComponent, customStatus } = option;
    // 安装插件时配置默认的渲染组件及组件的props
    component && (ConfigVpNotification.component = component);
    propsComponent && (ConfigVpNotification.propsComponent = propsComponent);
    // 注册插件使用方法
    const status = {
      "success": "#3effb0",
      "warning": "#ffc83e",
      "error": "#ff3e3e",
      "tip": "#000",
      ...(customStatus || {})
    };
    Object.keys(status).forEach(key => PluginNotification[key] = (message, option) => open({
      message,
      theme: status[key],
      ...option
    }));
    Vue.prototype.$notify = PluginNotification;
  }
};
export {
  PluginNotification
};