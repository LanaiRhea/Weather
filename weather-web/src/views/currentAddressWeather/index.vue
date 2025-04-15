<template>
  <div id="box1">
      <div>
          <p class="title">
              <el-popover
                placement="top"
                width="160"
                v-model="visible">
                <p style="font-size:15px;font-weight:bold">选择您想查看的城市</p>
                <ul style="display:flex">
                    <span>已订阅：</span>
                    <li id="liBox" v-for="(e,index) in $store.state.like" :key="index">
                        <a @click="gotolikeAddress(e)" href="#" style="margin-right:5px;text-decoration: none;color:green;">{{e}}</a>
                    </li>
                </ul>
                <template>
                    <el-select v-model="cityName" filterable placeholder="请选择">
                        <el-option
                        v-for="item in myAddress"
                        :key="item.id"
                        :label="item.address"
                        :value="item.address">
                        </el-option>
                    </el-select>
                </template>
                <!-- <el-input v-model="cityName"></el-input> -->
                <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                    <el-button type="primary" size="mini" @click="selectAddress">确定</el-button>
                </div>
                <a id="address1" style="font-size:30px;text-decoration: none;color:white;" href="#"  slot="reference">{{city}}</a> 
                
              </el-popover>
              <!-- <a style="font-size:30px;" @click="selectAddress">{{city}}</a> -->
              <span>
                <a v-if="mathIsLike" @click="addLike">
                    <img width="35px"  :src="'assets/weathercn/'+likeAddress+'.png'" alt="破损">
                </a>
                <a href="#"  @click="delLike"  v-else>
                    <img width="35px"  :src="'assets/weathercn/订阅.png'" alt="破损"><!--是否订阅-->
                </a>
                  <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;今天:&nbsp;&nbsp;{{weather}}{{templow}}°~{{temphigh}}°</span>
                  <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{winddirect}}{{windpower}}</span>
              </span>
              <span id="pday" class="title">
                <!-- <span>农历三月初七</span> -->
                <span>{{date}}</span>
                <span>{{week}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
              </span>
          </p>
          
      </div>
      <div style="display:flex;margin-top:45px">
          <div>
              <span style="font-size:79px;line-height:79px">{{temp}}°</span>
          </div>
          <div style="padding: 30px 0px 0px 30px">
            <p style="margin-top:-10px;" ref="pm25" id="pm25box">
                <span>{{pm25}}&nbsp;</span>
                <span>{{quality}}</span>
            </p>
            <span>&nbsp;&nbsp;{{level}}</span>
            
            <p style="margin-top:5px;">
                <span><img style="width:20px;" :src="'/assets/weathercn/'+weatherImg+'.png'" alt=""></span>
                <span>&nbsp;&nbsp;{{weather}}</span>
                <span>&nbsp;&nbsp;{{winddirect}}</span>
            </p>
          </div>
      </div>
      <div>
          <p style="margin-top:30px">
              <span>湿度{{humidity}}%&nbsp;&nbsp;&nbsp;&nbsp;</span>
              <span>紫外线强&nbsp;&nbsp;&nbsp;&nbsp;</span>
              <!-- <span>日出05:48&nbsp;&nbsp;</span>
              <span>日落18:40</span> -->
          </p>
      </div>
  </div>
</template>

<script>
import {addLike,delLike,getAllAddress} from '@/api/user'
import pubsub from "pubsub-js";
export default {
    data() {
        return {
            cityName:'',
            likeAddress:'未订阅',
            visible: false,
            myAddress:{}
        };
    },
    props:['city','weather','templow','weatherImg','temphigh','winddirect','windpower','date','week','temp','pm25','descriptColor','level','quality','humidity'],
    methods:{
        selectAddress(){
            pubsub.publish('selectCity',this.cityName)
            this.visible = false
            this.$store.commit('setCityName', this.cityName);
            this.cityName = ''
            
        },
        gotolikeAddress(e){
            pubsub.publish('selectCity',e)
            this.visible = false
            this.$store.commit('setCityName', e);
            this.cityName = ''
        },
        addLike(){//添加喜欢
            if(!this.$store.state.userinfo.id) return this.$message.warning('您需要登录')
            //判断能否添加喜欢（只能添加3个喜欢的城市）
            if(this.$store.state.like.length*1 >= 3) return this.$message.warning('不能添加了！最多只有3个喜欢！')
            addLike(this.$store.state.cityName,this.$store.state.userinfo.id).then(response=>{
                this.$message.success('订阅成功！')
                this.$store.commit('appendLike',this.$store.state.cityName)
                console.log(response)
            })
        },
        delLike(){//移除喜欢
            if(!this.$store.state.userinfo.id) return this.$message.warning('您需要登录')//判断是否登录
            delLike(this.$store.state.cityName,this.$store.state.userinfo.id).then(response=>{
                this.$message.success('取消订阅成功')
                //移除store中的喜欢
                
                var arr =  this.$store.state.like.filter((e)=>{
                    return e !== this.$store.state.cityName
                })
                this.$store.commit('setLike', arr);//保存过滤信息
                console.log(response)
            })
        }
        
    },
    computed:{
        mathIsLike(){//判断是否订阅
        var val = this.$store.state.like
        console.log(val)
        if(val == undefined || val == null || val.length == 0) return true
        for(var i=0;i<val.length;i++){
            if(val[i].indexOf(this.$store.state.cityName) !== -1) return false //已订阅
        }
        return true;
            
        }
    },
    
    mounted(){
        this.$refs.pm25.style.color = this.descriptColor;
    },
    created(){
        getAllAddress().then(response => this.myAddress = response.data)
    }
}
</script>

<style scoped>
#liBox{
    font-family: '微软雅黑';
    font-weight: bold;
    color: orange;
    
}
#box1 {
    width: 799px;
    height: 188px;
    margin-left: auto;
    margin-right: auto;
    font-size: 15px;
    font-family: '微软雅黑';
    padding: 30px;
    color:#ffffff;
    /* background-color: rgb(218, 82, 82); */
}
.title{
    color:#ffffff;
    
}
/* a :hover{
    color: orange;
} */
#pday span{
    float: right;
}
#pm25box{
    text-align: center;
    display: inline-block;
    background-color:rgba(0, 0, 0, 0.3);
    font-weight: bold;
    padding:1.5px;
    height: 20px;
    line-height: 20px;
    width: auto;
    border-radius: 9.5px;
}
</style>