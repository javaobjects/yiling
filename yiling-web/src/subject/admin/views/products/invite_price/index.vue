<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">批准文号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号" />
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
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
      <div class="down-box">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" @click="exportExcel">
            导出商品信息
          </el-link>
          <el-link class="mar-r-10" type="primary" :underline="false" @click="goImport">商品信息导入</el-link>
        </div>
      </div>

      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="120" label="商品名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="批准文号" prop="licenseNo">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="包装规格" prop="specifications">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="生产企业" prop="manufacturer">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="红线价">
            <template slot-scope="{ row }">
              <span>已设置 <span class="col-theme">{{ row.locationCount }}</span> 省份</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getInviteList } from '@/subject/admin/api/products'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'InvitePrice',
  components: {
  },
  computed: {
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0
      },
      dataList: [],
      loading: false
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
      let data = await getInviteList(
        query.page,
        query.limit,
        query.name,
        query.licenseNo,
        query.manufacturer
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      }
    },
    // 编辑
    edit(row) {
      this.$router.push(`/products/invite_price_detail/${row.id}`)
    },
    // 导出信息
    async exportExcel() {
      this.$common.showLoad()
      let query = this.query
      let data = await createDownLoad(
        'goodsBiddingPricePageListExportService',
        '商品信息',
        '供应商商品导出',
        '管理后台 - 商品管理',
        [
          {
            desc: '商品名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '批准文号',
            name: 'licenseNo',
            value: query.licenseNo || ''
          },
          {
            desc: '生产厂家',
            name: 'manufacturer',
            value: query.manufacturer || ''
          }
        ]
      )
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导入信息
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/pop/api/v1/admin/goods/importGoodsBiddingPrice'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
