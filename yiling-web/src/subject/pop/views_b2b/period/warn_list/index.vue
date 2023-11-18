<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入采购商名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">还款状态</div>
              <el-select
                v-model="query.status"
                placeholder="请选择还款状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in repaymentStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.value ==3"
                >
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">发货时间</div>
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
        <div class="mar-t-8 pad-t-16">
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
        <div class="right-btn">
          <yl-button v-role-btn="['1']" type="primary" @click="downLoadTemp" plain>导出查询结果</yl-button>
        </div>
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
          <el-table-column label="订单号" min-width="139" align="center" prop="orderNo">
          </el-table-column>
          <el-table-column label="采购商名称" min-width="139" align="center" prop="customerName">
          </el-table-column>
          <el-table-column label="发货时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.deliveryTime | formatDate('yyyy-MM-dd') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="应还款日期" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.expirationTime | formatDate('yyyy.MM.dd') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="139" align="center" :key="Math.random()">
            <template slot="header">
              <div>订单金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.orderAmount, '￥')}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.orderAmount | toThousand }}</span>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="139" align="center" :key="Math.random()">
            <template slot="header">
              <div>退款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.returnAmount, '￥')}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.returnAmount | toThousand }}</span>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="139" align="center" :key="Math.random()">
            <template slot="header">
              <div>已还款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.repaymentAmount, '￥')}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.repaymentAmount | toThousand }}</span>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="139" align="center" :key="Math.random()">
            <template slot="header">
              <div>待还款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.unRepaymentAmount, '￥')}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.unRepaymentAmount | toThousand }}</span>
            </template>
          </el-table-column>
          <el-table-column label="还款状态" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span :style="{ 'color': row.repaymentStatus == 1 ? '#F26313' : '' }">{{ row.repaymentStatus | dictLabel(repaymentStatus) }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getPeriodWarnList, getWarnTotalData } from '@/subject/pop/api/b2b_api/period'
import { createDownLoad } from '@/subject/pop/api/common'
import { repayStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'B2bWarn',
  components: {
  },
  computed: {
    // 账期状态
    repaymentStatus() {
      return repayStatus()
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
          title: '账期管理',
          path: ''
        },
        {
          title: '账期到期提醒'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10,
        status: 0,
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
    this.getTotalData()
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
      let data = await getPeriodWarnList(
        query.page,
        query.limit,
        query.name,
        '',
        query.status,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 获取统计数据
    async getTotalData() {
      let query = this.query
      let data = await getWarnTotalData(
        query.name,
        '',
        query.status,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
      )
      if (data) {
        this.totalData = data
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
      this.getTotalData()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        status: 0,
        createTime: []
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2bPaymentDaysOrderExportService',
        fileName: '账期到期提醒',
        groupName: '账期到期提醒',
        menuName: '账期管理-账期到期提醒',
        searchConditionList: [
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.name || ''
          },
          {
            desc: '授信主体名称',
            name: 'sellerEname',
            value: query.ename
          },
          {
            desc: '还款状态',
            name: 'repaymentStatus',
            value: query.status
          },
          {
            desc: '下单开始时间',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
