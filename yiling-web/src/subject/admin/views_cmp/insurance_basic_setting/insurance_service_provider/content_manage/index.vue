<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">保险名称</div>
              <el-input v-model="query.insuranceName" placeholder="请输入保险名称" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <div class="title">时间段</div>
              <el-date-picker
                v-model="query.timeRange"
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
        <div class="search-btn-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="addContent">添加</yl-button>
        </div>
      </div>
      <div class="table-box mar-t-8">
        <yl-table
          stripe
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="保险名称" min-width="100" align="center" prop="insuranceName"></el-table-column>
          <el-table-column label="保险售卖额(元)" min-width="100" align="center" prop="payAmount"></el-table-column>
          <el-table-column label="服务商扣服务费比例(%)" min-width="100" align="center" prop="serviceRatio"></el-table-column>
          <el-table-column label="服务商" min-width="100" align="center" prop="companyName"></el-table-column>
          <el-table-column label="定额付费类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>季度：{{ row.quarterIdentification }}</div>
              <div>年度：{{ row.yearIdentification }}</div>
            </template>
          </el-table-column>
          <el-table-column label="单个药品与以岭结算额" min-width="180" align="center">
            <template slot-scope="{ row }">
              <div v-for="item in row.insuranceDetailList" :key="item.id">{{ item.goodsName }}：{{ item.settlePrice }}元，每月1次，每次拿{{ item.monthCount }}盒</div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="100" align="center" prop="loginStatus">
            <template slot-scope="{ row }">
              <span v-if="row.status == 1" :class="['content-status', row.status == 1 ? 'col-down' : '' ]">{{ row.status | contentStatus }}</span>
              <span v-else :class="['content-status', row.status == 2 ? 'col-up' : '' ]">{{ row.status | contentStatus }}</span>
              <el-switch :value="row.status === 1" class="switch" @change="e => switchChange(e, row)"> </el-switch>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { queryInsuranceContentList, insuranceContentStatusUpdate } from '@/subject/admin/api/cmp_api/insurance_basic_setting'
export default {
  name: 'ServiceProviderContent',
  components: {
  },
  computed: {
  },
  filters: {
    contentStatus(e) {
      return e === 1 ? '启用' : '停用'
    }
  },
  mounted() {
    this.id = this.$route.params.id
    this.companyName = this.$route.params.companyName
    if (this.id) {
      this.getList(this.id)
    }
  },
  data() {
    return {
      id: '',
      query: {
        current: 1,
        size: 10,
        insuranceName: '',
        timeRange: [],
        total: 0
      },
      // 列表
      dataList: [],
      loading: false
    }
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let id = this.id
      let data = await queryInsuranceContentList(
        Number(id),
        query.current,
        query.size,
        query.insuranceName,
        query.timeRange && query.timeRange.length ? query.timeRange[0] : '',
        query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : ''
      )
      this.loading = false
      if (data) {
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
        insuranceName: '',
        timeRange: []
      }
    },
    //  状态修改
    async switchChange(e, row) {
      if (row.status !== 2) {
        this.$confirm('确定停止该服务内容吗？', '停用', {
          confirmButtonText: '停用',
          cancelButtonText: '取消',
          type: 'warning',
          customClass: 'stop-content-cfm'
        }).then(async () => {
          // 停用
          row.status = 2
          this.$common.showLoad()
          let data = await insuranceContentStatusUpdate(row.id, row.status)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('操作成功')
            this.getList()
          }
        }).catch(() => {})
      } else {
        // 开启
        row.status = 1
        this.$common.showLoad()
        let data = await insuranceContentStatusUpdate(row.id, row.status)
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        } else {
          this.getList()
        }
      }
    },
    //  添加
    addContent() {
      this.$router.push({
        name: 'AddServiceProviderContent',
        params: {
          id: this.id,
          companyName: this.companyName
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.stop-content-cfm {
  .el-message-box__header {
    padding-top: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
    .el-message-box__title {
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      text-align: center;
    }
  }
  .el-message-box__container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 31px 0;
    .el-message-box__status {
      position: relative;
      color: #fa8c15;
      font-size: 16px !important;
      transform: translateY(0);
    }
    .el-message-box__message {
      padding-left: 3px;
    }
  }
  .el-message-box__btns {
    border-top: 1px solid #f0f0f0;
  }
}
</style>