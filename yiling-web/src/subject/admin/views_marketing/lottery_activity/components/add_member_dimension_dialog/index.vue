<template>
  <!-- 选择需添加的会员方案 -->
  <yl-dialog title="设置" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加的会员方案
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="goodsList"
          :total="goodsTotal"
          :loading="loading1"
          @getList="getList"
        >
          <el-table-column label="会员名称" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" :disabled="operationType == 1" @click="addClick(row)">添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加会员方案 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加会员方案
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="bottomGoodsTotal"
          :loading="loading2"
          @getList="getBottomList"
        >
          <el-table-column label="会员名称" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" :disabled="running || operationType == 1" @click="removeClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { lotteryCustomerScopeQueryMemberPage, lotteryCustomerScopeQueryHadAddMemberPage, lotteryCustomerScopeAddMember, lotteryCustomerScopeDeleteMember } from '@/subject/admin/api/view_marketing/lottery_activity'

export default {
  // 会员维度
  name: 'AddMemberDimensionDialog',
  components: {},
  computed: {
  },
  data() {
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      operationType: 1,
      lotteryActivityId: '',
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      loading2: false,
      bottomGoodsQuery: {
        page: 1,
        limit: 10
      },
      bottomGoodsList: [],
      bottomGoodsTotal: 0
    };
  },
  mounted() {
    console.log('mounted')
  },
  methods: {
    init(lotteryActivityId, running, operationType) {
      this.lotteryActivityId = lotteryActivityId
      this.running = running
      this.operationType = operationType
      this.addGoodsDialog = true;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await lotteryCustomerScopeQueryMemberPage(
        goodsQuery.page,
        goodsQuery.limit
      );
      this.loading1 = false
      if (data) {
        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    async getBottomList() {
      this.loading2 = true
      let bottomGoodsQuery = this.bottomGoodsQuery
      let data = await lotteryCustomerScopeQueryHadAddMemberPage(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.lotteryActivityId
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
        this.$emit('selectNumChange', data.total)
      }
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
    },
    async addClick(row) {
      let data = await lotteryCustomerScopeAddMember(this.lotteryActivityId, row.id)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 商品搜索
    bottomGoodsHandleSearch() {
      this.bottomGoodsQuery.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.bottomGoodsQuery = {
        page: 1,
        limit: 10
      }
    },
    async removeClick(row) {
      let data = await lotteryCustomerScopeDeleteMember(this.lotteryActivityId, row.id)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
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
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
