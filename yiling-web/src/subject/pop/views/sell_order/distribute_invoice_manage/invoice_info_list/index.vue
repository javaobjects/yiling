<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="buy-order-half">
        <div class="common-box header-box">
          <div class="header-bar">
            <div class="sign"></div>
            订单信息
          </div>
          <div class="box-text">
            <el-row type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">订单编号：</span>{{ data.orderNo }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color">订单状态：</span>{{ data.orderStatus | dictLabel(orderStatus) }}
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color">货款总金额：</span>¥{{ data.totalAmount || '- -' }}
                </div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color">发票状态：</span
                  >{{ data.invoiceStatus | dictLabel(orderTicketStatus) }}
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">现折总金额：</span>¥{{ data.cashDiscountAmount }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color">支付方式：</span>{{ data.paymentMethod | dictLabel(payType) }}
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">销售组织：</span>{{ data.sellerEname }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">采购商：</span>{{ data.buyerEname }}</div>
              </el-col>
            </el-row>
          </div>
          <div class="top-view">
            <div class="my-form-item">
              <div class="item-view">
                发票金额：<span>{{ data.invoiceAmount | toThousand('￥') }}</span>
              </div>
              <div class="item-view">
                订单所使用的票折总金额：<span>{{ data.ticketDiscountAmount | toThousand('￥') }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table border :list="dataList" :total="0" show-header>
          <el-table-column label="申请时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="发票申请编号" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="showDetail(row)">{{ row.id }}</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="发票转换规则" min-width="250" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.transitionRuleName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="是否使用票折" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.ticketDiscountFlag == 1 ? '是' : '否' }}</div>
            </template>
          </el-table-column>
          <el-table-column label="票折金额" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.ticketDiscountAmount | toThousand('￥') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="发票金额" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.invoiceAmount | toThousand('￥') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="发票号码" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.invoiceNo }}</div>
            </template>
          </el-table-column>
          <el-table-column label="发票申请人" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>{{ row.createUserName }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getInvoiceApplyList } from '@/subject/pop/api/order'
import { paymentMethod, orderStatus, orderPayStatus, orderTicketStatus } from '@/subject/pop/utils/busi'

export default {
  name: 'DistributeInvoiceInfoList',
  components: {},
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 订单状态
    orderStatus() {
      return orderStatus()
    },
    // 支付状态
    orderPayStatus() {
      return orderPayStatus()
    },
    // 发票
    orderTicketStatus() {
      return orderTicketStatus()
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
          title: '销售订单管理'
        },
        {
          title: '分销发票管理'
        },
        {
          title: '分销发票列表'
        }
      ],
      // 订单ID
      id: '',
      data: {},
      dataList: []
    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getInvoiceApplyList(this.id)
      this.$common.hideLoad()
      if (data) {
        this.data = data
        this.dataList = data.orderInvoiceApplyOneList || []
      }
    },
    // 跳转 发票详情界面
    showDetail(row) {
      this.$router.push({
        name: 'DistributeInvoiceInfoDetail',
        params: { applyId: row.id }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
