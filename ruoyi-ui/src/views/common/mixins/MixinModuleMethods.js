import { isEnvIjd } from "@vci/vci/env";

export default {
  created() {
    if (isEnvIjd) this.getRealData();
    else this.simulationData();
  }
};
