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
            <span>今日退货单数</span>
          </div>
          <div class="title">
            {{ totalData.todayReturnOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>昨日退货单数</span>
          </div>
          <div class="title">
            {{ totalData.yesterdayReturnOrderNum }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="reject_order_num"></svg-icon>
            <span>总计退货单数</span>
          </div>
          <div class="title">
            {{ totalData.allReturnOrderNum }}
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">采购商名称</div>
              <el-input v-model="query.buyerEname" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
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
              <div class="title">退货单号</div>
              <el-input v-model="query.orderReturnNo" placeholder="请输入买家退货单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="7">
              <div class="title">退货类型</div>
              <el-select v-model="query.returnType" placeholder="请选择退货类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderReturnStatus"
                  v-show="item.value !== 2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">审核状态</div>
              <el-select v-model="query.returnStatus" placeholder="请选择审核状态">
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
                <div class="title font-size-lg bold clamp-t-1">{{ row.buyerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据编号：</span>{{ row.returnNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据类型：</span>{{ row.returnType | dictLabel(orderReturnStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据状态：</span>{{ row.returnStatus | dictLabel(returnStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="退款信息" min-width="284" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}（{{ row.paymentStatus | dictLabel(orderPayStatus) }}）</div>
                <div class="item-text font-size-base font-title-color"><span>退款总金额：</span>{{ row.totalReturnAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>优惠总金额：</span>{{ row.totalDiscountAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>实退总金额：</span>{{ row.returnAmount | toThousand('￥') }}</div>
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
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div v-role-btn="['3']"><yl-button type="text" @click="showDetail(row)">查看详情</yl-button></div>
              <div v-role-btn="['4']" v-show="row.returnStatus == 1"><yl-button type="text" @click="auditClick(row)">审核</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 审核 -->
    <yl-dialog
      title="退货单审核"
      width="540px"
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
          :autosize="{ minRows: 5 }"
          placeholder="备注"
          :maxlength="20"
          show-word-limit
          v-model="remark">
        </el-input>
      </div>
      <yl-button slot="left-btn" @click="auditDialogLeftClick">退货驳回</yl-button>
    </yl-dialog>
  </div>
</template>

<script>
import {
  getSellerOrderNums,
  getReturnGoodsList,
  returnOrderReject
} from '@/subject/pop/api/b2b_api/sell_order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'B2bGoodsReturnList',
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
          path: '/b2b_dashboard'
        },
        {
          title: '销售订单管理'
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
        // 退货类型
        returnType: 0,
        // 审核状态
        returnStatus: 0
      },
      dataList: [],
      // 汇总数据
      totalData: {},
      // 审核弹框
      showAuditDialog: false,
      // 备注
      remark: '',
      // 审核当前点击的 row
      currentRow: {}
    };
  },
  activated() {
    this.type = 1
    let { reviewStatus } = this.$route.query
    if (typeof reviewStatus !== 'undefined') {
      this.query.returnStatus = Number(reviewStatus)
    }
    this.getList()
    this.getTotal()
  },
  methods: {
    async getTotal() {
      let data = await getSellerOrderNums()
      if (data) {
        this.totalData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getReturnGoodsList(
        query.page,
        query.limit,
        query.orderId,
        query.orderReturnNo,
        query.returnStatus,
        query.returnType,
        query.buyerEname,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        this.type
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
        returnType: 0,
        returnStatus: 0
      }
    },
    //跳转详情界面
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'B2bGoodsReturnDetail',
        params: { id: row.id }
      });
    },
    // 审核点击
    auditClick(row) {
      this.currentRow = row
      this.remark = ''
      this.showAuditDialog = true
    },
    // 退货驳回
    async auditDialogLeftClick() {
      if (!this.remark.trim()){
        this.$common.warn('请输入退货驳回备注')
      } else {
        this.$common.showLoad()
        let data = await returnOrderReject(this.currentRow.id, false, this.remark.trim())
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.showAuditDialog = false
          this.$common.n_success('驳回成功');
        }
      }
    },
    // 审核通过点击
    async confirm() {
      this.$common.showLoad()
      let data = await returnOrderReject(this.currentRow.id, true, this.remark.trim())
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.showAuditDialog = false
        this.$common.n_success('审核成功')
        this.getList()
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2BAdminOrderReturnExportService',
        fileName: '导出退货单',
        groupName: '退货单申请导出',
        menuName: '退货单列表',
        searchConditionList: [
          {
            desc: '退货单来源',
            name: 'returnSource',
            value: 3
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '退货单状态',
            name: 'returnStatus',
            value: query.returnStatus
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
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 导出明细
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2BAdminOrderReturnDetailExportService',
        fileName: 'B2B退货单明细列表导出',
        groupName: 'B2B退货单申请导出',
        menuName: '退货单列表',
        searchConditionList: [
          {
            desc: '退货单来源',
            name: 'returnSource',
            value: 3
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '退货单号',
            name: 'orderReturnNo',
            value: query.orderReturnNo
          },
          {
            desc: '退货单类型',
            name: 'returnType',
            value: query.returnType
          },
          {
            desc: '退货单状态',
            name: 'returnStatus',
            value: query.returnStatus
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
      if (typeof data !== 'undefined') {
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
