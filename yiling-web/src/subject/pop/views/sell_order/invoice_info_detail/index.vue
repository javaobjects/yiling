<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div class="buy-order-half">
        <div class="common-box header-box">
          <div class="header-bar">
            <div class="sign"></div>发票详情
          </div>
          <div class="box-text">
            <el-row type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票申请编号：</span>{{ data.id }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票金额：</span>¥{{ data.invoiceAmount }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票申请人：</span>{{ data.createUserName }}</div>
              </el-col>
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票张数：</span>{{ data.invoiceQuantity }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="8" type="flex">
              <el-col :span="8">
                <div class="item"><span class="font-title-color">发票申请时间：</span>{{ data.createTime | formatDate }}</div>
              </el-col>
              <el-col :span="16">
                <div class="item"><span class="font-title-color">发票单号：</span>{{ data.invoiceNo }}</div>
              </el-col>
            </el-row>
            <el-row type="flex">
              <el-col :span="8">
                <div class="item">
                  <span class="font-title-color mar-r-10">设置发票的转换规则：</span>
                  <span>{{ data.transitionRuleName }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
          <!-- 是否使用票折 -->
          <div class="top-view">
            <div class="my-form-item">
              <div class="my-form-item-left">是否使用票折：</div>
              <div class="my-form-item-right">
                {{ ticketDiscountFlag == 1 ? '是' : '否' }}
              </div>
            </div>
            <div v-show="ticketDiscountFlag">
                <div class="top-view-item mar-b-8" v-for="item in orderTicketDiscountList" :key="item.ticketDiscountNo">
                <div class="scal-20-width">票折单号：<span>{{ item.ticketDiscountNo }}</span> </div>
                <div class="scal-20-width">使用票折金额：<span>￥{{ item.ticketDiscountAmount }}</span> </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="common-box buy-order-table">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>商品信息
        </div>
        <div>
          <div class="expand-row" v-if="data.orderInvoiceGroupList && data.orderInvoiceGroupList.length">
            <!-- 分组 -->
            <div class="expand-view" v-for="(groupItem, currentGroupIndex) in data.orderInvoiceGroupList" :key="currentGroupIndex">
              <!-- 出库单 -->
              <div v-for="(deliveryItem, deliveryIndex) in groupItem.orderInvoiceErpDeliveryNoList" :key="deliveryIndex">
                <div class="top-delivery-view border-1px-b mar-b-16">
                  <div class="delivery-view-left">
                    <div><span class="font-title-color">出库单:</span> <span v-if="deliveryItem.erpDeliveryNo">{{ deliveryItem.erpDeliveryNo }}</span></div>
                  </div>
                </div>
                <!-- 商品 -->
                <div class="delivery-goods-view" v-for="(item, index) in deliveryItem.applyInvoiceDetailInfo" :key="index">
                  <div class="title">{{ item.goodsName }}</div>
                  <div class="desc-box">
                    <el-image fit="contain" class="img" :src="item.goodsPic"></el-image>
                    <div class="right-text box1">
                      <div class="text-item"><span class="font-title-color">生产厂家：</span>{{ item.goodsManufacturer }}</div>
                      <div class="text-item"><span class="font-title-color">批准文号：</span>{{ item.goodsLicenseNo }}</div>
                      <div class="text-item"><span class="font-title-color">规格/型号：</span>{{ item.goodsSpecification }}</div>
                    </div>
                    <div class="right-text box2">
                      <div class="text-item"><span class="font-title-color">商品单价：</span>￥{{ item.goodsPrice }}</div>
                      <div class="text-item"><span class="font-title-color">开票数量：</span>{{ item.invoiceAllQuantity }}</div>
                      <div class="text-item"><span class="font-title-color">发货数量：</span>{{ item.deliveryQuantity }}</div>
                    </div>
                    <div class="right-text box3" v-show="ticketDiscountFlag">
                      <div class="text-item"><span class="font-title-color">金额小计：</span>￥{{ item.goodsAmount }}</div>
                      <div class="text-item"><span class="font-title-color">折扣金额：</span>￥{{ item.goodsDiscountAmount }}</div>
                      <div class="text-item"><span class="font-title-color">折扣比率：</span>{{ item.goodsDiscountRate }}%</div>
                    </div>
                  </div>
                  <!-- 批次 -->
                  <div>
                    <el-row class="batch-item" :gutter="8" type="flex" v-for="(batchItem, batchIndex) in item.batchList" :key="batchIndex">
                      <el-col :span="5">
                        <div>批次号/序列号：<span class="font-important-color">{{ batchItem.batchNo }}</span></div>
                      </el-col>
                      <el-col :span="5">
                        <div>生产日期：<span class="font-important-color">{{ batchItem.produceDate | formatDate }}</span></div>
                      </el-col>
                      <el-col :span="5">
                        <div>有效期至：<span class="font-important-color">{{ batchItem.expiryDate | formatDate }}</span></div>
                      </el-col>
                      <el-col :span="3">
                        <div>发货数量：<span class="font-important-color">{{ batchItem.deliveryQuantity }}</span></div>
                      </el-col>
                      <el-col :span="6">
                        <div>开票数量：<span class="font-important-color">{{ batchItem.invoiceQuantity }}</span></div>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>
              <!-- 开票摘要 -->
              <div class="invoice-form-item invoice-text mar-b-16 mar-t-16">
                <div class="invoice-form-item-left">开票摘要：</div>
                <div class="invoice-form-item-right">
                  <el-input
                    type="textarea"
                    placeholder="请输入开票摘要"
                    :maxlength="50"
                    show-word-limit
                    resize="none"
                    v-model="groupItem.invoiceSummary"
                    disabled
                    >
                  </el-input>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { 
  getInvoiceApplyDetail 
} from '@/subject/pop/api/order'

export default {
  components: {
  },
  computed: {
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
          title: '发票管理'
        },
        {
          title: '发票详情'
        }
      ],
      data: {},
      // 转换规则
      transitionRuleCode: '',
      // 邮箱
      invoiceEmail: '',
      //是否使用票折：0-否 1-是
      ticketDiscountFlag: 0, 
      // 开票行数提示
      orderTicketDiscountList: []
    };
  },
  mounted() {
    this.applyId = this.$route.params.applyId
    this.type = this.$route.params.type
    if (this.applyId) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      this.$common.showLoad()
      let data = await getInvoiceApplyDetail(this.applyId)
      this.$common.hideLoad()
      if (data) {
        this.orderTicketDiscountList = data.orderTicketDiscountList || []
        this.ticketDiscountFlag = data.ticketDiscountFlag
        this.orderInvoiceGroupList = data.orderInvoiceGroupList || []
        this.data = data
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>