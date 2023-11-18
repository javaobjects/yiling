<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <!-- 订单基本信息 -->
      <div class="common-box mar-t-8">
        <div class="header-bar mar-b-12">
          <div class="sign"></div>基本信息
        </div>
        <div class="pad-l-6 mar-b-32">
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">订单销售交易单号：<span class="font-important-color">{{ data.orderNo }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保司保单号：<span class="font-important-color">{{ data.policyNo }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保险兑付药店：<span class="font-important-color">{{ data.orderSource }}</span></div>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">支付金额：<span class="font-important-color">{{ data.amount }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保单提供商：<span class="font-important-color">{{ data.companyName }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保单名称：<span class="font-important-color">{{ data.insuranceName }}</span></div>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">对应保司定额标示：<span class="font-important-color">{{ data.comboName }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保单生效时间：<span class="font-important-color">{{ data.effectiveTime | formatDate }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">是否退保：<span class="font-important-color">{{ data.isRetreat ? "是":"否" }}</span></div>
            </el-col>

          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">开始时间：<span class="font-important-color">{{ data.startTime | formatDate }}</span></div>
            </el-col>
            <el-col :span="8">
              <!-- 销售人员id 来源类型也通过此字段判断 0-线上渠道 大于0 线下终端 -->
              <div class="font-size-base font-title-color">来源类型：<span class="font-important-color">{{ data.sellerUserId == 0 ? "线上渠道":"线下终端" }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">保单状态：<span class="font-important-color">{{ data.policyStatus | dictLabel(hmcPolicyStatus) }}</span></div>
            </el-col>

          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">销售员所属企业：<span class="font-important-color">{{ data.terminalName }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">销售员：<span class="font-important-color">{{ data.sellerUserName?data.sellerUserName:'--' }} <span v-if="data.sellerUserName && data.sellerUserName !='-'">【ID:{{ data.sellerUserId }}】</span></span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">终止时间：<span class="font-important-color">{{ data.endTime | formatDate }}</span></div>
            </el-col>
          </el-row>
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">支付流水号：<span class="font-important-color">{{ data.transactionId?data.transactionId:'--' }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">支付时间：<span class="font-important-color">{{ data.payTime | formatDate }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">泰康支付订单号：<span class="font-important-color">{{ data.billNo?data.billNo:'--' }}</span></div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="font-size-base font-title-color">当前所交期：<span class="font-important-color">{{ data.currentPayStage }}</span></div>
            </el-col>
          </el-row>
        </div>
        <div class="header-bar mar-b-12">
          <div class="sign"></div>投保人信息
        </div>
        <div class="pad-l-6 mar-b-32">
          <el-row :gutter="24" class="mar-b-8">
            <el-col :span="8">
              <div class="font-size-base font-title-color">投保人：<span class="font-important-color">{{ data.holderName }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">身份证：<span class="font-important-color">{{ data.holderCredentialNo }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">手机号：<span class="font-important-color">{{ data.holderPhone }}</span></div>
            </el-col>
          </el-row>
        </div>
        <div class="header-bar mar-b-12">
          <div class="sign"></div>被保人信息
          <div class="policy-url mar-l-32" @click="checkPolicyUrl">查看电子保单</div>
        </div>

        <div class="pad-l-6 mar-b-32">
          <el-row :gutter="24">
            <el-col :span="8">
              <div class="font-size-base font-title-color">被保人：<span class="font-important-color">{{ data.issueName }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">身份证：<span class="font-important-color">{{ data.issueCredentialNo }}</span></div>
            </el-col>
            <el-col :span="8">
              <div class="font-size-base font-title-color">手机号：<span class="font-important-color">{{ data.issuePhone }}</span></div>
            </el-col>
          </el-row>
        </div>
        <div class="header-bar mar-b-12">
          <div class="sign"></div>保单服务内容
        </div>
        <div v-for="(item,index) in planList " :key="index">
          <div class="pad-l-6 mar-b-8 plan-top">
            <el-row :gutter="24" class="plan-desc">
              <el-col :span="10">
                <div class="font-size-base font-title-color">初始拿药日期：<span class="font-important-color">{{ item.initFetchTime | formatDate }}</span></div>
              </el-col>
              <el-col :span="10">
                <div class="font-size-base font-title-color">真实拿药日期：<span class="font-important-color">{{ item.actualFetchTime | formatDate }}</span></div>
              </el-col>
              <el-col :span="4">
                <!-- 拿药状态 1-已拿，2-未拿 -->
                <div class="font-size-base font-title-color">拿药状态：<span class="font-important-color" :class="{ 'color-green': item.fetchStatus == 1 }">{{ item.fetchStatus == 1?"已拿":"未拿" }}</span></div>
              </el-col>
            </el-row>
            <div class="plan-btn">
              <yl-button v-if="item.cashable" type="primary" plain @click="cashing(item.insuranceRecordId)">兑付</yl-button>
            </div>
          </div>
          <div class="pad-l-6 mar-b-32">
            <yl-table :show-header="true" border stripe :list="planGoodsList">
              <el-table-column label="药品名称" align="center" prop="goodsName"></el-table-column>
              <el-table-column label="药品规格" align="center" prop="specificInfo"></el-table-column>
              <el-table-column label="以岭给终端的结算单价" align="center" prop="terminalSettlePrice"></el-table-column>
              <el-table-column label="每次（盒）" align="center" prop="perMonthCount"></el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="goBack(data)">返回</yl-button>
      </div>
      <!-- 身份验证 -->
      <yl-dialog title="信息核实" :visible.sync="show" width="488px" @confirm="submit1">
        <div class="dia-content">
          <el-form :model="form1" :rules="rules1" ref="form1" label-width="180px">
            <el-form-item label="被保人身份证后6位" prop="cardNumber">
              <el-input v-model="form1.cardNumber"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 兑付提交 -->
      <yl-dialog title="订单信息确认" :visible.sync="show2" @confirm="submit2" width="868px">
        <div class="dia-content">
          <el-form :model="form2" ref="form2" label-width="80px">
            <div>
              <div class="header-bar mar-b-12">
                <div class="sign"></div>收货人信息
              </div>
              <div class="mar-b-16 dis-flex">
                <div class="font-size-base font-title-color">姓名：<span class="font-important-color">{{ data.holderName }}</span></div>
                <div class="mar-l-73 font-size-base font-title-color">手机号：<span class="font-important-color">{{ data.holderPhone }}</span></div>
              </div>
              <div class="header-bar mar-b-12">
                <div class="sign"></div>配送方式
              </div>
              <div class="mar-b-15">
                <el-form-item label="" prop="DeliveryStatus" label-width="0">
                  <el-radio-group v-model="form2.DeliveryStatus">
                    <el-radio label="1">自提</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
              <div class="header-bar mar-b-12">
                <div class="sign"></div>商品明细
              </div>
              <yl-table :show-header="true" stripe :list="planGoodsList">
                <el-table-column label="药品信息" align="center" prop="goodsName">
                  <template slot-scope="{ row }">
                    <div>
                      <div>{{ row.goodsName }}</div>
                      <div>{{ row.specificInfo }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="数量" align="center" prop="perMonthCount"></el-table-column>
                <el-table-column label="以岭给终端的结算单价" align="center" prop="terminalSettlePrice"></el-table-column>
                <el-table-column label="合计" align="center" prop="subTotal"></el-table-column>
              </yl-table>
              <div class="total-amount mar-b-16">
                <div>用户结算方式：保险理赔支付</div>
                <div>合计：¥{{ totalPrice }}元</div>
              </div>
              <div class="header-bar mar-b-12">
                <div class="sign"></div>是否有处方
              </div>
              <div class="mar-b-12">
                <el-radio-group v-model="isPrescription" @change="changeIsPre">
                  <el-radio label="1">是</el-radio>
                  <el-radio label="2">否</el-radio>
                </el-radio-group>
              </div>
              <!--  传处方时显示 -->
              <el-row v-show="isPrescription == 1">
                <el-col :span="12" :offset="0">
                  <el-form-item label="医生姓名" prop="doctorName">
                    <el-input v-model="form2.doctorName" placeholder="请输入"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12" :offset="0">
                  <el-form-item label="诊断结果" prop="diagnosticResults">
                    <el-input v-model="form2.diagnosticResults" placeholder="请输入"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item v-if="isPrescription == 1" label="上传处方" label-width="80px">
                    <div class="dialog-contents">
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
                    </div>
                  </el-form-item>
                </el-col>
                
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="票据" label-width="80px">
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
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="备注" label-width="40px">
                <el-input type="textarea" v-model="form2.remark" placeholder="请输入"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </div>
      </yl-dialog>
    </div>
    <!-- 图片放大 -->
    <el-image-viewer style="z-index: 9999" :on-close="onClose" :url-list="imageList" v-if="showViewer"/>
    <!-- 弹窗提示 -->
    <yl-dialog width="488px" title="请完善被保人信息" :show-footer="false" :visible.sync="qrCodeShow">
      <div class="dialog-content">
        <div class="dialog-show">
          <div class="dialog-text-color">
            检测到，此被保人身份证与签名尚未完善，请扫码进行信息完善
          </div>
          <div class="qr-code" ref="qrCode"></div>
          <p>使用微信扫一扫，进行信息完善</p>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { getInsuranceDetail, cashingOrder, checkCardLastSix, downloadPolicyFile } from '@/subject/pop/api/cmp_api/order'
import { hmcPolicyStatus, hmcDeliveryStatus } from '@/subject/pop/utils/busi'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import loadQrCode from '@/common/utils/qrcode'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import customUpload from '@/subject/pop/components/CustomUpload';
export default {
  name: 'CmpInsuranceOrderDetail',
  components: {
    ElImageViewer,
    customUpload
  },
  computed: {
    // 保单状态
    hmcPolicyStatus() {
      return hmcPolicyStatus()
    },
    // 配送状态
    hmcDeliveryStatus() {
      return hmcDeliveryStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [],
      data: {},
      planList: [],
      planGoodsList: [],
      show: false,
      list1: [],
      url: '',
      form1: {
        cardNumber: ''
      },
      rules1: {
        cardNumber: [
          { required: true, message: '请输入身份证后6位', trigger: 'blur' }
        ]
      },
      // 是否有处方
      isPrescription: '1',
      // 兑付
      form2: {
        // 配送方式
        DeliveryStatus: '1',
        // 医生姓名
        doctorName: '',
        // 诊断结果
        diagnosticResults: '',
        // 备注·
        remark: ''
      },
      // 兑付id 需要兑付的参保记录id
      cashId: '',
      totalPrice: 0,
      // 兑付提交
      show2: false,
      buillImg: [],
      // 处方图片
      buillImg2: [],
      showViewer: false,
      imageList: [],
      // 二维码
      qrCodeShow: false
    };
  },
  created() {
  },
  mounted() {
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
        title: '保险销售记录',
        path: '/cmp_order/cmp_insurance_sale_record'
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
    }
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    // 切换处方显示隐藏
    changeIsPre(e) {
      if (e == 1) {
        this.isPrescription = '1'
      } else {
        this.isPrescription = '2'
      }
      this.buillImg2 = []
    },
    // 获取保单详情
    async getDetail() {
      let data = await getInsuranceDetail(this.id)
      if (data) {
        this.totalPrice = data.total
        this.data = data.insuranceRecord
        this.planList = data.fetchPlanList
        this.planGoodsList = data.fetchPlanDetailList
        this.url = data.insuranceRecord.policyUrl
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    seePrscription() {
      this.show = true
    },
    // 查看电子保单
    async checkPolicyUrl() {
      this.$common.showLoad()
      let data = await downloadPolicyFile(this.$route.params.id)
      this.$common.hideLoad()
      if (data) {
        this.$common.goThreePackage(data)
      }
    },
    cashing(id) {
      // 需要兑付的参保记录id
      this.cashId = id 
      this.show = true
    },
    // 兑付 验证身份证后6位
    submit1() {
      this.$refs['form1'].validate(async (valid) => {
        if (valid) {
          //  保单id， 身份证后6位
          this.$common.showLoad()
          let data = await checkCardLastSix(this.cashId, this.form1.cardNumber)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.show = false
            this.$common.n_success('验证成功')
            this.show2 = true
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 兑付确认
    async submit2() {
      if (this.isPrescription == '1') {
        if (this.buillImg2 && this.buillImg2.length > 0) {
          let imgData = [];
          let imgData2 = [];
          if (this.buillImg && this.buillImg.length > 0) {
            for (let i = 0; i < this.buillImg.length; i ++) {
              imgData.push(this.buillImg[i].key)
            }
          }
          if (this.buillImg2 && this.buillImg2.length > 0) {
            for (let y = 0; y < this.buillImg2.length; y ++) {
              imgData2.push(this.buillImg2[y].key)
            }
          }
          // 保单纪录里的。保单id
          this.$common.showLoad();
          let data = await cashingOrder(
            this.id, 
            this.form2.doctorName, 
            this.form2.diagnosticResults, 
            imgData2,
            this.form2.remark,
            imgData
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.show2 = false;
            if (data.hasMaintainFlag) {
              this.$router.push({
                name: 'CmpChoiceSendGoods',
                params: {
                  id: data.id,
                  type: 3
                }
              });
            } else {
              this.qrCodeShow = true;
              setTimeout(() => {
                loadQrCode(() => {
                  new window.QRCode(this.$refs.qrCode, {
                    width: 178,
                    height: 174,
                    text: `${process.env.VUE_APP_H5_URL}/#/cmp/infoCollect?id=${data.id}&currentUserId=${getCurrentUser().id}`,
                    correctLevel: 3
                  })
                })
              }, 100)
              this.getDetail();
            }
          }
        } else {
          this.$common.warn('请上传处方图片！')
        }
      } else {
        let imgData = [];
        let imgData2 = [];
        if (this.buillImg && this.buillImg.length > 0) {
          for (let i = 0; i < this.buillImg.length; i ++) {
            imgData.push(this.buillImg[i].key)
          }
        }
        // 保单纪录里的。保单id
        this.$common.showLoad();
        let data = await cashingOrder(
          this.id, 
          this.form2.doctorName, 
          this.form2.diagnosticResults, 
          imgData2,
          this.form2.remark,
          imgData
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.show2 = false;
          if (data.hasMaintainFlag) {
            this.$router.push({
              name: 'CmpChoiceSendGoods',
              params: {
                id: data.id,
                type: 3
              }
            });
          } else {
            this.qrCodeShow = true;
            setTimeout(() => {
              loadQrCode(() => {
                new window.QRCode(this.$refs.qrCode, {
                  width: 178,
                  height: 174,
                  text: `${process.env.VUE_APP_H5_URL}/#/cmp/infoCollect?id=${data.id}&currentUserId=${getCurrentUser().id}`,
                  correctLevel: 3
                })
              })
            }, 100)
            this.getDetail();
          }
        }
      }
    },
    // 票据图片回显
    billSuccess(obj) {
      this.buillImg.push({
        url: obj.url,
        key: obj.key
      })
    },
    // 处方回显
    billSuccess2(obj) {
      this.buillImg2.push({
        url: obj.url,
        key: obj.key
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
    // 删除 处方图片
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
    // 关闭弹窗图片
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
