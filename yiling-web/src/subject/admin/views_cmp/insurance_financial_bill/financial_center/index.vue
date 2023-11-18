<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="select-bar">
        <div class="item" v-for="item in selectBarList" :key="item.value" @click="selectItem(item)">
          <div :class="['item-title', item.value === selected ? 'active' : '']">{{ item.label }}</div>
          <div class="item-line" v-show="item.value === selected"></div>
        </div>
      </div>
      <div class="common-box box-search" v-if="selected === 1">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">服务商名称</div>
              <el-input v-model="query.insuranceCompanyName" placeholder="请输入服务商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">结账日期段</div>
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
      <div class="common-box box-search" v-if="selected === 2">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">药店名称</div>
              <el-input v-model="query.ename" placeholder="请输入药店名称" @keyup.enter.native="handleSearch"/>
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
      <div class="common-box box-search" v-if="selected === 3">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">药店名称</div>
              <el-input v-model="query.insuranceName" placeholder="请输入药店名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">销售员姓名</div>
              <el-input v-model="query.sellerName" placeholder="请输入销售人姓名" />
            </el-col>
            <el-col :span="6">
              <div class="title">销售员电话</div>
              <el-input v-model="query.sellerMobile" placeholder="请输入销售人电话" />
            </el-col>
            <el-col :span="6">
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
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保险账单类型</div>
              <el-select v-model="query.orderType" placeholder="请选择保险账单类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderTypeOptions"
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
      <div class="down-box clearfix" v-if="selected !== 2">
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" v-if="selected === 1" :href="'NO_10' | template"> 下载模板 </el-link>
          <ylButton type="primary" plain v-if="selected === 1" @click="goImport">导入结算数据</ylButton>
          <ylButton type="primary" plain v-if="selected === 3" @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <div class="common-box mar-t-8" v-if="selected === 1">
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
            :list="insuranceSettlement"
            :total="query.total"
            :page.sync="query.current"
            :limit.sync="query.size"
            :loading="loading"
            @getList="getList"
          >
            <el-table-column label="服务商名称" min-width="100" align="center" prop="insuranceCompanyName"> </el-table-column>
            <el-table-column label="交易打款时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.payTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="对应第三方打款单号" min-width="100" align="center" prop="thirdPayNo"> </el-table-column>
            <el-table-column label="数据创建时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.createTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="打款金额" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.payAmount | toThousand("") }} </span>
              </template>
            </el-table-column>
            <el-table-column label="对应保单号" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.policyNo || "- -" }} </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
              <template slot-scope="{ row }">
                  <yl-button type="text" @click="checkDetail(row)">查看详情 </yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="table-box mar-t-16" v-if="selected === 2">
        <yl-table
          key="table2"
          stripe
          :show-header="true"
          :list="enterpriseSettlement"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="药店名称" min-width="100" align="center" prop="ename"> </el-table-column>
          <el-table-column label="待处理" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>订单量：{{ row.unPayCount }} 笔， 合计： {{ row.unPayAmount | toThousand("") }}</div>
            </template>
          </el-table-column>
          <el-table-column label="已完结" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>订单量：{{ row.enPayCount }} 笔， 合计： {{ row.enPayAmount | toThousand("") }}</div>
            </template>
          </el-table-column>
          <el-table-column label="账户详情" min-width="120" align="center">
            <template slot-scope="{ row }">
                <yl-button type="text" @click="detail(row)">详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="table-box mar-t-8" v-if="selected === 3">
        <yl-table
          key="table3"
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="保单单号" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="销售交易单号" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="保险名" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="用户实际支付额" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="有效兑保次数" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="保险账单类型" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="保司" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="销售员" min-width="100" align="left" prop="name"> </el-table-column>
          <el-table-column label="创建时间" min-width="100" align="left" prop="name"> </el-table-column>
        </yl-table>
      </div>
      <!-- 详情 -->
      <yl-dialog class="detail-dialog" :visible.sync="detailDialogShow" width="820px" title="详情" :show-footer="false">
        <div class="dialog-content">
          <div class="insurance-info">
            <div class="info-box-item">
              <div class="insurance-info-item">客户姓名：{{ detailData.issueName }}</div>
              <div class="insurance-info-item">保单号：{{ detailData.policyNo || "- -" }}</div>
              <div class="insurance-info-item">服务商名称：{{ detailData.insuranceCompanyName }}</div>
            </div>
            <div class="info-box-item">
              <div class="insurance-info-item">打款金额：{{ detailData.payAmount | toThousand("") }}</div>
              <div class="insurance-info-item">收款账号：{{ detailData.accountNo }}</div>
              <div class="insurance-info-item">打款时间：{{ detailData.payTime | formatDate }}</div>
            </div>
            <div class="info-box-item">
              <div class="insurance-info-item">对应第三方打款流水号：{{ detailData.thirdPayNo }}</div>
            </div>
            <div class="table-box">
              <div class="header-bar">
                <div class="sign"></div>
                对应药品订单
              </div>
              <div class="order-info mar-t-16">
                <yl-table
                  stripe
                  :show-header="true"
                  :list="detailData.insuranceSettlementDetailList">
                  <el-table-column label="订单编号" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div class="order" @click="checkOrderDetail(row)"> {{ row.orderNo }} </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="对应保司结算额" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div> {{ row.payAmount | toThousand("") }} </div>
                    </template>
                  </el-table-column>
                </yl-table>
              </div>
            </div>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getInsuranceSettlement, getInsuranceSettlementDetail, getEnterpriseSettlement } from '@/subject/admin/api/cmp_api/insurance_financial_bill'

