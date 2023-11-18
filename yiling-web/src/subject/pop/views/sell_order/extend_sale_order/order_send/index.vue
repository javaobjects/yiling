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
                <div class="item"><span class="font-title-color">订单编号：</span>{{ data.orderNo }}</div>
                <div class="item">
                  <span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}
                </div>
                <div class="item">
                  <span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}（{{
                    data.paymentStatus | dictLabel(orderPayStatus)
                  }}）
                </div>
                <div class="item"><span class="font-title-color">商品总额：</span>￥{{ data.totalAmount }}</div>
                <div class="item"><span class="font-title-color">支付总额：</span>￥{{ data.paymentAmount }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="common-box header-box">
              <div class="header-bar">
                <div class="sign"></div>
                收货地址信息
              </div>
              <div class="box-text" v-if="data.orderAddress">
                <div class="item"><span class="font-title-color">联系人：</span>{{ data.orderAddress.name }}</div>
                <div class="item"><span class="font-title-color">联系方式：</span>{{ data.orderAddress.mobile }}</div>
                <div class="item"><span class="font-title-color">联系地址：</span>{{ data.orderAddress.address }}</div>
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
          <div class="sign"></div>
          商品信息
        </div>
        <div class="font-title-color font-size-lg mar-l-8 mar-b-16">
          <span class="col-up"></span>物流信息：
          <el-radio-group v-model="deliveryType" @change="deliveryTypeChange">
            <el-radio :label="1">自有物流</el-radio>
            <el-radio :label="2">第三方物流</el-radio>
          </el-radio-group>
        </div>
        <!-- 快递公司 -->
        <div class="font-title-color font-size-lg mar-l-8 mar-b-16 deliver-info" v-show="deliveryType == 2">
          <span class="col-up"></span>快递公司：
          <el-select v-model="deliveryCompany" placeholder="请选择快递公司">
            <el-option
              v-for="item in orderDeliveryCompany"
              :key="item.label"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
          <el-input v-model="deliveryNo" placeholder="快递单号" />
        </div>
        <div>
          <div class="expand-row" v-if="data.orderDetailDelivery && data.orderDetailDelivery.length">
            <div class="expand-view" v-for="(item, index) in data.orderDetailDelivery" :key="index">
              <div class="title">{{ item.goodsName }}</div>
              <div class="desc-box flex-row-left">
                <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                <div class="right-text box1">
                  <div class="text-item">
                    <span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}
                  </div>
                  <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                  <div class="text-item">
                    <span class="font-title-color">规格/型号：</span
                    >{{
                      item.goodsSpecification +
                        '*' +
                        item.packageNumber +
                        (item.goodsRemark ? ` (${item.goodsRemark}) ` : '')
                    }}
                  </div>
                </div>
                <div class="right-text box2">
                  <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                </div>
                <div class="right-text">
                  <div class="text-item"><span class="font-title-color">商品单价：</span>￥{{ item.goodsPrice }}</div>
                  <div class="text-item">
                    <span class="font-title-color">金额小计：</span>
                    <span class="money bold">￥{{ item.goodsAmount }}</span>
                  </div>
                  <div class="text-item"></div>
                </div>
              </div>
              <div class="nums-box" v-if="item.orderDeliveryList && item.orderDeliveryList.length">
                <div
                  v-for="(order, idx) in item.orderDeliveryList"
                  :key="idx"
                  :class="idx !== item.orderDeliveryList.length - 1 ? 'mar-b-8' : ''"
                >
                  <el-form class="content" :model="order" :rules="rules" :ref="'form' + index + idx" inline-message>
                    <div class="content-item flex-row-left font-title-color">
                      <span v-show="order.deliveryQuantity != 0" class="col-up"></span>生产日期：
                      <el-form-item inline-message>
                        <el-date-picker
                          format="yyyy/MM/dd"
                          value-format="yyyy-MM-dd"
                          v-model="order.produceDate"
                          type="date"
                          placeholder="生产日期"
                        >
                        </el-date-picker>
                      </el-form-item>
                    </div>
                    <div class="content-item flex-row-left font-title-color">
                      <span v-show="order.deliveryQuantity != 0" class="col-up">*</span>有效期至：
                      <el-form-item
                        prop="expiryDate"
                        :rules="{
                          required: order.deliveryQuantity != 0,
                          message: '请输入有效期',
                          trigger: 'change'
                        }"
                        inline-message
                      >
                        <el-date-picker
                          format="yyyy/MM/dd"
                          value-format="yyyy-MM-dd"
                          v-model="order.expiryDate"
                          type="date"
                          placeholder="有效期"
                        >
                        </el-date-picker>
                      </el-form-item>
                    </div>
                    <div class="content-item flex-row-left font-title-color">
                      <span v-show="order.deliveryQuantity != 0" class="col-up">*</span>批次号/序列号：
                      <el-form-item
                        prop="batchNo"
                        :rules="{
                          required: order.deliveryQuantity != 0,
                          message: '请输入批次号',
                          trigger: 'blur'
                        }"
                        inline-message
                      >
                        <el-input v-model="order.batchNo" :min="0" placeholder="批次号/序列号" />
                      </el-form-item>
                    </div>
                    <div class="content-item flex-row-left font-title-color">
                      <span class="col-up">*</span>发货数量：
                      <el-form-item prop="deliveryQuantity" inline-message>
                        <el-input
                          type="number"
                          v-model="order.deliveryQuantity"
                          :min="0"
                          @input="
                            e => (order.deliveryQuantity = checkReceiveQuantity(e, item.goodsQuantity, index, idx))
                          "
                          placeholder="请输入发货数量"
                        />
                      </el-form-item>
                    </div>
                    <div class="flex-row-left">
                      <svg-icon
                        class="batch-add"
                        v-if="idx == 0"
                        :icon-class="'batch_add'"
                        @click="batchAddClick(index)"
                      />
                      <svg-icon
                        class="batch-add"
                        v-else
                        :icon-class="'batch_subtract'"
                        @click="batchSubtractClick(index)"
                      />
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
        <yl-button type="primary" @click="receiveThePackage">发货</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getSellOrderDetail, orderDelivery } from '@/subject/pop/api/order'
