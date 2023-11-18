<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="search-bar-top" v-if="company.transcriptAuthStatus == 1">
        <!-- 认证状态：1-未认证 2-认证通过 3-认证不通过 -->
        <div class="success" >
          <div class="bar-top-left"><i class="el-icon-success"></i></div>
          <div class="search-bar-top-right">
            <p>提交成功，请等待审核</p>
            <p>预计 5 ~ 10 分钟审核完成，更新资质不影响企业采购，请耐心等待。</p>
          </div>
        </div>
      </div>
      <div class="search-bar-top" v-if="company.transcriptAuthStatus == 3">
        <div class="danger">
            <div class="bar-top-left"><i class="el-icon-error"></i></div>
            <div class="search-bar-top-right">
              <p>审核失败，请重新提交</p>
              <p>驳回说明：{{ company.authRejectReason }}</p>
            </div>
          </div>
        </div>
      <div class="search-bar">
        <div class="header-bar mar-b-10"><div class="sign"></div>客户基本信息</div>
        <div class="flex-row-left">
          <div class="flex1">
            <div class="intro"><span class="font-title-color">企业名称：</span>{{ company.name }}</div>
            <div class="intro"><span class="font-title-color">企业类型：</span>{{ company.type | dictLabel(companyType) }}</div>
            <div class="intro"><span class="font-title-color">企业地址：</span>{{ company.provinceName }}{{ company.cityName }}{{ company.regionName }}</div>
          </div>
          <div class="flex1">
            <div class="intro"><span class="font-title-color">企业营业执照号/医疗机构许可证号：</span>{{ company.licenseNumber }}</div>
            <div class="intro"><span class="font-title-color">联系人姓名：</span>{{ company.contactor }}</div>
            <div class="intro"><span class="font-title-color">联系人手机号：</span>{{ company.contactorPhone }}</div>
          </div>
        </div>
        <div class="flex-row-left">
          <div class="flex1">
            <div class="intro"><span class="font-title-color">详细地址：</span>{{ company.address }}</div>
          </div>
        </div>
      </div>
      <div class="bottom-box">
        <div class="header-bar"><div class="sign"></div>企业资质信息</div>
        <div class="flex-wrap img-view">
          <div class="flex-wrap-div">
            <div class="item" v-for="(item, index) in datalist" :key="index" >
              <div class="img-box flex-row-center">
                <img class="qualification-box-img" :src="item.fileUrl">
                <!-- <el-image class="qualification-box-img" @click="imgClick(item)" :src="item.fileUrl"></el-image> -->
                <p class="grad hover-grad" @click="imgClick(item)">
                  <img src="@/subject/pop/assets/zt_enterprise/enlarge.png" alt="">
                </p>
              </div>
              <div class="pic-desc">
                {{ item.name }}
                <p v-if="item.periodRequired">
                  <span class="font-light-color">资质有效期：</span>
                  <span v-if="item.longEffective == 0">
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - {{ item.periodEnd | formatDate('yyyy-MM-dd') }}
                  </span>
                  <span v-else>
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - 长期有效
                  </span>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
     <div class="bottom-bar-view flex-row-center" v-if="company.editStatus == 1">
      <yl-button v-role-btn="['3']" type="primary" plain @click="modifyClick">修改</yl-button> 
    </div>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { getEnterpriseInfo } from '@/subject/pop/api/zt_api/zt_enterprise'
import { enterpriseType } from '@/subject/pop/utils/busi'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'ZtEnterpriseInformation',
  components: {
    ElImageViewer
  },
  computed: {
    companyType() {
      return enterpriseType()
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
          title: '企业信息管理',
          path: ''
        },
        {
          title: '企业信息查看'
        }
      ],
      company: {},
      datalist: [],
      showViewer: false,
      imageList: []
    }
  },
  activated() {
    this.getInfo()
  },
  methods: {
    async getInfo() {
      let data = await getEnterpriseInfo({});
      if (data != undefined) {
        this.company = data;
        this.datalist = data.certificateVoList;
      }
    },
    // 修改
    modifyClick() {
      this.$router.push('/zt_enterprise/zt_enterprise_modify/');
    },
    imgClick(val) {
      if (val.fileUrl != '') {
        this.imageList = [val.fileUrl];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
