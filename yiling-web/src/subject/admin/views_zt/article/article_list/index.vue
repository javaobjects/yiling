<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">文章名称</div>
              <el-input v-model.trim="query.articleTitle" @keyup.enter.native="searchEnter" placeholder="请输入文章名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select class="select-width" v-model="query.articleStatus" placeholder="请选择">
                <el-option :key="0" label="全部" :value="0"></el-option>
                <el-option :key="1" label="启用" :value="1"></el-option>
                <el-option :key="2" label="停用" :value="2"></el-option>
              </el-select>
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
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" @click="addArticleClick">新建</ylButton>
        </div>
      </div>
      <!-- 下部 表格 -->
      <div class="search-bar">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" label="#" type="index" width="100">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="200" label="文章名称" prop="articleTitle">
          </el-table-column>
          <el-table-column align="center" min-width="200" label="描述" prop="articleDesc">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="状态" >
            <template slot-scope="{ row }">
              <div>
                <span v-if="row.articleStatus == 1" style="color:#42b983">
                  启用
                </span>
                <span v-else style="color:#ee0a24">
                  停用
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="创建信息" >
            <template slot-scope="{ row }">
              <p>{{ row.createUserName }}</p>
              <p>{{ row.createTime | formatDate }}</p>
              <!-- <p>{{ row.cjTime | formatDate }}</p> -->
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="最后维护信息" >
            <template slot-scope="{ row }">
              <p>{{ row.updateUserName }}</p>
              <p>{{ row.updateTime | formatDate }}</p>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
              </div>
              <div>
                <yl-button type="text" :disabled="row.articleStatus == 1 ? false : true " @click="urlClick(row)">复制链接</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="deactivateClick(row)">{{ row.articleStatus == 1 ? '停用' : '启用' }}</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { pageList, switchStatus } from '@/subject/admin/api/zt_api/article'
export default {
  name: 'ArticleList',
  components: {
  },
  data() {
    return {
      query: {
        articleTitle: '',
        articleStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
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
      let data = await pageList(
        query.articleStatus,
        query.articleTitle,
        query.page,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 新建文章
    addArticleClick() {
      this.$router.push('/article/article_list_detail/' + '0');
    },
    // 编辑
    modifyClick(row) {
      this.$router.push('/article/article_list_detail/' + row.id);
    },
    // 复制链接
    urlClick(row) {
      // ${process.env.VUE_APP_H5_URL}/#/b2b/html/${id}
      // console.log(row,'row')
      if (row.id != '' && row.id != undefined && row.id !== null) {
        let cInput = document.createElement('input');
        cInput.value = process.env.VUE_APP_H5_URL + '/#/b2b/html/' + row.id;
        document.body.appendChild(cInput);
        cInput.select();
        document.execCommand('Copy');
        this.$common.n_success('复制链接成功')
        cInput.remove();
      }
      // console.log(document.execCommand('copy'),99)
    },
    // 启用/停用
    deactivateClick(row) {
      this.$confirm(`确认${row.articleStatus == 1 ? '停用' : '启用'} ${ row.articleTitle } ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          this.$common.showLoad();
          let data = await switchStatus(
            row.articleStatus == 1 ? 2 : 1,
            row.id
          )
          this.$common.hideLoad();
          if (data!==undefined) {
            this.$common.n_success(`${row.articleStatus == 1 ? '停用' : '启用'} 成功!`);
            this.getList();
          }
        })
        .catch(() => {
          //取消
        });
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 清空查询
    handleReset() {
      this.query = {
        articleTitle: '',
        articleStatus: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>