<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="150px" 
          class="demo-ruleForm">
          <h4>基本信息：</h4>
          <el-form-item label="活动名称：" prop="activityName">
            <el-input 
              v-model.trim="form.activityName" 
              :disabled="type == 2" 
              maxlength="20" 
              show-word-limit 
              placeholder="请输入规则名称">
            </el-input>
          </el-form-item>
          <el-form-item label="活动时间：" prop="time">
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :disabled="type == 2">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="运营备注：" prop="activityRemark">
            <el-input
              type="textarea"
              maxlength="200"  
              show-word-limit
              style="max-width: 90%"
              :autosize="{ minRows: 4, maxRows: 5}"
              placeholder="请输入运营备注"
              v-model.trim="form.activityRemark"
              :disabled="type == 2">
            </el-input>
          </el-form-item>
          <el-form-item label="是否限制医生身份：" prop="restrictDocType">
             <el-radio-group v-model="form.restrictDocType" :disabled="type == 2">
                <el-radio :label="1">手动配置</el-radio>
             </el-radio-group>
          </el-form-item>
          <el-form-item label="是否限制用户身份：" prop="restrictUserType">
            <el-radio-group v-model="form.restrictUserType" :disabled="type == 2">
              <el-radio :label="1">平台新用户（没有注册过的）</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="患者是否需要审核：" prop="auditUserType">
            <el-radio-group v-model="form.auditUserType" :disabled="type == 2">
              <el-radio :label="1">需要审核</el-radio>
            </el-radio-group>
          </el-form-item>
          <h4>活动页配置：</h4>
          <el-form-item label="活动头图：" prop="activityHeadPicUrl">
            <yl-upload
              :default-url="form.activityHeadPicUrl"
              :extral-data="{type: 'hmcActivityPic'}"
              :oss-key="'hmcActivityPic'"
              @onSuccess="onSuccess"/>
            <p class="explain">建议尺寸：宽度为750像素，高度不限，支持.jpg、.png格式</p>
          </el-form-item>
          <el-form-item label="活动描述：" prop="content">
            <wang-editor
              :height="height"
              :content="content"
              :extral-data="{type: 'hmcActivityPic'}"
              :handle-content="handleContent"/>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservationClick" v-if="type != 2">提交</yl-button>
    </div>
  </div>    
</template>

<script>
import { wangEditor, ylUpload } from '@/subject/admin/components'
import { saveOrUpdateDocToPatient, activityQueryActivityById } from '@/subject/admin/api/cmp_api/education'
import { formatDate } from '@/subject/admin/utils';
export default {
  name: 'ActivityEstablish',
  components: {
    wangEditor,
    ylUpload
  },
  data() {
    return {
      height: 300,
      form: {
        activityName: '',
        time: [],
        activityRemark: '',
        restrictDocType: 1,
        restrictUserType: 1,
        auditUserType: 1,
        bannerImg: '',
        content: '',
        activityHeadPicUrl: '',
        activityHeadPic: '',
        id: ''
      },
      rules: {
        activityName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择生效时间', trigger: 'change' }],
        activityRemark: [{ required: true, message: '请输入运营备注', trigger: 'blur' }],
        restrictDocType: [{ required: true, message: '请选择是否限制医生身份', trigger: 'change' }],
        restrictUserType: [{ required: true, message: '请选择是否限制用户身份', trigger: 'change' }],
        auditUserType: [{ required: true, message: '请选择患者是否需要审核', trigger: 'change' }],
        content: [{ required: true, message: '请输入活动描述', trigger: 'blur' }],
        activityHeadPicUrl: [{ required: true, message: '请输入上传活动头图', trigger: 'blur' }]
      },
      content: '',
      type: 0
    }
  },
  mounted() {
    let query = this.$route.params;
    this.type = query.type
    if (query.id && query.id != '0') {
      this.getData(query.id)
    }
  },
  methods: {
    //编辑/查看 获取数据信息
    async getData(val) {
      let data = await activityQueryActivityById(
        val
      )
      if (data) {
        this.form = data;
        this.form.content = this.content = data.activityDesc;
        this.$nextTick(() => {
          this.$set(this.form, 'time', [formatDate(data.beginTime, 'yyyy-MM-dd hh:mm:ss') , formatDate(data.endTime, 'yyyy-MM-dd hh:mm:ss')]);
        });
      }
    },
    //点击底部保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveOrUpdateDocToPatient(
            form.activityName,
            form.time && form.time.length > 0 ? form.time[0] : '',
            form.time && form.time.length > 1 ? form.time[1] : '',
            form.activityRemark,
            form.restrictDocType,
            form.restrictUserType,
            form.auditUserType,
            form.activityHeadPic,
            form.content,
            form.id
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
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
     // 图片上传成功
    onSuccess(data) {
      if (data.key) {
        this.form.activityHeadPic = data.key
        this.form.activityHeadPicUrl = data.url
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
