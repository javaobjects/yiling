<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">引用业务线</div>
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
              <div class="title">内容所属医生</div>
              <el-autocomplete
                v-model="query.doctorName"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入内容所属医生"
                @select="handleSelect">
              </el-autocomplete>
            </el-col>
            <el-col :span="6">
              <div class="title">创建时间</div>
              <el-date-picker
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-model="query.establishTime">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="未发布" :value="1"></el-option>
                <el-option label="已发布" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">创建来源</div>
              <el-select class="select-width" v-model="query.createSource" placeholder="请选择">
                <el-option
                  v-for="item in createSourceData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">创建者</div>
              <el-input v-model="query.createUserName" @keyup.enter.native="searchEnter" placeholder="请输入创建者姓名" />
            </el-col> 
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="establishClick">添加</yl-button>
          <yl-button type="primary" plain @click="draftsClick">草稿箱</yl-button>
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
          <el-table-column align="center" min-width="150" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="120" label="业务引用">
            <template slot-scope="{ row }">
              <div v-for="(item,index) in row.displayLines" :key="index">
                {{ item | dictLabel(businessData) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="总浏览量(pv)" prop="pageView"></el-table-column>
          <el-table-column align="center" min-width="110" label="内容所属医生" prop="docName"></el-table-column>
          <el-table-column align="center" min-width="80" label="文章状态">
            <template slot-scope="{ row }">
              <div :style="{color: row.status == 2 ? '#52C41A' : '#E62412'}">
                {{ row.status == 2 ? '已发布' : '未发布' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="创建者" prop="createUserName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="最新修改人" prop="updateUserName">
          </el-table-column>
          <el-table-column align="center" min-width="130" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="创建来源">
             <template slot-scope="{ row }">
              <div>{{ row.createSource | dictLabel(createSourceData) }}</div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="180">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" v-if="row.createSource == 1" @click="modifyClick(row)">编辑</yl-button>
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
import { displayLine } from '@/subject/admin/utils/busi'
import { queryContentPage, updateContent2, queryDoctorByNameAndHospital } from '@/subject/admin/api/content_api/article_video'
import { cmsContentCreateSource } from '@/subject/admin/busi/content/article_video'
export default {
  name: 'ArticleList',
  components: {},
  computed: {
    businessData() {
      return displayLine()
    },
    createSourceData() {
      return cmsContentCreateSource()
    }
  },
  data() {
    return {
      query: {
        title: '',
        lineId: '',
        establishTime: [],
        doctorName: '',
        docId: '',
        createUserName: '',
        status: '',
        createSource: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false
    }
  },
  activated() {
    //获取数据
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
    // 获取数据
    async getList() {
      this.loading = true;
      let query = this.query;
      if (query.createUserName != '' && query.createSource == '') {
        this.$message.warning('请选择创建来源')
      } else {
        let data = await queryContentPage(
          '1',
          query.page,
          query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
          query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
          query.lineId,
          query.limit,
          query.title,
          query.docId,
          query.createUserName,
          query.status,
          query.createSource
        )
        if (data) {
          this.dataList = data.records;
          this.query.total = data.total;
        }
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
        lineId: '',
        establishTime: [],
        doctorName: '',
        docId: '',
        createUserName: '',
        status: '',
        createSource: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 创建
    establishClick() {
      this.$router.push({
        name: 'AddEditArticle',
        params: {
          id: 0,
          type: 1,
          // 1 文章 2视频
          category: 1
        }
      });
    },
    // 编辑
    modifyClick(row) {
      this.$router.push({
        name: 'AddEditArticle',
        params: {
          id: row.id,
          type: 2,
           // 1 文章 2视频
          category: 1
        }
      });
    },
    // 草稿箱
    draftsClick() {
      this.$router.push({
        name: 'ArticleDrafts'
      });
    },
    // 取消发布 / 发布
    staticClick(row) {
      this.$confirm(`确认${row.status == 1 ? '发布' : '取消发布'} ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await updateContent2(
          row.id,
          row.isTop,
          row.status == 1 ? 2 : 1,
          '1',
          row.docId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
