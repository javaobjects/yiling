<template>
  <div class="app-container app_administration_details">
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
       <!-- 上部条件搜索 -->
      <div class="common-box">
        <div class="search-box">
          <el-form :model="form" ref="dataForm" :rules="rules" label-width="150px" class="demo-ruleForm">
            <el-form-item label="Banner位置" prop="usageScenario">
              <el-select v-model="form.usageScenario" placeholder="请选择Banner位置">
                <el-option label="主Banner" :value="1"></el-option>
                <el-option label="副Banner" disabled :value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="Banner标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" show-word-limit maxlength="10"/>
            </el-form-item>
            <el-form-item label="Banner图片" prop="pic">
              <yl-upload :default-url="form.pic" :limit="limit" :extral-data="{type: 'bannerPicture'}" @onSuccess="(val) => onSuccess(val)">
              </yl-upload>
            </el-form-item>
            <el-form-item label="投放日期" prop="tfTime">
              <el-date-picker
                v-model="form.tfTime"
                type="daterange"
                format="yyyy/MM/dd"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
                >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="form.sort" @input="e => (form.sort = checkInput(e))" placeholder="请输入排序号，最大200最小1" show-word-limit/>
            </el-form-item>
            <el-form-item label="使用场景" prop="bannerCondition">
              <el-select v-model="form.bannerCondition" placeholder="请选择使用场景">
                <el-option label="以岭内部机构" :value="1"></el-option>
                <el-option label="非以岭机构" :value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="状态">
              <el-switch v-model="form.bannerStatus" active-color="rgb(19, 206, 102)" inactive-color="#dcdfe6" active-value="1" inactive-value="2"></el-switch>
            </el-form-item>
            <el-form-item label="超链接">
              <el-input v-model="form.activityLinks" placeholder="请输入超链接"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center" :style="{ left: sidebarWidth }">
      <yl-button style="margin-right:46px" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservation">保存</yl-button>
    </div>
  </div>
</template>

<script>
import { ylUpload } from '@/subject/admin/components'
import { mapGetters } from 'vuex'
import { formatDate } from '@/subject/admin/utils'
import { bannerSave, getById } from '@/subject/admin/api/views_sale/app_maintain'
export default {
  computed: {
    ...mapGetters([
      'sidebarWidth'
    ])
  },
  components: {
    ylUpload
  },
  data() {
    return {
      form: {
        usageScenario: 1,
        title: '',
        pic: '',
        bannerCondition: '',
        tfTime: [],
        sort: '',
        bannerStatus: '1', //1启用2禁用
        activityLinks: '',
        id: ''
      },
      limit: 1,
      rules: {
        usageScenario: [{ required: true, message: '请选择Banner位置', trigger: 'change' }],
        title: [{ required: true, message: '标题名称不能为空', trigger: 'blur' }],
        tfTime: [{ required: true, message: '请选择投放日期', trigger: 'change' }],
        sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }],
        bannerCondition: [{ required: true, message: '请选择使用情景', trigger: 'change' }],
        pic: [{ required: true, message: 'banner图片不能为空', trigger: 'blur' }]
      },

      // 选择当前日期之后的时间
      pickerOptions: { 
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;//如果没有后面的-8.64e7就是不可以选择今天的 
        }
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id != '0') {
      this.form.id = query.id;
      this.getData()
    }
  },
  methods: {
    async getData() {
      let data = await getById(
        this.form.id
      )
      if (data !== undefined) {
        let times = [];
        times = [formatDate(data.startTime, 'yyyy-MM-dd'), formatDate(data.stopTime, 'yyyy-MM-dd')]
        this.form = {
          usageScenario: data.usageScenario,
          title: data.title,
          pic: data.pic,
          bannerCondition: data.bannerCondition,
          tfTime: times,
          sort: data.sort,
          bannerStatus: data.bannerStatus.toString(), //1启用2禁用
          activityLinks: data.activityLinks,
          id: data.id
        }
      }
    },
    // 保存
    preservation() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await bannerSave(
            form.activityLinks,
            form.bannerCondition,
            form.bannerStatus,
            form.id,
            form.pic,
            form.sort,
            form.tfTime && form.tfTime.length > 0 ? form.tfTime[0] : '',
            form.tfTime && form.tfTime.length > 0 ? form.tfTime[1] : '',
            form.title,
            form.usageScenario
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
    onSuccess(val) {
      this.form.pic = val.url;
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
  .app_administration_details ::v-deep .el-form-item__label{
    font-size: 16px;
  }
</style>