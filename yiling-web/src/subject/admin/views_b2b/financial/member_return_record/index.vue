<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">单号</div>
              <el-input v-model="query.summaryNo" placeholder="请输入单号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="8">
            <el-col :span="8">
              <div class="title">结算时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-col></el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="单号" min-width="240" align="center" prop="summaryNo">
          </el-table-column>
          <el-table-column label="结算时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="应结金额" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.settleAmount | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结算手续费" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.realFee | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="到账金额" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.realAmount | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结算状态" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.status | dictLabel(yeeSettleStatus) }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  yeeSettleQuerySettlementPageList
} from '@/subject/admin/api/b2b_api/financial'
import { yeeSettleStatus } from '@/subject/admin/busi/b2b/financial'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'MemberReturnRecord',
  components: {
  },
  computed: {
    yeeSettleStatus() {
      return yeeSettleStatus()
    }
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        summaryNo: '',
        time: []
      },
      dataList: []
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await yeeSettleQuerySettlementPageList(
        query.page,
        query.limit,
        query.summaryNo,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        total: 0,
        summaryNo: '',
        time: []
      }
    },
    // 导出
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'yeeMemberSettleExportServiceImpl',
        fileName: '导出会员回款记录',
        groupName: '财务管理',
        menuName: '财务管理-会员回款记录',
        searchConditionList: [
          {
            desc: '结算订单号',
            name: 'summaryNo',
            value: this.query.summaryNo || ''
          },
          {
            desc: '结算开始时间',
            name: 'createTimeBegin',
            value: this.query.time && this.query.time.length > 0 ? this.query.time[0] : ''
          },
          {
            desc: '结算结束时间',
            name: 'createTimeEnd',
            value: this.query.time && this.query.time.length > 1 ? this.query.time[1] : ''
          }
        ]
      })
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
