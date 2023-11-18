<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :loading="loading"
          @getList="getList">
          <el-table-column align="center" min-width="200" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="120" label="所属栏目" prop="category">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="最新更新时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
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
import { queryDraftList, deleteDraft } from '@/subject/admin/api/content_api/article_video'
export default {
  name: 'ArticleDrafts',
  data() {
    return {
      dataList: [], 
      loading: false
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    // 获取数据
    async getList() {
      this.loading = true;
      let data = await queryDraftList()
      if (data) {
        this.dataList = data.list;
      }
      this.loading = false;
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AddEditArticle',
        params: { 
          id: row.id,
          type: 2,
          // 1 文章 2视频
          category: 1 
        }
      });
    },
    // 删除
    async deleteClick(row) {
      this.$confirm(`确认删除 ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await deleteDraft(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>