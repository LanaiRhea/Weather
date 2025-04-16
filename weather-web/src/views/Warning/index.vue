<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.type"
        placeholder="预警类型"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.status" placeholder="预警状态" clearable class="filter-item" style="width: 130px" @change="handleFilter">
        <el-option label="刚创建" :value="0" />
        <el-option label="已发布" :value="1" />
        <el-option label="已过期" :value="2" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleAdd">
        添加预警
      </el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="warningList"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="序号" type="index" align="center" width="80" />
      <el-table-column label="预警类型" prop="type" align="center" width="120">
        <template slot-scope="scope">
          <span class="warning-type">{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警等级" prop="level" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="getLevelTag(scope.row.level)">
            {{ scope.row.level }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警内容" prop="content" align="center" min-width="200">
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            width="400"
            trigger="hover">
            <div class="warning-content-detail">
              {{ scope.row.content }}
            </div>
            <div slot="reference" class="warning-content-preview">
              {{ scope.row.content }}
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="success"
            @click="handlePublish(scope.row)"
          >
            发布
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            size="mini"
            type="warning"
            @click="handleRevoke(scope.row)"
          >
            撤销
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="listQuery.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form ref="warningForm" :model="warningForm" :rules="rules" label-width="100px">
        <el-form-item label="预警类型" prop="type">
          <el-input v-model="warningForm.type" placeholder="请输入预警类型" />
        </el-form-item>
        <el-form-item label="预警等级" prop="level">
          <el-select v-model="warningForm.level" placeholder="请选择预警等级" style="width: 100%">
            <el-option label="蓝色" value="蓝色" />
            <el-option label="黄色" value="黄色" />
            <el-option label="橙色" value="橙色" />
            <el-option label="红色" value="红色" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警内容" prop="content">
          <el-input
            v-model="warningForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入预警内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getWarningList, addWarning, updateWarning, deleteWarning } from '@/api/warning'
import moment from 'moment'

export default {
  name: 'Warning',
  data() {
    return {
      loading: false,
      warningList: [],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        type: undefined,
        status: undefined
      },
      warningForm: {
        id: null,
        type: '',
        level: '',
        content: '',
        status: 0
      },
      rules: {
        type: [{ required: true, message: '请输入预警类型', trigger: 'blur' }],
        level: [{ required: true, message: '请选择预警等级', trigger: 'change' }],
        content: [{ required: true, message: '请输入预警内容', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const response = await getWarningList({
          pageNum: this.listQuery.pageNum,
          pageSize: this.listQuery.pageSize,
          type: this.listQuery.type,
          status: this.listQuery.status
        })
        if (response && response.data) {
          this.warningList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error('获取预警列表失败：返回数据格式错误')
        }
      } catch (error) {
        console.error('获取预警列表失败:', error)
        this.$message.error('获取预警列表失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '添加预警'
      this.warningForm = {
        id: null,
        type: '',
        level: '',
        content: '',
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑预警'
      this.warningForm = { ...row }
      this.dialogVisible = true
    },
    async handlePublish(row) {
      try {
        await this.$confirm('确认发布该预警吗？', '提示', {
          type: 'warning'
        })
        const response = await updateWarning({
          id: row.id,
          status: 1
        })
        if (response && response.status === 200) {
          this.$message.success('发布成功')
          this.getList()
        } else {
          throw new Error(response?.msg || '发布失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('发布失败：' + error.message)
        }
      }
    },
    async handleRevoke(row) {
      try {
        await this.$confirm('确认撤销该预警吗？', '提示', {
          type: 'warning'
        })
        const response = await updateWarning({
          id: row.id,
          status: 2
        })
        if (response && response.status === 200) {
          this.$message.success('撤销成功')
          this.getList()
        } else {
          throw new Error(response?.msg || '撤销失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('撤销失败：' + error.message)
        }
      }
    },
    async submitForm() {
      this.$refs.warningForm.validate(async (valid) => {
        if (valid) {
          try {
            const submitFunc = this.warningForm.id ? updateWarning : addWarning
            const response = await submitFunc(this.warningForm)
            
            if (response && response.status === 200) {
              this.$message.success(this.warningForm.id ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.getList()
            } else {
              throw new Error(response?.msg || (this.warningForm.id ? '更新失败' : '添加失败'))
            }
          } catch (error) {
            console.error(this.warningForm.id ? '更新失败:' : '添加失败:', error)
            this.$message.error((this.warningForm.id ? '更新' : '添加') + '失败：' + (error.message || '未知错误'))
          }
        }
      })
    },
    formatDate(date) {
      if (!date) return ''
      const dateObj = typeof date === 'string' ? new Date(date) : date
      return moment(dateObj).format('YYYY-MM-DD HH:mm:ss')
    },
    getLevelTag(level) {
      const levelMap = {
        '蓝色': 'info',
        '黄色': 'warning',
        '橙色': 'danger',
        '红色': 'red'
      }
      return levelMap[level] || 'info'
    },
    getStatusTag(status) {
      const statusMap = {
        0: 'danger',
        1: 'success',
        2: 'info'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        0: '刚创建',
        1: '已发布',
        2: '已过期'
      }
      return statusMap[status] || '未知状态'
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  padding-bottom: 10px;
}

.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin-bottom: 10px;
  margin-right: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.warning-type {
  font-weight: bold;
}

.el-tag--info {
  background-color: #e6f7ff;
  border-color: #91d5ff;
  color: #1890ff;
}

.el-tag--warning {
  background-color: #fffbe6;
  border-color: #ffe58f;
  color: #faad14;
}

.el-tag--danger {
  background-color: #fff2e8;
  border-color: #ffbb96;
  color: #fa541c;
}

.el-tag--red {
  background-color: #fff1f0;
  border-color: #ff4d4f;
  color: #cf1322;
}

.el-table {
  margin-top: 20px;
}

.el-table th > .cell {
  text-align: center;
}

.el-table .cell {
  text-align: center;
}

.dialog-footer {
  text-align: right;
}

.el-button + .el-button {
  margin-left: 10px;
}

.warning-content-preview {
  font-size: 14px;
  color: #606266;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;
  cursor: pointer;
  line-height: 1.5;
}

.warning-content-detail {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  word-break: break-all;
  white-space: pre-wrap;
}
</style>