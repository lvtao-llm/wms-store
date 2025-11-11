import { viewport } from "@vci/vci/other/adapt";

const proportionOrigin = viewport.width / viewport.height;
export default {
  data() {
    return {
      scale: 1,
      interEp: null
    };
  },
  mounted() {
    this.adapt(true);
    window.addEventListener("resize", this.adapt);
    this.$once("hook:beforeDestroy", () => {
      window.removeEventListener("resize", this.adapt);
      clearTimeout(this.interEp);
    });
  },
  methods: {
    adapt(immediate = false) {
      clearTimeout(this.interEp);
      // if (immediate) this._adapt();
      // else this.interEp = setTimeout(this._adapt, 100);
    },
    // _adapt() {
    //   const proportion = window.innerWidth / window.innerHeight;
    //   if (proportion < proportionOrigin) this.scale = window.innerWidth / viewport.width;
    //   else this.scale = window.innerHeight / viewport.height;
    // }
  }
};