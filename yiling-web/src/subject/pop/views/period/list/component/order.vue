<template>
  <yl-dialog
    :title="isHk ? '已还款订单' : '已使用订单'"
    width="70%"
    :visible.sync="show"
    :show-footer="false">
    <div class="used-box">
      <div class="box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8" v-if="isHk && (currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2)">
              <div class="title">还款状态</div>
              <el-select v-model="query.status">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="(item, idx) in repayStatus"
                  v-show="item.value != 1 && item.value != 2"
                  :key="idx"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16 pad-t-16 mar-l-16">
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
      <div class="pad-16">
        <yl-tool-tip v-if="!isHk">
          已使用额度：{{ $options.filters.toThousand(totalData.totalUsedAmount) }} 元，其中<span class="col-up">{{ $options.filters.toThousand(totalData.historyUsedAmount) }}</span> 元为系统上线前的历史已使用额度，这部分额度在系统中没有对应的订单信息 <br>
          已使用额度 = 历史已使用额度 + 订单金额
        </yl-tool-tip>
        <yl-tool-tip v-else>
          已还款额度：{{ $options.filters.toThousand(totalData.totalRepaymentAmount) }} 元，其中<span class="col-up">{{ $options.filters.toThousand(totalData.historyRepaymentAmount) }}</span> 元为系统上线前的历史已还款额度，这部分额度在系统中没有对应的订单信息 <br>
          已还款额度 = 历史已还款额度 + 订单已还款金额
        </yl-tool-tip>
      </div>
      <div class="desc">
        <span class="first-span" v-if="(currentEnterpriseInfo.yilingFlag || currentEnterpriseInfo.channelId == 2)"><span >授信主体：</span><span class="font-important-color">{{ totalData.ename }}</span></span>
        <span>采购企业：</span><span class="font-important-color">{{ totalData.customerName }}</span>
<!--        <div class="btn"><yl-button type="primary" @click="downLoadTemp" plain>导出查询结果</yl-button></div>-->
      </div>
      <div class="pad-lr-16">
        <yl-table
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="订单号" min-width="120" align="center" prop="orderNo">
          </el-table-column>
          <el-table-column label="下单时间" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div style="padding: 8px 0;">
                <span>{{ row.createTime | formatDate }}</span>
              </div>
            </template>
          </el-table-column>
