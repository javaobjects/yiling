<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">查询类型</div>
              <el-select v-model="query.timeType" @change="timeChange" placeholder="请选择">
                <el-option label="6个月以内" :value="0"></el-option>
                <el-option label="6个月以外" :value="1"></el-option>
              </el-select>
            </el-col>
            <el-col :span="8" v-show="query.timeType">
              <div class="title">购进时间</div>
              <el-date-picker
                v-model="query.time"
                type="month"
                value-format="yyyy-MM"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                :picker-options="pickerData">
              </el-date-picker>
            </el-col>
            <el-col :span="8" v-show="!query.timeType">
              <div class="title">购进时间</div>
              <el-date-picker
                v-model="query.createTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">标准商品名称</div>
              <el-select
                v-model="query.sellSpecificationsId"
                clearable
                filterable
                remote
                reserve-keyword
                placeholder="请输入标准商品名称搜索"
                :remote-method="remoteMethod"
                :loading="sellSpecificationsLoading"
              >
                <el-option v-for="item in sellSpecificationsOption" :key="item.sellSpecificationsId" :label="item.name" :value="item.sellSpecificationsId">
                  <span>{{ item.name }}---{{ item.sellSpecifications }}---{{ item.manufacturer }}</span>
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8" class="demo-ruleForm-form">
              <div class="title">商业名称</div>
              <el-input v-model.trim="query.bussinessName" placeholder="请输入商业名称搜索" show-word-limit></el-input>
              <div class="drop-down" v-if="downShow">
                <ul>
                  <li v-for="(item, index) in optionsData" :key="index" @click="optionsClick(item)">
                    {{ item.ename }}
                  </li>
                </ul>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="title">区域查询</div>
              <div class="flex-row-left">
                <yl-choose-address :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" is-all />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">

            <el-col :span="8">
              <div class="title">供应商名称</div>
              <el-input v-model="query.supplierName" placeholder="请输入内容" @keyup.enter.native="handleSearch"/>
            </el-col>

            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.productsName" placeholder="请输入内容" @keyup.enter.native="handleSearch"/>
            </el-col>
            <el-col :span="8">
              <div class="title">批准文号</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入内容" @keyup.enter.native="handleSearch"/>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">订单来源</div>
              <el-checkbox-group v-model="query.source">
                <el-checkbox class="option-class" v-for="item in erpPurchaseFlowSource" :label="item.value" :key="item.value">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
            <el-col :span="8">
              <div class="title">有无标准库规格关系</div>
              <el-radio-group v-model="query.specificationIdFlag">
                <el-radio :label="1">有</el-radio>
                <el-radio :label="0">无</el-radio>
              </el-radio-group>
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
        <yl-table ref="table" :list="dataList" stripe :total="total" :row-class-name="() => 'table-row'" show-header :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :cell-no-pad="true" @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="商品信息" align="left" min-width="300" fixed="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.goodsName }}</div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.poSpecifications }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.poLicense }}</span></div>
                <div class="font-size-base font-title-color"><span>{{ row.poManufacturer }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="供应商信息" align="left" min-width="220">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.enterpriseName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商业名称" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.ename }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单来源" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poSource | dictLabel(erpPurchaseFlowSource) }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="采购订单号" align="left" min-width="170">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poNo }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="购进时间" align="left" min-width="170">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数量" min-width="80" align="left">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poQuantity }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单位" min-width="80" align="left">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poUnit }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="含税单价" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poPrice }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="金额" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poTotalAmount }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="批号" align="left" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poBatchNo }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="有效期至" align="left" min-width="170">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.poEffectiveTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="省" align="center" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.provinceName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="市" align="center" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.cityName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="区" align="center" min-width="80">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.regionName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="标准商品信息" align="center" min-width="100">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.standardName }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.standardSellSpecifications }}</span></div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getErpQueryPurchaseListPage, queryEnterpriseList, getEnterpriseTagList, queryGoodsSpecification } from '@/subject/pop/api/erp_flow';
