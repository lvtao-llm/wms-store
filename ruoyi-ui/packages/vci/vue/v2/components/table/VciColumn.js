export default {
  functional: true,
  name: "VciColumn",
  props: {
    name: {
      type: [String, Number],
      default: null
    },
    prop: {
      type: String,
      default: null
    },
    width: {
      type: Number,
      default: null
    },
    height: {
      type: Number,
      default: null
    },
    justifyContent: {
      type: String,
      default: "center"
    },
    alignItems: {
      type: String,
      default: "center"
    },
    isOrderNumber: {
      type: Boolean,
      default: false
    }
  },
  render(h, context) {
    return h("div", { isVciColumn: true, props: context.props, scopedSlots: context.scopedSlots });
  }
};