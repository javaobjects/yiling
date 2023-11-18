<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>{{ data.name || '- -' }}
        </div>
        <div class="content-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="intro">
                {{ data.channelId | dictLabel(channelType) }}
              </div>
            </el-col>
            <el-col :span="8">
              <div class="intro">
                <span class="font-title-color">企业编码：</span>
                {{ easCode || '- -' }}
              </div>
            </el-col>
            <el-col :span="8">
              <div class="intro">
                <span class="font-title-color">已使用兑付总金额：</span>
                <span class="font-size-lg">{{ usedAmount | toThousand('￥') }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp">导出明细</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          stripe
          :list="dataList"
          :total="query.total"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="申请单号" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.applicantCode }}</div>
            </template>
          </el-table-column>
          <el-table-column label="申请类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.executeMeans | dictLabel(agreementRestitution) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="使用返利金额" min-width="120" align="center" prop="purchaseNumber">
            <template slot-scope="{ row }">
              <div>{{ row.totalAmount | toThousand('￥') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="审核状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.status | dictLabel(agreementUseStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="审核时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { agreementUseStatus, agreementRestitution, channelType } from '@/subject/admin/utils/busi';
import { queryFinanceUseListPageList } from '@/subject/admin/api/reconciliation';
export default {
  name: 'ReconciliationRebateInfo',
  components: {},
  computed: {
    agreementUseStatus() {
      return agreementUseStatus();
    },
    agreementRestitution() {
      return agreementRestitution();
    },
    channelType() {
      return channelType();
    }
  },
  data() {
    return {
      data: {},
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      easCode: '',//从上级返利对账中传递过来的
      eid: '',//从上级返利对账中传递过来的
      usedAmount: ''//从上级返利对账中传递过来的
    }
  },
  mounted() {
    this.easCode = this.$route.params.easCode;
    this.eid = this.$route.params.eid;
    this.usedAmount = this.$route.params.usedAmount;
    if (this.easCode != '' || this.eid != '') {
      this.getList();
    }

  },
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await queryFinanceUseListPageList(
        query.page,this.easCode,this.eid,query.limit
      )
      this.loading = false;
      if (data) {
        this.data = data;
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    // 导出明细
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementUseExportService',
        fileName: '导出明细',
        groupName: '返利使用次数-查看',
        menuName: '返利对账-财务',
        searchConditionList: [
          {
            desc: '企业id',
            name: 'enterpriseId',
            value: this.eid || ''
          },
          {
            desc: '企业编码',
            name: 'easCode',
            value: this.easCode || ''
          },
          {
            desc: '查询类型（默认商务） 1-商务 2-财务',
            name: 'queryType',
            value: 2
          }
        ]
      })
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
</style>
