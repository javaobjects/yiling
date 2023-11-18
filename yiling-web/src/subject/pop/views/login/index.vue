<template>
  <div class="login-container">
    <div class="top-box">
      <img @click="$router.replace('/')" src="@/common/assets/login-logo.png" />
    </div>
    <div class="login-box">
      <login
        @loginDone="afterLogin"
        class="login-view" />
      <div class="bottom-text">
        <div>
          京ICP备07504877号 | 互联网药品交易服务资格证编号：国A20150007 | 经营许可证编号：京B2-20192540 | 互联网药品信息服务资格证编号：(京)-经营性-2017-0019
        </div>
        <div>
          医疗器械网络交易服务第三方平台备案凭证:（京）网械平台备字（2019）第00006号
        </div>
        <div>
          Copyright©2019 Mypharma.com 北京瑞康医药有限公司 版权所有 京公网安备 11010502037300号
        </div>
      </div>
    </div>
    <yl-dialog
      width="40%"
      title="请选择一个要管理的企业"
      :show-footer="false"
      :visible.sync="showDialog">
      <div class="content">
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
            进入企业
          </yl-button>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import Login from './component/login'
  import { getMyCompanyList } from '@/subject/pop/api/user'
  import { mapGetters } from 'vuex'

  export default {
    components: {
      Login
    },
    data() {
      return {
        redirect: undefined,
        showDialog: false,
        companyList: [],
        choose: null
      }
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    computed: {
      ...mapGetters([
        'token'
      ])
    },
    mounted() {
    },
    methods: {
      // 登陆成功后
      afterLogin(user) {
        this.getList((list) => {
          if (list && list.length === 1) {
            this.choose = list[0].id
            this.goHome()
            return
          }
          if (user.currentEnterpriseInfo) {
            this.choose = user.currentEnterpriseInfo.id
          }
          this.showDialog = true
        })
      },
      // 获取企业列表
      async getList(callback) {
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
        this.$common.hideLoad()
        if (data) {
          await this.$store.dispatch('app/getPlatform', this.token)
          this.showDialog = false
          this.$router.push({ path: this.redirect || '/' })
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .login-container {
    width: 100vw;
    height: 100vh;
    min-height: 700px;
    overflow: auto;
    padding: 0;
    margin: 0;
    .login-box {
      width: 100vw;
      position: relative;
      height: calc(100% - 87px);
      background: url("../../assets/login-back.png") no-repeat center;
      background-size: cover;
      .login-view {
        position: absolute;
        top: 47px;
        right: 243px;
      }
      .bottom-text {
        position: absolute;
        width: 100%;
        bottom: 50px;
        text-align: center;
        div {
          height: 17px;
          font-size: 12px;
          font-weight: 400;
          color: #333333;
          line-height: 17px;
          margin-bottom: 10px;
        }
      }
    }
    .top-box {
      width: 100%;
      height: 87px;
      background: #FFFFFF;
      img {
        width: 230px;
        height: 41px;
        margin: 23px 183px;
      }
    }
    .content {
      padding: 38px 60px;
      .mat-b-20 {
        margin-bottom: 20px;
        text-align: left;
      }
      .btn {
        margin-top: 80px;
      }
    }
  }
  @media (max-width: 1366px) {
    .bottom-text {
        bottom: 0 !important;
    }
  }

</style>
