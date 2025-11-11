import ChartTooltip from "@vci/vci/vue/v2/components/chart/ChartTooltip";
import MaterialStatisticsTooltip from "./components/MaterialStatisticsTooltip/MaterialStatisticsTooltip.vue";
import { instantiateComponentVm } from "@vci/vci/vue/v2/helper/vue.component";

export default class MaterialStatistics extends ChartTooltip {
  $vm = instantiateComponentVm(MaterialStatisticsTooltip);

  initEcOption() {
    super.initEcOption({
      legend: {
        top: "10%",
        itemWidth: this.w(12),
        itemHeight: this.w(12),
        itemGap: this.w(64),
        icon: "rect",
        textStyle: {
          color: "#c7e7fd",
          padding: [0, 0, 0, this.w(4)],
          fontSize: this.w(18),
          fontWeight: "bold"
        }
      },
      tooltip: {
        trigger: "axis",
        backgroundColor: "transparent",
        borderColor: "transparent",
        padding: 0,
        formatter: null
      },
      xAxis: [
        {
          type: "category",
          data: [],
          axisLabel: {
            color: "#c7e7fd",
            fontSize: this.w(16),
            interval: 0,
            align: "center" // **确保 X 轴文字和深色柱子对齐**
          },
          axisTick: { show: false } // **去掉 X 轴刻度下标**
        },
        {
          type: "category",
          data: [],
          silent: true,
          axisLabel: { show: false },
          axisTick: { show: false },
          axisLine: { show: false }
        },
        {
          type: "category",
          data: [],
          silent: true,
          axisLabel: { show: false },
          axisTick: { show: false },
          axisLine: { show: false }
        }
      ],
      yAxis: [
        {
          type: "value",
          axisLabel: {
            color: "#c7e7fd",
            fontSize: this.w(16)
          },
          splitLine: {
            lineStyle: {
              color: "#517ba5",
              type: "dashed"
            }
          }
        },
        {
          type: "value",
          max: 1,
          silent: true,
          axisLabel: { show: false },
          axisTick: { show: false },
          axisLine: { show: false },
          splitLine: { show: false }
        }
      ],
      grid: {
        top: "25%",
        left: this.w(0),
        right: "4%",
        bottom: this.w(20),
        containLabel: true
      },
      series: [
        // 背景柱数据
        {
          type: "bar",
          xAxisIndex: 1,
          yAxisIndex: 1,
          barWidth: this.w(42),
          itemStyle: {
            color: "#7abfff",
            opacity: .17
          },
          data: [],
          silent: true,
          tooltip: { show: false },
          z: 1
        },
        {
          type: "bar",
          name: "",
          barWidth: this.w(12),
          itemStyle: {
            color: "",
            decal: {
              color: "#004c82",
              symbol: "rect",
              symbolSize: this.w(1),
              dashArrayY: [1, 0],
              dashArrayX: [1, 3],
              rotation: 45
            }
          },
          data: []
        },
        {
          type: "bar",
          name: "",
          barGap: "100%",
          barWidth: this.w(12),
          itemStyle: {
            color: "",
            decal: {
              color: "#004c82",
              symbol: "rect",
              symbolSize: this.w(1),
              dashArrayY: [1, 0],
              dashArrayX: [1, 3],
              rotation: 45
            }
          },
          data: []
        },
        {
          type: "pictorialBar",
          data: [],
          xAxisIndex: 2,
          symbol: "rect",
          symbolSize: [this.w(14), this.w(3)],
          symbolOffset: ["-90%", "-50%"],
          symbolPosition: "end",
          itemStyle: { color: "#fff" },
          tooltip: { show: false },
          z: 4
        },
        {
          type: "pictorialBar",
          data: [],
          xAxisIndex: 2,
          symbol: "rect",
          symbolSize: [this.w(14), this.w(3)],
          symbolOffset: ["90%", "-50%"],
          symbolPosition: "end",
          itemStyle: { color: "#fff" },
          tooltip: { show: false },
          z: 4
        }
      ]
    });
  }

  render(data) {
    super.render(data);
    if (!Object.keys(data).length) return false;
    this.ecOption.xAxis[0].data = data.axis;
    this.ecOption.xAxis[1].data = data.axis;
    this.ecOption.xAxis[2].data = data.axis;
    this.ecOption.series[0].data = data.series[0].data.map(() => 1);
    this.ecOption.series[1].data = data.series[0].data;
    this.ecOption.series[1].name = data.series[0].name;
    this.ecOption.series[1].itemStyle.color = data.series[0].color;
    this.ecOption.series[2].data = data.series[1].data;
    this.ecOption.series[2].name = data.series[1].name;
    this.ecOption.series[2].itemStyle.color = data.series[1].color;
    this.ecOption.series[3].data = data.series[0].data;
    this.ecOption.series[3].name = data.series[0].name;
    this.ecOption.series[4].data = data.series[1].data;
    this.ecOption.series[4].name = data.series[1].name;
    this.ecOption.tooltip.formatter = params => {
      this.$vm.extData = params;
      return this.$vm.$el;
    };
    this.ec.setOption(this.ecOption);
    this.play();
  }
}
