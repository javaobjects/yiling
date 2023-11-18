<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="100px" 
          class="demo-ruleForm">
          <el-form-item label="场景：" prop="sceneId" class="select-width">
            <el-select v-model="form.sceneId" placeholder="请选择">
              <el-option
                v-for="item in hmcGreetingSceneData"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运营备注：">
            <el-input
              type="textarea"
              maxlength="200" 
              show-word-limit
              style="max-width: 90%"
              :autosize="{ minRows: 4, maxRows: 5 }"
              placeholder="请输入运营备注"
              v-model.trim="form.remark">
            </el-input>
          </el-form-item>
          <el-form-item label="欢迎语：" class="pre-line" prop="draftVersion">
            <el-input
              type="textarea"
              style="max-width: 90%"
              :autosize="{ minRows: 8, maxRows: 10 }"
              placeholder="请输入活动描述"
              v-model="form.draftVersion">
            </el-input>
            <div>
              <yl-button type="text" @click="insertClick(1)">插入图标</yl-button>
              <!-- <yl-button type="text" @click="insertClick(2)">插入回车</yl-button> -->
              <yl-button type="text" @click="insertClick(3)">插入H5链接</yl-button>
              <yl-button type="text" @click="insertClick(4)">插入小程序链接</yl-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preserveClick">保存</yl-button>
    </div>
    <!-- 弹窗 -->
    <yl-dialog
      width="600px"
      :title="title"
      :show-footer="true"
      :visible.sync="showDialog"
      @confirm="confirm">
      <div class="pop-up">
        <div v-if="buttonType == 1" class="dialog-icon">
          <p>请选择图标：</p>
          <span 
            v-for="(item, index) in iconData" 
            :key="index" 
            :class="index == iconInd ? 'icon-color' : ''" 
            @click="iconClick(index)">
            {{ item.value }}
          </span>
        </div>
        <div v-if="buttonType == 3">
          <el-form 
            :model="formH5" 
            :rules="rulesH5" 
            ref="dataFormH5" 
            label-width="100px" 
            class="demo-ruleForm">
            <el-form-item label="链接文字：" prop="name" >
              <el-input v-model="formH5.name" placeholder="请输入链接文字"/>
            </el-form-item>
            <el-form-item label="H5链接：" prop="url" >
              <el-input v-model="formH5.url" placeholder="请输入链接"/>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="buttonType == 4">
          <el-form 
            :model="formApp" 
            :rules="rulesApp" 
            ref="dataFormApp" 
            label-width="110px" 
            class="demo-ruleForm">
            <el-form-item label="链接文字：" prop="name" >
              <el-input v-model="formApp.name" placeholder="请输入链接文字"/>
            </el-form-item>
            <el-form-item label="小程序URL：" prop="url" >
              <el-input v-model="formApp.url" placeholder="请输入小程序URL"/>
            </el-form-item>
            <el-form-item label="小程序appid" prop="appid">
              <el-input v-model="formApp.appid" placeholder="请输入小程序appid"/>
            </el-form-item>
            <el-form-item label="pagepath：" prop="pagepath" >
              <el-input v-model="formApp.pagepath" placeholder="请输入pagepath"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { saveGreetings, getDetailById } from '@/subject/admin/api/cmp_api/official_account'
import { hmcGreetingScene } from '@/subject/admin/busi/cmp/activity'
export default {
  name: 'WelcomeMessageEstablish',
  computed: {
    hmcGreetingSceneData() {
      return hmcGreetingScene()
    }
  },
  data() {
    return {
      form: {
        id: '',
        sceneId: '',
        remark: '',
        draftVersion: ''
      },
      rules: {
        sceneId: [{ required: true, message: '请选择场景', trigger: 'change' }],
        draftVersion: [{ required: true, message: '请输入欢迎语', trigger: 'blur' }]
      },
      certificateStateData: [],
      //弹窗是否显示
      showDialog: false,
      title: '',
      buttonType: 1,
      iconData: [
        {
          value: '👉'
        },
        {
          value: '🧧'
        },
        {
          value: '❗️'
        },
        {
          value: '👇'
        },
        {
          value: '🔍'
        },
        {
          value: '🔥'
        }
      ],
      //当前选中的图标
      iconInd: -1,
      //插入h5链接
      formH5: {
        name: '',
        url: ''
      },
      rulesH5: {
        name: [{ required: true, message: '请输入链接文字', trigger: 'blur' }],
        url: [{ required: true, message: '请输入链接', trigger: 'blur' }]
      },
      //插入小程序
      formApp: {
        name: '',
        url: '',
        appid: '',
        pagepath: ''
      },
      rulesApp: {
        name: [{ required: true, message: '请输入链接文字', trigger: 'blur' }],
        url: [{ required: true, message: '请输入小程序URL', trigger: 'blur' }],
        appid: [{ required: true, message: '请输入小程序appid', trigger: 'blur' }],
        pagepath: [{ required: true, message: '请输入pagepath', trigger: 'blur' }]
      }
    }
  },
  watch: {
    'buttonType': {
      handler(newName, oldName) {
        if (newName == 1) {
          this.title = '插入图标';
          this.iconInd = -1
        } else if (newName == 3) {
          this.title = '插入H5链接'
        } else if (newName == 4) {
          this.title = '插入小程序链接'
        }
      },
      immediate: true
    }
  },
  mounted() {
    let query = this.$route.params;
    if (query.id && query.id != '0') {
      this.getData(query.id)
    }
  },
  methods: {
    //点击插入按钮
    insertClick(val) {
      if (val == 2) {
        this.form.draftVersion = this.form.draftVersion + '\n'
      } else {
        this.formH5 = {
          name: '',
          url: ''
        }
        this.formApp = {
          name: '',
          url: '',
          appid: '',
          pagepath: ''
        }
        this.iconInd = -1;
        this.buttonType = val;
        this.showDialog = true
      }
    },
    //获取详情
    async getData(id) {
      let data = await getDetailById(
        id
      )
      if (data) {
        this.form = data
      }
    },
    //点击图标
    iconClick(val) {
      this.iconInd = val
    },
    //点击弹窗确认
    confirm() {
      if (this.buttonType == 1) {
        this.form.draftVersion = this.form.draftVersion + this.iconData[this.iconInd].value;
        this.showDialog = false
      } else if (this.buttonType == 3) {
        let form = this.formH5;
        this.$refs['dataFormH5'].validate((valid) => {
          if (valid) {
            this.form.draftVersion = this.form.draftVersion + `<a href = "${ form.url }"> ${ form.name } </a>`
            this.showDialog = false
          }
        })
      } else if (this.buttonType == 4) {
        let app = this.formApp;
        this.$refs['dataFormApp'].validate((valid) => {
          if (valid) {
            this.form.draftVersion = this.form.draftVersion + `<a href = "${ app.url } " data-miniprogram-appid = "${ app.appid }" data-miniprogram-path="${ app.pagepath }" > ${ app.name } </a>`
            this.showDialog = false
          }
        })
      }
    },
    //点击保存
    preserveClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form
          this.$common.showLoad();
          let data = await saveGreetings(
            form.id ? form.id : undefined,
            form.sceneId,
            form.remark,
            form.draftVersion
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>