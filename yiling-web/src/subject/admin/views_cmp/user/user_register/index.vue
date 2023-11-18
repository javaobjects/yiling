<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">活动名称</div>
              <el-input v-model="query.activityName" placeholder="请输入活动名称" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="8">
              <div class="title">活动ID</div>
              <el-input v-model="query.activityId" placeholder="请输入活动ID" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="8">
              <div class="title">注册时段</div>
              <el-date-picker
                v-model="query.registerTime"
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
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">推荐来源</div>
              <el-select class="select-width" v-model="query.registerSource" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in registerSource"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">医生ID</div>
              <el-input v-model="query.doctorId" placeholder="请输入医生ID" @keyup.enter.native="searchEnter"/>
            </el-col>
            <el-col :span="8">
              <div class="title">医生姓名</div>
              <el-input v-model="query.doctorName" placeholder="请输入医生姓名" @keyup.enter.native="searchEnter"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
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
          <el-table-column align="center" min-width="80" label="用户ID" prop="id">
          </el-table-column>
          <el-table-column align="center" min-width="260" label="用户信息">
            <template slot-scope="{ row }">
              <el-image class="img" :src="row.avatarUrl">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <div class="user-right">
                <p><span>昵称：</span>{{ row.name }}</p>
                <p><span>性别：</span>{{ row.gender == 1 ? '男' : (row.gender == 2 ? '女' : '未知') }}</p>
                <p><span>联系方式：</span>{{ row.mobile }}</p>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="推荐人">
            <template slot-scope="{ row }">
              <div>{{ row.inviteUserName == '-' ? '无' : row.inviteUserName }}</div>
              <div class="user-tjr" v-if="row.inviteUserId !== ''">
                <span v-if="row.inviteUserId != 0">[ ID:{{ row.inviteUserId }} ]</span>
              </div>
              <div>{{ row.inviteEname }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推荐来源">
            <template slot-scope="{ row }">
              <div>{{ row.registerSource | dictLabel(registerSource) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="注册渠道标识">
            <template slot-scope="{ row }">
              <div>
                <div>
                  <span v-if="row.activityId != 0 && row.activityId != ''">
                    活动 [ {{ row.activityId }} : {{ row.activityName }} ]
                  </span>
                  <span v-else>
                    - -
                  </span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="登录次数" prop="loginCount">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="关联就诊人数" prop="patientCount">
          </el-table-column>
          <el-table-column align="center" min-width="130" label="UNIONID" prop="unionId">
          </el-table-column>
          <el-table-column align="center" min-width="140" label="注册日期">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryUserPage } from '@/subject/admin/api/cmp_api/user'
import { hmcRegisterSource } from '@/subject/admin/utils/busi'
export default {
  name: 'UserRegister',
  computed: {
    registerSource() {
      return hmcRegisterSource()
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        activityName: '',
        activityId: '',
        registerSource: '',
        registerTime: [],
        doctorId: '',
        doctorName: ''
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        activityName: '',
        activityId: '',
        registerSource: '',
        registerTime: [],
        doctorId: '',
        doctorName: ''
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryUserPage(
        query.page,
        query.limit,
        query.registerTime && query.registerTime.length > 0 ? query.registerTime[0] : '',
        query.registerTime && query.registerTime.length > 1 ? query.registerTime[1] : '',
        query.activityName,
        query.activityId,
        query.registerSource,
        query.doctorId,
        query.doctorName
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
