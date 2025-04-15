<template>
  <div id="box1">
      <img src="@/assets/天气预报.png" alt="加载中。。。">
      <p class="title" style="margin-left:15px">智慧天气</p>

      <div id="box2">
        <p class="welcome-text" v-if="$store.state.userinfo.id">
          欢迎，{{ $store.state.userinfo.nickname }}！
        </p>
        
        <a @click="showDrawer" style="text-decoration: none;display:flex">
            <img style="height:45px" :src="'assets/weathercn/'+ ($store.state.userinfo.id ? '登录成功头像' : '默认头像') +'.png'" alt="破损">
        </a>
        
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
export default {
    data() {
        return {
            img: '默认头像'
        }
    },
    methods: {
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
        }
    },
    mounted() {
        pubsub.subscribe('updataHeadImg', () => {
            this.img = '登录成功头像'
        })
    }
}
</script>

<style scoped>
#box2 {
    position: absolute;
    left: 790px;
    width: 250px;
    display: flex;
    align-items: center;
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
    width: 859px;
    height: 45px;
    background-color: #ffffff;
    margin-left: auto;
    margin-right: auto;
    position: relative;
    left: -9px;
    display: flex;
}

.title {
    line-height: 45px;
    font-family: 'PingFang SC';
    font-size: 20px;
    font-weight: bold;
}
</style>