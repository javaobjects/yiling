<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
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
                    <div class="item-title">订单号：<span class="item-value">{{ detailData.orderNo }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">配送方式：<span class="item-value">{{ detailData.deliverType | dictLabel(hmcDeliveryStatus) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付状态：<span class="item-value">{{ detailData.paymentStatus | dictLabel(hmcPaymentStatus) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单状态：<span class="item-value">{{ detailData.orderStatus | dictLabel(hmcOrderStatus) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付方式：<span class="item-value">{{ detailData.paymentMethod | dictLabel(hmcPaymentMethod) }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">第三方支付交易单号：<span class="item-value">{{ detailData.thirdPayNo }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">订单商品总金额：<span class="item-value">{{ detailData.goodsTotalAmount | toThousand('¥') }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">运费：<span class="item-value">{{ detailData.freightAmount }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">订单金额：<span class="item-value">{{ detailData.orderTotalAmount | toThousand('¥') }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">第三方支付实付金额：<span class="item-value">{{ detailData.thirdPayAmount | toThousand('¥') }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">下单时间：<span class="item-value">{{ detailData.createTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">支付时间：<span class="item-value">{{ detailData.payTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">发货时间：<span class="item-value">{{ detailData.deliverTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">收货时间：<span class="item-value">{{ detailData.receiveTime | formatDate }}</span></div>
                  </div>
                  <div class="item">
                    <div class="item-title">取消时间：<span class="item-value">{{ detailData.cancelTime | formatDate }}</span></div>
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
            <div v-if="detailData.marketOrderType == 2">
              <div class="mar-t-16" v-if="prescriptionType == 1">
                <yl-table stripe :show-header="true" :list="dataList">
                  <el-table-column label="商品名称" min-width="100" align="center">\
                    <template slot-scope="{ row }">
                      {{ row.goodsName }}
                    </template>
                  </el-table-column>
                  <el-table-column label="商品ID" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div>标准库SPU-ID:{{ row.hmcGoodsId }} </div>
                      <div>标准库SKU-ID:{{ row.hmcSellSpecificationsId }} </div>
                      <div>企业商品ID:{{ row.ihGoodsId }} </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="单副用量(g)" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      {{ row.num }}
                    </template>
                  </el-table-column>
                  <el-table-column label="单价" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <span>{{ row.goodsPrice | toThousand('¥') }}</span>
                    </template>
                  </el-table-column>
                </yl-table>
                <div class="dise">
                  共 {{ doseCount }} 剂
                </div>
              </div>
              <div class="item-info mar-t-16" v-else>
                <yl-table stripe :show-header="true" :list="dataList">
                  <el-table-column label="主图" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div>
                        <img class="image-box" :src="row.picture" alt="">
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品名称" min-width="100" align="center" prop="goodsName">
                  </el-table-column>
                  <el-table-column label="商品规格" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div>{{ row.specifications }} </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品ID" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <div>标准库SPU-ID:{{ row.hmcGoodsId }} </div>
                      <div>标准库SKU-ID:{{ row.hmcSellSpecificationsId }} </div>
                      <div>企业商品ID:{{ row.ihGoodsId }} </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="销售价格" min-width="100" align="center">
                    <template slot-scope="{ row }">
                      <span>{{ row.goodsPrice | toThousand('¥') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="数量" min-width="100" align="center" prop="num"></el-table-column>
                </yl-table>
              </div>
            </div>
            <div class="item-info mar-t-16" v-if="detailData.marketOrderType == 1">
              <yl-table stripe :show-header="true" :list="detailVOS">
                <el-table-column label="主图" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div>
                      <img class="image-box" :src="row.pic" alt="">
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div> {{ row.goodsName }} </div>
                  </template>
                </el-table-column>
                <el-table-column label="商品规格" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div>{{ row.goodsSpecification }} </div>
                  </template>
                </el-table-column>
                <el-table-column label="商品ID" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <div>标准库SPU-ID:{{ row.standardId }} </div>
                    <div>标准库SKU-ID:{{ row.sellSpecificationsId }} </div>
                    <div>企业商品ID:{{ row.goodsId }} </div>
                  </template>
                </el-table-column>
                <el-table-column label="销售价格" min-width="100" align="center">
                  <template slot-scope="{ row }">
                    <span>{{ row.goodsPrice | toThousand('¥') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="数量" min-width="100" align="center" prop="goodsQuantity"></el-table-column>
              </yl-table>
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
                    <div class="item-title">姓名：<span class="item-value">{{ detailData.name }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">手机号：<span class="item-value">{{ detailData.mobile }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">地址：<span class="item-value">{{ detailData.address || "- -" }}</span></div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              物流信息
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">快递公司：<span class="item-value">{{ detailData.deliverCompanyName | dictLabel(hmcMarketOrderDeliverCompany) }}</span></div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="item">
                    <div class="item-title">快递单号：<span class="item-value">{{ detailData.deliverNo || '- -' }}</span></div>
                  </div>
                </el-col>
              </el-row>
              <!-- 4 待收货  5待发货 -->
              <div v-if="ihEid != 1">
                <yl-button v-if="detailData.orderStatus == 4" plain type="primary" @click="clickDeliver()">发货</yl-button>
                <yl-button v-if="detailData.orderStatus == 5" plain type="primary" @click="editDeliver()">更改物流信息</yl-button>
              </div>
            </div>
          </div>
          <div class="insurance-detail-item">
            <div class="header-bar">
              <div class="sign"></div>
              订单备注
            </div>
            <div class="item-info mar-t-16">
              <el-row>
                <el-col :span="24">
                  <div class="item">
                    <div class="item-title">用户留言：<span class="item-value">{{ detailData.remark || '无' }}</span></div>
                  </div>
                </el-col>
                <el-col :span="24">
                  <div class="item mar-t-16">
                    <div class="item-title">平台运营备注：<span class="item-value">{{ detailData.platformRemark || '无' }}</span></div>
                  </div>
                </el-col>
                <el-col :span="24">
                  <div class="item mar-t-16">
                    <div class="item-title">商家备注：<span class="item-value">{{ detailData.merchantRemark || '无' }}</span></div>
                    <yl-button v-if="ihEid != 1" class="mar-t-16" plain type="primary" @click="addRemark()">{{ detailData.merchantRemark ? '修改备注':'添加备注' }}</yl-button>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      </div>
    </div>
    <!-- 添加 修改备注-->
    <yl-dialog 
      :title="remarkTitle" 
      width="800px" 
      :visible.sync="showRemarkDialog" 
      :show-footer="true" 
      @confirm="confirm" 
      class="dialog-updata"
    >
      <div class="dialog-content">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="" prop="remark" label-width="0">
            <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="ruleForm.remark" maxlength="200" show-word-limit>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 快递 -->
    <yl-dialog
      :title="deliverTitle" 
      width="800px" 
      :visible.sync="showdeliverDialog" 
      :show-footer="true" 
      @confirm="confirm1" 
      class="dialog-updata"
    >
      <div class="dialog-content">
        <el-form :model="deliverForm" :rules="deliverRules" ref="deliverForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="快递公司：" prop="name">
            <el-select v-model="deliverForm.name" placeholder="请选择">
              <el-option v-for="item in hmcMarketOrderDeliverCompany" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="快递单号：" prop="no">
            <el-input v-model="deliverForm.no" placeholder="请输入" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { marketGoodsListDetail, editGoodsOrderDelivery, addPlatformRemark } from '@/subject/pop/api/cmp_api/order'
import {
  hmcOrderStatus,
  hmcPaymentStatus,
  hmcDeliveryStatus
} from '@/subject/pop/utils/busi'
import { hmcMarketOrderDeliverCompany, hmcPaymentMethod } from '@/subject/pop/busi/cmp/order'
export default {
  name: 'CmpGoodsDetail',
  components: {},
  computed: {
    // 订单状态
    hmcOrderStatus() {
      return hmcOrderStatus()
    },
    //  支付状态
    hmcPaymentStatus() {
      return hmcPaymentStatus()
    },
    //  支付方式
    hmcPaymentMethod() {
      return hmcPaymentMethod()
    },
    //  配送方式
    hmcDeliveryStatus() {
      return hmcDeliveryStatus()
    },
    // 快递公司
    hmcMarketOrderDeliverCompany() {
      return hmcMarketOrderDeliverCompany()
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
          title: '订单管理'
        },
        {
          title: '商品订单',
          path: '/cmp_order/cmp_goods_list'
        },
        {
          title: '商品订单详情'
        }
      ],
      detailData: {},
      detailVOS: [],
      showRemarkDialog: false,
      remarkTitle: '添加备注',
      ruleForm: {
        remark: ''
      },
      rules: {
        remark: [
          { required: true, message: '请填写', trigger: 'blur' }
        ]
      },
      deliverTitle: '发货',
      showdeliverDialog: false,
      deliverForm: {
        name: '',
        no: ''
      },
      deliverRules: {
        name: [
          { required: true, message: '请选择快递公司', trigger: 'change' }
        ],
        no: [
          { required: true, message: '请填写快递单号', trigger: 'change' }
        ]
      },
      // 1中药 2西药
      prescriptionType: 0,
      dataList: [],
      doseCount: '',
      //为 1时 是以岭医院
      ihEid: 0
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
      let data = await marketGoodsListDetail(this.id)
      if (data) {
        this.detailData = data
       if (data.prescriptionGoodsInfoVO) {
          this.ihEid = data.prescriptionGoodsInfoVO.ihEid;
        }
        // //判断是不是处方药
        if (data.marketOrderType == 2 && data.prescriptionGoodsInfoVO) {
          this.doseCount = data.prescriptionGoodsInfoVO.doseCount;
          this.prescriptionType = data.prescriptionGoodsInfoVO.prescriptionType;
          this.dataList = data.prescriptionGoodsInfoVO.goodsList;
        } else {
          this.detailVOS = data.detailVOS ? data.detailVOS : []
        }
        
      }
    },
    // 添加修改备注
    confirm() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await addPlatformRemark(
            this.id,
            this.ruleForm.remark
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('添加成功')
            this.showRemarkDialog = false;
            this.getDetail();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    addRemark() {
      if (this.detailData.merchantRemark) {
        this.remarkTitle = '修改备注'
        this.ruleForm.remark = this.detailData.merchantRemark
      } else {
        this.remarkTitle = '添加备注'
      }
      this.showRemarkDialog = true
    },
    clickDeliver() {
      this.deliverTitle = '发货'
      this.showdeliverDialog = true
    },
    editDeliver() {
      this.deliverTitle = '更改物流信息'
      this.showdeliverDialog = true
      this.deliverForm.name = this.detailData.deliverCompanyName
      this.deliverForm.no = this.detailData.deliverNo
      this.$nextTick(() => {
        this.$refs['deliverForm'].resetFields();
      })
    },
    // 填写快递信息
    confirm1() {
      this.$refs['deliverForm'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await editGoodsOrderDelivery(
            this.id,
            this.deliverForm.name,
            this.deliverForm.no
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.success('成功')
            this.showdeliverDialog = false;
            this.getDetail();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
