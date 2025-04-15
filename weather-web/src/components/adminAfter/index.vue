<template>
  <div>
    <el-container>
      <el-header>天气管理后台</el-header>
      <el-container>
        <el-main>
          <div id="box1">
            <h2>导航</h2>
            <el-button type="primary" @click="$router.push('/adminAfter/user')">用户管理</el-button>
            <el-button type="primary" @click="$router.push('/adminAfter/address')" style="margin-left:-1px">地区管理</el-button>
            <el-button @click="logout" type="success" style="margin-left:-1px">回到首页</el-button>
          </div>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  mounted(){
    this.$router.push('/adminAfter/user')
  },
  methods:{
    logout(){
      this.$router.push('/')
      this.$router.go(0)
    }
  }
};
</script>

<style scoped>
 .el-header {
    background-color: rgb(84, 92, 100);
    color: white;
    font-family: '微软雅黑';
    font-size: 25px;
    text-align: center;
    line-height: 60px;
    box-shadow: 0 0 10px white;
  }
  #box1{
    width: 100px;
    height: 400px;
    margin-top: 20px;
    
    position:  fixed;
    /* border: 1px solid black; */
    font-family: '微软雅黑';

  }
  
  

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    /* line-height: 160px; */
    height:calc(100vh - 60px);
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
</style>