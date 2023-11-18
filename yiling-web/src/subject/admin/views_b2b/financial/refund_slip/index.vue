<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" @keyup.enter.native="searchEnter" placeholder="请输入订单号" />
            </el-col>
            <el-col :span="6">
              <div class="title">供应商名称</div>
              <el-input v-model="query.sellerName" @keyup.enter.native="searchEnter" placeholder="请输入供应商名称" />
            </el-col>
            <el-col :span="12">
              <div class="title">创建时间</div>
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
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">采购商名称</div>
              <el-input v-model="query.buyerName" @keyup.enter.native="searchEnter" placeholder="请输入采购商名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">退款状态</div>
              <el-select class="select-width" v-model="query.refundStatus" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in tktzData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">退款来源</div>
              <el-select class="select-width2" v-model="query.refundType" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in tklyData"
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
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
    <div class="mar-t-8 bottom-content-view">
      <yl-table
        :list="dataList"
        :total="query.total"
        :page.sync="query.page"
        :limit.sync="query.limit"
        :loading="loading"
        :horizontal-border="false"
        :cell-no-pad="true"
        @getList="getList"
        >
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session font-size-base flex-between">
                  <div>
                    <span>
                      退款单号：
                      <span>{{ row.refundNo }}</span>
                    </span>
                    <span class="mar-l-32">
                      退款单状态：
                      <span>{{ row.refundStatus == 1 ? '待退款' : (row.refundStatus == 2 ? '退款中' : (row.refundStatus == 3 ? '退款成功 ' : '退款失败	') ) }}</span>
                      <span class="class-color" v-if="row.refundStatus == 4" @click="reasonClick(row)">查看原因</span>
                    </span>
                  </div>
                  <div>
                    <span>退款单创建时间：{{ row.createTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content">
                  <div class="content-center">
                    <div class="content-center-top">
                      {{ row.buyerEid }}
                      <p>{{ row.buyerEname }}</p>
                    </div>
                    <div class="content-center-bottom">
                      <div class="item font-size-base font-title-color">
                        {{ row.sellerEid }}
                        <p>{{ row.sellerEname }}</p> 
                      </div>
                    </div>
                  </div>
                  <div class="content-left content-w-max table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">退款来源：</span>
                      {{ row.refundType | dictLabel(tklyData) }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">关联单号：</span>
                      {{ row.returnNo }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">订单编号：</span>
                      {{ row.orderNo }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">订单支付方式：</span>
                      {{ row.payWay | dictLabel(zffsData) }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付流水号：</span>
                      {{ row.thirdTradeNo }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">退款流水号：</span>
                      {{ row.thirdFundNo }}
                    </div>
                  </div>
                  <div class="content-left content-w-min table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">订单总额：</span>
                      {{ row.totalAmount }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付总额：</span>
                      {{ row.paymentAmount }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">退款总额：</span>
                      {{ row.refundAmount }}
                    </div>
                  </div>
                  <div class="content-left content-w-min table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作人：</span>
                      {{ row.operateUserName }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作时间：</span>
                      {{ row.operateTime | formatDate }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center">
                    <div v-if="row.refundStatus == 4">
                      <yl-button type="text" @click="financeClick(row)">财务管理</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="退款失败原因" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="dialogTc">
        {{ failReason }}
      </div>
    </yl-dialog>
    <yl-dialog title="退款失败财务管理" @confirm="confirm2" width="570px" :visible.sync="showDialog2">
      <div class="dialogTc">
        <el-radio-group v-model="radioGroup.start">
          <el-radio :label="1">已退款,仅标记已处理</el-radio>
          <el-radio :label="2">未退款,通过接口退款</el-radio>
        </el-radio-group>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { tkdPageList, tkdRetry } from '@/subject/admin/api/b2b_api/financial'
import { createDownLoad } from '@/subject/admin/api/common'
import { paymentChannel, financeOrderFundType } from '@/subject/admin/utils/busi'
export default {
  name: 'RefundSlip',
  components: {
  },
  computed: {
    zffsData() {
      return paymentChannel()
    },
    tklyData() {
      return financeOrderFundType()
    }
  },
  data() {
    return {
      query: {
        buyerName: '',
        scTime: [],
        orderNo: '',
        refundStatus: 0,
        refundType: 0,
        sellerName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      tktzData: [
        {
          label: '待退款',
          value: '1'
        },
        {
          label: '退款中',
          value: '2'
        },
        {
          label: '退款成功',
          value: '3'
        },
        {
          label: '退款失败',
          value: '4'
        }
      ],
      loading: false,
      dataList: [],
      showDialog: false,
      //退款失败原因
      failReason: '', 
      //财务管理状态
      radioGroup: { 
        start: '',
        //退款单id
        refundId: '' 
      },  
      showDialog2: false,
      //退款失败财务处理
      failReason2: '' 
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
        buyerName: '',
        scTime: [],
        orderNo: '',
        refundStatus: 0,
        refundType: 0,
        sellerName: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 点击查看原因
    reasonClick(row) {
      this.showDialog = true;
      this.failReason = row.failReason;
    },
    // 导出
    async downLoadTemp() {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2BRefundExportService',
        fileName: '导出退款单',
        groupName: '退款单申请导出',
        menuName: '退款单列表',
        searchConditionList: [
          {
            desc: '订单号',
            name: 'orderNo',
            value: this.query.orderNo || ''
          },
          {
            desc: '供应商名称',
            name: 'sellerName',
            value: this.query.sellerName || ''
          },
          {
            desc: '采购商名称',
            name: 'buyerName',
            value: this.query.buyerName || ''
          },
          {
            desc: '退款状态',
            name: 'refundStatus',
            value: this.query.refundStatus || ''
          },
          {
            desc: '退款类型',
            name: 'refundType',
            value: this.query.refundType || ''
          },
          {
            desc: '开始时间',
            name: 'createStartTime',
            value: this.query.scTime && this.query.scTime.length > 0 ? this.query.scTime[0] : ''
          },
          {
            desc: '结束时间',
            name: 'createStopTime',
            value: this.query.scTime && this.query.scTime.length > 1 ? this.query.scTime[1] : ''
          }
        ]
      })
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await tkdPageList(
        query.buyerName,
        query.scTime && query.scTime.length > 0 ? query.scTime[0] : '',
        query.scTime && query.scTime.length > 1 ? query.scTime[1] : '',
        query.page,
        query.orderNo,
        query.refundStatus,
        query.refundType,
        query.sellerName,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击查看详情
    viewDetailsClick(row) {
      this.$router.push({
        name: 'SettlementSheetDetailed',
        params: {}
      })
    },
    // 关闭 查看原因的弹出窗
    confirm() {
      this.showDialog = false
    },
    // 点击财务处理
    financeClick(row) {
      this.radioGroup.refundId = row.id;
      this.showDialog2 = true
    },
    // 点击退款失败财务管理 确定
    async confirm2() {
      let val = this.radioGroup;
      if (val.start == '') {
        this.$common.error('请选择财务处理方式')
      } else {
        this.$common.showLoad();
        let data = await tkdRetry(
          val.start,
          val.refundId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.showDialog2 = false;
          this.$common.n_success('操作成功')
          this.getList();
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
