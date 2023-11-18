<template>
  <el-form :model="agreementRebateTerms" :rules="agreementRebateTermsRules" ref="dataForm" class="agreement-main" label-position="left">
    <div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>规则信息
        </div>
        <div class="content-box my-form-box">
          <el-row class="box rule-box">
            <el-col :span="7">
              <el-form-item label="是否底价供货:" prop="reserveSupplyFlag" class="pay-form-item">
                <el-radio-group v-model="agreementRebateTerms.reserveSupplyFlag" @change="reserveSupplyFlagChange" :disabled="agreementType == 5">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="14" v-if="agreementRebateTerms.reserveSupplyFlag == 0">
              <el-form-item label="商品返利规则设置方式:" prop="goodsRebateRuleType">
                <el-select v-model="agreementRebateTerms.goodsRebateRuleType" placeholder="请选择" @change="goodsRebateRuleTypeChange" :disabled="agreementType == 5">
                  <el-option v-for="item in agreementGoodsRebateRuleType" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </div>
      <div v-if="agreementRebateTerms.reserveSupplyFlag == 0">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>返利基础配置
          </div>
          <div class="content-box my-form-box">
            <el-row class="box">
              <el-col :span="14">
                <div class="title"><span class="red-text">*</span>返利支付方</div>
                <el-form-item prop="rebatePay">
                  <el-radio-group v-model="agreementRebateTerms.rebatePay" @change="rebatePayRadioChange">
                    <el-radio v-for="item in agreementRebatePay" :key="item.value" :label="item.value">
                      {{ item.label }}
                    </el-radio>
                    <yl-button icon="el-icon-circle-plus-outline" type="primary" v-if="agreementRebateTerms.rebatePay == 2" @click="addProviderClick">指定商业公司</yl-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 指定商业公司 -->
            <div class="pad-b-10" v-if="agreementRebateTerms.rebatePay == 2">
              <yl-table
                border
                show-header
                :list="agreementRebateTerms.agreementRebatePayEnterpriseList"
                :total="0"
              >
                <el-table-column label="企业ID" align="center" prop="eid">
                </el-table-column>
                <el-table-column label="企业名称" align="center" prop="ename">
                </el-table-column>
                <el-table-column label="操作" align="center" fixed="right">
                  <template slot-scope="{ $index }">
                    <div class="operation-view">
                      <yl-button type="text" @click="providerRemoveClick($index)">删除</yl-button>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
            <el-row class="box">
              <el-col :span="7">
                <div class="title">返利上限</div>
                <el-form-item>
                  <el-input v-model="agreementRebateTerms.maxRebate" @keyup.native="agreementRebateTerms.maxRebate = onInput(agreementRebateTerms.maxRebate, 2)" placeholder="请输入返利上限"></el-input>
                  <span class="font-title-color font-size-base label-height mar-l-10">元</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利兑付方式</div>
                <el-form-item prop="rebateCashType">
                  <el-select v-model="agreementRebateTerms.rebateCashType" placeholder="请选择">
                    <el-option v-for="item in agreementRebateCashType" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利兑付时间</div>
                <el-form-item prop="rebateCashTime">
                  <el-select v-model="agreementRebateTerms.rebateCashTime" placeholder="请选择">
                    <el-option v-for="item in agreementRebateCashTime" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利兑付时段</div>
                <div class="back-form-item">
                  <span class="font-title-color font-size-base mar-r-10 label-height">每隔</span>
                  <el-form-item prop="rebateCashSegment" class="money-form-item mar-r-16">
                    <el-input v-model="agreementRebateTerms.rebateCashSegment" @input="e => (agreementRebateTerms.rebateCashSegment = numberCheckInput(e))" placeholder="请输入"></el-input>
                  </el-form-item>
                  <el-form-item prop="rebateCashUnit" class="money-form-item mar-r-16">
                    <el-select v-model="agreementRebateTerms.rebateCashUnit">
                      <el-option label="月" :value="1"></el-option>
                      <el-option label="天" :value="2"></el-option>
                    </el-select>
                  </el-form-item>
                  <span class="font-title-color font-size-base label-height">兑付一次</span>
                </div>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="7">
                <div class="title">其他备注</div>
                <el-form-item>
                  <el-input v-model="agreementRebateTerms.otherRemark" placeholder="请输入其他备注" maxlength="20"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 非商品返利 -->
            <div class="box">
              <div class="title">非商品返利</div>
              <el-form-item v-if="!agreementRebateTerms.agreementOtherRebateList || agreementRebateTerms.agreementOtherRebateList.length == 0">
                <yl-button icon="el-icon-circle-plus-outline" type="primary" @click="addOtherRebateClick">新增非商品返利</yl-button>
              </el-form-item>
              <div v-for="(item,index) in agreementRebateTerms.agreementOtherRebateList" :key="index">
                <div class="nichtware-form-item">
                  <el-form-item class="money-form-item mar-r-16">
                    <el-select v-model="item.rebateType" placeholder="请选择">
                      <el-option v-for="rebateTypeItem in agreementNotProductRebateType" :key="rebateTypeItem.value" :label="rebateTypeItem.label"
                        :value="rebateTypeItem.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item class="money-form-item mar-r-16">
                    <el-select v-model="item.amountType" placeholder="请选择">
                      <el-option v-for="amountTypeItem in agreementNotProductRebateAmountType" :key="amountTypeItem.value" :label="amountTypeItem.label"
                        :value="amountTypeItem.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item class="money-form-item mar-r-16">
                    <el-input v-model="item.amount" placeholder="请输入"></el-input>
                  </el-form-item>
                  <el-form-item class="money-form-item mar-r-16 unit">
                    <el-select v-model="item.unit">
                      <el-option label="元" :value="1"></el-option>
                      <el-option label="%" :value="2"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item class="mar-r-16">
                    <el-checkbox :value="item.taxFlag">含税</el-checkbox>
                  </el-form-item>
                  <el-form-item class="mar-r-16">
                    <yl-button type="text" @click="deleteOtherRebateClick(index)">删除</yl-button>
                  </el-form-item>
                  <el-form-item v-if="agreementRebateTerms.agreementOtherRebateList.length == index + 1">
                    <yl-button icon="el-icon-circle-plus-outline" type="primary" @click="addOtherRebateClick">新增非商品返利</yl-button>
                  </el-form-item>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 返利标准设置 -->
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>返利标准设置
          </div>
          <div class="content-box my-form-box">
            <el-row class="box">
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>是否有任务量</div>
                <el-form-item prop="hasTask">
                  <el-radio-group v-model="agreementRebateTerms.taskFlag" @change="taskFlagRadioChange">
                    <el-radio :label="0">无</el-radio>
                    <el-radio :label="1">有</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利标准</div>
                <el-form-item prop="rebateStandard">
                  <el-select v-model="agreementRebateTerms.rebateStandard" placeholder="请选择">
                    <el-option v-for="item in agreementRebateTaskStandard" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7" v-if="agreementRebateTerms.taskFlag == 1">
                <div class="title"><span class="red-text">*</span>任务量标准</div>
                <el-form-item prop="taskStandard">
                  <el-select v-model="agreementRebateTerms.taskStandard" placeholder="请选择">
                    <el-option v-for="item in agreementRebateTaskStandard" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="box">
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利阶梯条件计算方法</div>
                <el-form-item prop="rebateStageMethod">
                  <el-select v-model="agreementRebateTerms.rebateStageMethod" placeholder="请选择">
                    <el-option v-for="item in agreementRebateStageMethod" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利计算单价</div>
                <el-form-item prop="rebateCalculatePrice">
                  <el-select v-model="agreementRebateTerms.rebateCalculatePrice" placeholder="请选择">
                    <el-option v-for="item in agreementRebateTaskStandard" :key="item.value" :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <div class="title"><span class="red-text">*</span>返利计算规则</div>
                <div class="money-form-item ">
                  <el-form-item prop="rebateCalculateRule" class="money-form-item mar-r-16">
                    <el-select v-model="agreementRebateTerms.rebateCalculateRule">
                      <el-option v-for="item in agreementRebateCalculateRule" :key="item.value" :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item prop="rebateRuleType" class="money-form-item mar-r-16 rule-form-item">
                    <el-select v-model="agreementRebateTerms.rebateRuleType">
                      <el-option v-for="item in agreementRebateRuleType" :key="item.value" :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 供销协议条款约定的全品 -->
        <div class="top-bar" v-if="agreementRebateTerms.goodsRebateRuleType == 1">
          <div class="content-box my-form-box">
            <div class="table-view">
              <yl-table
                border
                show-header
                :list="[1]"
                :total="0"
              >
                <el-table-column label="商品" align="center">
                  <template slot-scope="">
                    <div class="operation-view">供销协议条款约定的全品</div>
                  </template>
                </el-table-column>
                <el-table-column label="厂家/供应商" align="center">
                  <template slot-scope="">
                    <div class="operation-view">{{ ename }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="当前商品数量" align="center">
                  <template slot-scope="">
                    <div class="operation-view">{{ isAllGoods == 1 ? allGoodsNum : cateGoodsNum }}</div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
          </div>
        </div>
        <!-- 全品 多时段组合配置 -->
        <div class="tabs-view" v-if="agreementRebateTerms.goodsRebateRuleType == 1">
          <!-- tab切换 -->
          <div class="tab">
            <div v-for="(timeSegmentTypItem, timeSegmentTypIndex) in agreementTimeSegmentTypeSet" :key="timeSegmentTypIndex" class="tab-item" :class="tabsActiveName == timeSegmentTypItem.value ? 'tab-active' : ''" @click="clickTab(timeSegmentTypItem.value)">{{ timeSegmentTypItem.label }}</div>
          </div>
          <!-- 全品设置 时段配置 -->
          <div class="top-bar multiperiod-view">
            <!-- 全品设置 -->
            <div v-if="tabsActiveName != 1">
              <el-form-item label="有未达成任务量的子时段时:">
                <el-checkbox v-if="tabsActiveName == 3" v-model="agreementRebateTerms.cashAllSegmentFlag">兑付全时段</el-checkbox>
                <el-checkbox v-model="agreementRebateTerms.cashChildSegmentFlag">兑付子时段</el-checkbox>
              </el-form-item>
            </div>
            <div class="my-form-box">
              <div class="">
                <!-- 多时段 -->
                <div v-for="(periodItem, periodIndex) in agreementRebateTerms.agreementRebateTimeSegmentList" :key="periodIndex">
                  <div class="multiperiod-form-item mar-b-16">
                    <div class="title">
                      <div class="mar-b-10 data-select">
                        <span v-if="tabsActiveName == 3">{{ periodItem.type == 1 ? '协议全时段配置' : `子时段 ${periodIndex}配置` }}</span>
                        <span v-else>{{ periodItem.type == 1 ? '协议全时段配置' : `子时段 ${periodIndex + 1}配置` }}</span>
                        <div v-if="periodItem.type == 2" class="data-select-view mar-l-16">
                          <div class="font-size-base">{{ periodItem.startTime }}<span class="mar-l-8 mar-r-8">至</span></div>
                          <el-date-picker
                          v-model="periodItem.endTime"
                            type="date"
                            format="yyyy/MM/dd"
                            value-format="yyyy-MM-dd"
                            @change="(e) => endTimeChange(e, periodIndex)"
                            placeholder="选择日期">
                          </el-date-picker>
                          <yl-button v-if="periodIndex != 0" class="mar-l-16" icon="el-icon-remove-outline" type="text" @click="deletePeriodClick(periodIndex)"></yl-button>
                        </div>
                      </div>
                      <!-- 多范围 -->
                      <div v-for="(multiregionItem, multiregionIndex) in periodItem.agreementRebateGoodsGroupList[0].agreementRebateScopeList" :key="multiregionIndex">
                        <div class="multiregion-form-item mar-b-10">
                          <div>
                            <div class="header-bar header-renative">
                            <div class="sign"></div>返利范围设置<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" @click="deleteRegionClick(periodIndex, 0, multiregionIndex)"></yl-button>
                          </div>
                          <div class="content-box my-form-box">
                            <el-form-item label="控销类型:" label-width="100px">
                              <el-radio-group v-model="multiregionItem.controlSaleType" @change="(e) => controlSaleTypeChange(e, periodIndex, 0, multiregionIndex)">
                                <el-radio v-for="typeItem in agreementControlSaleType" :key="typeItem.value" :label="typeItem.value">
                                  {{ typeItem.label }}
                                </el-radio>
                              </el-radio-group>
                            </el-form-item>
                            <el-form-item label="控销条件:" label-width="100px" v-if="multiregionItem.controlSaleType != 1">
                              <el-checkbox-group v-model="multiregionItem.agreementRebateControlList" class="money-form-item">
                                <el-checkbox :label="1">区域</el-checkbox>
                                <el-form-item label="" class="money-form-item mar-r-16" v-if="multiregionItem.agreementRebateControlList.indexOf(1) != -1">
                                  <yl-button type="text" @click="selectClick(periodIndex, 0, multiregionIndex)">{{ multiregionItem.addAgreementRebateControlArea.jsonContent ? multiregionItem.addAgreementRebateControlArea.selectedDesc : '选择区域' }}</yl-button>
                                </el-form-item>
                                <el-checkbox :label="2" style="margin-left:32px;">客户类型</el-checkbox>
                                <el-form-item label="" class="money-form-item mar-l-16" v-if="multiregionItem.agreementRebateControlList.indexOf(2) != -1">
                                  <el-select collapse-tags v-model="multiregionItem.agreementRebateControlCustomerType" multiple placeholder="请选择">
                                    <el-option
                                      v-for="enterpriseTypeItem in enterpriseType"
                                      :key="enterpriseTypeItem.value"
                                      :label="enterpriseTypeItem.label"
                                      :value="enterpriseTypeItem.value">
                                    </el-option>
                                  </el-select>
                                </el-form-item>
                              </el-checkbox-group>
                            </el-form-item>
                          </div>
                          </div>
                          <!-- 多任务 -->
                          <div v-for="(multitaskItem, multitaskIndex) in multiregionItem.agreementRebateTaskStageList" :key="multitaskIndex">
                            <div class="multitask-form-item mar-b-10">
                              <!-- 销售任务量 -->
                              <div class="task-form-item flex-shrink-view mar-r-16 task-form-item-left" v-if="agreementRebateTerms.taskFlag == 1">
                                <span v-if="agreementRebateTerms.taskStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>销售任务量</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>购进任务量</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>付款任务量</span>
                                <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:48px;"><span class="red-text">*</span>任务量</span>
                                <el-form-item class="mar-r-16" :prop="'agreementRebateTimeSegmentList.' + periodIndex + '.agreementRebateGoodsGroupList.' + 0 + '.agreementRebateScopeList.' + multiregionIndex + '.agreementRebateTaskStageList.'+ multitaskIndex + '.taskNum'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                                  <el-input v-model="multitaskItem.taskNum" placeholder="请输入" @input="e => (multitaskItem.taskNum = checkInput(e, 99999999))"></el-input>
                                </el-form-item>
                                <el-form-item class="task-form-item">
                                  <el-select v-model="multitaskItem.taskUnit" placeholder="请选择" @change="(type) => taskUnitChange(type, periodIndex, 0, multiregionIndex, 1)">
                                    <el-option label="元" :value="1"></el-option>
                                    <el-option label="盒" :value="2"></el-option>
                                  </el-select>
                                </el-form-item>
                              </div>
                              <!-- 多梯度 -->
                              <div>
                                <div v-for="(multipleGradientsItem, multipleGradientsIndex) in multitaskItem.agreementRebateStageList" :key="multipleGradientsIndex">
                                  <div class="multipleGradients-form-item">
                                    <span class="font-title-color font-size-base label-height mar-r-16">满</span>
                                    <el-form-item class="task-form-item mar-r-16">
                                      <el-input v-model="multipleGradientsItem.full" @input="e => (multipleGradientsItem.full = checkInput(e, 99999999))" placeholder="请输入"></el-input>
                                    </el-form-item>
                                    <el-form-item class="task-form-item mar-r-16">
                                      <el-select v-model="multipleGradientsItem.fullUnit" placeholder="请选择" @change="(type) => fullUnitChange(type, periodIndex, 0, multiregionIndex, multitaskIndex, 1)">
                                        <el-option label="元" :value="1"></el-option>
                                        <el-option label="盒" :value="2"></el-option>
                                      </el-select>
                                    </el-form-item>
                                    <el-form-item label="返" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + periodIndex + '.agreementRebateGoodsGroupList.' + 0 + '.agreementRebateScopeList.' + multiregionIndex + '.agreementRebateTaskStageList.'+ multitaskIndex + '.agreementRebateStageList.' + multipleGradientsIndex + '.back'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                                      <el-input v-model="multipleGradientsItem.back" @keyup.native="multipleGradientsItem.back = onInput(multipleGradientsItem.back, 2)" placeholder="请输入"></el-input>
                                    </el-form-item>
                                    <el-form-item class="task-form-item mar-r-16">
                                      <el-select v-model="multipleGradientsItem.backUnit" placeholder="请选择" @change="(type) => fullUnitChange(type, periodIndex, 0, multiregionIndex, multitaskIndex, 2)">
                                        <el-option label="元" :value="1"></el-option>
                                        <el-option label="%" :value="2"></el-option>
                                      </el-select>
                                    </el-form-item>
                                    <span v-if="agreementRebateTerms.rebateStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于销售</span>
                                    <span v-else-if="agreementRebateTerms.rebateStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于购进</span>
                                    <span v-else-if="agreementRebateTerms.rebateStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于付款</span>
                                    <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于</span>
                                    <!-- 新增梯度 -->
                                    <el-form-item class="mar-r-16" v-if="multipleGradientsIndex == 0">
                                      <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addGradientsClick(periodIndex, 0, multiregionIndex, multitaskIndex)"></yl-button>
                                    </el-form-item>
                                    <!-- 删除梯度 -->
                                    <el-form-item class="mar-r-16" v-else>
                                      <yl-button icon="el-icon-remove-outline" type="text" @click="deleteGradientsClick(periodIndex, 0, multiregionIndex, multitaskIndex, multipleGradientsIndex)"></yl-button>
                                    </el-form-item>

                                  </div>
                                </div>
                              </div>
                              <!-- 超任务量汇总返 -->
                              <div class="task-form-item mar-l-32 task-form-item-right">
                                <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">超任务量汇总返</span>
                                <el-form-item class="task-form-item mar-r-16">
                                  <el-input v-model="multitaskItem.overSumBack" @input="e => (multitaskItem.overSumBack = checkInput(e, 999999))" placeholder="请输入"></el-input>
                                </el-form-item>
                                <el-form-item class="task-form-item mar-r-16">
                                  <el-select v-model="multitaskItem.overSumBackUnit" placeholder="请选择" @change="(type) => taskUnitChange(type, periodIndex, 0, multiregionIndex, 2)">
                                    <el-option label="元" :value="1"></el-option>
                                    <el-option label="%" :value="2"></el-option>
                                  </el-select>
                                </el-form-item>
                                <span v-if="agreementRebateTerms.taskStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于销售</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于购进</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于付款</span>
                                <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于</span>
                                <!-- 删除任务 -->
                                <el-form-item class="mar-r-16">
                                  <yl-button icon="el-icon-remove-outline" type="text" @click="deleteTaskClick(periodIndex, 0, multiregionIndex, multitaskIndex)"></yl-button>
                                </el-form-item>
                              </div>

                            </div>
                          </div>
                          <!-- 新增任务返利 -->
                          <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="text" @click="addTaskClick(periodIndex, 0, multiregionIndex)">新增任务量返利</yl-button></div>
                        </div>
                      </div>
                      <!-- 新增范围 -->
                      <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="primary" plain @click="addScopeClick(periodIndex, 0)">新增返利范围设置</yl-button></div>
                    </div>
                  </div>
                </div>
                <div class="add-period-view" v-if="tabsActiveName != 1" style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="primary" @click="addPeriodClick">新增子时段配置</yl-button></div>
              </div>
            </div>
          </div>
        </div>
        <!-- 分类设置 多时段组合配置 -->
        <div class="tabs-view" v-if="agreementRebateTerms.goodsRebateRuleType == 2">
          <!-- tab切换 -->
          <div class="tab">
              <div v-for="(timeSegmentTypItem, timeSegmentTypIndex) in agreementTimeSegmentTypeSet" :key="timeSegmentTypIndex" v-show="timeSegmentTypItem.value != 3" class="tab-item" :class="[tabsActiveName == timeSegmentTypItem.value ? 'tab-active' : '', agreementType == 5 ? 'disable' : '']" @click="clickTab(timeSegmentTypItem.value)">{{ timeSegmentTypItem.label }}</div>
          </div>
          <!-- 分类设置 时段配置 -->
          <div class="top-bar multiperiod-view">
            <!-- 分类设置 -->
            <div>
              <el-form-item label="核心商品组关联性:">
                <el-radio-group v-model="agreementRebateTerms.coreCommodityGroupRelevance">
                  <el-radio v-for="item in agreementCoreCommodityGroupRelevance" :key="item.value" :label="item.value">
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="当核心商品组任务量未完成时:">
                <el-radio-group v-model="agreementRebateTerms.coreCommodityGroupFail">
                  <el-radio v-for="item in agreementCoreCommodityGroupFail" :key="item.value" :label="item.value">
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            <!-- 添加时段 -->
            <div class="add-data-view mar-b-10" v-if="tabsActiveName != 1">
              <div class="add-data-item" :class="classifyCurrentIndex == dateIndex ? 'active' : ''" v-for="(dateItem, dateIndex) in agreementRebateTerms.agreementRebateTimeSegmentList" :key="dateIndex">
                <div class="font-size-base mar-b-8 flex-row-left" @click="selectedDateClick(dateIndex)">{{ dateItem.startTime }}至<el-link type="primary" v-if="dateIndex != 0" class="mar-l-16" icon="el-icon-remove-outline" @click.stop="deletePeriodClick(dateIndex)"></el-link></div>
                <el-date-picker
                v-model="dateItem.endTime"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy-MM-dd"
                  @change="(e) => endTimeChange(e, dateIndex)"
                  placeholder="选择日期">
                </el-date-picker>
              </div>
              <div class="add-data-item">
                <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addPeriodClick()">新增时段</yl-button>
              </div>
            </div>
            <!-- ka协议时才可能存在（规模返利、基础服务奖励、项目服务奖励） -->
            <div v-if="agreementType == 5">
              <el-form-item label="">
                <el-checkbox v-model="agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].scaleRebateFlag" @change="(flag) => kaRebateFlagChange(flag, 1)">规模返利</el-checkbox>
                <el-checkbox v-model="agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].basicServiceRewardFlag" @change="(flag) => kaRebateFlagChange(flag, 2)">基础服务奖励</el-checkbox>
                <el-checkbox :disabled="kaAgreementType == 4" v-model="agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].projectServiceRewardFlag" @change="(flag) => kaRebateFlagChange(flag, 3)">项目服务奖励</el-checkbox>
              </el-form-item>
            </div>
            <div class="my-form-box">
              <div class="">
                <!-- 多商品组 -->
                <div v-for="(groupItem, groupIndex) in agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].agreementRebateGoodsGroupList" :key="groupIndex">
                  <div class="multiperiod-form-item mar-b-16">
                    <div class="title">
                      <div class="mar-b-10">
                        <div class="mar-b-10">
                          <div v-if="groupItem.groupType == 1">全商品主任务</div>
                          <div v-else class="add-goods-item">
                            <div class="add-goods-item-left">
                              <span class="font-size-base label-height mar-r-16">商品组{{ groupIndex }}</span>
                              <el-form-item class="task-form-item">
                                <el-select placeholder="请选择" v-model="groupItem.groupCategory">
                                  <el-option label="普通商品组" :value="1"></el-option>
                                  <el-option label="核心商品组" :value="2"></el-option>
                                </el-select>
                              </el-form-item>
                            </div>
                            <div>
                              <yl-button icon="el-icon-remove-outline" type="primary" plain @click="deleteGroupGoodsClick(classifyCurrentIndex, groupIndex)">删除商品组</yl-button>
                              <ylButton type="primary" plain @click="goImport(classifyCurrentIndex, groupIndex)">导入</ylButton>
                              <ylButton type="primary" plain @click="downLoadTemplate">模版导出</ylButton>
                              <yl-button icon="el-icon-remove-outline" type="primary" plain @click="deleteGoodsClick(classifyCurrentIndex, groupIndex)">删除商品</yl-button>
                              <yl-button icon="el-icon-circle-plus-outline" type="primary" @click="addGoodsClick(classifyCurrentIndex, groupIndex)">新增商品</yl-button>
                            </div>
                          </div>
                        </div>
                        <!-- 全商品 -->
                        <div class="table-view" v-if="groupItem.groupType == 1">
                          <yl-table
                            border
                            show-header
                            :list="[1]"
                            :total="0"
                          >
                            <el-table-column label="商品" align="center">
                              <template slot-scope="">
                                <div class="operation-view">供销协议条款约定的全品</div>
                              </template>
                            </el-table-column>
                            <el-table-column label="厂家/供应商" align="center">
                              <template slot-scope="">
                                <div class="operation-view">{{ ename }}</div>
                              </template>
                            </el-table-column>
                            <el-table-column label="当前商品数量" align="center">
                              <template slot-scope="">
                                <div class="operation-view">{{ isAllGoods == 1 ? allGoodsNum : cateGoodsNum }}</div>
                              </template>
                            </el-table-column>
                          </yl-table>
                        </div>
                        <!-- 商品组 商品 -->
                        <div v-if="groupItem.groupType == 2">
                          <yl-table
                            :selection-change="(selection) => deleteSelectionChange(selection, classifyCurrentIndex, groupIndex)"
                            border
                            show-header
                            :height="600"
                            :list="groupItem.agreementRebateGoodsList"
                            :total="0"
                          >
                            <el-table-column type="selection" width="55"></el-table-column>
                            <el-table-column label="商品编号" align="center">
                              <template slot-scope="{ row }">
                                <div class="font-size-base">{{ row.goodsId }}</div>
                              </template>
                            </el-table-column>
                            <el-table-column label="商品名称" min-width="162" align="center">
                              <template slot-scope="{ row }">
                                <div class="goods-desc">
                                  <div class="font-size-base">{{ row.goodsName }}</div>
                                </div>
                              </template>
                            </el-table-column>
                            <el-table-column label="商品规格" min-width="150" align="center">
                              <template slot-scope="{ row }">
                                <div class="font-size-base">{{ row.specifications }}</div>
                              </template>
                            </el-table-column>
                          </yl-table>
                        </div>
                      </div>
                      <!-- 多范围 -->
                      <div v-for="(multiregionItem, multiregionIndex) in groupItem.agreementRebateScopeList" :key="multiregionIndex">
                        <div class="multiregion-form-item mar-b-10">
                          <div>
                            <div class="header-bar header-renative">
                            <div class="sign"></div>返利范围设置<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" @click="deleteRegionClick(classifyCurrentIndex, groupIndex, multiregionIndex)"></yl-button>
                          </div>
                          <div class="content-box my-form-box">
                            <el-form-item label="控销类型:" label-width="100px">
                              <el-radio-group v-model="multiregionItem.controlSaleType" @change="(e) => controlSaleTypeChange(e, classifyCurrentIndex, groupIndex, multiregionIndex)">
                                <el-radio v-for="typeItem in agreementControlSaleType" :key="typeItem.value" :label="typeItem.value">
                                  {{ typeItem.label }}
                                </el-radio>
                              </el-radio-group>
                            </el-form-item>
                            <el-form-item label="控销条件:" label-width="100px" v-if="multiregionItem.controlSaleType != 1">
                              <el-checkbox-group v-model="multiregionItem.agreementRebateControlList" class="money-form-item">
                                <el-checkbox :label="1">区域</el-checkbox>
                                <el-form-item label="" class="money-form-item mar-r-16" v-if="multiregionItem.agreementRebateControlList.indexOf(1) != -1">
                                  <yl-button type="text" @click="selectClick(classifyCurrentIndex, groupIndex, multiregionIndex)">{{ multiregionItem.addAgreementRebateControlArea.jsonContent ? multiregionItem.addAgreementRebateControlArea.selectedDesc : '选择区域' }}</yl-button>
                                </el-form-item>
                                <el-checkbox :label="2" style="margin-left:32px;">客户类型</el-checkbox>
                                <el-form-item label="" class="money-form-item mar-l-16" v-if="multiregionItem.agreementRebateControlList.indexOf(2) != -1">
                                  <el-select collapse-tags v-model="multiregionItem.agreementRebateControlCustomerType" multiple placeholder="请选择">
                                    <el-option
                                      v-for="enterpriseTypeItem in enterpriseType"
                                      :key="enterpriseTypeItem.value"
                                      :label="enterpriseTypeItem.label"
                                      :value="enterpriseTypeItem.value">
                                    </el-option>
                                  </el-select>
                                </el-form-item>
                              </el-checkbox-group>
                            </el-form-item>
                          </div>
                          </div>
                          <!-- 多任务 -->
                          <div v-for="(multitaskItem, multitaskIndex) in multiregionItem.agreementRebateTaskStageList" :key="multitaskIndex">
                            <div class="multitask-form-item mar-b-10">
                              <!-- 销售任务量 -->
                              <div class="task-form-item flex-shrink-view mar-r-16 task-form-item-left" v-if="agreementRebateTerms.taskFlag == 1">
                                <span v-if="agreementRebateTerms.taskStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>销售任务量</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>购进任务量</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:75px;"><span class="red-text">*</span>付款任务量</span>
                                <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view" style="width:48px;"><span class="red-text">*</span>任务量</span>
                                <el-form-item class="mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateGoodsGroupList.' + groupIndex + '.agreementRebateScopeList.' + multiregionIndex + '.agreementRebateTaskStageList.'+ multitaskIndex + '.taskNum'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                                  <el-input v-model="multitaskItem.taskNum" placeholder="请输入" @input="e => (multitaskItem.taskNum = checkInput(e, 99999999))"></el-input>
                                </el-form-item>
                                <el-form-item class="task-form-item">
                                  <el-select v-model="multitaskItem.taskUnit" placeholder="请选择" @change="(type) => taskUnitChange(type, classifyCurrentIndex, groupIndex, multiregionIndex, 1)">
                                    <el-option label="元" :value="1"></el-option>
                                    <el-option label="盒" :value="2"></el-option>
                                  </el-select>
                                </el-form-item>
                              </div>
                              <!-- 多梯度 -->
                              <div>
                                <div v-for="(multipleGradientsItem, multipleGradientsIndex) in multitaskItem.agreementRebateStageList" :key="multipleGradientsIndex">
                                  <div class="multipleGradients-form-item">

                                    <span class="font-title-color font-size-base label-height mar-r-16">满</span>
                                    <el-form-item class="task-form-item mar-r-16">
                                      <el-input v-model="multipleGradientsItem.full" @input="e => (multipleGradientsItem.full = checkInput(e, 99999999))" placeholder="请输入"></el-input>
                                    </el-form-item>
                                    <el-form-item prop="full" class="task-form-item mar-r-16">
                                      <el-select v-model="multipleGradientsItem.fullUnit" placeholder="请选择" @change="(type) => fullUnitChange(type, classifyCurrentIndex, groupIndex, multiregionIndex, multitaskIndex, 1)">
                                        <el-option label="元" :value="1"></el-option>
                                        <el-option label="盒" :value="2"></el-option>
                                      </el-select>
                                    </el-form-item>
                                    <el-form-item label="返" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateGoodsGroupList.' + groupIndex + '.agreementRebateScopeList.' + multiregionIndex + '.agreementRebateTaskStageList.'+ multitaskIndex + '.agreementRebateStageList.' + multipleGradientsIndex + '.back'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                                      <el-input v-model="multipleGradientsItem.back" @keyup.native="multipleGradientsItem.back = onInput(multipleGradientsItem.back, 2)" placeholder="请输入"></el-input>
                                    </el-form-item>
                                    <el-form-item class="task-form-item mar-r-16">
                                      <el-select v-model="multipleGradientsItem.backUnit" placeholder="请选择" @change="(type) => fullUnitChange(type, classifyCurrentIndex, groupIndex, multiregionIndex, multitaskIndex, 2)">
                                        <el-option label="元" :value="1"></el-option>
                                        <el-option label="%" :value="2"></el-option>
                                      </el-select>
                                    </el-form-item>
                                    <span v-if="agreementRebateTerms.rebateStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于销售</span>
                                    <span v-else-if="agreementRebateTerms.rebateStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于购进</span>
                                    <span v-else-if="agreementRebateTerms.rebateStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于付款</span>
                                    <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于</span>
                                    <!-- 新增梯度 -->
                                    <el-form-item class="mar-r-16" v-if="multipleGradientsIndex == 0">
                                      <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addGradientsClick(classifyCurrentIndex, groupIndex, multiregionIndex, multitaskIndex)"></yl-button>
                                    </el-form-item>
                                    <!-- 删除梯度 -->
                                    <el-form-item class="mar-r-16" v-else>
                                      <yl-button icon="el-icon-remove-outline" type="text" @click="deleteGradientsClick(classifyCurrentIndex, groupIndex, multiregionIndex, multitaskIndex, multipleGradientsIndex)"></yl-button>
                                    </el-form-item>

                                  </div>
                                </div>
                              </div>
                              <!-- 超任务量汇总返 -->
                              <div class="task-form-item mar-l-32 task-form-item-right">
                                <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">超任务量汇总返</span>
                                <el-form-item class="task-form-item mar-r-16">
                                  <el-input v-model="multitaskItem.overSumBack" @input="e => (multitaskItem.overSumBack = checkInput(e, 999999))" placeholder="请输入"></el-input>
                                </el-form-item>
                                <el-form-item class="task-form-item mar-r-16">
                                  <el-select v-model="multitaskItem.overSumBackUnit" placeholder="请选择" @change="(type) => taskUnitChange(type, classifyCurrentIndex, groupIndex, multiregionIndex, 2)">
                                    <el-option label="元" :value="1"></el-option>
                                    <el-option label="%" :value="2"></el-option>
                                  </el-select>
                                </el-form-item>
                                <span v-if="agreementRebateTerms.taskStandard == 1" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于销售</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 2" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于购进</span>
                                <span v-else-if="agreementRebateTerms.taskStandard == 3" class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于付款</span>
                                <span v-else class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">基于</span>
                                <!-- 删除任务 -->
                                <el-form-item class="mar-r-16">
                                  <yl-button icon="el-icon-remove-outline" type="text" @click="deleteTaskClick(classifyCurrentIndex, groupIndex, multiregionIndex, multitaskIndex)"></yl-button>
                                </el-form-item>
                              </div>
                            </div>
                          </div>
                          <!-- 多任务 -->
                          <!-- 新增任务返利 -->
                          <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="text" @click="addTaskClick(classifyCurrentIndex, groupIndex, multiregionIndex)">新增任务量返利</yl-button></div>
                        </div>
                      </div>
                      <!-- 新增范围 -->
                      <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="primary" plain @click="addScopeClick(classifyCurrentIndex, groupIndex)">新增返利范围设置</yl-button></div>
                    </div>
                  </div>
                </div>
                <div class="add-period-view" style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="primary" @click="addGoodsGroupClick">新增商品组</yl-button></div>
              </div>
            </div>
            <!-- ka协议时才可能存在（规模返利、基础服务奖励、项目服务奖励） -->
            <!-- 规模返利 -->
            <div class="ka-item mar-t-10" v-if="agreementType == 5 && agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].scaleRebateFlag">
              <span class="font-important-color font-size-base label-height mar-r-16">规模返利</span>
              <div>
                <div v-for="(scaleRebateItem, scaleRebateIndex) in agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].agreementScaleRebateList" :key="scaleRebateIndex">
                  <div class="kaGradients-form-item">
                    <el-form-item label="目标达成率" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementScaleRebateList.' + scaleRebateIndex + '.targetReachRatio'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="scaleRebateItem.targetReachRatio" @keyup.native="scaleRebateItem.targetReachRatio = onInput(scaleRebateItem.targetReachRatio, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <el-form-item label="目标返利率" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementScaleRebateList.' + scaleRebateIndex + '.targetRebateRatio'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="scaleRebateItem.targetRebateRatio" @keyup.native="scaleRebateItem.targetRebateRatio = onInput(scaleRebateItem.targetRebateRatio, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <!-- 新增梯度 -->
                    <el-form-item class="mar-r-16" v-if="scaleRebateIndex == 0">
                      <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addKaScaleRebateGradientsClick(1, classifyCurrentIndex)"></yl-button>
                    </el-form-item>
                    <!-- 删除梯度 -->
                    <el-form-item class="mar-r-16" v-else>
                      <yl-button icon="el-icon-remove-outline" type="text" @click="deleteKaScaleRebateGradientsClick(1, classifyCurrentIndex, scaleRebateIndex)"></yl-button>
                    </el-form-item>

                  </div>
                </div>
              </div>
            </div>
            <!-- 基础服务奖励 -->
            <div class="ka-item mar-t-10" v-if="agreementType == 5 && agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].basicServiceRewardFlag">
              <span class="font-important-color font-size-base label-height mar-r-16">基础服务奖励</span>
              <div>
                <div v-for="(basicServiceItem, basicServiceIndex) in agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].agreementRebateBasicServiceRewardList" :key="basicServiceIndex">
                  <div class="kaGradients-form-item">
                    <el-form-item label="增长率" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateBasicServiceRewardList.' + basicServiceIndex + '.increaseRatio'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="basicServiceItem.increaseRatio" @keyup.native="basicServiceItem.increaseRatio = onInput(basicServiceItem.increaseRatio, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <el-form-item label="返利" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateBasicServiceRewardList.' + basicServiceIndex + '.rebateNum'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="basicServiceItem.rebateNum" @keyup.native="basicServiceItem.rebateNum = onInput(basicServiceItem.rebateNum, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <!-- 新增梯度 -->
                    <el-form-item class="mar-r-16" v-if="basicServiceIndex == 0">
                      <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addKaScaleRebateGradientsClick(2, classifyCurrentIndex)"></yl-button>
                    </el-form-item>
                    <!-- 删除梯度 -->
                    <el-form-item class="mar-r-16" v-else>
                      <yl-button icon="el-icon-remove-outline" type="text" @click="deleteKaScaleRebateGradientsClick(2, classifyCurrentIndex, basicServiceIndex)"></yl-button>
                    </el-form-item>

                  </div>
                </div>
              </div>
            </div>
            <!-- 项目服务奖励 -->
            <div class="ka-item mar-t-10" v-if="agreementType == 5 && agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].projectServiceRewardFlag">
              <span class="font-important-color font-size-base label-height mar-r-16">项目服务奖励</span>
              <div>
                <div v-for="(projectServiceItem, projectServiceIndex) in agreementRebateTerms.agreementRebateTimeSegmentList[classifyCurrentIndex].agreementRebateProjectServiceRewardList" :key="projectServiceIndex">
                  <div class="kaGradients-form-item">
                    <el-form-item label="覆盖率" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateProjectServiceRewardList.' + projectServiceIndex + '.coverRatio'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="projectServiceItem.coverRatio" @keyup.native="projectServiceItem.coverRatio = onInput(projectServiceItem.coverRatio, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <el-form-item label="其它" class="task-form-item mar-r-16">
                      <el-input v-model="projectServiceItem.other" @keyup.native="projectServiceItem.other = onInput(projectServiceItem.other, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <el-form-item label="返利" class="task-form-item mar-r-16" :prop="'agreementRebateTimeSegmentList.' + classifyCurrentIndex + '.agreementRebateProjectServiceRewardList.' + projectServiceIndex + '.rebateNum'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                      <el-input v-model="projectServiceItem.rebateNum" @keyup.native="projectServiceItem.rebateNum = onInput(projectServiceItem.rebateNum, 2)" placeholder="请输入"></el-input>
                    </el-form-item>
                    <span class="font-title-color font-size-base label-height mar-r-16 flex-shrink-view">％</span>
                    <!-- 新增梯度 -->
                    <el-form-item class="mar-r-16" v-if="projectServiceIndex == 0">
                      <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addKaScaleRebateGradientsClick(3, classifyCurrentIndex)"></yl-button>
                    </el-form-item>
                    <!-- 删除梯度 -->
                    <el-form-item class="mar-r-16" v-else>
                      <yl-button icon="el-icon-remove-outline" type="text" @click="deleteKaScaleRebateGradientsClick(3, classifyCurrentIndex, projectServiceIndex)"></yl-button>
                    </el-form-item>

                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
      <!-- 备注 -->
      <div class="top-bar">
        <div class="header-bar header-renative">
          备注
        </div>
        <div class="content-box my-form-box">
          <el-form-item >
            <el-input type="textarea" :rows="3" placeholder="请输入备注" maxlength="200" v-model="agreementRebateTerms.remark"></el-input>
          </el-form-item>
        </div>
      </div>
    </div>
    <!-- 指定商业公司 -->
    <yl-dialog title="供应商信息查询" :visible.sync="addProviderVisible" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">供应商名称</div>
                <el-input v-model="addProviderQuery.ename" placeholder="请输入供应商名称" @keyup.enter.native="addProviderHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="addProviderTotal"
                  @search="addProviderHandleSearch"
                  @reset="addProviderHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="addProviderList"
            :total="addProviderTotal"
            :page.sync="addProviderQuery.page"
            :limit.sync="addProviderQuery.limit"
            :loading="loading3"
            @getList="getBusinessPage"
          >
            <el-table-column label="企业ID" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.eid }}</div>
              </template>
            </el-table-column>
            <el-table-column label="供应商名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="供应商类型" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.type | dictLabel(enterpriseType) }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="地区" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.address }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" v-if="!row.isSelect" type="text" @click="addProviderItemClick(row)">添加</yl-button>
                  <yl-button class="view-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 选择区域 -->
    <city-select :show.sync="showCityDialog" :init-ids="selectedData" platform="pop" @choose="citySelectConfirm"></city-select>
    <!-- 添加商品弹窗 -->
    <yl-dialog title="添加商品" :visible.sync="addGoodsDialog" width="966px" @confirm="addGoodsConfirm">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品编号</div>
                <el-input v-model="goodsQuery.goodsId" placeholder="请输入商品编号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="goodsTotal"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            ref="multipleTable"
            :selection-change="handleSelectionChange"
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsTotal"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="loading2"
            @getList="getGoodsList"
          >
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="商品编号" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsId }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品规格" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.specifications }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 导入商品 -->
    <yl-dialog title="导入" :visible.sync="importDialog" aria-atomic="" :show-footer="false">
      <div class="import-dialog-content flex-row-center">
        <yl-upload-file
          :action="info.action"
          :extral-data="info.extralData"
          @onSuccess="onSuccess"
        />
      </div>
    </yl-dialog>
  </el-form>
