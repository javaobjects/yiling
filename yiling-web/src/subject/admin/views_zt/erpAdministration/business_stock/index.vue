<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="8" class="demo-ruleForm-form">
              <div class="title">商业名称</div>
              <el-input v-model.trim="query.bussinessName" placeholder="请输入内容" show-word-limit></el-input>
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
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.productsName" placeholder="请输入内容" />
            </el-col>
            <el-col :span="6">
              <div class="title">批准文号</div>
              <el-input v-model="query.licenseNumber" placeholder="请输入内容" />
            </el-col>
            <el-col :span="6">
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
            <el-col :span="6">
              <div class="title">订单来源</div>
              <el-checkbox-group v-model="query.source">
                <el-checkbox class="option-class" v-for="item in erpGoodsBatchFlowSource" :label="item.value" :key="item.value">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="24">
              <div class="title">商业标签</div>
              <el-checkbox-group v-model="query.enterpriseTagIdList">
                <el-checkbox class="option-class" v-for="item in tagList" :key="item.id" :label="item.id" >{{ item.name }}</el-checkbox>
              </el-checkbox-group >
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">有无标准库规格关系</div>
              <el-radio-group v-model="query.specificationIdFlag">
                <el-radio :label="1">有</el-radio>
                <el-radio :label="0">无</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
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
          <ylButton type="primary" plain @click="downLoadTemp">导出查询结果</ylButton>
        </div>
      </div>
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
          <el-table-column 
            label-class-name="mar-l-16" 
            label="商品信息" 
            align="left" 
            min-width="220px" 
            fixed="left">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="title font-size-lg bold clamp-t-1">{{ row.gbName }}</div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbSpecifications }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbLicense }}</span></div>
                <div class="font-size-base font-title-color"><span>{{ row.gbManufacturer }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商业名称" align="center" min-width="220px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.ename }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单来源" align="center" min-width="150px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbSource | dictLabel(erpGoodsBatchFlowSource) }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品内码" align="center" min-width="80px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.inSn }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="批号" align="center" min-width="100px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbBatchNo }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="生产日期" align="center" min-width="160px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbProduceTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="有效期至" align="center" min-width="160px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbEndTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数量" align="center" min-width="50px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbNumber }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单位" align="center" min-width="50px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.gbUnit }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="总计数量" align="center" min-width="80px">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.totalNumber }}</span></div>
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
import { getErpQueryGoodsBatchListPage, queryEnterpriseList, getEnterpriseTagList, queryGoodsSpecification } from '@/subject/admin/api/zt_api/erp_flow'
import { erpGoodsBatchFlowSource } from '@/subject/admin/busi/erp/flowDirection';
import { createDownLoad } from '@/subject/admin/api/common';
import { ylChooseAddress } from '@/subject/admin/components';

export default {
  name: 'ErpBussinessStock',
  components: {
    ylChooseAddress
  },
  computed: {
    erpGoodsBatchFlowSource() {
      return erpGoodsBatchFlowSource();
    }
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        bussinessName: '',
        source: [],
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
      sellSpecificationsLoading: false,
      sellSpecificationsOption: []
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
            //获取企业名称
            this.qyDataApi(); 
            this.downShow = true;
          } else {
            this.downShow = false;
          }
        }
      },
      deep: true
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.name) {
      this.query.bussinessName = query.name;
      this.optionsClick({
        ename: query.name
      })
      this.handleSearch();
    }
  },
  activated() {
    this.getEnterpriseTagListMethods()
  },
  methods: {
    async getList() {
      this.loading = true;
      let query = this.query;
      let data = await getErpQueryGoodsBatchListPage(
        query.page,
        query.limit,
        //商业名称
        query.bussinessName, 
        //商品名称
        query.productsName, 
        query.licenseNumber,
        query.manufacturer,
        query.source,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.enterpriseTagIdList,
        // 商品规格ID
        query.sellSpecificationsId,
        // 有无标准库规格关系：0-无, 1-有
        query.specificationIdFlag
      );
      this.loading = false;
      console.log(data);
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
        this.query.licenseNumber ||
        this.query.source.length != 0 ||
        this.query.provinceCode ||
        this.query.cityCode ||
        this.query.regionCode ||
        this.query.enterpriseTagIdList.length != 0 ||
        this.query.sellSpecificationsId ||
        this.query.specificationIdFlag !== undefined
      ) {
        this.getList();
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
        className: 'flowGoodsBatchPageListExportService',
        fileName: '库存流向导出',
        groupName: 'erp流向',
        menuName: '运营后台 - erp流向',
        searchConditionList: [
          {
            desc: '导出来源',
            name: 'menuSource',
            value: '1'
          },
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
            desc: '订单来源',
            name: 'source',
            value: query.source.join(',') || ''
          },
          {
            desc: '商品批准文号',
            name: 'license',
            value: query.licenseNumber || ''
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