<!--          <el-table-column min-width="120" align="center" v-if="!isHk" :key="Math.random()">-->
<!--            <template slot="header">-->
<!--              <div>已使用额度</div>-->
<!--              <span>{{ `（总 ${$options.filters.toThousand(totalData.totalUsedAmount)}元）` }}</span>-->
<!--            </template>-->
<!--            <template slot-scope="{ row }">-->
<!--              <span>{{ row.usedAmount | toThousand('￥') }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column min-width="120" align="center" :key="Math.random()">
            <template slot="header">
              <div>订单金额</div>
              <span>{{ `（总 ${$options.filters.toThousand(totalData.orderAmount)}元）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.orderAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
<!--          <el-table-column min-width="120" align="center" :key="Math.random()">-->
<!--            <template slot="header">-->
<!--              <div>退款金额</div>-->
<!--              <span>{{ `（总 ${$options.filters.toThousand(totalData.returnAmount)}元）` }}</span>-->
<!--            </template>-->
<!--            <template slot-scope="{ row }">-->
<!--              <span>{{ row.returnAmount | toThousand('￥') }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="还款状态" min-width="120" align="center" v-if="isHk">
            <template slot-scope="{ row }">
              <div style="padding: 8px 0;">
                <span>{{ row.repaymentStatus | dictLabel(repayStatus) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" v-if="isHk" :key="Math.random()">
            <template slot="header">
              <div>已还款金额</div>
              <span>{{ `（总 ${$options.filters.toThousand(totalData.repaymentAmount)}元）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" v-if="isHk" :key="Math.random()">
            <template slot="header">
              <div>待还款金额</div>
              <span>{{ `（总 ${$options.filters.toThousand(totalData.unRepaymentAmount)}元）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.unRepaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
  import { getUsedList, getUsedListTotalData } from '@/subject/pop/api/period'
  import { repayStatus } from '@/subject/pop/utils/busi'
  import { createDownLoad } from '@/subject/pop/api/common'
  import { mapGetters } from 'vuex'

  export default {
    name: 'Order',
    props: {
      // 是否展示
      visible: {
        type: Boolean,
        default: false
      },
      // 是否是还款订单
      isHk: {
        type: Boolean,
        default: false
      },
      // 接口入参id必传
      id: {
        type: [String, Number],
        default: '',
        required: true
      }
    },
    computed: {
      show: {
        get() {
          return this.visible
        },
        set(val) {
          this.$emit('update:visible', val)
        }
      },
      // 还款状态
      repayStatus() {
        return repayStatus()
      },
      ...mapGetters([
        'currentEnterpriseInfo'
      ])
    },
    watch: {
      visible(val) {
        if (val) {
          this.$log('open')
          this.query = {
            total: 0,
            page: 1,
            limit: 10,
            status: 0,
            createTime: []
          }
          this.getTotal()
          this.getList()
        }
      }
    },
    data() {
      return {
        query: {
          total: 0,
          page: 1,
          limit: 10,
          status: 0,
          createTime: []
        },
        // 列表
        dataList: [],
        loading: false,
        totalData: {}
      }
    },
    mounted() {
    },
    methods: {
      // 已使用订单列表
      async getList(callback) {
        this.loading1 = true
        let query = this.query
        let data = await getUsedList(
          this.isHk ? 2 : 1,
          this.id,
          query.status,
          query.page,
          query.limit,
          query.createTime && query.createTime.length ? query.createTime[0] : undefined,
          query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
        )
        this.loading1 = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
          if (callback) callback()
        }
      },
      // 获取统计数据
      async getTotal() {
        let query = this.query
        let data = await getUsedListTotalData(
          this.isHk ? 2 : 1,
          this.id,
          query.status,
          query.page,
          query.limit,
          query.createTime && query.createTime.length ? query.createTime[0] : undefined,
          query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
        )
        if (data) {
          this.totalData = this.$common.clone(data)
        }
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
        this.getTotal()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10,
          status: 0,
          createTime: []
        }
      },
      // 下载模板
      async downLoadTemp() {
        let query = this.query
        let obj = {
          className: 'orderUsePaymentDaysExportService',
          fileName: 'string',
          groupName: '已使用订单列表',
          menuName: '账期管理-已使用订单列表',
          searchConditionList: [
            {
              desc: '采购商ID',
              name: 'acountId',
              value: this.id || null
            },
            {
              desc: '下单开始时间',
              name: 'startTime',
              value: query.createTime && query.createTime.length ? query.createTime[0] : ''
            },
            {
              desc: '下单结束时间',
              name: 'endTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
            }
          ]
        }
        if (this.isHk) {
          obj = {
            className: 'returnQuotaOrderExportService',
            fileName: 'string',
            groupName: '已还款订单列表',
            menuName: '账期管理-已还款订单列表',
            searchConditionList: [
              {
                desc: '采购商ID',
                name: 'acountId',
                value: this.id || null
              },
              {
                desc: '下单开始时间',
                name: 'startTime',
                value: query.createTime && query.createTime.length ? query.createTime[0] : ''
              },
              {
                desc: '下单结束时间',
                name: 'endTime',
                value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
              },
              {
                desc: '还款状态',
                name: 'status',
                value: query.status
              }
            ]
          }
        }
        this.$common.showLoad()
        let data = await createDownLoad(obj)
        this.$common.hideLoad()
        if (data && data.result) {
          this.$common.n_success('创建下载任务成功，请在下载中心查看')
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .used-box {
    padding: 8px 8px 20px 8px;
    .desc {
      padding: 0 16px;
      height: 22px;
      font-size: 16px;
      font-weight: 400;
      color: #666666;
      line-height: 22px;
      margin-bottom: 8px;
      position: relative;
      .first-span {
        padding-right: 66px;
      }
      .btn {
        position: absolute;
        right: 16px;
        bottom: 0;
      }
    }
  }
  .box-search {
    position: relative;
    &-no-pad {
      padding: 0 !important;
    }
    .el-input {
      width: 220px;
    }
    .search-box {
      margin-top: 15px;
      padding: 0 16px;
      position: relative;
      .box {
        .title {
          font-size: $font-size-large;
          line-height: $font-size-large-lh;
          color: $font-important-color;
          margin-bottom: 8px;
        }
      }
      .el-select {
        width: 220px;
      }

      .bottom-btn {
        position: absolute;
        bottom: 0;
        right: 0;
      }
    }
  }
</style>
