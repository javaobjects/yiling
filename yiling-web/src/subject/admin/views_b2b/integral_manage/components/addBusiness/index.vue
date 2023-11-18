<template>
  <!-- 添加商品弹框 -->
  <div>
    <yl-dialog 
      width="900px"
      title="设置供应商"
      :show-footer="false"
      :visible.sync="businessShow"
      >
      <div class="dialog-content-box-customer">
        <div v-if="type != 2">
          <div class="dialog-content-top">
            <div class="title-view">
              <div class="sign"></div>选择需添加供应商
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="8">
                  <div class="title">企业ID</div>
                  <el-input v-model="query.eid" placeholder="请输入企业ID" />
                </el-col>
                <el-col :span="8">
                  <div class="title">企业名称</div>
                  <el-input v-model="query.ename" placeholder="请输入企业名称" @keyup.enter.native="goodsHandleSearch" />
                </el-col>
                <el-col :span="8">
                  <div class="title">企业所属区域</div>
                  <yl-choose-address width="230px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="24">
                  <div class="title">企业标签</div>
                  <div class="checkbox-group company-checkbox-group">
                    <el-checkbox-group v-model="query.tagIds" size="mini">
                      <el-checkbox border v-for="(item, index) in labelData" :key="index" :label="item.id">
                        {{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="16">
                  <yl-search-btn
                    :total="query.total"
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
              :total="query.total"
              :page.sync="query.page"
              :limit.sync="query.limit"
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
                <yl-choose-address 
                  width="230px" 
                  :province.sync="bottomGoodsQuery.provinceCode" 
                  :city.sync="bottomGoodsQuery.cityCode" 
                  :area.sync="bottomGoodsQuery.regionCode" 
                  is-all/>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="24">
                <div class="title">企业标签</div>
                <div class="checkbox-group company-checkbox-group">
                  <el-checkbox-group v-model="bottomGoodsQuery.tagIds" size="mini">
                    <el-checkbox border v-for="(item, index) in labelData" :key="index" :label="item.id">
                      {{ item.name }}
                    </el-checkbox>
                  </el-checkbox-group>
                </div>
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
            <el-table-column label="所属区域" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.address }}</div>
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
  </div>
 
</template>

<script>
import { queryEnterpriseListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { giveSellerScope, pageList, giveSellerScopeDelete, selectTags } from '@/subject/admin/api/b2b_api/integral'
import { ylChooseAddress } from '@/subject/admin/components'

// 选择商家
export default {
  components: {
    ylChooseAddress
  },
  computed: {
  },
  data() {
    return {
      // 弹窗是否显示
      businessShow: false,
      loading1: false,
      //上部列表商家
      query: {
        eid: '',
        ename: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        total: 0,
        page: 1,
        limit: 10
      },
      goodsList: [],
      
      //下部所添加的商家
      bottomGoodsQuery: {
        eid: '',
        ename: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        total: 0,
        page: 1,
        limit: 10
      },
      loading2: false,
      bottomGoodsList: [],
      //传递过来的规则id
      ruleId: '',
      //1 创建 2查看 3编辑
      type: 1,
      //企业标签
      labelData: []
    };
  },
  mounted() {
    
  },
  methods: {
    init(ruleId, show, type) {
      //规则id
      this.ruleId = ruleId;
      this.businessShow = show;
      //1 创建 2查看 3编辑
      this.type = type;
      //获取列表数据
      this.getList()
      this.getBottomList()
      //获取企业标签
      this.getTables();
    },
    async getTables() {
      let data = await selectTags();
      if (data) {
        this.labelData = data.list;
      }
    },
    async getList() {
      this.loading1 = true
      let query = this.query
      let data = await queryEnterpriseListPage(
        query.page,
        query.limit,
        Number(query.eid)|| '',
        query.ename,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.tagIds
      );
      if (data) {
        this.goodsList = data.records
        this.query.total = data.total
      }
      this.loading1 = false
    },
    //获取底部已添加的 供应商
    async getBottomList() {
      this.loading2 = true
      let query = this.bottomGoodsQuery;
      let data = await pageList(
        query.eid,
        query.ename,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        this.ruleId,
        query.page,
        query.limit,
        query.tagIds
      )
      if (data) {
        this.bottomGoodsList = data.records
        this.bottomGoodsQuery.total = data.total
        this.$emit('selectNumChange', data.total)
      }
      this.loading2 = false
    },
    // 商品搜索
    goodsHandleSearch() {
      this.query.page = 1
      this.getList()
    },
    goodsHandleReset() {
      this.query = {
        eid: '',
        ename: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async addClick(row) {
      this.$common.showLoad();
      let data = await giveSellerScope(
        row.eid,
        [],
        this.ruleId
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('添加成功');
        this.getBottomList()
      } 
    },
    // 批量添加商品
    async batchAddClick() {
      let enterpriseLimitList = [];
      this.goodsList.forEach(item => {
        enterpriseLimitList.push(item.eid);
      });
      this.$common.showLoad();
      let data = await giveSellerScope(
        '',
        enterpriseLimitList,
        this.ruleId
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
        eid: '',
        ename: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        tagIds: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    async removeClick(row) {
      this.$common.showLoad();
      let data = await giveSellerScopeDelete(
        row.eid,
        [],
        this.ruleId
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
    // 批量删除商品
    async batchRemoveClick() {
      let ids = [];
      this.bottomGoodsList.forEach(item => {
        ids.push(item.eid);
      });

      let data = await giveSellerScopeDelete(
        '',
        ids,
        this.ruleId
      )
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

<style lang="scss" scoped>
@import './index.scss';
</style>

