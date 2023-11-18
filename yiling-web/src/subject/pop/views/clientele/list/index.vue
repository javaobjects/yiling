<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="11">
              <div class="title">客户名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的客户名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="13">
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
            <el-col :span="11">
              <div class="title">客户社会统一信用代码</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入客户社会统一信用代码" @keyup.enter.native="handleSearch" />
            </el-col>
            <!-- 是否设置支付方式 -->
            <el-col :span="13">
              <div class="title">客户类型</div>
              <el-radio-group v-model="query.type">
                <el-radio class="option-class" :key="0" :label="0">全部</el-radio>
                <el-radio
                  class="option-class"
                  v-for="item in enterpriseType"
                  :key="item.id"
                  :label="item.value"
                >{{ item.label }}</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="客户ID" min-width="60" align="center" prop="customerEid"></el-table-column>
          <el-table-column label="客户信息" min-width="450" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div style="margin-bottom: 8px;">{{ row.customerName }}</div>
                <div style="margin-bottom: 8px;">{{ row.contactor }} {{ row.contactorPhone }}</div>
                <div>{{ row.address }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="客户类型" min-width="85" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.customerType }}</div>
            </template>
          </el-table-column>
          <!-- <el-table-column label="ERP内码" min-width="120" align="center" prop="customerErpCode"></el-table-column>
          <el-table-column label="采购次数" min-width="95" align="center" prop="purchaseNumber"></el-table-column> -->
          <el-table-column label="支付方式" min-width="174" align="center">
            <template slot-scope="{ row }">
              <div v-for="item in row.paymentMethods" :key="item">{{ item }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['2']" type="text" @click="editDetailClick(row)">编辑</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getCustomerList } from '@/subject/pop/api/channel';
import { enterpriseType } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';

export default {
  name: 'List',
  components: {
    ylChooseAddress
  },
  computed: {
    enterpriseType() {
      return enterpriseType();
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
          title: '客户管理'
        },
        {
          title: '客户列表'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        type: 0
      },
      total: 0,
      dataList: []
    };
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getCustomerList(
        query.page,
        query.limit,
        query.type,
        query.name, //渠道商名称
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.licenseNumber //统一信用代码
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        type: 0
      };
    },
    //跳转详情界面
    editDetailClick(row) {
      this.$log('row:', row);
      this.$router.push({
        name: 'ListEdit',
        params: { customerEid: row.customerEid, eid: row.eid }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
