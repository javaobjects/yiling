<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="search-bar">
        <div class="header-bar">
          <div class="sign"></div>您正在更换绑定手机号
        </div>
        <div class="step">
          <steps :active="active" :finish-status="status" :steps="['手机短信验证码', '绑定新号码', '修改成功']" />
        </div>
        <div class="form">
          <el-form :model="form" :rules="rules" ref="form" label-position="right" label-width="180px" class="flex-center">
            <p v-if="active === 1" class="old-text">请输入{{ userInfo.mobile?userInfo.mobile.substr(0, 3) + '****' +userInfo.mobile.slice(-4) : '' }} 收到的短信验证码</p>
            <el-form-item v-if="active === 2" label="新手机号：" prop="newPhone">
              <el-input class="inp" type="text" v-model="form.newPhone"></el-input>
            </el-form-item>
            <el-form-item label="验证码：" prop="code">
              <el-input class="inp" type="text" v-model="form.code"></el-input>
              <yl-sms-count ref="smsCountDom" v-if="active ==1" class="mar-l-16" :mobile="form.mobile" type="2" />
              <yl-sms-count v-if="active ==2" class="mar-l-16" :mobile="form.newPhone" :code="form.oldCode" type="3" />
            </el-form-item>
            <el-form-item>
              <yl-button v-show="active === 1" type="primary" @click="goStep('form')">下一步</yl-button>
              <yl-button v-show="active === 2" type="primary" @click="submitForm('form')">提交</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <yl-dialog title="提示" :visible.sync="show" :show-footer="false" :show-header="false">
      <div class="dialog-content">
        <div class="dia-message">
          <img src="../../../assets/ok.png" alt="">
          您的手机号换绑成功，请重新登录
        </div>
        <div class="flex-row-center">
          <yl-button class="dia-btn" type="primary" size="mini" @click="goLogin">返回登录</yl-button>
        </div>
      </div>
    </yl-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { checkOldPhoneVCode, checkNewPhoneVCode } from '@/subject/pop/api/user';
import { validateTel } from '@/subject/pop/utils/validate'
import { ylSmsCount } from '@/subject/pop/components'
import Steps from '@/common/components/Steps';
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
    const validateUsername = (rule, value, callback) => {
      if (!validateTel(value)) {
        callback(new Error('请输入正确的用户手机号码'))
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
          title: '手机号换绑'
        }
      ],
      active: 1,
      status: 'finish',
      form: {
        oldCode: '',
        code: '',
        newPhone: '',
        oldPhone: '',
        mobile: ''
      },
      rules: {
        code: [
          { required: true, trigger: 'blur', validator: validateSms }
        ],
        newPhone: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ]
      },
      show: false
    }
  },
  mounted() {
    this.form.oldPhone = this.$store.getters.userInfo.mobile
    this.form.mobile = this.$store.getters.userInfo.mobile
  },
  methods: {
    // 提交
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let data = await checkNewPhoneVCode(this.form.newPhone, this.form.oldCode, this.form.code)
          if (data && data.result) {
            this.active = 3
            this.show = true
            this.status = 'success'
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
          //  保留原手机号验证码，在绑定新手机号时使用。
          this.form.oldCode = this.form.code
          let data = await checkOldPhoneVCode(this.form.code)
          console.log(data);
          if (data && data.result) {
            this.$refs.smsCountDom.restTimer()
            this.active = 2
            this.form.code = ''
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    async goLogin() {
      this.$common.showLoad()
      await this.$store.dispatch('user/resetToken')
      this.$common.hideLoad()
      if (process.env.NODE_ENV === 'dev') {
        this.$router.replace('/login')
      } else {
        this.$common.goLogin()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

