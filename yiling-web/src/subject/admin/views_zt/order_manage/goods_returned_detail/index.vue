<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box return-summary-box">
        <div class="return-summary-item"><span class="item-title">订单编号：</span>{{ data.orderNo }}</div>
        <div class="return-summary-item"><span class="item-title">订单来源：</span>{{ data.returnSource | dictLabel(returnSource) }}</div>
        <div class="return-summary-item"><span class="item-title">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}</div>
        <div class="return-summary-item"><span class="item-title">单据编号：</span>{{ data.returnNo }}</div>
        <div class="return-summary-item"><span class="item-title">单据类型：</span>{{ data.returnType | dictLabel(orderReturnStatus) }}</div>
      </div>
      <div class="order-info-box">
        <el-collapse v-model="activeNames">
          <el-collapse-item name="buyerEnterpriseInfo">
            <template slot="title">采购商信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text" v-if="data.buyerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">采购商：</span>{{ data.buyerEnterpriseInfo.name }}</div>
                  <div class="item"><span class="font-title-color">下单时间：</span>{{ data.createTime | formatDate }}</div>
                  <div class="item remark" v-if="data.remark">
                    <span class="font-title-color">备注：</span>
                    <el-input type="textarea" :rows="2" :value="data.remark" disabled></el-input>
                  </div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.buyerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系人：</span>{{ data.buyerEnterpriseInfo.contactor }}</div>
                  <div class="item"><span class="font-title-color">订单来源：</span>{{ data.returnSource | dictLabel(returnSource) }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.buyerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系电话：</span>{{ data.buyerEnterpriseInfo.contactorPhone }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.buyerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系地址：</span>{{ data.buyerEnterpriseInfo.address }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="enterpriseDistributorInfo">
            <template slot="title">供应商信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text" v-if="data.sellerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">企业名称：</span>{{ data.sellerEnterpriseInfo.name }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.sellerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系人：</span>{{ data.sellerEnterpriseInfo.contactor }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.sellerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系电话：</span>{{ data.sellerEnterpriseInfo.contactorPhone }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text" v-if="data.sellerEnterpriseInfo">
                  <div class="item"><span class="font-title-color">联系地址：</span>{{ data.sellerEnterpriseInfo.address }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="returnInfo">
            <template slot="title">退款信息
              <div class="pay">
                <span>实退货款金额：</span>
                <span class="pay-amount">{{ data.returnAmount | toThousand('￥') }}</span>
              </div>
            </template>
            <el-row>
              <el-col :span="6" v-if="false">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">货款总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6" v-if="false">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6" v-if="false">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">实付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"><span class="font-title-color">实退货款金额：</span>{{ data.returnAmount | toThousand('￥') }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="returnOrderInfo">
            <template slot="title">退单轨迹</template>
            <el-row>
              <el-col :span="24">
                <div class="box-text">
                  <yl-time-line :data="data.logList"></yl-time-line>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <!-- 物流模块暂时保留 B2B-72 -->
          <el-collapse-item name="deliveryInfo" v-if="false">
            <template slot="title">物流信息</template>
            <el-row>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"> <span class="font-title-color">物流（快递）单号：</span>{{ data.deliveryNo || "- -" }}</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="box-text">
                  <div class="item"> <span class="font-title-color">物流（快递）名称：</span>{{ data.deliveryCompany || "- -" }}</div>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
          <el-collapse-item name="returnOrderGoodsInfo">
            <template slot="title">商品信息
            </template>
            <div class="buy-order-table">
              <div class="expand-row" v-if="data.orderDetailVOList && data.orderDetailVOList.length">
                <div class="expand-view" v-for="(item, index) in data.orderDetailVOList" :key="index">
                  <div class="title">
                    <div class="goods-name">{{ item.goodsName }}</div>
                    <div v-if="item.promotionActivityType === 2" :class="['goods-tag', item.promotionActivityType === 2 ? 'special-sale' : '']">特价</div>
                    <div v-if="item.promotionActivityType === 3" :class="['goods-tag', item.promotionActivityType === 3 ? 'flash-sale' : '']">秒杀</div>
                  </div>
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
                      <div class="text-item"><span class="font-title-color">收货数量：</span>{{ item.receiveQuantity }}</div>
                      <div class="text-item"><span class="font-title-color">退货数量：</span>{{ item.returnQuantity }}</div>
                    </div>
                    <div class="right-text box3">
                      <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">退款金额：</span>{{ item.totalReturnAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">实退金额：</span>{{ item.returnAmount | toThousand('￥') }}</div>
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
  </div>
</template>

<script>
import { getRejectOrderDetail } from '@/subject/admin/api/zt_api/order'
import { paymentMethod, orderStatus, orderPayStatus, orderReturnStatus, returnSource } from '@/subject/admin/utils/busi'
import ylTimeLine from '@/common/components/TimeLine'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'GoodsReturnedDetail',
  components: {
    ylTimeLine
  },
  computed: {
     // 退货类型
    orderReturnStatus() {
      return orderReturnStatus()
    },
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
    // 来源
    returnSource() {
      return returnSource()
    }
  },
  data() {
    return {
      // 展开面板
      activeNames: ['buyerEnterpriseInfo','enterpriseDistributorInfo','returnInfo','returnOrderInfo','deliveryInfo','returnOrderGoodsInfo'],
      // 头部导航
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
      let data = await getRejectOrderDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        if (data.logList && data.logList.length) {
          data.logList = data.logList.map(item => {
            item.info = formatDate(item.logTime) + ' ' + item.logContent
            return item
          })
        }
        this.data = data
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
