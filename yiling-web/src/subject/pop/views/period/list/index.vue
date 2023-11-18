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
      <div class="common-box box-total" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
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
          <div class="right-btn">
            <yl-button v-role-btn="['1']" type="primary" @click="setGroupAccount" plain>设置集团总额度</yl-button>
          </div>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
        <div class="right-btn">
          <yl-button v-role-btn="['2']" type="primary" @click="downLoadTemp" plain>导出查询结果</yl-button>
          <yl-button v-role-btn="['3']" type="primary" @click="addPeriod">添加企业额度</yl-button>
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
          <el-table-column label="授信主体" min-width="139" align="center" prop="ename" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
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
          <el-table-column label="信用额度" min-width="139" align="center" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
            <template slot-scope="{ row }">
              <span>{{ row.totalAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="临时额度" min-width="139" align="center" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
            <template slot-scope="{ row }">
              <span>{{ row.temporaryAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 1)">{{ row.usedAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已还款额度" min-width="139" align="center" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
            <template slot-scope="{ row }">
              <span class="col-theme touch" @click="showAmountDialog(row, 2)">{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="可使用额度" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.availableAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.status === 1 ? 'col-down' : 'col-up']">{{ row.status | enable }}</span>
            </template>
          </el-table-column>
          <el-table-column label="信用账期" min-width="139" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.period ? row.period + '天' : '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="111" fixed="right" align="center">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['4']" type="text" @click="editRow(row)">修改</yl-button>
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
            <el-form-item label="授信主体" prop="company" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
              <el-select
                v-model="form.company"
                :disabled="isEdit"
                placeholder="请选择授信主体">
                <el-option
                  v-for="item in enterList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="采购商名称" prop="name">
              <el-select
                v-model="form.name"
                filterable
                :disabled="isEdit"
                remote
                reserve-keyword
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
            <el-form-item label="信用额度" prop="amount" v-if="currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2">
              <el-input
                type="number"
                v-model="form.amount"
                placeholder="请输入信用额度" />
            </el-form-item>
            <el-form-item label="可使用额度" prop="availableAmount" v-else>
              <el-input
                type="number"
                v-model="form.availableAmount"
                placeholder="请输入可使用额度" />
            </el-form-item>
            <el-form-item label="是否长期有效" prop="longEffective">
              <el-radio-group v-model="form.longEffective">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="账期周期" prop="time" v-if="form.longEffective === 0">
              <el-date-picker
                v-model="form.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="账期周期" prop="singleTime" v-else>
              <el-date-picker
                v-model="form.singleTime"
                type="date"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                placeholder="请选择开始日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="信用账期" prop="day">
              <el-input
                type="number"
                v-model="form.day"
                placeholder="请输入信用账期">
                <template slot="append">天</template>
              </el-input>
            </el-form-item>
            <el-form-item label="已使用额度" style="margin-bottom: 0;" v-if="(currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2) && isEdit">
              <div class="used-amount">
                <span>{{ form.usedAmount | toThousand('￥') }}</span>
              </div>
              <div class="repayment-amount">
                <span>已还款额度</span>
                <span class="mar-l-10">{{ form.repaymentAmount | toThousand('￥') }}</span>
              </div>
            </el-form-item>
            <el-form-item label="可用额度" style="margin-bottom: 0;" v-if="(currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2) && isEdit">
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
  saveCompanyBossPeriod,
  createCompanyPeriod,
  editCompanyPeriod,
  getCompanyPeriodMainPartList
} from '@/subject/pop/api/period'
import { getCustomerList } from '@/subject/pop/api/products'
import { createDownLoad } from '@/subject/pop/api/common'
import { periodStatus } from '@/subject/pop/utils/busi'
import { validateIsNumZ, validateNumFloatLength } from '@/subject/pop/utils/validate'
import { formatDate } from '@/subject/pop/utils/index'
import { isNumZZ, isNumZ, isNum } from '@/subject/pop/utils/rules'
import order from './component/order'
import { mapGetters } from 'vuex'
export default {
  name: 'PeriodIndex',
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
      return this.$common.add(this.$common.add(form.amount, form.temporaryAmount), this.$common.sub(form.repaymentAmount, form.usedAmount))
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
      // 集团额度信息
      period: {},
      form: {},
      rules: {
        company: [{ required: true, message: '请选择授信主体', trigger: 'change' }],
        name: [{ required: true, message: '请输入采购商名称', trigger: 'blur' }],
        amount: [{ required: true, validator: isNumZ, trigger: 'blur', fixed: 2 }],
        time: [{ required: true, message: '请选择到账周期', trigger: 'change' }],
        singleTime: [{ required: true, message: '请选择到账周期', trigger: 'change' }],
        day: [{ required: true, validator: isNumZZ, trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        availableAmount: [{ required: true, validator: isNum, trigger: 'blur', fixed: 2 }],
        longEffective: [{ required: true, message: '请选择是否长期有效', trigger: 'change' }]
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
      // 点击弹框是都是还款订单
      isHk: false,
      // 是否展示授信主体选择
      showMainPart: null,
      // 授信主体数组
      enterList: []
    }
  },
  activated() {
    this.getList()
    if (this.currentEnterpriseInfo.yilingFlag || this.currentEnterpriseInfo.channelId == 2) {
      this.getCompanyPeriod()
    }
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
        query.name,
        1,
        null
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 获取总账期信息
    async getCompanyPeriod() {
      let data = await getCompanyBossPeriod()
      if (data) {
        this.period = data
      }
    },
    // 获取授信主体 是否显示授信主体
    async getCompanyMainPartList(callback) {
      if (this.showMainPart === null) {
        this.$common.showLoad()
        let data = await getCompanyPeriodMainPartList()
        this.$common.hideLoad()
        if (data) {
          this.showMainPart = !!data.showMainPart
          this.enterList = data.enterpriseList
          if (callback) callback()
        }
      } else {
        if (callback) callback()
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
      this.getCompanyMainPartList((isShow) => {
        this.isEdit = false
        this.form = { 
          isShow,
          longEffective: 0
        }
        this.showDialog = true
      })
    },
    // 修改
    editRow(row) {
      this.getCompanyMainPartList((isShow) => {
        this.isEdit = true
        let timeArray = []
        let timeString = ''
        if (row.longEffective === 0 && row.startTime && row.endTime) {
          timeArray = [formatDate(row.startTime, 'yyyy-MM-dd'), formatDate(row.endTime, 'yyyy-MM-dd')]
        } else {
          timeString = formatDate(row.startTime, 'yyyy-MM-dd')
        }
        console.log('row', row);
        console.log('timeArray', timeArray);
        this.form = Object.assign({
          isShow,
          company: row.ename,
          name: row.customerName,
          amount: row.totalAmount,
          time: timeArray,
          singleTime: timeString,
          day: row.period
        }, row)
        this.showDialog = true
      })
    },
    // 设置集团总额度
    setGroupAccount() {
      this.$prompt('设置集团总账期额度为', '设置集团总账期额度', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入额度',
        inputValidator: (value) => {
          if (validateIsNumZ(value)) {
            if (validateNumFloatLength(value, 2)) {
              return '仅支持输入2位小数'
            } else {
              return true
            }
          } else {
            return '请输入正确的额度值'
          }
        },
        inputErrorMessage: '请输入额度'
      }).then(async ({ value }) => {
        this.$common.showLoad()
        let data = await saveCompanyBossPeriod(value)
        this.$common.hideLoad()
        if (data && data.result) {
          this.$common.n_success('设置集团额度成功')
          this.getCompanyPeriod()
        }
      })
    },
    // 获取客户列表
    async getUserList(text) {
      this.userLoading = true
      let data = await getCustomerList(
        1,
        999,
        null,
        null,
        null,
        text || ''
      )
      this.userLoading = false
      if (data) {
        this.userList = data.records
      }
    },
    // 新增企业额度
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          let form = this.form
          let data = null
          console.log('form',form);
          let amount = (this.currentEnterpriseInfo.yilingFlag || this.currentEnterpriseInfo.channelId == 2) ? form.amount : form.availableAmount
          this.$common.showLoad()
          if (this.isEdit) {
            if (form.longEffective === 0) {
              data = await editCompanyPeriod(
                form.id,
                parseFloat(amount),
                form.day,
                form.status,
                form.time.length ? form.time[0] : undefined,
                form.time.length > 1 ? form.time[1] : undefined,
                form.longEffective
              )
            } else {
              data = await editCompanyPeriod(
                form.id,
                parseFloat(amount),
                form.day,
                form.status,
                form.singleTime,
                '',
                form.longEffective
              )
            }
            
          } else {
            if (form.longEffective === 0) {
              data = await createCompanyPeriod(
                form.name,
                form.company,
                parseFloat(amount),
                form.day,
                form.status,
                form.time.length ? form.time[0] : undefined,
                form.time.length > 1 ? form.time[1] : undefined,
                form.longEffective
              )
            } else {
              data = await createCompanyPeriod(
                form.name,
                form.company,
                parseFloat(amount),
                form.day,
                form.status,
                form.singleTime,
                '',
                form.longEffective
              )
            }
          }
          this.$common.hideLoad()
          if (data && data.result) {
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
      if (type === 1) {
        this.isHk = false
      } else {
        this.isHk = true
      }
      this.showDialog1 = true
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'paymentDaysAccountService',
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
