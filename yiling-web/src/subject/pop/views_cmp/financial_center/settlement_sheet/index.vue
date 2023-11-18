<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
     <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box mar-b-16 order-total">
        <div class="box border-1px-r">
          <div>
            <svg-icon class="svg-icon" icon-class="to_be_settled"></svg-icon>
            <span>待结帐</span>
          </div>
          <div class="title">
            {{ totalData.unPayAmount | toThousand('￥') }}
          </div>
        </div>
        <div class="box">
          <div>
            <svg-icon class="svg-icon" icon-class="payment_amount"></svg-icon>
            <span>累积打款总额</span>
          </div>
          <div class="title">
            {{ totalData.enPayAmount | toThousand('￥') }}
          </div>
        </div>
      </div>
      <!-- tab切换 -->
      <div class="tab">
        <div
          v-for="(item,index) in tabList"
          :key="index"
          class="tab-item"
          :class="tabActive === item.id ? 'tab-active' : ''"
          @click="clickTab(item.id)">
          {{ item.name }}
        </div>
      </div>
      <!-- 搜索 -->
      <div class="common-box mar-t-8">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">{{ tabActive == '1' ? '订单创建日期段' : '打款日期段' }}</div>
              <el-date-picker
                v-model="query.dataTime"
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
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="my-table mar-t-16">
        <div class="table-total">
          共{{ totalCount }}条 合计: <span>{{ totalAmount | toThousand('￥') }}</span>
        </div>
        <yl-table
          stripe 
          :show-header="true" 
          :list="dataList"
          :total="query.total"
          :page.sync="query.page" 
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="商品名称" prop="goodsName" align="center"></el-table-column>
          <el-table-column label="售卖数量" prop="goodsQuantity" align="center"></el-table-column>
          <el-table-column label="结算单价" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.price | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单编号" prop="orderNo" min-width="130" align="center"></el-table-column>
          <el-table-column label="订单明细编号" prop="id" align="center"></el-table-column>
          <el-table-column label="合计" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsAmount | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建日期" prop="createTime" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单完成日期" prop="createTime" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.finishTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column 
            v-if="tabActive == '1'" 
            label="管控渠道" 
            prop="createTime" 
            min-width="150"
            align="center">
            <template slot-scope="{ row }">
               <div v-for="val in row.channelNameList" :key="val">
                  {{ val }}
                </div>
            </template>
          </el-table-column>
          <el-table-column 
            v-if="tabActive == '2'" 
            label="打款日期" 
            prop="createTime" 
            align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.settlementTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column v-if="tabActive == '2'" label="结算额" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.settlementAmount | toThousand('￥') }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { queryPayAmount, pageList } from '@/subject/pop/api/cmp_api/financialCenter'
export default {
  name: 'SettlementSheet',
  data() {
    return {
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '财务中心',
          path: ''
        },
        {
          title: '药品结算账单'
        }
      ],
      loading: false,
      query: {
        dataTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      totalData: {
        unPayAmount: '',
        enPayAmount: ''
      },
      tabList: [
        {
          name: '待结账',
          id: '1'
        },
        {
          name: '已结算打款',
          id: '2'
        }
      ],
      // 总比数	
      totalCount: '',
      // 合计结算金额
      totalAmount: '',
      // 1 待结账 2 已结算打款
      tabActive: '1',
      dataList: []
    }
  },
  activated() {
    // 获取顶部 待结账/累积打款总额数据
    this.topDataApi();
    // 获取表格数据
    this.getList();
  },
  methods: {
    async topDataApi() {
      let data = await queryPayAmount()
      if (data) {
        this.totalData = data
      }
    },
    clickTab(e) {
      this.tabActive = e;
      this.query = {
        dataTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
      this.tabActive = e;
      this.getList()
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await pageList(
        query.page,
        query.limit,
        query.dataTime && query.dataTime.length > 0 ? query.dataTime[0] : '',
        query.dataTime && query.dataTime.length > 1 ? query.dataTime[1] : '',
        this.tabActive
      )
      if (data) {
        this.totalCount = data.totalCount;
        this.totalAmount = data.totalAmount;
        this.dataList = data.page.records;
        this.query.total = data.page.total;
        
      }
      this.loading = false;
    },
    handleSearch() {
      this.query.page = 1;
      this.getList()
    },
    handleReset() {
      this.query = {
        dataTime: [],
        total: 0,
        page: 1,
        limit: 10
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>