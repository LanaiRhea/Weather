<template>
  <div class="address-container">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <span class="header-title">地区管理</span>
        <el-button type="primary" @click="handleAdd" size="medium" icon="el-icon-plus">
          新增地区
        </el-button>
      </div>
      
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        :stripe="true"
        :highlight-current-row="true">
        <el-table-column
          type="index"
          label="序号"
          width="80"
          align="center"
          :index="indexMethod">
        </el-table-column>
        <el-table-column
          prop="address"
          label="地区名称"
          min-width="180"
          align="center">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="180"
          align="center">
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top">
              <el-button
                @click="handleEdit(scope.row)"
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

    <!-- 地区表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      :close-on-click-modal="false">
      <el-form
        :model="currentAddress"
        label-width="80px"
        :rules="rules"
        ref="addressForm">
        <el-form-item label="地区名称" prop="address">
          <el-input v-model="currentAddress.address" placeholder="请输入地区名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllAddress, addAddress, putAddress as editAddress, delAddress } from '@/api/user'

export default {
    data() {
        return {
            tableData: [],
            dialogVisible: false,
            dialogTitle: '',
            currentAddress: {},
            rules: {
                address: [
                    { required: true, message: '请输入地区名称', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        indexMethod(index) {
            return index + 1;
        },
        // 获取所有地区数据
        async getAddressList() {
            try {
                const response = await getAllAddress()
                if (response && response.data) {
                    this.tableData = response.data
                }
            } catch (error) {
                console.error('获取地区数据失败:', error)
                this.$message.error('获取地区数据失败，请稍后重试')
            }
        },

        // 新增地区
        handleAdd() {
            this.dialogTitle = '新增地区'
            this.currentAddress = {
                address: ''
            }
            this.dialogVisible = true
        },

        // 编辑地区
        handleEdit(row) {
            this.dialogTitle = '编辑地区'
            this.currentAddress = JSON.parse(JSON.stringify(row))
            this.dialogVisible = true
        },

        // 删除地区
        handleDelete(row) {
            this.$confirm('确认删除该地区?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    await delAddress(row.id)
                    this.$message.success('删除成功')
                    this.getAddressList()
                } catch (error) {
                    this.$message.error('删除失败，请稍后重试')
                }
            }).catch(() => {})
        },

        // 提交表单
        handleSubmit() {
            this.$refs.addressForm.validate(async valid => {
                if (valid) {
                    try {
                        if (this.dialogTitle === '新增地区') {
                            await addAddress(this.currentAddress.address)
                            this.$message.success('新增成功')
                        } else {
                            await editAddress(this.currentAddress)
                            this.$message.success('更新成功')
                        }
                        this.dialogVisible = false
                        this.getAddressList()
                    } catch (error) {
                        this.$message.error(this.dialogTitle === '新增地区' ? '新增失败' : '更新失败')
                    }
                }
            })
        },

        // 关闭对话框
        handleClose() {
            this.$refs.addressForm.resetFields()
            this.currentAddress = {}
            this.dialogVisible = false
        }
    },
    created() {
        this.getAddressList()
    }
}
</script>

<style scoped>
.address-container {
    padding: 20px 20px 20px 0;
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

.dialog-footer {
    text-align: right;
}

.el-button + .el-button {
    margin-left: 15px;
}

.el-tooltip {
    margin: 0 5px;
}

/* 调整表格行高 */
.el-table >>> .el-table__row {
    height: 35px;
}

/* 调整表头行高 */
.el-table >>> .el-table__header th {
    height: 40px;
    line-height: 40px;
    padding: 0;
}
</style>