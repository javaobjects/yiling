<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="top-bar">
        <div class="header-bar mar-b-8">
          <div class="sign"></div>
          店铺公告
        </div>
        <div class="content-box mar-b-16">
          <div class="mar-b-16 item_box">
            <div class="logo_title mar-b-8">店铺公告</div>
            <el-input
              type="textarea"
              :rows="15"
              placeholder="请输入店铺公告"
              maxlength="300"
              show-word-limit
              v-model="shopAnnouncement"
            >
            </el-input>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-bar-view">
        <yl-button v-role-btn="['1']" type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getShopAnnounce, setShopAnnounce } from '@/subject/pop/api/b2b_api/store_manage'

export default {
  name: 'StoreAnnounce',
  components: {},
  mounted() {
    this.getDetail()
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
          title: '店铺管理'
        },
        {
          title: '店铺公告'
        }
      ],
      shopAnnouncement: ''
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getShopAnnounce()
      this.$common.hideLoad()
      if (data) {
        this.shopAnnouncement = data.shopAnnouncement
      }
    },
    async saveClick() {
      this.$common.showLoad()
      let data = await setShopAnnounce(this.shopAnnouncement)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('保存成功')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
