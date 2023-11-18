<template>
  <div class="resultDetails">
    <el-form
      :model="form"
      :rules="rules"
      class="demo-ruleForm"
      label-width="100px"
      ref="dataForm">
      <div class="c-box">
        <div class="form-item-one">
          <el-form-item 
            label="对应分值"
            class="item-one-form"
            prop="scoreStartType">
            <el-select class="select-width" v-model="form.scoreStartType" placeholder="请选择">
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
            prop="scoreStart">
            <el-input 
              class="el_input" 
              v-model="form.scoreStart" 
              placeholder="请输入"
              @input="e => (form.scoreStart = checkInput(e))"/> 
          </el-form-item>
          <span>至</span>
          <el-form-item 
            label=""
            class="item-one-form item-one-input"
            prop="scoreEndType">
            <el-select class="select-width" v-model="form.scoreEndType" placeholder="请选择">
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
            prop="scoreEnd">
            <el-input 
              class="el_input" 
              v-model="form.scoreEnd" 
              placeholder="请输入"
              @input="e => (form.scoreEnd = checkInput(e))"/> 
          </el-form-item>
        </div>
        <el-form-item 
          label="测评结果"
          prop="evaluateResult">
          <el-input
            style="max-width: 100%"
            v-model="form.evaluateResult" 
            maxlength="50"
            show-word-limit 
            placeholder="请输入"/> 
        </el-form-item>
        <el-form-item 
          label="结果描述"
          prop="resultDesc">
          <el-input 
            v-model="form.resultDesc" 
            show-word-limit
            type="textarea" 
            placeholder="请输入结果描述"
            maxlength="500" 
            :autosize="{minRows: 4, maxRows: 6 }">
          </el-input>
        </el-form-item>
        <el-form-item
          label="健康小贴士"
          prop="healthTip">
          <wang-editor
            :height="300"
            :content="healthTip"
            :extral-data="{type: 'healthCoverAnswer'}"
            :handle-content="handleContent"/>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { wangEditor } from '@/subject/admin/components'
import { addHealthEvaluateResult, updateHealthEvaluateResult } from '@/subject/admin/api/content_api/evaluation'
export default {
  components: {
    wangEditor
  },
  props: {
    healthEvaluateId: {
      type: Number,
      default: 0
    },
    resultDetailsData: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      form: {
        scoreStartType: '',
        scoreStart: '',
        scoreEndType: '',
        scoreEnd: '',
        evaluateResult: '',
        resultDesc: '',
        healthTip: '',
        id: ''
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
      ],
      rules: {
        scoreStartType: [{ required: true, message: '请选择对应分值', trigger: 'change' }],
        scoreStart: [{ required: true, message: '请输入对应分值', trigger: 'blur' }],
        scoreEndType: [{ required: true, message: '请选择对应分值', trigger: 'change' }],
        scoreEnd: [{ required: true, message: '请输入对应分值', trigger: 'blur' }],
        evaluateResult: [{ required: true, message: '请输入测评结果', trigger: 'blur' }],
        resultDesc: [{ required: true, message: '请输入结果描述', trigger: 'blur' }],
        healthTip: [{ required: true, message: '请输入健康小贴士', trigger: 'blur' }]
      },
      healthTip: ''
    }
  },
  mounted() {
    if (this.resultDetailsData.id) {
      this.$nextTick(() => {
        this.form = {...this.resultDetailsData}
        this.healthTip = this.resultDetailsData.healthTip
      })
    }
  },
  methods: {
    //点击保存
    preservation() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          if (form.id) {
            // 修改
            this.$common.showLoad();
            let data1 = await updateHealthEvaluateResult(
              form.evaluateResult,
              this.healthEvaluateId,
              form.healthTip,
              form.id,
              form.resultDesc,
              form.scoreStartType,
              form.scoreStart,
              form.scoreEndType,
              form.scoreEnd
            )
             this.$common.hideLoad();
             if (data1 !== undefined) {
              this.$emit('dialogPop')
             }
          } else {
            // 新增
            this.$common.showLoad();
            let data = await addHealthEvaluateResult(
              [{
                scoreStartType: form.scoreStartType,
                scoreStart: form.scoreStart,
                scoreEndType: form.scoreEndType,
                scoreEnd: form.scoreEnd,
                evaluateResult: form.evaluateResult,
                resultDesc: form.resultDesc,
                healthTip: form.healthTip,
                healthEvaluateId: this.healthEvaluateId
              }],
              1
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$emit('dialogPop')
            }
          }
         
        }
      })
    },
    handleContent(content, editor) {
      this.form.healthTip = content;
    },
    // 验证
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