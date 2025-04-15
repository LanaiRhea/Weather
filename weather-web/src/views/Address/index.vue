<template>
  <div id="box1">

     <el-dialog
      :modal-append-to-body="false"
  title="编辑地区"
  :visible.sync="dialog"
  width="30%"
  :before-close="handleClose">
  <h2>当前地区id</h2>
  <el-input disabled v-model="currentAddress.id"></el-input>
  <h2>当前地区名</h2>
  <el-input v-model="currentAddress.address"></el-input>
  <span slot="footer" class="dialog-footer">
    <el-button @click="dialog = false">取 消</el-button>
    <el-button type="primary" @click="editCommitBtn">确 定</el-button>
  </span>
</el-dialog>


      <el-dialog
      :modal-append-to-body="false"
  title="添加地区"
  :visible.sync="dialogVisible"
  width="30%"
  :before-close="handleClose">
  <h2>请输入地区名</h2>
  <el-input v-model="addressName"></el-input>
  <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="commitAddBtn">确 定</el-button>
  </span>
</el-dialog>



      <div>
        <h1>地区管理</h1>
        <el-button  type="primary" @click="dialogVisible = true" style="float:left; ">新增地区</el-button>
    </div>
    <template>
      <el-table @cell-dblclick="editEvent" :data="tableData" border style="width: 100%">
        <el-table-column fixed prop="id" show-overflow-tooltip label="id">
        </el-table-column>
        <el-table-column prop="address" label="地区">
        </el-table-column>
        <el-table-column fixed="right" label="操作" >
          <template slot-scope="scope">
            <el-button type="primary"  @click="editBtn(scope.row)" size="mini">编辑</el-button>
            <el-button type="danger" size="mini"  @click="delUserBtn(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
import {getAllAddress,delAddress,putAddress,addAddress} from "@/api/user"
export default {
    data() {
        return {
            tableData: [],
            addressName:'',
            dialogVisible:false,
            currentAddress:{},
            dialog:false,
        };
    },
    methods:{
      editCommitBtn(){
        putAddress(this.currentAddress).then(this.$message.success('修改成功'))
        this.dialog = false;
      },
        async delUserBtn(id){
            await delAddress(id).then(this.$message.success('删除成功'))
            this.getAllUserCopy()
        },
        getAllUserCopy(){
            getAllAddress().then(response=>this.tableData = response.data)
        },
        async commitAddBtn(){

            await addAddress(this.addressName).then(this.$message.success('添加成功'))
            this.addressName = ''
            this.getAllUserCopy()
            this.dialogVisible =false
        },
        editBtn(e){
          this.currentAddress = e
          this.dialog = true
        }
        

    },
    created(){
        this.getAllUserCopy()
    }
}
</script>

<style scoped>
#box1 {
    width: 800px;
    margin-left: 170px;
    font-family: '微软雅黑';
}
</style>