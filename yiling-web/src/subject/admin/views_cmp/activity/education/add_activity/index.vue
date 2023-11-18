<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="80px" 
          class="demo-ruleForm">
          <el-form-item label="活动名称" prop="activityName">
            <el-input v-model.trim="form.activityName" maxlength="20" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="时间日期" prop="time">
            <el-date-picker 
              v-model="form.time" 
              type="datetimerange" 
              format="yyyy-MM-dd HH:mm:ss" 
              value-format="yyyy-MM-dd HH:mm:ss" 
              range-separator="至" 
              start-placeholder="开始日期" 
              end-placeholder="结束日期" 
              :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="活动描述">
            <el-input
              type="textarea"
              maxlength="200" 
              show-word-limit
              style="max-width: 60%"
              :autosize="{ minRows: 4, maxRows: 6}"
              placeholder="请输入活动描述"
              v-model="form.activityDesc">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
  </div>
</template>
<script>
import { saveActivity, queryActivityById } from '@/subject/admin/api/cmp_api/education'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'AddActivity',
  data() {
    return {
      form: {
        activityName: '',
        time: [],
        activityDesc: '',
        id: ''
      },
      rules: {
        activityName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        time: [{ required: true, message: '请设置投放时间', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id != '0') {
      this.form.id = query.id;
      this.getData()
    }
  },
  methods: {
    // 获取数据
    async getData() {
      let data = await queryActivityById(this.form.id)
      if (data) {
        this.form = {
          activityName: data.activityName,
          time: [formatDate(data.beginTime), formatDate(data.endTime)],
          activityDesc: data.activityDesc,
          id: data.id
        }
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveActivity(
            form.activityDesc,
            form.activityName,
            form.time && form.time.length > 0 ? form.time[0] : '',
            form.time && form.time.length > 1 ? form.time[1] : '',
            form.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
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