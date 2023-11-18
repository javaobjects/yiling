<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">批准文号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号/注册证编号" />
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品状态</div>
              <el-select v-model="query.goodsStatus" placeholder="请选择商品状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-show="item.value != 5 && item.value != 6 && item.value != 4 && item.value != 3"
                  v-for="item in goodStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="top-title">
        <span>商品列表</span>
        <span></span>
      </div>
      <div class="my-table">
        <yl-table 
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="商品ID" align="center" prop="id"></el-table-column>
          <el-table-column label="商品信息" prop="id" min-width="160">
              <template slot-scope="{ row }">
                <div>
                  <div class="products-box">
                    <el-image class="my-image" :src="row.pic" fit="contain" />
                    <div class="products-info">
                      <div>{{ row.name }}</div>
                      <div>{{ row.sellSpecifications }}</div>
                      <div>{{ row.licenseNo }}</div>
                      <div>{{ row.manufacturer }}</div>
                    </div>
                  </div>
                </div>
              </template>
          </el-table-column>
          <el-table-column label="商品状态" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="每单限购" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.orderRestrictionQuantity === 0 ? '无限制' : row.orderRestrictionQuantity }}</div>
            </template>
          </el-table-column>
          <el-table-column label="时间内总限购" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.timeRestrictionQuantity === 0 ? '无限制' : row.timeRestrictionQuantity }}</div>
            </template>
          </el-table-column>
          <el-table-column label="限购时间" align="center" min-width="200">
            <template slot-scope="{ row }">
              <div v-if="row.timeType === 1 && row.startTime">{{ row.startTime | formatDate }} - {{ row.endTime | formatDate }} </div>
              <div v-if="row.timeType !== 1">{{ row.timeType | reviewStatus }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div>
                  <yl-button v-role-btn="['1']" type="text" @click="showDetailPage(row)">编辑</yl-button>
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
import { queryGoodsPage } from '@/subject/pop/api/b2b_api/purchase_restric'
import { goodsStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'PurchaseRestric',
  components: {},
  computed: {
    goodStatus() {
      return goodsStatus()
    }
  },
  filters: {
    reviewStatus(e) {
      let res
      switch (e) {
        case 1:
          res = '自定义'
          break;
        case 2:
          res = '每天'
          break;
        case 3:
          res = '每周'
          break;
        case 4:
          res = '每月'
          break;
        default:
          res = '- -'
          break;
      }
      return res
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '限购管理'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10,
        goodsStatus: 0,
        manufacturer: '',
        name: ''
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
    async getList(type) {
      this.loading = true
      let query = this.query
      let data = await queryGoodsPage(
        query.page,
        query.limit,
        '',
        query.name,
        query.licenseNo,
        query.manufacturer,
        query.goodsStatus
      )
      this.loading = false
      if (data !== undefined) {
        this.dataList = data.records
        this.query.total = data.total
      }
      console.log('this.query.total', this.query.total);
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        goodsStatus: '',
        manufacturer: '',
        name: ''
      }
    },
    selectChange(value) {
      this.$log(value)
    },
    // 查看编辑
    showDetailPage(row) {
      this.$router.push({
        path: `/b2b_products/purchase_restric_detail/${row.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
