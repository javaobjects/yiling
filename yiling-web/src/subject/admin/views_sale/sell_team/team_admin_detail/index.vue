<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <div class="header-bar">
            <div class="sign"></div>{{ userInfo.name }}
          </div>
          <el-row class="color-3 mar-t-16 font-14">
            <el-col :span="8"><span class="font-title-color">联系方式：</span>{{ userInfo.mobilePhone }}</el-col>
            <el-col :span="8"><span class="font-title-color">状态：</span>{{ userInfo.registerStatus | enRegister }}</el-col>
            <el-col :span="8"><span class="font-title-color">拉取人：</span>{{ userInfo.inviteName }}</el-col>
          </el-row>
          <el-row class="color-3 mar-t-8 font-14">
            <el-col :span="8"><span class="font-title-color">证件号：</span>{{ userInfo.idNumber }}</el-col>
            <el-col :span="8"><span class="font-title-color">注册时间：</span>{{ userInfo.registerTime | formatDate }}</el-col>
          </el-row>

        </div>
      </div>
      <div class="tab mar-t-16">
        <div v-for="(item,index) in tabList" :key="index" class="tab-item" :class="tabActive === index?'tab-active':''" @click="clickTab(index)">{{ item }}</div>
      </div>
      <div v-show=" tabActive == 0 " class="summary mar-t-8">
        <span>已下单总金额：</span>
        <span>¥{{ userInfo.orderAmount }}</span>
        <!-- <span class="mar-l-16">已下单总金额：</span>
      <span>¥30,000.00</span> -->
      </div>
      <!-- 订单列表 -->
      <div v-show=" tabActive == 0 ">
        <yl-table :list="orderList" :show-header="true" stripe center>
          <el-table-column prop="orderNo" label="订单编号" align="center"></el-table-column>
          <el-table-column prop="customerName" label="客户名称" align="center"></el-table-column>
          <el-table-column prop="orderAmount" label="订单金额" align="center"></el-table-column>
          <el-table-column prop="orderStatus" label="订单状态" align="center">
            <template slot-scope="scope" align="center">
              <span>{{ scope.row.orderStatus | dictLabel(orderStatus) }}</span>
              <!-- <div>
              <span class="color-green" v-if="scope.row.status ===1">已签收</span>
              <span class="color-yellow" v-else>待审核</span>
            </div> -->
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="下单时间" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showOrderDetail(row)">查看</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 客户列表 -->
      <div v-show=" tabActive == 1 " class="mar-t-8">
        <yl-table :list="customerList" :show-header="true" stripe center>
          <el-table-column prop="name" label="客户名称" align="center"></el-table-column>
          <!-- 状态：1-待审核 2-审核通过 3-审核驳回	 -->
          <el-table-column prop="status" label="状态" align="center">
            <template slot-scope="scope" align="center">
              <div>
                <span class="color-yellow" v-if="scope.row.status ===1">待审核</span>
                <span class="color-green" v-else-if="scope.row.status === 2">审核通过</span>
                <span class="color-yellow" v-else>审核驳回</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="orderAmount" label="订单金额" align="center"></el-table-column>
          <el-table-column prop="contactor" label="联系人姓名" align="center"></el-table-column>
          <el-table-column prop="contactorPhone" label="联系人电话" align="center"></el-table-column>
          <el-table-column prop="auditTime" label="认证时间" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.auditTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showCustomerDetail(row)">查看</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 订单查看弹窗 -->
      <yl-dialog width="580px" title="订单查看" :visible.sync="showOrder" :show-footer="true" :show-header="false" @confirm="showOrder=false" :show-cancle="false">
        <div class="dialog-content">
          <div class="header-bar">
            <div class="sign"></div>收货信息
          </div>
          <div class="color-3 font-14">
            <el-row>
              <el-col :span="12" :offset="0"><span class="color-6">收货人：</span>{{ orderInfo.name }}</el-col>
              <el-col :span="12" :offset="0"><span class="color-6">发货量：</span>{{ orderInfo.deliveryQuantity }}</el-col>
            </el-row>
            <el-row class="mar-t-8">
              <el-col :span="12" :offset="0"><span class="color-6">联系方式：</span>{{ orderInfo.mobile }}</el-col>
              <el-col :span="12" :offset="0"><span class="color-6">收货量：</span>{{ orderInfo.receiveQuantity }}</el-col>
            </el-row>
            <el-row class="mar-t-8">
              <el-col :span="24" :offset="0"><span class="color-6">收货地址：</span>{{ orderInfo.provinceName }}-{{ orderInfo.cityName }}-{{ orderInfo.regionName }}-{{ orderInfo.address }}</el-col>
            </el-row>
            <el-row class="mar-t-8 line-24">
              <el-col :span="24" :offset="0"><span class="color-6">备注：</span>{{ orderInfo.remark }}</el-col>
            </el-row>
            <div class="header-bar">
              <div class="sign"></div>商品信息
            </div>
            <div class="order-item" v-for="(item,index) in orderShowList " :key="index">
              <div class="order-title">{{ item.goodsName }}</div>
              <el-row :gutter="24" class="mar-t-8">
                <el-col :span="12" :offset="0"><span class="color-6">规格/型号：</span>{{ item.goodsSpecification }}</el-col>
                <el-col :span="12" :offset="0"><span class="color-6">单价：</span>{{ item.goodsPrice }}</el-col>
              </el-row>
              <el-row :gutter="24" class="mar-t-8">
                <el-col :span="12" :offset="0"><span class="color-6">购买数量：</span>{{ item.goodsQuantity }}</el-col>
                <el-col :span="12" :offset="0"><span class="color-6">支付金额：</span>{{ item.goodsAmount }}</el-col>
              </el-row>
            </div>
          </div>
        </div>
      </yl-dialog>
      <!-- 客户列表查看详情弹窗数据 -->
      <yl-dialog width="580px" title="客户信息查看" :visible.sync="showCustomer" :show-footer="true" :show-header="false" @confirm="showCustomer=false" :show-cancle="false">
        <div class="dialog-content color-3 font-14">
          <div class="order-item" v-for="(item,index) in customerShowList " :key="index">
            <el-row :gutter="24" class="mar-t-8">
              <el-col :span="12" :offset="0"><span class="color-6">订单编号：</span>{{ item.orderNo }}</el-col>
              <el-col :span="12" :offset="0"><span class="color-6">订单金额：</span>{{ item.orderAmount }}</el-col>
            </el-row>
            <el-row :gutter="24" class="mar-t-8">
              <el-col :span="12" :offset="0"><span class="color-6">下单时间：</span>{{ item.createTime | formatDate }}</el-col>
              <el-col :span="12" :offset="0"><span class="color-6">订单状态：</span>
                <!--	 -->
                <span class="color-green">{{ item.orderStatus | dictLabel(orderStatus) }}</span>
                <!-- <span class="color-yellow">未签收</span> -->
              </el-col>
            </el-row>
          </div>
        </div>
      </yl-dialog>

    </div>
  </div>

