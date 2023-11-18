<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <el-form :model="contentForm" class="contentForm" ref="contentFormRef" :rules="contentFormRules" label-position="left" label-width="150px">
          <el-form-item label="保险名称" prop="insuranceName">
            <el-input v-model="contentForm.insuranceName" placeholder="请输入保险名称"> </el-input>
          </el-form-item>
          <el-form-item label="保险提供服务商" prop="provider">
            <span>{{ companyName ? companyName : "" }}</span>
          </el-form-item>
          <el-form-item label="定额付费类型" prop="mark">
            <div class="pay-type-item">
              <div class="item-title">季度对应保司标识</div>
              <el-input v-model="contentForm.quarterIdentification" placeholder="请输入季度对应保司标识"> </el-input>
            </div>
            <div class="pay-type-item">
              <div class="item-title">年度对应保司标识</div>
              <el-input v-model="contentForm.yearIdentification" placeholder="请输入年度对应保司标识"> </el-input>
            </div>
          </el-form-item>
          <el-form-item label="售卖金额" prop="payAmount">
            <el-input v-model="contentForm.payAmount" @keyup.native="contentForm.payAmount = onInput(contentForm.payAmount, 2)" placeholder="请输入售卖金额"></el-input> 元
          </el-form-item>
          <el-form-item label="服务商扣服务费比例" prop="serviceRatio">
            <el-input v-model="contentForm.serviceRatio" placeholder="请输入服务商扣服务费比例"> </el-input> %
          </el-form-item>
          <el-form-item label="保险对应药品给以岭结算额" prop="insuranceDetailSaveList">
            <yl-button plain type="primary" @click="addDrugs">添加</yl-button>
            <div class="mar-t-8">
              <yl-table
                stripe
                :show-header="true"
                :list="contentForm.insuranceDetailSaveList"
              >
                <el-table-column label="药品名称" min-width="180" align="left" prop="goodsName"></el-table-column>
                <el-table-column label="规格" min-width="180" align="left" prop="sellSpecifications"></el-table-column>
                <el-table-column label="单个商品结算金额(元)" min-width="150" align="center" prop="settlePrice">
                  <template slot-scope="{ row }">
                    <el-input v-model="row.settlePrice" @keyup.native="row.settlePrice = onInput(row.settlePrice, 2)"></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="每月1次，每次拿多少盒" min-width="150" align="center" prop="monthCount">
                 <template slot-scope="{ row }">
                  <el-input v-model="row.monthCount" @keyup.native="row.monthCount = onInput(row.monthCount, 0)"></el-input>
                 </template>
                </el-table-column>
                <el-table-column label="保司药品编码" min-width="150" align="center" prop="insuranceGoodsCode">
                  <template slot-scope="{ row }">
                    <el-input v-model.trim="row.insuranceGoodsCode"></el-input>
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
          <el-form-item label="售卖地址--h5页面链接" prop="url">
            <el-input v-model="contentForm.url" placeholder="请输入售卖地址--h5页面链接"> </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
      <!-- 选择商品 -->
      <choose-product ref="choose" :muti-select="true" :id="id" :choose-list="contentForm.insuranceDetailSaveList" type="goodsInAddContent" @selectItem="selectItem"/>
    </div>
  </div>
