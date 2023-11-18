<template>
  <!-- 选择需添加的会员方案 -->
  <yl-dialog title="设置" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加的会员方案
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">会员名称</div>
              <el-input v-model="goodsQuery.name" placeholder="请输入会员名称" @keyup.enter.native="goodsHandleSearch" />
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
          <ylButton type="primary" plain :disabled="operationType == 1" @click="batchAddClick">添加当前页</ylButton>
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
          <el-table-column label="会员名称" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.memberName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="有效期" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.validTime }}天</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.price }}元</div>
            </template>
          </el-table-column>
          <el-table-column label="展示名称" min-width="200" align="center">
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
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">会员名称</div>
              <el-input v-model="bottomGoodsQuery.name" placeholder="请输入会员名称" @keyup.enter.native="bottomGoodsHandleSearch" />
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
          <ylButton type="primary" plain @click="batchRemoveClick" :disabled="running || operationType == 1">删除当前页</ylButton>
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
          <el-table-column label="会员名称" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.memberName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="有效期" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.validTime }}天</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.price }}元</div>
            </template>
          </el-table-column>
          <el-table-column label="展示名称" min-width="200" align="center">
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
import { queryMemberCouponSpecsList, queryMemberCouponRelationLimit, saveMemberCouponRelation, deleteMemberCouponRelation } from '@/subject/admin/api/b2b_api/discount_coupon'
import { standardGoodsType } from '@/subject/admin/utils/busi'

export default {
  // 时长维度
  name: 'AddMemberDialog',
  components: {},
  computed: {
    // 商品类型
    standardGoodsType() {
      return standardGoodsType()
    }
  },
  data() {
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      operationType: 1,
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        name: ''
      },
      goodsList: [],
      goodsTotal: 0,
      loading2: false,
      bottomGoodsQuery: {
        page: 1,
        limit: 10,
        name: ''
      },
      bottomGoodsList: [],
      bottomGoodsTotal: 0
    };
  },
  mounted() {
    console.log('mounted')
  },
  methods: {
    init(couponActivityId, running, operationType) {
      this.couponActivityId = couponActivityId
      this.running = running
      this.operationType = operationType
      this.addGoodsDialog = true;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await queryMemberCouponSpecsList(
        goodsQuery.page,
        goodsQuery.limit,
        this.couponActivityId,
        goodsQuery.name
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
      let data = await queryMemberCouponRelationLimit(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.couponActivityId,
        bottomGoodsQuery.name
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
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
        limit: 10,
        name: ''
      }
    },
    async addClick(row) {
      let ids = []
      ids.push(row.id)
      let data = await saveMemberCouponRelation(this.couponActivityId, ids)
      if (data) {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let ids = [];
      this.goodsList.forEach(item => {
        ids.push(item.id)
      });

      let data = await saveMemberCouponRelation(this.couponActivityId, ids)
      if (data) {
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
        limit: 10,
        name: ''
      }
    },
    async removeClick(row) {
      let ids = []
      ids.push(row.id)
      let data = await deleteMemberCouponRelation(ids)
      if (data) {
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
        ids.push(item.id);
      });

      let data = await deleteMemberCouponRelation(ids)
      if (data) {
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
