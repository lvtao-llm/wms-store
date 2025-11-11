import Chart from "./Chart";
import { isEmpty } from "@vci/helper/src/other";

export default class ChartTooltip extends Chart {

  init() {
    super.init();
    this.isChartTooltip = true;
    this.amtTooltip = {
      indexCurrent: 0
    };
  }

  initEc() {
    super.initEc();
    this._pause = this.pause.bind(this);
    this._play = this.play.bind(this);
    this.el.addEventListener("mousemove", this._pause);
    this.el.addEventListener("mouseout", this._play);
    this.ec.on("showTip", e => {
      if (!isEmpty(e.dataIndex)) this.amtTooltip.indexCurrent = e.dataIndex;
      if (e.batch && e.batch.length > 0) this.amtTooltip.indexCurrent = e.batch[0].dataIndex;
      this.dispatchEvent("showTip", { indexCurrent: this.amtTooltip.indexCurrent });
    });
  }

  play() {
    const { indexCurrent } = this.amtTooltip;
    const { xAxis } = this.ecOption;
    const lengthData = (Array.isArray(xAxis) ? xAxis[0] : xAxis).data.length;
    this.pause();
    this._ic = Math.min(indexCurrent, lengthData - 1);
    this.ec.dispatchAction({
      type: "showTip",
      seriesIndex: 0,
      dataIndex: this._ic
    });
    this.dispatchEvent("play", { indexCurrent: this._ic });
    if (indexCurrent + 1 >= lengthData) {
      this.amtTooltip.indexCurrent = 0;
      this.dispatchEvent("playEnd");
    } else this.amtTooltip.indexCurrent++;
    this.inter.tooltip = setTimeout(this.play.bind(this), 2e3);
  }

  pause() {
    clearTimeout(this.inter.tooltip);
  }

  stop() {
    this.pause();
    this.ec.dispatchAction({ type: "downplay" });
  }

  destroy() {
    this.el.removeEventListener("mousemove", this._pause);
    this.el.removeEventListener("mouseout", this._play);
    this.stop();
    super.destroy();
  }
}
