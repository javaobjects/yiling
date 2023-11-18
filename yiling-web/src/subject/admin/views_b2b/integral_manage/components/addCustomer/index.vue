<template>
  <yl-dialog 
    title="设置指定客户" 
    :visible.sync="addGoodsDialog" 
    width="900px" 
    :show-footer="false">
    <div class="dialog-content-box-customer">
      <div v-if="type != 2">
        <div class="dialog-content-top">
          <div class="title-view">
            <div class="sign"></div>
            选择需添加客户
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">企业ID</div>
                <el-input v-model="goodsQuery.id" placeholder="请输入企业ID" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="goodsQuery.name" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
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
          <div class="sign"></div>已添加客户
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业ID</div>
              <el-input v-model="bottomGoodsQuery.id" placeholder="请输入企业ID" @keyup.enter.native="bottomGoodsHandleSearch" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="bottomGoodsQuery.name" placeholder="请输入企业名称" @keyup.enter.native="bottomGoodsHandleSearch" />
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
import { giveEnterprisePage, giveEnterprisePageList, giveEnterpriseAdd, giveEnterpriseDelete } from '@/subject/admin/api/b2b_api/integral'
export default {
  components: {},
  computed: {
  },
  data() {
    return {
      addGoodsDialog: false,
      // 搜索商品
      loading1: false,
      goodsQuery: {
        id: '',
        name: '',
        total: 0,
        page: 1,
        limit: 10
      },
      goodsList: [],
      loading2: false,
      bottomGoodsQuery: {
        id: '',
        name: '',
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
      this.ruleId = ruleId;
      this.addGoodsDialog = show;
      //1 创建 2查看 3编辑
      this.type = type;
      this.getList()
      this.getBottomList()
    },
    async getList() {
      this.loading1 = true
      let query = this.goodsQuery
      let data = await giveEnterprisePage(
        query.id,
        query.name,
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
      let data = await giveEnterprisePageList(
        this.ruleId,
        query.id,
        query.name,
        query.page,
        query.limit
      );
      this.loading2 = false
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsQuery.total = data.total
        this.$emit('customerChange', data.total)
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
      this.$common.showLoad();
      let data = await giveEnterpriseAdd(
        this.ruleId,
        row.id,
        [],
        '',
        ''
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
      let data = await giveEnterpriseAdd(
        this.ruleId,
        '',
        [],
        query.id,
        query.name
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加搜索结果成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let eidList = [];
      this.goodsList.forEach(item => {
        eidList.push(item.id);
      });
      this.$common.showLoad();
      let data = await giveEnterpriseAdd(
        this.ruleId,
        '',
        eidList,
        '',
        ''
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
        page: 1,
        limit: 10
      }
    },
    //单个删除
    async removeClick(row) {
      this.$common.showLoad();
      let data = await giveEnterpriseDelete(
        this.ruleId,
        row.eid,
        [],
        '',
        ''
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('删除成功');
        if (this.bottomGoodsQuery.page > 1 && this.bottomGoodsList.length < 2) {
          this.bottomGoodsQuery.page = this.bottomGoodsQuery.page - 1 || 1
        }
        this.getBottomList()
      } 
    },
    // 删除搜索结果
    async deleteSearchClick() {
      let query = this.bottomGoodsQuery
      let data = await giveEnterpriseDelete(
        this.ruleId,
        '',
        [],
        query.id,
        query.name
      )
      if (data !== undefined) {
        this.$common.n_success('删除搜索结果成功');
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
        ids.push(item.eid);
      });
      this.$common.showLoad();
      let data = await giveEnterpriseDelete(
        this.ruleId,
        '',
        ids,
        '',
        ''
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
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
