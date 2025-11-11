import Vue from "vue";
import VpPopupQuick from "./PopupQuick.vue";
import { hexToRgba } from "@vci/helper/src/color";
import { mergeForRenderFn } from "../../../helper/vue.component";
import { getElementBody } from "@vci/helper/src/element";

const PopupQuickExt = Vue.extend({
  data() {
    return {
      props: {
        visible: false,
        enableMask: true,
        enableMaskClose: false,
        enableLockScroll: true,
        zIndex: null,
        maskStyle: {
          backgroundColor: hexToRgba("#000", 0.6),
          backdropFilter: "blur(6px)"
        }
      },
      on: {
        beforeOpen: null,
        afterOpen: null,
        beforeClose: null,
        afterClose: null
      },
      component: null,
      propsComponent: {}
    };
  },
  methods: {
    open(option = {}) {
      if (option.props) this.props = mergeForRenderFn(this.props, option.props);
      if (option.on) this.on = mergeForRenderFn(this.on, option.on);
      if (option.component) this.component = option.component;
      if (option.propsComponent) this.propsComponent = mergeForRenderFn(this.propsComponent, option.propsComponent);
      this.props.visible = true;
    },
    close() {
      this.props.visible = false;
      getElementBody().removeChild(this.$el);
      this.$destroy();
    },
    closeAmt() {
      if (this._isClosing) return false;
      this._isClosing = true;
      const targetVm = this.$children[0];
      if (!targetVm) return false;
      targetVm["close"]();
    }
  },
  render(h) {
    const on = {
      afterClose: vm => {
        this.on.afterClose && this.on.afterClose(vm);
        this.close();
      }
    };
    if (this.on.beforeOpen) on.beforeOpen = this.on.beforeOpen;
    if (this.on.afterOpen) on.afterOpen = this.on.afterOpen;
    if (this.on.beforeClose) on.beforeClose = this.on.beforeClose;
    return h(
      VpPopupQuick,
      {
        props: this.props,
        on,
        scopedSlots: {
          default: props => {
            const component = this.component || "div";
            return h(
              component,
              {
                props: {
                  ...this.propsComponent,
                  ...props
                }
              }
            );
          }
        }
      }
    );
  }
});
function open(option) {
  const popupQuick = new PopupQuickExt();
  popupQuick.$mount();
  getElementBody().appendChild(popupQuick.$el);
  popupQuick.open(option || {});
  return popupQuick;
}
function close(popupQuick) {
  popupQuick && popupQuick.close();
}
const PluginPopupQuick = {
  open,
  close,
  install: Vue => {
    Vue.prototype.$popupQuick = { open, close };
  }
};
export {
  PluginPopupQuick
};
