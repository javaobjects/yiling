<template>
<!-- 添加/编辑保险服务商 -->
  <div class="add-provider-page">
      <el-form 
        :model="providerForm" 
        class="providerForm"
        ref="providerFormRef" 
        :rules="providerFormRules" 
        label-position="left" 
        label-width="150px">
        <el-form-item label="保险服务商名称" prop="companyName">
          <el-input
            v-model="providerForm.companyName" 
            placeholder="请输入保险服务商名称" 
            :disabled="disableEdit" 
            maxlength="20" 
            show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="执业许可证号/社会信用统一代码" prop="insuranceNo">
          <el-input 
            v-model="providerForm.insuranceNo" 
            placeholder="请输入执业许可证号/社会信用统一代码" 
            :disabled="disableEdit" 
            maxlength="18" 
            show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="chooseAddress">
          <div class="flex-row-left">
            <yl-choose-address 
              :province.sync="providerForm.provinceCode" 
              :city.sync="providerForm.cityCode" 
              :area.sync="providerForm.regionCode" 
              :disabled="disableSelectAddress" 
              v-if="asycUpdate" />
          </div>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input 
            v-model="providerForm.address" 
            placeholder="请输入详细地址" 
            :disabled="disableEdit" 
            maxlength="200" 
            show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contactName">
          <el-input v-model="providerForm.contactName" placeholder="请输入联系人姓名" maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="退保客服电话" prop="cancelInsuranceTelephone">
          <el-input 
            v-model="providerForm.cancelInsuranceTelephone" 
            placeholder="请输入退保客服电话" 
            :disabled="disableEdit" 
            maxlength="11" 
            show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="退保地址" prop="cancelInsuranceAddress">
          <el-input v-model="providerForm.cancelInsuranceAddress" placeholder="请输入退保地址" maxlength="200" show-word-limit> </el-input>
        </el-form-item>
        <el-form-item label="续保地址" prop="renewInsuranceAddress">
          <el-input v-model="providerForm.renewInsuranceAddress" placeholder="请输入续保地址" maxlength="200" show-word-limit> </el-input>
        </el-form-item>
        <el-form-item label="互联网问诊地址" prop="internetConsultationUrl">
          <el-input v-model="providerForm.internetConsultationUrl" placeholder="请输入互联网问诊地址" maxlength="200" show-word-limit> </el-input>
        </el-form-item>
        <el-form-item label="代理赔协议地址" prop="claimProtocolUrl">
          <el-input 
            v-model="providerForm.claimProtocolUrl" 
            placeholder="请输入代理赔协议地址" 
            maxlength="200" 
            show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input 
            class="remark" 
            v-model="providerForm.remark" 
            type="textarea" 
            :rows="3" 
            maxlength="255" 
            show-word-limit 
            placeholder="请输入备注">
          </el-input>
        </el-form-item>
      </el-form>
  </div>
</template>
<script>
import { getInsuranceCompanyDetail } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { ylChooseAddress } from '@/subject/admin/components'
export default {
  name: 'AddProvider',
  components: {
    ylChooseAddress
  },
  props: {
    id: {
      type: [Number, String],
      default: undefined
    },
    disableSelectAddress: {
      type: Boolean,
      default: false
    },
    disableEdit: {
      type: Boolean,
      default: false
    }
  },
  created() {
    this.$nextTick(async () => {
      if (this.id) {
        await this.getDetail(this.id)
        this.asycUpdate = true
      } else {
        this.asycUpdate = true
      }
    })
  },
  data() {
    var chooseAddressVlidator = (rule, value, callback) => {
      if (this.providerForm.provinceCode === '' || this.providerForm.cityCode === '' || this.providerForm.regionCode === '' ) {
        return callback(new Error('请选择所在区域'));
      } else return callback();
    }
    return {
      asycUpdate: false,
      showDialog: false,
      providerForm: {
        companyName: '',
        insuranceNo: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        address: '',
        contactName: '',
        cancelInsuranceTelephone: '',
        cancelInsuranceAddress: '',
        renewInsuranceAddress: '',
        remark: ''
      },
      providerFormRules: {
        companyName: [ { required: true, message: '请输入保险服务商名称', trigger: 'blur' } ],
        insuranceNo: [ { required: true, message: '请输入执业许可证号/社会信用统一代码', trigger: 'blur' } ],
        chooseAddress: [ { required: true, message: '请选择所在地区', validator: chooseAddressVlidator, trigger: 'blur' } ],
        address: [ { required: true, message: '请输入详细地址', trigger: 'blur' } ],
        cancelInsuranceTelephone: [ { required: true, message: '请输入退保客服电话', trigger: 'blur' } ],
        claimProtocolUrl: [{ required: true, message: '请输入代理赔协议地址', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 获取详情
    async getDetail(id) {
      let data = await getInsuranceCompanyDetail(id)
      if (data) {
        this.providerForm = { ...data }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.el-form {
  .el-form-item {
    display: flex;
    align-items: center;
    ::v-deep .el-form-item__label {
      font-weight: 400;
      line-height: 20px;
    }
    ::v-deep .el-form-item__content {
      margin-left: 0px !important;
      min-width: 360px;
    }
    .remark {
      width: 524px;
    }
  }
}
</style>