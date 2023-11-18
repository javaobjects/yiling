<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">场景名称</div>
              <el-select v-model="query.sceneId" placeholder="请选择">
              <el-option
                v-for="item in hmcGreetingSceneData"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <yl-button type="primary" @click="modifyClick(0)">新建场景</yl-button>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="80" label="场景ID" prop="sceneId">
          </el-table-column>
          <el-table-column align="center" min-width="180" label="场景名称">
             <template slot-scope="{ row }">
              {{ row.sceneId | dictLabel(hmcGreetingSceneData) }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="累计触发次数" prop="triggerCount">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="发布状态">
            <template slot-scope="{ row }">
              <span :style="{color: row.publishStatus == 1 ? '#67C23A' : '#E6A23C'}">{{ row.publishStatus == 1 ? '已发布' : '待发布' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="最近更新时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="140" label="最近发布时间">
            <template slot-scope="{ row }">
              <div>{{ row.publishDate | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="100">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="modifyClick(row.id)">编辑</yl-button>
              <yl-button type="text" @click="publishClick(row)" v-if="row.publishStatus == 2">发布</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { pageList, publishGreetings } from '@/subject/admin/api/cmp_api/official_account'
import { hmcGreetingScene } from '@/subject/admin/busi/cmp/activity'
export default {
  name: 'WelcomeMessage',
  computed: {
    hmcGreetingSceneData() {
      return hmcGreetingScene()
    }
  },
  data() {
    return {
      query: {
        sceneId: '',
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
    // 获取数据列表
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await pageList(
        query.sceneId,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 编辑
    modifyClick(id, type) {
      this.$router.push({
        name: 'WelcomeMessageEstablish',
        params: {
          id: id
        }
      })
    },
    //点击发布
    publishClick(row) {
      this.$confirm(`确定发布 ${ row.sceneName }！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }) 
      .then( async() => {
        this.$common.showLoad();
        let data = await publishGreetings(row.id)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('发布成功！');
          this.getList()
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
        sceneId: '',
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
