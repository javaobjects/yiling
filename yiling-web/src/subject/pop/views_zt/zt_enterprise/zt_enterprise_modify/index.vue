<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <el-form :model="form" :rules="rules" ref="dataForm">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>基本资料
          </div>
          <div class="content-box">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="6">
                  <div class="title"><span class="red-text"></span>企业类型</div>
                  <el-form-item>
                    <!-- <el-input v-model="form.type" maxlength="10" show-word-limit></el-input> -->
                    <p class="box-conter">
                      {{ form.type | dictLabel(companyType) }}
                    </p>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <div class="title"><span class="red-text">*</span>企业名称</div>
                  <el-form-item prop="name">
                    <el-input class="add-title" v-model="form.name" maxlength="30" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <div class="title"><span class="red-text">*</span>企业营业执照号/医疗机构许可证</div>
                  <el-form-item prop="licenseNumber">
                    <el-input class="add-title" v-model="form.licenseNumber" maxlength="20" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="6">
                  <div class="title"><span class="red-text">*</span>联系人姓名</div>
                  <el-form-item prop="contactor">
                    <el-input class="add-title" v-model="form.contactor" maxlength="5" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <div class="title"><span class="red-text">*</span>联系人电话</div>
                  <el-form-item prop="contactorPhone">
                    <el-input class="add-title" v-model="form.contactorPhone" maxlength="11" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
                 <el-col :span="12">
                  <div class="title"><span class="red-text">*</span>企业地址</div>
                  <el-form-item prop="regionCode">
                    <div class="flex-row-left">
                      <yl-choose-address2
                        :province.sync="form.province"
                        :city.sync="form.city"
                        :area.sync="form.area"
                      />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="6">
                  <div class="title"><span class="red-text">*</span>详细地址</div>
                  <el-form-item prop="address">
                    <el-input class="add-title" v-model="form.address" maxlength="50" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </el-form>
      <div class="bottom-box">
        <div class="header-bar"><div class="sign"></div>企业资质信息</div>
          <div class="flex-wrap img-view">
            <div class="flex-wrap-div">
              <div class="item" v-for="(item, index) in datalist" :key="index" :style="{height:item.periodRequired ? '270px' : '200px'}">
              <!-- <div class="pic-desc"> <span class="asterisk" v-show="item.required">*</span>{{ item.name }}</div> -->
              <div class="img-box flex-row-center">
                <yl-upload
                :default-url="item.fileUrl"
                :extral-data="{type: 'enterpriseCertificate'}"
                :width="'239px'"
                :height="'159px'"
                @onSuccess="(val) => onSuccess(val, item, index)"
              />
                <!-- <img :src="item.fileUrl"> -->
                <!-- <p class="grad hover-grad">
                  <span>重新上传</span>
                </p> -->
              </div>
              <div class="item-bottom">
                <div class="pic-desc"> 
                  <!-- <span class="asterisk" v-show="item.required">*</span> -->
                  <span>{{ item.name }}</span>
                  <span @click="seeClick(item)">查看示例</span>
                </div>
                <div class="pic-desc-bottom" v-if="item.periodRequired">
                  <div class="pic-desc-radio">
                    <span>是否长期有效：</span>
                    <el-radio-group v-model="item.longEffective" @change="radioChange(item)">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </div>
                  <div class="pic-desc-time" v-show="item.longEffective==0">
                    <span class="pic-desc-radio-span"><span class="asterisk" v-show="item.periodRequired">*</span>资质有效期：</span>
                    <el-date-picker
                    v-model="item.scTime"
                    type="daterange"
                    format="yyyy/MM/dd"
                    value-format="yyyy-MM-dd"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                    </el-date-picker>
                  </div>
                  <div class="pic-desc-time" v-show="item.longEffective==1">
                    <span class="pic-desc-radio-span"><span class="asterisk" v-show="item.periodRequired">*</span>资质有效期：</span>
                    <el-date-picker
                      v-model="item.periodBegin"
                      type="date"
                      clearable
                      format="yyyy/MM/dd"
                      value-format="yyyy-MM-dd"
                      placeholder="开始日期">
                    </el-date-picker>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom-bar-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservationClick" >保存</yl-button> 
    </div>
    <!-- 图片放大 -->
    <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
  </div>
