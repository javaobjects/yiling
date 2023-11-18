<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box order-summary-box">
        <div class="summary-l">
          <div>B2B订单返利信息</div>
          <div class="mar-t-16 mar-l-8">
            <div class="order-summary-item"><span class="item-title">企业名称：</span>{{ bussinessName }}</div>
            <div class="order-summary-item"><span class="item-title">返利状态：</span>{{ status | dictLabel(dataTableReportStatus) }}</div>
            <div class="order-summary-item"><span class="item-title">操作时间：</span>{{ updateTime | formatDate }}</div>
          </div>
        </div>
        <div class="summary-r">
          <div>日志信息</div>
          <el-row class="mar-t-16 mar-l-8">
            <el-col :span="24">
              <div class="box-text">
                <yl-time-line :data="orderLogInfo"></yl-time-line>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 搜索模块 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">客户名称</div>
              <el-select
                v-model="query.eid"
                clearable
                filterable
                remote
                :remote-method="remoteMethod"
                @clear="clearEnterprise"
                :loading="searchLoading"
                placeholder="请搜索并选择客户名称"
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
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
              >
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">区域查询</div>
              <yl-choose-address width="230px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all/>
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="8">
              <div class="title">标识状态</div>
              <el-select placeholder="请选择标识状态" v-model="query.identificationStatus">
                <el-option v-for="item in reportOrderIdent" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">返利状态</div>
              <el-select placeholder="请选择返利状态" v-model="query.rebateStatus">
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
      <!-- 标志返利操作模块 -->
      <div class="down-box" v-if="$route.params.status == 3">
        <div></div>
        <div class="btn">
          <yl-button type="primary" v-role-btn="['1']" v-if="$route.params.status == 3" :disabled="dataList.length === 0" plain @click="changeRebate">标识返利</yl-button>
        </div>
      </div>
      <div class="mar-t-8">
        <yl-table
          border
          row-key="id"
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
          :selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" align="center" width="55" fixed="left"></el-table-column>
          <el-table-column min-width="100" align="center" label="订单编号" prop="orderNo" fixed="left"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商ID" prop="buyerEid"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商名称" prop="buyerEName"></el-table-column>
          <el-table-column min-width="100" align="center" label="ERP客户名称" prop="erpCustomerName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在省" prop="provinceName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在市" prop="cityName"></el-table-column>
          <el-table-column min-width="100" align="center" label="采购商所在区" prop="regionName"></el-table-column>
          <el-table-column min-width="100" align="center" label="供应商ID" prop="sellerEid"></el-table-column>
          <el-table-column min-width="100" align="center" label="供应商名称" prop="sellerEName"></el-table-column>
          <el-table-column min-width="100" align="center" label="下单时间" prop="orderCreateTime">
            <template slot-scope="{ row }">
              <div>{{ row.orderCreateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="签收时间" prop="receiveTime">
            <template slot-scope="{ row }">
              <div>{{ row.receiveTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="订单状态" prop="orderStatus">
            <template slot-scope="{ row }">
              <!-- 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -->
              <div>{{ row.orderStatus | dictLabel(orderStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="支付方式" prop="paymentMethod">
            <template slot-scope="{ row }">
              <!-- 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付 -->
              <div>{{ row.paymentMethod | dictLabel(paymentMethod) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="支付状态" prop="paymentStatus">
            <template slot-scope="{ row }">
              <!-- 支付状态：1-待支付 2-已支付	 -->
              <div>{{ row.paymentStatus | dictLabel(orderPayStatus) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="商品ID" prop="goodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品类型" prop="goodsType"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品内码" prop="goodsErpCode"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
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
          <el-table-column min-width="100" align="center" label="商销价" prop="merchantSalePrice"></el-table-column>
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
          <el-table-column min-width="100" align="center" label="销售额返利金额" prop="salesAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="购进渠道" prop="purchaseChannel">
            <template slot-scope="{ row }">
              <div>{{ row.purchaseChannel | dictLabel(reportPurchaseChannel) }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯活动名称" prop="ladderName"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯数量" prop="thresholdCount"></el-table-column>
          <el-table-column min-width="100" align="center" label="阶梯返利金额" prop="ladderAmount"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品阶梯周期">
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
          <el-table-column min-width="100" align="center" label="小三元基础奖励" prop="xsyAmount"></el-table-column>
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
          <el-table-column min-width="100" align="center" label="返利状态" prop="rebateStatus">
            <template slot-scope="{ row }">
              <div>{{ row.rebateStatus | dictLabel(reportRebateStatus) }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 标识返利弹窗 -->
      <yl-dialog
        width="480px"
        title="修改返利状态"
        :visible.sync="rebateDialogShow"
        :show-cancle="false"
        @confirm="confirmChangeRebate"
      >
        <div class="dialog-content">
          <div class="rebate-box">
            <div class="rebate-item">商业名称：{{ dataList.length > 0 ? dataList[0].buyerEName : '' }}</div>
            <div class="rebate-item">订单：{{ multipleSelection.length > 0 ? selectOrderNum : orderCount }}个订单</div>
            <div class="rebate-item">商品条数：{{ multipleSelection.length > 0 ? multipleSelection.length : goodsCount }}条商品</div>
            <div class="rebate-item">返利状态：
              <el-select placeholder="请选择返利状态" v-model="rebateStatus">
                <el-option label="已返利" :value="0"></el-option>
              </el-select>
            </div>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { orderStatus, paymentMethod, orderPayStatus, dataTableReportStatus } from '@/subject/admin/utils/busi';
import { queryB2bPageList, queryEnterpriseList, requestChangeRebate } from '@/subject/admin/api/zt_api/dataReport';
import ylTimeLine from '@/common/components/TimeLine'
import { formatDate } from '@/subject/admin/utils'
import { reportType } from '@/subject/admin/busi/zt/dataReport'
import { ylChooseAddress } from '@/subject/admin/components'
import { syncPurChannel, reportPurchaseChannel, reportRebateStatus, reportOrderIdent, reportAbnormalReason } from '@/subject/admin/busi/zt/dataReport'

export default {
  name: 'SummaryDetailReportB2b',
  components: {
    ylTimeLine,
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
    dataTableReportStatus() {
      return dataTableReportStatus()
    },
    // 状态
    reportType() {
      return reportType()
    },
    // 动销渠道
    syncPurChannel() {
      return syncPurChannel()
    },
    // 购进渠道
    reportPurchaseChannel() {
      return reportPurchaseChannel()
    },
    // 返利状态
    reportRebateStatus() {
      return reportRebateStatus()
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
        current: 1,
        size: 10,
        eid: '',
        // 	标识状态：0-全部 1-正常订单,2-无效订单,3-异常订单
        identificationStatus: '0',
        // 返利状态：0-全部 1-待返利 2-已返利 3-部分返利
        rebateStatus: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        startCreateOrderTime: '',
        endCreateOrderTime: ''
      },
      loading: false,
      bussinessName: '',
      status: '',
      updateTime: '',
      dataList: [],
      orderLogInfo: [],
      // 查看详情携带过去
      reportId: '',
      // 列表勾选的数据
      multipleSelection: [],
      rebateDialogShow: false,
      // 批量修改标识返利状态
      rebateStatus: 0,
      searchLoading: false,
      // 客户名称搜索下拉的数据
      sellerEnameOptions: [],
      // 标识操作弹窗商品数量
      goodsCount: 0,
      // 标识操作弹窗订单数量
      orderCount: 0,
      // 勾选列表的去重订单数量合计
      selectOrderNum: 0
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryB2bPageList(
        query.current,
        query.size,
        this.$route.params.id,
        query.eid,
        query.identificationStatus,
        query.rebateStatus,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.time && query.time.length ? query.time[0] : undefined,
        query.time && query.time.length > 1 ? query.time[1] : undefined
      );
      this.loading = false;
      if (data) {
        this.bussinessName = data.ename
        this.status = data.status
        this.updateTime = data.updateTime
        this.reportId = data.reportId
        this.dataList = data.records;
        this.orderLogInfo = data.logList
        this.orderLogInfo.map(item => {
          let text = ''
          // 日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利
          if (item.type == 1) {
            text = '提交返利'
          } else if (item.type == 2) {
            text = '运营确认'
          } else if (item.type == 3) {
            text = '运营驳回'
          } else if (item.type == 4) {
            text = '财务确认'
          } else if (item.type == 5) {
            text = '财务驳回'
          } else if (item.type == 6) {
            text = '调整金额'
          } else if (item.type == 7) {
            text = '修改B2B订单标识'
          } else if (item.type == 8) {
            text = '修改流向订单标识'
          } else if (item.type == 9) {
            text = '管理员驳回'
          } else if (item.type == 10) {
            text = '报表返利'
          }
          item.info = formatDate(item.createTime) + '' + text
          return item
        })
        this.query.total = data.total;
        this.goodsCount = data.goodsCount;
        this.orderCount = data.orderCount;
      }
    },
    handleSearch() {
      this.query.current = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        identificationStatus: '0',
        rebateStatus: 0,
        time: []
      };
    },
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    changeRebate() {
      this.rebateDialogShow = true
      if (this.multipleSelection.length > 0) {
        // 勾选订单数据去重后的数量
        this.selectOrderNum = [...new Set(this.multipleSelection.map(item => item.orderNo))].length
      }
    },
    // 批量修改标识返利状态
    async confirmChangeRebate() {
      this.$common.showLoad()
      let data = await requestChangeRebate(Number(this.$route.params.id), this.multipleSelection.map(item => item.id))
      this.$common.hideLoad()
      if (data !== undefined) {
        this.rebateDialogShow = false
        this.getList()
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
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
