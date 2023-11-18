<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="权益名称" prop="name">
            <el-input v-model="form.name" maxlength="5" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="权益说明" prop="description">
            <el-input v-model="form.description" maxlength="6" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="上传图标" prop="icon">
            <yl-upload
              :default-url="form.icon"
              :extral-data="{type: 'memberEquityPicture'}"
              @onSuccess="onSuccess"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-switch 
              v-model="form.status" 
              :active-value="1"
              :inactive-value="0">
            </el-switch>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="save">保存</yl-button>
    </div>
  </div>
</template>

<script>
import { createEquity, updateEquity, getEquity } from '@/subject/admin/api/b2b_api/membership'
import { ylUpload } from '@/subject/admin/components'

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
        name: '',
        description: '',
        icon: '',
        status: 1,
        id: ''
      },
      show: false,
      rules: {
        name: [{ required: true, message: '请输入权益名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入权益说明', trigger: 'blur' }],
        icon: [{ required: true, message: '请上传图标', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let query = this.$route.params
    if (query.id) {
      this.form.id = query.id
      this.getData()
    }
  },
  methods: {
    async getData() {
      let data = await getEquity(this.form.id)
      if (data) {
        this.form = data
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
            data = await updateEquity(
              this.query.id,
              form.name,
              form.description,
              form.icon,
              form.status
            )
          } else {
            data = await createEquity(
              form.name,
              form.description,
              form.icon,
              form.status
            )
          }
          this.$common.hideLoad()
          if (data !== undefined) {
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
        this.form.icon = data.url
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>
