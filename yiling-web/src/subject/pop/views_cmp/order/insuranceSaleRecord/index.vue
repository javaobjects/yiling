<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>

    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">保险名称</div>
              <el-input v-model="query.name" placeholder="请输入" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">日期段</div>
              <el-date-picker v-model="query.createTime" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-col>

            <el-col :span="8">
              <div class="title">保单状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in hmcPolicyStatus" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">销售员</div>
              <el-select v-model="query.sellId" filterable placeholder="请输入">
                <el-option v-for="(item,index) in optionsName" :key="index" :label="item.sellerUserName" :value="item.sellerUserId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">保单号</div>
              <el-input v-model="query.policyNo" placeholder="请输入" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">被保人姓名</div>
              <el-input v-model="query.issueName" placeholder="请输入" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">投保人姓名</div>
              <el-input v-model="query.holderName" placeholder="请输入" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">投/被保人手机号</div>
              <el-input v-model="query.issuePhone" placeholder="请输入" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出</ylButton>
        </div>
      </div>
      <!-- table  -->
      <div class="my-table mar-t-8">
        <yl-table
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="保单详情" align="left" min-width="250">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="item-text font-size-lg bold clamp-t-1 title">{{ row.insuranceName }}</div>
                <div class="item-text font-size-base font-title-color">保司保单号：<span class="font-important-color">{{ row.policyNo }}</span></div>
                <div class="item-text font-size-base font-title-color">销售交易单号：<span class="font-important-color">{{ row.orderNo }}</span></div>
                <div class="item-text font-size-base font-title-color">保司：<span class="font-important-color">{{ row.companyName }}</span></div>
                <!-- 定额方案类型 1-季度，2-年 -->
                <div class="item-text font-size-base font-title-color">定额方案：<span class="font-important-color">{{ row.billType == 1 ? "季度":"年" }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" align="left" min-width="200">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color">保单来源终端：<span class="font-important-color">{{ row.terminalName }}</span></div>
                <div class="item-text font-size-base font-title-color">销售员：<span class="font-important-color">{{ row.sellerUserName?row.sellerUserName:'--' }} <span v-if="row.sellerUserName">【ID:{{ row.sellerUserId }}】</span></span></div>
                <!-- 	销售人员id 来源终端类型： 等于0代表线上 大于0代表线下	 -->
                <div class="item-text font-size-base font-title-color">来源类型：<span class="font-important-color">{{ row.sellerUserId == 0? "线上":"线下" }}</span></div>
                <div class="item-text font-size-base font-title-color">支付金额：<span class="font-important-color">¥{{ row.amount }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="" align="left" min-width="160">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-lg bold clamp-t-1 title"></div>
                <div class="item-text font-size-base font-title-color">交费时间：<span class="font-important-color">{{ row.payTime | formatDate }}</span></div>
                <div class="item-text font-size-base font-title-color">被保人：<span class="font-important-color">{{ row.issueName }}<span class="mar-l-8">{{ row.issuePhone }}</span> </span></div>
                <div class="item-text font-size-base font-title-color">投保人：<span class="font-important-color">{{ row.holderName }}<span class="mar-l-8">{{ row.holderPhone }}</span> </span></div>
                <div class="item-text font-size-base font-title-color">兑付次数：<span class="font-important-color">{{ row.cashTimes }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <!-- policyStatus 保单状态 1-进行中，2-已退保，3-已终止，4-已失效 -->
                <div class="item-text font-size-base color-999"><span>{{ row.policyStatus | dictLabel(hmcPolicyStatus) }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="60">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">详情</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getInsuranceList, getSellUser } from '@/subject/pop/api/cmp_api/order'
import { createDownLoad } from '@/subject/pop/api/common'
import { hmcPolicyStatus } from '@/subject/pop/utils/busi'
export default {
  name: 'CmpInsuranceSaleRecord',
  components: {
  },
  computed: {
    // 保单状态
    hmcPolicyStatus() {
      return hmcPolicyStatus()
    },
    goodStatus() {
      return goodsStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理',
          path: ''
        },
        {
          title: '保险销售记录'
        }
      ],
      query: {
        page: 1,
        limit: 20,
        name: '',
        sellId: '',
        createTime: [],
        // 全部，已上架，已下架 ，带设置
        status: 0
      },
      dataList: [],
      loading: false,
      total: 0,
      optionsName: []
    }
  },
  activated() {
    this.getSellUser();
    this.getList()
  },
  created() {
  },
  mounted() {
  },
  methods: {
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getInsuranceList(
        query.page,
        query.limit,
        query.name,
        query.status || '',
        query.sellId,
        query.policyNo,
        query.issueName,
        query.issuePhone,
        query.holderName,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length ? query.createTime[1] : undefined
      )
      this.loading = false;
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
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
        name: '',
        status: '',
        sellId: '',
        createTime: []
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'insurancePayRecordExportService',
        fileName: '保险销售记录导出',
        groupName: '订单管理',
        menuName: 'C端管理-订单管理',
        searchConditionList: [
          {
            desc: '保险名称',
            name: 'insuranceName',
            value: query.name || ''
          },
          {
            desc: '保单状态',
            name: 'policyStatus',
            value: query.status || ''
          },
          {
            desc: '销售人员',
            name: 'sellerUserId',
            value: query.sellId || ''
          },
          {
            desc: '保单号',
            name: 'policyNo',
            value: query.policyNo || ''
          },
          {
            desc: '被保人姓名',
            name: 'issueName',
            value: query.issueName || ''
          },
          {
            desc: '被保人手机号',
            name: 'issuePhone',
            value: query.issuePhone || ''
          },
          {
            desc: '投保人姓名',
            name: 'holderName',
            value: query.holderName || ''
          },
          {
            desc: '开始日期',
            name: 'startPayTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[0] : ''
          }, {
            desc: '结束日期',
            name: 'endPayTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          }
        ]

      })
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 保险纪录详情
    showDetail(row) {
      this.$router.push({
        name: 'CmpInsuranceOrderDetail',
        params: {
          id: row.id,
          type: 1
        }
      });
    },
    // 获取全部销售管理人员
    async getSellUser() {
      let data = await getSellUser()
      console.log(data);
      if (data !== undefined) {
        this.optionsName = data.list
      }
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
