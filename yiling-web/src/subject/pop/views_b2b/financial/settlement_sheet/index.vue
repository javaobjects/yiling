<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 顶部 -->
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="last_order"></svg-icon>
            <span>今日结算金额</span>
            <div class="title">
              {{ topData.currentAmount | toThousand('￥') }}
            </div>
          </div>
        </div>
        <div class="box border-1px-r">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="today_order"></svg-icon>
            <span>昨日结算金额</span>
            <div class="title">
              {{ topData.yesterdayAmount | toThousand('￥') }}
            </div>
          </div>  
        </div>
        <div class="box">
          <div class="flex-row-center">
            <svg-icon class="svg-icon" icon-class="year_order"></svg-icon>
            <span>总计结算金额</span>
             <div class="title">
              {{ topData.totalAmount | toThousand('￥') }}
            </div>
          </div>
        </div>
      </div>
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">结算单号</div>
              <el-input v-model="query.code" @keyup.enter.native="searchEnter" placeholder="请输入结算单号" />
            </el-col>
            <el-col :span="8">
              <div class="title">生成时间</div>
              <el-date-picker
                v-model="query.scTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">结算类型</div>
              <el-select class="select-width" v-model="query.type" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in settlementType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">结算状态</div>
              <el-select class="select-width2" v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in settlementStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
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
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain v-role-btn="['5']" @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          ref="table"
          :list="dataList"
          :total="query.total"
          :row-class-name="() => 'table-row'"
          :cell-class-name="getCellClass"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="基本信息" min-width="320" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.code }}</div>
                <div class="item-text font-size-base font-title-color">
                  <span>订单数量：</span>
                  {{ row.orderCount }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>生成时间：</span>
                  {{ row.createTime | formatDate }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态信息" min-width="210" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color">
                  <span>结算状态：</span>
                  {{ row.status | dictLabel(settlementStatus) }}
                </div>
                <div class="item-text font-size-base font-title-color">
                  <span>结算类型：</span>
                  {{ row.type | dictLabel(settlementType) }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额明细" min-width="240" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div v-if="row.type == 1 || row.type == 2">
                  <div class="item-text font-size-base font-title-color">
                    <span>{{ row.type == 1 ? '货款金额：' : '促销金额：' }}</span>
                    {{ row.type == 1 ? row.goodsAmount : row.discountAmount | toThousand('￥') }}
                  </div>
                  <div class="item-text font-size-base font-title-color">
                    <span>{{ row.type == 1 ? '货款退款金额：' : '促销退款金额：' }}</span>
                    {{ row.type == 1 ? row.refundGoodsAmount : row.refundDiscountAmount | toThousand('￥-') }}
                  </div>
                </div>
                <div v-else>
                  <div class="item-text font-size-base font-title-color">
                    <span>预售违约金额：</span>
                    {{ row.presaleDefaultAmount | toThousand('￥') }}
                  </div>
                  <div class="item-text font-size-base font-title-color">
                    <span>预售违约退款金额：</span>
                    {{ row.refundPresaleDefaultAmount | toThousand('￥') }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结算金额" min-width="330" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>结算金额：</span>{{ row.amount | toThousand('￥') }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" align="left">
            <template slot-scope="{ row }">
              <div><yl-button type="text" v-role-btn="['4']" @click="showDetail(row)">查看详情</yl-button></div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { querySettlementPageList } from '@/subject/pop/api/b2b_api/financial';
import { b2bSettlementStatus, b2bSettlementType } from '@/subject/pop/utils/busi';
import { createDownLoad } from '@/subject/pop/api/common';
export default {
  name: 'SettlementSheet',
  components: {
  },
  computed: {
    settlementStatus() {
      return b2bSettlementStatus()
    },
    settlementType() {
      return b2bSettlementType()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '结算单',
          path: ''
        },
        {
          title: '结算单对账'
        }
      ],
      topData: {
        currentAmount: '',
        yesterdayAmount: '',
        totalAmount: ''
      },
      query: {
        code: '',
        scTime: '',
        status: 0,
        type: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: []
    }
  },
  activated() {
    this.getList();
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    // 重置
    handleReset() {
      this.query = {
        code: '',
        scTime: '',
        status: 0,
        type: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 导出
    async downLoadTemp() {
      let query = this.query;
      this.$common.showLoad();
      let data = await createDownLoad({
        className: 'b2bSettlementExportServiceImpl',
        fileName: '导出结算单',
        groupName: '财务管理-结算单',
        menuName: '结算单对账',
        searchConditionList: [
          {
            desc: '结算单号',
            name: 'code',
            value: query.code || ''
          },
          {
            desc: '开始生成结算单最小时间',
            name: 'minTime',
            value: query.scTime[0] || ''
          },
          {
            desc: '开始生成结算单最大时间',
            name: 'maxTime',
            value: query.scTime[1] || ''
          },
          {
            desc: '结算单类型',
            name: 'type',
            value: query.type || ''
          },
          {
            desc: '结算单状态',
            name: 'status',
            value: query.status || ''
          }
        ]
      });
      this.$log('data:', data);
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看');
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await querySettlementPageList(
        query.code,
        query.page,
        query.scTime && query.scTime.length > 1 ? query.scTime[1] : '',
        query.scTime && query.scTime.length > 0 ? query.scTime[0] : '',
        query.limit,
        query.status,
        query.type
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
        this.topData = {
          currentAmount: data.currentAmount,
          yesterdayAmount: data.yesterdayAmount,
          totalAmount: data.totalAmount
        }
      }
      this.loading = false;
    },
    getCellClass() {
      return 'border-1px-b'
    },
    // 点击查看详情
    showDetail(row) {
      this.$router.push({
        name: 'SettlementSheetDetailed',
        params: { 
          id: row.id
        }
      });
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
