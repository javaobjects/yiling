<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
    <!-- 上部 信息 -->
      <div class="top-box">
        <el-row>
          <el-col :span="12" style="border-right:1px solid #f2f3f4">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">订单信息</span>
            </div>
            <div class="font-size-base font-title-color item-text">
              订单编号：{{ data.orderNo }}
            </div>
            <div class="font-size-base font-title-color item-text">
              支付方式：{{ data.paymentMethod | dictLabel(method) }} 
              <span v-if="data.payChannel != ''">
                ({{ data.payChannel | dictLabel(channel) }})
              </span>
            </div>
            <div class="font-size-base font-title-color item-text">
              结算状态：
              <span>{{ data.status | dictLabel(settlementStatus) }}</span>
            </div>
            <div class="font-size-base font-title-color item-text">
              下单时间：{{ data.orderCreateTime | formatDate }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算单生成时间：{{ data.createTime | formatDate }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算单结算时间：{{ data.settlementTime | formatDate }}
            </div>
          </el-col>
          <el-col :span="12" style="padding-left:20px">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">
                {{ data.type == 2 ? '结算单金额信息' : '金额信息' }}
              </span>
            </div>
            <el-row>
              <el-col :span="10">
                <div class="font-size-base font-title-color item-text">
                  <span v-if="data.type == 1">
                    货款金额：{{ data.goodsAmount | toThousand('￥') }}
                  </span>
                  <span v-else-if="data.type == 2">
                    优惠券总金额：{{ data.couponAmount | toThousand('￥') }}
                  </span>
                  <span v-else>
                    预售违约金额：{{ data.presaleDefaultAmount | toThousand('￥') }}
                  </span>
                </div>
                <div class="font-size-base font-title-color item-text">
                  <span v-if="data.type == 1">
                    退款金额：{{ data.refundGoodsAmount | toThousand('￥-') }}
                  </span>
                  <span v-if="data.type == 2">
                    优惠券退款金额：{{ data.refundCouponAmount | toThousand('￥-') }}
                  </span>
                  <span v-if="data.type == 3">
                    预售违约退款金额：
                    {{ data.refundPresaleDefaultAmount | toThousand('￥-') }}
                  </span>
                </div>
                <div class="font-size-base font-title-color item-text" v-if="data.type == 1">
                  结算总金额：<span style="color:#07c160">{{ data.goodsSettlementAmount | toThousand('￥') }}</span>
                </div>
                <div class="font-size-base font-title-color item-text" v-if="data.type == 3">
                  结算总金额：<span style="color:#07c160">{{ data.preDefSettlementAmount | toThousand('￥') }}</span>
                </div>
                <div v-if="data.type == 2">
                  <div class="font-size-base font-title-color item-text">
                    秒杀/特价总金额：{{ data.promotionAmount | toThousand('￥') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    秒杀/特价退款金额：{{ data.refundPromotionAmount | toThousand('￥-') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    满赠总金额：{{ data.giftAmount | toThousand('￥') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    满赠退款金额：{{ data.refundGiftAmount | toThousand('￥-') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    套装总金额：{{ data.comPacAmount | toThousand('￥') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    套装退款金额：{{ data.refundComPacAmount | toThousand('￥-') }}
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div v-if="data.type == 2">
                  <div class="font-size-base font-title-color item-text">
                    支付促销总金额： 
                    <span>{{ data.payDiscountAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    支付促销退款金额： 
                    <span>{{ data.refundPayAmount | toThousand('￥-') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    预售优惠总金额：
                    {{ data.presaleDiscountAmount | toThousand('￥') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    预售优惠退款金额：
                    {{ data.refundPreAmount | toThousand('￥-') }}
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    结算总金额：
                    <span style="color:#07c160">{{ data.saleSettlementAmount | toThousand('￥') }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
      <!-- 下部列表信息 -->
      <div class="mar-t-16 pad-b-30 order-table-view" v-if="data.orderDetailList">
        <yl-table
          ref="table"
          :list="data.orderDetailList"
          :total="query.total"
          :row-class-name="() => 'table-row'"
          :cell-class-name="getCellClass"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="基本信息" min-width="340" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.goodsName }}</div>
                <div class="item-text font-size-base font-title-color text-length">
                  <span>生产厂家：</span>
                  <span class="title" v-if="row.goodsManufacturer.length < 17">{{ row.goodsManufacturer }}</span>
                  <el-tooltip v-else class="title" placement="top">
                    <div slot="content" style="width:300px">{{ row.goodsManufacturer }}</div>
                    <span>{{ row.goodsManufacturer }}</span>
                  </el-tooltip>
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>规格：</span>
                  {{ row.goodsSpecification }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品数量信息" min-width="180" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>下单商品数量：</span> 
                  {{ row.goodsQuantity }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>发货商品数量：</span> 
                  {{ row.deliveryQuantity }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>签收商品数量：</span> 
                  {{ row.receiveQuantity }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额信息" min-width="180" align="left" v-if="data.type == 3">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>预售违约金小计：</span>
                  {{ row.presaleDefaultAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售违约退款小计：</span>
                  {{ row.refundPresaleDefaultAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="货款信息" min-width="180" align="left" v-if="data.type == 1">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>货款金额：</span>{{ row.goodsTotalAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>退款金额：</span>{{ row.goodsRefundAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担优惠券金额信息" min-width="180" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>优惠券金额：</span>{{ row.couponAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>优惠券退款金额：</span>{{ row.couponRefundAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担秒杀/特价金额信息" min-width="180" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>秒杀/特价金额：</span> 
                  {{ row.promotionSaleSubTotal | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>秒杀/特价退款金额：</span> 
                  {{ row.returnPromotionSaleSubTotal | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担套装金额信息" min-width="180" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>套装金额：</span> 
                  {{ row.comPacAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>套装退款金额：</span> 
                  {{ row.returnComPacAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担预售优惠金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>预售优惠金额：</span>
                    {{ row.presaleDiscountAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售优惠退款金额：</span> 
                  {{ row.returnPresaleDiscountAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担支付促销金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>支付促销金额：</span>
                    {{ row.paymentPlatformDiscountAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>支付促销退款金额：</span> 
                  {{ row.returnPlatformPaymentDiscountAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="小计信息" min-width="180" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color">
                  <span>结算小计：</span> 
                  <span style="color:#faab0c" v-if="data.type == 1">
                    {{ row.goodsSettlementAmount | toThousand('￥') }}
                  </span>
                  <span style="color:#faab0c" v-if="data.type == 2">
                    {{ row.saleSettlementAmount | toThousand('￥') }}
                  </span>
                  <span style="color:#faab0c" v-if="data.type == 3">
                    {{ row.preDefSettlementAmount | toThousand('￥') }}
                  </span>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { queryOrderDetailPageList } from '@/subject/admin/api/b2b_api/financial'
import { paymentMethod, paymentChannel, b2bSettlementStatus } from '@/subject/admin/utils/busi'
export default {
  name: 'OrderItemDetailed',
  computed: {
    method() {
      return paymentMethod()
    },
    channel() {
      return paymentChannel()
    },
    settlementStatus() {
      return b2bSettlementStatus()
    }
  },
  data() {
    return {
      data: {
        orderNo: '',
        paymentMethod: '',
        status: '',
        orderCreateTime: '',
        createTime: '',
        settlementTime: '',
        type: '',
        saleAmount: '',
        saleRefundAmount: '',
        saleSettlementAmount: '',
        goodsAmount: '',
        goodsRefundAmount: '',
        goodsSettlementAmount: '',
        payChannel: '',
        promotionAmount: '',
        refundPromotionAmount: '',
        giftAmount: '',
        refundGiftAmount: '',
        discountAmount: '',
        refundGoodsAmount: '',
        refundDiscountAmount: '',
        refundCouponAmount: '',
        couponAmount: '',
        comPacAmount: '',
        refundComPacAmount: '',
        presaleDefaultAmount: '',
        returnPreDefAmount: '',
        refundPresaleDefaultAmount: '',
        payDiscountAmount: '',
        refundPayAmount: ''
      },
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false
    }
  },
  mounted() {
    let data = this.$route.params;
    if (data.settlementId && data.orderId) {
      let val = data;
      this.getList(val);
    }
  },
  methods: {
    async getList(val) {
      let query = this.query;
      this.loading = true;
      let data = await queryOrderDetailPageList(
        query.page,
        val.orderId,
        val.settlementId,
        query.limit
      )
      if (data) {
        this.data = data;
      }
      this.loading = false;
    },
    getCellClass({row, rowIndex}) {
      return 'border-1px-b'
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>