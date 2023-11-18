<template>
  <div class="top-bar flex-row-left">
    <img class="top-bar-img" src="@/common/assets/login-logo.png" />
    <el-scrollbar style="flex: 1;">
      <el-menu :default-active="platform" class="yl-menu" mode="horizontal" @select="handleSelect">
        <el-menu-item
          v-for="(item, index) in topList"
          v-show="item.name"
          :key="index"
          :index="(item.appId + '')">
          {{ item.name }}
          <div :class="platform == item.appId ? 'position' : ''"></div>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
    <div v-if="showPop" class="pop-go flex-row-center" @click="goPop">
      <img class="pop-go-img" src="@/common/assets/topbar/pop-btn-start.png">
      <div class="box border-1px-l">
        POP前台
      </div>
    </div>
    <down-load class="mar-l-16"></down-load>
    <div class="avatar font-size-base flex-row-left">
      <img class="avatar-img" src="@/common/assets/avatar.png" />
      <div>
        <el-dropdown class="touch" trigger="click" @command="handleCommand">
          <span class="title">
            {{ companyName }}<i class="el-icon-caret-bottom el-icon--right bold"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-if="isDev" command="test"><span class="col-up">路由调试：</span><span :class="[routeTestModel ? 'col-down' : 'col-up']">{{ routeTestModel ? '开' : '关' }}</span></el-dropdown-item>
            <el-dropdown-item command="transfer">切换企业</el-dropdown-item>
            <el-dropdown-item command="change">手机号换绑</el-dropdown-item>
            <el-dropdown-item command="reset">重置登录密码</el-dropdown-item>
            <el-dropdown-item command="logout">立即退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <div class="user-title">登录人：{{ userInfo && userInfo.name ? userInfo.name : '' }}</div>
      </div>
    </div>
    <yl-dialog
      width="40%"
      title="请选择一个要切换的企业"
      :show-footer="false"
      :visible.sync="showDialog">
      <div class="d-content">
        <el-radio-group style="width: 100%;" v-model="choose">
          <el-row style="width: 100%;">
            <el-col
              class="mat-b-20"
              v-for="(item, index) in companyList"
              :key="index"
              :span="12">
              <el-radio :label="item.id">{{ item.name }}</el-radio>
            </el-col>
          </el-row>
        </el-radio-group>
        <div class="flex-row-center btn">
          <yl-button style="width: 212px;" :disabled="choose == null || !choose || choose <= 0" type="danger" @click="goHome">
            切换企业
          </yl-button>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import DownLoad from './DownLoad'
  import router, { resetRouter } from '@/subject/pop/router'
  import { handleSelect } from '@/subject/pop/utils'
  import { getMyCompanyList } from '@/subject/pop/api/user'

  export default {
    name: 'TopBar',
    components: {
      DownLoad
    },
    data() {
      return {
        showDialog: false,
        companyList: [],
        choose: null,
        routeTestModel: sessionStorage.getItem('ROUTE_TEST_MODEL') == 1
      }
    },
    computed: {
      ...mapGetters([
        'platform',
        'userInfo',
        'userCompany',
        'token',
        'dict',
        'topList',
        'currentEnterpriseInfo'
      ]),
      isDev() {
        return process.env.NODE_ENV === 'dev'
      },
      companyName() {
        return this.currentEnterpriseInfo ? this.currentEnterpriseInfo.name : ''
      },
      showPop() {
        return !!this.topList.find(item => item.appId == 2)
      }
    },
    // watch: {
    //   $route: {
    //     handler: function(route) {
    //       const query = route.query
    //       if (query && typeof query.tab_platform !== 'undefined' && query.tab_platform) {
    //         const platform = query.tab_platform
    //         console.log(this.platform, query.tab_platform, this.platform != query.tab_platform)
    //         setTimeout(() => {
    //           if (this.platform != query.tab_platform) {
    //             this.$store.dispatch('app/getTopList').then(topList => {
    //               if (topList.find(item => item.appId == platform)) {
    //                 this.handleSelect(platform, route.path !== '/403' ? route.fullPath : '')
    //               }
    //             })
    //           } else {
    //             this.$router.push(route.path !== '/403' ? route.fullPath : '/')
    //           }
    //         }, 200)
    //       }
    //     },
    //     immediate: true
    //   }
    // },
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
          if (process.env.NODE_ENV === 'dev') {
            this.$router.replace('/login')
          } else {
            this.$common.goLogin()
          }
        } else if (command === 'transfer') {
          this.getCompanyList((list) => {
            if (list.length > 1) {
              if (this.userCompany) {
                this.choose = parseInt(this.userCompany)
              }
              this.showDialog = true
            } else {
              this.$common.warn('暂无可以切换的企业')
            }
          })
        } else if (command === 'change') {
          this.$router.push('/change_phone')
        } else if (command === 'reset') {
          this.$router.push('/forget_pass')
        } else if (command === 'test') {
          this.routeTestModel = !this.routeTestModel
          sessionStorage.setItem('ROUTE_TEST_MODEL', this.routeTestModel ? 1 : 0)
          this.$common.n_success(this.routeTestModel ? '已开启路由调试模式' : '已关闭路由调试模式')
        }
      },
      // 获取企业列表
      async getCompanyList(callback) {
        this.$common.showLoad()
        let data = await getMyCompanyList(this.token)
        this.$common.hideLoad()
        if (data) {
          this.companyList = data.list
          if (callback) callback(data.list)
        }
      },
      // 变更企业
      async goHome() {
        this.$common.showLoad()
        let data = await this.$store.dispatch('user/changeCompany', {id: this.choose, token: this.token})
        if (data) {
          this.showDialog = false
          resetRouter()
          await this.$store.dispatch('permission/setRoutes')
          let platform = await this.$store.dispatch('app/getPlatform', this.token)
          let accessRoutes = await this.$store.dispatch('permission/initRoutes', platform)
          router.addRoutes(accessRoutes)
          let routeStr = ''
          if (accessRoutes.length > 1 && typeof accessRoutes[1].children !== 'undefined') {
            for (let i= 0; i< accessRoutes[1].children.length; i++) {
              const item = accessRoutes[1].children[i]
              if (!item.hidden) {
                routeStr = accessRoutes[1].path + '/' + item.path
                break
              }
            }
          }
          this.$common.hideLoad()
          this.$router.push(routeStr || '/')
          this.$common.n_success('切换成功')
          // this.$router.push({ path: this.redirect || '/' })
        }
      },
      goPop() {
        this.$common.goPackage(99, {timestamp: new Date().getTime()}, true)
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
    .user-title {
      height: 17px;
      font-size: 12px;
      font-weight: 500;
      color: #999999;
      line-height: 17px;
    }
  }
  .pop-go {
    width: 100px;
    height: 24px;
    background: #FFFFFF;
    border-radius: 2px;
    opacity: 0.51;
    border: 1px solid #AEAEAE;
    cursor: pointer;
    &-img {
      width: 14px;
      height: 14px;
      line-height: 10px;
      margin-right: 5px;
    }
    .box {
      padding-left: 5px;
      font-size: 14px;
      font-weight: 400;
      color: #333333;
      line-height: 10px;
    }
    &:hover {
      border: 1px solid $primary;
    }
  }
  .d-content {
    padding: 38px 60px;
    .mat-b-20 {
      margin-bottom: 20px;
      text-align: left;
    }
    .btn {
      margin-top: 80px;
    }
  }
</style>
