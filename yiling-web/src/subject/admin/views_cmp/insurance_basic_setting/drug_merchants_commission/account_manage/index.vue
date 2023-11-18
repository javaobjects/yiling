<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <el-form :model="accountForm" class="accountForm" ref="accountFormRef" :rules="accountFormRules" label-position="left" label-width="80px">
          <el-form-item label="商家" prop="name">
            <el-input v-model="accountForm.ename" placeholder="请输入商家" disabled> </el-input>
          </el-form-item>
          <el-form-item label="账户类型" prop="accountType">
            <el-select v-model="accountForm.accountType" placeholder="请选择账户类型">
              <el-option
                v-for="item in hmcEnterpriseAccountType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="账户名" prop="accountName">
            <el-input v-model="accountForm.accountName" placeholder="请输入账户名" maxlength="30" show-word-limit> </el-input>
          </el-form-item>
          <el-form-item label="账户" prop="accountNumber">
            <el-input v-model="accountForm.accountNumber" placeholder="请输入账户" maxlength="20" show-word-limit> </el-input>
          </el-form-item>
          <el-form-item label="开户行" prop="accountBank">
            <el-input v-model="accountForm.accountBank" placeholder="请输入开户行" maxlength="30" show-word-limit> </el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input class="remark" v-model="accountForm.remark" type="textarea" :rows="8" maxlength="255" show-word-limit placeholder="请输入备注"> </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
  </div>
</template>
<script>
import { enterpriseAccountDetail, enterpriseAccountSave } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { hmcEnterpriseAccountType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

export default {
  name: 'MerchantsAccountManage',
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail(this.id)
    }
  },
  computed: {
    hmcEnterpriseAccountType() {
      return hmcEnterpriseAccountType()
    }
  },
  data () {
    return {
      accountForm: {
        accountType: '',
        accountName: '',
        accountNumber: '',
        accountBank: '',
        remark: ''
      },
      accountFormRules: {
        accountType: [ { required: true, message: '请选择账户类型', trigger: 'blur' } ],
        accountName: [ { required: true, message: '请输入账户名', trigger: 'blur' } ],
        accountNumber: [ { required: true, message: '请输入账户', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '只能输入数字', trigger: 'blur' } ],
        accountBank: [ { required: true, message: '请输入开户行', trigger: 'blur' } ]
      }
    }
  },
  methods: {
    //  获取结账账户详情
    async getDetail(id) {
      this.$common.showLoad()
      let data = await enterpriseAccountDetail(id)
      this.$common.hideLoad()
      if (data) {
        this.accountForm.accountBank = data.accountBank
        this.accountForm.accountName = data.accountName
        this.accountForm.accountNumber = data.accountNumber
        this.accountForm.accountType = data.accountType
        this.accountForm.eid = data.eid
        this.accountForm.ename = data.ename
        this.accountForm.id = data.id
        this.accountForm.remark = data.remark
      }
    },
    //  添加
    save() {
      let form = this.accountForm
      this.$refs.accountFormRef.validate(async valid => {
        if (valid) {
          this.$common.showLoad()
          let data = await enterpriseAccountSave(
            form.eid,
            form.ename,
            form.accountBank,
            form.accountName,
            form.accountNumber,
            form.accountType,
            form.remark,
            form.id
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('操作成功')
            this.$router.go(-1)
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.el-form {
  .el-form-item {
    ::v-deep .el-form-item__label {
      font-weight: 400;
    }
    ::v-deep .el-form-item__content {
      .el-input {
        max-width: 500px;
      }
      .el-select {
        width: 500px;
        max-width: 500px;
      }
    }
    .remark {
      width: 500px;
    }
  }
}
</style>