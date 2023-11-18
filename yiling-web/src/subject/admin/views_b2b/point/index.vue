<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入采购商名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
      </div>
      <div>
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="采购商名称" min-width="139" align="center" prop="customerName">
          </el-table-column>
          <el-table-column label="订单号" min-width="139" align="center" prop="orderNo">
          </el-table-column>
          <el-table-column label="" min-width="139" align="center" :key="Math.random()">
            <template slot="header">
              <div>积分值（分）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.integralValue)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.integralValue }}</span>
            </template>
          </el-table-column>
          <el-table-column label="时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="积分类型" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.integralType == 1 ? '订单送积分' : '- -' }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getPointList,
  getPointTotal
} from '@/subject/admin/api/b2b_api/point'
export default {
  name: 'PointList',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '积分管理',
          path: ''
        },
        {
          title: '积分列表'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10,
        createTime: []
      },
      // 列表
      dataList: [],
      loading: false,
      // 统计数据
      totalData: {}
    }
  },
  activated() {
    this.getList()
    this.getTotal()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取统计数据
    async getTotal() {
      let query = this.query
      let data = await getPointTotal(
        query.page,
        query.limit,
        query.name,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
      )
      this.totalData = {
        integralValue: data == 0 ? '- -' : data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPointList(
        query.page,
        query.limit,
        query.name,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
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
      this.getTotal()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        createTime: []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
