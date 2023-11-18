<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="auto" label-position="top">
      <div class="app-container-content has-bottom-bar">
        <div class="top-bar">
          <div class="header-bar mar-b-12">
            <div class="sign"></div>基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="6" :offset="0">
              <el-form-item label="商品名称" prop="name">
                <el-input type="string" v-model="form.name"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="批准文号/生产许可证号" prop="licenseNo">
                <el-input type="string" v-model="form.licenseNo"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="生产厂家" prop="manufacturer">
                <el-input type="string" v-model="form.manufacturer"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">

              <el-form-item label="生产地址">
                <el-input type="string" v-model="form.manufacturerAddress"></el-input>
              </el-form-item>
            </el-col>

          </el-row>
          <el-row :gutter="24">
            <el-col :span="6" :offset="0">
              <el-form-item label="包装规格" prop="specifications">
                <el-input type="string" v-model="form.specifications"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="基本单位" prop="unit">
                <el-input type="string" v-model="form.unit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 详细信息 -->
          <div class="header-bar mar-b-12">
            <div class="sign"></div>详细信息
          </div>
          <div class="color-666">商品图片</div>
          <div class="packing-img-box mar-t-8">
            <div class="packing-img" v-for="(item,index) in fileList" :key="index" @mouseenter="showDialog(index)" @mouseleave="hideDialog(index)">
              <el-image class="packing-image" :src="item.picUrl" fit="contain" :lazy="true"></el-image>
              <div class="packing-cover" v-if=" packingShow && index === packingCurrent ">
                <i @click="deletPackingImg(index)"></i>
              </div>
            </div>
            <!-- 图片上传组件 -->
            <ylUploadImage @onSuccess="handleAddUrlSucess" :limit="10" :extral-data="uploadData" :show-file-list="false">
              <div>
                <i class="el-icon-plus"></i>
              </div>
            </ylUploadImage>
          </div>
          <!-- 说明书 -->
          <div class="color-666 mar-t-16">说明书</div>

          <!-- 普通药品说明书 -->
          <div v-if="form.goodsType == 1">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="成分：" prop="drugDetails" label-position="left">
                  <!-- // 标准库id ，表示已经关联标准库 如果关联了标准库，不能编辑说明书信息 -->
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.drugDetails"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="性状：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.drugProperties"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">

              <el-col :span="12" :offset="0">
                <el-form-item label="适应症：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.indications"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="用法用量：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">

              <el-col :span="12" :offset="0">
                <el-form-item label="不良反应：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.adverseEvents"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="禁忌：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.contraindication"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="药物相互作用：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.interreaction"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row class="goodsInstructionsInfo">

              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.storageConditions"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="执行标准：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.executiveStandard"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.shelfLife"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="包装：" prop="" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="goodsInstructionsInfo.packingInstructions"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 中药饮片说明书 2 -->
          <div v-if="form.goodsType == 2">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="净含量：" prop="netContent" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.netContent"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="包装清单：" prop="packingList" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.packingList"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="原产地：" prop="sourceArea" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.sourceArea"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="用法用量：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="decoctionInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <!-- 中药材说明书 3 -->
          <div v-if="form.goodsType == 3">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="性状：" prop="drugProperties" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.drugProperties"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="功效：" prop="effect" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.effect"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保证期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="性味：" prop="propertyFlavor" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.propertyFlavor"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="用法与用量：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="materialsInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 消杀说明书 4 -->
          <div v-if="form.goodsType == 4">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="成分：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="disinfectionInstructionsInfo.drugDetails"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="noteEvents" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="disinfectionInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="disinfectionInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="灭菌类别：" prop="sterilizationCategory" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="disinfectionInstructionsInfo.sterilizationCategory"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="使用方法：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="disinfectionInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 保健食品说明书 5 -->
          <div v-if="form.goodsType == 5">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="保健功能：" prop="healthcareFunction" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.healthcareFunction"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="辅料：" prop="ingredients" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.ingredients"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="原料：" prop="netCrawMaterialontent" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.rawMaterial"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="适宜人群：" prop="suitablePeople" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.suitablePeople"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="不适宜人群：" prop="unsuitablePeople" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.unsuitablePeople"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="食用量及食用方法：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="noteEvents" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="healthInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
          </div>
          <!-- 食品说明书 6 -->
          <div v-if="form.goodsType == 6">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="致敏源信息：" prop="allergens" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="foodsInstructionsInfo.allergens"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="配料：" prop="ingredients" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="foodsInstructionsInfo.ingredients"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="foodsInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="isEditInfo" v-model="foodsInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 产品线信息 -->
          <div class="header-bar mar-b-10">
            <div class="sign"></div>其他信息
          </div>
          <!-- <div class="intro"><span class="font-title-color">主体企业</span></div> -->
          <div class="select-box flex-row-left mar-t-8" v-if="currentEnterpriseInfo.yilingFlag">
            <el-form-item label="主体企业：" size="normal" prop="eid">
              <el-select v-model="form.eid" value-key="" placeholder="请选择" clearable filterable>
                <el-option v-for="item in bussinessList" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>

      </div>

    </el-form>
    <div class="flex-row-center bottom-view" v-if="type !== 'see'">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="saveChange">{{ type === 'reSubmit' ? '重新提交' : '保存' }}</yl-button>
    </div>
  </div>
