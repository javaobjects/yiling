<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">热词名称</div>
              <el-input v-model="query.content" @keyup.enter.native="searchEnter" placeholder="请输入热词名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.cjTime"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">投放时间</div>
               <el-date-picker
                v-model="query.tfTime"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
          </el-row>  
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <div class="title">状态</div>
            <el-radio-group v-model="query.useStatus">
              <el-radio :label="0">全部</el-radio>
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <yl-search-btn
            :total="query.total"
            @search="handleSearch"
            @reset="handleReset"/>
        </div>
      </div>
    <!-- <div class="search-bar">
      <el-form class="pad-t-8" label-width="72px" label-position="top">
        <el-row>
          <el-col :span="8">
            <el-form-item label="热词名称">
              <el-input v-model="query.content" placeholder="请输入热词名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间">
              <el-date-picker
                v-model="query.cjTime"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="投放时间">
              <el-date-picker
                v-model="query.tfTime"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-radio-group v-model="query.useStatus">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="mar-tb-10 pad-tb-10">
          <yl-search-btn
            :total="query.total"
            @search="handleSearch"
            @reset="handleReset"
          />
        </div>
      </el-form>
    </div> -->
    <div class="down-box clearfix">
      <div class="btn">
        <ylButton type="primary" @click="addBanner">新增热词</ylButton>
      </div>
    </div>
    <div>
      <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
        <el-table-column align="center" min-width="150" label="热词名称" prop="content"></el-table-column>
        <el-table-column align="center" min-width="80" label="排序" prop="sort"></el-table-column>
        <el-table-column align="center" min-width="200" label="开始时间-结束时间">
          <template slot-scope="{ row }">
            <div>{{ row.startTime | formatDate }} - {{ row.stopTime | formatDate }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="80" label="活动状态">
          <template slot-scope="{ row }">
            <div>{{ row.useStatus == '1' ? '启用' : '停用' }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="创建信息">
          <template slot-scope="{ row }">
            {{ row.createUserName }}
            <div>{{ row.createTime | formatDate }}</div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" align="center" label="操作" min-width="140">
          <template slot-scope="{ row }">
            <div>
              <yl-button type="text" @click="modifyClick(row)">编辑</yl-button>
            </div>
            <div>
              <yl-button type="text" @click="deactivateClick(row)">{{ row.useStatus == 1 ? '停用' : '启用' }}</yl-button>
            </div>
            <div>
              <yl-button type="text" @click="sortClick(row)">排序</yl-button>
            </div>
          </template>
        </el-table-column>
      </yl-table>
    </div>
    </div>
    <yl-dialog title="排序" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form :model="form" :rules="rules" ref="dataForm1" label-width="80px" class="demo-ruleForm">
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 200px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>

  </div>
</template>

<script>
import { hotWordsPageList, hotWordsEditStatus, hotWordsEditWeight } from '@/subject/admin/api/b2b_api/b2bAdministration.js'
  export default {
    name: 'B2bHotWord',
    components: {
    },
    computed: {
    },
    data() {
      return {
        query: {
          content: '',
          page: 1,
          limit: 10,
          total: 0,
          cjTime: [],
          tfTime: [],
          useStatus: 0
        },
        dataList: [],
        loading: false,
        showDialog: false, //排序弹窗
        form: {
          sort: '',
          id: ''
        },
        rules: {
          sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
        },
        clearable: false
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
        this.loading = true;
        let query = this.query;
        let data = await hotWordsPageList(
          query.content,
          query.cjTime && query.cjTime.length > 0 ? query.cjTime[1] : '',
          query.cjTime && query.cjTime.length > 0 ? query.cjTime[0] : '',
          query.page,
          query.limit,
          query.tfTime && query.tfTime.length > 0 ? query.tfTime[1] : '',
          query.tfTime && query.tfTime.length > 0 ? query.tfTime[0] : '',
          query.useStatus
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      handleSearch() {
        this.query.page = 1;
        this.getList()
      },
      handleReset() {
        this.query = {
          content: '',
          page: 1,
          limit: 10,
          total: 0,
          cjTime: [],
          tfTime: [],
          useStatus: 0
        }
      },
      addBanner() {
        this.$router.push({
          name: 'B2bHotWordEdit',
          params: {}
        });
      },
      
      // 启用、停用
      deactivateClick(row) {
        this.$confirm(`确认${row.useStatus == 1 ? '停用' : '启用'} ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          //确定
          this.$common.showLoad();
          let data = await hotWordsEditStatus(
            row.id,
            row.useStatus == 1 ? 2 : 1
          )
          this.$common.hideLoad();
          if (data!==undefined) {
            this.$common.n_success(`${row.useStatus == 1 ? '停用' : '启用'} 成功!`);
            this.getList();
          }
        })
        .catch(() => {
          //取消
        });
      },
      sortClick(row) {
        this.form ={
          sort: row.sort,
          id: row.id
        }
        this.showDialog = true;
      },
      confirm() {
        this.$refs['dataForm1'].validate(async (valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await hotWordsEditWeight(
              this.form.id,
              this.form.sort
            )
            this.$common.hideLoad();
            if (data!=undefined) {
              this.showDialog = false;
              this.$common.n_success('修改成功!');
              this.getList();
            }
          }
        })
      },
      // 编辑
      modifyClick(row) {
        this.$router.push('/b2bAdministration/b2b_hot_word_edit/' + row.id);
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
  @import "./index.scss";
</style>

