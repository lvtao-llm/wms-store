export default {
  props: {
    zh: {
      type: String,
      default: ""
    },
    en: {
      type: String,
      default: ""
    },
    switchTitle: {
      type: Array,
      default: () => ([])
    }
  },
  computed: {
    st() {
      return this.switchTitle.length;
    }
  }
};
