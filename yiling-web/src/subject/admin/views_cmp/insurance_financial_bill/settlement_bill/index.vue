<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="header-bar">
          <div class="sign"></div>
          {{ detailData.ename }}——收款账户信息
        </div>
        <div class="header-box mar-t-8">
          <div class="table-bar">
            <el-row>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">账户类型：<span class="item-value">{{ detailData.accountType | dictLabel(hmcEnterpriseAccountType) }}</span></div>
                </div>
                <div class="item">
                  <div class="item-title">账户名：<span class="item-value">{{ detailData.accountName }}</span></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">账户：<span class="item-value">{{ detailData.accountNumber }}</span></div>
                </div>
                <div class="item">
                  <div class="item-title">开户行：<span class="item-value">{{ detailData.accountBank }}</span></div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <div class="select-bar mar-t-16">
        <div class="item" v-for="item in selectBarList" :key="item.value" @click="selectItem(item)">
          <div :class="['item-title', item.value === query.terminalSettleStatus ? 'active' : '']">{{ item.label }}</div>
          <div class="item-line" v-show="item.value === query.terminalSettleStatus"></div>
        </div>
      </div>
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入商品名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6" v-if="query.terminalSettleStatus === 1">
              <div class="title">创建日期段</div>
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
            <el-col :span="6" v-if="query.terminalSettleStatus === 2">
              <div class="title">结算日期段</div>
              <el-date-picker
                v-model="query.payTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="6" v-if="query.terminalSettleStatus === 1">
              <div class="title">保司结算状态</div>
              <el-select v-model="query.insuranceSettlementStatus" placeholder="请选择保司结算状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in hmcInsuranceSettleStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" v-if="query.terminalSettleStatus === 2" :href="'NO_9' | template"> 下载模板 </el-link>
          <ylButton type="primary" plain @click="downLoadTemp('all')" v-if="query.terminalSettleStatus === 1">全部导出</ylButton>
          <ylButton type="primary" plain @click="downLoadTemp" v-if="query.terminalSettleStatus === 1" :disabled="multipleSelection.length > 0 ? false : true">仅选中的导出</ylButton>
          <ylButton type="primary" plain @click="goImport" v-if="query.terminalSettleStatus === 2">导入结账数据</ylButton>
        </div>
      </div>
      <div class="common-box mar-t-8" v-if="query.terminalSettleStatus === 1">
        <div class="total-box">
          合计：
          <div class="item">
            <span class="title">共</span>
            <span class="value">{{ totalCount | toThousand('') }}</span>
            <span class="title">笔订单</span>
          </div>
          <div class="item">
            <span class="title">合计结算额：</span>
            <span class="value">{{ totalAmount | toThousand('') }}</span>
            <span class="title">元</span>
          </div>
        </div>
        <div class="table-box mar-t-16">
          <yl-table
            key="table1"
            stripe
            :show-header="true"
            :list="dataList"
            :total="query.total"
            :page.sync="query.current"
            :limit.sync="query.size"
            :loading="loading"
            @getList="getList"
            :selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="50"> </el-table-column>
            <el-table-column label="商品名称" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div> {{ row.goodsName }} </div>
                <div> 规格: {{ row.goodsSpecifications }} </div>
              </template>
            </el-table-column>
            <el-table-column label="售卖数量" width="80" align="center" prop="goodsQuantity"></el-table-column>
            <el-table-column label="结算单价" width="80" align="center" prop="price"></el-table-column>
            <el-table-column label="订单编号" min-width="150" align="center" prop="orderNo"></el-table-column>
            <el-table-column label="订单明细编号" min-width="100" align="center" prop="id"></el-table-column>
            <el-table-column label="合计" min-width="100" align="center" prop="goodsAmount"></el-table-column>
            <el-table-column label="创建日期" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.createTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="订单完成日期" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.finishTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="管控渠道" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div v-for="(item, index) in row.channelNameList" :key="index"> {{ item }} </div>
              </template>
            </el-table-column>
            <el-table-column label="保司结算状态" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.insuranceSettlementStatus | dictLabel(hmcInsuranceSettleStatus) }} </span>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="common-box mar-t-8" v-if="query.terminalSettleStatus === 2">
        <div class="total-box">
          合计：
          <div class="item">
            <span class="title">共</span>
            <span class="value">{{ totalCount | toThousand('') }}</span>
            <span class="title">笔订单</span>
          </div>
          <div class="item">
            <span class="title">合计结算额：</span>
            <span class="value">{{ totalAmount | toThousand('') }}</span>
            <span class="title">元</span>
          </div>
        </div>
        <div class="table-box mar-t-16">
          <yl-table
            key="table2"
            stripe
            :show-header="true"
            :list="dataList"
            :total="query.total"
            :page.sync="query.current"
            :limit.sync="query.size"
            :loading="loading"
            @getList="getList"
          >
            <el-table-column label="商品名称" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div> {{ row.goodsName }} </div>
                <div> 规格: {{ row.goodsSpecifications }} </div>
              </template>
            </el-table-column>
            <el-table-column label="数量" min-width="100" align="center" prop="goodsQuantity"></el-table-column>
            <el-table-column label="结算单价" min-width="100" align="center" prop="price"></el-table-column>
            <el-table-column label="订单编号" min-width="150" align="center" prop="orderNo"></el-table-column>
            <el-table-column label="订单明细编号" min-width="100" align="center" prop="id"></el-table-column>
            <el-table-column label="合计" min-width="100" align="center" prop="goodsAmount"></el-table-column>
            <el-table-column label="结账金额" min-width="100" align="center" prop="settlementAmount"></el-table-column>
            <el-table-column label="订单完成日期" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.finishTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="对账执行时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.executionTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="结算完成时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.settlementTime | formatDate }} </span>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { enterpriseAccountDetail } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { getEnterpriseSettlementDetail } from '@/subject/admin/api/cmp_api/insurance_financial_bill'
