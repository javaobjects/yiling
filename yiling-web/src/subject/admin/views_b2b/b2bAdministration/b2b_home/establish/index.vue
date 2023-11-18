<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
    <div class="container">
      <div class="c-box">
        <el-form :model="form" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="标题名称" prop="title">
            <el-input v-model.trim="form.title" maxlength="5" show-word-limit></el-input>
          </el-form-item>
          <el-form-item label="上传图片" prop="pic">
            <yl-upload
              :default-url="form.pic"
              :extral-data="{type: 'bannerPicture'}"
              @onSuccess="onSuccess"
            />
          </el-form-item>
         <p class="explain">建议尺寸：144 X 144像素，支持.jpg 、.png格式</p>
          <!-- <el-form-item label="投放时间" prop="time">
            <el-date-picker
              v-model="form.time"
              type="daterange"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间">
            </el-date-picker>
          </el-form-item> -->
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" style="width: 100px;"
              @input="e => (form.sort = checkInput(e))" placeholder="排序"
            ></el-input>
            <span class="font-size-base font-light-color" style="margin-left:20px;">排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序</span>
          </el-form-item>
          <el-form-item label="状态" prop="vajraStatus">
            <el-radio-group v-model="form.vajraStatus">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="页面配置" prop="linkType">
            <el-select class="select-width" v-model="form.linkType" placeholder="请选择">
              <el-option
                v-for="item in positionType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="页面参数配置" prop="configuration" v-if="form.linkType != 6 && form.linkType != 7">
            <div v-show="form.linkType == ''">
            </div>
            <div v-show="form.linkType == 1">
              <el-input v-model="form.configuration" placeholder="请输入跳转链接"></el-input>
            </div>
            <div v-show="form.linkType == 3">
              <el-input v-model="form.configuration" placeholder="请输入关键词"></el-input>
            </div>
            <div v-show="form.linkType == 4">
              <el-input v-model="form.configuration" placeholder="请输入商品ID"></el-input>
            </div>
            <div v-show="form.linkType == 5">
              <el-input v-model="form.configuration" placeholder="请输入供应商ID"></el-input>
            </div>
            <div v-show="form.linkType == 8">
              <el-input v-model="form.configuration" placeholder="请填写会员ID"></el-input>
            </div>
          </el-form-item>
        </el-form>
      </div>
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
import { save, getById } from '@/subject/admin/api/b2b_api/b2bAdministration'
import { b2bAppVajraPositionType } from '@/subject/admin/busi/b2b/marketing_activity'
export default {
  name: 'PopBannerEdit',
  components: {
    ylUpload
  },
  computed: {
    positionType() {
      return b2bAppVajraPositionType()
    }
  },
  data() {
    return {
      form: {
        title: '',
        pic: '',
        vajraStatus: 1,
        linkType: '',
        sort: '',
        activityLinks: '',
        eid: '',
        goodsId: '',
        id: '',
        searchKeywords: '',
        sellerEid: '',
        configuration: ''
      },
      show: false,
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        pic: [{ required: true, message: '请上传图片', trigger: 'blur' }],
        vajraStatus: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        // time: [{ required: true, message: '请设置投放时间', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        linkType: [{ required: true, message: '请选择页面配置', trigger: 'change' }],
        activityLinks: [{ required: true, message: '请输入跳转链接', trigger: 'blur' }],
        searchKeywords: [{ required: true, message: '请输入关键词', trigger: 'blur' }],
        goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
        sellerEid: [{ required: true, message: '请输入供应商ID', trigger: 'blur' }],
        configuration: [{ required: true, message: '请输入页面参数配置', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.modifyData && query.modifyData !='0') {
      this.form.id = query.modifyData;
      this.getData()
    }
  },
  methods: {
    async getData() {
      let data = await getById(this.form.id)
      if (data !== undefined) {
        this.form = {
          title: data.title,
          pic: data.pic,
          vajraStatus: data.vajraStatus,
          linkType: data.linkType,
          sort: data.sort,
          activityLinks: data.activityLinks,
          eid: data.eid,
          goodsId: data.goodsId,
          id: data.id,
          sellerEid: data.sellerEid,
          configuration: data.linkType == 1 || data.linkType == 8 ? data.activityLinks : (data.linkType == 3 ? data.searchKeywords : (data.linkType == 4 ? data.goodsId : (data.linkType == 5 ? data.sellerEid : '')))
        }
      }
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await save(
              form.linkType == 1 || form.linkType == 8 ? form.configuration : '',
              form.eid,
              form.linkType == 4 ? form.configuration : '',
              form.id,
              form.linkType,
              form.pic,
              form.linkType == 3 ? form.configuration : '',
              form.linkType == 5 ? form.configuration : '',
              form.sort,
              form.title,
              form.vajraStatus
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
        // this.form.fileKey = data.key
        this.form.pic = data.url
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
