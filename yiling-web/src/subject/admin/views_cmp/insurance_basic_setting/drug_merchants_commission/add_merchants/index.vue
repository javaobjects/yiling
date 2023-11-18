<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>
          创建
        </div>
        <div class="process mar-t-16">
          <steps :active="active" :finish-status="status" :steps="['基础设置', '提成设置']" />
        </div>
        <div class="form-wrap">
          <el-form :model="accountForm" class="accountForm" ref="accountFormRef" :rules="accountFormRules" label-position="left" label-width="80px">
            <div class="basic-setting" v-if="active === 1">
              <el-form-item label="选择商家" prop="ename">
                <el-input v-model="accountForm.ename" placeholder="请选择商家" @focus="selectEnterprise"></el-input>
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
            </div>
            <div class="goods-setting" v-if="active === 2">
              <el-form-item label="选择药品" prop="remark">
                <yl-button plain type="primary" @click="addDrugs">添加</yl-button>
                <div class="mar-t-8">
                  <yl-table
                    stripe
                    :show-header="true"
                    :list="goodsList"
                  >
                    <el-table-column label="保险涉及药品" min-width="100" align="left" prop="goodsName"></el-table-column>
                    <el-table-column label="规格" min-width="100" align="center" prop="sellSpecifications"></el-table-column>
                    <el-table-column label="商家售卖金额/盒" min-width="100" align="center" prop="insurancePrice"> </el-table-column>
                    <el-table-column label="给终端结算额/盒" min-width="150" align="center" prop="itemPrice">
                      <template slot-scope="{ row }">
                        <el-input v-model="row.terminalSettlePrice" @keyup.native="row.terminalSettlePrice = onInput(row.terminalSettlePrice, 2)"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" min-width="100" align="center">
                      <template slot-scope="{ row, $index }">
                        <yl-button type="text" v-if="row.canDelete" @click="del(row, $index)">删除</yl-button>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="active === 1" @click="firstSave" >保存，下一步</yl-button>
        <yl-button type="primary" v-if="active === 2" @click="finalSave">保存</yl-button>
      </div>
      <!-- 选择商品 -->
      <choose-drug-merchants ref="chooseDrugMerchants" @selectData="selectEnterpriseData" :choose-item="accountForm.eid"></choose-drug-merchants>
      <choose-product ref="choose" type="insuranceGoods" :muti-select="true" :choose-list="goodsList" @selectItem="selectItem"/>
    </div>
  </div>
</template>
<script>
import { enterpriseAccountSave, enterpriseGoodsUpdate } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { onInputLimit } from '@/common/utils'
import ChooseProduct from '../../component/ChooseProduct.vue'
 import ChooseDrugMerchants from '../component/DrugMerchants.vue'
import Steps from '@/common/components/Steps';
import { hmcEnterpriseAccountType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

export default {
  name: 'AddDrugMerchants',
  components: {
    Steps,
    ChooseProduct,
    ChooseDrugMerchants
  },
  mounted() {
  },
  computed: {
    hmcEnterpriseAccountType() {
      return hmcEnterpriseAccountType()
    }
  },
  data () {
    return {
      active: 1,
      status: 'finish',
      accountForm: {
        eid: '',
        ename: '',
        accountType: '',
        accountName: '',
        accountNumber: '',
        accountBank: '',
        remark: ''
      },
      accountFormRules: {
        ename: [ { required: true, message: '请选择商家', trigger: 'blur' } ],
        accountType: [ { required: true, message: '请选择账户类型', trigger: ['blur', 'change'] } ],
        accountName: [ { required: true, message: '请输入账户名', trigger: 'blur' } ],
        accountNumber: [ { required: true, message: '请输入账户', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '只能输入数字', trigger: 'blur' } ],
        accountBank: [ { required: true, message: '请输入开户行', trigger: 'blur' } ]
      },
      goodsList: []
    }
  },
  methods: {
    selectEnterprise() {
      this.$refs.chooseDrugMerchants.openEnterpriseDialog()
      this.$refs.accountFormRef.clearValidate('ename')
    },
    addDrugs() {
      this.$refs.choose.openGoodsDialog()
    },
    // 选择商家
    selectEnterpriseData(data) {
      this.accountForm.eid = data.id
      this.accountForm.ename = data.name
      this.$refs.accountFormRef.clearValidate('ename')
    },
    // 删除
    del(row, index) {
      this.goodsList.splice(index, 1)
    },
    // 点击选择
    selectItem(data) {
      this.goodsList.push({
        sellSpecificationsId: data.sellSpecificationsId,
        standardId: data.standardId,
        goodsName: data.name,
        sellSpecifications: data.sellSpecifications,
        insurancePrice: data.insurancePrice,
        terminalSettlePrice: data.marketPrice,
        // 新添加的可以删除(本地操作)
        canDelete: true
      })
    },
    // 账户保存
    firstSave() {
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
            form.remark
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('操作成功')
            this.active = 2
          }
        }
      })
    },
    // 提成保存
    async finalSave() {
      if (this.goodsList.length == 0) {
        this.$common.warn('请选择药品');
        return false
      }
      if (this.checkPriceInput()) {
        this.$common.warn('请设置给终端结算额');
        return false
      }
      let formData = {}
      formData.eid = this.accountForm.eid
      formData.ename = this.accountForm.ename
      formData.goodsRequest = this.goodsList.map(item => {
        return {
          id: item.id,
          sellSpecificationsId: item.sellSpecificationsId,
          standardId: item.standardId,
          goodsName: item.goodsName,
          salePrice: item.insurancePrice,
          terminalSettlePrice: Number(item.terminalSettlePrice)
        }
      })
      this.$common.showLoad()
      let data = await enterpriseGoodsUpdate(
        formData.eid,
        formData.ename,
        formData.goodsRequest
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('操作成功')
        this.$router.go(-1)
      }
    },
    // 校验价格
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    // 校验列表里的终端结算额是否设置
    checkPriceInput() {
      let inputPriceValid = this.goodsList.some(item => !item.terminalSettlePrice || item.terminalSettlePrice == 0)
      return inputPriceValid
    }

  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>