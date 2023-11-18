<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">标题</div>
              <el-input v-model="query.title" @keyup.enter.native="searchEnter" placeholder="请输入标题名称"/>
            </el-col>
            <el-col :span="6">
              <div class="title">所属板块</div>
              <div>
                <el-select class="select-width" v-model="query.moduleId" placeholder="请选择" @change="plateChange">
                  <el-option label="全部" :value="''"></el-option>
                  <el-option
                    v-for="item in modulesByLineData"
                    :key="item.moduleId"
                    :label="item.moduleId | dictLabel(hmcModuleData)"
                    :value="item.moduleId">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">所属栏目</div>
              <el-select class="select-width" v-model="query.categoryId" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in columnData"
                  :key="item.categoryId"
                  :label="item.categoryName"
                  :value="item.categoryId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">内容类别</div>
              <el-select class="select-width" v-model="query.contentType" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="文章" :value="1"></el-option>
                <el-option label="视频" :value="2"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">栏目列表是否手动排序</div>
              <el-select class="select-width" v-model="query.handRankFlag" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="否" :value="0"></el-option>
                <el-option label="是" :value="1"></el-option>
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
              <div class="title">状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="未发布" :value="1"></el-option>
                <el-option label="已发布" :value="2"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
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
            <el-col :span="6">
              <div class="title">创建来源</div>
              <el-select class="select-width" v-model="query.createSource" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in createSourceData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">是否置顶</div>
              <el-select class="select-width" v-model="query.topFlag" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="是" :value="1"></el-option>
                <el-option label="否" :value="0"></el-option>
              </el-select>
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
      <!-- 添加引用文章 -->
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" @click="addArticleClick">添加引用</yl-button>
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
          <el-table-column align="center" min-width="150" label="标题" prop="title"></el-table-column>
          <el-table-column align="center" min-width="150" label="所属板块">
            <template slot-scope="{ row }">
              {{ row.moduleId | dictLabel(hmcModuleData) }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="所属栏目" prop="categoryName"></el-table-column>
          <el-table-column align="center" min-width="150" label="内容类别">
            <template slot-scope="{ row }">
              {{ row.contentType == 1 ? '文章' : '视频' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="浏览量(pv)" prop="view"></el-table-column>
          <el-table-column align="center" min-width="150" label="栏目列表排序" prop="categoryRank"></el-table-column>
          <el-table-column align="center" min-width="150" label="内容所属医生" prop="docName"></el-table-column>
          <el-table-column align="center" min-width="150" label="状态">
            <template slot-scope="{ row }">
              {{ row.status == 1 ? '未发布' : '已发布' }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="创建来源">
            <template slot-scope="{ row }">
              {{ row.createSource | dictLabel(createSourceData) }}
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="发布人" prop="createUserName"></el-table-column>
          <el-table-column align="center" min-width="150" label="创建时间">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="180" label="操作">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
                <yl-button type="text" @click="isTopClick(row)">{{ row.topFlag == 1 ? '取消置顶' : '置顶' }}</yl-button>
                <yl-button type="text" @click="referStatusClick(row)">{{ row.referStatus == 1 ? '取消引用' : '引用' }}</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="排序" @confirm="sortConfirm" width="570px" :visible.sync="sortDialog">
      <div class="dialogTc">
        <el-form 
          :model="sortForm" 
          :rules="sortRules" 
          ref="dataForm1" 
          label-width="80px">
          <el-form-item label="排序" prop="categoryRank">
            <el-input v-model="sortForm.categoryRank" style="width: 200px;"
              @input="e => (sortForm.categoryRank = checkInput(e))" placeholder="排序"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { queryDoctorByNameAndHospital, hmcUpdateReferStatus } from '@/subject/admin/api/content_api/article_video'
import { displayLine } from '@/subject/admin/utils/busi'
import { queryContentPage, contentRank, getModulesByLineId } from '@/subject/admin/api/content_api/multiterminal'
import { hmcModule, cmsContentCreateSource } from '@/subject/admin/busi/content/article_video'
export default {
  name: 'HealthManagement',
  components: {
  },
  computed: {
    businessData() {
      return displayLine()
    },
    // 健康管理中心 板块
    hmcModuleData() {
      return hmcModule()
    },
    createSourceData() {
      return cmsContentCreateSource()
    }
  },
  data() {
    return {
      query: {
        title: '',
        //所属板块
        moduleId: '',
        //所属栏目
        categoryId: '',
        //内容类别
        contentType: '',
        //是否手动排序 0-否，1-是	
        handRankFlag: '',
        //医生姓名
        doctorName: '',
        //内容所属医生ID
        docId: '',
        //状态
        status: '',
        //创建时间
        establishTime: [],
        //创建来源
        createSource: '',
        //是否置顶
        topFlag: '',
        total: 0,
        page: 1,
        limit: 10
      },
      // 表格loading
      loading: false,
      dataList: [],
      // 排序弹窗
      sortDialog: false,
      sortForm: {
        categoryRank: '',
        id: '',
        topFlag: ''
      },
      sortRules: {
        categoryRank: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      },
      tableType: 1,
      // 所属栏目
      columnData: [],
      state: '',
      // 获取到的医生数据
      restaurants: [],
      //获取到的 板块和 栏目信息
      modulesByLineData: []
    }
  },
  activated() {
    // 获取板块以及栏目
    this.getColumn();
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
    //点击板块
    plateChange(e) {
      this.query.categoryId = ''
      if (e == '') {
        this.columnList()
      } else {
        this.columnData = [];
        let data = this.modulesByLineData;
        for (let i = 0; i < data.length; i ++ ) {
          if (data[i].moduleId == e) {
            for ( let y = 0; y < data[i].moduleCategoryList.length; y ++) {
              this.columnData.push(data[i].moduleCategoryList[y])
            }
          }
        }
      }
    },
    // 获取所属板块以及栏目
    async getColumn() {
      let data = await getModulesByLineId(1)
      if (data) {
        this.modulesByLineData = data;
        this.columnList()
      }
    },
    //过滤出 栏目信息
    columnList() {
      this.columnData = [];
      let data = this.modulesByLineData;
      for (let i = 0; i < data.length; i ++ ) {
        for ( let y = 0; y < data[i].moduleCategoryList.length; y ++) {
          this.columnData.push(data[i].moduleCategoryList[y])
        }
      }
    },
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryContentPage(
        query.title,
        query.moduleId,
        query.categoryId,
        query.contentType,
        query.handRankFlag,
        query.docId,
        query.status,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.createSource,
        query.topFlag,
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total
      }
      this.loading = false
    },
    // 排序
    sortClick(row) {
      this.sortDialog = true;
      this.sortForm = {
        categoryRank: row.categoryRank ? row.categoryRank : '',
        id: row.id,
        topFlag: row.topFlag
      }
    },
    // 是否置顶
    isTopClick(row) {
      this.$confirm(`确认${row.topFlag == 1 ? '取消置顶' : '置顶'} ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await contentRank(
          row.categoryRank,
          row.id,
          row.topFlag == 1 ? 0 : 1
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
        moduleId: '',
        categoryId: '',
        contentType: '',
        handRankFlag: '',
        doctorName: '',
        docId: '',
        status: '',
        establishTime: [],
        createSource: '',
        topFlag: '',
        total: 0,
        page: 1,
        limit: 10
      }
      this.getColumn();
    },
    // 排序点击确定
    sortConfirm() {
      this.$refs['dataForm1'].validate(async (valid) => {
        if (valid) {
          let sortForm = this.sortForm;
          this.$common.showLoad();
          let data = await contentRank(
            sortForm.categoryRank,
            sortForm.id,
            sortForm.topFlag
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('修改成功!');
            this.sortDialog = false;
            this.getList();
          }
        }
      })
    },
    // 添加文章引用
    addArticleClick() {
      this.$router.push({
        name: 'AddArticle'
      });
    },
    //点击引用/取消引用
    referStatusClick(row) {
      this.$confirm(`确认${row.referStatus == 1 ? '取消引用' : '引用'} ${row.title} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await hmcUpdateReferStatus(
          row.id,
          row.referStatus == 1 ? 2 : 1
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