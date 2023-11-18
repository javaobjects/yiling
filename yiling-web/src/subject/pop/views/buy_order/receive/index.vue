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
                <div class="item"><span class="font-title-color">货款总金额：</span>{{ data.totalAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">现折总金额：</span>{{ data.cashDiscountAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">票折总金额：</span>{{ data.ticketDiscountAmount | toThousand('￥') }}</div>
                <div class="item"><span class="font-title-color">支付总金额：</span>{{ data.paymentAmount | toThousand('￥') }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>供应商信息
              </div>
              <div class="box-text" v-if="data.enterpriseInfo && data.enterpriseInfo.name">
                <div class="item"><span class="font-title-color">供应商名称：</span>{{ data.enterpriseInfo.name }}</div>
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.enterpriseInfo.contactor }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.enterpriseInfo.contactorPhone }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.enterpriseInfo.address }}</div>
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
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div class="font-title-color font-size-lg mar-l-8 mar-b-16" v-if="data.orderStatus === 40">
          <span class="col-up">*</span>退货类型：
          <el-radio-group v-model="returnType">
            <el-radio
              v-for="(item, index) in orderReturnStatus"
              v-show="item.value !== 1"
              :key="index"
              :label="item.value">
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
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
                  <!-- <div class="text-item"><span class="font-title-color">收货数量：</span>{{ item.receiveQuantity }}</div> -->
                  <div class="text-item"></div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                  <div class="text-item"><span class="font-title-color">折扣金额：</span>{{ item.discountAmount | toThousand('￥-') }}</div>
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
                    <div class="content-item font-title-color" v-if="data.orderStatus === 30">
                      发货数量：
                      <span class="font-important-color">{{ order.deliveryQuantity }}</span>
                    </div>
                    <div class="content-item flex-row-left font-title-color" v-if="data.orderStatus === 30">
                      <span class="col-up">*</span>收货数量：
                      <el-form-item
                        prop="receiveQuantity"
                        inline-message>
                        <el-input
                          type="number"
                          v-model="order.receiveQuantity"
                          :min="0"
                          @input="e => (order.receiveQuantity = checkReceiveQuantity(e, order.deliveryQuantity))"
                          placeholder="请输入收货数量" />
                      </el-form-item>
                    </div>
                    <div class="content-item flex-row-left font-title-color return-contenr-item" v-else-if="data.orderStatus === 40">
                      <span class="col-up">*</span>退货数量：
                      <el-form-item
                        prop="returnQuantity"
                        inline-message>
                        <el-input
                          type="number"
                          v-model="order.returnQuantity"
                          :min="0"
                          @input="e => (order.returnQuantity = checkReceiveQuantity(e, order.receiveQuantity))"
                          placeholder="请输入退货数量" />
                      </el-form-item>
                    </div>
                    <div class="content-item font-title-color" v-if="data.orderStatus === 40">
                      <i class="el-icon-warning"></i>
                      <span class="font-important-color tip-color">可退货数量 {{ order.receiveQuantity }}件</span>
                    </div>
                  </el-form>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="data.orderStatus === 30 && (currentEnterpriseInfo.channelId == 3 || data.ylflag)">
          <div class="font-size-lg font-important-color mar-b-16">
            上传回执单<span class="font-light-color pad-l-8 font-size-sm">格式要求JPG、PNG、JPEG、BMP格式， 文件不超过4M</span>
          </div>
          <div>
            <yl-multiple-upload
              :limit="5"
              :extral-data="{type: 'orderReceiveOneReceipt'}"
              @onSuccess="onSuccess"
            />
          </div>
        </div>
        <div class="header-bar mar-b-16" v-if="data.orderStatus === 40">
          <div class="sign"></div>退货备注
        </div>
        <div v-if="data.orderStatus === 40">
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
        <yl-button v-if="data.orderStatus === 30" type="primary" @click="receiveThePackage">确认收货</yl-button>
        <yl-button :disabled="disabled" v-if="data.orderStatus === 40" type="primary" @click="rejectThePackage">提交退货</yl-button>
      </div>
    </div>
    <!-- 审核 -->
    <yl-dialog
      title="提示"
      :visible.sync="showAuditDialog"
      :destroy-on-close="false"
      :show-footer="false"
      >
      <div class="order-audit-content">
        <div class="info-view">{{ data.refuseReturnReason }}</div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { getOrderDetail, orderReceive, orderReject } from '@/subject/pop/api/order'
  import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus, orderReturnStatus } from '@/subject/pop/utils/busi'
  import { formatDate } from '@/subject/pop/utils'
  import { ylMultipleUpload } from '@/subject/pop/components'
  import { mapGetters } from 'vuex'

  export default {
    components: {
      ylMultipleUpload
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
      },
      ...mapGetters([
        'currentEnterpriseInfo'
      ])
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
            title: '采购订单',
            path: '/buyOrder/buyOrder_index'
          },
          {
            title: '订单详情'
          }
        ],
        data: {},
        // 回执单
        receiveReceipt: [],
        // 退货类型
        returnType: null,
        rules: {
          receiveQuantity: [{ required: true, message: '请输入收货数量', trigger: 'blur' }],
          returnQuantity: [{ required: true, message: '请输入退货数量', trigger: 'blur' }]
        },
        // 显示不能提交退货原因
        showAuditDialog: false,
        disabled: false,
        // 退货备注
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
        let data = await getOrderDetail(this.id)
        this.$common.hideLoad()
        if (data) {
          if (data.orderLogInfo && data.orderLogInfo.length) {
            data.orderLogInfo = data.orderLogInfo.map(item => {
              item.info = formatDate(item.logTime) + ' ' + item.logContent
              return item
            })
          }
          if (data.orderDetailDelivery && data.orderDetailDelivery.length) {
            data.orderDetailDelivery.map(item => {
              if (item.orderDeliveryList && item.orderDeliveryList.length) {
                item.orderDeliveryList.map(order => {
                  if (data.orderStatus === 40) {
                    // 退货
                    order.returnQuantity = 0
                  } else if (data.orderStatus === 30) {
                    // 收货
                    order.receiveQuantity = order.deliveryQuantity
                  }
                  return order
                })
              }
            })
          }
          if (data.orderStatus === 30 || data.orderStatus === 40) {
            this.navList[3].title = data.orderStatus === 30 ? '订单详情（收货）' : '订单详情（退货）'
          }
          if (data.isAllowReturn == 1 ) {
            this.showAuditDialog = true
            this.disabled = true
          }
          this.data = data
        }
      },
      // 收货
      receiveThePackage() {
        if (!this.data.id) {
          return
        }
        // if (this.data.orderStatus === 30 && (this.currentEnterpriseInfo.channelId == 3 || this.data.ylflag) && !this.receiveReceipt.length) {
        //   this.$common.warn('请先上传回执单')
        //   return false
        // }
        this.checkFormValidate().then(async (result) => {
          let array = []
          if (this.data.orderDetailDelivery && this.data.orderDetailDelivery.length) {
            for (let i=0; i<this.data.orderDetailDelivery.length; i++) {
              let item = this.data.orderDetailDelivery[i]
              let arr = []
              for (let i=0; i<item.orderDeliveryList.length; i++) {
                let order = item.orderDeliveryList[i]
                arr.push({
                  id: order.id,
                  receiveQuantity: order.receiveQuantity
                })
              }
              array.push({
                detailId: item.id,
                goodsId: item.goodsId,
                receiveInfoList: arr
              })
            }
            this.$common.showLoad()
            let keys = this.receiveReceipt.map(item => {
              return item.key
            })
            let data = await orderReceive(this.data.id, array, keys)
            this.$common.hideLoad()
            if (data && data.result) {
              this.$common.alert('确认收货成功', r => {
                this.$router.go(-1)
              })
            }
          }
        }).catch(err => {
          this.$log('表单验证失败')
        })
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
        if (!this.returnType) {
          this.$common.warn('请先选择退货类型')
          return false
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
              array.push({
                detailId: item.id,
                goodsId: item.goodsId,
                orderDeliveryList: arr
              })
            }
            if (err === 0) {
              this.$common.warn('退货数量不可全部为0')
              return false
            }
            this.$common.showLoad()
            let data = await orderReject(this.data.id, array, this.returnType, this.remark)
            this.$common.hideLoad()
            if (data && data.result) {
              this.$common.alert('退货成功', r => {
                this.$router.go(-1)
              })
            }
          }
        }).catch(err => {
          this.$log('表单验证失败')
        })
      },
      // 图片上传成功
      onSuccess(data) {
        if (data.length) {
          this.receiveReceipt = data
        }
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
