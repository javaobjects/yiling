<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          可售区域
        </div>
        <div class="content-box">
          <el-row>
            <el-col :span="6">
              <span class="title">公司名称：</span>
              <span class="item">{{ saleAreaData.name ? saleAreaData.name : "- -" }}</span>
            </el-col>
            <el-col :span="6">
              <span class="title">可售区域：</span>
              <span class="item">{{ saleAreaData.description || "- -" }}</span>
            </el-col>
            <el-col :span="6">
              <yl-button v-role-btn="['1']" type="text" @click="detail" :disabled="disableCheck">查看</yl-button>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getSaleArea } from '@/subject/pop/api/sale_api/authAdmin';

export default {
  name: 'SaleArea',
  filters: {},
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/sale_dashboard'
        },
        {
          title: '权限管理',
          path: ''
        },
        {
          title: '可售区域'
        }
      ],
      saleAreaData: {},
      disableCheck: false
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {
    this.getDetail()
  },
  methods: {
    // 获取可售区域信息
    async getDetail() {
      this.loading = true
      let data = await getSaleArea()
      this.loading = false
      if (data) {
        this.saleAreaData = data
      } else {
        this.disableCheck = true
      }
    },
    // 查看
    detail() {
      this.$router.push({
        name: 'SaleAreaDetail'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
