<template>
  <div>
    <el-container>
      <el-header style="height:45px">
          <Header/>
      </el-header>
      <el-main>
        <div id="sun">
          <!-- 当前地址的信息 -->
          <CurrentAddressWeather :city="we.city" :weather="we.weather" :templow="we.templow" :temphigh="we.temphigh" :winddirect="we.winddirect" :windpower="we.windpower"
          :date="we.date" :weatherImg="we.img" :week="we.week" :temp="we.temp" :pm25="we.aqi.ipm2_5" :descriptColor="we.aqi.aqiinfo.color" :level="we.aqi.aqiinfo.level" :quality="we.aqi.quality " :humidity="we.humidity"   />
          <!-- 今天24小时线形天气 -->
          <TodayWeather :hourly="we.hourly" />
          <!-- 生活指数 -->
          <LiveExponent :index="we.index" />
          <!-- 未来线形天气 -->
          <FutureThreadWeather :daily="we.daily" />
          <!-- 未来网格天气 -->
          <FutureGridWeather v-if="false"/>
        </div>
      </el-main>
    </el-container>
    <!-- 右侧抽屉 -->
      <el-drawer
        title="个人中心"
        style="font-size:25px;font-weight: bold;font-family:'微软雅黑'"
        :visible.sync="drawer"
        size="30%"
        >
        <div>
          <div style="background-color:rgb(222,225,230)" class="userBox">
            <img  id="userImg" :src="'assets/weathercn/'+img+'.png'" alt="破损">
          </div>
          <div style="margin-top:50px;text-align: center;font-size:15px">
            <p v-if="false">
              未登录
            </p>
            <p style="margin-right:10px">
              {{userInfo.nickname}}
            </p>
          </div>
          <!-- 登录表单 -->
          <div v-if="!userInfo.nickname" v-show="register==0"  class="formBox">
            <el-form>
              <el-form-item>
                <span>
                  <i class="el-icon-user-solid userIcon"></i>
                </span>
                <input v-model="user.username" style="background: transparent;" class="userInput" />
              </el-form-item>
              <el-form-item style="margin-bottom:0px">
                <span>
                  <i class="el-icon-lock userIcon"></i>
                </span>
                <input type="password" v-model="user.password" style="background: transparent;" class="userInput" />
                <!-- <a style="float:right;">忘记密码？</a> -->
              </el-form-item>
            </el-form>
            <button class="userBtn" @click="login">登录</button>
             <a style="float:right;font-size:15px;margin-top:10px;margin-right:5px;color:green;" @click="register=1">免费注册</a>
          </div>
          <!-- 用户信息 -->
          <div v-else class="userInfo">
            <h3 style="font-size:20px;margin-bottom:20px;margin-left:-25px">基本信息</h3>
            <p>
              <span>&nbsp;I&nbsp;D&nbsp;&nbsp;&nbsp;&nbsp;</span>
              <span style="width:100px;overflow:hidden;display:inline-block;">{{userInfo.id}}...</span>
            </p>
            <p class="userEdit">
              <span>账号&nbsp;&nbsp;</span>
              <span v-show="edit==0">{{userInfo.username}}</span>
              <input style="width:140px;height:25px;"  v-model="userInfo.username" v-show="edit==1" />
              <a v-show="edit==0"  @click="edit=1" href="#">&nbsp;&nbsp; <i class="el-icon-edit-outline"></i>编辑</a>
              <el-button type="success" size="mini" v-show="edit==1"  @click="submitUserinfo(userInfo.username)" href="#"> <i style="color:green" class="el-icon-success"></i>完成</el-button>
            </p>
            <p>
              <span>邮箱&nbsp;&nbsp;</span>
              <span v-show="edit2==0">{{userInfo.email}}</span>
         
              <input style="width:140px;height:25px;" v-model="userInfo.email" v-show="edit2==2" />
              <a v-show="edit2==0"  @click="edit2=2" href="#">&nbsp;&nbsp; <i class="el-icon-edit-outline"></i>编辑</a>
              <el-button type="success" size="mini" v-show="edit2==2"  @click="submitUserinfo(userInfo.email)" href="#"> <i style="color:green" class="el-icon-success"></i>完成</el-button>
            </p>
            <p>
              <span>姓名&nbsp;&nbsp;</span>
              <span v-show="edit3==0">{{userInfo.nickname}}</span>
              
              <input style="width:140px;height:25px;" v-model="userInfo.nickname" v-show="edit3==3" />
              <a v-show="edit3==0"  @click="edit3=3" href="#">&nbsp;&nbsp; <i class="el-icon-edit-outline"></i>编辑</a>
              <el-button type="success" size="mini" v-show="edit3==3"  @click="submitUserinfo(userInfo.nickname)" href="#"> <i style="color:green" class="el-icon-success"></i>完成</el-button>
            </p>
            <p>
              <span>性别&nbsp;&nbsp;</span>
              <span v-show="edit4==0">{{userInfo.sex}}</span>
         
              <input style="width:140px;height:25px;" v-model="userInfo.sex" v-show="edit4==4" />
              <a v-show="edit4==0"  @click="edit4=4" href="#">&nbsp;&nbsp; <i class="el-icon-edit-outline"></i>编辑</a>
              <el-button type="success" size="mini" v-show="edit4==4"  @click="submitUserinfo(userInfo.sex)" href="#"> <i style="color:green" class="el-icon-success"></i>完成</el-button>
            </p>
            <p>
              <span>年龄&nbsp;&nbsp;</span>
              <span v-show="edit5==0">{{userInfo.age}}</span>
            
              <input style="width:140px;height:25px;" v-model="userInfo.age" v-show="edit5==5" />
              <a v-show="edit5==0"  @click="edit5=5" href="#">&nbsp;&nbsp; <i class="el-icon-edit-outline"></i>编辑</a>
              <el-button type="success" size="mini" v-show="edit5==5"  @click="submitUserinfo(userInfo.age)" href="#"> <i style="color:green" class="el-icon-success"></i>完成</el-button>
            </p>
            <el-button style="float:right" type="error" @click="logout" size="mini">退出登录</el-button>
            <el-button style="float:right" type="primary" @click="editPwd=1" size="mini">修改密码</el-button>
            <div style="margin-top:50px;margin-left: auto;margin-right:auto;" v-show="editPwd==1">
              <span>请输入旧密码</span>
              <input v-model="editUser.oldPwd" /><br />
              <span>请输入新密码</span>
              <input v-model="editUser.newPwd"/>
              <el-button style="float:right" type="success" @click="submitUpdatePwd" size="mini">提交</el-button>
            </div>
          </div>
          
          <!-- 注册div -->
          <div>
            <div v-show="register==1"  class="formBox">
            <el-form>
              <el-form-item>
                <span style="margin-left:20px">昵称：</span>
                <input v-model="userR.nickname" style="width:140px;background: transparent;" class="userInput" />
              </el-form-item>
              <el-form-item>
                <span>
                  <i class="el-icon-user-solid userIcon"></i>
                </span>
                <input v-model="userR.username" placeholder="登录账号" style="background: transparent;" class="userInput" />
              </el-form-item>
              <el-form-item style="margin-bottom:0px">
                <span>
                  <i class="el-icon-lock userIcon"></i>
                </span>
                <input v-model="userR.password"  placeholder="登录密码" style="background: transparent;" class="userInput" />
                
              </el-form-item>
            </el-form>
            <button class="userBtn" @click="gotoRegister">注册</button>
            <a style="float:right;font-size:15px;margin-top:10px;margin-right:5px;color:green;" @click="register=0">去登录</a>
             
          </div>
          </div>
          
        </div>
      </el-drawer>
  </div>
