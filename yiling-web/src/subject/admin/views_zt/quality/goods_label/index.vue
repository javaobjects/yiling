<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">标签查询</div>
              <el-input 
                class="mar-r-5" 
                v-model="query.name" 
                @keyup.enter.native="searchEnter" 
                placeholder="请输入标签名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-tb-16 down-box">
        <div class="down-box-left">
          <yl-button type="primary" @click="addTable">新建</yl-button>
        </div>
        <div class="btn">
          <yl-button type="primary" plain @click="batchDelect">批量删除</yl-button>
        </div>
      </div>
      <!-- 下部表格 -->
      <div>
        <yl-table 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList" 
          :selection-change="selectionChange">
          <el-table-column align="center" type="selection" width="50"></el-table-column>
          <el-table-column align="center" label="标签名称" prop="name"></el-table-column>
          <el-table-column 
            align="center" 
            min-width="200" 
            label="标签描述" 
            prop="description"
          ></el-table-column>
          <el-table-column 
            align="center" 
            min-width="100" 
            label="关联商品数量" 
            prop="standardNum"
          ></el-table-column>
          <el-table-column 
            align="center" 
            min-width="100" 
            label="标签类型" 
            prop="type">
            <template slot-scope="{ row }">
              <div>
                {{ row.type == 1 ? '手动标签' : '自动标签' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最后修改时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.updateTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column 
            fixed="right" 
            align="center" 
            label="操作" 
            width="120">
            <template slot-scope="{ row, $index }">
              <div>
                <yl-button type="text" @click="edit(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deleteClick(row,$index)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { queryTagsListPage, batchDeleteTags } from '@/subject/admin/api/quality';
export default {
  name: 'GoodsLabel',
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        name: ''
      },
      dataList: [],
      loading: false,
      //多选id
      dataId: [] 
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
      let data = await queryTagsListPage(
        query.page,
        query.name,
        query.limit
      )
      this.loading = false;
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      }
    },
    // 新建标签
    addTable() {
      this.$router.push({
        name: 'GoodsLabelAdd',
        params: { add: 0 } 
      });
    },
    selectionChange(val) {
      this.dataId = [];
      for (let i = 0; i < val.length; i++) {
        this.dataId.push(val[i].id)
      }
    },
    // 编辑标签
    edit(row) {
      this.$router.push({
        name: 'GoodsLabelAdd',
        params: row 
      });
    },
    // 删除标签
    deleteClick(row, index) {
      this.$confirm('确认删除 ' + row.name + ' 标签', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.$common.showLoad();
        let data = await batchDeleteTags([row.id])
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.dataList.splice(index, 1)
          this.getList();
        }
      }).catch(() => {});
    },
    // 批量删除
    batchDelect() {
      if (this.dataId.length < 1) {
        this.$message.warning('请勾选要删除的标签')
      } else {
        this.$confirm('确认删除 ！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          //确定
          this.$common.showLoad();
          let data = await batchDeleteTags(this.dataId)
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('删除成功!');
            this.getList();
          }
        }).catch(() => {});
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>