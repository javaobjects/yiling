<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="7">
              <div class="title">采购商名称</div>
              <el-input v-model="query.buyerEname" @keyup.enter.native="searchEnter" placeholder="请输入采购商名称" />
            </el-col>
            <el-col :span="10">
              <div class="title">下单时间</div>
              <el-date-picker
                v-model="query.createTime"
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
            <el-col :span="7">
              <div class="title">批准文号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入申请单号" />
            </el-col>
            <el-col :span="7">
              <div class="title">渠道商类型</div>
              <el-select v-model="query.channelId" placeholder="请选择渠道类型">
                <el-option
                  v-for="item in channelType"
                  v-show="item.value != 1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">发货时间</div>
              <el-date-picker
                v-model="query.endDeliveryTime"
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
            <el-col :span="7">
              <div class="title">商品规格</div>
              <el-input v-model="query.sellSpecifications" @keyup.enter.native="searchEnter" placeholder="请输入商品规格" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-8">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="tab-switch">
          <span>基础数据</span>
          <!-- <span>商品分析</span> -->
        </div>
        <div class="btn">
          <ylButton v-role-btn="['5']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部表格 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          ref="table"
          :list="dataList"
          :total="total"
          :cell-class-name="() => 'border-1px-b'"
          show-header
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column fixed="left" label="商品名称" min-width="120" align="center" prop="goodsName">
          </el-table-column>
          <el-table-column label="商品erp内码" min-width="120" align="center" prop="goodsInSn">
          </el-table-column>
          <el-table-column label="采购商名称" min-width="160" align="center" prop="buyerEname">
          </el-table-column>
          <el-table-column label="渠道类型" min-width="120" align="center" prop="buyerChannelName">
          </el-table-column>
          <el-table-column label="批准文号" min-width="120" align="center" prop="licenseNo">
          </el-table-column>
          <el-table-column label="规格" min-width="80" align="center" prop="sellSpecifications">
          </el-table-column>
          <el-table-column label="单位" min-width="80" align="center" prop="sellUnit">
          </el-table-column>
          <el-table-column label="批次" min-width="80" align="center" prop="batchNo">
          </el-table-column>
          <el-table-column label="采购数量" min-width="80" align="center" prop="goodsQuantity">
          </el-table-column>
          <el-table-column label="发货数量" min-width="120" align="center" prop="deliveryQuantity">
          </el-table-column>
          <el-table-column label="退回退货数量" min-width="120" align="center" prop="returnQuantity">
          </el-table-column>
          <el-table-column label="采购商退货数量" min-width="120" align="center" prop="buyerReturnQuantity">
          </el-table-column>
          <el-table-column label="商品单价(元)" min-width="120" align="center" prop="goodsPrice">
          </el-table-column>
          <el-table-column label="商品小计(元)" min-width="120" align="center" prop="goodsAmount">
          </el-table-column>
          <el-table-column label="下单时间" min-width="120" align="center" >
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="发货时间" min-width="120" align="center" >
            <template slot-scope="{ row }">
              <div>
                {{ row.deliveryTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <!-- <el-table-column label="操作" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div><yl-button type="text" @click="showDetail(row)">查看详情</yl-button></div>
              <div v-show="row.isAllowCheck == 1"><yl-button type="text" @click="auditClick(row)">审核</yl-button></div>
            </template>
          </el-table-column> -->
        </yl-table>
      </div>
    </div>
  </div>
</template>
<script>
import { channelType } from '@/subject/pop/utils/busi';
import { flowDirectionPage } from '@/subject/pop/api/order';
import { createDownLoad } from '@/subject/pop/api/common';
export default {
  name: 'FlowDirectionFirst',
  components: {
  },
  computed: {
    channelType() {
      return channelType();
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '流向管理'
        },
        {
          title: '以岭发货数据'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        goodsName: '', //商品名称
        buyerEname: '', //采购商名称
        createTime: [], //下单时间
        licenseNo: '', //批准文号
        channelId: '', //渠道商类型
        endDeliveryTime: [],//发货时间
        sellSpecifications: ''//商品规格
      },
      total: 0,
      dataList: [],//底部表格数据
      tab: false
    };
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
      this.loading = true;
      let query = this.query;
      let data = await flowDirectionPage(
        query.channelId, 
        query.buyerEname, 
        query.page,  
        query.createTime && query.createTime.length ? query.createTime[1] : undefined,
        query.endDeliveryTime && query.endDeliveryTime.length ? query.endDeliveryTime[1] : undefined,
        query.goodsName,  
        query.licenseNo,  
        query.sellSpecifications,  
        // sellerName, 
        query.limit, 
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.endDeliveryTime && query.endDeliveryTime.length ? query.endDeliveryTime[0] : undefined
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.total = data.total;
      }
      // 
      // this.total = data.total;
      // this.$log("this.dataList:", this.dataList);
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10
      };
    },
    tabTypeClick(flag) {
      this.tab = flag
      if (flag) {
        this.creatTopEcharts()
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query;
      this.$common.showLoad();
      let data = await createDownLoad({
        className: 'orderFlowYilingExportService',
        fileName: 'string',
        groupName: '流向管理(一级)',
        menuName: '流向管理-药品流向(一级)',
        searchConditionList: [
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName || ''
          },
          {
            desc: '采购商名称',
            name: 'buyerEname',
            value: query.buyerEname || ''
          },
          {
            desc: '下单开始时间',
            name: 'startCreateTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : undefined
          },
          {
            desc: '下单结束时间',
            name: 'endCreateTime',
            value: query.createTime && query.createTime.length ? query.createTime[1] : undefined
          },
          {
            desc: '批准文号',
            name: 'licenseNo',
            value: query.licenseNo || ''
          },
          {
            desc: '渠道商类型',
            name: 'buyerChannelId',
            value: query.channelId || ''
          },
          {
            desc: '发货开始时间',
            name: 'startDeliveryTime',
            value: query.endDeliveryTime && query.endDeliveryTime.length ? query.endDeliveryTime[0] : undefined
          },
          {
            desc: '发货结束时间',
            name: 'endDeliveryTime',
            value: query.endDeliveryTime && query.endDeliveryTime.length ? query.endDeliveryTime[1] : undefined
          },
          {
            desc: '商品规格',
            name: 'sellSpecifications',
            value: query.sellSpecifications
          }
        ]
      });
      this.$log('data:', data);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看');
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
