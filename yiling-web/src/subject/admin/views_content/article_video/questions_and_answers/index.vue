<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">问答ID</div>
              <el-input v-model="query.id" @keyup.enter.native="searchEnter" @input="e => (query.id = checkInput(e))" placeholder="请输入问答ID"/>
            </el-col>
            <el-col :span="6">
              <div class="title">针对的内容</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入针对的内容"/>
            </el-col>
            <el-col :span="6">
              <div class="title">创建人类型</div>
              <el-select class="select-width" v-model="query.userType" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="患者" :value="1"></el-option>
                <el-option label="医生" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">上级问答ID</div>
              <el-input v-model="query.qaId" @keyup.enter.native="searchEnter" @input="e => (query.qaId = checkInput(e))" placeholder="请输入上级问答ID"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">来源产品端</div>
              <el-select class="select-width" v-model="query.lineId" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in businessData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">发表时间</div>
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
      <!-- 底部列表 -->
      <div class="mar-t-8 order-table-view">
        <yl-table
          border 
          show-header 
          :list="dataList"
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="90" label="问答ID" prop="id"></el-table-column>
          <el-table-column align="center" min-width="160" label="针对的内容" prop="contentTitle"></el-table-column>
          <el-table-column align="center" min-width="200" label="问答详情" prop="content"></el-table-column>
          <el-table-column align="center" min-width="200" label="创建人">
            <template slot-scope="{ row }">
              <div v-if="row.qaType == 1">
                <div class="table-p">
                  <p><span>注册用户ID：</span>{{ row.createUser }}</p>
                  <p><span>昵称：</span>{{ row.nickName }}</p>
                  <p><span>手机号：</span>{{ row.mobile }}</p>
                </div>
              </div>
              <div v-else>
                <div class="table-p" >
                  <p>{{ row.doctorInfoVo.doctorName }} ( {{ row.doctorInfoVo.hospitalName }} )</p>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="创建人类型">
            <template slot-scope="{ row }">
              {{ row.qaType == 1 ? '患者' : '医生' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="上级问答ID">
            <template slot-scope="{ row }">
              {{ row.qaId ? row.qaId : '' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="90" label="来源产品端">
            <template slot-scope="{ row }">
              {{ row.lineId | dictLabel(businessData) }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="是否显示">
            <template slot-scope="{ row }">
              <el-switch
                v-model="row.showStatus"
                active-color="#13ce66"
                inactive-color="#909399"
                :active-value="1"
                :inactive-value="2"
                active-text="是"
                inactive-text="否"
                @change="switchChange(row)">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="发表时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>

import { queryQaPage, switchShowStatus } from '@/subject/admin/api/content_api/article_video'
import { displayLine } from '@/subject/admin/utils/busi'
export default {
  name: 'QuestionsAndAnswers',
  components: {
  },
  computed: {
    businessData() {
      return displayLine()
    }
  },
  data() {
    return {
      query: {
        id: '',
        title: '',
        userType: '',
        qaId: '',
        lineId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      // 表格loading
      loading: false,
      dataList: []
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
      let query = this.query;
      this.loading = true;
      let data = await queryQaPage(
        query.id,
        query.title,
        query.userType,
        query.qaId,
        query.lineId,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total
      }
      this.loading = false;
    },
    //点击启用禁用
    async switchChange(row) {
      this.$common.showLoad();
      let data = await switchShowStatus(
        row.id,
        row.showStatus
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('操作成功!');
      }
      this.getList()
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        id: '',
        title: '',
        userType: '',
        qaId: '',
        lineId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>