</template>

<script>
import {getWeather} from '@/api/home'
import Header from '@/components/header'
import CurrentAddressWeather from '@/views/currentAddressWeather'
import FutureGridWeather from '@/views/futureGridWeather'
import FutureThreadWeather from '@/views/futureThreadWeather'
import TodayWeather from '@/views/todayWeather'
import LiveExponent from '@/views/liveExponent'
import pubsub from 'pubsub-js'
import {login,editPwd,editInfo,addUser} from '@/api/user'
export default {
  components:{
    Header,CurrentAddressWeather,FutureGridWeather,FutureThreadWeather,TodayWeather,LiveExponent
  },
  data() {
    return {
      register:0,
      img:'默认头像',
      userInfo:{},
      editUser:{},
      user:{},
      we:{},
      cityName:'',
      drawer:false,
      edit: 0,
      edit2: 0,
      edit3: 0,
      edit4: 0,
      edit5: 0,
      editPwd: 0,
      userR:{}
      
    }
  },
  computed:{
    userInfoCache(){
      return this.$store.state.userinfo
    }
  },
  methods:{
    gotoRegister(){
      addUser(this.userR).then(
      this.$message.success('注册成功！'),
      this.register=0
      )

    },
    logout(){
      this.$store.commit('setRole','')
      this.$store.commit('setUserinfo',{})
      this.$store.commit('setLike',[])
      this.$router.go(0)
    },
    login(){
      login(this.user).then(response =>{
        this.userInfo = response.data
        this.$message.success('欢迎'+response.data.nickname+'登录系统！')
        this.$store.commit('setRole',response.data.role)
        this.$store.commit('setUserinfo',response.data)
        this.$store.commit('setLike',response.data.like)
        pubsub.publish('updataHeadImg')
        this.img = '登录成功头像'
        this.drawer = false
      })
    },
     submitUserinfo(e){//修改身份信息
       if(!e.trim()) return this.$message.warning('输入不能为空')
       //请求修改
      editInfo(this.userInfo).then(this.$message.success('修改成功！'))
       this.edit = 0
       this.edit2 = 0
       this.edit3 = 0
       this.edit4 = 0
       this.edit5 = 0
     },
     submitUpdatePwd(){//修改密码
      if(this.editUser.oldPwd !== this.userInfo.password) this.$message.warning('验证密码失败')
      else{
        var editUser = this.userInfo
        editUser.password = this.editUser.newPwd
        editPwd(editUser).then(response =>{
          var info = this.userInfo
          info.password = this.editUser.newPwd
          this.$store.commit('setUserinfo',info)
          this.editPwd = 0
          this.$message.success('修改成功！')
          console.log(response)
        })
      }
     },
     getWeatherCopy(){
       getWeather(this.cityName).then( 
         response => {
          //  console.log('=======>',JSON.parse(response.data[0]).result)
           this.we = JSON.parse(response.data[0]).result
           
          }
       )
     }
  },
  mounted(){
    pubsub.subscribe('selectCity',async (methodName,e)=>{
      console.log("===>"+methodName)
      this.cityName = e
      // alert("===>"+e)
      await this.getWeatherCopy()
      this.$nextTick(()=>{
        pubsub.publish('gotoFlush',1)
      })
    })
    pubsub.subscribe('showDrawer',()=>{this.drawer = true});
  },
  created(){
    this.getWeatherCopy()
    
  }
};
</script>

