<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <!-- 编辑保险服务商表单 -->
        <add-provider ref="addProviderRef"></add-provider>
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
  name: 'AddServiceProvider',
  data () {
    return {
    }
  },
  methods: {
    //  添加
    save() {
      this.$refs.addProviderRef.$refs.providerFormRef.validate(async valid => {
        if (valid) {
          //  新增服务商
          let formData = this.$refs.addProviderRef.providerForm;
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
            // 保险服务商状态 1-启用 2-停用 (新建默认给开启)
            1, 
            // 代理理赔协议地址
            formData.claimProtocolUrl
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
<style lang="scss" scoped>

</style>