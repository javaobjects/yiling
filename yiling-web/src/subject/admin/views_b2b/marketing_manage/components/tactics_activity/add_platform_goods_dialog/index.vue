<template>
  <!-- 添加商品弹框 -->
  <yl-dialog title="设置" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
    <div class="dialog-content-box-customer">
      <div class="dialog-content-top">
        <div class="title-view">
          <div class="sign"></div>选择需添加平台SKU
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="goodsQuery.standardId" placeholder="请输入商品ID" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">规格ID</div>
              <el-input v-model="goodsQuery.sellSpecificationsId" placeholder="请输入规格ID" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="goodsQuery.manufacturer" placeholder="请输入生产厂家" @keyup.enter.native="goodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">以岭品</div>
              <el-select v-model="goodsQuery.isYiLing" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="以岭品" :value="1"></el-option>
                <el-option label="非以岭品" :value="2"></el-option>
              </el-select>
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
          <el-table-column label="商品ID" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.standardId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="规格ID" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.id }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div class="goods-detail">
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.name" placement="top-start">
                  <div class="font-size-base detail">{{ row.name }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.sellSpecifications" placement="top-start">
                  <div class="font-size-base detail">{{ row.sellSpecifications }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.manufacturer" placement="top-start">
                  <div class="font-size-base detail">{{ row.manufacturer }}</div>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="以岭品" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ylFlag == 1 ? '是' : '否' }}</div>
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
          <div class="sign"></div>已添加平台SKU
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品ID</div>
              <el-input v-model="bottomGoodsQuery.standardId" placeholder="请输入商品ID" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">规格ID</div>
              <el-input v-model="bottomGoodsQuery.sellSpecificationsId" placeholder="请输入规格ID" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="bottomGoodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="bottomGoodsQuery.manufacturer" placeholder="请输入生产厂家" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">以岭品</div>
              <el-select v-model="bottomGoodsQuery.isYiLing" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="以岭品" :value="1"></el-option>
                <el-option label="非以岭品" :value="2"></el-option>
              </el-select>
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
          <el-table-column label="商品ID" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.standardId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="规格ID" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.sellSpecificationsId }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div class="goods-detail">
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.name" placement="top-start">
                  <div class="font-size-base detail">{{ row.name }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.sellSpecifications" placement="top-start">
                  <div class="font-size-base detail">{{ row.sellSpecifications }}</div>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.manufacturer" placement="top-start">
                  <div class="font-size-base detail">{{ row.manufacturer }}</div>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="以岭品" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ylFlag == 1 ? '是' : '否' }}</div>
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
import { strategyLimitPlatformGoodsList, strategyLimitPlatformGoodsAdd, strategyLimitPlatformGoodsDelete, strategyLimitPlatformGoodsPageList } from '@/subject/admin/api/b2b_api/marketing_manage'
import { standardGoodsType } from '@/subject/admin/utils/busi'

export default {
  name: 'AddPlatformGoodsDialog',
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
      marketingStrategyId: '',
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        isYiLing: 0
      },
      goodsList: [],
      goodsTotal: 0,
      loading2: false,
      bottomGoodsQuery: {
        page: 1,
        limit: 10,
        isYiLing: 0
      },
      bottomGoodsList: [],
      bottomGoodsTotal: 0
    };
  },
  mounted() {
  },
  methods: {
    init(marketingStrategyId, running, operationType) {
      this.marketingStrategyId = marketingStrategyId
      this.running = running
      this.operationType = operationType
      this.addGoodsDialog = true;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await strategyLimitPlatformGoodsList(
        goodsQuery.page,
        goodsQuery.limit,
        this.marketingStrategyId,
        Number(goodsQuery.standardId) || '',
        Number(goodsQuery.sellSpecificationsId) || '',
        goodsQuery.goodsName,
        goodsQuery.manufacturer,
        goodsQuery.isYiLing
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
      let data = await strategyLimitPlatformGoodsPageList(
        bottomGoodsQuery.page,
        bottomGoodsQuery.limit,
        this.marketingStrategyId,
        Number(bottomGoodsQuery.standardId) || '',
        Number(bottomGoodsQuery.sellSpecificationsId) || '',
        bottomGoodsQuery.goodsName,
        bottomGoodsQuery.manufacturer,
        bottomGoodsQuery.isYiLing
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
        limit: 10,
        isYiLing: 0
      }
    },
    async addClick(row) {
      let sellSpecificationsIdList = []
      sellSpecificationsIdList.push(row.id)
      let data = await strategyLimitPlatformGoodsAdd(this.marketingStrategyId, sellSpecificationsIdList)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 添加搜索结果
    async addSearchClick() {
      let goodsQuery = this.goodsQuery
      // 供应商限制（1-全部供应商；2-部分供应商）
      let data = await strategyLimitPlatformGoodsAdd(this.marketingStrategyId, undefined, Number(goodsQuery.standardId) || '', Number(goodsQuery.sellSpecificationsId) || '', goodsQuery.goodsName, goodsQuery.manufacturer, goodsQuery.isYiLing)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let sellSpecificationsIdList = [];
      this.goodsList.forEach(item => {
        sellSpecificationsIdList.push(item.id);
      });

      let data = await strategyLimitPlatformGoodsAdd(this.marketingStrategyId, sellSpecificationsIdList)
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
        limit: 10,
        isYiLing: 0
      }
    },
    async removeClick(row) {
      let ids = []
      ids.push(row.sellSpecificationsId)
      let data = await strategyLimitPlatformGoodsDelete(this.marketingStrategyId, ids)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (ids.length == this.bottomGoodsList.length ){
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
      } 
    },
    // 删除搜索结果
    async deleteSearchClick() {
      let bottomGoodsQuery = this.bottomGoodsQuery
      // enterpriseLimit 供应商限制（1-全部供应商；2-部分供应商）
      let data = await strategyLimitPlatformGoodsDelete(this.marketingStrategyId, undefined, Number(bottomGoodsQuery.standardId) || '', Number(bottomGoodsQuery.sellSpecificationsId) || '', bottomGoodsQuery.goodsName, bottomGoodsQuery.manufacturer, bottomGoodsQuery.isYiLing)
      if (typeof data !== 'undefined') {
        this.$common.n_success('保存成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.sellSpecificationsId);
      });

      let data = await strategyLimitPlatformGoodsDelete(this.marketingStrategyId, ids)
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
.my-tooltip-view{
  max-width: 500px;
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
