<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <div class="common-box">
        <div class="search-box">
          <el-form
            :model="form" 
            :rules="rules" 
            ref="dataForm" 
            label-width="100px" 
            class="demo-ruleForm">
            <el-form-item label="积分说明：" prop="content">
              <wang-editor
                :height="height"
                :content="content"
                :extral-data="{type: 'richTextEditorFile'}"
                :handle-content="handleContent"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" @click="preservationClick">提交</yl-button>
    </div>
  </div>
</template>
<script>
import { wangEditor } from '@/subject/admin/components'
import { ructionConfigGet, saveConfig } from '@/subject/admin/api/b2b_api/integral_record'
export default {
  name: 'IntegralConfiguration',
  components: {
    wangEditor
  },
  data() {
    return {
      height: 600,
      content: '',
      form: {
        id: '',
        content: ''
      },
      rules: {
        content: [{ required: true, message: '请输入积分说明', trigger: 'blur' }]
      }
    }
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      let data = await ructionConfigGet()
      if (data) {
        this.form = data;
        this.content = data.content;
      }
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
    //点击提交
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          let data = await saveConfig(
            form.content,
            form.id
          )
          if (data !== undefined) {
            this.$common.n_success('保存成功')
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>