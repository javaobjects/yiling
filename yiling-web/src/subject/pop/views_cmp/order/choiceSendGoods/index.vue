<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <el-row :gutter="24">
        <!-- 订单信息 -->
        <el-col :span="14">
          <div class="common-box mar-t-8">
            <div class="header-bar mar-b-12">
              <div class="sign"></div>订单信息
            </div>
            <div class="pad-l-6">
              <el-row :gutter="24" class="mar-b-8">
                <el-col :span="12">
                  <div class="font-size-base font-title-color">
                    平台订单编号：
                    <span class="font-important-color">
                      {{ data.order.orderNo }}
                    </span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <!-- 支付方式:1-保险理赔结算	 -->
                  <div class="font-size-base font-title-color">
                    支付方式：
                    <span class="font-important-color">
                      {{ data.order.paymentMethod == 1 ? '保险理赔结算' : '--' }}
                    </span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24" class="mar-b-8">
                <el-col :span="12">
                  <div class="font-size-base font-title-color">拿货核销码：<span class="font-important-color">{{ data.offCode }}</span></div>
                </el-col>
                <el-col :span="12">
                  <!-- 订单类型:1-其他/2-虚拟商品订单/3-普通商品/4-药品订单	 -->
                  <div class="font-size-base font-title-color">订单类型：<span class="font-important-color">{{ data.order.orderType | dictLabel(hmcOrderType) }}</span></div>
                </el-col>
              </el-row>
              <el-row :gutter="24" class="mar-b-8">

                <el-col :span="12">
                  <!-- data.order.deliverType 配送方式 1-自提 2-物流 -->
                  <div class="font-size-base font-title-color">配送方式：<span class="font-important-color">{{ data.order.deliverType | dictLabel(hmcDeliveryStatus) }}</span></div>
                </el-col>
                <el-col :span="12">
                  <div class="font-size-base font-title-color">药品服务终端：<span class="font-important-color">{{ data.order.ename }}</span></div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
        <!-- 收获信息 -->
        <el-col :span="10">
          <div class="common-box mar-t-8">
            <div class="header-bar mar-b-12">
              <div class="sign"></div>收货信息
            </div>
            <div class="pad-l-6">
              <el-row :gutter="24" class="mar-b-8">
                <el-col :span="24">
                  <div class="font-size-base font-title-color">
                    姓名：
                    <span class="font-important-color">
                      {{ data.orderRelateUser.userName ? data.orderRelateUser.userName : '- -' }}
                    </span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24" class="mar-b-8">
                <el-col :span="24">
                  <div class="font-size-base font-title-color">
                    手机号：
                    <span class="font-important-color">
                      {{ data.orderRelateUser.userTel ? data.orderRelateUser.userTel : '- -' }}
                    </span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24" class="mar-b-8">
                <el-col :span="24">
                  <div class="font-size-base font-title-color">
                    地址：
                    <span class="font-important-color" v-if="data.orderRelateUser.provinceName">
                      {{ data.orderRelateUser.provinceName }}
                      {{ data.orderRelateUser.cityName }}
                      {{ data.orderRelateUser.districtName }}
                      {{ data.orderRelateUser.detailAddress }}
                    </span>
                    <span class="font-important-color" v-else>
                      - -
                    </span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
      <!--  商品明细 -->
      <div class="common-box mar-t-16">
        <div class="header-bar mar-b-12">
          <div class="sign"></div>商品明细
        </div>
        <div class="pro-box mar-b-12" v-for="(item, index) in list" :key="index">
          <div class="pro-name">
            {{ item.goodsName }}
          </div>
          <div class="pro-content mar-t-8">
            <div class="pro-desc">
              <div class="font-size-base font-title-color">规格型号：<span class="font-important-color">{{ item.goodsSpecifications }}</span></div>
              <div class="font-size-base font-title-color channelList">
                <div>管控采购渠道：</div>
                <div v-if="item.channelNameList">
                  <div class="font-important-color" v-for="(citem, cindex) in item.channelNameList" :key="cindex">{{ citem }}</div>
                </div>
                <div class="font-important-color" v-else>- -</div>
              </div>
              <div class="font-size-base font-title-color">购买数量：X<span class="font-important-color">{{ item.goodsQuantity }}</span></div>
            </div>
          </div>
        </div>
      </div>
      <!-- 票据信息 -->
      <div class="common-box mar-t-16">
        <div class="header-bar mar-b-12">
          <div class="sign"></div>票据信息
        </div>
        <div class="bill-information">
          <el-row class="mar-b-8">
            <el-col :span="12">
              <div class="font-size-base font-title-color">处方信息</div>
              <div class="data-img">
                <div v-for="(item, index) in prescriptionInfo.prescriptionSnapshotUrlList" :key="index" @click="prescriptionClick2(item.prescriptionSnapshotUrl, prescriptionInfo.prescriptionSnapshotUrlList)">
                  <img :src="item.prescriptionSnapshotUrl" alt="">
                </div>
              </div>
              <yl-button type="primary" plain @click="uploadPrescriptionClick">{{ prescriptionInfo && prescriptionInfo.prescriptionSnapshotUrlList && prescriptionInfo.prescriptionSnapshotUrlList.length > 0 ? '修改处方' : '上传处方' }}</yl-button>
            </el-col>
            <el-col :span="12">
              <div class="font-size-base font-title-color">出库票据</div>
              <div class="data-img">
                <div v-for="(item, index) in data.orderReceiptsList" :key="index" @click="prescriptionClick(item.orderReceiptsUrl, data.orderReceiptsList)">
                  <img :src="item.orderReceiptsUrl" alt="">
                </div>
              </div>
              <yl-button type="primary" plain @click="billClick">{{ data.orderReceiptsList && data.orderReceiptsList.length > 0 ? '修改票据' : '上传票据' }}</yl-button>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="goBack(data)">返回</yl-button>
        <yl-button type="primary" @click="sure1(data)" v-if="data.order.deliverType == 1">确定，设为已提</yl-button>
        <yl-button type="primary" @click="sure2(data)" v-if="data.order.deliverType == 2">确定，填写发货信息</yl-button>
      </div>
    </div>
    <!-- 库存弹窗 -->
    <yl-dialog title="请核实保单信息" :visible.sync="show" width="488px" @confirm="submit1">
      <div class="dia-content-tips">
        <i class="el-icon-info"></i>
        点击确定，核实信息正确后，说明药已经给到用户，请谨慎操作</div>
      <div class="dia-content">
        <el-form :model="form1" :rules="rules1" ref="form1" label-width="150px">
          <el-form-item label="保单人身份证后6位" prop="cardNumber">
            <el-input v-model="form1.cardNumber"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 若是店家从商家后台发起的兑付，不需要再次输入 身份证信息 -->
    <yl-dialog title="请核实保单信息" :visible.sync="dialogShow" width="488px" @confirm="determine">
      <div class="dialog-content">
        <p>点击确定说明药已经给到用户，请谨慎操作</p>
        <p>确定要设为已自提吗？</p> 
      </div>
    </yl-dialog>
    <!-- 库存弹窗 -->
    <yl-dialog title="完善快递信息" :visible.sync="show1" width="685px" @confirm="submit2">
      <div class="dia-content">
        <el-form :model="form2" :rules="rules2" ref="form2" label-width="180px">
          <el-form-item label="快递公司" prop="">
            <el-input v-model="form2.company"></el-input>
          </el-form-item>
          <el-form-item label="快递单号" prop="">
            <el-input v-model="form2.companyNo"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form2.remark"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
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
            <el-col :span="3">
              <div class="font-size-base font-title-color">医生名称</div>
            </el-col>
            <el-col :span="12">
              <el-input v-model="prescriptionData.doctor" placeholder="请输入医生名称" />
            </el-col>
          </el-row>
          <el-row class="mar-b-8">
            <el-col :span="3">
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
    <!-- 二维码弹窗提示 -->
    <yl-dialog width="488px" title="请完善被保人信息" :show-footer="false" :visible.sync="codeShow">
      <div class="dialog-content-qrcode">
        <div class="dialog-show">
          <div class="dialog-text-color">
            检测到，此被保人身份证与签名尚未完善，请扫码进行信息完善
          </div>
          <div class="qr-code" ref="qrCodes"></div>
          <p>使用微信扫一扫，进行信息完善</p>
        </div>
      </div>
    </yl-dialog>
    <!-- 图片放大 -->
    <el-image-viewer style="z-index: 9999" v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import 
  { 
    getOrderDetail, 
    orderReceived, 
    orderSend, 
    saveOrderReceipts, 
    saveOrderPrescription 
  } 
  from '@/subject/pop/api/cmp_api/order'
