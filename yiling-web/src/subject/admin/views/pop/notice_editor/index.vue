<template>
  <div class="app-container">
    <div class="app-container-content container">
      <div class="edit-view">
        <el-form ref="dataForm" :rules="rules" :model="form" label-position="top">
          <el-row>
            <el-col :span="7">
              <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" type="textarea" :clearable="true" :maxlength="32" :minlength="3" :autosize="{ minRows: 3, maxRows: 6 }" placeholder="在这里输入标题" />
              </el-form-item>
              <el-form-item label="开始结束时间" prop="time">
                <el-date-picker v-model="form.time" type="daterange" format="yyyy/MM/dd" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="状态" prop="state">
                <el-radio-group v-model="form.state">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="17">
              <wang-editor style="padding-top: 20px;" :height="height" :content="content" :extral-data="{type: 'richTextEditorFile'}" :handle-content="handleContent" />
            </el-col>
          </el-row>
        </el-form>
      </div>

      <div class="bottom-view flex-row-center border-1px-t">
        <yl-button class="btn" plain @click="goBack">
          返回
        </yl-button>
        <yl-button class="btn mar-l-5" type="primary" @click="handleSave">
          保存
        </yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { saveNotice, getNoticeDetail } from '@/subject/admin/api/pop'
import { wangEditor } from '@/subject/admin/components'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'PopNoticeEdit',
  components: {
    wangEditor
  },
  data() {
    return {
      form: {
        time: [],
        state: 1
      },
      rules: {
        title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
        state: [{ required: true, message: '请选择状态', trigger: 'change' }],
        time: [{ required: true, message: '请选择开始结束时间', trigger: 'change' }]
      },
      content: '',
      height: 500
    }
  },
  computed: {
  },
  mounted() {
    this.queryId = ''
    let query = this.$route.params
    if (query && query.id && query.id !== 'none') {
      this.getRich(query.id)
      this.queryId = query.id
    }
    this.height = parseInt(window.innerHeight * 0.6)
  },
  methods: {
    // 获取富文本信息
    async getRich(id) {
      this.$common.showLoad()
      let data = await getNoticeDetail(id)
      this.$common.hideLoad()
      if (data) {
        if (data.startTime && data.endTime) {
          data.time = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.endTime, 'yyyy-MM-dd')]
        }
        this.content = data.content
        this.form = data
      }
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.$log(content)
      this.form.content = content
    },
    // 点击保存
    handleSave() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let info = this.form
          if (!info.content) {
            this.$common.warn('富文本内容不可为空')
            return
          }
          this.$common.showLoad()
          let data = await saveNotice(
            this.queryId,
            info.title,
            info.content,
            info.state,
            info.time.length ? info.time[0] : '',
            info.time.length > 1 ? info.time[1] : ''
          )
          this.$common.hideLoad()
          if (data) {
            this.$common.n_success('保存成功')
            this.goBack()
          }
        } else {
          return false
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
