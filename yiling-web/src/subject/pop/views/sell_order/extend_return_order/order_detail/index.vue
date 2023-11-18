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
                <div class="sign"></div>
                订单信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">订单ID：</span>{{ data.orderId }}</div>
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
                <div class="sign"></div>
                收货地址信息
              </div>
              <div class="box-text">
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.receiveUserName }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.receiveUserMobile }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.receiveUserAdress }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                采购商备注
              </div>
              <div class="box-text">
                <div class="mark-item">{{ data.orderRemark }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                退款信息
              </div>
              <div class="box-text">
                <div class="item">
                  <span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{ data.paymentStatus | dictLabel(orderPayStatus) }}）
                </div>
                <div class="item"><span class="font-title-color">实退货款金额：</span>￥{{ data.returnAmount }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                退货信息
              </div>
              <div class="box-text" v-if="data.logList && data.logList.length">
                <el-scrollbar wrap-class="max-scroll">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(activity, index) in data.logList"
                      :key="index"
                      :class="index === 0 ? 'active' : ''"
                      :timestamp="activity.info"
                    >
                    </el-timeline-item>
                  </el-timeline>
                </el-scrollbar>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                退货备注
              </div>
              <div class="box-text">
                <div class="mark-item">{{ data.remark }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="8" type="flex" v-if="data.returnStatus == 3">
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                驳回原因
              </div>
              <div class="box-text">
                <div class="mark-item">{{ data.refuseReason }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>
          商品信息
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailVOList && data.orderDetailVOList.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailVOList" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item">
                    <span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}
                  </div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item">
                    <span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}
                  </div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                  <div class="text-item">
                    <span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}
                  </div>
                  <div class="text-item" v-if="data.returnType == 1">
                    <span class="font-title-color">收货数量：</span>{{ item.receiveQuantity }}
                  </div>
                </div>
                <div class="right-text">
                  <div class="text-item">
                    <span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}
                  </div>
                  <div class="text-item">
                    <span class="font-title-color">折扣金额：</span>{{ item.discountAmount | toThousand('￥-') }}
                  </div>
                  <div class="text-item">
                    <span class="font-title-color">退款金额：</span>{{ item.returnAmount | toThousand('￥') }}
                  </div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== item.orderDeliveryList.length - 1 ? 'mar-b-8' : ''"
                >
                  <el-form class="content" :model="order" :rules="rules" :ref="'form' + index + idx" inline-message>
                    <div class="content-item font-title-color">批次号/序列号：<span class="font-important-color">{{ order.batchNo }}</span></div>
                    <div class="content-item font-title-color">有效期至：<span class="font-important-color">{{ order.expiryDate | formatDate }}</span></div>
                    <div class="content-item flex-row-left font-title-color" v-if="data.isAllowCheck == 1">
                      <span class="col-up">*</span>退货数量：
                      <el-form-item prop="returnQuantity" inline-message>
                        <el-input
                          type="number"
                          v-model="order.returnQuantity"
                          :min="0"
                          @input="e => (order.returnQuantity = checkReceiveQuantity(e, order.allowReturnQuantity))"
                          placeholder="请输入退货数量"
                        />
                      </el-form-item>
                    </div>
                    <div v-else class="content-item font-title-color">
                      退货数量：
                      <span class="font-important-color">{{ order.returnQuantity }}</span>
                    </div>
                  </el-form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button v-role-btn="['4']" type="primary" v-show="data.isAllowCheck == 1" @click="auditClick">审核</yl-button >
      </div>
      <!-- 审核 -->
      <yl-dialog
        title="退货单审核"
        right-btn-name="审核通过"
        :visible.sync="showAuditDialog"
        :destroy-on-close="false"
        :show-cancle="false"
        @confirm="confirm"
      >
        <div class="order-audit-content">
          <div class="info-view">退货审核通过后，已付款部分将转为退货核销单，未付款部分将释放掉占用额度；退货驳回终端可以再次提交退货</div>
          <el-input
            type="textarea"
            :autosize="{ minRows: 3 }"
            placeholder="备注"
            :maxlength="20"
            show-word-limit
            v-model="remark"
          >
          </el-input>
        </div>
        <yl-button slot="left-btn" @click="auditDialogLeftClick">退货驳回</yl-button>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getSellerOrderReturnDetail, checkOrderReturn, returnOrderReject } from '@/subject/pop/api/order'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnStatus } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'
