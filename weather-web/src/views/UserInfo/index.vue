<template>
  <div class="user-info-container">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <span class="header-title">个人信息</span>
        <el-button type="primary" @click="goBack" size="medium" icon="el-icon-back">
          返回首页
        </el-button>
      </div>
      
      <div class="user-info-content">
        <div class="avatar-section">
          <img :src="'assets/weathercn/登录成功头像.png'" alt="用户头像" class="user-avatar">
          <h2>{{ userInfo.nickname }}</h2>
        </div>
        
        <div class="info-section">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户ID">{{ userInfo.id }}</el-descriptions-item>
            <el-descriptions-item label="账号">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ userInfo.sex }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ userInfo.age }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="userInfo.role === '1' ? 'success' : 'info'">
                {{ userInfo.role === '1' ? '管理员' : '普通用户' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="action-section">
          <el-button type="primary" @click="editInfo" size="medium">编辑信息</el-button>
          <el-button type="warning" @click="changePassword" size="medium">修改密码</el-button>
          <el-button type="danger" @click="logout" size="medium">退出登录</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserInfo',
  data() {
    return {
      userInfo: {}
    }
  },
  created() {
    this.userInfo = this.$store.state.userinfo
    if (!this.userInfo.id) {
      this.$message.warning('请先登录')
      this.$router.push('/')
    }
  },
  methods: {
    goBack() {
      this.$router.push('/')
    },
    editInfo() {
      this.$router.push('/user-edit')
    },
    changePassword() {
      this.$router.push('/change-password')
    },
    logout() {
      this.$store.commit('setRole', '')
      this.$store.commit('setUserinfo', {})
      this.$store.commit('setLike', [])
      this.$message.success('已退出登录')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.user-info-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #001529;
  font-family: 'Microsoft YaHei', sans-serif;
}

.user-info-content {
  padding: 20px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 30px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-bottom: 15px;
}

.info-section {
  margin-bottom: 30px;
}

.action-section {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.el-descriptions {
  margin-top: 20px;
}

.el-descriptions-item__label {
  width: 100px;
  background-color: #f5f7fa;
}
</style> 