<template>
  <div class="top-bar flex-row-left">
    <img class="top-bar-img" src="@/common/assets/admin-logo.png" />
    <el-scrollbar style="flex: 1;">
      <el-menu :default-active="platform" class="yl-menu" mode="horizontal" @select="handleSelect">
        <el-menu-item
          v-for="(item, index) in topList"
          :key="index"
          :index="item.id + ''">
          {{ item.menuName }}
          <div :class="platform == item.id ? 'position' : ''"></div>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
    <down-load></down-load>
    <div class="avatar font-size-base flex-row-left">
      <img class="avatar-img" src="@/common/assets/avatar.png" />
      <el-dropdown class="touch" trigger="click" @command="handleCommand">
        <span class="title">
          {{ userInfo && userInfo.name ? userInfo.name : '' }}<i class="el-icon-caret-bottom el-icon--right bold"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if="isDev" command="test"><span class="col-up">路由调试：</span><span :class="[routeTestModel ? 'col-down' : 'col-up']">{{ routeTestModel ? '开' : '关' }}</span></el-dropdown-item>
          <el-dropdown-item command="editPsw">修改密码</el-dropdown-item>
          <el-dropdown-item command="logout">立即退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import DownLoad from './DownLoad'
  import { handleSelect } from '@/subject/admin/utils'

  export default {
    name: 'TopBar',
    components: {
      DownLoad
    },
    data() {
      return {
        routeTestModel: sessionStorage.getItem('YY_ROUTE_TEST_MODEL') == 1
      }
    },
    computed: {
      ...mapGetters([
        'platform',
        'userInfo',
        'topList',
        'dict'
      ]),
      isDev() {
        return process.env.NODE_ENV === 'dev'
      }
    },
    mounted() {
      if (!this.dict || this.dict.length === 0) {
        this.$store.dispatch('app/getDict')
      }
      if (!this.topList.length) {
        setTimeout(() => {
          this.$store.dispatch('app/getTopList')
        }, 50)
      }
    },
    methods: {
      handleSelect,
      async handleCommand(command) {
        if (command === 'logout') {
          this.$common.showLoad()
          await this.$store.dispatch('user/resetToken')
          this.$common.hideLoad()
          this.$router.replace('/login')
        } else if (command === 'editPsw') {
          this.$router.replace('/editPass')
        } else if (command === 'test') {
          this.routeTestModel = !this.routeTestModel
          sessionStorage.setItem('YY_ROUTE_TEST_MODEL', this.routeTestModel ? 1 : 0)
          this.$common.n_success(this.routeTestModel ? '已开启路由调试模式' : '已关闭路由调试模式')
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .top-bar {
    position: relative;
    height: $topBarHeight;
    &-img {
      width: $sideBarWidth;
      padding: 2px 16px;
    }
  }
  ::v-deep .el-menu--horizontal>.el-menu-item,.el-menu--horizontal>.el-submenu {
    float: none !important;
    display: inline-block !important;
  }
  ::v-deep .el-scrollbar .el-scrollbar__wrap .el-scrollbar__view{
    white-space: nowrap;
  }
  .yl-menu {
    flex: 1;
    font-weight: 500;
    height: 60px;
    width: 100%;
    &.el-menu.el-menu--horizontal {
      position: relative;
      border-bottom: none;
      .el-menu-item {
        border-bottom: none;
        .is-active {
          color: #333;
        }
      }
    }
    .position {
      width: 20%;
      height: 4px;
      background-color: #1482f0;
      position: absolute;
      bottom: 2px;
      left: 40%;
      border-radius: 4px;
      overflow: hidden;
    }
  }
  .avatar {
    padding: 0 16px;
    .avatar-img {
      width: 35px;
      height: 35px;
      margin-right: 10px;
    }
    .title {
      font-weight: 600;
      color: #34335B;
    }
  }
</style>
