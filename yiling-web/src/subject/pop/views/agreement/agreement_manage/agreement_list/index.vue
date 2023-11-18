<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top:0;">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">生效日期</div>
              <el-date-picker
                v-model="query.effectTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="7">
              <div class="title">协议编号</div>
              <el-input v-model="query.agreementNo" placeholder="请输入协议编号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">协议负责人</div>
              <el-input v-model="query.mainUserName" placeholder="请输入协议负责人" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">创建日期</div>
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
            <el-col :span="7">
              <div class="title">协议类型</div>
              <el-select v-model="query.type" placeholder="请选择协议类型">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">购进渠道</div>
              <el-select v-model="query.buyChannel" placeholder="请选择购进渠道">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementSupplySalesBuyChannel"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">乙方</div>
              <el-input v-model="query.secondName" placeholder="请输入乙方" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">商品编码</div>
              <el-input v-model="query.goodsId" placeholder="请输入商品编码" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">甲方</div>
              <el-input v-model="query.ename" placeholder="请输入甲方" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="10">
              <div class="title">返利标准</div>
              <el-select v-model="query.rebateStandard" placeholder="请选择返利标准">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementRebateTaskStandard"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">是否活动协议</div>
              <el-select v-model="query.activeFlag" placeholder="请选择是否活动协议">
                <el-option label="否" :value="0"></el-option>
                <el-option label="是" :value="1"></el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <div class="title">审核通过时间</div>
              <el-date-picker
                v-model="query.authPassTime"
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
            <el-col :span="10">
              <div class="title">商家运营</div>
              <el-input v-model="query.businessOperatorName" placeholder="请输入商业运营签订人名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="7">
              <div class="title">协议生效状态</div>
              <el-select v-model="query.effectStatus" placeholder="请选择协议生效状态">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in agreementEffectStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
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
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view" style="padding-bottom: 10px;background: #FFFFFF;border-radius: 4px;">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>协议列表
        </div>
        <yl-table
          border
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="协议编号" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showDetail(row)">{{ row.agreementNo }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议甲方" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="enameClick(row)">{{ row.ename }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议乙方" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.secondName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="生效日期" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.startTime | formatDate('yyyy-MM-dd') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结束日期" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate('yyyy-MM-dd') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="审核通过时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.authTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="甲方类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.firstType | dictLabel(agreementFirstType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="甲方联系人" min-width="100" align="center" prop="firstSignUserName">
          </el-table-column>
          <el-table-column label="甲方联系电话" min-width="100" align="center" prop="firstSignUserPhone">
          </el-table-column>
          <el-table-column label="乙方类型" min-width="100" align="center" prop="secondType">
          </el-table-column>
          <el-table-column label="乙方联系人" min-width="200" align="center" prop="secondSignUserName">
          </el-table-column>
          <el-table-column label="乙方联系电话" min-width="200" align="center" prop="secondSignUserPhone">
          </el-table-column>
          <el-table-column label="协议类型" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.agreementType | dictLabel(agreementType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="付款方式" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.payMethodList.join(';') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="结算方式" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.settlementMethodList.join(';') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利兑付方式" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCashType | dictLabel(agreementRebateCashType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利兑付时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCashTime | dictLabel(agreementRebateCashTime) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="购进渠道" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.buyChannel | dictLabel(agreementSupplySalesBuyChannel) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="是否活动协议" min-width="200" align="center" prop="activeFlag">
            <template slot-scope="{ row }">
              <div>
                {{ row.activeFlag == 1 ? '是' : '否' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="协议品种数" min-width="200" align="center" prop="goodsNumber">
          </el-table-column>
          <el-table-column label="协议负责人" min-width="200" align="center" prop="mainUserName">
          </el-table-column>
          <el-table-column label="协议生效状态" min-width="200" align="center" prop="effectStatus">
            <template slot-scope="{ row }">
              <div>
                {{ row.effectStatus | dictLabel(agreementEffectStatus) }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
        <div class="header-bar mar-t-16 mar-b-10">
          <div class="sign"></div>协议商品列表
        </div>
        <yl-table
          border
          show-header
          :list="goodsList"
          :total="goodsQuery.total"
          :page.sync="goodsQuery.page"
          :limit.sync="goodsQuery.limit"
          :loading="loading1"
          @getList="queryAgreementGoodsPageMethods"
        >
          <el-table-column label="商品ID" min-width="200" align="center" prop="goodsId">
          </el-table-column>
          <el-table-column label="商品名称" min-width="320" align="center" prop="goodsName">
          </el-table-column>
          <el-table-column label="规格" min-width="200" align="center" prop="specifications">
          </el-table-column>
          <el-table-column label="生产厂家" min-width="200" align="center" prop="manufacturerName">
          </el-table-column>
          <el-table-column label="品牌厂家" min-width="200" align="center" prop="brandManufacturerName">
          </el-table-column>
          <el-table-column label="返利计算单价" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCalculatePrice | dictLabel(agreementRebateTaskStandard) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利标准" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateStandard | dictLabel(agreementRebateTaskStandard) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="阶梯条件计算方式" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateStageMethod | dictLabel(agreementRebateStageMethod) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利计算规则" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCalculateRule | dictLabel(agreementRebateCalculateRule) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利兑付方式" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCashType | dictLabel(agreementRebateCashType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="返利兑付时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.rebateCashTime | dictLabel(agreementRebateCashType) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="购进渠道" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.buyChannel | dictLabel(agreementSupplySalesBuyChannel) }}
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import {
  queryAgreementPage,
  queryAgreementGoodsPage
} from '@/subject/pop/api/agreement';
import { agreementType, agreementSupplySalesBuyChannel, agreementRebateTaskStandard, agreementEffectStatus, agreementRebateCashType, agreementRebateCashTime, agreementFirstType, agreementRebateCalculateRule, agreementRebateStageMethod } from '@/subject/pop/utils/busi'

export default {
  name: 'AgreementList',
  components: {
  },
  computed: {
    // 协议类型
    agreementType() {
      return agreementType()
    },
    // 购进渠道
    agreementSupplySalesBuyChannel() {
      return agreementSupplySalesBuyChannel()
    },
    // 返利标准
    agreementRebateTaskStandard() {
      return agreementRebateTaskStandard()
    },
    // 协议生效状态
    agreementEffectStatus() {
      return agreementEffectStatus()
    },
    // 返利兑付方式
    agreementRebateCashType() {
      return agreementRebateCashType()
    },
    // 返利兑付时间
    agreementRebateCashTime() {
      return agreementRebateCashTime()
    },
    // 甲方类型
    agreementFirstType() {
      return agreementFirstType()
    },
    // 返利计算规则
    agreementRebateCalculateRule() {
      return agreementRebateCalculateRule()
    },
    // 返利阶梯条件计算方法
    agreementRebateStageMethod() {
      return agreementRebateStageMethod()
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
          title: '协议管理'
        },
        {
          title: '协议列表'
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        effectTime: [],
        agreementNo: '',
        mainUserName: '',
        createTime: [],
        type: 0,
        buyChannel: 0,
        secondName: '',
        goodsId: '',
        ename: '',
        rebateStandard: 0,
        activeFlag: '',
        authPassTime: [],
        businessOperatorName: '',
        effectStatus: 0
      },
      currentRow: {},
      loading1: false,
      dataList: [],
      goodsQuery: {
        page: 1,
        limit: 10,
        total: 0
      },
      goodsList: []
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryAgreementPage(
        query.page,
        query.limit,
        query.effectTime && query.effectTime.length ? query.effectTime[0] : undefined,
        query.effectTime && query.effectTime.length > 1 ? query.effectTime[1] : undefined,
        query.agreementNo,
        query.mainUserName,
        query.createTime && query.createTime.length ? query.createTime[0] : undefined,
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : undefined,
        query.type,
        query.buyChannel,
        query.secondName,
        query.goodsId,
        query.ename,
        query.rebateStandard,
        query.activeFlag,
        query.authPassTime && query.authPassTime.length ? query.authPassTime[0] : undefined,
        query.authPassTime && query.authPassTime.length > 1 ? query.authPassTime[1] : undefined,
        query.businessOperatorName,
        query.effectStatus
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        total: 0,
        effectTime: [],
        agreementNo: '',
        mainUserName: '',
        createTime: [],
        type: 0,
        buyChannel: 0,
        secondName: '',
        goodsId: '',
        ename: '',
        rebateStandard: 0,
        activeFlag: '',
        authPassTime: [],
        businessOperatorName: '',
        effectStatus: 0
      }
    },
    // 查看
    showDetail(row) {
      // 跳转详情 operateType 1-详情 2-审核详情
      this.$router.push({
        name: 'AgreementDetail',
        params: { 
          id: row.id,
          operateType: 1
        }
      });
    },
    // 协议甲方点击
    enameClick(row) {
      this.currentRow = row
      this.queryAgreementGoodsPageMethods()
    },
    // 点击协议 查看商品
    async queryAgreementGoodsPageMethods() {
      this.loading1 = true
      let goodsQuery = this.goodsQuery
      let data = await queryAgreementGoodsPage(
        goodsQuery.page,
        goodsQuery.limit,
        this.currentRow.id
      );
      this.loading1 = false
      if (data) {
        this.goodsList = data.records
        this.goodsQuery.total = data.total
      }
    },
    getCellClass(row) {
      if (row.columnIndex == 4) {
        return 'border-1px-l'
      } 
      return ''
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
