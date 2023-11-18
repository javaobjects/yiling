<template>
  <div class="app-container">
    <div class="app-container-content">
      <!-- 顶部，头像，日历等 -->
      <div class="home-top flex-between">
        <div class="home-top-left">
          <div class="user-box">
            <div class="user-head">
              <img class="user-img mar-l-16" src="@/common/assets/avatar.png" />
              <img class="user-tip" src="../../assets/home/user-tips.png" alt="">
            </div>
            <span class="user-name mar-l-16">{{ currentEnterpriseInfo.name ? currentEnterpriseInfo.name : '' }}</span>
            <div class="user-tips mar-l-16">已认证</div>
          </div>
          <div class="short-func">
            <div class="search-bar">
              <div class="header-bar mar-b-10">
                <div class="sign"></div>快捷功能
              </div>
            </div>
            <!-- 快捷功能 -->
            <div class="func-box flex-between">
              <div v-route-role="['b2b:b2b_store:store_manage']" class="func-item" @click="clickItem(1)">
                <img src="../../assets/b2b-home/item-1.png" alt="">
                <div>店铺设置</div>
              </div>
              <div v-route-role="['b2b:product:index']" class="func-item" @click="clickItem(2)">
                <img src="../../assets/b2b-home/item-2.png" alt="">
                <div>商品列表</div>
              </div>
              <div v-route-role="['b2b:purchase_order:purchase_order_list']" class="func-item" @click="clickItem(7)">
                <img src="../../assets/home/func-item3.png" alt="">
                <div>采购订单</div>
              </div>
              <div v-route-role="['b2b:purchase_order:purchase_return_list']" class="func-item" @click="clickItem(8)">
                <img src="../../assets/home/func-item4.png" alt="">
                <div>退货订单</div>
              </div>
              <div v-route-role="['b2b:sell_order:sell_order_list']" class="func-item" @click="clickItem(3)">
                <img src="../../assets/b2b-home/item-3.png" alt="">
                <div>销售订单</div>
              </div>
              <div v-route-role="['b2b:period:list']" class="func-item" @click="clickItem(4)">
                <img src="../../assets/b2b-home/item-4.png" alt="">
                <div>账期列表</div>
              </div>
              <div v-route-role="['b2b:b2b_clientele:list']" class="func-item" @click="clickItem(5)">
                <img src="../../assets/b2b-home/item-5.png" alt="">
                <div>客户列表</div>
              </div>
              <div v-route-role="['b2b:discount_coupon:discount_coupon_list']" class="func-item" @click="clickItem(6)">
                <img src="../../assets/b2b-home/item-6.png" alt="">
                <div>优惠券列表</div>
              </div>
            </div>
          </div>
        </div>
        <div class="home-top-right">
          <div class="search-bar">
            <div class="header-bar">
              <div class="sign"></div>日历
            </div>
          </div>
          <div class="calendar-top">
            <div class="calendar-box">
              <div class="calendar-date">{{ date | formatDate('yyyy-MM') }}</div>
              <el-button-group class="calendar-box">
                <el-button @click="change('prev-month')" class="calendar-btn calendar-btn-l" type="primary" size="mini" icon="el-icon-arrow-left"></el-button>
                <el-button @click="change('next-month')" class="calendar-btn" type="primary" size="mini" icon="el-icon-arrow-right"></el-button>
              </el-button-group>
            </div>
          </div>
          <!-- 日历 -->
          <el-calendar class="el-calendar-box" ref="calendar" v-model="date"></el-calendar>
        </div>
      </div>
      <div class="home-list mar-t-16">
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>待处理事务
          </div>
        </div>
        <div class="home-list-box">
          <div v-route-role="['b2b:sell_order:sell_order_list']" class="list-item" @click="goPage(1)">
            <img src="../../assets/home/func-item10.png" alt="">
            <div>
              <p>{{ numInfo.unDeliveryQuantity }}</p>
              <p>待发货订单</p>
            </div>
          </div>
          <div v-route-role="['b2b:purchase_order:purchase_order_list']" class="list-item" @click="goPage(4)">
            <img src="../../assets/home/func-item6.png" alt="">
            <div>
              <p>{{ numInfo.unPurchasePaymentQuantity }}</p>
              <p>待支付订单</p>
            </div>
          </div>
          <div v-route-role="['b2b:sell_order:sell_order_list']" class="list-item" @click="goPage(2)">
            <img src="../../assets/home/func-item9.png" alt="">
            <div>
              <p>{{ numInfo.unReceiveQuantity }}</p>
              <p>已发货订单</p>
            </div>
          </div>
          <div v-route-role="['b2b:purchase_order:purchase_order_list']" class="list-item" @click="goPage(5)">
            <img src="../../assets/home/func-item7.png" alt="">
            <div>
              <p>{{ numInfo.unPurchaseDeliveryQuantity }}</p>
              <p>待发货订单</p>
            </div>
          </div>
          <div v-route-role="['b2b:sell_order:goods_return_list']" class="list-item" @click="goPage(3)">
            <img src="../../assets/home/func-item11.png" alt="">
            <div>
              <p>{{ numInfo.returnQuantity }}</p>
              <p>退货中订单</p>
            </div>
          </div>
          <div v-route-role="['b2b:purchase_order:purchase_order_list']" class="list-item" @click="goPage(6)">
            <img src="../../assets/home/func-item8.png" alt="">
            <div>
              <p>{{ numInfo.unPurchaseReceiveQuantity }}</p>
              <p>待签收订单</p>
            </div>
          </div>
        </div>
      </div>
      <div class="home-list mar-t-16" v-if="numInfo.memberPushShow == 1">
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>会员推广
          </div>
        </div>
        <div class="home-list-box">
          <div class="list-item" @click="showVipPage">
            <img src="../../assets/home/func-item10.png" alt="">
            <div>
              <p>预览</p>
              <p>推广页面</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <html2canvas :show.sync="showActivity">
      <div class="create-view">
        <div class="logo-box">
          <img class="logo" src="@/subject/pop/assets/activity/logo.png" />
        </div>
        <div class="title-view">
          <div class="title-1">大运河VIP</div>
          <div class="title-2"><span>1200元/年，全年至少可省2500元</span></div>
        </div>
        <div class="box-2">
          <div class="header-img flex-row-center">
            <img class="pic mar-r-7" src="@/subject/pop/assets/activity/left-line.png" />
            <div class="center">
              会员尊享六大专属权益
            </div>
            <img class="pic mar-l-7" src="@/subject/pop/assets/activity/right-line.png" />
          </div>
          <div class="box-3">
            <el-row :gutter="5">
              <el-col :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-1.png" />
                  <div class="title">专享福利券</div>
                  <div class="desc">以岭全系产品88折</div>
                  <div class="desc"></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-2.png" />
                  <div class="title">专享活动</div>
                  <div class="desc">会员专属品种</div>
                  <div class="desc">会员专属价格</div>
                </div>
              </el-col>
              <el-col class="pad-b-10" :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-3.png" />
                  <div class="title">专属客服</div>
                  <div class="desc">7X24小时</div>
                  <div class="desc">专属客服通道</div>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="5">
              <el-col :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-4.png" />
                  <div class="title">药店联盟</div>
                  <div class="desc">工业直供政策</div>
                  <div class="desc">发展新机遇</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-5.png" />
                  <div class="title">增量营销</div>
                  <div class="desc">C端运营服务</div>
                  <div class="desc">助力销售</div>
                </div>
              </el-col>
              <el-col class="pad-b-10" :span="8">
                <div class="pic-box flex-column-center">
                  <img class="image" src="@/subject/pop/assets/activity/item-6.png" />
                  <div class="title">金融特权</div>
                  <div class="desc">部分品种</div>
                  <div class="desc">可享免息优惠</div>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="company">邀请方：{{ currentEnterpriseInfo.name ? currentEnterpriseInfo.name : '' }}</div>
          <div class="content">
            <el-row>
              <el-col :span="10">
                <div class="qr-box flex-column-center">
                  <div class="image-box flex-row-center">
                    <div class="white-box flex-row-center">
                      <div ref="qrCode"></div>
                    </div>
                  </div>
                  <div class="desc">长按识别二维码</div>
                </div>
              </el-col>
              <el-col :span="2">
                <div class="line-v flex-row-right">
                  <img src="@/subject/pop/assets/activity/activity-line.png" />
                </div>
              </el-col>
              <el-col :span="12">
                <div class="text-box">
                  <div class="desc">在微信中识别图中二维码</div>
                  <div class="desc">打开/下载大运河APP</div>
                  <div class="desc">登录/注册企业账户</div>
                  <div class="desc">进入会员开通页并支付</div>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="bottom">
            最终解释权归以岭大运河平台所有
          </div>
        </div>
      </div>
    </html2canvas>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getOrderNumber } from '@/subject/pop/api/b2b_api/zt_home'
