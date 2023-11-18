<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息
        </div>
        <div class="content-box">
          <div class="content-box-top font-size-small font-light-color">
            <span>
              企业ID：
              <span>{{ detailData.eid }}</span>
            </span>
            <span class="mar-l-32">
              认证状态：
              <span
                :class="[status[detailData.authStatus]]"
              >{{ detailData.authStatus | dictLabel(enterpriseAuthStatus) }}</span>
            </span>
          </div>
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业名称：</span>
                  {{ detailData.customerName }}
                </div>
              </el-col>
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">渠道类型：</span>
                  {{ detailData.channelName }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业类型：</span>
                  {{ detailData.type | dictLabel(enterpriseType) }}
                </div>
              </el-col>
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">社会统一信用代码：</span>
                  {{ detailData.licenseNumber }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业地址：</span>
                  {{ detailData.address }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <!-- 商务联系人信息 -->
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>商务联系人信息
          <yl-button
            class="edit-btn"
            plain
            type="primary"
            @click="editBtnClick()"
          >{{ isEditStatus? '保存' : '编辑' }}</yl-button>
        </div>
        <div class="content-box">
          <div class="mar-t-16 add-view">
            <yl-button :disabled="!isEditStatus" type="text" @click="editBusinessClick()">选择/更换商务联系人</yl-button>
          </div>
          <div class="content-box-bottom">
            <el-row
              class="box"
              v-for="(contactItem, contactIndex) in currentCustomerContacts"
              :key="contactItem.id"
            >
              <el-col :span="4">
                <div class="intro">
                  <span class="font-title-color">联系人姓名：</span>
                  {{ contactItem.name || '- -' }}
                </div>
              </el-col>
              <el-col :span="4">
                <div class="intro">
                  <span class="font-title-color">联系人方式：</span>
                  {{ contactItem.mobile || '- -' }}
                </div>
              </el-col>
              <el-col :span="4">
                <div class="intro">
                </div>
              </el-col>
              <el-col :span="4">
                <yl-button
                  :disabled="!isEditStatus"
                  class="remove-btn"
                  type="text"
                  @click="removeContactClick(contactIndex)"
                >移除</yl-button>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 支付方式 -->
        <div class="header-bar mar-b-10 mar-t-16">
          <div class="sign"></div>支付方式选择
        </div>
        <div class="check-box">
          <el-checkbox-group v-model="paymentMethodIds" :disabled="!isEditStatus">
            <span v-for="item in paymentMethodList" :key="item.id">
              <el-checkbox
              :label="item.id"
              >{{ item.name }}</el-checkbox>
              <!-- <span class="check-tip font-size-base">{{ item.remark }}</span> -->
              <yl-tool-tip v-show="item.remark && item.remark.length > 0" class="check-tip font-size-base">{{ item.remark }}</yl-tool-tip>
            </span>
          </el-checkbox-group>
        </div>
        <!-- 客户分组 -->
        <div class="header-bar mar-b-10 mar-t-16">
          <div class="sign"></div>客户分组
        </div>
        <el-select
          :disabled="!isEditStatus"
          style="width: 387px;"
          v-model="customerGroupId"
          placeholder="请选择客户分组"
        >
          <el-option v-for="item in groupList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>
      <!-- EAS信息 -->
      <div class="top-bar">
        <!-- 商务联系人信息 -->
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>EAS信息
          <yl-button
            class="edit-btn"
            plain
            type="primary"
            @click="addEASBtnClick()"
          >新增EAS信息</yl-button>
        </div>
        <div class="content-box">
          <div class="content-box-bottom" v-if="detailData.easInfoList && detailData.easInfoList.length">
            <el-row
              class="box"
              v-for="item in detailData.easInfoList"
              :key="item.easCode"
            >
              <el-col :span="8">
                <div class="intro">
                  <span class="font-title-color">EAS客户名称：</span>
                  {{ item.easName || '- -' }}
                </div>
              </el-col>
              <el-col :span="6">
                <div class="intro">
                  <span class="font-title-color">EAS编码：</span>
                  {{ item.easCode || '- -' }}
                </div>
              </el-col>
              <el-col :span="10">
                <div class="intro-right">
                  <yl-button type="text" @click="deleteClick(item)">删除</yl-button>
                </div>
              </el-col>
            </el-row>
          </div>
          <div v-else class="font-light-color" style="padding: 16px;">
            暂无EAS信息
          </div>
        </div>
      </div>
      <div class="top-bar">
        <!-- 商务联系人信息 -->
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>采购关系管理
          <!-- <yl-button class="edit-btn" plain type="primary" @click="addProviderClick">新增供应商</yl-button> -->
        </div>
        <div class="content-box-bottom">
          <el-tabs
            v-if="channelType.length > 0"
            class="my-tabs"
            v-model="purchaseQuery.purchaseRelationType"
            @tab-click="handleTabClick"
          >
            <el-tab-pane :label="channelType[0].label + '(' + industryCount + ')'" name="1"></el-tab-pane>
            <el-tab-pane :label="channelType[1].label + '(' + industryDirectCount + ')'" name="2"></el-tab-pane>
            <el-tab-pane :label="channelType[2].label + '(' + level1Count + ')'" name="3"></el-tab-pane>
            <el-tab-pane :label="channelType[3].label + '(' + level2Count + ')'" name="4"></el-tab-pane>
            <el-tab-pane :label="channelType[5].label + '(' + z2p1Count + ')'" name="6"></el-tab-pane>
          </el-tabs>
          <!-- 采购关系信息 列表 -->
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="purchaseList"
              :total="purchaseTotal"
              :page.sync="purchaseQuery.page"
              :limit.sync="purchaseQuery.limit"
              :loading="loading"
              :cell-no-pad="true"
              @getList="getPurList"
            >
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <div class="purchase-table-view" :key="$index">
                    <div class="list-item">
                      <div class="item font-size-small bold">{{ row.name }}</div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业 ID：</span>
                        {{ row.eid }}
                      </div>
                      <div class="item font-size-small font-light-color">
                        <span class="font-light-color">企业地址：</span>
                        {{ row.address }}
                      </div>
                      <!-- <yl-button class="remove-btn" @click="removePurchaseClick(row)" type="text">移除</yl-button> -->
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 新增商务弹框 -->
      <yl-dialog title="选择/更换商务联系人" :visible.sync="showBusinessDialog" :show-footer="false">
        <div class="dialog-content-box">
          <div class="dialog-content-top">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="8">
                  <div class="title">业务姓名</div>
                  <el-input v-model="contactQuery.name" placeholder="请填写姓名" @keyup.enter.native="handleSearch" />
                </el-col>
                <el-col :span="8">
                  <div class="title">业务联系方式</div>
                  <el-input v-model="contactQuery.mobile" placeholder="请填写联系方式" @keyup.enter.native="handleSearch" />
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="16">
                  <yl-search-btn :total="contactTotal" @search="handleSearch" @reset="handleReset" />
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="contactList"
              :total="contactTotal"
              :page.sync="contactQuery.page"
              :limit.sync="contactQuery.limit"
              :loading1="loading1"
              :horizontal-border="false"
              :cell-no-pad="true"
              @getList="getContactListMethod"
            >
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <div class="table-view" :key="$index">
                    <div class="table-item-left">
                      <div class="name-item intro">
                        <div class="name">
                          <span class="font-title-color">姓名：</span>
                          {{ row.name }}
                        </div>
                        <div>
                          <span class="font-title-color">联系方式：</span>
                          {{ row.mobile }}
                        </div>
                      </div>
                      <!-- <div class="intro">
                        <span class="font-title-color">上级领导姓名：</span>
                        {{ row.leaderName }}
                      </div> -->
                    </div>
                    <div class="table-item-right">
                      <yl-button
                        class="edit-btn"
                        v-if="!row.isSelect"
                        type="text"
                        @click="addContactClick(row)"
                      >选择</yl-button>
                      <yl-button class="edit-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 新增供应商弹框 -->
      <yl-dialog title="新增供应商" :visible.sync="showProviderDialog" :show-footer="false">
        <div class="dialog-content-box">
          <div class="dialog-content-top">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="24">
                  <div class="title">渠道类型</div>
                  <el-select v-model="providerQuery.sellerChannelId" placeholder="请选择渠道类型">
                    <el-option
                      v-for="item in channelType"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                  <el-input
                    class="mar-l-16"
                    style="width:352px;"
                    v-model="providerQuery.sellerName"
                    placeholder="请填写供应商名称"
                    @keyup.enter.native="providerHandleSearch"
                  />
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="16">
                  <yl-search-btn
                    :total="providerTotal"
                    @search="providerHandleSearch"
                    @reset="providerHandleReset"
                  />
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <yl-table
              :list="providerList"
              :total="providerTotal"
              :page.sync="providerQuery.page"
              :limit.sync="providerQuery.limit"
              :loading2="loading2"
              :horizontal-border="false"
              :cell-no-pad="true"
              @getList="getCanPurchaseEnterprisePageListMethod"
            >
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <div class="table-view" :key="$index">
                    <div class="table-item-left">
                      <div class="intro">{{ row.name || '- -' }}</div>
                      <div class="intro">
                        <span class="font-title-color">企业 ID：</span>
                        {{ row.eid }}
                      </div>
                      <div class="intro">
                        <span class="font-title-color">企业地址：</span>
                        {{ row.address }}
                      </div>
                    </div>
                    <div class="table-item-right">
                      <yl-button
                        class="edit-btn"
                        v-if="!row.chooseFlag"
                        type="text"
                        @click="addProviderItemClick(row)"
                      >选择</yl-button>
                      <yl-button class="edit-btn" v-if="row.chooseFlag" disabled type="text">已添加</yl-button>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 新增商务弹框 -->
      <yl-dialog title="新增EAS信息" :visible.sync="showEasDialog" @confirm="confirm">
        <div class="Eas-content-dialog">
          <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
            <el-form :model="form" :rules="rules" ref="dataForm" label-width="180px" class="demo-ruleForm">
              <el-form-item label="EAS客户名称" prop="easName">
                <el-input v-model="form.easName" placeholder="请输入EAS客户名称"></el-input>
              </el-form-item>
              <el-form-item label="EAS编码" prop="easCode">
                <el-input v-model="form.easCode" placeholder="请输入EAS编码"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getChannelDetail,
  getChannelCount,
  getPurchaseList,
  getContactList,
  getEnterpriseList,
  getCustomerGroupList,
  saveChannel,
  getCanPurchaseEnterprisePageList,
  addPurchaseRelation,
  // removePurchaseRelation,
  addEasInfo,
  deleteEasInfo
} from '@/subject/pop/api/channel';
import {
  paymentMethod,
  enterpriseAuthStatus,
  enterpriseType,
  channelType
} from '@/subject/pop/utils/busi';

export default {
  components: {},
  computed: {
    paymentMethod() {
      return paymentMethod();
    },
    enterpriseAuthStatus() {
      return enterpriseAuthStatus();
    },
    enterpriseType() {
      return enterpriseType();
    },
    channelType() {
      return channelType();
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
          title: '渠道管理',
          path: '/clientele/channel'
        },
        {
          title: '渠道商详情'
        }
      ],
      loading: false,
      // 客户分组列表
      groupList: [],
      customerGroupId: null,
      //当前选择的商务联系人列表
      currentCustomerContacts: [],
      //支付方式
      paymentMethodList: [],
      //支付方式 选中的
      paymentMethodIds: [],
      detailData: {},
      status: {
        1: 'class1',
        2: 'class2',
        3: 'class3'
      },
      showBusinessDialog: false,
      // 是否是编辑状态
      isEditStatus: false,
      purchaseQuery: {
        //查询渠道商采购关系信息分页参数
        age: 1,
        limit: 10,
        purchaseRelationType: '1' //类型
      },
      industryCount: 0,
      industryDirectCount: 0, //以岭直采个数
      level1Count: 0, //一级商个数
      level2Count: 0, //二级商个数
      // 专二普一商个数
      z2p1Count: 0,
      purchaseList: [], //查询渠道商采购关系列表
      purchaseTotal: 0,
      // 商务联系人
      contactQuery: {
        page: 1,
        limit: 10,
        name: undefined,
        mobile: undefined
      },
      loading1: false,
      contactList: [],
      contactTotal: 0,
      // 部门列表
      departmentList: [],
      // 供应商弹框
      showProviderDialog: false,
      loading2: false,
      providerQuery: {
        page: 1,
        limit: 10,
        sellerChannelId: undefined,
        sellerName: undefined
      },
      providerList: [],
      providerTotal: 0,
      // EAS信息弹框
      showEasDialog: false,
      form: {
        easName: '',
        easCode: ''
      },
      rules: {
        easName: [{ required: true, message: '请输入EAS客户名称', trigger: 'blur' }],
        easCode: [{ required: true, message: '请输入EAS编码', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.getDetail();
    this.getCustomerGroupList();
    this.getBottomChannelCount();
  },
  methods: {
    async getDetail() {
      let params = this.params;
      let detailData = await getChannelDetail(params.customerEid);
      this.detailData = detailData;
      this.paymentMethodList = detailData.customerPaymentMethods;
      let paymentArray = [];
      detailData.customerPaymentMethods.forEach(item => {
        if (item.checked) {
          paymentArray.push(item.id);
        }
      });
      this.paymentMethodIds = paymentArray;
      if (detailData.customerGroup){
        this.customerGroupId = detailData.customerGroup.id;
      }
      this.currentCustomerContacts = detailData.customerContacts || [];

      this.$log('this.dataList:', detailData);
    },
    //客户分组列表
    async getCustomerGroupList() {
      let groupData = await getCustomerGroupList(1, 999, 0, 0);
      this.groupList = groupData.records;
      this.$log('groupData:', groupData);
    },
    // 获取采购商数量
    async getBottomChannelCount(row) {
      let params = this.params;
      let data = await getChannelCount(params.customerEid);
      this.industryCount = data.industryCount || 0;
      this.industryDirectCount = data.industryDirectCount || 0;
      this.level1Count = data.level1Count || 0;
      this.level2Count = data.level2Count || 0;
      this.z2p1Count = data.z2p1Count || 0;

      this.getPurList();
    },
    // 获取采购关系信息分页列表
    async getPurList() {
      this.loading = true;
      let params = this.params;
      let purchaseQuery = this.purchaseQuery;
      let purchaseData = await getPurchaseList(
        purchaseQuery.page,
        purchaseQuery.limit,
        parseInt(purchaseQuery.purchaseRelationType), //渠道商名称
        parseInt(params.customerEid)
      );

      this.loading = false;
      this.purchaseList = purchaseData.records;
      this.purchaseTotal = purchaseData.total;

      this.$log('purchaseData:', purchaseData);
    },
    //tab切换
    handleTabClick(tab, event) {
      this.purchaseQuery.page = 1;
      this.purchaseQuery.limit = 10;
      this.getPurList();
    },
    // 编辑保存按钮点击
    async editBtnClick() {
      this.isEditStatus = !this.isEditStatus;
      if (!this.isEditStatus) {
        // 保存
        this.$common.showLoad();
        let params = this.params;
        let contactUserIds = [];
        this.currentCustomerContacts.forEach(item => {
          contactUserIds.push(item.id);
        });
        let data = await saveChannel(
          contactUserIds,
          params.customerEid,
          this.customerGroupId,
          this.paymentMethodIds,
          params.eid
        );
        this.$common.hideLoad();
        if (data && data.result) {
          this.$common.n_success('保存成功');
        }
        this.$log('data:', data);
      }
    },
    // 编辑商务点击
    editBusinessClick() {
      this.showBusinessDialog = true;
      //查询商务联系人分页列表
      this.getContactListMethod();
      // this.getEnterpriseListMethod();
    },
    // 部门分页列表
    async getEnterpriseListMethod() {
      let params = this.params;
      let departmentData = await getEnterpriseList(
        1,
        999,
        1,
        parseInt(params.eid)
      );
      this.departmentList = departmentData.records;
      this.$log('departmentList:', departmentList);
    },
    // 查询商务联系人分页列表
    async getContactListMethod() {
      this.loading1 = false;
      let contactQuery = this.contactQuery;

      let contactData = await getContactList(
        contactQuery.page,
        contactQuery.limit,
        1,
        contactQuery.name,
        contactQuery.mobile
      );

      this.loading1 = false;
      contactData.records.forEach(item => {
        let hasIndex = this.currentCustomerContacts.findIndex(obj => {
          return obj.id == item.id;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });

      this.contactList = contactData.records;
      this.contactTotal = contactData.total;

      this.$log('contactData:', contactData);
    },
    // 搜索点击
    handleSearch() {
      this.contactQuery.page = 1
      this.getContactListMethod();
    },
    handleReset() {
      this.contactQuery = {
        page: 1,
        limit: 10,
        name: undefined,
        mobile: undefined
      };
    },
    // 商务联系人列表 添加点击
    addContactClick(item) {
      let currentArr = [...this.currentCustomerContacts, item];
      this.currentCustomerContacts = currentArr;

      let arr = this.contactList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.id == item.id;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });

      this.contactList = arr;
    },

    //移除商务联系人
    removeContactClick(contactIndex) {
      let arr = this.currentCustomerContacts;
      arr.splice(contactIndex, 1);
      this.currentCustomerContacts = arr;
    },
    // 新增供应商点击
    addProviderClick() {
      this.showProviderDialog = true;

      this.getCanPurchaseEnterprisePageListMethod();
    },

    async getCanPurchaseEnterprisePageListMethod() {
      this.loading2 = true;
      let providerQuery = this.providerQuery;
      let params = this.params;
      let providerData = await getCanPurchaseEnterprisePageList(
        providerQuery.page,
        providerQuery.limit,
        this.detailData.channelId,
        parseInt(params.customerEid),
        providerQuery.sellerChannelId,
        providerQuery.sellerName
      );

      this.loading2 = false;
      this.providerList = providerData.records;
      this.providerTotal = providerData.total;

      this.$log('providerData:', providerData);
    },

    providerHandleSearch() {
      this.providerQuery.page = 1
      this.getCanPurchaseEnterprisePageListMethod();
    },

    providerHandleReset() {
      this.providerQuery = {
        page: 1,
        limit: 10,
        sellerChannelId: undefined,
        sellerName: undefined
      };
    },
    // 添加渠道商
    async addProviderItemClick(item) {
      let params = this.params;
      let arr = [item.eid];

      this.$common.showLoad();

      arr.push();
      let data = await addPurchaseRelation(params.customerEid, arr, 1);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('保存成功');

        this.getBottomChannelCount();
        this.getCanPurchaseEnterprisePageListMethod();
      }
      this.$log('data:', data);
    },
    // 移除渠道商
    // async removePurchaseClick(item) {
    //   let params = this.params;
    //   let arr = [item.eid];

    //   this.$common.showLoad();

    //   arr.push();
    //   let data = await removePurchaseRelation(params.customerEid, arr);
    //   this.$common.hideLoad();
    //   if (data && data.result) {
    //     this.$common.n_success("移除成功");

    //     this.getBottomChannelCount();
    //   }
    // },
    // 新增EAS信息
    addEASBtnClick() {
      this.showEasDialog = true
    },
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let params = this.params;
          this.$common.showLoad()
          let data = await addEasInfo(
            params.customerEid,
            this.form.easName,
            this.form.easCode
          );
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('新增成功')
            this.showEasDialog = false
            this.getDetail()
          }
        } else {
          return false
        }
      })
    },
    // 删除Eas信息
    deleteClick(val) {
      this.$confirm(`确认删除 ${val.easName} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
      //确定
      let data = await deleteEasInfo(val.id)
      if (data!==undefined) {
        this.$common.n_success('删除成功!');
        this.getDetail();
        // this.getList();
        }
      })
      .catch(() => {
      //取消
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
