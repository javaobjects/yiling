<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商家名称</div>
              <el-input v-model="query.ename" placeholder="请输入商家名称" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="add">添加</yl-button>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="名称" min-width="100" align="center" prop="ename"></el-table-column>
          <el-table-column label="开通时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="accountManage(row)">结账账号设置</yl-button>
              <yl-button type="text" @click="commissionManage(row)">提成设置</yl-button>
              <yl-button type="text" @click="detail(row)">详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { enterpriseAccountList } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
export default {
  name: 'DrugMerchantsCommission',
  components: {
  },
  computed: {
  },
  filters: {
  },
  activated() {
    this.getList()
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        ename: ''
      },
      // 列表
      dataList: [],
      loading: false,
      channelTitle: '',
      channelType: 'ADD',
      editFormData: {}
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await enterpriseAccountList(
        query.current,
        query.size,
        query.ename
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        ename: ''
      }
    },
    //  添加
    add() {
      this.$router.push({
        name: 'AddDrugMerchants'
      })
    },
    //  提成设置
    commissionManage(row) {
      this.$router.push({
        name: 'MerchantsCommissionManage',
        params: { eid: row.eid}
      })
    },
    //  结账账号设置
    accountManage(row) {
      this.$router.push({
        name: 'MerchantsAccountManage',
        params: { id: row.id}
      })
    },
    //  详情
    detail(row) {
      this.$router.push({
        name: 'DrugMerchantsDetail',
        params: { id: row.id}
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>