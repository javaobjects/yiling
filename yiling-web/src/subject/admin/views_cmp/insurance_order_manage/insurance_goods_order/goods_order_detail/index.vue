<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="select-bar">
        <div :class="['item', item.value === 2 && !orderReturn.id ? 'hidden' : '']" v-for="item in selectBarList" :key="item.value" @click="selectItem(item)">
          <div :class="['item-title', item.value === selected ? 'active' : '']">{{ item.label }}</div>
          <div class="item-line" v-show="item.value === selected"></div>
        </div>
      </div>
      <div class="common-box" v-if="selected === 1">
        <div class="insurance-detail-info">
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              基本信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">平台订单编号：<span class="item-value">{{ order.orderNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保险服务提供商：<span class="item-value">{{ detailData.insuranceCompanyName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">下单时间：<span class="item-value">{{ order.orderTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">药品服务终端：<span class="item-value">{{ order.ename }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">完成时间：<span class="item-value">{{ order.finishTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">下单用户：
                      <span class="item-value">{{ detailData.orderUserName || "- -" }}</span>
                      <span class="item-value" v-if="detailData.orderUserName">【ID：{{ order.orderUser }}】</span>
                    </div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单创建类型：<span class="item-value">{{ order.createSource | dictLabel(hmcCreateSource) }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">第三方兑保单号：<span class="item-value">{{ order.thirdConfirmNo || "- -" }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">对应保司保险单号：<span class="item-value">{{ order.policyNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单状态：<span class="item-value">{{ order.orderStatus | dictLabel(hmcOrderStatus) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付方式：<span class="item-value">{{ order.paymentMethod | dictLabel(hmcPaymentMethod) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单类型：<span class="item-value">{{ order.orderType | dictLabel(hmcOrderType) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付时间/确认时间：<span class="item-value">{{ order.paymentTime | formatDate }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">对应保险名称：<span class="item-value">{{ detailData.insuranceName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付状态：<span class="item-value">{{ order.paymentStatus | dictLabel(hmcPaymentStatus) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">是否为保险理赔兑付单：<span class="item-value">{{ order.isInsuranceOrder == 1 ? "是" : "否" }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单处理者：
                      <span class="item-value">{{ detailData.operateUserName || "- -" }}</span>
                      <span class="item-value" v-if="detailData.operateUserName">【ID：{{ detailData.operateUserId }}】</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              结算信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">订单额总额：<span class="item-value">{{ order.totalAmount | toThousand('￥') }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保司结算状态：<span class="item-value">{{ order.insuranceSettleStatus | dictLabel(hmcInsuranceSettleStatus) }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">以岭给药品终端结算额：<span class="item-value">{{ order.terminalSettleAmount | toThousand('￥') }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">药品终端结算状态：<span class="item-value">{{ order.terminalSettleStatus | dictLabel(hmcTerminalSettleStatus) }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">以岭计算理赔金额：<span class="item-value">{{ order.insuranceSettleAmount | toThousand('￥') }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">保司试算理赔金额：<span class="item-value">{{ order.insuranceSettleAmountTrial | toThousand('￥') }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              收货人信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">姓名：<span class="item-value">{{ orderRelateUser.userName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ orderRelateUser.userTel }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">地址：<span class="item-value">{{ orderRelateUser.provinceName + orderRelateUser.cityName + orderRelateUser.districtName + orderRelateUser.detailAddress || "- -" }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              配送方式
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">{{ order.deliverType | dictLabel(hmcDeliverType) }}：<span class="item-value">{{ order.deliverType == 1 ? "以岭医院" : "- -" }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              商品明细
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="orderDetailList">
                <el-table-column label="商品名称" min-width="100" align="center" prop="goodsName">
                  <template slot-scope="{ row }">
                    <div> {{ row.goodsName }} </div>
                    <div> 规格：{{ row.goodsSpecifications }} </div>
                  </template>
                </el-table-column>
                <el-table-column label="数量" min-width="100" align="center" prop="goodsQuantity"></el-table-column>
                <el-table-column label="保司给以岭的结算单价" min-width="100" align="center" prop="settlePrice">
                  <template slot-scope="{ row }">
                    <span>{{ row.settlePrice }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="以岭给终端的结算单价" min-width="100" align="center" prop="terminalSettlePrice">
                  <template slot-scope="{ row }">
                    <span>{{ row.terminalSettlePrice }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="是否管控" min-width="100" align="center" prop="controlStatus">
                  <template slot-scope="{ row }">
                    <span> {{ row.controlStatus === 1 ? "是" : "否" }} </span>
                  </template>
                </el-table-column>
                <el-table-column label="管控采购渠道" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div v-for="(item, index) in row.channelNameList" :key="index"> {{ item }} </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              处方信息
            </div>
            <div class="item-info mar-t-16">
              <div class="data-img" v-if="orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList">
                <div v-for="(item, index) in orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList" :key="index" @click="prescriptionClick(item, orderPrescriptionDetail.prescription.prescriptionSnapshotUrlList)">
                  <img :src="item" alt="">
                </div>
              </div>
              <p v-else>暂无相关处方</p>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              票据信息
            </div>
            <div class="item-info mar-t-16">
              <div class="data-img" v-if="detailData.orderReceiptsList && detailData.orderReceiptsList.length > 0">
                <div v-for="item in detailData.orderReceiptsList" :key="item" @click="prescriptionClick(item, detailData.orderReceiptsList)">
                  <img :src="item" alt="">
                </div>
              </div>
              <p class="pad-l-6" v-else>暂无相关数据</p>
            </div>
          </div>
        </div>
      </div>
      <div class="common-box" v-if="orderReturn.id && selected === 2">
        <div class="insurance-detail-info">
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              基本信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">平台退单编号：<span class="item-value">{{ orderReturn.returnNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">数据来源：<span class="item-value">{{ detailData.insuranceCompanyName }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">药品服务终端：<span class="item-value">{{ orderReturn.ename }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">申请退款金额：<span class="item-value">{{ orderReturn.returnAmount | toThousand("¥") }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">第三方编号：<span class="item-value">{{ orderReturn.thirdReturnNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">对应平台订单号：<span class="item-value">{{ order.orderNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">状态：<span class="item-value">{{ orderReturn.returnStatus | dictLabel(hmcReturnStatus) }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">创建时间：<span class="item-value">{{ orderReturn.createTime | formatDate }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              申请人信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">姓名：<span class="item-value">{{ detailData.holderName }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ detailData.holderPhone }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              退单商品详情
            </div>
            <div class="item-info mar-t-16">
              <yl-table
                stripe
                :show-header="true"
                :list="orderReturnDetailList">
                <el-table-column label="商品名称" min-width="100" align="center" prop="goodsName">
                  <template slot-scope="{ row }">
                    <div> {{ row.goodsName }} </div>
                    <div> 规格：{{ row.goodsSpecifications }} </div>
                  </template>
                </el-table-column>
                <el-table-column label="退药数量" min-width="100" align="center" prop="returnQuality"></el-table-column>
                <el-table-column label="以岭给终端的结算单价" min-width="100" align="center" prop="name"></el-table-column>
                <el-table-column label="退单价格" min-width="100" align="center" prop="returnPrice"></el-table-column>
              </yl-table>
              <div class="total">合计退：{{ orderReturn.returnAmount | toThousand("¥") }} 元</div>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { insuranceGoodsOrderDetail } from '@/subject/admin/api/cmp_api/insurance_order_manage'
import {
  hmcOrderStatus,
  hmcCreateSource,
  hmcPaymentStatus,
  hmcPaymentMethod,
  hmcOrderType,
  hmcInsuranceSettleStatus,
  hmcTerminalSettleStatus,
  hmcDeliverType,
  hmcReturnStatus
} from '@/subject/admin/busi/cmp/insurance_order_manage'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';

export default {
  name: 'GoodsOrderDetail',
  components: {
    ElImageViewer
  },
  computed: {
    // 订单状态
    hmcOrderStatus() {
      return hmcOrderStatus()
    },
    //  订单创建类型
    hmcCreateSource() {
      return hmcCreateSource()
    },
    //  支付状态
    hmcPaymentStatus() {
      return hmcPaymentStatus()
    },
    //  支付方式
    hmcPaymentMethod() {
      return hmcPaymentMethod()
    },
    //  订单类型
    hmcOrderType() {
      return hmcOrderType()
    },
    //  保司结算状态
    hmcInsuranceSettleStatus() {
      return hmcInsuranceSettleStatus()
    },
    //  药品终端结算状态
    hmcTerminalSettleStatus() {
      return hmcTerminalSettleStatus()
    },
    //  配送方式
    hmcDeliverType() {
      return hmcDeliverType()
    },
    //  退货状态
    hmcReturnStatus() {
      return hmcReturnStatus()
    }
  },
  filters: {
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.getDetail(this.id)
    }
  },
  data() {
    return {
      selected: 1,
      selectBarList: [
        { label: '订单信息', value: 1 },
        { label: '退货信息', value: 2 }
      ],
      detailData: {
        insuranceCompanyName: '',
        orderUserName: '',
        insuranceName: '',
        // 票据信息
        orderReceiptsList: []
      },
      order: {},
      orderRelateUser: {},
      orderDetailList: [],
      orderPrescriptionDetail: {
        id: '',
        goodsList: [],
        prescription: {
          patientsName: '',
          patientsGender: '',
          patientsAge: '',
          prescriptionSnapshotUrl: ''
        }
      },
      orderReturn: {},
      orderReturnDetailList: [],
      // 图片放大是否显示
      showViewer: false,
      imageList: []
    }
  },
  methods: {
    async getDetail(id) {
      let data = await insuranceGoodsOrderDetail(id)
      if (data) {
        this.detailData = data
        if (data.order) { this.order = data.order }
        if (data.orderRelateUser) { this.orderRelateUser = data.orderRelateUser }
        if (data.orderDetailList) { this.orderDetailList = data.orderDetailList }
        if (data.orderReturn) { this.orderReturn = data.orderReturn }
        if (data.orderReturnDetailList) { this.orderReturnDetailList = data.orderReturnDetailList }
        if (data.orderPrescriptionDetail && data.orderPrescriptionDetail.prescription) { this.orderPrescriptionDetail = data.orderPrescriptionDetail }
      }
    },
    selectItem(item) {
      this.selected = item.value
    },
    // 点击票据信息
    prescriptionClick(val, dataList) {
      if (val) {
        this.imageList = [val];
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val != dataList[i]) {
            this.imageList.push(
              dataList[i]
            )
          }
        }
      }
      this.showViewer = true;
    },
    // 关闭弹窗图片
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
