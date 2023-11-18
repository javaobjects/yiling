<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="企业名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_3' | template">
            下载模板
          </el-link>
          <el-link class="mar-r-10" type="primary" :underline="false" @click="goImport">供应商商品导入</el-link>
        </div>
      </div>
      <div class="mar-t-10">
        <yl-table :list="dataList" :cell-class-name="() => 'border-1px-b'" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base">
                  <span>企业ID：{{ row.id }}</span>
                  <span>企业状态：<span :class="row.status === 1 ? 'col-down' : 'col-up'">{{ row.status | enable }}</span></span>
                </div>
                <div class="content flex-row-left">
                  <div class="table-item">
                    <div class="item font-size-base font-title-color"><span class="font-light-color">企业名称：</span>{{ row.name }}</div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color">企业类型：</span>{{ row.type | dictLabel(companyType) }}</div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color">社会信用统一代码：</span>{{ row.licenseNumber }}</div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color">企业地址：</span>{{ row.address }}</div>
                  </div>
                  <div class="table-item">
                    <div class="item font-size-base font-title-color"><span class="font-light-color">联系人：</span>{{ row.contactor }}</div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color">联系电话：</span>{{ row.contactorPhone }}</div>
                    <div class="item font-size-base font-title-color"></div>
                    <div class="item font-size-base font-title-color"></div>
                  </div>
                  <div class="flex1 font-size-base">
                    <div class="product-num">
                      <div>商品信息</div>
                      <div>
                        <span class="font-light-color">商品数量：{{ row.total }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="flex-column-center edit-btn">
                    <yl-button type="text" @click="showDetail(row, $index)">查看详情</yl-button>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getProductList } from '@/subject/admin/api/zt_api/products'
import { enterpriseType } from '@/subject/admin/utils/busi'

export default {
  name: 'ZtProductsIndex',
  components: {
  },
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      total: 0
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
      let data = await getProductList(
        query.page,
        query.limit,
        query.name
      )
      this.$log(data)
      this.loading = false
      if (data) {
        this.dataList = data.records.map(item => {
          item.total = item.auditPassCount + item.underReviewCount + item.rejectCount
          return item
        })
        this.total = data.total
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
    // 去导入页面
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/admin/goods/import'
        }
      })
    },
    // 查看详情
    showDetail(row, index) {
      this.$router.push(`zt_products_detail/${row.id}/${row.name}`)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
