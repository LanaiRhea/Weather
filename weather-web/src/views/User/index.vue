<template>
  <div class="user-container">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <span class="header-title">用户管理</span>
        <el-button type="primary" @click="addUserBtn" size="medium" icon="el-icon-plus">
          新增用户
        </el-button>
      </div>
      
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        :stripe="true"
        :highlight-current-row="true">
        <el-table-column
          fixed
          prop="id"
          label="ID"
          width="280"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="nickname"
          label="姓名"
          width="120"
          align="center">
        </el-table-column>
        <el-table-column
          prop="username"
          label="账号"
          width="120"
          align="center">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="sex"
          label="性别"
          width="80"
          align="center">
        </el-table-column>
        <el-table-column
          prop="age"
          label="年龄"
          width="80"
          align="center">
        </el-table-column>
        <el-table-column
          prop="role"
          label="角色"
          width="100"
          align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role == 1 ? 'success' : 'info'">
              {{ scope.row.role == 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="180"
          align="center">
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top">
              <el-button
                @click="showDialog(scope.row)"
                type="primary"
                size="mini"
                icon="el-icon-edit"
                circle>
              </el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                type="danger"
                size="mini"
                icon="el-icon-delete"
                circle
                @click="handleDelete(scope.row)">
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户表单对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="500px"
      @close="closeWindows"
      :close-on-click-modal="false">
      <el-form
        :model="currentUser"
        label-width="80px"
        :rules="rules"
        ref="userForm">
        <el-form-item label="用户名" prop="nickname">
          <el-input v-model="currentUser.nickname" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="currentUser.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="currentUser.sex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="currentUser.age" :min="1" :max="120"></el-input-number>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="currentUser.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="currentUser.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="currentUser.role">
            <el-radio label="1">管理员</el-radio>
            <el-radio label="0">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllUser, delUser, addUser, editInfo } from '@/api/user'

export default {
  data() {
    return {
      tableData: [],
      title: '',
      currentUser: {},
      shouAddBtn: '',
      dialogVisible: false,
      rules: {
        nickname: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    addUserBtn() {
      this.title = '新增用户'
      this.dialogVisible = true
      this.shouAddBtn = 0
      this.currentUser = {
        sex: '男',
        role: '0',
        age: 18
      }
    },
    getAllUserCopy() {
      getAllUser().then(response => this.tableData = response.data)
    },
    handleDelete(row) {
      this.$confirm('确认删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUser(row.id).then(() => {
          this.$message.success('删除成功！')
          this.getAllUserCopy()
        })
      }).catch(() => {})
    },
    closeWindows() {
      this.currentUser = {}
      this.$refs.userForm && this.$refs.userForm.resetFields()
    },
    showDialog(row) {
      this.shouAddBtn = 1
      this.title = '编辑用户信息'
      this.currentUser = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          if (this.shouAddBtn === 0) {
            addUser(this.currentUser).then(() => {
              this.$message.success('新增成功')
              this.getAllUserCopy()
              this.dialogVisible = false
            })
          } else {
            editInfo(this.currentUser).then(() => {
              this.$message.success('更新成功')
              this.getAllUserCopy()
              this.dialogVisible = false
            })
          }
        }
      })
    }
  },
  created() {
    this.getAllUserCopy()
  }
}
</script>

<style scoped>
.user-container {
  padding: 20px 20px 20px 0; /* 调整右边距，为固定侧边栏留出空间 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #001529;
  font-family: 'Microsoft YaHei', sans-serif;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 300px;
}

.el-input-number {
  width: 120px;
}

.dialog-footer {
  text-align: right;
}

.el-tag {
  font-weight: 500;
}

.el-button + .el-button {
  margin-left: 15px;  /* 增加按钮之间的间距 */
}

.el-tooltip {
  margin: 0 5px;  /* 增加工具提示之间的间距 */
}
</style>