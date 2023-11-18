<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <!-- 中部 搜索条件 -->
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">用药福利计划名称</div>
              <el-select filterable v-model="query.drugWelfareId" placeholder="请选择">
                <el-option
                  v-for="item in businessData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">核销时间</div>
              <el-date-picker
                v-model="query.establishTime"
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
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset"/>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 下部列表 -->
      <div class="search-bar mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column align="center" min-width="220" label="用药福利计划名称">
            <template slot-scope="{ row }">
              <div>
                {{ row.drugWelfareName }} 【id: {{ row.drugWelfareId }}】
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" label="福利详情">
            <template slot-scope="{ row }">
              <div>
                单次满{{ row.requirementNumber }}盒赠{{ row.giveNumber }}盒
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="100" label="券码" prop="couponCode">
          </el-table-column>
          <el-table-column align="center" min-width="90" label="核销人员" prop="verificationName">
          </el-table-column>
          <el-table-column align="center" min-width="100" label="核销时间">
            <template slot-scope="{ row }">
              <div>
                {{ row.verifyTime | formatDate }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <div class="bottom-bar-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { queryDrugWelfareList, queryVerificationList } from '@/subject/pop/api/cmp_api/drugWelfare'
export default {
  name: 'WriteOffRecord',
  data() {
    return {
      query: {
        drugWelfareId: '',
        establishTime: [],
        total: 0,
        page: 1,
        limit: 10
      },
      businessData: [],
      loading: false,
      dataList: [],
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理'
        },
        {
          title: '药品福利计划',
          path: '/cmp_order/drug_welfare'
        },
        {
          title: '核销记录'
        }
      ]
    }
  },
  mounted() {
    // 获取顶部用药福利计划名称
    this.drugWelfareList();
    // 获取数据列表
    this.getList();
  },
  methods: {
    // 获取顶部用药福利计划名称
    async drugWelfareList() {
      let data = await queryDrugWelfareList()
      if (data) {
        this.businessData = data.list;
      }
    },
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await queryVerificationList(
        query.drugWelfareId,
        query.establishTime && query.establishTime.length > 0 ? query.establishTime[0] : '',
        query.establishTime && query.establishTime.length > 1 ? query.establishTime[1] : '',
        query.page,
        query.limit
      )
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    //搜索
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    //重置
    handleReset() {
      this.query = {
        drugWelfareId: '',
        establishTime: [],
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