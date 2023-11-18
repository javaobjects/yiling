<!--
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:07:11
 * @LastEditTime: 2022-02-28 16:35:14
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">供应商名称</div>
              <el-select
                v-model="query.eid"
                clearable
                filterable
                remote
                :remote-method="remoteMethod"
                @clear="clearEnterprise"
                :loading="searchLoading"
                placeholder="请搜索并选择供应商"
              >
                <el-option
                  v-for="item in sellerEnameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">签收时间</div>
              <el-date-picker v-model="query.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" :picker-options="pickerOptions">
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
              <el-select v-model="query.reportStatus" placeholder="请选择操作状态">
                <el-option v-for="item in orderRewardStatus" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
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
        <div>
        </div>
        <div class="btn">
          <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_16' | template">下载导入模板</el-link>
          <yl-button type="primary" v-role-btn="['1']" plain :disabled="dataList.length === 0" @click="changeRebate">修改标识状态</yl-button>
          <yl-button type="primary" v-role-btn="['2']" plain @click="goImport">导入修改标识状态</yl-button>
          <yl-button type="primary" v-role-btn="['3']" plain @click="downLoadTemp">导出查询结果</yl-button>
          <yl-button type="primary" plain @click="add">批量生成返利</yl-button>
        </div>
      </div>
      <div class="mar-t-8">
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
          <el-table-column min-width="100" align="center" label="订单编号" prop="orderNo" fixed="left"></el-table-column>
          <el-table-column min-width="100" align="center" label="订单详情ID" prop="id"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商ID" prop="buyerEid"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商名称" prop="buyerEname"></el-table-column>
          <el-table-column min-width="100" align="center" label="ERP客户名称" prop="erpCustomerName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在省" prop="provinceName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在市" prop="cityName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在区" prop="regionName"></el-table-column>
          <el-table-column min-width="100" align="center" label="供应商ID" prop="sellerEid"></el-table-column>
          <el-table-column min-width="100" align="center" label="供应商名称" prop="sellerEname"></el-table-column>
          <el-table-column min-width="100" align="center" label="下单时间" prop="orderCreateTime">
            <template slot-scope="{ row }">
              <div> {{ row.orderCreateTime | formatDate }} </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="签收时间" prop="receiveTime">
            <template slot-scope="{ row }">
              <div> {{ row.receiveTime | formatDate }} </div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="订单状态" prop="orderStatus">
            <template slot-scope="{ row }">
              <div>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="支付方式" prop="paymentMethod">
            <template slot-scope="{ row }">
              <div>{{ row.paymentMethod | dictLabel(paymentMethod) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="支付状态" prop="paymentStatus">
            <template slot-scope="{ row }">
              <div>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作状态" prop="reportStatus">
            <template slot-scope="{ row }">
              <div>{{ row.reportStatus | dictLabel(orderRewardStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="商品ID" prop="goodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品类型" prop="category"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsErpCode"></el-table-column>
          <el-table-column min-width="100" align="center" label="规格型号" prop="specifications"></el-table-column>
          <el-table-column min-width="100" align="center" label="批准文号" prop="license"></el-table-column>
          <el-table-column min-width="100" align="center" label="批次号/序列号" prop="batchNo"></el-table-column>
          <el-table-column min-width="100" align="center" label="有效期至" prop="expiryDate">
            <template slot-scope="{ row }">
              <div>{{ row.expiryDate | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="下单数量" prop="goodsQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="发货数量" prop="deliveryQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="退货数量" prop="refundQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="收货数量" prop="receiveQuantity"></el-table-column>
          <el-table-column min-width="100" align="center" label="供货价" prop="supplyPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="出货价" prop="outPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="商销价" prop="goodsSellPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品原价" prop="originalPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品单价" prop="goodsPrice"></el-table-column>
          <el-table-column min-width="100" align="center" label="金额小计" prop="goodsAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="批次签收总折扣金额" prop="discountAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="总折扣比率" prop="discountPercentage"></el-table-column>
          <el-table-column min-width="100" align="center" label="支付金额" prop="paymentAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="活动类型" prop="activityType"></el-table-column>
          <el-table-column min-width="100" align="center" label="活动内容" prop="activityDescribe"></el-table-column>
          <el-table-column min-width="100" align="center" label="平台承担折扣比例" prop="platformPercentage"></el-table-column>
          <el-table-column min-width="100" align="center" label="批次签收平台承担折扣金额" prop="platformAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="商家承担折扣比例" prop="shopPercentage"></el-table-column>
          <el-table-column min-width="100" align="center" label="批次签收商家承担折扣金额" prop="shopAmount"></el-table-column>
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
            <div class="rebate-item">商业名称：{{ dataList.length > 0 ? dataList[0].sellerEname : '' }}</div>
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
import {
  getDataTableList,
  calculateQueryB2BOrderRebateReportPage,
  queryEnterpriseList,
  updateB2bIdentification,
  updateProportionIdentification
} from '@/subject/admin/api/zt_api/dataReport';
import { orderStatus, paymentMethod, orderPayStatus } from '@/subject/admin/utils/busi';
import { orderRewardStatus, syncPurChannel, reportOrderIdent, reportAbnormalReason } from '@/subject/admin/busi/zt/dataReport'
import { createDownLoad } from '@/subject/admin/api/common'

export default {
  name: 'ZtDataReportTable',
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
    // 返利状态
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
        eid: '',
        time: [],
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        // 操作状态 -1-全部 0-待返利 4-运营驳回 5-财务驳回 6-管理员驳回
        reportStatus: -1,
        // 标识状态：0-全部 1-正常订单 2-无效订单 3-异常订单
        identificationStatus: '0'
      },
      loading: false,
      dataList: [
      ],
      pickerOptions: { // 时间范围选择不能大于一个月
        onPick: ({ maxDate, minDate }) => {
          this.choiceDate = minDate.getTime()
          if (maxDate) {
            this.choiceDate = ''
          }
        },
        disabledDate: (time) => {
          if (this.choiceDate !== '') {
            const one = 90 * 24 * 3600 * 1000
            const minTime = this.choiceDate - one
            const maxTime = this.choiceDate + one
            return time.getTime() < minTime || time.getTime() > maxTime
          }
          return false
        }
      },
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
    }
  },
  activated() {},
  methods: {
    async getList() {
      if (!this.query.eid && !this.query.provinceCode) {
        return this.$common.warn('请设置商业名称或选择区域')
      }
      if (!this.query.time || this.query.time.length == 0 ) {
        return this.$common.warn('请选择签收时间')
      }
      let query = this.query
      this.loading = true
      let data = await getDataTableList(
        query.page,
        query.limit,
        query.eid,
        query.reportStatus,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.time && query.time.length ? query.time[0] : null,
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
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        eid: '',
        time: [],
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        reportStatus: -1,
        total: 0,
        identificationStatus: '0'
      }
      this.dataList = []
    },
    // 批量生成返利
    async add() {
      this.$common.showLoad()
      let data = await calculateQueryB2BOrderRebateReportPage(
        this.query.page,
        this.query.limit,
        this.query.eid,
        this.query.reportStatus,
        this.query.provinceCode,
        this.query.cityCode,
        this.query.regionCode,
        this.query.time && this.query.time.length > 0 ? this.query.time[0] : null,
        this.query.time && this.query.time.length > 1 ? this.query.time[1] : null,
        Number(this.query.identificationStatus)
      )
      this.$common.hideLoad()
      if (data != undefined) {
        this.$common.success('批量生成返利成功')
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
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 修改标识返利
    changeRebate() {
      this.rebateDialogShow = true
      if (this.multipleSelection.length > 0) {
        // 勾选订单数据去重后的数量
        this.selectOrderNum = [...new Set(this.multipleSelection.map(item => item.orderNo))].length
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
            let data = await updateB2bIdentification(
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
              1,
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
          action: '/dataCenter/api/v1/report/order/importB2bOrderIdent'
        }
      })
    },
    // 导出
    async downLoadTemp() {
      const idList = this.multipleSelection.map(item => item.id).join(',')
      const query = this.query
      this.$common.showLoad()
      const data = await createDownLoad({
        className: 'reportB2bOrderSyncInfoExportServiceImpl',
        fileName: '导出b2b返利',
        groupName: '数据报表',
        menuName: '数据报表-b2b返利',
        searchConditionList: [
          {
            desc: '选择导出',
            name: 'idList',
            value: idList
          },
          {
            desc: '商业eid',
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
            desc: '开始签收时间',
            name: 'startReceiveTime',
            value: query.time && query.time.length > 0 ? query.time[0] : ''
          },
          {
            desc: '结束签收时间',
            name: 'endReceiveTime',
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
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>