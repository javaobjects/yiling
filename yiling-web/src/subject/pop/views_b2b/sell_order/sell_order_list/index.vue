<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="last_order"></svg-icon>
            <span>今日订单数</span>
          </div>
          <div class="title">
            {{ totalData.todayOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>昨日订单数</span>
          </div>
          <div class="title">
            {{ totalData.yesterdayOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="year_order"></svg-icon>
            <span>近一年订单数</span>
          </div>
          <div class="title">
            {{ totalData.yearOrderNum }}
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="7">
              <div class="title">订单号</div>
              <el-input v-model="query.orderId" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="10">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">支付方式</div>
              <el-select v-model="query.payType" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in payType"
                  v-show="item.value !== 3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <!-- <el-col :span="7">
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderStatus"
                  v-show="item.value !== 100"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col> -->
            <el-col :span="7">
              <div class="title">支付状态</div>
              <el-select v-model="query.paymentStatus" placeholder="请选择支付状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderPayStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">区域查询</div>
              <div class="flex-row-left">
                <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="common-box mar-t-16">
        <!-- 导出按钮 -->
        <div class="down-box">
          <div class="select-bar">
            <el-radio-group
              class="select-radio-group"
              @change="selectItem"
              v-model="query.stateType"
              text-color="#1989FA"
              fill="#1989FA"
            >
              <el-radio-button v-for="item in selectBarList" :key="item.value" :label="item.value">
                <div class="text-box">
                  <span>{{ item.label }}</span>
                  <span class="num-box" v-if="item.value === 1">(<span class="num">{{ unDeliverQuantity }}</span>)</span>
                  <span class="num-box" v-if="item.value === 2">(<span class="num">{{ receiveQuantity }}</span>)</span>
                  <span class="num-box" v-if="item.value === 3">(<span class="num">{{ paymentDayQuantity }}</span>)</span>
                </div>
              </el-radio-button>
            </el-radio-group>
          </div>
          <div class="btn">
            <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
            <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp1">导出明细</ylButton>
          </div>
        </div>
        <div class="mar-t-16 order-table-view">
          <yl-table
            ref="table"
            :list="dataList"
            :total="query.total"
            :row-class-name="() => 'table-row'"
            :cell-class-name="getCellClass"
            show-header
            :page.sync="query.page"
            :limit.sync="query.limit"
            :loading="loading"
            :cell-no-pad="true"
            @getList="getList">
            <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="320" align="left">
              <template slot-scope="{ row }">
                <div class="item text-l mar-l-16">
                  <div class="title font-size-lg bold clamp-t-1">{{ row.buyerEname }}</div>
                  <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                  <div class="item-text font-size-base font-title-color"><span>下单时间：</span>{{ row.createTime | formatDate }}</div>
                  <div class="item-text font-size-base font-title-color"><span>订单状态：</span>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="支付信息" min-width="210" align="left">
              <template slot-scope="{ row }">
                <div class="item">
                  <div class="title"></div>
                  <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
                  <div class="item-text font-size-base font-title-color"><span>支付状态：</span>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>
                  <div class="item-text font-size-base font-title-color"></div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="相关金额信息" min-width="240" align="left">
              <template slot-scope="{ row }">
                <div class="item">
                  <div class="title"></div>
                  <div class="item-text font-size-base font-title-color"><span>原价总金额：</span>{{ row.originalAmount | toThousand('￥') }}</div>
                  <div class="item-text font-size-base font-title-color"><span>成交价总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                  <div class="item-text font-size-base font-title-color"><span>优惠总金额：</span>{{ row.discountAmount | toThousand('￥') }}</div>
                  <div class="item-text font-size-base font-title-color"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品信息" min-width="330" align="left">
              <template slot-scope="{ row }">
                <div class="item">
                  <div class="title"></div>
                  <div class="item-text font-size-base font-title-color"><span>购买商品：</span>{{ row.goodsOrderNum || '- -' }}种商品，数量{{ row.goodsOrderPieceNum || '- -' }}</div>
                  <div class="item-text font-size-base font-title-color"><span>发货商品：</span>数量{{ row.deliveryOrderPieceNum || '- -' }}</div>
                  <div class="item-text font-size-base font-title-color"></div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="left">
              <template slot-scope="{ row }">
                <div v-role-btn="['6']"><yl-button type="text" @click="showDetail(row, 1)">查看详情</yl-button></div>
                <div v-role-btn="['7']"><yl-button type="text" v-if="row.paymentDaySubmitFlag" @click="confirmPaymentClick(row)">确认已回款</yl-button></div>
                <div v-role-btn="['8']"><yl-button type="text" v-if="row.orderStatus === 30" @click="handleUpdateDelivery(row)">修改物流信息</yl-button></div>
                <!-- <div><yl-button type="text" v-if="row.orderStatus == 20 && !row.yilingSellerFlag && row.paymentMethod" @click="showDetail(row, 2)">发货</yl-button></div> -->
                <div v-role-btn="['3']"><yl-button type="text" v-if="row.judgeDeliveryFlag" @click="showDetail(row, 2)">发货</yl-button></div>
                <div v-role-btn="['4']"><yl-button type="text" v-if="row.judgeCancelFlag" @click="cancleDidClick(row)">取消订单</yl-button></div>
                <div v-role-btn="['5']"><yl-button type="text" v-show="row.orderReturnFlag" @click="showDetail(row, 3)">查看退货详情</yl-button></div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
    <yl-dialog title="账期订单回款信息确认" width="600px" @confirm="confirmClick" :visible.sync="showConfirmPayment" right-btn-name="确认已回款">
      <div class="order-submit">
        <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ currentOrder.orderNo }}</div>
        <div class="item-text font-size-base font-title-color"><span>下单时间：</span>{{ currentOrder.createTime | formatDate }}</div>
        <div class="item-text font-size-base font-title-color"><span>账期支付金额：</span>{{ currentOrder.paymentDayAmount | toThousand('￥') }}</div>
      </div>
    </yl-dialog>
    <!-- 修改物流信息 -->
    <yl-dialog
      class="delivery-dialog"
      :visible.sync="showDeliveryDialog"
      width="580px"
      title="物流信息"
      @confirm="confirmUpdateDelivery"
      :show-cancle="false"
      @close="closeDeliveryDialog"
    >
      <div class="dialog-content">
        <el-form :model="deliveryForm" class="deliveryForm" ref="deliveryFormRef" :rules="deliveryFormRules" label-position="left" label-width="80px">
          <el-form-item label="物流类型" prop="deliveryType">
            <el-radio-group v-model="deliveryForm.deliveryType" @change="handleSelectDeleveryType">
              <el-radio :label="1">自有物流</el-radio>
              <el-radio :label="2">第三方物流</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="deliveryForm.deliveryType === 2" label="快递公司" prop="deliveryCompany" class="delivery-company">
            <el-select class="mar-r-16" v-model="deliveryForm.deliveryCompany" placeholder="请选择物流公司">
              <el-option
                v-for="item in orderDeliveryCompany"
                :key="item.value"
                :label="item.label"
                :value="item.label">
              </el-option>
            </el-select>
            <el-input v-model="deliveryForm.deliveryNo" :disabled="!deliveryForm.deliveryCompany" placeholder="请填写物流单号"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template slot="left-btn">
        <yl-button plain @click="closeDeliveryDialog">取消</yl-button>
      </template>
    </yl-dialog>
  </div>
</template>

<script>
import {
  getSaleOrderNums,
  getOrderList,
  orderCancle,
  orderConfirmPayment,
  updateTransport
} from '@/subject/pop/api/b2b_api/sell_order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderStatus, orderPayStatus, orderDeliveryCompany } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'

export default {
  name: 'B2bSellOrderList',
  components: {
    ylChooseAddress
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
    // 物流公司
    orderDeliveryCompany() {
      return orderDeliveryCompany()
    }
  },
  data() {
    const deliveryCompanyVlidator = (rule, value, callback) => {
      if (this.deliveryForm.deliveryCompany === '' || this.deliveryForm.deliveryNo === '') {
        return callback(new Error('请选择物流公司并填写物流单号'))
      } else {
        return callback()
      }
    }
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
          title: '销售订单'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        orderStatus: 0,
        payType: 0,
        // 支付状态：1-待支付 2-已支付
        paymentStatus: 0,
        // 状态 0-全部,1-代发货,2-待收货,3-账期待支付,4-已完成,5-已取消
        stateType: 0
      },
      dataList: [],
      // 渠道商弹框
      showDialog: false,
      // 汇总数据
      totalData: {},
      // 查看发票信息
      showTicket: false,
      // 发票细心
      ticketInfo: {},
      showConfirmPayment: false,
      currentOrder: {},
      // 账期代付款
      paymentDayQuantity: 0,
      // 发货数量
      receiveQuantity: 0,
      // 待发货数量
      unDeliverQuantity: 0,
      selectBarList: [
        {
          label: '全部订单',
          value: 0
        },
        {
          label: '待发货',
          value: 1
        },
        {
          label: '待收货',
          value: 2
        },
        {
          label: '账期待付款',
          value: 3
        },
        {
          label: '已完成',
          value: 4
        },
        {
          label: '已取消',
          value: 5
        }
      ],
      // 修改物流信息弹窗
      showDeliveryDialog: false,
      // 物流信息
      deliveryForm: {
        // 订单单号
        orderId: '',
        // 物流公司
        deliveryCompany: '',
        // 物流单号
        deliveryNo: '',
        // 物流类型：1-自有物流 2-第三方物流
        deliveryType: ''
      },
      deliveryFormRules: {
        deliveryType: [{ required: true, message: '请选择物流类型', trigger: 'blur' }],
        deliveryCompany: [{ required: true, validator: deliveryCompanyVlidator, trigger: 'blur' }]
      }
    };
  },
  activated() {
    let { orderStatus } = this.$route.query
    if (typeof orderStatus !== 'undefined') {
      this.query.orderStatus = Number(orderStatus)
    }
    this.showConfirmPayment = false
    this.getList()
    this.getTotal()
  },
  methods: {
    async getTotal() {
      let data = await getSaleOrderNums()
      if (data) {
        this.totalData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getOrderList(
        query.page,
        query.limit,
        1,
        query.name,
        query.orderId,
        query.orderStatus,
        query.payType,
        query.paymentStatus,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.stateType
      );
      this.loading = false
      if (data) {
        this.dataList = data.orderPage.records.map(item => {
          item.show = false
          return item
        })
        this.query.total = data.orderPage.total
        this.paymentDayQuantity = data.paymentDayQuantity
        this.receiveQuantity = data.receiveQuantity
        this.unDeliverQuantity = data.unDeliverQuantity
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        createTime: [],
        orderStatus: 0,
        payType: 0,
        paymentStatus: 0,
        stateType: 0
      }
    },
    confirmPaymentClick(row) {
      this.showConfirmPayment = true
      this.currentOrder = row
    },
    async confirmClick() {
      let data = await orderConfirmPayment(this.currentOrder.id)
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('确认成功')
        this.showConfirmPayment = false
        this.getList()
      }
    },
    //跳转详情界面
    showDetail(row, type) { // sell_order_detail
      if (type === 1) {
        // 跳转详情
        this.$router.push({
          name: 'B2bSellOrderDetail',
          params: { id: row.id }
        });
      } else if (type === 2) {
        // 发货界面
        this.$router.push({
          name: 'B2bSellOrderSend',
          params: { id: row.id }
        });
      } else if (type === 3) {
        // 退货列表界面
        this.$router.push({
          name: 'B2bGoodsReturnList'
        })
      }
    },
    // 取消订单
    cancleDidClick(row) {
      this.$common.confirm('确认取消订单吗？', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await orderCancle(row.id)
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.n_success('取消成功')
            this.getList()
          }
        }
      })
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderB2BAdminExportService',
        fileName: 'orderB2BAdminExport',
        groupName: '销售订单列表',
        menuName: '销售订单-订单列表',
        searchConditionList: [
          {
            desc: '采购商名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus
          },
          {
            desc: '支付方式',
            name: 'paymentMethod',
            value: query.payType
          },
          {
            desc: '下单开始时间',
            name: 'startCreateTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endCreateTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          },
          {
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
          },
          {
            desc: '省',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '市',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区',
            name: 'regionCode',
            value: query.regionCode || ''
          },
          {
            desc: '状态',
            name: 'stateType',
            value: query.stateType
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出明细
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderB2BAdminDetailExportService',
        fileName: 'orderB2BAdminDetailExport',
        groupName: '销售订单明细列表',
        menuName: '销售订单-订单明细列表',
        searchConditionList: [
          {
            desc: '采购商名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus
          },
          {
            desc: '支付方式',
            name: 'paymentMethod',
            value: query.payType
          },
          {
            desc: '下单开始时间',
            name: 'startCreateTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endCreateTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          },
          {
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
          },
          {
            desc: '省',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '市',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区',
            name: 'regionCode',
            value: query.regionCode || ''
          },
          {
            desc: '状态',
            name: 'stateType',
            value: query.stateType
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    getCellClass(row) {
      if (!row.row.show) {
        return 'border-1px-b'
      }
      return ''
    },
    // 选择订单类型
    selectItem() {
      this.handleSearch()
    },
    // 修改物流信息
    handleUpdateDelivery(row) {
      this.deliveryForm.orderId = row.id
      this.showDeliveryDialog = true
    },
    // 提交修改物流信息
    confirmUpdateDelivery() {
      this.$refs.deliveryFormRef.validate(async valid => {
        if (valid) {
          this.$common.showLoad()
          let data = await updateTransport(
            this.deliveryForm.orderId,
            this.deliveryForm.deliveryType,
            this.deliveryForm.deliveryCompany,
            this.deliveryForm.deliveryNo
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.showDeliveryDialog = false
            this.$common.n_success('操作成功')
            this.getList()
          }
        }
      })
    },
    closeDeliveryDialog() {
      this.showDeliveryDialog = false
      this.deliveryForm.orderId = ''
      this.deliveryForm.deliveryCompany = ''
      this.deliveryForm.deliveryNo = ''
      this.deliveryForm.deliveryType = ''
    },
    handleSelectDeleveryType(value) {
      if (value === 1) {
        this.deliveryForm.deliveryCompany = ''
        this.deliveryForm.deliveryNo = ''
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
