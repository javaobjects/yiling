<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商业名称</div>
              <el-select v-model="query.eid"
              clearable
              filterable
              remote
              :remote-method="remoteMethod"
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
              <div class="title">下单时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">区域查询</div>
              <yl-choose-address width="230px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">操作状态</div>
              <el-select v-model="query.rebateStatus" placeholder="请选择操作状态">
                <el-option v-for="item in orderRewardStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="16">
              <div class="title">订单来源</div>
              <el-checkbox-group v-model="query.source">
                <el-checkbox class="option-class" v-for="item in erpSaleFlowSource" :label="item.value" :key="item.value">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">标识状态</div>
              <el-select v-model="query.identificationStatus" placeholder="请选择标识状态">
                <el-option v-for="item in reportOrderIdent" :key="item.value" :label="item.label" :value="item.value"></el-option>
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
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_16' | template">下载导入模板</el-link>
          <yl-button type="primary" v-role-btn="['1']" plain :disabled="dataList.length === 0" @click="changeRebate">修改标识状态</yl-button>
          <yl-button type="primary" v-role-btn="['2']" plain @click="goImport">导入修改标识状态</yl-button>
          <yl-button type="primary" v-role-btn="['3']" plain @click="downLoadTemp">导出查询结果</yl-button>
          <yl-button type="primary" plain @click="add">批量生成返利</yl-button>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table
          border
          row-key="id"
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
          :selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="center" width="55" fixed="left"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业ID" prop="eid" fixed="left"></el-table-column>
          <el-table-column min-width="100" align="center" label="商业名称" prop="ename"></el-table-column>
          <el-table-column min-width="100" align="center" label="销售订单号" prop="soNo"></el-table-column>
          <el-table-column min-width="100" align="center" label="订单来源" prop="soSourceStr"></el-table-column>
          <el-table-column min-width="100" align="center" label="销售日期" prop="soTime">
            <template slot-scope="{ row }">
              {{ row.soTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="规格" prop="soSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="批号" prop="soBatchNo"></el-table-column>
          <el-table-column min-width="100" align="center" label="客户编码" prop="enterpriseInnerCode"></el-table-column>
          <el-table-column min-width="100" align="center" label="客户名称" prop="enterpriseName"></el-table-column>
          <el-table-column min-width="100" align="center" label="数量" prop="soQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="单位" prop="soUnit"></el-table-column>
          <el-table-column min-width="100" align="center" label="单价" prop="soPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="金额" prop="soTotalAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="生产厂家" prop="soManufacturer"></el-table-column>
          <el-table-column min-width="100" align="center" label="生产日期" prop="soProductTime">
            <template slot-scope="{ row }">
              {{ row.soProductTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="有效日期" prop="soEffectiveTime">
            <template slot-scope="{ row }">
              {{ row.soEffectiveTime | formatDate }}
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsInSn"></el-table-column>
          <el-table-column min-width="100" align="center" label="批准文号" prop="soLicense"></el-table-column>
          <el-table-column min-width="100" align="center" label="省" prop="provinceName"></el-table-column>
          <el-table-column min-width="100" align="center" label="市" prop="cityName"></el-table-column>
          <el-table-column min-width="100" align="center" label="区" prop="regionName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品编码" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品名称" prop="ylGoodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="以岭品规格" prop="ylSpecifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="供货价" prop="supplyPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="出货价" prop="outPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="动销渠道" prop="syncPurChannel">
            <template slot-scope="{ row }">
              <div>{{ row.syncPurChannel | dictLabel(syncPurChannel) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯活动名称" prop="ladderName"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯数量" prop="thresholdCount"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯返利金额" prop="ladderAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯返利周期">
            <template slot-scope="{ row }">
              <div>
                {{ row.ladderStartTime | formatDate }} ~ {{ row.ladderEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="小三员活动名称" prop="xsyName"></el-table-column>
          <el-table-column min-width="100" align="center" label="小三元基础奖励单价">
            <template slot-scope="{ row }">
              <span>{{ row.xsyPrice }}</span>
              <!-- 小三员奖励类型：1-金额 2-百分比 -->
              <span v-if="row.xsyRewardType === 2">%</span>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="小三员基础奖励" prop="xsyAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="小三元活动周期">
            <template slot-scope="{ row }">
              <div>
                {{ row.xsyStartTime | formatDate }} ~ {{ row.xsyEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动1名称" prop="actFirstName"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动1金额" prop="actFirstAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动1周期" prop="actFirstStartTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.actFirstStartTime | formatDate }} ~ {{ row.actFirstEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动2名称" prop="actSecondName"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动2金额" prop="actSecondAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动2周期" prop="actSecondStartTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.actSecondStartTime | formatDate }} ~ {{ row.actSecondEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动3名称" prop="actThirdName"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动3金额" prop="actThirdAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动3周期" prop="actThirdStartTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.actThirdStartTime | formatDate }} ~ {{ row.actThirdEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动4名称" prop="actFourthName"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动4金额" prop="actFourthAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动4周期" prop="actFourthStartTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.actFourthStartTime | formatDate }} ~ {{ row.actFourthEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动5名称" prop="actFifthName"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动5金额" prop="actFifthAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="特殊活动5周期" prop="actFifthStartTime">
            <template slot-scope="{ row }">
              <div>
                {{ row.actFifthStartTime | formatDate }} ~ {{ row.actFifthEndTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作状态" prop="reportStatus">
            <template slot-scope="{ row }">
              <div>{{ row.reportStatus | dictLabel(orderRewardStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="标识状态" prop="identificationStatus">
            <template slot-scope="{ row }">
              <div>{{ row.identificationStatus | dictLabel(reportOrderIdent) }}</div>
              <div v-if="row.identificationStatus == 3 && row.abnormalReason !== 5">({{ row.abnormalReason | dictLabel(reportAbnormalReason) }})</div>
              <div v-if="row.identificationStatus == 3 && row.abnormalReason == 5">({{ row.abnormalDescribed }})</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 标识返利弹窗 -->
      <yl-dialog
        width="480px"
        title="修改标识状态"
        :show-cancle="false"
        :visible.sync="rebateDialogShow"
        @confirm="confirmChangeRebate"
        @close="closeRebateDialog"
      >
        <div class="dialog-content">
          <div class="rebate-box">
            <div class="rebate-item">商业名称：{{ dataList.length > 0 ? dataList[0].ename : '' }}</div>
            <div class="rebate-item">订单：{{ multipleSelection.length > 0 ? selectOrderNum : orderCount }}个订单</div>
            <div class="rebate-item">商品条数：{{ multipleSelection.length > 0 ? multipleSelection.length : goodsCount }}条商品</div>
            <el-form
              label-width="80px"
              label-position="left"
              class="idenStatusForm"
              ref="idenStatusFormRef"
              :rules="idenStatusFormRule"
              :model="idenStatusForm"
            >
              <el-form-item label="标识状态" prop="updateIdenStatus">
                <el-select placeholder="请选择标识状态" v-model="idenStatusForm.updateIdenStatus">
                  <el-option
                    v-for="item in reportOrderIdent"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    v-show="item.value != '0'"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="异常情况" prop="abnormalReason" v-if="idenStatusForm.updateIdenStatus === 3">
                <el-select placeholder="请选择异常情况" v-model="idenStatusForm.abnormalReason">
                  <el-option
                    v-for="item in reportAbnormalReason"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="异常描述" prop="abnormalDescribed" v-if="idenStatusForm.updateIdenStatus === 3 && idenStatusForm.abnormalReason === 5">
                <el-input v-model="idenStatusForm.abnormalDescribed" placeholder="请输入异常描述" clearable></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <template slot="left-btn">
          <yl-button plain @click="closeRebateDialog">取消</yl-button>
        </template>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { ylChooseAddress } from '@/subject/admin/components'
import { erpSaleFlowSource } from '@/subject/admin/utils/busi';
import {
  getqueryFlowOrderRebateReportPage,
  calculateQueryFlowOrderRebateReportPage,
  queryEnterpriseList,
  updateFlowIdentification,
  updateProportionIdentification
} from '@/subject/admin/api/zt_api/dataReport';
import { syncPurChannel } from '@/subject/admin/busi/zt/dataReport'
import { orderRewardStatus, reportOrderIdent, reportAbnormalReason } from '@/subject/admin/busi/zt/dataReport'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'ZtDataReportFlowTable',
  components: {
    ylChooseAddress
  },
  computed: {
    erpSaleFlowSource() {
      return erpSaleFlowSource().filter(item => item.value != 1)
    },
    orderRewardStatus() {
      return orderRewardStatus()
    },
    // 动销渠道
    syncPurChannel() {
      return syncPurChannel()
    },
    // 标识状态
    reportOrderIdent() {
      return reportOrderIdent()
    },
    // 异常情况
    reportAbnormalReason() {
      return reportAbnormalReason()
    }
  },
  data() {
    return {
      query: {
        total: 0,
        page: 1,
        limit: 10,
        // 订单来源
        source: [],
        // 返利状态
        rebateStatus: -1,
        eid: '',
        // 标识状态：0-全部 1-正常订单 2-无效订单 3-异常订单
        identificationStatus: '0'
      },
      loading: false,
      dataList: [],
      searchLoading: false,
      sellerEnameOptions: [],
      rebateDialogShow: false,
      // 列表勾选
      multipleSelection: [],
      // 标识操作弹窗商品数量
      goodsCount: 0,
      // 标识操作弹窗订单数量
      orderCount: 0,
      // 勾选列表的去重订单数量合计
      selectOrderNum: 0,
      // 标识状态修改
      idenStatusForm: {
        // 全部修改标识状态 1-正常订单,2-无效订单,3-异常订单
        updateIdenStatus: '',
        // 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
        abnormalReason: '',
        // 异常描述
        abnormalDescribed: ''
      },
      idenStatusFormRule: {
        updateIdenStatus: [{ required: true, message: '请选择标识状态', trigger: ['change', 'blur'] }],
        abnormalReason: [{ required: true, message: '请选择异常情况', trigger: ['change', 'blur'] }],
        abnormalDescribed: [{ required: true, message: '请输入异常描述', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    // this.getList();
  },
  methods: {
    async getList() {
      if (!this.query.eid && !this.query.provinceCode) {
        return this.$common.warn('请设置商业名称或选择区域')
      }
      if (!this.query.time || this.query.time.length == 0 ) {
        return this.$common.warn('请选择下单时间')
      }
      console.log(this.query.source);
      let query = this.query;
      this.loading = true;
      let data = await getqueryFlowOrderRebateReportPage(
        query.page,
        query.limit,
        query.eid,
        query.rebateStatus,
        query.source,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null,
        query.identificationStatus
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
        this.goodsCount = data.goodsCount;
        this.orderCount = data.orderCount;
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
        rebateStatus: -1,
        time: [],
        source: []
      };
    },
    // 批量生成返利
    async add() {
      let query = this.query
      this.$common.showLoad()
      let data = await calculateQueryFlowOrderRebateReportPage(
        query.page,
        query.limit,
        query.eid,
        query.rebateStatus,
        query.source,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null,
        Number(query.identificationStatus)
      )
      this.$common.hideLoad()
      if (data != undefined) {
        this.$common.success('批量生成返利成功');
      }
    },
    async remoteMethod(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList( 1, 10, query )
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
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 修改标识返利
    changeRebate() {
      this.rebateDialogShow = true
      if (this.multipleSelection.length > 0) {
        // 勾选订单数据去重后的数量
        this.selectOrderNum = [...new Set(this.multipleSelection.map(item => item.soNo))].length
      }
    },
    // 批量修改标识返利状态
    async confirmChangeRebate() {
      if (this.multipleSelection.length == 0) {
        // 全部修改
        this.$refs.idenStatusFormRef.validate(async valid => {
          if (valid) {
            let query = this.query
            this.$common.showLoad()
            let data = await updateFlowIdentification(
              query.eid,
              query.time && query.time.length > 0 ? query.time[0] : '',
              query.time && query.time.length > 1 ? query.time[1] : '',
              query.provinceCode,
              query.cityCode,
              query.regionCode,
              query.reportStatus,
              query.identificationStatus,
              // 全部修改标识状态 1-正常订单,2-无效订单,3-异常订单
              this.idenStatusForm.updateIdenStatus,
              this.idenStatusForm.abnormalReason,
              this.idenStatusForm.abnormalDescribed
            )
            this.$common.hideLoad()
            if (data !== undefined) {
              this.closeRebateDialog()
              this.getList()
            }
          }
        })
      } else {
        // 按勾选部分修改
        this.$refs.idenStatusFormRef.validate(async valid => {
          if (valid) {
            this.$common.showLoad()
            let data = await updateProportionIdentification(
              // 类型：1-B2B返利 2-流向返利
              2,
              this.multipleSelection.map(item => item.id),
              // 全部修改标识状态 1-正常订单,2-无效订单,3-异常订单
              this.idenStatusForm.updateIdenStatus,
              this.idenStatusForm.abnormalReason,
              this.idenStatusForm.abnormalDescribed
            )
            this.$common.hideLoad()
            if (data !== undefined) {
              this.closeRebateDialog()
              this.getList()
            }
          }
        })
      }
    },
    // 关闭修改返利弹窗
    closeRebateDialog() {
      this.rebateDialogShow = false
      this.$refs.idenStatusFormRef.resetFields()
    },
    // 导入
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/dataCenter/api/v1/report/order/importFlowOrderIdent'
        }
      })
    },
    // 导出
    async downLoadTemp() {
      const idList = this.multipleSelection.map(item => item.id).join(',')
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'reportFlowOrderSyncInfoExportServiceImpl',
        fileName: '导出流向返利',
        groupName: '数据报表',
        menuName: '数据报表-流向返利',
        searchConditionList: [
          {
            desc: '选中',
            name: 'idList',
            value: idList
          },
          {
            desc: '供应商id',
            name: 'eid',
            value: query.eid || ''
          },
          {
            desc: '操作状态',
            name: 'reportStatus',
            value: query.reportStatus || ''
          },
          {
            desc: '标识状态',
            name: 'identificationStatus',
            value: query.identificationStatus || ''
          },
          {
            desc: '订单来源',
            name: 'soSourceList',
            value: query.source.join(',') || ''
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
            desc: '开始下单时间',
            name: 'startSoTime',
            value: query.time && query.time.length > 0 ? query.time[0] : ''
          },
          {
            desc: '结束下单时间',
            name: 'endSoTime',
            value: query.time && query.time.length > 1 ? query.time[1] : ''
          }
        ]
      })
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
