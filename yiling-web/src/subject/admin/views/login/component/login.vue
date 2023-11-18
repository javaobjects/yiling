<template>
  <div class="login-view">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form-view"
      auto-complete="on"
      label-position="left">
      <el-form-item prop="userName">
        <div class="flex-row-left">
          <span class="svg-container flex-row-center">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="userName"
            v-model="loginForm.userName"
            placeholder="请输入用户名称"
            name="userName"
            type="text"
            tabindex="1"
            autocomplete="on"
            clearable
          />
        </div>
      </el-form-item>

      <el-form-item prop="password" v-if="!tab">
        <div class="flex-row-left">
          <span class="svg-container flex-row-center">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            key="password"
            ref="password"
            v-model="loginForm.password"
            type="password"
            placeholder="请输入用户密码"
            name="password"
            tabindex="2"
            autocomplete="on"
            @keyup.enter.native="handleLogin"
          />
        </div>
      </el-form-item>

      <el-form-item prop="smsCode">
        <div class="flex-row-left">
          <el-input
            ref="smsCode"
            v-model="loginForm.smsCode"
            placeholder="请输入验证码"
            name="smsCode"
            type="text"
            tabindex="3"
            @keyup.enter.native="handleLogin"
          />
          <div class="margin"></div>
          <div class="captcha" v-if="!tab">
            <img :src="codeSrc.url" @click="refreshCode">
          </div>
          <div class="captcha flex-row-center" v-else>
            <yl-button type="text" :loading="smsLoad" :disabled="disable" @click="getSmsCode">
              {{ sms.begin === 0 ? '获取验证码' : sms.timeLeft + '秒后重发' }}
            </yl-button>
          </div>
        </div>
      </el-form-item>
      <yl-button
        btn-class="my-btn-box"
        :loading="loading"
        type="danger"
        :disabled="loading"
        class="mart5"
        @click="handleLogin">
        登录
        <div class="error" v-show="errMsg">
          {{ errMsg }}
        </div>
      </yl-button>
<!--      <div class="forget" v-if="!tab" @click="goForget">-->
<!--        <span>忘记密码</span>-->
<!--      </div>-->
    </el-form>
  </div>
</template>

<script>
  import { validateTel } from '@/subject/admin/utils/validate'
  import { refreshVcodeUrl, getLoginSmsCode } from '@/subject/admin/api/common'

  const COUNT = 60

  export default {
    name: 'Login',
    props: {
    },
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入用户手机号码'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error('密码不得小于6位数'))
        } else {
          callback()
        }
      }
      const validateSms = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入验证码'))
        } else if (value && value.length < 4) {
          callback(new Error('请输入正确的验证码'))
        } else {
          callback()
        }
      }
      return {
        loginForm: {
          userName: '',
          password: '',
          smsCode: ''
        },
        loginRules: {
          userName: [{ required: true, trigger: 'blur', validator: validateUsername }],
          password: [{ required: true, trigger: 'blur', validator: validatePassword }],
          smsCode: [{ required: true, trigger: 'blur', validator: validateSms }]
        },
        loading: false,
        smsLoad: false,
        codeSrc: {},
        tab: false,
        sms: {
          timeLeft: COUNT,
          begin: 0
        },
        errMsg: '',
        redirect: undefined
      }
    },
    computed: {
      disable() {
        return this.checkMyPhone()
      }
    },
    watch: {
      tab(val) {
        if (this.loginForm.password) {
          this.loginForm.password = ''
        }
        if (this.loginForm.smsCode) {
          this.loginForm.smsCode = ''
        }
        this.$refs.loginForm.clearValidate()
      },
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    mounted() {
      if (this.loginForm.userName === '') {
        this.$refs.userName.focus()
      }
      // else if (this.loginForm.password === '') {
      //   this.$refs.password.focus()
      // }
      this.codeSrc = refreshVcodeUrl()
      this.timer = null
    },
    beforeDestroy() {
      clearInterval(this.timer)
      this.timer = null
    },
    methods: {
      // 登录
      handleLogin() {
        this.errMsg = ''
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            let dispatchStr = 'user/login'
            if (this.tab) {
              dispatchStr = 'user/smsLogin'
            }
            this.loginForm.code = this.codeSrc.code
            this.$log(this.loginForm)
            this.$store.dispatch(dispatchStr, this.loginForm).then(async (data) => {
              await this.$store.dispatch('app/getTopList')
              this.$router.push({ path: this.redirect || '/' })
              this.loading = false
            }).catch((err) => {
              if (err && err.code) {
                this.errMsg = err.message
              }
              this.refreshCode()
              this.loading = false
            })
          } else {
            return false
          }
        })
      },
      // 获取图形验证
      refreshCode() {
        this.codeSrc = refreshVcodeUrl()
      },
      // 获取短信
      async getSmsCode() {
        this.smsLoad = true
        this.errMsg = ''
        let { userName } = this.loginForm
        let data = await getLoginSmsCode(userName)
        this.smsLoad = false
        if (data && data.result) {
          this.countDownFn(this.sms.timeLeft)
        }
      },
      // 倒计时动作
      countDownFn(timeLeft) {
        if (timeLeft > 0) {
          this.sms.begin = 1
          this.timer = setInterval(() => {
            if (this.sms.timeLeft < 1) {
              clearInterval(this.timer)
              this.sms.begin = 0
              this.sms.timeLeft = COUNT
            } else {
              this.sms.timeLeft -= 1
            }
          }, 1000)
        }
      },
      // 校验手机号正确性
      checkMyPhone() {
        if (this.sms.begin === 1){
          return true
        }
        let { userName } = this.loginForm
        if (!userName) {
          return true
        } else {
          if (!validateTel(userName)) {
            return true
          }
        }
        return false
      }
    }
  }
