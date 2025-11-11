import Chart from "./Chart";
import { isEmpty } from "@vci/helper/src/other";

export default class ChartHighLight extends Chart {

  init() {
    super.init();
    this.isChartHighLight = true;
    this.amtHightLight = {
      indexCurrent: 0
    };
  }

  initEc() {
    super.initEc();
    this._pause = this.pause.bind(this);
    this._play = this.play.bind(this);
    this.el.addEventListener("mousemove", this._pause);
    this.el.addEventListener("mouseout", this._play);
    this.ec.on("highlight", e => {
      if (!isEmpty(e.dataIndex)) this.amtHightLight.indexCurrent = e.dataIndex;
      if (e.batch && e.batch.length > 0) this.amtHightLight.indexCurrent = e.batch[0].dataIndex;
      this.dispatchEvent("highlight", { indexCurrent: this.amtHightLight.indexCurrent });
    });
  }

  play() {
    const { indexCurrent } = this.amtHightLight;
    const lengthData = this._data.length;
    this.pause();
    !isEmpty(this._ic) && this.ec.dispatchAction({
      type: "downplay",
      dataIndex: this._ic
    });
    this._ic = Math.min(indexCurrent, lengthData - 1);
    this.ec.dispatchAction({
      type: "highlight",
      dataIndex: this._ic
    });
    this.dispatchEvent("play", { indexCurrent: this._ic });
    if (indexCurrent + 1 >= this._data.length) {
      this.amtHightLight.indexCurrent = 0;
      this.dispatchEvent("playEnd");
    } else this.amtHightLight.indexCurrent++;
    this.inter.highLight = setTimeout(this.play.bind(this), 2e3);
  }

  pause() {
    clearTimeout(this.inter.highLight);
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
