<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入终端ID" />
            </el-col>
            <el-col :span="8">
              <div class="title">终端名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入终端名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">申请时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">订单编号</div>
              <el-input v-model="query.orderNo" @keyup.enter.native="searchEnter" placeholder="请输入订单编号" />
            </el-col>
            <el-col :span="8">
              <div class="title">审核状态</div>
              <el-select class="select-width" v-model="query.authStatus" placeholder="请选择审核状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in memberReturnAuthStatus" :key="item.value" :label="item.label" :value="item.value">
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

      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <yl-button type="primary" plain @click="downLoadTemp">导出查询结果</yl-button>
        </div>
      </div>

      <div class="mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column align="center" min-width="120" label="审核状态" prop="memberName">
            <template slot-scope="{ row }">
              <span>{{ row.authStatus | dictLabel(memberReturnAuthStatus) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="订单编号" prop="orderNo">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="退款原因" prop="returnReason">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="终端ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="终端名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" label="支付方式" prop="payMethodName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="支付时间">
            <template slot-scope="{ row }">
              <span>{{ row.payTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="申请人" width="120" prop="applyUserName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="申请时间">
            <template slot-scope="{ row }">
              <span>{{ row.applyTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="备注信息" width="120" prop="returnRemark">
          </el-table-column>
          <el-table-column align="center" label="支付金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.payAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="退款金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="退款状态" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnStatus | dictLabel(memberReturnStatus) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作信息" width="120">
            <template slot-scope="{ row }">
              <div>最后操作人：{{ row.updateUserName }}</div>
              <div>最后操作时间：{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            align="center"
            label="操作"
            width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button v-role-btn="['1']" :disabled="row.authStatus !== 1" type="text" @click="handle(row, 2)">同意</yl-button>
              </div>
              <div>
                <yl-button v-role-btn="['2']" :disabled="row.authStatus !== 1" type="text" @click="handle(row, 3)">驳回</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
  import { createDownLoad } from '@/subject/admin/api/common'
  import { memberReturnAuthStatus, memberReturnStatus } from '@/subject/admin/busi/b2b/menbership'
  import { getMemberReturnListPage, memberReturn } from '@/subject/admin/api/b2b_api/membership'

  export default {
    name: 'ReviewMemberList',
    components: {
    },
    computed: {
      memberReturnAuthStatus() {
        return memberReturnAuthStatus()
      },
      memberReturnStatus() {
        return memberReturnStatus()
      }
    },
    data() {
      return {
        query: {
          eid: '',
          ename: '',
          time: [],
          orderNo: '',
          authStatus: 0,
          page: 1,
          limit: 10,
          total: 0
        },
        dataList: [],
        loading: false
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
      async getList() {
        this.loading = true
        let query = this.query
        let data = await getMemberReturnListPage(
          query.page,
          query.limit,
          query.eid,
          query.ename,
          query.orderNo,
          query.time && query.time.length > 0 ? query.time[0] : null,
          query.time && query.time.length > 1 ? query.time[1] : null,
          query.authStatus
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      // 同意或驳回
      handle(row, type) {
        this.$common.confirm(type === 2 ? '确定退款此会员吗？' : '确定驳回此会员吗？', async r => {
          if (r) {
            this.$common.showLoad()
            const data = await memberReturn(row.id, type)
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.$common.n_success(type === 2 ? '已同意退款' : '已驳回退款')
              this.getList()
            }
          }
        })
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          eid: '',
          ename: '',
          time: [],
          orderNo: '',
          authStatus: '',
          page: 1,
          limit: 10,
          total: 0
        }
      },
      // 导出
      async downLoadTemp() {
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'b2bMemberReturnExportService',
          fileName: '导出会员退款记录',
          groupName: '会员退款记录导出',
          menuName: '会员退款列表',
          searchConditionList: [
            {
              desc: '终端ID',
              name: 'eid',
              value: query.eid || ''
            },
            {
              desc: '终端名称',
              name: 'ename',
              value: query.ename || ''
            },
            {
              desc: '申请开始时间',
              name: 'applyStartTime',
              value: query.time && query.time.length ? query.time[0] : ''
            },
            {
              desc: '申请结束时间',
              name: 'applyEndTime',
              value: query.time && query.time.length > 1 ? query.time[1] : ''
            },
            {
              desc: '订单编号',
              name: 'orderNo',
              value: query.orderNo || ''
            }
          ]
        })
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('创建下载任务成功，请在下载中心查看')
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>

