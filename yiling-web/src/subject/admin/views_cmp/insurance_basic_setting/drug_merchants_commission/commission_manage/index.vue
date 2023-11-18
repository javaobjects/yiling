<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="header-bar">
          <div class="sign"></div>
          提成设置
        </div>
        <div class="table-box mar-t-16">
          <div class="table-bar">
            <div class="title">选择药品</div>
            <div class="btn">
              <yl-button plain type="primary" @click="add">添加</yl-button>
            </div>
          </div>
          <yl-table
            stripe
            :show-header="true"
            :list="detailData.enterpriseCommissionList"
          >
            <el-table-column label="保险涉及药品" min-width="100" align="center" prop="goodsName"></el-table-column>
            <el-table-column label="规格" min-width="100" align="center" prop="sellSpecifications"></el-table-column>
            <el-table-column label="商家售卖金额/盒" min-width="100" align="center" prop="salePrice"></el-table-column>
            <el-table-column label="给终端结算额/盒 " min-width="100" align="center">
              <template slot-scope="{ row }">
                <el-input v-model="row.terminalSettlePrice" @keyup.native="row.terminalSettlePrice = onInput(row.terminalSettlePrice, 2)"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="center">
              <template slot-scope="{ row, $index }">
                <yl-button type="text" v-if="row.canDelete" @click="del(row, $index)">删除</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
      <!-- 选择商品 -->
      <choose-product ref="choose" :muti-select="true" :choose-list="detailData.enterpriseCommissionList" type="insuranceGoods" @selectItem="selectItem"/>
    </div>
  </div>
</template>

<script>
import { enterpriseGoodsDetail, enterpriseGoodsUpdate } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import ChooseProduct from '../../component/ChooseProduct.vue'
import { onInputLimit } from '@/common/utils'

export default {
  name: 'CommissionManage',
  components: {
    ChooseProduct
  },
  computed: {
  },
  filters: {
  },
  mounted() {
    this.eid = this.$route.params.eid
    if (this.eid) {
      this.getDetail(this.eid)
    }
  },
  data() {
    return {
      detailData: {}
    }
  },
  methods: {
    // 获取提成设置详情
    async getDetail(eid) {
      this.$common.showLoad()
      let data = await enterpriseGoodsDetail(eid)
      this.$common.hideLoad()
      if (data) {
        if (data.enterpriseCommissionList.length) {
          // 手动新添加的允许删除, 已添加的不可删除
          data.enterpriseCommissionList.forEach(item => {
            item.canDelete = false
          })
        }
        this.detailData = data
      }
    },
    // 添加
    add() {
      this.$refs.choose.openGoodsDialog()
    },
    selectItem(data) {
      this.detailData.enterpriseCommissionList.push({
        sellSpecificationsId: data.sellSpecificationsId,
        standardId: data.standardId,
        goodsName: data.name,
        sellSpecifications: data.sellSpecifications,
        salePrice: data.insurancePrice,
        terminalSettlePrice: data.marketPrice,
        // 新添加的可以删除(本地操作)
        canDelete: true
      })
    },
    // 删除
    del(row, index) {
      this.detailData.enterpriseCommissionList.splice(index, 1)
    },
    // 保存
    async save() {
      if (this.checkPriceInput()) {
        this.$common.warn('请设置给终端结算额');
        return false
      }
      let formData = {}
      formData.eid = this.detailData.eid
      formData.ename = this.detailData.ename
      formData.goodsRequest = this.detailData.enterpriseCommissionList.map(item => {
        return {
          id: item.id,
          sellSpecificationsId: item.sellSpecificationsId,
          standardId: item.standardId,
          goodsName: item.goodsName,
          salePrice: item.salePrice,
          terminalSettlePrice: Number(item.terminalSettlePrice)
        }
      })
      this.$common.showLoad()
      let data = await enterpriseGoodsUpdate(
        this.eid,
        formData.ename,
        formData.goodsRequest
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('操作成功')
        this.getDetail(this.eid)
      }
    },
    // 校验输入
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    // 校验列表里的终端结算额是否设置
    checkPriceInput() {
      let inputPriceValid = this.detailData.enterpriseCommissionList.some(item => !item.terminalSettlePrice || item.terminalSettlePrice == 0)
      return inputPriceValid
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
