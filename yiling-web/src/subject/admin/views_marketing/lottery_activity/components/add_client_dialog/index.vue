<template>
  <!-- 添加客户弹框 -->
  <yl-dialog title="设置" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加客户
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="goodsQuery.eid" placeholder="请输入企业ID" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="goodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="goodsTotal"
                @search="goodsHandleSearch"
                @reset="goodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain :disabled="operationType == 1" @click="addSearchClick">添加搜索结果</ylButton>
          <ylButton type="primary" plain @click="batchAddClick" :disabled="operationType == 1">批量添加</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="goodsList"
          :total="goodsTotal"
          :page.sync="goodsQuery.page"
          :limit.sync="goodsQuery.limit"
          :loading="loading1"
          @getList="getList"
        >
          <el-table-column label="企业ID" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.name }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" @click="addClick(row)" :disabled="operationType == 1">添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加客户
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="bottomGoodsQuery.eid" placeholder="请输入企业ID" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="bottomGoodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="bottomGoodsTotal"
                @search="bottomGoodsHandleSearch"
                @reset="bottomGoodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="deleteSearchClick" :disabled="running || operationType == 1">删除搜索结果</ylButton>
          <ylButton type="primary" plain @click="batchRemoveClick" :disabled="running || operationType == 1">批量删除</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="bottomGoodsTotal"
          :page.sync="bottomGoodsQuery.page"
          :limit.sync="bottomGoodsQuery.limit"
          :loading="loading2"
          @getList="getBottomList"
        >
          <el-table-column label="企业ID" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.name }}</div>
              </div>
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
import { lotteryCustomerScopeQueryCustomerPage, lotteryCustomerScopeQueryHadAddCustomerPage, lotteryCustomerScopeAddCustomer, lotteryCustomerScopeDeleteCustomer } from '@/subject/admin/api/view_marketing/lottery_activity'

export default {
  // 指定客户
  name: 'AddClientDialog',
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
      let data = await lotteryCustomerScopeQueryCustomerPage(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.eid,
        goodsQuery.ename
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
      let data = await lotteryCustomerScopeQueryHadAddCustomerPage(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.lotteryActivityId,
        bottomGoodsQuery.eid,
        bottomGoodsQuery.ename
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
        this.$emit('selectNumChange', data.total)
      }
    },
    // 保存点击
    confirmClick() {
      this.addGoodsDialog = false;
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
      let eidList = []
      eidList.push(row.id)
      let data = await lotteryCustomerScopeAddCustomer(this.lotteryActivityId, 1, eidList)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 添加搜索结果
    async addSearchClick() {
      let goodsQuery = this.goodsQuery
      // 供应商限制（1-全部供应商；2-部分供应商）
      let data = await lotteryCustomerScopeAddCustomer(this.lotteryActivityId, 3, undefined, goodsQuery.eid, goodsQuery.ename)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let eidList = [];
      this.goodsList.forEach(item => {
        eidList.push(item.id);
      });

      let data = await lotteryCustomerScopeAddCustomer(this.lotteryActivityId, 2, eidList)
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
      let ids = []
      ids.push(row.id)
      let data = await lotteryCustomerScopeDeleteCustomer(this.lotteryActivityId, 1, ids)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 删除搜索结果
    async deleteSearchClick() {
      let bottomGoodsQuery = this.bottomGoodsQuery
      // enterpriseLimit 供应商限制（1-全部供应商；2-部分供应商）
      let data = await lotteryCustomerScopeDeleteCustomer(this.lotteryActivityId, 3, undefined, bottomGoodsQuery.eid, bottomGoodsQuery.ename)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量删除商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.id);
      });

      let data = await lotteryCustomerScopeDeleteCustomer(this.lotteryActivityId, 2, ids)
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
