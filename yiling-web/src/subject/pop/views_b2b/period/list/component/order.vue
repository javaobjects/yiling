<template>
  <yl-dialog
    :title="renderTitle"
    width="70%"
    :visible.sync="show"
    :show-footer="false">
    <div class="used-box">
      <div class="box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8" v-if="checkType == 2">
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
      <div class="desc mar-t-16">
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
          <el-table-column min-width="120" align="center" v-if="checkType == 1" :key="Math.random()">
            <template slot="header">
              <div>已使用额度（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.usedAmount)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.usedAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" v-if="checkType == 3" :key="Math.random()">
            <template slot="header">
              <div>待还款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.unRepaymentAmount)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.unRepaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" :key="Math.random()">
            <template slot="header">
              <div>订单金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.orderAmount)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.orderAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" :key="Math.random()">
            <template slot="header">
              <div>退款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.returnAmount)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.returnAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="120" align="center" v-if="checkType == 2" :key="Math.random()">
            <template slot="header">
              <div>已还款金额（元）</div>
              <span>{{ `（总计：${$options.filters.toThousand(totalData.repaymentAmount)}）` }}</span>
            </template>
            <template slot-scope="{ row }">
              <span>{{ row.repaymentAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="还款状态" min-width="120" align="center" v-if="checkType == 2">
            <template slot-scope="{ row }">
              <div style="padding: 8px 0;">
                <span>{{ row.repaymentStatus | dictLabel(repayStatus) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="还款时间" min-width="120" align="center" v-if="checkType == 2">
            <template slot-scope="{ row }">
              <div style="padding: 8px 0;">
                <span>{{ row.repaymentTime | formatDate }}</span>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
  import { getUsedList, getRepaymentList, getNeedRepaymentList, getListTotalData } from '@/subject/pop/api/b2b_api/period'
  import { repayStatus } from '@/subject/pop/utils/busi'

  export default {
    name: 'Order',
    props: {
      // 是否展示
      visible: {
        type: Boolean,
        default: false
      },
      // 查看类别
      checkType: {
        type: [String, Number],
        default: '1'
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
      // 标题
      renderTitle() {
        if (this.checkType == 1) {
          return '已使用订单'
        } else if (this.checkType == 2) {
          return '已还款订单'
        } else if (this.checkType == 3) {
          return '待还款订单'
        }
        return ''
      }
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
      async getList() {
        this.loading1 = true
        let query = this.query
        let data = null
        if (this.checkType == 1) {
          data = await getUsedList(
            this.id,
            query.status,
            query.page,
            query.limit,
            query.createTime && query.createTime.length ? query.createTime[0] : undefined,
            query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
          )
        } else if (this.checkType == 2) {
          data = await getRepaymentList(
            this.id,
            query.status,
            query.page,
            query.limit,
            query.createTime && query.createTime.length ? query.createTime[0] : undefined,
            query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
          )
        } else if (this.checkType == 3) {
          data = await getNeedRepaymentList(
            this.id,
            query.status,
            query.page,
            query.limit,
            query.createTime && query.createTime.length ? query.createTime[0] : undefined,
            query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined
          )
        }
        this.loading1 = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      // 获取统计数据
      async getTotal() {
        let query = this.query
        let data = await getListTotalData(
          this.id,
          query.status,
          this.checkType,
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