</template>

<script>
import {
  queryBusinessPage,
  queryGoodsPageList,
  getGoodsNumberByEid
} from '@/subject/pop/api/agreement';
import { agreementGoodsRebateRuleType, agreementRebatePay, enterpriseType, agreementRebateCashType, agreementRebateCashTime, agreementNotProductRebateType, agreementNotProductRebateAmountType, agreementRebateTaskStandard, agreementRebateStageMethod, agreementRebateCalculateRule, agreementRebateRuleType, agreementTimeSegmentTypeSet, agreementControlSaleType, agreementCoreCommodityGroupRelevance, agreementCoreCommodityGroupFail } from '@/subject/pop/utils/busi';
import citySelect from '@/subject/pop/components/CitySelect'
import { formatDate } from '@/subject/pop/utils'
import { downloadByUrl } from '@/subject/pop/utils'
import { ylUploadFile } from '@/subject/pop/components'
import { onInputLimit } from '@/common/utils'

export default {
  name: 'AgreementRebate',
  components: {
    citySelect,
    ylUploadFile
  },
  computed: {
    // 商品返利规则设置方式
    agreementGoodsRebateRuleType() {
      return agreementGoodsRebateRuleType()
    },
    // 返利支付方
    agreementRebatePay() {
      return agreementRebatePay()
    },
    // 企业类型
    enterpriseType() {
      return enterpriseType();
    },
    // 返利兑付方式
    agreementRebateCashType() {
      return agreementRebateCashType();
    },
    // 返利兑付时间
    agreementRebateCashTime() {
      return agreementRebateCashTime();
    },
    // 非商品返利方式
    agreementNotProductRebateType() {
      return agreementNotProductRebateType();
    },
    // 金额类型
    agreementNotProductRebateAmountType() {
      return agreementNotProductRebateAmountType();
    },
    // 返利标准
    agreementRebateTaskStandard() {
      return agreementRebateTaskStandard();
    },
    // 返利阶梯条件计算方法
    agreementRebateStageMethod() {
      return agreementRebateStageMethod();
    },
    // 返利计算规则
    agreementRebateCalculateRule() {
      return agreementRebateCalculateRule()
    },
    // 返利计算规则类型
    agreementRebateRuleType() {
      return agreementRebateRuleType()
    },
    // 时段类型设置
    agreementTimeSegmentTypeSet() {
      return agreementTimeSegmentTypeSet()
    },
    // 控销类型
    agreementControlSaleType() {
      return agreementControlSaleType();
    },
    // 核心商品组关联性
    agreementCoreCommodityGroupRelevance() {
      return agreementCoreCommodityGroupRelevance();
    },
    // 核心商品组任务量未完成时
    agreementCoreCommodityGroupFail() {
      return agreementCoreCommodityGroupFail();
    }
  },
  watch: {
  },
  data() {
    return {
      // 全品商品数量
      isAllGoods: 0,
      allGoodsNum: 0,
      cateGoodsNum: 0,
      // 协议类型
      agreementType: '',
      // KA协议类型
      kaAgreementType: '',
      NO_10: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_agreementv2_goods_template_v20220316.xls',
      // 导入商品弹窗
      importDialog: false,
      info: {
        action: '',
        extralData: {}
      },
      // 甲方ID
      eid: '',
      // 甲方类型
      firstType: '',
      // 生效日期
      startTime: '',
      endTime: '',
      // 分类设置 当前时段
      classifyCurrentIndex: 0,
      // 指定商业公司
      addProviderVisible: false,
      loading3: false,
      addProviderQuery: {
        page: 1,
        limit: 10
      },
      addProviderList: [],
      addProviderTotal: 0,
      // 选择区域
      selectPeriodIndex: 0,
      selectGroupIndex: 0,
      selectMultiregionIndex: 0,
      showCityDialog: false,
      selectedDesc: '',
      selectedData: [],

      // 添加商品弹窗
      addGoodsPeriodIndex: 0,
      addGoodsGroupIndex: 0,
      addGoodsDialog: false,
      loading2: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      // 表格选择
      multipleSelection: [],

      // 多时段配置
      tabsActiveName: 1,
      agreementRebateTerms: {
        // 是否底价供货
        reserveSupplyFlag: 0,
        // 商品返利规则设置方式：1-全品设置 2-分类设置
        goodsRebateRuleType: '',
        // 返利支付方
        rebatePay: 1,
        // 指定商业公司时
        agreementRebatePayEnterpriseList: [],
        // 返利上限(元)
        maxRebate: '',
        // 返利兑付方式
        rebateCashType: 1,
        // 返利兑付时间
        rebateCashTime: 2,
        // 返利兑付时段
        rebateCashSegment: '',
        // 返利兑付单位：1-月 2-天
        rebateCashUnit: 2,
        // 其他备注
        otherRemark: '',
        // 非商品返利集合（最多6个阶梯)
        agreementOtherRebateList: [],
        // 是否有任务量：0-否 1-是
        taskFlag: 0,
        // 返利标准
        rebateStandard: '',
        // 任务量标准：1-销售 2-购进 3-付款金额
        taskStandard: '',
        // 返利阶梯条件计算方法
        rebateStageMethod: '',
        // 返利计算单价
        rebateCalculatePrice: '',
        // 返利计算规则
        rebateCalculateRule: '',
        // 返利计算规则类型
        rebateRuleType: '',
        // 时段类型设置
        timeSegmentTypeSet: 1,
        // 全品设置
        // 是否兑付全时段：0-否 1-是（有未达成任务量的子时段时）
        cashAllSegmentFlag: false,
        // 是否兑付子时段：0-否 1-是（有未达成任务量的子时段时）
        cashChildSegmentFlag: false,
        // 分类设置
        // 核心商品组关联性
        coreCommodityGroupRelevance: '',
        // 核心商品组任务量未完成时
        coreCommodityGroupFail: '',
        // 协议返利时段集合
        agreementRebateTimeSegmentList: [
          {
            // 时段类型：1-全时段 2-子时段
            type: 1,
            // 时段开始时间
            startTime: '',
            // 时段结束时间
            endTime: '',
            // 规模返利
            scaleRebateFlag: false,
            // 基础服务奖励
            basicServiceRewardFlag: false,
            // 项目服务奖励
            projectServiceRewardFlag: false,
            // 规模返利阶梯集合
            agreementScaleRebateList: [],
            // 基础服务奖励阶梯集合
            agreementRebateBasicServiceRewardList: [],
            // 项目服务奖励阶梯集合
            agreementRebateProjectServiceRewardList: [],
            // 协议返利商品组集合
            agreementRebateGoodsGroupList: [
              {
                // 商品组分类：1-普通商品组 2-核心商品组
                groupCategory: '',
                groupName: '',
                // 1-全商品主任务 2-子商品组
                groupType: 1,
                // 协议返利商品组对应的商品集合
                agreementRebateGoodsList: [],
                // 协议返利范围集合
                agreementRebateScopeList: [
                  {
                    // 控销类型：1-无 2-黑名单 3-白名单
                    controlSaleType: 1,
                    // 协议返利控销条件：1-区域 2-客户类型
                    agreementRebateControlList: [],
                    // 协议返利控销区域
                    addAgreementRebateControlArea: { jsonContent: '' },
                    // 协议返利控销客户类型
                    agreementRebateControlCustomerType: [],
                    // 协议返利任务量
                    agreementRebateTaskStageList: [
                      {
                        // 任务量
                        taskNum: '',
                        // 任务量单位：1-元 2-盒
                        taskUnit: 1,
                        // 超任务量汇总返
                        overSumBack: '',
                        // 超任务量汇总返单位：1-元 2-%
                        overSumBackUnit: 1,
                        // 协议返利阶梯集合
                        agreementRebateStageList: [
                          {
                            // 满
                            full: '',
                            // 满单位：1-元 2-盒
                            fullUnit: 1,
                            // 返
                            back: '',
                            // 返单位：1-元 2-%
                            backUnit: 1
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ],
        // 备注
        remark: ''
      },
      agreementRebateTermsRules: {
        reserveSupplyFlag: [{ required: true, message: '请选择是否底价供货', trigger: 'change' }],
        goodsRebateRuleType: [{ required: true, message: '请选择', trigger: 'change' }],
        rebatePay: [{ required: true, message: '请选择返利支付方', trigger: 'change' }],
        rebateCashType: [{ required: true, message: '请选择返利兑付方式', trigger: 'change' }],
        rebateCashTime: [{ required: true, message: '请选择返利兑付时间', trigger: 'change' }],
        rebateCashSegment: [{ required: true, message: '请输入', trigger: 'blur' }],
        rebateCashUnit: [{ required: true, message: '请选择', trigger: 'change' }],
        taskFlag: [{ required: true, message: '请选择', trigger: 'change' }],
        rebateStandard: [{ required: true, message: '请选择返利标准', trigger: 'change' }],
        taskStandard: [{ required: true, message: '请选择任务量标准', trigger: 'change' }],
        rebateStageMethod: [{ required: true, message: '请选择', trigger: 'change' }],
        rebateCalculatePrice: [{ required: true, message: '请选择返利计算单价', trigger: 'change' }],
        rebateCalculateRule: [{ required: true, message: '请选择返利计算规则', trigger: 'change' }],
        rebateRuleType: [{ required: true, message: '请选择', trigger: 'change' }]
      }
    };
  },
  mounted() {
  },
  methods: {
    init(eid, firstType, effectTime, ename, isAllGoods, cateGoodsNum, agreementType, kaAgreementType) {
      this.$log('agreementRebateTerms:', eid, firstType, cateGoodsNum, agreementType, kaAgreementType)
      this.eid = eid
      this.firstType = firstType
      this.ename = ename
      this.isAllGoods = isAllGoods
      this.cateGoodsNum = cateGoodsNum
      
      if (effectTime && effectTime.length == 2) {
        this.startTime = effectTime[0]
        this.endTime = effectTime[1]
      }

      // ka协议
      if (agreementType == 5) {
        this.clickTab(2)

        this.agreementRebateTerms.goodsRebateRuleType = 2
        this.tabsActiveName = 2
      }
      this.agreementType = agreementType
      this.kaAgreementType = kaAgreementType

      this.getGoodsNum()
    },
    async getGoodsNum() {
      let data = await getGoodsNumberByEid(
        this.eid,
        this.firstType
      );
      if (data) {
        this.allGoodsNum = data
      }
    },
    // 下一步点击
    stepClickMethods(callback) {
      this.$log('stepClickMethods')
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          this.$log('stepClickMethods------')
          if (this.checkInputData()) {
            if (callback) callback(this.agreementRebateTerms)
          }
        } else {
          return false;
        }
      })
    },
    // 返利支付方切换
    rebatePayRadioChange(type) {
      if (type != 2) {// 不为指定商业公司
        this.agreementRebateTerms.agreementRebatePayEnterpriseList = []
      }
    },
    // ***************指定商业公司***************
    addProviderClick() {
      this.addProviderVisible = true
      this.getBusinessPage()
    },
    addProviderHandleSearch() {
      this.addProviderQuery.page = 1
      this.getBusinessPage()
    },
    addProviderHandleReset() {
      this.addProviderQuery = {
        page: 1,
        limit: 10
      }
    },
    // 查询指定商业公司
    async getBusinessPage() {
      this.loading3 = true
      let addProviderQuery = this.addProviderQuery
      let data = await queryBusinessPage(
        addProviderQuery.page,
        addProviderQuery.limit,
        addProviderQuery.ename
      );
      this.loading3 = false
      if (data) {
        let currentArr = this.agreementRebateTerms.agreementRebatePayEnterpriseList
        if (currentArr.length > 0) {
          data.records.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.eid == item.eid;
            });
            if (hasIndex != -1) {
              item.isSelect = true;
            } else {
              item.isSelect = false;
            }
          });
        }
        this.addProviderList = data.records
        this.addProviderTotal = data.total
      }
    },
    addProviderItemClick(row) {
      let currentArr = this.agreementRebateTerms.agreementRebatePayEnterpriseList
      if (currentArr && currentArr.length > 0 && currentArr.length == 5) {
        this.$common.warn('最多指定五个商业公司')
        return
      }
      currentArr.push(this.$common.clone(row))
      this.agreementRebateTerms.agreementRebatePayEnterpriseList = currentArr

      let arr = this.addProviderList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });

       this.addProviderList = this.$common.clone(arr);

    },
    providerRemoveClick(index) {
      let currentArr = this.agreementRebateTerms.agreementRebatePayEnterpriseList
      currentArr.splice(index, 1)
      this.agreementRebateTerms.agreementRebatePayEnterpriseList = currentArr
    },
    // ***************指定商业公司***************
    // ***************非商品返利*****************
    //
    addOtherRebateClick() {
      let agreementOtherRebateList = this.agreementRebateTerms.agreementOtherRebateList
      if (agreementOtherRebateList && agreementOtherRebateList.length > 0 && agreementOtherRebateList.length == 6) {
        this.$common.warn('最多为6个阶梯')
        return
      }
      let addOtherRebateItem = {
        // 非商品返利方式
        rebateType: '',
        // 金额类型
        amountType: '',
        amount: '',
        // 单位：1-固定金额 2-百分比
        unit: 1,
        // 是否含税：0-否 1-是
        taxFlag: true
      }
      agreementOtherRebateList.push(addOtherRebateItem)
    },
    deleteOtherRebateClick(index) {
      let currentArr = this.agreementRebateTerms.agreementOtherRebateList
      currentArr.splice(index, 1)
      this.agreementRebateTerms.agreementOtherRebateList = currentArr
    },
    // ***************非商品返利***************
    // ***************返利标准设置*****************
    taskFlagRadioChange(type) {
      if (type == 0) {// 无任务量
        this.agreementRebateTerms.taskStandard = ''
      }
    },
    // 是否底价供货
    reserveSupplyFlagChange(type) {
      let agreementRebateTerms = {
          // 是否底价供货
          reserveSupplyFlag: type,
          // 商品返利规则设置方式：1-全品设置 2-分类设置
          goodsRebateRuleType: '',
          // 返利支付方
          rebatePay: 1,
          // 指定商业公司时
          agreementRebatePayEnterpriseList: [],
          // 返利上限(元)
          maxRebate: '',
          // 返利兑付方式
          rebateCashType: 1,
          // 返利兑付时间
          rebateCashTime: 2,
          // 返利兑付时段
          rebateCashSegment: '',
          // 返利兑付单位：1-月 2-天
          rebateCashUnit: 2,
          // 其他备注
          otherRemark: '',
          // 非商品返利集合（最多6个阶梯)
          agreementOtherRebateList: [],
          // 是否有任务量：0-否 1-是
          taskFlag: 0,
          // 返利标准
          rebateStandard: '',
          // 任务量标准：1-销售 2-购进 3-付款金额
          taskStandard: '',
          // 返利阶梯条件计算方法
          rebateStageMethod: '',
          // 返利计算单价
          rebateCalculatePrice: '',
          // 返利计算规则
          rebateCalculateRule: '',
          // 返利计算规则类型
          rebateRuleType: '',
          // 时段类型设置
          timeSegmentTypeSet: 1,
          // 全品设置
          // 是否兑付全时段：0-否 1-是（有未达成任务量的子时段时）
          cashAllSegmentFlag: false,
          // 是否兑付子时段：0-否 1-是（有未达成任务量的子时段时）
          cashChildSegmentFlag: false,
          // 分类设置
          // 核心商品组关联性
          coreCommodityGroupRelevance: '',
          // 核心商品组任务量未完成时
          coreCommodityGroupFail: '',
          // 协议返利时段集合
          agreementRebateTimeSegmentList: [
            {
              // 时段类型：1-全时段 2-子时段
              type: 1,
              // 时段开始时间
              startTime: '',
              // 时段结束时间
              endTime: '',
              // 规模返利
              scaleRebateFlag: false,
              // 基础服务奖励
              basicServiceRewardFlag: false,
              // 项目服务奖励
              projectServiceRewardFlag: false,
              // 规模返利阶梯集合
              agreementScaleRebateList: [],
              // 基础服务奖励阶梯集合
              agreementRebateBasicServiceRewardList: [],
              // 项目服务奖励阶梯集合
              agreementRebateProjectServiceRewardList: [],
              // 协议返利商品组集合
              agreementRebateGoodsGroupList: [
                {
                  // 商品组分类：1-普通商品组 2-核心商品组
                  groupCategory: '',
                  groupName: '',
                  // 1-全商品主任务 2-子商品组
                  groupType: 1,
                  // 协议返利商品组对应的商品集合
                  agreementRebateGoodsList: [],
                  // 协议返利范围集合
                  agreementRebateScopeList: [
                    {
                      // 控销类型：1-无 2-黑名单 3-白名单
                      controlSaleType: 1,
                      // 协议返利控销条件：1-区域 2-客户类型
                      agreementRebateControlList: [],
                      // 协议返利控销区域
                      addAgreementRebateControlArea: { jsonContent: '' },
                      // 协议返利控销客户类型
                      agreementRebateControlCustomerType: [],
                      // 协议返利任务量
                      agreementRebateTaskStageList: [
                        {
                          // 任务量
                          taskNum: '',
                          // 任务量单位：1-元 2-盒
                          taskUnit: 1,
                          // 超任务量汇总返
                          overSumBack: '',
                          // 超任务量汇总返单位：1-元 2-%
                          overSumBackUnit: 1,
                          // 协议返利阶梯集合
                          agreementRebateStageList: [
                            {
                              // 满
                              full: '',
                              // 满单位：1-元 2-盒
                              fullUnit: 1,
                              // 返
                              back: '',
                              // 返单位：1-元 2-%
                              backUnit: 1
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            }
          ],
          // 备注
          remark: ''
        }
      this.agreementRebateTerms = agreementRebateTerms
    },
    // ***************返利标准设置*****************
    goodsRebateRuleTypeChange(type) {
      this.$log('goodsRebateRuleTypeChange:', type)
      this.tabsActiveName = 1
      this.classifyCurrentIndex = 0

      this.clickTab(1)
    },
    // 选择时段点击
    selectedDateClick(dateIndex) {
      this.classifyCurrentIndex = dateIndex
    },
    // 全品设置 全时段切换
    clickTab(e) {
      if (this.agreementType == 5) {
        return
      }
      this.tabsActiveName = e

      // 清楚有未达成任务量的子时段时 核心商品组关联性
      this.agreementRebateTerms.cashAllSegmentFlag = false
      this.agreementRebateTerms.cashChildSegmentFlag = false
      this.agreementRebateTerms.coreCommodityGroupRelevance = ''
      this.agreementRebateTerms.coreCommodityGroupFail = ''

      this.classifyCurrentIndex = 0
      // 商品返利规则设置方式：1-全品设置 2-分类设置
      // let goodsRebateRuleType = this.agreementRebateTerms.goodsRebateRuleType
      let newPeriod = {
        // 时段类型：1-全时段 2-子时段
        type: 1,
        // 时段开始时间
        startTime: '',
        // 时段结束时间
        endTime: '',
        // 规模返利
        scaleRebateFlag: false,
        // 基础服务奖励
        basicServiceRewardFlag: false,
        // 项目服务奖励
        projectServiceRewardFlag: false,
        // 规模返利阶梯集合
        agreementScaleRebateList: [],
        // 基础服务奖励阶梯集合
        agreementRebateBasicServiceRewardList: [],
        // 项目服务奖励阶梯集合
        agreementRebateProjectServiceRewardList: [],
        // 协议返利商品组集合
        agreementRebateGoodsGroupList: [
          {
            // 商品组分类：1-普通商品组 2-核心商品组
            groupCategory: '',
            groupName: '',
            // 1-全商品主任务 2-子商品组
            groupType: 1,
            // 协议返利商品组对应的商品集合
            agreementRebateGoodsList: [],
            // 协议返利范围集合
            agreementRebateScopeList: [
              {
                // 控销类型：1-无 2-黑名单 3-白名单
                controlSaleType: 1,
                // 协议返利控销条件：1-区域 2-客户类型
                agreementRebateControlList: [],
                // 协议返利控销区域
                addAgreementRebateControlArea: { jsonContent: '' },
                // 协议返利任务量
                agreementRebateTaskStageList: [
                  {
                    // 任务量
                    taskNum: '',
                    // 任务量单位：1-元 2-盒
                    taskUnit: 1,
                    // 超任务量汇总返
                    overSumBack: '',
                    // 超任务量汇总返单位：1-元 2-%
                    overSumBackUnit: 1,
                    // 协议返利阶梯集合
                    agreementRebateStageList: [
                      {
                        // 满
                        full: '',
                        // 满单位：1-元 2-盒
                        fullUnit: 1,
                        // 返
                        back: '',
                        // 返单位：1-元 2-%
                        backUnit: 1
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      }
      if (e == 2) {
        newPeriod.type = 2
        newPeriod.startTime = this.startTime
      }

      this.agreementRebateTerms.agreementRebateTimeSegmentList = [newPeriod]
    },
    endTimeChange(e, periodIndex) {
      this.$log(e, periodIndex)
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      if (agreementRebateTimeSegmentList && agreementRebateTimeSegmentList.length > 0) {
        // 最后一个
        if (periodIndex != agreementRebateTimeSegmentList.length - 1) {

          let nextPeriod = agreementRebateTimeSegmentList[periodIndex + 1]

          let nextDate = new Date(e).getTime() + 24*60*60*1000; //后一天
          nextPeriod.startTime = formatDate(nextDate, 'yyyy-MM-dd')
        }
      }
    },
    // 删除时段
    deletePeriodClick(periodIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      if (agreementRebateTimeSegmentList && agreementRebateTimeSegmentList.length > 0) {
        // 最后一个
        if (periodIndex != agreementRebateTimeSegmentList.length - 1) {
          let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
          let nextPeriod = agreementRebateTimeSegmentList[periodIndex + 1]
          nextPeriod.startTime = currentPeriod.startTime
        }
      }

      if (this.agreementRebateTerms.goodsRebateRuleType == 2 && periodIndex <= this.classifyCurrentIndex) {
        this.classifyCurrentIndex = this.classifyCurrentIndex - 1
      }

      agreementRebateTimeSegmentList.splice(periodIndex, 1)
    },
    // 新增子时段配置
    addPeriodClick() {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let newPeriod = {
        // 时段类型：1-全时段 2-子时段
        type: 2,
        // 时段开始时间
        startTime: '',
        // 时段结束时间
        endTime: '',
        // 规模返利
        scaleRebateFlag: false,
        // 基础服务奖励
        basicServiceRewardFlag: false,
        // 项目服务奖励
        projectServiceRewardFlag: false,
        // 规模返利阶梯集合
        agreementScaleRebateList: [],
        // 基础服务奖励阶梯集合
        agreementRebateBasicServiceRewardList: [],
        // 项目服务奖励阶梯集合
        agreementRebateProjectServiceRewardList: [],
        // 协议返利商品组集合
        agreementRebateGoodsGroupList: [
          {
            // 商品组分类：1-普通商品组 2-核心商品组
            groupCategory: '',
            groupName: '',
            // 1-全商品主任务 2-子商品组
            groupType: 1,
            // 协议返利商品组对应的商品集合
            agreementRebateGoodsList: [],
            // 协议返利范围集合
            agreementRebateScopeList: [
              {
                // 控销类型：1-无 2-黑名单 3-白名单
                controlSaleType: 1,
                // 协议返利控销条件：1-区域 2-客户类型
                agreementRebateControlList: [],
                // 协议返利控销区域
                addAgreementRebateControlArea: { jsonContent: '' },
                // 协议返利控销客户类型
                agreementRebateControlCustomerType: [],
                // 协议返利任务量
                agreementRebateTaskStageList: [
                  {
                    // 任务量
                    taskNum: '',
                    // 任务量单位：1-元 2-盒
                    taskUnit: 1,
                    // 超任务量汇总返
                    overSumBack: '',
                    // 超任务量汇总返单位：1-元 2-%
                    overSumBackUnit: 1,
                    // 协议返利阶梯集合
                    agreementRebateStageList: [
                      {
                        // 满
                        full: '',
                        // 满单位：1-元 2-盒
                        fullUnit: 1,
                        // 返
                        back: '',
                        // 返单位：1-元 2-%
                        backUnit: 1
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      }

      // 获取上一个时段时间
      if (agreementRebateTimeSegmentList.length > 0) {

        if (this.tabsActiveName == 3 && agreementRebateTimeSegmentList.length == 1) {// 全品设置 全时段+多时段组合配置
          newPeriod.startTime = this.startTime

        } else {
          let prePeriod = agreementRebateTimeSegmentList[agreementRebateTimeSegmentList.length - 1]
          let preEndTime = prePeriod.endTime
          if (!preEndTime) {
            this.$common.warn('请选择子时段时间')
            return
          }

          let nextDate = new Date(preEndTime).getTime() + 24*60*60*1000; //后一天
          newPeriod.startTime = formatDate(nextDate, 'yyyy-MM-dd')
        }
      }

      this.agreementRebateTerms.agreementRebateTimeSegmentList = [...agreementRebateTimeSegmentList, newPeriod]
      if (this.agreementRebateTerms.goodsRebateRuleType == 2) {
        this.classifyCurrentIndex = this.agreementRebateTerms.agreementRebateTimeSegmentList.length - 1
      }
    },
    // 新增商品组
    addGoodsGroupClick() {
      // 当前时段
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[this.classifyCurrentIndex]

      let newGroup = {
        // 商品组分类：1-普通商品组 2-核心商品组
        groupCategory: 1,
        groupName: '',
        // 1-全商品主任务 2-子商品组
        groupType: 2,
        // 协议返利商品组对应的商品集合
        agreementRebateGoodsList: [],
        // 协议返利范围集合
        agreementRebateScopeList: [
          {
            // 控销类型：1-无 2-黑名单 3-白名单
            controlSaleType: 1,
            // 协议返利控销条件：1-区域 2-客户类型
            agreementRebateControlList: [],
            // 协议返利控销区域
            addAgreementRebateControlArea: { jsonContent: '' },
            // 协议返利控销客户类型
            agreementRebateControlCustomerType: [],
            // 协议返利任务量
            agreementRebateTaskStageList: [
              {
                // 任务量
                taskNum: '',
                // 任务量单位：1-元 2-盒
                taskUnit: 1,
                // 超任务量汇总返
                overSumBack: '',
                // 超任务量汇总返单位：1-元 2-%
                overSumBackUnit: 1,
                // 协议返利阶梯集合
                agreementRebateStageList: [
                  {
                    // 满
                    full: '',
                    // 满单位：1-元 2-盒
                    fullUnit: 1,
                    // 返
                    back: '',
                    // 返单位：1-元 2-%
                    backUnit: 1
                  }
                ]
              }
            ]
          }
        ]
      }
      currentPeriod.agreementRebateGoodsGroupList.push(newGroup)
    },
    // 新增返利范围设置
    addScopeClick(periodIndex, groupIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]

      let newScope = {
        // 控销类型：1-无 2-黑名单 3-白名单
        controlSaleType: 1,
        // 协议返利控销条件：1-区域 2-客户类型
        agreementRebateControlList: [],
        // 协议返利控销区域
        addAgreementRebateControlArea: { jsonContent: '' },
        // 协议返利控销客户类型
        agreementRebateControlCustomerType: [],
        // 协议返利任务量
        agreementRebateTaskStageList: [
          {
            // 任务量
            taskNum: '',
            // 任务量单位：1-元 2-盒
            taskUnit: 1,
            // 超任务量汇总返
            overSumBack: '',
            // 超任务量汇总返单位：1-元 2-%
            overSumBackUnit: 1,
            // 协议返利阶梯集合
            agreementRebateStageList: [
              {
                // 满
                full: '',
                // 满单位：1-元 2-盒
                fullUnit: 1,
                // 返
                back: '',
                // 返单位：1-元 2-%
                backUnit: 1
              }
            ]
          }
        ]
      }
      currentGroup.agreementRebateScopeList.push(newScope)
    },
    // 新增任务量返利
    addTaskClick(periodIndex, groupIndex, multiregionIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]

      let newTask = {
        // 任务量
        taskNum: '',
        // 任务量单位：1-元 2-盒
        taskUnit: 1,
        // 超任务量汇总返
        overSumBack: '',
        // 超任务量汇总返单位：1-元 2-%
        overSumBackUnit: 1,
        // 协议返利阶梯集合
        agreementRebateStageList: [
          {
            // 满
            full: '',
            // 满单位：1-元 2-盒
            fullUnit: 1,
            // 返
            back: '',
            // 返单位：1-元 2-%
            backUnit: 1
          }
        ]
      }

      // 前一个任务量返利
      if (currentScope.agreementRebateTaskStageList.length > 0) {
        let preTask = currentScope.agreementRebateTaskStageList[currentScope.agreementRebateTaskStageList.length - 1]
        if (this.agreementRebateTerms.taskFlag == 1 && !preTask.taskNum) {
          this.$common.warn('请先输入任务量')
          return
        }
        if (!preTask.overSumBack) {
          this.$common.warn('请先输入超任务量汇总返')
          return
        }

        newTask.taskUnit = preTask.taskUnit
        newTask.overSumBackUnit = preTask.overSumBackUnit
      }

      currentScope.agreementRebateTaskStageList.push(newTask)
    },
    // 新增梯度
    addGradientsClick(periodIndex, groupIndex, multiregionIndex, multitaskIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let currentTask = currentScope.agreementRebateTaskStageList[multitaskIndex]

      if (currentTask.agreementRebateStageList.length == 6) {
        this.$common.warn('最多6个阶梯')
        return
      }

      let newGradients = {
        // 满
        full: '',
        // 满单位：1-元 2-盒
        fullUnit: 1,
        // 返
        back: '',
        // 返单位：1-元 2-%
        backUnit: 1
      }

      // 前一个梯度
      if (currentTask.agreementRebateStageList.length > 0) {
        let preGradients = currentTask.agreementRebateStageList[currentTask.agreementRebateStageList.length - 1]
        if (!preGradients.full) {
          this.$common.warn('请先输入返利规则')
          return
        }
        if (!preGradients.back) {
          this.$common.warn('请先输入返')
          return
        }
        newGradients.fullUnit = preGradients.fullUnit
        newGradients.backUnit = preGradients.backUnit
      }

      currentTask.agreementRebateStageList.push(newGradients)
    },
    // 梯度单位 改变
    fullUnitChange(type, periodIndex, groupIndex, multiregionIndex, multitaskIndex, nameType) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let currentTask = currentScope.agreementRebateTaskStageList[multitaskIndex]
      let agreementRebateStageList = currentTask.agreementRebateStageList

      agreementRebateStageList.forEach((item) => {
        if (nameType == 1) {
          item.fullUnit = type
        }
        if (nameType == 2) {
          item.backUnit = type
        }

      })

    },
    // 删除梯度
    deleteGradientsClick(periodIndex, groupIndex, multiregionIndex, multitaskIndex, multipleGradientsIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let currentTask = currentScope.agreementRebateTaskStageList[multitaskIndex]
      let agreementRebateStageList = currentTask.agreementRebateStageList

      agreementRebateStageList.splice(multipleGradientsIndex, 1)

    },
    // 任务量返利 单位改变
    taskUnitChange(type, periodIndex, groupIndex, multiregionIndex, nameType) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let multitaskList = currentScope.agreementRebateTaskStageList
      multitaskList.forEach((item) => {
        if (nameType == 1) {
          item.taskUnit = type
        }
        if (nameType == 2) {
          item.overSumBackUnit = type
        }

      })
    },
    // 删除任务
    deleteTaskClick(periodIndex, groupIndex, multiregionIndex, multitaskIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let multitaskList = currentScope.agreementRebateTaskStageList

      if (multitaskList.length == 1) {
        this.$common.warn('第一个任务量不能删除')
        return
      }

      multitaskList.splice(multitaskIndex, 1)
    },
    // 删除范围
    deleteRegionClick(periodIndex, groupIndex, multiregionIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let multiregionList = currentGroup.agreementRebateScopeList

      if (multiregionList.length == 1) {
        this.$common.warn('第一个返利范围不能删除')
        return
      }

      multiregionList.splice(multiregionIndex, 1)
    },
    // ka协议时才可能存在（规模返利、基础服务奖励、项目服务奖励)
    kaRebateFlagChange(flag, kaRebateType) {
      this.$log('kaRebateFlagChange:', flag)
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[this.classifyCurrentIndex]

      if (kaRebateType == 1) {
        if (flag) {
          let newGradients = {
            // 目标达成率
            targetReachRatio: '',
            // 目标达成率单位
            reachRatioUnit: 1,
            // 目标返利率
            targetRebateRatio: '',
            // 目标返利率单位
            rebateRatioUnit: 1
          }
          currentPeriod.agreementScaleRebateList = [newGradients]
        } else {
          currentPeriod.agreementScaleRebateList = []
        }
      } else if (kaRebateType == 2) {
        if (flag) {
          let newGradients = {
            // 增长率
            increaseRatio: '',
            // 增长率单位
            increaseRatioUnit: 1,
            // 返利
            rebateNum: '',
            // 返利单位
            rebateNumUnit: 1
          }
          currentPeriod.agreementRebateBasicServiceRewardList = [newGradients]
        } else {
          currentPeriod.agreementRebateBasicServiceRewardList = []
        }
      } else if (kaRebateType == 3) {
        if (flag) {
          let newGradients = {
            // 覆盖率
            coverRatio: '',
            // 覆盖率单位
            coverRatioUnit: 1,
            // 其它
            other: '',
            // 其它单位
            otherUnit: 1,
            // 返利
            rebateNum: '',
            // 返利单位
            rebateNumUnit: 1
          }
          currentPeriod.agreementRebateProjectServiceRewardList = [newGradients]
        } else {
          currentPeriod.agreementRebateProjectServiceRewardList = []
        }
      }
    },
    // 规模返利 kaRebateType:  1-规模返利 2-基础服务奖励 3-项目服务奖励
    addKaScaleRebateGradientsClick(kaRebateType, periodIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]

      if (kaRebateType == 1) {
        let newGradients = {
          // 目标达成率
          targetReachRatio: '',
          // 目标达成率单位
          reachRatioUnit: 1,
          // 目标返利率
          targetRebateRatio: '',
          // 目标返利率单位
          rebateRatioUnit: 1
        }
        // 前一个梯度
        if (currentPeriod.agreementScaleRebateList.length > 0) {
          let preGradients = currentPeriod.agreementScaleRebateList[currentPeriod.agreementScaleRebateList.length - 1]
          newGradients.reachRatioUnit = preGradients.reachRatioUnit
          newGradients.rebateRatioUnit = preGradients.rebateRatioUnit
        }

        currentPeriod.agreementScaleRebateList.push(newGradients)
      } else if (kaRebateType == 2) {
        let newGradients = {
          // 增长率
          increaseRatio: '',
          // 增长率单位
          increaseRatioUnit: 1,
          // 返利
          rebateNum: '',
          // 返利单位
          rebateNumUnit: 1
        }
        // 前一个梯度
        if (currentPeriod.agreementRebateBasicServiceRewardList.length > 0) {
          let preGradients = currentPeriod.agreementRebateBasicServiceRewardList[currentPeriod.agreementRebateBasicServiceRewardList.length - 1]
          newGradients.increaseRatioUnit = preGradients.increaseRatioUnit
          newGradients.rebateNumUnit = preGradients.rebateNumUnit
        }
        currentPeriod.agreementRebateBasicServiceRewardList.push(newGradients)
      } else if (kaRebateType == 3) {
        let newGradients = {
          // 覆盖率
          coverRatio: '',
          // 覆盖率单位
          coverRatioUnit: 1,
          // 其它
          other: '',
          // 其它单位
          otherUnit: 1,
          // 返利
          rebateNum: '',
          // 返利单位
          rebateNumUnit: 1
        }
        // 前一个梯度
        if (currentPeriod.agreementRebateProjectServiceRewardList.length > 0) {
          let preGradients = currentPeriod.agreementRebateProjectServiceRewardList[currentPeriod.agreementRebateProjectServiceRewardList.length - 1]
          newGradients.coverRatioUnit = preGradients.coverRatioUnit
          newGradients.otherUnit = preGradients.otherUnit
          newGradients.rebateNumUnit = preGradients.rebateNumUnit
        }
        currentPeriod.agreementRebateProjectServiceRewardList.push(newGradients)
      }
      
    },
    deleteKaScaleRebateGradientsClick(kaRebateType, periodIndex, kaScaleGradientsIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      if (kaRebateType == 1) {
        let agreementScaleRebateList = currentPeriod.agreementScaleRebateList
        agreementScaleRebateList.splice(kaScaleGradientsIndex, 1)
      } else if (kaRebateType == 2) {
        let agreementRebateBasicServiceRewardList = currentPeriod.agreementRebateBasicServiceRewardList
        agreementRebateBasicServiceRewardList.splice(kaScaleGradientsIndex, 1)
      } else if (kaRebateType == 3) {
        let agreementRebateProjectServiceRewardList = currentPeriod.agreementRebateProjectServiceRewardList
        agreementRebateProjectServiceRewardList.splice(kaScaleGradientsIndex, 1)
      }
      
    },
    // ka梯度单位 改变
    kaGradientsUnitChange(type, periodIndex, kaRebateType, nameType) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]

      if (kaRebateType == 2) {
        let agreementRebateBasicServiceRewardList = currentPeriod.agreementRebateBasicServiceRewardList
        agreementRebateBasicServiceRewardList.forEach((item) => {
          if (nameType == 1) {
            item.increaseRatioUnit = type
          }
          if (nameType == 2) {
            item.rebateNumUnit = type
          }

        })
      } else if (kaRebateType == 3) {
        let agreementRebateProjectServiceRewardList = currentPeriod.agreementRebateProjectServiceRewardList
        agreementRebateProjectServiceRewardList.forEach((item) => {
          if (nameType == 1) {
            item.coverRatioUnit = type
          }
          if (nameType == 2) {
            item.otherUnit = type
          }
          if (nameType == 3) {
            item.rebateNumUnit = type
          }

        })
      } 

    },
    // 控销类型切换
    controlSaleTypeChange(type, periodIndex, groupIndex, multiregionIndex) {
      if (type == 1) {
        let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

        let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
        let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
        let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]

        currentScope.agreementRebateControlList = []
        currentScope.addAgreementRebateControlArea = {
          jsonContent: ''
        }
        currentScope.agreementRebateControlCustomerType = []
      }
    },
    // 选择区域
    selectClick(periodIndex, groupIndex, multiregionIndex) {

      this.selectPeriodIndex = periodIndex
      this.selectGroupIndex = groupIndex
      this.selectMultiregionIndex = multiregionIndex

      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[multiregionIndex]
      let addAgreementRebateControlArea = currentScope.addAgreementRebateControlArea

      let jsonContent = addAgreementRebateControlArea.jsonContent
      this.selectedData = []
      if (jsonContent) {
        let nodes = JSON.parse(jsonContent)
        let check = []
        this.getSelectId(nodes, check)
        this.selectedData = check
      }

      this.showCityDialog = true
    },
    citySelectConfirm(data) {

      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let currentPeriod = agreementRebateTimeSegmentList[this.selectPeriodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[this.selectGroupIndex]
      let currentScope = currentGroup.agreementRebateScopeList[this.selectMultiregionIndex]
      let addAgreementRebateControlArea = currentScope.addAgreementRebateControlArea

      this.showCityDialog = false
      let jsonContent = JSON.stringify(data.nodes)
      addAgreementRebateControlArea.jsonContent = jsonContent
      addAgreementRebateControlArea.selectedDesc = data.desc
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length){
          this.getSelectId(item.children, selectArr)
        } else {
          selectArr.push(item.code)
        }
      });
    },
    // 添加商品
    addGoodsClick(periodIndex, groupIndex) {
      this.addGoodsPeriodIndex = periodIndex
      this.addGoodsGroupIndex = groupIndex

      this.addGoodsDialog = true
      this.getGoodsList()
    },
    // 删除表格勾选
    deleteSelectionChange(val, periodIndex, groupIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      currentGroup.deleteGoodsList = val

    },
    // 删除商品组
    deleteGroupGoodsClick(periodIndex, groupIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      currentPeriod.agreementRebateGoodsGroupList.splice(groupIndex, 1)

    },
    // 删除商品
    deleteGoodsClick(periodIndex, groupIndex) {
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
      let currentPeriod = agreementRebateTimeSegmentList[periodIndex]
      let currentGroup = currentPeriod.agreementRebateGoodsGroupList[groupIndex]
      let groupList = currentGroup.agreementRebateGoodsList

      let deleteGoodsList = currentGroup.deleteGoodsList || []
      if (!deleteGoodsList || deleteGoodsList.length == 0) {
        this.$common.warn('请选择需要删除的商品')
        return
      }

      for (let i = 0; i < groupList.length; i++) {
        let goodsItem = groupList[i]

        let hasIndex = deleteGoodsList.findIndex(obj => {
          return obj.specificationGoodsId == goodsItem.specificationGoodsId;
        });
        if (hasIndex != -1) {
          groupList.splice(i, 1)
          i--
        }
      }
      deleteGoodsList = []
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getGoodsList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
    },
    // 获取商品列表
    async getGoodsList() {

      let goodsQuery = this.goodsQuery
      this.loading2 = false
      let data = await queryGoodsPageList(
        goodsQuery.page,
        goodsQuery.limit,
        goodsQuery.goodsId,
        goodsQuery.goodsName,
        this.eid,
        this.firstType
      );
      this.loading2 = false
      if (data) {
        let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
        let currentPeriod = agreementRebateTimeSegmentList[this.addGoodsPeriodIndex]
        let currentGroup = currentPeriod.agreementRebateGoodsGroupList[this.addGoodsGroupIndex]
        let groupList = currentGroup.agreementRebateGoodsList

        this.goodsList = data.records
        this.goodsTotal = data.total

        this.$nextTick(() => {
          this.goodsList.forEach(row => {
            let hasIndex = groupList.findIndex(obj => {
              return obj.specificationGoodsId == row.specificationGoodsId;
            });

            if (hasIndex != -1) {
              this.$refs.multipleTable.toggleRowSelectionMethod(row);
            }
          });
        })

      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;

    },
    // 添加商品 确认点击
    addGoodsConfirm() {
      if (this.multipleSelection.length > 0) {
        let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
        let currentPeriod = agreementRebateTimeSegmentList[this.addGoodsPeriodIndex]
        let currentGroup = currentPeriod.agreementRebateGoodsGroupList[this.addGoodsGroupIndex]
        let groupList = currentGroup.agreementRebateGoodsList

        // 排除其他商品组已经添加的商品
        let hasAddGoodsList = []
        for (let j = 0; j < currentPeriod.agreementRebateGoodsGroupList.length; j++) {
          let groupItem = currentPeriod.agreementRebateGoodsGroupList[j]
          if (j != this.addGoodsGroupIndex) {
            hasAddGoodsList.push(...groupItem.agreementRebateGoodsList)
          }
        }
        this.$log('hasAddGoodsList:',hasAddGoodsList)
        if (hasAddGoodsList.length > 0) {
          let hasAdd = false
          for (let i = 0; i < this.multipleSelection.length; i++) {
              let goodsItem = this.multipleSelection[i]

              let hasOtherIndex = hasAddGoodsList.findIndex(obj => {
                return obj.specificationGoodsId == goodsItem.specificationGoodsId;
              });
              if (hasOtherIndex != -1) {
                hasAdd = true
              }
          }
          if (hasAdd) {
            this.$common.warn('一个商品只能存在一个商品组')
            return
          }
        }

        for (let i = 0; i < this.multipleSelection.length; i++) {
          let goodsItem = this.multipleSelection[i]

          let hasIndex = groupList.findIndex(obj => {
            return obj.specificationGoodsId == goodsItem.specificationGoodsId;
          });
          if (hasIndex == -1) {
            groupList.push(goodsItem)
          }
        }
        this.addGoodsDialog = false
        this.$log('addGoodsConfirm')
      } else {
        this.$common.warn('请选择需要添加的商品')
      }
    },
    // 校验数据
    checkInputData() {
      if (this.agreementRebateTerms.reserveSupplyFlag == 1) return true
      let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList

      let hasAgreementOtherRebate = false
      let agreementOtherRebateList = this.agreementRebateTerms.agreementOtherRebateList
      if (agreementOtherRebateList && agreementOtherRebateList.length > 0) {
        for (let m = 0; m < agreementOtherRebateList.length; m++) {
          let other = this.agreementRebateTerms.agreementOtherRebateList[m]
          if (!other.rebateType || !other.amountType || !other.amount) {
            hasAgreementOtherRebate = true
          }
        }
      }
      if (hasAgreementOtherRebate) {
        this.$common.warn('请填写正确的非商品返利')
        return false
      }

      // 子时段 是否有结束日期
      let hasEndTime = false
      // 子时段 有结束日期 需大于结束日期
      let hasWrongTime = false
      // 子时段 结束日期是否为结束日期
      let hasCorrectEndTime = false
      // 子时段 商品组是否添加商品
      let hasGoodsList = false
      //  需要填写控销区域
      // eslint-disable-next-line no-unused-vars
      let hasControlSaleType = false
      // 控销区域
      let hasControlArea = false
      // 控销客户类型
      let hasControlCustomerType = false

      if (agreementRebateTimeSegmentList.length > 0) {
        for (let i = 0; i < agreementRebateTimeSegmentList.length; i++) {
          // 时段
          let period = agreementRebateTimeSegmentList[i]
          if (period.type == 2) {
            if (!period.endTime) {
              hasEndTime = true
            }
            let startDate = new Date(period.startTime)
            let endDate = new Date(period.endTime)
            if (startDate > endDate) {
              hasWrongTime = true
            }
            if (i == agreementRebateTimeSegmentList.length - 1) {
              if (period.endTime != this.endTime) {
                hasCorrectEndTime = true
              }
            }
          }
          for (let j = 0; j < period.agreementRebateGoodsGroupList.length; j++) {
            // 商品组
            let goodsGroup = period.agreementRebateGoodsGroupList[j]
            if (goodsGroup.groupType == 2) {
              if (goodsGroup.agreementRebateGoodsList.length == 0) {
                hasGoodsList = true
              }
            }
            for (let k = 0; k < goodsGroup.agreementRebateScopeList.length; k++) {
              // 返利范围
              let ScopeItem = goodsGroup.agreementRebateScopeList[k]
              if (ScopeItem.controlSaleType != 1) {
                if (ScopeItem.agreementRebateControlList.length == 0) {
                  hasControlSaleType = true
                }
                if (ScopeItem.agreementRebateControlList.indexOf(1) != -1) {
                  if (!ScopeItem.addAgreementRebateControlArea.jsonContent) {
                    hasControlArea = true
                  }
                }
                if (ScopeItem.agreementRebateControlList.indexOf(2) != -1) {
                  if (ScopeItem.agreementRebateControlCustomerType.length == 0) {
                    hasControlCustomerType = true
                  }
                }

              }
            }
          }
        }

        if (hasEndTime) {
          this.$common.warn('请选择子时段结束日期')
          return false
        }
        if (hasWrongTime) {
          this.$common.warn('子时段结束日期必须大于开始时间')
          return false
        }
        if (hasCorrectEndTime) {
          this.$common.warn('子时段结束日期必须为协议结束时间')
          return false
        }
        if (hasGoodsList) {
          this.$common.warn('商品组需添加商品')
          return false
        }
        if (hasControlSaleType) {
          this.$common.warn('请选择控销条件')
          return false
        }
        if (hasControlArea) {
          this.$common.warn('请选择控销区域')
          return false
        }
        if (hasControlCustomerType) {
          this.$common.warn('请选择控销客户类型')
          return false
        }

      }

      return true

    },
    downLoadTemplate() {
      downloadByUrl(this.NO_10)
    },
    // 导入商品
    goImport(periodIndex, groupIndex) {
      this.addGoodsPeriodIndex = periodIndex
      this.addGoodsGroupIndex = groupIndex
      this.importDialog = true

      let extralData = {
        eid: this.eid,
        firstType: this.firstType
      }

      let info = {
        action: '/admin/pop/api/v1/agreementTerms/importAgreementGoods',
        extralData: extralData
      }
      this.info = info
    },
    onSuccess(data) {
      this.$log(data)
      if (data && data.length > 0) {
        this.$common.n_success('成功导入：' + data.length + '条')
        this.importDialog = false

        let agreementRebateTimeSegmentList = this.agreementRebateTerms.agreementRebateTimeSegmentList
        let currentPeriod = agreementRebateTimeSegmentList[this.addGoodsPeriodIndex]
        let currentGroup = currentPeriod.agreementRebateGoodsGroupList[this.addGoodsGroupIndex]
        let groupList = currentGroup.agreementRebateGoodsList

        if (groupList && groupList.length > 0) {
          data.forEach(item => {
            let hasIndex = groupList.findIndex(obj => {
              return obj.specificationGoodsId == item.specificationGoodsId;
            });
            this.$log(item)
            if (hasIndex != -1) { // 已经存在
            } else {
              groupList.push(this.$common.clone(item))
            }
          });
        } else {
          this.$log('data:',data)
          data.forEach(item => {
            groupList.push(this.$common.clone(item))
          })
        }

      } else {
        this.$common.warn('暂无匹配商品')
      }
    },
    // 校验输入数值
    onInput(value, limit) {
      return onInputLimit(value, limit)
    },
    // 校验输入范围
    checkInput(val, maxValue) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > maxValue) {
        val = val.substr(0, val.length - 1)
      }
      if (val < 1) {
        val = ''
      }
      return val
    },
    // 只能输入数字
    numberCheckInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
    }

  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color;
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
.steps-view{
  .el-step{
    .is-finish{
      .el-step__icon{
        background: #1790ff;
      }
    }
    .el-step__icon{
      background: #CCCCCC;
      border: none;
      .el-step__icon-inner{
        color: $white;
      }
    }
    .el-step__title{
      color: #333;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
