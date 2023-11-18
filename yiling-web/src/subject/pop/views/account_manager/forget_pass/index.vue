<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="search-bar">
        <div class="header-bar">
          <div class="sign"></div>您正在通过“短信验证”设置
        </div>
        <div class="step">
          <steps :active="active" :finish-status="status" :steps="['身份验证', '设置登录密码', '修改成功']" />
        </div>

        <div class="form">
          <el-form :model="form" ref="form" :rules="rules" label-position="right" label-width="200px" class="flex-center">
            <p v-if="active === 1" class="old-text">请输入{{ userInfo.mobile? userInfo.mobile.substr(0, 3) + '****' + userInfo.mobile.slice(-4) : '' }} 收到的短信验证码</p>
            <el-form-item v-if="active === 1" label="验证码：" prop="code">
              <el-input class="inp" type="text" v-model="form.code"></el-input>
              <!-- <yl-button class="mar-l-16" type="primary" size="mini" :loading="smsLoad" :disabled="disable" @click="getSmsCode">
                {{ sms.begin === 0 ? '获取验证码' : sms.timeLeft + '秒后重发' }}
              </yl-button> -->
              <yl-sms-count class="mar-l-16" :mobile="form.oldPhone" type="1" />
            </el-form-item>
            <el-form-item v-if="active === 2" label="新密码：" prop="newPass">
              <el-input class="inp" type="text" v-model="form.newPass" show-password></el-input>
            </el-form-item>
            <el-form-item v-if="active === 2" label="重复输入密码：" prop="newPassAgain">
              <el-input class="inp" type="text" v-model="form.newPassAgain" show-password></el-input>
            </el-form-item>

            <el-form-item>
              <yl-button v-if="active === 1" type="primary" @click="goStep('form')">下一步</yl-button>
              <yl-button v-if="active === 2" type="primary" @click="submitForm('form')">提交</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <yl-dialog title="提示" :visible.sync="show" :show-footer="false" :show-header="false">
      <div class="dialog-content">
        <div class="dia-message">
          <img src="../../../assets/ok.png" alt="">
          您的密码修改成功
        </div>
      </div>
    </yl-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Steps from '@/common/components/Steps';
import { ylSmsCount } from '@/subject/pop/components'
import { testSmsCode, resetPass } from '@/subject/pop/api/user';
export default {
  name: '',
  components: {
    Steps, ylSmsCount
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  // mobile
  data() {
    const validateSms = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入验证码'))
      } else if (value && value.length < 4) {
        callback(new Error('请输入正确的验证码'))
      } else {
        callback()
      }
    }
    const reg = /(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,16}$/
    const validatePassword = (rule, value, callback) => {
      if (!reg.test(value)) {
        callback(new Error('请输入正确的8-16位字母+数字组合'))
      } else {
        callback()
      }
    }
    const validatePassword1 = (rule, value, callback) => {
      if (!reg.test(value)) {
        callback(new Error('请输入正确的8-16位字母+数字组合'))
      } else if (value != this.form.newPass) {
        callback(new Error('两次密码输入不一致，请重新填写'))
      } else {
        callback()
      }
    }
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '账号管理'
        },
        {
          title: '重置登录密码'
        }
      ],
      active: 1,
      status: 'finish',
      form: {
        code: '',
        oldPhone: '',
        newPass: '',
        newPassAgain: ''
      },
      rules: {
        code: [
          { required: true, trigger: 'blur', validator: validateSms }
        ],
        newPass: [{ required: true, trigger: 'blur', validator: validatePassword }],
        newPassAgain: [{ required: true, trigger: 'blur', validator: validatePassword1 }]
      },
      show: false
    }
  },
  created() {
  },
  mounted() {
    this.form.oldPhone = this.$store.getters.userInfo.mobile
  },
  methods: {
    // 提交
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await resetPass(this.form.oldPhone, this.form.newPass, this.form.code)
          this.$common.hideLoad()
          if (data && data.result) {
            this.active = 3
            this.show = true
            this.status = 'success'
          } else if (data && !data.result) {
            this.$common.error('重置密码失败，请稍后再试')
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    //   下一步
    goStep(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await testSmsCode(this.form.oldPhone, this.form.code)
          this.$common.hideLoad()
          if (data && data.result) {
            this.active = 2
          } else if (data && !data.result) {
            this.$common.error('验证码校验不通过，请重新再试')
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
