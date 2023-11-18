<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">日期段</div>
              <el-date-picker
                v-model="query.timeRange"
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
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box mar-t-16">
        <div class="total-box">
          合计：
          <div class="item">
            <span class="title">H5页面打开数：</span>
            <span class="value">{{ totalData.pageView | toThousand('') }}</span>
          </div>
          <div class="item">
            <span class="title">公众号关注数：</span>
            <span class="value">{{ totalData.follow | toThousand('') }}</span>
          </div>
          <div class="item">
            <span class="title">注册成功数：</span>
            <span class="value">{{ totalData.register | toThousand('') }}</span>
          </div>
        </div>
        <div class="table-box mar-t-16">
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
            <el-table-column label="日期" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.createTime | formatDate('yyyy-MM-dd') }} </span>
              </template>
            </el-table-column>
            <el-table-column label="H5页面打开数" min-width="120" align="center" prop="pageView"> </el-table-column>
             <el-table-column label="广告位点击次数" min-width="120" align="center" prop="adClick"> </el-table-column>
            <el-table-column label="公众号关注数" min-width="100" align="center" prop="follow"> </el-table-column>
            <el-table-column label="注册数" min-width="100" align="center" prop="register"> </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { QrCodeStatistics, QrCodeStatisticsTotal } from '@/subject/admin/api/cmp_api/insurance_statistical'
export default {
  name: 'QrcodeStatistical',
  components: {
  },
  computed: {
    // 默认时间
    timeDefault () {
        let date = new Date()
        // 通过时间戳计算
        let defalutStartTime = (new Date(date.getFullYear(), date.getMonth() - 1, 1)).getTime() // 转化为时间戳
        let defalutEndTime = date.getTime() - 24 * 3600 * 1000
        let startDateNs = new Date(defalutStartTime)
        let endDateNs = new Date(defalutEndTime)
        // 月，日 不够10补0
        defalutStartTime = startDateNs.getFullYear() + '-' + ((startDateNs.getMonth() + 1) >= 10 ? (startDateNs.getMonth() + 1) : '0' + (startDateNs.getMonth() + 1)) + '-' + (startDateNs.getDate() >= 10 ? startDateNs.getDate() : '0' + startDateNs.getDate())
        defalutEndTime = endDateNs.getFullYear() + '-' + ((endDateNs.getMonth() + 1) >= 10 ? (endDateNs.getMonth() + 1) : '0' + (endDateNs.getMonth() + 1)) + '-' + (endDateNs.getDate() >= 10 ? endDateNs.getDate() : '0' + endDateNs.getDate())
        return [defalutStartTime, defalutEndTime]
    }
  },
  filters: {
  },
  activated() {
    this.getList()
    this.getTotal()
  },
  created() {
    this.query.timeRange = this.timeDefault;
  },
  data() {
    return {
      query: {
        current: 1,
        size: 10,
        timeRange: []
      },
      // 列表
      dataList: [],
      totalData: {},
      loading: false
    }
  },

  methods: {
    async getTotal() {
      let query = this.query
      let data = await QrCodeStatisticsTotal(
        query.timeRange && query.timeRange.length ? query.timeRange[0] : '',
        query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : ''
      )
      if (data) {
        this.totalData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await QrCodeStatistics(
        query.current,
        query.size,
        query.timeRange && query.timeRange.length ? query.timeRange[0] : '',
        query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : ''
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
      this.getTotal()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        timeRange: []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>