<style scoped>
.userEdit{
  /* display:flex */
}
.userInfo p{
  margin-bottom: 8px;
}
.userBtn{
  margin-top:-30px;
  font-size: 20px;
  width: 200px;
  position: relative;
  left:20px;

}
.userInput{
  width: 200px;
  height: 30px;
  border-radius: 3px;
  text-indent: 2em;
  border: 1px solid #efefef;
}
.userIcon{
  font-size: 20px;
  position: relative;
  line-height:30px;
  left:22px;
}
.userBox{
  width: 100%;
  height: 70px;
  /* border: 1px solid black; */
}
p a{
  visibility: hidden;
  color:#1989fa;
  text-decoration: none;

}
p:hover a{
  visibility: initial;
}
.el-header{
    background-color: #ffffff;
    color: #333;
    /* text-align: center; */
    width: 100%;

    position: fixed;
    padding:0px;
    box-shadow: 0 0 10px rgb(0, 0, 0,0.9);
}
.userInfo{
  margin: 30px auto; 
  width: 260px;
  font-family: '微软雅黑';
  font-size: 15px;
  font-weight: normal;
  margin: 30px auto;
}
.el-main {
  /* background-color: #ac2a5c; */
  background: linear-gradient(rgb(13, 104, 188), rgb(54, 131, 195));
  padding: 0 0;
  background-position: center 0;
  color: #333;
  /* text-align: center; */
  margin-top: 45px;
  height:calc(100vh - 45px)
  
}

.formBox{
  width: 230px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 20px;
  font-family: '微软雅黑';
}
  
#sun{

  height:calc(100vh - 95px);
  width: 100%;
  background: url('@/assets/阳光_51f84c3.png') no-repeat;
  background: url('@/assets/阳光_51f84c3.png') no-repeat;
}
#userImg{
  height: 90px;
  width: 90px;
  margin-left: 37%;
  border-radius:50%;
  margin-top: 30px;
  /* border: 1px solid black; */
  background: #ffffff;
}
</style>