<template>
  <div class="app-container">
    <div class="app-container-content hot-bottome-bar">
      <div class="container">
        <div class="c-box">
          <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
            <el-form-item label="热词名称" prop="name">
              <el-input v-model="form.name" maxlength="10" show-word-limit placeholder="请输入热词名称"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.state">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">停用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="投放时间" prop="time">
              <el-date-picker v-model="form.time" type="daterange" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" style="width: 100px;" placeholder="排序" @input="e => (form.sort = checkInput(e))"></el-input>
              <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
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
import { saveHotWords, getHotWordDetail, updateHotWords } from '@/subject/admin/api/pop'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'HotWordEdit',
  components: {
  },
  computed: {
  },
  data() {
    return {
      form: {
        name: '',
        state: 1,
        time: [],
        sort: ''
      },
      show: false,
      rules: {
        name: [{ required: true, message: '请输入热词名称', trigger: 'blur' }],
        state: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        time: [{ required: true, message: '请设置投放时间', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      },
      // 已选择商品加载
      loading1: false
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
      let data = await getHotWordDetail(this.query.id)
      this.$common.hideLoad()
      if (data) {
        let time = []
        time = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.endTime, 'yyyy-MM-dd')]
        data.time = time
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
            data = await updateHotWords(
              this.query.id,
              form.name,
              form.state,
              form.time[0],
              form.time[1],
              Number(form.sort)
            )
          } else {
            data = await saveHotWords(
              form.name,
              form.state,
              form.time[0],
              form.time[1],
              Number(form.sort)
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
