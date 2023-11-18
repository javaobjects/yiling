<template>
 <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">接收人手机号</div>
              <el-input v-model.trim="query.mobile" placeholder="请输入终端名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">发送状态</div>
              <el-radio-group v-model="query.status">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">待发送</el-radio>
                <el-radio :label="2">发送成功</el-radio>
                <el-radio :label="3">发送失败</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="10">
              <div class="title">创建时间</div>
              <!-- <el-date-picker
                v-model="query.cjTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker> -->
              <!-- <el-date-picker
                v-model="query.cjTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker> -->
              <el-date-picker
                v-model="query.cjTime"
                type="datetimerange"
                format="yyyy/MM/dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
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
        <!-- <div class="btn">
          <ylButton type="primary">查询结果导出</ylButton>
          <ylButton plain>导入</ylButton>
        </div> -->
      </div>
      <!-- 下部 表格 -->
      <div class="search-bar">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <!-- <el-table-column align="center" label="#" type="index" width="60">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column> -->
          <el-table-column align="center" min-width="100" label="接收人手机号" prop="mobile">
          </el-table-column>
          <el-table-column align="left" min-width="200" label="短信内容" prop="content">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="短信类型" prop="type">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="发送状态">
            <template slot-scope="{ row }">
              <div>{{ row.status == 1 ? '待发送' : (row.status == 2 ? '发送成功' : ( row.status == 3 ? '发送失败' : '')) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="备注" prop="remark">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="发送通道标识" prop="channelCode">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建时间" prop="address">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="发送时间">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <!-- <el-table-column fixed="right" align="center" label="操作" min-width="140">
            <template slot-scope="{ row }">
              <div v-if="row.syncStatus == 3">
                <yl-button type="text" @click="matchingClick(row)">匹配</yl-button>
              </div>
              <div v-if="row.syncStatus == 3">
                <yl-button type="text" @click="maintainClick(row)">维护</yl-button>
              </div>
            </template>
          </el-table-column> -->
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { smsPageList } from '@/subject/admin/api/log.js'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'SmsLog',
  computed: {

  },
  data() {
    return {
      query: {
        mobile: '',
        status: 0,
        total: 0,
        page: 1,
        limit: 10,
        cjTime: []
      },
      dataList: [],
      loading: false

    }
  },
  activated() {
    this.getList();
  },
  watch: {

  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await smsPageList(
        query.page,
        query.mobile,
        query.limit,
        query.status == 0 ? null : query.status,
        query.cjTime && query.cjTime.length > 0 ? formatDate(query.cjTime[0], 'yyyy-MM-dd hh:mm:ss') : '',
        query.cjTime && query.cjTime.length > 0 ? formatDate(query.cjTime[1], 'yyyy-MM-dd hh:mm:ss') : ''
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 清空查询
    handleReset() {
      this.query = {
        mobile: '',
        status: 0,
        total: 0,
        page: 1,
        limit: 10,
        cjTime: []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>