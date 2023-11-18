<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">年月</div>
              <el-date-picker 
                v-model="monthTime" 
                format="yyyy 年 MM 月"
                value-format="yyyy-MM"
                type="month" 
                placeholder="请选择年月">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-select v-model="query.eid" filterable placeholder="请选择">
                <el-option
                  v-for="item in enterpriseData"
                  :key="item.suId"
                  :label="item.clientName"
                  :value="item.suId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">实施负责人</div>
              <el-input v-model.trim="query.installEmployee" @keyup.enter.native="searchEnter" placeholder="请输入实施负责人" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">下拉类型</div>
              <el-select class="select-width" v-model="query.quantityType" @change="selectChange" placeholder="请选择">
                <el-option 
                  v-for="item in dataType" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">{{ quantityName }}开始数量</div>
              <el-input v-model.trim="query.minQuantity" @input="e => (query.minQuantity = checkInput(e))" @keyup.enter.native="searchEnter" placeholder="请输入开始数量" />
            </el-col>
            <el-col :span="8">
              <div class="title">{{ quantityName }}结束数量</div>
              <el-input v-model.trim="query.maxQuantity" @input="e => (query.maxQuantity = checkInput(e))" @keyup.enter.native="searchEnter" placeholder="请输入结束数量" />
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
      <div class="mar-t-8">
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading"
          :row-style="rowStyle"
          @getList="getList">
          <el-table-column align="center" min-width="60" label="年月份" prop="time">
          </el-table-column>
          <el-table-column align="center" min-width="60" label="企业ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="企业名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="实施负责人" prop="installEmployee">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="采购数量/未匹配商品数量/同比上月增长率">
            <template slot-scope="{ row }">
              <div class="tab-div">
                <span @click="purchaseClick(row)">{{ row.poQuantity }}</span> / {{ row.noMatchPoQuantity }} / {{ row.poGrowthRate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="110" label="销售数量/未匹配商品数量/同比上月增长率">
            <template slot-scope="{ row }">
              <div class="tab-div">
                <span @click="saleClick(row)">{{ row.soQuantity }}</span> / {{ row.noMatchSoQuantity }} / {{ row.soGrowthRate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="月初库存量" prop="beginMonthQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="110" label="月末库存量/未匹配商品数量">
            <template slot-scope="{ row }">
              <div class="tab-div">
                <span @click="monthClick(row)">{{ row.gbQuantity }}</span> / {{ row.noMatchGbQuantity }}
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="80" label="平衡相差数" prop="differQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="操作">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="todayStatistical(row)">每天统计详情</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="statisticalDetails(row)">商品统计详情</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="toUpdateClick(row)">更新统计</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <yl-dialog
      width="800px"
      title="统计详情" 
      :show-footer="false"
      :visible.sync="showDialog">
      <div class="pop-up">
        <yl-table border :show-header="true" :list="todayData" @getList="getList">
          <el-table-column align="center" min-width="100" label="年月份">
            <template slot-scope="{ row }">
              <div>{{ row.dateTime | formatDate('yyyy-MM-dd') }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="60" label="企业ID" prop="eid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="企业名称" prop="ename">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="实施负责人" prop="installEmployee">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="采购数量" prop="poQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="销售数量" prop="soQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="昨日库存量" prop="lastGbQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="当前库存量" prop="gbQuantity">
          </el-table-column>
          <el-table-column align="center" min-width="80" label="平衡相差数" prop="differQuantity">
          </el-table-column>
        </yl-table>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
  import { monthPageList, enterpriseList, todayList, statistics } from '@/subject/admin/api/zt_api/month_flowto_monitor'
  export default {
    name: 'MonthFlowtoMonitor',
    data() {
      return {
        query: {
          eid: '',
          installEmployee: '',
          quantityType: '1',
          maxQuantity: '',
          minQuantity: '',
          total: 0,
          page: 1,
          limit: 10
        },
        monthTime: '',
        dataType: [
          {
            label: '采购',
            value: '1'
          },
          {
            label: '销售',
            value: '2'
          },
          {
            label: '月末库存',
            value: '3'
          },
          {
            label: '平衡相差数',
            value: '4'
          }
        ],
        loading: false,
        dataList: [],
        // 企业名称 数组
        enterpriseData: [],
        // 每天统计详情弹窗
        showDialog: false,
        todayData: [],
        quantityName: '采购'
      }
    },
    mounted() {
      this.getTime();
    },
    activated() {
      // 列表数据
      this.getList();
      // 获取企业名称
      this.qyDataApi();
    },
    methods: {
      // Enter
      searchEnter(e) {
        const keyCode = window.event ? e.keyCode : e.which;
        if (keyCode === 13) {
          this.getList()
        }
      },
      // 获取时间
      getTime() {
        let time = new Date();
        let month = time.getMonth() + 1;
        if (month >= 1 && month <= 9) {
          month = '0' + month
        }
        this.monthTime = time.getFullYear() + '-' + month;
      },
      // 选中下拉类型时触发
      selectChange(val) {
        this.quantityName = this.dataType[val - 1].label;
      },
      // 获取企业名称
      async qyDataApi() {
        let data = await enterpriseList()
        if (data) {
          this.enterpriseData = data;
        }
      },
      // 列表数据
      async getList() {
        this.loading = true;
        let query = this.query;
        let data = await monthPageList(
          query.page,
          query.eid,
          query.installEmployee,
          query.maxQuantity,
          query.minQuantity,
          query.quantityType,
          query.limit,
          this.monthTime
        )
        if (data) {
          this.dataList = data.records;
          this.query.total = data.total;
        }
        this.loading = false;
      },
      // 每天统计详情
      async todayStatistical(row) {
        this.$common.showLoad();
        let data = await todayList(
          row.eid,
          row.time
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.todayData = data;
          this.showDialog = true;
        }
      },
      // 统计详情
      statisticalDetails(row) {
        this.$router.push({
          name: 'CommodityDetails',
          params: { 
            eid: row.eid,
            monthTime: row.time
          }
        })
      },
      // 搜索点击
      handleSearch() {
        this.query.page = 1;
        this.getList()
      },
      // 清空查询
      handleReset() {
        this.query = {
          eid: '',
          installEmployee: '',
          quantityType: '1',
          maxQuantity: '',
          minQuantity: '',
          total: 0,
          page: 1,
          limit: 10
        }
        this.quantityName = this.dataType[0].label
      },
      // 更新统计
      async toUpdateClick(row) {
        this.$common.showLoad();
        let data = await statistics(
          row.eid,
          row.time
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('更新统计成功');
          this.getList()
        }
      },
      rowStyle({row, rowIndex}) {
        if (row.poQuantity == 0 || row.soQuantity == 0) {
          return {
            'color': '#ee0a24'
          }
        }
      },
      // 点击销售数量
      saleClick(row) {
        this.$router.push({
          name: 'ErpBussinessSaleFlows',
          params: { 
            name: row.ename,
            time: row.time
          }
        })
      },
      // 点击采购数量
      purchaseClick(row) {
        this.$router.push({
          name: 'ErpBussinessBuyFlows',
          params: { 
            name: row.ename,
            time: row.time
          }
        })
      },
      // 点击月末库存量
      monthClick(row) {
        this.$router.push({
          name: 'ErpBussinessStocks',
          params: { 
            name: row.ename
          }
        })
      },
      checkInput(val) {
        if (val.search(/-([1-9]|[0-9][0-9])*$/) == 0 || val.search(/^[0-9]*$/) == 0) {
          return val
        } else {
          return ''
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>