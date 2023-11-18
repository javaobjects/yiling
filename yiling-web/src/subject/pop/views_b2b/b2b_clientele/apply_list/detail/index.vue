<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="top-bar">
        <div class="flex-row-left font-size-lg item btn-item">
          <div class="header-bar">
            <div class="line-view"></div>
            <span>企业信息</span>
          </div>
          <!-- <div v-if="detailData.authStatus != 1"><span class="font-light-color">提交时间：</span>{{ detailData.updateTime | formatDate }}</div> -->
        </div>
        <div class="content-box">
          <div class="content-box-bottom">
            <el-row class="box">
              <el-col :span="24">
                <div class="intro">
                  <span class="font-name">{{ detailData && detailData.name }}</span>
                  <span class="font-type" :class="detailData.authStatus == 2 || detailData.authStatus == 4 ? 'font-color2' : (detailData.authStatus == 1 ? 'font-color1' : 'font-color3')">
                    {{ detailData.authStatus == 1 ? '待审核' : (detailData.authStatus == 2 || detailData.authStatus == 4 ? '已建采' : '已驳回') }}
                  </span>
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="7">
                <div class="intro">
                  <span class="font-title-color">企业类型：</span>
                  {{ detailData && $options.filters.dictLabel(detailData.type, enterpriseType) }}
                </div>
              </el-col>
              <el-col :span="10">
                <div class="intro">
                  <span class="font-title-color">所属地区：</span>
                  {{ detailData && detailData.provinceName }} ｜{{ detailData.cityName }} | {{ detailData.regionName }}
                </div>
              </el-col>
              <el-col :span="7">
                <div class="intro">
                  <span class="font-title-color">联系人：</span>
                  {{ detailData && detailData.contactor }}
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="7">
                <div class="intro">
                  <span class="font-title-color">社会统一信用代码：</span>
                  {{ detailData && detailData.licenseNumber }}
                  <span class="font-img" @click="copyClick(detailData.licenseNumber)"><img src="@/subject/pop/assets/invoice/copy.png" alt=""></span>
                </div>
              </el-col>
              <el-col :span="10">
                <div class="intro">
                  <span class="font-title-color">详细地址：</span>
                  {{ detailData && detailData.address }}
                </div>
              </el-col>
              <el-col :span="7">
                <div class="intro">
                  <span class="font-title-color">手机号：</span>
                  {{ detailData && detailData.contactorPhone }}
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <!-- 资质信息 -->
        <div class="header-bar mar-b-10"><div class="line-view"></div>资质信息</div>
        <div class="flex-wrap img-view">
          <div class="item" v-for="(item, index) in certificateVOList" :key="index">
            <div class="img-box flex-row-center" @click="imgClick(item)">
              <img object-fit="contain" :src="item.fileUrl">
            </div>
            <div class="pic-desc">
              <div class="title">{{ item.type | dictLabel(enterpriseCertificateType) }}</div>
              <div class="time">
                资质有效期
                <div>{{ item.periodBegin | formatDate('yyyy-MM-dd') }} 至 {{ item.periodEnd | formatDate('yyyy-MM-dd') }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 客户分组 -->
      <div class="top-bar">
        <div class="header-bar mar-b-10">
          <div class="line-view"></div>
          审核信息
          <span v-if="detailData.authStatus == 1" class="icon" @click="openClick"><img src="@/subject/pop/assets/invoice/gth.png" alt=""></span>
        </div>
        <div class="conter-padd">
          <div v-if="detailData.authStatus == 1">
            <div class="audit" v-if="promptShow">
              <div>
                <img src="@/subject/pop/assets/invoice/sjgth.png" alt="">
                审核操作说明
                <span @click="closeClick"><i class="el-icon-close"></i></span>
              </div>
              <p> ①  已对接ERP的供应商，在审核采销关系时，需先在ERP中做客户首营建档，确定客户在ERP中已存在，如未建档，平台不允许审核通过</p>
              <p>②  平台根据ERP对接刷新数据更新机制，若自动检测ERP中已有待审核客户，系统将自动审核通过，无需人工重复</p>
              <p>③  如ERP中已存在客户关系，操作审核通过时显示无数据，请联系平台ERP实施人员解决</p>
            </div>
          </div>
          <div class="content-box mar-t-10" v-if="detailData.authStatus == 1">
            <el-form :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="90px" class="demo-ruleForm" :disabled="detailData.authStatus != 1">
              <el-form-item label="申请时间:">
                {{ detailData.createTime | formatDate }}
              </el-form-item>
              <el-form-item label="ERP信息:" v-if="showErp" class="form-erp">
                <div class="erp-manger">
                  <el-row class="box">
                    <el-col :span="10">
                      ERP客户内码
                    </el-col>
                    <el-col :span="10">
                      ERP客户编码
                    </el-col>
                    <!-- <el-col :span="4">
                      操作
                    </el-col> -->
                  </el-row>
                  <el-row class="box-conter">
                    <el-col :span="10">
                      <span class="text">
                        {{ detailData.customerErpCode ? detailData.customerErpCode : '暂无数据' }}
                      </span>
                    </el-col>
                    <el-col :span="10">
                      <span class="text">
                        {{ detailData.customerCode ? detailData.customerCode : '暂无数据' }}
                      </span>
                    </el-col>
                    <!-- <el-col :span="4">
                      <el-button type="text" class="refresh" :loading="buttonLoading" @click="refreshClick">{{ buttonLoading ? '刷新中' : '刷新' }}</el-button>
                    </el-col> -->
                  </el-row>
                </div>
                <!-- <div class="prompt-size" v-if="buttonLoading">
                  <i class="el-icon-warning"></i>系统查询中，请耐心等候，关闭页面不会影响查询结果~
                </div> -->
              </el-form-item>
              <el-form-item label="审核结果:">
                <el-radio-group v-model="baseFormModel.authStatus" @change="authStatusChange">
                  <div class="radio-item">
                    <el-radio :label="2">通过</el-radio>
                    <el-radio :label="3">驳回</el-radio>
                  </div>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="驳回原因:" prop="authRejectReason" v-if="baseFormModel.authStatus == 3">
                <el-input
                  class="input-width"
                  type="textarea"
                  maxlength="100" 
                  show-word-limit
                  :autosize="{ minRows: 4, maxRows: 5}"
                  placeholder="请输入驳回原因"
                  v-model.trim="baseFormModel.authRejectReason">
                </el-input>
              </el-form-item>
            </el-form>
          </div>
          <el-row class="box mar-t-10" v-if="detailData.authStatus != 1">
            <el-col :span="7">
              <div class="intro">
                <span class="font-title-color">申请时间：</span>
                {{ detailData.createTime | formatDate }}
              </div>
            </el-col>
            <el-col :span="10">
              <div class="intro">
                <span class="font-title-color">审核人：</span>
                {{ detailData.authUserName }}
              </div>
            </el-col>
          </el-row>
          <el-row class="box" v-if="detailData.authStatus != 1 && showErp">
            <el-col :span="7">
              <div class="intro">
                <span class="font-title-color">ERP信息：</span>
                ERP客户内码：{{ detailData.customerErpCode ? detailData.customerErpCode : '暂无数据' }}
                <div style="margin-left:73px">ERP客户编码：{{ detailData.customerCode ? detailData.customerCode : '暂无数据' }}</div>
              </div>
            </el-col>
            <el-col :span="10">
              <div class="intro">
                <span class="font-title-color">审核时间：</span>
                {{ detailData.authTime | formatDate }}
              </div>
            </el-col>
          </el-row>
          <el-row class="box" v-if="detailData.authStatus == 3">
            <el-col :span="7">
              <div class="intro">
                <span class="font-title-color">驳回原因：</span>
                {{ detailData.authRejectReason }}
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 图片放大 -->
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick" v-if="detailData.authStatus == 1">确定</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getPurchaseApplyDetail,
  updateAuthStatus,
  refreshErpCode,
  getErpCode,
  getErpFlag
} from '@/subject/pop/api/b2b_api/client';
import { enterpriseType, enterpriseCertificateType } from '@/subject/pop/utils/busi';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'

