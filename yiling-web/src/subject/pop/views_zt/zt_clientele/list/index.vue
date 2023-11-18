<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的企业名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">ERP编码</div>
              <el-input v-model="query.customerCode" placeholder="请输入要查询的ERP编码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
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
            <el-col :span="14">
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
            <el-col :span="7">
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
        <div class="header-bar mar-b-10">
          <div class="sign"></div>客户列表
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
          <el-table-column label="" min-width="240" align="center">
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
              <yl-button v-role-btn="['1']" type="text" @click="editDetailClick(row)">查看</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getCustomerList } from '@/subject/pop/api/zt_api/client';
import { enterpriseType } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';

export default {
  name: 'ZtList',
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
          path: '/zt_dashboard'
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
        name: 'ZtListEdit',
        params: { customerEid: row.customerEid, eid: row.eid }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
