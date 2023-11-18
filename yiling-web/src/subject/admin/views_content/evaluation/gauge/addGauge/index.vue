<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        :model="form"
        :rules="rules"
        class="demo-ruleForm"
        label-width="130px"
        ref="dataForm">
        <div class="c-box">
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="量表名称" prop="healthEvaluateName">
                <el-input
                  maxlength="15" 
                  show-word-limit
                  placeholder="请输入量表名称"
                  v-model.trim="form.healthEvaluateName">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="量表类型" prop="healthEvaluateType">
                <el-select class="select-width" v-model="form.healthEvaluateType" placeholder="请选择">
                  <el-option
                    v-for="item in healthEvaluate"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="预计答题时长" prop="evaluateTime">
                <el-input
                  placeholder="请输入答题时长"
                  v-model.trim="form.evaluateTime"
                  @input="e => (form.evaluateTime = checkInput(e))">
                  <template slot="append">/分钟</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否医生私有">
                <el-radio-group v-model="form.docPrivate">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="发布平台" prop="lineIdList">
                <el-checkbox-group v-model="form.lineIdList">
                  <el-checkbox 
                    v-for="item in businessData" 
                    :key="item.value" 
                    :label="item.value">
                    {{ item.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.docPrivate == 1">
              <el-form-item label="量表所属医生" prop="docId">
                <div>
                  <el-tag
                    closable 
                    type="info" 
                    class="item-el-tag" 
                    v-for="(item, index) in form.docId" 
                    :key="index" 
                    @close="doctorClose(index)"> 
                    {{ item.hospitalName }} - {{ item.hospitalDepartment }} - {{ item.doctorName }}
                  </el-tag>
                </div>
                <span class="add-span" @click="addClick(1)">添加</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="量表描述" prop="healthEvaluateDesc">
                <el-input
                  type="textarea"
                  maxlength="100" 
                  show-word-limit
                  style="max-width: 90%"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入量表描述"
                  v-model.trim="form.healthEvaluateDesc">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否有结果">
                <el-radio-group v-model="form.resultFlag">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item 
                label="封面"
                prop="coverImageUrl"
                :rules="{ required: true, message: '请上传封面', trigger: 'change' }">
                <el-input style="display:none" v-model="form.coverImageUrl"/>
                <yl-upload
                  :default-url="form.coverImageUrl"
                  :extral-data="{type: 'healthCoverImage'}"
                  :oss-key="'healthCoverImage'"
                  @onSuccess="onSuccessImage"/>
                <p class="explain">建议尺寸：750 X 227像素，支持.jpg 、.png格式</p>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                prop="backImageUrl"
                label="设置分享背景图"
                :rules="{ required: true, message: '请上传分享背景图', trigger: 'change' }">
                <el-input style="display:none" v-model="form.backImageUrl"/>
                <yl-upload
                  :default-url="form.backImageUrl"
                  :extral-data="{type: 'healthBackImage'}"
                  :oss-key="'healthBackImage'"
                  @onSuccess="onSuccess"/>
                <p class="explain">建议尺寸：965 X 1751像素，支持.jpg 、.png格式; 大小不超过 2MB</p>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-form-item label="适用科室" prop="deptIdList">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in form.deptIdList" 
                  :key="index" 
                  @close="departmentClose(index)"> 
                  {{ item.label }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(2)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="适用疾病" prop="diseaseIdList">
              <div>
                <el-tag
                  closable 
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in form.diseaseIdList" 
                  :key="index" 
                  @close="diseaseClose(index)"> 
                  {{ item.name }} 
                </el-tag>
              </div>
              <span class="add-span" @click="addClick(3)">添加</span>
            </el-form-item>
          </div>
          <div>
            <el-form-item label="答题须知" prop="answerNotes">
              <wang-editor
                :height="500"
                :content="answerNotes"
                :extral-data="{type: 'healthCoverAnswer'}"
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
    <!-- 量表所属医生 -->
    <dialog-doctor 
      :data-list="form.docId"
      :show.sync="showDialog"
      @addDoctor="addDoctor"
      v-if="showDialog"/>
    <!-- 科室弹窗 -->
    <dialog-tree
      :data-list="form.deptIdList"
      :show.sync="showDialog2"
      @addTree="addTree"
      v-if="showDialog2"/>
    <!-- 疾病弹窗 -->
    <dialog-disease-drugs
      :data-list="form.diseaseIdList"
      :show.sync="showDialog3"
      :dialog-type="dialogType"
      :dialog-title="dialogTitle"
      @addCommodity="addCommodity"
      v-if="showDialog3"/>
  </div>
</template>

<script>
import { wangEditor, ylUpload } from '@/subject/admin/components'
import { displayLine } from '@/subject/admin/utils/busi'
import { addHealthEvaluate, getDetailById, updateHealthEvaluate } from '@/subject/admin/api/content_api/evaluation'
import { hmcHealthEvaluate } from '@/subject/admin/busi/content/article_video'
import dialogDoctor from '../../../article_video/establish/dialogDoctor'
import dialogTree from '../../../article_video/establish/dialogTree'
import dialogDiseaseDrugs from '../../../article_video/establish/dialogDiseaseDrugs'
export default {
  name: 'AddGauge',
  components: {
    wangEditor,
    ylUpload,
    dialogDoctor,
    dialogTree,
    dialogDiseaseDrugs
  },
  computed: {
    businessData() {
      return displayLine()
    },
    healthEvaluate() {
      return hmcHealthEvaluate()
    }
  },
  data() {
    return {
      form: {
        healthEvaluateName: '',
        healthEvaluateType: '',
        evaluateTime: '',
        docPrivate: 1,
        lineIdList: [],
        docId: [],
        healthEvaluateDesc: '',
        coverImage: '',
        coverImageUrl: '',
        backImage: '',
        backImageUrl: '',
        deptIdList: [],
        diseaseIdList: [],
        answerNotes: '',
        resultFlag: 1,
        id: ''
      },
      rules: {
        healthEvaluateName: [{ required: true, message: '请输入量表名称', trigger: 'blur' }],
        healthEvaluateType: [{ required: true, message: '请选择量表类型', trigger: 'change' }],
        evaluateTime: [{ required: true, message: '请输入答题时间', trigger: 'blur' }],
        lineIdList: [{ required: true, message: '请选择发布平台', trigger: 'change' }],
        answerNotes: [{ required: true, message: '请输入答题须知', trigger: 'blur' }],
        healthEvaluateDesc: [{ required: true, message: '请输入量表描述', trigger: 'blur' }],
        docId: [{ required: true, message: '请选择量表所属医生', trigger: 'change' }],
        deptIdList: [{ required: true, message: '请选择科室', trigger: 'change' }],
        diseaseIdList: [{ required: true, message: '请选择疾病', trigger: 'change' }]
      },
      //是否展示 量表所属医生
      showDialog: false,
      //关联科室弹窗
      showDialog2: false,
      //疾病类型 1 关联疾病 2 关联药品 
      dialogType: 1,
      dialogTitle: '适用疾病',
      showDialog3: false,
      answerNotes: ''
    }
  },
  mounted() {
    let dataId = this.$route.params;
    if (dataId.id != 0) {
      // 获取详情数据
      this.getContentData(dataId.id)
    }
  },
  methods: {
    async getContentData(val) {
      let data = await getDetailById(val)
      if (data) {
        this.form = data;
        this.form.docId = [{
          doctorName: data.docName,
          hospitalDepartment: data.hospitalDepartment,
          hospitalName: data.hospitalName,
          id: data.docId
        }];
        this.answerNotes = data.answerNotes;
        this.form.deptIdList = data.hospitalDeptVOS;
        this.form.diseaseIdList = data.diseaseVOList
      }
    },
    //从数组传过来的 疾病 信息
    addCommodity(row) {
      this.form.diseaseIdList.push(row)
    },
    // 从组件传过来的科室 信息
    addTree(row) {
      this.form.deptIdList = row;
    },
    //弹窗返回所属医生 数据
    addDoctor(row) {
      this.showDialog = false;
      this.form.docId = [row]
    },
    //添加医生 科室 疾病
    addClick(val) {
      if (val == 1) {
        this.showDialog = true
      } else if (val == 2) {
        this.showDialog2 = true;
      } else if (val == 3) {
        this.showDialog3 = true;
      }
    },
    //点击保存
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          // 科室ID 
          let departmentID = [];
          form.deptIdList.forEach(e => {
            departmentID.push(e.id)
          })
          // 疾病ID
          let diseaseID = [];
          form.diseaseIdList.forEach(e => {
            diseaseID.push(e.id)
          });
          if (form.id) {
            //编辑
            this.$common.showLoad();
            let data1 = await updateHealthEvaluate(
              form.healthEvaluateName,
              form.healthEvaluateType,
              form.evaluateTime,
              form.docPrivate,
              form.lineIdList,
              form.docId && form.docId.length > 0 ? form.docId[0].id : '',
              form.healthEvaluateDesc,
              form.coverImage,
              form.backImage,
              departmentID,
              diseaseID,
              form.answerNotes,
              form.resultFlag,
              form.id
            )
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          } else {
            // 新增
            this.$common.showLoad();
            let data = await addHealthEvaluate(
              form.healthEvaluateName,
              form.healthEvaluateType,
              form.evaluateTime,
              form.docPrivate,
              form.lineIdList,
              form.docId && form.docId.length > 0 ? form.docId[0].id : '',
              form.healthEvaluateDesc,
              form.coverImage,
              form.backImage,
              departmentID,
              diseaseID,
              form.answerNotes,
              form.resultFlag
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        } 
      })
    },
    //删除所属医生
    doctorClose(index) {
      this.form.docId.splice(index, 1)
    },
    //删除适用科室
    departmentClose(index) {
      this.form.deptIdList.splice(index, 1)
    },
    //删除适用疾病
    diseaseClose(index) {
      this.form.diseaseIdList.splice(index, 1)
    },
    //封面图
    onSuccessImage(row) {
      this.form.coverImage = row.key;
      this.form.coverImageUrl = row.url;
    },
    //背景图
    onSuccess(row) {
      this.form.backImage = row.key;
      this.form.backImageUrl = row.url;
    },
    handleContent(content, editor) {
      this.form.answerNotes = content
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
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