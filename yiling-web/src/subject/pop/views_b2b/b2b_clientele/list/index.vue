<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的企业名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">ERP编码</div>
              <el-input v-model="query.customerCode" placeholder="请输入要查询的ERP编码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">企业类型</div>
              <el-select v-model="query.type" placeholder="请选择企业类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in enterpriseType"
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
            <el-col :span="12">
              <div class="title">企业地址</div>
              <div class="flex-row-left">
                <yl-choose-address
                  :province.sync="query.provinceCode"
                  :city.sync="query.cityCode"
                  :area.sync="query.regionCode"
                  is-all
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">企业营业执照号/医疗机构许可证号</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入" @keyup.enter.native="handleSearch" />
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
      <div class="mar-t-16 bottom-table-view" style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;">
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>客户列表
          <div class="btn">
            <ylButton v-role-btn="['2']" type="primary" @click="openClick">批量管理线下支付</ylButton>
          </div>
        </div>
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
          <el-table-column label="序号" min-width="60" align="center" prop="customerEid"></el-table-column>
          <el-table-column label="客户信息" min-width="300" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div style="margin-bottom: 8px;" class="title">{{ row.customerName }}</div>
                <div class="item-text" style="margin-bottom: 8px;"><span class="font-light-color">企业类型：</span>{{ row.type | dictLabel(enterpriseType) }}</div>
                <div style="margin-bottom: 8px;"><span class="font-light-color">社会信用统一代码：</span>{{ row.licenseNumber }}</div>
                <div><span class="font-light-color">企业地址：</span>{{ row.address }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div style="margin-bottom: 8px;" class="title"></div>
                <div style="margin-bottom: 8px;"><span class="font-light-color">联系人：</span>{{ row.contactor }}</div>
                <div style="margin-bottom: 8px;"><span class="font-light-color">联系电话：</span>{{ row.contactorPhone }}</div>
                <div class="item-text"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div style="margin-bottom: 8px;" class="title"></div>
                <div style="margin-bottom: 8px;"><span class="font-light-color item-text">ERP客户名称：</span>{{ row.customerErpName }}</div>
                <div style="margin-bottom: 8px;"><span class="font-light-color">ERP内码：</span>{{ row.customerErpCode || '- -' }}</div>
                <div><span class="font-light-color">ERP编码：</span>{{ row.customerCode || '- -' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div style="margin-bottom: 8px;" class="title"></div>
                <div style="margin-bottom: 8px;"><span class="font-light-color">客户来源：</span>{{ row.source | dictLabel(sourceArray) }}</div>
                <div class="item-text" style="margin-bottom: 8px;"></div>
                <div class="item-text"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['1']" type="text" @click="editDetailClick(row)">编辑</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 批量开通线下支付弹框 -->
    <yl-dialog 
      width="900px" 
      title="批量管理线下支付" 
      :visible.sync="showCustomerDialog" 
      :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="6">
                <div class="title">企业名称</div>
                <el-input v-model="customerQuery.name" placeholder="请输入企业名称" @keyup.enter.native="customerHandleSearch" />
              </el-col>
              <el-col :span="16" :offset="2">
                <div class="title">地域查询</div>
                <div class="flex-row-left">
                  <yl-choose-address
                    width="144px"
                    :province.sync="customerQuery.provinceCode"
                    :city.sync="customerQuery.cityCode"
                    :area.sync="customerQuery.regionCode"
                    is-all
                  />
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">社会统一信用代码</div>
                <el-input v-model="customerQuery.licenseNumber" placeholder="请输入社会统一信用代码" @keyup.enter.native="customerHandleSearch" />
              </el-col>
              <el-col :span="8" >
                <div class="title">企业类型</div>
                <el-select v-model="customerQuery.type" placeholder="请选择企业类型">
                  <el-option
                    v-for="item in enterpriseType"
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
              <el-col :span="16">
                <yl-search-btn
                  :total="customerTotal"
                  @search="customerHandleSearch"
                  @reset="customerHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="border-1px-t line-view"></div>
        <div class="query-view">
          <ylButton type="danger" plain @click="queryBtnClick(2)">批量关闭</ylButton>
          <ylButton type="primary" plain @click="queryBtnClick(1)">批量开通</ylButton>
        </div>
        <div class="mar-t-8" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            border
            :show-header="true"
            :list="customerList"
            :total="customerTotal"
            :page.sync="customerQuery.page"
            :limit.sync="customerQuery.limit"
            :loading1="loading1"
            @getList="getCustomerPageListMethod"
          >
            <el-table-column label="序号" min-width="60" align="center" prop="customerEid"></el-table-column>
            <el-table-column label="客户信息" width="380" align="center">
              <template slot-scope="{ row }">
                <div class="product-desc">
                  <div class="name">{{ row.customerName }} <span class="type">({{ row.type | dictLabel(enterpriseType) }})</span></div>
                  <div>社会统一信用代码：{{ row.licenseNumber }}</div>
                  <div>企业地址：{{ row.address }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="当前所在分组" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="product-desc">
                  <div>{{ row.customerGroupName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="线下支付状态" min-width="100" align="center">
              <template slot-scope="{ row }">
                {{ row.openOfflineFlag == 1 ? '已开通' : '未开通' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="operate-view">
                  <yl-button v-if="row.openOfflineFlag" type="text" style="color: #ee0a24" @click="groupAddCustomerClick(row, 2)">关闭</yl-button>
                  <yl-button v-else type="text" @click="groupAddCustomerClick(row, 1)">开通</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getCustomerList, openOfflinePayByResult, openOfflinePay } from '@/subject/pop/api/b2b_api/client';
import { enterpriseType } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';

export default {
  name: 'B2BList',
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
          path: '/b2b_dashboard'
        },
        {
          title: '客户管理'
        },
        {
          title: '客户列表'
        }
      ],
      sourceArray: [
        {
          label: '后台导入',
          value: 1
        },
        {
          label: 'ERP对接',
          value: 2
        },
        {
          label: 'SAAS导入',
          value: 3
        },
        {
          label: '协议生成',
          value: 4
        },
        {
          label: '线上采购',
          value: 5
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        type: 0
      },
      total: 0,
      dataList: [],
      // 批量开通线下支付弹框
      showCustomerDialog: false,
      loading1: false,
      customerQuery: {
        opType: '',
        page: 1,
        limit: 10
      },
      customerTotal: 0,
      customerList: []
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
        undefined,
        query.name, //渠道商名称
        query.customerCode,
        query.type,
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
        name: 'B2bListEdit',
        params: { customerEid: row.customerEid, eid: row.eid }
      });
    },
    // 批量开通线下支付
    openClick() {
      this.showCustomerDialog = true
      this.customerQuery.page = 1
      this.getCustomerPageListMethod();
    },
    async getCustomerPageListMethod() {
      this.loading1 = true;
      let customerQuery = this.customerQuery;
      let data = await getCustomerList(
        customerQuery.page,
        customerQuery.limit,
        undefined,
        customerQuery.name, //客户名称
        undefined,
        customerQuery.type,
        customerQuery.provinceCode,
        customerQuery.cityCode,
        customerQuery.regionCode,
        customerQuery.licenseNumber //统一信用代码
      );
      this.loading1 = false;
      this.customerList = data.records;
      this.customerTotal = data.total;

    },
    customerHandleSearch() {
      this.customerQuery.page = 1
      this.getCustomerPageListMethod();
    },
    customerHandleReset() {
      this.customerQuery = {
        page: 1,
        limit: 10
      };
    },
    // 批量开通1 批量关闭2
    async queryBtnClick(val) {

      let customerQuery = this.customerQuery

      this.$common.showLoad();
      let data = await openOfflinePayByResult(
        customerQuery.name, //客户名称
        customerQuery.provinceCode,
        customerQuery.cityCode,
        customerQuery.regionCode,
        customerQuery.licenseNumber, //统一信用代码
        customerQuery.type,
        val
      );
      this.$common.hideLoad();
      if (typeof data != 'undefined') {
        this.$common.n_success('操作成功');
        this.getCustomerPageListMethod();
        this.showCustomerDialog = false;
      }
    },
   
    // 开通
    async groupAddCustomerClick(row, val) {
      this.$common.showLoad();
      let data = await openOfflinePay(
        row.customerEid,
        val
      );
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.$common.n_success('操作成功');
        this.getCustomerPageListMethod();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
