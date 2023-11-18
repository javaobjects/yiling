<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="search-bar">
        <div class="header-bar">
          <div class="sign"></div>您正在“重置密码”设置
        </div>
        <div class="step">
          <!-- <steps :active="active" :finish-status="status" :steps="['身份验证', '设置登录密码', '修改成功']" /> -->
        </div>

        <div class="form">
          <el-form :model="form" ref="form" :rules="rules" label-position="right" label-width="200px" class="flex-center">
            <el-form-item label="原密码：" prop="originalPassword">
              <el-input class="inp" type="text" v-model="form.originalPassword" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码：" prop="newPass">
              <el-input class="inp" type="text" v-model="form.newPass" show-password></el-input>
            </el-form-item>
            <el-form-item label="重复输入密码：" prop="newPassAgain">
              <el-input class="inp" type="text" v-model="form.newPassAgain" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <yl-button type="primary" @click="submitForm('form')">提交</yl-button>
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
// import Steps from "@/common/components/Steps";
import { updatePassword } from '@/subject/admin/api/user';
export default {
  name: '',
  components: {
    // Steps
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  data() {
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
      form: {
        originalPassword: '',
        newPass: '',
        newPassAgain: ''
      },
      rules: {

        originalPassword: [{required: true, message: '原密码不能为空', trigger: 'blur' }],
        newPass: [{ required: true, trigger: 'blur', validator: validatePassword }],
        newPassAgain: [{ required: true, trigger: 'blur', validator: validatePassword1 }]
      },
      show: false
    }
  },
  created() {
  },
  activated() {
    this.form.oldPhone = this.$store.getters.userInfo.mobile
  },
  methods: {
    // 提交
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await updatePassword(this.form.originalPassword, this.form.newPassAgain )
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
    }
    // async goLogin() {
    //   this.$common.showLoad()
    //   await this.$store.dispatch('user/resetToken')
    //   this.$common.hideLoad()
    //   if (process.env.NODE_ENV === 'dev') {
    //     this.$router.replace('/login')
    //   } else {
    //     this.$common.goLogin()
    //   }
    // }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

