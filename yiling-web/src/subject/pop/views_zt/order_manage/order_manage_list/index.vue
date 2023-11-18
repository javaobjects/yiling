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
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderStatus" v-show="item.value != 10 && item.value != 100" :key="item.value" :label="item.label" :value="item.value"></el-option>
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
            <el-col :span="6">
              <div class="title">支付状态</div>
              <el-select v-model="query.paymentStatus" placeholder="请选择支付状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderPayStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">支付方式</div>
              <el-select v-model="query.paymentMethod" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in payType" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">订单类型</div>
              <el-select v-model="query.orderType" placeholder="请选择订单类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderType" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">订单来源</div>
              <el-select v-model="query.orderSource" placeholder="请选择订单来源">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderSource" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">地域查询</div>
              <div class="flex-row-left">
                <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
              </div>
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出销售订单</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp1">导出采购订单</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table ref="table" :list="dataList" :total="query.total" :row-class-name="() => 'table-row'" :cell-class-name="getCellClass" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="订单信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.buyerEname }}<span style="font-weight: 400;" class="font-size-base font-title-color" v-show="row.buyerAddress">（{{ row.buyerAddress }}）</span></div>
                <div class="item-text font-size-base font-title-color"><span>订单编号：</span>{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color"><span>下单时间：</span>{{ row.createTime | formatDate }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单状态：</span>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单来源：</span>{{ row.orderSource | dictLabel(orderSource) }}</div>
                <div class="item-text font-size-base font-title-color"><span>订单ID：</span>{{ row.id }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付信息" min-width="210" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付状态：</span>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>
                <div class="item-text font-size-base font-title-color"><span>是否有满赠：</span>{{ row.haveGiftFlag == 1 ? '是' : '否' }}</div>
                <div class="item-text font-size-base font-title-color"><span>活动方式：</span>{{ row.activityType | dictLabel(activityType) }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="相关金额信息" min-width="240" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>货款总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>优惠总金额：</span>{{ row.discountAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>使用优惠券：</span>{{ row.couponActivityInfo || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品信息" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <div><span>购买商品：</span>{{ row.goodsOrderNum || '- -' }}种商品，数量{{ row.goodsOrderPieceNum || '- -' }}</div>
                </div>
                <div class="item-text font-size-base font-title-color">
                  <div v-show="row.deliveryOrderNum"><span>发货商品：</span>{{ row.deliveryOrderNum || '- -' }}种商品，数量{{ row.deliveryOrderPieceNum || '- -' }}</div>
                </div>
                <div class="item-text font-size-base font-title-color">
                  <div v-show="row.receiveOrderNum"><span>签收商品：</span>{{ row.receiveOrderNum || '- -' }}种商品，数量{{ row.receiveOrderPieceNum || '- -' }}</div>
                </div>
                <div class="item-text font-size-base font-title-color">
                  <div><span>供应商：</span>{{ row.sellerEname || '- -' }}</div>
                </div>
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
import { orderEnterpriseList } from '@/subject/pop/api/zt_api/order'
import { createDownLoad } from '@/subject/pop/api/common'
import { ylChooseAddress } from '@/subject/pop/components'
import { paymentMethod, orderStatus, orderPayStatus, orderSource } from '@/subject/pop/utils/busi'
import { activityType, orderType } from '@/subject/pop/busi/zt/order'

export default {
  name: 'ZtOrderManageList',
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
    // 来源
    orderSource() {
      return orderSource()
    },
    // 活动方式
    activityType() {
      return activityType()
    },
    // 订单类型
    orderType() {
      return orderType()
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
          title: '企业订单'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        name: '',
        // 订单号
        orderNo: '',
        // 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
        orderStatus: 0,
        // 支付状态：1-待支付 2-已支付
        paymentStatus: 0,
        // 支付方式：1-线下支付 2-账期 3-预付款
        paymentMethod: 0,
        // 订单类型：1-POP订单,2-B2B订单
        orderType: 0,
        // 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
        orderSource: 0,
        //省
        provinceCode: '',
        //市
        cityCode: '',
        //区
        regionCode: '',
        // 订单类型：1-销售订单 2-采购订单
        type: 1,
        createTime: []
      },
      dataList: [],
      tabList: [
        {
          label: '销售订单',
          value: 1
        },
        {
          label: '采购订单',
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
      let data = await orderEnterpriseList(
        query.page,
        query.limit,
        query.name,
        query.orderNo,
        query.orderStatus,
        query.paymentStatus,
        query.paymentMethod,
        query.orderType,
        query.orderSource,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
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
        orderStatus: 0,
        paymentStatus: 0,
        paymentMethod: 0,
        orderType: 0,
        orderSource: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        type: type,
        createTime: []
      }
    },
    //跳转详情界面
    showDetail(row, type) {
      if (type === 1) {
        // 跳转详情
        this.$router.push(`/zt_order_manage/zt_order_manage_detail/${row.id}`);
      }
    },
    // 下载模板
    async downLoadTemp() {
      if (!this.hasExport) {
        this.$common.warn('请搜索再进行导出')
        return
      }
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderEnterpriseSellExportServic',
        fileName: '销售订单',
        groupName: '销售订单导出',
        menuName: '企业订单-订单列表',
        searchConditionList: [
          {
            desc: '商业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus
          },
          {
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
          },
          {
            desc: '支付方式',
            name: 'paymentMethod',
            value: query.paymentMethod
          },
          {
            desc: '订单类型',
            name: 'orderType',
            value: query.orderType
          },
          {
            desc: '订单来源',
            name: 'orderSource',
            value: query.orderSource
          },
          {
            desc: '省',
            name: 'provinceCode',
            value: query.provinceCode
          },
          {
            desc: '市',
            name: 'cityCode',
            value: query.cityCode
          },
          {
            desc: '区',
            name: 'regionCode',
            value: query.regionCode
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
    // 下载明细
    async downLoadTemp1() {
      if (!this.hasExport) {
        this.$common.warn('请搜索再进行导出')
        return
      }
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderEnterpriseBuyerExportService',
        fileName: '采购订单',
        groupName: '采购订单导出',
        menuName: '企业订单-订单列表',
        searchConditionList: [
          {
            desc: '商业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '订单号',
            name: 'orderNo',
            value: query.orderNo || ''
          },
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus
          },
          {
            desc: '支付状态',
            name: 'paymentStatus',
            value: query.paymentStatus
          },
          {
            desc: '支付方式',
            name: 'paymentMethod',
            value: query.paymentMethod
          },
          {
            desc: '订单类型',
            name: 'orderType',
            value: query.orderType
          },
          {
            desc: '订单来源',
            name: 'orderSource',
            value: query.orderSource
          },
          {
            desc: '省',
            name: 'provinceCode',
            value: query.provinceCode
          },
          {
            desc: '市',
            name: 'cityCode',
            value: query.cityCode
          },
          {
            desc: '区',
            name: 'regionCode',
            value: query.regionCode
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
