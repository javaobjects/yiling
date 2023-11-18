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
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单ID</div>
              <el-input v-model="query.id" placeholder="请输入订单ID" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">支付方式</div>
              <el-select v-model="query.payType" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in payType"
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
            <el-col :span="6">
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderStatus"
                  v-show="item.value !== 10 && item.value !== 100"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">支付状态</div>
              <el-select v-model="query.orderPayStatus" placeholder="请选择支付状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderPayStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  v-show="item.value != 4"
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
          <ylButton type="primary" plain v-role-btn="['5']" @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton type="primary" plain v-role-btn="['7']" @click="downLoadTemp1">导出明细</ylButton>
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
          <el-table-column type="expand" width="1">
            <template slot-scope="{ row }">
              <div class="expand-row border-1px-b" v-show="row.goodOrderList && row.goodOrderList.length">
                <div class="expand-view" v-for="(item, index) in row.goodOrderList" :key="index">
                  <div class="title">{{ item.goodsName }}</div>
                  <div class="desc-box">
                    <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                    <div class="right-text box1">
                      <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                      <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                      <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                      <div class="text-item"></div>
                    </div>
                    <div class="right-text box2">
                      <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                      <div class="text-item text-item-red"><span class="font-title-color font-title-red">购买件数：</span>{{ item.goodsQuantity/item.packageNumber }}件</div>
                      <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                      <div class="text-item"></div>
                      <div class="text-item"></div>
                    </div>
                    <div class="right-text">
                      <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
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
                <div class="title font-size-lg bold clamp-t-1">{{ row.buyerEname }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.id }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>下单时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>销售组织：</span>{{ row.sellerEname }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付信息" min-width="210" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                 <div class="item-text font-size-base font-title-color"><span>订单状态：</span>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付状态：</span>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="相关金额信息" min-width="240" align="left">
            <template slot-scope="{ row, $index }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>货款总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>折扣总金额：</span>{{ row.discountAmount | toThousand('￥-') }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
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
                <div class="item-text font-size-base font-title-color"><span>发货商品：</span>{{ row.deliveryOrderNum || '- -' }}种商品，数量{{ row.deliveryOrderPieceNum || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>签收商品：</span>{{ row.receiveOrderNum || '- -' }}种商品，数量{{ row.receiveOrderPieceNum || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div><yl-button type="text" v-role-btn="['4']" @click="showDetail(row, 1)">查看详情</yl-button></div>
              <div><yl-button type="text" v-role-btn="['6']" v-if="row.judgeDeliveryFlag" @click="showDetail(row, 2)">发货</yl-button></div>
              <div><yl-button type="text" v-if="row.judgeCancelFlag" v-role-btn="['2']" @click="cancleDidClick(row)">取消订单</yl-button></div>
              <div><yl-button type="text" v-role-btn="['44']" v-show="row.orderReturnFlag" @click="showDetail(row, 3)">查看退货详情</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getOrderList,
  orderCancle
} from '@/subject/pop/api/order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderStatus, orderPayStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'SellOrderList',
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
          path: '/dashboard'
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
        orderPayStatus: 0,
        departmentType: 5
      },
      dataList: [],
      // 渠道商弹框
      showDialog: false,
      // 查看发票信息
      showTicket: false,
      // 发票细心
      ticketInfo: {}
    };
  },
  activated() {
    let { orderStatus } = this.$route.query
    if (typeof orderStatus !== 'undefined') {
      this.query.orderStatus = Number(orderStatus)
    }
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getOrderList(
        query.page,
        query.limit,
        1,
        query.name,
        query.orderNo,
        query.id,
        query.orderStatus,
        query.payType,
        query.orderPayStatus,
        undefined,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.departmentType
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
        orderPayStatus: 0,
        departmentType: 5
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
        this.$router.push({
          name: 'SellOrderDetail',
          params: { id: row.id }
        });
      } else if (type === 2) {
        // 发货界面
        this.$router.push({
          name: 'SellOrderSend',
          params: { id: row.id }
        });
      } else if (type === 3) {
        // 退货列表界面
        this.$router.push({
          name: 'GoodsReturnList'
        })
      }
    },
    // 取消订单
    async cancleDidClick(row) {
      this.$common.showLoad()
      let data = await orderCancle(row.id)
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('取消成功')
        this.getList()
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderSaleExportService',
        fileName: 'orderSaleExport',
        groupName: '销售订单列表',
        menuName: '销售订单-订单列表',
        searchConditionList: [
          {
            desc: '部门类型',
            name: 'departmentType',
            value: query.departmentType || ''
          },
          {
            desc: '采购商名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '订单ID',
            name: 'id',
            value: query.id || ''
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
            value: query.orderPayStatus
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
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 下载导出结果明细
    async downLoadTemp1() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderSaleDetailExportService',
        fileName: 'orderSaleDetailExport',
        groupName: '销售订单列表明细',
        menuName: '销售订单-订单列表明细',
        searchConditionList: [
          {
            desc: '部门类型',
            name: 'departmentType',
            value: query.departmentType || ''
          },
          {
            desc: '采购商名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '订单ID',
            name: 'id',
            value: query.id || ''
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
            value: query.orderPayStatus
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
      if (data && data.result) {
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
