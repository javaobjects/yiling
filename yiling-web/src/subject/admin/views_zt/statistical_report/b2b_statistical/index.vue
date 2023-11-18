<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">供应商名称</div>
              <el-select
                v-model="query.sellerEid"
                clearable
                filterable
                remote
                :remote-method="remoteMethod"
                :loading="searchLoading"
                placeholder="请搜索并选择供应商"
              >
                <el-option v-for="item in sellerEnameOptions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.timeRange"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">区域查询</div>
              <div class="address-select-box flex-row-left">
                <yl-choose-address
                  class="address-select-box"
                  :province.sync="query.provinceCode"
                  :city.sync="query.cityCode"
                  :area.sync="query.regionCode"
                  is-all
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div class="title">支付方式</div>
              <el-select v-model="query.paymentMethod" placeholder="请选择支付方式">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in paymentMethod"
                  v-show="item.value != 3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">商品标签</div>
              <el-select
                v-model="query.standardGoodsTagId"
                clearable
                filterable
                remote
                :remote-method="getGoodsTags"
                @clear="clearGoodsTag"
                :loading="searchTagLoading"
                placeholder="请搜索标签名并选择商品标签"
              >
                <el-option
                  v-for="item in goodsTags"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">是否会员</div>
              <el-select v-model="query.vipFlag" placeholder="请选择是否会员">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in vipFlag"
                  v-show="item.value != 10 && item.value != 100"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">订单来源</div>
              <el-select v-model="query.orderSource" placeholder="请选择订单来源">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in orderSource"
                  v-show="item.value != 1 && item.value != 2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box total-box mar-t-16">
        <div class="total-data-box">
          <div class="item-box">
            <div class="top-box">
              <div class="num">{{ totalData.originalAmount | toThousand('') }}</div>
              <div class="title">成交额</div>
            </div>
            <div class="bottom-box">
              <div class="yoy">
                <div class="num-box">
                  <div class="num">{{ totalData.originalAmountMonthRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.originalAmountMonthRatio)"></svg-icon>
                </div>
                <div class="title">环比</div>
              </div>
              <div class="mom">
                <div class="num-box">
                  <div class="num">{{ totalData.originalAmountYearRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.originalAmountYearRatio)"></svg-icon>
                </div>
                <div class="title">同比</div>
              </div>
            </div>
          </div>
          <div class="item-box">
            <div class="top-box">
              <div class="num">{{ totalData.discountAmount | toThousand('') }}</div>
              <div class="title">优惠金额</div>
            </div>
            <div class="bottom-box">
              <div class="yoy">
                <div class="num-box">
                  <div class="num">{{ totalData.discountAmountMonthRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.discountAmountMonthRatio)"></svg-icon>
                </div>
                <div class="title">环比</div>
              </div>
              <div class="mom">
                <div class="num-box">
                  <div class="num">{{ totalData.discountAmountYearRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.discountAmountYearRatio)"></svg-icon>
                </div>
                <div class="title">同比</div>
              </div>
            </div>
          </div>
          <div class="item-box">
            <div class="top-box">
              <div class="num">{{ totalData.orderQuantity | toThousand('') }}</div>
              <div class="title">订单数量</div>
            </div>
            <div class="bottom-box">
              <div class="yoy">
                <div class="num-box">
                  <div class="num">{{ totalData.orderQuantityMonthRatio	}}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.orderQuantityMonthRatio)"></svg-icon>
                </div>
                <div class="title">环比</div>
              </div>
              <div class="mom">
                <div class="num-box">
                  <div class="num">{{ totalData.orderQuantityYearRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.orderQuantityYearRatio)"></svg-icon>
                </div>
                <div class="title">同比</div>
              </div>
            </div>
          </div>
          <div class="item-box">
            <div class="top-box">
              <div class="num">{{ totalData.buyerQuantity | toThousand('') }}</div>
              <div class="title">下单客户数量</div>
            </div>
            <div class="bottom-box">
              <div class="yoy">
                <div class="num-box">
                  <div class="num">{{ totalData.buyerQuantityMonthRatio	}}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.buyerQuantityMonthRatio)"></svg-icon>
                </div>
                <div class="title">环比</div>
              </div>
              <div class="mom">
                <div class="num-box">
                  <div class="num">{{ totalData.buyerQuantityYearRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.buyerQuantityYearRatio)"></svg-icon>
                </div>
                <div class="title">同比</div>
              </div>
            </div>
          </div>
          <div class="item-box">
            <div class="top-box">
              <div class="num">{{ totalData.sellerQuantity | toThousand('') }}</div>
              <div class="title">销售供应商数量</div>
            </div>
            <div class="bottom-box">
              <div class="yoy">
                <div class="num-box">
                  <div class="num">{{ totalData.sellerQuantityMonthRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.sellerQuantityMonthRatio)"></svg-icon>
                </div>
                <div class="title">环比</div>
              </div>
              <div class="mom">
                <div class="num-box">
                  <div class="num">{{ totalData.sellerQuantityYearRatio }}%</div>
                  <svg-icon class="svg-icon" :icon-class="formatIcon(totalData.sellerQuantityYearRatio)"></svg-icon>
                </div>
                <div class="title">同比</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { paymentMethod, orderStatus, orderSource } from '@/subject/admin/utils/busi'
import { ylChooseAddress } from '@/subject/admin/components'
import { queryEnterpriseList } from '@/subject/admin/api/zt_api/dataReport'
import { requestGoodsTags, requestB2BCount } from '@/subject/admin/api/zt_api/b2b_statistical'
import { formatDate } from '@/common/utils'

export default {
  name: 'B2BStatistical',
  components: {
    ylChooseAddress
  },
  computed: {
    // 支付方式
    paymentMethod() {
      return paymentMethod()
    },
    // 订单状态
    orderStatus() {
      return orderStatus()
    },
    // 订单来源
    orderSource() {
      return orderSource()
    },
    // 环比同比增长降低icon
    formatIcon() {
      return value => {
        return value > 0 ? 'up_arrow' : (value < 0 ? 'down_arrow' : '')
      }
    },
    // 默认时间
    timeDefault () {
      const end = new Date()
      end.setTime(end.getTime() - 3600 * 1000 * 24)
      let defalutEndTime = formatDate(new Date(end), 'yyyy-MM-dd')
      let defalutStartTime = defalutEndTime
      return [defalutStartTime, defalutEndTime]
    }
  },
  data() {
    return {
      query: {
        // 下单时间
        timeRange: [],
        // 支付方式
        paymentMethod: 0,
        // 订单来源
        orderSource: 0,
        // 1-非会员 2-是会员
        vipFlag: 0,
        // 商品id标签
        standardGoodsTagId: ''
      },
      vipFlag: [
        { label: '非会员', value: 1 },
        { label: '会员', value: 2 }
      ],
      goodsTags: [],
      sellerEnameOptions: [],
      searchLoading: false,
      searchTagLoading: false,
      pickerOptions: {
        shortcuts: [
          {
            text: '上一日',
            onClick(picker) {
              const end = new Date()
              end.setTime(end.getTime() - 3600 * 1000 * 24)
              const start = end
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '当前月',
            onClick(picker) {
              const date = new Date()
              date.setDate(1)
              // 当月第一天
              const start = new Date(date)
              const end = new Date()
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '上一月',
            onClick(picker) {
              const date = new Date()
              date.setDate(1)
              date.setMonth(date.getMonth() - 1)
              // 上个月第一天(即上个月的1号)
              const start = new Date(date)
              // 上个月最后一天
              const end = new Date(new Date().setDate(0))
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '当前年',
            onClick(picker) {
              const date = new Date()
              date.setMonth(0)
              date.setDate(1)
              const start = new Date(date)
              const end = new Date()
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      totalData: {
        // 成交金额
        originalAmount: 0,
        // 成交金额环比
        originalAmountMonthRatio: 0,
        // 成交金额同比
        originalAmountYearRatio: 0,
        // 优惠金额
        discountAmount: 0,
        // 	优惠金额环比
        discountAmountMonthRatio: 0,
        // 优惠金额同比
        discountAmountYearRatio: 0,
        // 订单数据
        orderQuantity: 0,
        // 订单数据环比
        orderQuantityMonthRatio: 0,
        // 订单数据同比
        orderQuantityYearRatio: 0,
        // 下单客户数量
        buyerQuantity: 0,
        // 下单客户数量环比
        buyerQuantityMonthRatio: 0,
        // 下单客户数量同比
        buyerQuantityYearRatio: 0,
        // 销售供应商数量
        sellerQuantity: 0,
        // 销售供应商数量环比
        sellerQuantityMonthRatio: 0,
        // 销售供应商数量同比
        sellerQuantityYearRatio: 0
      }
    }
  },
  created() {
    this.query.timeRange = this.timeDefault
  },
  activated() {
    this.getGoodsTags()
    this.getB2BTotalData()
  },
  methods: {
    async getB2BTotalData() {
      if (!this.query.timeRange || this.query.timeRange.length == 0 ) {
        return this.$common.warn('请选择下单时间')
      }
      let query = this.query
      let data = await requestB2BCount(
        query.sellerEid,
        query.timeRange && query.timeRange.length ? query.timeRange[0] : '',
        query.timeRange && query.timeRange.length > 1 ? query.timeRange[1] : '',
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.paymentMethod,
        query.standardGoodsTagId,
        query.vipFlag,
        query.orderSource
      )
      if (data) {
        this.totalData = data
      }
    },
    // 搜索点击
    handleSearch() {
      this.getB2BTotalData()
    },
    handleReset() {
      this.query = {
        createTime: [],
        paymentMethod: 0,
        standardGoodsTagId: '',
        vipFlag: 0,
        orderSource: 0
      }
    },
    async remoteMethod(query) {
      if (query.trim() != '') {
        this.searchLoading = true
        let data = await queryEnterpriseList(1, 10, query.trim())
        this.searchLoading = false
        if (data) {
          this.sellerEnameOptions = data.records
        }
      } else {
        this.sellerEnameOptions = []
      }
    },
    async getGoodsTags(query = '') {
      this.searchTagLoading = true
      let data = await requestGoodsTags(1, 20, query.trim())
      this.searchTagLoading = false
      if (data && data.records.length) {
        this.goodsTags = data.records
      }
    },
    clearGoodsTag() {
      this.getGoodsTags('')
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
