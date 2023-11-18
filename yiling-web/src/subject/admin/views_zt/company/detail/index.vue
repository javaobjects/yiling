<template>
  <div class="app-container" v-show="row.enterpriseInfo.id">
    <div class="app-container-content">
    <div class="header-bar"><div class="sign"></div>企业信息</div>
    <div class="container">
      <div class="container-header flex-row-left">
        <div class="flex1">
          <span class="font-light-color">企业ID：</span>
          {{ row.enterpriseInfo.id }}
          <span class="font-important-color">（{{ row.enterpriseInfo.authStatus | dictLabel(companyStatus) }}）</span>
        </div>
        <div>
          <el-switch :value="row.switch === 1" @change="switchChange" active-text="启用" inactive-text="停用">
          </el-switch>
        </div>
      </div>
      <div class="c-box">
        <div class="font-size-base font-title-color">
          <span class="font-light-color">企业名称：</span>
          {{ row.enterpriseInfo.name }}
        </div>
        <div class="font-size-base font-title-color">
          <span class="font-light-color">企业类型：</span>{{ row.enterpriseInfo.type | dictLabel(companyType) }}
        </div>
        <div class="font-size-base font-title-color">
          <span class="font-light-color">{{ row.enterpriseInfo.type == 7 ? '医疗机构：' : '社会统一信用代码：' }}</span>
          {{ row.enterpriseInfo.licenseNumber }}
        </div>
        <div class="font-size-base font-title-color">
          <span class="font-light-color">企业地址：</span>
          {{ row.enterpriseInfo.provinceName }}{{ row.enterpriseInfo.cityName }}{{ row.enterpriseInfo.regionName }}
        </div>
        <div class="font-size-base font-title-color font-width">
          <span class="font-light-color">详细地址：</span>
          {{ row.enterpriseInfo.address }}
        </div>
        <div class="font-size-base font-title-color" v-if="row.enterpriseInfo.type == 2">
          <span class="font-light-color">APP推广链接：</span>
          {{ enterpriseUrl }}
          <span class="create" v-if="!createType" @click="createClick">生成二维码</span>
        </div>
        <div class="qrCode" v-if="createType">
          <div ref="qrCode"></div>
          <span @click="downloadClick">下载二维码</span>
        </div>
        <div class="c-box-rigth">
          <yl-button type="primary" @click="showDialog">修改企业信息</yl-button>
          <yl-button type="primary" @click="modifyLxClick">修改企业类型</yl-button>
        </div>
      </div>
    </div>

    <div class="header-bar"><div class="sign"></div>企业联系人信息</div>
    <div class="c-box">
      <div class="font-size-base font-title-color">
        <span class="font-light-color">联系人姓名：</span>
        {{ row.enterpriseInfo.contactor }}
      </div>
      <div class="font-size-base font-title-color">
        <span class="font-light-color">联系人电话：</span>
        {{ row.enterpriseInfo.contactorPhone }}
      </div>
    </div>

    <div v-if="row.subEnterpriseList && row.subEnterpriseList.length">
      <div class="header-bar"><div class="sign"></div>归属企业信息</div>
      <div class="c-box">
        <div class="font-size-base font-important-color" v-for="(item, index) in row.subEnterpriseList" :key="index">
          <div class="font-title-color item">归属企业ID：<span>{{ item.id }}</span></div>
          <div class="font-title-color item">归属企业名称：<span>{{ item.name }}</span></div>
          <div class="font-title-color item">编码：<span>{{ item.erpCode }}</span></div>
        </div>
      </div>
    </div>

    <div class="header-bar"><div class="sign"></div>平台产品线开通信息</div>
    <div class="c-box">
      <div class="font-size-base font-title-color">
        <div v-if="row.platformInfo.popVisiableFlag == 1" style="display: inline-block;">
          <span class="font-light-color">是否开通POP：</span>
          <el-radio-group v-if="row.platformInfo.popFlag==1" v-model="row.platformInfo.popFlag">
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
          <el-radio-group v-else :value="row.platformInfo.popFlag" @input="popFlagChange">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="font-size-base font-title-color" v-if="row.platformInfo.popFlag==1">
        <span class="font-light-color">渠道类型：</span>
        <el-radio-group :value="row.enterpriseInfo.channelId" @input="channelIdChange">
          <el-radio v-for="item in channelType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
        </el-radio-group>
      </div>
      <div class="font-size-base font-title-color" v-if="row.platformInfo.mallVisiableFlag == 1">
        <span class="font-light-color">是否开通B2B：</span>
        <el-radio-group v-if="row.platformInfo.mallFlag == 1" v-model="row.platformInfo.mallFlag" >
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
        <el-radio-group v-else :value="row.platformInfo.mallFlag" @input="mallFlagChange">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <div class="font-size-base font-title-color" v-if="row.platformInfo.salesAssistVisiableFlag == 1">
        <span class="font-light-color">是否开通销售助手：</span>
        <el-radio-group v-if="row.platformInfo.salesAssistFlag == 1" v-model="row.platformInfo.salesAssistFlag">
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
         <el-radio-group v-else :value="row.platformInfo.salesAssistFlag" @input="salesAssistFlagChange">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <div class="font-size-base font-title-color" v-if="row.platformInfo.hmcVisiableFlag == 1">
        <span class="font-light-color">是否开通健康管理中心：</span>
        <el-radio-group v-if="row.platformInfo.hmcFlag == 1" v-model="row.platformInfo.hmcFlag">
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
         <el-radio-group v-else :value="row.platformInfo.hmcFlag" @input="cEndClick" >
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <div class="font-size-base font-title-color" v-if="row.platformInfo.hmcFlag==1">
        <span class="font-light-color">健康管理中心 业务类型：</span>
        <el-radio-group :value="row.enterpriseInfo.hmcType" @input="channelHmcChange">
          <el-radio v-for="item in channelHmcType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
        </el-radio-group>
      </div>
      <div class="font-size-base font-title-color">
      </div>

    </div>

    <div class="header-bar"><div class="sign"></div>管理员账号信息</div>
    <div class="c-box">
      <div class="c-box-buttom"></div>
       <el-form :model="form3" :rules="rules3" ref="dataForm3" label-width="80px" class="demo-ruleForm">
        <div v-for="(item,index) in form3.managerList" :key="index">
          <el-row :gutter="50">
            <el-col :span="8">
              <el-form-item label="账号电话" :prop="`managerList.${index}.mobile`" :rules="{ required: true, message: '账号电话不能为空', trigger: 'blur'}">
                <el-input 
                  v-if="item.type && item.type == 1"
                  v-model.trim="item.mobile" 
                  placeholder="请输入账号电话" 
                  @focus="mobileFocus(item)" 
                  @blur="mobileBlur(item,index)" 
                  maxlength="11" 
                  show-word-limit 
                  @input="e => (item.mobile = checkInput(e))"
                />
                <span v-else>{{ item.mobile }} ({{ item.status | dictLabel(getUserStatus) }})</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="账号姓名" :prop="`managerList.${index}.name`" :rules="{ required: true, message: '账号姓名不能为空', trigger: 'blur'}" >
                <el-input 
                  v-if="item.type && item.type == 1" 
                  v-model.trim="item.name" 
                  maxlength="10" 
                  show-word-limit 
                  placeholder="请输入账号姓名" 
                />
                <span v-else>{{ item.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <yl-button v-if="item.type == 1" type="text" :disabled="item.mobileBlurType" @click="preservationClick(item,index)" >保存</yl-button>
              <yl-button v-else type="text" @click="modifyPassword(item,index)">修改账号信息</yl-button>
              <yl-button type="text" @click="resetPassword(item,index)">重置管理员密码</yl-button>
            </el-col>
          </el-row>
        </div>
       </el-form>
    </div>
    <!-- 资质信息 -->
    <div class="header-bar"><div class="sign"></div>资质信息</div>
    <div class="qualification-box">
      <ul>
        <li v-for="(item, index) in qualificationData" :key="index">
            <el-image class="qualification-box-img" @click="imgClick(item)" :src="item.fileUrl">
              <div slot="error" class="image-slot-imgs">
                <p>未上传...</p>
              </div>
            </el-image>
            <div class="qualification-box-conter" v-if="item.periodRequired">
              <p>{{ item.name }}</p>
              <p><span class="font-light-color">资质有效期</span>：
                <span v-if="item.longEffective == 0">
                  {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - {{ item.periodEnd | formatDate('yyyy-MM-dd') }}
                </span>
                <span v-else>
                  {{ item.periodBegin | formatDate('yyyy-MM-dd') }} - 长期有效
                </span>
              </p>
            </div>
            <div class="qualification-box-conter" v-else>
              <p>{{ item.name }}</p>
            </div>
        </li>
      </ul> 
    </div>

    <yl-dialog
      :visible.sync="show"
      title="修改详情"
      @confirm="confirm">
      <div class="dialog-content">
        <el-form
          ref="dataForm"
          :rules="rules"
          :model="form"
          label-width="auto"
          label-position="right">
          <el-form-item label="企业名称" prop="name">
            <el-input v-model="form.name" maxlength="50" show-word-limit placeholder="请输入企业名称" />
          </el-form-item>
          <el-form-item label="社会统一信用代码" prop="licenseNumber">
            <el-input v-model="form.licenseNumber" maxlength="22" show-word-limit placeholder="请输入社会统一信用代码" />
          </el-form-item>
          <el-form-item label="企业地址" prop="regionCode">
            <yl-choose-address :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode" />
          </el-form-item>
          <el-form-item label="详细地址" prop="address">
            <el-input v-model="form.address" maxlength="30" show-word-limit placeholder="请输入详细地址" />
          </el-form-item>
          <el-form-item label="联系人姓名" prop="contactor">
            <el-input v-model="form.contactor" maxlength="10" show-word-limit placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item label="联系人电话" prop="contactorPhone">
            <el-input v-model="form.contactorPhone" maxlength="11" show-word-limit placeholder="请输入联系人电话" />
          </el-form-item>
        </el-form>
      </div>
    </yl-dialog>
    <!-- 开通POP -->
    <yl-dialog
      :visible.sync="showPop"
      title="温馨提示"
      @confirm="confirmPop">
      <div class="pop-conter">
        <p class="title">请确认是否开通POP平台</p>
         <el-form
          ref="dataForm2"
          :rules="rules2"
          :model="form2"
          label-width="auto"
          label-position="right"
        >
            <el-form-item label="渠道类型：" prop="qdType">
              <el-radio-group v-model="form2.qdType">
                <el-radio v-for="item in channelType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
         </el-form>
      </div>
    </yl-dialog>
    <!-- 开通药+险 -->
    <yl-dialog
      :visible.sync="showPop2"
      title="温馨提示"
      @confirm="confirmPop2">
      <div class="pop-conter">
        <p class="title">请确认是否开通健康管理中心</p>
         <el-form
          ref="cdataForm"
          :rules="crules"
          :model="cform"
          label-width="auto"
          label-position="right">
            <el-form-item label="健康管理中心 业务类型：" prop="hmcTypes">
              <el-radio-group v-model="cform.hmcTypes">
                <el-radio v-for="item in channelHmcType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
         </el-form>
      </div>
    </yl-dialog>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList" />
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)" >返回</yl-button>
    </div>
  </div>
    <yl-dialog
      :visible.sync="typeShow"
      title="修改企业类型"
      @confirm="typeConfirm"
    >
      <div class="dialog-content">
        <el-form
          ref="dataFormType"
          :rules="rulesType"
          :model="formType"
          label-width="auto"
          label-position="right"
        >
          <p class="dialog-p">
            当前企业类型为 <span>{{ row.enterpriseInfo.type | dictLabel(companyType) }}</span> 更改为
          </p>
          <el-form-item label="企业类型：" prop="type">
            <el-select class="select-width" v-model="formType.type" placeholder="请选择企业类型" >
              <el-option v-for="item in companyType" :key="item.value" :label="item.label" :value="item.value" >
              </el-option>
            </el-select>
          </el-form-item>
          <p class="dialog-color">
            注：更改企业类型后需要重新开通产品线; 原商家后台自定义角色权限将被清空
          </p>
        </el-form>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
  import { 
    businessDetails, 
    updateStatus, 
    enterpriseUpdate, 
    openPlatform, 
    getByMobile, 
    updateManagerAccount, 
    updateType, 
    updateChannel, 
    updateHmcType,
    resetPassword
  } from '@/subject/admin/api/zt_api/company'
  import { enterpriseType, enterpriseAuthStatus, hmcType, userStatus, channelType} from '@/subject/admin/utils/busi'
  import { ylChooseAddress } from '@/subject/admin/components'
  import { isTel } from '@/subject/admin/utils/rules'
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import loadQrCode from '@/common/utils/qrcode'
  export default {
    name: 'ZtCompanyDetail',
    components: {
      ylChooseAddress, ElImageViewer
    },
    computed: {
      companyType() {
        return enterpriseType()
      },
      companyStatus() {
        return enterpriseAuthStatus()
      },
      channelType() {
        return channelType()
      },
      channelHmcType() {
        return hmcType()
      },
      getUserStatus() {
        return userStatus();
      }
    },
    data() {
      return {
        row: {
          switch: true,
          enterpriseInfo: {},
          platformInfo: {}
        },
        form: {},
        switch: true,
        show: false,
        form2: {
          qdType: ''
        },
        cform: {
          hmcTypes: ''
        },
        rules: {
          name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
          licenseNumber: [{ required: true, message: '请输入社会统一信用代码', trigger: 'blur' }],
          regionCode: [{ required: true, message: '请选择企业地区', trigger: 'change' }],
          address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
          contactor: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
          contactorPhone: [{ required: true, validator: isTel, trigger: 'blur' }]
        },
        rules2: {
          qdType: [{ required: true, message: '请选择渠道类型', trigger: 'change' }]
        },
        crules: {
          hmcTypes: [{ required: true, message: '请选择业务类型', trigger: 'change' }]
        },
        qualificationData: [],
        //开通pop
        showPop: false, 
        //开通药+险
        showPop2: false, 
        //图片放大是否显示
        showViewer: false, 
        imageList: [],
        //管理员账号信息
        form3: {
          managerList: []
        },
        rules3: {
          managerList: [{ required: true, message: '请输入', trigger: 'blur' }]
        },
        // 修改企业类型
        typeShow: false,
        rulesType: {
          type: [{ required: true, message: '请选择企业类型', trigger: 'change' }]
        },
        formType: {
          type: ''
        },
        //APP推广链接
        enterpriseUrl: '',
        //是否展示二维码
        createType: false
      }
    },
    created() {
      loadQrCode()
    },
    mounted() {
      this.query = this.$route.params;
      if (this.query.id) {
        this.getData()
      }
    },
    methods: {
      // 点击图片
      imgClick(val) {
        if (val.fileUrl != '') {
          this.imageList = [val.fileUrl];
          this.showViewer = true;
        }
      },
      onClose() {
        this.imageList = [];
        this.showViewer = false;
      },
      // 修改企业类型
      modifyLxClick() {
        this.typeShow = true;
      },
      typeConfirm() {
        this.$refs['dataFormType'].validate(async (valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await updateType(
              this.query.id,
              this.formType.type
            )
            this.$common.hideLoad()
            if (data !== undefined) {
              this.typeShow = false
              this.getData()
              this.$common.n_success('修改成功')
            }
          }
        })
      },
      async getData() {
        this.form3.managerList = [];
        let data = await businessDetails(this.query.id)
        if (data) {
          data.switch = data.enterpriseInfo.status
          this.row = data;
          this.qualificationData = data.enterpriseCertificateList;
          for (let i = 0; i < data.managerList.length; i++) {
            this.form3.managerList.push({
              ...data.managerList[i],
              type: 2,
              disabled: true,
              mobileBlurType: false
            })
          }
          if (data.enterpriseInfo.type == 2) {
            this.enterpriseUrl = `${process.env.VUE_APP_H5_URL}/#/b2b/downApp/?enterpriseId=${data.enterpriseInfo.id}`
          }
        }
      },
      //点击生成二维码
      createClick() {
        this.createType = true;
        setTimeout(() => {
          new window.QRCode(this.$refs.qrCode, {
            width: 150,
            height: 150,
            text: this.enterpriseUrl,
            correctLevel: 3
          })
        }, 100)
      },
      showDialog() {
        this.form = Object.assign({}, this.row.enterpriseInfo)
        this.show = true
      },
      confirm() {
        this.$refs['dataForm'].validate(async (valid) => {
          if (valid) {
            let form = this.form
            this.$common.showLoad()
            let data = await enterpriseUpdate(
              form.address,
              form.cityCode,
              form.contactor,
              form.contactorPhone,
              form.id,
              form.licenseNumber,
              form.name,
              form.provinceCode,
              form.regionCode 
            )
            this.$common.hideLoad()
            if (data && data.result) {
              this.show = false
              this.getData()
              this.$common.n_success('修改成功')
            }
          } else {
            return false
          }
        })
      },
      resetPassword(item) {
        this.$confirm('重置后，将变更为初始密码”888888“，请即时通知商户并提示登录后需即时修改密码，保护账号登录安全！', '确定重置商户登录密码吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then( async() => {
          this.$common.showLoad()
          let data = await resetPassword(item.mobile)
          this.$common.hideLoad()
          if (data && data.result) {
            this.$common.n_success('重置密码成功')
          }
        })
        .catch(() => {
          //取消
        });
      },
      // 修改账号信息
      modifyPassword(item) {
        item.disabled = true;
        item.type = 1;
      },
      switchChange(value) {
        let name = this.row.enterpriseInfo.name
        const update = async () => {
          this.$common.showLoad()
          let data = await updateStatus(this.query.id, value ? 1 : 2)
          this.$common.hideLoad()
          if (data && data.result) {
            this.row.switch = value ? 1 : 2
          }
        }
        if (!value) {
          this.$common.confirm(
            `停用企业可能存在未完结的订单，请和相关部门沟通后进行操作；<br>停用后该企业所有员工将不能登录该企业；<br>确认是否停用该企业：${name}？`,
            async (r) => {
            if (r) {
              update()
            }
          }, '', '', { dangerouslyUseHTMLString: true })
        } else {
          update()
        }
      },
      // 是否开通 pop
      popFlagChange(val) {
        if (val == 1) {
          this.showPop = true;
        }
      },
      // 确认开通pop
      confirmPop() {
        this.$refs['dataForm2'].validate(async (valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await openPlatform(
              this.form2.qdType,
              this.query.id,
              [1]
            )
            this.$common.hideLoad();
            if (data!==undefined) {
              this.$common.n_success('修改成功!');
              this.showPop = false;
              this.getData();
            }
          }
        })
      },
      // 确认开通药+险
      confirmPop2() {
        this.$refs['cdataForm'].validate(async (valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await openPlatform(
              '',
              this.query.id,
              [5],
              this.cform.hmcTypes
            )
            this.$common.hideLoad();
            if (data!==undefined) {
              this.$common.n_success('修改成功!');
              this.showPop2 = false;
              this.getData();
            }
          }
        })
      },
      // 修改健康管理中心
      channelHmcChange(val) {
        this.$confirm('是否重新选择 健康管理中心 业务类型 ！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then( async() => {
        //确定 this.query.id
          this.$common.showLoad();
          let data = await updateHmcType(
            val,
            this.query.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('修改成功!');
            this.getData();
          }
        })
        .catch(() => {
          //取消
        });
      },
      // 是否修改渠道类型
      channelIdChange(val) {
        this.$confirm('是否修改渠道类型 ！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then( async() => {
        //确定 this.query.id
          this.$common.showLoad();
          let data = await updateChannel(
            val,
            this.query.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('修改成功!');
            this.getData();
          }
        })
        .catch(() => {
          //取消
        });
      },
      // 是否开通B2B
      mallFlagChange(val) {
        if (val == 1) {
          this.$confirm('是否开通B2B ！', '提示', {
          confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            //确定 
            this.$common.showLoad();
            let data = await openPlatform(
              '',
              this.query.id,
              [2]
            )
            this.$common.hideLoad();
            if (data!==undefined) {
              this.$common.n_success('开通成功!');
              this.getData();
            }
          })
          .catch(() => {
            //取消
          });
        }
      },
      // 是否开通销售助手
      salesAssistFlagChange(val) {
        if (val == 1) {
          this.$confirm('是否开通销售助手 ！', '提示', {
          confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            //确定 
            this.$common.showLoad();
            let data = await openPlatform(
              '',
              this.query.id,
              [3]
            )
            this.$common.hideLoad();
            if (data!==undefined) {
              this.$common.n_success('开通成功!');
              this.getData();
            }
          })
          .catch(() => {
            //取消
          });
        }
      },
      // 是否开通 健康管理中心
      cEndClick(val) {
        if (val == 1) {
          this.showPop2 = true;
        }
      },
      // 管理员 账号信息 保存
      preservationClick(item,index) {
        this.$refs['dataForm3'].validate(async (valid) => {
          if (valid) {
            if (item.mobile.search(/^1[3456789]\d{9}$/) == 0) {
              this.$common.showLoad();
              let data = await updateManagerAccount(
                this.query.id,
                item.name,
                item.mobile,
                item.userId
              )
              this.$common.hideLoad();
              let val = item;
              if (data !== undefined) {
                this.$common.n_success('保存成功!');
                val.type = 2;
                val.disabled = false;
                this.getData();
              } else {
                val.type = 1;
                val.disabled = true;
              }
            } else {
              this.$common.error('请输入正确的手机号！')
            }
          }
        })
      },
      // 失去焦点
      async mobileBlur(item,index) {
        this.$common.showLoad();
        let data = await getByMobile(item.mobile)
        this.$common.hideLoad();
        let val = item;
        val.mobileBlurType = false;
        if (data !== null) {
          this.form3.managerList[index].name = data.name;
          this.form3.managerList[index].disabled = true;
        } else {
          this.form3.managerList[index].name = '';
          this.form3.managerList[index].disabled = false;
        }
      },
      // 获取焦点
      mobileFocus(item) {
        item.mobileBlurType = true;
      },
      // 验证
      checkInput(val) {
        if (val.search(/^[0-9]*[1-9][0-9]*$/) == 0) {
          return val
        } else {
          return ''
        }
      },
      //点击下载二维码
      downloadClick() {
        let drawing = document.getElementsByTagName('canvas')[0]
        if (drawing.getContext) {
            //取得图像的数据 URI
            let imgURI = drawing.toDataURL('image/png');
            //显示img格式图片开始(不要此步可以跳过)
            let image = document.createElement('img');
            image.src = imgURI;
            let alink = document.createElement('a');
            alink.target = '_self';
            alink.href = imgURI;
            alink.download = '二维码';
            alink.click();
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
