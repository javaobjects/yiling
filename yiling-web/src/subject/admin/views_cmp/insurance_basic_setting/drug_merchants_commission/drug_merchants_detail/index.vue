<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="header-bar">
          <div class="sign"></div>
          结算账户
        </div>
        <div class="table-box mar-t-16">
          <div class="table-bar">
            <el-row>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">账户类型：<span class="item-value">{{ detailData.accountType | dictLabel(hmcEnterpriseAccountType) }}</span></div>
                </div>
                <div class="item">
                  <div class="item-title">开户行：<span class="item-value">{{ detailData.accountBank }}</span></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">账户名：<span class="item-value">{{ detailData.accountName }}</span></div>
                </div>
                <div class="item">
                  <div class="item-title">备注：<span class="item-value">{{ detailData.remark }}</span></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <div class="item-title">账户：<span class="item-value">{{ detailData.accountNumber }}</span></div>
                </div>
              </el-col>
            </el-row>
          </div>
          <yl-table
            stripe
            :show-header="true"
            :list="detailData.enterpriseCommissionList"
          >
            <el-table-column label="保险涉及药品" min-width="100" align="center" prop="goodsName"></el-table-column>
            <el-table-column label="规格" min-width="100" align="center" prop="sellSpecifications"></el-table-column>
            <el-table-column label="商家售卖金额" min-width="100" align="center" prop="salePrice"></el-table-column>
            <el-table-column label="给终端结算额" min-width="100" align="center" prop="terminalSettlePrice"></el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 选择商品 -->
    </div>
  </div>
</template>

<script>
import { enterpriseDetail } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
import { hmcEnterpriseAccountType } from '@/subject/admin/busi/cmp/insurance_basic_setting'

export default {
  name: 'DrugMerchantsDetail',
  components: {
  },
  computed: {
    // 账户类型
    hmcEnterpriseAccountType() {
      return hmcEnterpriseAccountType()
    }
  },
  filters: {
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail(this.id)
    }
  },
  data() {
    return {
      // 详情数据
      detailData: {}
    }
  },
  methods: {
    async getDetail(id) {
      this.$common.showLoad()
      let data = await enterpriseDetail(id)
      this.$common.hideLoad()
      if (data) {
        this.detailData = data
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
