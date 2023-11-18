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
                <div class="item"><span class="font-title-color">订单ID：</span>{{ data.id }}</div>
                <div class="item"><span class="font-title-color">预订单编号：</span>{{ data.orderNo }}</div>
                <div class="item"><span class="font-title-color">下单时间：</span>{{ data.createTime | formatDate }}</div>
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>供应商信息
              </div>
              <div class="box-text" v-if="data.enterpriseDistributorInfo && data.enterpriseDistributorInfo.name">
                <div class="item"><span class="font-title-color">供应商名称：</span>{{ data.enterpriseDistributorInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseDistributorInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.enterpriseDistributorInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseDistributorInfo.address }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>收货信息
              </div>
<!--              <div class="box-text" v-if="data.enterpriseBuyerInfo && data.enterpriseBuyerInfo.name">-->
<!--                <div class="item"><span class="font-title-color">采购商名称：</span>{{ data.enterpriseBuyerInfo.name }}</div>-->
<!--                <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseBuyerInfo.contactor }}</div>-->
<!--                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.enterpriseBuyerInfo.contactorPhone }}</div>-->
<!--                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseBuyerInfo.address }}</div>-->
<!--              </div>-->
              <div class="box-text" v-if="data.orderAddress && data.orderAddress.name">
                <div class="item"><span class="font-important-color"></span>{{ data.orderAddress.buyerEname }}<span class="font-title-color" v-show="data.orderAddress.provinceName">（{{ data.orderAddress.provinceName }}）</span></div>
                <div class="item"><span class="font-title-color">收货人：</span>{{ data.orderAddress.name }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.orderAddress.mobile }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.orderAddress.address }}</div>
                <div class="item"><span class="font-title-color">备注：</span>{{ data.orderNote }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>审核信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">审核状态：</span>{{ data.auditStatus | dictLabel(orderPreStatus) }}</div>
                <div class="item"><span class="font-title-color">审核人：</span>{{ data.auditUserName }}</div>
                <div class="item"><span class="font-title-color">审核时间：</span>{{ data.auditTime | formatDate }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>付款信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">货款总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">折扣总金额：</span>{{ data.discountAmount | toThousand('￥-') }}</div>
                <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>商务信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">商务联系人：</span>{{ data.contacterName || '- -' }}</div>
                <div class="item"><span class="font-title-color">联系电话：</span>{{ data.contacterTelephone || '- -' }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" v-if="data.orderContractUrl && data.orderContractUrl.length">
          <el-col :span="24">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>购销合同
              </div>
              <div class="box-text">
                <div
                  class="img-box"
                  v-for="(img, index) in data.orderContractUrl"
                  :key="index">
                  <el-image
                    class="img-item"
                    fit="cover"
                    :preview-src-list="data.orderContractUrl"
                    :src="img" />
                  <div class="screen flex-row-center">
                    <svg-icon class="icon" icon-class="img-open"></svg-icon>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
<!--        <div class="font-title-color font-size-lg mar-l-8 mar-b-16">-->
<!--          物流信息：自有物流-->
<!--        </div>-->
        <div>
          <div class="expand-row" v-if="data.orderDetailList && data.orderDetailList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailList" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">折扣金额：</span>{{ item.discountAmount | toThousand('￥-') }}</div>
                  <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center" v-if="data.auditStatus === 1 || data.auditStatus === 2">
        <yl-button type="primary" plain @click="orderCancle(data.id)">取消订单</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getPreOrderDetail, preOrderCancle } from '@/subject/pop/api/order'
import { paymentMethod, orderTicketStatus, orderPreStatus } from '@/subject/pop/utils/busi'

export default {
  components: {
  },
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 发票
    orderTicketStatus() {
      return orderTicketStatus()
    },
    // 订单审核状态
    orderPreStatus() {
      return orderPreStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '采购订单管理'
        },
        {
          title: '预采购订单',
          path: '/buyOrder/buyOrder_pre_index'
        },
        {
          title: '预订单详情'
        }
      ],
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
      let data = await getPreOrderDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        this.data = data
      }
    },
    // 取消订单
    orderCancle(id) {
      this.$common.confirm('取消后不可恢复，如果需要可以再次下单！是否确认取消该预订单？', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await preOrderCancle(id)
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('取消订单成功')
            this.$router.go(-1)
          }
        }
      })
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
