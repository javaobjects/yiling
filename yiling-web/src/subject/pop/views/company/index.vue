<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="search-bar">
        <div class="header-bar mar-b-10"><div class="sign"></div>客户基本信息</div>
        <div class="flex-row-left">
          <div class="flex1">
            <div class="intro"><span class="font-title-color">企业名称：</span>{{ company.name }}</div>
            <div class="intro"><span class="font-title-color">企业类型：</span>{{ company.type | dictLabel(companyType) }}</div>
            <div class="intro"><span class="font-title-color">企业所属区域：</span>{{ company.provinceName }}{{ company.cityName }}{{ company.regionName }}{{ company.address }}</div>
          </div>
          <div class="flex1">
            <div class="intro"><span class="font-title-color">企业营业执照号/医疗机构许可证号：</span>{{ company.licenseNumber }}</div>
            <div class="intro"><span class="font-title-color">联系人姓名：</span>{{ company.contactor }}</div>
            <div class="intro"><span class="font-title-color">联系人手机号：</span>{{ company.contactorPhone }}</div>
          </div>
        </div>
      </div>
      <div class="bottom-box" v-if="false">
        <div class="header-bar"><div class="sign"></div>企业资质信息</div>
        <div class="flex-wrap img-view">
          <div class="item" v-for="(item, index) in [1,2,3,4,5, 6]" :key="index">
            <div class="img-box flex-row-center">
              <img src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.qftouch.com%2Fyouhgn%2F5b7f7b7761024.jpg&refer=http%3A%2F%2Fimg.qftouch.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626580169&t=1bebbffbf57e892d95222f07c1a692db">
            </div>
            <div class="pic-desc">企业资质证书</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCompanyInfo } from '@/subject/pop/api/company'
import { enterpriseType } from '@/subject/pop/utils/busi'

export default {
  name: 'CompanyIndex',
  components: {
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
          path: '/dashboard'
        },
        {
          title: '企业管理',
          path: ''
        },
        {
          title: '企业信息查询'
        }
      ],
      company: {}
    }
  },
  activated() {
    this.getInfo()
  },
  methods: {
    async getInfo() {
      this.$common.showLoad()
      let data = await getCompanyInfo()
      this.$common.hideLoad()
      if (data && data.enterpriseInfo) {
        this.company = data.enterpriseInfo
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
