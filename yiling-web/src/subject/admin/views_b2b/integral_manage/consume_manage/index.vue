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
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入规则名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">执行状态</div>
              <el-select class="select-width" v-model="query.status" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in statusData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">执行进度</div>
              <el-select class="select-width" v-model="query.progress" placeholder="请选择">
                <el-option label="全部" value=""></el-option>
                <el-option
                  v-for="item in integralRuleProgressType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">创建人</div>
              <el-input v-model="query.createUserName" @keyup.enter.native="searchEnter" placeholder="请输入创建人" />
            </el-col>
            <el-col :span="6">
              <div class="title">创建人手机号</div>
              <el-input v-model="query.mobile" @keyup.enter.native="searchEnter" placeholder="请输入创建人手机号" />
            </el-col>
            <el-col :span="6">
              <div class="title">有效时间</div>
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" @click="establishClick">创建消耗规则</ylButton>
        </div>
      </div>
       <!-- 下部列表 -->
      <div class="search-bar">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="规则ID" min-width="60" align="center" prop="id">
            <!-- <template slot-scope="scope">
              <span>{{ (query.page - 1) * query.limit + scope.$index + 1 }}</span>
            </template> -->
          </el-table-column>
          <el-table-column label="规则名称" min-width="130" align="center" prop="name">
          </el-table-column>
          <el-table-column label="执行状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              {{ row.status | dictLabel(statusData) }}
            </template>
          </el-table-column>
          <el-table-column label="执行进度" min-width="70" align="center">
            <template slot-scope="{ row }">
              {{ row.progress | dictLabel(integralRuleProgressType) }}
            </template>
          </el-table-column>
          <el-table-column label="规则生效时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              {{ row.startTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="规则失效时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              {{ row.endTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="规则创建时间" min-width="110" align="center">
            <template slot-scope="{ row }">
              {{ row.createTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="100" align="center" prop="createUserName">
          </el-table-column>
          <el-table-column label="创建人手机号" min-width="100" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="运营备注" min-width="120" align="center" prop="description">
          </el-table-column>
          <el-table-column label="操作" min-width="110" align="center" fixed="right">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="seeClick(2, row.id)">查看</yl-button>
              <yl-button type="text" v-if="row.progress == 1" @click="seeClick(3, row.id)">编辑</yl-button>
              <yl-button type="text" @click="copyClick(row.id)">复制</yl-button>
              <div v-if="row.progress != 3">
                <yl-button type="text" v-if="row.status == 1" @click="deactivateClick(row)">停用</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryListPage, updateStatus, integralUseRuleCopy } from '@/subject/admin/api/b2b_api/consume_manage'
import { integralRuleProgress } from '@/subject/admin/busi/b2b/integral'
export default {
  name: 'ConsumeManage',
  computed: {
    integralRuleProgressType() {
      return integralRuleProgress()
    }
  },
  data() {
    return {
      query: {
        name: '',
        status: '',
        progress: '',
        createUserName: '',
        mobile: '',
        time: [],
        total: 0,
        page: 1,
        limit: 10
      },
      statusData: [
        {
          value: 1,
          label: '启用'
        },
        {
          value: 2,
          label: '禁用'
        }
      ],
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
    // 点击创建发放规则
    establishClick() {
      this.$router.push({
        name: 'ConsumeManageEstablish',
        params: {
          //1 创建 2查看 3编辑
          type: 1,
          id: 0
        }
      });
    },
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryListPage(
        query.name,
        query.status,
        query.progress,
        query.createUserName,
        query.mobile,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    seeClick(type, id) {
      this.$router.push({
        name: 'ConsumeManageEstablish',
        params: {
          //1 创建 2查看 3编辑 4复制
          type: type,
          id: id
        }
      });
    },
    //点击停用
    deactivateClick(row) {
      this.$confirm(`确认停用 ${ row.name }！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await updateStatus(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('停用成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    //点击复制
    async copyClick(val) {
      this.$common.showLoad();
      let data = await integralUseRuleCopy(
        val
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.seeClick(4, data)
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
        name: '',
        status: '',
        progress: '',
        createUserName: '',
        mobile: '',
        time: [],
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>