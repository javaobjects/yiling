<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">模板编号</div>
              <el-input v-model="query.templateNumber" placeholder="请输入模板编号" @keyup.enter.native="searchEnter" />
            </el-col>
            <el-col :span="6">
              <div class="title">模板名称</div>
              <el-input v-model="query.templateName" placeholder="请输入模板名称" @keyup.enter.native="searchEnter" />
            </el-col>
            <el-col :span="6">
              <div class="title">操作人</div>
              <el-input v-model="query.operationUser" placeholder="请输入操作人" @keyup.enter.native="searchEnter" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box">
        <ylButton type="primary" v-role-btn="['1']" @click="addClick(0)">新增</ylButton>
      </div>
      <!-- 底部列表 -->
      <div class="table-data-box mar-t-8">
        <yl-table
          class="table-box company-fixed-table"
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList">
          <el-table-column label="模板编号" min-width="120" align="center" prop="templateNumber"></el-table-column>
          <el-table-column label="模板名称" min-width="220" align="center" prop="templateName"></el-table-column>
          <el-table-column label="操作人" min-width="90" align="center" prop="updateUserStr">
          </el-table-column>
          <el-table-column label="操作时间" min-width="90" align="center">
            <template slot-scope="{ row }">
              {{ row.updateTime | formatDate('yyyy-MM-dd') }}
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="140" align="center" fixed="right">
            <template slot-scope="{ row }">
              <yl-button type="text" v-role-btn="['2']" @click="addClick(row.id)">编辑</yl-button>
              <yl-button type="text" v-role-btn="['3']" @click="deleteClick(row)">删除</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryTemplatePage, deleteTemplate } from '@/subject/pop/api/establish_purchase'
export default {
  name: 'CommodityTemplate',
  components: {
  
  },
  computed: {
    
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '建采管理'
        },
        {
          title: '商品模板'
        }
      ],
      loading: false,
      query: {
        templateNumber: '',
        templateName: '',
        operationUser: '',
        total: 0,
        current: 1,
        size: 10  
      },
      dataList: []
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryTemplatePage(
        query.templateNumber,
        query.templateName,
        query.operationUser,
        query.current,
        query.size
      )
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
      this.loading = false
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        templateNumber: '',
        templateName: '',
        operationUser: '',
        total: 0,
        current: 1,
        size: 10
      }
    },
    //新增
    addClick(id) {
      this.$router.push({
        name: 'CommodityTemplateEstablish',
        params: {
          id: id
        }
      })
    },
    //删除
    deleteClick(row) {
      this.$confirm(`确定删除模板 ${row.templateName}？`, '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async () => {
        // 确认删除
        this.$common.showLoad()
        let data = await deleteTemplate(row.id)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        }
      })
      .catch(async () => {
        // 取消
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
