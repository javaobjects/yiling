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
              <div class="title">账期状态</div>
              <el-select
                v-model="query.status"
                placeholder="请选择账期状态">
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

      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
        <div class="right-btn">
          <yl-button v-role-btn="['1']" type="primary" @click="downLoadTemp" plain>导出查询结果</yl-button>
          <yl-button v-role-btn="['2']" type="primary" @click="addPeriod">添加企业额度</yl-button>
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
          <el-table-column label="采购商名称" min-width="139" align="center" prop="customerName">
          </el-table-column>
          <el-table-column label="信用额度（元）" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.totalAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已使用额度（元）" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 1)">{{ row.usedAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已还款额度（元）" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 2)">{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="待还款额度（元）" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 3)">{{ row.needRepaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="可使用额度（元）" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.availableAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : 'col-up']">{{ row.status | enable }}</span>
            </template>
          </el-table-column>
          <el-table-column label="信用周期（天）" min-width="139" align="center" prop="period">
          </el-table-column>
          <el-table-column label="上浮点位（%）" min-width="139" align="center" prop="upPoint">
            <template slot-scope="{ row }">
              <span>{{ !row.upPoint ? '- -' : row.upPoint }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="111" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['3']" type="text" @click="editRow(row)">修改</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog
        :title="isEdit ? '修改企业账期信息' : '添加企业账期信息'"
        @confirm="confirm"
        width="570px"
        :visible.sync="showDialog">
        <div class="period-form">
          <el-form
            ref="dataForm"
            :rules="rules"
            :model="form"
            label-width="120px"
            label-position="right">
            <el-form-item label="采购商名称" prop="name">
              <el-select
                v-model="form.name"
                filterable
                :disabled="isEdit"
                remote
                placeholder="请输入采购商名称"
                @focus="getUserList()"
                :remote-method="getUserList"
                :loading="userLoading">
                <el-option
                  v-for="item in userList"
                  :key="item.customerEid"
                  :label="item.customerName"
                  :value="item.customerEid">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="信用额度" prop="amount">
              <el-input
                type="number"
                v-model="form.amount"
                placeholder="请输入信用额度">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
            <el-form-item label="信用周期" prop="day">
              <el-input
                type="number"
                v-model="form.day"
                placeholder="请输入信用周期">
                <template slot="append">天</template>
              </el-input>
            </el-form-item>
            <el-form-item label="已使用额度" style="margin-bottom: 0;" v-if="isEdit">
              <div class="used-amount">
                <span>{{ form.usedAmount | toThousand('￥') }}</span>
              </div>
              <div class="repayment-amount">
                <span>已还款额度</span>
                <span class="mar-l-10">{{ form.repaymentAmount | toThousand('￥') }}</span>
              </div>
            </el-form-item>
            <el-form-item label="可用额度" style="margin-bottom: 0;" v-if="isEdit">
              <span>{{ canUseAmount.toFixed(2) | toThousand('￥') }}</span>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
              <div class="err-text">
                停用后当前企业将不可使用账期支付方式。
              </div>
            </el-form-item>
            <el-form-item label="上浮点位" prop="upPoint">
              <el-input
                v-model="form.upPoint"
                placeholder="需大于0，保留2位小数">
                <template slot="append">%</template>
              </el-input>
              <div class="up-text">
                例：输入12.34，则表示上浮点位为12.34%
              </div>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <order
        :visible.sync="showDialog1"
        :check-type="checkType"
        :data="showInfo"
        :id="showInfo.id">
      </order>
    </div>
  </div>
</template>

<script>
import {
  getPeriodList,
  createCompanyPeriod,
  editCompanyPeriod,
  getPeriodBuyerList
} from '@/subject/pop/api/b2b_api/period'
import { createDownLoad } from '@/subject/pop/api/common'
import { periodStatus } from '@/subject/pop/utils/busi'
import { isNumZZ, isNumZ } from '@/subject/pop/utils/rules'
import order from './component/order'

export default {
  name: 'B2bPeriodIndex',
  components: {
    order
  },
  computed: {
    // 账期状态
    periodStatus() {
      return periodStatus()
    },
    // 输入额度时计算可用额度
    canUseAmount() {
      let form = this.form
      return this.$common.sub(this.$common.add(form.amount, form.repaymentAmount), form.usedAmount)
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '账期管理',
          path: ''
        },
        {
          title: '账期额度管理'
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
      form: {},
      rules: {
        name: [{ required: true, message: '请输入采购商名称', trigger: 'blur' }],
        amount: [{ required: true, validator: isNumZ, trigger: 'blur', fixed: 2 }],
        day: [{ required: true, validator: isNumZZ, trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        upPoint: [{ required: false, validator: isNumZ, trigger: 'blur', fixed: 2 }]
      },
      isEdit: false,
      // 采购商列表
      userList: [],
      // 采购商搜索loading
      userLoading: false,
      // 展示列表弹框附带信息
      showInfo: {},
      // 是否展示弹框
      showDialog1: false,
      // 点击弹框类别
      checkType: 1
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
      let data = await getPeriodList(
        query.page,
        query.limit,
        query.status,
        query.name
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
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
    // 添加企业额度
    addPeriod() {
      this.isEdit = false
      this.form = {}
      this.showDialog = true
    },
    // 修改
    editRow(row) {
      this.isEdit = true
      let form = this.$common.clone(row)
      this.form = Object.assign(form, {
        name: row.customerName,
        amount: row.totalAmount,
        day: row.period,
        upPoint: row.upPoint == 0 ? '' : row.upPoint
      })
      this.showDialog = true
    },
    // 获取客户列表
    async getUserList(text = '') {
      this.userLoading = true
      let data = await getPeriodBuyerList(
        1,
        999,
        text
      )
      this.userLoading = false
      if (data) {
        this.userList = data.list
      }
    },
    // 新增企业额度
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          let form = this.form
          let data = null
          this.$common.showLoad()
          if (this.isEdit) {
            data = await editCompanyPeriod(
              form.id,
              parseFloat(form.amount),
              form.day,
              form.upPoint,
              form.status
            )
          } else {
            data = await createCompanyPeriod(
              form.name,
              parseFloat(form.amount),
              form.day,
              form.upPoint,
              form.status
            )
          }
          this.$common.hideLoad()
          if (typeof data !== 'undefined') {
            this.showDialog = false
            if (this.isEdit) {
              this.$common.n_success('修改成功')
            } else {
              this.$common.n_success('新增成功')
            }
            this.getList()
          }
        } else {
          return false
        }
      })
    },
    // 展示订单列表弹框
    showAmountDialog(row, type) {
      this.showInfo = row
      this.checkType = type
      this.showDialog1 = true
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2bPaymentDaysAccountExportService',
        fileName: 'string',
        groupName: '账期列表',
        menuName: '账期管理-账期列表',
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
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
