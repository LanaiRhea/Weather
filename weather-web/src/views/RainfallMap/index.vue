<template>
  <div class="rainfall-container">
    <div class="page-header">
      <h2>中国各省年降水量分布图</h2>
    </div>
    <div id="chineseWeather" ref="chartContainer"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
// 移除原来的引入方式
// import 'echarts/map/js/china'

export default {
  name: 'RainfallMap',
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.registerMap()
  },
  methods: {
    async registerMap() {
      try {
        const chinaJSON = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
          .then(response => response.json())
        
        echarts.registerMap('china', chinaJSON)
        this.$nextTick(() => {
          this.initChart()
        })
      } catch (error) {
        console.error('加载地图数据失败：', error)
        this.$message.error('地图数据加载失败，请刷新重试')
      }
    },
    initChart() {
      if (this.chart) {
        this.chart.dispose()
      }

      const chartDom = this.$refs.chartContainer
      this.chart = echarts.init(chartDom)
      
      const option = {
        title: {
          text: '2024年各省省会年降水量（单位：毫米）',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#333'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}<br/>降水量：{c}毫米'
        },
        visualMap: {
          min: 0,
          max: 2500,
          left: 'left',
          top: 'bottom',
          text: ['高', '低'],
          calculable: true,
          inRange: {
            color: ['#C1E8FF', '#65C8FF', '#3b87b8', '#1d69b7', '#043465']
          }
        },
        series: [
          {
            name: '降雨量（毫米）',
            type: 'map',
            map: 'china',
            aspectScale: 0.75,
            zoom: 1.2,
            roam: true,
            emphasis: {
              label: {
                show: true
              }
            },
            data: [
              { name: '北京市', value: 899.2 },
              { name: '天津市', value: 646.8 },
              { name: '上海市', value: 1398.9 },
              { name: '重庆市', value: 926.0 },
              { name: '河北省', value: 770.4 },
              { name: '河南省', value: 986.2 },
              { name: '云南省', value: 1005.7 },
              { name: '辽宁省', value: 1032.4 },
              { name: '黑龙江省', value: 659.5 },
              { name: '湖南省', value: 1734.6 },
              { name: '安徽省', value: 1066.8 },
              { name: '山东省', value: 668.7 },
              { name: '新疆维吾尔自治区', value: 421.2 },
              { name: '江苏省', value: 1501.7 },
              { name: '浙江省', value: 1657.0 },
              { name: '江西省', value: 1820.1 },
              { name: '湖北省', value: 1336.8 },
              { name: '广西壮族自治区', value: 1722.8 },
              { name: '甘肃省', value: 359.6 },
              { name: '山西省', value: 564.3 },
              { name: '内蒙古自治区', value: 684.7 },
              { name: '陕西省', value: 564.3 },
              { name: '吉林省', value: 811.9 },
              { name: '福建省', value: 1446.1 },
              { name: '贵州省', value: 1269.3 },
              { name: '广东省', value: 2463.9 },
              { name: '青海省', value: 629.8 },
              { name: '西藏自治区', value: 333.6 },
              { name: '四川省', value: 1069.1 },
              { name: '宁夏回族自治区', value: 394.9 },
              { name: '海南省', value: 2263.0 }
            ]
          }
        ]
      }
      
      this.chart.setOption(option)
      
      // 添加窗口大小改变的监听
      window.addEventListener('resize', this.handleResize)
      // 立即调整大小
      this.handleResize()
    },
    handleResize() {
      if (this.chart) {
        const container = this.$refs.chartContainer
        if (container) {
          // 设置容器大小为视口大小
          container.style.width = '100vw'
          container.style.height = '100vh'
          this.chart.resize()
        }
      }
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
      window.removeEventListener('resize', this.handleResize)
    }
  }
}
</script>

<style scoped>
.rainfall-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: #fff;
  z-index: 1000;
}

.page-header {
  margin-bottom: 20px;
  text-align: center;
}

#chineseWeather {
  width: 100%;
  height: 100%;
}
</style> 