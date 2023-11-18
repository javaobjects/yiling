<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">企业名称</div>
              <el-input v-model="query.customerName" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
            </el-col>
            <el-col :span="7">
              <div class="title">企业编码</div>
              <el-input v-model="query.easCode" @keyup.enter.native="searchEnter" placeholder="请输入企业编码" />
            </el-col>
            <el-col :span="7">
              <div class="title">社会统一信用代码</div>
              <el-input v-model="query.licenseNumber" @keyup.enter.native="searchEnter" placeholder="请输入社会统一信用代码" />
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
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          ref="table"
          :list="dataList"
          :total="query.total"
          :cell-class-name="() => 'border-1px-b'"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="getList"
          >
          <el-table-column label-class-name="mar-l-16" label="企业信息" min-width="380" align="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold">{{ row.name || '- -' }}</div>
                <div class="item-text font-size-base font-title-color"><span>社会统一信用代码：</span>{{ row.licenseNumber || '- - ' }}</div>
                <div class="item-text font-size-base font-title-color"><span>渠道类型：</span>{{ row.channelId | dictLabel(channelType) }}</div>
                <div class="item-text font-size-base font-title-color"><span>企业编码：</span>{{ row.easCode || '- - ' }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议财务信息" min-width="284" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>返利入账次数：</span>{{ row.discountCount || '- - ' }}</div>
                <div class="item-text font-size-base font-title-color"><span>返利使用次数：</span>{{ row.usedCount || '- - ' }}</div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="已兑付金额" min-width="306" align="left">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="title"></div>
                <div class="item-text font-size-base font-title-color"><span>已兑付金额：</span>{{ row.discountAmount | toThousand('￥') }}<yl-button class="detail-btn" type="text" @click="discountAmountDetail(row)">查看</yl-button></div>
                <div class="item-text font-size-base font-title-color"><span>已使用金额：</span>{{ row.usedAmount | toThousand('￥') }}<yl-button class="detail-btn" type="text" @click="usedAmountDetail(row)">查看</yl-button></div>
                <div class="item-text font-size-base font-title-color"></div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    
    </div>
      <!-- 已兑付金额列表 -->
    <yl-dialog title="已兑付返利列表" :visible.sync="showDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div class="dialog-content-top">
          <div class="flex-row-left item mar-b-10">
            <div class="line-view"></div>
            <span class="font-size-lg bold">{{ currentDetail.name }}</span>
          </div>
          <el-row>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">渠道类型：</span>{{ currentDetail.channelId | dictLabel(channelType) }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">企业编码：</span>{{ currentRow.easCode }}</div></el-col>
            <el-col :span="8"><div class="font-size-base font-important-color item-text"><span class="font-title-color">已兑付总金额：</span>{{ currentRow.discountAmount | toThousand('￥') }}</div></el-col>
          </el-row>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <div class="down-box clearfix mar-b-10">
            <div class="btn">
              <ylButton type="primary" plain @click="dialogDownLoadTemp">导出查询结果</ylButton>
            </div>
          </div>
          <yl-table
            stripe
            border
            show-header
            :list="providerList"
            :total="providerQuery.total"
            :page.sync="providerQuery.page"
            :limit.sync="providerQuery.limit"
            :loading2="loading2"
          >
            <el-table-column label-class-name="mar-l-16" label="企业信息" min-width="240" align="left">
              <template slot-scope="{ row }">
                <div class="item text-l mar-l-16">
                  <div class="title font-size-lg bold">{{ row.name || '- -' }}</div>
                  <div class="item-text font-size-base font-title-color"><span>渠道类型：</span>{{ row.channelId | dictLabel(channelType) }}</div>
                  <div class="item-text font-size-base font-title-color"><span>企业状态：</span>{{ row.entStatus | enable }}</div>
                  <div class="item-text font-size-base font-title-color"><span>企业编码：</span>{{ row.easCode || '- - ' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="申请入账信息" min-width="240" align="left">
              <template slot-scope="{ row }">
                <div class="item">
                  <div class="title"></div>
                  <div class="item-text font-size-base font-title-color"><span>申请金额：</span>{{ row.totalAmount | toThousand('￥') }}<yl-button class="detail-btn" type="text" @click="showDetail(row)">查看</yl-button></div>
                  <div class="item-text font-size-base font-title-color"><span>申请状态：</span>{{ row.status | dictLabel(statusArray) }}</div>
                  <div class="item-text font-size-base font-title-color"><span>归属年月：</span>{{ row.dateTypeStr || '- - ' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="申请人/时间" min-width="300" align="left">
              <template slot-scope="{ row }">
                <div class="item">
                  <div class="title"></div>
                  <div class="item-text font-size-base font-title-color"><span>申请人：</span>{{ row.createUserName || '- -' }}</div>
                  <div class="item-text font-size-base font-title-color"><span>申请时间：</span>{{ row.createTime | formatDate }}</div>
                  <div class="item-text font-size-base font-title-color"><span>申请单号：</span>{{ row.code || '- - ' }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100" align="center" fixed="right">
              <template slot-scope="{ row }">
                <div><yl-button type="text" @click="downShowDetail(row)">下载明细</yl-button></div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { createDownLoad } from '@/subject/admin/api/common'
import { queryFinancialRebateEntPageList, queryFinanceApplyListPageList } from '@/subject/admin/api/reconciliation';
import { agreementApplyStatus, channelType } from '@/subject/admin/utils/busi';
export default {
  name: 'ReconciliationRebate',
  components: {},
  computed: {
    agreementApplyStatus() {
      return agreementApplyStatus()
    },
    channelType() {
      return channelType()
    }
  },
  data() {
    return {
      query: {
        customerName: '',
        easCode: '',
        licenseNumber: '',
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
       // 申请单状态
      statusArray: [
        {
          label: '待审核',
          value: 1
        },
        {
          label: '已入账',
          value: 2
        },
        {
          label: '驳回',
          value: 3
        }
      ],
      // 已兑付返利列表
      loading2: false,
      currentRow: {},
      showDialog: false,
      currentDetail: {},
      providerList: [], //列表数据
      providerQuery: {
        total: 0,
        page: 1,
        limit: 10
      }
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    // 获取数据
    async getList() {
      this.loading = true;
      let val = this.query;
      let data = await queryFinancialRebateEntPageList(
        val.page,val.customerName,val.easCode,val.licenseNumber,val.limit
      )
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },

    // 点击查询
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    // 点击重置
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      }
    },
    // 点击 以兑付金额 查看
    discountAmountDetail(val) {
      this.currentRow = val;
      this.showDialog = true;
      this.currentDetail = {};
      this.queryFinanceApplyListPageList();//调取接口
    },
    async queryFinanceApplyListPageList() {
      this.loading2 = true
      let providerQuery = this.providerQuery;
      let data = await queryFinanceApplyListPageList(
        providerQuery.page,this.currentRow.easCode,this.currentRow.id,providerQuery.limit
      )
     this.loading2 = false;
      if (data) {
        this.currentDetail = data;
        this.providerList = data.records;
        this.providerQuery.total = data.total;
      }
    },
    // 点击 已使用金额
    usedAmountDetail(val) {
      this.$router.push({
        name: 'ReconciliationRebateInfo',
        params: { easCode: val.easCode,eid: val.id,usedAmount: val.usedAmount }
      });
    },
    // 已兑付金额
    //导出查询结果
    async dialogDownLoadTemp() {
      let item = this.currentRow;
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementApplyBatchExportService',
        fileName: '导出申请单',
        groupName: '返利申请导出',
        menuName: '返利对账 - 财务',
        searchConditionList: [
          {
            desc: '企业id',
            name: 'enterpriseId',
            value: item.id || ''
          },
          {
            desc: '企业编码',
            name: 'easCode',
            value: item.easCode || ''
          }
          // {
          //   desc: '查询类型（默认商务） 1-商务 2-财务',
          //   name: 'queryType',
          //   value: 2
          // }
        ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 点击 以兑付返利列表的 查看
    showDetail(val) {
      this.showDialog = false;
      this.$router.push({
        name: 'ReconciliationRebateApplication',
        params: { id: val.id }
      });
    },
    //点击下载明细
    async downShowDetail(val) {
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'agreementApplyDetailExportService',
        fileName: '返利申请单',
        groupName: '返利申请导出',
        menuName: '企业返利 - 财务',
        searchConditionList: [
          {
            desc: '申请单id',
            name: 'applyId',
            value: val.id || ''
          }
        ]
      })
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    }

  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
</style>
