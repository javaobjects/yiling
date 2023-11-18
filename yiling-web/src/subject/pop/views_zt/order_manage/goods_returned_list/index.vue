<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商业名称</div>
              <el-input v-model="query.name" placeholder="请输入商业名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
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
              <div class="title">审核状态</div>
              <el-select v-model="query.returnStatus" placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in returnStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
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
      <div class="down-box">
        <!-- tab切换 -->
        <div class="tab">
            <div v-for="(item,index) in tabList" :key="index" class="tab-item" :class="tabActive === item.value ? 'tab-active' : ''" @click="clickTab(item.value)">{{ item.label }}</div>
        </div>
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出销售退货单</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp1">导出采购退货单</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table ref="table" :list="dataList" :total="query.total" :row-class-name="() => 'table-row'" :cell-class-name="getCellClass" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="360" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold top-title"><span>采购商：</span>{{ row.buyerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单来源：</span>{{ row.returnSource | dictLabel(returnSource) }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据编号：</span>{{ row.returnNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据类型：</span>{{ row.returnType | dictLabel(orderReturnStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="退款信息" min-width="320" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title font-size-lg bold top-title"><span>供应商：</span>{{ row.sellerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据状态：</span>{{ row.returnStatus | dictLabel(returnStatus) }}</div>
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
import { refundEnterpriseList } from '@/subject/pop/api/zt_api/order'
import { createDownLoad } from '@/subject/pop/api/common'
import { orderReturnStatus, returnStatus, paymentMethod, orderPayStatus, returnSource } from '@/subject/pop/utils/busi'

export default {
  name: 'ZtGoodsReturnedList',
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
    returnStatus() {
      return returnStatus()
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
          path: '/zt_dashboard'
        },
        {
          title: '企业订单数据'
        },
        {
          title: '企业退货单'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        orderNo: '',
        orderReturnNo: '',
        returnType: 0,
        returnStatus: 0,
        type: 1,
        createTime: []
      },
      dataList: [],
      tabList: [
        {
          label: '销售退货单',
          value: 1
        },
        {
          label: '采购退货单',
          value: 2
        }
      ],
      tabActive: 1,
      hasExport: false
    };
  },
  activated() {
    // this.getList()
  },
  methods: {
    clickTab(e) {
      this.tabActive = e
      this.query.type = e
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    async getList() {
      this.hasExport = true
      this.loading = true
      let query = this.query
      let data = await refundEnterpriseList(
        query.page,
        query.limit,
        query.name,
        query.orderNo,
        query.orderReturnNo,
        query.returnType,
        query.returnStatus,
        query.type,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
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
      let type = this.query.type
      this.query = {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        orderNo: '',
        orderReturnNo: '',
        returnType: 0,
        returnStatus: 0,
        type: type,
        createTime: []
      }
    },
    //跳转详情界面
    showDetail(row, type) {
      if (type === 1) {
        // 跳转详情
        this.$router.push({
          name: 'ZtGoodsReturnedDetail',
          params: { id: row.id }
        });
      }
    },
    // 下载模板
    async downLoadTemp() {
      if (!this.hasExport) {
        this.$common.warn('请搜索数据再导出')
        return
      }
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderReturnEnterpriseSellExportService',
        fileName: '销售退货单',
        groupName: '销售退货单导出',
        menuName: '企业订单-退货单列表',
        searchConditionList: [
          {
            desc: '企业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo || ''
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '审核状态',
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
      if (!this.hasExport) {
        this.$common.warn('请搜索数据再导出')
        return
      }
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderReturnEnterpriseBuyerExportService',
        fileName: '采购退货单',
        groupName: '采购退货单导出',
        menuName: '企业订单-退货单列表',
        searchConditionList: [
          {
            desc: '企业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo || ''
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '审核状态',
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
