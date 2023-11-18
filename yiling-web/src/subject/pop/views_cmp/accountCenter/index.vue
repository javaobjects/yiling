<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="top-box">
        <div class="top-box-l">
          <div class="top-box-title"> <img src="../../assets/cmp-account/item-1.png" alt=""> 待结账</div>
          <div class="top-box-price">¥43534534534.000</div>
        </div>
        <div class="top-box-r">
          <div class="top-box-title"><img src="../../assets/cmp-account/item-2.png" alt=""> 累积打款总额</div>
          <div class="top-box-price">¥43534534534.000</div>
        </div>
      </div>
      <div class="order-tab mar-t-16">
        <div class="order-tab-item" :class="[ activeIndex == 0 ?'order-active':'' ]" @click="clickTab(0)">待结账</div>
        <div class="order-tab-item" :class="[ activeIndex == 1 ?'order-active':'']" @click="clickTab(1)">已结算打款</div>
      </div>
      <!-- table  -->
      <div v-show="activeIndex ==0" class="mar-t-8">
        <div class="common-box  mar-t-8">
          <div class="search-box" style="margin-top:0;">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">日期段</div>
                <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="my-table pad-l-16 pad-r-16">
          <yl-table :show-header="true" border stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
            <el-table-column label="商品名称" align="center"></el-table-column>
            <el-table-column label="售卖数量" align="center"></el-table-column>
            <el-table-column label="结算单价" align="center"></el-table-column>
            <el-table-column label="订单编号" align="center"></el-table-column>
            <el-table-column label="订单明细编号" align="center"></el-table-column>
            <el-table-column label="合计" align="center"></el-table-column>
            <el-table-column label="创建日期" align="center"></el-table-column>
            <el-table-column label="订单完成日期" align="center"></el-table-column>
            <el-table-column label="管控渠道" align="center"></el-table-column>
          </yl-table>
        </div>
      </div>
      <div v-show="activeIndex ==1" class="mar-t-8">
        <div class="common-box  mar-t-8">
          <div class="search-box" style="margin-top:0;">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">打款日期段</div>
                <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="my-table pad-l-16 pad-r-16">
          <yl-table :show-header="true" border stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
            <el-table-column label="商品名称" align="center"></el-table-column>
            <el-table-column label="售卖数量" align="center"></el-table-column>
            <el-table-column label="结算单价" align="center"></el-table-column>
            <el-table-column label="订单编号" align="center"></el-table-column>
            <el-table-column label="订单明细编号" align="center"></el-table-column>
            <el-table-column label="合计" align="center"></el-table-column>
            <el-table-column label="创建日期" align="center"></el-table-column>
            <el-table-column label="订单完成日期" align="center"></el-table-column>
            <el-table-column label="打款日期" align="center"></el-table-column>
            <el-table-column label="结算额" align="center"></el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCompanyProductList } from '@/subject/pop/api/products'
import { goodsStatus, goodsOutReason } from '@/subject/pop/utils/busi'
export default {
  name: 'CmpProductsIndex',
  components: {
  },
  computed: {
    goodsReason() {
      return goodsOutReason()
    },
    goodStatus() {
      return goodsStatus()
    }
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
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表'
        }
      ],
      activeIndex: 0,
      query: {
        page: 1,
        limit: 20,
        name: '',
        // 全部，已上架，已下架 ，带设置
        status: 0
      },
      dataList: [
        {
          a: '药瓶名称',
          b: '比萘芬喷雾剂 30ml:1%',
          c: 'sdfsdsfdfsds',
          goodsStatus: 2
        }
      ],
      loading: false,
      total: 100,
      showDialog: false,
      form: {},
      query1: {
        page: 1,
        limit: 20
      },
      total1: 0,
      loading1: false,
      stockDetailList: [
        { a: 10000, b: 3232 },
        { a: 10000, b: 3232 },
        { a: 10000, b: 3232 }
      ]

    }
  },
  activated() {
    // this.getCate()
    // this.chooseProduct = []
    // this.getList()
  },
  created() {
  },
  mounted() {
  },
  methods: {
    
    clickTab(e) {
      this.activeIndex = e;
      // this.query.status = e;
      // this.query.page = 1
      // this.query.limit = 10
      // this.getList()
    },
    //  获取商品列表
    async getList(type) {
      this.dataList = []
      this.loading = true
      let query = this.query
      let data = await getCompanyProductList(
        query.page,
        query.limit,
        query.status, // 状态
        query.name
      )
      this.loading = false
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    //  获取商品列表
    async getList1(type) {
      this.dataList1 = []
      this.loading1 = true
      let query = this.query1
      let data = await getCompanyProductList(
        query.page,
        query.limit
      )
      this.loading1 = false
      console.log(data);
      if (data !== undefined) {
        this.dataList1 = data.records
        this.total1 = data.total
      }
    },

    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        status: ''
      }
    },

    // 展示库存弹框
    showDetail(row) {
      this.showDialog = true
    },

    confirm() {
      this.showDialog = false;
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
