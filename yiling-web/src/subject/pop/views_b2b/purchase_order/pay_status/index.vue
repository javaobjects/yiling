<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box my-box">
        <div class="pay-view" v-if="paySuccess">
          <img class="pay-img" v-if="paySuccess == 2" src="@/subject/pop/assets/b2b-purchase/pay-success.png" />
          <img class="pay-img" v-if="paySuccess == 1" src="@/subject/pop/assets/b2b-purchase/pay-fail.png" />
          <div>
            <div class="success-text" v-if="paySuccess == 2">支付成功</div>
            <div class="fail-text" v-if="paySuccess == 1">支付失败</div>
            <div class="remain-text" v-if="paySuccess == 2">我们会尽快为您处理订单，如需协助请联系客服</div>
            <div class="remain-text" v-if="paySuccess == 1">下单遇到问题，如需协助请与平台联系</div>
            <div>
              <yl-button type="primary" v-if="paySuccess == 1" @click="payClick">继续支付</yl-button>
              <yl-button type="primary" plain @click="showDetail">查看详情</yl-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  paymentQuery,
  paymentPay
} from '@/subject/pop/api/b2b_api/purchase_order';
export default {
  components: {
  },
  computed: {
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
          title: '支付'
        }
      ],
      // 交易单号
      payNo: '',
      id: '',
      payId: '',
      // 1-支付失败 2-支付成功
      paySuccess: ''
    };
  },
  mounted() {
    console.log('this.$route.query:', this.$route.query)
    let query = this.$route.query
    this.payNo = query.orderId
    this.id = query.id
    this.paymentQueryMethods()
  },
  methods: {
    async paymentQueryMethods() {
      this.$common.showLoad()
      let data = await paymentQuery(this.payNo)
      this.$common.hideLoad()
      if (data) {
        this.paySuccess = 2
      } else {
        this.paySuccess = 1
      }
    },
    // 去支付
    async payClick() {
      this.$common.showLoad()
      let redirectUrl = window.location.origin + `/#/b2b_purchase_order/b2b_purchase_pay_status?tab_platform=3&id=${this.id}&payId=${this.payId}`
      let payData = await paymentPay(this.payNo, 'yeePayBank', redirectUrl, 'B2B-PC')
      if (payData.standardCashier) {
        this.$common.goThreePackage(payData.standardCashier)
      }
      this.$common.hideLoad()
    },
    //跳转详情界面
    showDetail() {
      // 跳转详情
      this.$router.push({
        name: 'B2bPurchaseOrderDetail',
        params: { id: this.id }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
