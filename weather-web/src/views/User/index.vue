<template>
  <div id="box1">
      <!-- 弹窗 -->
    <el-dialog :modal-append-to-body="false"  :title="title" :visible.sync="dialogVisible" @close="closeWindows" width="30%">
      <el-form>
        <el-form-item label="用户名">
          <el-input style="width: 200px" v-model="currentUser.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input style="width: 200px" v-model="currentUser.email"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input style="width: 200px" v-model="currentUser.sex"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input style="width: 200px" v-model="currentUser.age"></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input style="width: 200px" v-model="currentUser.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input style="width: 200px" v-model="currentUser.password"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio v-model="currentUser.role" label="1" >管理员</el-radio>
          <el-radio v-model="currentUser.role" label="0" >普通用户</el-radio>
        </el-form-item>
        
        
      </el-form>
 
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button v-if="shouAddBtn==0" type="primary" @click="commitAddBtn">确 定</el-button>
        <el-button v-else type="primary" @click="commitUpdataBtn">提交修改</el-button>
      </span>
    </el-dialog>



    <div>
        <h1>用戶管理</h1>
        <el-button @click="addUserBtn" type="primary" style="float:left; ">新增用户</el-button>
    </div>
    <template>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column fixed prop="id" show-overflow-tooltip label="id" width="150">
        </el-table-column>
        <el-table-column prop="nickname" label="姓名" width="120">
        </el-table-column>
        <el-table-column prop="username" label="账号" width="120">
        </el-table-column>
        <el-table-column prop="password" label="密码" width="120">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="300">
        </el-table-column>
        <el-table-column prop="sex" label="性别" width="120"> </el-table-column>
        <el-table-column prop="age" label="年龄" width="120"> </el-table-column>
        <el-table-column prop="role" label="角色" width="120">
            <template slot-scope="scoped">
                {{scoped.row.role*1 == 1?'管理员':'普通用户'}}
            </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="showDialog(scope.row)" type="text" size="small"
              >编辑</el-button
            >
            <el-button type="text"  @click="delUserBtn(scope.row.id)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>


<script>
import {getAllUser,delUser,addUser,editInfo} from '@/api/user'
export default {
    data() {
        return {
            tableData: [],
            title:'',
            currentUser:{},
            shouAddBtn:'',
            dialogVisible:false,
        };
    },
    methods:{
        addUserBtn(){
            this.title = '新增用户'
            this.dialogVisible = true;
            this.shouAddBtn = 0
        },
        getAllUserCopy(){
            getAllUser().then(response=>this.tableData = response.data)
        },
        async delUserBtn(id){
            await delUser(id).then(this.$message.success('删除成功！'))
            this.getAllUserCopy()
        },
        closeWindows(){//关闭弹窗
        this.currentUser={}
        },
        showDialog(e){
            this.shouAddBtn =1
            this.title = '编辑用户信息'
            this.currentUser = e
            this.dialogVisible = true
        },
        commitUpdataBtn(){//提交更新
            editInfo(this.currentUser).then(this.$message.success('更新成功'))
            this.dialogVisible = false
        },
        async commitAddBtn(){//提交添加
        await addUser(this.currentUser).then(this.$message.success('新增成功'))
        this.getAllUserCopy()
        this.dialogVisible = false

    }
        
    },
    created(){
    this.getAllUserCopy()
}
};


</script>

<style scoped>
#box1 {
    width: 800px;
    margin-left: 190px;
    font-family: '微软雅黑';
}
</style>