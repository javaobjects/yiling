<template>
   <!-- BMI计算题 -->
  <div class="calculationProblem">
    <el-form
      :model="calculationProblemForm"
      :rules="calculationProblemRules"
      class="demo-ruleForm"
      label-width="100px"
      ref="calculationProblemForms">
      <div class="c-box">
        <el-row>
          <el-col :span="12">
            <el-form-item label="体重" >
              <el-input
                maxlength="10" 
                show-word-limit
                :disabled="true"
                v-model.trim="calculationProblemForm.kg">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身高">
              <el-input
                maxlength="10" 
                show-word-limit
                :disabled="true"
                v-model.trim="calculationProblemForm.cm">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否计分" prop="ifScore">
              <el-radio-group v-model="calculationProblemForm.ifScore">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="ifBlank">
              <el-radio-group v-model="calculationProblemForm.ifBlank">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="form-item-one" v-for="(item, index) in calculationProblemForm.bmiList" :key="index">
          <div :class="item.ifScoreSex == 1 && calculationProblemForm.ifScore == 1 ? 'form-div2' : 'form-div1'">
            <el-form-item 
            :label="'区间' + `${ index + 1 }`"
            class="item-one-form"
            :prop="'bmiList.' + index + '.rangeStartType'"
            :rules="{ required: true, message: '区间选择不能为空', trigger: 'blur'}">
              <el-select class="select-width" v-model="item.rangeStartType" placeholder="请选择">
                <el-option
                  v-for="item1 in sectionData"
                  :key="item1.value"
                  :label="item1.label"
                  :value="item1.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item 
              label="" 
              class="item-one-form item-one-input"
              :prop="'bmiList.' + index + '.rangeStart'"
              :rules="{ required: true, message: '区间值不能为空', trigger: 'blur'}">
              <el-input 
                class="el_input" 
                v-model="item.rangeStart"
                placeholder="请输入"
                @input="e => (item.rangeStart = checkInput(e))"/> 
            </el-form-item>
            <span>至</span>
            <el-form-item 
              label=""
              class="item-one-form item-one-input"
              :prop="'bmiList.' + index + '.rangeEndType'"
              :rules="{ required: true, message: '区间选择不能为空', trigger: 'blur'}">
              <el-select class="select-width" v-model="item.rangeEndType" placeholder="请选择">
                <el-option
                  v-for="item1 in sectionData"
                  :key="item1.value"
                  :label="item1.label"
                  :value="item1.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item 
              label="" 
              class="item-one-form item-one-input"
              :prop="'bmiList.' + index + '.rangeEnd'"
              :rules="{ required: true, message: '区间值不能为空', trigger: 'blur'}">
              <el-input 
                class="el_input" 
                v-model="item.rangeEnd"
                placeholder="请输入"
                @input="e => (item.rangeEnd = checkInput(e))"/> 
            </el-form-item>
            <div class="display" v-if="calculationProblemForm.ifScore == 1">
              <el-form-item label="" class="item-one-form item-one-input">
                <el-checkbox v-model="item.ifScoreSex" :true-label="1" :false-label="0">记分区分性别</el-checkbox>
              </el-form-item>
              <el-form-item
                v-if="item.ifScoreSex == 0"
                class="item-one-form item-two-input" 
                label="记分" 
                :prop="'bmiList.' + index + '.score'"
                :rules="{ required: true, message: '记分不能为空', trigger: 'blur'}">
                <el-input v-model.trim="item.score" class="el_input"></el-input>
              </el-form-item>
            </div>
          </div>
          <div class="form-div" v-if="item.ifScoreSex == 1 && calculationProblemForm.ifScore == 1">
            <el-form-item 
              label="记分(男)" 
              class="item-one-form item-two-input" 
              :prop="'bmiList.' + index + '.scoreMen'" 
              :rules="{ required: true, message: '记分(男)不能为空', trigger: 'blur'}">
              <el-input v-model.trim="item.scoreMen	" class="el_input"></el-input>
            </el-form-item>
            <el-form-item 
              label="记分(女)" 
              class="item-one-form item-two-input" 
              :prop="'bmiList.' + index + '.scoreWomen'" 
              :rules="{ required: true, message: '记分(女)不能为空', trigger: 'blur'}">
              <el-input v-model.trim="item.scoreWomen" class="el_input"></el-input>
            </el-form-item>
          </div>
          <span class="item-details" :class="item.ifScoreSex == 1 ? 'item-detail1' : 'item-detail2'" v-if="index !== 0" @click="detailsResultClick(index)"><i class="el-icon-remove-outline icon_style"></i></span>
        </div>
        <div class="add-button">
          <yl-button type="primary" @click="addClick" plain>添加选项</yl-button>
        </div>
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
      calculationProblemForm: {
        kg: '',
        cm: '',
        ifScore: 1,
        ifBlank: 1,
        id: '',
        bmiList: [
          {
            rangeStartType: '',
            rangeStart: '',
            rangeEndType: '',
            rangeEnd: '',
            ifScoreSex: 0,
            score: '',
            scoreMen: '',
            scoreWomen: ''
          }
        ]
      },
      calculationProblemRules: {
        kg: [{ required: true, message: '请输入体重', trigger: 'blur' }],
        cm: [{ required: true, message: '请输入身高', trigger: 'blur' }]
      },
      sectionData: [
        {
          value: 1,
          label: '小于'
        },
        {
          value: 2,
          label: '小于等于'
        },
        {
          value: 3,
          label: '等于'
        },
        {
          value: 4,
          label: '大于等于'
        },
        {
          value: 5,
          label: '大于'
        }
      ]
    }
  },
  mounted() {
     if (JSON.stringify(this.objectData) !== '{}') {
      this.calculationProblemForm = this.objectData
    }
  },
  methods: {
    //点击保存
    preservation() {
       this.$refs['calculationProblemForms'].validate( async(valid) => {
        if (valid) {
          let form = this.calculationProblemForm;
          if (JSON.stringify(this.objectData) === '{}') {
            this.$common.showLoad();
            let data = await addHealthEvaluateQuestion(
              this.healthEvaluateId,
              [
                {
                  ifBlank: form.ifBlank,
                  ifScore: form.ifScore,
                  questionType: 4,
                  bmiList: form.bmiList
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
                ifScore: form.ifScore,
                questionType: 4,
                bmiList: form.bmiList
              }
            )
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$emit('dialogPop')
            }
          }
        }
      })
    },
    // 验证
    checkInput(val) {
      val = val.replace(/[^\d\.-]/g, '')
      return val
    },
    //添加选项
    addClick() {
      this.calculationProblemForm.bmiList.push({
        rangeStartType: '',
        rangeStart: '',
        rangeEndType: '',
        rangeEnd: '',
        ifScoreSex: 0,
        score: '',
        scoreMen: '',
        scoreWomen: ''
      })
    },
    // 删除区间
    detailsResultClick(index) {
      this.calculationProblemForm.bmiList.splice(index, 1)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>