import {
  paymentMethod,
  orderStatus,
  orderPayStatus,
  orderTicketStatus,
  orderReturnStatus,
  orderDeliveryCompany
} from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'
import { mapGetters } from 'vuex'
import { isRepeat } from '@/common/utils'

export default {
  name: 'ExtendSaleOrderSend',
  components: {},
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
    // 快递公司
    orderDeliveryCompany() {
      return orderDeliveryCompany()
    },
    ...mapGetters(['currentEnterpriseInfo'])
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
          title: '数拓销售订单',
          path: '/sell_order/extend_sale_order'
        },
        {
          title: '数拓销售订单详情(发货)'
        }
      ],
      data: {},
      // 物流类型：1-自有物流 2-第三方物流
      deliveryType: 1,
      // 物流单号
      deliveryNo: '',
      // 物流公司
      deliveryCompany: '',
      // 退货类型
      returnType: null,
      rules: {
        expiryDate: [{ required: true, message: '请输入有效期', trigger: 'change' }],
        batchNo: [{ required: true, message: '请输入批次号', trigger: 'blur' }],
        deliveryQuantity: [{ required: true, message: '请输入发货数量', trigger: 'blur' }]
      }
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
      let data = await getSellOrderDetail(this.id)
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
            if (item.orderDeliveryList.length == 0) {
              let order = {
                expiryDate: '',
                batchNo: '',
                produceDate: '',
                deliveryQuantity: item.goodsQuantity
              }
              item.orderDeliveryList.push(order)
            }
          })
        }

        this.data = data
      }
    },
    // 发货数量
    batchAddClick(clickIndex) {
      let data = this.data
      if (data.orderDetailDelivery && data.orderDetailDelivery.length) {
        data.orderDetailDelivery.forEach((item, index) => {
          if (clickIndex == index) {
            let order = {
              expiryDate: '',
              batchNo: '',
              produceDate: '',
              deliveryQuantity: 0
            }
            item.orderDeliveryList.push(order)
          }
        })
        this.data = data
      }
    },
    batchSubtractClick(clickIndex) {
      let data = this.data
      if (data.orderDetailDelivery && data.orderDetailDelivery.length) {
        data.orderDetailDelivery.forEach((item, index) => {
          if (clickIndex == index) {
            item.orderDeliveryList.pop()
          }
        })
        this.data = data
      }
    },
    // 物流切换
    deliveryTypeChange() {
      this.deliveryCompany = ''
      this.deliveryNo = ''
    },
    // 收货
    receiveThePackage() {
      if (!this.data.id) {
        return
      }
      if (this.deliveryType == 2) {
        // 第三方物流
        if (this.deliveryCompany == '') {
          this.$common.warn('请先选择快递公司')
          return false
        }
        if (this.deliveryNo == '') {
          this.$common.warn('请先填写快递单号')
          return false
        }
      }
      this.checkFormValidate()
        .then(async result => {
          let array = []
          if (this.data.orderDetailDelivery && this.data.orderDetailDelivery.length) {
            let hasErr = false // 是否有输入发货数量 超过购买数量
            let hasRepeat = false // 一个商品的批次号是否有重复的
            for (let i = 0; i < this.data.orderDetailDelivery.length; i++) {
              let item = this.data.orderDetailDelivery[i]
              let arr = []
              // 统计数量
              let currentNum = 0
              let batchNoArr = []
              for (let i = 0; i < item.orderDeliveryList.length; i++) {
                let order = item.orderDeliveryList[i]
                batchNoArr.push(order.batchNo)
                currentNum += Number(order.deliveryQuantity)
                arr.push({
                  batchNo: order.batchNo,
                  deliveryQuantity: Number(order.deliveryQuantity),
                  expiryDate: order.expiryDate,
                  produceDate: order.produceDate
                })
              }
              if (currentNum > item.goodsQuantity) {
                hasErr = true
              }
              if (batchNoArr.length > 1) {
                if (isRepeat(batchNoArr)) hasRepeat = true
              }
              array.push({
                detailId: item.id,
                goodsId: item.goodsId,
                deliveryInfoList: arr
              })
            }
            if (hasRepeat) {
              this.$common.warn('同一商品的批次号不允许相同')
              return false
            }
            if (hasErr) {
              this.$common.warn('请填写正确的发货数量')
              return false
            }

            this.$common.showLoad()
            let data = await orderDelivery(
              this.data.id,
              this.deliveryType,
              array,
              this.deliveryCompany,
              this.deliveryNo
            )
            this.$common.hideLoad()
            if (data && data.result) {
              this.$common.alert('发货成功', r => {
                this.$router.go(-1)
              })
            }
          }
        })
        .catch(err => {
          this.$log('表单验证失败')
        })
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
      for (let i = 0; i < this.data.orderDetailDelivery.length; i++) {
        let item = this.data.orderDetailDelivery[i]
        for (let k = 0; k < item.orderDeliveryList.length; k++) {
          arr.push(check('form' + i + k))
        }
      }
      return Promise.all(arr)
    },
    checkReceiveQuantity(val, maxVal, i, k) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > maxVal) {
        val = maxVal
      }
      let formName = 'form' + i + k
      this.$log(formName)
      if (val == 0) {
        this.$refs[formName][0].clearValidate()
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.app-container-content {
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
