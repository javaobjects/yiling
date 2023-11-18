<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <!-- 编辑保险服务商表单 -->
        <add-provider ref="editProviderRef" :disable-edit="true" :disable-select-address="true" :id="id"></add-provider>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
  </div>
</template>
<script>
import { addInsuranceCompany } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import AddProvider from '../component/AddProvider'
export default {
  components: { AddProvider },
  name: 'EditServiceProvider',
  created() {
    this.id = this.$route.params.id
  },
  data () {
    return {
      id: undefined
    }
  },
  methods: {
    //  修改
    save() {
      this.$refs.editProviderRef.$refs.providerFormRef.validate(async valid => {
        if (valid) {
          //  编辑服务商
          let formData = this.$refs.editProviderRef.providerForm;
          this.$common.showLoad();
          let data = await addInsuranceCompany(
            formData.companyName,
            formData.insuranceNo,
            formData.provinceCode,
            formData.cityCode,
            formData.regionCode,
            formData.address,
            formData.cancelInsuranceTelephone,
            formData.cancelInsuranceAddress,
            formData.renewInsuranceAddress,
            formData.internetConsultationUrl,
            formData.remark,
            formData.contactName,
            formData.status,
            // 代理理赔协议地址
            formData.claimProtocolUrl,
            formData.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('操作成功')
            this.$router.go(-1)
          }
        }
      })
    }
  }
}
</script>
<style>

</style>