<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="buy-order-half">
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                订单信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">订单编号：</span>{{ data.orderNo }}</div>
                <div class="item">
                  <span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}
                </div>
                <div class="item">
                  <span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{
                    data.paymentStatus | dictLabel(orderPayStatus)
                  }}）
                </div>
                <div class="item"><span class="font-title-color">订单ID：</span>{{ data.id }}</div>
                <div class="item"><span class="font-title-color">合同编号：</span>{{ data.contractNumber }}</div>
                <div class="item"><span class="font-title-color">销售组织：</span>{{ data.sellerEname }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                收货信息
              </div>
              <div class="box-text" v-if="data.orderAddress">
                <div class="item">
                  <span class="font-important-color"></span>{{ data.orderAddress.buyerEname
                  }}<span class="font-title-color" v-show="data.orderAddress.provinceName"
                    >（{{ data.orderAddress.provinceName }}）</span
                  >
                </div>
                <div class="item"><span class="font-title-color">收货人：</span>{{ data.orderAddress.name }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.orderAddress.mobile }}</div>
                <div class="item"><span class="font-title-color">收货地址：</span>{{ data.orderAddress.address }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                采购商备注
              </div>
              <div class="box-text">
                <div class="mark-item">
                  {{ data.orderNote }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                付款信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">商品总额：</span>￥{{ data.totalAmount }}</div>
                <div class="item"><span class="font-title-color">现折总额：</span>￥{{ data.cashDiscountAmount }}</div>
                <div class="item"><span class="font-title-color">支付总额：</span>￥{{ data.paymentAmount }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                账款信息<span
                  v-if="data.paymentDaysInfo"
                  class="status"
                  :class="[data.paymentDaysInfo.status == 1 ? 'col-down' : 'col-up']"
                  >{{ data.paymentDaysInfo.status == 1 ? '启用' : '停用' }}</span
                >
              </div>
              <div class="box-text">
                <div class="item">
                  <span class="font-title-color">起止时间：</span>
                  <span v-if="data.paymentDaysInfo.startTime"
                    >{{ data.paymentDaysInfo.startTime | formatDate('yyyy.MM.dd') }}-{{
                      data.paymentDaysInfo.endTime | formatDate('yyyy.MM.dd')
                    }}</span
                  >
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">信用额度：</span>
                  <span v-if="data.paymentDaysInfo.totalAmount">{{
                    data.paymentDaysInfo.totalAmount | toThousand('￥')
                  }}</span>
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">临时额度：</span>
                  <span v-if="data.paymentDaysInfo.temporaryAmount">{{
                    data.paymentDaysInfo.temporaryAmount | toThousand('￥')
                  }}</span>
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">已使用额度：</span>
                  <span v-if="data.paymentDaysInfo.usedAmount">{{
                    data.paymentDaysInfo.usedAmount | toThousand('￥')
                  }}</span>
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">可使用额度：</span>
                  <span v-if="data.paymentDaysInfo.availableAmount">{{
                    data.paymentDaysInfo.availableAmount | toThousand('￥')
                  }}</span>
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">已还款额度：</span>
                  <span v-if="data.paymentDaysInfo.repaymentAmount">{{
                    data.paymentDaysInfo.repaymentAmount | toThousand('￥')
                  }}</span>
                  <span v-else>- -</span>
                </div>
                <div class="item">
                  <span class="font-title-color">信用账期：</span>
                  {{ data.paymentDaysInfo.period || '- -' }}<span v-if="data.paymentDaysInfo.period">天</span>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                购销合同
              </div>
              <div class="box-text">
                <div class="img-box" v-for="(img, index) in data.orderContractUrl" :key="index">
                  <el-image class="img-item" fit="cover" :preview-src-list="data.orderContractUrl" :src="img" />
                  <div class="screen flex-row-center">
                    <svg-icon class="icon" icon-class="img-open"></svg-icon>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="24">
            <div class="common-box header-box" style="min-height:auto;">
              <div class="header-bar">
                <div class="sign"></div>商务信息
              </div>
              <div class="box-text" style="color:#666;">
                <div class="item"><span class="font-title-color">商务负责人：</span>{{ data.contacterName }}</div>
                <div class="item"><span class="font-title-color">归属部门：</span>{{ data.departmentName }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex" v-show="data && data.auditStatus == 4">
          <el-col :span="24">
            <div class="common-box header-box" style="min-height:auto;">
              <div class="header-bar">
                <div class="sign"></div>
                审核驳回原因
              </div>
              <div class="box-text" style="color:#666;">
                {{ data.auditRejectReason }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>
          商品信息
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailList && data.orderDetailList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailList" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item">
                    <span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}
                  </div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item">
                    <span class="font-title-color">规格/型号：</span >{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }} </div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item"></div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>￥{{ item.goodsPrice }}</div>
                  <div class="text-item">
                    <span class="font-title-color">金额小计：</span>
                    <span class="money bold">￥{{ item.goodsAmount }}</span>
                  </div>
                  <div class="text-item"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button v-show="type == 1" type="primary" plain @click="handleReject(1)">审核驳回</yl-button>
        <yl-button v-show="type == 1" type="primary" @click="handleReject(2)">审核通过</yl-button>
      </div>
    </div>
    <yl-dialog title="审核驳回" @confirm="confirm" :visible.sync="showDialog">
      <div class="review-dialog">
        <div class="font-size-lg font-important-color mar-b-8"><span class="col-up">*</span>反馈详情</div>
        <div>
          <el-input
            type="textarea"
            v-model="remark"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="请输入内容"
            maxlength="150"
          >
          </el-input>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getOrderReviewDetail, orderReviewReject, orderReviewResole } from '@/subject/pop/api/order'
import { paymentMethod, orderStatus, orderPayStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'DistributeOrderDetail',
  components: {},
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 订单状态
    orderStatus() {
      return orderStatus()
    },
    // 支付状态
    orderPayStatus() {
      return orderPayStatus()
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
          title: '订单审核管理'
        },
        {
          title: '分销订单审核',
          path: '/reviewOrder/distribute_order_review'
        },
        {
          title: '分销审核详情'
        }
      ],
      // type: 1-待审核 2-审核通过、审核驳回
      type: 1,
      data: {
        paymentDaysInfo: {}
      },
      showDialog: false,
      // 反馈详情
      remark: ''
    }
  },
  mounted() {
    this.id = this.$route.params.id
    this.type = this.$route.params.type
    if (this.id) {
      this.id = parseFloat(this.id)
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getOrderReviewDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        data.paymentDaysInfo = data.paymentDaysInfo || {}
        this.data = data
      }
    },
    // 通过 驳回
    handleReject(type) {
      if (type === 1) {
        // 驳回
        this.showDialog = true
      } else {
        // 通过
        this.$common.confirm(
          '审核通过后订单将推送至EAS进行发货，请确认是否审核通过',
          r => {
            if (r) {
              this.allow()
            }
          },
          null,
          null,
          null,
          '审核通过'
        )
      }
    },
    // 审核通过
    async allow() {
      this.$common.showLoad()
      let data = await orderReviewResole(this.id)
      this.$common.hideLoad()
      if (data && data.result) {
        this.$router.go(-1)
        this.$common.n_success('通过成功')
      }
    },
    // 审核拒绝确认
    async confirm() {
      if (!this.remark) {
        return
      }
      this.$common.showLoad()
      let data = await orderReviewReject(this.id, this.remark)
      this.$common.hideLoad()
      if (data && data.result) {
        this.showDialog = false
        this.$common.n_success('驳回成功')
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
