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
          :date="we.date" :weatherImg="we.img" :week="we.week" :temp="we.temp" :pm25="we.aqi.ipm2_5" :descriptColor="we.aqi.aqiinfo.color" :level="we.aqi.aqiinfo.level" :quality="we.aqi.quality " :humidity="we.humidity" @openDrawer="openLoginDrawer" />
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
        :visible.sync="drawer"
        size="30%"
        class="user-drawer">
        <div class="drawer-container">
          <!-- 头像区域 -->
          <div class="avatar-box">
            <img :src="'assets/weathercn/'+img+'.png'" alt="用户头像" class="user-avatar">
          </div>

          <!-- 用户昵称显示区 -->
          <div class="nickname-box">
            <p v-if="userInfo.nickname">{{userInfo.nickname}}</p>
            <p v-else class="login-tip">未登录</p>
          </div>

          <!-- 未登录状态显示登录/注册表单 -->
          <div v-if="!userInfo.nickname">
            <div v-show="register==0" class="login-form">
              <el-form>
                <el-form-item>
                  <div class="input-with-icon">
                    <i class="el-icon-user-solid"></i>
                    <input v-model="user.username" placeholder="请输入账号" class="custom-input" />
                  </div>
                </el-form-item>
                <el-form-item>
                  <div class="input-with-icon">
                    <i class="el-icon-lock"></i>
                    <input type="password" v-model="user.password" placeholder="请输入密码" class="custom-input" />
                  </div>
                </el-form-item>
              </el-form>
              <button class="custom-button login-button" @click="login">登 录</button>
              <div class="form-footer">
                <a class="register-link" @click="register=1">免费注册</a>
              </div>
            </div>

            <div v-show="register==1" class="register-form">
              <el-form>
                <el-form-item>
                  <div class="input-with-icon">
                    <i class="el-icon-user"></i>
                    <input v-model="userR.nickname" placeholder="请输入昵称" class="custom-input" />
                  </div>
                </el-form-item>
                <el-form-item>
                  <div class="input-with-icon">
                    <i class="el-icon-user-solid"></i>
                    <input v-model="userR.username" placeholder="请输入账号" class="custom-input" />
                  </div>
                </el-form-item>
                <el-form-item>
                  <div class="input-with-icon">
                    <i class="el-icon-lock"></i>
                    <input type="password" v-model="userR.password" placeholder="请输入密码" class="custom-input" />
                  </div>
                </el-form-item>
              </el-form>
              <button class="custom-button register-button" @click="gotoRegister">注 册</button>
              <div class="form-footer">
                <a class="login-link" @click="register=0">返回登录</a>
              </div>
            </div>
          </div>

          <!-- 已登录状态显示用户信息 -->
          <div v-else class="user-info">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="用户ID">{{userInfo.id}}</el-descriptions-item>
              <el-descriptions-item label="账号">{{userInfo.username}}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{userInfo.email}}</el-descriptions-item>
              <el-descriptions-item label="性别">{{userInfo.sex}}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{userInfo.age}}</el-descriptions-item>
              <el-descriptions-item label="角色">
                <el-tag :type="userInfo.role === '1' ? 'success' : 'info'">
                  {{userInfo.role === '1' ? '管理员' : '普通用户'}}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>

            <div class="action-buttons">
              <el-button type="primary" @click="editPwd=1" size="small">修改密码</el-button>
              <el-button type="danger" @click="logout" size="small">退出登录</el-button>
            </div>

            <!-- 修改密码表单 -->
            <div v-show="editPwd==1" class="change-password-form">
              <div class="password-input-group">
                <input type="password" v-model="editUser.oldPwd" placeholder="请输入旧密码" class="custom-input" />
                <input type="password" v-model="editUser.newPwd" placeholder="请输入新密码" class="custom-input" />
              </div>
              <el-button type="success" @click="submitUpdatePwd" size="small">确认修改</el-button>
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
      userInfo: this.$store.state.userinfo || {},  // 初始化时从store获取
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
  watch: {
    'userInfoCache': {
      handler(newVal) {
        this.userInfo = newVal;
        this.img = newVal.id ? '登录成功头像' : '默认头像';
      },
      immediate: true
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
        // 登录成功后刷新页面
        this.$router.go(0)
      }).catch(() => {
        // 错误提示已在请求拦截器中处理，这里不需要重复处理
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
     },
    goToRainfallMap() {
      if (this.$store.state.userinfo.id) {
        this.$router.push('/rainfall-map')
      } else {
        this.$message({
          message: '请先登录后查看',
          type: 'warning'
        })
        // 可以选择打开登录框
        this.drawer = true  // 假设drawer是控制登录框显示的变量
      }
    },
    openLoginDrawer() {
      this.drawer = true
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
.rainfall-btn-container {
  text-align: center;
  margin: 20px 0;
  padding: 10px;
}

.rainfall-btn {
  font-size: 16px;
  padding: 10px 20px;
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
}

.rainfall-btn:hover {
  background-color: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
}

/* 抽屉样式 */
.user-drawer {
  font-family: 'Microsoft YaHei', sans-serif;
}

.drawer-container {
  padding: 20px;
}

/* 头像区域 */
.avatar-box {
  background-color: rgb(222,225,230);
  padding: 20px 0;
  text-align: center;
  border-radius: 8px;
}

.user-avatar {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  background: #ffffff;
}

/* 昵称显示区 */
.nickname-box {
  text-align: center;
  margin: 20px 0;
  font-size: 16px;
}

.login-tip {
  color: #909399;
}

/* 表单样式 */
.login-form, .register-form {
  width: 100%;
  max-width: 300px;
  margin: 0 auto;
  padding: 20px;
}

.input-with-icon {
  position: relative;
  margin-bottom: 20px;
}

.input-with-icon i {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #909399;
  font-size: 16px;
}

.custom-input {
  width: 100%;
  height: 40px;
  padding: 0 15px 0 35px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  background: transparent;
  transition: all 0.3s;
}

.custom-input:focus {
  border-color: #409EFF;
  outline: none;
}

.custom-button {
  width: 100%;
  height: 40px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.login-button {
  background-color: #409EFF;
  color: white;
}

.register-button {
  background-color: #67C23A;
  color: white;
}

.custom-button:hover {
  opacity: 0.9;
}

.form-footer {
  text-align: right;
  margin-top: 15px;
}

.register-link, .login-link {
  color: #409EFF;
  cursor: pointer;
  font-size: 14px;
}

/* 用户信息样式 */
.user-info {
  padding: 20px;
}

.info-title {
  font-size: 20px;
  margin-bottom: 25px;
  color: #303133;
  font-weight: 500;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 4px;
  transition: all 0.3s;
}

.info-item:hover {
  background-color: #f5f7fa;
}

.info-label {
  width: 60px;
  color: #606266;
}

.info-value {
  flex: 1;
  color: #303133;
}

.edit-icon {
  color: #409EFF;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.3s;
}

.info-item:hover .edit-icon {
  opacity: 1;
}

.edit-input-group {
  display: flex;
  align-items: center;
  flex: 1;
}

.edit-input {
  flex: 1;
  height: 32px;
  padding: 0 10px;
  margin-right: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

/* 修改密码表单 */
.change-password-form {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.password-input-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 15px;
}
</style>