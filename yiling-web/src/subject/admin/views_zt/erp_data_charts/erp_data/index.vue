<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <!-- 流向列表 -->
        <div class="table-box">
          <yl-table
          ref="table"
          border
          stripe
          :list="dataList"
          show-header>
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
        <!-- 弹窗 -->
        <yl-dialog
          class="chartDialog"
          :visible.sync="dialogShow"
          destroy-on-close
          width="100%"
          fullscreen
          top="0"
          :show-footer="false"
          @open="openDialog"
          @close="closeDialog"
        >
          <div class="content">
            <div class="button-box">
              <ylButton type="primary" @click="closeDialog">关闭</ylButton>
            </div>
            <!-- 搜索选项 -->
            <div class="common-box tool-box mar-t-8">
              <el-row>
                <!-- 按日选择 -->
                <el-col :span="4">
                  <div class="title">{{ timeTitle }}</div>
                  <el-date-picker
                    v-model="query.timeRange"
                    type="daterange"
                    format="yyyy/MM/dd"
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    @change="getChartData"
                    :picker-options="dayPickerOptions"
                  ></el-date-picker>
                </el-col>
                <!-- 连花品类 -->
                <el-col :span="4" v-if="selectQuery == 4 || selectQuery == 5 || selectQuery == 6">
                  <div class="title">连花品类</div>
                  <el-select v-model="query.goodsCategory" placeholder="请选择商品品类" @change="getChartData">
                    <el-option label="全部" value=""></el-option>
                    <el-option
                      v-for="item in goodsCategoryOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-col>
                <!-- 渠道类型 -->
                <el-col :span="4">
                  <div class="title">渠道类型</div>
                  <el-select v-model="query.channelId" placeholder="请选择渠道类型" @change="changeChannel">
                    <el-option label="全部" :value="0"></el-option>
                    <el-option v-for="item in channelType" :key="item.value" :label="item.label" :value="item.value" ></el-option>
                  </el-select>
                </el-col>
                <!-- 是否云仓 -->
                <el-col :span="4">
                  <div class="title">是否云仓</div>
                  <el-checkbox v-model="isCloud" label="云仓" border @change="changeIsCloud"></el-checkbox>
                </el-col>
                <!-- 商业公司 -->
                <el-col :span="4" v-if="selectQuery == 4 || selectQuery == 5">
                  <div class="title">商业公司</div>
                  <el-select
                    v-model="query.eid"
                    filterable
                    remote
                    reserve-keyword
                    clearable
                    placeholder="请输入名称搜索选择商业公司"
                    :remote-method="remoteMethod"
                    :loading="loadingSearch"
                    @change="getChartData"
                  >
                    <el-option
                      v-for="item in enterpriseList"
                      :key="item.suId"
                      :label="item.clientName"
                      :value="item.suId"
                    ></el-option>
                  </el-select>
                </el-col>
              </el-row>
            </div>
            <!-- 折线图区域 -->
            <div class="common-box mar-t-8">
              <!-- ERP采购流向走势-折线图 -->
              <div class="echart-box" v-if="selectQuery == 1">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}ERP采购流向走势</div>
                <div ref="purchaseAmountRef" style="width: 100%; height: 220px"></div>
                <div ref="purchaseNumberRef" style="width: 100%; height: 220px"></div>
              </div>
              <!-- ERP销售流向走势-折线图 -->
              <div class="echart-box" v-if="selectQuery == 2">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}ERP销售流向走势</div>
                <div ref="saleAmountRef" style="width: 100%; height: 220px"></div>
                <div ref="saleNumberRef" style="width: 100%; height: 220px"></div>
              </div>
              <!-- ERP采购和POP发货对比-折线图 -->
              <div class="echart-box" v-if="selectQuery == 3">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}ERP采购和POP发货金额和数量对比</div>
                <!-- echart图表legend图例的名称必须和series名称相同,此处需求要在名称后面加统计数值,所以没有采用legend去配置,而用普通div区块样式展示,故而折线颜色必须正确匹配 -->
                <div class="statistic-box">
                  <div class="item">
                    <div class="icon erp-total"></div>
                    <div class="title">POP发货金额</div>
                    <div class="value">{{ erpTotalAmountTotal | toThousand('') }}</div>
                  </div>
                  <div class="item">
                    <div class="icon erp-quntity"></div>
                    <div class="title">POP发货数量</div>
                    <div class="value">{{ erpQuantityTotal | toThousand('') }}</div>
                  </div>
                  <div class="item">
                    <div class="icon pop-amount"></div>
                    <div class="title">采购流向金额</div>
                    <div class="value">{{ poTotalAmountTotal | toThousand('') }}</div>
                  </div>
                  <div class="item">
                    <div class="icon pop-quantity"></div>
                    <div class="title">采购流向数量</div>
                    <div class="value">{{ poQuantityTotal | toThousand('') }}</div>
                  </div>
                </div>
                <div ref="statisticsPopPurchaseRef" style="width: 100%; height: 600px"></div>
              </div>
              <!-- ERP采购流向-折线图 -->
              <div class="echart-box" v-if="selectQuery == 4">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}ERP采购流向</div>
                <div ref="lhPurchaseAmountRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 1"></div>
                <div ref="lhPurchaseNumberRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 1"></div>
                <div ref="notlhPurchaseAmountRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 2"></div>
                <div ref="notlhPurchaseNumberRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 2"></div>
              </div>
              <!-- ERP销售流向-折线图 -->
              <div class="echart-box" v-if="selectQuery == 5">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}ERP销售流向</div>
                <div ref="lhSaleAmountRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 1"></div>
                <div ref="lhSaleNumberRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 1"></div>
                <div ref="notlhSaleAmountRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 2"></div>
                <div ref="notlhSaleNumberRef" style="width: 100%; height: 180px" v-if="query.goodsCategory == '' || query.goodsCategory == 2"></div>
              </div>
              <!-- 库存销售数量报表-折线图 -->
              <div class="echart-box" v-if="selectQuery == 6">
                <div class="title">{{ echartTableTitle + (query.isCloudFlag == 1 ? '云仓' : '') }}实时库存走势分析</div>
                <div ref="lhBatchNumberRef" style="width: 100%; height: 220px" v-if="query.goodsCategory == '' || query.goodsCategory == 1"></div>
                <div ref="notlhBatchNumberRef" style="width: 100%; height: 220px" v-if="query.goodsCategory == '' || query.goodsCategory == 2"></div>
              </div>
            </div>
            <!-- 数据列表 -->
            <div class="common-box table-wrap mar-t-8" v-if="selectQuery == 1 || selectQuery == 2 || selectQuery == 6">
              <!-- ERP采购流向走势-数据列表 -->
              <div class="table-box" v-if="selectQuery == 1">
                <div class="table-data-box">
                  <yl-table border
                  :show-header="true"
                  show-summary
                  :summary-method="purchaseTableSummary"
                  :list="purchaseTableData.list">
                    <el-table-column align="center" label="购进日期" prop="time">
                      <template slot-scope="{ row }">
                        <span>{{ row.time | formatDate("yyyy-MM-dd") }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="采购金额" prop="amount">
                      <template slot-scope="{ row }">
                        <span>{{ row.amount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="数量" prop="number">
                      <template slot-scope="{ row }">
                        <span>{{ row.number | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
              </div>
              <!-- ERP销售流向走势-数据列表 -->
              <div class="table-box" v-if="selectQuery == 2">
                <div class="table-data-box">
                <!-- 二级商有 销售金额/数量（不含医疗）列 其他没有-->
                  <yl-table
                    border
                    :show-header="true"
                    show-summary
                    :summary-method="saleTableSummary"
                    :list="saleTableData.list">
                    <el-table-column align="center" label="销售日期" prop="time">
                      <template slot-scope="{ row }">
                        <span>{{ row.time | formatDate("yyyy-MM-dd") }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="销售金额" prop="amount">
                      <template slot-scope="{ row }">
                        <span>{{ row.amount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="销售数量" prop="number">
                      <template slot-scope="{ row }">
                        <span>{{ row.number | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="销售金额（不含医疗）" prop="terminalAmount" v-if="query.channelId == 4">
                      <template slot-scope="{ row }">
                        <span>{{ row.terminalAmount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="销售数量（不含医疗）" prop="terminalNumber" v-if="query.channelId == 4">
                      <template slot-scope="{ row }">
                        <span>{{ row.terminalNumber | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
              </div>
              <!-- 库存销售数量报表-数据列表 -->
              <div class="table-box" v-if="selectQuery == 6">
                <div class="total-statistic">
                  统计：
                  <div class="item">
                    <span class="title">日平均库存金额：</span>
                    <span class="value">{{ batchTableData.avgAmount | toThousand('') }}</span>
                  </div>
                  <div class="item">
                    <span class="title">日平均库存量：</span>
                    <span class="value">{{ batchTableData.avgNumber | toThousand('') }}</span>
                  </div>
                  <div class="item">
                    <span class="title">差值库存金额：</span>
                    <span class="value">{{ batchTableData.diffAmount | toThousand('') }}</span>
                  </div>
                  <div class="item">
                    <span class="title">差值库存量：</span>
                    <span class="value">{{ batchTableData.diffNumber | toThousand('') }}</span>
                  </div>
                </div>
                <div class="table-data-box mar-t-16" v-if="query.goodsCategory == ''">
                  <yl-table
                  key="batchTotalTable"
                  border
                  :show-header="true"
                  :list="batchTableData.list">
                    <el-table-column align="center" label="入库日期" prop="time">
                      <template slot-scope="{ row }">
                        <span>{{ row.time | formatDate("yyyy-MM-dd") }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="数量(总)" prop="number">
                      <template slot-scope="{ row }">
                        <span>{{ row.number | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="金额(总)" prop="amount">
                      <template slot-scope="{ row }">
                        <span>{{ row.amount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="连花数量" prop="lhNumber">
                      <template slot-scope="{ row }">
                        <span>{{ row.lhNumber | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="连花金额" prop="lhAmount">
                      <template slot-scope="{ row }">
                        <span>{{ row.lhAmount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="非连花数量" prop="unLhNumber">
                      <template slot-scope="{ row }">
                        <span>{{ row.unLhNumber | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="非连花金额" prop="unLhAmount">
                      <template slot-scope="{ row }">
                        <span>{{ row.unLhAmount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
                <div class="table-data-box mar-t-16" v-if="query.goodsCategory !== ''">
                  <yl-table
                  key="batchPartTable"
                  border
                  :show-header="true"
                  :list="batchTableData.list">
                    <el-table-column align="center" label="入库日期" prop="time">
                      <template slot-scope="{ row }">
                        <span>{{ row.time | formatDate("yyyy-MM-dd") }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="数量" prop="number">
                      <template slot-scope="{ row }">
                        <span>{{ row.number | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="金额" prop="amount">
                      <template slot-scope="{ row }">
                        <span>{{ row.amount | toThousand('') }}</span>
                      </template>
                    </el-table-column>
                  </yl-table>
                </div>
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
  getlhPurchaseAmount,
  getlhPurchaseNumber,
  getlhSaleAmount,
  getlhSaleNumber,
  getStatisticsPopPurchase,
  getGoodsBatchNumber,
  queryEnterpriseList,
  purchaseStatistics,
  saleStatistics,
  goodsBatchStatistics
} from '@/subject/admin/api/zt_api/erpDataCharts'
import { formatDate } from '@/common/utils'
import { toThousand } from '@/common/filters'
import { channelType } from '@/subject/admin/utils/busi';

export default {
  name: 'ErpDataChart',
  components: {},
  computed: {
    // 渠道类型
    channelType() {
      return channelType().filter(item => item.value == 3 || item.value == 4 || item.value == 5)
    },
    // 默认时间
    timeDefault () {
        let date = new Date()
        // 通过时间戳计算
        let defalutStartTime = formatDate(new Date(date.getFullYear(), date.getMonth() - 1, date.getDate()), 'yyyy-MM-dd')
        let defalutEndTime = formatDate(new Date(), 'yyyy-MM-dd')
        return [defalutStartTime, defalutEndTime]
    },
    // 库存-默认时间(只有2022-06-07日之后的数据)
    batchDefaultTime() {
      let batchStartTime = formatDate(new Date('2022-06-07'), 'yyyy-MM-dd')
      let batchEndTime = formatDate(new Date(), 'yyyy-MM-dd')
      return [batchStartTime, batchEndTime]
    }
  },
  filters: {},
  data() {
    return {
      selectQuery: '',
      timeTitle: '购进日期',
      echartTableTitle: '',
      goodsCategoryOptions: [
        { label: '连花', value: 1 },
        { label: '非连花', value: 2 }
      ],
      isCloud: false,
      query: {
        channelId: 0,
        eid: '',
        goodsCategory: '',
        timeRange: [],
        isCloudFlag: 0
      },
      // 商业公司
      enterpriseList: [],
      loadingSearch: false,
      dataList: [
        { name: 'ERP采购流向走势', value: 1 },
        { name: 'ERP销售流向走势', value: 2 },
        { name: 'ERP采购和POP发货对比', value: 3 },
        { name: 'ERP采购流向', value: 4 },
        { name: 'ERP销售流向', value: 5 },
        { name: '库存销售数量报表', value: 6 }
      ],
      dialogShow: false,
      echartInstance: null,
      // 通用配置项
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
        grid: { left: '5%', right: '5%', top: '8%', bottom: '8%', containLabel: true },
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
      // 1.ERP采购流向走势数据和配置项
      // 采购金额
      purchaseAmount: [],
      // 采购金额配置项
      purchaseAmountOption: {},
      // 采购数量
      purchaseNumber: [],
      // 采购数量配置项
      purchaseNumberOption: {},
      // 采购表格数据
      purchaseTableData: {
        // 采购总额合计
        amountTotal: 0,
        // 采购数量合计
        numberTotal: 0,
        // 数据集合
        list: []
      },

      // 2.ERP销售流向走势-数据和配置项
      // 销售金额
      saleAmount: [],
      // 销售金额配置项
      saleAmountOption: {},
      // 销售数量
      saleNumber: [],
      // 销售数量配置项
      saleNumberOption: {},
      // 销售表格数据
      saleTableData: {
        // 销售总额合计
        amountTotal: 0,
        // 销售数量合计
        numberTotal: 0,
        // 终端销售总额合计
        terminalAmountTotal: 0,
        // 终端销售数量合计
        terminalNumberTotal: 0,
        // 数据集合
        list: []
      },

      // 3.ERP采购和POP发货对比-数据和配置项
      // 对比数据
      statisticsPopPurchase: [],
      // 对比配置项
      statisticsPopPurchaseOption: {},
      // POP发货数量
      erpQuantityTotal: '',
      // POP发货金额
      erpTotalAmountTotal: '',
      // 采购流向数量
      poQuantityTotal: '',
      // 采购流向金额
      poTotalAmountTotal: '',

      // 4.ERP采购流向-数据和配置项
      // 连花采购金额
      lhPurchaseAmount: [],
      // 连花采购金额配置项
      lhPurchaseAmountOption: {},
      // 连花采购数量
      lhPurchaseNumber: [],
      // 连花采购数量配置项
      lhPurchaseNumberOption: {},
      // 非连花采购金额
      notlhPurchaseAmount: [],
      // 非连花采购金额配置项
      notlhPurchaseAmountOption: {},
      // 非连花采购数量
      notlhPurchaseNumber: [],
      // 非连花采购数量配置项
      notlhPurchaseNumberOption: {},

      // 5.ERP销售流向-数据和配置项
      // 连花销售金额
      lhSaleAmount: [],
      // 连花销售金额配置项
      lhSaleAmountOption: {},
      // 连花销售数量
      lhSaleNumber: [],
      // 连花销售数量配置项
      lhSaleNumberOption: {},
      // 非连花销售金额
      notlhSaleAmount: [],
      // 非连花销售金额配置项
      notlhSaleAmountOption: {},
      // 非连花销售数量
      notlhSaleNumber: [],
      // 非连花销售数量配置项
      notlhSaleNumberOption: {},

      // 6.库存销售数量报表-数据和配置项
      //连花库存量
      lhBatchNumber: [],
      //连花库存量配置项
      lhBatchNumberOption: {},
      //非连花库存量
      notlhBatchNumber: [],
      //非连花库存量配置项
      notlhBatchNumberOption: {},
      // 库存数据
      batchTableData: {
        // 日平均库存金额
        avgAmount: 0,
        // 日平均库存量
        avgNumber: 0,
        // 差值库存金额
        diffAmount: 0,
        // 差值库存量
        diffNumber: 0,
        // 统计列表
        list: []
      },

      // 日期区间选择器配置
      dayPickerOptions: {
        // 设置不能选择的日期
        onPick: ({ maxDate, minDate }) => {
          this.choiceDate0 = minDate.getTime();
          if (maxDate) {
            this.choiceDate0 = '';
          }
        },
        disabledDate:
          (time) => {
            if (this.selectQuery == 6) {
              return time.getTime() < new Date(new Date('2022-06-07').toLocaleDateString()).getTime() || time.getTime() > new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60 * 1000 - 1
            }
            let choiceDateTime = new Date(this.choiceDate0).getTime()
            const minTime = new Date(choiceDateTime).setMonth(new Date(choiceDateTime).getMonth() - 3)
            const maxTime = new Date(choiceDateTime).setMonth(new Date(choiceDateTime).getMonth() + 3)
            const min = minTime;
            const newDate = new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60 * 1000 - 1
            const max = newDate < maxTime ? newDate : maxTime;
            //如果已经选中一个日期 则 返回 该日期前后一个月时间可选
            if (this.choiceDate0) {
              return time.getTime() < min || time.getTime() > max
            }
            //若一个日期也没选中 则 返回 当前日期以前日期可选
            return time.getTime() > newDate
          }
      }
    }
  },
  activated() {},
  created() {
    this.query.timeRange = this.timeDefault
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.echartResize)
  },
  methods: {
    // 查看
    detail(row) {
      this.selectQuery = row.value
      this.openDialog()
    },
    // 搜索商业公司
    async remoteMethod(name) {
      let query = this.query
      if (name !== '') {
        this.loadingSearch = true
        let data = await queryEnterpriseList(name, query.channelId)
        this.loadingSearch = false
        if (data !== undefined) {
          this.enterpriseList = data
        }
      } else {
        this.options = []
      }
    },
    // 2. 设置echart配置项 绘制折线图
    async getChartData() {
      await this.requestData(this.selectQuery)
      if (this.selectQuery == 1) {
        // 配置采购金额图表
        this.purchaseAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.purchaseAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.purchaseAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '采购金额'
        }
        this.purchaseAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '采购金额',
          data: this.purchaseAmount.map(item => item.number)
        }
        this.purchaseAmountOption.grid = this.commonEchartOption.grid
        this.purchaseAmountOption.tooltip = this.commonEchartOption.tooltip
        this.initEcharts('purchaseAmountRef', this.purchaseAmountOption)
        // 配置采购数量图表
        this.purchaseNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '购进日期',
          data: this.purchaseNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.purchaseNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '采购数量'
        }
        this.purchaseNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '采购数量',
          data: this.purchaseNumber.map(item => item.number)
        }
        this.purchaseNumberOption.grid = this.commonEchartOption.grid
        this.purchaseNumberOption.tooltip = this.commonEchartOption.tooltip
        this.initEcharts('purchaseNumberRef', this.purchaseNumberOption)
      } else if (this.selectQuery == 2) {
        // 配置销售金额图表
        this.saleAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.saleAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.saleAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '销售金额'
        }
        this.saleAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '销售金额',
          data: this.saleAmount.map(item => item.number)
        }
        this.saleAmountOption.grid = this.commonEchartOption.grid
        this.saleAmountOption.tooltip = this.commonEchartOption.tooltip
        this.initEcharts('saleAmountRef', this.saleAmountOption)
        // 配置销售数量图表
        this.saleNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '销售日期',
          data: this.saleNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.saleNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '销售数量'
        }
        this.saleNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '销售数量',
          data: this.saleNumber.map(item => item.number)
        }
        this.saleNumberOption.grid = this.commonEchartOption.grid
        this.saleNumberOption.tooltip = this.commonEchartOption.tooltip
        this.initEcharts('saleNumberRef', this.saleNumberOption)
      } else if (this.selectQuery == 3) {
        this.statisticsPopPurchaseOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '对比日期',
          data: this.statisticsPopPurchase.map(item => formatDate(new Date(item.poTime), 'yyyy-MM-dd'))
        }
        this.statisticsPopPurchaseOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          name: '数量'
        }
        this.statisticsPopPurchaseOption.series = [
          { name: 'POP发货金额', type: 'line', itemStyle: { color: '#5470C6' }, data: this.statisticsPopPurchase.map(item => item.erpTotalAmount)},
          { name: 'POP发货数量', type: 'line', itemStyle: { color: '#91CC75' }, data: this.statisticsPopPurchase.map(item => item.erpQuantity)},
          { name: '采购流向金额', type: 'line', itemStyle: { color: '#FAC858' }, data: this.statisticsPopPurchase.map(item => item.poTotalAmount)},
          { name: '采购流向数量', type: 'line', itemStyle: { color: '#EE6666' }, data: this.statisticsPopPurchase.map(item => item.poQuantity)}
        ]
        this.statisticsPopPurchaseOption.grid = { left: '5%', right: '5%', top: '10', containLabel: true }

        this.statisticsPopPurchaseOption.tooltip = this.commonEchartOption.tooltip

        this.initEcharts('statisticsPopPurchaseRef', this.statisticsPopPurchaseOption)
      } else if (this.selectQuery == 4) {
        // 配置连花采购金额图表
        this.lhPurchaseAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.lhPurchaseAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.lhPurchaseAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '连花采购金额'
        }
        this.lhPurchaseAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '连花采购金额',
          data: this.lhPurchaseAmount.map(item => item.number)
        }
        this.lhPurchaseAmountOption.grid = this.commonEchartOption.grid
        this.lhPurchaseAmountOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 1) {
          this.initEcharts('lhPurchaseAmountRef', this.lhPurchaseAmountOption)
        }
        // 配置连花采购数量图表
        this.lhPurchaseNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.lhPurchaseNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.lhPurchaseNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '连花采购数量'
        }
        this.lhPurchaseNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '连花采购数量',
          data: this.lhPurchaseNumber.map(item => item.number)
        }
        this.lhPurchaseNumberOption.grid = this.commonEchartOption.grid
        this.lhPurchaseNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 1) {
          this.initEcharts('lhPurchaseNumberRef', this.lhPurchaseNumberOption)
        }
        // 配置非连花采购金额图表
        this.notlhPurchaseAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.notlhPurchaseAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.notlhPurchaseAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '非连花采购金额'
        }
        this.notlhPurchaseAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '非连花采购金额',
          data: this.notlhPurchaseAmount.map(item => item.number)
        }
        this.notlhPurchaseAmountOption.grid = this.commonEchartOption.grid
        this.notlhPurchaseAmountOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 2) {
          this.initEcharts('notlhPurchaseAmountRef', this.notlhPurchaseAmountOption)
        }
        // 配置非连花采购数量图表
        this.notlhPurchaseNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '购进日期',
          data: this.notlhPurchaseNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.notlhPurchaseNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '非连花采购数量'
        }
        this.notlhPurchaseNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '非连花采购数量',
          data: this.notlhPurchaseNumber.map(item => item.number)
        }
        this.notlhPurchaseNumberOption.grid = this.commonEchartOption.grid
        this.notlhPurchaseNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 2) {
          this.initEcharts('notlhPurchaseNumberRef', this.notlhPurchaseNumberOption)
        }
      } else if (this.selectQuery == 5) {
        // 配置连花销售金额图表
        this.lhSaleAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.lhSaleAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.lhSaleAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '连花销售金额'
        }
        this.lhSaleAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '连花销售金额',
          data: this.lhSaleAmount.map(item => item.number)
        }
        this.lhSaleAmountOption.grid = this.commonEchartOption.grid
        this.lhSaleAmountOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 1) {
          this.initEcharts('lhSaleAmountRef', this.lhSaleAmountOption)
        }
        // 配置连花销售数量图表
        this.lhSaleNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.lhSaleNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.lhSaleNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '连花销售数量'
        }
        this.lhSaleNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '连花销售数量',
          data: this.lhSaleNumber.map(item => item.number)
        }
        this.lhSaleNumberOption.grid = this.commonEchartOption.grid
        this.lhSaleNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 1) {
          this.initEcharts('lhSaleNumberRef', this.lhSaleNumberOption)
        }
        // 配置非连花销售金额图表
        this.notlhSaleAmountOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.notlhSaleAmount.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.notlhSaleAmountOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '非连花销售金额'
        }
        this.notlhSaleAmountOption.series = {
          ...this.commonEchartOption.series,
          name: '非连花销售金额',
          data: this.notlhSaleAmount.map(item => item.number)
        }
        this.notlhSaleAmountOption.grid = this.commonEchartOption.grid
        this.notlhSaleAmountOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 2) {
          this.initEcharts('notlhSaleAmountRef', this.notlhSaleAmountOption)
        }
        // 配置非连花销售数量图表
        this.notlhSaleNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '销售日期',
          data: this.notlhSaleNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.notlhSaleNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '非连花销售数量'
        }
        this.notlhSaleNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '非连花销售数量',
          data: this.notlhSaleNumber.map(item => item.number)
        }
        this.notlhSaleNumberOption.grid = this.commonEchartOption.grid
        this.notlhSaleNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 2) {
          this.initEcharts('notlhSaleNumberRef', this.notlhSaleNumberOption)
        }
      } else if (this.selectQuery == 6) {
        // 配置连花库存数量图表
        this.lhBatchNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          data: this.lhBatchNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.lhBatchNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '连花库存数量'
        }
        this.lhBatchNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '连花库存数量',
          data: this.lhBatchNumber.map(item => item.number)
        }
        this.lhBatchNumberOption.grid = this.commonEchartOption.grid
        this.lhBatchNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 1) {
          this.initEcharts('lhBatchNumberRef', this.lhBatchNumberOption)
        }
        // 配置非连花库存数量图表
        this.notlhBatchNumberOption.xAxis = {
          ...this.commonEchartOption.xAxis,
          name: '入库日期',
          data: this.notlhBatchNumber.map(item => formatDate(new Date(item.time), 'yyyy-MM-dd'))
        }
        this.notlhBatchNumberOption.yAxis = {
          ...this.commonEchartOption.yAxis,
          axisLabel: {
            formatter: params => this.formatValue(params)
          },
          name: '非连花库存数量'
        }
        this.notlhBatchNumberOption.series = {
          ...this.commonEchartOption.series,
          name: '非连花库存数量',
          data: this.notlhBatchNumber.map(item => item.number)
        }
        this.notlhBatchNumberOption.grid = this.commonEchartOption.grid
        this.notlhBatchNumberOption.tooltip = this.commonEchartOption.tooltip
        if (this.query.goodsCategory == '' || this.query.goodsCategory == 2) {
          this.initEcharts('notlhBatchNumberRef', this.notlhBatchNumberOption)
        }
      }
    },
    // 1. 请求数据
    async requestData(type) {
      let query = this.query
      if (type == 1) {
        let purchaseAmount = await getlhPurchaseAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (purchaseAmount) {
          this.purchaseAmount = purchaseAmount
        } else {
          this.purchaseAmount = []
        }
        let purchaseNumber = await getlhPurchaseNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (purchaseNumber) {
          this.purchaseNumber = purchaseNumber
        } else {
          this.purchaseNumber = []
        }
        let purchaseTableData = await purchaseStatistics(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (purchaseTableData) {
          this.purchaseTableData = purchaseTableData
        } else {
          this.purchaseTableData = {}
        }
      } else if (type == 2) {
        let saleAmount = await getlhSaleAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (saleAmount) {
          this.saleAmount = saleAmount
        } else {
          this.saleAmount = []
        }
        let saleNumber = await getlhSaleNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (saleNumber) {
          this.saleNumber = saleNumber
        } else {
          this.saleNumber = []
        }
        let saleTableData = await saleStatistics(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (saleTableData) {
          this.saleTableData = saleTableData
        } else {
          this.saleTableData = {}
        }
      } else if (type == 3) {
        let data = await getStatisticsPopPurchase(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (data) {
          this.statisticsPopPurchase = data.list
          this.erpQuantityTotal = data.erpQuantityTotal
          this.erpTotalAmountTotal = data.erpTotalAmountTotal
          this.poQuantityTotal = data.poQuantityTotal
          this.poTotalAmountTotal = data.poTotalAmountTotal
        } else {
          this.statisticsPopPurchase = []
          this.erpQuantityTotal = 0
          this.erpTotalAmountTotal = 0
          this.poQuantityTotal = 0
          this.poTotalAmountTotal = 0
        }
      } else if (type == 4) {
        let lhPurchaseAmount = await getlhPurchaseAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          1, //连花品类采购金额
          query.channelId,
          query.isCloudFlag
        )
        if (lhPurchaseAmount) {
          this.lhPurchaseAmount = lhPurchaseAmount
        } else {
          this.lhPurchaseAmount = []
        }
        let notlhPurchaseAmount = await getlhPurchaseAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          2, //非连花品类采购金额
          query.channelId,
          query.isCloudFlag
        )
        if (notlhPurchaseAmount) {
          this.notlhPurchaseAmount = notlhPurchaseAmount
        } else {
          this.notlhPurchaseAmount = []
        }
        let lhPurchaseNumber = await getlhPurchaseNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          1, // 连花品类采购数量
          query.channelId,
          query.isCloudFlag
        )
        if (lhPurchaseNumber) {
          this.lhPurchaseNumber = lhPurchaseNumber
        } else {
          this.lhPurchaseNumber = []
        }
        let notlhPurchaseNumber = await getlhPurchaseNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          2, // 非连花品类采购数量
          query.channelId,
          query.isCloudFlag
        )
        if (notlhPurchaseNumber) {
          this.notlhPurchaseNumber = notlhPurchaseNumber
        } else {
          this.notlhPurchaseNumber = []
        }
      } else if (type == 5) {
        let lhSaleAmount = await getlhSaleAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          1, //连花品类销售金额
          query.channelId,
          query.isCloudFlag
        )
        if (lhSaleAmount) {
          this.lhSaleAmount = lhSaleAmount
        } else {
          this.lhSaleAmount = []
        }
        let notlhSaleAmount = await getlhSaleAmount(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          2, //非连花品类销售金额
          query.channelId,
          query.isCloudFlag
        )
        if (notlhSaleAmount) {
          this.notlhSaleAmount = notlhSaleAmount
        } else {
          this.notlhSaleAmount = []
        }
        let lhSaleNumber = await getlhSaleNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          1, // 连花品类销售数量
          query.channelId,
          query.isCloudFlag
        )
        if (lhSaleNumber) {
          this.lhSaleNumber = lhSaleNumber
        } else {
          this.lhSaleNumber = []
        }
        let notlhSaleNumber = await getlhSaleNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          2, // 非连花品类销售数量
          query.channelId,
          query.isCloudFlag
        )
        if (notlhSaleNumber) {
          this.notlhSaleNumber = notlhSaleNumber
        } else {
          this.notlhSaleNumber = []
        }
      } else if (type == 6) {
        let lhBatchNumber = await getGoodsBatchNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          1,
          query.channelId,
          query.isCloudFlag
        )
        if (lhBatchNumber) {
          this.lhBatchNumber = lhBatchNumber
        } else {
          this.lhBatchNumber = []
        }
        let notlhBatchNumber = await getGoodsBatchNumber(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          2,
          query.channelId,
          query.isCloudFlag
        )
        if (notlhBatchNumber) {
          this.notlhBatchNumber = notlhBatchNumber
        } else {
          this.notlhBatchNumber = []
        }
        let batchTableData = await goodsBatchStatistics(
          query.eid,
          query.timeRange && query.timeRange.length ? query.timeRange[0] : undefined,
          query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : undefined,
          query.goodsCategory,
          query.channelId,
          query.isCloudFlag
        )
        if (batchTableData) {
          this.batchTableData = batchTableData
        } else {
          this.batchTableData = {}
        }
      }
    },
    // 初始化图表
    initEcharts(type, option) {
      // 图表配置项
      this.echartInstance = Echarts.getInstanceByDom(this.$refs[type])
      if (this.echartInstance === undefined) {
        this.echartInstance = Echarts.init(this.$refs[type])
      }
      this.echartInstance.setOption(option)
      window.addEventListener('resize', this.echartResize)
    },
    // 重新绘制图表
    echartResize() {
      this.echartInstance.resize()
    },
    // 打开弹窗
    openDialog() {
      this.dialogShow = true
      this.$nextTick(async () => {
        if (this.selectQuery == 1 || this.selectQuery == 4) {
          this.timeTitle = '购进日期'
        } else if (this.selectQuery == 2 || this.selectQuery == 5) {
          this.timeTitle = '销售日期'
        } else if (this.selectQuery == 3) {
          this.timeTitle = '对比日期'
        } else if (this.selectQuery == 6) {
          this.timeTitle = '日期区间'
          this.query.timeRange = this.batchDefaultTime
        }
        // 获取图表数据
        await this.getChartData()
      })
    },
    // 关闭弹窗
    closeDialog() {
      this.dialogShow = false
      this.query.eid = ''
      this.query.goodsCategory = ''
      this.query.timeRange = this.timeDefault
      this.query.channelId = 0
      this.query.isCloudFlag = 0
      this.isCloud = false
      this.echartTableTitle = ''
      window.removeEventListener('resize', this.echartResize)
    },
    // 格式化区间值纵轴使多个折线图纵向样式对齐, 预设纵轴数值一共15位字符,不够填充空白
    formatValue(value, unit = '') {
      if (value.toString() !== null) {
        return String.fromCharCode(8194).repeat((15 - ((value + unit).toString().length))) + (value + unit).toString()
      }
    },
    // 选择渠道类型
    async changeChannel(value) {
      this.query.eid = ''
      if (this.query.channelId) {
        this.echartTableTitle = this.channelType.find(item => item.value == value).label
      } else {
        this.echartTableTitle = ''
      }
      await this.getChartData()
    },
    // 选择是否云仓
    async changeIsCloud(value) {
      this.query.isCloudFlag = value ? 1 : 0
      await this.getChartData()
    },
    // ERP采购流向走势-列表合计
    purchaseTableSummary() {
      return ['合计', toThousand(this.purchaseTableData.amountTotal), toThousand(this.purchaseTableData.numberTotal)]
    },
    // ERP销售流向走势-列表合计
    saleTableSummary() {
      return ['合计', toThousand(this.saleTableData.amountTotal), toThousand(this.saleTableData.numberTotal), toThousand(this.saleTableData.terminalAmountTotal), toThousand(this.saleTableData.terminalNumberTotal)]
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
