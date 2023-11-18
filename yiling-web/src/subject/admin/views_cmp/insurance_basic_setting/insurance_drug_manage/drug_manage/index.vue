<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">药品名称</div>
              <el-input v-model="query.name" placeholder="请输入药品名称" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="addProduct">添加</yl-button>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="药品名称" min-width="100" align="center" prop="name"> </el-table-column>
          <el-table-column label="批准文号" min-width="120" align="center" prop="licenseNo"> </el-table-column>
          <el-table-column label="规格" min-width="100" align="center" prop="sellSpecifications"> </el-table-column>
          <el-table-column label="参保价格" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>¥{{ row.insurancePrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="市场价" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>¥{{ row.marketPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="进货渠道管控状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.controlStatus === 1 ? 'col-down' : 'col-up']">{{ row.controlStatus | controlStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
                <yl-button type="text" @click="setPrice(row)">价格修改 </yl-button>
                <yl-button type="text" @click="setSell(row)">销售管控</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 选择商品 -->
      <choose-product ref="choose" :choose-list="dataList" @selectItem="selectItem" type="standardGoods"/>
      <!-- 设置参保价格 -->
      <yl-dialog
        :visible.sync="priceDialogShow"
        width="580px"
        title="设置参保价格"
        type="add"
        @confirm="confirm"
        :show-cancle="false"
        @close="resetPriceForm"
      >
        <div class="dialog-content">
          <div class="drug-info">
            <div class="drug-info-item drug-name">{{ priceForm.name }}</div>
            <div class="drug-info-item sell-specifications">规格：{{ priceForm.sellSpecifications }}</div>
            <div class="drug-info-item sell-specifications">批准文号：{{ priceForm.licenseNo }}</div>
          </div>
          <el-form :model="priceForm" class="priceForm" ref="priceFormRef" :rules="priceFormRules" label-position="left" label-width="80px">
            <el-form-item label="参保价格" prop="insurancePrice">
              <el-input v-model="priceForm.insurancePrice" placeholder="请输入参保价格"> </el-input> 元/盒
            </el-form-item>
            <el-form-item label="市场价格" prop="marketPrice">
              <el-input v-model="priceForm.marketPrice" placeholder="请输入市场价格"> </el-input> 元/盒
            </el-form-item>
          </el-form>
        </div>
        <template slot="left-btn">
          <yl-button plain @click="closePriceModal">返回</yl-button>
        </template>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getInsuranceGoodsList, insuranceGoodsUpdate, addGoods } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import ChooseProduct from '../../component/ChooseProduct.vue'
export default {
  name: 'InsuranceDrugManage',
  components: {
    ChooseProduct
  },
  computed: {
  },
  filters: {
    controlStatus(e) {
      return e === 1 ? '开启' : '关闭'
    }
  },
  data() {
    var isPriceVlidator = (rule, value, callback) => {
      var pattern = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
      if (!pattern.test(value)) {
        return callback(new Error('请输入价格并不超过两位小数'))
      } else if (value == 0) {
        return callback(new Error('请输入正确的价格'))
      } else {
        return callback()
      }
    }
    return {
      query: {
        current: 1,
        size: 10,
        name: ''
      },
      priceDialogShow: false,
      // 列表
      dataList: [],
      loading: false,
      priceForm: {},
      priceFormRules: {
        insurancePrice: [
          { required: true, validator: isPriceVlidator, trigger: 'blur' }
        ],
        marketPrice: [
          { required: true, validator: isPriceVlidator, trigger: 'blur' }
        ]
      },
      priceFormType: 'ADD'
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getInsuranceGoodsList(
        query.current,
        query.size,
        query.name
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: ''
      }
    },
    //  添加
    addProduct() {
      this.$refs.choose.openGoodsDialog()
    },
    //  选择药品
    selectItem(data) {
      this.priceForm = {
        sellSpecificationsId: data.id,
        name: data.name,
        sellSpecifications: data.sellSpecifications,
        licenseNo: data.licenseNo,
        standardId: data.standardId
      }
      this.priceDialogShow = true
    },
    //  设置价格
    setPrice(row) {
      this.priceForm = { ...row }
      this.priceDialogShow = true
      this.priceFormType = 'EDIT'
    },
    resetPriceForm() {
      this.priceForm = {}
      this.priceFormType = 'ADD'
    },
    async confirm() {
      this.$refs.priceFormRef.validate(async valid => {
        if (valid) {
          let form = this.priceForm
          let data
          this.$common.showLoad()
          if (form.id) {
            //  修改
            data = await insuranceGoodsUpdate(
              form.id,
              form.insurancePrice,
              form.marketPrice
            )
          } else {
            //  添加
            data = await addGoods(
              form.insurancePrice,
              form.marketPrice,
              form.sellSpecificationsId,
              form.standardId
            )
          }
          this.$common.hideLoad()
          if (data !== undefined) {
            this.priceDialogShow = false
            this.$common.n_success('操作成功')
            this.getList()
          }
        }
      })

    },
    closePriceModal() {
      this.priceDialogShow = false
      if (this.priceFormType !== 'EDIT') {
        this.$refs.choose.openGoodsDialog()
      }
      this.priceFormType = 'ADD'
    },
    //  销售管控
    setSell(row) {
      this.$router.push({
        name: 'InsuranceChannelManage',
        params: { id: row.id }
      })
    }

  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>