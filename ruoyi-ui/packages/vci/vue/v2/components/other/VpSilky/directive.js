import Vue from "vue";
import { isEmpty } from "@vci/helper/src/other";
import { uuid } from "@vci/helper/src/string";
import VciSilky from "../VciSilky/VciSilky.vue";
import { instantiateComponentVm } from "../../../helper/vue.component";

/**
 * 处理组件状态
 * @param el
 * @param binding
 * @param vNode
 * @param vm
 */
function handleState(el, binding, vNode, vm) {
  vm.state = binding.value;
  vm.state.toString() === "2" && (vm.completed = true);
  vm.$nextTick(() => {
    const loadingSlot = vNode.context.$refs.loading;
    const errorSlot = vNode.context.$refs.error;
    // 将主要内容添加到silky的view中
    vm.$refs.view && vNode.children.forEach(vn => {
      if (vn.elm.parentNode !== vm.$refs.view) {
        if (vn.data) !["error", "loading"].includes(vn.data.ref) && vm.$refs.view.appendChild(vn.elm);
        else vm.$refs.view.appendChild(vn.elm);
      }
    });
    // 注入自定义加载插槽
    if (vm.$refs.loading && !isEmpty(loadingSlot)) {
      vNode.context["elLoadingOld"] = vm.$refs.loading.innerHTML;
      vm.$refs.loading.innerHTML = "";
      vm.$refs.loading.appendChild(!loadingSlot["_isVue"] ? loadingSlot : loadingSlot.$el);
    }
    // 注入自定义异常插槽
    if (vm.$refs.error && !isEmpty(errorSlot)) {
      vNode.context["elErrorOld"] = vm.$refs.error.innerHTML;
      vm.$refs.error.innerHTML = "";
      vm.$refs.error.appendChild(errorSlot.nodeType === 1 ? errorSlot : errorSlot.$el);
    }
  });
}
function DirectiveSilky() {
  Vue.directive("silky", {
    bind: function (el, binding, vNode) {
      if (!vNode.context["silkies"]) vNode.context["silkies"] = [];
      const vmSilky = instantiateComponentVm(VciSilky, {
        propsData: {
          state: 1,
          loadingAnyway: !!binding.modifiers["loading-anyway"],
          propsLoading: {
            color: "#fff",
            textColor: "#fff"
          },
          enableErrorTip: !!binding.modifiers["enable-error-tip"]
        }
      });
      vmSilky.silkyId = uuid();
      vNode.context["silkies"].push(vmSilky);
      el.setAttribute("data-silky-id", vmSilky.silkyId);
      el.appendChild(vmSilky.$el);
      // 隐藏原有node
      vNode.children.forEach(vNode => {
        const elVn = vNode.elm;
        elVn.parentNode.removeChild(elVn);
      });
      // 处理组件状态
      handleState(el, binding, vNode, vmSilky);
    },
    componentUpdated: function (el, binding, vNode) {
      vNode.context["silkies"].forEach(vm => el.getAttribute("data-silky-id") === vm.silkyId && handleState(el, binding, vNode, vm));
    },
    unbind: function (el, binding, vNode) {
      vNode.context["silkies"].forEach(vdS => {
        if (el.getAttribute("data-silky-id") === vdS.id) {
          const loadingSlot = vNode.context.$refs.loading;
          const errorSlot = vNode.context.$refs.error;
          if (!isEmpty(loadingSlot)) {
            vdS.vm.$refs.loading.removeChild(!loadingSlot["_isVue"] ? loadingSlot : loadingSlot.$el);
            loadingSlot.nodeType !== 1 && loadingSlot.$destroy && loadingSlot.$destroy();
            vdS.vm.$refs.loading.innerHTML = vNode.context["elLoadingOld"];
          }
          if (!isEmpty(errorSlot)) {
            vdS.vm.$refs.error.removeChild(errorSlot.nodeType === 1 ? errorSlot : errorSlot.$el);
            errorSlot.nodeType !== 1 && errorSlot.$destroy && errorSlot.$destroy();
            vdS.vm.$refs.error.innerHTML = vNode.context["elErrorOld"];
          }
          return false;
        }
      });
    }
  });
}
export {
  DirectiveSilky
};
