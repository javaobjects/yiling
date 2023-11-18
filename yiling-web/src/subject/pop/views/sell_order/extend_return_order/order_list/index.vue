<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">采购商名称</div>
              <el-input v-model="query.buyerEname" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
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
              <div class="title">退货单号</div>
              <el-input v-model="query.orderReturnNo" placeholder="请输入买家退货单号" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </el-col>
            <el-col :span="6">
              <div class="title">退货类型</div>
              <el-select v-model="query.returnType" placeholder="请选择退货类型">
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
          <ylButton type="primary" v-role-btn="['3']" plain @click="downLoadTemp">导出查询结果</ylButton>
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
          @getList="getList"
        >
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.buyerEname || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.orderId }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>单据编号：</span>{{ row.returnNo }}</div>
                <div class="item-text font-size-base font-title-color">
                  <span>单据类型：</span>{{ row.returnType | dictLabel(orderReturnStatus) }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="退款信息" min-width="284" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>单据状态：</span>{{ row.returnStatus | dictLabel(returnStatus) }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}（{{ row.paymentStatus | dictLabel(orderPayStatus) }}）
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>退款金额：</span>￥{{ row.returnAmount }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="306" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>提交时间：</span>{{ row.createTime | formatDate }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>退回商品：</span>{{ row.returnGoods || '- -' }}种商品，数量{{ row.returnGoodsNum || '- -' }}
                </div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" v-role-btn="['1']" v-show="row.isAllowCheck == 1" @click="auditClick(row)">审核</yl-button>
              </div>
              <div>
                <yl-button type="text" v-role-btn="['2']" @click="showDetail(row)">查看详情</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
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
        <div class="info-view">
          退货审核通过后，已付款部分将转为退货核销单，未付款部分将释放掉占用额度；退货驳回终端可以再次提交退货
        </div>
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
</template>

<script>
import { getReturnGoodsList, returnOrderReject, checkOrderReturn } from '@/subject/pop/api/order'
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderPayStatus, orderReturnStatus, returnStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'ExtendReturnOrder',
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
          title: '销售订单管理'
        },
        {
          title: '数拓退货单'
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
        returnStatus: 0,
        departmentType: 3
      },
      dataList: [],
      // 审核弹框
      showAuditDialog: false,
      // 备注
      remark: '',
      // 审核当前点击的 row
      currentRow: {}
    }
  },
  activated() {
    let { reviewStatus } = this.$route.query
    if (typeof reviewStatus !== 'undefined') {
      this.query.returnStatus = Number(reviewStatus)
    }
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getReturnGoodsList(
        query.page,
        query.limit,
        query.orderNo,
        query.orderId,
        query.orderReturnNo,
        query.returnStatus,
        query.returnType,
        query.buyerEname,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.departmentType
      )
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
        returnStatus: 0,
        departmentType: 3
      }
    },
    //跳转详情界面
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'ExtendReturnOrderDetail',
        params: { id: row.id }
      })
    },
    // 审核点击
    auditClick(row) {
      this.currentRow = row
      this.remark = ''
      this.showAuditDialog = true
    },
    // 退货驳回
    async auditDialogLeftClick() {
      if (!this.remark.trim()) {
        this.$common.warn('请输入退货驳回备注')
      } else {
        this.$common.showLoad()
        let data = await returnOrderReject(this.currentRow.orderId, this.currentRow.id, this.remark.trim())
        this.$common.hideLoad()
        if (data && data.result) {
          this.showAuditDialog = false
          this.$common.n_success('驳回成功')
          this.getList()
        }
      }
    },
    // 审核通过点击
    async confirm() {
      if (this.currentRow.orderDetailVOList && this.currentRow.orderDetailVOList.length) {
        let array = []
        for (let i = 0; i < this.currentRow.orderDetailVOList.length; i++) {
          let item = this.currentRow.orderDetailVOList[i]
          let arr = []
          for (let i = 0; i < item.orderDeliveryList.length; i++) {
            let order = item.orderDeliveryList[i]
            arr.push({
              batchNo: order.batchNo,
              orderReturnDetailId: order.orderReturnDetailId,
              returnQuantity: order.returnQuantity
            })
          }
          array.push({
            detailId: item.id,
            goodsId: item.goodsId,
            orderDeliveryList: arr
          })
        }
        this.$common.showLoad()
        let data = await checkOrderReturn(this.currentRow.orderId, this.currentRow.id, this.remark.trim(), array)
        this.$common.hideLoad()
        if (data && data.result) {
          this.showAuditDialog = false
          this.$common.n_success('审核成功')
          this.getList()
        }
      }
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
            desc: '部门类型',
            name: 'departmentType',
            value: query.departmentType || ''
          },
          {
            desc: '导出来源',
            name: 'queryType',
            value: 2
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
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
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.order-table-view {
  .table-cell {
    border-bottom: 1px solid #dddddd;
  }
}
</style>
