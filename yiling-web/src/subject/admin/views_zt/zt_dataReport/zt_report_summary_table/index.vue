<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-select
              v-model="query.eid"
              clearable
              filterable
              remote
              :remote-method="remoteMethod"
              @change="changeEnterprise"
              @clear="clearEnterprise"
              :loading="searchLoading"
              placeholder="请搜索并选择商业">
                <el-option
                  v-for="item in sellerEnameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">创建时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                @change="changeTime">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">区域查询</div>
              <yl-choose-address width="230px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">返利类型</div>
              <el-select placeholder="请选择返利类型" v-model="query.type" @change="changeType">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in reportType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">状态</div>
              <el-checkbox-group v-model="query.status" @change="changeStatus">
                <el-checkbox
                  v-for="item in dataTableReportStatus"
                  :label="item.value"
                  :key="item.value"
                >{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
            <el-col :span="8">
              <div class="title">返利状态</div>
              <el-select placeholder="请选择返利状态" v-model="query.rebateStatus" @change="changeType">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in reportRebateStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div></div>
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_15' | template">下载更新返利模板</el-link>
          <yl-button type="primary" v-role-btn="['6']" plain @click="goImport">导入更新返利状态</yl-button>
          <yl-button type="primary" plain @click="downLoadTemp">导出查询结果</yl-button>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table
          ref="summaryTableRef"
          border
          :show-header="true"
          row-key="id"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
          :selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="center" width="55"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid"></el-table-column>
          <el-table-column min-width="100" align="center" label="返利类型" prop="type">
            <template slot-scope="{ row }">
              <!-- ：1-B2B返利 2-流向返利 -->
              <div>{{ row.type == 1 ? "B2B返利":"流向返利" }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="会员返利" prop="memberAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="销售额返利" prop="salesAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="终端促销返利" prop="terminalSalesAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯返利金额" prop="ladderAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="小三员基础奖励" prop="xsyAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动1金额" prop="actFirstAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动2金额" prop="actSecondAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动3金额" prop="actThirdAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动4金额" prop="actFourthAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动5金额" prop="actFifthAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="调整金额" prop="adjustAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="调整原因" prop="adjustReason"></el-table-column>
          <el-table-column min-width="100" align="center" label="合计" prop="totalAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="创建时间" prop="createTime">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="最后操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column min-width="100" align="center" label="状态" prop="status">
            <template slot-scope="{ row }">
              <div>{{ row.status | dictLabel(dataTableReportStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="驳回原因" prop="rejectReason"></el-table-column>
          <el-table-column min-width="100" align="center" label="订单返利状态" prop="rebateStatus">
            <template slot-scope="{ row }">
              <div>{{ row.rebateStatus | dictLabel(reportRebateStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="订单返利金额" prop="rebateAmount"></el-table-column>
          <el-table-column min-width="180" fixed="right" align="center" label="操作">
            <template slot-scope="{ row }">
              <!-- 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 -->
              <div><yl-button v-role-btn="['1']" v-if="row.status == 1" type="text" @click="operateFinanceEdit(row, 1)">运营确认</yl-button></div>
              <div><yl-button v-role-btn="['2']" v-if="row.status == 2" type="text" @click="operateFinanceEdit(row, 2)">财务确认</yl-button> </div>
              <div><yl-button v-role-btn="['3']" v-if="row.status == 1" type="text" @click="edit(row)">调整</yl-button></div>
              <div><yl-button v-role-btn="['4']" type="text" @click="detail(row)">查看详情</yl-button></div>
              <div><yl-button v-role-btn="['5']" v-if="row.status == 3" type="text" @click="operateFinanceEdit(row, 3)">管理员驳回</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 返利调整 -->
      <yl-dialog :visible.sync="show" width="1152px" title="返利调整" @confirm="confirm">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业名称：">
                  <el-input v-model="form.businessName" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业ID：">
                  <el-input v-model="form.businessId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="调整原因：" prop="reason">
                  <el-input v-model="form.reason" placeholder="请输入内容/40字"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" :offset="0">
                <el-form-item label="调整金额：" prop="price">
                  <el-input v-model="form.price" placeholder="请输入内容"></el-input>
                  <span class="mar-l-8">调整的金额，为数字，支持小数点后2位</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 运营财务确认 -->
      <yl-dialog
        :visible.sync="show1"
        width="1152px"
        :title="diaTitle"
        :show-cancle="false"
        :show-confirm="isSure !== 3"
        @confirm="submit('form1')"
        @close="closeDialog"
      >
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form1" :model="form1" :rules="formRules1" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业名称：">
                  <el-input v-model="form1.businessName" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商业ID：">
                  <el-input v-model="form1.businessId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="pad-l-100">
              <!-- 运营确认 -->
              <div v-if="isSure == 1">商业总返利金额为{{ form1.totalAmount }}元，确认后对应单据将提交财务进行确认；驳回后需要重新提交返利</div>
              <!-- 财务确认 -->
              <div v-if="isSure == 2 || isSure == 3">商业总返利金额为{{ form1.totalAmount }}元，确认后对应单据将标识为已返利；驳回后需要重新提交返利</div>
              <div class="mar-t-16" v-if="hasRefund">商业推广会员存在退款情况，请在确认后再进行操作</div>
            </el-row>
            <el-row class="mar-t-16">
              <el-col :span="12" :offset="0">
                <el-form-item label="驳回原因：" prop="reason">
                  <el-input v-model="form1.reason" placeholder="请输入驳回原因"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <template slot="left-btn">
          <yl-button :type="isSure == 3 ? 'primary' : ''" @click="onReject('form1')">驳回</yl-button>
        </template>
        <template slot="right-btn" v-if="isSure == 3">
          <yl-button @click="closeDialog">取消</yl-button>
        </template>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { ylChooseAddress } from '@/subject/admin/components'
import { orderStatus, paymentMethod, orderPayStatus, dataTableReportStatus } from '@/subject/admin/utils/busi';
import { getSummaryTableList, updateSummaryTableAmount, confirmSummaryTableAmount, rejectSummaryTableAmount, queryIsRefund, queryEnterpriseList } from '@/subject/admin/api/zt_api/dataReport';
import { createDownLoad } from '@/subject/admin/api/common'
import { reportType, reportRebateStatus } from '@/subject/admin/busi/zt/dataReport'

export default {
  name: 'ZtDataReportSummaryTable',
  components: {
    ylChooseAddress
  },
  computed: {
    orderStatus() {
      return orderStatus()
    },
    paymentMethod() {
      return paymentMethod()
    },
    orderPayStatus() {
      return orderPayStatus()
    },
    // 状态
    reportType() {
      return reportType()
    },
    // 返利类型
    dataTableReportStatus() {
      return dataTableReportStatus()
    },
    // 返利类型
    reportRebateStatus() {
      return reportRebateStatus()
    }
  },
  watch: {
    'query.provinceCode': {
        handler() {
          if (this.$refs.summaryTableRef !== undefined) {
            this.clearSelect()
          }
        },
        immediate: true,
        deep: true
    },
    'query.cityCode': {
        handler() {
          if (this.$refs.summaryTableRef !== undefined) {
            this.clearSelect()
          }
        },
        immediate: true,
        deep: true
    },
    'query.regionCode': {
        handler() {
          if (this.$refs.summaryTableRef !== undefined) {
            this.clearSelect()
          }
        },
        immediate: true,
        deep: true
    }
  },
  data() {
    const validatePrice = (rule, value, callback) => {
      let reg = /(^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d{1,2})?$)/
      if (!value) {
        callback(new Error('金额不能为空'))
      } else if (!reg.test(value)) {
        callback(new Error('请输入正确的格式，最多保留2位小数'))
      } else if (value.length > 10) {
        callback(new Error('最多可输入10个字符'))
      } else {
        callback();
      }
    }
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        // 返利类型
        type: 0,
        // 状态
        status: [],
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        // 订单返利状态：1-待返利 2-已返利 3-部分返利
        rebateStatus: 0
      },
      loading: false,
      dataList: [],
      show: false,
      form: {
        id: '',
        businessName: '',
        businessId: '',
        reason: '',
        price: ''
      },
      formRules: {
        reason: [
          { required: true, message: '请输入', trigger: 'blur' },
          { min: 1, max: 40, message: '长度在40字以内', trigger: 'blur' }
        ],
        price: [
          { required: true, validator: validatePrice, trigger: 'blur' }
        ]
      },
      show1: false,
      diaTitle: '',

      form1: {
        id: '',
        businessName: '',
        businessId: '',
        reason: '',
        totalAmount: ''
      },
      formRules1: {
        reason: [
          { required: true, message: '请输入驳回原因', trigger: 'blur' }
        ]
      },
      // 1 运营确认 2 财务确认
      isSure: 1,
      reject: false,
      hasRefund: false, // 是否有退款
      searchLoading: false,
      sellerEnameOptions: [],
      // 列表多选选中的数据
      multipleSelection: []
    };
  },
  activated() {
    this.getList();
  },
  methods: {
    async getList() {
      const query = this.query;
      this.loading = true;
      const data = await getSummaryTableList(
        query.page,
        query.limit,
        query.eid,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.type,
        query.status,
        query.rebateStatus
      );
      this.loading = false;
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        time: [],
        type: 0,
        status: [],
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
      this.sellerEnameOptions = []
    },
    // 调整
    edit(row) {
      this.form.id = row.id;
      this.form.businessName = row.ename
      this.form.businessId = row.eid
      this.form.price = ''
      this.form.reason = ''
      this.show = true
    },
    // 返利调整
    confirm() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await updateSummaryTableAmount(
            this.form.id,
            this.form.price,
            this.form.reason
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.getList();
            this.show = false
            this.$common.success('调整成功');
          }
        } else {
          return false;
        }
      });
    },
    // 运营编辑 财务编辑 管理员驳回
    async operateFinanceEdit(row, isSureType) {
      let diaTitle
      switch (isSureType) {
        case 1:
          diaTitle = '返利运营确认'
          break;
        case 2:
          diaTitle = '返利财务确认'
          break;
        case 3:
          diaTitle = '管理员驳回'
          break;
        default:
          diaTitle = ''
          break;
      }
      // B2B类型返利,运营/财务确认操作时弹窗打开后判断是否有退款,有则提示
      if (row.type == 1) {
        let data = await queryIsRefund(row.id)
        if (data == true) {
          // 提示
          this.$common.confirm('商业推广会员存在退款情况，是否继续', confirm => {
            if (confirm) {
              this.hasRefund = true
              this.showDialog(row, isSureType, diaTitle)
            } else {
              this.hasRefund = false
              return false
            }
          })
        } else {
          this.hasRefund = false
          this.showDialog(row, isSureType, diaTitle)
        }
      } else {
        this.showDialog(row, isSureType, diaTitle)
      }
    },
    // 展示运营/财务确认/管理员驳回弹窗
    showDialog(row, isSure, diaTitle) {
      this.form1.id = row.id
      this.form1.businessName = row.ename
      this.form1.businessId = row.eid
      this.form1.totalAmount = row.totalAmount
      this.form1.reason = ''
      this.isSure = isSure
      this.diaTitle = diaTitle
      this.show1 = true
    },
    // 报表确认
    async submit() {
      this.$common.showLoad();
      let data = await confirmSummaryTableAmount(
        this.form1.id,
        // 确认类型 1-运营确认 2-财务确认
        this.isSure
      );
      this.$common.hideLoad();
      if (data) {
        this.show1 = false
        this.getList();
        this.$common.success('确认成功')
      }
    },
    // 报表驳回
    onReject() {
      this.$refs['form1'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await rejectSummaryTableAmount(
            this.form1.id,
            // 	驳回类型 1-运营驳回 2-财务驳回 3-管理员驳回
            this.isSure,
            this.form1.reason
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.show1 = false
            this.getList();
            this.$common.success('驳回成功')
          }
        } else {
          return false;
        }
      });
    },
    detail(e) {
      //1-B2B返利 2-流向返利
      if (e.type == 1) {
        this.$router.push({
          name: 'SummaryDetailReportB2b',
          params: { id: e.id, status: e.status }
        })
      } else {
        this.$router.push({
          name: 'SummaryDetailReportFlow',
          params: { id: e.id, status: e.status }
        })
      }

    },
    closeDialog() {
      this.show1 = false
      this.hasRefund = false
    },
    async downLoadTemp() {
      const reportIdList = this.multipleSelection.map(item => item.id).join(',')
      const query = this.query
      const reportStatusList = query.status.join(',')
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'reportExportServiceImpl',
        fileName: '导出返利报表',
        groupName: '数据报表',
        menuName: '数据报表-返利总表',
        searchConditionList: [
          {
            desc: '选择导出',
            name: 'reportIdList',
            value: reportIdList
          },
          {
            desc: '返利类型',
            name: 'type',
            value: query.type || ''
          },
          {
            desc: '状态',
            name: 'reportStatusList',
            value: reportStatusList
          },
          {
            desc: '返利状态',
            name: 'rebateStatus',
            value: query.rebateStatus || ''
          },
          {
            desc: '商业名称',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '省code',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '市code',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区code',
            name: 'regionCode',
            value: query.regionCode || ''
          },
          {
            desc: '开始创建时间',
            name: 'startCreateTime',
            value: query.time && query.time.length ? query.time[0] : ''
          },
          {
            desc: '结束创建时间',
            name: 'endCreateTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    async remoteMethod(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList( 1, 10, query.trim() )
        this.searchLoading = false
        if (data) {
          this.sellerEnameOptions = data.records
        }
      } else {
        this.sellerEnameOptions = []
      }
    },
    clearEnterprise() {
      this.sellerEnameOptions = []
    },
    // 改变选择的商业
    changeEnterprise() {
      this.clearSelect()
    },
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 清除勾选
    clearSelect() {
      this.$nextTick(() => {
        this.$refs.summaryTableRef.$refs.table.clearSelection()
      })
    },
    // 修改时间选择
    changeTime() {
      this.clearSelect()
    },
    // 修改返利类型选择
    changeType() {
      this.clearSelect()
    },
    // 修改状态选择
    changeStatus() {
      this.clearSelect()
    },
    // 导入
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/report/importRebate'
        }
      })
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
