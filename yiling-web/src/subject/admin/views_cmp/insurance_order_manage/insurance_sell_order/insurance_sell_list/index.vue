<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="select-bar">
        <div class="item" v-for="item in selectBarList" :key="item.value" @click="selectItem(item)">
          <div :class="['item-title', item.value === searchType ? 'active' : '']">{{ item.label }}</div>
          <div class="item-line" v-show="item.value === searchType"></div>
        </div>
      </div>
      <div class="common-box box-search" v-if="searchType === 1">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保险提供商</div>
              <el-select v-model="query.insuranceCompanyId" placeholder="请选择保险提供商">
                <el-option
                  v-for="item in providerOptions"
                  :key="item.id"
                  :label="item.companyName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">保险名称</div>
              <el-input v-model="query.insuranceName" placeholder="请输入保险名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">来源类型</div>
              <el-select v-model="query.sourceType" placeholder="请选择来源类型">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcSourceType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">保单来源终端</div>
              <el-input v-model="query.terminalName" placeholder="请输入保单来源终端" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保司保单号</div>
              <el-input v-model="query.policyNo" placeholder="请输入保司保单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select v-model="query.policyStatus" placeholder="请选择状态">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcPolicyStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">定额方案类型</div>
              <el-select v-model="query.billType" placeholder="请选择定额方案类型">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcBillType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">投保人姓名</div>
              <el-input v-model="query.holderName" placeholder="请输入投保人姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">被保人姓名</div>
              <el-input v-model="query.issueName" placeholder="请输入被保人姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">	投/被保人手机号</div>
              <el-input v-model="query.issuePhone" placeholder="请输入投/被保人手机号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">销售员姓名</div>
              <el-input v-model="query.sellerName" placeholder="请输入销售员姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">投保日期</div>
              <el-date-picker
                v-model="query.proposalTime"
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
      <div class="common-box box-search" v-if="searchType === 2">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保险提供商</div>
              <el-select v-model="query.insuranceCompanyId" placeholder="请选择保险提供商">
                <el-option
                  v-for="item in providerOptions"
                  :key="item.id"
                  :label="item.companyName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">保险名称</div>
              <el-input v-model="query.insuranceName" placeholder="请输入保险名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">来源类型</div>
              <el-select v-model="query.sourceType" placeholder="请选择来源类型">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcSourceType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">保单来源终端</div>
              <el-input v-model="query.terminalName" placeholder="请输入保单来源终端" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保司保单号</div>
              <el-input v-model="query.policyNo" placeholder="请输入保司保单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select v-model="query.policyStatus" placeholder="请选择状态">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcPolicyStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">定额方案类型</div>
              <el-select v-model="query.billType" placeholder="请选择定额方案类型">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcBillType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">投保人姓名</div>
              <el-input v-model="query.holderName" placeholder="请输入投保人姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">被保人姓名</div>
              <el-input v-model="query.issueName" placeholder="请输入被保人姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">投/被保人手机号</div>
              <el-input v-model="query.issuePhone" placeholder="请输入投/被保人手机号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">销售员姓名</div>
              <el-input v-model="query.sellerName" placeholder="请输入销售员姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">交易流水号</div>
              <el-input v-model="query.transactionId" placeholder="请输入交易流水号" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">交易日期</div>
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
          <ylButton type="primary" plain v-if="searchType === 1" @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton type="primary" plain v-if="searchType === 2" @click="downLoadTemp1">导出查询结果</ylButton>
        </div>
      </div>
      <div class="table-box mar-t-8">
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
          <el-table-column label="保险详情" min-width="860" align="left" prop="name">
            <template slot-scope="{ row }">
              <div class="insurancce-detail-wrap">
                <div class="title">{{ row.insuranceName }}</div>
                <div class="insurancce-detail">
                  <div class="list-col">
                    <div class="list-item"> 保司保单号：<span class="list-content"> {{ row.policyNo }} </span> </div>
                    <div class="list-item"> {{ searchType === 1 ? "平台单号：" : "销售交易单号：" }}<span class="list-content"> {{ row.orderNo }} </span> </div>
                    <div class="list-item"> 保司：<span class="list-content"> {{ row.companyName }} </span> </div>
                    <div class="list-item"> 定额方案类型：<span class="list-content"> {{ row.billType | dictLabel(hmcBillType) }} </span> </div>
                  </div>
                  <div class="list-col">
                    <div class="list-item"> 销售员所属企业：<span class="list-content"> {{ row.sellerEName || "- -" }} </span> </div>
                    <div class="list-item">销售员：
                      <span class="list-content">{{ row.sellerUserName || "- -" }} </span>
                      <span class="list-content" v-if="row.sellerUserName">【ID：{{ row.sellerUserId }}】</span>
                    </div>
                    <div class="list-item"> 来源类型：<span class="list-content"> {{ sourceTypeFormat(row.sellerUserId) }} </span> </div>
                    <div class="list-item" v-if="searchType === 1"> 累计支付金额：<span class="list-content"> {{ row.totalPayMoney | toThousand("¥") }} </span> </div>
                    <div class="list-item" v-if="searchType === 2"> 支付金额：<span class="list-content"> {{ row.amount | toThousand("¥") }} </span> </div>
                  </div>
                  <div class="list-col">
                    <div class="list-item" v-if="searchType === 1"> 投保时间：<span class="list-content"> {{ row.proposalTime | formatDate }} </span> </div>
                    <div class="list-item" v-if="searchType === 2"> 交易时间：<span class="list-content"> {{ row.payTime | formatDate }} </span> </div>
                    <div class="list-item"> 被保人：
                      <span class="list-content"> {{ row.issueName }} </span>
                      <span class="list-content"> {{ row.issuePhone }} </span>
                    </div>
                    <div class="list-item"> 投保人：
                      <span class="list-content"> {{ row.holderName }} </span>
                      <span class="list-content"> {{ row.holderPhone }} </span>
                    </div>
                    <div class="list-item"> 兑付次数：<span class="list-content"> {{ row.cashTimes }} </span> </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center" prop="loginStatus">
            <template slot-scope="{ row }">
              <span>{{ row.policyStatus | dictLabel(hmcPolicyStatus) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
                <yl-button type="text" @click="detail(row)">详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { insuranceRecordCompanyList, insuranceRecordList, insurancePayList } from '@/subject/admin/api/cmp_api/insurance_order_manage'
import { createDownLoad } from '@/subject/admin/api/common'
import { hmcSourceType, hmcPolicyStatus, hmcBillType } from '@/subject/admin/busi/cmp/insurance_order_manage'

export default {
  name: 'InsuranceSellOrder',
  components: {
  },
  computed: {
    // 来源类型-列表中数据转换
    sourceTypeFormat() {
      return function(type) {
        return type === 0 ? '线上渠道' : '线下渠道'
      }
    },
    // 来源类型
    hmcSourceType() {
      return hmcSourceType()
    },
    // 状态
    hmcPolicyStatus() {
      return hmcPolicyStatus()
    },
    // 定额方案类型
    hmcBillType() {
      return hmcBillType()
    }
  },
  filters: {
  },
  data() {
    return {
      selectBarList: [
        { label: '按保单查', value: 1 },
        { label: '按交易记录查', value: 2 }
      ],
      providerOptions: [],
      searchType: 1, // 查询方式
      query: {
        current: 1,
        size: 10,
        insuranceName: '',
        insuranceCompanyId: '',
        sourceType: '',
        terminalName: '',
        policyNo: '',
        policyStatus: '',
        billType: '',
        issueName: '',
        issuePhone: '',
        holderName: '',
        sellerName: '',
        proposalTime: [], // 投保日期
        payTime: [], // 交易日期
        transactionId: '' // 交易流水号
      },
      // 列表
      dataList: [],
      loading: false
    }
  },
  activated() {
    this.getInsuranceCompanyList()
    this.getList()
  },
  methods: {
    selectItem(item) {
      this.searchType = item.value
      this.handleReset()
      this.getList()
    },
    async getList() {
      this.loading = true
      let query = this.query
      if (this.searchType === 1) {
        let data = await insuranceRecordList(
          query.current,
          query.size,
          query.insuranceName,
          query.insuranceCompanyId,
          query.sourceType,
          query.terminalName,
          query.policyNo,
          query.policyStatus,
          query.billType,
          query.issueName,
          query.issuePhone,
          query.holderName,
          query.sellerName,
          query.proposalTime && query.proposalTime.length ? query.proposalTime[0] : '',
          query.proposalTime && query.proposalTime.length > 1 ? query.proposalTime[1] : ''
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          query.total = data.total
        }
      } else if (this.searchType === 2) {
        let data = await insurancePayList(
          query.current,
          query.size,
          query.insuranceName,
          query.insuranceCompanyId,
          query.sourceType,
          query.terminalName,
          query.policyNo,
          query.policyStatus,
          query.billType,
          query.issueName,
          query.issuePhone,
          query.holderName,
          query.sellerName,
          query.payTime && query.payTime.length ? query.payTime[0] : '',
          query.payTime && query.payTime.length > 1 ? query.payTime[1] : '',
          query.transactionId
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          query.total = data.total
        }
      }
    },
    async getInsuranceCompanyList() {
      let data = await insuranceRecordCompanyList()
      if (data && data.list) {
        this.providerOptions = data.list
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
        insuranceName: '',
        insuranceCompanyId: '',
        sourceType: '',
        terminalName: '',
        policyNo: '',
        policyStatus: '',
        billType: '',
        issueName: '',
        issuePhone: '',
        holderName: '',
        sellerName: '',
        proposalTime: [],
        payTime: [],
        transactionId: ''
      }
    },
    //  详情
    detail(row) {
      let type = this.searchType === 1 ? 'order' : 'pay'
      this.$router.push({
        name: 'InsuranceSellDetail',
        params: {
          type,
          id: row.id
        }
      })
    },
    changeSearchType() {
      this.getList()
    },
    //  导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'insuranceRecordExportService',
        fileName: 'insuranceRecordExport',
        groupName: '保险销售单-按保单查',
        menuName: '保险订单管理-保险销售单',
        searchConditionList: [
          {
            desc: '保险名称',
            name: 'insuranceName',
            value: query.insuranceName || ''
          },
          {
            desc: '保险服务商',
            name: 'insuranceCompanyId',
            value: query.insuranceCompanyId || ''
          },
          {
            desc: '来源类型',
            name: 'sourceType',
            value: query.sourceType || ''
          },
          {
            desc: '保单来源终端',
            name: 'terminalName',
            value: query.terminalName
          },
          {
            desc: '保司保单号',
            name: 'policyNo',
            value: query.policyNo
          },
          {
            desc: '保单状态',
            name: 'policyStatus',
            value: query.policyStatus
          },
          {
            desc: '定额方案类型',
            name: 'billType',
            value: query.billType
          },
          {
            desc: '被保人姓名',
            name: 'issueName',
            value: query.issueName
          },
          {
            desc: '被保人电话',
            name: 'issuePhone',
            value: query.issuePhone
          },
          {
            desc: '销售员姓名',
            name: 'sellerName',
            value: query.sellerName
          },
          {
            desc: '销售员电话',
            name: 'sellerMobile',
            value: query.sellerMobile || ''
          },
          {
            desc: '投保日期起',
            name: 'startProposalTime',
            value: query.proposalTime && query.proposalTime.length ? query.proposalTime[0] : ''
          },
          {
            desc: '投保日期止',
            name: 'endProposalTime',
            value: query.proposalTime && query.proposalTime.length > 1 ? query.proposalTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'insurancePayRecordExportService',
        fileName: 'insurancePayRecordExport',
        groupName: '保险销售单-按交易记录查',
        menuName: '保险订单管理-保险销售单',
        searchConditionList: [
          {
            desc: '保险名称',
            name: 'insuranceName',
            value: query.insuranceName || ''
          },
          {
            desc: '保险服务商',
            name: 'insuranceCompanyId',
            value: query.insuranceCompanyId || ''
          },
          {
            desc: '来源类型',
            name: 'sourceType',
            value: query.sourceType || ''
          },
          {
            desc: '保单来源终端',
            name: 'terminalName',
            value: query.terminalName
          },
          {
            desc: '保司保单号',
            name: 'policyNo',
            value: query.policyNo
          },
          {
            desc: '保单状态',
            name: 'policyStatus',
            value: query.policyStatus
          },
          {
            desc: '定额方案类型',
            name: 'billType',
            value: query.billType
          },
          {
            desc: '被保人姓名',
            name: 'issueName',
            value: query.issueName
          },
          {
            desc: '被保人电话',
            name: 'issuePhone',
            value: query.issuePhone
          },
          {
            desc: '销售员姓名',
            name: 'sellerName',
            value: query.sellerName
          },
          {
            desc: '销售员电话',
            name: 'sellerMobile',
            value: query.sellerMobile || ''
          },
          {
            desc: '交费日期起',
            name: 'startPayTime',
            value: query.payTime && query.payTime.length ? query.payTime[0] : ''
          },
          {
            desc: '交费日期止',
            name: 'endPayTime',
            value: query.payTime && query.payTime.length > 1 ? query.payTime[1] : ''
          },
          {
            desc: '交易流水号',
            name: 'transactionId',
            value: query.transactionId
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