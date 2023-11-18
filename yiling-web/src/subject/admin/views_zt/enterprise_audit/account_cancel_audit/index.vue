<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">姓名</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入姓名" />
            </el-col>
            <el-col :span="6">
              <div class="title">手机号</div>
              <el-input v-model="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入手机号" />
            </el-col>
            <el-col :span="6">
              <div class="title">账号ID</div>
              <el-input v-model="query.id" @keyup.enter.native="searchEnter" placeholder="请输入账号ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">申请时间</div>
              <el-date-picker
                class="send-time"
                v-model="query.sendTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
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
      <!-- 下部表格 -->
      <el-tabs class="tabs-bar" v-model="query.activeTab" @tab-click="handleTabClick" >
        <el-tab-pane label="待审核" name="1"></el-tab-pane>
        <el-tab-pane label="审核通过" name="2"></el-tab-pane>
        <el-tab-pane label="审核撤销" name="3"></el-tab-pane>
      </el-tabs>
      <!-- 底部列表 -->
      <div class="table-box mar-t-8">
        <yl-table
          :show-header="true"
          border
          stripe
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :horizontal-border="false"
          :cell-no-pad="true"
          @getList="getList"
        >
          <el-table-column label="账号信息" min-width="92%" align="left">
            <template slot-scope="{ row }">
              <div class="account-info-wrap">
                <div class="list-col">
                  <div class="list-item">
                    账号ID: <span class="list-content">{{ row.userId }}</span>
                  </div>
                  <div class="list-item">
                    <span class="list-content">{{ row.name }}</span>
                  </div>
                  <div class="list-item">
                    <span class="list-content">{{ row.mobile }}</span>
                  </div>
                </div>
                <div class="list-col">
                  <div class="list-item">
                    <div class="txt">
                      <span>所属企业: </span>
                      <span>{{ row.enterpriseNum || 0 }}家</span>
                    </div>
                  </div>
                </div>
                <div class="list-col">
                  <div class="list-item">
                    申请时间: <span class="list-content">{{ row.applyTime | formatDate }}</span>
                  </div>
                  <div class="list-item">
                    数据来源: <span class="list-content">{{ row.source === 1 ? '销售助手APP' : '大运河APP' }}</span>
                  </div>
                </div>
                <div class="list-col" v-if="query.activeTab != 1">
                  <div class="list-item">
                    审核人: <span class="list-content">{{ row.authUserName || '- -' }}</span>
                  </div>
                  <div class="list-item">
                    审核时间: <span class="list-content">{{ row.authTime | formatDate }}</span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" v-if="query.activeTab == 1" min-width="8%" align="center" >
            <template slot-scope="{ row }">
              <yl-button @click="toExamineClick(row)" type="text">审核</yl-button >
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { cancellationPageList } from '@/subject/admin/api/zt_api/enterprise_audit';
export default {
  name: 'AccountCancelAudit',
  components: {},
  data() {
    return {
      query: {
        name: '',
        licenseNumber: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        sendTime: [],
        id: null,
        type: 0,
        authType: 0,
        source: 0,
        page: 1,
        limit: 10,
        total: 0,
        activeTab: '1'
      },
      loading: false,
      dataList: []
    };
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
      const query = this.query;
      let data = await cancellationPageList(
        query.page,
        query.sendTime && query.sendTime.length ? query.sendTime[1] : undefined,
        query.licenseNumber,
        query.name,
        query.limit,
        query.sendTime && query.sendTime.length ? query.sendTime[0] : undefined,
        Number(query.activeTab),
        query.id
      );
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 清空查询
    handleReset() {
      this.query = {
        name: '',
        licenseNumber: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        sendTime: [],
        id: null,
        type: 0,
        authType: 0,
        source: 0,
        page: 1,
        limit: 10,
        total: 0,
        activeTab: '1'
      };
    },
    // 点击切换
    handleTabClick(tab) {
      this.query.activeTab = tab.name;
      this.query.page = 1;
      this.getList();
    },
    //审核详情
    toExamineClick(row) {
      this.$router.push(`/enterprise_audit/cancel_audit_detail/${row.id}`);
    }
  }
};
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
