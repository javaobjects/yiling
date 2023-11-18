<template>
  <!-- 内容区域 -->
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="search-bar">
        <div class="flex-row-left">
          <div class="flex1">
            <div class="intro"><span class="font-title-color">企业名称：</span>{{ companyData.name }}</div>
            <div class="intro"><span class="font-title-color">银行账户：</span>{{ companyData.account }}</div>
          </div>
          <div class="flex1">
            <div class="intro"><span class="font-title-color">开户地：</span>{{ companyData.provinceName }} {{ companyData.cityName }}</div>
            <div class="intro"><span class="font-title-color">支行信息：</span>{{ companyData.subBankName }}</div>
          </div>
          <div class="flex1">
            <div class="intro"><span class="font-title-color">开户行：</span>{{ companyData.bankName }}</div>
            <div class="intro"><span class="font-title-color">审核状态：</span><span :style="{color: companyData.status==3 ? '#ee0a24':(companyData.status==2 ? '#42b983' : '#faab0c') }"> {{ companyData.status | dictLabel(shztData) }} </span>
            <span >  </span>
            </div>
          </div>
        </div>
        <div class="sh_div" v-if="companyData.status == 3">
          <span>审核失败原因：</span>{{ companyData.auditRemark }}
        </div>
        <div class="intro"><span class="font-title-color">开户许可证：</span></div>
        <div class="app-container-imgs">
          <ul>
            <li v-for="(item, index) in companyData.khxkz" :key="index">
              <!-- <img :src="item.img" alt=""> -->
              <!-- <el-image class="elImage" :src="item.img"></el-image> -->
              <el-image :src="item.img">
                <div slot="error" class="image-slots">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <p class="grad hover-grad" @click="scalePicSize(item)">
                <img src="@/subject/pop/assets/zt_enterprise/enlarge.png" alt="">
              </p>
            </li>
          </ul>
        </div>
        <div class="intro"><span class="font-title-color">其他证照：</span></div>
        <div class="app-container-imgs">
          <ul>
            <li v-for="(item, index) in companyData.qtzm" :key="index">
              <el-image class="elImage" :src="item.img">
                <div slot="error" class="image-slots">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <p class="grad hover-grad" @click="scalePicSize(item)">
                <img src="@/subject/pop/assets/zt_enterprise/enlarge.png" alt="">
              </p>
            </li>
          </ul>
        </div>
      </div>
      <el-image-viewer v-if="showViewer" :on-close="onClose" :url-list="imageList"/>
      <div class="bottom-bar-view flex-row-center" v-if="ztType">
        <yl-button type="primary" v-role-btn="['1']" plain @click="addClick">新增</yl-button> 
      </div>
      <div class="bottom-bar-view flex-row-center" v-if="companyData.status!=1 && ztType != true">
        <yl-button type="primary" v-role-btn="['3']" plain @click="addClick"> 修改 </yl-button> 
      </div>
    </div>
    
  </div>
</template>

<script>
import { queryReceiptAccount } from '@/subject/pop/api/b2b_api/financial'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import { b2bReceiptAccountStatus } from '@/subject/pop/utils/busi'
export default {
  components: {
    ElImageViewer
  },
  computed: {
    shztData() {
      return b2bReceiptAccountStatus()
    }
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
      companyData: {
        name: '',
        account: '',
        provinceName: '',
        cityName: '',
        subBankName: '',
        bankName: '',
        status: '',
        auditRemark: '',
        khxkz: [
          {
            img: ''
          }
        ],
        qtzm: [
          {
            img: ''
          }
        ]
      },
      imageList: [],
      showViewer: false,
      ztType: false
    }
  },
  mounted() {
    this.dataList();
  },
  methods: {
    async dataList() {
      let data = await queryReceiptAccount();
      if (data != undefined) {
        console.log(data.accountVo,99)
        if (data.accountVo === null){
          this.ztType = true;
        } else {
          let val = data.accountVo;
          this.companyData = {
            name: val.name,
            account: val.account,
            provinceName: val.provinceName,
            cityName: val.cityName,
            subBankName: val.subBankName,
            bankName: val.bankName,
            status: val.status,
            auditRemark: val.auditRemark,
            khxkz: [{
              img: data.accountOpeningPermitUrl
            }],
            qtzm: [{
              img: val.licenceUrl
            }]
          }
        }
      }
    },
    // 图片放大
    scalePicSize(val){
      if (val.img != '') {
        this.imageList = [val.img];
        this.showViewer = true;
      }
    },
    onClose() {
      this.imageList = [];
      this.showViewer = false;
    },
    // 点击添加
    addClick() {
      let data = ''; // 1修改 2新增
      if (this.companyData.status == 2 || this.companyData.status == 3 && this.ztType == false){
        data = 1
      } else {
        data = 2
      } 
      this.$router.push('/financial/add_collection_account/' + data);
    }
  }
}
</script>
<style lang="scss" scoped>
  @import "./index.scss";
  .app-container-imgs ::v-deep .el-image{
    width: 100%;
    height: 159px;
    position: relative;
    cursor: pointer;
    text-align: center;
  }
  .app-container-imgs ::v-deep .el-image img {
    width: auto;
    height: 100%;
  }
  .app-container-imgs ::v-deep .image-slots{
    width: 100%;
    height: 159px;
    line-height: 159px;
    // border: 1px solid red !important;
    font-size: 40px;
    color: #666;
    text-align: center;
    // position: absolute;
    // top: 50%;
    // left: 50%;
    // transform: translate(-50%,-50%);
    // margin: 20px auto;
  }
</style>