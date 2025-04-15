<template>
  <div id="box1">
      <img src="@/assets/天气预报.png" alt="加载中。。。">
      <p class="title" style="margin-left:15px">智慧天气</p>

      <div id="box2">
        <el-button type="success" size="mini" @click="$router.push('/adminAfter')" v-if="$store.state.role*1 == 1" style="float:right;margin-top:10px;">管理员入口</el-button>
        <a @click="showDrawer" style="text-decoration: none;display:flex">
            <!-- <span class="myName">admin</span> -->
            <img style="height:45px" :src="'assets/weathercn/'+img+'.png'" alt="破损">
        </a>
      </div>


      
  </div>
</template>

<script>
import pubsub from 'pubsub-js'
export default {
    data() {
        return {
            img:'默认头像'
        }
    },
    methods:{
        showDrawer(){
            pubsub.publish('showDrawer',1)
        }
        
    },
    mounted(){
        pubsub.subscribe('updataHeadImg',()=>this.img='登录成功头像')
    }

}
</script>

<style scoped>
#box2 {
    /* border: 1px solid black; */
    position: absolute;
    left: 790px;
    /* line-height:45px; */
    width: 150px;
}
#box1 {
    width: 859px;
    height: 45px;
    background-color: #ffffff;
    margin-left: auto;
    margin-right: auto;
    position: relative;
    left: -9px;
    display:flex;
    
}
.title {
    line-height:45px;
    font-family: 'PingFang SC';
    font-size:20px;
    font-weight:bold;
}
.myName{
    line-height:45px;
    font-family: "微软雅黑";
    font-weight: bold;
    font-size: 15px;
}
</style>