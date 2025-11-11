import Vue from "vue";
import { cloneDeepByJSON, mergeDeep } from "@vci/helper/src/object";
import { getElementMaxZIndex } from "@vci/helper/src/element";
import VciLoading from "./VciLoading.vue";
import PopupQuick from "../popup/PopupQuick/PopupQuick.vue";

const PropsLayer = {
  maskStyle: {
    backgroundColor: "rgba(0,0,0,0.01)"
  },
  animation: "zoom-out"
};
const ConfigVpLoading = {
  component: VciLoading,
  propsComponent: null
};
const VpLoading = Vue.extend({
  functional: true,
  props: {
    component: {
      type: Object,
      default: null
    },
    propsComponent: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      visible: false,
      loadAmount: 0,
      isOpening: false
    };
  },
  methods: {
    open() {
      this.visible = true;
      this.isOpening = true;
      this.loadAmount++;
    },
    close(force = false) {
      this.loadAmount--;
      if (force) this.loadAmount = 0;
      if (this.loadAmount < 1) {
        this.loadAmount = 0;
        this.$children[0] && this.$children[0].close();
      }
    }
  },
  render(h) {
    return h(
      PopupQuick,
      {
        props: {
          visible: this.visible,
          maskStyle: PropsLayer.maskStyle,
          animation: PropsLayer.animation
        },
        scopedSlots: {
          default: () => {
            const component = this.component ? this.component : (ConfigVpLoading.component || VciLoading);
            return h(
              component,
              { props: mergeDeep(cloneDeepByJSON(ConfigVpLoading.propsComponent), this.propsComponent) }
            );
          }
        },
        on: {
          afterOpen: () => this.isOpening = false,
          afterClose: () => {
            this.visible = false;
            // setTimeout(() => this.isOpening && (this.visible = true));
          }
        }
      }
    );
  }
});
const vl = {
  vm: null
};
function open(propsComponent, component) {
  if (!vl.vm) {
    vl.vm = new VpLoading();
    vl.vm.$mount();
    document.querySelector("body").appendChild(vl.vm.$el);
  }
  vl.vm.component = component;
  vl.vm.propsComponent = propsComponent;
  vl.vm.zIndex = getElementMaxZIndex(vl.vm.$el.parentNode);
  vl.vm.open();
}
function close(force = false) {
  vl.vm && vl.vm.close(force);
}
const PluginLoading = {
  open,
  close,
  install: (Vue, option = {}) => {
    const { component, propsComponent, propsLayer } = option;
    // 安装插件时配置默认的渲染组件及组件的props
    component && (ConfigVpLoading.component = component);
    propsComponent && (ConfigVpLoading.propsComponent = propsComponent);
    // 定义加载弹框默认属性
    mergeDeep(PropsLayer, propsLayer);
    // 暴露加赞函数
    Vue.prototype.$loading = PluginLoading;
  }
};
export { PluginLoading };
