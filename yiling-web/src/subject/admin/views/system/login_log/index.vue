<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <!-- <el-col :span="6">
              <div class="title">用户名</div>
              <el-input v-model="query.userName" placeholder="请输入用户名" />
            </el-col> -->
            <el-col :span="6">
              <div class="title">用户ID</div>
               <el-input v-model="query.userId" placeholder="请输入用户ID" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">应用</div>
              <el-select v-model="query.appId" placeholder="请选择应用ID">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in loginLogAppType"
                  :label="item.label"
                  :value="item.value"
                  :key="item.id"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">登录账号</div>
               <el-input v-model="query.loginAccount" placeholder="请输入登录账号" @keyup.enter.native="handleSearch"/>
            </el-col>

          </el-row>
          <el-row class="box">
             <el-col :span="12">
              <div class="title">登录时间</div>
              <!-- <el-date-picker
                v-model="loginTimeRange"
                type="daterange"
                format="yyyy/MM/dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker> -->
              <!-- <el-date-picker
                v-model="loginTimeRange"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker> -->
              <el-date-picker
                v-model="loginTimeRange"
                type="datetimerange"
                format="yyyy/MM/dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
      </div>
      <div class="table-box">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="应用" min-width="100" align="center" prop="appId">
            <template slot-scope="{ row }">
              <span>{{ row.appId | dictLabel(loginLogAppType) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户ID" min-width="100" align="center" prop="userId"> </el-table-column>
          <el-table-column label="用户名" min-width="100" align="center" prop="userName"> </el-table-column>
          <el-table-column label="登录账号" min-width="120" align="center" prop="loginAccount"> </el-table-column>
          <el-table-column label="登录方式" min-width="100" align="center" prop="grantType"> </el-table-column>
          <el-table-column label="登录终端" min-width="100" align="center" prop="grantTerminal"> </el-table-column>
          <el-table-column label="登录状态" min-width="100" align="center" prop="loginStatus">
            <template slot-scope="{ row }">
              <span :class="[row.loginStatus === 'success' ? 'col-down' : 'col-up']">{{
                row.loginStatus | loginStatus
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="登录IP" min-width="100" align="center" prop="ipAddress"> </el-table-column>
          <el-table-column min-width="160" align="center">
            <template slot="header">
              <el-tooltip class="item" effect="dark" content="IOS:app版本  Android:app版本+渠道号" popper-class="longLogTooltip" placement="top" >
                <div class="login-remark">登录设备<i class="el-icon-question "></i></div>
              </el-tooltip>
            </template>
            <template slot-scope="{ row }">
              <div>{{ row.loginBrowser }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="160" align="center">
            <template slot="header">
              <el-tooltip class="item" effect="dark" content="IOS:型号+操作系统版本  Android:品牌+型号+操作系统版本+SDK版本" popper-class="longLogTooltip" placement="top" >
                <div class="login-remark">操作系统<i class="el-icon-question "></i></div>
              </el-tooltip>
            </template>
            <template slot-scope="{ row }">
              <div>{{ row.osInfo }}</div>
            </template>
          </el-table-column>
          <el-table-column label="登录时间" min-width="150" align="center" prop="loginTime">
            <template slot-scope="{ row }">
              <div>{{ row.loginTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="返回内容" min-width="150" align="center" prop="returnContent">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.returnContent"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.returnContent.length > 20">{{ row.returnContent.slice(0, 10) + "..." }}</span>
                <span v-else>{{ row.returnContent || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="用户代理" min-width="150" align="center" prop="userAgent">
            <template slot-scope="{ row }">
              <el-tooltip
                class="item"
                effect="dark"
                :content="row.userAgent"
                popper-class="longLogTooltip"
                placement="top"
              >
                <span v-if="row.userAgent.length > 20">{{ row.userAgent.slice(0, 10) + "..." }}</span>
                <span v-else>{{ row.userAgent || "- -" }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getLoginLogList } from '@/subject/admin/api/log'
import { loginLogApp } from '@/subject/admin/utils/busi'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'LoginLog',
  components: {},
  computed: {
    // 业务类型
    loginLogAppType() {
      return loginLogApp()
    }
  },
  filters: {
    loginStatus(e) {
      return e === 'success' ? '成功' : '失败'
    }
  },
  data() {
    return {
      query: {
        //  页码
        current: 1,
        //  每页记录数
        size: 10,
        // 应用id
        appId: '',
        // 用户名
        userName: '',
        //  开始登录时间
        startLoginTime: '',
        //  结束登录时间
        endLoginTime: '',
        loginAccount: '',
        userId: ''
      },
      loginTimeRange: [],
      // 列表
      dataList: [],
      loading: false
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getLoginLogList(
        query.current,
        query.size,
        query.appId,
        // query.userName,
        this.loginTimeRange && this.loginTimeRange.length > 0 ? formatDate(this.loginTimeRange[0], 'yyyy-MM-dd hh:mm:ss') : '',
        this.loginTimeRange && this.loginTimeRange.length > 0 ? formatDate(this.loginTimeRange[1], 'yyyy-MM-dd hh:mm:ss') : '',
        query.userId,
        query.loginAccount
      )
      if (data) {
        this.loading = false
        this.dataList = data.records
        query.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        appId: '',
        userName: '',
        startLoginTime: '',
        endLoginTime: '',
        loginAccount: '',
        userId: ''
      }
      this.loginTimeRange = []
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.longLogTooltip {
  max-width: 20%;
}
</style>
