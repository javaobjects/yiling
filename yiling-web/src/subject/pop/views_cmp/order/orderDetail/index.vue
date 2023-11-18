<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="order-tab">
        <div class="order-tab-item" :class="[ activeIndex == 1 ?'order-active':'' ]" @click="changeTab(1)">订单基本信息</div>
        <div class="order-tab-item" :class="[ activeIndex == 2 ?'order-active':'' ]" @click="changeTab(2)">发货信息</div>
      </div>
      <!-- 订单基本信息 -->
      <div v-if="activeIndex === 1 ">
        <div class="common-box mar-t-8">
          <div class="header-bar mar-b-12">
            <div class="sign"></div>商品信息
          </div>
          <div class="pad-l-6 mar-b-32">
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">平台订单编号：<span class="font-important-color">{{ data.order.orderNo }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">第三方兑保编号：<span class="font-important-color">{{ data.order.thirdConfirmNo }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">对应保险名称：<span class="font-important-color">{{ data.insuranceName }}</span></div>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">保险服务供应商：<span class="font-important-color">{{ data.insuranceCompanyName }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">对应平台保险单号：<span class="font-important-color">{{ data.insuranceRecord.policyNo }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 支付状态:1-未支付/2-已支付/3-已退款/4-部分退款	 -->
                <div class="font-size-base font-title-color">支付状态：<span class="font-important-color">{{ data.order.paymentStatus | dictLabel(hmcPaymentStatus) }}</span></div>
              </el-col>
            </el-row>
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">下单时间：<span class="font-important-color">{{ data.order.createTime | formatDate }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退	 -->
                <div class="font-size-base font-title-color">订单状态：<span class="font-important-color">{{ data.order.orderStatus | dictLabel(hmcOrderStatus) }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">是否为保险理赔兑付单：<span class="font-important-color">{{ data.order.isInsuranceOrder ==1 ? "是" : "否" }}</span></div>
              </el-col>

            </el-row>
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">药品服务终端：<span class="font-important-color">{{ data.order.ename }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 支付方式:1-保险理赔结算	 -->
                <div class="font-size-base font-title-color">支付方式：<span class="font-important-color">{{ data.order.paymentMethod ==1 ? "保险理赔结算":"--" }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 订单类型:1-其他/2-虚拟商品订单/3-普通商品/4-药品订单	 -->
                <div class="font-size-base font-title-color">订单类型：<span class="font-important-color">{{ data.order.orderType | dictLabel(hmcOrderType) }}</span></div>
              </el-col>

            </el-row>
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">完成时间：<span class="font-important-color">{{ data.order.finishTime | formatDate }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  下单用户：
                  <span class="font-important-color">
                    {{ data.orderUserName ? data.orderUserName : '- -' }} 
                  </span>
                  <span v-if="data.order.orderUser">
                    【ID:{{ data.order.orderUser }}】
                  </span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  订单处理者：
                  <span class="font-important-color">{{ data.operateUserName ? data.operateUserName : '- -' }}
                  </span> 
                  <span v-if="data.operateUserId">
                    【ID:{{ data.operateUserId }}】
                  </span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <div class="font-size-base font-title-color">用户支付/确认时间：<span class="font-important-color">{{ data.order.paymentTime | formatDate }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 1. 商家后台，2。c端小程序 -->
                <div class="font-size-base font-title-color">订单创建类型：<span class="font-important-color">{{ data.order.createSource == 1 ? "商家创建":"用户创建" }}</span></div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>结算信息
          </div>

          <div class="pad-l-6 mar-b-32">
            <el-row :gutter="24" class="mar-b-8">
              <el-col :span="8">
                <div class="font-size-base font-title-color">订单额总额：<span class="font-important-color">{{ data.order.totalAmount | toThousand('¥') }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">以岭给药品终端结算额：<span class="font-important-color">{{ data.order.terminalSettleAmount | toThousand('¥') }}</span></div>
              </el-col>
              <el-col :span="8">
                <!-- 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单	 -->
                <div class="font-size-base font-title-color">药品终端结算状态：<span class="font-important-color">{{ data.order.terminalSettleStatus | dictLabel(hmcSettlementStatus) }}</span></div>
              </el-col>
            </el-row>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>收货人信息
          </div>
          <div class="pad-l-6 mar-b-32">
            <el-row :gutter="24">
              <el-col :span="8">
                <div class="font-size-base font-title-color">姓名：<span class="font-important-color">{{ data.orderRelateUser? data.orderRelateUser.userName:'--' }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">手机号：<span class="font-important-color">{{ data.orderRelateUser? data.orderRelateUser.userTel:'--' }}</span></div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">地址：<span class="font-important-color">{{ data.orderRelateUser?data.orderRelateUser.provinceName:'--' }}{{ data.orderRelateUser?data.orderRelateUser.cityName:'--' }}{{ data.orderRelateUser?data.orderRelateUser.districtName:'--' }}{{ data.orderRelateUser?data.orderRelateUser.detailAddress:'--' }}</span></div>
              </el-col>
            </el-row>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>配送方式
          </div>
          <div class="pad-l-6 mar-b-32">
            <el-row :gutter="24">
              <!-- data.order.deliverType 配送方式 1-自提 2-物流 -->
              <!-- 快递显示 -->
              <el-col :span="8" v-if="data.order.deliverType == 2">
                <div class="font-size-base font-title-color">快递<span class="font-important-color">--</span></div>
              </el-col>
              <!-- 自提显示 -->
              <el-col :span="8" v-if="data.order.deliverType == 1">
                <div class="font-size-base font-title-color">自提：<span class="font-important-color">以岭医院</span></div>
              </el-col>
            </el-row>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>商品明细
          </div>
          <div class="pad-l-6 mar-b-32">
            <yl-table :show-header="true" border stripe :list="list">
              <el-table-column label="商品名称" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div>{{ row.goodsName }}</div>
                    <div>{{ row.goodsSpecifications }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="数量" align="center" prop="goodsQuantity"></el-table-column>
              <el-table-column label="以岭给终端的结算单价" align="center" prop="terminalSettlePrice"></el-table-column>
              <el-table-column label="合计" align="center" prop="goodsAmount"></el-table-column>
              <el-table-column label="是否管控" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <!-- 1管控 0不管控	 -->
                    <div> {{ row.controlStatus == 0 ?"不管控":"管控" }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="管控采购渠道" align="center" prop="goodsName">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="row.channelNameList">
                      <div v-for="(item, index) in row.channelNameList" :key="index">{{ item }}</div>
                    </div>
                    <div v-else>- -</div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>处方信息
          </div>
          <div class="data-img">
            <div class="data-img" v-if="prescriptionInfo.prescriptionSnapshotUrlList && prescriptionInfo.prescriptionSnapshotUrlList.length > 0">
              <div v-for="(item, index) in prescriptionInfo.prescriptionSnapshotUrlList" :key="index" @click="prescriptionClick2(item.prescriptionSnapshotUrl, prescriptionInfo.prescriptionSnapshotUrlList)">
                <img :src="item.prescriptionSnapshotUrl" alt="">
              </div>
            </div>
            <p v-else>暂无相关处方</p>
          </div>
          <div class="header-bar mar-b-12">
            <div class="sign"></div>票据信息
          </div>
          <div class="data-img" v-if="data.orderReceiptsList && data.orderReceiptsList.length > 0">
            <div v-for="(item, index) in data.orderReceiptsList" :key="index" @click="prescriptionClick(item.orderReceiptsUrl, data.orderReceiptsList)">
              <img :src="item.orderReceiptsUrl" alt="">
            </div>
          </div>
          <p class="pad-l-6" v-else>暂无相关数据</p>
        </div>
        <div class="bottom-bar-view flex-row-center">
          <!-- 新增票据管理 与 上传处方按钮 -->
          <yl-button type="primary" @click="billClick" v-if="data.order.synchronousType != 2">票据管理</yl-button>
          <yl-button type="primary" @click="uploadPrescriptionClick" v-if="data.order.synchronousType != 2">上传处方</yl-button>
          <!-- orderStatus 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退	-->
          <yl-button type="primary" plain @click="goBack(data)">返回列表</yl-button>
          <!-- 处理显示要求：待自提/待发货   因为一期没有快递所以只有待自提显示 -->
          <yl-button v-if="data.order.orderStatus == 3 || data.order.orderStatus == 4" type="primary" @click="orderCancle(data)">处理</yl-button>
          <!-- 退单按钮显示要求：待自提/待发货/待收货显示 -->
          <yl-button v-if="data.order.orderStatus == 3 || data.order.orderStatus == 4 || data.order.orderStatus == 5" type="primary" @click="chargeback">退单</yl-button>  
        </div>
      </div>
      <!-- 发货信息 -->
      <div v-else-if="activeIndex === 2">
        <div class="common-box mar-t-8">
          <div class="header-bar mar-b-12">
            <div class="sign"></div>包裹信息
          </div>
          <div class="pad-l-6 mar-b-32">
            <!-- deliverType  配送方式 1-自提 2-物流	 -->
            <!-- 快递显示 -->
            <el-row class="mar-b-8" v-if="data.order.deliverType == 2">
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  快递公司：
                  <span class="font-important-color">未找到返回参数</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  发货时间：
                  <span class="font-important-color">
                    <span v-if="operateList && operateList.length > 0">
                      {{ operateList[0].operateTime | formatDate }}
                    </span>
                    <span v-else>- -</span>
                  </span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  处理者：
                  <span class="font-important-color">
                    {{ operateList && operateList.length > 0 ? operateList[0].operateUserName : '- -' }}
                  </span>
                </div>
              </el-col>
            </el-row>
            <!-- 快递显示 -->
            <el-row class="mar-b-8" v-if="data.order.deliverType == 2">
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  快递单号：
                  <span class="font-important-color">未找到返回参数</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  确定收货时间：
                  <span class="font-important-color">未找到返回参数</span>
                </div>
              </el-col>
            </el-row>
            <!-- 自提显示 -->
            <el-row class="mar-b-8" v-if="data.order.deliverType == 1">
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  提货时间：
                  <span class="font-important-color">
                    <span v-if="operateList && operateList.length > 0">
                      {{ operateList[0].operateTime | formatDate }}
                    </span>
                    <span v-else>- -</span>
                  </span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="font-size-base font-title-color">
                  处理者：
                  <span class="font-important-color">
                    {{ operateList && operateList.length > 0 ? operateList[0].operateUserName : '- -' }}
                  </span>
                </div>
              </el-col>
            </el-row>
            <div class="pro-box">
              <div class="pro-name">
                <div class="font-size-base font-title-color">
                  商品
                  <span class="font-important-color"></span>
                </div>
              </div>
              <div class="pro-content">
                <div class="pro-desc" v-for="(item, index) in list" :key="index">
                  <div class="font-size-base font-title-color">
                    药品{{ index + 1 }}：
                    <span class="font-important-color">
                      {{ item.goodsName }} 
                      规格:{{ item.goodsSpecifications }}
                    </span>
                  </div>
                  <div class="font-size-base font-title-color">
                    出货数量：
                    <span class="font-important-color">
                      {{ item.goodsQuantity }}
                    </span>
                  </div>
                  <div class="font-size-base font-title-color">
                    总计：
                    <span class="font-important-color">{{ item.goodsAmount }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-bar-view flex-row-center">
          <yl-button type="primary" plain @click="goBack(data)">返回列表</yl-button>
        </div>
      </div>
      <!-- 退单弹窗 -->
      <yl-dialog title="退单" width="868px" :visible.sync="show1" @confirm="submitReturn">
        <div class="dia-content2">
          <el-row :gutter="24" class="mar-b-16">
            <el-col :span="10">
              <div class="font-size-base font-title-color">
                平台退单编号：
                <span class="font-important-color">
                  {{ data.order.orderNo ? data.order.orderNo : '- -' }}
                </span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">
                退款单联系人信息：
                <span class="font-important-color">
                  {{ data.orderRelateUser.userName ? data.orderRelateUser.userName : '- -' }}
                </span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="12" class="font-size-lg font-important-color bold dia-title">
              退货的商品订单来自{{ data.order.orderNo ? data.order.orderNo : '- -' }}
            </el-col>
          </el-row>
          <div class="mar-b-16">
            <yl-table :show-header="true" border stripe :list="list">
              <el-table-column label="商品名称" align="center" prop="goodsName"></el-table-column>
              <el-table-column label="规格" align="center" prop="goodsSpecifications"></el-table-column>
              <el-table-column label="退药数量" align="center" prop="goodsQuantity"></el-table-column>
              <el-table-column label="单价" align="center" prop="terminalSettlePrice"></el-table-column>
              <el-table-column label="退单合计价格" align="center" prop="terminalSettleAmount"></el-table-column>
            </yl-table>
          </div>
          <div class="flex-between  mar-b-16">
            <div class="color-red mar-b-16">请确定药品确实没给到用户，负责一旦退单无法拿追回</div>
            <div class="font-important-color bold">
              合计：
              {{ data.order.terminalSettleAmount ? data.order.terminalSettleAmount : '- -' }}
              元
            </div>
          </div>
          <div class="mar-b-16">
            <el-form :model="form" :rules="rules" ref="form" label-width="60px">
              <el-form-item label="备注" prop="remark">
                <el-input class="remark-inp" type=" textarea" v-model="form.remark" placeholder="请输入" autosize></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </yl-dialog>

    </div>
    <!-- 图片放大 -->
    <el-image-viewer style="z-index: 9999" v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    <!-- 处方信息上传 -->
    <yl-dialog 
      width="650px" 
      title="处方信息上传" 
      :show-footer="true" 
      :visible.sync="prescriptionShow" 
      @confirm="prescriptionConfirm">
      <div class="dialog-content">
        <div class="header-bar mar-b-12">
          <div class="sign"></div>处方信息
        </div>
        <div>
          <el-row class="mar-b-8">
            <el-col :span="4">
              <div class="font-size-base font-title-color">医生名称</div>
            </el-col>
            <el-col :span="12">
              <el-input v-model="prescriptionData.doctor" placeholder="请输入医生名称" />
            </el-col>
          </el-row>
          <el-row class="mar-b-8">
            <el-col :span="4">
              <div class="font-size-base font-title-color">诊断结果</div>
            </el-col>
            <el-col :span="12">
              <el-input v-model="prescriptionData.interrogationResult" placeholder="请输入诊断结果" />
            </el-col>
          </el-row>
          <el-row class="mar-b-8">
            <el-col :span="4">
              <div class="font-size-base font-title-color">上传处方图片</div>
            </el-col>
            <el-col :span="20" class="dialog-contents" style="padding:0">
              <custom-upload
                class="uploadImages"
                width="'100%'"
                height="'100%'"
                :max-size="12288"
                :limit="15"
                :file-list="buillImg2"
                oss-key="prescriptionPic"
                @onSuccess="billSuccess2"
                @on-remove="remove2"
                @on-preview="onPreview">
              </custom-upload>
            </el-col>
          </el-row>
        </div>
        <div class="header-bar mar-b-12">
          <div class="sign"></div>患者信息
        </div>
        <el-row>
          <el-col :span="4">
            <div class="font-size-base font-title-color">
              姓名：
              <span class="font-important-color"> {{ data.patientsName }} </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="font-size-base font-title-color">
              手机号：
              <span class="font-important-color"> {{ data.patientsPhone }} </span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="font-size-base font-title-color">
              年龄：
              <span class="font-important-color"> {{ data.patientsAge }} </span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="font-size-base font-title-color">
              性别：
              <span class="font-important-color"> {{ data.patientsGender }} </span>
            </div>
          </el-col>
        </el-row>
      </div>
    </yl-dialog>
    <!-- 票据管理 -->
    <yl-dialog 
      width="660px" 
      title="票据"
      :show-footer="true" 
      :visible.sync="billShow"
      @confirm="billConfirm">
      <div class="dialog-contents">
        <custom-upload
          class="uploadImages"
          width="'100%'"
          height="'100%'"
          :max-size="12288"
          :limit="15"
          :file-list="buillImg"
          oss-key="orderReceipts"
          @onSuccess="billSuccess"
          @on-remove="remove"
          @on-preview="onPreview">
        </custom-upload>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getOrderDetail, returnOrder, saveOrderReceipts, saveOrderPrescription } from '@/subject/pop/api/cmp_api/order'
import { hmcOrderStatus, hmcSettlementStatus, hmcOrderType, hmcPaymentStatus } from '@/subject/pop/utils/busi'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import customUpload from '@/subject/pop/components/CustomUpload';
export default {
  name: 'CmpOrderDetail',
  components: {
    ElImageViewer,
    customUpload
  },
  computed: {
    // 订单状态
    hmcOrderStatus() {
      return hmcOrderStatus()
    },
    // 药品终端结算状态
    hmcSettlementStatus() {
      return hmcSettlementStatus()
    },
    // 订单类型
    hmcOrderType() {
      return hmcOrderType()
    },
    // 支付类型
    hmcPaymentStatus() {
      return hmcPaymentStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [],
      data: {
        insuranceRecord: {},
        order: {},
        orderRelateUser: {},
        orderDetailList: [],
        orderReturn: {},
        orderReturnDetailList: [],
        // 票据信息
        orderReceiptsList: [],
        // 患者年龄
        patientsAge: '',
        // 患者性别
        patientsGender: '',
        // 患者姓名
        patientsName: '',
        // 患者手机号	
        patientsPhone: ''
      },
      // 操作人等，去货人。
      operateList: [],
      // 商品明细列表
      list: [],
      // 处方详情弹窗
      prescriptionInfo: {},
      // 处方药品列表
      prescriptionList: [],
      activeIndex: 1,
      // 退单弹窗
      show1: false,
      form: {
        remark: ''
      },
      rules: {
        remark: [
          { required: true, message: '请输入', trigger: 'blur' }
        ]
      },
      // 图片放大是否显示
      showViewer: false,
      imageList: [],
      // 处方信息弹窗
      prescriptionShow: false,
      // 处方信息上传
      prescriptionData: {
        doctor: '',
        interrogationResult: ''
      },
      // 票据弹窗
      billShow: false,
      buillImg: [],
      // 处方图片
      buillImg2: []
    };
  },
  created() {
    console.log(this.navList);
    this.id = this.$route.params.id
    this.type = this.$route.params.type
    if (this.type == 1) {
      this.navList = [{
        title: '首页',
        path: ''
      },
      {
        title: '订单管理'
      },
      {
        title: '全部药品订单',
        path: '/cmp_order/cmp_order_list'
      },
      {
        title: '订单详情'
      }]
    } else if (this.type == 2) {
      this.navList = [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理'
        },
        {
          title: '药品待处理订单',
          path: '/cmp_order/cmp_pending_order'
        },
        {
          title: '订单详情'
        }
      ]
    } else if (this.type == 3) {
      this.navList = [
        {
          title: '首页',
          path: ''
        },
        {
          title: '订单管理'
        },
        {
          title: '退货退单',
          path: '/cmp_order/cmp_return_order'
        },
        {
          title: '订单详情'
        }
      ]
    }
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    changeTab(index) {
      this.activeIndex = index;
    },
    async getDetail() {
      let data = await getOrderDetail(this.id)
      if (data) {
        this.data = data
        // 商品列表
        this.list = data.orderDetailList
        this.prescriptionInfo = data.orderPrescriptionDetail.prescription ? data.orderPrescriptionDetail.prescription : {}// 处方详情
        this.prescriptionList = data.orderPrescriptionDetail.goodsList// 处方药品列表
        this.operateList = data.operateList // 订单操作记录，操作人，取货人等
      }
    },
    // 处理 发货
    orderCancle() {
      this.$router.push({
        name: 'CmpChoiceSendGoods',
        params: {
          id: this.id,
          type: this.type
        }
      });
    },
    goBack() {
      this.$router.go(-1)
    },

    // 退单
    submitReturn() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          let data = await returnOrder(this.id, this.form.remark)
          if (data !== undefined) {
            this.form.remark = ''
            this.show1 = false
            this.getDetail();
            this.$common.n_success('退单成功')
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 关闭弹窗图片
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 点击图片
    prescriptionClick(val, dataList) {
      if (val != '') {
        this.imageList = [val];
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val != dataList[i].orderReceiptsUrl) {
            this.imageList.push(
              dataList[i].orderReceiptsUrl
            )
          }
        }
      }
      this.showViewer = true;
    },
    // 点击 处方图片
    prescriptionClick2(val, dataList) {
      if (val != '') {
        this.imageList = [val];
      }
      if (dataList && dataList.length > 1) {
        for (let i = 0; i < dataList.length; i ++) {
          if (val != dataList[i].prescriptionSnapshotUrl) {
            this.imageList.push(
              dataList[i].prescriptionSnapshotUrl
            )
          }
        }
      }
      this.showViewer = true;
    },
    // 点击票据管理
    billClick() {
      this.buillImg = [];
      for (let i = 0; i < this.data.orderReceiptsList.length; i++) {
        this.buillImg.push({
          url: this.data.orderReceiptsList[i].orderReceiptsUrl,
          key: this.data.orderReceiptsList[i].orderReceiptsKey
        })
      }
      this.billShow = true;
    },
    // 上传处方
    uploadPrescriptionClick() {
      this.prescriptionData = {
        doctor: this.prescriptionInfo.doctor,
        interrogationResult: this.prescriptionInfo.interrogationResult
      }
      this.buillImg2 = [];
      if (this.prescriptionInfo && this.prescriptionInfo.prescriptionSnapshotUrlList && this.prescriptionInfo.prescriptionSnapshotUrlList.length > 0) {
        for (let i = 0; i < this.prescriptionInfo.prescriptionSnapshotUrlList.length; i ++) {
          this.buillImg2.push({
            url: this.prescriptionInfo.prescriptionSnapshotUrlList[i].prescriptionSnapshotUrl,
            key: this.prescriptionInfo.prescriptionSnapshotUrlList[i].prescriptionSnapshotKey
          })
        }
      }
      this.prescriptionShow = true
    },
    billSuccess(obj) {
      this.buillImg.push({
        key: obj.key,
        url: obj.url
      })
    },
    // 处方图片
    billSuccess2(obj) {
      this.buillImg2.push({
        key: obj.key,
        url: obj.url
      })
    },
     // 删除图片
    remove(file, fileList) {
      for (let i = 0; i < this.buillImg.length; i ++) {
        if (this.buillImg[i].key == file.key) {
          this.buillImg.splice(i, 1)
        }
      }
    },
    // 删除处方图片
    remove2(file, fileList) {
      for (let i = 0; i < this.buillImg2.length; i ++) {
        if (this.buillImg2[i].key == file.key) {
          this.buillImg2.splice(i, 1)
        }
      }
    },
    // 图片放大
    onPreview(file, fileList) {
      if (file.url != '') {
        this.imageList = [file.url];
        this.showViewer = true;
      }
    },
    // 处方点击确定
    async prescriptionConfirm() {
      let query = this.prescriptionData;
      if (this.buillImg2 && this.buillImg2.length > 0) {
        let imgData = [];
        for (let i = 0; i < this.buillImg2.length; i ++) {
          imgData.push(this.buillImg2[i].key)
        }
        this.$common.showLoad()
        let data = await saveOrderPrescription(
          query.doctor,
          query.interrogationResult,
          this.id,
          imgData
        )
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('处方信息更新成功！')
          this.getDetail()
          this.prescriptionShow = false;
        }
      } else {
        this.$common.warn('处方图片不能为空')
      } 
    },
    // 点击票据保存
    async billConfirm() {
      if (this.buillImg && this.buillImg.length > 0) {
        let imgData = [];
        for (let i = 0; i < this.buillImg.length; i ++) {
          imgData.push(this.buillImg[i].key)
        }
        this.$common.showLoad()
        let data = await saveOrderReceipts(
          this.id,
          imgData
        )
        this.$common.hideLoad()
        if (data !== undefined) {
          this.$common.n_success('票据更新成功！')
          this.getDetail();
          this.billShow = false;
        }
      } else {
        this.$common.warn('票据图片不能为空')
      }
    },
    // 点击退单
    chargeback() {
      this.show1 = true;
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
