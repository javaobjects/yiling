<template>

  <div class="app-container  ">
    <div class="app-container-content">
      <div class="mar-t-16 pad-b-100">
        <yl-table :list="orderList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList" :show-header="true" stripe center>
          <el-table-column prop="userTaskId" label="任务ID" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.taskId ==0 ? '--':row.taskId }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="taskName" label="任务名称" align="center"></el-table-column>
          <el-table-column prop="finishType" label="任务类型" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.finishType | dictLabel(commissionTaskType) }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="taskTime" label="任务起止时间" align="center"></el-table-column>
          <el-table-column prop="amount" label="佣金金额" align="center">
            <template slot-scope="{ row }">
              <div v-if="row.type == 1">{{ row.amount }}</div>
              <div v-else-if="row.type == 2">-{{ row.amount }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button :disabled="row.type == 2" type="text" @click="showOrderDetail(row)">查看</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog title="查看明细" :visible.sync="show" :show-footer="true" :show-header="false" @confirm="show=false">
        <div class="dialog-content">
          <div class="common-box box-search">
            <div class="search-box">
              <el-row class="color-3">
                <el-col :span="8"><span class="font-title-color">任务名称：</span>{{ detail.taskName }}</el-col>
                <el-col :span="8"><span class="font-title-color">任务ID：</span>{{ detail.taskId }}</el-col>
                <el-col :span="8"><span class="font-title-color">佣金总额：</span>{{ detail.amount }}</el-col>
              </el-row>
              <el-row class="color-3 mar-t-8">
                <el-col :span="8"><span class="font-title-color">任务计算方式：</span>{{ detail.ruleValue }}</el-col>
                <el-col :span="8"><span class="font-title-color">任务类型：</span>{{ detail.finishType | dictLabel(commissionTaskType) }}</el-col>
                <el-col :span="8"><span class="font-title-color">任务起止时间：</span>{{ detail.taskTime }}</el-col>
              </el-row>
            </div>
          </div>
          <yl-table class="mar-t-16" 
            :list="memberList" 
            :total="query1.total" 
            :page.sync="query1.page" 
            :limit.sync="query1.limit" 
            :loading="loading1" 
            @getList="commissionUserNameTaskListDetailMethods" 
            :show-header="true" 
            stripe>
            <el-table-column prop="orderCode" :label="finishType == 10 ? '单据编号' : '订单编号'"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="subordinateUserName" label="下线推广人"></el-table-column>
            <el-table-column prop="taskOwnershipName" label="所属企业"></el-table-column>
            <el-table-column label="任务承接人群">
              <template slot-scope="{ row }">
                <div>{{ row.taskUserType | dictLabel(commissionUserType) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="subAmount" label="佣金"></el-table-column>
            <el-table-column prop="status" label="状态">
              <template slot-scope="{ row }">
                <div v-if="row.status == 1">待结算</div>
                <div v-else-if="row.status == 2">已结算</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </yl-dialog>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>

<script>
import { commissionUserNameTaskList, commissionUserNameTaskListDetail } from '@/subject/admin/api/views_sale/commission_admin';
import { commissionTaskType, commissionUserType } from '@/subject/admin/utils/busi'
export default {
  name: 'CommissionSettlementDetail',
  components: {},
  computed: {
    // 佣金任务类型
    commissionTaskType() {
      return commissionTaskType();
    },
    // 用户类型：1-以岭人员 2-小三元 3-自然人
    commissionUserType() {
      return commissionUserType();
    }
  },
  filters: {
    filterSource(val) {
      // ：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广
      let arr = [
        { label: '交易额', value: 1 },
        { label: '交易量', value: 2 },
        { label: '新用户推广', value: 3 },
        { label: '促销推广', value: 4 },
        { label: '会议推广', value: 5 },
        { label: '学术推广', value: 6 },
        { label: '新人推广', value: 7 }
      ]
      for (let i = 0; i < arr.length; i++) {
        if (val == arr[i].value) return arr[i].label
      }
    }
  },
  data() {
    return {
      orderList: [],
      query: {
        page: 1,
        limit: 20,
        total: 0
      },
      show: false,
      memberList: [],
      loading: false,
      loading1: false,
      query1: {
        page: 1,
        limit: 20,
        total: 0,
        id: ''
      },
      currentRow: {},
      detail: {},
      //区分table表头 为10时叫单据编号 否则叫 订单编号
      finishType: 0
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    showOrderDetail(row) {
      this.currentRow = row;
      this.finishType = row.finishType
      this.show = true
      this.commissionUserNameTaskListDetailMethods()
    },
    async commissionUserNameTaskListDetailMethods() {
      this.query1.id = this.currentRow.id
      this.detail = {}
      this.memberList = []
      this.loading1 = true
      let query = this.query1
      let data = await commissionUserNameTaskListDetail(
        query.id,
        query.page,
        query.limit
      )
      this.loading1 = false
      console.log(data);
      if (data) {
        this.memberList = data.records
        this.query1.total = data.total
        this.detail = data
      }
    },
    async getList() {
      console.log(this.query);
      this.loading = true
      let query = this.query
      let data = await commissionUserNameTaskList(
        query.page,
        query.limit,
        this.$route.params.id
      )
      this.loading = false
      console.log(data);
      if (data) {
        this.orderList = data.records
        this.query.total = data.total
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>