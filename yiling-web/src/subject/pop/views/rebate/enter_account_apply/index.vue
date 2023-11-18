<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 企业信息  -->
      <div class="top-box" v-show="currentSelectCustomer.easName">
        <div class="flex-row-left item">
          <div class="line-view"></div>
          <span class="font-size-lg bold">{{ currentSelectCustomer.easName }}</span>
        </div>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">{{ currentSelectCustomer.channelId | dictLabel(channelType) }}</div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">商务负责人：{{ currentSelectCustomer.contactorPhone }}</div></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">负责人联系方式：{{ currentSelectCustomer.contactor }}</div></el-col>
          <el-col :span="8"><div class="font-size-base font-title-color item-text">企业编码：{{ currentSelectCustomer.easCode }}</div></el-col>
        </el-row>
      </div>
      <div class="common-box">
        <div class="search-box">
          <div class="btn mar-b-16" v-show="!currentSelectCustomer.easName">
            <ylButton type="primary" @click="addMerchantClick">添加商家</ylButton>
          </div>
          <el-row class="box">
            <el-col :span="7">
              <div class="title">归属年度</div>
              <el-select v-model="form.year" placeholder="请选择归属年度"
                @change="yearChange"
              >
                <el-option
                  v-for="item in yearList"
                  :key="item"
                  :label="item"
                  :value="item"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">归属月度</div>
              <el-select v-model="form.month" placeholder="请选择归属月度"
                @change="monthChange"
              >
                <el-option
                  v-for="(item, index) in monthList"
                  :key="item.label + index"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="10">
              <div class="title">归属省份</div>
              <el-select v-loading="provinceLoading" v-model="query.provinceName" @visible-change="visibleChange"
                placeholder="请选择归属省份">
                <el-option
                  v-for="(item, index) in provinceList"
                  :key="item.name + index"
                  :label="item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">品种</div>
              <el-select v-loading="goodsLoading" v-model="query.goodsName" @visible-change="goodsVisibleChange"
                placeholder="请选择品种">
                <el-option
                  v-for="(item, index) in goodsList"
                  :key="item.name + index"
                  :label="item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">返利金额</div>
              <el-input :value="totalAmount" disabled placeholder="返利金额" />
            </el-col>
            <el-col :span="7">
              <div class="title">入账明细数量</div>
              <el-input :value="agreementDetailList.length" disabled placeholder="入账明细数量" />
            </el-col>
          </el-row>
        </div>
        <!-- <div class="search-box" v-show="currentSelectCustomer.isFirst"> -->
        <div class="search-box" v-show="currentSelectCustomer.isFirst">
          <el-row class="box">
            <el-col :span="7">
              <div class="title">兑付一级商</div>
              <el-select v-loading="firstLoading" v-model="query.entryEid" @visible-change="firstVisibleChange"
                @change="entryEidChange"
                placeholder="请选择兑付一级商">
                <el-option
                  v-for="(item, index) in firstList"
                  @click.native="firstListChange(index)"
                  :key="item.eid"
                  :label="item.name"
                  :value="item.eid">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">兑付一级商编码</div>
              <el-select v-model="query.entryCode" placeholder="请选择兑付一级商编码" @change="entryCodeChange">
                <el-option
                  v-for="item in easAccountList"
                  :key="item.easCode"
                  :label="item.easCode"
                  :value="item.easCode">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box pad-t-8">
          <el-row class="box">
            <ylButton type="primary" @click="calculateClick">计算返利</ylButton>
          </el-row>
        </div>
      </div>
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" plain @click="addOtherClick">添加其他返利</ylButton>
        </div>
      </div>
      <el-tabs
        class="my-tabs"
        v-model="activeTab"
        @tab-click="handleTabClick"
        >
        <el-tab-pane label="入账明细" name="1"></el-tab-pane>
        <el-tab-pane label="协议订单商品明细" name="2"></el-tab-pane>
      </el-tabs>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          v-show="activeTab == 1"
          border
          stripe
          show-header
          :list="agreementDetailList"
          :total="0"
          :cell-no-pad="false"
          >
          <el-table-column label="入账类型" min-width="60" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? '协议' : '其它返利' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="入账原因" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.detailType == 1 ? row.name : row.entryDescribe }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单数量" min-width="100" align="center" prop="orderCount"></el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center" prop="amount"></el-table-column>
          <el-table-column label="发货组织" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.sellerName }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
        <yl-table
          v-show="activeTab == 2"
          border
          stripe
          show-header
          :list="orderDetailList"
          :total="0"
          :cell-no-pad="false"
          >
          <el-table-column label="协议ID" min-width="60" align="center" prop="id">
          </el-table-column>
          <el-table-column label="协议名称" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议内容" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column label="订单号" min-width="100" align="center" prop="orderCode"></el-table-column>
          <el-table-column label="商品ID" min-width="100" align="center" prop="goodsId"></el-table-column>
          <el-table-column label="商品名称" min-width="100" align="center" prop="goodsName">
          </el-table-column>
          <el-table-column label="商品erp内码" min-width="100" align="center" prop="erpCode">
          </el-table-column>
          <el-table-column label="成交数量" min-width="100" align="center" prop="goodsQuantity">
          </el-table-column>
          <el-table-column label="采购金额" min-width="100" align="center" prop="price">
          </el-table-column>
          <el-table-column label="返利金额" min-width="100" align="center" prop="discountAmount" fixed="right">
          </el-table-column>
        </yl-table>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 新增供应商弹框 -->
    <yl-dialog title="请选择企业" :visible.sync="showProviderDialog" :show-footer="false">
      <div class="dialog-content-box">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="24">
                <div class="title">企业名称</div>
                <el-input
                  style="width:352px;"
                  v-model="providerQuery.customerName"
                  placeholder="请输入企业名称"
                  @keyup.enter.native="providerHandleSearch"
                />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="providerTotal"
                  @search="providerHandleSearch"
                  @reset="providerHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :list="providerList"
            :total="providerTotal"
            :page.sync="providerQuery.page"
            :limit.sync="providerQuery.limit"
            :loading2="loading2"
            :horizontal-border="false"
            :cell-no-pad="true"
            @getList="queryEasAccountPageListMethod"
          >
            <el-table-column>
              <template slot-scope="{ row, $index }">
                <div class="table-view" :key="$index">
                  <div class="table-item-left">
                    <div class="intro">{{ row.easName || '---' }}</div>
                    <el-row>
                      <el-col :span="12">
                        <span class="font-title-color">渠道商类型：</span>
                        {{ row.channelId | dictLabel(channelType) }}
                      </el-col>
                      <el-col :span="12">
                        <span class="font-title-color">企业编码：</span>
                        {{ row.easCode }}
                      </el-col>
                    </el-row>
                    <div class="intro">
                        <span class="font-title-color">地址：</span>
                        {{ row.address }}
                    </div>
                  </div>
                  <div class="table-item-right">
                    <yl-button
                      class="edit-btn"
                      v-if="!row.chooseFlag"
                      type="text"
                      @click="addProviderItemClick(row)"
                    >选择</yl-button>
                    <yl-button class="edit-btn" v-if="row.chooseFlag" disabled type="text">已选择</yl-button>
                  </div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 添加其他返利 -->
    <yl-dialog title="添加其他返利" :visible.sync="otherDialog"
      @confirm="otherDialogConfirm"
      >
      <div class="dialog-content-box">
        <!-- 企业信息  -->
        <div class="top-box add-other-top">
          <div class="flex-row-left item">
            <span class="font-size-lg bold">{{ currentSelectCustomer.easName }}</span>
          </div>
          <el-row>
            <el-col :span="12"><div class="font-size-base font-title-color item-text">{{ currentSelectCustomer.channelId | dictLabel(channelType) }}</div></el-col>
            <el-col :span="12"><div class="font-size-base font-title-color item-text">归属年度：{{ form.year }}</div></el-col>
          </el-row>
          <el-row>
            <el-col :span="12"><div class="font-size-base font-title-color item-text">企业编码：{{ currentSelectCustomer.easCode }}</div></el-col>
            <el-col :span="12"><div class="font-size-base font-title-color item-text">归属月度：{{ form.month | dictLabel(monthList) }}</div></el-col>
          </el-row>
        </div>
        <div class="common-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">返利金额</div>
                <el-input style="width:90%;" v-model="otherRebateForm.amount"
                 @keyup.native="otherRebateForm.amount=otherRebateForm.amount.replace(otherRebateForm.amount,RestrictedMoney(otherRebateForm.amount))" @change="materielExtraCostChange(otherRebateForm)"
                 placeholder="请输入返利金额" />
              </el-col>
              <el-col :span="12">
                <div class="title">返利原因</div>
                <el-input class="show-word-limit" style="width:90%;" :maxlength="20" show-word-limit v-model="otherRebateForm.entryDescribe" placeholder="请输入返利原因" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <div class="title">添加后金额</div>
                <el-input style="width:90%;" :value="otherRebateForm.addAmount" disabled placeholder="添加返利金额后金额" />
              </el-col>
              <el-col :span="12">
                <div class="title">发货组织</div>
                <el-select style="width:90%;" v-model="otherRebateForm.sellerEid" placeholder="请选择发货组织">
                  <el-option
                    v-for="item in agreementMainPartList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  queryEasAccountPageList,
  calculateRebateApply,
  queryGoods,
  saveRebateApply,
  queryCashingEntList,
  queryLocation
} from '@/subject/pop/api/rebate';
import {
  getAgreementMainPartList
} from '@/subject/pop/api/protocol';
import { plusOrMinus } from '@/common/utils'
import { channelType } from '@/subject/pop/utils/busi'

