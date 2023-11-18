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
              <el-input v-model="query.buyerEname" placeholder="请输入采购商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单编号</div>
              <el-input v-model="query.orderId" placeholder="请输入订单编号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单ID</div>
              <el-input v-model="query.id" placeholder="请输入订单ID" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">发票编号</div>
              <el-input v-model="query.invoiceNo" placeholder="请输入发票编号" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">发票申请时间</div>
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
              <div class="title">销售组织</div>
              <el-select v-model="query.distributorEid" placeholder="请选择销售组织">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in mainPartList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">支付方式</div>
              <el-select v-model="query.payType" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in payType"
                  v-show="item.value != 1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">发票状态</div>
              <el-select v-model="query.ticketStatus" placeholder="请选择发票状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderTicketStatus"
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
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderStatus"
                  v-show="item.value !== -10 && item.value !== 10 && item.value !== 20 && item.value !== 100"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.orderTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="12">
              <div class="title">采购商区域</div>
              <div class="flex-row-left">
                <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
              </div>
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
          <ylButton type="primary" v-role-btn="['5']" plain @click="downLoadTemp">导出查询结果</ylButton>
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
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="340" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.buyerEname }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.id }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>销售组织：</span>{{ row.distributorEname }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单状态：</span>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额信息" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>货款总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>折扣总金额：</span>{{ row.discountAmount | toThousand('￥-') }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发票状态" min-width="150" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"></div>
                <div class="item-text font-size-base font-title-color">{{ row.invoiceStatus | dictLabel(orderTicketStatus) }}</div>
                <div class="item-text font-size-base font-title-color"></div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发票信息" min-width="240" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color"><span>发票编号：</span>{{ row.invoiceNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>已开票金额：</span>{{ row.invoiceFinishAmount | toThousand('￥') }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div><yl-button type="text" v-role-btn="['1']" v-show="row.invoiceStatus == 1" @click="editTicketClick(row, 1)">申请发票</yl-button></div>
              <div><yl-button type="text" v-role-btn="['4']" @click="showOrderTicket(row)">查看发票详情</yl-button></div>
              <div><yl-button type="text" v-role-btn="['44']" @click="showDetail(row, 1)">查看订单详情</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 发票信息 -->
      <yl-dialog
        title="发票信息"
        click-right-btn-close
        :visible.sync="showTicket">
        <div class="order-ticket-check" v-if="ticketInfo.id">
          <div class="order-content">
            <div class="font-size-base font-important-color flex-row-left mar-b-8">
              <div class="font-title-color flex1"><span>订单编号：</span>{{ ticketInfo.orderNo || '- -' }}</div>
              <div class="font-title-color flex1"><span>发票状态：</span>{{ ticketInfo.status ? $options.filters.dictLabel(ticketInfo.status, orderTicketStatus) : '- -' }}</div>
            </div>
            <div class="font-size-base font-important-color flex-row-left mar-b-10">
              <div class="font-title-color flex1"><span>发票申请人：</span>{{ ticketInfo.createUserName || '- -' }}</div>
              <div class="font-title-color flex1"><span>发票申请时间：</span>{{ ticketInfo.createTime | formatDate }}</div>
            </div>
          </div>
          <yl-table
            border
            :show-header="true"
            :list="ticketInfo.orderInvoiceInfo">
            <el-table-column label="发票单号" prop="invoiceNo" align="center">
            </el-table-column>
          </yl-table>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getInvoiceList,
  getAgreementMainPartList
} from '@/subject/pop/api/order';
import { createDownLoad } from '@/subject/pop/api/common'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'

export default {
  name: 'InvoiceList',
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
    // 发票
    orderTicketStatus() {
      return orderTicketStatus()
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
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        distributorEid: 0,
        createTime: [],
        payType: 0,
        ticketStatus: 0,
        orderStatus: 0,
        departmentType: 5,
        // 下单时间
        orderTime: []
      },
      dataList: [],
      // 查看发票信息
      showTicket: false,
      // 发票信息
      ticketInfo: {},
      // 销售组织公司
      mainPartList: []
    };
  },
  activated() {
    // 获取销售组织
    this.getMainPartList()
    this.getList()
  },
  methods: {
    async getMainPartList() {
      let data = await getAgreementMainPartList()
      if (data) {
        this.mainPartList = data.list || []
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getInvoiceList(
        query.page,
        query.limit,
        query.buyerEname,
        query.distributorEid,
        query.invoiceNo,
        query.orderId,
        query.id,
        query.ticketStatus,
        query.orderStatus,
        query.payType,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.departmentType,
        // 下单时间
        query.orderTime && query.orderTime.length ? query.orderTime[0] : undefined,
        query.orderTime && query.orderTime.length > 1 ? query.orderTime[1] : undefined,
        query.provinceCode,
        query.cityCode,
        query.regionCode
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
        ticketStatus: 0,
        departmentType: 5
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
      }
    },
    // 跳转申请发票界面
    editTicketClick(row, type) {
      // type: 1- 申请发票  2- 修改发票
      this.$router.push({
        name: 'InvoiceApply',
        params: { id: row.id, type: type}
      });
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderInvoiceExportService',
        fileName: 'orderInvoiceExport',
        groupName: '发票管理列表',
        menuName: '销售订单-发票管理',
        searchConditionList: [
          {
            desc: '部门类型',
            name: 'departmentType',
            value: query.departmentType || ''
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '订单编号',
            name: 'orderNo',
            value: query.orderId || ''
          },
          {
            desc: '订单ID',
            name: 'id',
            value: query.id || ''
          },
          {
            desc: '发票编号',
            name: 'invoiceNo',
            value: query.invoiceNo || ''
          },
          {
            desc: '销售组织',
            name: 'distributorEid',
            value: query.distributorEid
          },
          {
            desc: '支付方式',
            name: 'paymentMethod',
            value: query.payType
          },
          {
            desc: '发票状态',
            name: 'invoiceStatus',
            value: query.ticketStatus
          },
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus
          },
          {
            desc: '发票申请开始时间',
            name: 'startInvoiceTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '发票申请结束时间',
            name: 'endInvoiceTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          },
          {
            desc: '下单开始时间',
            name: 'startOrderTime',
            value: query.orderTime && query.orderTime.length ? query.orderTime[0] : ''
          },
          {
            desc: '下单结束时间',
            name: 'endOrderTime',
            value: query.orderTime && query.orderTime.length > 1 ? query.orderTime[1] : ''
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
    // 查看发票详情
    async showOrderTicket(row) {
      this.$router.push({
        name: 'InvoiceInfoList',
        params: { id: row.id }
      });
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
