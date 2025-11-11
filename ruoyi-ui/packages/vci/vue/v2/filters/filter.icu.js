import { isEmpty } from "@vci/helper/src/other";

export default {
  install: Vue => {
    // empty
    Vue.filter("ept", value => {
      if (isEmpty(value)) return "-";
      return value;
    });
    // empty - number
    Vue.filter("eptNum", value => {
      if (isEmpty(value) || isNaN(Number(value))) return "-";
      return value;
    });
  }
};
