<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">专场活动名称</div>
              <el-input v-model="query.specialActivityName" placeholder="请输入活动名称" />
            </el-col>
            <el-col :span="6">
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
            <el-col :span="6">
              <div class="title">专场活动开始时间</div>
              <el-date-picker
                v-model="query.startTimeRange"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="6">
              <div class="title">专场活动结束时间</div>
              <el-date-picker
                v-model="query.endTimeRange"
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
            <el-col :span="24">
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <ylButton type="primary" @click="addClick">创建专场活动</ylButton>
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
          <el-table-column label="活动ID" min-width="120" align="center" prop="id">
          </el-table-column>
          <el-table-column label="专场活动名称" min-width="100" align="center" prop="specialActivityName">
          </el-table-column>
          <el-table-column label="专场活动开始时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="专场活动结束时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="专场活动创建时间" min-width="150" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="150" align="center" prop="createrName">
          </el-table-column>
          <el-table-column label="创建人手机号" min-width="150" align="center" prop="mobile">
          </el-table-column>
          <el-table-column label="专场活动类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(typeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.progress | dictLabel(progressArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <div class="option">
                  <yl-button type="text" @click="showDetail(row)">查看</yl-button>
                  <yl-button type="text" :disabled="row.progress != 1" @click="editClick(row)">编辑</yl-button>
                  <yl-button type="text" :disabled="row.status == 2 || row.progress == 3" @click="stopClick(row)">停用</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 停用弹框 -->
    <yl-dialog
        title="提示"
        :visible.sync="stopVisible"
        :show-footer="true"
        :destroy-on-close="true"
        width="500px"
        @confirm="stopConfirm"
      >
        <div class="stop-content">
          <div class="tip-box">
            <i class="el-icon-warning"></i>
            <span>日历管理的活动停用之后，APP活动日历将会下架，单独设置的营销活动依然有效，是否停用。</span>
          </div>
        </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  specialActivityPageList,
  specialActivityStatus
} from '@/subject/admin/api/b2b_api/marketing_manage';

export default {
  name: 'SpecialActivityList',
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
          title: '专场活动'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        specialActivityName: '',
        status: 0,
        type: 0,
        startTimeRange: [],
        endTimeRange: []
      },
      dataList: [],
      // 停用弹框
      stopVisible: false,
      currentOperationRow: {}
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await specialActivityPageList(
        query.page,
        query.limit,
        query.specialActivityName,
        query.status,
        query.type,
        query.startTimeRange && query.startTimeRange.length ? query.startTimeRange[0] : undefined,
        query.startTimeRange && query.startTimeRange.length > 1 ? query.startTimeRange[1] : undefined,
        query.endTimeRange && query.endTimeRange.length ? query.endTimeRange[0] : undefined,
        query.endTimeRange && query.endTimeRange.length > 1 ? query.endTimeRange[1] : undefined
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
        specialActivityName: '',
        status: 0,
        type: 0,
        startTimeRange: [],
        endTimeRange: []
      }
    },
    // 创建促销活动
    // 查看 operationType: 1-查看 2-修改 3-新增
    addClick() {
      this.$router.push({
        name: 'SpecialActivityEdit',
        params: {
          operationType: 3
        }
      });
    },
    // 查看 operationType: 1-查看 2-修改 3-新增
    showDetail(row) {
      // 跳转详情
      this.$router.push({
        name: 'SpecialActivityEdit',
        params: {
          id: row.id,
          operationType: 1
        }
      });
    },
    // 修改
    editClick(row) {
      this.$router.push({
        name: 'SpecialActivityEdit',
        params: {
          id: row.id,
          operationType: 2
        }
      });
    },
    // 停用点击
    stopClick(row) {
      this.currentOperationRow = row
      this.stopVisible = true
    },
    async stopConfirm() {
      this.$common.showLoad()
      let data = await specialActivityStatus(this.currentOperationRow.id ,2)
      this.$common.hideLoad()
      if (typeof data != 'undefined') {
        this.$common.n_success('停用成功')
        this.stopVisible = false
        this.getList()
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