</script>
<style lang="scss">
  .login-form-view {
    .el-input {
      display: inline-block;
      height: 50px;
      width: 289px;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        color: $font-important-color;
        height: 50px;
        caret-color: $font-important-color;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px #F7F8F9 inset !important;
          -webkit-text-fill-color: $font-important-color !important;
        }
      }
    }
    .el-form-item {
      background: #F7F8F9;
      border-radius: 4px;
      margin-bottom: 24px !important;
    }
    .my-btn-box {
      width: 100%;
      height: 50px;
      /*background: #D7000F;*/
      /*border-color: #D7000F;*/
      border-radius: 4px;
      font-size: 18px;
      font-weight: 500;
      margin-bottom: 20px;
      position: relative;
    }
  }
</style>
<style lang="scss" scoped>
  @import "~@/common/styles/mixin.scss";

  .login-view {
    background: $white;
    box-shadow: 0px 2px 15px 0px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    width: 372px;
    height: 426px;
    padding: 28px;

    .tab {
      position: relative;
      height: 32px;
      font-size: 16px;
      font-weight: 400;
      color: #333333;
      line-height: 28px;
      text-align: center;
      border-bottom: 1px solid #EAEAEA;
      .border-px {
        height: 20px;
        width: 1px;
        border-right: 1px solid #EAEAEA;
      }
      .table-item {
        flex: 1;
        transition: all .3s ease-in-out;
        cursor: pointer;
        &.active {
          font-size: 20px;
          font-weight: 600;
        }
      }
      .bottom-line {
        position: absolute;
        bottom: -1px;
        left: 55px;
        width: 48px;
        height: 1px;
        background-color: #D7000F;
        transition: all .5s ease-in-out;
      }
      .active {
        &.bottom-line {
          left: 213px;
        }
      }
    }
    .login-form-view {
      padding-top: 24px;
      color: $font-important-color !important;
      .svg-container {
        font-size: 15px;
        margin-left: 12px;
        color: #BFBFBF;
        height: 50px;
      }
      .captcha {
        width: 112px;
        height: 50px;
        position: relative;
        border-radius: 4px;
        overflow: hidden;
        cursor: pointer;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .margin {
        width: 12px;
        background-color: $white;
        height: 50px;
      }
      .mart5 {
        margin-top: 6px;
      }
      .forget {
        text-align: right;
        height: 20px;
        font-size: 14px;
        font-weight: 400;
        color: #666666;
        line-height: 20px;
        cursor: pointer;
      }
      .error {
        position: absolute;
        top: -26px;
        left: 0;
        font-size: 14px;
        font-weight: 400;
        color: #D7000F;
        line-height: 20px;
        @include clamp(1);
      }
    }
  }
</style>
