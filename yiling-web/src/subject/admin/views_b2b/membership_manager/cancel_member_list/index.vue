<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端名称</div>
              <el-input v-model="query.ename" @keyup.enter.native="searchEnter" placeholder="请输入终端名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">取消时间</div>
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
            <el-col :span="8">
              <div class="title">推广方ID</div>
              <el-input v-model="query.promoterId" @keyup.enter.native="searchEnter" placeholder="请输入推广方ID" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">操作人</div>
              <el-input v-model="query.updateUserName" @keyup.enter.native="searchEnter" placeholder="请输入操作人" />
            </el-col>
            <el-col :span="8">
              <div class="title">推广人名称</div>
              <el-input v-model="query.promoterUserName" @keyup.enter.native="searchEnter" placeholder="请输入推广人名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">推广人ID</div>
              <el-input v-model="query.promoterUserId" @keyup.enter.native="searchEnter" placeholder="请输入推广人ID" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">终端ID</div>
              <el-input v-model="query.eid" @keyup.enter.native="searchEnter" placeholder="请输入终端ID" />
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
          <el-table-column 
            fixed="left"
            align="center" 
            min-width="140" 
            label="订单ID" 
            prop="orderNo"
          >
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="150" 
            label="会员名称">
            <template slot-scope="{ row }">
              {{ row.memberName }}<span>（ID：{{ row.memberId }}）</span>
            </template>
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="90" 
            label="终端ID" 
            prop="eid">
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="终端名称" 
            prop="ename">
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="终端地址" 
            prop="address">
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="企业管理员手机号" 
            prop="contactorPhone">
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="购买规则" 
            prop="buyRule">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="购买时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广方ID">
            <template slot-scope="{ row }">
              <div v-if="row.promoterId">{{ row.promoterId }}</div>
            </template>
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="推广方名称" 
            prop="promoterName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="推广人ID">
            <template slot-scope="{ row }">
                <div v-if="row.promoterUserId">{{ row.promoterUserId }}</div>
            </template>
          </el-table-column>
          <el-table-column 
            align="center" 
            min-width="120" 
            label="推广人名称" 
            prop="promoterUserName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员起始时间">
            <template slot-scope="{ row }">
              <span>{{ row.startTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="120" label="会员结束时间">
            <template slot-scope="{ row }">
              <span>{{ row.endTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="支付方式" prop="payMethodName">
          </el-table-column>
          <el-table-column align="center" label="原价" >
            <template slot-scope="{ row }">
              <span>{{ row.originalPrice | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="优惠金额">
            <template slot-scope="{ row }">
              <span>{{ row.discountAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="支付金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.payAmount | toThousand('￥') }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="退款金额" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnAmount | toThousand('￥', true) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="是否退款" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.returnFlag | dictLabel(refundFlag) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="记录是否过期" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.expireFlag | dictLabel(expireFlag) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="开通类型" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.openType | dictLabel(memberOpenType) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="数据来源" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.source | dictLabel(memberDataSource) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="会员状态" width="70">
            <template slot-scope="{ row }">
              <span>{{ row.cancelFlag | dictLabel(cancelFlagList) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作人" width="120">
            <template slot-scope="{ row }">
              <span>{{ row.updateUserName || '- -' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="取消时间" width="160">
            <template slot-scope="{ row }">
              <span>{{ row.updateTime | formatDate }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
  import { createDownLoad } from '@/subject/admin/api/common'
  import { cancelBuyRecordListPage } from '@/subject/admin/api/b2b_api/membership'
  import { memberDataSource, memberOpenType } from '@/subject/admin/busi/b2b/menbership'

  export default {
    name: 'CancelMemberList',
    components: {},
    computed: {
      memberDataSource() {
        return memberDataSource()
      },
      memberOpenType() {
        return memberOpenType()
      }
    },
    data() {
      return {
        query: {
          page: 1,
          limit: 10,
          total: 0,
          // 终端名称
          ename: '',
          // 取消时间
          time: [],
          // 推广方id
          promoterId: '',
          // 操作人
          updateUserName: '',
          // 推广方名称
          promoterUserName: '',
          // 推广人ID
          promoterUserId: '',
          // 终端ID
          eid: ''
        },
        loading: false,
        dataList: [],
        refundFlag: [
          {
            label: '全部',
            value: 0
          },
          {
            label: '未退款',
            value: 1
          },
          {
            label: '已退款',
            value: 2
          }
        ],
        expireFlag: [
          {
            label: '全部',
            value: 0
          },
          {
            label: '未过期',
            value: 1
          },
          {
            label: '已过期',
            value: 2
          }
        ],
        cancelFlagList: [
          {
            label: '未取消',
            value: 0
          },
          {
            label: '已取消',
            value: 1
          }
        ]
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
      async getList() {
        this.loading = true
        let query = this.query
        let data = await cancelBuyRecordListPage(
          query.page,
          query.limit,
          query.ename,
          query.time && query.time.length > 0 ? query.time[0] : null,
          query.time && query.time.length > 1 ? query.time[1] : null,
          query.promoterId,
          query.updateUserName,
          query.promoterUserName,
          query.promoterUserId,
          query.eid
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.query.total = data.total
        }
      },
      handleSearch() {
        this.query.page = 1
        this.getList()
      },
      handleReset() {
        this.query = {
          page: 1,
          limit: 10,
          total: 0,
          ename: '',
          time: [],
          promoterId: '',
          updateUserName: '',
          promoterUserName: '',
          promoterUserId: '',
          eid: ''
        }
      },
      // 导出
      async downLoadTemp() {
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'b2bMemberBuyRecordExportService',
          fileName: '导出取消会员查看',
          groupName: '取消会员查看导出',
          menuName: '取消会员查看',
          searchConditionList: [
            {
              desc: '页面类型',
              name: 'pageType',
              value: 2
            },
            {
              desc: '终端名称',
              name: 'ename',
              value: query.ename || ''
            },
            {
              desc: '取消开始时间',
              name: 'cancelStartTime',
              value: query.time && query.time.length ? query.time[0] : ''
            },
            {
              desc: '取消结束时间',
              name: 'cancelEndTime',
              value: query.time && query.time.length > 1 ? query.time[1] : ''
            },
            {
              desc: '推广方ID',
              name: 'promoterId',
              value: query.promoterId || ''
            },
            {
              desc: '操作人',
              name: 'updateUserName',
              value: query.updateUserName || ''
            },
            {
              desc: '推广人名称',
              name: 'promoterUserName',
              value: query.promoterUserName || ''
            },
            {
              desc: '推广人ID',
              name: 'promoterId',
              value: query.promoterId || ''
            },
            {
              desc: '终端ID',
              name: 'eid',
              value: query.eid || ''
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
  @import "./index.scss";
</style>

