<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">供应商名称</div>
              <el-input v-model="query.name" placeholder="请输入供应商名称" @keyup.enter.native="handleSearch"/>
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
            <el-col :span="7">
              <div class="title">支付状态</div>
              <el-select v-model="query.paymentStatus" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderPayStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-8">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp1">导出明细</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
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
                <div class="title font-size-lg bold clamp-t-1">{{ row.sellerEname }}</div>
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
                <div class="item-text font-size-base font-title-color"><span>原价总金额：</span> {{ row.originalAmount | toThousand('￥') }} </div>
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
              <div><yl-button type="text" v-role-btn="['3']" v-if="row.paymentButtonFlag" :disabled="row.paymentNameType == 2 && !row.presaleButtonFlag" @click="payClick(row)">{{ row.orderCategory == 1 ? '立即支付' : row.paymentNameType == 1 ? '支付定金' : '支付尾款' }}</yl-button></div>
              <div><yl-button type="text" v-role-btn="['4']" v-if="row.cancelButtonFlag" @click="cancleDidClick(row)">取消订单</yl-button></div>
              <div><yl-button type="text" v-role-btn="['5']" v-if="row.receiveButtonFlag" @click="receiveThePackage(row, 1)">确认收货</yl-button></div>
              <div><yl-button type="text" v-role-btn="['6']" v-if="row.returnButtonFlag" @click="receiveThePackage(row, 2)">退货</yl-button></div>
              <div><yl-button type="text" v-role-btn="['7']" @click="showDetail(row)">查看明细</yl-button></div>
              <div><yl-button type="text" v-role-btn="['8']" v-if="row.deleteButtonFlag" @click="deleteClick(row)">删除订单</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getPurchaseList,
  purchaseCancle,
  toPay,
  paymentPay,
  purchaseDelete
} from '@/subject/pop/api/b2b_api/purchase_order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderStatus, orderPayStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'B2bPurchaseOrderList',
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
          title: '采购订单'
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
        paymentStatus: 0
      },
      dataList: []
    };
  },
  activated() {
    let { orderStatus, paymentMethod, paymentStatus } = this.$route.query
    if (typeof orderStatus !== 'undefined') {
      this.query.orderStatus = Number(orderStatus)
    }
    if (typeof paymentMethod !== 'undefined') {
      this.query.payType = Number(paymentMethod)
    }
    if (typeof paymentStatus !== 'undefined') {
      this.query.paymentStatus = Number(paymentStatus)
    }
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPurchaseList(
        query.page,
        query.limit,
        query.name,
        query.orderId,
        query.orderStatus,
        query.payType,
        query.paymentStatus,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
      );
      this.loading = false
      if (data) {
        this.dataList = data.records.map(item => {
          item.show = false
          return item
        })
        this.query.total = data.total
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
        paymentStatus: 0
      }
    },
    //跳转详情界面
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'B2bPurchaseOrderDetail',
        params: { id: row.id }
      });
    },
    // 去支付
    async payClick(row) {
      this.$common.showLoad()
      let list = []
      let order = {}
      order.orderId = row.id
      order.orderNo = row.orderNo
      list.push(order)
      let tradeType = 2
      if (row.orderCategory == 2) {
        if (row.paymentNameType == 1) {
          tradeType = 1
        }
        if (row.paymentNameType == 2) {
          tradeType = 4
        }
      }
      let payId = await toPay(tradeType, list)
      if (payId) {
        let redirectUrl = window.location.origin + `/#/b2b_purchase_order/b2b_purchase_pay_status?tab_platform=3&id=${row.id}&payId=${payId}`
        let payData = await paymentPay(payId, 'yeePayBank', redirectUrl, 'B2B-PC')
        if (payData.standardCashier) {
          this.$common.goThreePackage(payData.standardCashier)
        }
      }
      this.$common.hideLoad()
    },
    // 取消订单
    cancleDidClick(row) {
      this.$common.confirm('订单取消后不可恢复，所使用的优惠券部分支持退回，具体见优惠券规则', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await purchaseCancle(row.id)
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.n_success('取消成功')
            this.getList()
          }
        }
      })
    },
    receiveThePackage(row, type) {
      this.$router.push(`/b2b_purchase_order/b2b_purchase_order_receive/${type}/${row.id}`)
    },
    deleteClick(row) {
      this.$common.confirm('删除订单将无法查看，确认删除订单？', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await purchaseDelete(row.id)
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.$common.n_success('删除成功')
            this.getList()
          }
        }
      })
    },
    // 导出查询结果
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderB2BAdminPurchaseExportService',
        fileName: 'orderB2BAdminPurchaseExport',
        groupName: '采购订单列表',
        menuName: '采购订单-采购订单列表',
        searchConditionList: [
          {
            desc: '供应商名称',
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
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
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
        className: 'orderB2BAdminPurchaseDetailExportService',
        fileName: 'orderB2BAdminPurchaseDetailExport',
        groupName: '采购订单明细',
        menuName: '采购订单-采购订单明细',
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
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
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
