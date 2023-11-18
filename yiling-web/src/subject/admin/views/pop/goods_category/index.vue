<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">分类名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入分类名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
              <el-radio-group v-model="query.state">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>

      <div class="mar-tb-16">
        <yl-button type="primary" @click="addBanner">新增分类</yl-button>
      </div>

      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="120" label="分类名称" prop="name">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="包含的商品数" prop="goodsNum">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="排序" prop="sort">
          </el-table-column>
          <el-table-column align="center" width="120" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.status == 1 ? 'success' : 'danger'">{{ row.status | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getCategoryList } from '@/subject/admin/api/pop'

export default {
  name: 'GoodsCategory',
  components: {
  },
  computed: {
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        time: [],
        tTime: [],
        state: 0
      },
      dataList: [],
      loading: false
    }
  },
  activated() {
    this.getList()
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
      this.loading = true
      let query = this.query
      let data = await getCategoryList(
        query.page,
        query.limit,
        query.state,
        query.name
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        time: [],
        tTime: [],
        state: 0
      }
    },
    addBanner() {
      this.$router.push({
        name: 'GoodsCategoryEdit'
      });
    },
    edit(row) {
      // 跳转详情
      this.$router.push({
        name: 'GoodsCategoryEdit',
        params: { id: row.id }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

