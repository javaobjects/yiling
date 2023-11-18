<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">标题</div>
              <el-input v-model="query.title" placeholder="请输入标题" @keyup.enter.native="searchEnter" />
            </el-col>
            <el-col :span="6">
              <div class="title">状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in statusArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
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
              <div class="title">投放开始时间</div>
              <el-date-picker
                v-model="query.launchTime"
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
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <ylButton type="primary" @click="addClick(0)">新建</ylButton>
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
          <el-table-column label="序号" min-width="60" align="center">
            <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="兑换消息名称" min-width="180" align="center" prop="title">
          </el-table-column>
          <el-table-column label="图标" min-width="120" align="center">
            <template slot-scope="{ row }">
              <el-image class="img" :src="row.iconUrl">
              </el-image>
            </template>
          </el-table-column>
          <el-table-column label="页面配置" min-width="120" align="center">
            <template slot-scope="{ row }">
              {{ row.pageConfig == 1 ? '活动链接' : '' }}
            </template>
          </el-table-column>
          <el-table-column label="排序" min-width="60" align="center" prop="sort">
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center" prop="createUserName">
            <template slot-scope="{ row }">
              <div>
                {{ row.status | dictLabel(statusArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="最后维护信息" min-width="120" align="center">
             <template slot-scope="{ row }">
              <div>
                <p>{{ row.updateUserName }}</p>
                {{ row.updateTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <yl-button type="text" @click="addClick(row.id)">编辑</yl-button>
                <yl-button type="text" @click="stopClick(row)">{{ row.status == 1 ? '停用' : '启用' }}</yl-button>
                <yl-button type="text" @click="sortClick(row)">排序</yl-button>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="排序" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="80px" class="demo-ruleForm">
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
import { 
  queryMessageListPage, 
  messageUpdateStatus, 
  messageDeleteConfig, 
  messageUpdateOrder 
  } from '@/subject/admin/api/b2b_api/integral_record'
export default {
  name: 'IntegralNews',
  components: {
  },
  computed: {
   
  },
  data() {
    return {
      // 活动状态
      statusArray: [
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 2
        }
      ],
      loading: false,
      query: {
        title: '',
        status: '',
        time: [],
        launchTime: [],
        page: 1,
        limit: 10,
        total: 0
      },
      dataList: [],
      showDialog: false,
      form: {
        sort: '',
        id: ''
      },
      rules: {
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
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
      let data = await queryMessageListPage(
        query.title,
        query.status,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.launchTime && query.launchTime.length > 0 ? query.launchTime[0] : '',
        query.launchTime && query.launchTime.length > 1 ? query.launchTime[1] : '',
        query.page,
        query.limit
      );
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
      this.loading = false
    },
    //点击新增/编辑
    addClick(id) {
      this.$router.push({
        name: 'IntegralNewsEstablish',
        params: {
          id: id
        }
      });
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        title: '',
        status: '',
        time: [],
        launchTime: [],
        page: 1,
        limit: 10,
        total: 0
      }
    },
    //启用/停用
    stopClick(row) {
      this.$confirm(`确认 ${ row.status == 1 ? '停用' : '启用' } ${ row.title }！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await messageUpdateStatus(
          row.id,
          row.status == 1 ? 2 : 1
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
    //删除
    deleteClick(row) {
      this.$confirm(`确认删除 ${ row.title }！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await messageDeleteConfig(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    //点击排序
    sortClick(row) {
      this.form ={
        sort: row.sort,
        id: row.id
      }
      this.showDialog = true;
    },
    //排序保存
    confirm() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await messageUpdateOrder(
            this.form.id,
            this.form.sort
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.showDialog = false;
            this.$common.n_success('修改成功!');
            this.getList();
          }
        }
      })
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
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
