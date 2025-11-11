import { clearScheduledTasks } from "@vci/helper/src/browser";

export default {
  data() {
    return {
      inter: {}
    };
  },
  created() {
    this.$once("hook:deactivated", this._clearScheduledTasks.bind(this));
    this.$once("hook:beforeDestroy", this._clearScheduledTasks.bind(this));
  },
  methods: {
    _clearScheduledTasks() {
      clearScheduledTasks(this.inter);
    }
  }
};