<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="设置供应商" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加供应商
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
            <el-col :span="8">
              <div class="title">企业所属区域</div>
              <yl-choose-address width="230px" :province.sync="goodsQuery.provinceCode" :city.sync="goodsQuery.cityCode" :area.sync="goodsQuery.regionCode" is-all />
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
          <ylButton type="primary" plain :disabled="operationType == 1" @click="batchAddClick">批量添加</ylButton>
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
              <div class="font-size-base">{{ row.eid }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.ename }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="所属区域" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.address }}</div>
              </div>
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
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加供应商
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
            <el-col :span="8">
              <div class="title">企业所属区域</div>
              <yl-choose-address width="230px" :province.sync="bottomGoodsQuery.provinceCode" :city.sync="bottomGoodsQuery.cityCode" :area.sync="bottomGoodsQuery.regionCode" is-all />
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
              <div class="font-size-base">{{ row.eid }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.ename }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="所属区域" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.address }}</div>
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
import { queryEnterpriseListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { presalePromoterLimitPromoterMemberPageList, presalePromoterLimitPromoterMemberAdd, presalePromoterLimitPromoterMemberDelete } from '@/subject/admin/api/view_marketing/goods_presell'
import { ylChooseAddress } from '@/subject/admin/components'

// 选择商家
export default {
  name: 'AddProviderDialog',
  components: {
    ylChooseAddress
  },
  computed: {
  },
  data() {
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 1-指定商家 2-指定会员推广方
      fromType: 1,
      operationType: 1,
      marketingStrategyId: '',
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
    init(marketingStrategyId, running, operationType) {
      this.initData()
      this.marketingStrategyId = marketingStrategyId
      this.running = running
      this.operationType = operationType
      this.addGoodsDialog = true;
      this.getList()
      this.getBottomList()
    },
    initData() {
      this.goodsHandleReset()
      this.bottomGoodsHandleReset()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await queryEnterpriseListPage(
        goodsQuery.page,
        goodsQuery.limit,
        Number(goodsQuery.eid)|| '',
        goodsQuery.ename,
        goodsQuery.provinceCode,
        goodsQuery.cityCode,
        goodsQuery.regionCode
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
      let data = await presalePromoterLimitPromoterMemberPageList(
          bottomGoodsQuery.page,
          bottomGoodsQuery.limit,
          this.marketingStrategyId,
          Number(bottomGoodsQuery.eid) || '',
          bottomGoodsQuery.ename,
          bottomGoodsQuery.provinceCode,
          bottomGoodsQuery.cityCode,
          bottomGoodsQuery.regionCode
        )
      
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
        this.$emit('selectNumChange', data.total, this.fromType)
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
      let enterpriseLimitList = []
      enterpriseLimitList.push(row.eid)

      let data = await presalePromoterLimitPromoterMemberAdd(this.marketingStrategyId, enterpriseLimitList)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let enterpriseLimitList = [];
      this.goodsList.forEach(item => {
        enterpriseLimitList.push(item.eid);
      });

      let data = await presalePromoterLimitPromoterMemberAdd(this.marketingStrategyId, enterpriseLimitList)
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
      ids.push(row.eid)

      let data = await presalePromoterLimitPromoterMemberDelete(this.marketingStrategyId, ids)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (ids.length == this.bottomGoodsList.length ){
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.eid);
      });

      let data = await presalePromoterLimitPromoterMemberDelete(this.marketingStrategyId, ids)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (ids.length == this.bottomGoodsList.length ){
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
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
