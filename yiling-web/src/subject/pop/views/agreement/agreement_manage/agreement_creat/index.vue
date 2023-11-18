<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="top-content-view">
          <div class="top-bar">
            <yl-button v-if="step == 1" type="primary" @click="topSelectClick">协议导入</yl-button>
            <div class="header-bar header-renative mar-t-16">
              <div class="sign"></div>进度提示
            </div>
            <div class="steps-view">
              <el-steps :active="step" align-center>
                <el-step title="协议主条款"></el-step>
                <el-step title="协议供销条款"></el-step>
                <el-step title="协议结算返利条款"></el-step>
                <el-step title="协议返利条款"></el-step>
              </el-steps>
            </div>
          </div>
          <!-- step1 -->
          <agreement-main v-if="step == 1" ref="AgreementMain" @firstPartyChange="selectFirstPartyChange"></agreement-main>
          <!-- step2 -->
          <agreement-supply-sale v-if="step == 2" ref="AgreementSupplySale"></agreement-supply-sale>
          <!-- step3 -->
          <agreement-pay-method v-if="step == 3" ref="AgreementPayMethod"></agreement-pay-method>
          <agreement-rebate v-if="step == 4" ref="AgreementRebate"></agreement-rebate>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button v-if="step != 1" type="primary" @click="preStepClick">上一步</yl-button>
        <yl-button v-if="step != 4" type="primary" @click="nextStepClick">下一步</yl-button>
        <yl-button v-if="step == 4" type="primary" @click="submitClick">提交</yl-button>
      </div>
    </div>
    <!-- 导入协议 -->
    <yl-dialog title="导入协议" :visible.sync="addAgreementVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">协议甲方</div>
                <el-input v-model="addAgreementQuery.ename" placeholder="请输入协议甲方" @keyup.enter.native="addAgreemenHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">协议乙方</div>
                <el-input v-model="addAgreementQuery.secondName" placeholder="请输入协议乙方" @keyup.enter.native="addAgreemenHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="agreemenTotal"
                  @search="addAgreemenHandleSearch"
                  @reset="addAgreemenHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="agreemenList"
            :loading="loading"
            @getList="getAgreementPage"
          >
            <el-table-column label="协议编号" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.agreementNo }}</div>
              </template>
            </el-table-column>
            <el-table-column label="甲方" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="乙方" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.secondName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.createTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="生效日期" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.startTime | formatDate('yyyy-MM-dd') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="结束日期" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.endTime | formatDate('yyyy-MM-dd') }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="甲方类型" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.firstType | dictLabel(agreementFirstType) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="返利兑付时间" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.rebateCashTime | dictLabel(agreementRebateCashTime) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="购进渠道" min-width="200" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.buyChannel | dictLabel(agreementSupplySalesBuyChannel) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品数" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsNumber }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="甲方协议联系人" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.firstSignUserName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="addSelectItemClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  createAgreement,
  queryImportAgreementList
} from '@/subject/pop/api/agreement';
import { agreementSupplySalesBuyChannel, agreementRebateCashTime, agreementFirstType } from '@/subject/pop/utils/busi'

import { formatDate } from '@/subject/pop/utils'

