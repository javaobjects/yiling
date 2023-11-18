<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :model="form" :rules="rules" ref="dataForm" label-width="80px" class="demo-ruleForm">
        <div class="c-box">
          <el-form-item label="文献">
            <div>
              <el-tag
                closable 
                type="info"
                class="item-el-tag"
                v-for="(item,index) in diseaseData" 
                :key="index"
                @close="diseaseClose(item,index)"> 
                {{ item.title }} 
              </el-tag>
            </div>
            <span class="add-span" @click="addLiterature">添加</span>
          </el-form-item>
          <el-form-item label="上传附件">
            <yl-upload-file 
              class="upload-file" 
              :action="info.action" 
              :oss-key="'questionReplyResourcePicture'" 
              :file-type="'pdf'" 
              @onSuccess="onSuccess">
            </yl-upload-file>
            <a class="upload-file-url" :href="fileData.url" target="_blank">{{ fileData.name }}</a>
          </el-form-item>
          <div>
            <el-form-item label="回复内容" prop="content">
              <wang-editor
                :height="height" 
                :content="content" 
                :extral-data="{type: 'richTextEditorFile'}" 
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" @click="preservationClick()">保存</yl-button>
    </div>
    <!-- 弹窗 -->
    <dialog-popup
      :show.sync="showDialog" 
      :dialog-type="dialogType" 
      :dialog-title="dialogTitle" 
      :data-list="dataList"
      @addCommodity="addCommodity"
      @determine="determine"
      v-if="showDialog">
    </dialog-popup>
  </div>
</template>

<script>
import { replyAdd } from '@/subject/admin/api/content_api/question_handling'
import { wangEditor, ylUploadFile } from '@/subject/admin/components'
import dialogPopup from '../../question_list/components/dialogPopup'
export default {
  name: 'Reply',
  components: {
    wangEditor, dialogPopup, ylUploadFile
  },
  data() {
    return {
      height: 600,
      form: {
        content: ''
      },
      rules: {
        content: [{ required: true, message: '请输入回复内容', trigger: 'blur' }]
      },
      //展示在本界面的文献资料
      diseaseData: [], 
      //以选取的文献
      dataList: [],
      showDialog: false,
      //回复ID
      replyID: '', 
      // 弹窗类型 1 关联文献资料 2 关联药品
      dialogType: 1, 
      dialogTitle: '文献资料',
      info: {
        action: '/system/api/v1/file/upload',
        extralData: { type: 'questionReplyResourcePicture'}
      },
      documentFileUrl: {
        fileName: '',
        replyFileKey: ''
      },
      //上传附件成功后的数据
      fileData: {
        name: '',
        url: ''
      },
      content: ''
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query && query.id != '') {
      this.replyID = query.id;
    }
  },
  methods: {
    //点击确定
    determine() {
      this.showDialog = false
    },
    // 文献弹窗返回的数据
    addCommodity(row) {
      this.diseaseData.push(row)
    },
    // 点击添加文献
    addLiterature() {
      this.dataList = this.diseaseData;
      this.showDialog = true;
    },
    // 删除文献
    diseaseClose(row,index) {
      this.diseaseData.splice(index,1)
    },
    // 上传附件 返回
    onSuccess(data) {
      if (data.key) {
        this.$common.n_success('上传成功！');
        this.documentFileUrl = {
          fileName: data.name,
          replyFileKey: data.key
        };
        this.fileData = data;
      }
    },
    // 点击保存
    async preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let literatureID = [];
          this.diseaseData.forEach(e => {
            literatureID.push(e.id)
          });
          this.$common.showLoad();
          let data = await replyAdd(
            this.replyID,
            this.form.content,
            literatureID,
            this.documentFileUrl && this.documentFileUrl.replyFileKey !='' ? [this.documentFileUrl] : []
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('回复成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>