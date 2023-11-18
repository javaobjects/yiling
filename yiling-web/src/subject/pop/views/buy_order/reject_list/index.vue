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
              <div class="title">供应商名称</div>
              <el-input v-model="query.name" placeholder="请输入供应商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单ID</div>
              <el-input v-model="query.orderId" placeholder="请输入订单ID" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
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
            <el-col :span="6">
              <div class="title">买家退货单号</div>
              <el-input v-model="query.rejectId" placeholder="请输入买家退货单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">退货类型</div>
              <el-select v-model="query.rejectType" placeholder="请选择退货类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderReturnStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">审核状态</div>
              <el-select v-model="query.reviewStatus" placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in returnStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="[2]" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          ref="table"
          :list="dataList"
          :total="query.total"
          :cell-class-name="() => 'border-1px-b'"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.sellerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.orderId }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据编号：</span>{{ row.returnNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据类型：</span>{{ row.returnType | dictLabel(orderReturnStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="退款信息" min-width="284" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>单据状态：</span>{{ row.returnStatus | dictLabel(returnStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}（{{ row.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                <div class="item-text font-size-base font-title-color"><span>退款总金额：</span>{{ row.returnAmount | toThousand('￥') }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="306" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>提交时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>退回商品：</span>{{ row.returnGoods || '- -' }}种商品，数量{{ row.returnGoodsNum || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div><yl-button v-role-btn="[1]" type="text" @click="showDetail(row)">查看详情</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getRejectOrderList } from '@/subject/pop/api/order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'BuyOrderReject',
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
    // 退货单审核状态
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
          title: '采购订单管理'
        },
        {
          title: '退货单'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        rejectType: 0,
        reviewStatus: 0
      },
      dataList: []
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getRejectOrderList(
        query.page,
        query.limit,
        query.orderNo,
        query.orderId,
        query.rejectId,
        query.reviewStatus,
        query.rejectType,
        query.name,
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
      this.query = {
        page: 1,
        limit: 10,
        createTime: [],
        rejectType: 0,
        reviewStatus: 0
      }
    },
    //跳转详情界面
    showDetail(row) {
      // 跳转详情
      this.$router.push(`/buyOrder/buyOrder_reject_detail/${row.id}`)
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderReturnExportService',
        fileName: '导出退货单',
        groupName: '退货单申请导出',
        menuName: '退货单列表',
        searchConditionList: [
          {
            desc: '导出来源',
            name: 'queryType',
            value: 1
          },
          {
            desc: '供应商名称',
            name: 'sellerEname',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '订单ID',
            name: 'orderId',
            value: query.orderId || ''
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.rejectId
          },
          {
            desc: '退货类型',
            name: 'returnType',
            value: query.rejectType
          },
          {
            desc: '退货单状态',
            name: 'returnStatus',
            value: query.reviewStatus
          },
          {
            desc: '开始时间',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '结束时间',
            name: 'endTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
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
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
</style>