</template>
<script>
import { insuranceContentUpdate } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import ChooseProduct from '../../component/ChooseProduct.vue'
import { onInputLimit } from '@/common/utils'
export default {
  name: 'AddServiceProviderContent',
  components: {
    ChooseProduct
  },
  created() {
    this.id = this.$route.params.id
    this.companyName = this.$route.params.companyName
  },
  data () {
    var markVlidator = (rule, value, callback) => {
      if (this.contentForm.quarterIdentification === '' || this.contentForm.yearIdentification === '') {
        return callback(new Error('请输入对应保司标识'));
      } else return callback();
    }
    var drugPayVlidator = (rule, value, callback) => {
      if (this.contentForm.insuranceDetailSaveList.length == 0) {
        return callback(new Error('请添加对应药品'));
      }
      for (let i = 0; i < this.contentForm.insuranceDetailSaveList.length; i ++) {
        let item = this.contentForm.insuranceDetailSaveList[i]
        if (parseInt(item.settlePrice) == 0 || parseInt(item.monthCount) == 0 || item.insuranceGoodsCode == '') {
          return callback(new Error('请输入相应药品金额和数量'));
        }
      }
      return callback();
    }
    return {
      asycUpdate: false,
      disableSelectAddress: false,
      showDialog: false,
      contentForm: {
        insuranceName: '',
        quarterIdentification: '',
        yearIdentification: '',
        payAmount: '',
        serviceRatio: '',
        insuranceDetailSaveList: [],
        url: ''
      },
      contentFormRules: {
        insuranceName: [ { required: true, message: '请输入保险名称', trigger: 'blur' } ],
        licenseNo: [ { required: true, message: '请输入执业许可证号/社会信用统一代码', trigger: 'blur' } ],
        mark: [ { required: true, message: '请输入对应保司标识', validator: markVlidator, trigger: 'blur' } ],
        payAmount: [ { required: true, message: '请输入售卖金额', trigger: 'blur' } ],
        serviceRatio: [
          { required: true, message: '请输入服务商扣服务费比例', trigger: 'blur' },
          { pattern: /^(0|100|\d{1,2})$/, message: '服务比请输入整数', trigger: 'blur' }
        ],
        insuranceDetailSaveList: [ { required: true, message: '请添加药品并输入相应金额和数量', validator: drugPayVlidator, trigger: ['blur', 'change'] } ],
        url: [ { required: true, message: '请输入售卖地址--h5页面链接', trigger: 'blur' } ]
      }
    }
  },
  methods: {
    addDrugs() {
      this.$refs.choose.openGoodsDialog()
    },
    selectItem(data) {
      this.contentForm.insuranceDetailSaveList.push({
        controlId: data.id,
        sellSpecificationsId: data.sellSpecificationsId,
        standardId: data.standardId,
        goodsName: data.name,
        sellSpecifications: data.sellSpecifications,
        settlePrice: 0,
        monthCount: 0,
        insuranceGoodsCode: '',
        // 新添加的可以删除(本地操作)
        canDelete: true
      })
    },
    //  删除列表项
    del(row, index) {
      this.contentForm.insuranceDetailSaveList.splice(index, 1)
    },
    //  添加
    save() {
      this.$refs.contentFormRef.validate(async valid => {
        if (valid) {
          if (this.checkPriceInput()) {
            this.$common.warn('请设置单个商品结算额和每月拿药数量');
            return false
          }
          let formData = {
            ...this.contentForm
          }
          formData.insuranceCompanyId = Number(this.id)
          formData.insuranceDetailSaveList = this.contentForm.insuranceDetailSaveList.map(item => {
            return { 
              controlId: item.controlId, 
              goodsName: item.goodsName, 
              settlePrice: Number(item.settlePrice), 
              monthCount: Number(item.monthCount),
              insuranceGoodsCode: item.insuranceGoodsCode
            }
          })
          this.$common.showLoad()
          let data = await insuranceContentUpdate(
            formData.insuranceCompanyId,
            formData.insuranceName,
            formData.payAmount,
            formData.quarterIdentification,
            formData.yearIdentification,
            formData.serviceRatio,
            formData.url,
            formData.insuranceDetailSaveList,
            // 新建的默认设为开启
            1
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('操作成功')
            this.$router.go(-1)
          }
        }
      })
    },
    // 校验输入
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    //  校验列表里的终端结算额是否设置
    checkPriceInput() {
      let inputPriceCountValid = this.contentForm.insuranceDetailSaveList.some(item => !item.settlePrice || item.settlePrice == 0 || !item.monthCount || item.monthCount == 0)
      return inputPriceCountValid
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
      min-width: 500px;
      .pay-type-item {
        display: flex;
        justify-content: space-between;
        &:not(:last-child) {
          margin-bottom: 22px;
        }
      }
    }
    .remark {
      width: 540px;
    }
  }
}
</style>