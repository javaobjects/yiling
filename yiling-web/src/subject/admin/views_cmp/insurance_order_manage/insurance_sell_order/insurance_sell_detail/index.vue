<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="select-bar">
        <div :class="['item', item.value === 3 && detailData.insuranceRecord.policyStatus !== 2 ? 'hidden' : '']" v-for="item in selectBarList" :key="item.value" @click="selectItem(item)">
          <div :class="['item-title', item.value === selected ? 'active' : '']">{{ item.label }}</div>
          <div class="item-line" v-show="item.value === selected"></div>
        </div>
      </div>
      <div class="common-box" v-if="selected === 1">
        <div class="insurance-detail-info">
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              基本信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title" v-if="queryDetailType === 'order'">订单编号：<span class="item-value">{{ detailData.insuranceRecord.orderNo }}</span></div>
                    <div class="item-title" v-if="queryDetailType === 'pay'">订单销售交易单号：<span class="item-value">{{ detailData.recordPayVO.orderNo }}</span></div>
                  </div>
                  <div class="item" v-if="queryDetailType === 'pay'">
                    <div class="item-title">支付金额：<span class="item-value">{{ detailData.insuranceRecord.totalPayMoney }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">对应保司定额标识：<span class="item-value">{{ detailData.insuranceRecord.comboName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title" v-if="queryDetailType === 'order'">投保时间：<span class="item-value">{{ detailData.insuranceRecord.proposalTime | formatDate }}</span></div>
                    <div class="item-title" v-if="queryDetailType === 'pay'">支付时间：<span class="item-value">{{ detailData.recordPayVO.payTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保险兑付药店：<span class="item-value">{{ detailData.insuranceRecord.ename }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">销售员所属企业：<span class="item-value">{{ detailData.insuranceRecord.sellerEName || "- -" }}</span></div>
                  </div>
                  <div class="item" v-if="queryDetailType === 'pay'">
                    <div class="item-title">当前所交期：<span class="item-value">{{ detailData.currentPayStage }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">保司保单号：<span class="item-value">{{ detailData.insuranceRecord.policyNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保单提供商：<span class="item-value">{{ detailData.insuranceCompanyName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保单生效时间：<span class="item-value">{{ detailData.insuranceRecord.effectiveTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">来源类型：<span class="item-value">{{ detailData.sourceType | dictLabel(hmcSourceType) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">销售员：
                      <span class="item-value">{{ detailData.insuranceRecord.sellerUserName || "- -" }} </span>
                      <span class="item-value" v-if="detailData.insuranceRecord.sellerUserName">【ID：{{ detailData.insuranceRecord.sellerUserId }}】</span>
                    </div>
                  </div>
                  <div class="item" v-if="queryDetailType === 'pay'">
                    <div class="item-title">泰康支付订单号：<span class="item-value">{{ detailData.recordPayVO.billNo || "- -" }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item" v-if="queryDetailType === 'pay'">
                    <div class="item-title">开始时间：<span class="item-value">{{ detailData.insuranceRecord.effectiveTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保单名称：<span class="item-value">{{ detailData.insuranceName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">是否退保：<span class="item-value">{{ detailData.insuranceRecord.policyStatus === 2 ? "是" : "否" }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保单状态：<span class="item-value">{{ detailData.insuranceRecord.policyStatus | dictLabel(hmcPolicyStatus) }}</span></div>
                  </div>
                  <div class="item" v-if="queryDetailType === 'pay'">
                    <div class="item-title">支付流水号：<span class="item-value">{{ detailData.recordPayVO.transactionId || "- -" }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title" v-if="queryDetailType === 'order'">保单终止时间：<span class="item-value">{{ detailData.insuranceRecord.currentEndTime | formatDate }}</span></div>
                    <div class="item-title" v-if="queryDetailType === 'pay'">终止时间：<span class="item-value">{{ detailData.insuranceRecord.currentEndTime | formatDate }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              投保人信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">投保人：<span class="item-value">{{ detailData.insuranceRecord.holderName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">身份证号：<span class="item-value">{{ detailData.insuranceRecord.holderCredentialNo }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ detailData.insuranceRecord.holderPhone }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              被保人信息
              <div class="insurance-order" @click="checkPolicyUrl">查看电子保单</div>
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">被保人：<span class="item-value">{{ detailData.insuranceRecord.issueName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">身份证号：<span class="item-value">{{ detailData.insuranceRecord.issueCredentialNo }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ detailData.insuranceRecord.issuePhone }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item" v-if="queryDetailType === 'pay'">
            <div class="header-bar">
              <div class="sign"></div>
              保单服务内容
            </div>
            <div class="item-info mar-t-16">
              <div class="table-box" v-for="(item,index) in detailData.newFetchPlanList" :key="index">
                <div class="status-box mar-b-8">
                  <div class="item-status">初始拿药日期: {{ item.dateStatus.initFetchTime | formatDate }}</div>
                  <div class="item-status">真实拿药日期: {{ item.dateStatus.actualFetchTime | formatDate }}</div>
                  <div class="item-status">拿药状态:
                    <span :class="item.dateStatus.fetchStatus === 1 ? 'success' : 'fail'"> {{ item.dateStatus.fetchStatus === 1 ? "已拿" : "未拿" }} </span>
                  </div>
                </div>
                <yl-table
                  border
                  stripe
                  :show-header="true"
                  :list="item.fetchPlanDetailList">
                  <el-table-column label="药品名称" min-width="100" align="center" prop="goodsName"></el-table-column>
                  <el-table-column label="规格" min-width="100" align="center" prop="specificInfo"></el-table-column>
                  <el-table-column label="给以岭结算单价" min-width="100" align="center" prop="settlePrice">
                    <template slot-scope="{ row }">
                      <span>{{ row.settlePrice | toThousand("") }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="每次(盒)" min-width="100" align="center" prop="perMonthCount"></el-table-column>
                </yl-table>
              </div>
            </div>
          </div>
          <div class="insurance-detail-item" v-if="queryDetailType === 'order'">
            <div class="header-bar">
              <div class="sign"></div>
              保单服务内容
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="detailData.fetchPlanDetailList">
                <el-table-column label="内容项" min-width="100" align="center" prop="goodsName"></el-table-column>
                <el-table-column label="数量" min-width="100" align="center" prop="totalCount"></el-table-column>
                <el-table-column label="给以岭结算单价" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.settlePrice | toThousand("") }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="已兑数量" min-width="100" align="center" prop="tookTotalCount"></el-table-column>
                <el-table-column label="剩余(盒)" min-width="100" align="center" prop="leftTotalCount"></el-table-column>
                <el-table-column label="共兑几次" min-width="100" align="center" prop="totalTimes"></el-table-column>
                <el-table-column label="还剩几次" min-width="100" align="center" prop="leftTimes"></el-table-column>
                <el-table-column label="每次拿多少盒" min-width="100" align="center" prop="perMonthCount"></el-table-column>
              </yl-table>
            </div>
          </div>
          <div class="insurance-detail-item" v-if="queryDetailType === 'order'">
            <div class="header-bar">
              <div class="sign"></div>
              保单交费记录
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="detailData.payHisList">
                <el-table-column label="第几期" width="80" align="center" type="index"></el-table-column>
                <el-table-column label="保单号" min-width="100" align="center" prop="policyNo"></el-table-column>
                <el-table-column label="金额" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.amount | toThousand("") }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="开始时间" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.startTime | formatDate }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="结束时间" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.endTime | formatDate }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="支付时间" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.payTime | formatDate }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="支付流水号" min-width="100" align="center" prop="transactionId">
                  <template slot-scope="{ row }">
                    <span>{{ row.transactionId || "- -" }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="泰康支付订单号" min-width="100" align="center" prop="billNo">
                  <template slot-scope="{ row }">
                    <span>{{ row.billNo || "- -" }}</span>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </div>
        </div>
      </div>
      <div class="common-box" v-if="selected === 2">
        <div class="insurance-detail-info">
          <yl-table
            stripe
            :show-header="true"
            :list="dataList"
            :loading="loading"
            :page.sync="query.current"
            :limit.sync="query.size"
            :total="query.total"
            @getList="getList"
          >
            <el-table-column label="兑付单编号" min-width="150" align="left" prop="id">
              <template slot-scope="{ row }">
                <div> 平台单号：{{ row.orderNo }} </div>
                <div> 第三方单号：{{ row.thirdConfirmNo || "- -" }} </div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div v-for="item in row.goodsList" :key="item.id"> {{ item.goodsName }} 规格：{{ item.goodsSpecifications }} 数量：{{ item.goodsQuantity }} </div>
              </template>
            </el-table-column>
            <el-table-column label="兑付申请时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.orderTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="最新更新时间" min-width="100" align="center">
              <template slot-scope="{ row }">
                <span> {{ row.updateTime | formatDate }} </span>
              </template>
            </el-table-column>
            <el-table-column label="关联处方" min-width="100" align="center" prop="orderPrescriptionId"></el-table-column>
            <el-table-column label="状态" min-width="100" align="center" prop="orderStatus">
              <template slot-scope="{ row }">
                <span> {{ row.orderStatus | dictLabel(hmcOrderStatus) }} </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="checkDetail(row)">详情</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 退保信息 保单状态为退保状态才有 -->
      <div class="common-box" v-if="selected === 3 && detailData.insuranceRecord.policyStatus === 2">
        <div class="insurance-detail-info">
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              基本信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">退保单编号：<span class="item-value">{{ retreatDetailData.orderNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">退保金额：<span class="item-value"> {{ retreatDetailData.retMoney | toThousand("¥") }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">第三方保险标识：<span class="item-value">{{ retreatDetailData.comboName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">保单服务商：<span class="item-value">{{ retreatDetailData.companyName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">对应保单单号：<span class="item-value">{{ retreatDetailData.policyNo }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">退保时间：<span class="item-value">{{ retreatDetailData.retTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保单名称：<span class="item-value">{{ retreatDetailData.insuranceName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">退保单状态：<span class="item-value">{{ retreatDetailData.endPolicyType | dictLabel(hmcPolicyEndType) }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              退保人信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">投保人：<span class="item-value">{{ retreatDetailData.holderName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">身份证号：<span class="item-value">{{ retreatDetailData.holderCredentialNo }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ retreatDetailData.holderPhone }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { insuranceRecordDetail, insuranceRecordPayDetail, insuranceRecordcCashDetail, insuranceRetreatDetail, downloadPolicyFile } from '@/subject/admin/api/cmp_api/insurance_order_manage'
import { hmcSourceType, hmcOrderStatus, hmcPolicyEndType, hmcPolicyStatus } from '@/subject/admin/busi/cmp/insurance_order_manage'

export default {
  name: 'InsuranceSellDetail',
  components: {
  },
  computed: {
    // 来源类型
    hmcSourceType() {
      return hmcSourceType()
    },
    // 保单兑付状态
    hmcOrderStatus() {
      return hmcOrderStatus()
    },
    // 退保单状态
    hmcPolicyEndType() {
      return hmcPolicyEndType()
    },
    // 保单状态
    hmcPolicyStatus() {
      return hmcPolicyStatus()
    }
  },
  filters: {
  },
  mounted() {
    this.id = this.$route.params.id
    this.queryDetailType = this.$route.params.type
    if (this.id) {
      this.getDetail()
      this.getList()
      this.getReturnDetail()
    }
  },
  data() {
    return {
      queryDetailType: '',
      selected: 1,
      selectBarList: [
        { label: '保单信息', value: 1 },
        { label: '保单兑付记录', value: 2 },
        { label: '退保明细', value: 3 }
      ],
      query: {
        current: 1,
        size: 10
      },
      // 保单信息
      detailData: {
        fetchPlanDetailList: [],
        insuranceRecord: {},
        payHisList: [],
        recordPayVO: {},
        newFetchPlanList: []
      },
      // 列表
      dataList: [],
      // 退保明细
      retreatDetailData: {},
      loading: false
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await insuranceRecordcCashDetail(
        query.current,
        query.size,
        this.id
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
      }
    },
    async getDetail() {
      // order-按保单查详情  pay-按交易记录查详情
      if (this.queryDetailType === 'order') {
        let data = await insuranceRecordDetail(this.id)
        if (data !== undefined) {
          this.detailData = data
        }
      } else if (this.queryDetailType === 'pay') {
        let data = await insuranceRecordPayDetail(this.id)
        if (data !== undefined) {
          this.detailData = data
          this.detailData.newFetchPlanList = data.fetchPlanList.map(item => {
            return {
              dateStatus: item,
              fetchPlanDetailList: data.fetchPlanDetailList
            }
          })
        }
      }
    },
    async getReturnDetail() {
      let data = await insuranceRetreatDetail( this.id )
      if (data !== undefined) {
        this.retreatDetailData = data
      }
    },
    selectItem(item) {
      this.selected = item.value
    },
    checkDetail(row) {
      this.$router.push({
        name: 'GoodsOrderDetail',
        params: { id: row.id}
      })
    },
    async checkPolicyUrl() {
      this.$common.showLoad()
      let data = await downloadPolicyFile(this.$route.params.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.goThreePackage(data)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
