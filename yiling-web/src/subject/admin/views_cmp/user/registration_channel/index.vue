<template>
  <div class="app-container">
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">渠道ID</div>
              <el-input v-model="query.id" placeholder="请输入渠道ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">渠道名称</div>
              <el-input v-model="query.inviteUserName" placeholder="请输入渠道名称" />
            </el-col>
            <el-col :span="12">
              <div class="title">渠道类别</div>
              <el-select class="select-width" v-model="query.lb" placeholder="请选择">
                <!-- <el-option
                  v-for="item in sixData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option> -->
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <ylButton type="primary" @click="addClick">添加</ylButton>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="80" label="渠道ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="渠道名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="关联终端" prop="terminal">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="渠道类别" prop="channelType">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建时间" prop="">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="操作">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="editData(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deleteData(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 弹窗 -->
    <yl-dialog 
      width="700px" 
      title="推广渠道"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <el-form
          class="demo-ruleForm"
          label-width="100px"
          :label-position="'left'"
          :model="form"
          :rules="rules" 
          ref="dataForm">
          <el-form-item label="渠道名称：" prop="name">
            <el-input maxlength="20" show-word-limit placeholder="请输入名称" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="渠道类别：" prop="lb">
            <el-select class="select-width" v-model="form.lb" placeholder="请选择">
              <!-- <el-option
                v-for="item in sixData"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option> -->
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
export default {
  name: 'RegistrationChannel',
  data() {
    return {
      query: {
        id: '',
        name: '',
        lb: '',
        total: 0,
        page: 1,
        limit: 10,
        registerTime: []
      },
      loading: false,
      form: {
        name: '',
        lb: ''
      },
      rules: {
        name: [{ required: true, message: '请输入渠道名称', trigger: 'blur' }],
        lb: [{ required: true, message: '请选择渠道类别', trigger: 'change' }]
      },
      dataList: [
        {
          id: '112',
          name: '河北以岭（互联网医院）',
          terminal: '国药大药房朝阳门',
          channelType: '医院'
        },
        {
          id: '112',
          name: '河北以岭（互联网医院）',
          terminal: '国药大药房朝阳门',
          channelType: '医院'
        },
        {
          id: '112',
          name: '河北以岭（互联网医院）',
          terminal: '国药大药房朝阳门',
          channelType: '医院'
        }
      ],
      // 弹窗是否显示
      showDialog: false
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    getList() {

    },
    // 点击删除
    deleteData(row) {
      this.$confirm(`确认删除 ${row.name} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {

      })
      .catch(() => {
        //取消
      });
    },  
    // 点击编辑
    editData() {

    },
    // 新增渠道
    addClick() {
      this.showDialog = true;
    },
    // 点击弹窗保存
    confirm() {
      // this.$refs['dataForm'].validate(async (valid) => {
      //   if (valid) {

      //   } else {
      //     return false
      //   }
      // })
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        registerTime: []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>