<template>
  <div class="app-container">
    <!-- 上部企业信息 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 企业信息  -->
      <div class="top-box">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">{{ data.name || '- -' }}</span>
        </div>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">渠道类型：{{ data.channelId | dictLabel(channelType) }}</div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">企业状态：<span :class="[data.status == '1' ? 'enable' : 'disable']">{{ data.status | enable }}</span></div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">总申请入账金额：{{ data.totalAmount }}</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">企业编码：{{ data.easCode || '- -' }}</div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">入账申请单号：{{ data.code || '- - ' }}</div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">申请状态：{{ data.applyStatus | dictLabel(agreementApplyStatus) }}</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">申请时间：<span class="font-important-color">{{ data.createTime | formatDate }}</span></div></el-col>
        </el-row>
      </div>
      <!-- 下部表格切换 -->
      <el-tabs
        class="my-tabs"
        v-model="activeTab"
        @tab-click="handleTabClick"
      >
        <el-tab-pane label="入账明细" name="1"></el-tab-pane>
        <el-tab-pane label="协议订单商品明细" name="2"></el-tab-pane>
      </el-tabs>
      <!-- 当 activeTab 为1 时-->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          v-if="activeTab == 1"
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getDataList">
          <el-table-column label="入账类型" min-width="60" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? '协议' : '其它返利' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="入账原因" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? row.name : row.entryDescribe }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content || '- -' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单数量" min-width="100" align="center" prop="orderCount"></el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.amount }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发货组织" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.sellerName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.applyTime }}</div>
            </template>
          </el-table-column>
          <el-table-column label="返利种类" min-width="100" align="center" prop="rebateCategory"></el-table-column>
          <el-table-column label="费用科目" min-width="100" align="center" prop="costSubject"></el-table-column>
          <el-table-column label="费用归属部门" min-width="100" align="center" prop="costDept"></el-table-column>
          <el-table-column label="批复代码" min-width="100" align="center" prop="replyCode"></el-table-column>
          <!-- <el-table-column key="isEdit" label="操作" min-width="100" align="center" v-if="isEdit && activeTab == 1">
            <template slot-scope="{ row }">
              <div v-if="row.detailType == 2 && data.applyStatus == 3"><yl-button type="text" @click="editClick(row)">修改</yl-button></div>
            </template>
          </el-table-column> -->
        </yl-table>

        <yl-table
          v-if="activeTab == 2"
          border
          stripe
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getDataList">
          <el-table-column label="协议ID" min-width="60" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.id }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议名称" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单号" min-width="100" align="center" prop="orderCode"></el-table-column>
          <el-table-column label="商品ID" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsId }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品erp内码" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.erpCode }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="成交数量" min-width="100" align="center" prop="goodsQuantity">
          </el-table-column>
          <el-table-column label="采购金额" min-width="100" align="center" prop="price">
          </el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center" prop="discountAmount" fixed="right">
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { agreementApplyStatus, channelType } from '@/subject/admin/utils/busi'
import { queryRebateApplyPageList, queryRebateApplyOrderDetailPageList } from '@/subject/admin/api/reconciliation'
export default {
  name: 'ReconciliationEntryInfo',
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
      data: {},
      query: {
        page: 1,
        limit: 10,
        total: 0
      },
      activeTab: '1',
      dataList: [],
      id: '',
      loading: false
    }
  },
  mounted() {
    this.id = this.$route.params.id;
    if ( this.id ) {
      this.getDataList(); //获取企业信息
    }
  },
  methods: {
    async getDataList() {
      this.loading = true;
      let data = null;
      if (this.activeTab == 1) {
        data = await queryRebateApplyPageList(this.id,this.query.page,this.query.limit
        )
      } else {
        data = await queryRebateApplyOrderDetailPageList(this.id,this.query.page,this.query.limit)
      }
      this.loading = false;
      if (data) {
        this.data = data;
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },

    // 点击表格上部切换
    handleTabClick ( tab, event ) {
      this.dataList = []
      this.query = {
        page: 1,
        limit: 10
      }
      this.getDataList();
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