import { erpPurchaseFlowSource } from '@/subject/pop/utils/busi';
import { ylChooseAddress } from '@/subject/pop/components';
import { createDownLoad } from '@/subject/pop/api/common';

export default {
  name: 'ErpBussinessBuyFlow',
  components: {
    ylChooseAddress
  },
  computed: {
    erpPurchaseFlowSource() {
      return erpPurchaseFlowSource();
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
          title: 'erp'
        },
        {
          title: '药品采购流向'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        timeType: 0,
        createTime: [],
        time: '',
        bussinessName: '',
        source: [],//订单来源
        enterpriseTagIdList: [],
        // 有无标准库规格关系：0-无, 1-有
        specificationIdFlag: undefined
      },
      total: 0,
      dataList: [],
      downShow: false,
      optionsData: [],
      optionS: '',
      tagList: [],
      pickerOptions: {
        disabledDate: (time) => {
          //当前日期（具体到秒）
          const date = new Date()
          //当前年份
          const year = date.getFullYear() 
          //当前月份
          let month = date.getMonth() + 1
          // 1-9月份前加0
          if (month >= 1 && month <= 9) {
            month = '0' + month
          }
          const currentdate = year.toString() + '-' + month.toString()
          const timeyear = time.getFullYear()
          let timemonth = time.getMonth() + 1
          if (timemonth >= 1 && timemonth <= 9) {
              timemonth = '0' + timemonth
            }
          const timedate = timeyear.toString() + timemonth.toString()
          // /**当月 */
          let reduce = this.calcMonths(currentdate, - 5)
          let plus = year.toString() + month.toString()
          return timedate < reduce || plus < timedate
        }
      },
      sellSpecificationsLoading: false,
      sellSpecificationsOption: [],
      pickerData: {
        disabledDate: (time) => {
          //当前日期（具体到秒）
          const date = new Date()
          //当前年份
          const year = date.getFullYear() 
          //当前月份
          let month = date.getMonth() + 1
          // 1-9月份前加0
          if (month >= 1 && month <= 9) {
            month = '0' + month
          }
          const currentdate = year.toString() + '-' + month.toString()
          const timeyear = time.getFullYear()
          let timemonth = time.getMonth() + 1
          if (timemonth >= 1 && timemonth <= 9) {
              timemonth = '0' + timemonth
            }
          const timedate = timeyear.toString() + timemonth.toString()
          // /**当月 */
          let reduce = this.calcMonths(currentdate, - 5)
          let plus = year.toString() + month.toString()
          return !(timedate < reduce || plus < timedate) || time.getFullYear() < '2020' || time.getFullYear() > new Date().getFullYear()
        }
      }
    };
  },
  watch: {
    'query.bussinessName': {
      handler(newName, oldName) {
        if (newName == '') {
          this.downShow = false;
        }
        if (newName != this.optionS) {
          if (newName != '') {
            this.qyDataApi(); //获取企业名称
            this.downShow = true;
          } else {
            this.downShow = false;
          }
        }
      },
      deep: true
    }
  },
  created() {
    // this.getList();
  },
  activated() {
    this.getEnterpriseTagListMethods()
  },
  methods: {
    // 年月加减
    calcMonths(originalYtd, monthNum) {
      let arr = originalYtd.split('-');
      let year = parseInt(arr[0]);
      let month = parseInt(arr[1]);
      month = month + monthNum;
      if (month > 12) {
        let yearNum = parseInt((month - 1) / 12);
        month = month % 12 == 0 ? 12 : month % 12;
        year += yearNum;
      } else if (month <= 0) {
        month = Math.abs(month);
        let yearNum = parseInt((month + 12) / 12);
        let n = month % 12;
        if (n == 0) {
          year -= yearNum;
          month = 12
        } else {
          year -= yearNum;
          month = Math.abs(12 - n)
        }
 
      }
      month = month < 10 ? '0' + month : month;
      return year + month;
    },
    //点击查询类型切换
    timeChange() {
      this.query.createTime = [];
      this.query.time = ''
    },
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getErpQueryPurchaseListPage(
        query.page,
        query.limit,
        //商业名称
        query.bussinessName,
        //商品名称
        query.productsName,
        //供应商名称
        query.supplierName,
        query.licenseNumber,
        query.manufacturer,
        query.timeType == 1 ? query.time + '-01' : (query.createTime && query.createTime.length > 0 ? query.createTime[0] : ''),
        query.timeType == 1 ? '' : (query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''),
        query.source,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.enterpriseTagIdList,
        query.sellSpecificationsId,
        // 有无标准库规格关系：0-无, 1-有
        query.specificationIdFlag,
        query.timeType
      );
      this.loading = false;
      if (data !== undefined) {
        this.dataList = data.records;
        this.total = data.total;
      }
    },
    async getEnterpriseTagListMethods() {
      let data = await getEnterpriseTagList()
      if (data !== undefined) {
        this.tagList = data.list
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      if (
        this.query.bussinessName ||
        this.query.productsName ||
        this.query.supplierName ||
        this.query.createTime.length != 0 ||
        this.query.licenseNumber ||
        this.query.source.length != 0 ||
        this.query.provinceCode ||
        this.query.cityCode ||
        this.query.regionCode ||
        this.query.enterpriseTagIdList.length != 0 ||
        this.query.sellSpecificationsId ||
        this.query.specificationIdFlag !== undefined ||
        this.query.time
      ) {
        if (this.query.createTime.length != 0 || this.query.time != '') {
          this.getList();
        } else {
          this.$message.error('请选择购进时间');
        }
      } else {
        this.$message.warning('请至少输入一个查询条件')
        this.dataList = []
        this.total = 0
      }
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        timeType: 0,
        createTime: [],
        time: '',
        bussinessName: '',
        source: [],
        enterpriseTagIdList: [],
        specificationIdFlag: undefined
      };
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
        className: 'flowPurchasePageListExportService',
        fileName: '采购流向导出',
        groupName: 'erp流向',
        menuName: 'POP管理-erp流向',
        searchConditionList: [
          {
            desc: '商业名称',
            name: 'ename',
            value: query.bussinessName || ''
          },
          {
            desc: '商品名称',
            name: 'goodsName',
            value: query.productsName || ''
          },
          {
            desc: '商品批准文号',
            name: 'license',
            value: query.licenseNumber || ''
          },
          {
            desc: '供应商名称',
            name: 'enterpriseName',
            value: query.supplierName || ''
          },
          {
            desc: '订单来源',
            name: 'source',
            value: query.source.join(',') || ''
          },
          {
            desc: '下单时间-开始',
            name: 'startTime',
            value: query.timeType == 1 ? query.time + '-01' : (query.createTime && query.createTime.length > 0 ? query.createTime[0] : '')
          },
          {
            desc: '下单时间-结束',
            name: 'endTime',
            value: query.timeType == 1 ? '' : (query.createTime && query.createTime.length > 1 ? query.createTime[1] : '')
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
          },
          {
            desc: '	商品规格ID',
            name: 'sellSpecificationsId',
            value: query.sellSpecificationsId || ''
          },
          {
            desc: '有无标准库规格关系',
            name: 'specificationIdFlag',
            value: query.specificationIdFlag
          },
          {
            desc: '查询类型',
            name: 'timeType',
            value: query.timeType
          }
        ]

      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    // 获取企业名称
    async qyDataApi() {
      let data = await queryEnterpriseList(this.query.bussinessName)
      if (data !== undefined) {
        this.optionsData = data;
      }
    },
    // 点击下拉框的 内容
    optionsClick(item) {
      console.log(item);
      this.query.bussinessName = this.optionS = item.ename;
      this.downShow = false;
    },
    // 搜索规格名称
    async remoteMethod(query) {
      if (query !== '') {
        this.sellSpecificationsLoading = true
        let data = await queryGoodsSpecification(query)
        this.sellSpecificationsLoading = false
        if (data) {
          this.sellSpecificationsOption = data.list
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
