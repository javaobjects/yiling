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
                <div class="item"><span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}</div>
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{ data.paymentStatus | dictLabel(orderPayStatus) }}）</div>
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
                  {{ data.orderNote }}
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>付款信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">原价总金额：</span> {{ data.originalAmount | toThousand('￥') }} </div>
                <div class="item"><span class="font-title-color">成交价总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">优惠总金额：</span>{{ data.discountAmount | toThousand('￥') }}</div>
                <div class="item" v-if="data.orderCategory == 2"><span class="font-title-color">定金金额：</span>{{ data.depositAmount | toThousand('￥') }}</div>
                <div class="item" v-if="data.orderCategory == 2"><span class="font-title-color">尾款金额：</span>{{ data.balanceAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>日志信息
              </div>
              <div class="box-text" v-if="data.orderLogInfo && data.orderLogInfo.length">
                <el-scrollbar wrap-class="max-scroll">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(activity, index) in data.orderLogInfo"
                      :key="index"
                      :class="index === 0 ? 'active' : ''"
                      :timestamp="activity.info">
                    </el-timeline-item>
                  </el-timeline>
                </el-scrollbar>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>物流信息
              </div>
              <div class="box-text">
                <yl-empty></yl-empty>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div class="font-title-color font-size-lg mar-l-8 mar-b-16">
         物流信息：
         <span v-if="data.deliveryType == 2">第三方物流</span>
         <span v-else-if="data.deliveryType == 1">自有物流</span>
         <span v-else>- -</span>
       </div>
       <div class="font-title-color font-size-lg mar-l-8 mar-b-16" v-if="data.deliveryType == 2">
         快递公司：<span class="mar-r-16">{{ data.deliveryCompany }}</span>
         快递单号：<span>{{ data.deliveryNo }}</span>
       </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
              <div class="title flex-row-left">{{ item.goodsName }}<span v-if="item.promotionActivityType" class="activity-type">{{ item.promotionActivityType | dictLabel(typeArray) }}</span></div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item">
                    <span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}
                    <span v-if="type == 2 && item.promotionActivityType == 4" class="activity-unit">{{ item.promotionNumber }}{{ item.unit }}/套</span>
                  </div>
                  <div class="text-item"></div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                  <!-- <div class="text-item"><span class="font-title-color">收货数量：</span>{{ item.receiveQuantity }}</div> -->
                  <div class="text-item"></div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.couponDiscountAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== (item.orderDeliveryList.length - 1) ? 'mar-b-8' : ''">
                  <el-form
                    class="content"
                    :model="order"
                    :rules="rules"
                    :ref="'form' + index + idx"
                    inline-message>
                    <div class="content-item font-title-color">
                      批次号/序列号：
                      <span class="font-important-color">{{ order.batchNo }}</span>
                    </div>
                    <div class="content-item font-title-color">
                      有效期至：
                      <span class="font-important-color">{{ order.expiryDate | formatDate }}</span>
                    </div>
                    <div class="content-item font-title-color">
                      发货数量：
                      <span class="font-important-color">{{ order.deliveryQuantity }}</span>
                    </div>
                    <div class="content-item flex-row-left font-title-color return-contenr-item" v-if="type == 2">
                      <span class="col-up">*</span>退货数量：
                      <el-form-item
                        prop="returnQuantity"
                        inline-message>
                        <el-input
                          type="number"
                          v-model="order.returnQuantity"
                          :min="0"
                          @input="e => (order.returnQuantity = checkReceiveQuantity(e, order.remainReturnQuantity))"
                          placeholder="请输入退货数量" />
                      </el-form-item>
                    </div>
                    <div class="content-item font-title-color" v-if="type == 2">
                      <i class="el-icon-warning"></i>
                      <span class="font-important-color tip-color">可退货数量 {{ order.remainReturnQuantity }}件</span>
                    </div>
                  </el-form>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="header-bar mar-b-16" v-if="type == 2">
          <div class="sign"></div>退货备注
        </div>
        <div v-if="type == 2">
          <el-input
            type="textarea"
            placeholder="请输入退货备注"
            :maxlength="100"
            show-word-limit
            resize="none"
            v-model="remark"
            >
          </el-input>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button v-if="type == 1" type="primary" @click="receiveThePackage">确认收货</yl-button>
        <yl-button v-if="type == 2" type="primary" @click="rejectThePackage">提交退货</yl-button>
      </div>
    </div>
    <!-- 审核 -->
    <yl-dialog
      title="确认收货"
      :visible.sync="showAuditDialog"
      :destroy-on-close="false"
      @confirm="receiveConfirm"
      >
      <div class="order-audit-content">
        <div class="info-view">确认收货后不能在线申请退货，请收到商品确认无误后再确认收货！</div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { getPurchaseDetail, purchaseReceive, returnApply } from '@/subject/pop/api/b2b_api/purchase_order'
  import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus, orderReturnStatus } from '@/subject/pop/utils/busi'
  import ylEmpty from '@/common/components/Empty'
  import { formatDate } from '@/subject/pop/utils'

  export default {
    components: {
      ylEmpty
    },
    computed: {
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
      // 发票
      orderTicketStatus() {
        return orderTicketStatus()
      },
      // 退货类型
      orderReturnStatus() {
        return orderReturnStatus()
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
            title: '采购订单管理'
          },
          {
            title: '采购订单',
            path: '/b2b_purchase_order/b2b_purchase_order_list'
          },
          {
            title: '订单详情'
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
        type: '',
        rules: {
          returnQuantity: [{ required: true, message: '请输入退货数量', trigger: 'blur' }]
        },
        showAuditDialog: false,
        // 退货备注
        remark: ''
      };
    },
    mounted() {
      this.id = this.$route.params.id
      this.type = this.$route.params.type
      this.navList[3].title = this.type == 1 ? '订单详情（收货）' : '订单详情（退货）'
      if (this.id) {
        this.getDetail()
      }
    },
    methods: {
      async getDetail() {
        this.$common.showLoad()
        let data = await getPurchaseDetail(this.id)
        this.$common.hideLoad()
        if (data) {
          if (data.orderLogInfo && data.orderLogInfo.length) {
            data.orderLogInfo = data.orderLogInfo.map(item => {
              item.info = formatDate(item.logTime) + ' ' + item.logContent
              return item
            })
          }
          this.data = data
        }
      },
      // 收货
      receiveThePackage() {
        this.showAuditDialog = true
      },
      async receiveConfirm() {
        if (!this.id) {
          return
        }
        this.$common.showLoad()
        let data = await purchaseReceive(this.id)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.showAuditDialog = false
          this.$common.alert('确认收货成功', r => {
            this.$router.go(-1)
          })
        }
      },
      // 多表单验证
      checkFormValidate() {
        let check = (formName) => {
          return new Promise((resolve, reject) => {
            this.$refs[formName][0].validate(async valid => {
              if (valid) {
                resolve(true)
              } else {
                reject()
              }
            })
          })
        }
        var arr = []
        for (let i=0; i<this.data.orderDetailDelivery.length; i++) {
          let item = this.data.orderDetailDelivery[i]
          for (let k=0; k<item.orderDeliveryList.length; k++) {
            arr.push(check('form' + i + k))
          }
        }
        return Promise.all(arr)
      },
      // 提交退货
      rejectThePackage() {
        if (!this.data.id) {
          return
        }
        let err = 0
        this.checkFormValidate().then(async (result) => {
          let array = []
          if (this.data.orderDetailDelivery && this.data.orderDetailDelivery.length) {
            for (let i=0; i<this.data.orderDetailDelivery.length; i++) {
              let item = this.data.orderDetailDelivery[i]
              let arr = []
              for (let i=0; i<item.orderDeliveryList.length; i++) {
                let order = item.orderDeliveryList[i]
                if (order.returnQuantity > 0) {
                  err += 1
                  arr.push({
                    batchNo: order.batchNo,
                    returnQuantity: parseFloat(order.returnQuantity)
                  })
                }
              }
              if (arr && arr.length > 0) {
                array.push({
                  detailId: item.id,
                  goodsId: item.goodsId,
                  goodsSkuId: item.goodsSkuId,
                  orderReturnDetailBatchList: arr
                })
              }
            }
            if (err === 0) {
              this.$common.warn('退货数量不可全部为0')
              return false
            }
            this.$common.showLoad()
            let data = await returnApply(this.data.id, this.data.orderNo, array, this.remark)
            this.$common.hideLoad()
            if (data) {
              this.$common.alert('退货单提交成功，待供应商审核', r => {
                this.$router.go(-1)
              })
            }
          }
        }).catch(err => {
          this.$log('表单验证失败')
        })
      },
      checkReceiveQuantity(val, maxVal) {
        val = val.replace(/[^0-9]/gi, '')
        if (val > maxVal) {
          val = maxVal
        }
        return val
      }
    }
  };
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
<style lang="scss">
  .app-container-content {
    .header-box {
      .el-timeline {
        padding-left: 0 !important;
        .el-timeline-item {
          padding-bottom: 12px;
          .el-timeline-item__tail {
            border-left: 2px solid #D8D8D8 !important;
            z-index: 1;
            left: 2px !important;
          }
          .el-timeline-item__node--normal {
            width: 8px;
            height: 8px;
            border: 2px solid #BCBCBC;
            border-radius: 50%;
            background: #FFFFFF !important;
            z-index: 2;
          }
          .el-timeline-item__wrapper {
            padding-left: 12px;
          }
          .el-timeline-item__content {
            font-size: 12px !important;
            color: $font-title-color !important;
          }
          .el-timeline-item__timestamp {
            margin-top: 0;
          }
        }
        .active {
          .el-timeline-item__node--normal {
            border: 2px solid #1B9AEE;
          }
        }
      }
    }
  }
</style>
