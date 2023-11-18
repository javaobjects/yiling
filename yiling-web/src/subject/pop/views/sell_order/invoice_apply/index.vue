<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="invoice-total-box">
        <div class="invoice-total">
          <div class="total-box">
            <div class="header-bar">
              <div class="sign"></div>申请发票金额合计
            </div>
            <div class="total-data-box">
              <div class="box border-1px-r">
                <div class="flex-row-center">
                  <svg-icon class="svg-icon" icon-class="total_invoice_price"></svg-icon>
                  <span>发票金额</span>
                </div>
                <div class="title">¥{{ totalInvoiceAllAmount | toThousand('') }}</div>
              </div>
              <div class="box border-1px-r">
                <div class="flex-row-center">
                  <svg-icon class="svg-icon" icon-class="total_goods_price"></svg-icon>
                  <span>货款金额</span>
                </div>
                <div class="title">¥{{ totalGoodsAmount | toThousand('') }}</div>
              </div>
              <div class="box border-1px-r">
                <div class="flex-row-center">
                  <svg-icon class="svg-icon" icon-class="total_order_discount"></svg-icon>
                  <span>订单折扣</span>
                </div>
                <div class="title">¥{{ totalCashDiscountAmount | toThousand('') }}</div>
              </div>
              <div class="box border-1px-r">
                <div class="flex-row-center">
                  <svg-icon class="svg-icon" icon-class="total_ticket_discount"></svg-icon>
                  <span>票折金额</span>
                </div>
                <div class="title">¥{{ totalTicketDiscountAmount | toThousand('') }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="buy-order-half">
        <div class="common-box header-box mar-t-16">
          <div class="header-bar">
            <div class="sign"></div>订单信息
          </div>
          <div class="box-text">
            <el-row type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">订单编号：</span>{{ data.orderNo }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">货款总金额：</span>¥{{ data.totalAmount || '- -' }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票状态：</span>{{ data.invoiceStatus | dictLabel(orderTicketStatus) }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">现折总金额：</span>¥{{ data.cashDiscountAmount }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">销售组织：</span>{{ data.sellerEname }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">采购商：</span>{{ data.buyerEname }}</div>
              </el-col>
            </el-row>
            <el-row type="flex">
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color mar-r-10">设置发票的转换规则</span>
                  <el-select :disabled="isSetRuleCode" v-model="transitionRuleCode" placeholder="转换规则" @change="transitionRuleCodeChange">
                    <el-option
                      v-for="(item, index) in orderInvoiceTransitionRule"
                      :key="item.label + index"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8" v-if="transitionRuleCode == 'AR00004'">
                <div class="item">
                  <span class="col-up font-size-base">*</span>
                  <span class="font-title-color mar-r-10">电子邮箱</span>
                  <el-input v-model="invoiceEmail" placeholder="请输入电子邮箱" />
                </div>
              </el-col>
            </el-row>
            <div class="invoice-form-item invoice-text mar-b-16">
              <div class="invoice-form-item-left mar-b-8 font-title-color">开票摘要：</div>
              <div class="invoice-form-item-right">
                <el-input
                  type="textarea"
                  placeholder="请输入开票摘要"
                  :maxlength="50"
                  show-word-limit
                  resize="none"
                  v-model="invoiceSummary"
                  >
                </el-input>
              </div>
            </div>
          </div>
          <!-- 是否使用票折 -->
          <div class="top-view">
            <div class="my-form-item">
              <div class="my-form-item-left">是否使用票折：</div>
              <div class="my-form-item-right">
                <el-radio-group :disabled="transitionRuleCode == 'AR00002'" v-model="ticketDiscountFlag" @change="ticketDiscountFlagChange">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </div>
              <yl-button v-show="ticketDiscountFlag" type="text" class="btn-view" @click="checkMoreTicket">选择票折</yl-button>
            </div>
            <div v-show="ticketDiscountFlag">
                <div class="top-view-item mar-b-8" v-for="item in ticketDiscountNoList" :key="item.ticketDiscountNo">
                <div class="scal-20-width">票折单号：<span>{{ item.ticketDiscountNo }}</span> </div>
                <div class="scal-20-width">票折金额：<span>￥{{ item.totalAmount }}</span> </div>
                <div class="scal-20-width">可用金额：<span>￥{{ item.availableAmount }}</span> </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div>
          <div class="expand-row" v-if="data.orderInvoiceErpDeliveryNoList && data.orderInvoiceErpDeliveryNoList.length">
            <!-- 出库单 -->
            <div class="expand-view" v-for="(deliveryItem, deliveryIndex) in data.orderInvoiceErpDeliveryNoList" :key="deliveryIndex">
              <div class="top-delivery-view border-1px-b mar-b-16">
                <div class="delivery-view-left">
                  <div><span class="font-title-color">出库单:</span> <span v-if="deliveryItem.erpDeliveryNo">{{ deliveryItem.erpDeliveryNo }}</span></div>
                </div>
              </div>
              <!-- 商品 -->
              <div class="delivery-goods-view" v-for="(item, index) in deliveryItem.applyInvoiceDetailInfo" :key="index">
                <div class="title flex-row-left"><el-checkbox @change="checkedChange" v-model="item.checked"></el-checkbox><span class="mar-l-10">{{ item.goodsName }}</span></div>
                <div class="desc-box">
                  <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                  <div class="right-text box1">
                    <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                    <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                    <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification }}</div>
                  </div>
                  <div class="right-text box2">
                    <div class="text-item"><span class="font-title-color">商品单价：</span>￥{{ item.goodsPrice }}</div>
                    <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                    <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                  </div>
                  <div class="right-text box3">
                    <div class="text-item"><span class="font-title-color">货款金额：</span>￥{{ item.remainInvoiceAllAmount }}</div>
                    <div class="text-item"><span class="font-title-color">现折金额：</span>￥{{ item.cashDiscountAmount }}</div>
                    <div class="text-item"><span class="font-title-color">票折金额：</span>￥{{ item.ticketDiscountAmount }}</div>
                  </div>
                  <div class="right-text box4" v-show="ticketDiscountFlag">
                    <div class="form-view">
                      <el-select v-model="item.invoiceDiscountType" placeholder="请选择票折" @change="(type) => invoiceDiscountTypeChange(type, item)">
                        <el-option label="票折折扣金额" :value="2"></el-option>
                        <el-option label="票折折扣比率" :value="1"></el-option>
                      </el-select>
                      <el-form
                        class="form"
                        :model="item"
                        :rules="rules"
                        :ref="'form' + deliveryIndex + index"
                        >
                        <el-form-item
                          v-show="item.invoiceDiscountType == 1"
                          prop="ticketDiscountRate"
                          inline-message>
                          <el-input
                            v-model="item.ticketDiscountRate"
                            :min="0"
                            :max="100"
                            type="number"
                            @input="value => inputChange(value, deliveryIndex, index, 1)"
                            placeholder="票折折扣比率" />
                        </el-form-item>
                        <el-form-item
                          v-show="item.invoiceDiscountType == 2"
                          prop="ticketDiscountAmount"
                          inline-message>
                          <el-input
                            v-model="item.ticketDiscountAmount"
                            :min="0"
                            type="number"
                            @input="value => inputChange(value, deliveryIndex, index, 2)"
                            placeholder="票折折扣金额" />
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>
                  <div class="right-text" v-show="ticketDiscountFlag">
                    <div class="text-item">
                      <span class="font-title-color">商品现折金额：</span>
                      ￥{{ item.cashDiscountAmount }} ({{ item.caseDiscountRate }}%)
                      </div>
                    <div class="text-item">
                      <span class="font-title-color">商品票折金额：</span>
                      ￥{{ item.ticketDiscountAmount }} ({{ item.ticketDiscountRate }}%)
                    </div>
                    <div class="text-item">
                      <span class="font-title-color">商品折扣比率：</span>
                      {{ item.goodsDiscountRate }}%
                    </div>
                  </div>
                </div>
                <!-- 批次 -->
                <div>
                  <el-row class="batch-item" :gutter="8" type="flex" v-for="(batchItem, batchIndex) in item.batchList" :key="batchIndex">
                    <el-col :span="5">
                      <div>批次号/序列号：<span class="font-important-color">{{ batchItem.batchNo }}</span></div>
                    </el-col>
                    <el-col :span="5">
                      <div>生产日期：<span class="font-important-color">{{ batchItem.produceDate | formatDate }}</span></div>
                    </el-col>
                    <el-col :span="5">
                      <div>有效期至：<span class="font-important-color">{{ batchItem.expiryDate | formatDate }}</span></div>
                    </el-col>
                    <el-col :span="3">
                      <div>发货数量：<span class="font-important-color">{{ batchItem.deliveryQuantity }}</span></div>
                    </el-col>
                    <el-col :span="6">
                      <div class="flex-row-left batch-item-input">
                        <span>申请数量：</span>
                        <el-input
                          type="number"
                          v-model.number="batchItem.invoiceQuantity"
                          :min="0"
                          :max="batchItem.remainInvoiceQuantity"
                          :disabled="!item.checked || !batchItem.remainInvoiceQuantity"
                          @input="value => checkReceiveQuantity(value, deliveryIndex, index, batchIndex)"
                          placeholder="请输入发货数量" />
                          <span class="mar-l-10">剩余可开票数量：{{ batchItem.remainInvoiceQuantity }}</span>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="submitClick">提交</yl-button>
      </div>
    </div>
    <yl-dialog
      title="选择票折"
      :visible.sync="showTicket"
      :destroy-on-close="false"
      @confirm="confirm"
      >
      <div class="order-ticket-check">
        <yl-table
            stripe
            ref="multipleTable"
            :no-border="false"
            :show-header="true"
            :selection-change="handleSelectionChange"
            :list="ticketList">
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="票折单号" prop="ticketDiscountNo" align="center">
            </el-table-column>
            <el-table-column label="票折金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.totalAmount | toThousand }}</div>
              </template>
            </el-table-column>
            <el-table-column label="已用金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.usedAmount | toThousand }}</div>
              </template>
            </el-table-column>
            <el-table-column label="使用票折订单个数" align="center">
              <template slot-scope="{ row }">
                <yl-button type="text" class="btn-view" @click="viewTicketOrder(row)">{{ row.usedOrder }}个</yl-button>
              </template>
            </el-table-column>
            <el-table-column label="可用金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.availableAmount | toThousand }}</div>
              </template>
            </el-table-column>
          </yl-table>
      </div>
    </yl-dialog>
    <!-- 查看票折 -->
    <yl-dialog
      title="查看"
      :width="'65%'"
      :visible.sync="showTicketOrder"
      click-right-btn-close
      >
      <div class="order-ticket-check">
        <div class="mar-b-16">
          <el-row>
            <el-col :span="6">票折单号 <span>{{ currentTickerOrder.ticketDiscountNo }}</span></el-col>
            <el-col :span="6">票折金额 <span>{{ currentTickerOrder.totalAmount | toThousand('￥') }}</span></el-col>
            <el-col :span="6">已用金额 <span style="color:#E62412;">{{ currentTickerOrder.usedAmount | toThousand('￥') }}</span></el-col>
            <el-col :span="6">可用金额 <span style="color:#15AD31;">{{ currentTickerOrder.availableAmount | toThousand('￥') }}</span></el-col>
          </el-row>
        </div>
        <yl-table
            stripe
            :no-border="false"
            :show-header="true"
            :list="ticketOrderList">
            <el-table-column label="订单号" prop="orderNo" align="center">
            </el-table-column>
            <el-table-column label="货款金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.totalAmount | toThousand }}</div>
              </template>
            </el-table-column>
            <el-table-column label="使用票折金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.useAmount | toThousand }}</div>
              </template>
            </el-table-column>
            <el-table-column label="申请时间" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.createTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="现折金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.cashDiscountAmount | toThousand }}</div>
              </template>
            </el-table-column>
            <el-table-column label="支付金额" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.paymentAmount | toThousand }}</div>
              </template>
            </el-table-column>
          </yl-table>
      </div>
    </yl-dialog>
    <!-- 票折行数提示 -->
    <yl-dialog
      title="提示"
      :visible.sync="showLineNum"
      :destroy-on-close="false"
      @confirm="lineNumConfirm"
      >
      <div class="line-num-view">
        发票行数超过{{ ticketDiscountFlag == 1 ? '4' : '8' }}行，将打印发票清单页面，是否继续？
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { validateNumFloatLength } from '@/subject/pop/utils/validate'
import {
  getApplyInvoiceDetail,
  getInvoiceChoiceList,
  getInvoiceUseOrder,
  submitInvoice,
  computeInvoiceAmount,
  getInvoiceMaxAmount,
  getApplyInvoiceTotalAmount
} from '@/subject/pop/api/order'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus, orderInvoiceTransitionRule } from '@/subject/pop/utils/busi'
import debounce from 'lodash.debounce'

