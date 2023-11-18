<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>企业基本信息
        </div>
        <div class="examine-row">
          <el-row >
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业名称：{{ query.entName }}</p>
                <p>社会信用统一代码：{{ query.licenseNumber }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业类型：{{ query.type | dictLabel(companyType) }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>收款账户信息
        </div>
        <div class="examine-row">
          <el-row >
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业收款账户名称：{{ query.name }}</p>
                <p>开户行：{{ query.bankName }}</p>
                <p>支行信息：{{ query.subBankName }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>开户地：{{ query.provinceName }} {{ query.cityName }}</p>
                <p>银行账户：{{ query.account }}</p>
                <!-- <p>开户许可证：{{ query.accountOpeningPermitUrl || '--' }}</p> -->
                <!-- <p>开户许可证：
                  <img src="https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/dev/b2bReceiptAccount/2021/11/15/cddde9b2dd564cf58042fb934d60b826.png" alt="">
                </p> -->

              </div>
            </el-col>
          </el-row>
          <div class="examine-img">
            <el-row>
              <el-col :span="12">
                <span>开户许可证：</span>
                <el-image :src="query.accountOpeningPermitUrl" @click="scalePicSize(query.accountOpeningPermitUrl)">
                  <div slot="error" class="image-slots">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </el-col>
              <el-col :span="12">
                <span>其他证照：</span>
                <el-image :src="query.licenceUrl" @click="scalePicSize(query.licenceUrl)">
                  <div slot="error" class="image-slots">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </el-col>
            </el-row>
            
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>审核信息
        </div>
        <div class="examine-row">
          <p>审核状态：{{ query.status == 1 ? '待审核' : (query.status == 2 ? '审核成功' : '审核失败' ) }}</p>
          <p v-if="typeStatue == 1">审核操作：
            <el-radio-group v-model="authStatus">
              <el-radio :label="2">审核通过</el-radio>
              <el-radio :label="3">审核不通过</el-radio>
            </el-radio-group>
          </p>
          <div v-if="authStatus==3">
            <el-input type="textarea" :rows="3" maxlength="200" placeholder="请输入不通过原因" v-model="auditRemarks"></el-input>
          </div>
          <div v-if="query.status == 3 && typeStatue == 2">
            失败原因： {{ auditRemark }}
          </div>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick" v-if="typeStatue == 1">保存</yl-button>
    </div>
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { queryReceiptAccount, auditReceiptAccount } from '@/subject/admin/api/zt_api/enterprise_audit';
import { enterpriseType } from '@/subject/admin/utils/busi'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
export default {
  name: 'CollectionAccountToExamine',
  components: {
    ElImageViewer
  },
  computed: {
    companyType() {
      return enterpriseType()
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
          title: '企业审核'
        },
        {
          title: '企业收款账户列表'
        }
      ],
      query: {
        id: '',
        status: '',
        entName: '',
        type: '',
        licenseNumber: '',
        name: '',
        subBankName: '',
        bankName: '',
        account: '',
        accountOpeningPermitUrl: '',
        provinceName: '',
        cityName: '',
        licenceUrl: ''
      },
      authStatus: '',
      auditRemarks: '',
      auditRemark: '', //不通过原因
      typeStatue: 1,
      showViewer: false,
      imageList: []
    };
  },
  mounted() {
    let val = this.$route.params.id
    if (val) {
      this.query.id = JSON.parse(val).id;
      this.typeStatue = JSON.parse(val).data; //1代表是审核 2 代表查看
      this.getList()
    }
    
  },
  methods: {
    async getList() {
      let query = this.query;
      let data = await queryReceiptAccount(
        query.id
      )
      if (data != undefined) {
        this.query = {
          status: data.status,
          entName: data.entName,
          type: data.type,
          licenseNumber: data.licenseNumber,
          name: data.name,
          subBankName: data.subBankName,
          bankName: data.bankName,
          account: data.account,
          accountOpeningPermitUrl: data.accountOpeningPermitUrl,
          provinceName: data.provinceName,
          cityName: data.cityName,
          id: query.id,
          licenceUrl: data.licenceUrl
        };
        this.auditRemark = data.auditRemark;
      }
    },
    // 点击保存
    async saveClick() {
      if (this.authStatus == '') {
        this.$common.error('请进行审核操作')
      } else if (this.authStatus == 3 && this.auditRemarks == '') {
        this.$common.error('请输入驳回原因')
      } else {
        this.$common.showLoad();
        let data = await auditReceiptAccount(
          this.auditRemarks,
          this.query.id,
          this.authStatus
        )
        this.$common.hideLoad();
        if (data) {
          this.$common.alert('保存成功', r => {
            this.$router.go(-1)
          })
        }
      }
    },
     // 图片放大
    scalePicSize(val){
      if (val != '') {
        this.imageList = [val];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
};
</script>
<style lang="scss" scoped>
  @import "./index.scss";
  .examine-img ::v-deep .el-image{
    width: 200px;
    height: 159px;
    position: relative;
    border: 1px solid #eee;
    background: #FAFAFA;
    cursor: pointer;
    text-align: center;
  }
  .examine-img ::v-deep .el-image img {
    width: auto;
    height: 100%;
  }
  .examine-img ::v-deep .image-slots{
    width: 100%;
    height: 159px;
    line-height: 159px;
    font-size: 40px;
    color: #666;
    text-align: center;
  }
</style>