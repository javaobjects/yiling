<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <!-- 采购入库列表集合 -->
        <div class="table-box">
          <yl-table ref="table" show-header border stripe :list="dataList">
            <el-table-column label="序号" width="120" align="center" prop="value"></el-table-column>
            <el-table-column label="类别" min-width="120" align="center" prop="name"></el-table-column>
            <el-table-column label="操作" min-width="120" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button type="text" @click="detail(row)">查看</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
        <!-- 采购入库仪表弹窗 -->
        <yl-dialog
          class="chart-dialog"
          width="100%"
          top="0"
          fullscreen
          destroy-on-close
          :visible.sync="purchaseDialogShow"
          :show-footer="false"
          @open="openPurchaseDialog"
          @close="closePurchaseDialog"
        >
          <div class="content">
            <div class="button-box">
              <ylButton type="primary" @click="closePurchaseDialog">关闭</ylButton>
            </div>
            <!-- 搜索选项 -->
            <div class="common-box tool-box mar-t-8">
              <el-row>
                <!-- 按月选择 -->
                <el-col :span="4">
                  <div class="title">采购年月</div>
                  <el-date-picker
                    type="month"
                    format="yyyy/MM"
                    value-format="yyyy-MM"
                    placeholder="选择采购年月"
                    v-model="query.time"
                    :picker-options="monthPickerOptions"
                    @change="changeMonth"
                  >
                  </el-date-picker>
                </el-col>
                <!-- 采购商业渠道类型 -->
                <el-col :span="4" v-if="selectQuery == 1">
                  <div class="title">采购商业渠道类型</div>
                  <el-select placeholder="请选择采购商业渠道类型" v-model="query.purchaseChannelId" @change="changePurchaseChannelId">
                    <el-option label="全部" value=""></el-option>
                    <el-option v-for="item in channelType" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </el-col>
                <!-- 采购商业 -->
                <el-col :span="4">
                  <div class="title">采购商业</div>
                  <el-select
                    filterable
                    multiple
                    collapse-tags
                    clearable
                    placeholder="请选择采购商业"
                    v-model="query.purchaseEnterpriseIds"
                    :filter-method="filterPurchaseEnterprise"
                    @change="changePurchaseEnterprise"
                  >
                    <el-checkbox
                      class="el-all-select"
                      v-model="isAllPurchaseEnterprise"
                      :indeterminate="
                        query.purchaseEnterpriseIds.length > 0 &&
                          query.purchaseEnterpriseIds.length < purchaseEnterpriseOptions.length
                      "
                      @change="selectAllPurchaseEnterprise"
                      >全选</el-checkbox
                    >
                    <el-option v-for="item in filterPurchaseEnterpriseOptions" :key="item.suId" :label="item.clientName" :value="item.suId"></el-option>
                  </el-select>
                </el-col>
                <!-- 供应商渠道类型 -->
                <el-col :span="4" v-if="selectQuery == 1">
                  <div class="title">供应商渠道类型</div>
                  <el-select placeholder="请选择供应商渠道类型" v-model="query.supplierChannelId" @change="changeSupplierChannelId">
                    <el-option label="全部" value=""></el-option>
                    <el-option v-for="item in channelType" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </el-col>
                <!-- 供应商 -->
                <el-col :span="4" v-if="selectQuery == 1">
                  <div class="title">供应商</div>
                  <el-select
                    filterable
                    multiple
                    collapse-tags
                    clearable
                    placeholder="请选择供应商"
                    v-model="query.supplierEnterpriseIds"
                    @change="getPurchaseList"
                  >
                    <el-checkbox
                      class="el-all-select"
                      v-model="isAllSupplierEnterprise"
                      :indeterminate="
                        query.supplierEnterpriseIds.length > 0 &&
                          query.supplierEnterpriseIds.length < supplierEnterpriseOptions.length
                      "
                      @change="selectAllSupplierEnterprise"
                      >全选</el-checkbox
                    >
                    <el-option v-for="item in supplierEnterpriseOptions" :key="item.suId" :label="item.clientName" :value="item.suId"></el-option>
                  </el-select>
                </el-col>
                <!-- 采购商品名称 -->
                <el-col :span="4" v-if="selectQuery == 2">
                  <div class="title">采购商品名称</div>
                  <el-select
                    filterable
                    multiple
                    collapse-tags
                    clearable
                    placeholder="请选择采购商品"
                    v-model="query.goodsNameList"
                    @change="getPurchaseGoodsList"
                  >
                    <el-checkbox
                      class="el-all-select"
                      v-model="isAllPurchaseGoods"
                      :indeterminate="
                        query.goodsNameList.length > 0 && query.goodsNameList.length < purchaseGoodsOptions.length
                      "
                      @change="selectAllPurchaseGoods"
                      >全选</el-checkbox
                    >
                    <el-option v-for="(item, index) in purchaseGoodsOptions" :key="index" :label="item" :value="item"></el-option>
                  </el-select>
                </el-col>
                <!-- 入库数量 -->
                <el-col :span="4" v-if="selectQuery == 2">
                  <div class="title">入库数量</div>
                  <div class="range-box">
                    <el-input class="range-input" placeholder="最小值" v-model.trim="query.minQuantity" @change="changeMinInput">
                      <template slot="append">≤</template>
                    </el-input>
                    <span class="separator">值</span>
                    <el-input class="range-input" placeholder="最大值" v-model.trim="query.maxQuantity" @change="changeMaxInput">
                      <template slot="prepend">&lt;</template>
                    </el-input>
                  </div>
                </el-col>
                <el-col :span="8" v-if="selectQuery == 2">
                  <div class="title">省市区</div>
                  <div class="flex-row-left">
                    <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
                  </div>
                </el-col>
              </el-row>
            </div>
            <!-- 采购入库表-数据列表 -->
            <div class="common-box table-wrap mar-t-8">
              <!-- ERP采购商采购入库表-数据列表 -->
              <div class="table-box" v-if="selectQuery == 1">
                <div class="table-title">ERP采购商采购入库表</div>
                <div class="table-data-box mar-t-8">
                  <yl-table
                    ref="purchaseTableRef"
                    border
                    stripe
                    height="100%"
                    show-summary
                    :show-header="true"
                    :loading="loading"
                    :list="purchaseTableData"
                    :summary-method="purchaseSummary"
                  >
                    <el-table-column align="center" label="采购日期">
                      <template slot="header">
                        <div class="header-box">
                          <span></span>
                          <span>采购日期</span>
                          <span @click="sortMonth">
                            <i class="sort-icon el-icon-caret-left" v-if="monthHeaderSort"></i>
                            <i class="sort-icon el-icon-caret-right" v-if="!monthHeaderSort"></i>
                          </span>
                        </div>
                      </template>
                      <el-table-column min-width="240" label="采购商业名称" prop="purchaseEnterpriseName">
                        <template slot-scope="{ row, $index }">
                          <div class="pur-name" @click="checkPurchaseEnterpriseDetail(row, $index)">
                            {{ row.purchaseEnterpriseName }}
                          </div>
                        </template>
                      </el-table-column>
                      <el-table-column min-width="120" label="采购商渠道类型" prop="purchaseChannelDesc"></el-table-column>
                      <el-table-column min-width="240" label="供应商名称" prop="supplierEnterpriseName"></el-table-column>
                      <el-table-column min-width="120" label="供应商渠道类型" prop="supplierChannelDesc"></el-table-column>
                    </el-table-column>
                    <el-table-column align="center" v-for="(item, index) in monthHeader" :key="index" :label="item">
                      <el-table-column
                        header-align="center"
                        align="right"
                        min-width="120"
                        label="入库金额(百万)"
                        v-show="purchaseTableData.length > 0"
                        :prop="`${item}.storageMoney`"
                        :key="`${index}.storageMoney`"
                      >
                        <template slot-scope="{ row }">
                          <span>{{ row.storageInfoMap[item].storageMoney | toThousand }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column
                        header-align="center"
                        align="right"
                        min-width="120"
                        label="入库数量"
                        v-show="purchaseTableData.length > 0"
                        :prop="`${item}.storageQuantity`"
                        :key="`${index}.storageQuantity`"
                      >
                        <template slot-scope="{ row }">
                          <span>{{ row.storageInfoMap[item].storageQuantity | toThousand }}</span>
                        </template>
                      </el-table-column>
                    </el-table-column>
                    <el-table-column width="140" header-align="center" align="right" label="入库金额(百万)">
                      <template slot-scope="{ row }">
                        <span>{{ row.storageMoneySum | toThousand }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column width="140" header-align="center" align="right" label="入库数量">
                      <template slot-scope="{ row }">
                        <span>{{ row.storageQuantitySum | toThousand }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
              </div>
              <!-- 采购入库商品表-数据列表 -->
              <div class="table-box" v-if="selectQuery == 2">
                <div class="table-title">采购入库商品表</div>
                <div class="table-data-box mar-t-8">
                  <yl-table
                    ref="purchaseGoodsTableRef"
                    border
                    stripe
                    height="100%"
                    show-summary
                    :show-header="true"
                    :loading="loading"
                    :list="purchaseGoodsTableData"
                    :summary-method="purchaseGoodsSummary"
                  >
                    <el-table-column align="center" label="采购日期">
                      <el-table-column min-width="100" align="center" prop="spec" label="规格"></el-table-column>
                      <el-table-column min-width="200" align="center" prop="goodsName" label="商品名称"></el-table-column>
                    </el-table-column>
                    <el-table-column align="center" v-for="(item, index) in monthHeader" :key="index" :label="item">
                      <el-table-column
                        header-align="center"
                        align="right"
                        min-width="100"
                        label="入库数量"
                        v-if="purchaseGoodsTableData.length > 0"
                        :prop="`${item}.storageQuantity`"
                      >
                        <template slot-scope="{ row }">
                          <span>{{ row.storageInfoMap[item].storageQuantity | toThousand }}</span>
                        </template>
                      </el-table-column>
                      <el-table-column
                        header-align="center"
                        align="right"
                        min-width="100"
                        label="入库排名"
                        v-if="purchaseGoodsTableData.length > 0"
                        :prop="`${item}.rank`"
                      >
                        <template slot-scope="{ row }">
                          <span>{{ row.storageInfoMap[item].rank }}</span>
                        </template>
                      </el-table-column>
                    </el-table-column>
                    <el-table-column width="120" header-align="center" align="right" label="入库数量">
                      <template slot-scope="{ row }">
                        <span>{{ row.storageQuantitySum | toThousand }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      sortable
                      width="120"
                      header-align="center"
                      align="right"
                      prop="rank"
                      label="入库排名"
                    ></el-table-column>
                  </yl-table>
                </div>
              </div>
            </div>
          </div>
        </yl-dialog>
        <!-- 采购入库明细表弹窗 -->
        <yl-dialog
          class="chart-dialog"
          :visible.sync="purchaseDetailDialogShow"
          destroy-on-close
          width="100%"
          fullscreen
          top="0"
          :show-footer="false"
          @close="closeDetailDialog"
        >
          <div class="content">
            <div class="button-box">
              <ylButton type="primary" plain @click="closeDetailDialog">关闭</ylButton>
            </div>
            <!-- 数据列表 -->
            <div class="common-box table-wrap mar-t-8">
              <!-- ERP采购商采购入库明细表-数据列表 -->
              <div class="table-box">
                <div class="table-title">采购商业采购入库明细表</div>
                <div class="table-data-box mar-t-8">
                  <yl-table
                    ref="purchaseDetailTableRef"
                    border
                    stripe
                    height="100%"
                    show-summary
                    :loading="loading1"
                    :show-header="true"
                    :list="purchaseDetailTableData"
                    :span-method="purchaseDetailSpanMethod"
                    :summary-method="purchaseDetailSummary"
                  >
                    <el-table-column label="采购商业" prop="purchaseEnterpriseName"></el-table-column>
                    <el-table-column label="渠道类型" prop="channelDesc"></el-table-column>
                    <el-table-column label="采购日期">
                      <template slot-scope="{ row }">
                        <span>{{ row.time }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="入库金额(万)">
                      <template slot-scope="{ row }">
                        <span>{{ row.storageMoney | toThousand }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="入库数量">
                      <template slot-scope="{ row }">
                        <span>{{ row.storageQuantity | toThousand }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
              </div>
            </div>
            <!-- 折线图区域 -->
            <div class="common-box echart-wrap mar-t-8">
              <!-- 采购入库走势图 -->
              <div class="echart-box">
                <div class="title">采购入库走势图</div>
                <div class="echart-data-box mar-t-16" ref="purchaseDetailChartRef" style="width: 100%; height: 100%"></div>
              </div>
            </div>
          </div>
        </yl-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import * as Echarts from 'echarts'
import {
  requestPurchaseEnterpriseList,
  requestSupplierEnterpriseList,
  requestPurchaseList,
  requestPurchaseDetail,
  requestPurchaseGoodsList,
  requestPurchaseGoodsNameList
} from '@/subject/admin/api/zt_api/erp_purchase_storage'
import { channelType } from '@/subject/admin/utils/busi'
import { ylChooseAddress } from '@/subject/admin/components'
import { validateIsNumZZ } from '@/subject/admin/utils/validate'
import { toThousand } from '@/common/filters'

export default {
  name: 'ErpPurchaseStorageChart',
  components: {
    ylChooseAddress
  },
  computed: {
    // 渠道类型
    channelType() {
      return channelType()
    },
    // 采购商全选状态
    isAllPurchaseEnterprise: {
      get() {
        if (this.query.purchaseEnterpriseIds.length == this.purchaseEnterpriseOptions.length) {
          return true
        }
        return false
      },
      set() {}
    },
    // 供应商全选状态
    isAllSupplierEnterprise: {
      get() {
        if (this.query.supplierEnterpriseIds.length == this.supplierEnterpriseOptions.length) {
          return true
        }
        return false
      },
      set() {}
    },
    // 采购商品全选状态
    isAllPurchaseGoods: {
      get() {
        if (this.query.goodsNameList.length == this.purchaseGoodsOptions.length) {
          return true
        }
        return false
      },
      set() {}
    }
  },
  watch: {
    'query.provinceCode': {
      handler() {
        this.getPurchaseGoodsList()
      },
      deep: true
    },
    'query.cityCode': {
      handler() {
        this.getPurchaseGoodsList()
      },
      deep: true
    },
    'query.regionCode': {
      handler() {
        this.getPurchaseGoodsList()
      },
      deep: true
    }
  },
  filters: {},
  data() {
    return {
      selectQuery: 0,
      dataList: [
        { name: '采购入库仪表', value: 1 },
        { name: '采购入库商品表', value: 2 }
      ],
      query: {
        // 采购年月
        time: '',
        // 采购商渠道类型
        purchaseChannelId: '',
        // 采购商业
        purchaseEnterpriseIds: [],
        // 供应商渠道类型
        supplierChannelId: '',
        // 供应商
        supplierEnterpriseIds: [],
        // 采购商品名称
        goodsNameList: [],
        // 入库数量最小值
        minQuantity: 0,
        // 入库数量最大值
        maxQuantity: 10000,
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      loading: false,
      loading1: false,
      // 采购商下拉选项数据
      purchaseEnterpriseOptions: [],
      // 采购商下拉选项数据(截取部分)
      filterPurchaseEnterpriseOptions: [],
      // 供应商下拉选项数据
      supplierEnterpriseOptions: [],
      // 采购商品名称下拉数据
      purchaseGoodsOptions: [],
      // 采购入库表数据
      purchaseTableData: [],
      // 采购入库表-合计-月份(数组对象形式)
      monthMoneyQuantity: [],
      // 采购入库表-合计-总计
      purchaseSummarySumData: [],
      // 表格月份列头部
      monthHeader: [],
      // 切换月份头部顺序
      monthHeaderSort: false,
      // 入库表弹窗
      purchaseDialogShow: false,
      // 明细表弹窗
      purchaseDetailDialogShow: false,
      // 采购入库明细表折线图实例
      echartInstance: null,
      // 折线图通用配置项
      commonEchartOption: {
        title: {
          text: '',
          left: 'center',
          textStyle: { fontSize: 16 }
        },
        tooltip: {
          trigger: 'axis',
          borderWidth: 1,
          textStyle: {
            fontSize: 12
          }
        },
        axisPointer: {
          link: { xAxisIndex: 'all' }
        },
        grid: {
          left: '5%',
          right: '5%',
          top: '8%',
          bottom: '8%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          name: '',
          nameLocation: 'center',
          nameGap: 23,
          nameTextStyle: {
            fontSize: 12
          },
          splitNumber: 4,
          data: []
        },
        yAxis: {
          type: 'value',
          position: 'left',
          name: '',
          nameLocation: 'center',
          nameGap: 100,
          axisLine: {
            show: true
          }
        },
        series: {
          type: 'line'
        }
      },
      // 采购入库明细表数据
      purchaseDetailTableData: [],
      // 采购入库明细合计数据
      purchaseDetailSummaryData: [],
      // 采购金额配置项
      purchaseDetailOption: {},
      // 采购入库商品表
      purchaseGoodsTableData: [],
      // 商品表合计数据
      purchaseGoodsSummaryData: [],
      // 月份选择器配置
      monthPickerOptions: {
        disabledDate: time => {
          return (
            time.getFullYear() !== new Date().getFullYear() ||
            (time.getFullYear() == new Date().getFullYear() && time.getMonth() + 1 <= new Date().getMonth() + 1 - 6) ||
            (time.getFullYear() == new Date().getFullYear() && time.getMonth() + 1 > new Date().getMonth() + 1)
          )
        }
      }
    }
  },
  activated() {},
  created() {},
  beforeDestroy() {
    window.removeEventListener('resize', this.echartResize)
  },
  methods: {
    // 查看
    detail(row) {
      this.selectQuery = row.value
      this.openPurchaseDialog()
    },
    // 打开弹窗
    openPurchaseDialog() {
      this.purchaseDialogShow = true
      if (this.selectQuery == 1) {
        this.getSupplierEnterpriseList()
        this.getPurchaseList()
      } else if (this.selectQuery == 2) {
        this.getPurchaseGoodsNameList()
        this.getPurchaseGoodsList()
      }
      this.getPurchaseEnterpriseList()
    },

    /************** 采购入库仪表 **************/
    // 获取采购商数据
    async getPurchaseEnterpriseList() {
      const query = this.query
      const data = await requestPurchaseEnterpriseList(query.purchaseChannelId)
      if (data) {
        this.purchaseEnterpriseOptions = data
        this.filterPurchaseEnterpriseOptions = data.slice(0, 30)
      }
    },
    // 获取供应商数据
    async getSupplierEnterpriseList() {
      const query = this.query
      const data = await requestSupplierEnterpriseList(query.supplierChannelId)
      if (data) {
        this.supplierEnterpriseOptions = data
      }
    },
    // 获取采购商采购入库数据
    async getPurchaseList() {
      this.purchaseTableData = []
      this.monthHeader = []
      this.loading = true
      const query = this.query
      const data = await requestPurchaseList(
        query.time,
        query.purchaseChannelId,
        query.purchaseEnterpriseIds,
        query.supplierChannelId,
        query.supplierEnterpriseIds
      )
      this.loading = false
      if (data) {
        if (data.dataList.length > 1) {
          this.monthHeader = data.monthList.reverse()
          this.purchaseTableData = data.dataList.slice(1, data.dataList.length)
          // 合计行月份数据数据
          this.monthMoneyQuantity = Object.keys(data.dataList[0].storageInfoMap).map(item => {
            return {
              storageMoney: toThousand(data.dataList[0].storageInfoMap[item].storageMoney),
              storageQuantity: toThousand(data.dataList[0].storageInfoMap[item].storageQuantity)
            }
          })
          // 合计行总计数据
          this.purchaseSummarySumData = [
            toThousand(data.dataList[0].storageMoneySum),
            toThousand(data.dataList[0].storageQuantitySum)
          ]
        }
      }
    },
    // 关闭弹窗
    closePurchaseDialog() {
      this.purchaseDialogShow = false
      this.purchaseTableData = []
      this.purchaseGoodsTableData = []
      this.monthHeader = []
      this.query.time = ''
      this.query.purchaseChannelId = ''
      this.query.purchaseEnterpriseIds = []
      this.query.supplierChannelId = ''
      this.query.supplierEnterpriseIds = []
      this.query.goodsNameList = []
      this.query.minQuantity = 0
      this.query.maxQuantity = 10000
      this.query.provinceCode = ''
      this.query.cityCode = ''
      this.query.regionCode = ''
    },
    // 选择年月
    changeMonth(value) {
      if (value === null) {
        this.query.time = ''
      }
      if (this.selectQuery == 1) {
        this.getPurchaseList()
      } else if (this.selectQuery == 2) {
        this.getPurchaseGoodsList()
      }
    },
    // 选择采购商类型
    changePurchaseChannelId() {
      this.query.purchaseEnterpriseIds = []
      this.getPurchaseEnterpriseList()
      this.getPurchaseList()
    },
    // 选择采购商
    changePurchaseEnterprise() {
      if (this.selectQuery == 1) {
        this.getPurchaseList()
      } else if (this.selectQuery == 2) {
        this.getPurchaseGoodsList()
      }
    },
    // 全选采购商
    async selectAllPurchaseEnterprise(checked) {
      if (checked) {
        this.query.purchaseEnterpriseIds = this.purchaseEnterpriseOptions.map(item => item.suId)
      } else {
        this.query.purchaseEnterpriseIds = []
      }
      if (this.selectQuery == 1) {
        await this.getPurchaseList()
      } else if (this.selectQuery == 2) {
        await this.getPurchaseGoodsList()
      }
    },
    // 过滤采购商
    filterPurchaseEnterprise(value) {
      this.filterPurchaseEnterpriseOptions = this.purchaseEnterpriseOptions
        .filter(item => item.clientName.includes(value))
        .slice(0, 30)
    },
    // 选择供应商类型
    changeSupplierChannelId() {
      this.query.supplierEnterpriseIds = []
      this.getSupplierEnterpriseList()
      this.getPurchaseList()
    },
    // 全选供应商
    async selectAllSupplierEnterprise(checked) {
      if (checked) {
        this.query.supplierEnterpriseIds = this.supplierEnterpriseOptions.map(item => item.suId)
      } else {
        this.query.supplierEnterpriseIds = []
      }
      await this.getPurchaseList()
    },
    // 全选采购商品
    async selectAllPurchaseGoods(checked) {
      if (checked) {
        this.query.goodsNameList = this.purchaseGoodsOptions
      } else {
        this.query.goodsNameList = []
      }
      await this.getPurchaseGoodsList()
    },
    // 查看采购商详情
    async checkPurchaseEnterpriseDetail(row, index) {
      this.purchaseDetailDialogShow = true
      this.loading1 = true
      const data = await requestPurchaseDetail(
        row.purchaseEnterpriseId,
        row.supplierEnterpriseId,
        this.query.time
      )
      this.loading1 = false
      if (data) {
        this.purchaseDetailTableData = data.storageInfoList.slice(1, data.storageInfoList.length).map((item, index) => {
          return {
            purchaseEnterpriseId: data.purchaseEnterpriseId,
            purchaseEnterpriseName: data.purchaseEnterpriseName,
            channelId: data.channelId,
            channelDesc: data.channelDesc,
            rank: item.rank,
            time: item.time,
            storageMoney: item.storageMoney,
            storageQuantity: item.storageQuantity
          }
        })
        this.purchaseDetailSummaryData = [toThousand(data.storageInfoList[0].storageMoney), toThousand(data.storageInfoList[0].storageQuantity)]
        if (this.purchaseDetailTableData.length > 0) {
          this.setChartData()
        }
      }
    },
    // 采购商入库表-合并行列
    purchaseSpanMethod({ rowIndex, columnIndex }) {
      // 1.制作合并数组
      // 合并位置(索引)
      let position = 0
      // 控制合并行的参照数组
      let spanArr = []
      this.purchaseTableData.forEach((item, index) => {
        if (index === 0) {
          // 第一位默认占一行
          spanArr.push(1)
        } else {
          // 被判断数据与前一项id相同, 将参照数组对应位置+1 并立即加一项0 (前一项多占一行,后面占0行, 也就是后一项不显示)
          // 如 [1, 1, 3, 0, 0] 第三项与第四项以及第五项id相同,第三项占3行,第四第五行占0行,类推
          if (item.purchaseEnterpriseId === this.purchaseTableData[index - 1].purchaseEnterpriseId) {
            spanArr[position] += 1
            spanArr.push(0)
          } else {
            position = index
            spanArr.push(1)
          }
        }
      })
      // 2.根据合并数组对行列进行合并
      if (rowIndex === 0) {
        if (columnIndex === 0) {
          return {
            rowspan: 1,
            colspan: 4
          }
        } else if (columnIndex > 0 && columnIndex < 4) {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
      } else if (rowIndex !== 0) {
        if (columnIndex === 0 || columnIndex === 1) {
          const row = spanArr[rowIndex]
          const col = row > 0 ? 1 : 0
          return {
            rowspan: row,
            colspan: col
          }
        }
      }
    },
    // 采购商入库表-月份排序
    sortMonth() {
      this.monthMoneyQuantity = this.monthMoneyQuantity.reverse()
      this.monthHeader = this.monthHeader.reverse()
      this.monthHeaderSort = !this.monthHeaderSort
    },
    // 采购商入库表合计
    purchaseSummary() {
      this.$nextTick(() => {
        if (this.$refs.table.$el) {
          const current = this.$refs.purchaseTableRef.$el
            .querySelector('.el-table__footer-wrapper')
            .querySelector('.el-table__footer')
          let cell = current.rows[0].cells
          cell[0].colSpan = '4'
          cell[0].style.textAlign = 'left'
          cell[1].style.display = 'none'
          cell[2].style.display = 'none'
          cell[3].style.display = 'none'
        }
      })
      let monthData = []
      this.monthMoneyQuantity.forEach(item => {
        monthData.push(item.storageMoney)
        monthData.push(item.storageQuantity)
      })
      return ['合计', '', '', '', ...monthData, ...this.purchaseSummarySumData]
    },

    /************** 采购入库明细表 **************/
    // 关闭采购入库明细表
    closeDetailDialog() {
      this.purchaseDetailDialogShow = false
      window.removeEventListener('resize', this.echartResize)
    },
    // 采购入库明细表-合并列
    purchaseDetailSpanMethod({ rowIndex, columnIndex }) {
      if (columnIndex === 0 || columnIndex === 1) {
        if (rowIndex === 0) {
          return {
            rowspan: this.purchaseDetailTableData.length,
            colspan: 1
          }
        } else {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
      }
    },
    // 设置echart配置项 绘制折线图
    async setChartData() {
      this.purchaseDetailOption.xAxis = {
        ...this.commonEchartOption.xAxis,
        name: '采购日期',
        data: this.purchaseDetailTableData.map(item => item.time)
      }
      this.purchaseDetailOption.yAxis = [
        {
          ...this.commonEchartOption.yAxis,
          position: 'left',
          name: '入库金额(万)'
        },
        {
          ...this.commonEchartOption.yAxis,
          position: 'right',
          name: '入库数量'
        }
      ]
      this.purchaseDetailOption.series = [
        {
          name: '入库金额(万)',
          type: 'line',
          yAxisIndex: 0,
          itemStyle: { color: '#5470C6' },
          data: this.purchaseDetailTableData.map(item => item.storageMoney)
        },
        {
          name: '入库数量',
          type: 'line',
          yAxisIndex: 1,
          itemStyle: { color: '#91CC75' },
          data: this.purchaseDetailTableData.map(item => item.storageQuantity)
        }
      ]
      this.purchaseDetailOption.grid = {
        left: '5%',
        right: '5%',
        top: '10',
        containLabel: true
      }
      this.purchaseDetailOption.tooltip = this.commonEchartOption.tooltip
      this.initEcharts('purchaseDetailChartRef', this.purchaseDetailOption)
    },
    // 初始化图表
    initEcharts(type, option) {
      // 图表配置项
      this.echartInstance = Echarts.getInstanceByDom(this.$refs[type])
      if (this.echartInstance == undefined) {
        this.echartInstance = Echarts.init(this.$refs[type])
      }
      this.echartInstance.setOption(option)
      window.addEventListener('resize', this.echartResize)
    },
    // 重新绘制图表
    echartResize() {
      this.echartInstance.resize()
    },
    // 明细表合计
    purchaseDetailSummary() {
      this.$nextTick(() => {
        if (this.$refs.table.$el) {
          const current = this.$refs.purchaseDetailTableRef.$el
            .querySelector('.el-table__footer-wrapper')
            .querySelector('.el-table__footer')
          let cell = current.rows[0].cells
          cell[0].colSpan = '3'
          cell[0].style.textAlign = 'left'
          cell[1].style.display = 'none'
          cell[2].style.display = 'none'
        }
      })
      return ['合计', '', '', ...this.purchaseDetailSummaryData]
    },

    /************** 采购入库商品表 **************/
    // 入库数量输入修改
    changeMinInput(value) {
      if (value === '0' || validateIsNumZZ(value)) {
        if (this.query.maxQuantity !== '' && Number(value) > Number(this.query.maxQuantity)) {
          this.query.minQuantity = ''
        } else {
          this.query.minQuantity = value
        }
      } else {
        this.query.minQuantity = ''
      }
      if (this.query.minQuantity !== '') {
        this.getPurchaseGoodsList()
      }
    },
    changeMaxInput(value) {
      if (value === '0' || validateIsNumZZ(value)) {
        if (this.query.minQuantity !== '' && Number(value) < Number(this.query.minQuantity)) {
          this.query.maxQuantity = ''
        } else {
          this.query.maxQuantity = value
        }
      } else {
        this.query.maxQuantity = ''
      }
      if (this.query.maxQuantity !== '') {
        this.getPurchaseGoodsList()
      }
    },
    // 获取采购商品
    async getPurchaseGoodsNameList() {
      const data = await requestPurchaseGoodsNameList()
      if (data) {
        this.purchaseGoodsOptions = data
      }
    },
    // 获取采购入库商品表数据
    async getPurchaseGoodsList() {
      const query = this.query
      if (query.minQuantity === '' || query.maxQuantity === '') {
        return this.$common.warn('请先输入正确的入库数量')
      }
      this.purchaseGoodsTableData = []
      this.monthHeader = []
      this.loading = true
      const data = await requestPurchaseGoodsList(
        query.time,
        query.purchaseEnterpriseIds,
        query.goodsNameList,
        Number(query.minQuantity),
        Number(query.maxQuantity),
        query.provinceCode,
        query.cityCode,
        query.regionCode
      )
      this.loading = false
      if (data) {
        if (data.dataList.length > 1) {
          this.purchaseGoodsTableData = data.dataList.slice(1, data.dataList.length)
          this.monthHeader = data.monthList.reverse()
          let monthQuantityRank = []
          Object.keys(data.dataList[0].storageInfoMap).forEach(item => {
            monthQuantityRank.push(toThousand(data.dataList[0].storageInfoMap[item].storageQuantity))
            monthQuantityRank.push(data.dataList[0].storageInfoMap[item].rank)
          })
          this.purchaseGoodsSummaryData = [
            ...monthQuantityRank,
            toThousand(data.dataList[0].storageQuantitySum),
            data.dataList[0].rank
          ]
        }
      }
    },
    // 商品表合并单元格
    purchaseGoodsSpanMethod({ rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        if (columnIndex === 0) {
          return {
            rowspan: 1,
            colspan: 2
          }
        } else if (columnIndex === 1) {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
      }
    },
    // 商品表合计
    purchaseGoodsSummary() {
      this.$nextTick(() => {
        if (this.$refs.table.$el) {
          const current = this.$refs.purchaseGoodsTableRef.$el
            .querySelector('.el-table__footer-wrapper')
            .querySelector('.el-table__footer')
          let cell = current.rows[0].cells
          cell[0].colSpan = '2'
          cell[0].style.textAlign = 'left'
          cell[1].style.display = 'none'
        }
      })
      return ['合计', '', ...this.purchaseGoodsSummaryData]
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
