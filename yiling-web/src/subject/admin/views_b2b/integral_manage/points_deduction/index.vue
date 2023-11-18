<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">规则名称</div>
              <el-input v-model="query.ruleName	" @keyup.enter.native="searchEnter" placeholder="请输入规则名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">行为名称</div>
              <el-input v-model="query.behaviorName	" @keyup.enter.native="searchEnter" placeholder="请输入行为名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">提交人手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入提交人手机号" />
            </el-col>
            <el-col :span="6">
              <div class="title">扣减时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">用户ID</div>
              <el-input v-model="query.uid" @keyup.enter.native="searchEnter" placeholder="请输入用户ID"/>
            </el-col>
            <el-col :span="6">
              <div class="title">用户名称</div>
              <el-input v-model="query.uname" @keyup.enter.native="searchEnter" placeholder="请输入用户名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">备注</div>
              <el-input v-model="query.opRemark" @keyup.enter.native="searchEnter" placeholder="请输入备注" />
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="downLoadTemp">导出扣减记录</yl-button>
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
          <el-table-column label="序号" min-width="60" align="center">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="扣减时间" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.operTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="积分值" min-width="70" align="center" prop="integralValue"></el-table-column>
          <el-table-column label="备注" min-width="110" align="center" prop="opRemark"></el-table-column>
          <el-table-column label="规则ID" min-width="70" align="center">
             <template slot-scope="{ row }">
              {{ row.ruleId ? row.ruleId : '' }}
            </template>
          </el-table-column>
          <el-table-column label="规则名称" min-width="110" align="center" prop="ruleName"></el-table-column>
          <el-table-column label="行为ID" min-width="70" align="center" prop="behaviorId"></el-table-column>
          <el-table-column label="行为名称" min-width="110" align="center" prop="behaviorName"></el-table-column>
          <el-table-column label="用户ID" min-width="70" align="center" prop="uid"></el-table-column>
          <el-table-column label="用户名称" min-width="100" align="center" prop="uname"></el-table-column>
          <el-table-column label="提交人手机号" min-width="100" align="center" prop="mobile"></el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { integralRecordListPage } from '@/subject/admin/api/b2b_api/integral_record'
import { createDownLoad } from '@/subject/admin/api/common'
export default {
  name: 'PointsDeduction',
  computed: {},
  data() {
    return {
      query: {
        type: 2,
        ruleName: '',
        behaviorName: '',
        mobile: '',
        time: [],
        uid: '',
        uname: '',
        opRemark: '',
        total: 0,
        page: 1,
        limit: 10
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
      let query = this.query;
      this.loading = true;
      let data = await integralRecordListPage(
        query.type,
        query.ruleName,
        query.behaviorName,
        query.mobile,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.uid,
        query.uname,
        query.opRemark,
        query.page,
        query.limit
      )
      if (data) {
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
        type: 2,
        ruleName: '',
        behaviorName: '',
        mobile: '',
        time: [],
        uid: '',
        uname: '',
        opRemark: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    //导出发放记录
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'integralRecordExportService',
        fileName: '导出扣减记录',
        groupName: '导出扣减记录',
        menuName: '积分扣减记录列表',
        searchConditionList: [
          {
            desc: '发放类型',
            name: 'type',
            value: query.type || ''
          },
          {
            desc: '规则名称',
            name: 'ruleName',
            value: query.ruleName || ''
          },
          {
            desc: '行为名称',
            name: 'behaviorName',
            value: query.behaviorName || ''
          },
          {
            desc: '提交人手机号',
            name: 'mobile',
            value: query.mobile || ''
          },
          {
            desc: '扣减开始时间',
            name: 'startOperTime',
            value: query.time && query.time.length > 0 ? query.time[0] : ''
          },
          {
            desc: '扣减结束时间',
            name: 'endOperTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          },
          {
            desc: '用户ID',
            name: 'uid',
            value: query.uid || ''
          },
          {
            desc: '用户名称',
            name: 'uname',
            value: query.uname || ''
          },
          {
            desc: '备注',
            name: 'opRemark',
            value: query.opRemark || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>