<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box box-search">
        <el-tabs v-model="activeTab" type="border-card">
          <el-tab-pane label="订单信息" name="orderInfo">
            <div class="tab-content order-tab">
              <div class="search-box">
                <el-row class="box">
                  <el-col :span="6">
                    <div class="title">数据类型</div>
                    <el-select v-model="query.recordType" placeholder="请选择数据类型">
                      <el-option
                        v-for="item in queryOrderRecordType"
                        :label="item.label"
                        :value="item.value"
                        :key="item.id"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="6">
                    <div class="title">商品品类</div>
                    <el-select v-model="query.categoryType" placeholder="请选择商品品类">
                      <el-option
                        v-for="item in queryOrderCategoryType"
                        :label="item.label"
                        :value="item.value"
                        :key="item.id"
                      ></el-option>
                    </el-select>
                  </el-col>
                  <el-col :span="6">
                    <div class="title">日期</div>
                    <el-date-picker
                      v-model="query.createTime"
                      type="daterange"
                      format="yyyy/MM/dd"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      value-format="yyyy-MM-dd"
                    >
                    </el-date-picker>
                  </el-col>
                </el-row>
              </div>
              <div class="search-btn-box">
                <el-row class="box">
                  <el-col>
                    <ylButton type="primary" plain @click="downLoadTemp">导出</ylButton>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="数据统计" name="statisticalData">
            <div class="tab-content statistical-tab">
              <div class="info">
                <yl-tool-tip>说明：所有统计数据为非时实数据，所有数据截止为昨日数据 年统计范围：自然年 月统计范围：自然月 日统计范围：自然日</yl-tool-tip>
              </div>
              <div class="statistical-item-wrap">
                <div class="statistical-item pop-buy">
                  <div class="icon pop-buy-icon">年</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.buyPOPYearAmount | toThousand('') }}万元</div>
                    <div class="info">POP年购进总金额</div>
                  </div>
                </div>
                <div class="statistical-item pop-buy">
                  <div class="icon pop-buy-icon">月</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.buyPOPMonthAmount | toThousand('') }}万元</div>
                    <div class="info">POP月购进总金额</div>
                  </div>
                </div>
                <div class="statistical-item pop-buy">
                  <div class="icon pop-buy-icon">日</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.buyPOPDayAmount | toThousand('') }}万元</div>
                    <div class="info">POP日购进总金额</div>
                  </div>
                </div>
              </div>
              <div class="statistical-item-wrap">
                <div class="statistical-item pop-income">
                  <div class="icon pop-income-icon">年</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.backPOPYearAmount | toThousand('') }}万元</div>
                    <div class="info">POP年回款总金额</div>
                  </div>
                </div>
                <div class="statistical-item pop-income">
                  <div class="icon pop-income-icon">月</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.backPOPMonthAmount | toThousand('') }}万元</div>
                    <div class="info">POP月回款总金额</div>
                  </div>
                </div>
                <div class="statistical-item pop-income">
                  <div class="icon pop-income-icon">日</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.backPOPDayAmount | toThousand('') }}万元</div>
                    <div class="info">POP日回款总金额</div>
                  </div>
                </div>
              </div>
              <div class="statistical-item-wrap">
                <div class="statistical-item b2b">
                  <div class="icon b2b-icon">年</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.sellB2BYearAmount | toThousand('') }}万元</div>
                    <div class="info">B2B年动销总金额</div>
                  </div>
                </div>
                <div class="statistical-item b2b">
                  <div class="icon b2b-icon">月</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.sellB2BMonthAmount | toThousand('') }}万元</div>
                    <div class="info">B2B月动销总金额</div>
                  </div>
                </div>
                <div class="statistical-item b2b">
                  <div class="icon b2b-icon">日</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.sellB2BDayAmount | toThousand('') }}万元</div>
                    <div class="info">B2B日动销总金额</div>
                  </div>
                </div>
              </div>
              <div class="statistical-item-wrap">
                <div class="statistical-item platform">
                  <div class="icon platform-icon">年</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.ownSetPlatformYearAmount | toThousand('') }}万元</div>
                    <div class="info">自建平台年总金额</div>
                  </div>
                </div>
                <div class="statistical-item platform">
                  <div class="icon platform-icon">月</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.ownSetPlatformMonthAmount | toThousand('') }}万元</div>
                    <div class="info">自建平台月总金额</div>
                  </div>
                </div>
                <div class="statistical-item platform">
                  <div class="icon platform-icon">日</div>
                  <div class="data-box">
                    <div class="count">{{ statisticalData.ownSetPlatformDayAmount | toThousand('') }}万元</div>
                    <div class="info">自建平台日总金额</div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { getStatistics } from '@/subject/admin/api/zt_api/statistical_data'
import { orderRecordType, orderCategoryType } from '@/subject/admin/utils/busi'
export default {
  name: 'StatisticalData',
  components: {},
  computed: {
    // 业务类型
    queryOrderRecordType() {
      return orderRecordType()
    },
    // 系统标识
    queryOrderCategoryType() {
      return orderCategoryType()
    }
  },
  data() {
    return {
      activeTab: 'orderInfo',
      query: {
        // 数据类型
        recordType: '',
        //  商品品类
        categoryType: '',
        // 日期
        createTime: []
      },
      loading: false,
      statisticalData: {
        backPOPDayAmount: '', // POP日回款总金额
        backPOPMonthAmount: '', // POP月回款总金额
        backPOPYearAmount: '', // POP年回款总金额
        buyPOPDayAmount: '', // POP日购进总金额
        buyPOPMonthAmount: '', // POP月购进总金额
        buyPOPYearAmount: '', // POP年购进总金额
        ownSetPlatformDayAmount: '', // 自建平台日总金额
        ownSetPlatformMonthAmount: '', // 自建平台月总金额
        ownSetPlatformYearAmount: '', // 自建平台年总金额
        sellB2BDayAmount: '', // B2B日动销总金额
        sellB2BMonthAmount: '', // B2B月动销总金额
        sellB2BYearAmount: '' // B2B年动销总金额
      }
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let data = await getStatistics()
      this.loading = false
      if (data) {
        this.statisticalData = data
      }
    },
    // 导出
    async downLoadTemp() {
      let query = this.query
      if (query.recordType == '' || query.categoryType == '' || query.createTime === null || query.createTime.length == 0) {
        this.$common.warn('请选择数据类型和商品品类和日期');
        return false
      } else {
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'orderDataReportExportService',
          fileName: '导出统计报表订单信息',
          groupName: '运营后台统计报表',
          menuName: '数据信息-订单信息',
          searchConditionList: [
            {
              desc: '数据类型',
              name: 'recordType',
              value: query.recordType
            },
            {
              desc: '商品品类',
              name: 'categoryType',
              value: query.categoryType
            },
            {
              desc: '开始时间',
              name: 'startCreatTime',
              value: query.createTime && query.createTime.length ? query.createTime[0] : ''
            },
            {
              desc: '结束时间',
              name: 'endCreatTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
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
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
