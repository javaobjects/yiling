<template>
  <div class="app-container">
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
              <div class="title">供应商名称</div>
              <el-input v-model="query.sellerEname" placeholder="请输入供应商名称" @keyup.enter.native="handleSearch"/>
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
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderStatus" v-show="item.value != 10 && item.value != 100" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">支付方式</div>
              <el-select v-model="query.payType" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in payType" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">支付状态</div>
              <el-select v-model="query.orderPayStatus" placeholder="请选择支付状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderPayStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">发货时间</div>
              <el-date-picker v-model="query.sendTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">订单来源</div>
              <el-select v-model="query.orderSource" placeholder="请选择订单来源">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderSource" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">活动方式</div>
              <el-select v-model="query.activityType" placeholder="请选择活动方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in activityType" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
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
            <el-col :span="6">
              <div class="title">订单类型</div>
              <el-select v-model="query.orderType" placeholder="请选择订单类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in orderType" :key="item.value" :label="item.label" :value="item.value"></el-option>
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="downLoadTemp1">导出明细</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table ref="table" :list="dataList" :total="query.total" :row-class-name="() => 'table-row'" :cell-class-name="getCellClass" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column type="expand" width="1">
            <template slot-scope="{ row }">
              <div class="expand-row border-1px-b" v-show="row.goodOrderList && row.goodOrderList.length">
                <div class="expand-view" v-for="(item, index) in row.goodOrderList" :key="index">
                  <div class="title flex-row-left">{{ item.goodsName }}<span v-if="item.promotionActivityType" class="activity-type">{{ item.promotionActivityType | dictLabel(typeArray) }}</span></div>
                  <div class="desc-box flex-row-left">
                    <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                    <div class="right-text box1">
                      <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                      <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                      <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification + '*' + item.packageNumber + (item.goodsRemark ? ` (${item.goodsRemark}) ` : '') }}</div>
                    </div>
                    <div class="right-text box2">
                      <div class="text-item"><span class="font-title-color">购买数量：</span>{{ item.goodsQuantity }}</div>
                      <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                      <div class="text-item"></div>
                    </div>
                    <div class="right-text">
                      <div class="text-item"><span class="font-title-color">商品单价：</span>{{ item.goodsPrice | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">金额小计：</span>{{ item.goodsAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">优惠金额：</span>{{ item.discountAmount | toThousand('￥') }}</div>
                      <div class="text-item"><span class="font-title-color">支付金额：</span>{{ item.realAmount | toThousand('￥') }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
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
            <template slot-scope="{ row, $index }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>原价总金额：</span>{{ row.originalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>成交价总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>优惠总金额：</span>{{ row.discountAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                <div class="item-text font-size-base font-title-color"><span>使用优惠券：</span>{{ row.couponActivityInfo || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
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
import { getOrderList } from '@/subject/admin/api/zt_api/order'
import { createDownLoad } from '@/subject/admin/api/common'
import { ylChooseAddress } from '@/subject/admin/components'
import { paymentMethod, orderStatus, orderPayStatus, orderSource } from '@/subject/admin/utils/busi'
import { activityType, orderType } from '@/subject/admin/busi/zt/order'

export default {
  name: 'OrderManageList',
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
          path: '/dashboard'
        },
        {
          title: '订单管理'
        },
        {
          title: '订单列表'
        }
      ],
      // 活动类型
      typeArray: [
        {
          label: '特价',
          value: 2
        },
        {
          label: '秒杀',
          value: 3
        },
        {
          label: '套装',
          value: 4
        },
        {
          label: '预售',
          value: 6
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        orderStatus: 0,
        sendTime: [],
        payType: 0,
        orderPayStatus: 0,
        orderSource: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        // 活动方式: 2-特价 3-秒杀
        activityType: 0,
        // 订单类型：1-POP订单,2-B2B订单
        orderType: 0
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
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getOrderList(
        query.page,
        query.limit,
        query.buyerEname,
        query.sellerEname,
        query.orderNo,
        query.orderStatus,
        query.payType,
        query.orderPayStatus,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.sendTime && query.sendTime.length ? query.sendTime[0] : undefined,
        query.sendTime && query.sendTime.length > 1 ? query.sendTime[1] : undefined,
        query.orderSource,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.activityType,
        query.orderType
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
        sendTime: [],
        orderSource: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        activityType: 0,
        orderType: 0
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
        this.$router.push(`/order_manage/order_manage_detail/${row.id}`);
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'orderB2BCenterAdminExportService',
        fileName: 'orderB2BCenterAdminExport',
        groupName: '运营后台订单列表',
        menuName: '订单管理-订单列表',
        searchConditionList: [
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
          },
          {
            desc: '发货开始时间',
            name: 'startCreateTime',
            value: query.sendTime && query.sendTime.length ? query.sendTime[0] : ''
          },
          {
            desc: '发货结束时间',
            name: 'endCreateTime',
            value: query.sendTime && query.sendTime.length > 1 ? query.sendTime[1] : ''
          },
          {
            desc: '来源',
            name: 'orderSource',
            value: query.orderSource
          },
          {
            desc: '活动方式',
            name: 'activityType',
            value: query.activityType
          },
          {
            desc: '订单类型',
            name: 'orderType',
            value: query.orderType
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
        className: 'orderB2BCenterDetailAdminExportService',
        fileName: 'orderB2BCenterDetailAdminExport',
        groupName: '运营后台订单明细列表',
        menuName: '订单管理-订单明细列表',
        searchConditionList: [
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
          },
          {
            desc: '发货开始时间',
            name: 'startCreateTime',
            value: query.sendTime && query.sendTime.length ? query.sendTime[0] : ''
          },
          {
            desc: '发货结束时间',
            name: 'endCreateTime',
            value: query.sendTime && query.sendTime.length > 1 ? query.sendTime[1] : ''
          },
          {
            desc: '来源',
            name: 'orderSource',
            value: query.orderSource
          },
          {
            desc: '活动方式',
            name: 'activityType',
            value: query.activityType
          },
          {
            desc: '订单类型',
            name: 'orderType',
            value: query.orderType
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
