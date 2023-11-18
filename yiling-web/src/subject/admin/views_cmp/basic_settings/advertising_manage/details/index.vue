<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="90px" class="demo-ruleForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model.trim="form.title" maxlength="10" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="图片" prop="picUrl">
            <yl-upload
              :default-url="form.picUrl"
              :extral-data="{type: 'advertisementPic'}"
              @onSuccess="onSuccess"
            />
          </el-form-item>
          <p class="explain">建议尺寸：355 X 100 像素 支持.jpg 、.png格式</p>
          <el-form-item label="链接类型">
            <el-radio-group v-model="form.redirectType">
              <el-radio :label="1">H5链接</el-radio>
              <el-radio :label="2">小程序链接</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="链接地址">
           <el-input v-model.trim="form.url" placeholder="生产上仅支持https://h.59yi.com下页面访问"></el-input>
          </el-form-item>
          <el-form-item label="广告时段">
            <el-date-picker 
              v-model="form.time" 
              type="datetimerange" 
              format="yyyy-MM-dd HH:mm:ss" 
              value-format="yyyy-MM-dd HH:mm:ss" 
              range-separator="至" 
              start-placeholder="开始日期" 
              end-placeholder="结束日期" 
              :default-time="['00:00:00', '23:59:59']" 
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="投放位置">
            <el-checkbox-group 
            v-model="form.position" 
            style="display: inline-block;">
              <el-checkbox v-for="item in positionData" :key="item.value" :label="item.value">{{ item.label }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="显示顺序" >
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
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
import { save, queryById } from '@/subject/admin/api/cmp_api/advertisement'
import { hmcAdvertisementPosition } from '@/subject/admin/utils/busi'
export default {
  name: 'AdvertisingManageDetails',
  components: {
    ylUpload
  },
  computed: {
    positionData() {
      return hmcAdvertisementPosition()
    }
  },
  data() {
    return {
      form: {
        title: '',
        picUrl: '',
        pic: '',
        url: '',
        position: [],
        time: [],
        sort: '',
        redirectType: 1,
        id: ''
      },
      show: false,
      rules: {
        title: [{ required: true, message: '请输入banner标题', trigger: 'blur' }],
        picUrl: [{ required: true, message: '请上传banner图片', trigger: 'blur' }]
      },
      pickerOptions: { 
        disabledDate(time) {
          return time.getTime() < Date.now()-8.64e7;//如果没有后面的-8.64e7就是不可以选择今天的 
        }
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id !='0') {
      this.getData(query.id)
    }
  },
  methods: {
    async getData(val) {
     let data = await queryById(val);
     if (data !== undefined) {
       this.form = { ...data }
       this.form.time = [formatDate(data.startTime, 'yyyy-MM-dd hh:mm:ss'),formatDate(data.stopTime, 'yyyy-MM-dd hh:mm:ss')]
     }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let query = this.form;
          if (query.time !== null && query.time[0] == '- -') {
            query.time = [];
          }
          this.$common.showLoad();
          let data = await save(
            query.id,
            query.pic,
            query.position,
            query.sort,
            query.time && query.time.length > 0 ? query.time[0] : '',
            query.time && query.time.length > 1 ? query.time[1] : '',
            query.title,
            query.url,
            query.redirectType
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 图片上传成功
    onSuccess(data) {
      if (data.key) {
        this.form.pic = this.form.picUrl = data.key;
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
  @import "./index.scss";
</style>
