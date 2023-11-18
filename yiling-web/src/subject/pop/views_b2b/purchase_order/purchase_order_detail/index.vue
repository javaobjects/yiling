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
                <div class="sign"></div>订单信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">订单编号：</span>{{ data.orderNo }}</div>
                <div class="item"><span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}</div>
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{ data.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                <div v-if="data.returnButtonFlag" class="item red-text">订单还有{{ data.remainReceiveDay }}天将自动签收</div>
                <div v-if="data.orderCategory == 2 && data.paymentNameType == 2" class="item red-text">订单尾款支付剩余时间{{ data.presaleRemainTime }}</div>
                <div v-else>
                  <div v-if="data.paymentButtonFlag" class="item red-text">订单支付剩余时间{{ data.remainTime }}</div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>供应商信息
              </div>
              <div class="box-text" v-if="data.sellerEnterpriseInfo && data.sellerEnterpriseInfo.name">
                <div class="item"><span class="font-title-color">供应商名称：</span>{{ data.sellerEnterpriseInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.sellerEnterpriseInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.sellerEnterpriseInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.sellerEnterpriseInfo.address }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>采购商备注
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
                <div class="sign"></div>付款信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">货款总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                <div class="item" v-if="data.orderCategory == 2"><span class="font-title-color">定金金额：</span>{{ data.depositAmount | toThousand('￥') }}</div>
                <div class="item" v-if="data.orderCategory == 2"><span class="font-title-color">尾款金额：</span>{{ data.balanceAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>日志信息
              </div>
              <div class="box-text" v-if="data.orderLogInfo && data.orderLogInfo.length">
                <el-scrollbar wrap-class="max-scroll">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(activity, index) in data.orderLogInfo"
                      :key="index"
                      :class="index === 0 ? 'active' : ''"
                      :timestamp="activity.info">
                    </el-timeline-item>
                  </el-timeline>
                </el-scrollbar>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>物流信息
              </div>
              <div class="box-text">
                <yl-empty></yl-empty>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div class="font-title-color font-size-lg mar-l-8 mar-b-16">
         物流信息：
         <span v-if="data.deliveryType == 2">第三方物流</span>
         <span v-else-if="data.deliveryType == 1">自有物流</span>
         <span v-else>- -</span>
       </div>
       <div class="font-title-color font-size-lg mar-l-8 mar-b-16" v-if="data.deliveryType == 2">
         快递公司：<span class="mar-r-16">{{ data.deliveryCompany }}</span>
         快递单号：<span>{{ data.deliveryNo }}</span>
       </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
              <div class="title flex-row-left">{{ item.goodsName }}<span v-if="item.promotionActivityType" class="activity-type">{{ item.promotionActivityType | dictLabel(typeArray) }}</span></div>
              <div class="desc-box">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                  <div class="text-item"></div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.couponDiscountAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  class="content"
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== (item.orderDeliveryList.length - 1) ? 'mar-b-8' : ''">
                  <div class="content-item font-title-color">
                    批次号/序列号：
                    <span class="font-important-color">{{ order.batchNo }}</span>
                  </div>
                  <div class="content-item font-title-color">
                    有效期至：
                    <span class="font-important-color">{{ order.expiryDate | formatDate }}</span>
                  </div>
                  <div class="content-item font-title-color">
                    发货数量：
                    <span class="font-important-color">{{ order.deliveryQuantity }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" plain v-if="data.deleteButtonFlag" @click="deleteDidClick">删除订单</yl-button>
        <yl-button type="primary" plain v-if="data.cancelButtonFlag" @click="cancleDidClick">取消订单</yl-button>
        <yl-button type="primary" v-if="data.paymentButtonFlag" :disabled="data.paymentNameType == 2 && !data.presaleButtonFlag" @click="payClick">{{ data.orderCategory == 1 ? '立即支付' : data.paymentNameType == 1 ? '支付定金' : '支付尾款' }}</yl-button>
        <yl-button type="primary" v-if="data.receiveButtonFlag" @click="receiveThePackage(1)">确认收货</yl-button>
        <yl-button type="primary" v-if="data.returnButtonFlag" @click="receiveThePackage(2)">提交退货</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getPurchaseDetail, purchaseCancle, toPay, paymentPay, purchaseDelete } from '@/subject/pop/api/b2b_api/purchase_order'
import { paymentMethod, orderStatus, orderPayStatus } from '@/subject/pop/utils/busi'
import ylEmpty from '@/common/components/Empty'
import { formatDate } from '@/subject/pop/utils'

export default {
  components: {
    ylEmpty
  },
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
          path: '/b2b_dashboard'
        },
        {
          title: '采购订单管理'
        },
        {
          title: '采购订单',
          path: '/b2b_purchase_order/b2b_purchase_order_list'
        },
        {
          title: '订单详情'
        }
      ],
      // 活动类型
      typeArray: [
        {
          label: '特价',
          value: 2
        },
        {
          label: '秒杀',
          value: 3
        },
        {
          label: '套装',
          value: 4
        },
        {
          label: '预售',
          value: 6
        }
      ],
      data: {}
    };
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getPurchaseDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        if (data.orderLogInfo && data.orderLogInfo.length) {
          data.orderLogInfo = data.orderLogInfo.map(item => {
            item.info = formatDate(item.logTime) + ' ' + item.logContent
            return item
          })
        }
        this.data = data
      }
    },
    // 去支付
    async payClick() {
      this.$common.showLoad()
      let list = []
      let order = {}
      order.orderId = this.id
      order.orderNo = this.data.orderNo
      list.push(order)
      let tradeType = 2
      if (this.data.orderCategory == 2) {
        if (this.data.paymentNameType == 1) {
          tradeType = 1
        }
        if (this.data.paymentNameType == 2) {
          tradeType = 4
        }
      }
      let payId = await toPay(tradeType, list)
      if (payId) {
        let redirectUrl = window.location.origin + `/#/b2b_purchase_order/b2b_purchase_pay_status?tab_platform=3&id=${this.id}&payId=${payId}`
        let payData = await paymentPay(payId, 'yeePayBank', redirectUrl, 'B2B-PC')
        if (payData.standardCashier) {
          this.$common.goThreePackage(payData.standardCashier)
        }
      }
      this.$common.hideLoad()
    },
    // 删除订单
    deleteDidClick() {
      this.$common.confirm('删除订单将无法查看，确认删除订单？', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await purchaseDelete(this.id)
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.alert('删除成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 取消订单
    cancleDidClick() {
      this.$common.confirm('订单取消后不可恢复，所使用的优惠券部分支持退回，具体见优惠券规则', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await purchaseCancle(this.id)
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.alert('取消成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 收货 退货
    receiveThePackage(type) {
      // type 1-确认收货 2-提交退货
      if (this.data.id) {
        this.$router.push(`/b2b_purchase_order/b2b_purchase_order_receive/${type}/${this.data.id}`)
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .app-container-content {
    .max-scroll {
      height: 150px;
      padding: 2px;
    }
    .header-box {
      .el-timeline {
        padding-left: 0 !important;
        .el-timeline-item {
          padding-bottom: 12px;
          .el-timeline-item__tail {
            border-left: 2px solid #D8D8D8 !important;
            z-index: 1;
            left: 2px !important;
          }
          .el-timeline-item__node--normal {
            width: 8px;
            height: 8px;
            border: 2px solid #BCBCBC;
            border-radius: 50%;
            background: #FFFFFF !important;
            z-index: 2;
          }
          .el-timeline-item__wrapper {
            padding-left: 12px;
          }
          .el-timeline-item__content {
            font-size: 12px !important;
            color: $font-title-color !important;
          }
          .el-timeline-item__timestamp {
            margin-top: 0;
          }
        }
        .active {
          .el-timeline-item__node--normal {
            border: 2px solid #1B9AEE;
          }
        }
      }
    }
  }
</style>
