<template>
  <div id="box1">
      <img src="@/assets/天气预报.png" alt="加载中。。。" class="logo-img">
      <p class="title" style="margin-left:15px">智慧天气</p>

      <div id="box2">
        <p class="welcome-text" v-if="$store.state.userinfo.id">
          欢迎，{{ $store.state.userinfo.nickname }}！
        </p>
        
        <a @click="handleAvatarClick" style="text-decoration: none;display:flex">
            <img style="height:45px" :src="'assets/weathercn/'+ ($store.state.userinfo.id ? '登录成功头像' : '默认头像') +'.png'" alt="破损">
        </a>
        
        <!-- 通知图标 -->
        <el-popover
          v-if="$store.state.userinfo.id"
          placement="bottom"
          width="300"
          trigger="hover">
          <div class="warning-list">
            <div v-if="warnings.length === 0" class="no-warnings">
              暂无预警信息
            </div>
            <template v-else>
              <div v-for="(warning, index) in warnings" :key="index" class="warning-item">
                <div class="warning-header">
                  <span :class="['warning-type-level', getWarningLevelClass(warning.level)]">
                    {{warning.type}}{{warning.level}}预警
                  </span>
                  <span class="warning-time">发布于{{formatDate(warning.updateTime)}}</span>
                </div>
                <div class="warning-content">{{warning.content}}</div>
              </div>
            </template>
          </div>
          <el-button 
            slot="reference" 
            type="text" 
            class="notification-btn">
            <i class="el-icon-bell" style="font-size: 20px; color: #606266;"></i>
            <el-badge v-if="warnings.length > 0" :value="warnings.length" class="warning-badge"></el-badge>
          </el-button>
        </el-popover>
        
        <a 
          v-if="$store.state.role === '1'"
          @click="goToAdmin"
          style="margin-left: 10px"
          target="_blank"
          rel="noopener noreferrer">
          <el-button 
            type="success" 
            size="mini">
            管理员入口
          </el-button>
        </a>
      </div>
  </div>
</template>

<script>
import pubsub from 'pubsub-js'
import { getWarningList } from '@/api/warning'
import moment from 'moment'

export default {
    data() {
        return {
            img: '默认头像',
            warnings: []
        }
    },
    methods: {
        handleAvatarClick() {
            this.showDrawer()
        },
        showDrawer() {
            pubsub.publish('showDrawer', 1)
        },
        goToAdmin() {
            if (!this.$store.state.userinfo.id) {
                this.$message({
                    message: '请先登录',
                    type: 'warning'
                })
                this.showDrawer()
            } else if (this.$store.state.role !== '1') {
                this.$message({
                    message: '您没有管理员权限',
                    type: 'error'
                })
            } else {
                // 在新标签页中打开管理后台
                const adminUrl = `${window.location.origin}/adminAfter`
                window.open(adminUrl, '_blank')
            }
        },
        getWarningType(type) {
            const typeMap = {
                '暴雨': 'danger',
                '大风': 'warning',
                '雷电': 'info',
                '高温': 'danger',
                '大雾': 'info'
            }
            return typeMap[type] || 'info'
        },
        getWarningLevel(level) {
            const levelMap = {
                '红色': 'danger',
                '橙色': 'warning',
                '黄色': 'warning',
                '蓝色': 'info'
            }
            return levelMap[level] || 'info'
        },
        getWarningLevelClass(level) {
            const levelMap = {
                '红色': 'warning-red',
                '橙色': 'warning-orange',
                '黄色': 'warning-yellow',
                '蓝色': 'warning-blue'
            }
            return levelMap[level] || 'warning-blue'
        },
        fetchWarnings() {
            if (this.$store.state.userinfo.id) {
                console.log('开始获取预警信息...')
                getWarningList({ status: 1 }).then(response => {
                    console.log('预警信息响应:', response)
                    if (response && response.data) {
                        // 检查数据结构
                        console.log('响应数据结构:', response.data)
                        // 尝试不同的数据结构
                        if (Array.isArray(response.data)) {
                            this.warnings = response.data
                        } else if (response.data.list && Array.isArray(response.data.list)) {
                            this.warnings = response.data.list
                        } else if (response.data.data && Array.isArray(response.data.data)) {
                            this.warnings = response.data.data
                        } else if (response.data.records && Array.isArray(response.data.records)) {
                            this.warnings = response.data.records
                        } else {
                            // 如果都不符合，尝试直接使用data
                            this.warnings = response.data
                        }
                        console.log('处理后的预警信息:', this.warnings)
                    } else {
                        console.warn('响应数据为空')
                        this.warnings = []
                    }
                }).catch(error => {
                    console.error('获取预警信息失败:', error)
                    console.error('错误详情:', error.response || error)
                    this.warnings = []
                })
            } else {
                console.log('用户未登录，不获取预警信息')
            }
        },
        formatDate(date) {
            if (!date) return ''
            return moment(date).format('YYYY-MM-DD HH:mm')
        }
    },
    mounted() {
        pubsub.subscribe('updataHeadImg', () => {
            this.img = '登录成功头像'
        })
        this.fetchWarnings()
        // 每5分钟更新一次预警信息
        setInterval(this.fetchWarnings, 300000)
    }
}
</script>

<style scoped>
#box2 {
    position: absolute;
    right: 0;
    display: flex;
    align-items: center;
    padding-right: 20px;
}

.welcome-text {
    margin: 0;
    margin-right: 10px;
    font-family: 'Microsoft YaHei';
    font-size: 14px;
    color: #000000;
    font-weight: 500;
    line-height: 45px;
    white-space: nowrap;
}

#box1 {
    width: 100%;
    height: 45px;
    background-color: #ffffff;
    display: flex;
    align-items: center;
    position: relative;
}

.title {
    line-height: 45px;
    font-family: 'PingFang SC';
    font-size: 20px;
    font-weight: bold;
}

.warning-list {
    max-height: 400px;
    overflow-y: auto;
}

.warning-item {
    padding: 10px;
    border-bottom: 1px solid #ebeef5;
}

.warning-item:last-child {
    border-bottom: none;
}

.warning-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.warning-type-level {
    font-size: 14px;
    font-weight: bold;
}

.warning-red {
    color: #ff4d4f;
}

.warning-orange {
    color: #fa8c16;
}

.warning-yellow {
    color: #faad14;
}

.warning-blue {
    color: #1890ff;
}

.warning-time {
    font-size: 12px;
    color: #909399;
}

.warning-content {
    font-size: 14px;
    color: #606266;
    line-height: 1.6;
}

.no-warnings {
    text-align: center;
    color: #909399;
    padding: 20px;
}

.notification-btn {
    position: relative;
    margin-left: 10px;
    padding: 0;
}

.warning-badge {
    position: absolute;
    top: 0;
    right: 0;
    transform: translate(50%, -50%);
}

.logo-img {
    height: 35px;
    width: auto;
    margin-left: 10px;
}
</style>