<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="topicSetting">
        <transition-group class="topicSetting-list">
          <ul
            @dragstart="dragstart(index)"
            @dragenter="dragenter($event, index)"
            @dragover="dragover($event, index)"
            draggable="true"
            v-for="(item, index) in list"
            :key="'dragstart' + index"
            class="list-item">
            <div class="list-content">
              <div class="title">
                <span class="form-item-title">
                  题目 {{ index + 1 }}
                  <span>( {{ item.questionType == 1 ? '单选题' : (item.questionType == 2 ? '填空题' : (item.questionType == 3 ? '多选题' : (item.questionType == 4 ? 'BMI计算题' : '性别选择题'))) }})</span>
                </span>
                <span class="form-item-icon">
                  <span class="item-details" @click="detailsResultClick(item)">
                    <i class="el-icon-delete icon_style"></i>
                  </span>
                  <span class="item-modify" @click="modifyClick(item)">
                    <i class="el-icon-edit icon_style"></i>
                  </span>
                </span>
              </div>
              <div class="item-content">
                <!-- 单选以及多选 的展示 -->
                <div v-if="item.questionType == 1 || item.questionType == 3">
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>题干：{{ item.questionTopic }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否记分：{{ item.ifScore == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否必填：{{ item.ifBlank == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                  </el-row>
                  <el-row v-for="(item1, index1) in item.selectList	" :key="'selectList' + index1">
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>选项{{ index1 + 1 }}：{{ item1.optionText }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4" v-if="item.ifScore == 1">
                      <div class="examine-row-conter">
                        <div v-if="item1.ifScoreSex == 1">
                          <p>记分(男)：{{ item1.scoreMen }}, 记分(女)：{{ item1.scoreWomen }}</p>
                        </div>
                        <div v-else>
                          <p>记分：{{ item1.score }}</p>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <!-- bmi的展示 -->
                <div v-if="item.questionType == 4">
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p></p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否记分：{{ item.ifScore == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否必填：{{ item.ifBlank == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                  </el-row>
                  <el-row v-for="(item1, index1) in item.bmiList" :key="'bmiList' + index1">
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>区间{{ index1 + 1 }}：
                          {{ item1.rangeStartType | dictLabel(sectionData) }}
                          {{ item1.rangeStart }} 
                          至 
                          {{ item1.rangeEndType | dictLabel(sectionData) }} 
                          {{ item1.rangeEnd }}
                        </p> 
                      </div>
                    </el-col>
                    <el-col :span="4" v-if="item.ifScore == 1">
                      <div class="examine-row-conter">
                        <div v-if="item1.ifScoreSex == 1">
                          <p>记分(男)：{{ item1.scoreMen }}, 记分(女)：{{ item1.scoreWomen }}</p>
                        </div>
                        <div v-else>
                          <p>记分：{{ item1.score }}</p>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <!-- 填空的展示 -->
                <div v-if="item.questionType == 2">
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>题干：{{ item.questionTopic }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p></p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否必填：{{ item.ifBlank == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>填空类型：{{ item.blank.blankType | dictLabel(completionData) }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4" v-if="item.blank.blankType == 2 || item.blank.blankType == 3">
                      <div class="examine-row-conter">
                        <p>是否可输入小数：{{ item.blank.ifDecimal == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                    <el-col :span="4" v-if="item.blank.ifUnit == 1">
                      <div class="examine-row-conter">
                        <p>单位：{{ item.blank.unit }}</p>
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <!-- 性别题的展示 -->
                <div v-if="item.questionType == 5">
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p></p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p></p>
                      </div>
                    </el-col>
                    <el-col :span="4">
                      <div class="examine-row-conter">
                        <p>是否必填：{{ item.ifBlank == 1 ? '是' : '否' }}</p>
                      </div>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <div class="examine-row-conter">
                        <p>选项： 男/女</p>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
          </ul>
        </transition-group>
      </div>
      <!-- 题目弹窗 -->
      <yl-dialog
        width="1000px"
        title="题目"
        :visible.sync="showDialog"
        @confirm="confirm">
        <div class="dialogTc">
          <div class="question-title">
            <span class="question-t-title">题型</span>
            <el-radio-group v-model="questionType" :disabled="disabled">
              <el-radio :label="1">单选题</el-radio>
              <el-radio :label="2">填空题</el-radio>
              <el-radio :label="3">多选题</el-radio>
              <el-radio :label="4">BMI计算题</el-radio>
              <el-radio :label="5">性别选择题</el-radio>
            </el-radio-group>
          </div>
          <single-choice 
            ref="singleChoice" 
            v-if="questionType == 1 || questionType == 3" 
            :health-evaluate-id="healthEvaluateId" 
            :question-type="questionType"
            @dialogPop="dialogPop"
            :object-data="objectData">
          </single-choice>
          <calculation-problem 
            ref="calculationProblem" 
            v-if="questionType == 4"
            :health-evaluate-id="healthEvaluateId" 
            :object-data="objectData" 
            @dialogPop="dialogPop">
          </calculation-problem>
          <completion
            ref="completion"
            v-if="questionType == 2"
            :health-evaluate-id="healthEvaluateId" 
            :object-data="objectData" 
            @dialogPop="dialogPop">
          </completion>
          <gender-selection 
            ref="genderSelection" 
            v-if="questionType == 5"
            :health-evaluate-id="healthEvaluateId"
            :object-data="objectData" 
            @dialogPop="dialogPop">
          </gender-selection>
        </div>
      </yl-dialog>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" @click="addTopic">添加题目</yl-button>
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <!-- <yl-button type="primary" @click="submitClick">排序保存</yl-button> -->
    </div>
  </div>
</template>

<script>
import singleChoice from '../../components/singleChoice'
import calculationProblem from '../../components/calculationProblem'
import completion from '../../components/completion'
import genderSelection from '../../components/genderSelection'
import { getQuestionsById, delQuestionsById, editQuestions } from '@/subject/admin/api/content_api/evaluation'
export default {
  name: 'TopicSetting',
  components: {
    singleChoice,
    calculationProblem,
    completion,
    genderSelection
  },
  data() {
    return {
      questionType: 1,
      showDialog: false,
      list: [],
      //源对象的下标
      dragIndex: '',
      //目标对象的下标
      enterIndex: '',
      timeout: null,
      //量表id
      healthEvaluateId: 0,
      objectData: {},
      disabled: false,
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
      completionData: [
        {
          value: 1,
          label: '文本'
        },
        {
          value: 2,
          label: '数字'
        },
        {
          value: 3,
          label: '数字型（区间）'
        },
        {
          value: 4,
          label: '日期'
        },
        {
          value: 5,
          label: '上传图片'
        }
      ]
    }
  },
  mounted() {
    let dataId = this.$route.params;
    if (dataId.id) {
      // 获取量表id
      this.healthEvaluateId = parseInt(dataId.id)
      // 获取题目列表
      this.getList();
    }
  },
  methods: {
    //点击删除
    detailsResultClick(row) {
      this.$confirm(`确认删除 ${row.questionTopic } ！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        //确定
        this.$common.showLoad();
        let data = await delQuestionsById(
          row.id
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 点击编辑
    modifyClick(row) {
      this.questionType = row.questionType;
      this.disabled = true;
      this.showDialog = true;
      this.objectData = JSON.parse(JSON.stringify(row));
    },
    //弹窗返回
    dialogPop() {
      this.showDialog = false;
      this.getList();
    },
    // 获取数据
    async getList() {
      let data = await getQuestionsById(this.healthEvaluateId)
      if (data) {
        this.list = data;
      }
    },
    // 点击确认
    confirm() {
      if (this.questionType == 1 || this.questionType == 3) {
        this.$refs.singleChoice.preservation()
      } else if (this.questionType == 4) {
        this.$refs.calculationProblem.preservation()
      } else if (this.questionType == 2) {
        this.$refs.completion.preservation()
      } else if (this.questionType == 5) {
        this.$refs.genderSelection.preservation()
      }
    },
    // 添加题目
    addTopic() {
      this.objectData = {}
      this.questionType = 1;
      this.disabled = false;
      this.showDialog = true
    },
    //点击排序保存
    async submitClick() {
      this.$common.showLoad();
      //进行排序字段变动
      let sortList = [];
      for (let i = 0; i < this.list.length; i ++) {
        sortList.push({
          ...this.list[i],
          questionRank: i + 1
        })
      }
      let data = await editQuestions(
        this.healthEvaluateId,
        [
          ...sortList
        ]
        
      )
      this.$common.hideLoad();
      if (data !== undefined) {
        this.$common.n_success('更新题目排序成功！')
      }
    },
    dragstart(index) {
      this.dragIndex = index
    },
    // dragenter 和 dragover 事件的默认行为是拒绝接受任何被拖放的元素。 
    // 因此，我们要在这两个拖放事件中使用`preventDefault`来阻止浏览器的默认行为
    dragenter(e, index) {  
      e.preventDefault();
      this.enterIndex = index;
      if ( this.timeout !== null) {
        clearTimeout(this.timeout)
      }
      // 拖拽事件的防抖
      this.timeout = setTimeout(() => {
        if (this.dragIndex !== index){
          const source = this.list[this.dragIndex]
          this.list.splice(this.dragIndex, 1)
          this.list.splice(index, 0 , source )
          // 排序变化后目标对象的索引变成源对象的索引
          this.dragIndex = index;
        }
      }, 100);
    },
    dragover(e, index) {
      e.preventDefault();
    }
  },
  destroyed() {
   // 每次离开当前界面时，清除定时器
    clearInterval(this.timeout)
    this.timeout = null
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>