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
            <span class="user-name mar-l-16">{{ userInfo.currentEnterpriseInfo.name?userInfo.currentEnterpriseInfo.name:'' }}</span>
            <div class="user-tips mar-l-16">已认证</div>
          </div>
          <div class="short-func">
            <div class="search-bar">
              <div class="header-bar mar-b-10">
                <div class="sign"></div>快捷功能
              </div>
            </div>
            <!-- 以岭快捷功能 -->
            <!-- 1,工业以岭本部
                2，工业直属企业  3一级商 4 二级商  2，3，4都属于一级商功能
                5，ka 用户
             -->
            <div v-if="userInfo.currentEnterpriseInfo.channelId === 1" class="func-box flex-between">
              <div class="func-item" @click="clickItem(1)">
                <img src="../../assets/home/func-item1.png" alt="">
                <div>渠道商列表</div>
              </div>
              <div class="func-item" @click="clickItem(2)">
                <img src="../../assets/home/func-item2.png" alt="">
                <div>销售订单</div>
              </div>
              <div class="func-item" @click="clickItem(3)">
                <img src="../../assets/home/func-item3.png" alt="">
                <div>订单审核列表</div>
              </div>
              <div class="func-item" @click="clickItem(4)">
                <img src="../../assets/home/func-item4.png" alt="">
                <div>账期额度管理</div>
              </div>
            </div>
            <!-- 一二级商功能 -->
            <div v-if="userInfo.currentEnterpriseInfo.channelId === 2 || userInfo.currentEnterpriseInfo.channelId === 3 || userInfo.currentEnterpriseInfo.channelId === 4" class="func-box flex-between">
              <div class="func-item" @click="clickItem(5)">
                <img src="../../assets/home/func-item1.png" alt="">
                <div>商品列表</div>
              </div>
              <div class="func-item" @click="clickItem(2)">
                <img src="../../assets/home/func-item2.png" alt="">
                <div>销售订单</div>
              </div>
              <div class="func-item" @click="clickItem(6)">
                <img src="../../assets/home/func-item3.png" alt="">
                <div>采购订单</div>
              </div>
              <div class="func-item" @click="clickItem(7)">
                <img src="../../assets/home/func-item4.png" alt="">
                <div>客户列表</div>
              </div>
            </div>
            <!-- ka 客户功能 -->
            <div v-if="userInfo.currentEnterpriseInfo.channelId === 5" class="func-box flex-between">
              <div class="func-item" @click="clickItem(6)">
                <img src="../../assets/home/func-item3.png" alt="">
                <div>采购订单</div>
              </div>
              <div class="func-item" @click="clickItem(8)">
                <img src="../../assets/home/func-item4.png" alt="">
                <div>退货单</div>
              </div>
              <div class="func-item"></div>
              <div class="func-item"></div>
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
      <!-- 顶部，头像，日历等 end -->
      <!-- 1,工业以岭本部  2，工业直属企业  3一级商 4 二级商  2，3，4都属于一级商功能  5，ka 用户 -->
      <!-- 一二级商 ka  显示采购待处理 -->

      <div v-if="userInfo.currentEnterpriseInfo.channelId !== 1" class="home-list mar-t-16">
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>采购待处理事务
          </div>
        </div>
        <div class="home-list-box">
          <div class="list-item" @click="$router.push({path:'/buyOrder/buyOrder_index',query:{orderPayStatus:1}})">
            <img src="../../assets/home/func-item6.png" alt="">
            <div>
              <p>{{ buyerUnPaymentNum }}</p>
              <p>待付款订单</p>
            </div>
          </div>
          <div class="list-item" @click="$router.push({path:'/buyOrder/buyOrder_index',query:{orderStatus:20}})">
            <img src="../../assets/home/func-item7.png" alt="">
            <div>
              <p>{{ buyerUnDeliveryNum }}</p>
              <p>待发货订单</p>
            </div>
          </div>
          <div class="list-item" @click="$router.push({path:'/buyOrder/buyOrder_index',query:{orderStatus:30}})">
            <img src="../../assets/home/func-item8.png" alt="">
            <div>
              <p>{{ buyerUnReceiveNum }}</p>
              <p>待签收订单</p>
            </div>
          </div>
        </div>
      </div>
      <!-- 以岭 和 一二级商 显示销售待处理事物 -->
      <div v-if="userInfo.currentEnterpriseInfo.channelId !== 5" class="home-list mar-t-16">
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>销售待处理事务
          </div>
        </div>
        <div class="home-list-box">

          <div class="list-item" @click="$router.push({path:'/sell_order/sell_order_list',query:{orderStatus:20}})">
            <img src="../../assets/home/func-item10.png" alt="">
            <div>
              <p>{{ sellerUnDeliveryNum }}</p>
              <p>待发货订单</p>
            </div>
          </div>
          <div class="list-item" @click="$router.push({path:'/sell_order/sell_order_list',query:{orderStatus:30}})">
            <img src="../../assets/home/func-item9.png" alt="">
            <div>
              <p>{{ sellerDeliveryNum }}</p>
              <p>已发货订单</p>
            </div>
          </div>

          <div class="list-item" @click="$router.push({name:'GoodsReturnList',query:{reviewStatus:1}})">
            <img src="../../assets/home/func-item11.png" alt="">
            <div>
              <p>{{ sellerReturningNum }}</p>
              <p>退货中订单</p>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getOrderNumber } from '@/subject/pop/api/home';

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters(['userInfo'])
  },
  data() {
    return {
      date: new Date(),
      //采购待发货订单数
      buyerUnDeliveryNum: 0,
      //采购待支付订单数
      buyerUnPaymentNum: 0,
      //采购待签收订单数
      buyerUnReceiveNum: 0,
      //销售已发货订单
      sellerDeliveryNum: 0,
      //销售退货中订单
      sellerReturningNum: 0,
      //销售待发货订单数
      sellerUnDeliveryNum: 0,
      // 是否有权限
      auth: true
    }
  },
  created() {
    this.getOrder()

  },
  methods: {
    change(type) {
      this.$refs.calendar.selectDate(type)
    },
    async getOrder() {
      let data = await getOrderNumber()
      if (data) {
        this.buyerUnDeliveryNum = data.buyerUnDeliveryNum ? data.buyerUnDeliveryNum : 0
        this.buyerUnPaymentNum = data.buyerUnPaymentNum ? data.buyerUnPaymentNum : 0
        this.buyerUnReceiveNum = data.buyerUnReceiveNum ? data.buyerUnReceiveNum : 0
        this.sellerDeliveryNum = data.sellerDeliveryNum ? data.sellerDeliveryNum : 0
        this.sellerReturningNum = data.sellerReturningNum ? data.sellerReturningNum : 0
        this.sellerUnDeliveryNum = data.sellerUnDeliveryNum ? data.sellerUnDeliveryNum : 0
      } else {
        this.auth = false
      }
    },
    clickItem(type) {
      if (this.auth) {
        switch (type) {
          case 1:
            this.$router.push('/clientele/channel')
            break;
          case 2:
            this.$router.push('/sell_order/sell_order_list')
            break;
          case 3:
            this.$router.push('/reviewOrder/reviewOrder_index')
            break;
          case 4:
            this.$router.push({
              name: 'PeriodIndex'
            })
            break;
          case 5:
            this.$router.push('/products/products_index')
            break;
          case 6:
            this.$router.push('/buyOrder/buyOrder_index')
            break;
          case 7:
            this.$router.push('/clientele/list')
            break;
          case 8:
            this.$router.push({
              name: 'GoodsReturnList'
            })
            break;
          default:
            break;
        }
      } else {
        return this.$common.error('暂无相关权限');
      }
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