import { createDownLoad } from '@/subject/admin/api/common'
import { hmcInsuranceSettleStatus, hmcTerminalSettleStatus } from '@/subject/admin/busi/cmp/insurance_order_manage'
import { hmcEnterpriseAccountType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

export default {
  name: 'SettlementBill',
  components: {
  },
  computed: {
    // 保司结算状态
    hmcInsuranceSettleStatus() {
      return hmcInsuranceSettleStatus()
    },
    // 药品终端结算状态
    hmcTerminalSettleStatus() {
      return hmcTerminalSettleStatus()
    },
    // 账户类型
    hmcEnterpriseAccountType() {
      return hmcEnterpriseAccountType()
    }
  },
  filters: {
  },
  mounted() {
    this.id = this.$route.params.id
    this.eid = this.$route.params.eid
    if (this.id) {
      this.getDetail()
      this.getList()
    }
  },
  data() {
    return {
      selectBarList: [
        { label: '待结账', value: 1 },
        { label: '已完结', value: 2 }
      ],
      query: {
        current: 1,
        size: 10,
        total: 0,
        terminalSettleStatus: 1,
        goodsName: '',
        createTime: [],
        payTime: [],
        insuranceSettlementStatus: 0
      },
      detailData: {},
      // 列表
      dataList: [],
      totalAmount: '',
      totalCount: '',
      loading: false,
      // 多选
      multipleSelection: [],
      importDialog: false
    }
  },
  methods: {
    selectItem(item) {
      if (item.value !== this.query.terminalSettleStatus) {
        this.query.terminalSettleStatus = item.value
        if (this.query.terminalSettleStatus === 1) {
          this.query.goodsName = ''
          this.query.payTime = []
        } else if (this.query.terminalSettleStatus === 2) {
          this.query.goodsName = ''
          this.query.createTime = []
          this.query.insuranceSettlementStatus = 0
        }
        this.getList()
      }
    },
    async getDetail() {
      let data = await enterpriseAccountDetail(this.id)
      if (data) {
        this.detailData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getEnterpriseSettlementDetail(
        query.current,
        query.size,
        this.eid,
        query.terminalSettleStatus,
        query.goodsName,
        query.createTime && query.createTime.length ? query.createTime[0] : '',
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : '',
        query.payTime && query.payTime.length ? query.payTime[0] : '',
        query.payTime && query.payTime.length > 1 ? query.payTime[1] : '',
        query.insuranceSettlementStatus
      )
      this.loading = false
      if (data) {
        this.dataList = data.page.records
        this.totalAmount = data.totalAmount
        this.totalCount = data.totalCount
        query.total = data.page.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        goodsName: '',
        createTime: [],
        payTime: [],
        insuranceSettlementStatus: '',
        terminalSettleStatus: 1
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async downLoadTemp(type) {
      let exportData = type === 'all' ? '' : this.multipleSelection.map(item => item.id).join(',')
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcEnterpriseSettlementExportService',
        fileName: '导出药品待对账',
        groupName: '药品待对账导出',
        menuName: '药品待对账',
        searchConditionList: [
          {
            desc: '仅选中导出需要的查询',
            name: 'idList',
            value: exportData
          },
          {
            desc: '药品服务终端id',
            name: 'eid',
            value: this.eid || ''
          },
          {
            desc: '药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单',
            name: 'terminalSettleStatus',
            value: query.terminalSettleStatus || ''
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName
          },
          {
            desc: '保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结',
            name: 'insuranceSettlementStatus',
            value: query.insuranceSettlementStatus
          },
          {
            desc: '创建开始时间',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '投保日期止',
            name: 'stopTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导入信息
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/hmc/api/v1/settlement/enterprise/importEnterpriseSettlement',
          extralData: {
            eid: this.eid
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
