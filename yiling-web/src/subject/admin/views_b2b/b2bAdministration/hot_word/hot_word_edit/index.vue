<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="热词名称" prop="content">
            <el-input v-model.trim="form.content" maxlength="10" show-word-limit placeholder="请输入热词名称"></el-input>
          </el-form-item>
          <el-form-item label="有效日期" prop="time">
            <el-date-picker
              v-model="form.time"
              type="daterange"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 100px;" placeholder="排序"
              @input="e => (form.sort = checkInput(e))"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
          <el-form-item label="状态" prop="useStatus">
            <el-radio-group v-model="form.useStatus">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
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
import { hotWordsSave, bannerQuery } from '@/subject/admin/api/b2b_api/b2bAdministration.js'
import { formatDate } from '@/subject/admin/utils'

export default {
  name: 'B2bHotWordEdit',
  components: {
  },
  computed: {
  },
  data() {
    return {
      form: { 
        content: '',
        id: '',
        sort: '',
        time: '',
        useStatus: 1
      },
      show: false,
      rules: {
        content: [{ required: true, message: '请输入热词名称', trigger: 'blur' }],
        useStatus: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        time: [{ required: true, message: '请设置投放时间', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id) {
      this.form.id = query.id;
      this.getData()
    }
  },
  methods: {
    async getData() {     
      let data = await bannerQuery(this.form.id)
      if (data) {
        let times = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.stopTime, 'yyyy-MM-dd')]
        this.form = {
          content: data.content,
          id: data.id,
          sort: data.sort,
          time: times,
          useStatus: data.useStatus
        }
      }
    },
    save() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await hotWordsSave(
            form.content,
            form.id,
            form.sort,
            form.time[0],
            form.time[1],
            form.useStatus
          )
          this.$common.hideLoad();
          if (data != undefined) {
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
  @import "./index.scss";
</style>
