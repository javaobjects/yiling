<template>
  <div class="app-container">
    <div class="app-container-content">
      <el-form 
        ref="form" 
        :model="form" 
        :rules="rules" 
        label-width="216px" 
        label-position="right">
        <div class="search-bar">
          <ylToolTip class="mar-t-16">为保证药品库信息的完整性，请尽量填写非必填项</ylToolTip>
          <div>
            <el-row>
              <el-col :span="12">
                <el-form-item label="录入类型：" prop="goodsType">
                  <el-select v-model="form.goodsType" :disabled="isEdit !== 'add' && isEdit != 'toStandardAdd'" @change="changeGoodsType" placeholder="请选择录入类型">
                    <el-option 
                      v-for="item in standardGoodsType" 
                      :key="item.value" 
                      :label="item.label" 
                      :value="item.value"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="商品类型：" prop="isCn">
                  <el-radio-group v-model="form.isCn">
                    <el-radio v-for="item in standardGoodsIsCn" :key="item.value" :label="item.value">
                      {{ item.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="商品所属经营范围：" prop="businessScope">
                  <el-select v-model="form.businessScope" placeholder="请选择商品所属经营范围">
                    <el-option v-for="item in goodsBusinessScopeType" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 食品,肖杀品，保健食品，中药饮片，中药材显示 -->
                <el-form-item v-if="type === 2 || type === 4 || type === 5 || type === 6 || type === 8" label="产品名：" prop="name">
                  <el-input v-model="form.name" placeholder="请输入产品名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="type === 4" label="商品名：" prop="commonName">
                  <el-input v-model="form.aliasName" placeholder="请输入商品名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="通用名：" prop="name">
                  <el-input v-model="form.name" placeholder="请输入通用名" />
                </el-form-item>
                <!-- 食品,肖杀品，保健食品，中药饮片，中药材显示, 医疗器械 -->
                <el-form-item v-if="type === 7" label="产品名：" prop="name">
                  <el-input v-model="form.name" placeholder="请输入产品名" />
                </el-form-item>
                <el-form-item v-if="type === 2 || type === 5 || type === 6 || type === 8" label="商品名：" prop="commonName">
                  <el-input v-model="form.aliasName" placeholder="请输入商品名" />
                </el-form-item>
                <el-form-item v-if="type === 3" label="产品名：" prop="name">
                  <el-input v-model="form.name" placeholder="请输入产品名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="type === 7" label="商品名：" prop="commonName">
                  <el-input v-model="form.aliasName" placeholder="请输入商品名" />
                </el-form-item>
                <el-form-item v-if="type === 1" label="商品名：" prop="commonName">
                  <el-input v-model="form.aliasName" placeholder="请输入商品名" />
                </el-form-item>
                <!-- 食品显示 -->
                <el-form-item v-if="type === 6" label="产品类别：" prop="productClassification">
                  <el-input v-model="form.productClassification" placeholder="请输入产品类别" />
                </el-form-item>
                <!-- 保健食品,中药饮片,中药材显示在这里 -->
                <el-form-item v-if="type === 2 || type === 3 || type === 5 || type === 8" label="生产厂家：" prop="manufacturer" :rules="form.goodsType !== 3 ? rules.manufacturer : []">
                  <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" :label="form.businessScope === 1 ? '备案凭证编号：' : '注册证编号：'" prop="licenseNo">
                  <el-input v-model="form.licenseNo" :placeholder="form.businessScope === 1 ? '备案凭证编号' : '注册证编号'" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="批准日期：" prop="approvalDate" style="width:100%">
                  <el-date-picker type="date" value-format="yyyy-MM-dd" placeholder="选择批准日期" v-model="form.approvalDate"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1 || type === 5" label="批准文号：" prop="licenseNo">
                  <el-input v-model="form.licenseNo" placeholder="请输入批准文号" />
                </el-form-item>
                <!-- 食品，肖杀,中药饮片显示 -->
                <el-form-item v-if="type === 6 || type === 4 || type === 2 || type === 8" label="生产许可证号：" prop="licenseNo">
                  <el-input v-model="form.licenseNo" placeholder="请输入生产许可证号" />
                </el-form-item>
                <el-form-item v-if="type === 3" label="商品名：" prop="commonName">
                  <el-input v-model="form.aliasName" placeholder="请输入商品名" />
                </el-form-item>
              </el-col>
              <!-- 医疗器械显示 -->
              <el-col :span="12">
                <el-form-item v-if="type === 7" label="生产厂家：" prop="manufacturer" :rules="form.goodsType !== 3 ? rules.manufacturer : []">
                  <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 1-6都显示 -->
                <!-- 中药材类型 提交是验证生产地址 -->
                <el-form-item label="生产地址：" prop="manufacturerAddress" :rules="form.goodsType === 3 ? rules.manufacturerAddress : []">
                  <el-input v-model="form.manufacturerAddress" placeholder="请输入生产地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 药品， 食品，消杀都显示 -->
                <el-form-item v-if="type === 1 || type === 6 || type === 4" label="生产厂家：" prop="manufacturer" :rules="form.goodsType !== 3 ? rules.manufacturer : []">
                  <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="处方类型：" prop="otcType">
                  <el-select v-model="form.otcType" placeholder="请选择处方类型">
                    <el-option v-for="item in standardGoodsOtcType" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </el-form-item>
                <!-- 肖杀品显示 -->
                <el-form-item v-if="type === 4" label="含量：" prop="roughWeight">
                  <el-input v-model="form.roughWeight" placeholder="请输入含量" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="剂型：" prop="gdfName">
                  <el-input v-model="form.gdfName" placeholder="请输入剂型名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="剂型规格：" prop="gdfSpecifications">
                  <el-input v-model="form.gdfSpecifications" placeholder="请输入剂型规格" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 药品， 食品，消杀都显示 -->
                <el-form-item label="一级分类：" prop="standardCategoryId1">
                  <el-select v-model="form.standardCategoryId1" @change="selectChange" placeholder="请选择一级分类">
                    <el-option v-for="item in category" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品， 食品，消杀都显示  -->
                <el-form-item label="二级分类：" prop="standardCategoryId2">
                  <el-select v-model="form.standardCategoryId2" placeholder="请选择二级分类">
                    <el-option v-for="item in cateChild" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <!-- 医疗器械显示  -->
                <el-form-item v-if="type === 7" label="规格型号：">
                  <el-input v-model="form.gdfSpecifications" placeholder="请输入规格型号" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>其他信息
          </div>
          <div>
            <!-- 第一行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品,医疗器械显示 -->
                <el-form-item v-if="type === 1 || type === 7" label="是否医保：" prop="isYb">
                  <el-radio-group v-model="form.isYb">
                    <el-radio v-for="item in standardGoodsIsYb" :key="item.value" :label="item.value">
                      {{ item.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
                <!-- 食品显示 -->
                <el-form-item v-if="type === 6" label="产品标准号：" prop="productStandardCode">
                  <el-input v-model="form.productStandardCode" placeholder="请输入产品标准号" />
                </el-form-item>
                <!-- 消杀品，中药饮片显示，医疗器械显示 -->
                <el-form-item v-if="type === 4 || type === 2 || type === 8" label="执行标准：" prop="executiveStandard">
                  <el-input v-model="form.executiveStandard" placeholder="请输入执行标准" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="批准日期：" prop="approvalDate">
                  <el-date-picker type="date" value-format="yyyy-MM-dd" placeholder="选择批准日期" v-model="form.approvalDate"></el-date-picker>
                </el-form-item>
                <!-- 中药材显示 -->
                <el-form-item v-if="type === 3" label="来源：" prop="goodsSource">
                  <el-input v-model="form.goodsSource" placeholder="请输入来源" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="管制类型：" prop="controlType">
                  <el-radio-group v-model="form.controlType">
                    <el-radio v-for="item in standardGoodsControlType" :key="item.value" :label="item.value">
                      {{ item.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
                <!-- 中药饮片显示 -->
                <el-form-item v-if="type === 2 || type === 8" label="等级：" prop="goodsGrade">
                  <el-input v-model="form.goodsGrade" placeholder="请输入等级" />
                </el-form-item>

              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="特殊成分：" prop="specialComposition">
                  <el-radio-group v-model="form.specialComposition">
                    <el-radio v-for="item in standardGoodsSpecialComposition" :key="item.value" :label="item.value">
                      {{ item.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="备案人名称/注册人名称：">
                  <el-input v-model="form.registrant" placeholder="请输入备案人名称/注册人名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="药品本位码：" prop="goodsCode">
                  <el-input v-model="form.goodsCode" placeholder="请输入药品本位码" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="备案人地址/注册人地址：">
                  <el-input v-model="form.registrantAddress" placeholder="请输入地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="复方制剂成分：" prop="ingredient">
                  <el-input v-model="form.ingredient" placeholder="请输入复方制剂成分" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7 && form.isCn === 2" label="代理人名称：">
                  <el-input v-model="form.agent" placeholder="请输入代理人名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="质量标准类别：" prop="qualityType">
                  <el-select v-model="form.qualityType" placeholder="请选择">
                    <el-option v-for="item in standardGoodsQualityType" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7 && form.isCn === 2" label="代理人地址：">
                  <el-input v-model="form.agentAddress" placeholder="请输入代理人地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row>
              <el-col :span="12">
              <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="生产许可证编号：">
                  <el-input v-model="form.productStandardCode" placeholder="请输入生产许可证编号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
              <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="变更情况：">
                  <el-input v-model="form.changes" placeholder="请输入变更情况" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>说明书信息
          </div>
          <div class="instructions-info">
            <!-- 第一行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品 -->
                <el-form-item v-if="type === 1" label="成分：" prop="goodsInstructionsInfo.drugDetails">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.drugDetails" placeholder="请输入药品成分" />
                </el-form-item>
                <!-- 中药饮片 -->
                <el-form-item v-if="type === 2" label="净含量：" prop="netContent">
                  <el-input type="textarea" v-model="form.decoctionInstructionsInfo.netContent" placeholder="请输入净含量" />
                </el-form-item>
                <!-- 中药材 -->
                <el-form-item v-if="type === 3" label="性味：" prop="materialsInstructionsInfo.propertyFlavor">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.propertyFlavor" placeholder="请输入性味" />
                </el-form-item>
                <!-- 消杀显示 -->
                <el-form-item v-if="type === 4" label="成分：" prop="disinfectionInstructionsInfo.drugDetails">
                  <el-input type="textarea" v-model="form.disinfectionInstructionsInfo.drugDetails" placeholder="请输入药品成分" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="原料：" prop="healthInstructionsInfo.rawMaterial">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.rawMaterial" placeholder="请输入原料" />
                </el-form-item>
                <!-- 食品显示 -->
                <el-form-item v-if="type === 6" label="配料：" prop="foodsInstructionsInfo.ingredients">
                  <el-input type="textarea" v-model="form.foodsInstructionsInfo.ingredients" placeholder="请输入配料" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="结构组成：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.structure" placeholder="请输入结构组成" />
                </el-form-item>
                <!-- 配方颗粒 -->
                <el-form-item v-if="type === 8" label="净含量：" prop="netContent">
                  <el-input type="textarea" v-model="form.dispensingGranuleInfo.netContent" placeholder="请输入净含量" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品 -->
                <el-form-item v-if="type === 1" label="性状：" prop="goodsInstructionsInfo.drugProperties">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.drugProperties" placeholder="请输入性状" />
                </el-form-item>
                <!-- 中药饮片 -->
                <el-form-item v-if="type === 2" label="保质期：" prop="decoctionInstructionsInfo.expirationDate">
                  <el-input v-model="form.decoctionInstructionsInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
                <!-- 中药材显示 -->
                <el-form-item v-if="type === 3" label="性状：" prop="materialsInstructionsInfo.drugProperties">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.drugProperties" placeholder="请输入性状" />
                </el-form-item>
                <!-- 肖杀显示 -->
                <el-form-item v-if="type === 4" label="保质期：" prop="disinfectionInstructionsInfo.expirationDate">
                  <el-input type="textarea" v-model="form.disinfectionInstructionsInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
                <!-- 保健品 -->
                <el-form-item v-if="type === 5" label="保质期：" prop="healthInstructionsInfo.expirationDate">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
                <!-- 食品 -->
                <el-form-item v-if="type === 6" label="保质期：" prop="foodsInstructionsInfo.expirationDate">
                  <el-input type="textarea" v-model="form.foodsInstructionsInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="注意事项：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.noteEvents" placeholder="请输入注意事项" />
                </el-form-item>
                <!-- 配方颗粒 -->
                <el-form-item v-if="type === 8" label="保质期：" prop="dispensingGranuleInfo.expirationDate">
                  <el-input v-model="form.dispensingGranuleInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="适应症：" prop="goodsInstructionsInfo.indications">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.indications" placeholder="请输入适应症" />
                </el-form-item>
                <!-- 中药饮片 -->
                <el-form-item v-if="type === 2" label="原产地：" prop="decoctionInstructionsInfo.sourceArea">
                  <el-input type="textarea" v-model="form.decoctionInstructionsInfo.sourceArea" placeholder="请输入原产地" />
                </el-form-item>
                <!-- 中药材 -->
                <el-form-item v-if="type === 3" label="功效：" prop="materialsInstructionsInfo.effect">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.effect" placeholder="请输入功效" />
                </el-form-item>
                <!-- 肖杀显示 -->
                <el-form-item v-if="type === 4" label="使用方法：" prop="disinfectionInstructionsInfo.usageDosage">
                  <el-input type="textarea" v-model="form.disinfectionInstructionsInfo.usageDosage" placeholder="请输入使用方法" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="适宜人群：" prop="healthInstructionsInfo.suitablePeople">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.suitablePeople" placeholder="请输入适宜人群" />
                </el-form-item>
                <!-- 食品显示 -->
                <el-form-item v-if="type === 6" label="贮存条件：" prop="foodsInstructionsInfo.store">
                  <el-input type="textarea" v-model="form.foodsInstructionsInfo.store" placeholder="请输入贮存条件" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="适用范围：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.useScope" placeholder="请输入适用范围" />
                </el-form-item>
                <!-- 配方颗粒 -->
                <el-form-item v-if="type === 8" label="原产地：" prop="dispensingGranuleInfo.sourceArea">
                  <el-input type="textarea" v-model="form.dispensingGranuleInfo.sourceArea" placeholder="请输入原产地" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="用法用量：" prop="goodsInstructionsInfo.usageDosage">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.usageDosage" placeholder="请输入用法用量" />
                </el-form-item>
                <!-- 中药饮片 -->
                <el-form-item v-if="type === 2" label="包装清单：" prop="decoctionInstructionsInfo.packingList">
                  <el-input type="textarea" v-model="form.decoctionInstructionsInfo.packingList	" placeholder="请输入包装清单" />
                </el-form-item>
                <!-- 中药材 -->
                <el-form-item v-if="type === 3" label="用法用量：" prop="materialsInstructionsInfo.usageDosage">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.usageDosage" placeholder="请输入用法用量" />
                </el-form-item>
                <!-- 肖杀显示 -->
                <el-form-item v-if="type === 4" label="注意事项：" prop="disinfectionInstructionsInfo.noteEvents">
                  <el-input type="textarea" v-model="form.disinfectionInstructionsInfo.noteEvents" placeholder="请输入注入事项" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="辅料：" prop="healthInstructionsInfo.ingredients">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.ingredients" placeholder="请输入辅料" />
                </el-form-item>
                <!-- 食品显示 -->
                <el-form-item v-if="type === 6" label="致敏源信息：" prop="foodsInstructionsInfo.allergens">
                  <el-input type="textarea" v-model="form.foodsInstructionsInfo.allergens" placeholder="请输入致敏源信息" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="使用说明：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.usageDosage" placeholder="请输入使用说明" />
                </el-form-item>
                <!-- 配方颗粒 -->
                <el-form-item v-if="type === 8" label="包装清单：" prop="dispensingGranuleInfo.packingList">
                  <el-input type="textarea" v-model="form.dispensingGranuleInfo.packingList	" placeholder="请输入包装清单" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="不良反应：" prop="goodsInstructionsInfo.adverseEvents">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.adverseEvents" placeholder="请输入不良反应" />
                </el-form-item>
                <!-- 中药饮片-->
                <el-form-item v-if="type === 2" label="贮藏：" prop="decoctionInstructionsInfo.store">
                  <el-input type="textarea" v-model="form.decoctionInstructionsInfo.store	" placeholder="请输入贮藏信息" />
                </el-form-item>
                <!-- 中药材 -->
                <el-form-item v-if="type === 3" label="储藏：" prop="materialsInstructionsInfo.store">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.store" placeholder="请输入储藏信息" />
                </el-form-item>
                <!-- 肖杀显示 -->
                <el-form-item v-if="type === 4" label="灭菌类别：" prop="disinfectionInstructionsInfo.sterilizationCategory">
                  <el-input type="textarea" v-model="form.disinfectionInstructionsInfo.sterilizationCategory" placeholder="请输入灭菌类别" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="不适宜人群：" prop="healthInstructionsInfo.unsuitablePeople">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.unsuitablePeople" placeholder="请输入不适宜人群" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="储藏条件：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.storageConditions" placeholder="请输入储藏条件" />
                </el-form-item>
                <!-- 配方颗粒-->
                <el-form-item v-if="type === 8" label="贮藏：" prop="dispensingGranuleInfo.store">
                  <el-input type="textarea" v-model="form.dispensingGranuleInfo.store	" placeholder="请输入贮藏信息" />
                </el-form-item>

              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="禁忌：" prop="goodsInstructionsInfo.contraindication">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.contraindication" placeholder="请输入禁忌" />
                </el-form-item>
                <!-- 中药饮片  -->
                <el-form-item v-if="type === 2" label="用法用量：" prop="decoctionInstructionsInfo.usageDosage">
                  <el-input type="textarea" v-model="form.decoctionInstructionsInfo.usageDosage" placeholder="请输入用法用量" />
                </el-form-item>
                <!-- 中药材  -->
                <el-form-item v-if="type === 3" label="保质期：" prop="materialsInstructionsInfo.expirationDate">
                  <el-input type="textarea" v-model="form.materialsInstructionsInfo.expirationDate" placeholder="请输入保质期" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="保健功能：" prop="healthInstructionsInfo.healthcareFunction">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.healthcareFunction" placeholder="请输入保健功能" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="包装：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.packingInstructions" placeholder="请输入包装" />
                </el-form-item>
                <!-- 配方颗粒  -->
                <el-form-item v-if="type === 8" label="用法用量：" prop="dispensingGranuleInfo.usageDosage">
                  <el-input type="textarea" v-model="form.dispensingGranuleInfo.usageDosage" placeholder="请输入用法用量" />
                </el-form-item>

              </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="注意事项：" prop="goodsInstructionsInfo.noteEvents">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.noteEvents" placeholder="请输入注意事项" />
                </el-form-item>
                <!-- 保健食品 -->
                <el-form-item v-if="type === 5" label="贮藏方法：" prop="healthInstructionsInfo.store">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.store" placeholder="请输入贮藏方法" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="有效期：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.expirationDate" placeholder="请输入有效期" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="药物相互作用：" prop="goodsInstructionsInfo.interreaction">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.interreaction	" placeholder="请输入药物相互作用" />
                </el-form-item>
                <!-- 保健食品显示 -->
                <el-form-item v-if="type === 5" label="食用量及使用方法：" prop="healthInstructionsInfo.usageDosage">
                  <el-input type="textarea" v-model="form.healthInstructionsInfo.usageDosage" placeholder="请输入食用量及使用方法" />
                </el-form-item>
                <!-- 医疗器械显示 -->
                <el-form-item v-if="type === 7" label="备注：">
                  <el-input type="textarea" v-model="form.medicalInstrumentInfo.remark" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第五行 -->
            <el-row>
              <el-col :span="12">
                <!-- 药品显示 中药材显示 -->
                <el-form-item v-if="type === 1" label="储藏：" prop="goodsInstructionsInfo.storageConditions">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.storageConditions" placeholder="请输入储藏信息" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <!-- 药品显示 -->
                <el-form-item v-if="type === 1" label="执行标准：" prop="goodsInstructionsInfo.executiveStandard">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.executiveStandard" placeholder="请输入执行标准" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第六行 -->
            <el-row>
              <el-col :span="12">
                <!-- 普通药品 -->
                <el-form-item v-if="type === 1" label="保质期：" prop="goodsInstructionsInfo.shelfLife">
                  <el-input type="textarea" v-model="form.goodsInstructionsInfo.shelfLife" placeholder="请输入保质期" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="search-bar">
          <div class="header-bar">
            <div class="sign"></div>包装规格信息
          </div>
          <div class="packing-img-box">
            <div 
              class="packing-img" 
              v-for="(item, index) in fileList" 
              :key="item.pic" 
              @mouseenter="showDialog(index)" 
              @mouseleave="hideDialog(index)">
              <el-image class="packing-image" :src="item.picUrl" fit="contain" :lazy="true"></el-image>
              <div class="packing-cover" v-if="packingShow && index === packingCurrent">
                <i class="el-icon-zoom-in" @click="changeBigUrl(item.picUrl)"></i>
                <i class="el-icon-delete" @click="deletPackingImg(index)"></i>
              </div>
            </div>

            <!-- :file-list="fileList" -->
            <ylUploadImage 
              v-if="fileList.length < 5" 
              @onSuccess="handleAddUrlSucess" 
              :limit="5" 
              :extral-data="uploadData" 
              :show-file-list="false">
              <div>
                <i class="el-icon-plus"></i>
              </div>
            </ylUploadImage>
          </div>
          <ylToolTip class="mar-t-16">上传图片像素要等于720*720，只能上传jpg/png文件</ylToolTip>
          <!--  -->
          <div class="mar-tb-16">
            <yl-button type="primary" @click="add">新增包装规格</yl-button>
          </div>
          <yl-table border :show-header="true" :list="dataList">
            <el-table-column align="center" min-width="80" label="规格ID" prop="id"></el-table-column>
            <el-table-column align="center" min-width="80" label="*包装规格" prop="sellSpecifications">
              <template slot="header">
                <div class="table-header-slot"><span>*</span> 包装规格</div>
              </template>
              <template slot-scope="scope">
                <el-input 
                  type="text" 
                  :maxlength="20" 
                  v-model="editSellSpecifications" 
                  v-if="editIndex === scope.$index" 
                  placeholder="请输入包装规格" />
                <span v-else>{{ scope.row.sellSpecifications }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="80" label="单位" prop="unit">
              <template slot-scope="scope">
                <el-input 
                  type="text" 
                  :maxlength="10" 
                  show-word-limit
                  v-model="editUnit" 
                  v-if="editIndex === scope.$index" 
                  placeholder="请输入单位" />
                <span v-else>{{ scope.row.unit }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" min-width="100" label="条形码" prop="barcode">
              <template slot="header">
                <div class="table-header-slot"><span>*</span> 条形码</div>
              </template>
              <template slot-scope="scope">
                <div v-if="editIndex === scope.$index && showBarCodeError" class="color-red h-22"></div>
                <el-input v-if="editIndex === scope.$index" v-model="editBarCode" placeholder="请输入无或13位以内的数字" />
                <span v-else>{{ scope.row.barcode }}</span>
                <div v-if="editIndex === scope.$index && showBarCodeError" class="color-red">条形码请输入无或13位以内的数字</div>
              </template>
            </el-table-column>

            <el-table-column align="center" min-width="100" label="YPID" prop="ypidCode">
              <template slot-scope="scope">
                <el-input 
                  type="text" 
                  :maxlength="12" 
                  v-model="editYpidCode" 
                  v-if="editIndex === scope.$index" 
                  placeholder="请输入YPID编码" />
                <span v-else>{{ scope.row.ypidCode }}</span>
              </template>
            </el-table-column>
            <el-table-column align="left" min-width="280" label="商品图片">
              <template slot-scope="scope">
                <div class="table-upload-img">
                  <div class="packing-img-box">
                    <div 
                      class="packing-img" 
                      v-for="(item, index) in scope.row.picInfoList" 
                      :key="item.pic" 
                      @mouseenter="tableImgShowDialog(index)" 
                      @mouseleave="tableImgHideDialog(index)">
                      <el-image class="packing-image" :src="item.picUrl" fit="contain" :lazy="true"></el-image>
                      <div class="packing-cover" v-if="tableShow&& editIndex === scope.$index && index === packingCurrent ">
                        <i class="el-icon-zoom-in" @click="changeBigUrl(item.picUrl)"></i>
                        <i class="el-icon-delete" @click="deletTableImg(scope.$index,index)"></i>
                      </div>
                    </div>
                    <div v-if="editIndex === scope.$index && scope.row.picInfoList.length < 5">
                      <ylUploadImage @onSuccess="tableUploadSuccess" :limit="5" :extral-data="uploadData" :show-file-list="false">
                        <div>
                          <i class="el-icon-plus"></i>
                        </div>
                      </ylUploadImage>
                    </div>
                  </div>

                </div>
              </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" label="操作" width="120">
              <template slot-scope="scope">
                <div>
                  <yl-button v-if="editIndex === scope.$index" type="text" @click="save(scope.row,scope.$index)">完成</yl-button>
                  <yl-button v-if="editIndex === scope.$index" type="text" @click="cancel(scope.row,scope.$index)">取消</yl-button>
                  <yl-button v-else type="text" @click="edit(scope.row,scope.$index)">编辑</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>

          <el-form-item class="flex-row-center mar-t-16">
            <yl-button @click="close">关闭</yl-button>
            <yl-button type="primary" @click="onSubmit('form')">保存</yl-button>
          </el-form-item>
        </div>

      </el-form>

      <!-- 弹窗 -->
      <yl-dialog :visible.sync="show" title="图片预览" @confirm="confirm" :show-cancle="false">
        <div class="dialog-content flex-row-center">
          <el-image :src="bigUrl" fit="contain" :lazy="true"></el-image>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { paymentMethod, standardGoodsControlType, standardGoodsSpecialComposition, standardGoodsType, standardGoodsIsYb, standardGoodsOtcType, standardGoodsIsCn, standardGoodsQualityType, goodsBusinessScope } from '@/subject/admin/utils/busi'
import { editGoodsDetail, getGoodsDetailById, getJumpGoodsDetailById, getProductCategory } from '@/subject/admin/api/quality'
import { ylUploadImage } from '@/subject/admin/components/index'
export default {
  name: 'BasicDrugsEdit',
  computed: {
    // 支付方式
    payType() {
      return paymentMethod()
    },
    // 管制类型
    standardGoodsControlType() {
      return standardGoodsControlType()
    },
    // 特殊成分
    standardGoodsSpecialComposition() {
      return standardGoodsSpecialComposition()
    },
    // 录入类型， 药品类型
    standardGoodsType() {
      return standardGoodsType()
    },
    // 是否医保
    standardGoodsIsYb() {
      return standardGoodsIsYb()
    },
    // 处方类型
    standardGoodsOtcType() {
      return standardGoodsOtcType()
    },
    // 是否国产
    standardGoodsIsCn() {
      return standardGoodsIsCn()
    },
    // 质量标准类别
    standardGoodsQualityType() {
      return standardGoodsQualityType()
    },
    goodsBusinessScopeType(){
      return goodsBusinessScope()
    }
  },
  components: {
    ylUploadImage
  },
  data() {
    return {
      form: {
        goodsId: '',
        goodsType: '',
        isCn: '',
        name: '',
        aliasName: '',
        commonName: '',
        licenseNo: '',
        manufacturer: '',
        manufacturerAddress: '',
        otcType: '',
        gdfName: '',
        gdfSpecifications: '',
        standardCategoryId1: '',
        standardCategoryId2: '',
        roughWeight: '',
        isYb: '',
        controlType: '',
        specialComposition: '',
        goodsCode: '',
        ingredient: '',
        qualityType: '',
        executiveStandard: '',
        goodsGrade: '',
        goodsSource: '',
        approvalDate: '',
        productStandardCode: '',
        productClassification: '',
        //医疗器械-商品所属经营范围
        businessScope: '',
        //医疗器械-备案人地址/注册人地址
        registrantAddress: '',
        //医疗器械-代理人名称
        agent: '',
        //医疗器械-代理人地址
        agentAddress: '',
        //医疗器械-变更情况
        changes: '',
        businessScopeList: [
          {
            value: '',
            lable: '一类医疗器械'
          },
          {
            value: '',
            lable: '二类医疗器械'
          },
          {
            value: '',
            lable: '三类医疗器械'
          }
        ],
        // 普通药品说明书信息
        goodsInstructionsInfo: {
          id: '',
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
          interreaction: ''
        },
        // 中药饮片说明书信息
        decoctionInstructionsInfo: {
          expirationDate: '',
          id: '',
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
          id: '',
          propertyFlavor: '',
          store: '',
          usageDosage: ''
        },
        // 消杀
        disinfectionInstructionsInfo: {
          drugDetails: '',
          expirationDate: '',
          id: '',
          noteEvents: '',
          sterilizationCategory: '',
          usageDosage: ''
        },
        // 保健品
        healthInstructionsInfo: {
          expirationDate: '',
          healthcareFunction: '',
          id: '',
          ingredients: '',
          rawMaterial: '',
          store: '',
          suitablePeople: '',
          unsuitablePeople: '',
          usageDosage: ''
        },
        // 食品
        foodsInstructionsInfo: {
          allergens: '',
          expirationDate: '',
          id: '',
          ingredients: '',
          store: ''
        },
        // 医疗器械说明书
        medicalInstrumentInfo: {
          id: '',
          structure: '',
          noteEvents: '',
          useScope: '',
          usageDosage: '',
          storageConditions: '',
          packingInstructions: '',
          expirationDate: '',
          remark: ''
        },
        // 配方颗粒信息
        dispensingGranuleInfo: {
          expirationDate: '',
          id: '',
          netContent: '',
          packingList: '',
          sourceArea: '',
          store: '',
          usageDosage: ''
        }
      },
      rules: {
        // 基本信息验证
        goodsType: [
          { required: true, message: '请选择录入类型', trigger: 'change' }
        ],
        isCn: [
          { required: true, message: '请选择商品类型', trigger: 'change' }
        ],
        businessScope: [
          { required: true, message: '请选择商品类型', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        licenseNo: [
          { required: true, message: '请输入唯一编号', trigger: 'blur' },
          { max: 30, message: '长度请控制在30以内', trigger: 'blur' }
        ],
        // 中药材，生产厂家非必填
        manufacturer: [
          { required: true, message: '请输入生产厂家', trigger: 'blur' },
          { max: 100, message: '长度请控制在100以内', trigger: 'blur' }
        ],
        // 中药材的时候，判断生产地址必填写
        manufacturerAddress: [
          { required: true, message: '请输入生产地址', trigger: 'blur' },
          { max: 20, message: '长度请控制在20以内', trigger: 'blur' }
        ],
        otcType: [
          { required: true, message: '请选择处方类型', trigger: 'change' }
        ],
        gdfName: [
          { required: true, message: '请输入剂型', trigger: 'blur' },
          { max: 20, message: '长度请控制在20以内', trigger: 'blur' }
        ],
        gdfSpecifications: [
          { required: true, message: '请输入剂型规格', trigger: 'blur' }
          // { max: 20, message: '长度请控制在20以内', trigger: 'blur' }
        ],
        //医疗器械，判断备案凭证编号/注册证编号必填写
        registration: [
          { required: true, message: '请输入编号', trigger: 'change' }
        ],
        standardCategoryId1: [
          { required: true, message: '请选择一级分类', trigger: 'change' }
        ],
        standardCategoryId2: [
          { required: true, message: '请选择二级分类', trigger: 'change' }
        ],
        // 其他信息验证
        productStandardCode: [
          { required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }
        ],
        executiveStandard: [{ required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }],
        goodsSource: [{ required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }],
        goodsGrade: [{ required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }],
        goodsCode: [{ required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }],
        ingredient: [{ required: false, max: 100, message: '长度请控制在100以内', trigger: 'blur' }],
        // 普通药品说明书
        'goodsInstructionsInfo.drugDetails': [
          { required: false, message: '成分不能为空', trigger: 'blur' }
        ],
        'goodsInstructionsInfo.drugProperties': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.executiveStandard': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.indications': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.adverseEvents': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.contraindication': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.noteEvents': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.storageConditions': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.shelfLife': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'goodsInstructionsInfo.interreaction': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        // 中药饮片说明书
        'decoctionInstructionsInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'decoctionInstructionsInfo.netContent': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'decoctionInstructionsInfo.packingList': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'decoctionInstructionsInfo.sourceArea': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'decoctionInstructionsInfo.store': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'decoctionInstructionsInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        // 中药材说明书
        'materialsInstructionsInfo.drugProperties': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'materialsInstructionsInfo.effect': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'materialsInstructionsInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'materialsInstructionsInfo.propertyFlavor': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'materialsInstructionsInfo.store': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'materialsInstructionsInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        //消杀
        'disinfectionInstructionsInfo.drugDetails': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'disinfectionInstructionsInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'disinfectionInstructionsInfo.noteEvents': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'disinfectionInstructionsInfo.sterilizationCategory': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'disinfectionInstructionsInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        // 保健品
        'healthInstructionsInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.healthcareFunction': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.ingredients': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.rawMaterial': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.store': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.suitablePeople': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.unsuitablePeople': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'healthInstructionsInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        // 食品
        'foodsInstructionsInfo.allergens': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'foodsInstructionsInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'foodsInstructionsInfo.ingredients': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'foodsInstructionsInfo.store': [{ required: false, message: '长度请控制500以内', trigger: 'blur' }],
        // 配方颗粒
        'dispensingGranuleInfo.expirationDate': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'dispensingGranuleInfo.netContent': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'dispensingGranuleInfo.packingList': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'dispensingGranuleInfo.sourceArea': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'dispensingGranuleInfo.store': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }],
        'dispensingGranuleInfo.usageDosage': [{ required: false, message: '长度请控制在500以内', trigger: 'blur' }]
        
      },
      // 1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品
      type: 1,
      // 一级分类
      category: [],
      // 二级分类
      cateChild: [],
      //  包装规格 上传图片
      fileList: [],
      dialogVisible: false,
      // action: process.env.VUE_APP_REQ_URL + "/file/upload",
      uploadData: { type: 'goodsPicture' },
      // 规格图片信息
      dataList: [],
      // 规格表格，显示
      editIndex: -1,
      editSellSpecifications: '',
      editUnit: '',
      editYpidCode: '',
      editBarCode: '',
      maxBarCode: 13,
      // 图片遮罩层
      packingShow: false,
      packingCurrent: -1,
      tableShow: false,
      // 图片预览
      show: false,
      bigUrl: '',
      // 基础药品列表新增add  编辑edit   对应标准库匹配商品时新增  toStandardAdd
      isEdit: '',
      showBarCodeError: false
    }
  },
  mounted() {
    // 基础药品列表新增add  编辑edit   对应标准库匹配商品时新增  toStandardAdd
    if (this.$route.params.type) {
      this.isEdit = this.$route.params.type
    }
    this.getCate()
  },
  methods: {
    // 删除规格图片
    deletTableImg(scopeIndex, index) {
      this.dataList[scopeIndex].picInfoList.splice(index, 1)

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
    //上传图片成功
    handleAddUrlSucess(res) {
      this.fileList.push({
        pic: res.key,
        picUrl: res.url
      })
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
    },
    // 包装表格 上传图片成功
    tableUploadSuccess(res) {
      this.dataList[this.editIndex].picInfoList.push({
        pic: res.key,
        picUrl: res.url,
        url: res.url
      })

    },
    edit(row, index) {
      this.editIndex = index
      this.editSellSpecifications = row.sellSpecifications // 包装规格
      this.editUnit = row.unit // 单位
      this.editYpidCode = row.ypidCode // YPID编码
      this.editBarCode = row.barcode // 条形码
    },
    // 编辑规格保存
    save(row, index) {
      this.showBarCodeError = false;
      this.editBarCode = this.editBarCode.replace(/\s+/g, '');
      // 1.不能为空  2.可以= '无'   3。必须是数字。 4.不能超过13位
      let reg = /^[0-9]*[1-9][0-9]*$/
      if (this.editBarCode) {
        if (this.editBarCode === '无' || (reg.test(this.editBarCode) && this.editBarCode.length <= 13)) {
          this.showBarCodeError = false;
        } else {
          this.showBarCodeError = true;
          return
        }
      } else {
        this.showBarCodeError = true;
        return
      }

      if (this.editSellSpecifications) {
        for (let i = 0; i < this.dataList.length; i ++) {
          if (this.editSellSpecifications == this.dataList[i].sellSpecifications && index !== i) {
            return this.$common.error('包装规格不能重名')
          }
          if (this.editBarCode !== '无' && this.editBarCode === this.dataList[i].barcode && index !== i) {
            return this.$common.error('条形码不能重复')
          }
        }
        let item = this.dataList[index]
        this.$set(item, 'sellSpecifications', this.editSellSpecifications)
        this.$set(item, 'unit', this.editUnit)
        this.$set(item, 'ypidCode', this.editYpidCode)
        this.$set(item, 'barcode', this.editBarCode)

        this.editIndex = -1
      } else {
        return this.$common.error('包装规格不能为空')
      }

    },
    cancel(item, index) {
      if (index === this.dataList.length - 1 && !item.id) {
        this.dataList.splice(index, 1)
      }
      this.editIndex = -1
      this.showBarCodeError = false;
    },
    // 新增规格
    add() {
      if (this.editIndex === -1) {
        this.editSellSpecifications = ''
        this.editUnit = ''
        this.editYpidCode = ''
        this.editBarCode = ''
        let obj = {
          'id': '',
          'picInfoList': [
            // {
            //   "id": "",
            //   "pic": "",
            //   "picDefault": "",
            //   "picOrder": ""
            // },
          ],
          'sellSpecifications': '',
          'standardId': '',
          'unit': '',
          'ypidCode': '',
          'barcode': ''
        }
        this.dataList.push(obj)
        this.editIndex = this.dataList.length - 1
      }

    },
    // 获取分类
    async getCate() {
      if (this.category.length === 0) {
        let { list } = await getProductCategory()
        this.category = list
        if (this.isEdit != 'add') {
          this.getGoodsDetail();
        }
      }
    },
    // 选择分类大类
    selectChange(value) {
      this.$log(value)
      if (this.form.standardCategoryId2) {
        this.form.standardCategoryId2 = null
      }
      let data = this.category.find(item => item.id === value)
      if (data.children && data.children.length) {
        this.cateChild = data.children
      }
    },
    selectChangeFirst(value) {
      let data = this.category.find(item => item.id === value)
      if (data && data.children && data.children.length) {
        this.cateChild = data.children
      }
    },

    // 获取商品详情
    async getGoodsDetail() {
      let data = {}
      // 基础药品列表新增add  编辑edit   对应标准库匹配商品时新增  toStandardAdd
      if (this.isEdit === 'edit') {
        // 编辑获取 商品详情
        if (this.$route.params.id) {
          data = await getGoodsDetailById(this.$route.params.id)
        }
      } else if (this.isEdit === 'toStandardAdd') {
        // 匹配商品库，新增获取商品详情
        if (this.$route.params.id) {
          data = await getJumpGoodsDetailById(this.$route.params.id)
        }
      }
      if (data) {
        this.selectChangeFirst(Number(data.baseInfo.standardCategoryId1))
        // ：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品 7-医疗器械 8-配方颗粒
        let numArr = [1, 2, 3, 4, 5, 6, 7, 8]
        let result = numArr.some(v => v == data.baseInfo.goodsType)
        if (result) {
          this.type = data.baseInfo.goodsType
          this.form.goodsType = data.baseInfo.goodsType
        } else {
          this.type = 1
          this.form.goodsType = 1
        }
        //  药品id  当匹配标准库过来新增时，保存id也传空
        if (this.isEdit !== 'toStandardAdd') {
          this.form.goodsId = data.baseInfo.id
        }
        // 商品类型
        this.form.isCn = data.baseInfo.isCn
        // 商品所属经营范围
        this.form.businessScope = data.baseInfo.businessScope !== 0 ? data.baseInfo.businessScope : ''
        // 商品名称
        this.form.name = data.baseInfo.name
        // 商品别名
        this.form.aliasName = data.baseInfo.aliasName
        // 通用名
        this.form.commonName = data.baseInfo.commonName
        // 批准文号
        this.form.licenseNo = data.baseInfo.licenseNo
        // 生产地址
        this.form.manufacturerAddress = data.baseInfo.manufacturerAddress
        // 生产厂家
        this.form.manufacturer = data.baseInfo.manufacturer
        // 处方类型
        this.form.otcType = data.baseInfo.otcType ? data.baseInfo.otcType : ''
        // 	剂型名称
        this.form.gdfName = data.baseInfo.gdfName
        // 剂型规格
        this.form.gdfSpecifications = data.baseInfo.gdfSpecifications
        // 一级分类
        this.form.standardCategoryId1 = Number(data.baseInfo.standardCategoryId1) ? Number(data.baseInfo.standardCategoryId1) : ''
        // 二级分类
        this.form.standardCategoryId2 = Number(data.baseInfo.standardCategoryId2) ? Number(data.baseInfo.standardCategoryId2) : ''
        // 含量
        this.form.roughWeight = data.baseInfo.roughWeight
        // 医保
        this.form.isYb = data.baseInfo.isYb
        // 管制类型
        this.form.controlType = data.baseInfo.controlType
        // 特殊成分
        this.form.specialComposition = data.baseInfo.specialComposition
        // 药品本位码
        this.form.goodsCode = data.baseInfo.goodsCode
        // 复方制剂成分
        this.form.ingredient = data.baseInfo.ingredient
        // 质量标准类别
        this.form.qualityType = data.baseInfo.qualityType
        // 执行标准
        this.form.executiveStandard = data.baseInfo.executiveStandard
        // 等级
        this.form.goodsGrade = data.baseInfo.goodsGrade
        // 来源
        this.form.goodsSource = data.baseInfo.goodsSource
        // 批准日期
        this.form.approvalDate = data.baseInfo.approvalDate
        // 产品标准号
        this.form.productStandardCode = data.baseInfo.productStandardCode
        // 产品类别
        this.form.productClassification = data.baseInfo.productClassification
        // 备案人名称/注册人名称
        this.form.registrant = data.baseInfo.registrant
        // 备案人地址/注册人地址
        this.form.registrantAddress = data.baseInfo.registrantAddress
        // 代理人名称
        this.form.agent = data.baseInfo.agent
        // 代理人地址
        this.form.agentAddress = data.baseInfo.agentAddress
        // 变更情况
        this.form.changes = data.baseInfo.changes

        // 说明书的各种信息 ====================================================
        // 普通药品 说明书
        if (data.goodsInstructionsInfo) {
          //  说明书id
          this.form.goodsInstructionsInfo.id = data.goodsInstructionsInfo.id
          // 成分
          this.form.goodsInstructionsInfo.drugDetails = data.goodsInstructionsInfo.drugDetails
          // 药品性状
          this.form.goodsInstructionsInfo.drugProperties = data.goodsInstructionsInfo.drugProperties
          // 适应症
          this.form.goodsInstructionsInfo.indications = data.goodsInstructionsInfo.indications
          // 用法用量
          this.form.goodsInstructionsInfo.usageDosage = data.goodsInstructionsInfo.usageDosage
          // 不良反应
          this.form.goodsInstructionsInfo.adverseEvents = data.goodsInstructionsInfo.adverseEvents
          // 禁忌
          this.form.goodsInstructionsInfo.contraindication = data.goodsInstructionsInfo.contraindication
          // 注意事项
          this.form.goodsInstructionsInfo.noteEvents = data.goodsInstructionsInfo.noteEvents
          // 药物相互作用
          this.form.goodsInstructionsInfo.interreaction = data.goodsInstructionsInfo.interreaction
          // 储藏
          this.form.goodsInstructionsInfo.storageConditions = data.goodsInstructionsInfo.storageConditions
          // 执行标准
          this.form.goodsInstructionsInfo.executiveStandard = data.goodsInstructionsInfo.executiveStandard
          // 保质期
          this.form.goodsInstructionsInfo.shelfLife = data.goodsInstructionsInfo.shelfLife
        }
        // 中药饮片 说明书
        if (data.decoctionInstructionsInfo) {
          //保质期
          this.form.decoctionInstructionsInfo.expirationDate = data.decoctionInstructionsInfo.expirationDate
          // 说明书id
          this.form.decoctionInstructionsInfo.id = data.decoctionInstructionsInfo.id
          // 净含量
          this.form.decoctionInstructionsInfo.netContent = data.decoctionInstructionsInfo.netContent
          // 包装清单
          this.form.decoctionInstructionsInfo.packingList = data.decoctionInstructionsInfo.packingList
          // 原产地
          this.form.decoctionInstructionsInfo.sourceArea = data.decoctionInstructionsInfo.sourceArea
          // 储藏
          this.form.decoctionInstructionsInfo.store = data.decoctionInstructionsInfo.store
          // 用法与用量
          this.form.decoctionInstructionsInfo.usageDosage = data.decoctionInstructionsInfo.usageDosage
        }
        // 中药材 说明书
        if (data.materialsInstructionsInfo) {
          this.form.materialsInstructionsInfo.drugProperties = data.materialsInstructionsInfo.drugProperties
          this.form.materialsInstructionsInfo.effect = data.materialsInstructionsInfo.effect
          this.form.materialsInstructionsInfo.expirationDate = data.materialsInstructionsInfo.expirationDate
          this.form.materialsInstructionsInfo.id = data.materialsInstructionsInfo.id
          this.form.materialsInstructionsInfo.propertyFlavor = data.materialsInstructionsInfo.propertyFlavor
          this.form.materialsInstructionsInfo.store = data.materialsInstructionsInfo.store
          this.form.materialsInstructionsInfo.usageDosage = data.materialsInstructionsInfo.usageDosage
        }
        // 消杀 说明书
        if (data.disinfectionInstructionsInfo) {
          this.form.disinfectionInstructionsInfo.drugDetails = data.disinfectionInstructionsInfo.drugDetails
          this.form.disinfectionInstructionsInfo.expirationDate = data.disinfectionInstructionsInfo.expirationDate
          this.form.disinfectionInstructionsInfo.id = data.disinfectionInstructionsInfo.id
          this.form.disinfectionInstructionsInfo.noteEvents = data.disinfectionInstructionsInfo.noteEvents
          this.form.disinfectionInstructionsInfo.sterilizationCategory = data.disinfectionInstructionsInfo.sterilizationCategory
          this.form.disinfectionInstructionsInfo.usageDosage = data.disinfectionInstructionsInfo.usageDosage
        }
        // 保健品 说明书
        if (data.healthInstructionsInfo) {
          this.form.healthInstructionsInfo.expirationDate = data.healthInstructionsInfo.expirationDate
          this.form.healthInstructionsInfo.healthcareFunction = data.healthInstructionsInfo.healthcareFunction
          this.form.healthInstructionsInfo.id = data.healthInstructionsInfo.id
          this.form.healthInstructionsInfo.ingredients = data.healthInstructionsInfo.ingredients
          this.form.healthInstructionsInfo.rawMaterial = data.healthInstructionsInfo.rawMaterial
          this.form.healthInstructionsInfo.store = data.healthInstructionsInfo.store
          this.form.healthInstructionsInfo.suitablePeople = data.healthInstructionsInfo.suitablePeople
          this.form.healthInstructionsInfo.unsuitablePeople = data.healthInstructionsInfo.unsuitablePeople
          this.form.healthInstructionsInfo.usageDosage = data.healthInstructionsInfo.usageDosage
        }
        // 食品 说明书
        if (data.foodsInstructionsInfo) {
          this.form.foodsInstructionsInfo.allergens = data.foodsInstructionsInfo.allergens
          this.form.foodsInstructionsInfo.expirationDate = data.foodsInstructionsInfo.expirationDate
          this.form.foodsInstructionsInfo.id = data.foodsInstructionsInfo.id
          this.form.foodsInstructionsInfo.ingredients = data.foodsInstructionsInfo.ingredients
          this.form.foodsInstructionsInfo.store = data.foodsInstructionsInfo.store
        }
        // 医疗器械 说明书
        if (data.medicalInstrumentInfo) {
          this.form.medicalInstrumentInfo.id = data.medicalInstrumentInfo.id
          this.form.medicalInstrumentInfo.structure = data.medicalInstrumentInfo.structure
          this.form.medicalInstrumentInfo.noteEvents = data.medicalInstrumentInfo.noteEvents
          this.form.medicalInstrumentInfo.useScope = data.medicalInstrumentInfo.useScope
          this.form.medicalInstrumentInfo.usageDosage = data.medicalInstrumentInfo.usageDosage
          this.form.medicalInstrumentInfo.storageConditions = data.medicalInstrumentInfo.storageConditions
          this.form.medicalInstrumentInfo.packingInstructions = data.medicalInstrumentInfo.packingInstructions
          this.form.medicalInstrumentInfo.expirationDate = data.medicalInstrumentInfo.expirationDate
          this.form.medicalInstrumentInfo.remark = data.medicalInstrumentInfo.remark
        }
        //  包装图片
        if (data.picBasicsInfoList && data.picBasicsInfoList.length > 0) {
          this.fileList = data.picBasicsInfoList
          this.fileList.forEach(item => {
            this.$set(item, 'url', item.picUrl)
          })
        }
        // 包装规格列表表格
        if (data.specificationInfo) {
          this.dataList = data.specificationInfo
          this.dataList.forEach(item => {
            if (item.picInfoList) {
              item.picInfoList.forEach(citem => {
                this.$set(citem, 'url', citem.picUrl)
              })
            }
            if (item.barcode == '') {
              item.barcode = '无'
            }
          })
        }
        // 配方颗粒
        if (data.dispensingGranuleInfo) {
          //保质期
          this.form.dispensingGranuleInfo.expirationDate = data.dispensingGranuleInfo.expirationDate
          // 说明书id
          this.form.dispensingGranuleInfo.id = data.dispensingGranuleInfo.id
          // 净含量
          this.form.dispensingGranuleInfo.netContent = data.dispensingGranuleInfo.netContent
          // 包装清单
          this.form.dispensingGranuleInfo.packingList = data.dispensingGranuleInfo.packingList
          // 原产地
          this.form.dispensingGranuleInfo.sourceArea = data.dispensingGranuleInfo.sourceArea
          // 储藏
          this.form.dispensingGranuleInfo.store = data.dispensingGranuleInfo.store
          // 用法与用量
          this.form.dispensingGranuleInfo.usageDosage = data.dispensingGranuleInfo.usageDosage
        }
      }
    },
    // 修改 药品类型
    changeGoodsType(e) {
      this.type = e;
      console.log('this.type',this.type);
    },
    // 关闭
    close() {
      if (this.isEdit == 'edit') {
        this.$router.push('/quality/basic_drugs_index')
      } else {
        this.$router.go(-1)
      }
    },
    // 提交
    onSubmit(formName) {
      this.$log(this.dataList)
      if (this.editIndex !== -1) {
        return this.$common.error('包装规格编辑未完成')
      }
      // 药品基本信息，和其他信息
      let baseInfo = {
        'agent': this.form.agent,
        'agentAddress': this.form.agentAddress,
        'aliasName': this.form.aliasName,
        'approvalDate': this.form.approvalDate,
        'businessScope': this.form.businessScope,
        'changes': this.form.changes,
        'commonName': this.form.commonName,
        'controlType': this.form.controlType,
        'executiveStandard': this.form.executiveStandard,
        'gdfName': this.form.gdfName,
        'gdfSpecifications': this.form.gdfSpecifications,
        'goodsCode': this.form.goodsCode,
        'goodsGrade': this.form.goodsGrade,
        'goodsSource': this.form.goodsSource,
        'goodsType': this.form.goodsType,
        'id': this.form.goodsId,
        'ingredient': this.form.ingredient,
        'isCn': this.form.isCn,
        'isYb': this.form.isYb,
        'licenseNo': this.form.licenseNo,
        'manufacturer': this.form.manufacturer,
        'manufacturerAddress': this.form.manufacturerAddress,
        'name': this.form.name,
        'otcType': this.form.otcType,
        'productClassification': this.form.productClassification,
        'productStandardCode': this.form.productStandardCode,
        'qualityType': this.form.qualityType,
        'registrant': this.form.registrant,
        'registrantAddress': this.form.registrantAddress,
        'roughWeight': this.form.roughWeight,
        'specialComposition': this.form.specialComposition,
        'standardCategoryId1': this.form.standardCategoryId1,
        'standardCategoryId2': this.form.standardCategoryId2
      }
      // 中药饮片 说明书
      let decoctionInstructionsInfo = this.form.decoctionInstructionsInfo
      // 消杀 说明书
      let disinfectionInstructionsInfo = this.form.disinfectionInstructionsInfo
      // 食品 说明书
      let foodsInstructionsInfo = this.form.foodsInstructionsInfo
      // 普通药品 说明书
      let goodsInstructionsInfo = this.form.goodsInstructionsInfo
      // 保健品 说明书
      let healthInstructionsInfo = this.form.healthInstructionsInfo
      // 中药材 说明书
      let materialsInstructionsInfo = this.form.materialsInstructionsInfo
      // 医疗器械 说明书
      let medicalInstrumentInfo = this.form.medicalInstrumentInfo
      // 配方颗粒
      let dispensingGranuleInfo = this.form.dispensingGranuleInfo
      // 包装图片
      let picBasicsInfoList = []
      for (let i = 0; i < this.fileList.length; i ++) {
        let obj = {}
        if (this.fileList[i].id) {
          obj.id = this.fileList[i].id
        }
        obj.pic = this.fileList[i].pic
        picBasicsInfoList.push(obj)
      }
      this.dataList.forEach(item => {
        if (item.barcode == '无') {
          item.barcode = ''
        }
      })
      let specificationInfo = this.dataList
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad()
          let data = await editGoodsDetail(
            baseInfo,
            decoctionInstructionsInfo,
            disinfectionInstructionsInfo,
            foodsInstructionsInfo,
            goodsInstructionsInfo,
            healthInstructionsInfo,
            materialsInstructionsInfo,
            medicalInstrumentInfo,
            dispensingGranuleInfo,
            picBasicsInfoList,
            specificationInfo
          )
          this.$common.hideLoad()
          if (data) {
            this.$common.success('保存成功')
            if (this.isEdit === 'add') {
              this.$router.go(-1)
            } else if (this.isEdit === 'edit') {
              this.getGoodsDetail();
            } else if (this.isEdit === 'toStandardAdd') {
              this.$router.go(-1)
            }

          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
