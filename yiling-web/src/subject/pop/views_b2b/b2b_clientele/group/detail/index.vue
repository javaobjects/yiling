<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <!-- 顶部分组名 -->
      <div class="top-view">
        <div>
          <span>分组名称：</span>
          <span>{{ name }}</span>
        </div>
        <div>
          <ylButton type="primary" plain @click="updateGroupName">修改</ylButton>
          <ylButton type="primary" @click="showCustomerDialogClick">配置客户</ylButton>
        </div>
      </div>
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" placeholder="请输入要查询的企业名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="16">
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
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select v-model="query.type" placeholder="请选择企业类型">
                <el-option
                  v-for="item in enterpriseType"
                  v-show="item.value != 1"
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
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="batchRemoveClick">批量移除</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div
        class="mar-t-8"
        style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;"
      >
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :selection-change="handleSelectionChange"
          @getList="getList"
        >
          <el-table-column type="selection" align="center" width="70"></el-table-column>
          <el-table-column label="序号" min-width="55" align="center">
            <template slot-scope="{ $index }">
              <div>{{ (query.page - 1) * query.limit + $index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column label="客户信息" width="500" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>{{ row.customerName }}</div>
                <div>企业地址：{{ row.address }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="180" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>联系人：{{ row.contactor }}</div>
                <div>联系电话：{{ row.contactorPhone }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" min-width="184" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>ERP客户名称：{{ row.customerErpName }}</div>
                <div>ERP内码：{{ row.customerErpCode }}</div>
                <div>ERP编码：{{ row.customerCode }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="groupDeleteClick(row)">移除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 新建分组名弹框 -->
      <el-dialog
        title="修改分组名称"
        width="33%"
        :visible.sync="showAddGroupDialog"
        custom-class="add-dialog"
        :show-close="true"
        :destroy-on-close="true"
        :close-on-click-modal="false"
      >
        <div class="move-dialog-content">
          <div>
            <el-form
              :model="addGroupForm"
              :rules="addGroupRules"
              ref="addGroupRef"
              label-width="78px"
              class="demo-ruleForm"
            >
              <el-form-item label="分组名称" prop="name">
                <el-input v-model="addGroupForm.name" :maxlength="10" show-word-limit></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <yl-button @click="showAddGroupDialog = false">取消</yl-button>
          <yl-button type="primary" @click="addGroupDialogClick">确定</yl-button>
        </span>
      </el-dialog>
      <!-- 新增配置客户弹框 -->
      <yl-dialog title="配置客户" :visible.sync="showCustomerDialog" :show-footer="false">
        <div class="dialog-content-box-customer">
          <div class="dialog-content-top">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="6">
                  <div class="title">客户名称</div>
                  <el-input v-model="customerQuery.name" placeholder="请输入客户名称" @keyup.enter.native="customerHandleSearch" />
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
                <el-col :span="6">
                  <div class="title">社会统一信用代码</div>
                  <el-input v-model="customerQuery.licenseNumber" placeholder="请输入社会统一信用代码" @keyup.enter.native="customerHandleSearch" />
                </el-col>
                <el-col :span="6" :offset="2">
                  <div class="title">企业类型</div>
                  <el-select v-model="customerQuery.type" placeholder="请选择企业类型">
                    <el-option
                      v-for="item in enterpriseType"
                      v-show="item.value != 1"
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
            <ylButton type="primary" plain @click="queryBtnClick">查询结果添加</ylButton>
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
              <el-table-column label="序号" min-width="55" align="center">
                <template slot-scope="{ $index }">
                  <div>{{ (query.page - 1) * query.limit + $index + 1 }}</div>
                </template>
              </el-table-column>
              <el-table-column label="客户信息" width="490" align="center">
                <template slot-scope="{ row }">
                  <div class="product-desc">
                    <div>{{ row.customerName }}</div>
                    <div>社会统一信用代码：{{ row.licenseNumber }}</div>
                    <div>企业地址：{{ row.address }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="174" align="center">
                <template slot-scope="{ row }">
                  <div class="operate-view">
                    <yl-button
                      v-if="row.customerGroupId == parseInt(params.customerGroupId)"
                      type="text"
                      disabled
                    >已添加</yl-button>
                    <yl-button v-else-if="row.customerGroupId > 0" type="text" disabled>已经添加其它分组</yl-button>
                    <yl-button v-else type="text" @click="groupAddCustomerClick(row)">添加</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getCustomerList,
  updateGroup,
  addCustomers,
  addResultCustomers,
  removeCustomers
} from '@/subject/pop/api/b2b_api/client';
import { ylChooseAddress } from '@/subject/pop/components';
import { channelType, enterpriseType } from '@/subject/pop/utils/busi';

export default {
  components: {
    ylChooseAddress
  },
  computed: {
    channelType() {
      return channelType();
    },
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
          title: '客户分组',
          path: '/b2b_clientele/b2b_group'
        },
        {
          title: '编辑分组'
        }
      ],
      name: '',
      loading: false,
      query: {
        page: 1,
        limit: 10
      },
      total: 0,
      dataList: [],
      // 表格选择
      multipleSelection: [],
      // 新建分组弹框
      showAddGroupDialog: false,
      addGroupForm: {
        name: ''
      },
      addGroupRules: {
        name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
      },
      // 配置客户
      showCustomerDialog: false,
      loading1: false,
      customerQuery: {
        page: 1,
        limit: 10
      },
      customerTotal: 0,
      customerList: []
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.name = this.$route.params.name;
    this.addGroupForm.name = this.$route.params.name;
    this.getList();
  },
  methods: {
    async getList() {
      let params = this.params;
      this.loading = true;
      let query = this.query;
      let data = await getCustomerList(
        query.page,
        query.limit,
        parseInt(params.customerGroupId),
        query.name, //渠道商名称
        undefined,
        query.type,
        query.provinceCode,
        query.cityCode,
        query.regionCode
      );
      this.loading = false;
      this.dataList = data.records;
      this.total = data.total;

      this.$log('this.dataList:', this.dataList);
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      };
    },
    // 移动分组
    groupMoveClick(row) {
      this.showMoveGroupDialog = true;
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$log('this.multipleSelection:', val);
    },
    // 修改分组名
    updateGroupName() {
      this.addGroupForm.name = this.name;
      this.showAddGroupDialog = true;
    },
    addGroupDialogClick() {
      //type: 1:稍后配置  2:立即配置
      this.$refs['addGroupRef'].validate(valid => {
        if (valid) {
          this.addGroupMethod();
        } else {
          return false;
        }
      });
    },
    async addGroupMethod() {
      let params = this.params;
      this.$common.showLoad();
      let addGroupForm = this.addGroupForm;
      let data = await updateGroup(
        parseInt(params.customerGroupId),
        addGroupForm.name //渠道商名称
      );
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.name = this.addGroupForm.name;
        this.$common.n_success('修改成功');
        this.showAddGroupDialog = false;
      }
      this.$log('adata:', data);
    },
    // 客户配置
    showCustomerDialogClick() {
      this.showCustomerDialog = true;
      this.customerQuery.page = 1;
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

      this.$log('this.customerList:', this.customerList);
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
    // 分组中添加客户
    groupAddCustomerClick(row) {
      let customerEids = [];
      customerEids.push(row.customerEid);
      this.groupAddCustomerMethod(customerEids);
    },
    // 查询结果添加
    async queryBtnClick() {

      let params = this.params
      let customerQuery = this.customerQuery

      this.$common.showLoad();
      let data = await addResultCustomers(
        parseInt(params.customerGroupId),
        customerQuery.name, //客户名称
        customerQuery.provinceCode,
        customerQuery.cityCode,
        customerQuery.regionCode,
        customerQuery.licenseNumber, //统一信用代码
        customerQuery.type
      );
      this.$common.hideLoad();
      if (typeof data != 'undefined') {
        this.$common.n_success('添加成功');
        this.showCustomerDialog = false;
        this.getList();
      }
    },
    //  分组中添加客户方法
    async groupAddCustomerMethod(customerEids) {
      let params = this.params;

      this.$common.showLoad();
      let data = await addCustomers(
        parseInt(params.customerGroupId),
        customerEids
      );
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.$common.n_success('添加成功');
        this.getCustomerPageListMethod();
        this.getList();
      }
      this.$log('data:', data);
    },
    // 批量移除
    batchRemoveClick() {
      this.$log('this.multipleSelection:', this.multipleSelection);
      if (!this.multipleSelection || this.multipleSelection.length == 0) {
        this.$common.warn('暂未选择移除客户')
        return false
      }
      let customerEids = [];
      this.multipleSelection.forEach(item => {
        customerEids.push(item.customerEid);
      });
      this.groupDeleteMethod(customerEids);
    },
    // 移除分组中客户
    groupDeleteClick(row) {
      let customerEids = [];
      customerEids.push(row.customerEid);
      this.groupDeleteMethod(customerEids);
    },
    // 移除分组中客户
    async groupDeleteMethod(customerEids) {
      let params = this.params;

      this.$common.showLoad();
      let data = await removeCustomers(
        parseInt(params.customerGroupId),
        customerEids
      );
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.$common.n_success('移除成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (customerEids.length == this.dataList.length ){
          this.query.page = this.query.page - 1 || 1
        }
        this.getList();
      }
      this.$log('data:', data);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