import { onInputLimit } from '@/common/utils'

export default {
  name: 'ExtendReturnOrderDetail',
  components: {},
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
          path: '/dashboard'
        },
        {
          title: '销售订单管理'
        },
        {
          title: '数拓退货单',
          path: '/sell_order/extend_return_order',
          name: 'ExtendReturnOrder'
        },
        {
          title: '数拓退货单详情'
        }
      ],
      data: {},
      rules: {
        returnQuantity: [{ required: true, message: '请输入退货数量', trigger: 'blur' }]
      },
      showAuditDialog: false,
      remark: ''
    }
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
      this.checkFormValidate()
        .then(async result => {
          this.showAuditDialog = true
        })
        .catch(err => {
          this.$log('表单验证失败')
        })
    },
    // 退货驳回
    auditDialogLeftClick() {
      if (!this.remark.trim()) {
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
    async checkOrderMethod(type) {
      // type 1- 审核通过 2- 审核驳回
      let array = []
      if (this.data.orderDetailVOList && this.data.orderDetailVOList.length) {
        for (let i = 0; i < this.data.orderDetailVOList.length; i++) {
          let item = this.data.orderDetailVOList[i]
          let arr = []
          for (let i = 0; i < item.orderDeliveryList.length; i++) {
            let order = item.orderDeliveryList[i]
            arr.push({
              batchNo: order.batchNo,
              orderReturnDetailId: order.orderReturnDetailId,
              returnQuantity: Number(order.returnQuantity)
            })
          }
          array.push({
            detailId: item.id,
            goodsId: item.goodsId,
            orderDeliveryList: arr
          })
        }
        this.$common.showLoad()
        let data = null
        if (type == 1) {
          data = await checkOrderReturn(this.data.orderId, this.data.id, this.remark, array)
        } else {
          data = await returnOrderReject(this.data.orderId, this.data.id, this.remark, array)
        }
        this.$common.hideLoad()
        if (data && data.result) {
          let str = '审核成功'
          if (type == 2) {
            str = '驳回成功'
          }
          this.$common.alert(str, r => {
            this.$router.go(-1)
          })
        }
      }
    },

    // 多表单验证
    checkFormValidate() {
      let check = formName => {
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
      for (let i = 0; i < this.data.orderDetailVOList.length; i++) {
        let item = this.data.orderDetailVOList[i]
        for (let k = 0; k < item.orderDeliveryList.length; k++) {
          arr.push(check('form' + i + k))
        }
      }
      return Promise.all(arr)
    },
    // 图片上传成功
    onSuccess(data) {
      if (data.key) {
        this.receiveReceipt = data.key
      }
    },
    checkReceiveQuantity(val, maxVal) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > maxVal) {
        val = maxVal
      }
      return val
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.app-container-content {
  .max-scroll {
    height: 150px;
    padding: 4px 2px;
  }
  .header-box {
    .el-timeline {
      padding-left: 0 !important;
      .el-timeline-item {
        padding-bottom: 12px;
        .el-timeline-item__tail {
          border-left: 2px solid #d8d8d8 !important;
          z-index: 1;
          left: 2px !important;
        }
        .el-timeline-item__node--normal {
          width: 8px;
          height: 8px;
          border: 2px solid #bcbcbc;
          border-radius: 50%;
          background: #ffffff !important;
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
          border: 2px solid #1b9aee;
        }
      }
    }
  }
}
</style>
