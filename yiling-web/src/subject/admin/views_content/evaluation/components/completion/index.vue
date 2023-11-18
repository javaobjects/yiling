<template>
  <!-- 填空题 -->
  <div class="completion">
    <el-form
      :model="completionForm"
      :rules="completionRules"
      class="demo-ruleForm"
      label-width="120px"
      ref="completionForms">
      <div class="c-box">
        <el-row>
          <el-col :span="12">
            <el-form-item label="题干" prop="questionTopic">
              <el-input
                type="textarea"
                maxlength="50" 
                show-word-limit
                :autosize="{ minRows: 2, maxRows: 3}"
                placeholder="请输入题干"
                v-model.trim="completionForm.questionTopic">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="ifBlank">
              <el-radio-group v-model="completionForm.ifBlank">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="填空类型">
              <el-radio-group v-model="completionForm.blank.blankType">
                <el-radio :label="1">文本</el-radio>
                <el-radio :label="2">数字</el-radio>
                <el-radio :label="3">数字型（区间）</el-radio>
                <el-radio :label="4">日期</el-radio>
                <el-radio :label="5">上传图片</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="">
          <el-input
            v-if="completionForm.blank.blankType == 1"
            type="textarea"
            maxlength="500" 
            show-word-limit
            :disabled="true"
            :autosize="{ minRows: 2, maxRows: 3}"
            placeholder="请输入"
            v-model.trim="test">
          </el-input>
          <el-input
            v-if="completionForm.blank.blankType == 2"
            type="input"
            :disabled="true"
            placeholder="请输入"
            v-model.trim="test">
          </el-input>
          <el-row v-if="completionForm.blank.blankType == 3">
            <el-col :span="4">
              <el-input
                type="input"
                :disabled="true"
                placeholder="请输"
                v-model.trim="test">
              </el-input>
            </el-col>
            <el-col :span="1" style="text-align: center">
              -
            </el-col>
            <el-col :span="4">
              <el-input
                type="input"
                :disabled="true"
                placeholder="请输"
                v-model.trim="test">
              </el-input>
            </el-col>
          </el-row>
          <el-date-picker
            v-if="completionForm.blank.blankType == 4"
            v-model="test"
            :disabled="true"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
          <div v-if="completionForm.blank.blankType == 5">
            <div class="size-border">
              <i class="el-icon-plus"></i>
            </div>
            <div class="size-color">支持jpg，png格式，最多可上传6张</div>
          </div>
        </el-form-item>
        <el-row v-if="completionForm.blank.blankType == 2 || completionForm.blank.blankType == 3">
          <el-col :span="6">
            <el-form-item label="是否可输入小数">
              <el-radio-group v-model="completionForm.blank.ifDecimal">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-form-item label="">
              <el-checkbox 
                v-model="completionForm.blank.ifUnit" 
                :true-label="1" 
                :false-label="0">
                有单位
              </el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="10" v-if="completionForm.blank.ifUnit == 1">
            <el-form-item label="" prop="blank.unit">
              <el-input
                type="input"
                placeholder="请输单位名称"
                v-model.trim="completionForm.blank.unit">
              </el-input>
              <span></span>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="completionForm.blank.ifUnit == 1">
            <p class="size-color">如：mmHg，mmol/L，%，次/分钟</p>
          </el-col>
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
      completionForm: {
        questionTopic: '',
        ifBlank: 1,
        id: '',
        blank: {
          blankType: 1,
          ifDecimal: 1,
          ifUnit: 0,
          unit: ''
        }
      },
      test: '',
      completionRules: {
        questionTopic: [{ required: true, message: '请输入题干', trigger: 'blur' }],
        ifBlank: [{ required: true, message: '请选择是否必填', trigger: 'change' }],
        blankType: [{ required: true, message: '请选择填空类型', trigger: 'change' }],
        blank: {
          unit: [{ required: true, message: '请输入单位', trigger: 'blur' }]
        }
        
      }
    }
  },
  mounted() {
    if (JSON.stringify(this.objectData) !== '{}') {
      this.completionForm = this.objectData
    }
  },
  methods: {
    preservation() {
      this.$refs['completionForms'].validate( async(valid) => {
        if (valid) {
          let form = this.completionForm;
          if (JSON.stringify(this.objectData) === '{}') {
            this.$common.showLoad();
            let data = await addHealthEvaluateQuestion(
              this.healthEvaluateId,
              [
                {
                  ifBlank: form.ifBlank,
                  questionTopic: form.questionTopic,
                  questionType: 2,
                  blank: form.blank
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
                questionTopic: form.questionTopic,
                questionType: 2,
                blank: form.blank
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