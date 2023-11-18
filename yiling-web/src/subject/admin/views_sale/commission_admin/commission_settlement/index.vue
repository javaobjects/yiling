<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="last_order"></svg-icon>
            <span>累计佣金总额</span>
          </div>
          <div class="title">
            {{ totalAmount }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>待结算总金额</span>
          </div>
          <div class="title">
            {{ surplusAmount }}
          </div>
        </div>
        <div class="box">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="all_price"></svg-icon>
            <span>已结算总金额</span>
          </div>
          <div class="title">
            {{ paidAmount }}
          </div>
        </div>
      </div>
      <!-- 搜索条件 -->
      <div class="search-box mar-t-8">
        <el-row class="box">
          <el-col :span="8">
            <div class="title">获佣人</div>
            <el-input v-model="query.userName" @keyup.enter.native="searchEnter" placeholder="请输入姓名" />
          </el-col>
          <el-col :span="8">
            <div class="title">联系方式</div>
            <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入联系方式" />
          </el-col>
        </el-row>
        <div class="mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="downLoadTemp">导出</yl-button>
          <yl-button class="mar-l-16" type="primary" plain @click="goImport">导入</yl-button>
          <!-- <el-link class="mar-r-10" type="primary" :underline="false" @click="goImport">导入</el-link> -->
        </div>
      </div>
      <!-- 表格 -->
      <div class="mar-t-16">
        <yl-table :list="dataList" :total="total" :cell-class-name="() => 'border-1px-b'" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList" class="team-table" :show-header="true" stripe center>
          <el-table-column prop="name" label="获佣人" align="center"></el-table-column>
          <el-table-column prop="totalAmount" label="累计佣金总额" align="center"></el-table-column>
          <el-table-column prop="paidAmount" label="已结算佣金总额" align="center"></el-table-column>
          <el-table-column prop="surplusAmount" label="待结算佣金总额" align="center"></el-table-column>
          <el-table-column prop="mobile" label="联系方式" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showOrderDetail(row)">查看</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>

</template>

<script>
import { commissionList } from '@/subject/admin/api/views_sale/commission_admin';
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'CommissionSettlement',
  components: {},
  data() {
    return {
      query: {
        userName: '',
        mobile: '',
        page: 1,
        limit: 10
      },
      total: 100,
      loading: false,
      dataList: [],
      totalAmount: 0,
      surplusAmount: 0,
      paidAmount: 0

    }
  },
  created() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = Object.assign({}, {
        userName: '',
        mobile: '',
        page: 1,
        limit: 10
      })
    },
    // 佣金列表查询
    async getList() {
      this.loading = true
      let query = this.query
      let data = await commissionList(
        query.page,
        query.limit,
        query.userName,
        query.mobile
      )
      this.loading = false
      console.log(data);
      if (data) {
        this.dataList = data.records
        this.paidAmount = data.paidAmount ? data.paidAmount : 0 // 已结算
        this.surplusAmount = data.surplusAmount ? data.surplusAmount : 0 // 待结算
        this.totalAmount = data.totalAmount ? data.totalAmount : 0 // 累计总金额
        this.total = data.total
      }
    },
    // 导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad(
        {
          'className': 'commissionsExportService',
          'fileName': '导出佣金',
          'groupName': '佣金管理',
          'menuName': '佣金管理-佣金结算',
          'searchConditionList': [
            {
              'desc': '用户名',
              'name': 'username',
              'value': query.userName || ''
            },
            {
              'desc': '联系方式',
              'name': 'mobile',
              'value': query.mobile || ''
            }
          ]
        }
      )
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    showOrderDetail(row) {
      this.$router.push({
        name: 'CommissionSettlementDetail',
        params: { id: row.userId }
      })
    },
    // 去导入页面
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/salesAssistant/api/v1/commissions/importCommissions'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>