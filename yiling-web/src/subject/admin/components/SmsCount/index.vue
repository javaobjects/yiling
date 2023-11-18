<template>
  <yl-button type="danger" :loading="smsLoad" :disabled="disable" @click="getSmsCode">
    获取验证码{{ sms.begin === 0 ? '' : `(${sms.timeLeft})` }}
  </yl-button>
</template>

<script>
  import { getResetSmsCode } from '@/subject/admin/api/user'
  import { validateTel } from '@/subject/admin/utils/validate'
  const COUNT = 60

  export default {
    name: 'SmsCount',
    props: {
      // 手机号
      mobile: {
        type: String,
        default: ''
      },
      // 获取验证码类别 1-重置密码 2-暂定
      type: {
        type: String,
        default: '',
        require: true
      }
    },
    computed: {
      disable() {
        return this.checkMyPhone()
      }
    },
    data() {
      return {
        smsLoad: false,
        sms: {
          timeLeft: COUNT,
          begin: 0
        }
      }
    },
    mounted() {
      this.timer = null
    },
    beforeDestroy() {
      clearInterval(this.timer)
      this.timer = null
    },
    methods: {
      // 获取短信
      async getSmsCode() {
        this.smsLoad = true
        let data = null
        if (this.type === '1') {
          // 重置密码验证码
          data = await getResetSmsCode(this.mobile)
        }
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
        let mobile = this.mobile
        if (!mobile) {
          return true
        } else {
          if (!validateTel(mobile)) {
            return true
          }
        }
        return false
      }
    }
  }
</script>

<style lang="scss" scoped>
  .contain {

  }
</style>
