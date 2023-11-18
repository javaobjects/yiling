<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8" class="demo-ruleForm-form">
              <div class="title">总店名称</div>
              <el-select
                v-model="query.eid"
                clearable
                filterable
                remote
                reserve-keyword
                placeholder="请输入标准商品名称搜索"
                :remote-method="remoteMethod"
                :loading="sellSpecificationsLoading"
                @change="headquartersChange"
              >
                <el-option 
                  v-for="item in sellSpecificationsOption" 
                  :key="item.eid" 
                  :label="item.ename" 
                  :value="item.eid">
                  <span>{{ item.ename }}</span>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8" class="demo-ruleForm-form">
              <div class="title">门店名称</div>
              <el-select
                v-model="query.shopEid"
                clearable
                filterable
                remote
                reserve-keyword
                placeholder="请输入标准商品名称搜索"
                :remote-method="remoteMethodSubbranch"
                :loading="sellSpecificationsLoading"
              >
                <el-option 
                  v-for="item in sellSpecificationsOption2" 
                  :key="item.eid" 
                  :label="item.ename" 
                  :value="item.eid">
                  <span>{{ item.ename }}</span>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.goodsName" placeholder="请输入商品名称" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">批准文号</div>
              <el-input v-model="query.soLicense" placeholder="请输入内容" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">销售时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" placeholder="请输入内容" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="12">
              <div class="title">区域查询</div>
              <div class="flex-row-left">
                <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">商业标签</div>
              <el-checkbox-group v-model="query.enterpriseTagIdList">
                <el-checkbox
                  class="option-class"
                  v-for="item in tagList"
                  :key="item.id"
                  :label="item.id"
                >{{ item.name }}</el-checkbox >
              </el-checkbox-group >
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
          <ylButton v-role-btn="['1']" type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table 
          ref="table" 
          :list="dataList" 
          stripe 
          :total="total" 
          :row-class-name="() => 'table-row'" 
          show-header 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          :cell-no-pad="true" 
          @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="商品信息" align="left" min-width="300" fixed="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.goodsName }}</div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.soSpecifications }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.soLicense }}</span></div>
                <div class="font-size-base font-title-color"><span>{{ row.soManufacturer }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="总店信息" align="left" min-width="220">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.ename }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="门店信息" align="left" min-width="150">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.shopEname }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="销售时间" align="left" min-width="170">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数量" min-width="80" align="left">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soQuantity }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单位" min-width="80" align="left">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soUnit }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soPrice }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soTotalAmount }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="批号" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soBatchNo }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="有效日期" align="left" min-width="170">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.soEffectiveTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="省" align="left" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.provinceName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="市" align="left" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.cityName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="区" align="left" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.regionName }}</span></div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getErpqueryShopSaleListPage, queryFlowShopEnterpriseList, getEnterpriseTagList } from '@/subject/pop/api/erp_flow';
import { ylChooseAddress } from '@/subject/pop/components';
import { createDownLoad } from '@/subject/pop/api/common';

export default {
  name: 'ErpChainFlow',
  components: {
    ylChooseAddress
  },
  computed: {},
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: 'erp'
        },
        {
          title: '连锁纯销流向'
        }
      ],
      loading: false,
      sellSpecificationsOption: [],
      sellSpecificationsOption2: [],
      query: {
        page: 1,
        limit: 10,
        ename: '',
        eid: '',
        shopEid: '',
        goodsName: '',
        soLicense: '',
        startTime: '',
        endTime: '',
        manufacturer: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        enterpriseTagIdList: []
      },
      total: 0,
      dataList: [],
      tagList: [],
      sellSpecificationsLoading: false
    };
  },
  activated() {
    // this.getList()
    this.getEnterpriseTagListMethods()
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getErpqueryShopSaleListPage(
        query.page,
        query.limit,
        query.ename,
        query.eid,
        query.shopEid,
        query.goodsName,
        query.soLicense,
        query.createTime && query.createTime.length ? query.createTime[0] : '',
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : '',
        query.manufacturer,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.enterpriseTagIdList
      );
      this.loading = false;
      if (data !== undefined) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    // 搜索
    handleSearch() {
      let query = this.query
      if (
        query.ename ||
        query.eid ||
        query.shopEid ||
        query.goodsName ||
        query.soLicense ||
        query.startTime ||
        query.endTime ||
        query.manufacturer ||
        query.provinceCode ||
        query.cityCode ||
        query.regionCode ||
        (query.createTime && query.createTime.length != 0) ||
        query.enterpriseTagIdList.length > 0
      ) {
        if (query.createTime && query.createTime.length >= 1) {
          this.query.page = 1
          this.getList();
        } else {
          return this.$message.error('请选择销售时间');
        }
      } else {
        this.dataList = []
        this.total = 0
        return this.$message.warning('请至少输入一个查询条件')
      }
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        ename: '',
        eid: '',
        shopEid: '',
        goodsName: '',
        soLicense: '',
        startTime: '',
        endTime: '',
        manufacturer: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        enterpriseTagIdList: []
      };
      this.sellSpecificationsOption = []
      this.sellSpecificationsOption2 = []
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      if (this.dataList.length == 0) {
        return this.$message.warning('请搜索数据再导出')
      }
      if (this.total > 100000) {
        return this.$message.warning('数据量已超过10万无法导出,请重新搜索后再导出')
      }
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'flowShopSalePageListExportService',
        fileName: '连锁纯销流向导出',
        groupName: 'erp流向',
        menuName: 'POP管理-erp流向-连锁纯销流向',
        searchConditionList: [
          {
            desc: '导出来源',
            name: 'menuSource',
            value: '2'
          },
          {
            desc: '总店企业ID',
            name: 'mainEid',
            value: query.eid || ''
          },
          {
            desc: '门店企业ID',
            name: 'shopEid',
            value: query.shopEid || ''
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.goodsName || ''
          },
          {
            desc: '批准文号',
            name: 'license',
            value: query.soLicense || ''
          },
          {
            desc: '销售时间-开始',
            name: 'startTime',
            value: query.createTime && query.createTime.length ? query.createTime[0] : ''
          },
          {
            desc: '销售时间-结束',
            name: 'endTime',
            value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
          },
          {
            desc: '省份编码',
            name: 'provinceCode',
            value: query.provinceCode || ''
          },
          {
            desc: '城市编码',
            name: 'cityCode',
            value: query.cityCode || ''
          },
          {
            desc: '区域编码',
            name: 'regionCode',
            value: query.regionCode || ''
          },
          {
            desc: '商业标签',
            name: 'enterpriseTagId',
            value: query.enterpriseTagIdList.join(',') || ''
          }
        ]

      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 搜索总店名称
    async remoteMethod(query) {
      if (query !== '') {
        this.sellSpecificationsLoading = true
        let data = await queryFlowShopEnterpriseList(query, 1)
        this.sellSpecificationsLoading = false
        if (data) {
          this.sellSpecificationsOption = data
        }
      }
    },
    headquartersChange() {
      let currentdate = this.sellSpecificationsOption.find(v => v.eid === this.query.eid)
      this.query.ename = currentdate.ename
    },
    // 搜索门店名称
    async remoteMethodSubbranch(query) {
      if (query !== '') {
        this.sellSpecificationsLoading = true
        let data = await queryFlowShopEnterpriseList(query, 2)
        this.sellSpecificationsLoading = false
        if (data) {
          this.sellSpecificationsOption2 = data
        }
      }
    },
    async getEnterpriseTagListMethods() {
      let data = await getEnterpriseTagList()
      if (typeof data !== 'undefined') {
        this.tagList = data.list
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
