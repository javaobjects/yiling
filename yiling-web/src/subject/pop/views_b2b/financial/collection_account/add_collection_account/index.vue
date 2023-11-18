<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="search-bar">
        <el-form :model="query" :rules="rules" ref="dataForm" label-width="150px" class="demo-ruleForm">
          <el-form-item label="企业名称" prop="name">
            <el-input class="add-title" v-model="query.name" placeholder="请输入企业名称" />
            <span> 请填写开户许可证上正确的企业名称</span>
          </el-form-item>
          <el-form-item label="开户行" prop="zBanks">
             <el-select
                class="add-title"
                v-model="query.zBanks"
                filterable
                remote
                reserve-keyword
                placeholder="请输入需搜索的银行名称"
                @focus="jobRemoteMethod()"
                @change="zBankChange"
                :remote-method="jobRemoteMethod"
                :loading="jobLoading">
                <div slot="prefix" class="el-icon-search"></div>
                <el-option
                  v-for="item in bannkDatas"
                  :key="item.headCode+';'+item.id+';'+item.headName"
                  :label="item.headName"
                  :value="item.headCode+';'+item.id+';'+item.headName">
                </el-option>
              </el-select>
            <!-- <el-input class="add-title" v-model="query.bankName" placeholder="请输入开户行" /> -->
            <!-- <el-select class="add-title" v-model="query.bankName" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select> -->
          <span> 请填写开户许可证上正确的开户行</span>
          </el-form-item>
          <el-form-item label="支行信息">
            <!-- <el-input class="add-title" v-model="query.subBankName" placeholder="请输入支行信息" /> -->
            <el-select
              class="add-title"
              v-model="query.fBanks"
              filterable
              remote
              reserve-keyword
              clearable
              placeholder="请输入需搜索的分行名称"
              @focus="jobRemoteMethod2()"
              @change="fBankChange"
              :remote-method="jobRemoteMethod2"
              :loading="jobLoading2">
              <div slot="prefix" class="el-icon-search"></div>
              <el-option
                v-for="item in bannkDatas2"
                :key="item.headCode+';'+item.id+';'+item.branchName"
                :label="item.branchName"
                :value="item.headCode+';'+item.id+';'+item.branchName">
                <span style="float: left">{{ item.branchName }}</span>
                <span style="float: right;"></span>
              </el-option>
            </el-select>
            <span> 请填写开户许可证上正确的支行信息</span>
          </el-form-item>
          <el-form-item label="开户地" prop="province">
            <div class="row-br-choose">
              <yl-choose-address2
                :province.sync="query.province"
                :city.sync="query.city"
                :area-show.sync="areaShow"/>
            </div>
          <span class="row-br-choose-span"> 请填写开户许可证上正确的开户地省市</span>
          </el-form-item>
          <el-form-item label="银行账户" prop="account">
            <el-input v-model.trim="query.account" @input="e => (query.account = checkInput(e))" class="add-title" placeholder="请输入银行账户" />
            <span> 请填写开户许可证上正确的银行账户</span>
          </el-form-item>
          <el-form-item label="开户许可证" prop="" class="app-container-imgs">
            <el-image class="elImage" :src="query.accountOpeningPermitUrl">
              <div slot="error" class="image-slots">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
            <!-- <yl-upload style="display:inline-block" :limit="5" :extral-data="{type: 'orderReceiveOneReceipt'}" :default-url="query.accountOpeningPermitUrl" @onSuccess="onSuccess"></yl-upload> -->
          </el-form-item>
          <el-form-item label="其他证照" prop="">
              <yl-upload style="display:inline-block" :limit="5" :extral-data="{type: 'b2bReceiptAccount'}" :default-url="query.licenceUrl" @onSuccess="onSuccess"></yl-upload>
          </el-form-item>
        </el-form>
      </div>
    </div> 
    <div class="flex-bottom-view">

    </div>
    <!-- <div class="bottom-view flex-row-center" :style="{ left: sidebarWidth }">
      <yl-button type="primary">保存</yl-button>
    </div> -->
     <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveChange">保存</yl-button>
      </div>
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>

