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
            <span>待审核订单数</span>
          </div>
          <div class="title">
            {{ totalData.reviewingNumber }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>已审核订单数</span>
          </div>
          <div class="title">
            {{ totalData.reviewNumber }}
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="year_order"></svg-icon>
            <span>驳回订单数</span>
          </div>
          <div class="title">
            {{ totalData.rejectNumber }}
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" placeholder="请输入供应商名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderId" placeholder="请输入订单号" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">订单ID</div>
              <el-input v-model="query.id" placeholder="请输入订单ID" @keyup.enter.native="handleSearch"/>
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
            <el-col :span="6">
              <div class="title">审核状态</div>
              <el-select v-model="query.auditStatus" placeholder="请选择订单状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderPreStatus"
                  v-show="item.value != 1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">地域查询</div>
              <div class="flex-row-left">
                <yl-choose-address
                  :province.sync="query.provinceCode"
                  :city.sync="query.cityCode"
                  :area.sync="query.regionCode"
                  is-all
                />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商务负责人</div>
              <el-input v-model="query.contacterName" placeholder="请输入商务负责人" @keyup.enter.native="handleSearch"/>
            </el-col>
            <!-- 企业类型为工业时显示归属部门 -->
            <el-col :span="6" v-if="currentEnterpriseInfo && currentEnterpriseInfo.type === 1">
              <div class="title">归属部门</div>
              <el-select
                v-model="query.departmentIdCode"
                ref="selectDept"
                placeholder="请选择部门">
                <el-option :value="query.departmentIdCode" :label="query.parentName" style="height: auto; padding: 0;">
                  <department-tree
                    node-key="id"
                    :current-node-key="query.departmentIdCode"
                    :props="{ children: 'children', label: 'name' }"
                    @node-click="handleNodeQueryClick">
                  </department-tree>
                </el-option>
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
      <!-- 底部列表 -->
      <div class="mar-t-16">
        <yl-table
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :row-class-name="() => 'mar-b-16'"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session">
                  <div class="left">订单编号：{{ row.orderNo }}</div>
                  <div class="center"><span>下单时间：{{ row.createTime | formatDate }}</span></div>
                  <div class="center">订单ID：{{ row.id }}</div>
                  <div class="center">商务负责人：{{ row.contacterName }}</div>
                  <div class="center">归属部门：{{ row.departmentName }}</div>
                  <div class="right">审核状态：<span class="col-warn">{{ row.auditStatus | dictLabel(orderPreStatus) }}</span></div>
                </div>
                <div class="content flex-row-left">
                  <div class="content-left">
                    <div>{{ row.buyerEname }}</div>
                    <div class="font-size-base font-title-color" v-show="row.buyerAddress">({{ row.buyerAddress }})</div>
                    <!-- <div class="item font-size-base font-title-color">合同编号：{{ row.contractNumber }}</div> -->
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="item">
                      <span class="font-title-color">合同编号：</span>{{ row.contractNumber }}
                    </div>
                    <div class="item"><span class="font-title-color">支付方式：</span>{{ row.paymentMethod | dictLabel(payType) }}</div>
                    <div class="item"><span class="font-title-color">支付状态：</span>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>

                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="item"><span class="font-title-color">货款总金额：</span>{{ row.totalAmount | toThousand('￥') }}</div>
                    <div class="item"><span class="font-title-color">折扣总金额：</span>{{ row.discountAmount | toThousand('￥-') }}</div>
                    <div class="item"><span class="font-title-color">支付总金额：</span>{{ row.paymentAmount | toThousand('￥') }}</div>
                  </div>
                  <div class="content-center-1 font-size-base font-important-color flex1">
                    <div class="item"><span class="font-title-color">购买商品：</span>{{ row.goodsOrderNum || '- -' }}种商品，数量{{ row.goodsOrderPieceNum || '- -' }}</div>
                    <div class="item remark"><span class="font-title-color">采购商备注：</span>{{ row.orderNote }}</div>
                    <div class="item"></div>
                  </div>
                  <div class="content-right flex-column-center">
                    <!-- <yl-button v-role-btn="['1']" v-show="row.auditStatus == 2" type="text" @click="showDetail(row, 1)">审核订单</yl-button> -->
                    <yl-button v-role-btn="['1']" v-show="row.orderManageDisableVO.checkDisable" type="text" @click="showDetail(row, 1)">审核订单</yl-button>
                    <yl-button v-role-btn="['2']" v-show="row.auditStatus == 3 || row.auditStatus == 4" type="text" @click="showDetail(row, 2)">查看预订单详情</yl-button>
                  </div>
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
import {
  getOrderReviewNum,
  getOrderReviewList
} from '@/subject/pop/api/order';
import { ylChooseAddress } from '@/subject/pop/components';
import { paymentMethod, orderPayStatus, orderPreStatus } from '@/subject/pop/utils/busi'
import departmentTree from '../component/DepartmentTree'
import { mapGetters } from 'vuex'

export default {
  name: 'ReviewOrderIndex',
  components: {
    ylChooseAddress,
    departmentTree
  },
  computed: {
    // 企业信息
    ...mapGetters(['currentEnterpriseInfo']),
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 支付状态
    orderPayStatus() {
      return orderPayStatus()
    },
    // 审核状态
    orderPreStatus() {
      return orderPreStatus()
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
          title: '订单审核管理'
        },
        {
          title: '订单审核列表'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        createTime: [],
        payType: 0,
        auditStatus: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        departmentType: 2,
        // 商务负责人
        contacterName: ''
      },
      dataList: [],
      // 汇总数据
      totalData: {}
    };
  },
  activated() {
    this.getList()
    this.getTotal()
  },
  methods: {
    async getTotal() {
      let data = await getOrderReviewNum(2)
      if (data) {
        this.totalData = data
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getOrderReviewList(
        query.departmentType,
        query.page,
        query.limit,
        query.name,
        query.orderId,
        query.id,
        query.payType,
        query.auditStatus,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.contacterName,
        query.departmentIdCode
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
        payType: 0,
        auditStatus: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        departmentType: 2
      }
    },
    //跳转审核详情
    showDetail(row, type) {
      // 跳转详情
      this.$router.push(`/reviewOrder/reviewOrder_detail/${row.id}/${type}`)
    },
    // 搜索部门点击
    handleNodeQueryClick(data) {
      let form = this.$common.clone(this.query)
      form.parentName = data.name
      form.departmentIdCode = data.id
      this.query = form
      this.$refs.selectDept.blur()
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