export default {
  components: {
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
    // 转换规则
    orderInvoiceTransitionRule() {
      return orderInvoiceTransitionRule()
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
          title: '发票管理'
        },
        {
          title: '申请发票'
        }
      ],
      // 1- 申请发票
      type: 1,
      data: {},
      // 总发票金额
      totalInvoiceAllAmount: 0,
      // 总贷款金额
      totalGoodsAmount: 0,
      // 总订单折扣
      totalCashDiscountAmount: 0,
      // 总票折金额
      totalTicketDiscountAmount: 0,
      // 点击合单拷贝的数据
      currentData: {},
      // 开票限额
      maxAmount: '',
      // 转换规则
      transitionRuleCode: '',
      // 是否设置过转换规则
      isSetRuleCode: false,
      // 开票摘要
      invoiceSummary: '',
      // 邮箱
      invoiceEmail: '',
      //是否使用票折：0-否 1-是
      ticketDiscountFlag: 0,
      // 选择票折
      showTicket: false,
      // 票折数据
      ticketList: [],
      // 表格选择
      multipleSelection: [],
      // 票折订单
      showTicketOrder: false,
      // 选择的票折
      ticketDiscountNoList: [],
      currentTickerOrder: {},
      //  使用票折订单个数
      ticketOrderList: [],
      rules: {
          ticketDiscountRate: [{ required: true, message: '请输入折扣比率', trigger: 'blur' }],
          ticketDiscountAmount: [{ required: true, message: '请输入折扣金额', trigger: 'blur' }]
      },
      // 开票行数提示
      showLineNum: false
    };
  },
  created() {
    this.inputChange = debounce(this.inputChange, 1000) // 搜索框防抖
  },
  mounted() {
    this.id = this.$route.params.id
    this.type = this.$route.params.type
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getApplyInvoiceDetail(this.id)
      this.$common.hideLoad()
      if (data) {
        for (let i = 0; i < data.orderInvoiceErpDeliveryNoList.length; i++) {
          let deliveryItem = data.orderInvoiceErpDeliveryNoList[i]
          for (let i=0; i<deliveryItem.applyInvoiceDetailInfo.length; i++) {
            let goodsItem = deliveryItem.applyInvoiceDetailInfo[i]
            goodsItem.checked = true
            for (let i = 0; i < goodsItem.batchList.length; i ++) {
              let batchItem = goodsItem.batchList[i]
              batchItem.invoiceQuantity = batchItem.remainInvoiceQuantity
            }
          }
        }
        if (data.transitionRuleCode) {
          this.isSetRuleCode = true
        }
        if (!this.transitionRuleCode) {
          this.transitionRuleCode = data.transitionRuleCode
        }
        if (!this.invoiceEmail) {
          this.invoiceEmail = data.invoiceEmail
        }
        if (data.invoiceMaxAmount) {
          this.maxAmount = data.invoiceMaxAmount
        }
        this.data = data
        await this.getTotalDetail()
      }
    },
    // 获取订单支付总额和发票总额字段信息
    async getTotalDetail() {
      let totalList = this.data.orderInvoiceErpDeliveryNoList
      let submitList = totalList.map((item, index) => {
        return {
          erpDeliveryNo: item.erpDeliveryNo,
          detailFormList: item.applyInvoiceDetailInfo.filter(filterGoodsItem => filterGoodsItem.checked === true).map((goodsItem) => {
            return {
              detailId: goodsItem.id,
              cashDiscountAmount: goodsItem.cashDiscountAmount,
              ticketDiscountAmount: goodsItem.ticketDiscountAmount,
              goodsDiscountAmount: goodsItem.goodsDiscountAmount,
              saveOrderTicketBatchList: goodsItem.batchList.map((batchItem) => {
                return {
                  batchNo: batchItem.batchNo,
                  invoiceQuantity: batchItem.invoiceQuantity
                }
              })
            }
          })
        }
      })
      submitList = submitList.filter(item => item.detailFormList.length > 0)
      if (submitList.length > 0) {
        let data = await getApplyInvoiceTotalAmount(submitList)
        if (data !== undefined) {
          this.totalInvoiceAllAmount = data.invoiceAllAmount
          this.totalGoodsAmount = data.goodsAmount
          this.totalCashDiscountAmount = data.cashDiscountAmount
          this.totalTicketDiscountAmount = data.ticketDiscountAmount
        }
      } else {
        // 所有商品都取消勾选 各项总额重置
        this.totalInvoiceAllAmount = 0
        this.totalGoodsAmount = 0
        this.totalCashDiscountAmount = 0
        this.totalTicketDiscountAmount = 0
      }
    },
    // 获取最开票限制金额
    async getInvoiceMaxAmountMethod(value) {
      this.$common.showLoad()
      let data = await getInvoiceMaxAmount(this.id, value)
      this.$common.hideLoad()
      if (data) {
        this.maxAmount = data
      } else {
        this.maxAmount = ''
      }
    },
    // 选择票折
    async checkMoreTicket() {
      this.$common.showLoad()
      let data = await getInvoiceChoiceList(this.id)
      this.$common.hideLoad()
      if (data && data.list) {
        this.ticketList = data.list
        this.showTicket = true
        this.$nextTick(() => {
          if (this.ticketDiscountNoList){
            this.ticketList.forEach(row => {
              let hasIndex = this.ticketDiscountNoList.findIndex(obj => {
                return row.ticketDiscountNo == obj.ticketDiscountNo;
              });
              if (hasIndex != -1){
                this.$refs.multipleTable.toggleRowSelectionMethod(row);
              }
            });
          }
        });
      } else {
        this.$common.warn('暂无票折信息')
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$log('this.multipleSelection:', val);
    },
    // 添加票折
    confirm() {
      if (this.multipleSelection.length > 0) {
        this.ticketDiscountNoList = this.multipleSelection
        this.showTicket = false
      } else {
        this.$common.warn('请选择票折')
      }
    },
    // 使用票折订单
    async viewTicketOrder(row) {
      // 当前点击的
      this.currentTickerOrder = row
      this.$common.showLoad()
      let data = await getInvoiceUseOrder(row.ticketDiscountNo)
      this.$common.hideLoad()
      if (data && data.list) {
        this.ticketOrderList = data.list
        this.showTicketOrder = true
      } else {
        this.$common.warn('暂无信息')
      }
    },
    // 转换票折切换
    transitionRuleCodeChange(value) {
      this.$common.log(value)
      if (value == 'AR00002') { // 不开票
        if (this.ticketDiscountFlag == 1) {
          this.ticketDiscountFlag = 0
          this.ticketDiscountFlagChange(0)
        }
      }
      this.getInvoiceMaxAmountMethod(value)
    },
    // 是否使用票折切换
    ticketDiscountFlagChange(type) {
      this.$common.log(type)
      if (type == 0) {
        // 清空选中的票折
        this.ticketDiscountNoList = []
        this.multipleSelection = []
      }

      this.getDetail()
    },
    // 提交
    submitClick() {
      // 设置转换规则
      if (!this.transitionRuleCode || this.transitionRuleCode.trim() == '') {
        this.$common.warn('请设置转换规则')
        return false
      }
      if (this.transitionRuleCode == 'AR00004') {
        if (this.invoiceEmail == '') {
          this.$common.warn('请输入电子邮箱')
          return false
        }
        if (this.invoiceEmail.indexOf('@') == -1) {
          this.$common.warn('请输入正确的电子邮箱')
          return false
        }
      }

      // 选择了票折
      if (this.ticketDiscountFlag == 1) {
        if (!this.ticketDiscountNoList || this.ticketDiscountNoList.length == 0) {
          this.$common.warn('请选择票折')
          return false
        } else {
          if (this.totalTicketDiscountAmount) {
            var canUsedAmount = 0
            for (let i=0; i<this.ticketDiscountNoList.length; i++) {
              let item = this.ticketDiscountNoList[i]
              canUsedAmount = this.$common.add(item.availableAmount, canUsedAmount)
            }
            if (Number(this.totalTicketDiscountAmount) > Number(canUsedAmount)) {
              this.$common.warn('已超过票折的可用额度，请重新输入！')
              return false
            }
          }
        }
      }

      // 限额
      if (this.transitionRuleCode !== 'AR00002' && this.maxAmount && Number(this.totalInvoiceAllAmount) > Number(this.maxAmount)) {
        this.$common.warn(`开票金额不能超过${this.maxAmount}元，请修改申请数量`)
        return false
      }

      this.checkFormValidate().then(async (result) => {
          let ticketDiscountNoList = []
          if (this.ticketDiscountNoList && this.ticketDiscountNoList.length > 0) {
            this.ticketDiscountNoList.forEach((item) => {
              ticketDiscountNoList.push(item.ticketDiscountNo)
            })
          }

          // 关联出库单
          let erpDeliveryNoList = []
          let ticketDiscount = []
          // eslint-disable-next-line no-unused-vars
          let invoiceLineNum = 0
          if (this.data.orderInvoiceErpDeliveryNoList && this.data.orderInvoiceErpDeliveryNoList.length > 0){

              for (let i=0; i< this.data.orderInvoiceErpDeliveryNoList.length; i++) {
                // 出库单
                let deliveryItem = this.data.orderInvoiceErpDeliveryNoList[i]

                for (let j=0; j<deliveryItem.applyInvoiceDetailInfo.length; j++) {
                  // 商品
                  let goodsItem = deliveryItem.applyInvoiceDetailInfo[j]
                  this.$log('goodsItem', goodsItem)
                  if (goodsItem.checked != undefined && goodsItem.checked) {
                    let saveOrderTicketBatchList = []
                    // 计算有开票数量的批次
                    let hasNum = false
                    if (goodsItem.batchList && goodsItem.batchList.length > 0) {
                      for (let k = 0; k < goodsItem.batchList.length; k++) {
                        let batchItem = goodsItem.batchList[k]
                        if (batchItem.invoiceQuantity && batchItem.invoiceQuantity != 0) {
                          saveOrderTicketBatchList.push(batchItem)
                          hasNum = true
                          invoiceLineNum += 1
                        }
                      }
                    }
                    // 改商品可以参与计算
                    if (hasNum) {
                      erpDeliveryNoList.push(deliveryItem.erpDeliveryNo)
                      if (this.ticketDiscountFlag == 1) {
                        ticketDiscount.push({
                          detailId: goodsItem.id,
                          invoiceDiscountType: goodsItem.invoiceDiscountType,
                          ticketDiscountRate: Number(goodsItem.ticketDiscountRate),
                          ticketDiscountAmount: Number(goodsItem.ticketDiscountAmount),
                          erpDeliveryNo: deliveryItem.erpDeliveryNo,
                          saveOrderTicketBatchList: saveOrderTicketBatchList
                        })
                      } else {
                        ticketDiscount.push({
                          detailId: goodsItem.id,
                          erpDeliveryNo: deliveryItem.erpDeliveryNo,
                          saveOrderTicketBatchList: saveOrderTicketBatchList
                        })
                      }
                    }

                  }
                }

              }
              this.$log(this.id, this.ticketDiscountFlag, ticketDiscountNoList, ticketDiscount, this.transitionRuleCode, this.invoiceSummary, erpDeliveryNoList, this.invoiceEmail)
              if (invoiceLineNum == 0) {
                this.$common.warn('请输入开票数量')
                return
              }
              // 选择了票折
              if (this.ticketDiscountFlag == 1) {
                if (invoiceLineNum > 4) {
                  this.showLineNum = true
                  return
                }
              } else {
                if (invoiceLineNum > 8) {
                  this.showLineNum = true
                  return
                }
              }

              this.$common.showLoad()
              erpDeliveryNoList = erpDeliveryNoList.filter((item, i, arr) => arr.indexOf(item) === i)
              // 1-申请发票
              let data = await submitInvoice(this.id, this.ticketDiscountFlag, ticketDiscountNoList, ticketDiscount, this.transitionRuleCode, this.invoiceSummary, erpDeliveryNoList, this.invoiceEmail)
              this.$common.hideLoad()
              if (data && data.result) {
                this.$common.alert('提交成功', r => {
                  this.$router.go(-1)
                })
              }
          }

        }).catch(err => {
          this.$log('表单验证失败',err)
        })
    },
    // 发票行数确认点击
    async lineNumConfirm() {
      this.showLineNum = false
      let ticketDiscountNoList = []
      if (this.ticketDiscountNoList && this.ticketDiscountNoList.length > 0) {
        this.ticketDiscountNoList.forEach((item) => {
          ticketDiscountNoList.push(item.ticketDiscountNo)
        })
      }

      let erpDeliveryNoList = []
      let ticketDiscount = []

      if (this.data.orderInvoiceErpDeliveryNoList && this.data.orderInvoiceErpDeliveryNoList.length > 0){

          for (let i=0; i< this.data.orderInvoiceErpDeliveryNoList.length; i++) {
            // 出库单
            let deliveryItem = this.data.orderInvoiceErpDeliveryNoList[i]

            for (let j=0; j<deliveryItem.applyInvoiceDetailInfo.length; j++) {
              // 商品
              let goodsItem = deliveryItem.applyInvoiceDetailInfo[j]
              this.$log('goodsItem', goodsItem)
              if (goodsItem.checked != undefined && goodsItem.checked) {
                let saveOrderTicketBatchList = []
                // 计算有开票数量的批次
                let hasNum = false
                if (goodsItem.batchList && goodsItem.batchList.length > 0) {
                  for (let k = 0; k < goodsItem.batchList.length; k++) {
                    let batchItem = goodsItem.batchList[k]
                    if (batchItem.invoiceQuantity && batchItem.invoiceQuantity != 0) {
                      saveOrderTicketBatchList.push(batchItem)
                      hasNum = true
                    }
                  }
                }
                // 改商品可以参与计算
                if (hasNum) {
                  erpDeliveryNoList.push(deliveryItem.erpDeliveryNo)
                  if (this.ticketDiscountFlag == 1) {
                    ticketDiscount.push({
                      detailId: goodsItem.id,
                      invoiceDiscountType: goodsItem.invoiceDiscountType,
                      ticketDiscountRate: Number(goodsItem.ticketDiscountRate),
                      ticketDiscountAmount: Number(goodsItem.ticketDiscountAmount),
                      erpDeliveryNo: deliveryItem.erpDeliveryNo,
                      saveOrderTicketBatchList: saveOrderTicketBatchList
                    })
                  } else {
                    ticketDiscount.push({
                      detailId: goodsItem.id,
                      erpDeliveryNo: deliveryItem.erpDeliveryNo,
                      saveOrderTicketBatchList: saveOrderTicketBatchList
                    })
                  }
                }

              }
            }

          }
          this.$log(this.id, this.ticketDiscountFlag, ticketDiscountNoList, ticketDiscount, this.transitionRuleCode, this.invoiceSummary, erpDeliveryNoList, this.invoiceEmail)

          this.$common.showLoad()
          erpDeliveryNoList = erpDeliveryNoList.filter((item, i, arr) => arr.indexOf(item) === i)
          // 1-申请发票
          let data = await submitInvoice(this.id, this.ticketDiscountFlag, ticketDiscountNoList, ticketDiscount, this.transitionRuleCode, this.invoiceSummary, erpDeliveryNoList, this.invoiceEmail)
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.alert('提交成功', r => {
              this.$router.go(-1)
            })
          }
      }
    },
    invoiceDiscountTypeChange(type, item) {
      this.$common.log(item, type)
      item.invoiceDiscountType = type
    },
    async inputChange(value, deliveryIndex, index, type) {
      this.$common.hideLoad()
      if (!value) return
      // 出库单
      let deliveryItem = this.data.orderInvoiceErpDeliveryNoList[deliveryIndex]
      //  商品
      let item = deliveryItem.applyInvoiceDetailInfo[index]
      if (!item.checked) {
        this.$common.warn('请选择开票商品')
        return false
      }
      // 批次 确认批次有输入数量
      let batchFormList = []
      let hasNum = false
      // 有开票数量大于可开票数量
      let hasOverNum = false
      if (item.batchList && item.batchList.length > 0) {
        for (let i = 0; i < item.batchList.length; i++) {
          let batchItem = item.batchList[i]
          if (batchItem.invoiceQuantity && batchItem.invoiceQuantity != 0) {
            hasNum = true
            batchFormList.push(batchItem)
            if (batchItem.invoiceQuantity > batchItem.remainInvoiceQuantity) {
              hasOverNum = true
            }
          }
        }
      }
      // 没有开票数量
      if (!hasNum) {
        this.$common.warn('该商品未设置开票数量')
        return
      }
      // 开票数量超过 剩余可开票数量
      if (hasOverNum) {
        this.$common.warn('数量需小于可开票数量')
        return
      }

      this.$common.log(item)
      if (type == 1) {
        let num1 = this.$common.sub(100, Number(item.caseDiscountRate))
        this.$common.log(value, num1)
        if (value > num1) {
          value = num1
        }
      } else {
        let num2 = this.$common.sub(item.remainInvoiceAllAmount, item.cashDiscountAmount)
        if (value > num2) {
          value = num2
        }
      }
      let hasFloatLength = validateNumFloatLength(value, 2)

      if (hasFloatLength){// 超过2位小数
        value = Number(value).toFixed(2)
      }

      let data = await computeInvoiceAmount(this.id, item.id, item.invoiceDiscountType, value, deliveryItem.erpDeliveryNo, item.goodsPrice, batchFormList)
      this.$common.hideLoad()
      if (data) {
        this.$common.log(data)
        item.cashDiscountAmount = data.cashDiscountAmount

        item.ticketDiscountAmount = data.ticketDiscountAmount
        item.ticketDiscountRate = data.ticketDiscountRate

        item.goodsDiscountAmount = data.goodsDiscountAmount
        item.goodsDiscountRate = data.goodsDiscountRate

        // 货款金额
        item.remainInvoiceAllAmount = data.remainInvoiceAllAmount
      }
      await this.calculateTicketDiscountAmount()

    },
    // 勾选事件
    checkedChange() {
      this.calculateTicketDiscountAmount()
    },
    // 计算票折总金额
    async calculateTicketDiscountAmount() {
      // 总额各项信息
      await this.getTotalDetail()
      this.$forceUpdate()
    },
    // 多表单验证
    checkFormValidate() {
      let check = (formName) => {
        return new Promise((resolve, reject) => {
          this.$refs[formName][0].validate(async valid => {
            this.$log('valid',formName,valid)
            if (valid) {
              resolve(true)
            } else {
              reject()
            }
          }).catch((err) => {
            this.$log(err)
          })
        })
      }

      var arr = []
      for (let i=0; i< this.data.orderInvoiceErpDeliveryNoList.length; i++) {
        let deliveryItem = this.data.orderInvoiceErpDeliveryNoList[i]

        for (let j=0; j<deliveryItem.applyInvoiceDetailInfo.length; j++) {
          // 出库单
          let goodsItem = deliveryItem.applyInvoiceDetailInfo[j]

          if (goodsItem.checked) {
            this.$log('form' + i + j)
            arr.push(check('form' + i + j))
          }

        }
      }
      return Promise.all(arr)
    },
    async checkReceiveQuantity(value, deliveryIndex, index, batchIndex) {
      this.$common.hideLoad()
      if (!value) return
      // 出库单
      let deliveryItem = this.data.orderInvoiceErpDeliveryNoList[deliveryIndex]
      //  商品
      let item = deliveryItem.applyInvoiceDetailInfo[index]
      if (!item.checked) {
        this.$common.warn('请选择开票商品')
        return false
      }
      // 批次 确认批次有输入数量
      let batchFormList = []
      // 有开票数量
      if (item.batchList && item.batchList.length > 0) {
        for (let i = 0; i < item.batchList.length; i++) {
          let batchItem = item.batchList[i]
          if (batchItem.invoiceQuantity !== '') {
            batchFormList.push(batchItem)
            if (batchItem.invoiceQuantity > batchItem.remainInvoiceQuantity) {
              value = batchItem.remainInvoiceQuantity
              batchItem.invoiceQuantity = batchItem.remainInvoiceQuantity
            }
          }

        }
      }

      // 输入的比率或金额值
      let inputVallue = 0
      if (item.invoiceDiscountType == 1) {
        let num1 = this.$common.sub(100, Number(item.caseDiscountRate))
        if (item.ticketDiscountRate > num1) {
          inputVallue = num1
        } else {
          inputVallue = item.ticketDiscountRate
        }
      } else {
        let num2 = this.$common.sub(item.remainInvoiceAllAmount, item.cashDiscountAmount)
        if (item.ticketDiscountAmount >= num2) {
          inputVallue = 0
        } else {
          inputVallue = item.ticketDiscountAmount
        }
      }

      let data = await computeInvoiceAmount(this.id, item.id, item.invoiceDiscountType, inputVallue, deliveryItem.erpDeliveryNo, item.goodsPrice, batchFormList)
      this.$common.hideLoad()
      if (data) {
        this.$common.log(data)
        item.cashDiscountAmount = data.cashDiscountAmount

        item.ticketDiscountAmount = data.ticketDiscountAmount
        item.ticketDiscountRate = data.ticketDiscountRate

        item.goodsDiscountAmount = data.goodsDiscountAmount
        item.goodsDiscountRate = data.goodsDiscountRate

        // 货款金额
        item.remainInvoiceAllAmount = data.remainInvoiceAllAmount
      }
      await this.calculateTicketDiscountAmount()

    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>