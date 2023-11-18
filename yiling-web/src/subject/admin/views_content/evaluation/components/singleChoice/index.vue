<template>
  <!-- 单选题 -->
  <div class="singleChoice">
    <el-form
      :model="singleChoiceForm"
      :rules="singleChoiceRules"
      class="demo-ruleForm"
      label-width="100px"
      ref="singleChoiceForms">
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
                v-model.trim="singleChoiceForm.questionTopic">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否计分" prop="ifScore">
              <el-radio-group v-model="singleChoiceForm.ifScore">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="ifBlank">
              <el-radio-group v-model="singleChoiceForm.ifBlank">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-for="(item, index) in singleChoiceForm.selectList" :key="index"> 
          <el-col :span="12">
            <el-form-item
              :label="'选项 ' + `${ index + 1 }`"
              :prop="'selectList.' + index + '.optionText'"
              :rules="{ required: true, message: '对应选项不能为空', trigger: 'blur'}">
              <el-input
                type="textarea"
                style="max-width: 100%"
                maxlength="50" 
                show-word-limit
                :autosize="{ minRows: 2, maxRows: 3}"
                placeholder="请输入"
                v-model.trim="item.optionText">
              </el-input>
            </el-form-item>
          </el-col>
          <div v-if="singleChoiceForm.ifScore == 1">
            <el-col :span="3" class="col-option">
              <el-form-item label="" class="form-item-width">
                <el-checkbox 
                  v-model="item.ifScoreSex" 
                  :true-label="1" 
                  :false-label="0" 
                  :disabled="singleChoiceForm.ifScore ? false : true">
                  记分区分性别
                </el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="5" v-if="item.ifScoreSex == 1">
              <el-form-item label="记分(男)" :prop="'selectList.' + index + '.scoreMen'"
                :rules="{ required: true, message: '记分(男)不能为空', trigger: 'blur'}">
                <el-input v-model.trim="item.scoreMen" :disabled="singleChoiceForm.ifScore ? false : true" @input="e => (item.scoreMen = checkInput(e))"></el-input>
              </el-form-item>
              <el-form-item label="记分(女)" :prop="'selectList.' + index + '.scoreWomen'"
                :rules="{ required: true, message: '记分(女)不能为空', trigger: 'blur'}">
                <el-input v-model.trim="item.scoreWomen" :disabled="singleChoiceForm.ifScore ? false : true" @input="e => (item.scoreWomen = checkInput(e))"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5" v-if="item.ifScoreSex == 0" class="col-option">
              <el-form-item 
                label="记分" 
                :prop="'selectList.' + index + '.score'"
                :rules="{ required: true, message: '记分不能为空', trigger: 'blur'}">
                  <el-input 
                    v-model.trim="item.score" 
                    :disabled="singleChoiceForm.ifScore ? false : true" 
                    @input="e => (item.score = checkInput(e))">
                  </el-input>
              </el-form-item>
            </el-col>
          </div>
          <el-col :span="4" style="text-align: right;">
            <span class="item-details" v-if="index !== 0" @click="detailsResultClick(index)"><i class="el-icon-remove-outline icon_style"></i></span>
          </el-col>
        </el-row>
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
    //题目类型
    questionType: {
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
      singleChoiceForm: {
        questionTopic: '',
        ifScore: 1,
        ifBlank: 1,
        id: '',
        selectList: [
          {
            optionText: '',
            ifScoreSex: 0,
            score: '',
            scoreMen: '',
            scoreWomen: ''
          }
        ]
      },
      singleChoiceRules: {
        questionTopic: [{ required: true, message: '请输入题干', trigger: 'blur' }],
        ifScore: [{ required: true, message: '请选择是否记分', trigger: 'change' }],
        ifBlank: [{ required: true, message: '请选择是否必填', trigger: 'change' }]
      }
    }
  },
  mounted() {
    if (JSON.stringify(this.objectData) !== '{}') {
      this.singleChoiceForm = this.objectData
    }
  },
  methods: {
    preservation() {
      this.$refs['singleChoiceForms'].validate( async(valid) => {
        if (valid) {
          let form = this.singleChoiceForm;
          if (JSON.stringify(this.objectData) === '{}') {
            this.$common.showLoad();
            let data = await addHealthEvaluateQuestion(
              this.healthEvaluateId,
              [
                {
                  ifBlank: form.ifBlank,
                  ifScore: form.ifScore,
                  questionTopic: form.questionTopic,
                  questionType: this.questionType,
                  selectList: form.selectList
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
                questionTopic: form.questionTopic,
                questionType: this.questionType,
                selectList: form.selectList
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
    //点击添加选项
    addClick() {
      this.singleChoiceForm.selectList.push({
        optionText: '',
        ifScoreSex: 0,
        score: '',
        scoreMen: '',
        scoreWomen: ''
      })
    },
    detailsResultClick(index) {
      this.singleChoiceForm.selectList.splice(index, 1)
    },
    checkInput(val) {
      val = val.replace(/[^\d\.-]/g, '')
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>