import { hmcDeliveryStatus, hmcOrderType } from '@/subject/pop/utils/busi'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import { getCurrentUser } from '@/subject/pop/utils/auth';
import loadQrCode from '@/common/utils/qrcode'
import customUpload from '@/subject/pop/components/CustomUpload';
export default {
  name: 'CmpChoiceSendGoods',
  components: {
    ElImageViewer,
    customUpload
  },
  computed: {
    // 配送方式
    hmcDeliveryStatus() {
      return hmcDeliveryStatus()
    },
    // 订单类型
    hmcOrderType() {
      return hmcOrderType()
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
      list: [],
      activeIndex: true,
      show: false,
      show1: false,
      form1: {
        cardNumber: ''
      },
      rules1: {
        cardNumber: [
          { required: true, message: '请输入正确的身份证后6位', trigger: 'blur' }
        ]
      },
      form2: {
        company: '',
        companyNo: '',
        remark: ''
      },
      rules2: {},
      // 出库票据
      buillImg: [],
      // 处方图片
      buillImg2: [],
      prescriptionInfo: {},
      billShow: false,
      // 处方信息
      prescriptionShow: false,
      prescriptionData: {
        doctor: '',
        interrogationResult: '',
        // 图片key
        prescriptionSnapshotUrl: '',
        // 图片 url
        prescriptionSnapshotUrlStr: ''
      },
      // 图片放大弹窗
      showViewer: false,
      imageList: [],
      // 1来源于商家后台 的设为已提
      dialogShow: false,
      // 二维码
      codeShow: false
    };
  },
  created() {
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
        title: '备货发货'
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
          title: '备货发货'
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
          title: '保险销售记录',
          path: '/cmp_order/cmp_insurance_order_detail'
        },
        {
          title: '备货发货'
        }
      ]
    }
    if (this.id) {
      this.getDetail()
    }
  },
  mounted() {

  },
  methods: {
    async getDetail() {
      let data = await getOrderDetail(this.id)
      if (data) {
        this.data = data
        // 商品列表
        this.list = data.orderDetailList 
        // 退单商品列表
        this.returnList = data.orderReturnDetailList 
        // 处方详情
        this.prescriptionInfo = data.orderPrescriptionDetail.prescription ? data.orderPrescriptionDetail.prescription : {}
      }
    },
    // 取消订单
    orderCancle(data) {
      this.$common.confirm('取消后不可恢复，如果需要可以再次下单！是否确认取消该预订单？', async r => {
        if (r) {
          this.$common.showLoad()
          let resData = {}
          if (data.auditStatus === 1) {
            resData = await preOrderCancle(data.id)
          } else if (data.auditStatus === 2) {
            resData = await preOrderSuperCancle(data.id)
          }
          this.$common.hideLoad()
          if (resData && resData.result) {
            this.$common.n_success('取消订单成功')
            this.$router.go(-1)
          }
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    sure1() {
      // 判断来源 1-商家后台，2-C端小程序
      if (this.data.order && this.data.order.createSource == 1) {
        this.dialogShow = true
      } else {
        this.show = true
      }
    },
    sure2() {
      this.show1 = true
    },
    // 提交省份证后6位
    submit1() {
      this.$refs['form1'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await orderReceived(
            this.id, 
            this.data.insuranceRecord.insuranceId, 
            this.form1.cardNumber
          )
          this.$common.hideLoad()
          if (data === null) {
            this.dialogShow = false;
            this.$common.n_success('自提成功');
            this.$router.go(-1);
          } else {
            const { code, message } = data;
            if (code == 10010) {
              this.codeShow = true;
              setTimeout(() => {
                loadQrCode(() => {
                  new window.QRCode(this.$refs.qrCodes, {
                    width: 178,
                    height: 174,
                    text: `${process.env.VUE_APP_H5_URL}/#/cmp/infoCollect?id=${this.data.order.id}&currentUserId=${getCurrentUser().id}`,
                    correctLevel: 3
                  })
                })
              }, 100)
            } else if (code == 200) {
              this.show = false;
              this.$common.n_success('自提成功');
              this.$router.go(-1);
            } else {
              this.$common.warn(message);
            }
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 发货
    submit2() {
      this.$refs['form2'].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await orderSend(this.id, this.form2.companyNo, this.form2.company, this.form2.remark)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.getDetail();
            this.show = false
            this.$common.n_success('提交成功')
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 点击图片
    prescriptionClick(val, dataList) {
      if (val != '') {
        this.imageList.push(val)
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
    // 点击处方图片
    prescriptionClick2(val, dataList) {
      if (val != '') {
        this.imageList.push(val)
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
      this.prescriptionShow = true;
    },
    // 上传票据
    billClick() {
      this.buillImg = [];
      for (let i = 0; i < this.data.orderReceiptsList.length; i ++) {
        this.buillImg.push({
          url: this.data.orderReceiptsList[i].orderReceiptsUrl,
          key: this.data.orderReceiptsList[i].orderReceiptsKey
        })
      }
      this.billShow = true;
    },
    // 处方上传
    onSuccess(data) {
      if (data) {
        this.prescriptionData.prescriptionSnapshotUrl = data.key;
      }
    },
    // 出库票据上传
    billSuccess(obj) {
      this.buillImg.push({
        key: obj.key,
        url: obj.url
      })
    },
    // 处方图片上传
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
        for (let i = 0; i < this.buillImg.length; i++) {
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
     // 关闭弹窗图片
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 店家从商家后台发起的兑付 点击设为已提弹窗 的确定按钮
    async determine() {
      this.$common.showLoad();
      let data = await orderReceived(
        this.id, 
        this.data.insuranceRecord.insuranceId, 
        ''
      )
      this.$common.hideLoad();
      if (data === null) {
        this.dialogShow = false;
        this.$common.n_success('自提成功');
        this.$router.go(-1);
      } else {
        const { code, message } = data;
        if (code == 10010) {
          this.codeShow = true;
          setTimeout(() => {
            loadQrCode(() => {
              new window.QRCode(this.$refs.qrCodes, {
                width: 178,
                height: 174,
                text: `${process.env.VUE_APP_H5_URL}/#/cmp/infoCollect?id=${this.data.order.id}&currentUserId=${getCurrentUser().id}`,
                correctLevel: 3
              })
            })
          }, 100)
        } else if (code == 200) {
          this.dialogShow = false;
          this.$common.n_success('自提成功');
          this.$router.go(-1);
        } else {
          this.$common.warn(message)
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
