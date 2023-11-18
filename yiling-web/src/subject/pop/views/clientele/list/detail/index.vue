<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息
        </div>
        <div class="content-box mar-b-10">
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="24">
                <div class="intro">
                  <span class="font-title-color">企业名称：</span>
                  {{ customerInfo && customerInfo.name }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="24">
                <div class="intro">
                  <span class="font-title-color">企业地址：</span>
                  {{ customerInfo && customerInfo.address }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="24">
                <div class="intro">
                  <span class="font-title-color">社会统一信用代码：</span>
                  {{ customerInfo && customerInfo.licenseNumber }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 商务联系人信息 -->
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>商务联系人信息
        </div>
        <div class="content-box">
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="5">
                <div class="intro">
                  <span class="font-title-color">联系人姓名：</span>
                  {{ customerInfo && customerInfo.contactor }}
                </div>
              </el-col>
              <el-col :span="4">
                <div class="intro">
                  <span class="font-title-color">联系人方式：</span>
                  {{ customerInfo && customerInfo.contactorPhone }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 支付方式 -->
        <div class="header-bar mar-b-10 mar-t-16">
          <div class="sign"></div>支付方式选择
        </div>
        <div class="check-box mar-b-10">
          <el-checkbox-group v-model="paymentMethodIds">
            <span v-for="item in paymentMethodList" :key="item.id">
              <el-checkbox :label="item.id">{{ item.name }}</el-checkbox>
              <span class="check-tip font-size-base">{{ item.remark }}</span>
            </span>
          </el-checkbox-group>
        </div>
        <!-- 客户分组 -->
        <div v-if="currentEnterpriseInfo && currentEnterpriseInfo.yilingFlag">
          <div class="header-bar mar-b-10 mar-t-16">
            <div class="sign"></div>客户分组
          </div>
          <el-select style="width: 387px;" class="mar-b-10" v-model="customerGroupId" placeholder="请选择客户分组">
            <el-option v-for="item in groupList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
        <!-- 账期额度 -->
        <div v-if="currentEnterpriseInfo && currentEnterpriseInfo.yilingFlag">
          <div class="header-bar mar-b-10 header-renative">
            <div class="sign"></div>账期额度
          </div>
          <div class="content-box mar-b-10">
            <div class="content-box-bottom">
              <el-row class="box">
                <el-col :span="6">
                  <div class="intro">
                    <span class="font-title-color">账期额度：</span>
                    ¥{{ (paymentDaysInfo && paymentDaysInfo.totalAmount) || '- -' }}元
                  </div>
                </el-col>
                <el-col :span="9">
                  <div class="intro">
                    <span class="font-title-color">账期有效时间：</span>
                    {{ paymentDaysInfo.startTime | formatDate }}-{{ paymentDaysInfo.endTime | formatDate }}
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="intro">
                    <span class="font-title-color">信用账期：</span>
                    {{ (paymentDaysInfo && paymentDaysInfo.period) || '- -' }}天
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
        <!-- ERP信息 -->
        <div class="header-bar mar-b-10 header-renative">
          <div class="sign"></div>ERP信息
        </div>
        <div class="content-box mar-b-10">
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="6">
                <div class="intro">
                  <span class="font-title-color">ERP客户名称：</span>
                  {{ erpCustomerInfo.customerName || '- -' }}
                </div>
              </el-col>
              <el-col :span="9">
                <div class="intro">
                  <span class="font-title-color">ERP编码：</span>
                  {{ erpCustomerInfo.customerCode || '- -' }}
                </div>
              </el-col>
              <el-col :span="6">
                <div class="intro">
                  <span class="font-title-color">ERP内码：</span>
                  {{ erpCustomerInfo.customerErpCode || '- -' }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- EAS信息 -->
        <div v-if="detailData.showEasInfoFlag">
          <!-- 商务联系人信息 -->
          <div class="header-bar mar-b-10 header-renative">
            <div class="sign"></div>EAS信息
          </div>
          <div class="content-box">
            <div class="content-box-bottom" v-if="easInfoList && easInfoList.length">
              <el-row
                class="box"
                v-for="item in easInfoList"
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
              </el-row>
            </div>
            <div v-else class="font-light-color" style="padding: 16px;">
              暂无EAS信息
            </div>
          </div>
        </div>
        <!-- 资质信息 -->
        <!-- <div class="header-bar mar-b-10"><div class="sign"></div>资质信息</div>
      <div class="flex-wrap img-view">
        <div class="item" v-for="(item, index) in [1,2,3,4,5, 6]" :key="index">
          <div class="img-box flex-row-center">
            <img src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.qftouch.com%2Fyouhgn%2F5b7f7b7761024.jpg&refer=http%3A%2F%2Fimg.qftouch.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626580169&t=1bebbffbf57e892d95222f07c1a692db">
          </div>
          <div class="pic-desc">企业资质证书</div>
        </div>
        </div>-->
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getCustomerDetail,
  getCustomerGroupList,
  updateCustomer
} from '@/subject/pop/api/channel';
import { mapGetters } from 'vuex'

export default {
  components: {},
  computed: {
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
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
          title: '客户列表',
          path: '/clientele/list'
        },
        {
          title: '编辑资料'
        }
      ],
      loading: false,
      customerInfo: {},
      erpCustomerInfo: {},
      //支付方式
      paymentMethodList: [],
      //支付方式 选中的
      paymentMethodIds: [],
      // 账期额度信息
      paymentDaysInfo: {},
      // 客户分组列表
      groupList: [],
      // EAS信息
      easInfoList: [],
      detailData: {},
      customerGroupId: undefined
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.getDetail();
    this.getCustomerGroupList();
  },
  methods: {
    async getDetail() {
      this.$common.showLoad();
      let params = this.params;
      let detailData = await getCustomerDetail(params.customerEid, params.eid);
      this.$common.hideLoad();
      if (detailData) {
        this.detailData = detailData
        this.easInfoList = detailData.easInfoList
        this.customerInfo = detailData.customerInfo;
        this.erpCustomerInfo = detailData.erpCustomerInfo;
        this.paymentMethodList = detailData.paymentMethodList;
        this.paymentDaysInfo = detailData.paymentDaysInfo ? detailData.paymentDaysInfo : {};

        let paymentArray = [];
        detailData.paymentMethodList.forEach(item => {
          if (item.checked) {
            paymentArray.push(item.id);
          }
        });
        this.paymentMethodIds = paymentArray;

        this.customerGroupId = detailData.customerInfo.customerGroupId || undefined;
      }
    },
    //客户分组列表
    async getCustomerGroupList() {
      let groupData = await getCustomerGroupList(1, 999, 0, 0);
      if (groupData) {
        this.groupList = groupData.records;
      }
    },
    // 修改客户信息
    async saveClick() {
      this.$common.showLoad();
      let params = this.params;
      let data = await updateCustomer(
        params.customerEid,
        this.paymentMethodIds,
        this.customerGroupId
      );
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('保存成功');
      }
      this.$log('data:', data);
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
