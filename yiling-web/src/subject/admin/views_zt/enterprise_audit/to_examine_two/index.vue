<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="common-box" >
        <div class="header-bar">
          <div class="sign"></div>基本信息
        </div>
        <div class="examine-row">
          <el-row>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>任务编号：{{ query.id }}</p>
                <p>申请时间：{{ query.createTime | formatDate }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>审核类型：{{ query.authType == 1 ? '首次认证 ' : (query.authType == 2 ? '资质更新' : '驳回后再次认证') }}</p>
                <p>数据来源：{{ query.source | dictLabel(sourceType) }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>企业信息
        </div>
        <div class="examine-row">
          <div class="examine-row-title">{{ query.originEnterpriseInfo.name }}</div>
          <el-row >
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业类型：{{ query.updateEnterpriseInfo.type | dictLabel(companyType) }}</p>
                <p>联系人姓名：{{ query.originEnterpriseInfo.contactor }}</p>
                <p>统一社会信用代码/执业许可证号：{{ query.originEnterpriseInfo.licenseNumber }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="examine-row-conter">
                <p>企业ID：{{ query.updateEnterpriseInfo.eid }}</p>
                <p>联系人手机号：{{ query.originEnterpriseInfo.contactorPhone }}</p>
                <p>企业地址：{{ query.originEnterpriseInfo.provinceName }}{{ query.originEnterpriseInfo.cityName }}{{ query.originEnterpriseInfo.regionName }} {{ query.originEnterpriseInfo.address }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 资质信息 -->
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>资质信息
        </div>
        <div class="examine-row">
          <div class="qualification-box">
            <ul>
              <li v-for="(item, index) in query.originEnterpriseInfo.certificateVOList" :key="index">
                <el-image class="qualification-box-img" @click="imgClick(item)" :src="item.fileUrl">
                  <div slot="error" class="image-slot-imgs">
                    <p>未上传...</p>
                  </div>
                </el-image>
                <div class="qualification-box-conter" v-if="item.periodRequired">
                  <p>{{ item.name }}</p>
                  <p><span class="font-light-color">资质有效期：</span>
                  <span v-if="item.longEffective == 0">
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - {{ item.periodEnd | formatDate('yyyy-MM-dd') }}
                  </span>
                  <span v-else>
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - 长期有效
                  </span>
                  </p>
                </div>
                 <div class="qualification-box-conter" v-else>
                  <p>{{ item.name }}</p>
                 </div>
              </li>
            </ul> 
          </div>
        </div>
      </div>
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>更新的资质信息
        </div>
        <div class="examine-row-conter">
          <p v-if="query.updateEnterpriseInfo.name || query.updateEnterpriseInfo.name!= ''">企业名称更新为：{{ query.updateEnterpriseInfo.name }}</p>
          <p v-if="query.updateEnterpriseInfo.licenseNumber || query.updateEnterpriseInfo.licenseNumber!= ''">统一社会信用代码/执业许可证号更新为：{{ query.updateEnterpriseInfo.licenseNumber }}</p>
          <p v-if="query.updateEnterpriseInfo.regionName || query.updateEnterpriseInfo.regionName!= ''">企业地址更新为：{{ query.updateEnterpriseInfo.provinceName }}{{ query.updateEnterpriseInfo.cityName }}{{ query.updateEnterpriseInfo.regionName }} {{ query.updateEnterpriseInfo.address }}</p>
        </div>
        <div class="examine-row">
          <div class="qualification-box">
            <ul>
              <li v-for="(item, index) in query.updateEnterpriseInfo.certificateVOList" :key="index">
                <el-image class="qualification-box-img" @click="imgClick(item)" :src="item.fileUrl">
                  <div slot="error" class="image-slot-imgs">
                    <p>未上传...</p>
                  </div>
                </el-image>
                <div class="qualification-box-conter" v-if="item.periodRequired">
                  <p>{{ item.name }}</p>
                  <p><span class="font-light-color">资质有效期：</span>
                  <!-- {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - {{ item.periodEnd | formatDate('yyyy-MM-dd') }} -->
                   <span v-if="item.longEffective == 0">
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - {{ item.periodEnd | formatDate('yyyy-MM-dd') }}
                  </span>
                  <span v-else>
                    {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - 长期有效
                  </span>
                  </p>
                </div>
                <div class="qualification-box-conter" v-else>
                  <p>{{ item.name }}</p>
                </div>
              </li>
            </ul> 
          </div>
        </div>
      </div>
      <!-- 审核 -->
      <div class="common-box">
        <div class="header-bar mar-b-16">
          <div class="sign"></div>审核
        </div>
        <div class="examine-row">
          <el-form :model="form" label-position="left" :rules="rules" ref="dataForm" label-width="80px" class="demo-ruleForm">
            <el-form-item label="审核" prop="authStatus">
              <el-radio-group v-model="form.authStatus">
                <el-radio :label="2">通过</el-radio>
                <el-radio :label="3">不通过</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- <el-form-item v-show="form.authStatus == '' || form.authStatus == 2" label="通过原因">
              <el-input type="textarea"
                :rows="3"
                placeholder="请输入通过原因"
                v-model="form.authRejectReason"
                maxlength="200"
                show-word-limit="true"
                >
              </el-input>
            </el-form-item> -->
            <el-form-item v-show="form.authStatus == 3" label="驳回原因" prop="authRejectReason">
              <el-input type="textarea"
                :rows="3"
                placeholder="请输入驳回原因"
                maxlength="200"
                v-model="form.authRejectReason">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { getCertificateUpdate, updateAuth } from '@/subject/admin/api/zt_api/enterprise_audit';
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
import { enterpriseType, enterpriseSource } from '@/subject/admin/utils/busi';
export default {
  components: {
    ElImageViewer
  },
  computed: {
    companyType() {
      return enterpriseType()
    },
    sourceType() {
      return enterpriseSource()
    }
  },
  data() {
    return {
      // 头部导航
      query: {
        originEnterpriseInfo: {}, //原企业信息
        updateEnterpriseInfo: {} // 更新后的企业信息（为空字段则表示未更新）
      },
      showViewer: false, //图片放大是否显示
      imageList: [],
      form: {
        authStatus: 2,
        authRejectReason: ''
      },
      rules: {
        authStatus: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
        authRejectReason: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    this.query.eid = this.$route.params.eid
    if (this.query.eid) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
     let data = await getCertificateUpdate(
       this.query.eid
     )
      if (data != undefined) {
        this.query = data;
      }
    },
    // 点击图片
    imgClick(val) {
      if (val.fileUrl != '') {
        this.imageList = [val.fileUrl];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 保存
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        let form = this.form;
        let data = null;
        if (form.authStatus == 2) {
          this.$common.showLoad();
          data = await updateAuth(
            form.authRejectReason,
            form.authStatus,
            this.query.updateEnterpriseInfo.eid
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          } 
        } else {
          if (valid) {
            this.$common.showLoad();
            data = await updateAuth(
              form.authRejectReason,
              form.authStatus,
              this.query.updateEnterpriseInfo.eid
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            } 
          } else {
            return false
          }
        }
      })
    }
  
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
.qualification-box ul li ::v-deep .el-image img{
    width: auto !important;
    height: 100% !important;
  }
</style>