</template>

<script>
import { againProducts, getProductDetail, getEnterpriseMainPart } from '@/subject/pop/api/zt_api/zt_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent, standardGoodsType } from '@/subject/pop/utils/busi'
import { ylUploadImage } from '@/subject/pop/components/index'
import { mapGetters } from 'vuex'
export default {
  components: {
    ylUploadImage
  },
  computed: {
    // 药品类型
    standardGoodsType() {
      return standardGoodsType()
    },
    goodStatus() {
      return goodsStatus()
    },
    companyType() {
      return enterpriseType()
    },
    goodsReason() {
      return goodsOutReason()
    },
    // 专利非专利
    patent() {
      return goodsPatent()
    },
    isYiLing() {
      let user = getCurrentUser()
      let flag = null;
      if (user.currentEnterpriseInfo) {
        if (user.currentEnterpriseInfo.yilingFlag) {
          flag = !!user.currentEnterpriseInfo.yilingFlag
        }
      }
      return flag
    },
    // currentEnterpriseInfo.yilingFlag 判断登陆主体企业是否是以岭本部
    ...mapGetters([
      'currentEnterpriseInfo'
    ])
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
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表',
          path: '/zt_products/products_list'
        },
        {
          title: '商品详情'
        }
      ],
      data: {
        standardGoodsAllInfo: {
          baseInfo: {
            specificationInfo: []
          }
        }
      },
      form: {
        id: '',
        goodsType: '',
        isCn: '',
        name: '',
        eid: '',
        licenseNo: '',
        manufacturer: '',
        manufacturerAddress: '',
        specifications: '',
        unit: '',
        goodsLines: [],
        // 标准库id  ，表示已经关联标准库 如果关联了标准库，不能编辑说明书信息
        standardId: ''
      },
      // 普通药品说明书信息
      goodsInstructionsInfo: {
        drugDetails: '',
        drugProperties: '',
        executiveStandard: '',
        indications: '',
        usageDosage: '',
        adverseEvents: '',
        contraindication: '',
        noteEvents: '',
        storageConditions: '',
        shelfLife: '',
        interreaction: '',
        packingInstructions: ''
      },
      // 中药饮片说明书信息
      decoctionInstructionsInfo: {
        expirationDate: '',
        netContent: '',
        packingList: '',
        sourceArea: '',
        store: '',
        usageDosage: ''
      },
      // 中药材说明信息
      materialsInstructionsInfo: {
        drugProperties: '',
        effect: '',
        expirationDate: '',
        propertyFlavor: '',
        store: '',
        usageDosage: ''
      },
      // 消杀
      disinfectionInstructionsInfo: {
        drugDetails: '',
        expirationDate: '',
        noteEvents: '',
        sterilizationCategory: '',
        usageDosage: ''
      },
      // 保健品
      healthInstructionsInfo: {
        expirationDate: '',
        healthcareFunction: '',
        ingredients: '',
        rawMaterial: '',
        store: '',
        suitablePeople: '',
        unsuitablePeople: '',
        usageDosage: '',
        noteEvents: ''
      },
      // 食品
      foodsInstructionsInfo: {
        allergens: '',
        expirationDate: '',
        ingredients: '',
        store: ''
      },
      type: '',
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        licenseNo: [{ required: true, message: '请输入批准文号', trigger: 'blur' }],
        unit: [{ required: true, message: '请输入基本单位', trigger: 'blur' }],
        manufacturer: [{ required: true, message: '请输入生产厂家', trigger: 'blur' }],
        specifications: [{ required: true, message: '请输入售卖规格', trigger: 'blur' }],
        eid: [{ required: true, message: '请选择主体企业', trigger: 'change' }]
      },
      fileList: [],
      uploadData: { type: 'goodsPicture' },
      // 主体企业
      bussinessList: [],
      packingShow: false,
      packingCurrent: -1,
      isEdit: '',
      isEditInfo: false

    }
  },
  created() {
    this.getMainList()
  },
  mounted() {
    console.log(this.$route);
    //    again:重新申请
    this.isEdit = this.$route.query.type
    if (this.isEdit === 'again') {
      if (this.$route.params.id) {
        this.id = this.$route.params.id
        this.id = parseFloat(this.id)
        this.getData()
      }
    }
    // this.$nextTick(() => {
    //   // type: see -只查看 reSubmit -重新提交 为空则为编辑
    //   this.type = this.$route.query.type
    // })
  },
  methods: {
    // 获取商品详情
    async getData() {
      this.$common.showLoad()
      let data = await getProductDetail(this.id)
      this.$common.hideLoad()
      this.$log(data)
      if (data !== undefined) {
        this.form.id = data.id
        this.form.name = data.name
        this.form.eid = data.eid
        this.form.licenseNo = data.licenseNo
        this.form.manufacturer = data.manufacturer
        this.form.manufacturerAddress = data.manufacturerAddress
        this.form.specifications = data.specifications
        this.form.unit = data.unit
        // 标准库。表示已经关联标准库
        this.form.standardId = data.standardId
        if (this.form.standardId) {
          this.isEditInfo = true;
        } else {
          this.isEditInfo = false;
        }
        this.form.goodsType = data.standardGoodsAllInfo.baseInfo.goodsType ? data.standardGoodsAllInfo.baseInfo.goodsType : ''
        //  包装图片
        if (data.standardGoodsAllInfo.picBasicsInfoList && data.standardGoodsAllInfo.picBasicsInfoList.length > 0) {
          this.fileList = data.standardGoodsAllInfo.picBasicsInfoList
        }
        // 产品线
        if (data.goodsLineVO) {
          if (data.goodsLineVO.popFlag == 1) { this.form.goodsLines.push('POP') }
          if (data.goodsLineVO.mallFlag == 1) { this.form.goodsLines.push('B2B') }
        }
        /**
         * 
         *  根据商品类别不同，说明书数据data不同
         *  商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品	
         * 
         * */
        if (data.standardGoodsAllInfo.goodsInstructionsInfo) {
          // 普通药品
          this.goodsInstructionsInfo = data.standardGoodsAllInfo.goodsInstructionsInfo
        }
        else if (data.standardGoodsAllInfo.decoctionInstructionsInfo) {
          // 中药饮片
          this.decoctionInstructionsInfo = data.standardGoodsAllInfo.decoctionInstructionsInfo
        }
        else if (data.standardGoodsAllInfo.materialsInstructionsInfo) {
          // 中药材
          this.materialsInstructionsInfo = data.standardGoodsAllInfo.materialsInstructionsInfo
        }
        else if (data.standardGoodsAllInfo.disinfectionInstructionsInfo) {
          // 消杀
          this.disinfectionInstructionsInfo = data.standardGoodsAllInfo.disinfectionInstructionsInfo
        }
        else if (data.standardGoodsAllInfo.healthInstructionsInfo) {
          // 保健食品
          this.healthInstructionsInfo = data.standardGoodsAllInfo.healthInstructionsInfo
        }
        else if (data.standardGoodsAllInfo.foodsInstructionsInfo) {
          // 食品
          this.foodsInstructionsInfo = data.standardGoodsAllInfo.foodsInstructionsInfo
        }
      }
    },
    // 获取以岭主体企业列表
    async getMainList() {
      let data = await getEnterpriseMainPart()
      console.log(data);
      if (data !== undefined) {
        this.bussinessList = data.list
      }
    },
    // 保存
    async saveChange() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.isEdit === 'addNorms') {
            // 新增规格
            this.AddNormsSubmit()
          } else {
            // 新增，编辑，或重新提交
            this.addOrEdit()
          }
        } else {
          return false
        }
      })
    },
    // 新增 编辑 或重新提交
    async addOrEdit() {
      let arr = []
      for (let i = 0; i < this.fileList.length; i++) {
        let obj = {}
        obj.id = this.fileList[i].id || ''
        obj.pic = this.fileList[i].pic
        obj.picDefault = this.fileList.picDefault || ''
        obj.picOrder = i
        arr.push(obj)
      }
      this.$common.showLoad()
      let obj = {
        id: this.form.id,
        goodsType: this.form.goodsType,
        name: this.form.name,
        eid: this.form.eid,
        licenseNo: this.form.licenseNo,
        manufacturer: this.form.manufacturer,
        manufacturerAddress: this.form.manufacturerAddress,
        specifications: this.form.specifications,
        unit: this.form.unit,
        picBasicsInfoList: arr,
        goodsInstructionsInfo: this.form.goodsType == 1 ? this.goodsInstructionsInfo : {},
        decoctionInstructionsInfo: this.form.goodsType == 2 ? this.decoctionInstructionsInfo : {},
        materialsInstructionsInfo: this.form.goodsType == 3 ? this.materialsInstructionsInfo : {},
        disinfectionInstructionsInfo: this.form.goodsType == 4 ? this.disinfectionInstructionsInfo : {},
        healthInstructionsInfo: this.form.goodsType == 5 ? this.healthInstructionsInfo : {},
        foodsInstructionsInfo: this.form.goodsType == 6 ? this.foodsInstructionsInfo : {},
        sellSpecificationsId: 0,
        standardId: 0
      }
      let data = await againProducts(obj)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('保存成功')
        // this.getData()
        this.$router.push('/zt_products/products_list')
      }
    },
    // 包装删除图片
    deletPackingImg(index) {
      console.log(index);
      this.fileList.splice(index, 1)
      console.log(this.fileList);
    },
    // 查看大图
    changeBigUrl(url) {
      this.show = true
      this.bigUrl = url
    },
    confirm() {
      this.show = false
    },
    //显示操作项
    showDialog(index, item) {
      console.log(index);
      this.packingShow = true;
      this.packingCurrent = index;
    },
    //隐藏蒙层
    hideDialog(index, item) {
      this.packingShow = false;
      this.packingCurrent = -1;
    },
    //
    tableImgShowDialog(index) {
      this.tableShow = true;
      this.packingCurrent = index;
    },
    tableImgHideDialog(index) {
      this.tableShow = false;
      this.packingCurrent = -1
    },
    //  商品图片上传图片成功
    handleAddUrlSucess(res) {
      let obj = res
      obj.pic = obj.key
      obj.picUrl = obj.url
      console.log(res);
      this.fileList.push(obj)
      console.log(this.fileList);

    },
    //大图预览
    handlePictureCardPreview(file, fileList) {
      //   this.dialogVisible = true;
      //   this.$log(file);
      //   if (file.response != null) {
      //     this.dialogImageUrl = file.response.data.url;
      //   } else {
      //     this.dialogImageUrl = "";
      //   }
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss" scoped>
.top-bar {
  ::v-deep .el-form-item__label {
    font-weight: 400 !important;
    color: $font-title-color;
    padding: 0 0 8px;
  }
  .el-input {
    width: 320px;
  }
}
</style>
