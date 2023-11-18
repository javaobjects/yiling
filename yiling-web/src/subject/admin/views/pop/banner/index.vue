<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">标题</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入标题" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始月份" end-placeholder="结束月份"></el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">投放时间</div>
              <el-date-picker v-model="query.tTime" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始月份" end-placeholder="结束月份"></el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
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
        <yl-button type="primary" @click="addBanner">新增Banner</yl-button>
      </div>

      <div>
        <yl-table border :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <!-- 顶部 -->
                <div class="session font-size-base flex-between">
                  <div class="flex-row-left">
                    <span>
                      Banner状态：
                      <span v-show="row.bannerStatus == 1">未开始</span>
                      <span v-show="row.bannerStatus == 2">进行中</span>
                      <span v-show="row.bannerStatus == 3">已结束</span>
                    </span>
                    <span class="mar-l-16">
                      排序：
                      <span>{{ row.sort }}</span>
                    </span>
                  </div>
                  <div>
                    <span>创建时间：{{ row.createTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content flex-between">
                  <div class="content-left">
                    <div class="img-view">
                      <el-image fit="contain" class="img" :src="row.pic"></el-image>
                    </div>
                    <div>
                      <div class="title font-size-large font-title-color">{{ row.title }}</div>
                      <div class="font-size-base font-light-color"><span>开始时间：</span>{{ row.startTime | formatDate }}</div>
                      <div class="font-size-base font-light-color"><span>结束时间：</span>{{ row.endTime | formatDate }}</div>
                    </div>
                  </div>
                  <div class="content-right">
                    <div>
                      <yl-button type="text" @click="edit(row)">修改</yl-button>
                    </div>
                    <div>
                      <yl-button type="text" @click="editStatus(row)">{{ row.status == 1 ? '停用' : '启用' }}</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getBannerList, updateBannerStatus } from '@/subject/admin/api/pop'

export default {
  name: 'PopBanner',
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
        status: 0
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
      let data = await getBannerList(
        query.page,
        query.limit,
        query.status,
        query.name,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null,
        query.tTime && query.tTime.length > 0 ? query.tTime[0] : null,
        query.tTime && query.tTime.length > 1 ? query.tTime[1] : null
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
        status: 0
      }
    },
    addBanner() {
      this.$router.push({
        name: 'PopBannerEdit'
      });
    },
    edit(row) {
      // 跳转详情
      this.$router.push({
        name: 'PopBannerEdit',
        params: { id: row.id }
      });
    },
    // 启用、停用
    async editStatus(row) {
      let currentStatus = row.status
      // eslint-disable-next-line no-unused-vars
      let str = ''
      if (currentStatus == 1) {
        row.status = 2
        str = '停用成功'
      } else {
        row.status = 1
        str = '启用成功'
      }
      this.$common.showLoad();
      let data = await updateBannerStatus(row.id, row.status)
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success(str)
        this.getList()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>

