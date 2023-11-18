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
          <el-table-column align="center" min-width="300" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="80" label="阅读量(pV)" prop="pageView"></el-table-column>
          <el-table-column align="center" min-width="100" label="创建时间">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="状态">
            <template slot-scope="{ row }">
              <div :style="{color: row.status == 2 ? '#52C41A' : '#E62412'}">{{ row.status == 2 ? '已发布' : '未发布' }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="创建者" prop="createUserName"></el-table-column>
          <el-table-column align="center" min-width="100" label="最新修改人" prop="updateUserName">
          </el-table-column>
         <el-table-column fixed="right" align="center" label="操作" min-width="100">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
                <yl-button type="text" @click="staticClick(row)">{{ row.status == 1 ? '发布' : '取消发布' }}</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { queryContentPage, isTopping } from '@/subject/admin/api/content_api/literature'
export default {
  name: 'LiteratureList',
  components: {
  },
  computed: {
  },
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
      let data = await queryContentPage(
        query.page,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.title,
        query.limit,
        ''
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击添加
    establishClick() {
      this.$router.push({
        name: 'AddEdit',
        params: { 
          id: 0,
          type: 1
        }
      });
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AddEdit',
        params: { 
          id: row.id,
          type: 2
        }
      });
    },
    //发布 /取消发布
    staticClick(row) {
      this.$confirm(`确认${ row.status == 1 ? '发布' : '取消发布'} ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await isTopping(
          row.id,
          row.status == 1 ? 2 : 1
        )
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
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
        establishTime: [],
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