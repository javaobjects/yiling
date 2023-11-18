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
                <div class="item"><span class="font-title-color">单据编号：</span>{{ data.returnNo }}</div>
                <div class="item"><span class="font-title-color">单据类型：</span>{{ data.returnType | dictLabel(orderReturnStatus) }}</div>
                <div class="item"><span class="font-title-color">单据状态：</span>{{ data.returnStatus | dictLabel(returnStatus) }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>收货地址信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">收货人：</span>{{ data.receiveUserName }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.receiveUserMobile }}</div>
                <div class="item"><span class="font-title-color">收货地址：</span>{{ data.receiveUserAdress }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>采购商名称
              </div>
              <div class="box-text" v-if="data.buyerEnterpriseInfo">
                <div class="item"><span class="font-title-color"></span>{{ data.buyerEnterpriseInfo.name }}</div>
              </div>
              <div class="header-bar">
                <div class="sign"></div>供应商名称
              </div>
              <div class="box-text" v-if="data.sellerEnterpriseInfo">
                <div class="item"><span class="font-title-color"></span>{{ data.sellerEnterpriseInfo.name }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>退款信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">退款总金额：</span>{{ data.returnAllAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">实退总金额：</span>{{ data.returnAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>日志信息
              </div>
              <div class="box-text" v-if="data.logList && data.logList.length">
                <el-scrollbar wrap-class="max-scroll">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(activity, index) in data.logList"
                      :key="index"
                      :class="index === 0 ? 'active' : ''"
                      :timestamp="activity.info">
                    </el-timeline-item>
                  </el-timeline>
                </el-scrollbar>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailVOList && data.orderDetailVOList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailVOList" :key="index">
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
                  <div class="text-item"><span class="font-title-color">收货数量：</span>{{ item.receiveQuantity }}</div>
                  <div class="text-item"><span class="font-title-color">退货数量：</span>{{ item.returnQuantity }}</div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">退款金额：</span>{{ item.totalReturnAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">实退金额：</span>{{ item.returnAmount | toThousand('￥') }}</div>
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

  </div>
</template>

<script>
import { refundEnterpriseDetail } from '@/subject/pop/api/zt_api/order'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnSource, returnStatus } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'

export default {
  name: 'ZtGoodsReturnedDetail',
  components: {
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
    // 支付状态
    orderPayStatus() {
      return orderPayStatus()
    },
    // 来源
    returnSource() {
      return returnSource()
    },
    // 退货单审核状态
    returnStatus() {
      return returnStatus()
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
          title: '企业退货单',
          path: '/zt_order_manage/zt_goods_returned_list'
        },
        {
          title: '退货单详情'
        }
      ],
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
      let data = await refundEnterpriseDetail(this.id)
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
