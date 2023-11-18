<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">采购商名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入采购商名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">审核状态</div>
              <el-select
                v-model="query.status"
                placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in periodAuditStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16 pad-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="query.total"
                @search="handleSearch"
                @reset="handleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box box-total" v-if="currentEnterpriseInfo.yilingFlag">
        <div class="flex-row-left">
          <div>
            <div class="mar-b-16">
              <div class="left"><span>集团总账期额度：{{ period.totalAmount | toThousand('￥') }}</span></div>
              <div class="left"><span>已使用账期额度：{{ period.usedAmount | toThousand('￥') }}</span></div>
            </div>
            <div>
              <div class="left"><span>账期还款额度：{{ period.repaymentAmount | toThousand('￥') }}</span></div>
              <div class="left"><span>可用账期额度：{{ period.availableAmount | toThousand('￥') }}</span></div>
            </div>
          </div>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
        <div class="right-btn">
          <yl-button v-role-btn="['1']" type="primary" @click="downLoadTemp" plain>导出查询结果</yl-button>
        </div>
      </div>
      <div>
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="授信主体" min-width="139" align="center" prop="ename">
          </el-table-column>
          <el-table-column label="采购商名称" min-width="139" align="center" prop="customerName">
          </el-table-column>
<!--          <el-table-column label="临时额度" min-width="139" align="center">-->
<!--            <template slot-scope="{ row }">-->
<!--              <span>{{ row.temporaryAmount | toThousand('￥') }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="申请临时额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.shortTimeQuota | toThousand('￥') }}</span>
            </template>
          </el-table-column>
<!--          <el-table-column label="同意申请后可使用额度" min-width="139" align="center">-->
<!--            <template slot-scope="{ row }">-->
<!--              <span>{{ row.afterAgreementTotalAmount | toThousand('￥') }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="审核状态" min-width="120" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.auditStatus | dictLabel(periodAuditStatus) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="审核时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.updateTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="审核人" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.updateUser }}</span>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="111" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['2']" type="text" :disabled="row.auditStatus !== 1" @click="requestPeriod(row)">审核</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog
        title="临时额度审核"
        width="50%"
        @confirm="confirm"
        right-btn-name="审核通过"
        :show-cancle="false"
        :visible.sync="showDialog">
        <div class="period-apply-form">
          <el-form
            :model="form"
            label-width="140px"
            label-position="right">
            <el-row>
              <el-col :span="12">
                <el-form-item label="授信主体">
                  <span>{{ form.ename }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="集团可用账期额度">
                  <span>{{ period.availableAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业名称" style="margin-bottom: 0;">
                  <span>{{ form.customerName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="授信状态" style="margin-bottom: 0;">
                  <span :class="[form.status === 1 ? 'col-down' : 'col-up']">{{ form.status | enable }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="信用额度" style="margin-bottom: 0;">
                  <span>{{ form.totalAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信用账期" style="margin-bottom: 0;">
                  <span>{{ form.period ? form.period + '天' : '- -' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="已使用额度" style="margin-bottom: 0;">
                  <span>{{ form.usedAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="已还款额度" style="margin-bottom: 0;">
                  <span>{{ form.repaymentAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="未还款额度">
              <span>{{ form.unRepaymentAmount | toThousand('￥') }}</span>
              <span>（其中到期未还款额度{{ form.exprUnRepaymentAmount | toThousand('￥') }}元）</span>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业可使用额度" style="margin-bottom: 0;">
                  <span>{{ form.availableAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="历史剩余临时额度" style="margin-bottom: 0;">
                  <span>{{ form.temporaryAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="申请临时额度" style="margin-bottom: 0;">
                  <span>{{ form.shortTimeQuota | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="通过后可使用额度" style="margin-bottom: 0;">
                  <span>{{ form.afterAgreementTotalAmount | toThousand('￥') }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <yl-button slot="left-btn" type="primary" plain @click="handleRequest(form.id, 3)">审核驳回</yl-button>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getIsApplyList, getCompanyBossPeriod, setIsApplyStatus } from '@/subject/pop/api/period'
import { createDownLoad } from '@/subject/pop/api/common'
import { periodStatus, periodAuditStatus } from '@/subject/pop/utils/busi'
import { mapGetters } from 'vuex'
export default {
  name: 'Apply',
  components: {
  },
  computed: {
    // 账期状态
    periodStatus() {
      return periodStatus()
    },
    // 账期审核状态
    periodAuditStatus() {
      return periodAuditStatus()
    },
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
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
          title: '账期管理',
          path: ''
        },
        {
          title: '临时额度申请'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10,
        status: 0
      },
      // 列表
      dataList: [],
      loading: false,
      // 展示弹框
      showDialog: false,
      // 集团额度信息
      period: {},
      form: {}
    }
  },
  activated() {
    this.getList()
    this.getCompanyPeriod()
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
      let data = await getIsApplyList(
        query.page,
        query.limit,
        query.name,
        query.status
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    async getCompanyPeriod() {
      let data = await getCompanyBossPeriod()
      if (data) {
        this.period = data
      }
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        status: 0
      }
    },
    // 点击审核临时额度
    requestPeriod(row) {
      this.form = row
      this.showDialog = true
    },
    confirm() {
      // 审核通过
      this.handleRequest(this.form.id, 2)
    },
    // 审核通过不通过
    async handleRequest(id, status) {
      this.$common.showLoad()
      let data = await setIsApplyStatus(id, status)
      this.$common.hideLoad()
      if (data && data.result) {
        this.showDialog = false
        if (status === 2) {
          this.$common.n_success('审核通过成功')
          this.getCompanyPeriod()
        } else {
          this.$common.n_success('审核已拒绝')
        }
        this.getList()
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'paymentDaysTemporaryExport',
        fileName: '临时额度审核',
        groupName: '临时额度审核',
        menuName: '账期管理-临时额度审核',
        searchConditionList: [
          {
            desc: '采购商名称',
            name: 'customerName',
            value: query.name || ''
          },
          {
            desc: '状态',
            name: 'status',
            value: query.status
          }
        ]
      })
      this.$common.hideLoad()
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
