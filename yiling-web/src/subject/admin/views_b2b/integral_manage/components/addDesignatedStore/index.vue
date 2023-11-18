<template>
  <!-- 添加商品弹框 -->
  <yl-dialog 
    title="设置商品" 
    :visible.sync="addGoodsDialog" 
    width="900px" 
    :show-footer="false">
    <div class="dialog-content-box-customer">
      <div v-if="type != 2">
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
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="goodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">以岭品</div>
                <el-select v-model="goodsQuery.yilingGoodsFlag" placeholder="请选择">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option label="以岭品" :value="1"></el-option>
                  <el-option label="非以岭品" :value="2"></el-option>
                </el-select>
              </el-col>
              <el-col :span="8">
                <div class="title">上下架状态</div>
                <el-select v-model="goodsQuery.goodsStatus" placeholder="请选择">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option label="上架" :value="1"></el-option>
                  <el-option label="下架" :value="2"></el-option>
                  <el-option label="待设置" :value="3"></el-option>
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
                <div class="font-size-base">{{ row.goodsId }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品类型" min-width="80" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="250" align="center">
              <template slot-scope="{ row }">
                <div class="goods-detail">
                  <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.goodsName" placement="top-start">
                    <div class="font-size-base detail">{{ row.goodsName }}</div>
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
            <el-table-column label="企业名称" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.ename }}</div>
              </template>
            </el-table-column>
            <el-table-column label="销售单价" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">¥{{ row.price }}</div>
              </template>
            </el-table-column>
            <el-table-column label="库存数量（单位）" min-width="80" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsInventory }} {{ row.sellUnit }}</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div v-if="row.goodsStatus == 1" class="font-size-base up">上架</div>
                <div v-else-if="row.goodsStatus == 2" class="font-size-base">下架</div>
                <div v-else class="font-size-base">待设置</div>
              </template>
            </el-table-column>
            <el-table-column label="以岭品" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.yilingGoodsFlag == 1 ? '是' : '否' }}</div>
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
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="bottomGoodsQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">以岭品</div>
              <el-select v-model="bottomGoodsQuery.yilingGoodsFlag" placeholder="请选择">
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
              <div class="font-size-base">{{ row.goodsId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品类型" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div class="goods-detail">
                <el-tooltip class="item" effect="dark" popper-class="my-tooltip-view" :content="row.goodsName" placement="top-start">
                  <div class="font-size-base detail">{{ row.goodsName }}</div>
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
          <el-table-column label="企业名称" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.ename }}</div>
            </template>
          </el-table-column>
          <el-table-column label="销售单价" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">¥{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="库存数量（单位）" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.goodsInventory }} {{ row.sellUnit }}</div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div v-if="row.goodsStatus == 1" class="font-size-base up">上架</div>
              <div v-else-if="row.goodsStatus == 2" class="font-size-base">下架</div>
              <div v-else class="font-size-base">待设置</div>
            </template>
          </el-table-column>
          <el-table-column label="以岭品" min-width="55" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.yilingGoodsFlag == 1 ? '是' : '否' }}</div>
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
import { giveEnterpriseGoodsList, addGiveEnterpriseGoods, giveEnterpriseGoodsPageList, deleteGiveEnterpriseGoods } from '@/subject/admin/api/b2b_api/integral'
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
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        goodsName: '',
        goodsId: '',
        ename: '',
        yilingGoodsFlag: 0,
        goodsStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      loading2: false,
      bottomGoodsQuery: {
        goodsName: '',
        goodsId: '',
        ename: '',
        yilingGoodsFlag: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      bottomGoodsList: [],
      //传递过来的规则id
      ruleId: '',
      //商家范围
      merchantScope: '',
      //1 创建 2查看 3编辑
      type: 1
    };
  },
  mounted() {
  },
  methods: {
    init(ruleId, show, type, merchantScope) {
      this.ruleId = ruleId;
      this.addGoodsDialog = show;
      this.type = type;
      this.merchantScope = merchantScope
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let query = this.goodsQuery
      let data = await giveEnterpriseGoodsList(
        this.ruleId,
        this.merchantScope,
        query.goodsName,
        query.goodsId,
        query.ename,
        query.yilingGoodsFlag,
        query.goodsStatus,
        query.page,
        query.limit
      );
      if (data) {
        this.goodsList = data.records
        this.goodsQuery.total = data.total
      }
      this.loading1 = false
    },
    async getBottomList() {
      this.loading2 = true
      let query = this.bottomGoodsQuery
      let data = await giveEnterpriseGoodsPageList(
        this.ruleId,
        this.merchantScope,
        query.goodsName,
        query.goodsId,
        query.ename,
        query.yilingGoodsFlag,
        query.page,
        query.limit
      );
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsQuery.total = data.total
        this.$emit('designatedStoreChange', data.total)
      }
      this.loading2 = false
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        goodsName: '',
        goodsId: '',
        ename: '',
        yilingGoodsFlag: 0,
        goodsStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async addClick(row) {
      this.$common.showLoad();
      let data = await addGiveEnterpriseGoods(
        this.ruleId,
        this.merchantScope,
        '',
        '',
        '',
        row.yilingGoodsFlag,
        row.goodsStatus,
        row.goodsId,
        []
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
      let data = await addGiveEnterpriseGoods(
        this.ruleId,
        this.merchantScope,
        query.goodsName,
        query.goodsId,
        query.ename,
        query.yilingGoodsFlag,
        query.goodsStatus,
        '',
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
      let goodsIdList = [];
      this.goodsList.forEach(item => {
        goodsIdList.push(item.goodsId);
      });
      this.$common.showLoad();
      let data = await addGiveEnterpriseGoods(
        this.ruleId,
        this.merchantScope,
        '',
        '',
        '',
        0,
        0,
        '',
        goodsIdList
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
        goodsName: '',
        goodsId: '',
        ename: '',
        yilingGoodsFlag: 0,
        goodsStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //单独删除
    async removeClick(row) {
      this.$common.showLoad();
      let data = await deleteGiveEnterpriseGoods(
        this.ruleId,
        '',
        '',
        '',
        row.yilingGoodsFlag,
        row.goodsId,
        []
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
      let data = await deleteGiveEnterpriseGoods(
        this.ruleId,
        query.goodsName,
        query.goodsId,
        query.ename,
        query.yilingGoodsFlag,
        '',
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
    // 批量删除商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.goodsId);
      });
      this.$common.showLoad();
      let data = await deleteGiveEnterpriseGoods(
        this.ruleId,
        '',
        '',
        '',
        0,
        '',
        ids
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('批量删除商品成功');
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
