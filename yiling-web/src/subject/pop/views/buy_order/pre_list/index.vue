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
              <div class="title">预订单编号</div>
              <el-input v-model="query.orderId" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="10">
              <div class="title">下单时间</div>
              <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">审核状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderPreStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">订单ID</div>
              <el-input v-model="query.id" placeholder="请输入订单ID" @keyup.enter.native="handleSearch"/>
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
          <ylButton v-role-btn="[6]" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table ref="table" :list="dataList" :total="query.total" :row-class-name="() => 'table-row'" :cell-class-name="getCellClass" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column type="expand" width="1">
            <template slot-scope="{ row }">
              <div class="expand-row border-1px-b" v-show="row.orderDetailList && row.orderDetailList.length">
                <div class="expand-view" v-for="(item, index) in row.orderDetailList" :key="index">
                  <div class="title">{{ item.goodsName }}</div>
                  <div class="desc-box">
                    <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                    <div class="right-text box1">
                      <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                      <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                      <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                    </div>
                    <div class="right-text box2">
                      <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                      <div class="text-item text-item-red"><span class="font-title-color font-title-red">购买件数：</span>{{ item.goodsQuantity/item.packageNumber }}件</div>
                      <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                      <!-- <div class="text-item"></div> -->
                    </div>
                    <div class="right-text">
                      <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">折扣金额：</span>{{ item.discountAmount | toThousand('￥-') }}</div>
                      <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="320" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.distributorEname }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.id }}</div>
                <div class="item-text font-size-base font-title-color"><span>预订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>下单时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付信息" min-width="210" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>审核状态：</span>{{ row.auditStatus | dictLabel(orderPreStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>审核人：</span>{{ row.auditUserName }}</div>
                <div class="item-text font-size-base font-title-color"><span>审核时间：</span>{{ row.auditTime | formatDate }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="相关金额信息" min-width="240" align="left">
            <template slot-scope="{ row, $index }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base"><span>货款总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base"><span>折扣总金额：</span>{{ row.discountAmount | toThousand('￥-') }}</div>
                <div class="item-text font-size-base"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                <div class="expand-item flex-row-left">
                  <div class="expand" @click="showExpand(row, $index)">
                    <span v-show="!row.show">共{{ row.goodsOrderNum }}件/查看更多</span>
                    <span v-show="row.show">点击收起</span>
                    <el-icon class="el-icon-arrow-down" :class="row.show === true ? 'show' : ''"></el-icon>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>购买商品：</span>{{ row.goodsOrderNum || '- -' }}种商品，数量{{ row.goodsOrderPieceNum || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div>
                <yl-button v-role-btn="[1]" type="text" v-if="row.auditStatus === 1" @click="showDetail(row, 4)">选择支付方式</yl-button>
              </div>
              <div>
                <yl-button v-role-btn="[4]" type="text" @click="showDetail(row, 1)">查看订单详情</yl-button>
              </div>
              <div>
                <yl-button v-role-btn="[5]" type="text" v-if="row.auditStatus === 3 || row.auditStatus === 4" @click="showDetail(row, 2)">查看审核详情</yl-button>
              </div>
              <div>
                <yl-button v-role-btn="[3]" type="text" v-if="row.auditStatus === 4" @click="showDetail(row, 3)">订单编辑</yl-button>
              </div>
              <div>
                <yl-button v-role-btn="[2]" type="text" v-if="row.auditStatus === 1 || row.auditStatus === 2" @click="orderCancle(row)">取消订单</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog title="审核详情" :show-footer="false" click-right-btn-close :visible.sync="showDialog">
        <div class="order-review-box">
          <div class="head-box">
            <div class="font-size-base font-important-color flex-row-left box">
              <div class="font-title-color flex1"><span>预订单编号：</span>{{ showRow.orderNo || '- -' }}</div>
              <div class="font-title-color flex1"><span>审核状态：</span>{{ $options.filters.dictLabel(showRow.auditStatus, orderPreStatus) }}</div>
            </div>
            <div class="font-size-base font-important-color flex-row-left">
              <div class="font-title-color flex1"><span>审核人：</span>{{ showRow.auditUserName || '- -' }}</div>
              <div class="font-title-color flex1"><span>审核时间：</span>{{ showRow.auditTime | formatDate }}</div>
            </div>
          </div>
          <div class="bottom-box font-size-base font-important-color">
            <div class="title">驳回原因</div>
            <div class="desc">{{ showRow.auditRejectReason }}</div>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getPreOrderList,
  preOrderCancle
} from '@/subject/pop/api/order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderPreStatus, orderPayStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'BuyOrderPreIndex',
  components: {
  },
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 订单审核状态
    orderPreStatus() {
      return orderPreStatus()
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
          path: '/dashboard'
        },
        {
          title: '采购订单管理'
        },
        {
          title: '预采购订单'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        orderStatus: 0
      },
      dataList: [],
      // 弹框
      showDialog: false,
      // 弹框展示信息
      showRow: {}
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPreOrderList(
        query.page,
        query.limit,
        query.name,
        query.orderId,
        query.id,
        query.orderStatus,
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
        orderStatus: 0
      }
    },
    // 打开关闭
    showExpand(row, index) {
      let myTable = this.$refs.table
      if (myTable) {
        myTable.$refs.table.toggleRowExpansion(row)
        this.dataList[index].show = !this.dataList[index].show
      }
    },
    //跳转详情界面
    showDetail(row, type) {
      if (type === 1) {
        // 跳转详情
        this.$router.push(`/buyOrder/buyOrder_pre_detail/${row.id}`)
      } else if (type === 2) {
        // 查看审核详情
        this.showRow = row
        this.showDialog = true
      } else if (type === 3) {
        // 编辑 跳转提交订单
        this.$common.goPackage(3, { orderNo: row.orderNo })
      } else if (type === 4) {
        // 查看支付方式 跳转前台收银台
        this.$common.goPackage(2, { ids: row.orderNo })
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderExpectExportService',
        fileName: 'orderExpectExport',
        groupName: '预订单列表',
        menuName: '采购订单-预订单列表',
        searchConditionList: [
          {
            desc: '供应商名称',
            name: 'distributorEname',
            value: query.name || ''
          },
          {
            desc: '预订单编号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '订单ID',
            name: 'id',
            value: query.id || ''
          },
          {
            desc: '审核状态',
            name: 'auditStatus',
            value: query.orderStatus
          },
          {
            desc: '下单开始时间',
            name: 'startCreatTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endCreatTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    getCellClass(row) {
      if (!row.row.show) {
        return 'border-1px-b'
      }
      return ''
    },
    // 取消订单
    orderCancle(row) {
      this.$common.confirm('取消后不可恢复，如果需要可以再次下单！是否确认取消该预订单？', async r => {
        if (r) {
          this.$common.showLoad()
          let data = await preOrderCancle(row.id)
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('取消订单成功')
            this.getList()
          }
        }
      })
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
