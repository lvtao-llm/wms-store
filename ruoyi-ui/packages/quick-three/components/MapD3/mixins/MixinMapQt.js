const MixinMapQt = {
  qt: null
};
export { MixinMapQt };
export default {
  computed: {
    qt() {
      return MixinMapQt.qt;
    }
  },
  methods: {
    setQt(qt) {
      MixinMapQt.qt = qt;
    }
  }
};
