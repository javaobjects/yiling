<template>
  <div class="app-container">
    <div class="app-container-content">
      <!-- 顶部，头像，日历等 -->
      <div class="home-top flex-between">
        <div class="home-top-left">
          <div class="user-box">
            <div class="user-head">
              <img class="user-img mar-l-16" src="@/common/assets/avatar.png" />
              <img class="user-tip" src="../../assets/home/user-tips.png" alt="">
            </div>
            <span class="user-name mar-l-16">{{ currentEnterpriseInfo.name ? currentEnterpriseInfo.name : '' }}</span>
            <div class="user-tips mar-l-16">已认证</div>
          </div>
          <div class="short-func">
            <div class="search-bar">
              <div class="header-bar mar-b-10">
                <div class="sign"></div>快捷功能
              </div>
            </div>
            <!-- 以岭快捷功能 -->
            <div class="func-box flex-between">
              <div v-route-role="['zt:zt_enterprise:zt_enterprise_information']" class="func-item" @click="clickItem(1)">
                <img src="../../assets/zt-home/item-1.png" alt="">
                <div>企业信息</div>
              </div>
              <div v-route-role="['zt:product:index']" class="func-item" @click="clickItem(2)">
                <img src="../../assets/zt-home/item-2.png" alt="">
                <div>商品列表</div>
              </div>
              <div v-if="erpSyncLevel === 0" v-route-role="['zt:product:add']" class="func-item" @click="clickItem(3)">
                <img src="../../assets/zt-home/item-3.png" alt="">
                <div>添加商品</div>
              </div>
              <div v-route-role="['zt:roles:department']" class="func-item" @click="clickItem(4)">
                <img src="../../assets/zt-home/item-4.png" alt="">
                <div>部门管理</div>
              </div>
              <div v-route-role="['zt:roles:user']" class="func-item" @click="clickItem(5)">
                <img src="../../assets/zt-home/item-5.png" alt="">
                <div>员工管理</div>
              </div>
            </div>
          </div>
        </div>
        <div class="home-top-right">
          <div class="search-bar">
            <div class="header-bar">
              <div class="sign"></div>日历
            </div>
          </div>
          <div class="calendar-top">
            <div class="calendar-box">
              <div class="calendar-date">{{ date | formatDate('yyyy-MM') }}</div>
              <el-button-group class="calendar-box">
                <el-button @click="change('prev-month')" class="calendar-btn calendar-btn-l" type="primary" size="mini" icon="el-icon-arrow-left"></el-button>
                <el-button @click="change('next-month')" class="calendar-btn" type="primary" size="mini" icon="el-icon-arrow-right"></el-button>
              </el-button-group>
            </div>
          </div>
          <!-- 日历 -->
          <el-calendar class="el-calendar-box" ref="calendar" v-model="date"></el-calendar>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import routeRole from '@/subject/pop/directive/routeRole'
import { queryCurrentEnterpriseInfo } from '@/subject/pop/api/zt_api/dashboard'

export default {
  name: 'ZtDashboard',
  directives: {
    routeRole
  },
  computed: {
    ...mapGetters(['currentEnterpriseInfo'])
  },
  data() {
    return {
      date: new Date(),
      // 	企业是否开通erp 0：未开通 1：开通
      erpSyncLevel: 0
    }
  },
  mounted() {
    this.getCurrentEnterpriseInfo()
  },
  methods: {
    change(type) {
      this.$refs.calendar.selectDate(type)
    },
    clickItem(type) {
      switch (type) {
        case 1:
          this.$router.push({
            name: 'ZtEnterpriseInformation'
          })
          break;
        case 2:
          this.$router.push({
            name: 'ZtProductsList'
          })
          break;
        case 3:
          this.$router.push({
            name: 'ZtProductsAdd'
          })
          break;
        case 4:
          this.$router.push({
            name: 'ZtRolesDepartment'
          })
          break;
        case 5:
          this.$router.push({
            name: 'ZtRolesUser'
          })
          break;
        default:
          break;
      }
    },
    // 获取当前登录人企业信息(是否已对接ERP)
    async getCurrentEnterpriseInfo() {
      this.$common.showLoad()
      let data = await queryCurrentEnterpriseInfo()
      this.$common.hideLoad()
      if (data && data.enterpriseInfo) {
        this.erpSyncLevel = data.enterpriseInfo.erpSyncLevel
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
