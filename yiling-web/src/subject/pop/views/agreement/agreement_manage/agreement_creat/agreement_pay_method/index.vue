<template>
  <el-form :model="agreementSettlementTerms" :rules="agreementSettlementTermsRules" ref="dataForm" class="agreement-main" label-position="left">
    <div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>付款方式设置
        </div>
        <div class="content-box my-form-box">
          <div class="font-title-color font-size-base mar-b-10">付款方式</div>
          <el-form-item label="">
            <el-checkbox-group v-model="payTypeList">
              <div class="box flex-row-left pay-view">
                <div class="pay-form-item pay-item mar-b-16" v-for="(item, index) in agreementPayMethodList" :key="item.value">
                  <el-checkbox :label="item.value">{{ item.label }}</el-checkbox>
                  <el-form-item label="" class="pay-form-item mar-l-16">
                    <span class="font-title-color font-size-base mar-r-10">占比:</span>
                    <!-- <el-input v-model="item.ratio" @input="change(index)"></el-input> -->
                    <el-input v-model="item.ratio" @input="change(index)"></el-input>
                    <span class="font-title-color font-size-base mar-l-10">%</span>
                  </el-form-item>
                </div>
              </div>
            </el-checkbox-group>
          </el-form-item>
        </div>
      </div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>结算方式设置
        </div>
        <div class="content-box my-form-box">
          <div class="font-title-color font-size-base mar-b-10">结算方式</div>
          <div class="line-box">
            <el-form-item label="" required>
              <div class="mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.advancePaymentFlag" label="1" class="checkbox-width">预付款</el-checkbox>
              </div>
              <div class="pay-form-item mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.paymentDaysFlag" class="checkbox-width">账期</el-checkbox>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.paymentDaysFlag" label="结算周期:" :prop="'agreementSettlementMethod' + '.paymentDaysSettlementPeriod'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementPeriod" @input="e => (agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementPeriod = checkInput(e, 9999))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">天</span>
                </el-form-item>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.paymentDaysFlag" label="结算日:" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementDay" @input="e => (agreementSettlementTerms.agreementSettlementMethod.paymentDaysSettlementDay = checkInput(e, 31))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">日</span>
                </el-form-item>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.paymentDaysFlag" label="支付日:" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.paymentDaysPayDays" @input="e => (agreementSettlementTerms.agreementSettlementMethod.paymentDaysPayDays = checkInput(e, 31))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">日</span>
                </el-form-item>
              </div>
              <div class="pay-form-item mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementFlag" class="checkbox-width">实销实结</el-checkbox>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementFlag" label="结算周期:" :prop="'agreementSettlementMethod' + '.actualSalesSettlementPeriod'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPeriod" @input="e => (agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPeriod = checkInput(e, 9999))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">天</span>
                </el-form-item>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementFlag" label="结算日:" :prop="'agreementSettlementMethod' + '.actualSalesSettlementDay'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementDay" @input="e => (agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementDay = checkInput(e, 31))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">日</span>
                </el-form-item>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementFlag" label="支付日:" :prop="'agreementSettlementMethod' + '.actualSalesSettlementPayDays'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPayDays" @input="e => (agreementSettlementTerms.agreementSettlementMethod.actualSalesSettlementPayDays = checkInput(e, 31))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">日</span>
                </el-form-item>
              </div>
              <div class="pay-form-item mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.payDeliveryFlag">货到付款</el-checkbox>
              </div>
              <div class="pay-form-item mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.pressGroupFlag" class="checkbox-width">压批</el-checkbox>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.pressGroupFlag" label="压批次数:" :prop="'agreementSettlementMethod' + '.pressGroupNumber'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.pressGroupNumber" @input="e => (agreementSettlementTerms.agreementSettlementMethod.pressGroupNumber = numberCheckInput(e))"></el-input>
                </el-form-item>
              </div>
              <div class="pay-form-item mar-b-16">
                <el-checkbox v-model="agreementSettlementTerms.agreementSettlementMethod.creditFlag" class="checkbox-width">授信</el-checkbox>
                <el-form-item v-if="agreementSettlementTerms.agreementSettlementMethod.creditFlag" label="授信金额:" :prop="'agreementSettlementMethod' + '.creditAmount'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.creditAmount" @keyup.native="agreementSettlementTerms.agreementSettlementMethod.creditAmount = onInput(agreementSettlementTerms.agreementSettlementMethod.creditAmount, 2)"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">万</span>
                </el-form-item>
              </div>
            </el-form-item>
          </div>
          <div class="font-title-color font-size-base mar-b-10 mar-t-10">其他</div>
          <div class="line-box">
            <el-row class="box">
              <el-col :span="6">
                <el-form-item label="到货周期:" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.arrivePeriod" @input="e => (agreementSettlementTerms.agreementSettlementMethod.arrivePeriod = checkInput(e, 999))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">天</span>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最小发货量:" class="pay-form-item mar-l-16">
                  <el-input v-model="agreementSettlementTerms.agreementSettlementMethod.minShipmentNumber" @input="e => (agreementSettlementTerms.agreementSettlementMethod.minShipmentNumber = numberCheckInput(e))"></el-input>
                  <span class="font-title-color font-size-base mar-l-10">件</span>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="送货方式:">
                  <el-select v-model="agreementSettlementTerms.agreementSettlementMethod.deliveryType" placeholder="请选择">
                    <el-option v-for="item in agreementDeliveryType" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="采购负责人:" class="pay-form-item mar-l-16">
                  {{ userInfo && userInfo.name ? userInfo.name : '' }}
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'
import { agreementPayMethod, agreementDeliveryType } from '@/subject/pop/utils/busi';
import { onInputLimit } from '@/common/utils'

