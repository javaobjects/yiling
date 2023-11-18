<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <el-form ref="form" :model="data" :rules="rules" label-width="auto">
        <div class="common-box box-1 top-bar">
          <div class="header-bar mar-b-10">
            <div class="sign"></div>基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="intro"><span class="font-title-color">商品名称：</span>{{ data.name || '- -' }}</div>
            </el-col>
            <el-col :span="6">
              <div class="intro" v-if="form.goodsType !== 7"><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo || '- -' }}</div>
              <div class="intro" v-else><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo || '- -' }}</div>
            </el-col>
            <el-col :span="6">
              <div class="intro"><span class="font-title-color">售卖规格：</span>{{ data.sellSpecifications || '- -' }}</div>
            </el-col>
            <el-col :span="6">
              <div class="intro"><span class="font-title-color">基本单位：</span>{{ data.unit || '- -' }}</div>
            </el-col>

          </el-row>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="intro"><span class="font-title-color">生产地址：</span>{{ data.manufacturerAddress || '- -' }}</div>
            </el-col>
            <el-col :span="6">
              <div class="intro"><span class="font-title-color">生产厂家：</span>{{ data.manufacturer || '- -' }}</div>
            </el-col>
          </el-row>
          <!-- 详细信息 -->
          <div>
            <div class="header-bar mar-b-10">
              <div class="sign"></div>详细信息
            </div>
            <div class="img-box">
              <div class="intro"><span class="font-title-color">商品图片 <span v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.picBasicsInfoList == 0">--</span> </span></div>
              <div class="flex-wrap imgs" v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.picBasicsInfoList">
                <el-image v-for="(img, index) in data.standardGoodsAllInfo.picBasicsInfoList" :key="index" class="img" :src="img.picUrl" />
              </div>
              <!-- 说明书 -->
              <!-- 说明书 -->
              <div class="intro"><span class="font-title-color">说明书</span></div>

              <!-- 1 普通药品 -->
              <div v-if="form.goodsType == 1" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">成分：{{ goodsInstructionsInfo.drugDetails || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">性状：{{ goodsInstructionsInfo.drugProperties || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">适应症：{{ goodsInstructionsInfo.indications || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">用法用量：{{ goodsInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">不良反应：{{ goodsInstructionsInfo.adverseEvents || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">禁忌：{{ goodsInstructionsInfo.contraindication || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">注意事项：{{ goodsInstructionsInfo.noteEvents || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">药物相互作用：{{ goodsInstructionsInfo.interreaction || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ goodsInstructionsInfo.shelfLife || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">执行标准：{{ goodsInstructionsInfo.executiveStandard || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">贮藏：{{ goodsInstructionsInfo.storageConditions || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">包装：{{ goodsInstructionsInfo.packingInstructions || '- -' }}</div>
                  </div>
                </div>
              </div>
              <!-- 中药饮片说明书 2 -->
              <div v-if="form.goodsType == 2" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ decoctionInstructionsInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">净含量：{{ decoctionInstructionsInfo.netContent || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">包装清单：{{ decoctionInstructionsInfo.packingList || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">原产地：{{ decoctionInstructionsInfo.sourceArea || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">储藏：{{ decoctionInstructionsInfo.store || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">用法用量：{{ decoctionInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
              </div>
              <!-- 中药材说明书 3 -->
              <div v-if="form.goodsType == 3" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">性状：{{ materialsInstructionsInfo.drugProperties || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">功效：{{ materialsInstructionsInfo.effect || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保证期：{{ materialsInstructionsInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">性味：{{ materialsInstructionsInfo.propertyFlavor || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">储藏：{{ materialsInstructionsInfo.store || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">用法用量：{{ materialsInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
              </div>
              <!-- 消杀说明书 4 -->
              <div v-if="form.goodsType == 4" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">成分：{{ disinfectionInstructionsInfo.drugDetails || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">注意事项：{{ disinfectionInstructionsInfo.noteEvents || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ disinfectionInstructionsInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">灭菌类别：{{ disinfectionInstructionsInfo.sterilizationCategory || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">使用方法：{{ disinfectionInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                  <div style="width: 506px;">
                  </div>
                </div>
              </div>
              <!-- 保健食品说明书 5 -->
              <div v-if="form.goodsType == 5" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ healthInstructionsInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">保健功能：{{ healthInstructionsInfo.healthcareFunction || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">辅料：{{ healthInstructionsInfo.ingredients || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">原料：{{ healthInstructionsInfo.rawMaterial || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">储藏：{{ healthInstructionsInfo.store || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">适宜人群：{{ healthInstructionsInfo.suitablePeople || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">不适宜人群：{{ healthInstructionsInfo.unsuitablePeople || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">食用量及食用方法：{{ healthInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">注意事项：{{ healthInstructionsInfo.noteEvents || '- -' }}</div>
                  </div>
                  <div style="width: 506px;">
                  </div>
                </div>
              </div>
              <!-- 食品说明书 6 -->
              <div v-if="form.goodsType == 6" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">致敏源信息：{{ foodsInstructionsInfo.allergens || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">配料：{{ foodsInstructionsInfo.ingredients || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ foodsInstructionsInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">储藏：{{ foodsInstructionsInfo.store || '- -' }}</div>
                  </div>
                </div>
              </div>
              <!-- 医疗器械说明书 7 -->
              <div v-if="form.goodsType == 7" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">结构组成：{{ medicalInstrumentInfo.structure || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">注意事项：{{ medicalInstrumentInfo.noteEvents || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">适用范围：{{ medicalInstrumentInfo.useScope || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">使用说明：{{ medicalInstrumentInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">储藏条件：{{ medicalInstrumentInfo.storageConditions || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">包装：{{ medicalInstrumentInfo.packingInstructions || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">有效期：{{ medicalInstrumentInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">备注：{{ medicalInstrumentInfo.remark || '- -' }}</div>
                  </div>
                </div>
              </div>
              <!-- 配方颗粒说明书 2 -->
              <div v-if="form.goodsType == 8" class="intro-box font-title-color font-size-base">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ dispensingGranuleInfo.expirationDate || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">净含量：{{ dispensingGranuleInfo.netContent || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">包装清单：{{ dispensingGranuleInfo.packingList || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">原产地：{{ dispensingGranuleInfo.sourceArea || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">储藏：{{ dispensingGranuleInfo.store || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">用法用量：{{ dispensingGranuleInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="header-bar mar-b-10">
            <div class="sign"></div>其他信息
          </div>
          <div class="intro"><span class="font-title-color">供货渠道：</span></div>
          <div class="select-box flex-row-left mar-t-8 mar-l-8">
            <el-form-item label="" prop="goodsLines">
              <!-- 查看 -->
              <el-checkbox-group v-if="type === 'see'" v-model="form.goodsLines">
                <el-checkbox v-for="(item, index) in goodsLine" :key="index" :disabled="type === 'see'" :label="item.label">{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            <!-- 编辑 -->
              <el-checkbox-group v-else v-model="form.goodsLines">
                 <el-checkbox :disabled="form.goodsLines2 == 'POP'" label="POP">以岭分销商城</el-checkbox>
                 <el-checkbox :disabled="form.goodsLines3 == 'B2B'" label="B2B">大运河采购商城</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
          <div class="intro" v-if="currentEnterpriseInfo.yilingFlag"><span class="font-title-color">主体企业：</span></div>
          <div class="select-box flex-row-left mar-t-8 mar-l-8">
            <el-form-item label="" size="normal" v-if="currentEnterpriseInfo.yilingFlag" prop="eid">
              <el-select v-model="data.eid" :disabled="type === 'see'" placeholder="请选择" filterable>
                <el-option v-for="item in bussinessList" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button v-if="type == 'edit'" type="primary" @click="saveChange">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getProductDetail, addOrEditProducts, getEnterpriseMainPart } from '@/subject/pop/api/zt_api/zt_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent, goodsLine } from '@/subject/pop/utils/busi'
import { mapGetters } from 'vuex'
export default {
  components: {},
  computed: {
    goodsLine() {
      return goodsLine()
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
        goodsLines: [],
        goodsType: '',
        goodsLines2: '',
        goodsLines3: ''
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
      // 医疗器械说明书
      medicalInstrumentInfo: {
        structure: '',
        noteEvents: '',
        useScope: '',
        usageDosage: '',
        storageConditions: '',
        packingInstructions: '',
        expirationDate: '',
        remark: ''
      },
      // 配方颗粒
      dispensingGranuleInfo: {
        expirationDate: '',
        netContent: '',
        packingList: '',
        sourceArea: '',
        store: '',
        usageDosage: ''
      },
      rules: {
        eid: [{ required: true, message: '请选择主体企业', trigger: 'change' }]
      },
      type: '',
      bussinessList: [],
      isEditInfo: true
    }
  },
  created() {
    this.getMainList()
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.id = parseFloat(this.id)
      this.getData()
    }
    this.$nextTick(() => {
      // type: see -只查看 edit 编辑   again -重新提交
      this.type = this.$route.query.type
    })
    this.$log(this.$route)
  },
  methods: {
    // 获取以岭主体企业列表
    async getMainList() {
      let data = await getEnterpriseMainPart()
      console.log(data);
      if (data !== undefined) {
        this.bussinessList = data.list
      }
    },
    // 获取商品详情
    async getData() {
      this.$common.showLoad()
      let data = await getProductDetail(this.id)
      this.$common.hideLoad()

      if (data !== undefined) {
        this.data = data
        if (data.goodsLineVO) {
          if (data.goodsLineVO.popFlag) { this.form.goodsLines.push('POP'); this.form.goodsLines2 = 'POP' }
          if (data.goodsLineVO.mallFlag) { this.form.goodsLines.push('B2B'); this.form.goodsLines3 = 'B2B' }
        }
        this.form.goodsType = data.standardGoodsAllInfo.baseInfo.goodsType ? data.standardGoodsAllInfo.baseInfo.goodsType : ''

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
        else if (data.standardGoodsAllInfo.medicalInstrumentInfo) {
          // 医疗器械
          this.medicalInstrumentInfo = data.standardGoodsAllInfo.medicalInstrumentInfo
        }
        else if (data.standardGoodsAllInfo.dispensingGranuleInfo) {
          // 配方颗粒
          this.dispensingGranuleInfo = data.standardGoodsAllInfo.dispensingGranuleInfo
        }
      }
    },
    // 保存
    async saveChange() {
      let obj = {
        'goodsId': this.data.id,
        'mallFlag': 0,
        'popFlag': 0
      }
      if (this.form.goodsLines.indexOf('POP') > -1) {
        obj.popFlag = 1
      }
      if (this.form.goodsLines.indexOf('B2B') > -1) {
        obj.mallFlag = 1
      }
      this.$common.showLoad()
      let data = await addOrEditProducts(
        this.data.id,
        this.data.eid,
        obj
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('保存成功')
        this.$router.push('/zt_products/products_list')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
