<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="query.ename" class="mar-r-5" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">姓名</div>
              <el-input v-model="query.name" class="mar-r-5" @keyup.enter.native="searchEnter" placeholder="请输入姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">手机号</div>
              <el-input v-model="query.mobile" class="mar-r-5" @keyup.enter.native="searchEnter" placeholder="请输入手机号" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">账户状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option label="启用" :value="1"></el-option>
                <el-option label="停用" :value="2"></el-option>
                <el-option label="注销" :value="9"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select v-model="query.etype" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in companyType" :key="item.value" :label="item.label" :value="item.value" >
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <yl-button v-role-btn="['1']" class="mar-r-10" @click="importClick" type="text">账号数据导入</yl-button>
        </div>
      </div>
      <div class="mar-t-10">
        <yl-table 
          :list="dataList" 
          :total="total" 
          :cell-class-name="() => 'border-1px-b'" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList"
        >
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base">
                  <span>账户状态：<span :class="row.staffInfo.status === 1 ? 'col-down' : 'col-up'">{{ row.staffInfo.status | dictLabel(getUserStatus) }}</span></span>
                  <span>创建时间：{{ row.staffInfo.createTime | formatDate }}</span>
                </div>
                <div class="content flex-row-left">
                  <div class="table-item">
                    <div class="item font-size-lg pad-tb-10 bold">{{ row.staffInfo.name }}</div>
                    <div class="item font-size-base font-title-color">{{ row.staffInfo.mobile }}</div>
                  </div>
                  <div class="table-item">
                    <div class="item font-size-base font-title-color text-c">
                      <div class="font-important-color bold">{{ row.enterpriseNames.length || 0 }}</div>所属企业
                    </div>
                  </div>
                  <div class="flex1">
                    <el-tag class="tag" v-for="(tag, idx) in row.enterpriseNames" :key="idx">{{ tag }}</el-tag>
                  </div>
                  <div class="table-item time">
                    <div class="item font-size-base font-title-color"><span class="font-light-color">最后维护时间：</span>{{ row.staffInfo.updateTime | formatDate }}</div>
                    <div class="item font-size-base font-title-color"><span class="font-light-color">最后维护人：</span>{{ row.staffInfo.updateUserName }}</div>
                  </div>
                  <div class="flex-column-center last-child">
                    <div>
                      <yl-button type="text" @click="showDetail(row, $index)">查看详情</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- excel导入 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import { getCompanyUserList } from '@/subject/admin/api/zt_api/company'
import { enterpriseType , userStatus} from '@/subject/admin/utils/busi'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'ZtCompanyUser',
  components: {
    ImportSendDialog
  },
  computed: {
    companyType() {
      return enterpriseType();
    },
    getUserStatus() {
      return userStatus();
    }
  },
  data() {
    return {
      query: {
        status: 0,
        etype: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      total: 0,
      // 导入
      importSendVisible: false,
      // 导入任务参数 excelCode: importStaff-账号数据导入
      info: {
        excelCode: 'importStaff'
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
      let data = await getCompanyUserList(
        query.page,
        query.limit,
        query.ename,
        query.etype,
        query.mobile,
        query.name,
        query.status
      )
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        status: 0,
        etype: 0,
        page: 1,
        limit: 10
      }
    },
    // excel导入
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 查看详情
    showDetail(row) {
      this.$router.push(`/zt_company/zt_company_user_detail/${row.staffInfo.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
