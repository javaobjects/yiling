<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="header-bar"><div class="sign"></div>基本信息：</div>
      <div class="container">
        <div class="item-text">
          <span>患者姓名：</span>
          {{ data.patientName }}
        </div>
        <div class="item-text">
          <span>手机号：</span>
          {{ data.mobile }}
        </div>
        <div class="item-text">
          <span>性别：</span>
          {{ data.gender }}
        </div>
        <div class="item-text">
          <span>年龄：</span>
          {{ data.age }}
        </div>
        <div class="item-text">
          <span>疾病史：</span>
          {{ data.diseaseTags }}
        </div>
        <div class="item-text">
          <span>用药：</span>
          {{ data.medicationTags }}
        </div>
      </div>
      <div v-if="data.certificateState == 2">
        <div class="header-bar"><div class="sign"></div>提审信息：</div>
        <div class="container">
          <el-row>
            <el-col :span="8">
              <div class="item-text">
                <span>提审时间：{{ data.arraignmentTime | formatDate }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="item-text">
                <span>上传人：{{ data.hospitalName }} - {{ data.doctorName }} - [ID:{{ data.doctorId }}]</span>
              </div>
            </el-col>
          </el-row>
          <div class="col-img" style="text-align: left">
            <el-image 
              class="img" 
              v-for="(item, index) in data.picture" 
              :key="index" 
              :src="item"
              @click="seeClick(item)">
            </el-image>
          </div>
        </div>
        <div class="header-bar"><div class="sign"></div>审核：</div>
        <div class="container">
          <el-form 
            :model="form" 
            :rules="rules" 
            ref="dataForm" 
            label-width="100px" 
            class="demo-ruleForm">
            <el-form-item label="审核结果：" prop="checkResult">
              <el-radio-group v-model="form.checkResult">
                <el-radio :label="1">通过</el-radio>
                <el-radio :label="2">驳回</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="驳回原因：" prop="rejectReason" v-if="form.checkResult == 2">
              <el-input
                type="textarea"
                maxlength="100"  
                show-word-limit
                style="max-width: 90%"
                :autosize="{ minRows: 4, maxRows: 5}"
                placeholder="请输入驳回原因"
                v-model.trim="form.rejectReason">
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div v-if="data.logList && data.logList.length > 0">
        <div class="header-bar"><div class="sign"></div>历史审核记录：</div>
        <div class="container">
          <el-row class="container-row" v-for="(item, index) in data.logList" :key="index">
            <el-col :span="8">
              <p>提审时间：{{ item.arraignmentTime | formatDate }}</p>
              <p>审核人：{{ item.createUserName }}</p>
            </el-col>
            <el-col :span="8">
              <p>审核时间：{{ item.auditTime | formatDate }}</p>
              <p>审核结果：{{ item.checkResult == 1 ? '通过' : '驳回' }}
                <span v-if="item.checkResult == 2">( {{ item.rejectReason }} )</span>
              </p>
            </el-col>
            <el-col :span="8">
              <div class="col-img">
                <el-image 
                  class="img" 
                  v-for="(item2, index2) in item.picture" 
                  :key="index2" 
                  :src="item2"
                  @click="seeClick(item2)">
                </el-image>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <!-- 图片放大弹窗 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
    <!-- 底部 -->
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" v-if="data.certificateState == 2" @click="preservationClick">提交</yl-button>
    </div>
  </div>
</template>
<script>
import { queryActivityDocPatientDetail, activityDocPatientAudit } from '@/subject/admin/api/cmp_api/education'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  name: 'ViewPatientsEstablish',
  components: {
    ElImageViewer
  },
  data() {
    return {
      form: {
        checkResult: '',
        rejectReason: '',
        id: ''
      },
      data: {
        patientName: '',
        mobile: '',
        gender: '',
        diseaseTags: '',
        medicationTags: '',
        arraignmentTime: '',
        hospitalName: '',
        doctorName: '',
        doctorId: '',
        certificateState: '',
        logList: [],
        picture: []
      },
      rules: {
        checkResult: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
        rejectReason: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }]
      },
      showViewer: false,
      imageList: []
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.getList(query.id);
      this.form.id = query.id
    }
  },
  methods: {
    async getList(val) {
      let data = await queryActivityDocPatientDetail(val)
      if (data) {
        this.data = data
      }
    },
    //点击提交
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await activityDocPatientAudit(
            form.id,
            form.checkResult,
            form.checkResult == 1 ? '' : form.rejectReason
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('提交成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    //点击图片
    seeClick(val) {
      this.imageList.push(val)
      this.showViewer = true;
    },
    //关闭图片弹窗
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