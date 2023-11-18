<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">授信主体名称</div>
              <el-input v-model="query.name" placeholder="请输入授信主体名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
              <el-select
                v-model="query.status"
                placeholder="请选择状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in periodStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-8 pad-t-16">
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
          <el-table-column label="开始时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.startTime | formatDate('yyyy-MM-dd') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span v-if="row.longEffective === 0">{{ row.endTime | formatDate('yyyy-MM-dd') }}</span>
              <span v-else>长期有效</span>
            </template>
          </el-table-column>
          <el-table-column label="信用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.totalAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="临时额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.temporaryAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 1)">{{ row.usedAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已还款额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 2)">{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="可使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.availableAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="80" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : 'col-up']">{{ row.status | enable }}</span>
            </template>
          </el-table-column>
          <el-table-column label="信用账期" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.period ? row.period + '天' : '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="139" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['1']" type="text" :disabled="row.type === 2" @click="requestPeriod(row)">申请临时额度</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog
        title="申请临时额度"
        width="425px"
        @confirm="confirm"
        right-btn-name="提交申请"
        :visible.sync="showDialog">
        <div class="period-form">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="form"
            label-width="140px"
            label-position="right">
            <el-form-item label="授信主体" style="margin-bottom: 0;">
              <span>{{ form.ename }}</span>
            </el-form-item>
            <el-form-item label="申请临时额度" prop="amount">
              <el-input
                type="number"
                v-model="form.amount"
                placeholder="请输入申请临时额度" />
            </el-form-item>
            <el-form-item label="当前可用额度" style="margin-bottom: 0;">
              <span>{{ form.availableAmount | toThousand('￥') }}</span>
            </el-form-item>
            <el-form-item label="申请后可用额度" style="margin-bottom: 0;">
              <span>{{ applyMount | toThousand('￥') }}</span>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <order
        :visible.sync="showDialog1"
        :is-hk="isHk"
        :data="showInfo"
        :id="showInfo.id">
      </order>
    </div>
  </div>
</template>

<script>
import {
  getPeriodList,
  getCompanyBossPeriod,
  periodApply
} from '@/subject/pop/api/period'
import { periodStatus } from '@/subject/pop/utils/busi'
import { isNumZ } from '@/subject/pop/utils/rules'
import order from '../list/component/order'
import { mapGetters } from 'vuex'
export default {
  name: 'BuyerList',
  components: {
    order
  },
  computed: {
    // 账期状态
    periodStatus() {
      return periodStatus()
    },
    // 申请后可使用额度
    applyMount() {
      const mount = parseFloat(this.form.availableAmount || 0) + parseFloat(this.form.amount || 0)
      return mount.toFixed(2)
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
          title: '采购商账期列表'
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
      form: {},
      rules: {
        amount: [{ required: true, validator: isNumZ, trigger: 'blur', fixed: 2 }]
      },
      // 展示列表弹框附带信息
      showInfo: {},
      // 是否展示弹框
      showDialog1: false,
      // 点击弹框是都是还款订单
      isHk: false
    }
  },
  activated() {
    this.getList()
    this.getCompanyPeriod()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getPeriodList(
        query.page,
        query.limit,
        query.status,
        '',
        2,
        query.name
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
    // 申请临时额度
    requestPeriod(row) {
      this.form = this.$common.clone(row)
      this.showDialog = true
    },
    // 提交申请临时额度
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          let form = this.form
          this.$common.showLoad()
          let data = await periodApply(
            form.id,
            parseFloat(form.amount)
          )
          this.$common.hideLoad()
          if (data && data.result) {
            this.showDialog = false
            this.$common.n_success('提交申请成功')
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 展示订单列表弹框
    showAmountDialog(row, type) {
      this.showInfo = this.$common.clone(row)
      if (type === 1) {
        this.isHk = false
      } else {
        this.isHk = true
      }
      this.showDialog1 = true
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
