<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
    <!-- 上部 信息 -->
      <div class="top-box">
        <el-row>
          <el-col :span="10">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">结算单金额信息</span>
            </div>
            <el-row>
              <el-col :span="14">
                <div v-if="data.type == 1 || data.type == 2">
                  <div class="font-size-base font-title-color item-text">
                  {{ data.type == 1 ? '货款金额：' : '优惠券总金额：' }}
                  <span v-if="data.type =='1'">
                    {{ data.goodsAmount | toThousand('￥') }}
                  </span>
                  <span v-else>
                    {{ data.couponAmount | toThousand('￥') }}
                  </span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    {{ data.type =='1' ? '货款退款金额：': '优惠券退款金额：' }}
                    {{ data.type =='1' ? data.refundGoodsAmount : data.refundCouponAmount | toThousand('￥-') }}
                  </div>
                  <div class="font-size-base font-title-color item-text" v-if="data.type == 1">
                    结算总金额：
                    <span style="color:#07c160">{{ data.amount | toThousand('￥') }}</span>
                  </div>
                </div>
                <div v-if="data.type == 2">
                  <div class="font-size-base font-title-color item-text">
                    秒杀/特价总金额： 
                    <span>{{ data.promotionAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    秒杀/特价退款金额：
                    <span>{{ data.refundPromotionAmount | toThousand('￥-') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    满赠总金额：
                    <span>{{ data.giftAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    满赠退款金额：
                    <span>{{ data.refundGiftAmount | toThousand('￥-') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    套装总金额：
                    <span>{{ data.comPacAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    套装退款金额：
                    <span>{{ data.refundComPacAmount | toThousand('￥-') }}</span>
                  </div>
                </div>
                <div v-if="data.type == 3">
                  <div class="font-size-base font-title-color item-text">
                    预售违约金额：
                    <span>{{ data.presaleDefaultAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    预售违约退款金额：
                    <span>{{ data.refundPresaleDefaultAmount | toThousand('￥-') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    结算总金额：
                    <span style="color:#07c160">{{ data.amount | toThousand('￥') }}</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="10">
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
                    <span>{{ data.presaleDiscountAmount | toThousand('￥') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    预售优惠退款金额： 
                    <span>{{ data.refundPreAmount | toThousand('￥-') }}</span>
                  </div>
                  <div class="font-size-base font-title-color item-text">
                    结算总金额：
                    <span style="color:#07c160">{{ data.amount | toThousand('￥') }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="8" style="padding-left:20px; border-left:1px solid #f2f3f4">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">结算单信息</span>
            </div>
            <div class="font-size-base font-title-color item-text">
              结算编号：
              {{ data.code }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算类型：
              {{ data.type | dictLabel(settlementType) }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算状态：
              {{ data.status | dictLabel(settlementStatus) }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算单生成时间：
              {{ data.createTime | formatDate }}
            </div>
            <div class="font-size-base font-title-color item-text">
              结算单结算时间：
              {{ data.settlementTime | formatDate }}
            </div>
            <div class="font-size-base font-title-color item-text">
              备注：
              {{ data.settlementRemark }}
            </div>
          </el-col>
          <el-col :span="6" style="padding-left:20px; border-left:1px solid #f2f3f4" v-if="data.type != 3">
            <div class="flex-row-left item">
              <div class="line-view"></div>
              <span class="font-size-lg bold">收款账户信息</span>
            </div>
            <div class="font-size-base font-title-color item-text">企业名称：{{ data.name }}</div>
            <div class="font-size-base font-title-color item-text">开户行：{{ data.bankName }}</div>
            <div class="font-size-base font-title-color item-text">开户地：{{ data.address }}</div>
            <div class="font-size-base font-title-color item-text">支行信息：{{ data.subBankName }}</div>
            <div class="font-size-base font-title-color item-text">银行账户：{{ data.account }}</div>
          </el-col>
        </el-row>
      </div>
      <!-- 下部列表信息 -->
      <div class="mar-t-16 pad-b-10 order-table-view" v-if="data.records">
        <yl-table
          ref="table"
          :list="data.records"
          :total="query.total"
          :row-class-name="() => 'table-row'"
          :cell-class-name="getCellClass"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="基本信息" min-width="320" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color">
                  <span>采购商：</span>{{ row.buyerName }} 
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column v-if="data.type == 1 || data.type == 2" :label="data.type == 1 ? '金额信息' : '平台承担优惠券金额信息'" min-width="200" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>{{ data.type == '1' ? "货款金额：" : "优惠券金额：" }}</span> 
                    {{ data.type == '1' ? row.goodsAmount : row.couponAmount | toThousand('￥') }}
                  </div>
                <div class="item-text font-size-base font-title-color">
                  <span>{{ data.type == '1' ? "货款退款金额：" : "优惠券退款金额：" }}</span> 
                  {{ data.type == '1' ? row.refundGoodsAmount : row.refundCouponAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额信息" min-width="200" align="left" v-if="data.type == 3">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售违约金额：</span>
                  {{ row.presaleDefaultAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售违约退款金额：</span>
                  {{ row.refundPresaleDefaultAmount | toThousand('￥') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担秒杀/特价金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>秒杀/特价金额：</span>
                    {{ row.promotionAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>秒杀/特价退款金额：</span> {{ row.refundPromotionAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担满赠金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>满赠金额：</span>
                    {{ row.giftAmount | toThousand('￥') }}
                  </div>
                <div class="item-text font-size-base font-title-color">
                  <span>满赠退款金额：</span> 
                  {{ row.refundGiftAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担套装金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>套装金额：</span>
                    {{ row.comPacAmount | toThousand('￥') }}
                  </div>
                <div class="item-text font-size-base font-title-color">
                  <span>套装退款金额：</span> {{ row.refundComPacAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担预售优惠金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售优惠金额：</span>
                    {{ row.presaleDiscountAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>预售优惠退款金额：</span> 
                  {{ row.refundPreAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="平台承担支付促销金额信息" min-width="200" align="left" v-if="data.type == 2">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>支付促销金额：</span>
                    {{ row.payDiscountAmount | toThousand('￥') }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>支付促销退款金额：</span> 
                  {{ row.refundPayAmount | toThousand('￥-') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="总计金额" min-width="200" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>结算金额：</span>{{ row.amount | toThousand('￥') }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="showDetail(row)">查看详情</yl-button></div>
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
import { querySettlementDetailPageList } from '@/subject/admin/api/b2b_api/financial'
import { b2bSettlementType, b2bSettlementStatus } from '@/subject/admin/utils/busi'
export default {
  name: 'SettlementSheetDetailed',
  computed: {
    settlementType() {
      return b2bSettlementType()
    },
    settlementStatus() {
      return b2bSettlementStatus()
    }
  },
  data() {
    return {
      data: {
        goodsAmount: '',
        refundAmount: '',
        amount: '',
        code: '',
        type: '',
        status: '',
        createTime: '',
        settlementTime: '',
        name: '',
        bankName: '',
        address: '',
        subBankName: '',
        account: '',
        saleAmount: '',
        settlementRemark: '',
        promotionAmount: '',
        refundPromotionAmount: '',
        giftAmount: '',
        refundGiftAmount: '',
        discountAmount: '',
        refundGoodsAmount: '',
        refundDiscountAmount: '',
        couponAmount: '',
        refundCouponAmount: '',
        comPacAmount: '',
        refundComPacAmount: '',
        presaleDefaultAmount: '',
        refundPresaleDefaultAmount: '',
        presaleDiscountAmount: '',
        refundPreAmount: '',
        preDefSettlementAmount: '',
        payDiscountAmount: '',
        refundPayAmount: ''
      },
      query: {
        total: 0,
        page: 1,
        limit: 10,
        //结算单id
        settlementId: ''
      },
      loading: false
    }
  },
   mounted() {
     let query = this.$route.params;
     if (query.id) {
       this.query.settlementId = query.id;
       this.getList();
     }
   },
   methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await querySettlementDetailPageList(
        query.page,
        query.settlementId,
        query.limit
      ) 
      if (data) {
        this.data = data;
        this.query.total = data.total
      }
      this.loading = false;
    },
    getCellClass({row,rowIndex}) {
      return 'border-1px-b'
    },
    // 查看明细
    showDetail(row) {
      this.$router.push({
        name: 'OrderItemDetailed',
        params: { 
          orderId: row.orderId,
          settlementId: this.query.settlementId
        }
      })
    }
   }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>