<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="container">
        <div class="c-box">
          <el-form :model="form" :label-position="labelPosition" :rules="rules" ref="dataForm" label-width="80px" class="demo-ruleForm">
            <el-form-item label="标题名称" prop="articleTitle">
              <el-input 
                type="textarea" 
                class="elInput" 
                :rows="3" 
                placeholder="请输入标题名称" 
                show-word-limit 
                v-model="form.articleTitle" 
                maxlength="50">
              </el-input>
            </el-form-item>
            <el-form-item label="文章描述" prop="articleDesc">
              <el-input type="textarea" class="elInput" :rows="3" placeholder="请输入内容" show-word-limit v-model="form.articleDesc" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="文章状态" prop="articleStatus">
              <el-switch v-model="form.articleStatus" :active-value="1" :inactive-value="2"></el-switch>
            </el-form-item>
          </el-form>
          <!-- 文章内容 -->
          <div class="acticle-conter">
            <wang-editor :height="height" :content="content" :extral-data="{type: 'richTextEditorFile'}" :handle-content="handleContent" />
          </div>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button plain @click="previewClick">预览</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
    <preview-dialog title="预览" :visible.sync="showPreviewDialog" :content="form.content"></preview-dialog>
  </div>
</template>
<script>
import { wangEditor } from '@/subject/admin/components'
import { saveOrUpdate, queryById } from '@/subject/admin/api/zt_api/article'
import PreviewDialog from '@/common/components/PreviewDialog'
export default {
  name: 'ArticleListDetail',
  components: {
    wangEditor, PreviewDialog
  },
  data() {
    return {
      form: {
        articleTitle: '',
        articleDesc: '',
        articleStatus: 1,
        id: '',
        content: ''
      },
      rules: {
        articleTitle: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        articleDesc: [{ required: true, message: '请输入文章描述', trigger: 'blur' }]
      },
      content: '',
      height: 600,
      labelPosition: 'left',
      showPreviewDialog: false
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id !='0') {
      this.getData(query.id)
    }
  },
  methods: {
    // 获取数据信息
    async getData(val) {
      let data = await queryById(val);
      if (data !== undefined) {
        this.form = {
          articleTitle: data.articleTitle,
          articleDesc: data.articleDesc,
          articleStatus: data.articleStatus,
          id: data.id
        }
        this.content = data.articleContent;
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let query = this.form;
          if (query.content == '') {
            this.$common.warn('请输入文章内容')
          } else {
            this.$common.showLoad();
            let data = await saveOrUpdate(
              query.content,
              query.articleDesc,
              query.articleStatus,
              query.articleTitle,
              query.id
            );
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            } 
          }
        }
      })
    },
     // 富文本编辑器回调
    handleContent(content, editor) {
      this.$log(content);
      this.form.content = content
    },
    // 预览
    previewClick() {
      this.showPreviewDialog = true
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>