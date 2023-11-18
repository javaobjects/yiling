<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">授信主体名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入授信主体名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
              <el-select v-model="query.status" placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="启用" :value="1"></el-option>
                <el-option label="停用" :value="2"></el-option>
              </el-select>
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
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
        <div class="right-btn">
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column label="授信主体" min-width="139" align="center" prop="ename">
          </el-table-column>
          <el-table-column label="采购商名称" min-width="139" align="center" prop="customerName">
          </el-table-column>
          <el-table-column label="开始时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.startTime | formatDate('yyyy-MM-dd') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.endTime | formatDate('yyyy-MM-dd') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="信用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.totalAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="临时额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.temporaryAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 1)">{{ row.usedAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已还款额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 2)">{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="可使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.availableAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="60" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : 'col-up']">{{ row.status | enable }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上浮点位（%）" min-width="90" align="center" prop="upPoint">
          </el-table-column>
          <el-table-column label="信用周期（天）" min-width="90" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.period ? row.period : '- -' }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <order :visible.sync="showDialog1" :is-hk="isHk" :data="showInfo" :id="showInfo.id">
      </order>
    </div>
  </div>
</template>

<script>
import {
  getPeriodList
} from '@/subject/admin/api/zt_api/period'
import { createDownLoad } from '@/subject/admin/api/common'
import order from './component/order'
export default {
  name: 'BuyerList',
  components: {
    order
  },
  computed: {
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        status: 0
      },
      // 列表
      dataList: [],
      loading: false,
      // 展示列表弹框附带信息
      showInfo: {},
      // 是否展示弹框
      showDialog1: false,
      // 点击弹框是都是还款订单
      isHk: false
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
      let data = await getPeriodList(
        query.page,
        query.limit,
        query.status,
        query.name
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
        limit: 10,
        status: 0
      }
    },
    // 展示订单列表弹框
    showAmountDialog(row, type) {
      this.showInfo = this.$common.clone(row)
      if (type === 1) {
        this.isHk = false
      } else {
        this.isHk = true
      }
      this.showDialog1 = true
    },
    // 导出明细
    async downLoadTemp() {
      this.$common.showLoad()
      let query = this.query
      let data = await createDownLoad({
        className: 'b2bCustomerPaymentDaysExportService',
        fileName: '采购商账期列表',
        groupName: '采购商账期列表',
        menuName: '账期管理-采购商账期列表',
        searchConditionList: [
          {
            desc: '授信主体名称',
            name: 'ename',
            value: query.name || ''
          },
          {
            desc: '状态',
            name: 'status',
            value: query.status
          }
        ]
      })
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
