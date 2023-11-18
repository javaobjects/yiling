<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="erp-total mar-b-16">
        <div 
          v-for="(item, index) in topDataList" 
          :key="index" 
          :class="['box', query.openType == index + 1 ? 'active' : '' ]" 
          @click="countStatistics(index + 1)">
          <div class="box-item-title">
            <svg-icon class="svg-icon" :icon-class="item.icon"></svg-icon>
            <span>{{ item.name }}</span>
          </div>
          <div class="box-item-value">{{ item.value }}</div>
        </div>
      </div>
      <div class="common-box box-search">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">{{ query.openType == 4 ? '销售' : (query.openType == 5 ? '采购' : '') }}企业名称</div>
              <el-input v-model="query.clientName" @keyup.enter.native="searchEnter" placeholder="请输入" />
            </el-col>
            <el-col :span="6" v-if="query.openType == 0 || query.openType == 1 ||query.openType == 2 || query.openType == 3">
              <div class="title">负责人</div>
              <el-input v-model="query.installEmployee" @keyup.enter.native="searchEnter" placeholder="请输入负责人" />
            </el-col>
            <el-col :span="6">
              <div class="title">{{ query.openType == 4 ? '销售' : (query.openType == 5 ? '采购' : '') }}企业ID</div>
              <el-input v-model="query.rkSuId" placeholder="请输入" @keyup.enter.native="searchEnter" @input="e => (query.rkSuId = checkInput(e))"/>
            </el-col>
            <el-col :span="6" v-if="query.openType == 4">
              <div class="title">销售单主键ID</div>
              <el-input v-model="query.soId" placeholder="请输入" @keyup.enter.native="searchEnter" @input="e => (query.soId = checkInput(e))"/>
            </el-col>
            <el-col :span="6" v-if="query.openType == 4">
              <div class="title">销售单号</div>
              <el-input v-model="query.soNo" placeholder="请输入" @keyup.enter.native="searchEnter" @input="e => (query.soNo = checkInput(e))"/>
            </el-col>
            <el-col :span="6" v-if="query.openType == 5">
              <div class="title">采购时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6" v-if="query.openType == 4">
              <div class="title">销售时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- <div class="top-btn font-size-lg" >
        <span class="bold mar-r-16">{{ query.openType == 4 ? '销售异常列表' : (query.openType == 5 ? '采购异常列表' : '对接企业列表') }}</span>
        <div class="erp-tool-tip" v-if="query.openType == 0 || query.openType == 1 || query.openType == 2 || query.openType == 3">
          <yl-tool-tip @mouseover.native="showCountInfo = true" @mouseout.native="showCountInfo=false">ERP对接请求次数阈值信息</yl-tool-tip>
          <div class="erp-countinfo-table" v-show="showCountInfo" @mouseover="showCountInfo = true" @mouseout="showCountInfo=false">
            <yl-table :show-header="true" stripe border :list="countInfoList">
              <el-table-column label="任务编号" align="center" prop="taskNo"></el-table-column>
              <el-table-column label="任务名称" align="center" prop="taskName"></el-table-column>
              <el-table-column label="请求次数阈值" align="center" prop="value"></el-table-column>
            </yl-table>
          </div>
        </div>
      </div> -->
      <div class="table-box mar-t-10" v-if="query.openType == 4">
        <yl-table 
          border 
          :show-header="true" 
          :list="saleDataList" 
          :total="query.total" 
          :page.sync="query.current" 
          :limit.sync="query.size" 
          :loading="loading"
          key="4" 
          @getList="getData()">
          <el-table-column label="企业ID" min-width="100" align="center" prop="eid"> </el-table-column>
          <el-table-column label="企业名称" min-width="130" align="center" prop="ename"> </el-table-column>
          <el-table-column label="销售单主键ID" min-width="160" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.soId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="销售单号" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.soNo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="销售单据时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.flowTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上传时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.uploadTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="原始单据上传时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.parentUploadTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.operType | dictLabel(erpType) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="任务上传ID" min-width="100" align="center" prop="controlId">
          </el-table-column>
        </yl-table>
      </div>
      <div class="table-box mar-t-10" v-else-if="query.openType == 5">
        <yl-table 
          border 
          :show-header="true" 
          :list="purchaseDataList" 
          :total="query.total" 
          :page.sync="query.current" 
          :limit.sync="query.size" 
          :loading="loading"
          key="5"
          @getList="getData()">
          <el-table-column label="企业ID" min-width="100" align="center" prop="eid"> </el-table-column>
          <el-table-column label="企业名称" min-width="200" align="left" prop="ename"> </el-table-column>
          <el-table-column label="供应商企业ID" min-width="120" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.supplierId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="采购时间" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.poTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="商品规格id" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.specificationId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="采购数量小计" min-width="100" align="center" prop="totalPoQuantity">
            <template slot-scope="{ row }">
              <span>{{ row.totalPoQuantity }}</span>
            </template>
          </el-table-column>
          <el-table-column label="销售数量小计" min-width="100" align="center" prop="totalSoQuantity">
             <template slot-scope="{ row }">
              <span>{{ row.totalSoQuantity }}</span>
            </template>
          </el-table-column>
          <el-table-column label="有无销售" min-width="140" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.saleFlag | dictLabel(erpPurchaseSale) }}</span>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="table-box mar-t-10" v-else>
        <yl-table 
          border 
          :show-header="true" 
          :list="dataList" 
          :total="query.total" 
          :page.sync="query.current" 
          :limit.sync="query.size" 
          :loading="loading" 
          key="1"
          @getList="getData()">
          <el-table-column label="企业ID" min-width="100" align="center" prop="rkSuId"> </el-table-column>
          <el-table-column label="企业名称" min-width="200" align="left" prop="clientName"> </el-table-column>
          <el-table-column label="最后一次心跳时间" min-width="120" align="center" prop="heartBeatTime">
            <template slot-scope="{ row }">
              <span>{{ row.heartBeatTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="指令心跳时间" min-width="100" align="center"> 
            <template slot-scope="{ row }">
              <span>{{ row.redisHeartTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="版本号" min-width="100" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.versions }}</span>
            </template>
          </el-table-column>
          <el-table-column label="当天请求次数(总)" min-width="100" align="center" prop="requestTotalCont"> </el-table-column>
          <el-table-column label="新增次数" min-width="80" align="center" prop="requestAddCont"> </el-table-column>
          <el-table-column label="修改次数" min-width="80" align="center" prop="requestUpdateCont"> </el-table-column>
          <el-table-column label="删除次数" min-width="80" align="center" prop="requestDeleteCont"> </el-table-column>
          <el-table-column label="企业对接状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.clientStatus === 1 ? 'col-down' : 'col-up']">{{ row.clientStatus | clientStatusFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column label="负责人" min-width="80" align="center" prop="installEmployee"> </el-table-column>
          <el-table-column label="同步状态" min-width="70" align="center">
            <template slot-scope="{ row }">
              <span :class="[row.syncStatus === 1 ? 'col-down' : 'col-up']">{{ row.syncStatus | syncStatusFilter }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" min-width="150">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="operationClick(row)" :disabled="row.commandButtonStatus == 1 ? true : false">{{ row.commandButtonDesc }}</yl-button>      
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { erpMonitorList, erpMonitorTotal, querySaleExceptionListPage, queryPurchaseExceptionListPage } from '@/subject/admin/api/zt_api/erpAdministration'
import { updateComand } from '@/subject/admin/api/zt_api/erpMonitorDetails'
import { erpOperType, erpPurchaseSaleFlag } from '@/subject/admin/busi/erp/flowDirection'

export default {
  name: 'ErpMonitor',
  components: {},
  computed: {
    erpType() {
      return erpOperType()
    },
    erpPurchaseSale() {
      return erpPurchaseSaleFlag()
    }
  },
  filters: {
    clientStatusFilter(status) {
      return status === 1 ? '开启' : '关闭'
    },
    syncStatusFilter(status) {
      return status === 1 ? '已开启' : '未开启'
    }
  },
  data() {
    return {
      query: {
        // 1 关闭对接数量 2 1小时内无心跳对接数量 3 当月未上传销售 4 异常数据 5 采购异常
        openType: 0,
        //企业名称
        clientName: '',
        // 企业ID
        rkSuId: '',
        // 负责人
        installEmployee: '',
        // 销售单主键ID
        soId: '',
        // 销售单号
        soNo: '',
        time: [],
        total: 0,
        current: 1,
        size: 10
      },
      // 企业对接列表
      dataList: [],
      // 销售异常数据
      saleDataList: [],
      // 采购异常数据
      purchaseDataList: [],
      loading: false,
      // 阈值弹窗列表
      showCountInfo: false,
      // 阈值列表
      countInfoList: [],
      // 顶部数据
      topDataList: [
        {
          icon: 'last_order',
          name: '关闭对接企业',
          value: ''
        },
        {
          icon: 'today_order',
          name: '1天无心跳企业',
          value: ''
        },
        {
          icon: 'noFlowSaleCount',
          name: '当月无销售企业',
          value: ''
        },
        {
          icon: 'saleExceptionCount',
          name: '销售异常',
          value: ''
        },
        {
          icon: 'purchaseExceptionCount',
          name: '采购异常',
          value: ''
        }
      ]
    }
  },
  activated() {
    // 获取顶部数据
    this.getTotal();
    // 获取列表数据
    this.handleSearch()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.handleSearch()
      }
    },
    // 分页
    getData() {
      if (this.query.openType == 4) {
        // 获取销售异常数据
        this.saleApiData();
      } else if (this.query.openType == 5) {
        // 获取采购异常数据
        this.purchaseApiData();
      } else {
        this.getList()
      }
    },
    // 点击远程操作
    async operationClick(row) {
      if (row.commandButtonStatus == 0) {
        this.$common.showLoad();
        let data = await updateComand(row.suId)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功')
          this.getList()
        }
      } else if (row.commandButtonStatus == 2) {
        this.$router.push({
          name: 'ErpMonitorDetails',
          params: {
            id: row.suId
          }
        });
      }
    },
    async getTotal() {
      let data = await erpMonitorTotal()
      if (data) {
        this.topDataList[0].value = data.closedCount;
        this.topDataList[1].value = data.noHeartCount;
        this.topDataList[2].value = data.noFlowSaleCount;
        this.topDataList[3].value = data.saleExceptionCount;
        this.topDataList[4].value = data.purchaseExceptionCount;
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await erpMonitorList(
        query.current,
        query.size,
        query.rkSuId,
        query.clientName,
        query.installEmployee,
        query.openType 
      )
      this.loading = false
      if (data) {
        this.dataList = data.records;
        this.countInfoList = data.monitorCountInfoList;
        query.total = data.total;
      }
    },
    handleSearch() {
      this.query.current = 1;
      if (this.query.openType == 4) {
        // 获取销售异常数据
        this.saleApiData();
      } else if (this.query.openType == 5) {
        // 获取采购异常数据
        this.purchaseApiData();
      } else {
        this.getList()
      }
    },
    handleReset() {
      this.query = {
        openType: this.query.openType,
        clientName: '',
        rkSuId: '',
        installEmployee: '',
        soId: '',
        soNo: '',
        time: [],
        total: 0,
        current: 1,
        size: 10
      }
      this.dataTime();
    },
    countStatistics(index) {
      this.dataList = [];
      this.saleDataList = [];
      this.purchaseDataList = [];
      this.query.openType = this.query.openType == index ? 0 : index;
      this.query.current = 1;
      if (this.query.openType == 4) {
        // 获取销售时间
        this.dataTime()
        // 获取销售异常数据
        this.saleApiData();
      } else if (this.query.openType == 5) {
        // 获取采购时间
        this.dataTime()
        // 获取采购异常数据
        this.purchaseApiData();
      } else {
        this.getList()
      }
    },
    dataTime() {
      // 获取当前月份第一天日期
      var date = new Date();
      date.setDate(1);
      var month = parseInt(date.getMonth() + 1);
      var day = date.getDate();
      if (month < 10) {
        month = '0' + month
      }
      if (day < 10) {
        day = '0' + day
      }
      // 获取当前月份最后一天日期
      var currentMonth = date.getMonth();
      var nextMonth = ++currentMonth;
      var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
      var oneDay = 1000 * 60 * 60 * 24;
      var lastTime = new Date(nextMonthFirstDay - oneDay);
      var day2 = lastTime.getDate();
      if (day2 < 10) {
        day = '0' + day
      }
      let date1 = date.getFullYear() + '-' + month + '-' + day;
      let date2 = date.getFullYear() + '-' + month + '-' + day2;
      this.query.time = [date1, date2]
    },
    // 获取销售异常数据
    async saleApiData() {
      let query = this.query;
      this.loading = true;
      let data = await querySaleExceptionListPage(
        query.clientName,
        query.rkSuId,
        query.soId,
        query.soNo,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.openType,
        query.current,
        query.size
      )
      if (data) {
        this.saleDataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false
    },
    // 采购异常
    async purchaseApiData() {
      let query = this.query;
      this.loading = true;
      let data = await queryPurchaseExceptionListPage(
        query.clientName,
        query.rkSuId,
        query.time && query.time.length > 0 ? query.time[0] : '',
        query.time && query.time.length > 1 ? query.time[1] : '',
        query.openType,
        query.current,
        query.size
      )
      if (data) {
        this.purchaseDataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