export default {
  components: {
    ElImageViewer
  },
  computed: {
    enterpriseType() {
      return enterpriseType();
    },
    enterpriseCertificateType() {
      return enterpriseCertificateType();
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '客户管理'
        },
        {
          title: '采销审核',
          path: '/b2b_clientele/b2b_apply_list'
        },
        {
          title: '审核'
        }
      ],
      loading: false,
      detailData: {},
      showViewer: false,
      // 资质信息
      certificateVOList: [],
      imageList: [],
      baseRules: {
        authStatus: [{ required: true, message: '请选择有效期', trigger: 'change' }],
        authRejectReason: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }]
      },
      baseFormModel: {
        authStatus: 2,
        authRejectReason: ''
      },
      promptShow: true,
      //是否展示客户内部编码
      showErp: false,
      //是否加载中
      buttonLoading: false,
      // 轮循 定时器
      intervalTime: '',
      //调用次数
      num: 0
    };
  },
  mounted() {
    this.params = this.$route.params;
    this.getErpFlagApi();
    this.getDetail();
    
  },
  methods: {
    async getErpFlagApi() {
      let data = await getErpFlag()
      if (data) {
        this.showErp = data
      }
    },
    // 点击图片
    imgClick(val) {
      if (val.fileUrl && val.fileUrl != '') {
        this.imageList = [val.fileUrl];
        this.showViewer = true;
      }
    },
    //点击关闭图
    closeClick() {
      this.promptShow = false
    },
    //开启
    openClick() {
      this.promptShow = true
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    async getDetail() {
      this.$common.showLoad();
      let params = this.params;
      let detailData = await getPurchaseApplyDetail(params.customerEid);
      this.$common.hideLoad();
      if (detailData) {
        this.detailData = detailData
        this.certificateVOList = detailData.certificateVOList

        if (detailData.authStatus == 1) {
          this.baseFormModel.authStatus = 2
        } else {
          this.baseFormModel.authStatus = detailData.authStatus
        }
        this.baseFormModel.authRejectReason = detailData.authRejectReason
      }
    },
    authStatusChange() {
      this.baseFormModel.authRejectReason = ''
    },
    // 修改客户信息
    saveClick() {
      this.$refs['dataForm'].validate( (valid) => {
        if (valid) {
          //审核结果为 通过时
          if (this.baseFormModel.authStatus == 2 && this.showErp) {
            this.refreshClick()
          } else {
            // 不通过
            this.getDetermineApi()
          }
        } 
      })
    },
    //点击复制社会信用统一代码
    copyClick(val) {
      if (val != '') {
        let cInput = document.createElement('input');
        cInput.value = val;
        document.body.appendChild(cInput);
        cInput.select();
        document.execCommand('Copy');
        this.$common.n_success('复制成功')
        cInput.remove();
      }
    },
    //点击刷新
    async refreshClick() {
      // //this.buttonLoading = true
      this.$common.showLongLoad('系统处理中，请稍后~')
      let data = await refreshErpCode(
        this.params.customerEid
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        if (data.status == 1) {
          this.$common.showLongLoad('系统处理中，请稍后~')
          this.intervalTime = setInterval(() =>{
            this.num = this.num + 1;
            this.getErpApi()
          }, 5000)
        } else if (data.status == 2) {
          this.$common.n_success('系统已自动通过审核，请返回！')
          this.$router.go(-1)
        } else {
          this.$alert(
            `<div class="alert-conter">
              <div class="title"><span style="color: #EB4438"><i class="el-icon-error"></i></span>系统查询失败！</div>
              <div class="assistant">
                <div class="assistant-title">失败原因可能是:</div>
                <p>1. ERP远程查询工具版本过低</p>
                <p>2. ERP远程查询工具未开通</p>
                <p>请联系平台ERP实施售后解决！</p>
              </div>
            </div>`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '已了解',
            customClass: 'pop-up-alert'
          });
        }
        // this.buttonLoading = false;
      } else {
        // this.buttonLoading = false;
      }
    },
    async getErpApi() {
      let data = await getErpCode(
        this.params.customerEid
      )
      const { code, message } = data;
      if (code) {
        if (code == 100336 || code == 100337 || code == 100338) {
          if (this.num == 12) {
            this.$alert(
              `<div class="alert-conter">
                <div class="title"><span style="color: #F39217"><i class="el-icon-info"></i></span>ERP客户内码及编码不能为空！</div>
                <div class="assistant">
                  <div class="assistant-title">解决办法如下:</div>
                  <p>1. 请先前往您的ERP系统，为此客户建立采销关系 (首营资质)</p>
                  <p>2. 如ERP中已建档，请刷新页面或联系平台ERP实施售后解决!</p>
                </div>
              </div>`, '提示', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '已了解',
              customClass: 'pop-up-alert'
            });
            this.$common.hideLoad()
            clearInterval(this.intervalTime);
            this.num = 0
          } else {
            this.$common.showLongLoad('系统处理中，请稍后~')
          }
        } else {
          this.$common.hideLoad()
          this.$message.error(message);
          clearInterval(this.intervalTime);
        }
      } else {
        this.detailData.customerErpCode = data.innerCode
        this.detailData.customerCode = data.erpCode
        this.$common.hideLoad()
        this.getDetermineApi()
        clearInterval(this.intervalTime);
      }
    },
    async getDetermineApi() {
      let baseFormModel = this.baseFormModel
      this.$common.showLoad()
      let data = await updateAuthStatus(this.detailData.id, baseFormModel.authStatus, baseFormModel.authRejectReason)
      this.$common.hideLoad()
      const { code, message } = data;
      if (code) {
        if (code == 100336) {
          this.$alert(
            `<div class="alert-conter">
              <div class="title"><span style="color: #F39217"><i class="el-icon-info"></i></span>ERP客户内码及编码不能为空！</div>
              <div class="assistant">
                <div class="assistant-title">解决办法如下:</div>
                <p>1. 请先前往您的ERP系统，为此客户建立采销关系 (首营资质)</p>
                <p>2. 如ERP中已建档，请刷新页面或联系平台ERP实施售后解决!</p>
              </div>
            </div>`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '已了解',
            customClass: 'pop-up-alert'
          });
        } else if (code == 100327) {
          this.$common.n_success(message);
          this.$router.go(-1) 
        } else {
          this.$message.error(message);
        }
      } else {
        this.$common.n_success('状态已更新！')
        this.$router.go(-1)
      }
    },
    beforeDestroy() {
      clearInterval(this.intervalTime)
    }
  }
};
</script>
<style lang="scss">
  //弹窗样式
  .pop-up-alert {
    .el-message-box__header {
      border-bottom: 1px solid #DCDEE0;
    }
    .alert-conter {
      .title {
        i {
          padding-right: 6px;
        }
        font-size: 14px;
        font-weight: bold;
        color: #323233;
      }
      .assistant {
        margin-top: 12px;
        padding-left: 20px;
        .assistant-title {
          font-weight: bold;
          color: #646566;
          margin-bottom: 8px;
        }
        p {
          color: #646566;
        }
      }
    }
    .el-button {
      background-color: #1AAC75;
      border-color: #1AAC75
    }
  }
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
