<template>
  <div class="app-container">
    <!-- 今日人员数据概览 -->
    <div class="overview">
      <div class="card">
        <div class="icon"><i class="el-icon-user-solid"></i></div>
        <div class="value">0</div>
        <div class="label">员工（人）</div>
        <div class="change">+1人</div>
      </div>
      <div class="card">
        <div class="icon"><i class="el-icon-user"></i></div>
        <div class="value">0</div>
        <div class="label">访客（人）</div>
        <div class="change">0人</div>
      </div>
      <div class="card">
        <div class="icon"><i class="el-icon-group"></i></div>
        <div class="value">0</div>
        <div class="label">承包商（人）</div>
        <div class="change">0人</div>
      </div>
    </div>

    <!-- 人员流量数据统计 -->
    <div class="chart-section">
      <h3>人员流量数据统计</h3>
      <div class="tabs">
        <span class="tab active" @click="switchTab('7')">近7天</span>
        <span class="tab" @click="switchTab('30')">近30天</span>
      </div>
      <div ref="lineChart" class="chart1"></div>
    </div>

    <!-- 员工占比 -->
    <div class="chart-section">
      <div class="row">
        <div class="chart-item">
          <h3>员工占比</h3>
          <div class="charts-container">
            <div ref="pieChart1" class="chart"></div>
            <div ref="pieChart2" class="chart"></div>
          </div>
        </div>
        <div class="chart-item">
          <h3>部门统计</h3>
          <div class="charts-container">
            <div ref="pieChart3" class="chart"></div>
            <div ref="pieChart4" class="chart"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Wms_statistics_people",
  data() {
    return {
      tab: '7',
      lineData: [
        { date: '2025-10-26', value: 0 },
        { date: '2025-10-27', value: 1 },
        { date: '2025-10-28', value: 0 },
        { date: '2025-10-29', value: 1 },
        { date: '2025-10-30', value: 1 },
        { date: '2025-10-31', value: 1 },
        { date: '2025-11-01', value: 0 }
      ],
      pieData1: [
        { name: '正式工', value: 80 },
        { name: '临时工', value: 20 }
      ],
      pieData2: [
        { name: '仓储部', value: 40 },
        { name: '物流部', value: 30 },
        { name: '技术部', value: 30 }
      ]
    }
  },
  mounted() {
    this.initLineChart()
    this.initPieChart1()
    this.initPieChart2()
    this.initPieChart3()
    this.initPieChart4()
  },
  methods: {
    switchTab(type) {
      this.tab = type
      this.initLineChart()
    },
    initLineChart() {
      const chart = echarts.init(this.$refs.lineChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: this.lineData.map(d => d.date),
          axisLabel: { interval: 0, rotate: 45 }
        },
        yAxis: {
          type: 'value',
          max: 1,
          splitLine: { show: true }
        },
        series: [{
          type: 'line',
          data: this.lineData.map(d => d.value),
          smooth: true,
          lineStyle: { color: '#0066ff' },
          areaStyle: { color: 'rgba(0, 102, 255, 0.1)' }
        }]
      })
    },
    initPieChart1() {
      const chart = echarts.init(this.$refs.pieChart1)
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          data: this.pieData1,
          label: { show: true, formatter: '{b}: {c}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
        }]
      })
    },
    initPieChart2() {
      const chart = echarts.init(this.$refs.pieChart2)
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          data: this.pieData2,
          label: { show: true, formatter: '{b}: {c}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
        }]
      })
    },
    initPieChart3() {
      const chart = echarts.init(this.$refs.pieChart3)
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          data: this.pieData1,
          label: { show: true, formatter: '{b}: {c}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
        }]
      })
    },
    initPieChart4() {
      const chart = echarts.init(this.$refs.pieChart4)
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          data: this.pieData2,
          label: { show: true, formatter: '{b}: {c}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
        }]
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  background-color: #fff;
}

.overview {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  flex: 1;
  background: #eee;
  border-radius: 8px;
  padding: 8px;
  text-align: center;
}

.card .icon {
  font-size: 24px;
  color: #0066ff;
  margin-bottom: 2px;
}

.card .value {
  font-size: 24px;
  font-weight: bold;
}

.card .label {
  font-size: 14px;
  margin: 5px 0;
}

.card .change {
  font-size: 12px;
  color: #ccc;
}

.chart-section h3 {
  margin-bottom: 2px;
}

.tabs {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  margin-bottom: 2px;
}

.tab {
  padding: 5px 10px;
  background: #eee;
  cursor: pointer;
  border-radius: 4px;
}

.tab.active {
  background: #0066ff;
  color: white;
}

.row {
  display: flex;
  gap: 20px;
}

.charts-container {
  display: flex;
  gap: 15px;
}

.chart-item {
  flex: 1;
  background: #eee;
  border-radius: 8px;
  padding: 15px;
}

.chart {
  flex: 1;
  width: 50%;
  height: 250px;
}

.chart1 {
  flex: 1;
  width: 100%;
  height: 250px;
}
</style>
