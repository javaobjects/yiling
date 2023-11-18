<template>
  <div>
    <!-- 内容区 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="auto">
      <div class="common-box box-1 top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息
        </div>
        <el-row :gutter="24">
          <el-col :span="6">
            <div class="intro"><span class="font-title-color">商品名称：</span>{{ data.name || '- -' }}</div>
          </el-col>
          <el-col :span="6">
            <div class="intro"><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo || '- -' }}</div>
          </el-col>
          <el-col :span="6">
            <div class="intro"><span class="font-title-color">售卖规格：</span>{{ specificationInfo[0] ? specificationInfo[0].sellSpecifications : '- -' }}</div>
          </el-col>
          <el-col :span="6">
            <div class="intro"><span class="font-title-color">基本单位：</span>{{ specificationInfo[0] ? specificationInfo[0].unit : '- -' }}</div>
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
            <div class="intro"><span class="font-title-color">商品图片</span></div>
            <div class="flex-wrap imgs">
              <el-image v-for="(img, index) in picBasicsInfoList" :key="index" class="img" :src="img.picUrl" />
            </div>
            <!-- 1 普通药品 -->
            <div v-if="data.goodsType == 1" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 2" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 3" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 4" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 5" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 6" class="intro-box font-title-color font-size-base">
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
            <div v-if="data.goodsType == 7" class="intro-box font-title-color font-size-base">
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
          </div>
        </div>
        <div class="header-bar mar-b-10">
          <div class="sign"></div>其他信息
        </div>
        <div class="intro"><span class="font-title-color">使用产品线：</span></div>
        <div class="select-box flex-row-left mar-t-8 mar-l-8">
          <el-form-item label="" prop="goodsLines">
            <el-checkbox-group v-model="form.goodsLines">
              <el-checkbox v-for="(item, index) in goodsLine" :disabled="type === 'see'" :label="item.label" :key="index">{{ item.label }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div class="intro" v-if="currentEnterpriseInfo.yilingFlag">
          <span class="font-title-color">主体企业：</span>
        </div>
        <div class="select-box flex-row-left mar-t-8 mar-l-8">
          <el-form-item label="" size="normal" v-if="currentEnterpriseInfo.yilingFlag" prop="eid">
            <el-select v-model="form.eid" :disabled="type === 'see'" placeholder="请选择" filterable>
              <el-option v-for="item in bussinessList" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
      </div>
    </el-form>
    <!-- <div class="flex-row-center bottom-view">
      <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
      <yl-button type=" primary" @click="submit">保存</yl-button>
    </div> -->
  </div>
</template>

<script>
import { addOrEditProducts, getStandardStandardGoodsDetail, getEnterpriseMainPart } from '@/subject/pop/api/zt_api/zt_products'
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
  props: {
    // 获取的商品id
    selectGoodsId: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      // baseInfo信息
      data: {},
      // 图片
      picBasicsInfoList: [],
      // 说明书信息
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
      // 规格信息
      specificationInfo: [],
      type: '',
      rules: {
        eid: [{ required: true, message: '请选择主体企业', trigger: 'change' }]
      },
      form: {
        goodsLines: [],
        eid: ''
      },
      linesList: ['POP', 'B2B'],
      bussinessList: []
    }
  },
  watch: {
    selectGoodsId: {
      handler(newVal, oldVal) {
        console.log(newVal);
        if (newVal.SpecificationsId !== -1 && newVal.standardId !== -1) {
          this.getData();
        }
      },
      deep: true
    }
  },
  mounted() {
    this.getMainList()
  },
  methods: {
    async getData() {
      this.$common.showLoad()
      //  params  规格的id
      let data = await getStandardStandardGoodsDetail(this.selectGoodsId.SpecificationsId, this.selectGoodsId.standardId)
      this.$common.hideLoad()
      console.log(data);
      if (data !== undefined) {
        this.data = data.baseInfo
        this.picBasicsInfoList = data.picBasicsInfoList
        /**
        * 
        *  根据商品类别不同，说明书数据data不同
        *  商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品	
        * 
        **/
        if (data.goodsInstructionsInfo) {
          // 普通药品
          this.goodsInstructionsInfo = data.goodsInstructionsInfo
        }
        else if (data.decoctionInstructionsInfo) {
          // 中药饮片
          this.decoctionInstructionsInfo = data.decoctionInstructionsInfo
        }
        else if (data.materialsInstructionsInfo) {
          // 中药材
          this.materialsInstructionsInfo = data.materialsInstructionsInfo
        }
        else if (data.disinfectionInstructionsInfo) {
          // 消杀
          this.disinfectionInstructionsInfo = data.disinfectionInstructionsInfo
        }
        else if (data.healthInstructionsInfo) {
          // 保健食品
          this.healthInstructionsInfo = data.healthInstructionsInfo
        }
        else if (data.foodsInstructionsInfo) {
          // 食品
          this.foodsInstructionsInfo = data.foodsInstructionsInfo
        }
        else if (data.medicalInstrumentInfo) {
          // 医疗器械
          this.medicalInstrumentInfo = data.medicalInstrumentInfo
        }
        this.specificationInfo = data.specificationInfo

      }
    },
    // 获取以岭主体企业列表
    async getMainList() {
      this.$common.showLoad()
      let data = await getEnterpriseMainPart()
      this.$common.hideLoad()
      if (data !== undefined) {
        this.bussinessList = data.list
      }
    },
    // 保存
    submit(id) {
      let obj = {
        'goodsId': this.form.id,
        'mallFlag': 0,
        'popFlag': 0
      }
      if (this.form.goodsLines.indexOf('POP') > -1) {
        obj.popFlag = 1
      }
      if (this.form.goodsLines.indexOf('B2B') > -1) {
        obj.mallFlag = 1
      }
      let _this = this;

      this.$refs.form.validate(async valid => {
        if (valid) {
          this.$common.showLoad()
          let data = await addOrEditProducts(
            // id 选择规格之后，张爽说的id 传0
            0,
            this.form.eid,
            obj,
            this.data.goodsType,
            this.data.isCn,
            this.selectGoodsId.SpecificationsId,
            this.selectGoodsId.standardId,
            // sellSpecificationsId,，
            // standardId,
            this.data.licenseNo,
            this.data.manufacturer,
            this.data.manufacturerAddress,
            this.data.name,
            // specifications,
            this.specificationInfo[0].sellSpecifications,
            this.specificationInfo[0].unit
            // unit
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('保存成功')
            _this.$emit('change', true)
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
// @import './index.scss';
@import '~@/common/styles/mixin.scss';

.app-container {
  padding-bottom: 80px;
  .reject-view {
    margin-bottom: 16px;
    .textarea {
      width: 550px;
      color: $font-title-color;
      background: #f7f7f7;
      border-radius: 4px;
      border: 1px solid rgba(38, 38, 38, 0.08);
      padding: 11px 12px;
      .desc {
        width: 528px;
        height: 53px;
        overflow-x: hidden;
        overflow-y: scroll;
        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }
  .top-bar {
    background: $white;
    padding: 16px;
    border-radius: 4px;
    .intro {
      color: $font-important-color;
      font-size: $font-size-base;
      line-height: $font-size-base-lh;
      margin-bottom: 8px;
      padding: 0 10px;
      span {
        color: $font-title-color;
      }
    }
  }
  .box-1 {
    color: $font-important-color;
    font-size: $font-size-large;
    line-height: $font-size-large-lh;
    .input-view {
      width: 100%;
      margin-top: 10px;
      margin-left: 16px;
      .input-1 {
        .el-input {
          margin-top: 10px;
          width: 190px;
        }
      }
      .input-2 {
        margin-left: 190px;
        .el-input {
          margin-top: 10px;
          width: 320px;
        }
      }
      .pak-box {
        .flex-wrap {
          margin-top: 12px;
        }
        .el-input {
          margin-top: 10px;
          width: 320px;
        }
        .box {
          position: relative;
          width: 103px;
          height: 32px;
          background: #ffffff;
          border-radius: 16px;
          border: 1px solid #d8d8d8;
          color: $font-title-color;
          line-height: 32px;
          font-size: 16px;
          margin-right: 16px;
          i {
            position: absolute;
            right: 5px;
            top: 8px;
            font-size: 16px;
            color: $font-light-color;
            cursor: pointer;
          }
          .not-touch {
            cursor: not-allowed;
          }
        }
      }
    }
    .img-box {
      margin-top: 7px;
      margin-bottom: 31px;
      .imgs {
        margin-bottom: 17px;
        .img {
          width: 100px;
          height: 100px;
          margin-left: 8px;
          margin-right: 16px;
          border-radius: 4px;
          border: 1px solid #d8d8d8;
        }
      }
      .intro-box {
        margin-left: 8px;
        margin-bottom: 16px;
        margin-top: 16px;
        .box-view {
          overflow: hidden;
          width: 506px;
          height: 61px;
          background: #f7f7f7;
          border-radius: 4px;
          padding: 10px 12px 11px 12px;
          border: 1px solid rgba(38, 38, 38, 0.08);
          .box {
            width: 482px;
            height: 40px;
            overflow-x: hidden;
            overflow-y: scroll;
            &::-webkit-scrollbar {
              display: none;
            }
          }
        }
        .marr150 {
          margin-right: 150px;
        }
        .marb8 {
          margin-bottom: 8px;
        }
      }
    }
  }
  .box-3 {
    margin-top: 16px;
    color: $font-important-color;
    font-size: $font-size-large;
    line-height: $font-size-large-lh;
    .select-box {
      padding: 10px 16px;
      .select {
        flex: 1;
        span {
          padding-bottom: 8px;
          margin-right: 16px;
        }
        .el-input,
        .el-select {
          width: 280px;
        }
      }
    }
  }
  .bottom-view {
    position: fixed;
    bottom: 0;
    left: $sideBarWidth;
    right: 0;
    height: 64px;
    background-color: $white;
    padding: 16px;
    z-index: 10;
    box-shadow: 0px -2px 9px 0px rgba(182, 182, 182, 0.16);
  }
}
.unit-box {
  display: flex;
}
.unit-content {
  display: flex;
}
.unit-list {
  display: flex;
  align-items: center;
  height: 49px;
  padding: 0 12px;
  background: #fafafa;
  color: #666;
  ::v-deep .el-input__inner {
    height: 32px;
    border-radius: 0;
  }
}
.mar-l-35 {
  margin-left: 35px;
}

.unit-label {
  &::before {
    content: '*';
    color: #eb1616;
    // margin-right: 4px;
  }
}
.unit-icon {
  width: 23px;
  height: 49px;
  display: flex;
  align-items: center;
  margin-left: 11px;
  cursor: pointer;
  img {
    width: 23px;
    height: 23px;
  }
}
.color-333 {
  color: #333;
}
</style>
