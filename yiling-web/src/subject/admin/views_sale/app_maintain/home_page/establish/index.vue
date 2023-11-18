<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="100px" 
          class="demo-ruleForm">
          <el-form-item label="标题名称：" prop="title">
            <el-input v-model.trim="form.title" maxlength="50" show-word-limit :disabled="type == 3"></el-input>
          </el-form-item>
          <el-form-item label="配置链接：">
            <el-input v-model.trim="form.link" :disabled="type == 3"></el-input>
          </el-form-item>
          <el-form-item label="发布状态：" prop="status">
            <el-radio-group v-model="form.status" :disabled="type == 3">
              <el-radio :label="2">立即发布</el-radio>
              <el-radio :label="1">暂不发布</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="上传图片：" prop="picture" v-if="type != 3">
            <div class="img-conter">
              <div class="file-image">
                <yl-file-list
                  class="file-images"
                  width="144px"
                  height="256px"
                  :file-list.sync="fileList">
                </yl-file-list>
                <span class="test" v-if="fileList && fileList.length < 1">图片展示区</span>
              </div>
              <div class="upload-left">
                <span v-show="false">{{ form.picture }}</span>
                <yl-upload-file
                  :file-type="'png,jpg,gif'"
                  :oss-key="'b2bOpenPosition'"
                  :max-size="2048"
                  :show-upload-button="true"
                  :upload-btn-text="'点击上传图片'"
                  @onSuccess="onSuccess"
                >
                </yl-upload-file>
              </div>
              <div class="describe">
                <div>上传图片规则：</div>
                <p>1、建议比例：16 : 9</p>
                <p>2、建议尺寸：高·1334像素 X 宽·750像素</p>
                <p>3、格式限制：PNG/GPG/GIF</p>
                <p>3、质量限制：图片大小≤2M</p>
                <p><i class="el-icon-info"></i> 比例或尺寸不符合建议，将会导致图片变形或虚化</p>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick" v-if="type != 3">保存</yl-button>
    </div>
  </div>
</template>

<script>
import YlFileList from '@/common/components/CustomUpload/fileList'
import { ylUploadFile } from '@/subject/admin/components';
import { saveOpenPosition, homeById } from '@/subject/admin/api/views_sale/app_maintain'
export default {
  name: 'HomePageEdit',
  components: {
    YlFileList,
    ylUploadFile
  },
  data() {
    return {
      form: {
        id: '',
        title: '',
        link: '',
        picture: '',
        status: '',
        key: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题名称', trigger: 'blur' }],
        picture: [{ required: true, message: '请上传图片', trigger: 'blur' }],
        status: [{ required: true, message: '请选择发布状态', trigger: 'blur' }]
      },
      fileList: [],
      //1新增 2编辑 3查看
      type: 1
    }
  },
  watch: {
    fileList: {
      handler(newValue, oldValue) {
        if (newValue.length < 1) [
          this.form.picture = ''
        ]
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id != '0') {
      this.form.id = query.id;
      this.type = query.type;
      this.getData()
    }
  },
  methods: {
    //点击保存
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveOpenPosition(
            form.id,
            form.title,
            form.link,
            form.status,
            form.picture
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
          
        }
      })
    },
    async getData() {
      let data = await homeById(this.form.id)
      if (data) {
        this.form = data;
        this.fileList = [
          {
            type: 'image',
            url: data.pictureUrl,
            hideDel: this.type == 3
          }
        ]
      }
    },
    onSuccess(data) {
      if (data.key) {
        this.fileList = [
          {
            type: 'image',
            ...data
          }
        ]
        this.form.picture = data.key
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>