</template>
<script>
import { getEnterpriseInfo, updateEnterpriseInfo } from '@/subject/pop/api/zt_api/zt_enterprise'
import { enterpriseType } from '@/subject/pop/utils/busi'
import ylChooseAddress2 from '../components/ChooseAddress2/index.vue';
import { ylUpload } from '@/subject/pop/components'
import { formatDate } from '@/subject/pop/utils'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
export default {
  components: {
    ylChooseAddress2, ylUpload, ElImageViewer
  },
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/zt_dashboard'
        },
        {
          title: '企业信息管理',
          path: ''
        },
        {
          title: '企业信息修改'
        }
      ],
      form: {
        type: '',
        name: '',
        licenseNumber: '',
        provinceCode: '',
        cityCode: '',
        regionCode: '',
        contactorPhone: '',
        address: '',
        contactor: '',
        cityName: '',
        provinceName: '',
        regionName: '',
        eid: '',
        province: '',
        city: '',
        area: ''
      },
      company: {},
      rules: {
        name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        licenseNumber: [{ required: true, message: '请输入企业营业执照号/医疗机构许可证', trigger: 'blur' }],
        regionCode: [{ required: true, message: '请选择省市区', trigger: 'blur' }],
        contactorPhone: [{ required: true, message: '请输入电话号码', trigger: 'blur' }],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        contactor: [{ required: true, message: '请输入联系人', trigger: 'blur' }]
      },
      datalist: [],
      showViewer: false,
      imageList: []
    }
  },
  mounted() {
    this.getInfo()
  },
  methods: {
    async getInfo() {
      let data = await getEnterpriseInfo({});
      if (data != undefined) {
        this.form = {
          name: data.name,
          licenseNumber: data.licenseNumber,
          provinceCode: data.provinceCode,
          cityCode: data.cityCode,
          regionCode: data.regionCode,
          contactorPhone: data.contactorPhone,
          address: data.address,
          contactor: data.contactor,
          type: data.type,
          cityName: data.cityName,
          provinceName: data.provinceName,
          regionName: data.regionName,
          eid: data.eid,
          province: data.provinceCode + ';' + data.provinceName,
          city: data.cityCode + ';' + data.cityName,
          area: data.regionCode + ';' + data.regionName
        }
        for (let i = 0; i < data.certificateVoList.length; i++) {
          data.certificateVoList[i].scTime = [formatDate(data.certificateVoList[i].periodBegin,'yyyy-MM-dd'),formatDate(data.certificateVoList[i].periodEnd, 'yyyy-MM-dd')];
          data.certificateVoList[i].periodBegin = formatDate(data.certificateVoList[i].periodBegin,'yyyy-MM-dd')
        }
        this.datalist = data.certificateVoList;
      }
    },
    onSuccess(val,item,index) {
      item.fileUrl = val.url;
      item.fileKey = val.key;
    },
    // 保存
    preservationClick() {
      let dataImg = this.datalist;
      let imgData = [];
      for (let i = 0; i < dataImg.length; i++) {
        // 判断资质是否必填 是
        if (dataImg[i].required) {
          if (dataImg[i].fileUrl == '' && dataImg[i].fileKey=='') {
            this.$common.warn('请填写完整带有 * 的企业资质信息')
            return
          } else {
            if (dataImg[i].periodRequired) {
              if (dataImg[i].longEffective == 0) { //是否长期有效 不是
                if (dataImg[i].scTime[0] =='- -' || dataImg[i].scTime.length < 1) {
                  this.$common.warn('请填写完整带有 * 的企业资质有效期')
                  return
                } else {
                  imgData.push({
                    fileKey: dataImg[i].fileKey,
                    fileUrl: dataImg[i].fileUrl,
                    name: dataImg[i].name,
                    periodBegin: dataImg[i].scTime[0],
                    periodEnd: dataImg[i].scTime[1],
                    id: dataImg[i].id,
                    longEffective: dataImg[i].longEffective,
                    required: dataImg[i].required,
                    type: dataImg[i].type
                  })
                }
              } else {
                // 长期有效
                if (dataImg[i].periodBegin == '- -' || dataImg[i].periodBegin === null) {
                  this.$common.warn('请填写完整带有 * 的企业资质有效期')
                  return
                } else {
                  imgData.push({
                    fileKey: dataImg[i].fileKey,
                    fileUrl: dataImg[i].fileUrl,
                    name: dataImg[i].name,
                    periodBegin: dataImg[i].periodBegin,
                    periodEnd: '',
                    id: dataImg[i].id,
                    longEffective: dataImg[i].longEffective,
                    required: dataImg[i].required,
                    type: dataImg[i].type
                  })
                 
                }
              }
            } else {
              // if (dataImg[i].longEffective == 0) { //是否长期有效 不是
                imgData.push({
                  fileKey: dataImg[i].fileKey,
                  fileUrl: dataImg[i].fileUrl,
                  name: dataImg[i].name,
                  periodBegin: '',
                  periodEnd: '',
                  // periodBegin: dataImg[i].scTime[0] =='- -' ? '' : dataImg[i].scTime[0],
                  // periodEnd: dataImg[i].scTime[1] =='- -' ? '' : dataImg[i].scTime[1],
                  id: dataImg[i].id,
                  longEffective: dataImg[i].longEffective,
                  required: dataImg[i].required,
                  type: dataImg[i].type
                })
              // } else {
              //   imgData.push({
              //     fileKey: dataImg[i].fileKey,
              //     fileUrl: dataImg[i].fileUrl,
              //     name: dataImg[i].name,
              //     periodBegin: dataImg[i].periodBegin,
              //     periodEnd: '',
              //     id: dataImg[i].id,
              //     longEffective: dataImg[i].longEffective,
              //     required: dataImg[i].required,
              //     type: dataImg[i].type
              //   })
              // }
            }
          }
        } else { // 资质是否必填 否
          //资质有效期是否必填  是
          if (dataImg[i].periodRequired) { 
            if (dataImg[i].longEffective == 0) { //是否长期有效 不是
              if (dataImg[i].scTime[0] =='- -' || dataImg[i].scTime.length < 1) {
                this.$common.warn('请填写完整带有 * 的企业资质有效期')
                return
              } else {
                imgData.push({
                  fileKey: dataImg[i].fileKey,
                  fileUrl: dataImg[i].fileUr,
                  name: dataImg[i].name,
                  periodBegin: dataImg[i].scTime[0] =='- -' ? '' : dataImg[i].scTime[0],
                  periodEnd: dataImg[i].scTime[1] =='- -' ? '' : dataImg[i].scTime[1],
                  id: dataImg[i].id,
                  longEffective: dataImg[i].longEffective,
                  required: dataImg[i].required,
                  type: dataImg[i].type
                })
              }
            } else { //是否长期有效 是
              if (dataImg[i].periodBegin == '- -' || dataImg[i].periodBegin === null) {
                this.$common.warn('请填写完整带有 * 的企业资质有效期')
                return
              } else {
                imgData.push({
                  fileKey: dataImg[i].fileKey,
                  fileUrl: dataImg[i].fileUr,
                  name: dataImg[i].name,
                  periodBegin: dataImg[i].periodBegin,
                  periodEnd: '',
                  id: dataImg[i].id,
                  longEffective: dataImg[i].longEffective,
                  required: dataImg[i].required,
                  type: dataImg[i].type
                })
              }
            }
          } else { //资质有效期 非必填
            // if (dataImg[i].longEffective == 0) { //是否长期有效 不是
              imgData.push({
                fileKey: dataImg[i].fileKey,
                fileUrl: dataImg[i].fileUr,
                name: dataImg[i].name,
                periodBegin: '',
                periodEnd: '',
                // periodBegin: dataImg[i].scTime[0] =='- -' ? '' : dataImg[i].scTime[0],
                // periodEnd: dataImg[i].scTime[1] =='- -' ? '' : dataImg[i].scTime[1],
                id: dataImg[i].id,
                longEffective: dataImg[i].longEffective,
                required: dataImg[i].required,
                type: dataImg[i].type
              })
            // } 
            // else {
            //   imgData.push({
            //     fileKey: dataImg[i].fileKey,
            //     fileUrl: dataImg[i].fileUr,
            //     name: dataImg[i].name,
            //     periodBegin: dataImg[i].periodBegin,
            //     periodEnd: '',
            //     id: dataImg[i].id,
            //     longEffective: dataImg[i].longEffective,
            //     required: dataImg[i].required,
            //     type: dataImg[i].type
            //   })
            // }
          }
        }  
      }
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form;
          let province = form.province.split(';');
          let city = form.city.split(';');
          let area = form.area.split(';');
          this.$common.showLoad();
          let data = await updateEnterpriseInfo(
            form.address,
            imgData,
            city[0],
            city[1],
            form.contactor,
            form.contactorPhone,
            form.eid,
            form.licenseNumber,
            form.name,
            province[0],
            province[1],
            area[0],
            area[1]
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
    // 点击 切换 是否长期有效
    radioChange(val) {
      val.periodBegin = null;
      val.scTime = [];
    },
    // 点击 查看示例
    seeClick(val) {
      if (val.fileUrl != '') {
        this.imageList = [val.fileUrl];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
  // .img-box ::v-deep .image-uploader{
  //   width: 200px;
  //   height: 200px;
  // }
  .img-box ::v-deep .el-upload-dragger{
    width: 239px !important;
    height: 159px !important;
    background-color: rgba(255,255,255,0);
    border: none
  }
  .img-box ::v-deep .single-image-upload{
    width: 239px !important;
    height: 159px !important;
  }
  .img-box ::v-deep .single-image-upload .image-uploader .avatar{
    width: auto;
    height: 159px;
  }
  .pic-desc-time ::v-deep div{
    margin-top: 4px;
    width: 220px !important;
  }
  .pic-desc-time ::v-deep .el-input__prefix{
    display: none;
  }
  // .pic-desc-time ::v-deep .el-input__suffix{
  //   display: none;
  // }
  .pic-desc-time ::v-deep .el-input__inner{
    padding: 0;
    text-align: center;
  }
  .pic-desc-radio ::v-deep .el-radio{
    margin-right: 10px;
  }
  .pic-desc-time ::v-deep .el-date-editor .el-range__icon{
     display: none !important;
  }

</style>