export default {
  components: {
  },
  computed: {
    channelType() {
      return channelType();
    }
  },
  data() {
    return {
      value: '',
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '企业返利'
        },
        {
          title: '入账申请'
        }
      ],
      form: {
        year: '',
        month: '',
        provinceId: ''

      },
      // 年份
      yearList: [],
      // 月度
      monthList: [
        {label: '1月', value: 1},
        {label: '2月', value: 2},
        {label: '3月', value: 3},
        {label: '4月', value: 4},
        {label: '5月', value: 5},
        {label: '6月', value: 6},
        {label: '7月', value: 7},
        {label: '8月', value: 8},
        {label: '9月', value: 9},
        {label: '10月', value: 10},
        {label: '11月', value: 11},
        {label: '12月', value: 12},
        {label: '一季度', value: 13},
        {label: '二季度', value: 14},
        {label: '三季度', value: 15},
        {label: '四季度', value: 16},
        {label: '上半年', value: 17},
        {label: '下半年', value: 18},
        {label: '全年', value: 19}
      ],
      // 一级兑付商
      firstLoading: false,
      firstList: [],
      easAccountList: [],
      // 品种
      goodsLoading: false,
      goodsList: [],
      // 省份
      provinceLoading: false,
      provinceList: [],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        provinceName: '',
        goodsName: ''
      },
      data: {},
      // 返利金额
      totalAmount: 0,
      // 入账明细
      agreementDetailList: [],
      // 协议订单商品明细
      orderDetailList: [],
      // 发组织
      agreementMainPartList: [],
      activeTab: '1',
      // 供应商弹框
      showProviderDialog: false,
      loading2: false,
      providerQuery: {
        page: 1,
        limit: 10,
        customerName: ''
      },
      providerList: [],
      providerTotal: 0,
      // 当前选中的企业
      currentSelectCustomer: {},
      // 添加其他返利
      otherDialog: false,
      // 添加其他返利数组
      applyDetails: [],
      otherRebateForm: {
      },
      //是否点击计算按钮：0-否 1-是
      calculateStatus: 0
    };
  },
  mounted() {
    // 获取年份
    let yearList = this.$common.getBeforeYearsList(2)
    this.yearList = yearList
  },
  methods: {
    //tab切换
    handleTabClick(tab, event) {
      this.$common.log(tab)
    },
    // 添加商家
    addMerchantClick() {
      this.showProviderDialog = true
      // 获取添加企业列表
      this.queryEasAccountPageListMethod()
    },
    // 获取添加企业列表
    async queryEasAccountPageListMethod() {
      this.loading2 = true
      let providerQuery = this.providerQuery
      let data = await queryEasAccountPageList(
        providerQuery.page,
        providerQuery.limit,
        providerQuery.customerName
      );
      this.loading2 = false
      if (data) {
        this.providerList = data.records
        this.providerTotal = data.total
      }
    },
    // 搜索点击
    providerHandleSearch() {
      this.providerQuery.page = 1
      this.queryEasAccountPageListMethod()
    },
    providerHandleReset() {
      this.providerQuery = {
        page: 1,
        limit: 10,
        customerName: ''
      }
      this.providerTotal = 0
    },
    // 企业列表 点击选择
    addProviderItemClick(item) {
      this.currentSelectCustomer = item
      this.showProviderDialog = false
    },
    // 年度选择改变
    yearChange() {
      this.data = {}
      this.orderDetailList = []
      this.agreementDetailList = []
      this.calculateStatus = 0
      this.totalAmount = 0
    },
    // 月度切换
    monthChange() {
      this.data = {}
      this.orderDetailList = []
      this.agreementDetailList = []
      this.calculateStatus = 0
      this.totalAmount = 0
    },
    // 兑付一级商
    entryEidChange() {
      this.data = {}
      this.orderDetailList = []
      this.agreementDetailList = []
      this.calculateStatus = 0
      this.totalAmount = 0
      this.query.entryCode = ''
    },
    // 兑付一级商编码
    entryCodeChange() {
      this.data = {}
      this.orderDetailList = []
      this.agreementDetailList = []
      this.calculateStatus = 0
      this.totalAmount = 0
    },
    checkCalculate() {
      if (!this.currentSelectCustomer.easName) {
        this.$common.warn('请添加商家')
        return false
      }
      if (!this.form.year) {
        this.$common.warn('请选择归属年度')
        return false
      }
      if (!this.form.month) {
        this.$common.warn('请选择归属月度')
        return false
      }
      return true
    },
    // 计算返利按钮点击
    async calculateClick() {
      let checkResult = this.checkCalculate()
      if (!checkResult) return
      this.$common.showLoad();
      let currentSelectCustomer = this.currentSelectCustomer
      let form = this.form
      let query = this.query
      let data = await calculateRebateApply(
        currentSelectCustomer.customerEid,
        currentSelectCustomer.easCode,
        form.year,
        form.month,
        query.entryEid,
        query.entryCode
      );
      this.$common.hideLoad();
      if (data) {
        this.data = data
        this.totalAmount = data.totalAmount
        if (data.orderDetailList) {
          this.orderDetailList = data.orderDetailList
        }
        if (data.agreementDetailList) {
          this.agreementDetailList = data.agreementDetailList
        }
        this.calculateStatus = 1
      }

    },
    // 添加其他返利
    addOtherClick() {
      let checkResult = this.checkCalculate()
      if (!checkResult) return
      this.otherRebateForm = {}
      this.otherDialog = true
      this.getAgreementMainPartListMethod()
    },
    // 获取协议主体
    async getAgreementMainPartListMethod() {
      let data = await getAgreementMainPartList(
      );
      this.agreementMainPartList = data.list
    },
    // 添加其他返利确认点击
    otherDialogConfirm() {
      let checkResult = this.checkOtherDialogConfirm()
      if (!checkResult) return
      let otherRebateForm = this.otherRebateForm
      let sellerName = ''
      this.agreementMainPartList.forEach(item => {
        if (item.id == otherRebateForm.sellerEid) {
          sellerName = item.name
        }
      });
      let item = {
        amount: otherRebateForm.amount,
        entryDescribe: otherRebateForm.entryDescribe,
        sellerEid: otherRebateForm.sellerEid,
        sellerName: sellerName
      }
      this.applyDetails.push(item)

      this.agreementDetailList.push(item)
      this.totalAmount = otherRebateForm.addAmount
      this.otherDialog = false
    },
    // 保存
    async saveClick() {
      let checkResult = this.checkCalculate()
      if (!checkResult) return

      let query = this.query
      if (query.provinceName == '') {
        this.$common.warn('请选择省份')
        return
      }
      if (query.goodsName == '') {
        this.$common.warn('请选择品种')
        return
      }
      if (!this.agreementDetailList || this.agreementDetailList.length == 0) {
        this.$common.warn('请先计算返利或添加其他返利')
        return
      }
      this.$common.showLoad();
      let currentSelectCustomer = this.currentSelectCustomer
      let form = this.form
      
      let addParams = {
        year: form.year,
        month: form.month,
        provinceName: query.provinceName,
        goodsName: query.goodsName,
        entryEid: query.entryEid,
        entryCode: query.entryCode,
        easCode: currentSelectCustomer.easCode,
        eid: currentSelectCustomer.customerEid,
        calculateStatus: this.calculateStatus,
        totalAmount: this.totalAmount,
        applyDetails: this.applyDetails
      }
      let data = await saveRebateApply(addParams);
      this.$common.hideLoad();
      if (data && data.result) {
        this.$common.n_success('保存成功');
        this.$router.go(-1)
      }
    },
    // 添加其他返利 金额改变 暂时不用
    inputChange(value) {// /(^[\-0-9][0-9]*(.[0-9]+)?)$/
      value = value.replace(/[^0-9.-]/g, '') // 保留数字和小数点
      this.otherRebateForm.addAmount = this.$common.add(Number(value), Number(this.totalAmount))
    },
    onInput(value) {
      return plusOrMinus(value)
    },
    // 额外费用校验输入正负数， 保留2位小数 调用公共方法
    RestrictedMoney(values) {
      return plusOrMinus(values.toString())
    },
    // 结合change事件对失去焦点进行判断，防止输入一些无效值
    materielExtraCostChange(item) {
      // 防止删除为空
      if (!item.amount) {
        item.amount = '0.00'
      }
      // 一些错误金额输入的判断
      if (item.amount.toString().indexOf('.') > 0 && Number(item.amount.toString().split('.')[1].length) < 1) {
        item.amount = item.amount.toString().split('.')[0]
      }
      // 一些错误金额输入的判断
      if (!item.amount || item.amount === '-' || item.amount === '-0') {
        item.amount = '0.00'
        return
      }
      item.amount = parseFloat(item.amount).toFixed(2)
      // 此时item.amount的值即是最终有效值
      // .......在这里可以继续你的代码..........
      this.$common.log(item.amount)
      this.otherRebateForm.addAmount = this.$common.add(Number(item.amount), Number(this.totalAmount))
    },
    // 校验添加其他返利点击
    checkOtherDialogConfirm() {
      let otherRebateForm = this.otherRebateForm
      if (!otherRebateForm.amount) {
        this.$common.warn('请输入返利金额')
        return false
      }
      if (!otherRebateForm.entryDescribe) {
        this.$common.warn('请输入返利原因')
        return false
      }
      if (!otherRebateForm.sellerEid) {
        this.$common.warn('请选择发货组织')
        return false
      }
      if (otherRebateForm.addAmount < 0) {
        this.$common.warn('添加后金额不能为负')
        return false
      }
      return true
    },
    // 品种
    goodsVisibleChange(e) {
      if (e) {
        if (this.goodsList.length === 0) {
          this.getGoodsList()
        }
      }
    },
    async getGoodsList() {
      this.goodsLoading = true
      let data = await queryGoods()
      this.goodsLoading = false
      this.goodsList = data.list
    },
    // 选择兑付一级商
    firstListChange(index) {
      this.$common.log(index)
      if (this.firstList.length > 0) {
        this.easAccountList = this.firstList[index].easAccountList
      }
    },
    // 一级兑付商
    firstVisibleChange(e) {
      if (e) {
        if (!this.currentSelectCustomer.easName) {
          this.$common.warn('请添加商家')
          return false
        }
        if (this.firstList.length === 0) {
          this.queryCashingEntListMethod()
        }
      }
    },
    async queryCashingEntListMethod() {
      this.firstLoading = true
      let data = await queryCashingEntList(this.currentSelectCustomer.customerEid)
      this.firstLoading = false
      this.firstList = data.list
    },
    // 省份
    visibleChange(e) {
      if (e) {
        if (this.provinceList.length === 0) {
          this.getAddress('')
        }
      }
    },
    // 获取区域选择器
    async getAddress() {
      this.provinceLoading = true
      let data = await queryLocation()
      this.provinceLoading = false
      this.provinceList = data.list
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-cell {
      border-bottom: 1px solid #DDDDDD;
    }
  }
  .show-word-limit{
    .el-input__inner{
      padding-right: 46px;
    }
  }
</style>
