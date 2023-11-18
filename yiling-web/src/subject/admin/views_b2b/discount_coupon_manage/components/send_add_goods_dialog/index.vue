<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="设置商品" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加商品
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="goodsQuery.goodsId" placeholder="请输入商品ID" @keyup.enter.native="goodsHandleSearch" />
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
          <ylButton type="primary" plain @click="batchAddClick">批量添加</ylButton>
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
          <el-table-column label="商品编号" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsName }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.sellSpecifications }}</div>
            </template>
          </el-table-column>
          <el-table-column label="包装规格" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.sellSpecifications }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ename }}</div>
            </template>
          </el-table-column>
          <el-table-column label="销售价" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">¥{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="库存数量（单位）" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsInventory }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" @click="addClick(row)">添加</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>已添加商品
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="bottomGoodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="bottomGoodsQuery.goodsId" placeholder="请输入商品ID" @keyup.enter.native="bottomGoodsHandleSearch" />
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
          <ylButton type="primary" plain @click="batchRemoveClick">批量删除</ylButton>
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
          <el-table-column label="商品编号" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsName }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="包装规格" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.sellSpecifications }}</div>
            </template>
          </el-table-column>
          <el-table-column label="企业名称" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ename }}</div>
            </template>
          </el-table-column>
          <el-table-column label="销售价" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">¥{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="库存数量（单位）" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsInventory }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button class="view-btn" type="text" @click="removeClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { 
  autoGiveQueryGoodsListPage, 
  autoGiveQueryGoodsLimitListPage, 
  autoGiveSaveGoodsLimit, 
  autoGiveDeleteGoodsLimit 
  } from '@/subject/admin/api/b2b_api/discount_coupon'
import { standardGoodsType } from '@/subject/admin/utils/busi'

export default {
  name: 'AddGoodsDialog',
  components: {},
  computed: {
    standardGoodsType() {
      return standardGoodsType()
    }
  },
  data() {
    return {
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
    init(couponActivityId) {
      this.couponActivityId = couponActivityId
      this.addGoodsDialog = true;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await autoGiveQueryGoodsListPage(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.goodsName,
        goodsQuery.goodsId
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
      let data = await autoGiveQueryGoodsLimitListPage(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.couponActivityId,
        bottomGoodsQuery.goodsName,
        bottomGoodsQuery.goodsId
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsTotal = data.total
      }
    },
    // 保存点击
    confirmClick() {
      this.addGoodsDialog = false;
      this.$emit('save', '111')
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
      row.couponActivityAutoGiveId = this.couponActivityId
      enterpriseLimitList.push(row)
      let data = await autoGiveSaveGoodsLimit(enterpriseLimitList)
      if (data) {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let enterpriseLimitList = [];
      this.goodsList.forEach(item => {
        item.couponActivityAutoGiveId = this.couponActivityId
        enterpriseLimitList.push(item);
      });
      if (enterpriseLimitList.length == 0) {
        return false
      }

      let data = await autoGiveSaveGoodsLimit(enterpriseLimitList)
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
        limit: 10
      }
    },
    async removeClick(row) {
      let ids = []
      ids.push(row.id)
      let data = await autoGiveDeleteGoodsLimit(this.couponActivityId, ids)
      if (data) {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.id);
      });
      if (ids.length == 0) {
        return false
      }

      let data = await autoGiveDeleteGoodsLimit(this.couponActivityId, ids)
      if (data) {
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