</template>

<script>
import {
  getMemberDetail,
  getMemberOrderList,
  getTeamOrderListShow,
  getTeamCustomerList,
  getTeamCustomerDetail
} from '@/subject/admin/api/views_sale/sale_team_admin';
import { orderStatus } from '@/subject/admin/utils/busi';
export default {
  name: 'TeamAdmin',
  components: {},
  filters: {
    enRegister(e) {
      return parseInt(e) === 1 ? '已注册' : '未注册';
    }
  },
  computed: {
    // 订单状态
    orderStatus() {
      return orderStatus();
    }
  },
  data() {
    return {
      userInfo: {},
      tabList: ['订单信息', '客户信息'],
      tabActive: 0,
      query: {
        page: 1,
        limit: 20,
        total: 0
      },
      orderList: [],
      customerList: [],
      showOrder: false,
      showCustomer: false,
      orderInfo: {},
      orderShowList: [],
      customerShowList: []
    };
  },
  created() {
    this.getUserDetail();
    this.getOrderList();
  },
  methods: {
    async getUserDetail() {
      let data = await getMemberDetail(this.$route.params.id);
      console.log(data);
      if (data) {
        this.userInfo = data;
      }
    },
    //  获取订单列表
    async getOrderList() {
      this.loading = true;
      let data = await getMemberOrderList(this.$route.params.id);
      console.log(data);
      this.loading = false;
      if (data && data.records) {
        this.orderList = data.records;
        this.query.total = data.total;
      }
    },
    clickTab(e) {
      this.tabActive = e;
      if (e == 0) {
        this.getOrderList();
      } else {
        this.getCustomerList();
      }
    },
    // 查看订单弹窗详情
    async showOrderDetail(row) {
      this.orderInfo = {};
      this.orderShowList = [];
      let data = await getTeamOrderListShow(row.orderId);
      console.log(data);
      this.showOrder = true;
      if (data) {
        this.orderInfo = data.orderReceiveInfoVO;
        this.orderShowList = data.productItemVoList;
      }
    },
    // 查看客户信息列表 弹窗详情
    async showCustomerDetail(row) {
      let query = this.query;
      let data = await getTeamCustomerDetail(
        query.page,
        row.customerEid,
        query.limit,
        this.$route.params.id
      );
      this.showCustomer = true;
      if (data && data.records) {
        this.customerShowList = data.records;
      }
    },
    //  获取客户信息列表
    async getCustomerList() {
      this.loading = true;
      let data = await getTeamCustomerList(this.$route.params.id);
      console.log(data);
      this.loading = false;
      if (data) {
        this.customerList = data.records;
        this.total = data.total;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>