import MixinClearScheduledTasks from "./MixinClearScheduledTasks";
import { uuid } from "@vci/helper/src/string";

export default {
  mixins: [MixinClearScheduledTasks],
  data() {
    return {
      aborts: {},
      inter: {},
      pools: {}
    };
  },
  methods: {
    async refresh(handle, delay = 5e3, key = "getData") {
      clearTimeout(this.inter[key]);
      if (!this.pools[key]) this.pools[key] = [];
      const idRefresh = uuid();
      this.pools[key].push(idRefresh);
      if (this.aborts[key] === undefined) this.aborts[key] = false;
      if (this["_isDestroyed"]) return Promise.reject("vm is destroyed");
      try {
        await handle.bind(this)();
      } catch (e) {
        console.error("error on refresh: ", e);
      }
      if (idRefresh !== this.pools[key][this.pools[key].length - 1] && this.pools[key].length > 1) {
        return Promise.reject(`refresh be killed by next`);
      }
      if (this.aborts[key]) {
        delete this.aborts[key];
        return Promise.reject(`refresh-${key} is aborts`);
      }
      delete this.aborts[key];
      if (this.pools[key].length > 20) this.pools[key].splice(0, 5);
      this.inter[key] = setTimeout(() => this.refresh(...arguments), delay);
      return this.inter[key];
    },
    abort(key = "getData") {
      clearTimeout(this.inter[key]);
      if (this.aborts[key] !== undefined) this.aborts[key] = true;
    }
  }
};
