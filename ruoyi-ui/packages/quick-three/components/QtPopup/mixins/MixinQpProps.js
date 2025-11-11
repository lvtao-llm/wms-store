export default {
  props: {
    extData: {
      type: Object,
      default: () => ({})
    },
    left: {
      type: [Number, String],
      default: 0
    },
    top: {
      type: [Number, String],
      default: 0
    },
    zIndex: {
      type: Number,
      default: 1
    },
    isOverRight: {
      type: Boolean,
      default: false
    }
  }
};
