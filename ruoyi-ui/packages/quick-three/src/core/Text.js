import Graph from "./Graph";
import { mergeDeep } from "@vci/helper/src/object";

export default class Text extends Graph {
  constructor(option) {
    super(mergeDeep({
      width: 88,
      height: 14,
      text: null,
      textSize: 12
    }, option));
  }

  init() {
    super.init();
    this.isText = true;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.drawGraph();
  }

  update(option) {
    super.update(option);
    const { drawer } = this;
    const { text, textSize, width, height } = this.option;
    // 计算文字宽度
    if (!text) this.setSize(width, height);
    else {
      drawer.ctx.font = `normal normal bolder ${textSize}px`;
      const widthText = drawer.ctx.measureText(text).width;
      this.setSize(Math.min(widthText, width), height);
    }
    this.drawGraph();
    return this;
  }

  drawGraph() {
    const { text } = this.option;
    const { drawer } = this;
    drawer.render(ctx => {
      if (text) {
        ctx.strokeStyle = "#333";
        ctx.textBaseline = "top";
        ctx.textAlign = "center";
        ctx.strokeText(text, drawer.width / 2, 4);
        ctx.fillStyle = "#fff";
        ctx.fillText(text, drawer.width / 2, 4);
      }
    });
    this.updateMaterial();
  }
}
