<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="last_order"></svg-icon>
            <span>今日订单数</span>
          </div>
          <div class="title">
            {{ totalData.todayReturnOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>昨日订单数</span>
          </div>
          <div class="title">
            {{ totalData.yesterdayReturnOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="year_order"></svg-icon>
            <span>近一年订单数</span>
          </div>
          <div class="title">
            {{ totalData.allReturnOrderNum }}
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">采购商名称</div>
              <el-input v-model="query.buyerEname" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">供应商名称</div>
              <el-input v-model="query.sellerEname" placeholder="请输入供应商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单来源</div>
              <el-select v-model="query.orderSource" placeholder="请选择订单来源">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in returnSource" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">退货单号</div>
              <el-input v-model="query.orderReturnNo" placeholder="请输入退货单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">退货类型</div>
              <el-select v-model="query.returnType" placeholder="请选择退货类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderReturnStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">退货状态</div>
              <el-select v-model="query.returnStatus" placeholder="请选择退货状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderRejectStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">下单时间</div>
              <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
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
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton type="primary" plain @click="downLoadTemp1">导出明细</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table ref="table" :list="dataList" :total="query.total" :row-class-name="() => 'table-row'" :cell-class-name="getCellClass" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="320" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.buyerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单来源：</span>{{ row.returnSource | dictLabel(returnSource) }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据编号：</span>{{ row.returnNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据类型：</span>{{ row.returnType | dictLabel(orderReturnStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="退款信息" min-width="240" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>单据状态：</span>{{ row.returnStatus | dictLabel(orderRejectStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}（{{ row.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                <div class="item-text font-size-base font-title-color"><span>实退总金额：</span>{{ row.returnAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>提交时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>退回商品：</span>{{ row.returnGoods || '- -' }}种商品，数量{{ row.returnGoodsNum || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showDetail(row, 1)">查看详情</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getGoodsReturnedNumber,
  getRejectOrderList,
  checkOrderTicket
} from '@/subject/admin/api/zt_api/order';
import { createDownLoad } from '@/subject/admin/api/common'
import { orderReturnStatus, orderRejectStatus, paymentMethod, orderPayStatus, returnSource } from '@/subject/admin/utils/busi'

export default {
  name: 'GoodsReturnedList',
  components: {
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
    orderRejectStatus() {
      return orderRejectStatus()
    },
    // 来源
    returnSource() {
      return returnSource()
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
          title: '订单管理'
        },
        {
          title: '订单列表'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        returnStatus: 0,
        returnType: 0,
        orderSource: 0
      },
      dataList: [],
      // 渠道商弹框
      showDialog: false,
      // 汇总数据
      totalData: {},
      // 发票细心
      ticketInfo: {}
    };
  },
  activated() {
    this.getList()
    this.getTotal()
  },
  methods: {
    async getTotal() {
      let data = await getGoodsReturnedNumber()
      if (data) {
        this.totalData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getRejectOrderList(
        query.page,
        query.limit,
        query.orderNo,
        query.buyerEname,
        query.returnStatus,
        query.returnType,
        query.sellerEname,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.orderSource,
        query.orderReturnNo
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
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
        returnStatus: 0,
        returnType: 0,
        orderSource: 0
      }
    },
    //跳转详情界面
    showDetail(row, type) {
      if (type === 1) {
        // 跳转详情
        this.$router.push({
          name: 'GoodsReturnedDetail',
          params: { id: row.id }
        });
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2BCenterOrderReturnExportService',
        fileName: '导出退货单',
        groupName: '退货单申请导出',
        menuName: '退货单列表',
        searchConditionList: [
          {
            desc: '退货单来源',
            name: 'returnSource',
            value: query.orderSource
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '供应商名称',
            name: 'sellerEname',
            value: query.sellerEname || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo
          },
          {
            desc: '退货单状态',
            name: 'returnStatus',
            value: query.returnStatus
          },
          {
            desc: '下单开始时间',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 下载明细
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2BCenterOrderReturnDetailExportService',
        fileName: '导出退货单明细',
        groupName: '退货单明细申请导出',
        menuName: '退货单列表',
        searchConditionList: [
          {
            desc: '退货单来源',
            name: 'returnSource',
            value: query.orderSource
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '供应商名称',
            name: 'sellerEname',
            value: query.sellerEname || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo
          },
          {
            desc: '退货单状态',
            name: 'returnStatus',
            value: query.returnStatus
          },
          {
            desc: '下单开始时间',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endTime',
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
    },
    // 查看发票详情
    async showOrderTicket(row) {
      this.$common.showLoad()
      let data = await checkOrderTicket(row.id)
      this.$common.hideLoad()
      if (data && data.id) {
        this.ticketInfo = data
        this.ticketInfo.orderNo = row.orderNo
        this.showTicket = true
      } else {
        this.$common.warn('暂无发票信息')
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.order-table-view {
  .table-row {
    margin: 0 30px;
    td {
      .el-table__expand-icon {
        visibility: hidden;
      }
    }
  }
}
</style>
