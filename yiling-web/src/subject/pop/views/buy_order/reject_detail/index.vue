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
                <div class="sign"></div>供应商信息
              </div>
              <div class="box-text" v-if="data.sellerEnterpriseInfo && data.sellerEnterpriseInfo.name">
                <div class="item"><span class="font-title-color">供应商名称：</span>{{ data.sellerEnterpriseInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.sellerEnterpriseInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.sellerEnterpriseInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.sellerEnterpriseInfo.address }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>采购商备注
              </div>
              <div class="box-text">
                <div class="mark-item">
                  {{ data.orderRemark }}
                </div>
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
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{ data.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                <div class="item"><span class="font-title-color">退款总金额：</span>{{ data.returnAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>退货信息
              </div>
              <div class="box-text" v-if="data.logList && data.logList.length">
                <yl-time-line wrap-class="max-scroll" :data="data.logList"></yl-time-line>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>退货备注
              </div>
              <div class="box-text">
                <div class="mark-item">
                  {{ data.remark }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex" v-if="data.returnStatus == 3">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>驳回原因
              </div>
              <div class="box-text">
                <div class="mark-item">
                  {{ data.refuseReason }}
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
          <div class="expand-row" v-if="data.orderDetailVOList && data.orderDetailVOList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailVOList" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                  <div class="text-item" v-if="data.returnType == 1"><span class="font-title-color">退货数量：</span>{{ item.returnQuantity }}</div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">折扣金额：</span>{{ item.discountAmount | toThousand('￥-') }}</div>
                  <div class="text-item"><span class="font-title-color">退款金额：</span>{{ item.returnAmount | toThousand('￥') }}</div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  class="content"
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== (item.orderDeliveryList.length - 1) ? 'mar-b-8' : ''">
                  <div class="content-item font-title-color">
                    批次号/序列号：
                    <span class="font-important-color">{{ order.batchNo }}</span>
                  </div>
                  <div class="content-item font-title-color">
                    有效期至：
                    <span class="font-important-color">{{ order.expiryDate | formatDate }}</span>
                  </div>
                  <div class="content-item font-title-color">
                    退货数量：
                    <span class="font-important-color">{{ order.returnQuantity }}</span>
                  </div>
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
import { uploadReceipt, getRejectOrderDetail } from '@/subject/pop/api/order'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnStatus } from '@/subject/pop/utils/busi'
import { ylTimeLine } from '@/subject/pop/components'
import { formatDate } from '@/subject/pop/utils'

export default {
  components: {
    ylTimeLine
  },
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 支付状态
    orderPayStatus() {
      return orderPayStatus()
    },
    // 退货类型
    orderReturnStatus() {
      return orderReturnStatus()
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
          path: '/dashboard'
        },
        {
          title: '采购订单管理'
        },
        {
          title: '退货单',
          path: '/buyOrder/buyOrder_index'
        },
        {
          title: '退货单详情'
        }
      ],
      data: {},
      // 查看发票
      showTicket: false
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
    },
    // 收货 退货
    receiveThePackage() {
      if (this.data.id) {
        this.$router.push(`/buyOrder/buyOrder_receive/${this.data.id}`)
      }
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key && this.data.id) {
        this.$common.showLoad()
        let result = await uploadReceipt(this.data.id, data.key)
        this.$common.hideLoad()
        if (result && result.result) {
          this.$common.n_success('上传回执单成功')
          this.getDetail()
        }
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
      padding: 4px 0 0 2px;
    }
  }
</style>
