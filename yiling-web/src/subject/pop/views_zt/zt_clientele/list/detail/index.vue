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
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业名称：</span>
                  {{ customerInfo && customerInfo.name }}
                </div>
              </el-col>
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业类型：</span>
                  {{ customerInfo && $options.filters.dictLabel(customerInfo.type, enterpriseType) }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业地址：</span>
                  {{ customerInfo && customerInfo.address }}
                </div>
              </el-col>
              <el-col :span="12">
                <div class="intro">
                  <span class="font-title-color">企业联系电话：</span>
                  {{ customerInfo && customerInfo.contactorPhone }}
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
      </div>
      <!-- ERP信息 -->
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>ERP信息
        </div>
        <div class="content-box">
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
      </div>
      <!-- 客户分组 -->
      <!-- <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>客户分组
        </div>
        <div class="content-box">
          <el-select style="width: 387px;" v-model="customerGroupId" placeholder="请选择客户分组">
            <el-option v-for="item in groupList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
      </div> -->
      <!-- 支付方式 -->
      <!-- <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>支付方式选择
        </div>
        <div class="check-box">
          <div class="content-box">
            <el-checkbox-group v-model="paymentMethodIds">
              <span v-for="item in paymentMethodList" :key="item.id">
                <el-checkbox :label="item.id">{{ item.name }}</el-checkbox>
                <span class="check-tip font-size-base">{{ item.remark }}</span>
              </span>
            </el-checkbox-group>
          </div>
        </div>
      </div> -->
      <div class="top-bar">
        <!-- 其他信息 -->
        <div class="header-bar mar-b-10">
          <div class="sign"></div>其他信息
        </div>
        <div class="check-box">
          <div class="content-box">
            <div class="flex-row-left">
              <span class="font-title-color font-size-base">使用产品线：</span>
              <el-checkbox-group v-model="useLineList">
                <el-checkbox v-for="item in customerLineList" :key="item.useLine" :label="item.useLine">{{ item.useLineName }}</el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </div>
      </div>
      <div class="top-bar" v-if="certificateList.length > 0">
        <!-- 资质信息 -->
        <div class="header-bar mar-b-10"><div class="sign"></div>资质信息</div>
        <div class="flex-wrap img-view">
          <div class="item" v-for="(item, index) in certificateList" :key="index">
            <div class="img-box flex-row-center" @click="imgClick(item)">
              <img object-fit="contain" :src="item.fileUrl">
            </div>
            <div class="pic-desc">
              <div class="title">{{ item.type | dictLabel(enterpriseCertificateType) }}</div>
              <div class="time">资质有效期 {{ item.periodBegin | formatDate }} ~ {{ item.periodEnd | formatDate }}</div>
            </div>
          </div>
        </div>
      </div>
      <!-- 图片放大 -->
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
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
  updateLine
} from '@/subject/pop/api/zt_api/client';
import { enterpriseType, enterpriseCertificateType } from '@/subject/pop/utils/busi';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'

export default {
  components: {
    ElImageViewer
  },
  computed: {
    enterpriseType() {
      return enterpriseType();
    },
    enterpriseCertificateType() {
      return enterpriseCertificateType();
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
          title: '客户列表',
          path: '/zt_clientele/list'
        },
        {
          title: '查看'
        }
      ],
      loading: false,
      customerInfo: {},
      erpCustomerInfo: {},
      //支付方式
      paymentMethodList: [],
      //支付方式 选中的
      paymentMethodIds: [],
      // 其他信息 - 使用产品线
      customerLineList: [],
      useLineList: [],
      // 客户分组列表
      groupList: [],
      // EAS信息
      easInfoList: [],
      detailData: {},
      customerGroupId: undefined,
      showViewer: false,
      // 资质信息
      certificateList: [],
      imageList: []
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.getDetail();
  },
  methods: {
    // 点击图片
    imgClick(val) {
      if (val.fileUrl && val.fileUrl != '') {
        this.imageList = [val.fileUrl];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
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
        // 产品线数据
        this.customerLineList = detailData.customerLineList;
        this.certificateList = detailData.certificateList
        // 使用产品线
        let useLineList = [];
        detailData.customerLineList.forEach(item => {
          if (item.checked) {
            useLineList.push(item.useLine);
          }
        });
        this.useLineList = useLineList;

        this.customerGroupId = detailData.customerInfo.customerGroupId || undefined;
      }
    },
    // 修改使用产品线
    async saveClick() {
      this.$common.showLoad();
      let params = this.params;
      let data = await updateLine(
        params.customerEid,
        this.customerInfo.name,
        this.useLineList
      );
      this.$common.hideLoad();
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
