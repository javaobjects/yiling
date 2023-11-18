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
  // import routeRole from '@/subject/pop/directive/routeRole'

  export default {
    name: 'SaleDashboard',
    directives: {
      // routeRole
    },
    computed: {
      ...mapGetters(['currentEnterpriseInfo'])
    },
    data() {
      return {
        date: new Date()
      }
    },
    mounted() {
    },
    methods: {
      change(type) {
        this.$refs.calendar.selectDate(type)
      },
      clickItem(type) {
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
