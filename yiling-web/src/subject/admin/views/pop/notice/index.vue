<template>
  <div class="app-container">
    <div class="app-container-content">
       <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">公告标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入公告标题" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
               <el-radio-group v-model="query.status">
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
        <yl-button type="primary" @click="addNew">新增公告</yl-button>
      </div>
      <div>
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column align="center" min-width="120" label="标题" prop="title">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="开始时间">
            <template slot-scope="{ row }">
              <span>{{ row.startTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="结束时间">
            <template slot-scope="{ row }">
              <span>{{ row.endTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="创建时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width="120" label="状态">
            <template slot-scope="{ row }">
              <el-tag :type="row.state === 1 ? 'success' : 'danger'">{{ row.state | enable }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="edit(row)">查看</yl-button>
              </div>
              <!--            <div>-->
              <!--              <yl-status-->
              <!--                url="/recommend/update/status"-->
              <!--                :status="row.status"-->
              <!--                status-key="status"-->
              <!--                :data="{id: row.id}"-->
              <!--                @change="getList">-->
              <!--              </yl-status>-->
              <!--            </div>-->
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getNoticeList } from '@/subject/admin/api/pop'
// import { ylStatus } from '@/subject/admin/components'

export default {
  name: 'PopNotice',
  components: {
    // ylStatus
  },
  computed: {
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        status: 0,
        time: []
      },
      // 推荐位列表
      dataList: [],
      // 列表加载
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
      let data = await getNoticeList(
        query.page,
        query.limit,
        query.title,
        query.status,
        query.time && query.time.length ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : ''
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 首页搜索
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        status: 0,
        time: []
      }
    },
    // 新增
    addNew() {
      this.$router.push('/pop/pop_notice_edit/none')
    },
    // 查看
    edit(row) {
      this.$router.push(`/pop/pop_notice_edit/${row.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