export default {
  name: 'FinancialCenter',
  components: {
  },
  computed: {
    // 默认时间
    timeDefault () {
        let date = new Date()
        // 通过时间戳计算
        let defalutStartTime = (new Date(date.getFullYear(), date.getMonth() - 3, date.getDate())).getTime() // 转化为时间戳
        let defalutEndTime = date.getTime()
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
  created() {
    this.query.payTime = this.timeDefault;
  },
  activated() {
    this.getList()
  },
  data() {
    return {
      selected: 1,
      selectBarList: [
        { label: '保司结账明细', value: 1 },
        { label: '以岭给商家药品对账', value: 2 }
        // { label: "以岭与商家保险提成对账", value: 3 },
      ],
      orderTypeOptions: [
        { label: '季度', value: 1 },
        { label: '半年', value: 2 },
        { label: '年', value: 3 }
      ],
      query: {
        current: 1,
        size: 10,
        insuranceCompanyName: '',
        payTime: [],
        ename: '',
        orderType: 0
      },
      // 列表
      dataList: [],
      // 保司结账列表
      insuranceSettlement: [],
      totalAmount: 0,
      totalCount: 0,
      // 以领给商家药品对账列表
      enterpriseSettlement: [],
      loading: false,
      detailDialogShow: false,
      detailData: {
        insuranceSettlementDetailList: []
      }
    }
  },
  methods: {
    async getList() {
      this.loading = false
      let query = this.query
      if (this.selected === 1) {
        let data = await getInsuranceSettlement(
          query.current,
          query.size,
          query.insuranceCompanyName,
          query.payTime && query.payTime.length ? query.payTime[0] : '',
          query.payTime && query.payTime.length > 1 ? query.payTime[1] : ''
        )
        this.loading = false
        if (data && data.page) {
          this.insuranceSettlement = data.page.records
          this.totalAmount = data.totalAmount
          this.totalCount = data.totalCount
          query.total = data.page.total
        }
      } else if (this.selected === 2) {
        let data = await getEnterpriseSettlement(
          query.current,
          query.size,
          query.ename
        )
        this.loading = false
        if (data) {
          this.enterpriseSettlement = data.records
          query.total = data.total
        }
      } else if (this.selected === 3) {
        // 获取以领与商家保险提成对账
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
        insuranceCompanyName: '',
        ename: '',
        payTime: this.timeDefault
      }
    },
    selectItem(item) {
      this.selected = item.value
      this.getList()
    },
    detail(row) {
      this.$router.push({
        name: 'SettlementBill',
        params: {
          id: row.id,
          eid: row.eid
        }
      })
    },
    async checkDetail(row) {
      let data = await getInsuranceSettlementDetail(row.id)
      if (data) {
        this.detailData = data
        this.detailDialogShow = true
      }
    },
    checkOrderDetail(row) {
      this.detailDialogShow = false
      this.$router.push({
        name: 'GoodsOrderDetail',
        params: { id: row.orderId}
      })
    },
    // 导入信息
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/hmc/api/v1/settlement/insurance/importInsuranceSettlement'
        }
      })
    },
    downLoadTemp() {
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