export default {
  name: 'AgreementPayMethod',
  components: {
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
    // 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
    agreementPayMethod() {
      return agreementPayMethod()
    },
    //
    agreementDeliveryType() {
      return agreementDeliveryType()
    }
  },
  watch: {
    'agreementPayMethod': {
        handler(newName, oldName) {
          this.$log('watch:',newName, oldName)
          let agreementPayMethodList = newName || []
          for (let i = 0; i < agreementPayMethod.length; i++) {
            let payItem = this.$common.clone(agreementPayMethod[i])
            payItem.ratio = ''
            agreementPayMethodList.push(payItem)
          }
          this.agreementPayMethodList = agreementPayMethodList
        },
        deep: true,
        immediate: true
      }
  },
  data() {
    return {
      // 付款方式
      payTypeList: [],
      agreementPayMethodList: [],
      agreementSettlementTerms: {
        // 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑（见字典：agreement_pay_method）
        agreementPaymentMethodList: [],
        agreementSettlementMethod: {
          // 是否为预付款结算：0-否 1-是
          advancePaymentFlag: 0,

          // 是否为账期结算：0-否 1-是
          paymentDaysFlag: 0,
          // 账期结算周期
          paymentDaysSettlementPeriod: '',
          // 账期结算日
          paymentDaysSettlementDay: '',
          // 账期支付日
          paymentDaysPayDays: '',

          // 是否为实销实结：0-否 1-是
          actualSalesSettlementFlag: 0,
          // 实销实结结算周期
          actualSalesSettlementPeriod: '',
          // 实销实结结算日
          actualSalesSettlementDay: '',
          // 实销实结支付日
          actualSalesSettlementPayDays: '',

          // 是否为货到付款结算：0-否 1-是
          payDeliveryFlag: 0,

          // 是否为压批结算：0-否 1-是
          pressGroupFlag: 0,
          // 压批次数
          pressGroupNumber: '',

          // 是否为授信结算：0-否 1-是
          creditFlag: 0,
          // 授信金额
          creditAmount: '',

          // 其他
          // 到货周期
          arrivePeriod: '',
          // 最小发货量
          minShipmentNumber: '',
          deliveryType: 1
        }
      },
      agreementSettlementTermsRules: {
        paymentDaysSettlementPeriod: [{ required: true, message: '请输入结算周期', trigger: 'blur' }],
        paymentDaysSettlementDay: [{ required: true, message: '请输入结算日', trigger: 'blur' }],
        paymentDaysPayDays: [{ required: true, message: '请输入支付日', trigger: 'blur' }],
        actualSalesSettlementPeriod: [{ required: true, message: '请输入结算周期', trigger: 'blur' }],
        actualSalesSettlementDay: [{ required: true, message: '请输入结算日', trigger: 'blur' }],
        actualSalesSettlementPayDays: [{ required: true, message: '请输入支付日', trigger: 'blur' }],
        pressGroupNumber: [{ required: true, message: '请输入压批次数', trigger: 'blur' }],
        creditAmount: [{ required: true, message: '请输入授信金额', trigger: 'blur' }]
      }
    };
  },
  mounted() {
  },
  methods: {
    init(agreementSettlementTerms) {
      this.$log('agreementSupplySalesTerms:', agreementSettlementTerms)
      this.agreementSettlementTerms = agreementSettlementTerms

      let agreementPaymentMethodList = agreementSettlementTerms.agreementPaymentMethodList
      for (let i = 0; i < this.agreementPayMethodList.length; i++) {
        let item = this.agreementPayMethodList[i]
        let hasIndex = agreementPaymentMethodList.findIndex(obj => {
          return item.value == obj.payMethod;
        });
        if (hasIndex != -1) {
          item.ratio = agreementPaymentMethodList[hasIndex].ratio
        } else {
          item.ratio = ''
        }
      }

      let payTypeList = []
      for (let j = 0; j < agreementPaymentMethodList.length; j++) {
        let payItem = agreementPaymentMethodList[j]
        payTypeList.push(payItem.payMethod)
      }
      this.payTypeList = payTypeList

    },
    // 下一步点击
    stepClickMethods(callback) {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let agreementPaymentMethodList = []
          for (let i = 0; i < this.payTypeList.length; i++) {
            let payItem = this.payTypeList[i]
            let hasIndex = this.agreementPayMethodList.findIndex(obj => {
              return payItem == obj.value;
            });
            if (hasIndex != -1) {
              let item = this.agreementPayMethodList[hasIndex]
              let paymentMethod = {
                payMethod: item.value,
                ratio: item.ratio
              }
              agreementPaymentMethodList.push(paymentMethod)
            }
          }
          this.agreementSettlementTerms.agreementPaymentMethodList = agreementPaymentMethodList
          if (callback) callback(this.agreementSettlementTerms)
        } else {
          return false;
        }
      })
    },
    change(index) {
      // 重新赋值
      let agreementPayMethodList = this.agreementPayMethodList

      let tmpObj = agreementPayMethodList[index]
      tmpObj.ratio = this.checkInput(tmpObj.ratio, 100)

      this.$set(agreementPayMethodList, index, tmpObj);
    },
    // 校验输入数值
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    // 校验输入范围
    checkInput(val, maxValue) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > maxValue) {
        val = val.substr(0, val.length - 1)
      }
      if (val < 1) {
        val = ''
      }
      return val
    },
    // 只能输入数字
    numberCheckInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.steps-view{
  .el-step{
    .is-finish{
      .el-step__icon{
        background: #1790ff;
      }
    }
    .el-step__icon{
      background: #CCCCCC;
      border: none;
      .el-step__icon-inner{
        color: $white;
      }
    }
    .el-step__title{
      color: #333;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
