<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.establishTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn 
                :total="query.total" 
                @search="handleSearch" 
                @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="establishClick">添加</yl-button>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border 
          show-header 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="150" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建人" prop="createUserName">
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="120">
            <template slot-scope="{ row, $index }">
              <div>
                <yl-button type="text" @click="detailsClick(row)">详情</yl-button>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
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
import { pageList, questionDelete } from '@/subject/admin/api/content_api/question_handling'
export default {
  name: 'QuestionList',
  components: {},
  computed: {},
  data() {
    return {
      query: {
        title: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
    }
  },
  activated() {
    //获取数据
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
    // 获取数据
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await pageList(
        query.title,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.page,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
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
        title: '',
        lineId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 创建
    establishClick() {
      this.$router.push({
        name: 'AddEditQuestion',
        params: {
          id: 0,
          type: 1
        }
      });
    },
    // 详情
    detailsClick(row) {
      this.$router.push({
        name: 'AddEditQuestion',
        params: {
          id: row.id,
          type: 3
        }
      });
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AddEditQuestion',
        params: {
          id: row.id,
          type: 2
        }
      });
    },
    // 删除
    deleteClick(row, index) {
      this.$confirm(`确认删除 ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await questionDelete(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功!');
          this.dataList.splice(index,1);
          if (this.dataList && this.dataList.length < 1) {
            if (this.query.page > 1) {
              this.query.page --;
            }
          }
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
@import "./index.scss";
</style>