<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">订单号</div>
              <el-input v-model="query.orderNo" placeholder="请输入订单号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">订单商品</div>
              <el-input v-model="query.goodsName" placeholder="请输入订单商品名称" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">订单状态</div>
              <el-select v-model="query.orderStatus" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="item in hmcOrderStatus" 
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
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
          <el-row class="box mar-t-16">
            <el-col :span="6">
              <div class="title">收货人姓名</div>
              <el-input v-model="query.name" placeholder="请输入收货人姓名" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">收货人手机号</div>
              <el-input v-model="query.mobile" placeholder="请输入收货人手机号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">支付状态</div>
              <el-select v-model="query.paymentStatus" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option
                  v-for="item in hmcPaymentStatusData"
                  :key="item.value" 
                  :label="item.label" 
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">处方编号</div>
              <el-input v-model="query.ihPrescriptionNo" placeholder="请输入处方编号" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
          <el-row class="box mar-t-16">
            <el-col :span="6">
              <div class="title">订单处方类型</div>
              <el-select v-model="query.prescriptionType" placeholder="请选择">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="西药" :value="1"></el-option>
                <el-option label="中药" :value="0"></el-option>
              </el-select>
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
      <!-- table  -->
      <div class="my-table mar-t-8">
        <yl-table
          stripe
          :show-header="true" 
          :list="dataList" 
          :total="total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          @getList="getList">
          <el-table-column label="平台订单号" align="center" min-width="200">
            <template slot-scope="{ row }">
              <div>
                <div>{{ row.orderNo }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单商品" align="center" min-width="400">
            <template slot-scope="{ row }">
              <div class="mar-l-18" v-if="row.marketOrderType == 2">
                <div v-for="(item, index) in row.prescriptionGoodsInfoVO.goodsList" :key="index" class="goods-box mar-b-16">
                  <div class="goods-img">
                    <img :src="item.picture" alt="">
                  </div>
                  <div class="goods-info mar-l-16">
                    <div class="goods-info-item">
                      <span class="item-name">{{ item.goodsName }}</span>
                      <span class="item-number" v-if="item.goodsPrice">￥{{ item.goodsPrice }}</span>
                    </div>
                    <div class="goods-info-item">
                      <span class="item-name">
                        <span v-show="row.prescriptionType == 2">{{ item.specifications }}</span>
                      </span>
                      <span class="item-number" v-if="item.num">
                        x{{ item.num }}
                        <span v-show="row.prescriptionType == 1">g</span>
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="mar-l-18" v-else>
                <div v-for="(item, index) in row.detailVOS" :key="index" class="goods-box mar-b-16">
                  <div class="goods-img">
                    <img :src="item.pic" alt="" />
                  </div>
                  <div class="goods-info mar-l-16">
                    <div class="goods-info-item">
                      <span class="item-name">{{ item.goodsName }}</span>
                      <span class="item-number">{{ item.goodsPrice | toThousand('￥') }}</span>
                    </div>
                    <div class="goods-info-item">
                      <span class="item-name">{{ item.goodsSpecification }}</span>
                      <span class="item-number">x{{ item.goodsQuantity }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="订单金额（元）" align="left" min-width="200">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base">
                  需付金额：<span>{{ row.orderTotalAmount | toThousand('￥') }}</span>
                </div>
                <div class="item-text font-size-base">
                  实收金额：<span>{{ row.realTotalAmount | toThousand('￥') }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="收货人信息" align="left" min-width="200">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base">
                  姓名：<span>{{ row.name }}</span>
                </div>
                <div class="item-text font-size-base">
                  手机号：<span>{{ row.mobile }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="邀请医生" align="center" min-width="120">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base">{{ row.doctorName }}<span v-if="row.hospitalName">({{ row.hospitalName }})</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="处方编号" align="center" min-width="120" prop="ihPrescriptionNo"></el-table-column>
          <el-table-column label="订单处方类型" align="center" min-width="90">
            <template slot-scope="{ row }">
               {{ row.prescriptionType == 1 ? '中药' : (row.prescriptionType == 2 ? '西药' : '') }}
            </template>
          </el-table-column>
          <el-table-column label="订单状态" align="center" min-width="120">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base">
                  <span>{{ row.orderStatus | dictLabel(hmcOrderStatus) }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="支付状态" align="center" min-width="120">
            <template slot-scope="{ row }">
              {{ row.paymentStatus | dictLabel(hmcPaymentStatusData) }}
            </template>
          </el-table-column>
          <el-table-column label="下单时间" align="center" min-width="180">
            <template slot-scope="{ row }">
              <div class="item">
                <div class="item-text font-size-base">
                  <span>{{ row.createTime | formatDate }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="90" fixed="right">
            <template slot-scope="{ row }">
              <yl-button v-role-btn="['2']" type="text" @click="showDetail(row)">去处理</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { marketGoodsListPage } from '@/subject/pop/api/cmp_api/order'
import { hmcOrderStatus } from '@/subject/pop/utils/busi'
import { createDownLoad } from '@/subject/pop/api/common'
import { hmcPaymentStatus } from '@/subject/pop/busi/cmp/order'
export default {
  name: 'CmpGoodsList',
  components: {
  },
  computed: {
    hmcOrderStatus() {
      return hmcOrderStatus()
    },
    //  支付状态
    hmcPaymentStatusData() {
      return hmcPaymentStatus()
    }
  },
  data() {
    return {
      navList: [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理',
          path: ''
        },
        {
          title: '商品订单',
          path: ''
        }
      ],
      query: {
        page: 1,
        limit: 10,
        createTime: [],
        goodsName: '',
        mobile: '',
        name: '',
        nickName: '',
        orderNo: '',
        orderStatus: 0,
        paymentStatus: '',
        ihPrescriptionNo: '',
        prescriptionType: ''
      },
      dataList: [],
      loading: false,
      total: 0
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await marketGoodsListPage(
        query.page,
        query.limit,
        query.createTime && query.createTime.length > 0 ? query.createTime[0] : '',
        query.createTime && query.createTime.length > 1 ? query.createTime[1] : '',
        query.goodsName,
        query.name,
        query.mobile,
        query.nickName,
        query.orderNo,
        query.orderStatus,
        query.paymentStatus,
        query.ihPrescriptionNo,
        query.prescriptionType
      )
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
      }
    },

    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        createTime: [],
        goodsName: '',
        mobile: '',
        name: '',
        nickName: '',
        orderNo: '',
        orderStatus: '',
        paymentStatus: '',
        ihPrescriptionNo: '',
        prescriptionType: ''
      }
    },
    // 下载模板
    async downLoadTemp() {
      let query = this.query
      if (this.dataList.length == 0) return this.$message.warning('请搜索数据再导出')
      this.$common.showLoad()
      let data = await createDownLoad({
        className: 'hmcB2BMarketOrderExportServiceImpl',
        fileName: '导出商城订单',
        groupName: '健康中心订单管理',
        menuName: '订单管理-商城订单',
        searchConditionList:
          [
            {
              desc: '订单编号',
              name: 'orderNo',
              value: query.orderNo || ''
            },
            {
              desc: '订单商品',
              name: 'goodsName',
              value: query.goodsName || ''
            },
            {
              desc: '用户昵称',
              name: 'nickName',
              value: query.nickName || ''
            },
            {
              desc: '收货人姓名',
              name: 'name',
              value: query.name || ''
            },
            {
              desc: '收货人手机号',
              name: 'mobile',
              value: query.mobile || ''
            },
            {
              desc: '下单时间-开始时间',
              name: 'beginTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[0] : ''
            },
            {
              desc: '下单时间-截止时间',
              name: 'endTime',
              value: query.createTime && query.createTime.length > 1 ? query.createTime[1] : ''
            },
            {
              desc: '订单状态',
              name: 'orderStatus',
              value: query.orderStatus || ''
            },
            {
              desc: '支付状态',
              name: 'paymentStatus',
              value: query.paymentStatus || ''
            },
            {
              desc: '处方编号',
              name: 'ihPrescriptionNo',
              value: query.ihPrescriptionNo || ''
            },
            {
              desc: '订单处方类型',
              name: 'prescriptionType',
              value: query.prescriptionType || ''
            }
          ]
      })
      this.$common.hideLoad()
      if (data && data.result) {
        this.$common.n_success('创建下载任务成功，请在下载中心查看')
      }
    },
    showDetail(e) {
      this.$router.push({
        name: 'CmpGoodsDetail',
        params: {
          id: e.id
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