<script>
import { queryReceiptAccount, saveOrUpdateReceiptAccount, queryBankPageList } from '@/subject/pop/api/b2b_api/financial'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
// import { getLocation } from '@/subject/pop/api/common'
import { ylUpload } from '@/subject/pop/components'
import ylChooseAddress2 from '../components/ChooseAddress2/index.vue';
export default {
  components: {
    ylUpload, ElImageViewer,ylChooseAddress2
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '财务管理',
          path: ''
        },
        {
          title: '企业收款账户'
        }
      ],
      query: {
        name: '',
        account: '',
        provinceCode: '',
        cityCode: '',
        subBankName: '',
        bankName: '',
        status: '',
        accountOpeningPermitUrl: '',
        licence: '',
        licenceUrl: '',
        id: '',
        provinceName: '',
        cityName: '',
        province: '',
        city: '',
        zBank: '',
        fBank: '',
        zBanks: '',
        fBanks: ''
      },
      rules: {
        name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        zBanks: [{ required: true, message: '请选择开户行名称', trigger: 'change' }],
        fBanks: [{ required: true, message: '请选择支行信息', trigger: 'change' }],
        province: [{ required: true, message: '请选择开户地', trigger: 'change' }],
        account: [{ required: true, message: '请输入银行账户', trigger: 'blur' }]

      },
      cityData: [],
      provinceList: [], //省
      MultipleImg: true,
      imgData: [], // 上传的图片
      imageList: [],
      showViewer: false,
      areaShow: false,
      bannkDatas: [],//银行数据
      bannkDatas2: [],//分行数据
      bannk: {
        page: 1,
        limit: 30,
        name: '',
        type: 1
      },
      // 分行
      bannk2: {
        page: 1,
        limit: 30,
        name: '',
        type: 2
      },
      jobLoading: false,
      jobLoading2: false
    }
  },
  watch: {
    
  },
  mounted() {
    let query = this.$route.params;
    if (query.type == '1') {
      this.dataList(); //获取信息
    }
    this.bankApi();
    this.bankApi2();
  },
  methods: {
    async dataList() {
      let data = await queryReceiptAccount();
      if (data != undefined) {
        let val = data.accountVo;
          this.query = {
            name: val.name,
            account: val.account,
            provinceCode: val.provinceCode,
            provinceName: val.provinceName,
            cityCode: val.cityCode,
            cityName: val.cityName,
            subBankName: val.subBankName,
            bankName: val.bankName,
            status: val.status, 
            licence: val.licence,
            licenceUrl: val.licenceUrl,
            id: data.id,
            province: val.provinceCode+';'+val.provinceName,
            city: val.cityCode+';'+val.cityName,
            zBank: val.bankCode + ';' + val.bankId + ';' + val.bankName,
            zBanks: val.bankName,
            fBank: val.branchCode + ';' + val.branchBankId + ';' + val.subBankName,
            fBanks: val.subBankName
        }
        this.query.accountOpeningPermitUrl = data.accountOpeningPermitUrl;
      }
    },
    async bankApi() {
      this.jobLoading = true;
      let val = this.bannk;
      let data = await queryBankPageList(
        val.page,
        val.name,
        val.limit,
        val.type
      );
      if (data!=undefined) {
        this.bannkDatas = data.records
      }
      this.jobLoading = false;
    },
    async bankApi2() {
      this.jobLoading2 = true;
      let val = this.bannk2;
      let data = await queryBankPageList(
        val.page,
        val.name,
        val.limit,
        val.type
      );
      if (data!=undefined) {
        this.bannkDatas2 = data.records
      }
      this.jobLoading2 = false;
    },
    // 选中开户行
    zBankChange(val) {
      this.query.zBank = val;
    },
    // 选中支行
    fBankChange(val) {
      this.query.fBank = val;
    },
    jobRemoteMethod(val) {
      this.bannk.name = val;
      this.bankApi();
    },
    //分行
    jobRemoteMethod2(val) {
      this.bannk2.name = val;
      this.bankApi2();
    },
     // 图片放大
    scalePicSize(val){
      if (val.url != '') {
        this.imageList = [val.url];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // async getAddress(code) {
    //   let data = await getLocation(code)
    //   if (data) {
    //     if (this.query.province == '') {
    //       this.provinceList = data.list;
    //     } else {
    //       this.cityData = data.list;
    //     }
    //   }
    // },
    // 点击保存
    saveChange() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let query = this.query;
          let zBank = query.zBank.split(';');
          let fBank = query.fBank && query.fBank.length > 0 ? query.fBank.split(';') : [];
          if (zBank.length < 3) {
            this.$common.error('请选择开户行信息')
          } 
          // else if (fBank.length < 3) {
          //   this.$common.error('请选择支行信息')
          // } 
          else {
              let city = query.city.split(';');
              let province = query.province.split(';');
              let data = null;
              if (query.id == '') {
                this.$common.showLoad();
                data = await saveOrUpdateReceiptAccount({
                  account: query.account,
                  bankCode: zBank[0],
                  bankId: zBank[1],
                  bankName: zBank[2],
                  branchBankId: fBank && fBank.length > 1 ? fBank[1] : '',
                  branchCode: fBank && fBank.length > 0 ? fBank[0] : '',
                  cityCode: city[0],
                  cityName: city[1],
                  licenceFile: query.licence,
                  name: query.name,
                  provinceCode: province[0],
                  provinceName: province[1],
                  subBankName: fBank && fBank.length > 2 ? fBank[2] : ''
                })
                this.$common.hideLoad();
                if (data != undefined) {
                  this.$common.alert('新增成功', r => {
                    this.$router.go(-1)
                  })
                }
              } else {
                this.$common.showLoad();
                data = await saveOrUpdateReceiptAccount({
                  account: query.account,
                  bankCode: zBank[0],
                  bankId: zBank[1],
                  bankName: zBank[2],
                  branchBankId: fBank && fBank.length > 1 ? fBank[1] : '',
                  branchCode: fBank && fBank.length > 0 ? fBank[0] : '',
                  cityCode: city[0],
                  cityName: city[1],
                  licenceFile: query.licence,
                  name: query.name,
                  provinceCode: province[0],
                  provinceName: province[1],
                  receiptAccountId: query.id,
                  subBankName: fBank && fBank.length > 2 ? fBank[2] : ''
                })
                this.$common.hideLoad();
                if (data != undefined) {
                  this.$common.alert('保存成功', r => {
                    this.$router.go(-1)
                  })
                }
              }
              
          }
        }
      })
    },
    checkInput(val) {
      val = val.replace(/\s+/g, '');
      return val;
    },
    onSuccess(data){
      this.query.licenceUrl = data.url;
      this.query.licence = data.key;
      // this.imgData.push(data)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
  .app-container-imgs ::v-deep .el-image{
    width: 200px;
    height: 159px;
    border: 1px solid #EEEEEE;
    background: #FAFAFA;
    text-align: center;
  }
  .app-container-imgs ::v-deep .el-image img{
    width: auto;
    height: 100%;
  }
  .app-container-imgs ::v-deep .image-slots{
    width: 100%;
    height: 159px;
    line-height: 159px;
    font-size: 40px;
    color: #666;
    text-align: center;
  }
</style>