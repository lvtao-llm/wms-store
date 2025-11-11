import Vue from "vue";
import PopupDrag from "./PopupDrag.vue";
import { mergeForRenderFn } from "../../../helper/vue.component";
import { getElementBody } from "@vci/helper/src/element";

const PopupDragExt = Vue.extend({
  data() {
    return {
      props: {
        visible: false,
        zIndex: null,
        left: 0,
        top: 0,
        toBody: false,
        animation: ""
      },
      on: {
        beforeOpen: null,
        afterOpen: null,
        beforeClose: null,
        close: null
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
      close: vm => {
        this.on.close && this.on.close(vm);
        this.close();
      }
    };
    if (this.on.beforeOpen) on.beforeOpen = this.on.beforeOpen;
    if (this.on.afterOpen) on.afterOpen = this.on.afterOpen;
    if (this.on.beforeClose) on.beforeClose = this.on.beforeClose;
    return h(
      PopupDrag,
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
  const popupDrag = new PopupDragExt();
  popupDrag.$mount();
  getElementBody().appendChild(popupDrag.$el);
  popupDrag.open(option || {});
  return popupDrag;
}
function close(popupDrag) {
  popupDrag && popupDrag.close();
}
const PluginPopupDrag = {
  open,
  close,
  install: Vue => {
    Vue.prototype.$popupDrag = { open, close };
  }
};
export {
  PluginPopupDrag
};
