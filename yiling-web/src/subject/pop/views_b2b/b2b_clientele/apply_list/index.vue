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
              <el-input v-model="query.name" placeholder="请输入企业名称" @keyup.enter.native="handleSearch" />
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
            <el-col :span="6">
              <div class="title">营业执照</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入社会统一信用代码" @keyup.enter.native="handleSearch" />
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
        <!-- tab切换 -->
        <div class="tab">
          <div v-for="(item,index) in tabList" :key="index" class="tab-item" :class="tabActive === item.value ? 'tab-active' : ''" @click="clickTab(item.value)">
            <span>{{ item.label }}</span>
          </div>
        </div>
        <!-- 提示语 -->
        <div class="prompt">列表信息（已为您检索出 <span>{{ total }}</span> 条数据）</div>
        <div class="mar-t-8">
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
            <!-- <el-table-column label="序号" min-width="60" align="center" prop="customerEid"></el-table-column> -->
            <el-table-column label="客户信息" min-width="120" align="left">
              <template slot-scope="{ row }">
                <div class="product-desc">
                  <div class="title">{{ row.name }}</div>
                  <div class="font-light-color"><span class="font-light-color">企业类型：</span>{{ row.type | dictLabel(enterpriseType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="基本信息" min-width="240" align="left">
              <template slot-scope="{ row }">
                <div>
                  <span class="font-light-color">社会统一信用代码：</span>
                  {{ row.licenseNumber }}
                  <span class="font-img" @click="copyClick(row.licenseNumber)">
                    <img src="@/subject/pop/assets/invoice/copy.png" alt="">
                  </span>
                </div>
                <div><span class="font-light-color">所属地区：</span>{{ row.provinceName }} ｜ {{ row.cityName }} ｜ {{ row.regionName }}</div>
                <div><span class="font-light-color">企业地址：</span>{{ row.address }}</div>
              </template>
            </el-table-column>
            <el-table-column label="联系信息" min-width="140" align="left">
              <template slot-scope="{ row }">
                <div class="product-desc">
                  <div><span class="font-light-color">联系人：</span>{{ row.contactor }}</div>
                  <div><span class="font-light-color">手机号：</span>{{ row.contactorPhone }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="showErp && tabActive != 1" label="客户内部编码" min-width="140" align="left">
              <template slot-scope="{ row }">
                <div class="product-desc">
                  <div><span class="font-light-color">客户ERP内码：</span>{{ row.customerErpCode }}</div>
                  <div><span class="font-light-color">客户ERP编码：</span>{{ row.customerCode }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="tabActive != 1" label="审核人" min-width="100" align="left">
              <template slot-scope="{ row }">
                {{ row.authUserName }}
              </template>
            </el-table-column>
            <el-table-column label="申请时间" min-width="110" align="left">
              <template slot-scope="{ row }">
                <div>{{ row.createTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column v-if="tabActive != 1" label="审核时间" min-width="110" align="left">
              <template slot-scope="{ row }">
                <div>{{ row.authTime | formatDate }}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row }">
                <yl-button v-role-btn="['1']" type="text" @click="editDetailClick(row)">
                  <span class="font-color">
                    {{ row.authStatus == 1 ? '审核' : '查看' }}</span>
                </yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPurchaseApply, getErpFlag } from '@/subject/pop/api/b2b_api/client';
import { enterpriseType, enterprisePurchaseApplyStatus } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';

export default {
  name: 'B2BApplyList',
  components: {
    ylChooseAddress
  },
  computed: {
    enterpriseType() {
      return enterpriseType();
    },
    enterprisePurchaseApplyStatus() {
      return enterprisePurchaseApplyStatus();
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
          title: '采销审核'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        type: 0,
        authStatus: 1
      },
      total: 0,
      dataList: [],
      tabList: [
        {
          label: '待审核',
          value: 1
        },
        {
          label: '已建采',
          value: 2
        },
        {
          label: '已驳回',
          value: 3
        }
      ],
      tabActive: 1,
      //是否展示客户内部编码
      showErp: false
    };
  },
  activated() {
    this.getErpFlagApi();
    this.getList();
  },
  methods: {
    clickTab(e) {
      this.tabActive = e;
      this.query.authStatus = e;
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    async getErpFlagApi() {
      let data = await getErpFlag()
      if (data) {
        this.showErp = data
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getPurchaseApply(
        query.page,
        query.limit,
        query.name, //渠道商名称
        query.type,
        query.authStatus,
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
        type: 0,
        authStatus: this.tabActive
      };
    },
    //跳转详情界面
    editDetailClick(row) {
      this.$log('row:', row);
      this.$router.push({
        name: 'B2bApplyEdit',
        params: { customerEid: row.customerEid }
      });
    },
    //点击复制社会信用统一代码
    copyClick(val) {
      if (val != '') {
        let cInput = document.createElement('input');
        cInput.value = val;
        document.body.appendChild(cInput);
        cInput.select();
        document.execCommand('Copy');
        this.$common.n_success('复制成功')
        cInput.remove();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
