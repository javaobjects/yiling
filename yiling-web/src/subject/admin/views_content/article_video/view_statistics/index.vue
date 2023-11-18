<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">文章标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题名称"/>
            </el-col>
            <el-col :span="8">
              <div class="title">内容所属医生</div>
              <el-autocomplete 
                v-model="query.doctorName" 
                :fetch-suggestions="querySearchAsync" 
                placeholder="请输入内容所属医生" 
                @select="handleSelect">
              </el-autocomplete>
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
          <el-table-column align="center" min-width="200" label="文章名称" prop="title"></el-table-column>
          <el-table-column align="center" min-width="120" label="内容所属医生" prop="docName"></el-table-column>
          <el-table-column align="center" min-width="90" label="健康管理中心浏览量" prop="hmcView"></el-table-column>
          <el-table-column align="center" min-width="100" label="以岭医院用户侧浏览量" prop="ihPatientView"></el-table-column>
          <el-table-column align="center" min-width="90" label="医生侧浏览量" prop="ihDocView"></el-table-column>
          <el-table-column align="center" min-width="90" label="大运河侧浏览量" prop="b2bView"></el-table-column>
          <el-table-column align="center" min-width="90" label="销售助手侧浏览量" prop="saView"></el-table-column>
          <el-table-column align="center" min-width="90" label="合计浏览量" prop="pageView"></el-table-column>
          <el-table-column align="center" min-width="130" label="创建时间">
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
import { queryContentPage, queryDoctorByNameAndHospital } from '@/subject/admin/api/content_api/article_video'
import { displayLine } from '@/subject/admin/utils/busi'
export default {
  name: 'ViewStatistics',
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
        title: '',
        docId: '',
        doctorName: '',
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
    async querySearchAsync(queryString, cb) {
      var results = [];
      if (queryString == '') {
        cb(results)
      } else {
        this.$common.showLoad();
        let data = await queryDoctorByNameAndHospital(
          queryString,
          '',
          1,
          50
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          if (data.records && data.records.length > 0) {
            for (let i = 0; i < data.records.length; i ++) {
              results.push({
                value: data.records[i].doctorName + '-' + data.records[i].hospitalName,
                name: data.records[i].doctorName,
                id: data.records[i].id
              })
            }
            cb(results)
          } else {
            results.push({
              value: '暂未搜索到数据',
              name: '',
              id: ''
            })
            cb(results)
          }
        } else {
          results = [];
          cb(results)
        }
      }
    },
    handleSelect(item) {
      if (item.id == '') {
        this.query.docId = '';
        this.query.doctorName = '';
      } else {
        this.query.docId = item.id;
        this.query.doctorName = item.name
      }
    },
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryContentPage(
        1,
        query.page,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        '',
        query.limit,
        query.title,
        query.docId,
        '',
        '',
        ''
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total
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
        docId: '',
        doctorName: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 200) {
        val = 200
      }
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