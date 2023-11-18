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
                <div class="item"><span class="font-title-color">退款总金额：</span>{{ data.returnAllAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.totalDiscountAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">实退总金额：</span>{{ data.returnAmount | toThousand('￥') }}</div>
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
          <el-col :span="8" v-if="data.returnStatus == 3">
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
          <el-col :span="8" v-if="data.remark">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>退货单备注
              </div>
              <div class="box-text">
                <div class="mark-item">
                  {{ data.remark }}
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
        <div>
          <div class="expand-row" v-if="data.orderDetailVOList && data.orderDetailVOList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailVOList" :key="index">
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
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">实退金额：</span>{{ item.returnAmount | toThousand('￥') }}</div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== (item.orderDeliveryList.length - 1) ? 'mar-b-8' : ''">
                  <div class="content">
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
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-show="data.returnStatus == 1" @click="auditClick">审核</yl-button>
      </div>
      <!-- 审核 -->
      <yl-dialog
        title="退货单审核"
        right-btn-name="审核通过"
        :visible.sync="showAuditDialog"
        :destroy-on-close="false"
        :show-cancle="false"
        width="540px"
        @confirm="confirm"
        >
        <div class="order-audit-content">
          <div class="info-view">退货审核通过后，已付款部分将转为退货核销单，未付款部分将释放掉占用额度；退货驳回终端可以再次提交退货</div>
          <el-input
            type="textarea"
            :autosize="{ minRows: 5 }"
            placeholder="备注"
            :maxlength="20"
            show-word-limit
            v-model="remark">
          </el-input>
        </div>
        <yl-button slot="left-btn" @click="auditDialogLeftClick">退货驳回</yl-button>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
  import { getSellerOrderReturnDetail, returnOrderReject } from '@/subject/pop/api/b2b_api/sell_order'
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
      // 退货状态
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
            path: '/b2b_dashboard'
          },
          {
            title: '销售订单管理'
          },
          {
            title: '退货单',
            path: '/b2b_sell_order/b2b_goods_return_list',
            name: 'B2bGoodsReturnList'
          },
          {
            title: '退货单详情'
          }
        ],
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
        ],
        data: {},
        showAuditDialog: false,
        remark: ''
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
        let data = await getSellerOrderReturnDetail(this.id)
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
      // 审核点击
      auditClick() {
        this.showAuditDialog = true
      },
      // 退货驳回
      auditDialogLeftClick() {
        if (!this.remark.trim()){
          this.$common.warn('请输入退货驳回备注')
        } else {
          this.checkOrderMethod(2)
        }
      },
      // 退货审核 通过
      async confirm() {
        this.checkOrderMethod(1)
      },
      // 审核接口
      async checkOrderMethod(type) { // type 1- 审核通过 2- 审核驳回
        this.$common.showLoad()
        let data = null
        if (type == 1) {
          data = await returnOrderReject(this.data.id, true, this.remark)
        } else {
          data = await returnOrderReject(this.data.id, false, this.remark)
        }
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          let str = '审核成功'
          if (type == 2) {
            str = '驳回成功'
          }
          this.$common.alert(str, r => {
            this.$router.go(-1)
          })
        }
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
      padding: 4px 2px;
    }
  }
</style>
