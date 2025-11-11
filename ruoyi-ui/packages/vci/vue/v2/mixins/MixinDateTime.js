import moment from "moment";
import MixinClearScheduledTasks from "./MixinClearScheduledTasks";

moment.locale("zh-cn");
export default {
  mixins: [MixinClearScheduledTasks],
  data() {
    return {
      datetime: moment(),
      inter: {
        datetime: -1
      }
    };
  },
  created() {
    clearInterval(this.inter.datetime);
    this.inter.datetime = setInterval(() => this.datetime = moment(), 1e3);
  }
};