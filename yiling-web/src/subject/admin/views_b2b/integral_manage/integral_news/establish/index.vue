<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="积分兑换消息标题" prop="title">
            <el-input 
              v-model.trim="form.title" 
              maxlength="10" 
              show-word-limit 
              placeholder="请输入积分兑换消息标题">
            </el-input>
          </el-form-item>
          <el-form-item label="图标" prop="iconUrl">
            <yl-upload
              :default-url="form.iconUrl"
              :extral-data="{type: 'integralMessagePicture'}"
              @onSuccess="onSuccess"
            /> 
          </el-form-item>
          <p class="explain">建议尺寸：260 X 260像素，支持.jpg 、.gif、.png格式</p>
          <el-form-item label="投放日期" prop="time">
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="排序">
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="页面配置">
            <el-select class="select-width" v-model="form.pageConfig" placeholder="请选择">
              <el-option
                v-for="item in bannerType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="页面参数配置">
            <el-input v-model="form.link" placeholder="请输入您要跳转的链接，例:www.baidu.com"></el-input>
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
import { ylUpload } from '@/subject/admin/components'
import { formatDate } from '@/subject/admin/utils'
import { saveConfigMessage, messageGet } from '@/subject/admin/api/b2b_api/integral_record'
export default {
  name: 'IntegralNewsEstablish',
  components: {
    ylUpload
  },
  computed: {
   
  },
  data() {
    return {
      form: {
        id: '',
        title: '',
        icon: '',
        iconUrl: '',
        time: [],
        sort: '',
        status: 1,
        pageConfig: '',
        link: ''
      },
      show: false,
      rules: {
        title: [{ required: true, message: '请输入积分兑换消息标题', trigger: 'blur' }],
        iconUrl: [{ required: true, message: '请上传图标', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        time: [{ required: true, message: '请选择投放时间', trigger: 'change' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        pageConfig: [{ required: true, message: '请选择页面配置', trigger: 'change' }],
        link: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }]
      },
      bannerType: [
        {
          label: '活动链接',
          value: 1
        }
      ],
      // 选择当前日期之后的时间
      pickerOptions: {
        disabledDate(time) {
          //如果没有后面的-8.64e7就是不可以选择今天的
          return time.getTime() < Date.now() - 8.64e7;
        }
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id !='0') {
      this.form.id = query.id;
      this.getData()
    }
  },
  methods: {
    async getData() {
      let data = await messageGet(this.form.id)
      if (data) {
        let times = [];
        times = [formatDate(data.startTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.endTime, 'yyyy-MM-dd hh:mm:ss')]
        this.form = {
          ...data,
          time: times
        }
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveConfigMessage(
            form.id,
            form.title,
            form.icon,
            form.time && form.time.length > 0 ? form.time[0] : '',
            form.time && form.time.length > 1 ? form.time[1] : '',
            form.sort,
            form.status,
            form.pageConfig,
            form.link
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        } else {
          return false
        }
      })
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.form.iconUrl = data.url
        this.form.icon = data.key
      }
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
  @import './index.scss';
</style>
