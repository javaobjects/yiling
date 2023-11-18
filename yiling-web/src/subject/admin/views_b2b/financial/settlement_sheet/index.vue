<template>
  <div class="app-container">
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
            <el-col :span="6">
              <div class="title">结算单号</div>
              <el-input v-model="query.code" @keyup.enter.native="searchEnter" placeholder="请输入结算单号" />
            </el-col>
            <el-col :span="6">
              <div class="title">供应商</div>
              <el-input v-model="query.entName" @keyup.enter.native="searchEnter" placeholder="请输入供应商名称" />
            </el-col>
            <el-col :span="12">
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
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
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
            <el-col :span="6">
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
      <div class="down-box">
        <div class="down-box-left">
          <span style="padding-left:6px"><el-checkbox v-model="checked">全选</el-checkbox></span>
          <span>勾选小计(元)：<span style="color: #45a6ff">{{ subtotal }}</span></span>
          <span>总计(元)：<span style="color:#45a6ff">{{ platformTotal }}</span></span>
        </div>
        <div class="btn">
          <ylButton type="primary" plain @click="cxbatchPaymentClick">批量审批通过(促销)</ylButton>
          <ylButton type="primary" plain @click="batchPaymentClick">批量审批通过并打款(货款/预售)</ylButton>
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
          <ylButton type="primary" plain @click="downLoadDd">导出结算单订单</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8">
        <yl-table
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :row-class-name="() => 'mar-b-16'"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="session">
                  <el-checkbox 
                    v-model="checkList" 
                    :label="row" 
                    :key="row.id" 
                    :disabled="(row.status == 1 || row.status == 4) ? false : true" @change="checkboxChange">
                    {{ '' }}
                  </el-checkbox>
                  <div class="left">结算单号：{{ row.code }}</div>
                  <div class="center">
                    <span>结算状态：{{ row.status | dictLabel(settlementStatus) }}</span>
                    <span class="reason" v-if="row.status == 4" @click="reasonClick(row)">原因</span>
                  </div>
                  <span class="right">生成时间：
                    <span class="col-warn">
                      {{ row.createTime | formatDate }}
                    </span>
                  </span>
                </div>
                <div class="content flex-row-left">
                  <div class="content-left">
                    <div class="content-left-title">{{ row.ename }}</div>
                    <div class="item" style="font-size:14px;font-weight: normal;">
                      <span class="font-title-color">结算类型：</span>
                      {{ row.type | dictLabel(settlementType) }}
                    </div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="item">
                      <span class="font-title-color">订单数量：</span>
                      {{ row.orderCount }}
                    </div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="item">
                      <span class="font-title-color">
                        {{ row.type == 1 ? '货款金额：' : (row.type == 2 ? '促销金额：' : '预售违约金额：') }}
                      </span>
                      {{ row.type == 1 ? row.goodsAmount : (row.type == 2 ? row.discountAmount : row.presaleDefaultAmount) | toThousand('￥') }}
                    </div>
                    <div class="item">
                      <span class="font-title-color">
                        {{ row.type == 1 ? '货款退款金额：' : (row.type == 2 ? '促销退款金额：' : '预售退款金额：') }}
                      </span>
                      {{ row.type == 1 ? row.refundGoodsAmount : (row.type == 2 ? row.refundDiscountAmount : row.refundPresaleDefaultAmount) | toThousand('￥-') }}
                    </div>
                    <div class="item">
                      <span class="font-title-color">结算金额：</span>
                      {{ row.amount | toThousand('￥') }}
                    </div>
                  </div>
                  <div class="content-center-1 font-size-base font-important-color flex1">
                    <div class="item">
                      <span class="font-title-color">操作人：</span>
                      {{ row.updateUserName }}
                    </div>
                    <div class="item remark">
                      <span class="font-title-color">操作时间：</span>
                      {{ row.settlementTime | formatDate }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center table-button">
                    <yl-button type="text" @click="viewDetailsClick(row)">查看明细</yl-button>
                    <div v-if="row.type == 1 || row.type == 3">
                      <yl-button type="text" v-if="row.status == 1 || row.status == 4" @click="paymentClick(row)">审核通过并打款</yl-button> 
                    </div> 
                    <div v-if="row.type == 2">
                      <yl-button type="text" v-if="row.status == 1 || row.status == 4" @click="promotionClick(row)">审核通过</yl-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  <!-- 审核弹窗 -->
  <yl-dialog 
    :title="title"  
    width="700px" 
    :visible.sync="showDialog" 
    :show-footer="showFooter">
    <div class="dialogTc">
      <el-row class="dialogTc-row">
        <el-col :span="3">
          <div class="title">结算金额：</div>
        </el-col>
        <el-col :span="8">
          <div>
            {{ dialogData.money | toThousand('￥') }}
          </div>
        </el-col>
      </el-row>
      <el-row class="dialogTc-row">
        <el-col :span="3">
          <div class="title">结算备注：</div>
        </el-col>
        <el-col :span="21">
          <div>
            <el-input 
              type="textarea" 
              :rows="3" 
              show-word-limit 
              maxlength="100"
              v-model="dialogData.textArea"
              placeholder="请输入结算内容" >
            </el-input>
          </div>
        </el-col>
      </el-row>
      <div class="bottom-btn">
        <yl-button type="primary" @click="adoptClick">审核通过</yl-button>
        <yl-button plain @click="cancelClick">取消</yl-button>
      </div>
    </div>
  </yl-dialog>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { querySettlementPageList, payment } from '@/subject/admin/api/b2b_api/financial'
import { b2bSettlementType, b2bSettlementStatus } from '@/subject/admin/utils/busi'
export default {
  name: 'SettlementSheet',
  computed: {
    settlementType() {
      return b2bSettlementType()
    },
    settlementStatus() {
      return b2bSettlementStatus()
    }
  },
  data() {
    return {
      topData: {
        currentAmount: '',
        yesterdayAmount: '',
        totalAmount: ''
      },
      query: {
        code: '',
        scTime: [],
        type: 0,
        status: 0,
        entName: '',
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: [],
      checkList: [],
      //小计
      subtotal: 0, 
      //总计
      platformTotal: 0, 
      title: '促销结算审核',
      showDialog: false,
      showFooter: false,
      dialogData: {
        money: '',
        textArea: ''
      },
      // 0 批量促销 审批  否则 点击列表单个促销 审批
      promotion: 0,
      //是否全选
      checked: false
    }
  },
  watch: {
    checked: {
      handler(newValue, oldValue) {
        this.checkList = []
        if (newValue) {
          for (let i = 0; i < this.dataList.length; i ++) {
            if (this.dataList[i].status == 1 || this.dataList[i].status == 4) {
              this.checkList.push(this.dataList[i])
            }
          }
          this.checkboxChange()
        } else {
          this.subtotal = 0
        }
      }
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
    // 获取列表
    async getList() {
      this.checkList = [];
      this.checked = false;
      this.subtotal = 0;
      this.platformTotal = 0;
      this.loading = true;
      let query = this.query;
      let data = await querySettlementPageList(
        query.code,
        query.page,
        query.entName,
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
        this.platformTotal = data.platformTotal;
      }
      this.loading = false;
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
        scTime: [],
        type: 0,
        status: 0,
        entName: '',
        total: 0,
        page: 1,
        limit: 10
      }
    },
    // 打款 
    async paymentClick(row) {
      let val = [];
      val.push({settlementId: row.id})
      this.$confirm(`${row.ename} 审核后将通过支付渠道打款给供应商`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await payment(val, '')
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功！');
          this.checkList = [];
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 勾选
    checkboxChange(val) {
      this.subtotal = 0;
      let data = this.checkList;
      for (let i = 0; i < data.length; i ++) {
        this.subtotal = this.addition(this.subtotal, data[i].amount);
      }
    },
    addition(arg1, arg2) {
      var r1,r2,m;
      try {
        r1 = arg1.toString().split('.')[1].length;
      } catch (e){
        r1 = 0;
      }
      try {
        r2 = arg2.toString().split('.')[1].length;
      } catch (e){
        r2 = 0;
      }
      m = Math.pow(10, Math.max(r1, r2));
      return Math.round(((arg1 * m + arg2 * m) / m) * 100) / 100;
    },
    // 单个 促销 
    promotionClick(row) {
      this.promotion = row.id;
      this.showDialog = true;
      this.dialogData.money = row.amount;
    },
    // 批量打款 促销
    cxbatchPaymentClick() {
      this.promotion = 0;
      let valType = 1;
      if (this.checkList.length < 1) {
        this.$common.warn('请勾选促销结算订单')
      } else {
        for (let i = 0; i < this.checkList.length; i ++) {
          if (this.checkList[i].type == 2) {
            this.dialogData.money = this.subtotal;
          } else {
            valType = 2
          }
        }
        if (valType == 1) {
          this.showDialog = true;
        } else {
          this.$common.warn('请全部选择 促销 结算单，再操作批量结算！')
        }
      }
    },
    // 审核通过
    async adoptClick() {
      if (this.promotion == 0) {
        let val = [];
        for (let i = 0; i < this.checkList.length; i ++) {
          if (this.checkList[i].type == 2) {
            val.push({ settlementId: this.checkList[i].id })
          } else {
            this.$common.warn('请全部选择 促销 结算单，再操作批量结算！')
            val = [];
            return
          }
        }
        if (val.length > 0) {
          this.$common.showLoad();
          let data = await payment(val,this.dialogData.textArea)
          this.$common.hideLoad();
          if (data !== undefined) {
            this.subtotal = 0;
            this.$common.n_success('操作成功！');
            this.checkList = [];
            this.getList();
            this.showDialog = false;
            this.dialogData = {
              money: '',
              textArea: ''
            }
          }
        }
      } else {
        this.$common.showLoad();
        let data2 = await payment([{settlementId: this.promotion}], this.dialogData.textArea)
        this.$common.hideLoad();
        if (data2 !== undefined) {
          this.subtotal = 0;
          this.$common.n_success('操作成功！');
          this.getList();
          this.checkList = [];
          this.showDialog = false;
          this.dialogData = {
            money: '',
            textArea: ''
          }
        }
      }
    },
    // 取消
    cancelClick() {
      this.showDialog = false;
      this.subtotal = 0;
      this.dialogData = {
        money: '',
        textArea: ''
      }
      this.getList();
      this.checkList = []
    },
    // 批量打款 货款
    async batchPaymentClick() {
      if (this.checkList.length < 1) {
        this.$common.warn('请勾选 货款/预售 结算单！')
      } else {
        let val = [];
        for (let i = 0; i < this.checkList.length; i ++) {
          if (this.checkList[i].type == 1 || this.checkList[i].type == 3) {
            val.push({settlementId: this.checkList[i].id})
          } else {
            this.$common.warn('请全部选择 货款/预售 结算单，再操作批量结算！')
            val = [];
            return
          }
        }
        if (val.length > 0) {
          this.$confirm('审核后将通过支付渠道打款给供应商', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            this.$common.showLoad();
            let data = await payment(val, '')
            this.$common.hideLoad();
            if (data !== undefined) {
              this.subtotal = 0;
              this.$common.n_success('操作成功！');
              this.getList();
              this.checkList = [];
            }
          })
          .catch(() => {
            //取消
          });
        }
      }
    },
    // 导出
    async downLoadTemp() {
      let query = this.query;
      this.$common.showLoad()
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
            value: query.scTime && query.scTime.length > 0 ? query.scTime[0] : ''
          },
          {
            desc: '开始生成结算单最大时间',
            name: 'maxTime',
            value: query.scTime && query.scTime.length > 1 ? query.scTime[1] : ''
          },
          {
            desc: '供应商名称',
            name: 'entName',
            value: query.entName || ''
          },
          {
            desc: '结算单类型',
            name: 'type',
            value: query.type
          },
          {
            desc: '结算单状态',
            name: 'status',
            value: query.status
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    //导出结算单订单
    async downLoadDd() {
      let query = this.query;
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'b2bSettlementSimpleInfoExportServiceImpl',
        fileName: '导出结算单订单',
        groupName: '财务管理-结算单订单',
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
            value: query.scTime && query.scTime.length > 0 ? query.scTime[0] : ''
          },
          {
            desc: '开始生成结算单最大时间',
            name: 'maxTime',
            value: query.scTime && query.scTime.length > 1 ? query.scTime[1] : ''
          },
          {
            desc: '供应商名称',
            name: 'entName',
            value: query.entName || ''
          },
          {
            desc: '结算单类型',
            name: 'type',
            value: query.type
          },
          {
            desc: '结算单状态',
            name: 'status',
            value: query.status
          }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 点击查看详情
    viewDetailsClick(row) {
      this.$router.push({
        name: 'SettlementSheetDetailed',
        params: { 
          id: row.id
        }
      });
    },
    // 点击原因
    reasonClick(row) {
      this.$alert(row.errMsg, '银行处理失败原因', {
        confirmButtonText: '确定'
      });
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
