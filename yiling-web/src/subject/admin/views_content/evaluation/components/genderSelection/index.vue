<template>
  <!-- 性别题 -->
  <div class="singleChoice">
    <el-form
      :model="genderSelectionForm"
      :rules="genderSelectionRules"
      class="demo-ruleForm"
      label-width="100px"
      ref="genderSelectionForms">
      <div class="c-box">
        <el-row>
          <!-- <el-col :span="12">
            <el-form-item label="是否计分" prop="ifScore">
              <el-radio-group v-model="genderSelectionForm.ifScore">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="是否必填" prop="ifBlank">
              <el-radio-group v-model="genderSelectionForm.ifBlank">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="请选择性别">
            <el-radio-group v-model="test" disabled>
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
import { addHealthEvaluateQuestion, editQuestions } from '@/subject/admin/api/content_api/evaluation'
export default {
  props: {
    //量表id
    healthEvaluateId: {
      type: Number,
      default: 0
    },
    objectData: {
      type: Object,
      default: ()=> {}
    }
  },
  data() {
    return {
      genderSelectionForm: {
        ifBlank: 1,
        id: ''
      },
      genderSelectionRules: {
        ifBlank: [{ required: true, message: '请选择是否必填', trigger: 'change' }]
      },
      test: 3
    }
  },
  mounted() {
    if (JSON.stringify(this.objectData) !== '{}') {
      this.genderSelectionForm = this.objectData
    }
  },
  methods: {
    preservation() {
      this.$refs['genderSelectionForms'].validate( async(valid) => {
        if (valid) {
          let form = this.genderSelectionForm;
          if (JSON.stringify(this.objectData) === '{}') {
            this.$common.showLoad();
            let data = await addHealthEvaluateQuestion(
              this.healthEvaluateId,
              [
                {
                  ifBlank: form.ifBlank,
                  questionType: 5
                } 
              ]
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$emit('dialogPop')
            }
          } else {
            this.$common.showLoad();
            let data1 = await editQuestions(
              this.healthEvaluateId,
              {
                id: form.id,
                ifBlank: form.ifBlank,
                questionType: 5
              }
            )
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$emit('dialogPop')
            }
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