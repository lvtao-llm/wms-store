import VciTreeItem from "./VciTreeItem.vue";
import { isEmpty } from "@vci/helper/src/other";
import Vue from "vue";
import { uuid } from "@vci/helper/src/string";

function scopeSlotChildren(h, context) {
  return function (props) {
    const parent = props.item;
    if (props.children && props.children.length > 0) return h(
      "div",
      { class: "VciTreeChildren" },
      props.children.map(item => {
        return h(
          VciTreeItem,
          {
            props: {
              item,
              parent,
              enableSelection: context.props.enableSelection,
              enableSelectionItem: context.props.enableSelectionItem
            },
            on: context.listeners,
            scopedSlots: {
              ...context.scopedSlots,
              children: scopeSlotChildren(h, context)
            }
          }
        );
      })
    );
  };
}
function formatList(list, parent = null) {
  list.forEach(item => {
    item.$parent = parent;
    if (isEmpty(item.id)) Vue.set(item, "id", uuid());
    if (isEmpty(item.checked)) Vue.set(item, "checked", true);
    if (isEmpty(item.folding)) Vue.set(item, "folding", false);
    item.children && item.children.length > 0 && formatList(item.children, item);
  });
}
function handleChangeCheckedUp(item) {
  const parent = item.$parent;
  if (parent) {
    const children = parent.children;
    parent.checked = children.filter(item => item.checked).length > 0;
    handleChangeCheckedUp(parent);
  }
}
function handleChangeCheckedDown(item, checked) {
  const children = item.children;
  children && children.length > 0 && children.forEach(child => {
    child.checked = checked;
    handleChangeCheckedDown(child, checked);
  });
}
export { scopeSlotChildren };
export default {
  functional: true,
  props: {
    list: {
      type: Array,
      default: () => []
    },
    enableSelection: {
      type: Boolean,
      default: false
    },
    enableSelectionItem: {
      type: Boolean,
      default: false
    }
  },
  render(h, context) {
    formatList(context.props.list);
    if (context.listeners["change-checked"]) {
      const listener = context.listeners["change-checked"];
      context.listeners["change-checked"] = function (e) {
        handleChangeCheckedUp(e.item);
        handleChangeCheckedDown(e.item, e.checked);
        Array.isArray(listener) ? listener.forEach(l => l(e)) : listener(e);
      };
    }
    return h(
      "div",
      { class: "VciTree" },
      context.props.list.map(item => {
        return h(
          VciTreeItem,
          {
            props: {
              item,
              parent: null,
              enableSelection: context.props.enableSelection,
              enableSelectionItem: context.props.enableSelectionItem
            },
            on: context.listeners,
            scopedSlots: {
              ...context.scopedSlots,
              children: scopeSlotChildren(h, context)
            }
          }
        );
      })
    );
  }
};