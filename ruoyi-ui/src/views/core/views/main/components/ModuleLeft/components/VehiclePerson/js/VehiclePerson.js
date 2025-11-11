import Chart from "@vci/vci/vue/v2/components/chart/Chart";

export default class VehiclePerson extends Chart {
  initEcOption() {
    super.initEcOption({
      series: {
        type: "pie",
        radius: ["60%", "70%"],
        label: { show: false },
        labelLine: { show: false },
        padAngle: 8,
        data: []
      }
    });
  }

  render(data) {
    super.render(data);
    if (!Object.keys(data).length) return false;
    this.ecOption.series.data = data.map(item => ({
      ...item,
      itemStyle: {
        color: item.color
      }
    }));
    this.ec.setOption(this.ecOption);
  }
}
