<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        :model="form"
        :rules="rules"
        class="demo-ruleForm"
        label-width="100px"
        ref="dataForm">
        <div class="c-box">
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="标题" prop="title">
                <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :disabled="query.type == 3"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入标题"
                  v-model.trim="form.title">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="主讲人">
                <el-input
                  type="textarea"
                  maxlength="50" 
                  show-word-limit
                  style="max-width: 90%"
                  :disabled="query.type == 3"
                  :autosize="{ minRows: 2, maxRows: 2}"
                  placeholder="请输入主讲人"
                  v-model.trim="form.mainSpeaker">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="发布状态">
                <el-checkbox
                  :true-label="1" 
                  :false-label="0" 
                  :disabled="query.type == 3" 
                  v-model="form.publishFlag">
                  立即发布
                </el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="活动时间段" prop="activityTime">
                <el-date-picker
                  type="datetimerange"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  align="right"
                  @blur="blurDate"
                  :default-time="['00:00:00', '23:59:59']"
                  :disabled="query.type == 3"
                  v-model="form.activityTime">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="报名结束时间">
                <el-date-picker
                  type="datetime"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期"
                  :disabled="query.type == 3"
                  v-model="form.applyEndTime"
                  :picker-options="pickerOptions">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="活动形式" prop="activityModus">
                <el-select placeholder="请选择" :disabled="query.type == 3" v-model="form.activityModus">
                  <el-option 
                    v-for="item in activityData" 
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
              <el-form-item label="活动类型" prop="activityType">
                <el-select placeholder="请选择" :disabled="query.type == 3" v-model="form.activityType">
                  <el-option 
                    v-for="item in meetingType" 
                    :key="item.value" 
                    :label="item.label" 
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否有学分" style="display:inline-block">
                <div class="el-col-div">
                  <el-checkbox 
                    :true-label="1" 
                    :false-label="0" 
                    :disabled="query.type == 3" 
                    v-model="form.creditFlag" >
                    是
                  </el-checkbox>
                </div>
              </el-form-item>
              <div class="el-credit-div" v-if="form.creditFlag == 1">
                <el-form-item label="学分值/人" prop="creditValue">
                  <el-input 
                    style="width: 100px;"
                    placeholder="输入整数"
                    :disabled="query.type == 3" 
                    v-model="form.creditValue" 
                    @input="e => (form.creditValue = checkInput(e))"/>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="是否公开" prop="publicFlag">
                <el-radio-group v-model="form.publicFlag" :disabled="query.type == 3">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="引用业务线" prop="businessLineList">
                <el-checkbox-group v-model="form.businessLineList" :disabled="query.type == 3" @change="businessChange">
                  <el-checkbox
                    v-for="item in businessData" 
                    :key="item.value" 
                    :label="item.value">
                    {{ item.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="50">
            <el-col :span="12">
              <el-form-item label="内容权限" v-if="limitShow">
                <el-radio-group v-model="form.viewLimit" :disabled="query.type == 3">
                  <el-radio :label="1">仅登录</el-radio>
                  <el-radio :label="2">需认证通过</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-form-item
              label="封面" 
              prop="backgroundPic" 
              :rules="{ required: true, message: '请上传封面图片', trigger: 'change' }">
              <el-input style="display:none" v-model="form.backgroundPicUrl"/>
              <yl-upload
                :default-url="form.backgroundPicUrl"
                :extral-data="{type: 'meetingBackgroundPic'}"
                :oss-key="'meetingBackgroundPic'"
                :max-size="128"
                @onSuccess="onSuccess"/>
              <p class="explain">建议尺寸：640 X 200像素，支持.jpg 、.png格式</p>
            </el-form-item>
          </div>
          <div class="acticle-conter">
            <el-form-item label="内容详情" prop="content">
              <wang-editor
                :height="height" 
                :content="content" 
                :extral-data="{type: 'richTextEditorFile'}" 
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view" >
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" class="bottom-margin" v-if="query.type != 3" @click="preservationClick()">保存</yl-button>
    </div>
  </div>
</template>

<script>
import { wangEditor, ylUpload } from '@/subject/admin/components'
import { displayLine } from '@/subject/admin/utils/busi'
import { meetingActivityType } from '@/subject/admin/busi/content/article_video'
import { saveMeeting, getMeeting } from '@/subject/admin/api/content_api/meeting'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'AddEditMeeting',
  components: {
    wangEditor, ylUpload
  },
  computed: {
    businessData() {
      return displayLine()
    },
    meetingType() {
      return meetingActivityType()
    }
  },
  data() {
    return {
      height: 600,
      form: {
        //标题
        title: '', 
         //主讲人
        mainSpeaker: '',
         // 是否立即发布：0-否 1-是
        publishFlag: 0,
        //活动时间
        activityTime: [], 
        //报名结束时间
        applyEndTime: '', 
        //活动形式
        activityModus: '', 
        //活动类型
        activityType: '', 
        // 是否有学分：0-否 1-是
        creditFlag: 0,
        //学分值/人
        creditValue: '',
        // 是否公开
        publicFlag: 0, 
        //引用业务线集合
        businessLineList: [], 
        //封面图 key
        backgroundPic: '', 
        //封面图 绝对路径
        backgroundPicUrl: '', 
        //内容
        content: '', 
        id: '',
        // 内容权限 1-仅登录 2-需认证通过
        viewLimit: 1
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        activityTime: [{ required: true, message: '请选择活动时间段', trigger: 'change' }],
        activityModus: [{ required: true, message: '请选择活动形式', trigger: 'change' }],
        categoryId: [{ required: true, message: '请选择所属前台分类', trigger: 'change' }],
        activityType: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
        publicFlag: [{ required: true, message: '请选择是否公开', trigger: 'change' }],
        businessLineList: [{ required: true, message: '请选择引用业务线', trigger: 'change' }],
        backgroundPicUrl: [{ required: true, message: '请上传封面图', trigger: 'change' }],
        content: [{ required: true, message: '请输入内容详情', trigger: 'blur' }],
        creditValue: [{ required: true, message: '请输入学分值', trigger: 'blur' }]
      },
      activityData: [
        {
          label: '线上',
          value: 1
        },
        {
          label: '线下',
          value: 2
        }
      ],
      query: {
        id: '',
        // 1新增 2编辑 3详情
        type: '' 
      },
      pickerOptions: this.pickerTime(),
      content: '',
      // 是否显示内容权限
      limitShow: false 
    }
  },
  watch: {
    'form.creditFlag': {
      handler(newName, oldName) {
        if (newName == 0) {
          this.form.creditValue = ''
        }
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    this.query = query;
    if (query.type && query.type != 1) {
      this.getContentData()
    }
  },
  methods: {
    // 业务引用 判断是否选中了医生
    businessChange(e) {
      if (e && e.length > 0) {
        if (e.indexOf(2) > -1) {
          this.limitShow = true
        } else {
          this.limitShow = false
        }
      } else {
        this.limitShow = false
      }
    },
    // 设置 报名时间小于 活动开始时间
    pickerTime() {
      const that = this;
      return {
        disabledDate(time) {
          let times = that.form.activityTime;
          if (times && times.length > 0) {
            let timestamp = Date.parse(new Date(times[0]));
            return time.getTime() > timestamp - 8.64e7;
          } else {
            return ''
          }
        }
      }
    },
    blurDate() {
      let times = this.form.activityTime;
      if (times && times.length > 0 && this.form.applyEndTime != '') {
        let timestamp = Date.parse(new Date(times[0]));
        let timestamp2 = Date.parse(new Date(this.form.applyEndTime));
        if (timestamp2 > timestamp) {
          this.form.applyEndTime = ''
        } 
      }
    },
    // 获取 编辑数据
    async getContentData() {
      let data = await getMeeting(this.query.id)
      if (data) {
        data.creditValue = data.creditValue == 0 ? '' : data.creditValue;
        this.form = {
          ...data,
          activityTime: [formatDate(data.activityStartTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.activityEndTime, 'yyyy-MM-dd hh:mm:ss')],
          applyEndTime: formatDate(data.applyEndTime, 'yyyy-MM-dd hh:mm:ss')
        };
        this.content = data.content;
        this.form.applyEndTime = this.form.applyEndTime == '- -' ? '' : this.form.applyEndTime
        // 是否显示 内容权限
        this.businessChange(data.businessLineList)
      }
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.form.backgroundPic = data.key;
        this.form.backgroundPicUrl = data.url;
      }
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.content = content
    },
    // 点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveMeeting(
            form.title,
            form.mainSpeaker,
            form.publishFlag,
            form.activityTime && form.activityTime.length > 0 ? form.activityTime[0] : '',
            form.activityTime && form.activityTime.length > 1 ? form.activityTime[1] : '',
            form.applyEndTime == '- -' ? '' : form.applyEndTime,
            form.activityModus,
            form.activityType,
            form.creditFlag,
            form.creditValue,
            form.publicFlag,
            form.businessLineList,
            form.backgroundPic,
            form.content,
            this.limitShow ? form.viewLimit : '',
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