import html2canvas from '@/common/components/Html2canvas'
import routeRole from '@/subject/pop/directive/routeRole'
import loadQrCode from '@/common/utils/qrcode'

export default {
  name: 'B2bDashboard',
  components: {
    html2canvas
  },
  directives: {
    routeRole
  },
  computed: {
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
  },
  data() {
    return {
      date: new Date(),
      numInfo: {},
      showActivity: false
    }
  },
  created() {
    this.getOrder()
    loadQrCode()
  },
  methods: {
    change(type) {
      this.$refs.calendar.selectDate(type)
    },
    async getOrder() {
      let data = await getOrderNumber()
      if (data) {
        this.numInfo = data
      }
    },
    showVipPage() {
      if (this.currentEnterpriseInfo && this.currentEnterpriseInfo.id) {
        this.showActivity = true
        setTimeout(() => {
          new window.QRCode(this.$refs.qrCode, {
            width: 71,
            height: 71,
            text: `${process.env.VUE_APP_H5_URL}/#/b2b/downApp/${this.currentEnterpriseInfo.id}`,
            correctLevel: 3
          })
        }, 100)
      } else {
        this.$common.alertError('当前企业ID为空，无法生成二维码')
      }
    },
    clickItem(type) {
      switch (type) {
        case 1:
          this.$router.push({
            name: 'StoreManage'
          })
          break;
        case 2:
          this.$router.push({
            name: 'B2BProductsList'
          })
          break;
        case 3:
          this.$router.push({
            name: 'B2bSellOrderList'
          })
          break;
        case 4:
          this.$router.push({
            name: 'B2bPeriodIndex'
          })
          break;
        case 5:
          this.$router.push({
            name: 'B2BList'
          })
          break;
        case 6:
          this.$router.push({
            name: 'DiscountCouponList'
          })
          break;
        case 7:
          this.$router.push({
            name: 'B2bPurchaseOrderList'
          })
          break;
        case 8:
          this.$router.push({
            name: 'B2bPurchaseReturnList'
          })
          break;
        default:
          break;
      }
    },
    goPage(type) {
      switch (type) {
        case 1:
          this.$router.push({ path: '/b2b_sell_order/b2b_sell_order_list', query: { orderStatus: 20 }})
          break;
        case 2:
          this.$router.push({ path: '/b2b_sell_order/b2b_sell_order_list', query: { orderStatus: 30 }})
          break;
        case 3:
          this.$router.push({ path: '/b2b_sell_order/b2b_goods_return_list', query: { reviewStatus: 1 }})
          break;
        case 4:
          this.$router.push({ path: '/b2b_purchase_order/b2b_purchase_order_list', query: { paymentMethod: 4, paymentStatus: 1, orderStatus: 10 }})
          break;
        case 5:
          this.$router.push({ path: '/b2b_purchase_order/b2b_purchase_order_list', query: { orderStatus: 20 }})
          break;
        case 6:
          this.$router.push({ path: '/b2b_purchase_order/b2b_purchase_order_list', query: { orderStatus: 30 }})
          break;
        default:
          break;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
