<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">活动名称</div>
              <el-input v-model="query.specialActivityName" placeholder="请输入活动名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">活动状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in progressArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">活动时间</div>
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
              <div class="title">预约时间</div>
              <el-date-picker
                v-model="query.appointmentTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">预约人</div>
              <el-input v-model="query.appointmentUserName" placeholder="请输入预约人姓名" />
            </el-col>
            <el-col :span="8">
              <div class="title">专场活动类型</div>
              <el-radio-group v-model="query.type">
                <el-radio class="option-class" :label="0">全部</el-radio>
                <el-radio class="option-class" v-for="item in typeArray" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">营销活动参与企业</div>
              <el-input v-model="query.specialActivityEnterpriseName" placeholder="请输入活动参与企业" />
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
          @getList="getList"
        >
          <el-table-column label="活动主题" min-width="100" align="center" prop="specialActivityName">
          </el-table-column>
          <el-table-column label="活动开始时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动结束时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="预约时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.appointmentTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="预约人" min-width="150" align="center" prop="appointmentUserName">
          </el-table-column>
          <el-table-column label="预约人联系方式" min-width="150" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="预约人所在企业" min-width="150" align="center" prop="appointmentUserEnterpriseName">
          </el-table-column>
          <el-table-column label="营销活动参与企业" min-width="150" align="center" prop="specialActivityEnterpriseName">
          </el-table-column>
          <el-table-column label="活动类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(typeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.process | dictLabel(progressArray) }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  appointmentPageList
} from '@/subject/admin/api/b2b_api/marketing_manage';

export default {
  name: 'SubscribeList',
  components: {
  },
  computed: {
  },
  data() {
    return {
      // 活动进度
      progressArray: [
        {
          label: '未开始',
          value: 1
        },
        {
          label: '进行中',
          value: 2
        },
        {
          label: '已结束',
          value: 3
        }
      ],
      // 活动类型
      typeArray: [
        {
          label: '满赠',
          value: 1
        },
        {
          label: '特价',
          value: 2
        },
        {
          label: '秒杀',
          value: 3
        },
        {
          label: '组合包',
          value: 4
        }
      ],
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '营销管理'
        },
        {
          title: '预约管理'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        specialActivityName: '',
        status: 0,
        time: [],
        appointmentTime: [],
        appointmentUserName: '',
        type: 0,
        specialActivityEnterpriseName: ''
      },
      dataList: []
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await appointmentPageList(
        query.page,
        query.limit,
        query.specialActivityName,
        query.status,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined,
        query.appointmentTime && query.appointmentTime.length ? query.appointmentTime[0] : undefined,
        query.appointmentTime && query.appointmentTime.length > 1 ? query.appointmentTime[1] : undefined,
        query.appointmentUserName,
        query.type,
        query.specialActivityEnterpriseName
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        status: 0,
        type: 0,
        time: [],
        appointmentTime: []
      }
    },
    getCellClass(row) {
      if (row.columnIndex == 4) {
        return 'border-1px-l'
      }
      return ''
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
