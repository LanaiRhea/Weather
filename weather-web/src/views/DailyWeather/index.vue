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
          width="140"
          align="center"
          sortable>
          <template slot-scope="scope">
            {{ formatDate(scope.row.time) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="rainfall"
          label="降雨量"
          width="120"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getRainfallColor(scope.row.rainfall) }">
              {{ scope.row.rainfall }} mm
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="minTemp"
          label="最低气温"
          width="120"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTemperatureColor(scope.row.minTemp) }">
              {{ scope.row.minTemp }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="maxTemp"
          label="最高气温"
          width="120"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTemperatureColor(scope.row.maxTemp) }">
              {{ scope.row.maxTemp }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="日温差"
          width="120"
          align="center">
          <template slot-scope="scope">
            <span :style="{ color: getTempDiffColor(scope.row.maxTemp - scope.row.minTemp) }">
              {{ (scope.row.maxTemp - scope.row.minTemp).toFixed(1) }}℃
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="maxWindSpeed"
          label="最大风速"
          width="120"
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
  </div>
</template>

<script>
import { getWeatherDataList } from '@/api/dailyWeather'
import moment from 'moment'

export default {
  name: 'DailyWeather',
  data() {
    return {
      dateRange: [],
      tableData: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      pickerOptions: {
        shortcuts: [{
          text: '第一季度',
          onClick(picker) {
            picker.$emit('pick', [
              moment('2024-01-01').startOf('day'),
              moment('2024-03-31').endOf('day')
            ])
          }
        }, {
          text: '第二季度',
          onClick(picker) {
            picker.$emit('pick', [
              moment('2024-04-01').startOf('day'),
              moment('2024-06-30').endOf('day')
            ])
          }
        }, {
          text: '第三季度',
          onClick(picker) {
            picker.$emit('pick', [
              moment('2024-07-01').startOf('day'),
              moment('2024-09-30').endOf('day')
            ])
          }
        }, {
          text: '第四季度',
          onClick(picker) {
            picker.$emit('pick', [
              moment('2024-10-01').startOf('day'),
              moment('2024-12-31').endOf('day')
            ])
          }
        }, {
          text: '2024年全年',
          onClick(picker) {
            picker.$emit('pick', [
              moment('2024-01-01').startOf('day'),
              moment('2024-12-31').endOf('day')
            ])
          }
        }]
      }
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
  created() {
    this.dateRange = ['2024-01-01', '2024-12-31']
    this.fetchData()
  }
}
</script>

<style scoped>
.daily-weather-container {
  padding: 20px 20px 20px 0;
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
  margin-top: 15px;
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
</style> 