import AgreementMain from './agreement_main';
import AgreementSupplySale from './agreement_supply_sale';
import AgreementPayMethod from './agreement_pay_method';
import AgreementRebate from './agreement_rebate';
export default {
  components: {
    AgreementMain,
    AgreementSupplySale,
    AgreementPayMethod,
    AgreementRebate
  },
  computed: {
    // 购进渠道
    agreementSupplySalesBuyChannel() {
      return agreementSupplySalesBuyChannel()
    },
    // 返利兑付时间
    agreementRebateCashTime() {
      return agreementRebateCashTime()
    },
    // 甲方类型
    agreementFirstType() {
      return agreementFirstType()
    }
  },
  watch: {
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
          title: '协议管理'
        },
        {
          title: '新建协议'
        }
      ],
      addAgreementVisible: false,
      loading: false,
      addAgreementQuery: {
        page: 1,
        limit: 10
      },
      agreemenTotal: 0,
      agreemenList: [],
      // 当前步骤
      step: 1,
      // 协议主条款
      agreementMainTerms: {
        // 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商（见字典：agreement_first_type）
        firstType: '',
        // 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（见字典：agreement_type）
        agreementType: '',
        // 签订日期
        signTime: '',
        // 关联甲方
        ename: '',
        eid: '',
        // 乙方名称
        secondName: '',
        // 乙方ID
        secondEid: '',
        // 乙方关联子公司集合
        secondSubEidList: [],
        // 生效日期
        effectTime: [],
        startTime: '',
        endTime: '',
        // 甲方协议签订人名称
        firstSignUserName: '',
        // 甲方协议签订人ID
        firstSignUserId: '',
        // 甲方协议签订人手机号
        firstSignUserPhone: '',
        // 乙方协议签订人名称
        secondSignUserName: '',
        // 乙方协议签订人ID
        secondSignUserId: '',
        // 乙方协议签订人手机号
        secondSignUserPhone: '',
        // 协议负责人：1-甲方联系人 2-乙方联系人
        mainUser: '',
        // 是否提供流向：0-否 1-是，默认为否
        flowFlag: 0,
        // 是否为草签协议：0-否 1-是
        draftFlag: 0,
        // 是否交保证金：0-否 1-是
        marginFlag: 0,
        // 保证金支付方：2-乙方 3-指定商业公司
        marginPayer: 2,
        // 指定商业公司ID
        businessEid: '',
        businessName: '',
        // 保证金金额
        marginAmount: '',
        // 保证金返还方式：1-协议结束后返还 2-指定日期返还
        backType: true,
        marginBackType: 1,
        // 保证金返还日期
        marginBackDate: '',
        // KA协议类型：1-统谈统签，统一支付 2-统谈统签，分开支付 3-统谈分签，分开支付 4-单独签订
        kaAgreementType: '',
        // 是否活动协议：0-否 1-是
        activeFlag: 0,
        // 协议附件类型：1-协议原件 2-协议复印件
        attachmentType: 1,
        // 商业运营签订人ID
        businessOperatorId: '',
        // 商业运营签订人名称
        businessOperatorName: '',
        // 协议附件集合
        agreementAttachmentList: []
      },
      // 协议供销条款
      agreementSupplySalesTerms: {
        // 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
        buyChannel: 1,
        // 指定商业公司集合（只有购进渠道选择指定商业公司，才传入值）
        supplySalesEnterpriseList: [],
        // 是否拉单支持：0-否 1-是
        pullOrderFlag: 0,
        // 是否分销协议支持：0-否 1-是
        distributionAgreementFlag: 0,
        // 是否全系列品种：0-否 1-是
        allLevelKindsFlag: 0,
        // 供销商品组集合（只有是否全系列品种字段选择否，才传入值）
        supplySalesGoodsGroupList: [
          {
            // 供销商品集合
            supplySalesGoodsList: [],
            // 出库价含税是否维价
            exitWarehouseTaxPriceFlag: false,
            // 零售价含税是否维价
            retailTaxPriceFlag: false,
            // 控销类型：1-无 2-黑名单 3-白名单
            controlSaleType: 1,
            // 控销条件集合
            agreementControlList: [],
            // 控销区域对象（只有勾选区域后，才必须有值）
            controlArea: {
              jsonContent: '',
              selectedDesc: ''
            },
            // 控销客户类型
            controlCustomerTypeList: []
          }
        ]
      },
      // 协议结算条款
      agreementSettlementTerms: {
        // 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑（见字典：agreement_pay_method）
        agreementPaymentMethodList: [],
        agreementSettlementMethod: {
          // 是否为预付款结算：0-否 1-是
          advancePaymentFlag: 0,

          // 是否为账期结算：0-否 1-是
          paymentDaysFlag: 0,
          // 账期结算周期
          paymentDaysSettlementPeriod: '',
          // 账期结算日
          paymentDaysSettlementDay: '',
          // 账期支付日
          paymentDaysPayDays: '',

          // 是否为实销实结：0-否 1-是
          actualSalesSettlementFlag: 0,
          // 实销实结结算周期
          actualSalesSettlementPeriod: '',
          // 实销实结结算日
          actualSalesSettlementDay: '',
          // 实销实结支付日
          actualSalesSettlementPayDays: '',

          // 是否为货到付款结算：0-否 1-是
          payDeliveryFlag: 0,

          // 是否为压批结算：0-否 1-是
          pressGroupFlag: 0,
          // 压批次数
          pressGroupNumber: '',

          // 是否为授信结算：0-否 1-是
          creditFlag: 0,
          // 授信金额
          creditAmount: '',

          // 其他
          // 到货周期
          arrivePeriod: '',
          // 最小发货量
          minShipmentNumber: '',
          deliveryType: 1
        }
      },
      // 协议返利条款
      agreementRebateTerms: {
        // 是否底价供货
        reserveSupplyFlag: 0,
        // 商品返利规则设置方式：1-全品设置 2-分类设置
        goodsRebateRuleType: '',
        // 返利支付方
        rebatePay: 1,
        // 指定商业公司时
        agreementRebatePayEnterpriseList: [],
        // 返利上限(元)
        maxRebate: '',
        // 返利兑付方式
        rebateCashType: 1,
        // 返利兑付时间
        rebateCashTime: 2,
        // 返利兑付时段
        rebateCashSegment: '',
        // 返利兑付单位：1-月 2-天
        rebateCashUnit: 2,
        // 其他备注
        otherRemark: '',
        // 非商品返利集合（最多6个阶梯)
        agreementOtherRebateList: [],
        // 是否有任务量：0-否 1-是
        taskFlag: 0,
        // 返利标准
        rebateStandard: '',
        // 任务量标准：1-销售 2-购进 3-付款金额
        taskStandard: '',
        // 返利阶梯条件计算方法
        rebateStageMethod: '',
        // 返利计算单价
        rebateCalculatePrice: '',
        // 返利计算规则
        rebateCalculateRule: '',
        // 返利计算规则类型
        rebateRuleType: '',
        // 时段类型设置
        timeSegmentTypeSet: 1,
        // 全品设置
        // 是否兑付全时段：0-否 1-是（有未达成任务量的子时段时）
        cashAllSegmentFlag: false,
        // 是否兑付子时段：0-否 1-是（有未达成任务量的子时段时）
        cashChildSegmentFlag: false,
        // 分类设置
        // 核心商品组关联性
        coreCommodityGroupRelevance: '',
        // 核心商品组任务量未完成时
        coreCommodityGroupFail: '',
        // 协议返利时段集合
        agreementRebateTimeSegmentList: [
          {
            // 时段类型：1-全时段 2-子时段
            type: 1,
            // 时段开始时间
            startTime: '',
            // 时段结束时间
            endTime: '',
            // 规模返利
            scaleRebateFlag: false,
            // 基础服务奖励
            basicServiceRewardFlag: false,
            // 项目服务奖励
            projectServiceRewardFlag: false,
            // 规模返利阶梯集合
            agreementScaleRebateList: [],
            // 基础服务奖励阶梯集合
            agreementRebateBasicServiceRewardList: [],
            // 项目服务奖励阶梯集合
            agreementRebateProjectServiceRewardList: [],
            // 协议返利商品组集合
            agreementRebateGoodsGroupList: [
              {
                // 商品组分类：1-普通商品组 2-核心商品组
                groupCategory: '',
                groupName: '',
                // 1-全商品主任务 2-子商品组
                groupType: 1,
                // 协议返利商品组对应的商品集合
                agreementRebateGoodsList: [],
                // 协议返利范围集合
                agreementRebateScopeList: [
                  {
                    // 控销类型：1-无 2-黑名单 3-白名单
                    controlSaleType: 1,
                    // 协议返利控销条件：1-区域 2-客户类型
                    agreementRebateControlList: [],
                    // 协议返利控销区域
                    addAgreementRebateControlArea: { jsonContent: '' },
                    // 协议返利控销客户类型
                    agreementRebateControlCustomerType: [],
                    // 协议返利任务量
                    agreementRebateTaskStageList: [
                      {
                        // 任务量
                        taskNum: '',
                        // 任务量单位：1-元 2-盒
                        taskUnit: 1,
                        // 超任务量汇总返
                        overSumBack: '',
                        // 超任务量汇总返单位：1-元 2-%
                        overSumBackUnit: 1,
                        // 协议返利阶梯集合
                        agreementRebateStageList: [
                          {
                            // 满
                            full: '',
                            // 满单位：1-元 2-盒
                            fullUnit: 1,
                            // 返
                            back: '',
                            // 返单位：1-元 2-%
                            backUnit: 1
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ],
        // 备注
        remark: ''
      }
    };
  },
  mounted() {
  },
  methods: {
    // 创建协议提交
    async creatMethods() {
      this.$common.showLoad()
      let data = await createAgreement(
        this.agreementMainTerms,
        this.agreementSupplySalesTerms,
        this.agreementSettlementTerms,
        this.agreementRebateTerms
      );
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.alert('协议新建成功', r => {
          this.$router.push({
            name: 'AgreementAudit'
          })
        })
      }
    },
    // 协议导入点击
    topSelectClick() {
      this.addAgreementVisible = true
      this.getAgreementPage()
    },
    async getAgreementPage() {
      this.loading = true
      let addAgreementQuery = this.addAgreementQuery
      let data = await queryImportAgreementList(
        addAgreementQuery.ename,
        addAgreementQuery.secondName
      );
      this.loading = false
      if (data) {
        this.agreemenList = data
        this.agreemenTotal = data.length
      }
    },
    addAgreemenHandleSearch() {
      this.getAgreementPage()
    },
    addAgreemenHandleReset() {
      this.addAgreementQuery = {}
    },
    // 协议导入选择协议点击
    addSelectItemClick(row) {
      this.$log(row)
      this.addAgreementVisible = false
      let selectAgreementMainTerms = row.agreementMainTerms
      let agreementMainTerms = this.agreementMainTerms

      let selectAgreementSupplySalesTerms = row.agreementSupplySalesTerms
      let agreementSupplySalesTerms = this.agreementSupplySalesTerms

      let selectAgreementSettlementTerms = row.agreementSettlementTerms
      let agreementSettlementTerms = this.agreementSettlementTerms

      let selectAgreementRebateTerms = row.agreementRebateTerms
      let agreementRebateTerms = this.agreementRebateTerms

      // 协议主条款
      Object.keys(selectAgreementMainTerms).forEach((key) => {
        this.$log(key)
        if (key == 'signTime' || key == 'startTime' || key == 'endTime') {
          if (key == 'signTime' && selectAgreementMainTerms.signTime) {
            agreementMainTerms.signTime = formatDate(selectAgreementMainTerms.signTime, 'yyyy-MM-dd')
          }
          if (key == 'startTime' && selectAgreementMainTerms.startTime) {
            agreementMainTerms.startTime = formatDate(selectAgreementMainTerms.startTime, 'yyyy-MM-dd')
          }
          if (key == 'endTime' && selectAgreementMainTerms.endTime) {
            agreementMainTerms.signTime = formatDate(selectAgreementMainTerms.endTime, 'yyyy-MM-dd')
          }
        } else {
          agreementMainTerms[key] = selectAgreementMainTerms[key]
        }
      }) 
      if (selectAgreementMainTerms.startTime && selectAgreementMainTerms.endTime) {
        agreementMainTerms.effectTime = [formatDate(selectAgreementMainTerms.startTime, 'yyyy-MM-dd'), formatDate(selectAgreementMainTerms.endTime, 'yyyy-MM-dd')]
      }

      // 协议供销条款
      Object.keys(selectAgreementMainTerms).forEach((key) => {
        agreementSupplySalesTerms[key] = selectAgreementSupplySalesTerms[key]
      })

      //  协议结算条款
      agreementSettlementTerms.agreementPaymentMethodList = selectAgreementSettlementTerms.agreementPaymentMethodList || []
      if (JSON.stringify(selectAgreementSettlementTerms.agreementSettlementMethod) != '{}') {
        Object.keys(selectAgreementSettlementTerms.agreementSettlementMethod).forEach((key) => {
          agreementSettlementTerms.agreementSettlementMethod[key] = selectAgreementSettlementTerms.agreementSettlementMethod[key]
        })
      }

      // 协议返利条款
      agreementRebateTerms.reserveSupplyFlag = selectAgreementRebateTerms.reserveSupplyFlag
      agreementRebateTerms.goodsRebateRuleType = selectAgreementRebateTerms.goodsRebateRuleType

      this.$refs['AgreementMain'].init(this.agreementMainTerms)
    },
    // 关联甲方 切换
    selectFirstPartyChange() {
      this.$log('selectFirstPartyChange')
      let agreementSupplySalesTerms = this.agreementSupplySalesTerms
      if (agreementSupplySalesTerms.supplySalesGoodsGroupList && agreementSupplySalesTerms.supplySalesGoodsGroupList.length > 0) {
        agreementSupplySalesTerms.supplySalesGoodsGroupList.forEach( (item) => {
          item.supplySalesGoodsList = []
        })
      }
    },
    // 上一步
    preStepClick() {
      this.step = this.step - 1
      if (this.step == 1) {
        this.$nextTick(() => {
          this.$refs['AgreementMain'].init(this.agreementMainTerms)
          this.$refs['AgreementMain'].clearValidateMethods()
        }) 
      }
      if (this.step == 2) {
        this.$nextTick(() => {
          let agreementMainTerms = this.agreementMainTerms
          let eid = agreementMainTerms.eid
          let firstType = agreementMainTerms.firstType
          this.$refs['AgreementSupplySale'].init(eid, firstType, this.agreementSupplySalesTerms)
        }) 
      }
      if (this.step == 3) {
        this.$nextTick(() => {
          this.$refs['AgreementPayMethod'].init(this.agreementSettlementTerms)
        }) 
      }
    },
    // 下一步
    nextStepClick() {
      if (this.step == 1) {
        this.$refs['AgreementMain'].stepClickMethods((agreementMainTerms) => {
          this.agreementMainTerms = agreementMainTerms
          let eid = agreementMainTerms.eid
          let firstType = agreementMainTerms.firstType
          this.step = this.step + 1
          this.$nextTick(() => {
            this.$refs['AgreementSupplySale'].init(eid, firstType, this.agreementSupplySalesTerms)
          }) 
        })
      } else if (this.step == 2) {
        this.$refs['AgreementSupplySale'].stepClickMethods((agreementSupplySalesTerms) => {
          this.agreementSupplySalesTerms = agreementSupplySalesTerms
          this.step = this.step + 1
          this.$nextTick(() => {
            this.$refs['AgreementPayMethod'].init(this.agreementSettlementTerms)
          }) 
        })
      } else if (this.step == 3) {
        this.$refs['AgreementPayMethod'].stepClickMethods((agreementSettlementTerms) => {
          this.agreementSettlementTerms = agreementSettlementTerms

          let agreementMainTerms = this.agreementMainTerms
          let eid = agreementMainTerms.eid
          let firstType = agreementMainTerms.firstType
          let effectTime = agreementMainTerms.effectTime
          let ename = agreementMainTerms.ename

          let cateGoodsNum = 0
          let agreementSupplySalesTerms = this.agreementSupplySalesTerms
          // 是否全系列品种
          let isAllGoods = agreementSupplySalesTerms.allLevelKindsFlag
          let supplySalesGoodsGroupList = agreementSupplySalesTerms.supplySalesGoodsGroupList
          for (let i = 0; i < supplySalesGoodsGroupList.length; i++) {
            let goodsItem = supplySalesGoodsGroupList[i]
            cateGoodsNum += goodsItem.supplySalesGoodsList.length
          }

          // 协议类型
          let agreementType = agreementMainTerms.agreementType
          // KA协议类型
          let kaAgreementType = agreementMainTerms.kaAgreementType || ''

          this.step = this.step + 1
          this.$nextTick(() => {
            this.$refs['AgreementRebate'].init(eid, firstType, effectTime, ename, isAllGoods, cateGoodsNum, agreementType, kaAgreementType)
          }) 
        })
      }
       
    },
    // 提交
    submitClick() {
      this.$refs['AgreementRebate'].stepClickMethods((agreementRebateTerms) => {
        this.agreementRebateTerms = agreementRebateTerms
        this.creatMethods()
      })
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.steps-view{
  .el-step{
    .is-finish{
      .el-step__icon{
        background: #1790ff;
      }
    }
    .el-step__icon{
      background: #CCCCCC;
      border: none;
      .el-step__icon-inner{
        color: $white;
      }
    }
    .el-step__title{
      color: #333;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
