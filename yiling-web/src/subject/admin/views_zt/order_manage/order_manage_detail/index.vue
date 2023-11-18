<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box order-summary-box">
        <div class="order-summary-item"><span class="item-title">订单编号：</span>{{ data.orderNo }}</div>
        <div class="order-summary-item"><span class="item-title">订单类型：</span>{{ data.orderSource | dictLabel(orderSource) }}</div>
        <div class="order-summary-item"><span class="item-title">下单时间：</span>{{ data.createTime | formatDate }}</div>
      </div>
      <div class="order-info-box">
        <el-collapse v-model="activeNames">
          <el-collapse-item name="enterpriseBuyerInfo">
            <template slot="title">采购商信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseBuyerInfo">
                  <div class="item"><span class="font-title-color">采购商：</span>{{ data.enterpriseBuyerInfo.name }}</div>
                  <div class="item"><span class="font-title-color">下单时间：</span>{{ data.createTime | formatDate }}</div>
                  <div class="item remark" v-if="data.orderNote">
                    <span class="font-title-color">备注：</span>
                    <el-input type="textarea" :rows="2" :value="data.orderNote" disabled></el-input>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseBuyerInfo">
                  <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseBuyerInfo.contactor }}</div>
                  <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }} （{{ data.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseBuyerInfo">
                  <div class="item"><span class="font-title-color">联系电话：</span>{{ data.enterpriseBuyerInfo.contactorPhone }}</div>
                  <div class="item"><span class="font-title-color">订单来源：</span>{{ data.orderSource | dictLabel(orderSource) }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseBuyerInfo">
                  <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseBuyerInfo.address }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="enterpriseDistributorInfo">
            <template slot="title">供应商信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseDistributorInfo">
                  <div class="item"><span class="font-title-color">企业名称：</span>{{ data.enterpriseDistributorInfo.name }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseDistributorInfo">
                  <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseDistributorInfo.contactor }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseDistributorInfo">
                  <div class="item"><span class="font-title-color">联系电话：</span>{{ data.enterpriseDistributorInfo.contactorPhone }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.enterpriseDistributorInfo">
                  <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseDistributorInfo.address }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="paymentInfo">
            <template slot="title">付款信息
              <div class="pay">
                <span>实付总金额：</span>
                <span class="pay-amount">{{ data.paymentAmount | toThousand('￥') }}</span>
              </div>
            </template>
            <el-row>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">原价总金额：</span>{{ data.originalAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">成交价总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item" v-if="data.orderSource !== 3"><span class="font-title-color">票折总金额：</span>{{ data.ticketDiscountAmount || '- -' }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="invoiceInfo" v-if="data.orderSource !== 3">
            <template slot="title">发票信息
            </template>
            <el-row>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">发票状态：</span>{{ data.orderInvoiceApplyAllInfo ? $options.filters.dictLabel(data.orderInvoiceApplyAllInfo.status, orderTicketStatus) : '- -' }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">已开发票张数：</span>{{ data.orderInvoiceApplyAllInfo ? data.orderInvoiceApplyAllInfo.invoiceNumber : '- -' }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <el-link type="primary" :underline="false" v-if="data.orderInvoiceApplyAllInfo && data.orderInvoiceApplyAllInfo.orderInvoiceInfo.length" @click="checkMoreTicket">更多</el-link>
                </div>
              </el-col>
            </el-row>
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
          </el-collapse-item>
          <el-collapse-item name="orderLogInfo">
            <template slot="title">订单轨迹</template>
            <el-row>
              <el-col :span="24">
                <div class="box-text">
                  <yl-time-line :data="data.orderLogInfo"></yl-time-line>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="receiveReceiptInfo">
            <template slot="title">物流信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"> <span class="font-title-color">物流（快递）单号：</span>{{ data.deliveryNo || "- -" }}</div>
                  <div class="item" v-if="data.orderSource === 1 || data.orderSource === 2">
                    <span class="font-title-color receive">收货回执单：</span>
                    <div class="img-box" v-for="(img, idx) in data.receiveReceiptList" :key="idx">
                      <el-image
                        class="img-item"
                        fit="cover"
                        :preview-src-list="data.receiveReceiptPreviewList"
                        :src="img.fileUrl" />
                      <div class="screen flex-row-center">
                        <svg-icon class="icon" icon-class="img-open"></svg-icon>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"> <span class="font-title-color">物流（快递）名称：</span>{{ data.deliveryCompany || "- -" }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"> <span class="font-title-color">物流（快递）类型：</span>{{ formatDeliveryType(data.deliveryType) }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="orderGoodsInfo">
            <template slot="title">商品信息
              <div class="pay">
                <span>实付总金额：</span>
                <span class="pay-amount">{{ data.paymentAmount | toThousand('￥') }}</span>
              </div>
            </template>
            <div class="buy-order-table">
              <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
                <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
                  <div class="title flex-row-left">{{ item.goodsName }}<span v-if="item.promotionActivityType" class="activity-type">{{ item.promotionActivityType | dictLabel(typeArray) }}</span></div>
                  <div class="desc-box flex-row-left">
                    <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                    <div class="right-text box1">
                      <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                      <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                      <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                    </div>
                    <div class="right-text box2">
                      <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                      <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                      <div class="text-item"></div>
                    </div>
                    <div class="right-text box3">
                      <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                    </div>
                    <div class="right-text box4">
                      <div class="text-item">
                        <yl-button type="text" v-if="item.orderDeliveryList && item.orderDeliveryList.length" @click="deliveryDetail(item)">查看发货详情</yl-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div class="bottom-view flex-row-center">
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
    <!-- 发货详情 批次 -->
    <yl-dialog
      title="发货详情"
      click-right-btn-close
      :visible.sync="showDelivery">
      <div class="order-ticket-check dialog-content">
        <div v-if="orderDeliveryList.length">
          <yl-table
            border
            :show-header="true"
            :list="orderDeliveryList">
            <el-table-column label="批次号" prop="batchNo" align="center"> </el-table-column>
            <el-table-column label="有效期" prop="expiryDate" align="center">
              <template slot-scope="{ row }">
                <div> {{ row.expiryDate | formatDate }} </div>
              </template>
            </el-table-column>
            <el-table-column label="发货数量" prop="deliveryQuantity" align="center">
              <template slot-scope="{ row }">
                <div> {{ row.deliveryQuantity || "- -" }} </div>
              </template>
            </el-table-column>
            <el-table-column label="收货数量" prop="receiveQuantity" align="center">
              <template slot-scope="{ row }">
                <div> {{ row.receiveQuantity || "- -" }} </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getOrderDetail } from '@/subject/admin/api/zt_api/order'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus, orderSource } from '@/subject/admin/utils/busi'
import ylTimeLine from '@/common/components/TimeLine'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'OrderManageDetail',
  components: {
    ylTimeLine
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
      // 展开面板
      activeNames: ['enterpriseBuyerInfo','enterpriseDistributorInfo','paymentInfo','invoiceInfo','orderLogInfo','receiveReceiptInfo','orderGoodsInfo'],
      // 头部导航
      data: {},
      // 查看发票
      showTicket: false,
      // 查看发货详情
      showDelivery: false,
      // 发货详情列表
      orderDeliveryList: [],
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
      ]
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
        data.receiveReceiptPreviewList = data.receiveReceiptList.map(item => {
          return item.fileUrl
        })
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
    // 查看库存详情
    deliveryDetail(item) {
      if (item.orderDeliveryList) {
        this.orderDeliveryList = [...item.orderDeliveryList]
        this.showDelivery = true
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
