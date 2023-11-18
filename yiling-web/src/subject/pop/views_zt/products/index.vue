<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="add-box" v-if="erpSyncLevel === 0">
          <ylButton v-role-btn="['5']" v-if="erpSyncLevel === 0" type="primary" @click="goAddProduct()">添加商品</ylButton>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品编码</div>
              <el-input v-model="query.goodsId" @keyup.enter.native="searchEnter" placeholder="请输入平台商品编码" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品通用名" />
            </el-col>
            <el-col :span="6">
              <div class="title">批文许可</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="批准文号/注册证编码/生产许可证" />
            </el-col>
            <el-col :span="6">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家名称" />
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="6">
              <div class="title">外部编码</div>
              <el-input v-model="query.inSnOrSn" @keyup.enter.native="searchEnter" placeholder="请输入ERP内码或ERP编码" />
            </el-col>
            <el-col :span="6">
              <div class="title">供货渠道</div>
              <el-checkbox-group v-model="query.goodsLines" @change="handleSelectGoodsline">
                <el-checkbox v-for="(item, index) in goodsLine" :key="index" :label="item.value">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-24">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="bottom-table-view common-box mar-t-16 mar-b-8">
        <div class="tab-box">
          <div class="tab">
            <div
              v-for="(item, index) in tabListFilter"
              :key="index"
              class="tab-item"
              :class="tabActive == item.value ? 'tab-active' : ''"
              @click="clickTab(item.value)"
            >
              <span>{{ item.label }}</span>
            </div>
          </div>
          <div class="btn-box">
            <ylButton v-if="query.auditStatus === 4" v-role-btn="['4']" type="primary" plain @click="upDownChange(1)">配置供货渠道</ylButton>
            <ylButton v-role-btn="['6']" @click="handleExport">批量导出</ylButton>
            <ylButton v-role-btn="['7']" @click="handleExportLog">导出记录</ylButton>
          </div>
        </div>
        <!-- 提示语 -->
        <div class="prompt mar-t-16">列表信息（已为您检索出 <span>{{ total }}</span> 条数据）<span class="from-erp" v-if="erpSyncLevel > 0">商品来源为ERP {{ query.auditStatus === 7 ? ',因系统未审核完成或未满足平台商品基础信息不能进入商品库, 请根据驳回原因前往ERP系统中维护!' : '' }}</span></div>
        <!-- 列表 -->
        <div class="my-table mar-t-16">
          <yl-table
            :show-header="true"
            stripe
            :list="dataList"
            :total="total"
            :page.sync="query.page"
            :limit.sync="query.limit"
            :loading="loading"
            :show-select-num="chooseProduct.length > 0"
            :select-num="chooseProduct.length"
            :selection-change="handleSelectionChange"
            @getList="getList"
          >
            <el-table-column type="selection" width="50">
            </el-table-column>
            <el-table-column label="商品信息" prop="id" align="left" min-width="200">
              <template slot-scope="{ row }">
                <div>
                  <div class="products-box">
                    <el-image class="my-image" :src="row.pic" fit="contain" />
                    <div class="products-info">
                      <div class="products-name">{{ row.name }}</div>
                      <div class="basic-info products-id"><span class="color-666">商品ID：</span>{{ row.id }}</div>
                      <div class="basic-info">{{ row.licenseNo }}</div>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <!-- 商品规格:全部的按状态区分 -->
            <el-table-column key="allSpecifications" v-if="query.auditStatus == 0" label="商品规格" align="left" prop="sellSpecifications" min-width="90">
              <template slot-scope="{ row }">
                <div class="specifications-box">
                  <div class="specifications-info" v-if="row.auditStatus == 4">{{ row.sellSpecifications }}</div>
                  <div class="specifications-info" v-if="row.auditStatus == 5 || row.auditStatus == 6">{{ row.specifications }}</div>
                </div>
              </template>
            </el-table-column>
            <!-- 商品规格: 已通过/未通过的取sellSpecifications -->
            <el-table-column key="sellSpecifications" v-if="query.auditStatus == 4 || query.auditStatus == 7" label="商品规格" align="left" prop="sellSpecifications" min-width="90">
              <template slot-scope="{ row }">
                <div class="specifications-box">
                  <div class="specifications-info">{{ row.sellSpecifications }}</div>
                </div>
              </template>
            </el-table-column>
            <!-- 商品规格: 审核中/未通过取specifications -->
            <el-table-column key="specifications" v-if="query.auditStatus == 5 || query.auditStatus == 6" label="商品规格" align="left" prop="specifications" min-width="90">
              <template slot-scope="{ row }">
                <div class="specifications-box">
                  <div class="specifications-info">{{ row.specifications }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column key="manufacturer" label="生产厂家" align="left" prop="manufacturer" min-width="140">
              <template slot-scope="{ row }">
                <div class="facture-box">
                  <div class="facture-info">{{ row.manufacturer }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="query.auditStatus == 0 || query.auditStatus == 4" label="商品类目" min-width="120" align="left" prop="manufacturer">
              <template slot-scope="{ row }">
                <div class="category-box">
                  <span class="category" v-if="row.standardCategoryName1 && row.standardCategoryName2">{{ row.standardCategoryName1 }} ＞ {{ row.standardCategoryName2 }}</span>
                  <span class="basic-info" v-else>暂未匹配</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="query.auditStatus == 0 || query.auditStatus == 4" label="供货渠道" align="left">
              <template slot-scope="{ row }">
                <div class="mall-flag">
                  <div class="table-content" v-if="row.popFlag === 1">以岭分销商城</div>
                  <div class="table-content" v-if="row.mallFlag === 1">大运河采购商城</div>
                  <div class="basic-info" v-if="row.popFlag === 0 && row.mallFlag === 0">暂未供货</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column v-if="query.auditStatus == 0" label="商品状态" align="left">
              <template slot-scope="{ row }">
                <div class="audit-status table-content">
                  <div class="status-box">
                    <div class="status">
                      <div class="status-icon-box">
                        <div :class="['status-icon', auditStatus(row.auditStatus)]"></div>
                        <div class="status-text">{{ row.auditStatus | dictLabel(tabList) }}</div>
                      </div>
                      <yl-button v-if="false" type="text" @click="applyDetail(row)">申请详情</yl-button>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column key="createTime" v-if="query.auditStatus == 0" label="首次添加时间" min-width="180" align="left" prop="createTime">
              <template slot-scope="{ row }">
                <div class="table-content">{{ row.createTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
              </template>
            </el-table-column>
            <el-table-column key="auditCreateTime" min-width="140" v-if="query.auditStatus == 4 || query.auditStatus == 5 || query.auditStatus == 6 || query.auditStatus == 7" label="最近申请时间" align="left">
              <template slot-scope="{ row }">
                <div class="table-content">{{ row.auditInfo && row.auditInfo.createTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
              </template>
            </el-table-column>
            <el-table-column key="auditUpdateTime" min-width="140" v-if="query.auditStatus == 4 || query.auditStatus == 6" label="最近审核时间" align="left">
              <template slot-scope="{ row }">
                <div class="table-content">{{ row.auditInfo && row.auditInfo.updateTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
              </template>
            </el-table-column>
            <el-table-column key="rejectReason" v-if="query.auditStatus == 6 || query.auditStatus == 7" :label="query.auditStatus == 6 ? '驳回原因' : (query.auditStatus == 7 ? '未入库原因' : '')" min-width="120" align="left">
              <template slot-scope="{ row }">
                <div class="reject-tooltip-box">
                  <el-tooltip
                    v-if="row.auditInfo && row.auditInfo.rejectMessage"
                    class="mark-box"
                    popper-class="comment-reject-box"
                    effect="light"
                    placement="top-end"
                  >
                    <div class="reject-reason" slot="content">
                      <div class="reason">原因：</div>
                      <div class="reason-info">
                        {{ row.auditInfo && row.auditInfo.rejectMessage }}
                      </div>
                    </div>
                    <div class="reject-reason-box table-content">
                      <div class="reason-icon"></div>
                      <div class="reason-text" v-if="row.auditInfo && row.auditInfo.rejectMessage"> 原因：{{ row.auditInfo && row.auditInfo.rejectMessage }} </div>
                      <!-- <yl-button class="reject-btn" v-show="row.auditInfo && row.auditInfo.rejectMessage" type="text" @click="applyDetail(row)">详情</yl-button> -->
                    </div>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column class-name="operate-column" label="操作" min-width="80" align="right" v-if="query.auditStatus !== 7">
              <template slot-scope="{ row }">
                <div class="table-content">
                  <!-- // 品状态：4已审核 5待审核 6驳回 -->
                  <!-- <yl-button type="text" @click="editAndDetail(row,'edit')">编辑</yl-button>
                  <yl-button type="text" @click="editAndDetail(row,'see')">查看</yl-button>
                  <yl-button type="text" @click="again(row)">重新提交</yl-button> -->
                  <yl-button v-role-btn="['1']" v-show="row.auditStatus == 4" type="text" @click="editAndDetail(row,'edit')">编辑</yl-button>
                  <yl-button v-role-btn="['2']" v-show="row.auditStatus == 5 || row.auditStatus == 6" type="text" @click="editAndDetail(row,'see')">查看</yl-button>
                  <!-- <yl-button v-role-btn="['3']" v-show="row.auditStatus == 6" type="text" @click="again(row)">重新提交</yl-button> -->
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 审核详情弹窗 -->
      <yl-dialog title="审核详情" :visible.sync="showDialog" @confirm="confirm">
        <div class="pad-16">
          <div class="mar-t-24">
            <yl-table
              :show-header="true"
              stripe
              :list="stockDetailList"
              :total="stockTotal"
              @getList="getDetailList"
              :page.sync="stockQuery.page"
              :limit.sync="stockQuery.limit"
              :loading="loading">
              <el-table-column label="审核时间" align="center" prop="updateTime">
                <template slot-scope="{ row }">
                  <div>{{ row.updateTime | formatDate }}</div>
                </template>
              </el-table-column>
              <el-table-column label="审核人员" align="center" prop="auditUser"></el-table-column>
              <!-- 0待审核1审核通过2审核不通3忽略 -->
              <el-table-column label="审核结果" align="center" prop="auditStatus">
                <template slot-scope="{ row }">
                  <div v-if="row.auditStatus == 2">审核不通过</div>
                  <div v-else>- -</div>
                </template>
              </el-table-column>
              <el-table-column label="反馈详情" align="center" prop="rejectMessage"></el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 批量设置弹窗 -->
      <yl-dialog title="批量配置供货渠道" width="480px" :visible.sync="showProductsLine" @confirm="submitProductsLine">
        <div class="line-content pad-16">
          <el-form :model="lineForm" :rules="lineRules" ref="lineForm" label-width="100px">
            <el-form-item label="供货渠道" prop="productsLine">
              <el-checkbox-group v-model="lineForm.productsLine">
                <el-checkbox v-for="(item, index) in goodsLine" :label="item.value" :key="index">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 批量导出弹窗 -->
      <yl-dialog class="export-dialog" title="导出" width="560px" :visible.sync="showExport" @confirm="confirmExport">
        <div class="export-content">
          <div class="export-title">请选择商品导出范围：</div>
          <div class="export-select">
            <el-radio-group v-model="exportType">
              <el-radio :label="1">导出查询商品</el-radio>
              <el-radio :label="2">导出选中商品</el-radio>
              <el-radio :label="3">导出所有商品</el-radio>
            </el-radio-group>
          </div>
          <div class="export-info">
            <div class="export-info-title">导出说明</div>
            <div class="info-item">导出查询商品：当前页签下查询的所有商品数据导出</div>
            <div class="info-item">导出选中商品：当前页签下当前页面中已选中的商品数据导出</div>
            <div class="info-item">导出所有商品：当前页签下所有商品数据导出</div>
            <div class="info-notice">点击【确定】后，系统开始自动导出，稍后请在“ 导出记录”中查看导出文件</div>
          </div>
        </div>
      </yl-dialog>
      <!-- 导出记录 -->
      <yl-dialog class="export-log-dialog" title="导出记录" width="990px" right-btn-name="关闭" :visible.sync="showExportLog" :show-cancle="false" @confirm="showExportLog = false">
        <div class="export-log-content">
          <div class="export-title">以下为近半年的导出记录</div>
          <div class="export-table-box">
            <yl-table
              stripe
              :max-height="600"
              :show-header="true"
              :list="exportLogList"
              :total="queryExportLog.total"
              :page.sync="queryExportLog.current"
              :limit.sync="queryExportLog.size"
              :loading="exportLoading"
              @getList="getGoodsExportLog"
            >
              <el-table-column label="导出文件名称" min-width="240" align="left" prop="fileName"></el-table-column>
              <el-table-column label="导出数量" align="center" prop="dataNumber"></el-table-column>
              <el-table-column label="导出状态" align="center">
                <template slot-scope="{ row }">
                  <div class="status-box">
                    <div :class="['status-icon', exportStatus(row.status)]"></div>
                    <div class="status-text">{{ row.status | dictLabel(downLoadStatus) }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作时间" width="180" align="center" prop="createTime">
                <template slot-scope="{ row }">
                  <div class="table-content">{{ row.createTime | formatDate('yyyy-MM-dd hh:mm:ss') }}</div>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="{ row }">
                  <yl-button v-show="row.status === 1" type="text" @click="handleDownExportFile(row)">下载文件</yl-button>
                  <yl-button v-show="row.status === -1" type="text" @click="handleReExport(row)">重新导出</yl-button>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getZtProductList, getZtUnStorageList, queryGoodsExportLog, queryReExport, getProductsAuditList, setProductsLines } from '@/subject/pop/api/zt_api/zt_products'
import { queryCurrentEnterpriseInfo } from '@/subject/pop/api/zt_api/dashboard'
import { goodsStatus, goodsLine, downLoadStatus } from '@/subject/pop/utils/busi'
import { mapGetters } from 'vuex'
import { createDownLoad, downLoadFile } from '@/subject/pop/api/common'
import { formatDate } from '@/common/utils'

export default {
  name: 'ZtProductsList',
  components: {
  },
  computed: {
    goodsLine() {
      return goodsLine()
    },
    goodStatus() {
      return goodsStatus()
    },
    // 状态
    auditStatus() {
      return status => {
        let statusStr = ''
        switch (status) {
          case 4:
            statusStr = 'pass'
            break
          case 5:
            statusStr = 'pending'
            break
          case 6:
            statusStr = 'reject'
            break
          case 7:
            statusStr = 'unjoin'
            break
          default:
            statusStr = ''
            break
        }
        return statusStr
      }
    },
    // 导出记录状态
    downLoadStatus() {
      return downLoadStatus()
    },
    // 导出样式状态
    exportStatus() {
      return status => {
        let statusStr = ''
        switch (status) {
          case 0:
            statusStr = 'pending'
            break
          case 1:
            statusStr = 'pass'
            break
          case -1:
            statusStr = 'reject'
            break
          default:
            statusStr = ''
            break
        }
        return statusStr
      }
    },
    tabListFilter() {
      if (this.erpSyncLevel > 0) {
        return this.tabList
      } else {
        return this.tabList.filter(item => item.value !== 7)
      }
    }
  },
  ...mapGetters([
    'currentEnterpriseInfo'
  ]),
  data() {
    const validateQty = (rule, value, callback) => {
      let form = this.form
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
      if (!reg.test(value)) {
        return callback(new Error('请输入正整数'))
      } else {
        if (value >= form.frozenQty) {
          callback()
        } else {
          callback(new Error('修改的库存必须大于或等于占有库存'))
        }
      }
    }
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: 'zt_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表'
        }
      ],
      query: {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        // 品状态：4已审核 5待审核 6驳回
        auditStatus: '0',
        // 使用产品线 0 全部 1 pop 2 b2b
        goodsLines: [],
        sellSpecifications: '',
        // 商品编码
        goodsId: '',
        // 内码或者编码
        inSnOrSn: '',
        // erp商品标识：1-未入库
        erpFlag: 1
      },
      total: 0,
      dataList: [],
      stockDetailList: [
        { erp: 1, frozenQty: 20 },
        { erp: 2, frozenQty: 20 },
        { erp: 3, frozenQty: 20 }
      ],
      stockQuery: {
        page: 1,
        limit: 10
      },
      stockTotal: 0,
      loading: false,
      form: {},
      showDialog: false,
      rules: {
        qtyNum: [{ required: true, validator: validateQty, trigger: 'blur' }]
      },
      showDetail: false,
      detailData: [],
      detailTotal: 0,
      // 	企业是否开通erp 0：未开通 1：开通
      erpSyncLevel: 0,
      tabList: [
        { label: '全部商品', value: 0 },
        { label: '已通过', value: 4 },
        { label: '审核中', value: 5 },
        { label: '未通过', value: 6 },
        { label: '未入库', value: 7 }
      ],
      tabActive: 0,
      chooseProduct: [],
      showProductsLine: false,
      lineForm: {
        productsLine: []
      },
      lineRules: {
        productsLine: [{ required: true, message: '请选择产品线', trigger: 'change' }]
      },
      // linesList: ["POP", "B2B"],
      auditId: '',
      showExport: false,
      exportType: 1,
      showExportLog: false,
      exportLoading: false,
      queryExportLog: {
        current: 1,
        size: 10,
        total: 0
      },
      exportLogList: []
    }
  },
  activated() {
    this.getList()
    // this.getCate()
    // this.chooseProduct = []
    this.getCurrentEnterpriseInfo()
  },
  created() {
    // this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    clickTab(e) {
      this.tabActive = e;
      this.query.auditStatus = e;
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    // 获取当前登录人企业信息(是否已对接ERP)
    async getCurrentEnterpriseInfo() {
      this.$common.showLoad()
      let data = await queryCurrentEnterpriseInfo()
      this.$common.hideLoad()
      if (data && data.enterpriseInfo) {
        this.erpSyncLevel = data.enterpriseInfo.erpSyncLevel
      }
    },
    async getList() {
      this.dataList = []
      let query = this.query
      let data
      if (query.auditStatus !== 7) {
        this.loading = true
        data = await getZtProductList(
          query.page,
          query.goodsLines,
          Number(query.auditStatus),
          query.licenseNo,
          query.manufacturer,
          query.name,
          query.limit,
          query.goodsId,
          query.inSnOrSn
        )
        this.loading = false
        console.log(data);
        if (data !== undefined) {
          this.dataList = data.records
          this.total = data.total
        }
      } else {
        this.loading = true
        data = await getZtUnStorageList(
          query.current,
          query.size,
          query.goodsId,
          query.name,
          query.licenseNo,
          query.manufacturer,
          query.inSnOrSn,
          query.goodsLines,
          query.erpFlag
        )
        this.loading = false
        if (data) {
          this.dataList = data.records
          this.total = data.total
        }
      }
    },
    // 切换供货渠道
    handleSelectGoodsline(value){
      this.getList()
    },
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.tabActive = 0
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        // 品状态：4已审核 5待审核 6驳回
        auditStatus: '0',
        // 使用产品线 0 全部 1 pop 2 b2b
        goodsLines: [],
        erpFlag: 1
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.$log(val)
      this.chooseProduct = val
    },
    // 批量上下架
    upDownChange(type) {
      // type  1 批量上架   2 批量下架
      if (this.chooseProduct.length === 0) {
        this.$common.warn('暂未选择任何商品')
        return
      }
      for (let i = 0; i < this.chooseProduct.length; i++) {
        if (this.chooseProduct[i].auditStatus != 4) {
          return this.$common.warn('已通过商品才能设置产品线')
        }
      }
      this.showProductsLine = true;
      this.lineForm.productsLine = []

    },
    // 批量设置产品线
    submitProductsLine() {

      console.log(this.chooseProduct);
      console.log(this.lineForm);
      let popItem = 0
      let mallItem = 0
      let arr = []
      if (this.lineForm.productsLine.includes(1)) {
        popItem = 1
      }
      if (this.lineForm.productsLine.includes(2)) {
        mallItem = 1
      }
      for (let i = 0; i < this.chooseProduct.length; i++) {
        let obj = {
          'goodsId': this.chooseProduct[i].id,
          'mallFlag': mallItem,
          'popFlag': popItem
        }
        arr.push(obj)
      }
      console.log(arr);

      this.$refs['lineForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await setProductsLines(arr)
          this.$common.hideLoad()
          console.log(data);
          if (data) {
            this.getList();
            this.showProductsLine = false;
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    async changeProductAll(ids, status) {
      this.$common.showLoad()
      let data = await updateStatusAll(ids, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('批量上架成功')
        } else {
          this.$common.n_success('批量下架成功')
        }
        this.getList()
      }
    },
    // 显示审核详情弹窗
    applyDetail(row) {
      this.auditId = row.id
      this.getDetailList()
    },
    // 获取审核详情
    async getDetailList() {
      this.$common.showLoad()
      let data = await getProductsAuditList(
        this.stockQuery.page,
        this.auditId,
        this.stockQuery.limit
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.showDialog = true
        this.stockDetailList = data.records
        this.stockTotal = data.total
      }
    },
    // 关闭审核详情弹窗
    confirm() {
      this.showDialog = false
    },

    // edit  detail
    editAndDetail(row, type) {
      // type : edit see
      this.$router.push({
        path: `/zt_products/products_edit_detail/${row.id}`,
        query: {
          type: type
        }
      })
    },
    // again
    again(row) {
      this.$router.push({
        path: `/zt_products/products_again/${row.id}`,
        query: {
          type: 'again'
        }
      })
    },
    // 跳转添加商品页面
    goAddProduct() {
      this.$router.push({
        path: '/zt_products/products_add'
      })
    },
    handleExport() {
      this.showExport = true
    },
    // 批量导出操作
    async confirmExport() {
      const dateTimeStr = formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss').replace(/[^0-9]/g, '')
      // 导出查询
      if (this.exportType === 1) {
        if (this.total === 0) {
          return this.$common.warn('查询数据为0条，请重新查询！')
        }
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'mallDataCenterGoodsExportService',
          fileName: `商品库商品导出${dateTimeStr}`,
          groupName: '企业数据管理',
          menuName: '商品列表',
          searchConditionList: [
            {
              desc: '商品编码',
              name: 'goodsId',
              value: query.goodsId || ''
            },
            {
              desc: '商品名称',
              name: 'name',
              value: query.name || ''
            },
            {
              desc: '批文许可',
              name: 'licenseNo',
              value: query.licenseNo
            },
            {
              desc: '生产厂家',
              name: 'manufacturer',
              value: query.manufacturer
            },
            {
              desc: '外部编码',
              name: 'inSnOrSn',
              value: query.inSnOrSn
            },
            {
              desc: '供货渠道',
              name: 'goodsLines',
              value: JSON.stringify(query.goodsLines)
            },
            {
              desc: '状态',
              name: 'auditStatus',
              value: query.auditStatus
            },
            {
              desc: '未入库',
              name: 'erpFlag',
              value: query.auditStatus === 7 ? query.erpFlag : ''
            }
          ]
        })
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('系统正在导出中，请稍后在导出记录中下载！')
          this.showExport = false
        }
      }
      // 导出勾选
      if (this.exportType === 2) {
        if (this.chooseProduct.length === 0) {
          return this.$common.warn('选中数据为0条，请先勾选！')
        }
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'mallDataCenterGoodsExportService',
          fileName: `商品库商品导出${dateTimeStr}`,
          groupName: '企业数据管理',
          menuName: '商品列表',
          searchConditionList: [
            {
              desc: '导出选中',
              name: 'selectIds',
              value: JSON.stringify(this.chooseProduct.map(item => item.id))
            },
            {
              desc: '商品编码',
              name: 'goodsId',
              value: query.goodsId || ''
            },
            {
              desc: '商品名称',
              name: 'name',
              value: query.name || ''
            },
            {
              desc: '批文许可',
              name: 'licenseNo',
              value: query.licenseNo
            },
            {
              desc: '生产厂家',
              name: 'manufacturer',
              value: query.manufacturer
            },
            {
              desc: '外部编码',
              name: 'inSnOrSn',
              value: query.inSnOrSn
            },
            {
              desc: '供货渠道',
              name: 'goodsLines',
              value: JSON.stringify(query.goodsLines)
            },
            {
              desc: '状态',
              name: 'auditStatus',
              value: query.auditStatus
            },
            {
              desc: '未入库',
              name: 'erpFlag',
              value: query.auditStatus === 7 ? query.erpFlag : ''
            }
          ]
        })
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('系统正在导出中，请稍后在导出记录中下载！')
          this.showExport = false
        }
      }
      // 导出所有
      if (this.exportType === 3) {
        let query = this.query
        this.$common.showLoad()
        let data = await createDownLoad({
          className: 'mallDataCenterGoodsExportService',
          fileName: `商品库商品导出${dateTimeStr}`,
          groupName: '企业数据管理',
          menuName: '商品列表',
          searchConditionList: [
            {
              desc: '状态',
              name: 'auditStatus',
              value: query.auditStatus
            },
            {
              desc: '未入库',
              name: 'erpFlag',
              value: query.auditStatus === 7 ? query.erpFlag : ''
            }
          ]
        })
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('系统正在导出中，请稍后在导出记录中下载！')
          this.showExport = false
        }
      }
    },
    // 导出记录按钮
    async handleExportLog() {
      this.getGoodsExportLog()
      this.$nextTick(() => {
        this.showExportLog = true
      })
    },
    // 获取导出记录列表
    async getGoodsExportLog() {
      this.exportLoading = true
      let data = await queryGoodsExportLog(
        this.queryExportLog.current,
        this.queryExportLog.size,
        // 商品导出的className
        'mallDataCenterGoodsExportService'
      )
      this.exportLoading = false
      if (data) {
        this.exportLogList = data.records.map(item => {
          return {
            ...item,
            fileName: item.fileName.split('_')[0]
          }
        })
        this.queryExportLog.total = data.total
      }
    },
    // 下载导出文件
    async handleDownExportFile(row) {
      if (row.id) {
        this.$common.showLoad()
        let data = await downLoadFile(row.id)
        this.$common.hideLoad()
        if (data && data.url) {
          const xRequest = new XMLHttpRequest()
          xRequest.open('GET', data.url, true)
          xRequest.responseType = 'blob'
          xRequest.onload = () => {
            const url = window.URL.createObjectURL(xRequest.response)
            const a = document.createElement('a')
            a.href = url
            a.download = row.fileName
            a.click()
          }
          xRequest.send()
        }
      }
    },
    // 重新导出
    async handleReExport(row) {
      this.$common.showLoad('', '.app')
      let data = await queryReExport(
        row.id,
        row.fileName.split('_')[0]
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('操作成功')
        this.getGoodsExportLog()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.comment-reject-box {
  width: 304px;
  display: flex;
  padding: 12px;
  border-radius: 4px !important;
  background: #FFFFFF !important;
  box-shadow: 0px 2px 12px 0px rgba(100,101,102,0.12);
  border: 1px solid #FFFFFF !important;
  font-size: 14px;
  font-weight: 400;
  color: #333333;
  line-height: 20px;
  .popper__arrow {
    border-top-color: #FFFFFF !important;
  }
  .reject-reason {
    word-break: break-all;
  }
  .name-box {
    display: flex;
    .svg-icon {
      flex-shrink: 0;
      width: 20px;
      height: 20px;
    }
    .user {
      font-size: 14px;
      line-height: 20px;
      margin-left: 8px;
    }
  }
}
</style>