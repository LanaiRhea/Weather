<template>
  <div class="admin-container">
    <el-container>
      <el-header class="header-fixed">
        <div class="header-content">
          <span class="header-title">天气管理后台</span>
          <div class="welcome-text">
            欢迎，{{ $store.state.userinfo.nickname }}！
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside-fixed">
          <div class="menu-container">
            <h2 class="menu-title">系统导航</h2>
            <el-menu
              :default-active="activeMenu"
              class="admin-menu"
              @select="handleSelect"
              background-color="#001529"
              text-color="#fff"
              active-text-color="#409EFF">
              <el-menu-item index="user">
                <i class="el-icon-user"></i>
                <span>用户管理</span>
              </el-menu-item>
              <el-menu-item index="address">
                <i class="el-icon-location"></i>
                <span>地区管理</span>
              </el-menu-item>
              <el-menu-item index="daily-weather">
                <i class="el-icon-s-data"></i>
                <span>地域数据</span>
              </el-menu-item>
              <el-menu-item index="warning">
                <i class="el-icon-warning"></i>
                <span>预警管理</span>
              </el-menu-item>
            </el-menu>
          </div>
        </el-aside>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeMenu: 'user'
    }
  },
  mounted() {
    this.$router.push('/adminAfter/user')
  },
  methods: {
    handleSelect(key) {
      this.$router.push(`/adminAfter/${key}`)
    }
  }
}
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.header-fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  padding: 0;
}

.header-content {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-title {
  font-size: 22px;
  font-weight: 600;
  color: #001529;
  font-family: 'Microsoft YaHei', sans-serif;
}

.welcome-text {
  font-size: 14px;
  color: #666;
  font-family: 'Microsoft YaHei', sans-serif;
  margin-right: 20px;
}

.aside-fixed {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  overflow-y: auto;
  background-color: #001529;
  border-right: none;
}

.menu-container {
  padding: 20px 0;
}

.menu-title {
  color: #fff;
  font-size: 16px;
  padding: 0 20px;
  margin-bottom: 20px;
  font-family: 'Microsoft YaHei', sans-serif;
}

.admin-menu {
  border: none;
}

.admin-menu :deep(.el-menu-item) {
  font-family: 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  height: 50px;
  line-height: 50px;
}

.admin-menu :deep(.el-menu-item.is-active) {
  background-color: #1890ff !important;
}

.main-content {
  position: relative;
  margin-left: 200px;
  margin-top: 60px;
  min-height: calc(100vh - 60px);
  background-color: #f0f2f5;
  padding: 20px;
}

.main-content::before {
  content: '';
  position: fixed;
  left: 200px;
  top: 60px;
  bottom: 0;
  width: 1px;
  background-color: #e8e8e8;
  z-index: 999;
}
</style>