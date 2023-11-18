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
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>收货地址信息
              </div>
              <div class="box-text" v-if="data.orderAddress && data.orderAddress.name">
                <div class="item"><span class="font-title-color">收货人：</span>{{ data.orderAddress.name }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.orderAddress.mobile }}</div>
                <div class="item"><span class="font-title-color">收货地址：</span>{{ data.orderAddress.address }}</div>
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
                <div class="item"><span class="font-title-color">原价总金额：</span>{{ data.originalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">成交价总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
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
                <el-timeline v-if="false">
                  <el-timeline-item
                    v-for="(activity, index) in data.orderLogInfo"
                    :key="index"
                    :class="index === 0 ? 'active' : ''"
                    :timestamp="activity.info">
                  </el-timeline-item>
                </el-timeline>
                <yl-empty v-else></yl-empty>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8" v-if="data.ylflag">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>票折信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">票折总金额：</span>{{ data.ticketDiscountAmount | toThousand('￥') }}</div>
                <yl-table
                  v-if="data.ticketDiscountList && data.ticketDiscountList.length"
                  border
                  stripe
                  cell-no-pad
                  :header-cell-style="{padding: '4px', 'font-size': '14px'}"
                  :row-style="{padding: '4px'}"
                  :show-header="true"
                  :list="data.ticketDiscountList.slice(0,2)">
                  <el-table-column label="票折单号" prop="ticketDiscountNo" align="center">
                  </el-table-column>
                  <el-table-column label="票折金额" prop="ticketDiscountAmount" align="center">
                  </el-table-column>
                </yl-table>
              </div>
              <div class="ticket-more" v-if="data.ticketDiscountList && data.ticketDiscountList.length">
                <yl-button type="text" @click="checkMoreTicketDiscount">更多</yl-button>
              </div>
            </div>
          </el-col>
          <el-col :span="16" v-if="data.ylflag">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>发票信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">发票申请人：</span>{{ data.orderInvoiceApplyAllInfo ? data.orderInvoiceApplyAllInfo.createUserName : '- -' }}</div>
                <div class="item"><span class="font-title-color">发票申请时间：</span>{{ data.orderInvoiceApplyAllInfo ? $options.filters.formatDate(data.orderInvoiceApplyAllInfo.createTime) : '- -' }}</div>
                <div class="item"><span class="font-title-color">发票状态：</span>{{ data.orderInvoiceApplyAllInfo ? $options.filters.dictLabel(data.orderInvoiceApplyAllInfo.status, orderTicketStatus) : '- -' }}</div>
                <yl-table
                  v-if="data.orderInvoiceApplyAllInfo && data.orderInvoiceApplyAllInfo.orderInvoiceInfo && data.orderInvoiceApplyAllInfo.orderInvoiceInfo.length"
                  border
                  stripe
                  cell-no-pad
                  :header-cell-style="{padding: '4px', 'font-size': '14px'}"
                  :row-style="{padding: '4px'}"
                  :show-header="true"
                  :list="data.orderInvoiceApplyAllInfo.orderInvoiceInfo.slice(0,2)">
                  <el-table-column label="发票单号" prop="invoiceNo" align="center">
                  </el-table-column>
                  <el-table-column label="发票金额" prop="invoiceAmount" align="center">
                  </el-table-column>
                </yl-table>
              </div>
              <div class="ticket-more" v-if="data.orderInvoiceApplyAllInfo && data.orderInvoiceApplyAllInfo.status === 3">
                <yl-button type="text" @click="checkMoreTicket">更多</yl-button>
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
              <div class="desc-box flex-row-left">
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
                  <div class="text-item"></div>
                  <div class="text-item"></div>
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
        <div class="gift-box" v-if="data.orderGiftList && data.orderGiftList.length">
          <div class="header-bar mar-b-8">
            <div class="sign"></div>赠品信息
          </div>
          <el-row :gutter="8">
            <el-col :span="12" v-for="(item, index) in data.orderGiftList" :key="index">
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.pictureUrl"></el-image>
                <div>
                  <div class="text-item"><span class="font-title-color">商品名称：</span>{{ item.name }}</div>
                  <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.specifications }}</div>
                  <div><span class="font-title-color">平台：</span>{{ item.sponsorType == 1 ? '平台' : '商家' }}赠送</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
    <yl-dialog
      title="发票信息"
      click-right-btn-close
      :visible.sync="showTicket">
      <div class="order-ticket-check" v-if="data.orderInvoiceApplyAllInfo">
        <div class="order-content">
          <div class="font-size-base font-important-color flex-row-left mar-b-8">
            <div class="font-title-color flex1"><span>订单号：</span>{{ data.orderNo || '- -' }}</div>
            <div class="font-title-color flex1"><span>发票状态：</span>{{ data.orderInvoiceApplyAllInfo.status ? $options.filters.dictLabel(data.orderInvoiceApplyAllInfo.status, orderTicketStatus) : '- -' }}</div>
          </div>
          <div class="font-size-base font-important-color flex-row-left mar-b-10">
            <div class="font-title-color flex1"><span>发票申请人：</span>{{ data.orderInvoiceApplyAllInfo.createUserName || '- -' }}</div>
            <div class="font-title-color flex1"><span>发票申请时间：</span>{{ data.orderInvoiceApplyAllInfo.createTime | formatDate }}</div>
          </div>
        </div>
        <yl-table
          border
          :show-header="true"
          :list="data.orderInvoiceApplyAllInfo.orderInvoiceInfo">
          <el-table-column label="发票单号" prop="invoiceNo" align="center">
          </el-table-column>
          <el-table-column label="发票金额" prop="invoiceAmount" align="center">
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
    <yl-dialog
      title="票折信息"
      click-right-btn-close
      :visible.sync="showTicketDiscount">
      <div class="order-ticket-check" v-if="data.ticketDiscountList">
        <div class="order-content">
          <div class="font-size-base font-important-color flex-row-left mar-b-8">
            <div class="font-title-color flex1"><span>票折总金额：</span>{{ data.ticketDiscountAmount | toThousand('￥') }}</div>
          </div>
        </div>
        <yl-table
          border
          :show-header="true"
          :list="data.ticketDiscountList">
          <el-table-column label="票折单号" prop="ticketDiscountNo" align="center">
          </el-table-column>
          <el-table-column label="票折金额" prop="ticketDiscountAmount" align="center">
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getSellOrderDetail } from '@/subject/pop/api/b2b_api/sell_order'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus } from '@/subject/pop/utils/busi'
import ylEmpty from '@/common/components/Empty'
import { formatDate } from '@/subject/pop/utils'
import { mapGetters } from 'vuex'

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
    },
    // 发票
    orderTicketStatus() {
      return orderTicketStatus()
    },
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
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
          title: '销售订单管理'
        },
        {
          title: '销售订单',
          path: '/b2b_sell_order/b2b_sell_order_list'
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
      data: {},
      // 查看发票
      showTicket: false,
      // 查看票折
      showTicketDiscount: false
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
      let data = await getSellOrderDetail(this.id)
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
    // 收货 退货
    receiveThePackage() {
      if (this.data.id) {
        this.$router.push(`/buyOrder/buyOrder_receive/${this.data.id}`)
      }
    },
    // 查看更多发票信息
    checkMoreTicket() {
      this.showTicket = true
    },
    // 查看更多票折信息
    checkMoreTicketDiscount() {
      this.showTicketDiscount = true
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
      padding: 4px 2px;
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
