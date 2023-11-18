<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入商品名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">终端服务商</div>
              <el-select v-model="query.eid" filterable remote :remote-method="remoteMethod" :loading="searchLoading" placeholder="请搜索并选择终端服务商">
                <el-option
                  v-for="item in providerOptions"
                  :key="item.id"
                  :label="item.ename"
                  :value="item.eid">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择订单状态">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in hmcOrderStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="6">
              <div class="title">收货人姓名</div>
              <el-input v-model="query.receiveUserName" placeholder="请输入收货人姓名" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">收货人电话</div>
              <el-input v-model="query.receiveUserTel" placeholder="请输入收货人电话" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">日期段</div>
              <el-date-picker
                v-model="query.timeRange"
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
        <div class="search-btn-box">
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
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="保单详情" min-width="550" align="left" prop="name">
            <template slot-scope="{ row }">
              <div class="insurancce-detail-wrap">
                <div class="title">平台单号：{{ row.orderNo }}</div>
                <div class="insurancce-detail">
                  <div class="list-col">
                    <div class="list-item"> 第三方单号：<span class="list-content"> {{ row.thirdConfirmNo || "- -" }} </span> </div>
                    <div class="list-item"> 对应保单号：<span class="list-content"> {{ row.policyNo }} </span> </div>
                  </div>
                  <div class="list-col">
                    <div class="list-item"> 商品名称 <span class="list-content"></span></div>
                    <div class="list-item" v-for="item in row.orderDetailList" :key="item.id"> {{ item.goodsName }}：<span class="list-content"> 数量{{ item.goodsQuantity }}</span></div>
                  </div>
                  <div class="list-col">
                    <div class="list-item"> 收货人姓名：<span class="list-content"> {{ row.orderRelateUser ? row.orderRelateUser.userName : "- -" }} </span> </div>
                    <div class="list-item"> 手机号：<span class="list-content"> {{ row.orderRelateUser ? row.orderRelateUser.userTel : "- -" }} </span> </div>
                    <div class="list-item"> 地址：
                      <span class="list-content"> {{ (row.orderRelateUser && row.orderRelateUser.provinceName) ? (row.orderRelateUser.provinceName + row.orderRelateUser.cityName + row.orderRelateUser.districtName + row.orderRelateUser.detailAddress) : "- -" }} </span>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="80" align="center" prop="loginStatus">
            <template slot-scope="{ row }">
              <span>{{ row.orderStatus | dictLabel(hmcOrderStatus) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="其他" min-width="120" align="left" prop="others">
            <template slot-scope="{ row }">
              <div class="insurancce-detail-wrap">
                <div class="insurancce-detail">
                  <div class="list-col">
                    <div class="list-item"> 创建时间：<span class="list-content"> {{ row.createTime | formatDate }} </span> </div>
                    <div class="list-item"> 理赔额：<span class="list-content"> {{ row.insuranceSettleAmount | toThousand("¥") }} </span> </div>
                    <div class="list-item"> 发货终端：<span class="list-content"> {{ row.ename }} </span> </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="80" align="center">
            <template slot-scope="{ row }">
                <yl-button type="text" @click="detail(row)">详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { enterpriseAccountList } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { insuranceGoodsOrderList } from '@/subject/admin/api/cmp_api/insurance_order_manage'
import { createDownLoad } from '@/subject/admin/api/common'
import { hmcOrderStatus } from '@/subject/admin/busi/cmp/insurance_order_manage'

export default {
  name: 'InsuranceGoodsOrder',
  components: {
  },
  computed: {
    // 订单状态
    hmcOrderStatus() {
      return hmcOrderStatus()
    }
  },
  filters: {
  },
  data() {
    return {
      providerOptions: [],
      query: {
        current: 1,
        size: 10,
        timeRange: [],
        goodsName: '',
        eid: '',
        orderStatus: '',
        receiveUserName: '',
        receiveUserTel: ''
      },
      // 列表
      dataList: [],
      loading: false,
      searchLoading: false
    }
  },
  activated() {
    this.remoteMethod('')
    this.getList()
  },
  methods: {
    // 搜索终端服务商
    async remoteMethod(query) {
      this.searchLoading = true
      let data = await enterpriseAccountList( 1, 10, query )
      this.searchLoading = false
      if (data) {
        this.providerOptions = data.records
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await insuranceGoodsOrderList(
        query.current,
        query.size,
        query.goodsName,
        query.eid,
        query.orderStatus,
        query.receiveUserName,
        query.receiveUserTel,
        query.timeRange && query.timeRange.length ? query.timeRange[0] : '',
        query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : ''
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        timeRange: [],
        goodsName: '',
        eid: '',
        orderStatus: '',
        receiveUserName: '',
        receiveUserTel: ''
      }
    },
    //  详情
    detail(row) {
      this.$router.push({
        name: 'GoodsOrderDetail',
        params: { id: row.id}
      })
    },
    //  导出
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcAdminOrderExportService',
        fileName: '导出兑保记录',
        groupName: '兑保记录导出',
        menuName: '订单管理-兑保记录',
        searchConditionList: [
          {
            desc: '订单状态',
            name: 'orderStatus',
            value: query.orderStatus || ''
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName || ''
          },
          {
            desc: '药品服务终端id',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '收货人姓名',
            name: 'receiveUserName',
            value: query.receiveUserName
          },
          {
            desc: '收货人手机号',
            name: 'receiveUserTel',
            value: query.receiveUserTel
          },

          {
            desc: '日期段-开始时间',
            name: 'startTime',
            value: query.timeRange && query.timeRange.length ? query.timeRange[0] : ''
          },
          {
            desc: '日期段-截止时间',
            name: 'stopTime',
            value: query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>