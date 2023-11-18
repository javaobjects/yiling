<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <el-form 
      ref="form" 
      :model="form" 
      :rules="rules" 
      label-width="auto" 
      label-position="top">
      <div class="app-container-content has-bottom-bar">
        <div class="top-bar">
          <div class="header-bar mar-b-12">
            <div class="sign"></div>基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="6" :offset="0">
              <el-form-item label="商品名称" prop="name">
                <el-input type="string" :disabled="isEdit === 'addNorms'" v-model="form.name"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="批准文号/生产许可证号" prop="licenseNo" v-if="form.goodsType !== 7">
                <el-input type="string" :disabled="isEdit === 'addNorms'" v-model="form.licenseNo"></el-input>
              </el-form-item>
              <el-form-item label="备案凭证编号/注册证编号" prop="licenseNo" v-else>
                <el-input type="string" :disabled="isEdit === 'addNorms'" v-model="form.licenseNo"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="生产厂家" prop="manufacturer">
                <el-input type="string" :disabled="isEdit === 'addNorms'" v-model="form.manufacturer"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" :offset="0">
              <el-form-item label="生产地址">
                <el-input type="string" :disabled="isEdit === 'addNorms'" v-model="form.manufacturerAddress"></el-input>
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
          <div class="header-bar mar-b-10">
            <div class="sign"></div>详细信息
          </div>
          <div class="color-666">商品图片</div>
          <div class="packing-img-box mar-t-8">
            <div 
              class="packing-img" 
              v-for="(item, index) in fileList" 
              :key="index" 
              @mouseenter="showDialog(index)" 
              @mouseleave="hideDialog(index)"
            >
              <el-image class="packing-image" :src="item.picUrl" fit="contain" :lazy="true"></el-image>
              <div class="packing-cover" v-if=" packingShow && index === packingCurrent">
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
          <!-- 说明书  商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品	7-医疗器械 8-配方颗粒-->
          <div class="color-666 mar-t-16 mar-b-16">说明书</div>
          <!-- 普通药品说明书 -->
          <div v-if="form.goodsType == 1">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="成分：" prop="drugDetails" label-position="left">
                  <!-- // 标准库id ，表示已经关联标准库 如果关联了标准库，不能编辑说明书信息 -->
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.drugDetails"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="性状：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.drugProperties"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="适应症：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.indications"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="用法用量：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="不良反应：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.adverseEvents"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="禁忌：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.contraindication"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="药物相互作用：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.interreaction"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.storageConditions"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="执行标准：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.executiveStandard"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.shelfLife"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="包装：" prop="" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="goodsInstructionsInfo.packingInstructions"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 中药饮片说明书 2 -->
          <div v-if="form.goodsType == 2">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="净含量：" prop="netContent" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.netContent"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="包装清单：" prop="packingList" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.packingList"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="原产地：" prop="sourceArea" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.sourceArea"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="用法用量：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="decoctionInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 中药材说明书 3 -->
          <div v-if="form.goodsType == 3">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="性状：" prop="drugProperties" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="materialsInstructionsInfo.drugProperties"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="功效：" prop="effect" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="materialsInstructionsInfo.effect"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="性味：" prop="propertyFlavor" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="materialsInstructionsInfo.propertyFlavor"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="materialsInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="用法与用量：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="materialsInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 消杀说明书 4 -->
          <div v-if="form.goodsType == 4">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="成分：" prop="drugDetails" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="disinfectionInstructionsInfo.drugDetails"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="noteEvents" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="disinfectionInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="disinfectionInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="灭菌类别：" prop="sterilizationCategory" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="disinfectionInstructionsInfo.sterilizationCategory"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="使用方法：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="disinfectionInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 保健食品说明书 5 -->
          <div v-if="form.goodsType == 5">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="保健功能：" prop="healthcareFunction" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.healthcareFunction"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="辅料：" prop="ingredients" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.ingredients"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="原料：" prop="netCrawMaterialontent" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.rawMaterial"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="适宜人群：" prop="suitablePeople" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.suitablePeople"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="不适宜人群：" prop="unsuitablePeople" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.unsuitablePeople"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="食用量及食用方法：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：" prop="noteEvents" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="healthInstructionsInfo.noteEvents"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
          </div>
          <!-- 食品说明书 6 -->
          <div v-if="form.goodsType == 6">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="致敏源信息：" prop="allergens" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="foodsInstructionsInfo.allergens"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="配料：" prop="ingredients" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="foodsInstructionsInfo.ingredients"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="foodsInstructionsInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="foodsInstructionsInfo.store"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 医疗器械说明书 7 -->
          <div v-if="form.goodsType == 7">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="结构组成：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.structure" placeholder="请输入结构组成" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="注意事项：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.noteEvents" placeholder="请输入注意事项" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="适用范围：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.useScope" placeholder="请输入适用范围" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="使用说明：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.usageDosage" placeholder="请输入使用说明" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="储藏条件：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.storageConditions" placeholder="请输入储藏条件" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="包装：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.packingInstructions" placeholder="请输入包装" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12" :offset="0">
                <el-form-item label="有效期：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.expirationDate" placeholder="请输入有效期" />
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="备注：">
                  <el-input type="textarea" v-model="medicalInstrumentInfo.remark" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 配方颗粒说明书 8 -->
          <div v-if="form.goodsType == 8">
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12">
                <el-form-item label="保质期：" prop="expirationDate" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.expirationDate"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="净含量：" prop="netContent" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.netContent"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12">
                <el-form-item label="包装清单：" prop="packingList" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.packingList"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="原产地：" prop="sourceArea" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.sourceArea"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="goodsInstructionsInfo">
              <el-col :span="12">
                <el-form-item label="储藏：" prop="store" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.store"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用法用量：" prop="usageDosage" label-position="left">
                  <el-input type="textarea" :disabled="form.standardId" v-model="dispensingGranuleInfo.usageDosage"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!-- 产品线信息 -->
          <div class="header-bar mar-b-10">
            <div class="sign"></div>其他信息
          </div>
          <div class="select-box flex-row-left mar-t-8 " v-if="currentEnterpriseInfo.yilingFlag">
            <el-form-item label="主体企业：" size="normal" prop="eid">
              <el-select 
                v-model="form.eid" 
                value-key="" 
                placeholder="请选择" 
                clearable 
                filterable>
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
import { againProducts, getEnterpriseMainPart, getStandardStandardGoodsDetail } from '@/subject/pop/api/zt_api/zt_products'
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
          title: '添加商品',
          path: '/zt_products/products_add'
        },
        {
          title: '新增商品'
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
      // 配方颗粒说明书信息
      dispensingGranuleInfo: {
        expirationDate: '',
        netContent: '',
        packingList: '',
        sourceArea: '',
        store: '',
        usageDosage: ''
      },
      type: '',
      rules: {
        name: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
          // { max: 20, message: '长度请控制在20以内', trigger: 'blur' }
        ],
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
      isEdit: ''
    }
  },
  created() {
    this.getMainList()
  },
  mounted() {
    //  add : 新增商品    addNorms:新增规格   again:重新申请
    this.isEdit = this.$route.query.type
    if (this.isEdit === 'add') {
      this.form.goodsType = this.$route.query.goodsType
      this.form.isCn = this.$route.query.isCn
      this.form.standardId = 0
      this.navList.push({ title: '新增商品' })
    } else if (this.isEdit === 'addNorms') {
      if (this.$route.params.id) {
        this.id = this.$route.params.id
        this.id = parseFloat(this.id)
        this.getStandardData()
        this.navList.push({ title: '新增规格' })
      }
    }
  },
  methods: {
    // 新增规格时，获取标准商品详情
    async getStandardData() {
      let data = await getStandardStandardGoodsDetail(0, this.id)
      if (data !== undefined) {
        //  包装图片
        if (data.picBasicsInfoList && data.picBasicsInfoList.length > 0) {
          this.fileList = data.picBasicsInfoList
        }
        if (data.baseInfo) {
          this.form.name = data.baseInfo.name
          this.form.licenseNo = data.baseInfo.licenseNo
          this.form.manufacturer = data.baseInfo.manufacturer
          this.form.manufacturerAddress = data.baseInfo.manufacturerAddress
          this.form.goodsType = data.baseInfo.goodsType
        }
        /**
           * 
           *  根据商品类别不同，说明书数据data不同
           *  商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品	7-医疗器械 8-配方颗粒
           * 
           * */
        if (data.baseInfo && data.baseInfo.goodsType == 1) {
          // 普通药品
          this.goodsInstructionsInfo = data.goodsInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 2) {
          // 中药饮片
          this.decoctionInstructionsInfo = data.decoctionInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 3) {
          // 中药材
          this.materialsInstructionsInfo = data.materialsInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 4) {
          // 消杀
          this.disinfectionInstructionsInfo = data.disinfectionInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 5) {
          // 保健食品
          this.healthInstructionsInfo = data.healthInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 6) {
          // 食品
          this.foodsInstructionsInfo = data.foodsInstructionsInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 7) {
          // 医疗器械
          this.foodsInstructionsInfo = data.medicalInstrumentInfo
        }
        else if (data.baseInfo && data.baseInfo.goodsType == 8) {
          // 配方颗粒
          this.dispensingGranuleInfo = data.dispensingGranuleInfo
        }
      }
    },
    // 获取以岭主体企业列表
    async getMainList() {
      let data = await getEnterpriseMainPart()
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
    // 新增规格
    async AddNormsSubmit() {
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
        // id: '',
        goodsType: this.form.goodsType,
        name: this.form.name,
        eid: this.form.eid,
        licenseNo: this.form.licenseNo,
        manufacturer: this.form.manufacturer,
        manufacturerAddress: this.form.manufacturerAddress,
        specifications: this.form.specifications,
        unit: this.form.unit,
        picBasicsInfoList: arr,
        sellSpecificationsId: 0,
        standardId: this.id
      }
      let data = againProducts(obj)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('新增成功')
        this.$router.push('/zt_products/products_add')
      }
    },
    // 新增 
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
        medicalInstrumentInfo: this.form.goodsType == 7 ? this.medicalInstrumentInfo : {},
        dispensingGranuleInfo: this.form.goodsType == 8 ? this.dispensingGranuleInfo : {},
        sellSpecificationsId: 0,
        standardId: 0
      }
      let data = await againProducts(obj)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('保存成功')
        this.$router.push('/zt_products/products_list')
      }
    },
    // 包装删除图片
    deletPackingImg(index) {
      this.fileList.splice(index, 1)
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
      this.fileList.push(obj)
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>