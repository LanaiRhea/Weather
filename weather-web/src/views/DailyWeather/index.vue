<template>
  <div class="daily-weather-container">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <span class="header-title">舟山市2024年气象数据</span>
        <div class="header-controls">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            value-format="yyyy-MM-dd"
            @change="handleDateChange">
          </el-date-picker>
          <el-button 
            type="primary" 
            icon="el-icon-search"
            @click="fetchData"
            :loading="loading">
            查询
          </el-button>
        </div>
      </div>
      
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        :stripe="true"
        v-loading="loading"
        :highlight-current-row="true"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle">
        <el-table-column
          prop="time"
          label="日期"
          width="217"
          align="center"
          sortable>
          <template slot-scope="scope">
            {{ formatDate(scope.row.time) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="rainfall"
          label="降雨量(mm)"
          width="167"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getRainfallColor(scope.row.rainfall) }">
              {{ scope.row.rainfall }} mm
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="minTemp"
          label="最低气温(°C)"
          width="167"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTemperatureColor(scope.row.minTemp) }">
              {{ scope.row.minTemp }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="maxTemp"
          label="最高气温(°C)"
          width="167"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTemperatureColor(scope.row.maxTemp) }">
              {{ scope.row.maxTemp }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="日温差(°C)"
          width="167"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTempDiffColor(scope.row.maxTemp - scope.row.minTemp) }">
              {{ (scope.row.maxTemp - scope.row.minTemp).toFixed(1) }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="maxWindSpeed"
          label="最大风速(m/s)"
          width="167"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getWindSpeedColor(scope.row.maxWindSpeed) }">
              {{ scope.row.maxWindSpeed }} m/s
            </span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>

    <!-- 添加数据分析结果模块 -->
    <div class="analysis-section">
      <h2 class="analysis-title">2024年舟山天气数据分析</h2>
      
      <!-- 修改统计数据展示的布局和样式 -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="8">
          <el-card class="stats-card" shadow="hover">
            <div slot="header" class="stats-header">
              <span>温度统计</span>
            </div>
            <div class="stats-content">
              <p>最高温度: {{ stats.temperature.max_temp.value }}°C ({{ stats.temperature.max_temp.date }})</p>
              <p>最低温度: {{ stats.temperature.min_temp.value }}°C ({{ stats.temperature.min_temp.date }})</p>
              <p>平均最高温度: {{ stats.temperature.max_temp.avg.toFixed(1) }}°C</p>
              <p>平均最低温度: {{ stats.temperature.min_temp.avg.toFixed(1) }}°C</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="stats-card" shadow="hover">
            <div slot="header" class="stats-header">
              <span>降雨统计</span>
            </div>
            <div class="stats-content">
              <p>年度总降雨量: {{ stats.rainfall.total.toFixed(1) }}mm</p>
              <p>最大单日降雨: {{ stats.rainfall.max.value }}mm ({{ stats.rainfall.max.date }})</p>
              <p>降雨天数: {{ stats.rainfall.rainy_days }}天</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="stats-card" shadow="hover">
            <div slot="header" class="stats-header">
              <span>极端天气统计</span>
            </div>
            <div class="stats-content">
              <p>高温天数: {{ stats.extreme_weather.high_temp_days }}天</p>
              <p>低温天数: {{ stats.extreme_weather.low_temp_days }}天</p>
              <p>暴雨天数: {{ stats.extreme_weather.heavy_rain_days }}天</p>
              <p>大风天数: {{ stats.extreme_weather.strong_wind_days }}天</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 修改图表展示部分为轮播图 -->
      <el-card class="chart-carousel-card" shadow="hover">
        <el-carousel height="600px" :interval="4000" arrow="always">
          <el-carousel-item>
            <div class="chart-item">
              <h3 class="chart-title">月度降水量分布</h3>
              <img src="/analysis_results/monthly_rainfall.png" alt="月度降雨量分布">
            </div>
          </el-carousel-item>
          <el-carousel-item>
            <div class="chart-item">
              <h3 class="chart-title">温度变化趋势</h3>
              <img src="/analysis_results/temperature_trend.png" alt="温度变化趋势">
            </div>
          </el-carousel-item>
        </el-carousel>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getWeatherDataList } from '@/api/dailyWeather'
import moment from 'moment'

