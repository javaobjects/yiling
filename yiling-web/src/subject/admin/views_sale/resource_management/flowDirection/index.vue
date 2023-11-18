<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="query.recvEname" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">单据编号</div>·
              <el-input v-model="query.docCode" @keyup.enter.native="searchEnter" placeholder="请输入单据编号" />
            </el-col>
            <el-col :span="8">
              <div class="title">ERP流向匹配时间</div>
              <el-date-picker
                v-model="query.erpMatchTime"
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
            <el-col :span="8">
              <div class="title">CRM流向匹配时间</div>
              <el-date-picker
                v-model="query.crmMatchTime"
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
            <el-col :span="24">
              <div class="title">流向核对结果</div>
              <el-radio-group v-model="query.result">
                <el-radio label="">全部</el-radio>
                <el-radio :label="item.value" v-for="(item, index) in accompanyStatusList" :key="index">{{ item.label }}</el-radio>
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
      <div class="mar-t-10">
        <yl-table 
          :list="dataList" 
          :cell-class-name="() => 'border-1px-b'" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="info-box">
                  <span class="font-weight"><span class="flow-name" v-if="row.recvEname">{{ row.recvEname }}</span><span class="flow-result">流向核对结果：<span>{{ row.result | dictLabel(accompanyStatusList) }}</span></span></span>
                  <span class="flow-result">单据编号：<span>{{ row.docCode || '- -' }}</span></span>
                </div>
                <div class="content">
                  <div class="info">
                    <div class="table-item">
                      <div class="item font-title-color"><span class="font-light-color">ERP流向匹配取时间：<span class="result">{{ row.erpMatchTime | formatDate }}</span></span></div>
                      <div class="item font-title-color"><span class="font-light-color">CRM流向匹配时间：<span class="result">{{ row.crmMatchTime | formatDate }}</span></span></div>
                    </div>
                    <div class="product-num">
                      <div class="font-light-color">ERP流向：<span class="result">{{ row.erpResult === 1 ? '有' : '无' }}</span></div>
                      <div class="font-light-color">CRM流向：<span class="result">{{ row.crmResult === 1 ? '有' : '无' }}</span></div>
                    </div>
                  </div>
                  <div class="flex-column-center edit-btn">
                    <yl-button type="text" @click="showDetail(row)">查看详情</yl-button>
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
import { queryMatchBillPage } from '@/subject/admin/api/views_sale/resource_management'
import { accompanyBillMatchStatus } from '@/subject/admin/busi/sale/resource'

export default {
  name: 'FlowDirection',
  computed: {
    accompanyStatusList(){
      return accompanyBillMatchStatus()
    }
  },
  data() {
    return {
      query: {
        page: 1,
        limit: 10,
        total: 0,
        docCode: '',
        recvEname: '',
        erpMatchTime: [],
        crmMatchTime: [],
        result: ''
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
      let data = await queryMatchBillPage(
        query.page,
        query.limit,
        query.recvEname,
        query.docCode,
        query.erpMatchTime && query.erpMatchTime.length ? query.erpMatchTime[0] : undefined,
        query.erpMatchTime && query.erpMatchTime.length > 1 ? query.erpMatchTime[1] : undefined,
        query.crmMatchTime && query.crmMatchTime.length ? query.crmMatchTime[0] : undefined,
        query.crmMatchTime && query.crmMatchTime.length > 1 ? query.crmMatchTime[1] : undefined,
        query.result
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
        total: 0,
        docCode: '',
        recvEname: '',
        erpMatchTime: [],
        crmMatchTime: [],
        result: ''
      }
    },
    // 查看详情
    showDetail(row) {
      this.$router.push({
        path: `/resource_management/flow_direction_detail/${row.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
