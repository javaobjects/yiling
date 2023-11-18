<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box" >
          <el-row class="box">
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.appOrderNo" @keyup.enter.native="searchEnter" placeholder="请输入订单号" />
            </el-col>
            <el-col :span="6">
              <div class="title">处理状态</div>
              <el-select class="select-width" v-model="query.dealState" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in clztData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <div class="title">支付时间</div>
              <el-date-picker
                v-model="query.zfTime"
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
              <div class="title">支付方式</div>
               <el-select class="select-width" v-model="query.payWay" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in zffsData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">单据类型</div>
              <el-select class="select-width" v-model="query.tradeType" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in djlxData"
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
      <!-- <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div> -->
      <!-- 底部列表 -->
    <div class="mar-t-8 bottom-content-view" style="padding-bottom: 10px;background: #FFFFFF;">
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
                      订单号：
                      <span>{{ row.appOrderNo }}</span>
                    </span>
                    <span class="mar-l-32">
                      处理状态：
                      <span>{{ row.dealState | dictLabel(clztData) }}</span>
                       <span class="reason" v-if="row.dealState == 3" @click="reasonClick(row)">原因</span>
                    </span>
                  </div>
                  <div>
                    <span>创建时间：{{ row.refundTime | formatDate }}</span>
                  </div>
                </div>
                <div class="content">
                  <div class="content-center">
                    <div class="content-center-top">
                      采购商信息
                      <p>{{ row.buyerName }}</p>
                    </div>
                    <div class="content-center-bottom">
                      <div class="item font-size-base font-title-color">
                        供应商信息
                        <p>{{ row.sellerName }}</p>
                        
                      </div>
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <!-- <div class="item font-size-base font-title-color">
                      <span class="font-light-color">交易订单号：</span>
                      {{ row.appOrderNo }}
                    </div> -->
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付流水号：</span>
                      {{ row.payNo }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付时间：</span>
                      {{ row.payDate | formatDate }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付方式：</span>
                      {{ row.payWay | dictLabel(zffsData) }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">单据类型：</span>
                      {{ row.tradeType | dictLabel(djlxData) }}
                    </div>
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">订单金额：</span>
                      {{ row.orderAmount | toThousand('￥') }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">支付金额：</span>
                      {{ row.payAmount | toThousand('￥') }}
                    </div>
                    <!-- <div class="item font-size-base font-title-color">
                      <span class="font-light-color">退款总额：</span>
                      43242424234
                    </div> -->
                  </div>
                  <div class="content-left table-item">
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作人：</span>
                      {{ row.updateUserName }}
                    </div>
                    <div class="item font-size-base font-title-color">
                      <span class="font-light-color">操作时间：</span>
                     {{ row.updateTime | formatDate }}
                    </div>
                  </div>
                  
                  <div class="content-right flex-column-center" v-if="row.dealState == 3 || row.dealState == 1">
                    <!-- <div>
                      <yl-button type="text" @click="showDetail(row)">查看详情</yl-button>
                    </div> -->
                    <div>
                      <yl-button type="text" @click="paymentClick(row)">财务管理</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog title="重复支付财务处理" @confirm="confirm" width="570px" :visible.sync="showDialog">
      <div class="dialogTc">
        <el-radio-group v-model="radioGroup.methodType">
          <el-radio :label="1">未退款,通过接口退款</el-radio>
          <el-radio :label="2">已退款,仅标记已处理</el-radio>
        </el-radio-group>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { repeat, process } from '@/subject/admin/api/b2b_api/financial'
import { paymentChannel ,paymentOrderType , paymentOrderDealType } from '@/subject/admin/utils/busi'
export default {
  name: 'Payment',
  components: {
  },
  computed: {
    zffsData() {
      return paymentChannel()
    },
    djlxData() {
      return paymentOrderType()
    },
    clztData() {
      return paymentOrderDealType()
    }
  },
  data() {
    return {
      query: {
        appOrderNo: '',
        zfTime: [],
        payWay: 0,
        dealState: 0,
        tradeType: 0,
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: [],
      radioGroup: { //财务管理状态
        methodType: '', //退款处理方式：1，未退款通过接口退款，2，已退款标记已处理
        repeatOrderId: '' //退款单id
      }, 
      showDialog: false
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
        appOrderNo: '',
        zfTime: [],
        payWay: 0,
        dealState: 0,
        tradeType: 0,
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 导出
    downLoadTemp() {
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await repeat(
        query.appOrderNo,
        query.page,
        query.zfTime && query.zfTime.length > 0 ? query.zfTime[1] : '',
        query.payWay,
        query.dealState,
        query.limit,
        query.zfTime && query.zfTime.length > 0 ? query.zfTime[0] : '',
        query.tradeType
      )
      if (data != undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    getCellClass({row,rowIndex}) {
      return 'border-1px-b'
    },
    // 点击财务管理
    paymentClick(row) {
      this.radioGroup.repeatOrderId = row.id;
      this.showDialog = true;
    },
    async confirm() {
      let val = this.radioGroup;
      if (val.methodType == '') {
        this.$common.error('请选择财务处理方式')
      } else {
        this.$common.showLoad();
        let data = await process(
          val.methodType,
          val.repeatOrderId
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.showDialog = false;
          this.$common.n_success('操作成功')
          this.getList();
        }
      }
    },
    // 点击原因
    reasonClick(row) {
      this.$alert(row.errorMsg, '失败原因', {
        confirmButtonText: '确定'
      });
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./index.scss";
  .table-button::v-deep .el-button--medium {
    padding: 6px 20px;
  }
</style>