export default {
  name: 'DailyWeather',
  data() {
    return {
      dateRange: ['2024-01-01', '2024-12-31'],
      tableData: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      pickerOptions: {
        shortcuts: [
          {
            text: '2024年全年',
            onClick: (picker) => {
              const start = '2024-01-01'
              const end = '2024-12-31'
              this.dateRange = [start, end]
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '第一季度',
            onClick: (picker) => {
              const start = '2024-01-01'
              const end = '2024-03-31'
              this.dateRange = [start, end]
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '第二季度',
            onClick: (picker) => {
              const start = '2024-04-01'
              const end = '2024-06-30'
              this.dateRange = [start, end]
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '第三季度',
            onClick: (picker) => {
              const start = '2024-07-01'
              const end = '2024-09-30'
              this.dateRange = [start, end]
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '第四季度',
            onClick: (picker) => {
              const start = '2024-10-01'
              const end = '2024-12-31'
              this.dateRange = [start, end]
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      stats: null
    }
  },
  methods: {
    formatDate(date) {
      return moment(date).format('YYYY-MM-DD')
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData()
    },
    handleDateChange() {
      this.currentPage = 1
      this.fetchData()
    },
    fetchData() {
      if (!this.dateRange || !this.dateRange[0] || !this.dateRange[1]) {
        this.$message.warning('请选择日期范围')
        return
      }

      this.loading = true
      getWeatherDataList({
        startDate: moment(this.dateRange[0]).format('YYYY-MM-DD'),
        endDate: moment(this.dateRange[1]).format('YYYY-MM-DD') + ' 23:59:59',
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(response => {
        if (response.status === 200) {
          this.tableData = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.msg || '获取数据失败')
        }
      }).catch(() => {
        this.$message.error('获取数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    headerCellStyle() {
      return {
        backgroundColor: '#f5f7fa',
        color: '#606266',
        fontWeight: 600,
        fontSize: '14px',
        height: '45px',
        padding: '0',
        borderRight: '1px solid #ebeef5'
      }
    },
    cellStyle() {
      return {
        fontSize: '14px',
        padding: '8px 0'
      }
    },
    getRainfallColor(value) {
      if (value === 0) return '#909399'
      if (value < 10) return '#67C23A'
      if (value < 25) return '#E6A23C'
      return '#F56C6C'
    },
    getTemperatureColor(temp) {
      if (temp < 0) return '#409EFF'
      if (temp < 15) return '#67C23A'
      if (temp < 25) return '#E6A23C'
      return '#F56C6C'
    },
    getTempDiffColor(diff) {
      if (diff < 5) return '#909399'
      if (diff < 10) return '#67C23A'
      if (diff < 15) return '#E6A23C'
      return '#F56C6C'
    },
    getWindSpeedColor(speed) {
      if (speed < 5.4) return '#67C23A'
      if (speed < 7.9) return '#E6A23C'
      if (speed < 10.7) return '#F56C6C'
      return '#FF4949'
    }
  },
  async created() {
    this.fetchData()
    // 加载统计数据
    try {
      const response = await fetch('/analysis_results/stats.json')
      this.stats = await response.json()
    } catch (error) {
      console.error('加载统计数据失败:', error)
      this.$message.error('加载统计数据失败')
    }
  }
}
</script>

<style scoped>
.daily-weather-container {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #001529;
  font-family: 'Microsoft YaHei', sans-serif;
}

.header-controls {
  display: flex;
  gap: 15px;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-date-editor {
  width: 350px !important;
}

.el-picker-panel__sidebar {
  width: 120px;
}

.el-picker-panel__shortcut {
  line-height: 28px;
  font-size: 14px;
  padding-left: 15px;
}

.el-button {
  padding: 9px 15px;
}

.el-table {
  margin-top: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-table::before {
  display: none;
}

.el-pagination {
  padding: 0;
}

.el-table tbody tr:hover > td {
  background-color: #f5f7fa !important;
}

.el-table td, .el-table th {
  border-right: 1px solid #ebeef5;
}

.el-table--border::after, .el-table--group::after {
  width: 0;
}

.el-table >>> .el-table__cell {
  padding: 12px 0;
}

.el-table >>> .el-table__header th {
  font-size: 16px;
  font-weight: bold;
}

.el-table >>> .el-table__body td {
  font-size: 15px;
}

/* 添加分析结果模块样式 */
.analysis-section {
  margin-top: 40px;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.analysis-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 20px 0;
  font-family: "Microsoft YaHei", sans-serif;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card, .chart-carousel-card {
  height: 100%;
  transition: all 0.3s;
}

.stats-card:hover, .chart-carousel-card:hover {
  transform: translateY(-5px);
}

.stats-header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

.stats-card {
  height: 240px;
  display: flex;
  flex-direction: column;
}

.stats-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 20px;
}

.stats-content p {
  margin: 12px 0;
  color: #606266;
  line-height: 1.8;
  font-size: 15px;
}

/* 添加轮播图相关样式 */
.chart-carousel-card {
  margin-top: 20px;
}

.chart-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart-title {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  margin: 20px 0;
}

.chart-item img {
  max-width: 90%;
  max-height: 500px;
  object-fit: contain;
}

/* 修改轮播图箭头样式 */
.el-carousel >>> .el-carousel__arrow {
  background-color: rgba(64, 158, 255, 0.7);
  font-size: 20px;
}

.el-carousel >>> .el-carousel__arrow:hover {
  background-color: #409EFF;
}
</style> 