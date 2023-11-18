<template>
  <!-- 添加商品弹框 -->
  <yl-dialog 
    title="指定平台SKU设置" 
    :visible.sync="addGoodsDialog" 
    width="900px" 
    :show-footer="false">
    <div class="dialog-content-box-customer">
      <div v-if="type != 2">
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
                  :total="goodsQuery.total"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="down-box clearfix">
          <div class="btn">
            <ylButton type="primary" plain @click="addSearchClick">添加搜索结果</ylButton>
            <ylButton type="primary" plain @click="batchAddClick">批量添加</ylButton>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsQuery.total"
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
                  <yl-button class="view-btn" type="text" @click="addClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
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
                :total="bottomGoodsQuery.total"
                @search="bottomGoodsHandleSearch"
                @reset="bottomGoodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix" v-if="type != 2">
        <div class="btn">
          <ylButton type="primary" plain @click="deleteSearchClick">删除搜索结果</ylButton>
          <ylButton type="primary" plain @click="batchRemoveClick">批量删除</ylButton>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
        <yl-table
          :stripe="true"
          :show-header="true"
          :list="bottomGoodsList"
          :total="bottomGoodsQuery.total"
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
          <el-table-column label="操作" min-width="55" align="center" v-if="type != 2">
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
import { givePlatformGoodsList, givePlatformGoodsPageList, addGivePlatformGoods, deleteGivePlatformGoods } from '@/subject/admin/api/b2b_api/integral'
import { standardGoodsType } from '@/subject/admin/utils/busi'

export default {
  components: {},
  computed: {
    // 商品类型
    standardGoodsType() {
      return standardGoodsType()
    }
  },
  data() {
    return {
      //是否展示弹窗
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      //上部列表数据
      goodsQuery: {
        standardId: '',
        sellSpecificationsId: '',
        goodsName: '',
        manufacturer: '',
        isYiLing: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      goodsList: [],

      loading2: false,
      //下部已添加的数据
      bottomGoodsQuery: {
        standardId: '',
        sellSpecificationsId: '',
        goodsName: '',
        manufacturer: '',
        isYiLing: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      bottomGoodsList: [],
      //传递过来的规则id
      ruleId: '',
      //1 创建 2查看 3编辑
      type: 1
    };
  },
  mounted() {
  },
  methods: {
    init(ruleId, show, type) {
      this.ruleId = ruleId
      this.addGoodsDialog = show
      //1 创建 2查看 3编辑
      this.type = type;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let query = this.goodsQuery
      let data = await givePlatformGoodsList(
        this.ruleId,
        query.standardId,
        query.sellSpecificationsId,
        query.goodsName,
        query.manufacturer,
        query.isYiLing,
        query.page,
        query.limit
      );
      if (data) {
        this.goodsList = data.records
        this.goodsQuery.total = data.total
      }
      this.loading1 = false
    },
    //获取底部 已添加的数据
    async getBottomList() {
      this.loading2 = true
      let query = this.bottomGoodsQuery
      let data = await givePlatformGoodsPageList(
        this.ruleId,
        query.standardId,
        query.sellSpecificationsId,
        query.goodsName,
        query.manufacturer,
        query.isYiLing,
        query.page,
        query.limit
      );
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsQuery.total = data.total
        this.$emit('specifyPlatformChange', data.total)
      }
      this.loading2 = false
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
        standardId: '',
        sellSpecificationsId: '',
        goodsName: '',
        manufacturer: '',
        isYiLing: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async addClick(row) {
      this.$common.showLoad();
      let data = await addGivePlatformGoods(
        this.ruleId,
        '',
        '',
        '',
        '',
        row.ylFlag,
        [row.id]
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加成功');
        this.getBottomList()
      } 
    },
    // 添加搜索结果
    async addSearchClick() {
      let query = this.goodsQuery
      this.$common.showLoad();
      let data = await addGivePlatformGoods(
        this.ruleId,
        query.standardId,
        query.sellSpecificationsId,
        query.goodsName,
        query.manufacturer,
        query.isYiLing,
        []
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加搜索结果成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let sellSpecificationsIdList = [];
      this.goodsList.forEach(item => {
        sellSpecificationsIdList.push(item.id);
      });
      this.$common.showLoad();
      let data = await addGivePlatformGoods(
        this.ruleId,
        '',
        '',
        '',
        '',
        0,
        sellSpecificationsIdList
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('批量添加成功');
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
        standardId: '',
        sellSpecificationsId: '',
        goodsName: '',
        manufacturer: '',
        isYiLing: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async removeClick(row) {
      this.$common.showLoad();
      let data = await deleteGivePlatformGoods(
        this.ruleId,
        '',
        '',
        '',
        '',
        row.ylFlag,
        [row.sellSpecificationsId]
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('删除成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (this.bottomGoodsQuery.page > 1 && this.bottomGoodsList.length < 2) {
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
      } 
    },
    // 删除搜索结果
    async deleteSearchClick() {
      let query = this.bottomGoodsQuery
      this.$common.showLoad();
      let data = await deleteGivePlatformGoods(
        this.ruleId,
        query.standardId,
        query.sellSpecificationsId,
        query.goodsName,
        query.manufacturer,
        query.isYiLing,
        []
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('删除搜索结果成功');
        // 如果当前页数据 全部删除，需要回前一页
        if (this.bottomGoodsQuery.page > 1 && this.bottomGoodsList.length < 2) {
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
      } 
    },
    // 批量删除
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.sellSpecificationsId);
      });
      this.$common.showLoad();
      let data = await deleteGivePlatformGoods(
        this.ruleId,
        '',
        '',
        '',
        '',
        0,
        ids
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('批量删除成功');
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
