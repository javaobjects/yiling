<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>

    <div class="app-container-content">
      <div class="tab">
        <span class="tab-item $font-size-large bold">待处理</span>
      </div>
      <div class="common-box  mar-t-8">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">日期段</div>
              <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">订单编号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单编号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">配送状态</div>
              <el-select v-model="query.deliverType" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in hmcDeliveryStatus" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出</ylButton>
        </div>
      </div>
      <!-- table  -->
      <div class="my-table mar-t-8">
        <yl-table 
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="保单详情" align="left" min-width="180">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="item-text font-size-lg bold clamp-t-1 title">平台单号：{{ row.orderNo }}</div>
                <div class="item-text font-size-base font-title-color">第三方单号：<span class="font-important-color">{{ row.thirdConfirmNo ? row.thirdConfirmNo : '- -' }}</span></div>
                <div class="item-text font-size-base font-title-color">对应保单号：<span class="font-important-color">{{ row.policyNo }}</span></div>
                <div class="item-text font-size-base font-title-color">发货终端：<span class="font-important-color">{{ row.ename }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color">商品名称<span class="font-important-color"></span></div>
                <div class="item-text font-size-base font-title-color" v-for="item in row.orderDetailList" :key="item.id">{{ item.goodsName }}：<span class="font-important-color">【数量{{ item.goodsQuantity }}】</span></div>
                <div class="item-text font-size-base font-title-color">理赔额：<span class="font-important-color">{{ row.insuranceSettleAmount | toThousand('￥') }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color">收货人姓名：<span class="font-important-color">{{ row.orderRelateUser?row.orderRelateUser.userName:'--' }}</span></div>
                <div class="item-text font-size-base font-title-color">手机号：<span class="font-important-color">{{ row.orderRelateUser?row.orderRelateUser.userTel:'--' }}</span></div>
                <div class="item-text font-size-base font-title-color">地址：<span class="font-important-color">{{ row.orderRelateUser?row.orderRelateUser.provinceName:'--' }}{{ row.orderRelateUser?row.orderRelateUser.cityName:'--' }}{{ row.orderRelateUser?row.orderRelateUser.districtName:'--' }}{{ row.orderRelateUser?row.orderRelateUser.detailAddress:'--' }}</span></div>
                <div class="item-text font-size-base font-title-color"><span class="font-important-color"></span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" min-width="60">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base"><span>{{ row.orderStatus | dictLabel(orderStatus) }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.createTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" min-width="80">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color">
                  <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">查看</yl-button>
                  <!-- 订单状态:1-待确认/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退	 -->
                  <yl-button v-role-btn="['3']" v-if="row.orderStatus == 3 || row.orderStatus == 4 " type="text" @click="edit(row)">处理</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getPeddingOrderList } from '@/subject/pop/api/cmp_api/order'
import { hmcOrderStatus, hmcDeliveryStatus } from '@/subject/pop/utils/busi'
import { createDownLoad } from '@/subject/pop/api/common'
export default {
  name: 'CmpPendingOrder',
  components: {
  },
  computed: {
    orderStatus() {
      return hmcOrderStatus()
    },
    hmcDeliveryStatus() {
      return hmcDeliveryStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理',
          path: ''
        },
        {
          title: '药品待处理订单'
        }
      ],
      query: {
        page: 1,
        limit: 20,
        orderNo: '',
        createTime: [],
        deliverType: 0
      },
      dataList: [],
      loading: false,
      total: 0

    }
  },
  activated() {
    this.getList()
  },
  created() {
  },
  mounted() {
  },
  methods: {
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPeddingOrderList(
        query.page,
        query.limit,
        query.deliverType,// 配送状态
        query.orderNo,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length ? query.createTime[1] : undefined,
        // 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
        // 1345 是待处理状态
        [1, 3, 4, 5]
      )
      this.loading = false
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },

    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        orderNo: '',
        status: 0,
        createTime: []
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      if (this.dataList.length == 0) return this.$message.warning('请搜索数据再导出')
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcAdminOrderExportService',
        fileName: '导出兑保记录',
        groupName: '兑保记录导出',
        menuName: '兑保记录',
        searchConditionList:
          [
            {
              desc: '订单编号',
              name: 'orderNo',
              value: query.orderNo || ''
            },
            {
              desc: '日期段-开始时间',
              name: 'startTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[0] : ''
            },
            {
              desc: '日期段-截止时间',
              name: 'stopTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
            },
            {
              desc: '配送状态',
              name: 'deliverType',
              value: query.deliverType || ''
            },
            {
              desc: '待处理状态',
              name: 'orderStatusStr',
              value: '1,3,4,5'
            }

          ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    showDetail(e) {
      this.$router.push({
        name: 'CmpOrderDetail',
        params: {
          id: e.id,
          type: 2
        }
      });
    },
    edit(e) {
      this.$router.push({
        name: 'CmpChoiceSendGoods',
        params: {
          id: e.id,
          type: 2
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
