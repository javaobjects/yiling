<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入企业名称"/>
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
          :cell-class-name="getCellClass"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="商家信息" min-width="350" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="item-text font-size-base font-title-color"><span>企业名称：</span>{{ row.name }}【ID:{{ row.id }}】<span class="item-span">企业状态：<span :style="{color:row.status == 1 ? '#42b983' : '#d7000f'}">{{ row.status == 1 ? '启用' : '停用' }}</span></span></div>
                <div class="multi-text font-size-base font-title-color"><span>企业类型：</span>{{ row.type | dictLabel(companyType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>联系人姓名：</span>{{ row.contactor }}</div>
                <div class="item-text font-size-base font-title-color"><span>联系人电话：</span>{{ row.contactorPhone }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品数量" min-width="300" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base font-title-color"><span>上架中：</span>{{ row.upShelfCount }}</div>
                <div class="item-text font-size-base font-title-color"><span>已下架：</span>{{ row.unShelfCount }}</div>
                <div class="item-text font-size-base font-title-color"><span>商品总数：</span>{{ row.goodsCount }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="100" align="center">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="detaiClick(row)">查看详情</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { enterpriseList } from '@/subject/admin/api/cmp_api/commodity'
import { enterpriseType } from '@/subject/admin/utils/busi'
export default {
  name: 'CommodityManage',
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      query: {
        name: '',
        total: 0,
        page: 1,//当前页码
        limit: 10//分页数量
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await enterpriseList(
        query.page,
        query.name,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false
    },
     // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        name: '',
        total: 0,
        page: 1,//当前页码
        limit: 10//分页数量
      }
    },
    // 点击查看详情
    detaiClick(row) {
      this.$router.push({
        name: 'Business',
        params: {
          id: row.id,
          ename: row.name
        }
      });
    },
    getCellClass(row) {
      if (row.columnIndex == 2) {
        return 'border-1px-l'
      } 
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>