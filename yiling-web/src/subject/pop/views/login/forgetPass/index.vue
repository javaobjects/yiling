<template>
  <div class="login-container">
    <div class="top-box">
      <img @click="$router.replace('/')" src="@/common/assets/login-logo.png" />
    </div>
    <div class="login-box">
      <div class="main-view">
        <div class="step-box">
          <el-steps :active="active" finish-status="success">
            <el-step title="输入要找回的账号并验证"></el-step>
            <el-step title="重置密码"></el-step>
            <el-step title="成功"></el-step>
          </el-steps>
        </div>

        <el-carousel class="carousel" ref="carousel" indicator-position="none" arrow="never" :initial-index="0" :autoplay="false">
          <el-carousel-item>
            <div class="content-box flex-row-center">
              <el-form ref="form" :model="form" :rules="rules" auto-complete="on" label-width="95px" label-position="right">
                <el-form-item class="form-1" label="手机号码:" prop="userName">
                  <el-input ref="userName" v-model="form.userName" placeholder="请输入手机号码" type="text" tabindex="1" autocomplete="on" clearable />
                </el-form-item>
                <el-form-item class="form-2" label="验证码:" prop="smsCode">
                  <div class="flex-row-left">
                    <el-input class="flex1" ref="smsCode" v-model="form.smsCode" placeholder="请输入验证码" type="text" />
                    <div class="captcha">
                      <yl-sms-count :mobile="form.userName" type="1" />
                    </div>
                  </div>
                </el-form-item>
                <el-form-item label="">
                  <yl-button class="btn-1" type="danger" @click="handleCheckSms">确认</yl-button>
                </el-form-item>
              </el-form>
            </div>
          </el-carousel-item>
          <el-carousel-item>
            <div class="content-box flex-row-center">
              <el-form ref="form1" :model="form1" :rules="rules1" auto-complete="on" label-width="152px" label-position="right">
                <el-form-item class="form-1" label="手机号码:">
                  <span>{{ form1.userName ? form1.userName.substr(0, 3) + '****' + form1.userName.slice(-4) : '' }}</span>
                </el-form-item>
                <el-form-item class="form-3" label="新的登录密码:" prop="password">
                  <el-input ref="passwordType" v-model="form1.password" placeholder="请输入8-16位字母+数字组合" :type="passwordType">
                    <div slot="suffix" class="show-pwd" @click="showPwd">
                      <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </div>
                  </el-input>
                </el-form-item>
                <el-form-item class="form-3" label="确认新的登录密码:" prop="password1">
                  <el-input ref="passwordType1" v-model="form1.password1" placeholder="请输入8-16位字母+数字组合" :type="passwordType1">
                    <div slot="suffix" class="show-pwd" @click="showPwd1">
                      <svg-icon :icon-class="passwordType1 === 'password' ? 'eye' : 'eye-open'" />
                    </div>
                  </el-input>
                </el-form-item>
                <el-form-item label="">
                  <yl-button class="btn-2" type="danger" @click="changePass">确认</yl-button>
                  <div class="remark">
                    重置成功后，平台其他产品的登录密码也将同步修改
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script>
import { ylSmsCount } from '@/subject/pop/components'
import { testSmsCode, resetPass } from '@/subject/pop/api/user'
import { validateTel } from '@/subject/pop/utils/validate'

export default {
  components: {
    ylSmsCount
  },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validateTel(value)) {
        callback(new Error('请输入正确的用户手机号码'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      let reg = /(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('请输入正确的8-16位字母+数字组合'))
      } else {
        callback()
      }
    }
    const validatePassword1 = (rule, value, callback) => {
      let reg = /(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('请输入正确的8-16位字母+数字组合'))
      } else if (value != this.form1.password) {
        callback(new Error('两次密码输入不一致，请重新填写'))
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
      active: 0,
      form: {
        userName: '',
        smsCode: ''
      },
      rules: {
        userName: [{ required: true, trigger: 'blur', validator: validateUsername }],
        smsCode: [{ required: true, trigger: 'blur', validator: validateSms }]
      },
      form1: {},
      rules1: {
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        password1: [{ required: true, trigger: 'blur', validator: validatePassword1 }]
      },
      passwordType: 'password',
      passwordType1: 'password'
    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    // 校验验证码
    handleCheckSms() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          let { userName, smsCode } = this.form
          this.$common.showLoad()
          let data = await testSmsCode(userName, smsCode)
          this.$common.hideLoad()
          if (data && data.result) {
            this.active = 1
            this.form1.userName = this.form.userName
            this.$refs.carousel.next()
          } else if (data && !data.result) {
            this.$common.error('验证码校验不通过，请重新再试')
          }
        } else {
          return false
        }
      })
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.passwordType.focus()
      })
    },
    showPwd1() {
      if (this.passwordType1 === 'password') {
        this.passwordType1 = ''
      } else {
        this.passwordType1 = 'password'
      }
      this.$nextTick(() => {
        this.$refs.passwordType1.focus()
      })
    },
    // 修改密码
    changePass() {
      this.$refs.form1.validate(async valid => {
        if (valid) {
          let { userName, password } = this.form1
          let { smsCode } = this.form
          this.$common.showLoad()
          let data = await resetPass(userName, password, smsCode)
          this.$common.hideLoad()
          if (data && data.result) {
            this.active = 2
            this.$confirm('您的密码修改成功，请妥善保存您设置的新密码', '提示', {
              confirmButtonText: '返回登录',
              showCancelButton: false,
              showClose: false,
              closeOnClickModal: false,
              type: 'success'
            }).then(() => {
              this.$router.push('/')
            })
          } else if (data && !data.result) {
            this.$common.error('重置密码失败，请稍后再试')
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
.login-container {
  .main-view {
    .carousel {
      .el-carousel__container {
        min-height: 450px;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
