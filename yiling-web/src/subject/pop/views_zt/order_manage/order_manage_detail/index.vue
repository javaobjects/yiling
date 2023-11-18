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
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }} （{{ data.paymentStatus | dictLabel(orderPayStatus) }}）</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>采购商信息
              </div>
              <div class="box-text" v-if="data.enterpriseBuyerInfo">
                <div class="item"><span class="font-title-color">采购商名称：</span>{{ data.enterpriseBuyerInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseBuyerInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.enterpriseBuyerInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseBuyerInfo.address }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>供应商信息
              </div>
              <div class="box-text" v-if="data.enterpriseDistributorInfo">
                <div class="item"><span class="font-title-color">供应商名称：</span>{{ data.enterpriseDistributorInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseDistributorInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.enterpriseDistributorInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseDistributorInfo.address }}</div>
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
                <div class="item"><span class="font-title-color">原价总金额：</span>{{ data.originalTotalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">成交价总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
                <div class="item" v-if="data.orderSource !== 3"><span class="font-title-color">票折总金额：</span>{{ data.ticketDiscountAmount || '- -' }}</div>
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
                <div class="item"><span class="font-title-color">物流(快递)方式：</span>{{ formatDeliveryType(data.deliveryType) }}</div>
                <div class="item"><span class="font-title-color">物流(快递)名称：</span>{{ data.deliveryCompany || "- -" }}</div>
                <div class="item"><span class="font-title-color">物流(快递)单号：</span>{{ data.deliveryNo || "- -" }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8" v-if="data.orderSource !== 3">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>发票信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">发票状态：</span>{{ data.orderInvoiceApplyAllInfo ? $options.filters.dictLabel(data.orderInvoiceApplyAllInfo.status, orderTicketStatus) : '- -' }}</div>
                <div class="item"><span class="font-title-color">已开发票张数：</span>{{ data.orderInvoiceApplyAllInfo ? data.orderInvoiceApplyAllInfo.invoiceNumber : '- -' }}</div>
                <yl-table
                  v-if="data.orderInvoiceApplyAllInfo && data.orderInvoiceApplyAllInfo.orderInvoiceInfo && data.orderInvoiceApplyAllInfo.orderInvoiceInfo.length"
                  border
                  stripe
                  cell-no-pad
                  :header-cell-style="{padding: '4px', 'font-size': '14px'}"
                  :row-style="{padding: '4px'}"
                  :show-header="true"
                  :list="data.orderInvoiceApplyAllInfo.orderInvoiceInfo.slice(0,2)">
                  <el-table-column label="发票申请单号" prop="applyId" align="center">
                  </el-table-column>
                  <el-table-column label="发票单号" prop="invoiceNo" align="center">
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
          <div class="pay">
            <span>实付总金额：</span>
            <span class="pay-amount">{{ data.paymentAmount | toThousand('￥') }}</span>
          </div>
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification+ '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
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
                  <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                </div>
              </div>
            </div>
          </div>
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
      <div class="order-ticket-check dialog-content">
        <div class="order-content">
          <div class="font-size-base font-important-color flex-row-left mar-b-8">
            <div class="font-title-color flex1"><span>发票状态：</span>{{ data.orderInvoiceApplyAllInfo ? $options.filters.dictLabel(data.orderInvoiceApplyAllInfo.status, orderTicketStatus) : '- -' }}</div>
            <div class="font-title-color flex1"><span>已开发票张数：</span>{{ data.orderInvoiceApplyAllInfo ? data.orderInvoiceApplyAllInfo.invoiceNumber : '- -' }}</div>
          </div>
        </div>
        <div v-if="data.orderInvoiceApplyAllInfo">
          <yl-table
            border
            :show-header="true"
            :list="data.orderInvoiceApplyAllInfo.orderInvoiceInfo">
            <el-table-column label="发票申请单号" prop="applyId" align="center">
            </el-table-column>
            <el-table-column label="发票单号" prop="invoiceNo" align="center">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getOrderDetail } from '@/subject/pop/api/zt_api/order'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus, orderSource } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'

export default {
  name: 'ZtOrderManageDetail',
  components: {
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
    // 来源
    orderSource() {
      return orderSource()
    },
    // 物流（快递）类型
    formatDeliveryType() {
      return function(type) {
        let res
        switch (type) {
          case 1:
            res = '自有物流'
            break;
          case 2:
            res = '第三方物流'
            break;
          default:
            res = '- -'
            break;
        }
        return res
      }
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/zt_dashboard'
        },
        {
          title: '企业订单数据'
        },
        {
          title: '企业订单',
          path: '/zt_order_manage/zt_order_manage_list'
        },
        {
          title: '订单详情'
        }
      ],
      // 头部导航
      data: {},
      // 查看发票
      showTicket: false,
      // 查看发货详情
      showDelivery: false
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
      let data = await getOrderDetail(this.id)
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
    // 查看更多发票信息
    checkMoreTicket() {
      this.showTicket = true
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