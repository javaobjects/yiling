<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="container">
        <div class="c-box">
          <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" maxlength="10" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="图片" prop="pic">
              <yl-upload :default-url="form.pic" :extral-data="{type: 'bannerPicture'}" @onSuccess="onSuccess" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="投放时间" prop="time">
              <el-date-picker v-model="form.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" style="width: 100px;" @input="e => (form.sort = checkInput(e))" placeholder="排序"></el-input>
              <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
            </el-form-item>
            <el-form-item label="链接类型">
              <el-radio-group v-model="form.linkType">
                <el-radio :label="2">链接</el-radio>
              </el-radio-group>
              <div>
                <el-input v-model="form.linkUrl" placeholder="请输入跳转链接"></el-input>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="save">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { saveBanner, getBannerDetail, updateBanner } from '@/subject/admin/api/pop'
import { ylUpload } from '@/subject/admin/components'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'PopBannerEdit',
  components: {
    ylUpload
  },
  computed: {
  },
  data() {
    return {
      form: {
        title: '',
        pic: '',
        status: 1,
        linkType: 2,
        linkUrl: '',
        time: [],
        fileKey: '',
        sort: '1'
      },
      show: false,
      rules: {
        title: [{ required: true, message: '请输入banner标题', trigger: 'blur' }],
        pic: [{ required: true, message: '请上传banner图片', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        time: [{ required: true, message: '请设置投放时间', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.query = this.$route.params
    if (this.query.id) {
      this.getData()
    }
  },
  methods: {
    async getData() {
      this.$common.showLoad()
      let data = await getBannerDetail(this.query.id)
      this.$common.hideLoad()
      if (data) {
        let time = []
        time = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.endTime, 'yyyy-MM-dd')]
        data.time = time
        this.form = data

        this.form.pic = data.fileInfo.fileUrl
        this.form.fileKey = data.fileInfo.fileKey
      }
    },
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form
          this.query = this.$route.params
          let data = null
          this.$common.showLoad()
          if (this.query.id) {
            data = await updateBanner(
              this.query.id,
              form.status,
              form.title,
              form.time[0],
              form.time[1],
              Number(form.sort),
              form.fileKey,
              form.linkType,
              form.linkUrl
            )
          } else {
            data = await saveBanner(
              form.status,
              form.title,
              form.time[0],
              form.time[1],
              Number(form.sort),
              form.fileKey,
              form.linkType,
              form.linkUrl
            )
          }
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        } else {
          return false
        }
      })
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.form.fileKey = data.key
        this.form.pic = data.url
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 200) {
        val = 200
      }
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
