<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="common-box-title">{{ title }}</div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">药品名称</div>
              <el-input v-model="query.title" placeholder="请输入药品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">上下架状态</div>
              <el-select v-model="query.goodsStatus" placeholder="请选择">
                <el-option v-for="item in typeData" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-8 order-table-view">
        <yl-table
          ref="table"
          stripe
          :list="dataList"
          :total="query.total"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="商品" min-width="300" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.goodsName }}</div>
                <div class="multi-text font-size-base font-title-color"><span>商品规格：</span>{{ row.specifications }}</div>
                <div class="multi-text font-size-base font-title-color"><span>批准文号：</span>{{ row.licenseNo }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品ID" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>标准库SPU-ID：{{ row.standardId }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>标准库SKU-ID：{{ row.skuId }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>企业商品ID：{{ row.goodsId }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="对码信息" min-width="140" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>C端平台药房ID：{{ row.ihCPlatformId }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>配送商商品ID：{{ row.ihPharmacyGoodsId }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="上下架状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span :style="{ color:row.goodsStatus == 1 ? '#52C41A' : '#F52922' }">{{ row.goodsStatus == 1 ? '上架' : '下架' }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="所属标准库分类" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.standardCategoryName }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="销售价" min-width="90" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.ihGoodsInfoVO.salePrice ? '￥' + row.ihGoodsInfoVO.salePrice : '' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="成本价" min-width="90" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.ihGoodsInfoVO.marketPrice ? '￥' + row.ihGoodsInfoVO.marketPrice : '' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="库存" min-width="90" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.ihGoodsInfoVO ? row.ihGoodsInfoVO.stock : '' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="安全库存" min-width="90" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.ihGoodsInfoVO ? row.ihGoodsInfoVO.safeStock : '' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.createTime | formatDate }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="药+险所需库存数量" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.frozenQty }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="当前实际库存" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">{{ row.qty }}</div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { detailsList } from '@/subject/admin/api/cmp_api/commodity'
export default {
  data() {
    return {
      query: {
        title: '',
        goodsStatus: '',
        total: 0,
        page: 1,//当前页码
        limit: 10,//分页数量
        eid: ''
      },
      loading: false,
      dataList: [],
      typeData: [
        {
          label: '全部',
          value: ''
        },
        {
          label: '上架',
          value: 1
        },
        {
          label: '下架',
          value: 2
        }
      ],
      showDialog: false,
      title: ''
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.query.eid = query.id;
      this.title = query.ename;
      this.getList();
    }
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await detailsList(
        query.eid,
        query.page,
        query.title,
        query.limit,
        query.goodsStatus
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total
      }
      this.loading = false;
    },
     // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        eid: this.query.eid,
        title: '',
        goodsStatus: '',
        total: 0,
        page: 1,//当前页码
        limit: 10//分页数量
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>