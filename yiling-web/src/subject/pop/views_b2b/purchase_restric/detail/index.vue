<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <!-- 商品信息 -->
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>商品信息
        </div>
        <div class="content-box">
          <div class="content-box-bottom">
            <div class="product-img">
              <img :src="detailData.pic" alt="">
            </div>
            <div class="product-info">
              <el-row class="box">
                <el-col :span="8">
                  <div class="intro">
                    <span class="font-title-color">商品名称：</span>
                    {{ detailData.name || '- -' }}
                  </div>
                </el-col>
                <el-col :span="16">
                  <div class="intro">
                    <span class="font-title-color">批准文号/生产许可证号：</span>
                    {{ detailData.licenseNo || '- -' }}
                  </div>
                </el-col>
              </el-row>
              <el-row class="box">
                <el-col :span="8">
                  <div class="intro">
                    <span class="font-title-color">包装规格：</span>
                    {{ detailData.sellSpecifications || '- -' }}
                  </div>
                </el-col>
                <el-col :span="16">
                  <div class="intro">
                    <span class="font-title-color">生产厂家：</span>
                    {{ detailData.manufacturer || '- -' }}
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <!-- 限购规则 -->
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>限购规则
        </div>
        <div class="content-box">
          <el-form 
            class="regulation"
            :model="purchaseRestrictionVO" 
            ref="form" 
            :rules="rules" 
            label-width="120px">
            <el-form-item label="每单限购" prop="orderRestrictionQuantity">
              <el-input class="width-200" v-model="purchaseRestrictionVO.orderRestrictionQuantity" placeholder="请输入每单限购"></el-input>
            </el-form-item>
            <el-form-item label="限购时间">
              <el-radio-group v-model="purchaseRestrictionVO.timeType" prop="timeType" @change="changeGroupType">
                <el-radio :label="2">每天</el-radio>
                <el-radio :label="3">每周</el-radio>
                <el-radio :label="4">每月</el-radio>
                <el-radio :label="1">自定义</el-radio>
              </el-radio-group>
              <span class="demonstration">请选择时间:</span>
              <el-date-picker 
                :disabled="purchaseRestrictionVO.timeType !== 1"
                v-model="purchaseRestrictionVO.time"
                type="daterange" 
                format="yyyy 年 MM 月 dd 日" 
                value-format="yyyy-MM-dd" 
                range-separator="至" 
                start-placeholder="开始时间" 
                end-placeholder="结束时间"
                :picker-options="pickerOptions"
                @focus="dateFocus"
                @change="changeTime">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="时间内总限购" prop="timeRestrictionQuantity">
              <el-input class="width-200" v-model="purchaseRestrictionVO.timeRestrictionQuantity" placeholder="请输入时间内总限购"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <!-- 客户设置 -->
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>客户设置
        </div>
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="7">
                <div class="title">客户设置</div>
                <el-radio-group v-model="purchaseRestrictionVO.customerSettingType" prop="timeType">
                  <el-radio :label="0">全部客户</el-radio>
                  <el-radio :label="1">部分客户</el-radio>
                </el-radio-group>
              </el-col>
              <el-col :span="8" v-if="purchaseRestrictionVO.customerSettingType === 1">
                <div class="title">客户名称</div>
                <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入客户名称" />
              </el-col>
              <el-col :span="8" v-if="purchaseRestrictionVO.customerSettingType === 1">
                <div class="title">客户分组</div>
                <el-select v-model="query.customerGroupId" placeholder="请选择客户分组" >
                  <el-option 
                    v-for="item in groupList" 
                    :key="item.id" 
                    :label="item.name" 
                    :value="item.id"></el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box" v-if="purchaseRestrictionVO.customerSettingType === 1">
            <el-row class="box">
              <el-col :span="7">
                <div class="title">客户类型</div>
                <el-select v-model="query.customerGroupType" placeholder="请选择商品状态">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option
                    v-show="item.value != 1 && item.value != 2"
                    v-for="item in geTenterpriseType"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="17">
                <div class="title">企业地址</div>
                <div class="flex-row-left">
                  <yl-choose-address is-all width="238px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" />
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="search-box" v-if="purchaseRestrictionVO.customerSettingType === 1">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="content-box" v-if="purchaseRestrictionVO.customerSettingType === 1">
          <!-- 按钮 -->
          <div class="down-box clearfix">
            <div class="right-btn">
              <yl-button type="danger" v-if="goodsLimitList.length" @click="multiDelGoodsItem">查询结果批量删除</yl-button>
              <yl-button class="mar-r-8 my-buttom" type="primary" @click="addGoodsClick">添加客户</yl-button>
            </div>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table 
              :show-header="true" 
              stripe 
              :list="goodsLimitList" 
              :total="query.total" 
              :page.sync="query.page" 
              :limit.sync="query.limit" 
              :loading="loading"
              @getList="getGoodsList">
                <el-table-column label="客户名称" align="center" prop="customerName">
                </el-table-column>
                <el-table-column label="省" align="center" prop="provinceName">
                </el-table-column>
                <el-table-column label="市" align="center" prop="cityName"></el-table-column>
                <el-table-column label="区" align="center" prop="regionName">
                </el-table-column>
                <el-table-column label="用户类型" align="center">
                  <template slot-scope="{ row }">
                    <div>{{ row.type | dictLabel(geTenterpriseType) }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="用户分组" align="center">
                  <template slot-scope="{ row }">
                    <div>{{ row.customerGroupName || '- -' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" min-width="120" align="center" fixed="right">
                  <template slot-scope="{ row }">
                    <div class="operation-view">
                      <yl-button type="text" @click="goodsRemoveClick(row)">删除</yl-button>
                    </div>
                  </template>
                </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>

    <!-- 添加商品 -->
    <yl-dialog 
      title="添加" 
      :visible.sync="addGoodsDialog" 
      width="1100px" 
      :show-footer="false"
      @close="closeAddGoodsDialog">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="7">
                <div class="title">客户名称</div>
                <el-input v-model="goodsQuery.name" @keyup.enter.native="searchEnterAdd" placeholder="请输入客户名称" />
              </el-col>
              <el-col :span="8">
                <div class="title">客户分组</div>
                <el-select style="width: 387px;" v-model="goodsQuery.customerGroupId" placeholder="请选择客户分组">
                  <el-option 
                    v-for="item in groupList" 
                    :key="item.id" 
                    :label="item.name" 
                    :value="item.id"></el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="7">
                <div class="title">客户类型</div>
                <el-select
                  v-model="goodsQuery.customerGroupType"
                  placeholder="请选择客户类型">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option
                    v-for="item in geTenterpriseType"
                    v-show="item.value != 1 && item.value != 2"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="17">
                <div class="title">企业地址</div>
                <div class="flex-row-left">
                  <yl-choose-address is-all width="238px" :province.sync="goodsQuery.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" />
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="goodsQuery.total" @search="handleSearchAdd" @reset="handleResetAdd" />
                <div class="search-title">(最多添加2000条数据)</div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="multi-action">
          <yl-button 
            plain 
            type="primary" 
            v-if="addGoodsList.length" 
            @click="multiAddGoodsItem">批量添加</yl-button>
        </div>
        <div class="mar-t-10">
          <yl-table
            :show-header="true" 
            stripe 
            :list="addGoodsList"
            :total="goodsQuery.total"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="addLoading"
            @getList="getGoodsListAdd">
            <el-table-column label="客户名称" align="center" prop="customerName">
            </el-table-column>
            <el-table-column label="省" align="center" prop="provinceName">
            </el-table-column>
            <el-table-column label="市" align="center" prop="cityName"></el-table-column>
            <el-table-column label="区" align="center" prop="regionName">
            </el-table-column>
            <el-table-column label="用户类型" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.type | dictLabel(geTenterpriseType) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="用户分组" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.customerGroupName || '- -' }}</div>
              </template>
            </el-table-column>
            <el-table-column 
              label="操作" 
              min-width="120" 
              align="center" 
              fixed="right">
              <template slot-scope="{ row }">
                <div class="operation-view">
                  <yl-button 
                    class="view-btn" 
                    type="text" 
                    @click="goodsItemAddClick(row)" 
                    v-if="!row.isPurchaseRestriction">添加</yl-button>
                  <yl-button 
                    class="edit-btn" 
                    disabled 
                    type="text" 
                    v-else>已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { enterpriseType } from '@/subject/pop/utils/busi';
import { getPurchaseRestriction, b2bProControlSellGroupList, restrictionCustomerPage, deleteAllRestrictionCustomer, deleteRestrictionCustomer, restrictionAddCustomerPage, addRestrictionCustomer, batchAddRestrictionCustomerByQuery, savePurchaseRestriction } from '@/subject/pop/api/b2b_api/purchase_restric'
import { ylChooseAddress } from '@/subject/pop/components'
import { formatDate } from '@/subject/pop/utils'
export default {
  components: {
    ylChooseAddress
  },
  computed: {
    // 客户类型（企业类型）
    geTenterpriseType() {
      return enterpriseType();
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/zt_dashboard'
        },
        {
          title: '客户管理'
        },
        {
          title: '限购管理',
          path: '/b2b_products/purchase_restric'
        },
        {
          title: '编辑限购'
        }
      ],
      params: '',
      // 详情数据
      detailData: '',
      // 限购规则
      purchaseRestrictionVO: {
        customerSettingType: '',
        goodsId: '',
        id: '',
        orderRestrictionQuantity: '',
        status: '',
        timeRestrictionQuantity: '',
        endTime: '',
        startTime: '',
        time: [],
        timeType: ''
      },
      // 客户类型列表
      customerGroupList: [],
      // 客户分组
      goodStatus: '',
      // 客户分组列表
      groupList: [],
      // 客户类型
      goodsStatus: '',
      rules: {
        timeType: [{ required: true, message: '请选择限购时间', trigger: 'blur' }],
        orderRestrictionQuantity: [
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'} 
        ],
        timeRestrictionQuantity: [
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'} 
        ]
      },
      loading: false,
      query: {
        total: 0,
        page: 1,
        limit: 10,
        name: '',
        // 客户分组id
        customerGroupId: '',
        // 客户分组类型
        customerGroupType: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      // 添加商品弹框
      addGoodsDialog: false,
      addLoading: false,
      providerList: [],
      providerTotal: 0,
      // 添加弹窗搜索query
      goodsQuery: {
        total: 0,
        page: 1,
        limit: 10,
        name: '',
        // 客户分组id
        customerGroupId: '',
        // 客户分组类型
        customerGroupType: 0,
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      addGoodsList: [],
      // 已选商品列表
      goodsLimitList: [],
      // 日期区间选择器配置
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          this.cuttentTime = minDate.getTime()
          if (maxDate) {
            this.cuttentTime = ''
          }
        },
        disabledDate: (time) => {
          if (this.cuttentTime !== '') {
            const one = 6 * 30 * 24 * 3600 * 1000
            const minTime = this.cuttentTime - one
            const maxTime = this.cuttentTime + one
            return time.getTime() <= minTime || time.getTime() >= maxTime
          }
        }
      }
    };
  },
  mounted() {
    let params = this.$route.params
    if (params) {
      this.params = params
    }
    this.getDetail();
    // 客户分组列表
    this.getCustomerGroupList();
    // 底部table列表
    this.getGoodsList();
  },
  methods: {
    async getDetail() {
      this.$common.showLoad();
      let detailData = await getPurchaseRestriction(this.params.id);
      this.$common.hideLoad();
      if (detailData !== 'undefined') {
        this.detailData = detailData
        if (detailData.purchaseRestrictionVO.startTime && detailData.purchaseRestrictionVO.endTime) {
          detailData.purchaseRestrictionVO.startTime = formatDate(detailData.purchaseRestrictionVO.startTime, 'yyyy-MM-dd') 
          detailData.purchaseRestrictionVO.endTime = formatDate(detailData.purchaseRestrictionVO.endTime, 'yyyy-MM-dd')
          detailData.purchaseRestrictionVO.time = [detailData.purchaseRestrictionVO.startTime, detailData.purchaseRestrictionVO.endTime] 
        }
        if (detailData.purchaseRestrictionVO.orderRestrictionQuantity === 0) {
          detailData.purchaseRestrictionVO.orderRestrictionQuantity = ''
        }
        if (detailData.purchaseRestrictionVO.timeRestrictionQuantity === 0) {
          detailData.purchaseRestrictionVO.timeRestrictionQuantity = ''
        }
        if (!detailData.purchaseRestrictionVO.time) {
          detailData.purchaseRestrictionVO.time = []
        }
        this.purchaseRestrictionVO = {...this.purchaseRestrictionVO, ...detailData.purchaseRestrictionVO }
      }
    },
    changeGroupType() {
      if ( this.purchaseRestrictionVO.timeType !== 1 ) {
        this.purchaseRestrictionVO.time = []
        this.purchaseRestrictionVO.startTime = ''
        this.purchaseRestrictionVO.endTime = ''
      }
    },
    changeTime() {
      this.purchaseRestrictionVO.startTime = this.purchaseRestrictionVO.time[0] || ''
      this.purchaseRestrictionVO.endTime = this.purchaseRestrictionVO.time[1] || ''
    },
    // 底部table-客户分组列表
    async getCustomerGroupList() {
      let data = await b2bProControlSellGroupList()
      if (data !== undefined) {
        this.groupList = data.list
      }
    },
    // 获取底部table列表
    async getGoodsList() {
      this.loading = true
      let query = this.query
      let data = await restrictionCustomerPage(
        query.page,
        query.limit,
        query.customerGroupId,
        Number(this.params.id),
        query.name,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.customerGroupType
      );
      this.loading = false
      if (data !== undefined) {
        this.goodsLimitList = data.records
        this.query.total = data.total
      }
    },
    searchEnter() {
      this.getGoodsList()
    },
    handleSearch() {
      this.query.page = 1
      this.query.goodsStatus = '0'
      this.getGoodsList()
    },
    handleReset() {
      this.query = {
        total: 0,
        page: 1,
        limit: 10,
        name: '',
        // 客户分组id
        customerGroupId: '',
        // 客户分组类型
        customerGroupType: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
    },
    // 底部table-批量删除
    async multiDelGoodsItem () {
      let query = this.query
      this.$common.showLoad()
      let data = await deleteAllRestrictionCustomer(
        '',
        '',
        Number(this.params.id),
        query.customerGroupId,
        query.name,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.customerGroupType
      );
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('删除成功');
        this.getGoodsList();
      }
    },
    // 底部table -单个删除
    async goodsRemoveClick(row) {
      this.$common.showLoad()
      let data = await deleteRestrictionCustomer(
        row.customerEid,
        Number(this.params.id)
      );
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('删除成功');
        this.getGoodsList();
      }
    },
    // 添加商品弹框点击
    addGoodsClick() {
      this.addGoodsDialog = true
      this.getGoodsListAdd()
    },
    async getGoodsListAdd() {
      let goodsQuery = this.goodsQuery
      this.addLoading = true
      let data = await restrictionAddCustomerPage(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.customerGroupId,
        Number(this.params.id),
        goodsQuery.name,
        goodsQuery.provinceCode,
        goodsQuery.cityCode,
        goodsQuery.regionCode,
        goodsQuery.customerGroupType
      );
      this.addLoading = false
      if (data !== undefined) {
        this.addGoodsList = data.records
        this.goodsQuery.total = data.total
      }
    },
    searchEnterAdd() {
      this.getGoodsListAdd();
    },
    handleSearchAdd() {
      this.query.page = 1
      this.getGoodsListAdd()
    },
    handleResetAdd() {
      this.goodsQuery = {
        total: 0,
        page: 1,
        limit: 10,
        name: '',
        // 客户分组id
        customerGroupId: '',
        // 客户分组类型
        customerGroupType: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
    },
    // 添加商品弹框每一项企业点击
    async goodsItemAddClick(res) {
      let row = res
      this.$common.showLoad()
      let data = await addRestrictionCustomer(
        row.customerEid,
        Number(this.params.id)
      );
      this.$common.hideLoad()
      if (data !== undefined) {
        row.isPurchaseRestriction = true
      }
    },
    // 商品-批量添加
    async multiAddGoodsItem() {
      let goodsQuery = this.goodsQuery
      this.$common.showLoad()
      let data = await batchAddRestrictionCustomerByQuery(
        '',
        '',
        Number(this.params.id),
        goodsQuery.customerGroupId,
        goodsQuery.name,
        goodsQuery.provinceCode,
        goodsQuery.cityCode,
        goodsQuery.regionCode,
        goodsQuery.customerGroupType
      );
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('批量添加成功');
        this.getGoodsListAdd();
      }
    },
    closeAddGoodsDialog(){
      this.getGoodsList();
    },
    // 保存或修改
    async saveClick() {
      let purchaseRestrictionVO = this.purchaseRestrictionVO
      if (purchaseRestrictionVO.timeRestrictionQuantity !== '') {
        if (purchaseRestrictionVO.timeType === 1 && (purchaseRestrictionVO.time&&purchaseRestrictionVO.time.length > 2) || !purchaseRestrictionVO.timeType ) {
          this.$common.warn('请正确限购时间！')
          return false
        }
      }
      if (purchaseRestrictionVO.customerSettingType === 1 && this.goodsLimitList.length == 0 ) {
        this.$common.warn('请正确添加客户！')
        return false
      }
      if (purchaseRestrictionVO.timeType !== 1 ) {
        this.purchaseRestrictionVO.startTime = ''
        this.purchaseRestrictionVO.endTime = ''
      }
      this.$common.showLoad();
      let data = await savePurchaseRestriction(
        this.purchaseRestrictionVO.customerSettingType,
        Number(this.params.id),
        purchaseRestrictionVO.id,
        Number(purchaseRestrictionVO.orderRestrictionQuantity),
        purchaseRestrictionVO.startTime,
        purchaseRestrictionVO.endTime,
        Number(purchaseRestrictionVO.timeRestrictionQuantity),
        purchaseRestrictionVO.timeType
      );
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('保存成功');
        this.$router.go(-1);
      }
    },
    dateFocus() {
      this.minDate = '';
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
