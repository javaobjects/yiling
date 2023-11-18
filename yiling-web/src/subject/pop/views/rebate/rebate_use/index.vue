<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" placeholder="请输入企业名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">企业编码</div>
              <el-input v-model="query.easCode" placeholder="请输入企业编码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">申请单号</div>
              <el-input v-model="query.applicantCode" placeholder="请输入申请单号" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-8">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton v-role-btn="['2']" type="primary" plain @click="applyClick">查看/申请返利使用</ylButton>
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
          <el-table-column label="企业名称" min-width="200" align="center" prop="name">
          </el-table-column>
          <el-table-column label="企业编码" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.easCode }}
              </div>
            </template>
          </el-table-column>
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
          <el-table-column label="发货组织" min-width="200" align="center" prop="sellerName">
          </el-table-column>
          <el-table-column label="操作" min-width="100" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="showDetail(row)">查看明细</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 查看明细 -->
    <yl-dialog title="查看明细" :visible.sync="showDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div class="dialog-content-top">
          <div class="flex-row-left item mar-b-10">
            <div class="line-view"></div>
            <span class="font-size-lg bold">{{ currentDetail.name }}</span>
          </div>
          <el-row>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">渠道类型：</span>{{ currentDetail.channelId | dictLabel(channelType) }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">企业状态：</span>{{ currentDetail.entStatus | enable }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">企业编码：</span>{{ currentDetail.easCode }}</div></el-col>
          </el-row>
          <el-row>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">申请总金额：</span>{{ currentDetail.totalAmount | toThousand('￥') }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">返利方式：</span>{{ currentDetail.executeMeans | dictLabel(agreementRestitution) }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">部门：</span>{{ currentDetail.executeDept }}</div></el-col>
          </el-row>
          <el-row>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">申请时间：</span>{{ currentDetail.createTime | formatDate }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">审核状态：</span>{{ currentDetail.status | dictLabel(agreementUseStatus) }}</div></el-col>
          </el-row>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <div class="flex-row-left item">
            <div class="line-view"></div>
            <span class="font-size-base">返利来源</span>
          </div>
          <yl-table
            stripe
            border
            show-header
            :list="providerList"
            :total="providerTotal"
            :page.sync="providerQuery.page"
            :limit.sync="providerQuery.limit"
            :loading2="loading2"
            @getList="queryUseDetailListPageListMethod"
          >
            <el-table-column label="返利种类" min-width="120" align="center" prop="rebateCategory">
            </el-table-column>
            <el-table-column label="归属年度" min-width="100" align="center" prop="year">
            </el-table-column>
            <el-table-column label="归属月度" min-width="100" align="center" prop="month">
            </el-table-column>
            <el-table-column label="费用科目" min-width="120" align="center" prop="costSubject">
            </el-table-column>
            <el-table-column label="费用归属部门" min-width="200" align="center" prop="costDept">
            </el-table-column>
            <el-table-column label="金额" min-width="200" align="center" prop="amount">
            </el-table-column>
            <el-table-column label="批复代码" min-width="200" align="center" prop="replyCode">
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  queryUseListPageList,
  queryUseDetailListPageList,
  getRebateSystemUrl
} from '@/subject/pop/api/rebate';
import { createDownLoad } from '@/subject/pop/api/common'
import { agreementUseStatus, agreementRestitution, channelType } from '@/subject/pop/utils/busi'

export default {
  name: 'RebateUse',
  components: {
  },
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
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '企业返利'
        },
        {
          title: '返利使用'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0
      },
      dataList: [],
      // 审核当前点击的 row
      currentRow: {},
      showDialog: false,
      loading2: false,
      providerQuery: {
        page: 1,
        limit: 10
      },
      providerList: [],
      providerTotal: 0,
      currentDetail: {}
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryUseListPageList(
        query.page,
        query.limit,
        query.name,
        query.easCode,
        query.applicantCode
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
        limit: 10
      }
    },
    // 查看明显
    showDetail(row) {
      this.currentRow = row
      this.showDialog = true
      this.currentDetail = {}
      this.queryUseDetailListPageListMethod()
    },
    async queryUseDetailListPageListMethod() {
      this.loading2 = true
      let providerQuery = this.providerQuery
      let data = await queryUseDetailListPageList(
        providerQuery.page,
        providerQuery.limit,
        this.currentRow.id
      );
      this.loading2 = false
      if (data) {
        this.currentDetail = data
        this.providerList = data.records
        this.providerTotal = data.total
      }
    },
    // 查看申请使用返利
    async applyClick() {
      let data = await getRebateSystemUrl(
      );
      if (data && data.url) {
        this.$common.goThreePackage(data.url)
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementUseExportService',
        fileName: '返利使用导出',
        groupName: '返利使用-商务',
        menuName: '返利使用-商务',
        searchConditionList: [
          {
            desc: '申请企业名称',
            name: 'name',
            value: query.name || ''
          },
          {
            desc: '申请企业easCode',
            name: 'easCode',
            value: query.easCode || ''
          },
          {
            desc: '申请单编号',
            name: 'applicantCode',
            value: query.applicantCode || ''
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
};
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
