<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div>
        <el-form :disabled="operationType == 1 || running" :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="110px" class="demo-ruleForm">
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>基本信息
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="满赠活动名称" prop="name">
                <el-input class="show-word-limit" v-model="baseFormModel.name" maxlength="20" show-word-limit placeholder="请输入满赠活动名称"></el-input>
              </el-form-item>
              <el-form-item label="活动分类" prop="sponsorType">
                <el-select v-model="baseFormModel.sponsorType" placeholder="请选择">
                  <el-option v-for="item in sponsorTypeArray" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="生效时间" prop="validDate">
                <el-date-picker
                  :picker-options="pickerOptions"
                  v-model="baseFormModel.validDate"
                  type="datetimerange"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :default-time="['00:00:00', '23:59:59']">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="运营备注" prop="operatingRemark">
                <el-input v-model="baseFormModel.operatingRemark" placeholder="请输入运营备注" maxlength="20"></el-input>
              </el-form-item>
              <el-form-item label="">
                <yl-button v-if="operationType != 1" type="primary" @click="topSaveClick">保存</yl-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      
      <div>
        <el-form :model="useRuleFormModel" :rules="useRules" ref="useRuleForm" label-position="left" label-width="110px" class="demo-ruleForm">
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>类型
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="策略类型" prop="strategyType">
                <el-radio-group v-model="useRuleFormModel.strategyType" :disabled="operationType == 1 || running" @change="typeChange">
                  <el-radio v-for="item in strategyType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            <!-- 适用范围 -->
            <div class="header-bar header-renative">
              <div class="sign"></div>适用范围
            </div>
            <!-- 使用平台 -->
            <div class="content-box my-form-box">
              <div v-if="useRuleFormModel.strategyType == 1 || !useRuleFormModel.strategyType">
                <el-form-item label="使用平台" prop="platformSelected">
                  <el-checkbox-group v-model="useRuleFormModel.platformSelected" :disabled="running || operationType == 1">
                      <el-checkbox :label="1">B2B</el-checkbox>
                      <el-checkbox :label="2">销售助手</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="商家范围" prop="conditionSellerType">
                  <el-radio-group v-model="useRuleFormModel.conditionSellerType">
                    <el-radio :label="1" :disabled="operationType == 1 || running">全部商家</el-radio>
                    <el-radio :label="2" :disabled="operationType == 1 || running">指定商家</el-radio>
                    <yl-button v-if="useRuleFormModel.conditionSellerType == 2" type="text" @click="addProviderClick(1)">设置商家 {{ useRuleFormModel.strategySellerLimitCount ? `(${useRuleFormModel.strategySellerLimitCount})` : '' }}</yl-button>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="商品范围" prop="conditionGoodsType">
                  <el-radio-group v-model="useRuleFormModel.conditionGoodsType">
                    <el-radio :label="1" :disabled="running || operationType == 1">全部商品</el-radio>
                    <el-radio :label="2" :disabled="running || operationType == 1">指定平台SKU</el-radio>
                    <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionGoodsType == 2" type="text" @click="addGoodsClick">设置 {{ useRuleFormModel.strategyPlatformGoodsLimitCount ? `(${useRuleFormModel.strategyPlatformGoodsLimitCount})` : '' }}</yl-button>
                    <el-radio :label="3" :disabled="running || operationType == 1">指定店铺SKU</el-radio>
                    <yl-button v-if="useRuleFormModel.conditionGoodsType == 3" type="text" @click="addEnterpriseGoodsClick">设置 {{ useRuleFormModel.strategyEnterpriseGoodsLimitCount ? `(${useRuleFormModel.strategyEnterpriseGoodsLimitCount})` : '' }}</yl-button>
                  </el-radio-group>
                </el-form-item>
              </div>
              <el-form-item label="客户范围" prop="conditionBuyerType">
                <el-radio-group v-model="useRuleFormModel.conditionBuyerType">
                  <el-radio :label="1" :disabled="running || operationType == 1 || useRuleFormModel.strategyType == 3">全部客户</el-radio>
                  <el-radio :label="2" :disabled="running || operationType == 1">指定客户</el-radio>
                  <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionBuyerType == 2" type="text" @click="addClientClick">设置 {{ useRuleFormModel.strategyBuyerLimitCount ? `(${useRuleFormModel.strategyBuyerLimitCount})` : '' }}</yl-button>
                  <el-radio :label="3" :disabled="running || operationType == 1">指定客户范围</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 指定范围客户 -->
              <div class="section-view" v-if="useRuleFormModel.conditionBuyerType == 3">
                <el-form-item label="企业类型" prop="conditionEnterpriseType">
                  <el-radio-group v-model="useRuleFormModel.conditionEnterpriseType" :disabled="running || operationType == 1">
                    <el-radio :label="1">全部类型</el-radio>
                    <el-radio :label="2">指定类型</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="" v-if="useRuleFormModel.conditionEnterpriseType == 2" prop="conditionEnterpriseTypeValue">
                  <el-checkbox-group v-model="useRuleFormModel.conditionEnterpriseTypeValue" :disabled="running || operationType == 1">
                      <el-checkbox
                        v-for="item in enterpriseType"
                        v-show="item.value != 1 && item.value != 2"
                        :key="item.value"
                        :label="item.value"
                      >{{ item.label }}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="用户类型" prop="conditionUserType">
                  <el-radio-group v-model="useRuleFormModel.conditionUserType">
                    <el-radio :label="1" :disabled="running || operationType == 1 || useRuleFormModel.strategyType == 3">全部用户</el-radio>
                    <el-radio :label="2" :disabled="running || operationType == 1 || useRuleFormModel.strategyType == 3">普通用户</el-radio>
                    <el-radio :label="3" :disabled="running || operationType == 1">全部会员</el-radio>
                    <el-radio :label="4" :disabled="running || operationType == 1">部分会员</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="" v-if="useRuleFormModel.conditionUserType == 4" prop="conditionUserMemberType">
                  <el-checkbox-group v-model="useRuleFormModel.conditionUserMemberType" :disabled="running || operationType == 1">
                    <el-checkbox :label="1">指定会员方案</el-checkbox>
                    <yl-button class="mar-r-16" type="text" @click="addMemberDimensionClick">设置 {{ useRuleFormModel.strategyMemberLimitCount ? `(${useRuleFormModel.strategyMemberLimitCount})` : '' }}</yl-button>
                    <el-checkbox :label="2">指定会员推广方</el-checkbox>
                    <yl-button class="mar-l-16" type="text" @click="addProviderClick(2)">设置 {{ useRuleFormModel.strategyPromoterMemberLimitCount ? `(${useRuleFormModel.strategyPromoterMemberLimitCount})` : '' }}</yl-button>
                  </el-checkbox-group>
                </el-form-item>
              </div>
              <el-form-item label="其他">
                <el-checkbox-group v-model="useRuleFormModel.conditionOther" :disabled="running || operationType == 1">
                  <el-checkbox :label="1">新客适用</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
          </div>
          <!-- 生效条件&计算规则 -->
          <div class="font-size-large font-important-color mar-b-16">生效条件&计算规则</div>
          <!-- 基本规则设置 -->
          <div class="top-bar">
            <!-- 订单累计金额 -->
            <div class="content-box my-form-box" v-if="useRuleFormModel.strategyType == 1 || !useRuleFormModel.strategyType">
              <el-form-item label="限制订单状态" prop="orderAmountStatusType">
                <el-radio-group v-model="useRuleFormModel.orderAmountStatusType" :disabled="running || operationType == 1">
                  <el-radio :label="1">发货计算</el-radio>
                  <el-radio :label="2">下单计算</el-radio>
                </el-radio-group>
              </el-form-item>
              <div class="explain-view">如选择下单计算，订单取消不会退回赠品！请慎重！</div>
              <!-- 支付方式 -->
              <el-form-item label="限制支付方式" prop="orderAmountPayType">
                <el-radio-group v-model="useRuleFormModel.orderAmountPayType" :disabled="running || operationType == 1">
                  <el-radio :label="1">全部支付方式</el-radio>
                  <el-radio :label="2">部分支付方式</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.orderAmountPayType == 2" prop="orderAmountPaymentType">
                <el-checkbox-group v-model="useRuleFormModel.orderAmountPaymentType" :disabled="running || operationType == 1">
                    <el-checkbox :label="4">在线支付</el-checkbox>
                    <el-checkbox :label="1">线下支付</el-checkbox>
                    <el-checkbox :label="2">账期</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="阶梯匹配方式" prop="orderAmountLadderType">
                <el-radio-group v-model="useRuleFormModel.orderAmountLadderType" :disabled="running || operationType == 1">
                  <el-radio :label="3">按单匹配</el-radio>
                  <el-radio :label="1">按单累计匹配</el-radio>
                  <el-radio :label="2">活动结束整体匹配</el-radio>
                </el-radio-group>
              </el-form-item>
              <div class="explain-view">按单匹配：订单满足条件时，按当前订单金额从低到高匹配阶梯，执行匹配到的最高阶梯。<br>按单累计匹配：订单满足条件时，即时计算累计金额从低到高匹配阶梯，依次执行。<br>活动结束整体匹配：活动结束后计算订单累计金额，从高到低匹配阶梯，执行匹配到的最高阶梯。</div>
              <!-- 多阶梯 -->
              <div class="Gradients-item mar-t-10" v-for="(ladderItem, ladderIndex) in useRuleFormModel.strategyAmountLadderList" :key="ladderIndex">
                <div>
                  <div class="header-bar header-renative">
                  <div class="sign"></div>阶梯{{ ladderIndex + 1 }}<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeLadderClick(ladderIndex)"></yl-button></div>
                  <div class="multitask-form-item">
                    <div class="task-form-item flex-shrink-view mar-r-16 task-form-item-left">
                      <span class="font-title-color font-size-base label-height flex-shrink-view left-label">满金额</span>
                      <el-form-item class="mar-r-16" label-width="0">
                        <el-input v-model="ladderItem.amountLimit" placeholder="请输入" @keyup.native="ladderItem.amountLimit = onInput(ladderItem.amountLimit, 2)" :disabled="running || operationType == 1"></el-input>
                      </el-form-item>
                    </div>
                    <!-- 多赠品 -->
                    <div>
                      <div v-for="(strategyGiftItem, strategyGiftIndex) in ladderItem.strategyGiftList" :key="strategyGiftIndex">
                        <div class="multipleGradients-form-item">
                          <span class="font-title-color font-size-base label-height left-label">赠送</span>
                          <div class="font-title-color font-size-base label-height mar-r-16 gift-name">{{ strategyGiftItem.giftName }}</div>
                          <el-form-item class="task-form-item mar-r-16" label-width="0">
                            <el-input v-model="strategyGiftItem.count" placeholder="数量" @keyup.native="strategyGiftItem.count = onInput(strategyGiftItem.count, 0)" :disabled="running || operationType == 1"></el-input>
                          </el-form-item>
                          <!-- 新增梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="selectGiftClick(1, ladderIndex, strategyGiftIndex)" :disabled="running || operationType == 1">选择</yl-button>
                          </el-form-item>
                          <!-- 删除梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="removeGiftClick(ladderIndex, strategyGiftIndex)" :disabled="running || operationType == 1">删除</yl-button>
                          </el-form-item>
                        </div>
                      </div>
                      <div class="mar-b-8 add-btn">
                        <yl-button icon="el-icon-circle-plus-outline" type="text" @click="addStrategyGift(ladderIndex)" :disabled="running || operationType == 1">添加</yl-button>
                      </div>
                    </div>
                    <!-- 多赠品 -->
                  </div>
                </div>
              </div>
              <!-- 多阶梯 -->
              <!-- 新增任务返利 -->
              <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="text" @click="addLadder" :disabled="running || operationType == 1">增加阶梯</yl-button></div>
            </div>
            <!-- 时间周期 -->
            <div class="content-box my-form-box" v-if="useRuleFormModel.strategyType == 3">
              <el-form-item label="频率" prop="cycleRate">
                <el-radio-group v-model="useRuleFormModel.cycleRate" :disabled="running || operationType == 1" @change="cycleRateTypeChange">
                  <el-radio :label="1">按周</el-radio>
                  <el-radio :label="2">按月</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 多阶梯 -->
              <div class="Gradients-item mar-t-10" v-for="(ladderItem, ladderIndex) in useRuleFormModel.strategyCycleLadderList" :key="ladderIndex">
                <div>
                  <div class="header-bar header-renative">
                  <div class="sign"></div>阶梯{{ ladderIndex + 1 }}<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" @click="cycleRateRemoveLadderClick(ladderIndex)" :disabled="running || operationType == 1"></yl-button></div>
                  <!-- 按月 -->
                  <div class="cycle-rate-view">
                    <el-checkbox-group v-model="ladderItem.conditionValue" :disabled="running || operationType == 1">
                      <el-checkbox
                        class="option-class"
                        v-for="item in cycleRateArray"
                        :key="item.value"
                        :label="item.value"
                      >{{ item.label }}</el-checkbox>
                    </el-checkbox-group>
                  </div>
                  <div class="multitask-form-item">
                    <!-- 多赠品 -->
                    <div>
                      <div v-for="(strategyGiftItem, strategyGiftIndex) in ladderItem.strategyGiftList" :key="strategyGiftIndex">
                        <div class="multipleGradients-form-item">
                          <span class="font-title-color font-size-base label-height left-label">赠送</span>
                          <div class="font-title-color font-size-base label-height mar-r-16 gift-name">{{ strategyGiftItem.giftName }}</div>
                          <el-form-item class="task-form-item mar-r-16" label-width="0">
                            <el-input v-model="strategyGiftItem.count" placeholder="数量" @keyup.native="strategyGiftItem.count = onInput(strategyGiftItem.count, 0)" :disabled="running || operationType == 1"></el-input>
                          </el-form-item>
                          <!-- 新增梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="selectGiftClick(3, ladderIndex, strategyGiftIndex)" :disabled="running || operationType == 1">选择</yl-button>
                          </el-form-item>
                          <!-- 删除梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="cycleRateRemoveGiftClick(ladderIndex, strategyGiftIndex)" :disabled="running || operationType == 1">删除</yl-button>
                          </el-form-item>
                        </div>
                      </div>
                      <div class="mar-b-8 add-btn">
                        <yl-button icon="el-icon-circle-plus-outline" type="text" @click="cycleRateAddStrategyGift(ladderIndex)" :disabled="running || operationType == 1">添加</yl-button>
                      </div>
                    </div>
                    <!-- 多赠品 -->
                  </div>
                  <div class="multitask-form-item">
                    <div class="multipleGradients-form-item">
                      <span class="font-title-color font-size-base label-height flex-shrink-view left-label">共执行</span>
                      <el-form-item class="task-form-item mar-r-16" label-width="0">
                        <el-input v-model="ladderItem.times" placeholder="请输入" @keyup.native="ladderItem.times = onInput(ladderItem.times, 0)" :disabled="running || operationType == 1"></el-input>
                      </el-form-item>
                      <span class="font-title-color font-size-base label-height mar-l-10">次</span>
                      <span class="font-title-color font-size-base label-height mar-l-16">（0为不限制）</span>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 多阶梯 -->
              <!-- 新增任务返利 -->
              <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="text" @click="cycleRateAddLadder" :disabled="running || operationType == 1">增加阶梯</yl-button></div>
            </div>
            <!-- 购买会员 -->
            <div class="content-box my-form-box" v-if="useRuleFormModel.strategyType == 4">
              <!-- <span class="buy-member-item">*</span> -->
              <el-form-item label="购买会员方案" class="buy-member-item">
                <yl-button type="text" @click="addMemberClick">设置 {{ useRuleFormModel.strategyStageMemberEffectCount ? `(${useRuleFormModel.strategyStageMemberEffectCount})` : '' }}</yl-button>
              </el-form-item>
              <!-- 购买会员 没有阶梯 -->
              <div class="Gradients-item mar-t-10">
                <div>
                  <div class="multitask-form-item">
                    <!-- 多赠品 -->
                    <div>
                      <div v-for="(strategyGiftItem, strategyGiftIndex) in useRuleFormModel.strategyGiftList" :key="strategyGiftIndex">
                        <div class="multipleGradients-form-item">
                          <span class="font-title-color font-size-base label-height left-label">购买时赠送</span>
                          <div class="font-title-color font-size-base label-height mar-r-16 gift-name">{{ strategyGiftItem.giftName }}</div>
                          <el-form-item class="task-form-item mar-r-16" label-width="0">
                            <el-input v-model="strategyGiftItem.count" placeholder="数量" @keyup.native="strategyGiftItem.count = onInput(strategyGiftItem.count, 0)" :disabled="running || operationType == 1"></el-input>
                          </el-form-item>
                          <!-- 新增梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="selectGiftClick(4, 0, strategyGiftIndex)" :disabled="running || operationType == 1">选择</yl-button>
                          </el-form-item>
                          <!-- 删除梯度 -->
                          <el-form-item class="mar-r-16" label-width="0">
                            <yl-button type="text" @click="buyMemberRemoveGiftClick(strategyGiftIndex)" :disabled="running || operationType == 1">删除</yl-button>
                          </el-form-item>
                        </div>
                      </div>
                      <div class="mar-b-8 add-btn">
                        <yl-button icon="el-icon-circle-plus-outline" type="text" @click="buyMemberAddStrategyGift" :disabled="running || operationType == 1">添加</yl-button>
                      </div>
                    </div>
                    <!-- 多赠品 -->
                  </div>
                </div>
              </div>
              <div class="multitask-form-item mar-t-16">
                <div class="multipleGradients-form-item">
                  <el-form-item label-width="0" class="mar-r-16">
                    <el-checkbox v-model="useRuleFormModel.memberRepeat" :disabled="running || operationType == 1">每月固定日期重复执行</el-checkbox>
                  </el-form-item>
                  <span class="font-title-color font-size-base label-height flex-shrink-view left-label">共执行</span>
                  <el-form-item class="task-form-item mar-r-16" label-width="0">
                    <el-input v-model="useRuleFormModel.memberTimes" placeholder="请输入" @keyup.native="useRuleFormModel.memberTimes = onInput(useRuleFormModel.memberTimes, 0)" :disabled="running || operationType == 1"></el-input>
                  </el-form-item>
                  <span class="font-title-color font-size-base label-height mar-l-10">次</span>
                  <span class="font-title-color font-size-base label-height mar-l-16">（0为不限制，从第二个月开始，购买时赠送不包含在重复执行次数中）</span>
                </div>
              </div>
              <!-- 按月 -->
              <div>
                <el-checkbox-group v-model="useRuleFormModel.memberRepeatDay" :disabled="running || operationType == 1">
                  <el-checkbox
                    class="option-class"
                    v-for="item in monthArray"
                    :key="item.value"
                    :label="item.value"
                  >{{ item.label }}</el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </div>
        </el-form>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1 && !running" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 选择商家 -->
    <add-provider-dialog v-if="addProviderVisible" ref="addProviderRef" @selectNumChange="strategySellerSelectNum"></add-provider-dialog>
    <!-- 指定平台SKU -->
    <add-platform-goods-dialog v-if="addPlatformGoodsVisible" ref="addPlatformGoodsRef" @selectNumChange="strategyPlatformGoodsSelectNum"></add-platform-goods-dialog>
    <!-- 指定店铺SKU -->
    <add-enterprise-goods-dialog v-if="addEnterpriseGoodsVisible" ref="addEnterpriseGoodsRef" @selectNumChange="strategyEnterpriseGoodsSelectNum"></add-enterprise-goods-dialog>
    <!-- 指定客户 -->
    <add-client-dialog v-if="addClientVisible" ref="addClientRef" @selectNumChange="strategyBuyerSelectNum"></add-client-dialog>
    <!-- 指定会员方案-会员维度 -->
    <add-member-dimension-dialog v-if="addMemberDimensionVisible" ref="addMemberDimensionRef" @selectNumChange="strategyMemberSelectNum"></add-member-dimension-dialog>
    <!-- 购买会员方案-时长维度 -->
    <add-member-dialog v-if="addMemberVisible" ref="addMemberRef" @selectNumChange="strategyStageMemberEffectSelectNum"></add-member-dialog>
    <!-- 选择赠品 -->
    <select-gift-dialog v-if="selectGiftVisible" ref="selectGiftRef" @save="confirmClick"></select-gift-dialog>
  </div>
</template>

<script>
import { strategyActivityQueryDetail, strategyActivitySave, strategyActivitySaveAll } from '@/subject/admin/api/b2b_api/marketing_manage'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { enterpriseType } from '@/subject/admin/utils/busi'
import { strategyType } from '@/subject/admin/busi/b2b/marketing_activity'

import AddProviderDialog from '../components/tactics_activity/add_provider_dialog'
import AddPlatformGoodsDialog from '../components/tactics_activity/add_platform_goods_dialog'
import AddEnterpriseGoodsDialog from '../components/tactics_activity/add_enterprise_goods_dialog'
import AddClientDialog from '../components/tactics_activity/add_client_dialog'
import AddMemberDimensionDialog from '../components/tactics_activity/add_member_dimension_dialog'
import AddMemberDialog from '../components/tactics_activity/add_member_dialog'
import SelectGiftDialog from '../components/tactics_activity/select_gift_dialog'

export default {
  components: {
    AddProviderDialog,
    AddPlatformGoodsDialog,
    AddEnterpriseGoodsDialog,
    AddClientDialog,
    AddMemberDimensionDialog,
    AddMemberDialog,
    SelectGiftDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    },
    // 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
    strategyType() {
      return strategyType()
    }
  },
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().getTime() - 86400000; 
        }
      },
      // 当前添加赠品的strategyType
      selectGiftStrategyType: 1,
      cycleRateArray: [],
      monthArray: [],
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 供应商
      addProviderVisible: false,
      // 指定平台SKU
      addPlatformGoodsVisible: false,
      addEnterpriseGoodsVisible: false,
      addClientVisible: false,
      addMemberDimensionVisible: false,
      addMemberVisible: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增 4-复制
      operationType: 2,
      id: '',
      loading: false,
      // 活动分类
      sponsorTypeArray: [
        {
          label: '平台活动',
          value: 1
        },
        {
          label: '商家活动',
          value: 2
        }
      ],
      // 选择赠品
      selectGiftVisible: false,
      // 当前选择的阶梯索引
      currentLadderIndex: 0,
      // 当前选择的赠品阶梯索引
      currentStrategyGiftIndex: 0,
      // 选择赠品操作type 1-选择 2-添加
      selectCouponType: 0,
      baseFormModel: {
        name: '',
        sponsorType: '',
        validDate: [],
        operatingRemark: ''
      },
      baseRules: {
        name: [{ required: true, message: '请输入满赠活动名称', trigger: 'blur' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        validDate: [{ required: true, message: '请选择生效时间', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        // 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
        strategyType: '',
        // 选择平台（1-B2B；2-销售助手)
        platformSelected: [],
        // 商家范围类型（1-全部商家；2-指定商家；）
        conditionSellerType: '',
        // 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
        conditionGoodsType: '',
        // 客户范围（1-全部客户；2-指定客户；3-指定客户范围）
        conditionBuyerType: '',
        // 指定企业类型(1:全部类型 2:指定类型)
        conditionEnterpriseType: '',
        // 指定企业类型值
        conditionEnterpriseTypeValue: [],
        // 	指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员；）
        conditionUserType: '',
        // 指定部分会员用户类型 1-指定会员方案 2-指定会员推广方
        conditionUserMemberType: [],
        // 新客适用
        conditionOther: [],
        // 生效条件&计算规则
        // 限制订单状态
        orderAmountStatusType: '',
        // 订单累计金额-限制支付方式(1-全部支付方式;2-部分支付方式)
        orderAmountPayType: '',
        // 订单累计金额-限制支付方式值(1-在线支付;2-线下支付;3-账期),逗号隔开
        orderAmountPaymentType: [],
        // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
        orderAmountLadderType: '',
        // 订单累计金额-优惠阶梯
        strategyAmountLadderList: [
          {
            // 满赠金额条件
            amountLimit: '',
            id: '',
            strategyGiftList: [
              {
                count: '',
                giftId: '',
                type: '',
                giftName: ''
              }
            ]
          }
        ],
        // 时间周期-频率(1-按周;2-按月)
        cycleRate: 1,
        // 时间周期-优惠阶梯
        strategyCycleLadderList: [
          {
            // (1-按周;2-按月)
            conditionValue: [],
            id: '',
            times: '',
            strategyGiftList: [
              {
                count: '',
                giftId: '',
                type: '',
                giftName: ''
              }
            ]
          }
        ],
        // 购买会员
        // 购买会员-是否每月固定日期重复执行(1-是；2-否；)
        memberRepeat: false,
        memberRepeatDay: [],
        memberTimes: '',
        strategyGiftList: [
          {
            count: '',
            giftId: '',
            type: '',
            giftName: ''
          }
        ],
        // 策略满赠商家数量
        strategySellerLimitCount: 0,
        strategyPlatformGoodsLimitCount: 0,
        strategyEnterpriseGoodsLimitCount: 0,
        strategyBuyerLimitCount: 0,
        strategyMemberLimitCount: 0,
        strategyPromoterMemberLimitCount: 0,
        // 策略满赠购买会员数量
        strategyStageMemberEffectCount: 0
      },
      useRules: {
        strategyType: [{ required: true, message: '请选择策略类型', trigger: 'change' }],
        platformSelected: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        conditionSellerType: [{ required: true, message: '请选择商家范围', trigger: 'change' }],
        conditionGoodsType: [{ required: true, message: '请选择商品范围', trigger: 'change' }],
        conditionBuyerType: [{ required: true, message: '请选择客户范围', trigger: 'change' }],
        conditionEnterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        conditionEnterpriseTypeValue: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        conditionUserType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        conditionUserMemberType: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        orderAmountStatusType: [{ required: true, message: '请选择限制订单状态', trigger: 'change' }],
        orderAmountPayType: [{ required: true, message: '请选择限制支付方式', trigger: 'change' }],
        orderAmountPaymentType: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        orderAmountLadderType: [{ required: true, message: '请选择阶梯匹配方式', trigger: 'change' }],
        cycleRate: [{ required: true, message: '请选时间周期频率', trigger: 'change' }]
      }
    };
  },
  mounted() {
    this.initWeekData()
    this.initMonthData()

    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    initWeekData() {
      let arr = ['日', '一', '二', '三', '四', '五', '六']
      let weekArr = []
      for (let i = 0; i < arr.length; i++) {
        let week = {
          label: '周' + arr[i],
          value: i + 1
        }
        weekArr.push(week)
      }
      this.cycleRateArray = weekArr 
    },
    initMonthData() {
      let monthArr = []
        for (let i = 1; i < 29; i++) {
          let month = {
            label: i + '号',
            value: i
          }
          monthArr.push(month)
        }
        this.monthArray = monthArr
    },
    async getDetail() {
      let data = await strategyActivityQueryDetail(this.id)
      if (data) {
        this.running = data.running

        let baseFormModel = {
          name: data.name,
          sponsorType: data.sponsorType,
          validDate: data.validDate,
          operatingRemark: data.operatingRemark
        }
        if (data.beginTime && data.beginTime > 0 && data.endTime && data.endTime > 0) {
          baseFormModel.validDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        let useRuleFormModel = {
          strategyType: data.strategyType || '',
          platformSelected: data.platformSelected,
          conditionSellerType: data.conditionSellerType || '',
          conditionGoodsType: data.conditionGoodsType || '',
          conditionBuyerType: data.conditionBuyerType || '',
          conditionEnterpriseType: data.conditionEnterpriseType || '',
          conditionEnterpriseTypeValue: data.conditionEnterpriseTypeValue,
          conditionUserType: data.conditionUserType || '',
          conditionUserMemberType: data.conditionUserMemberType,
          conditionOther: data.conditionOther,
          // 生效条件&计算规则
          orderAmountStatusType: data.orderAmountStatusType || '',
          orderAmountPayType: data.orderAmountPayType || '',
          orderAmountPaymentType: data.orderAmountPaymentType,
          orderAmountLadderType: data.orderAmountLadderType || '',
          // 时间周期
          cycleRate: data.cycleRate || '',
          // 购买会员
          memberRepeatDay: data.memberRepeatDay,
          memberTimes: data.memberTimes,

          strategySellerLimitCount: data.strategySellerLimitCount,
          strategyPlatformGoodsLimitCount: data.strategyPlatformGoodsLimitCount,
          strategyEnterpriseGoodsLimitCount: data.strategyEnterpriseGoodsLimitCount,
          strategyBuyerLimitCount: data.strategyBuyerLimitCount,
          strategyMemberLimitCount: data.strategyMemberLimitCount,
          strategyPromoterMemberLimitCount: data.strategyPromoterMemberLimitCount,
          strategyStageMemberEffectCount: data.strategyStageMemberEffectCount
        }
        // 每月固定日期重复执行
        useRuleFormModel.memberRepeat = data.memberRepeat == 1

        useRuleFormModel.strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
        useRuleFormModel.strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
        useRuleFormModel.strategyGiftList = this.useRuleFormModel.strategyGiftList

        if (data.strategyType == 1) {
          if (data.strategyAmountLadderList && data.strategyAmountLadderList.length > 0) {
            useRuleFormModel.strategyAmountLadderList = data.strategyAmountLadderList 
          }
        }
        if (data.strategyType == 3) {
          if (data.strategyCycleLadderList && data.strategyCycleLadderList.length > 0) {
            useRuleFormModel.strategyCycleLadderList = data.strategyCycleLadderList 
          }
          if (data.cycleRate && data.cycleRate == 2) {
            let monthArr = []
            for (let i = 1; i < 29; i++) {
              let month = {
                label: i + '号',
                value: i
              }
              monthArr.push(month)
            }
            this.cycleRateArray = monthArr
          }
        }
        if (data.strategyType == 4) {
          if (data.strategyGiftList && data.strategyGiftList.length > 0) {
            useRuleFormModel.strategyGiftList = data.strategyGiftList 
          }
        }
        
        this.useRuleFormModel = useRuleFormModel

        if (this.operationType == 4) {
          this.running = false
          this.baseFormModel.validDate = []
          this.baseFormModel.beginTime = ''
          this.baseFormModel.endTime = ''
        }
      }
    },
    cycleRateTypeChange(type) {
      let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
      strategyCycleLadderList.forEach(item => {
        item.conditionValue = []
      })
      if (type == 1) {
        this.initWeekData()
      } else {
        let monthArr = []
        for (let i = 1; i < 29; i++) {
          let month = {
            label: i + '号',
            value: i
          }
          monthArr.push(month)
        }
        this.cycleRateArray = monthArr
      }

    },
    // ---------------------订单累计金额---------------------
    // 添加阶梯
    addLadder() {
      let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
      if (strategyAmountLadderList.length == 3) {
        this.$common.warn('最多3个阶梯')
        return
      }
      let newLadder = {
        // 满赠金额条件
        amountLimit: '',
        id: '',
        strategyGiftList: [
          {
            count: '',
            giftId: '',
            type: '',
            giftName: ''
          }
        ]
      }
      strategyAmountLadderList.push(newLadder)
      
    },
    // 删除阶梯
    removeLadderClick(ladderIndex) {
      let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
      if (strategyAmountLadderList.length == 1) {
        this.$common.warn('最少有1个阶梯')
        return
      }
      strategyAmountLadderList.splice(ladderIndex, 1)
    },
    // 添加赠品阶梯
    addStrategyGift(ladderIndex) {
      this.selectGiftClick(1, ladderIndex)
    },
    // 删除赠品梯度
    removeGiftClick(ladderIndex, strategyGiftIndex) {
      let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
      let strategyGiftList = strategyAmountLadderList[ladderIndex].strategyGiftList
      if (strategyGiftList.length == 1) {
        this.$common.warn('每个阶梯中最少有1个赠品')
        return
      }
      strategyGiftList.splice(strategyGiftIndex, 1)

    },
    // ---------------------订单累计金额---------------------
    // ---------------------时间周期---------------------
    // 添加阶梯
    cycleRateAddLadder() {
      let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
      if (strategyCycleLadderList.length == 3) {
        this.$common.warn('最多3个阶梯')
        return
      }
      let newLadder = {
        // 满赠金额条件
        conditionValue: [],
        id: '',
        times: '',
        strategyGiftList: [
          {
            count: '',
            giftId: '',
            type: '',
            giftName: ''
          }
        ]
      }
      strategyCycleLadderList.push(newLadder)
      
    },
    // 删除阶梯
    cycleRateRemoveLadderClick(ladderIndex) {
      let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
      if (strategyCycleLadderList.length == 1) {
        this.$common.warn('最少有1个阶梯')
        return
      }
      strategyCycleLadderList.splice(ladderIndex, 1)
    },
    // 添加赠品阶梯
    cycleRateAddStrategyGift(ladderIndex) {
      this.selectGiftClick(3, ladderIndex)
    },
    // 删除赠品梯度
    cycleRateRemoveGiftClick(ladderIndex, strategyGiftIndex) {
      let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
      let strategyGiftList = strategyCycleLadderList[ladderIndex].strategyGiftList
      if (strategyGiftList.length == 1) {
        this.$common.warn('每个阶梯中最少有1个赠品')
        return
      }
      strategyGiftList.splice(strategyGiftIndex, 1)

    },
    // ---------------------时间周期---------------------
    // ---------------------购买会员---------------------
    buyMemberAddStrategyGift() {
      this.selectGiftClick(4, 0)
    },
    buyMemberRemoveGiftClick(strategyGiftIndex) {
      let strategyGiftList = this.useRuleFormModel.strategyGiftList
      if (strategyGiftList.length == 1) {
        this.$common.warn('每个阶梯中最少有1个赠品')
        return
      }
      strategyGiftList.splice(strategyGiftIndex, 1)
    },

    // 选择优惠券点击
    selectGiftClick(strategyType, ladderIndex, strategyGiftIndex) {
      this.$log(strategyType, ladderIndex, strategyGiftIndex, '111')
      this.selectGiftVisible = true
      this.selectGiftStrategyType = strategyType
      this.currentLadderIndex = ladderIndex
      
      if (strategyGiftIndex || strategyGiftIndex == 0) {
        this.currentStrategyGiftIndex = strategyGiftIndex
        this.selectCouponType = 1
      } else {
        this.selectCouponType = 2
      }
      this.$nextTick( () => {
        let strategyGiftList = []
        if (strategyType == 1) {
          let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
          strategyGiftList = strategyAmountLadderList[ladderIndex].strategyGiftList
        } else if (strategyType == 3) {
          let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
          strategyGiftList = strategyCycleLadderList[ladderIndex].strategyGiftList
        } else if (strategyType == 4) {
          strategyGiftList = this.useRuleFormModel.strategyGiftList
        }
        
        this.$refs.selectGiftRef.init(strategyGiftList)
      })
    },
    // 选择优惠券保存
    confirmClick(giftType, selectGiftList) {
      let strategyType = this.selectGiftStrategyType
      let strategyGiftList = []
      if (strategyType == 1) {
        let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
        strategyGiftList = strategyAmountLadderList[this.currentLadderIndex].strategyGiftList
      } else if (strategyType == 3) {
        let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
        strategyGiftList = strategyCycleLadderList[this.currentLadderIndex].strategyGiftList
      } else if (strategyType == 4) {
        strategyGiftList = this.useRuleFormModel.strategyGiftList
      }

      if (this.selectCouponType == 1) {
        if (selectGiftList && selectGiftList.length > 0) {
          let item = selectGiftList[selectGiftList.length - 1]

          let hasIndex = strategyGiftList.findIndex(obj => {
            return item.id == obj.giftId;
          })
          if (hasIndex == -1){
            let currentItem = strategyGiftList[this.currentStrategyGiftIndex]
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.count = currentItem.count
            newObj.giftId = item.id
            if (giftType == 3) {
              newObj.type = 3
              newObj.giftName = item.activityName
            } else {
              newObj.type = item.memberType
              newObj.giftName = item.name
            }
            
            strategyGiftList.splice(this.currentStrategyGiftIndex, 1, newObj)
          } else {
            this.$common.warn('当前赠品已添加');
          }
        }
        
      } else if (this.selectCouponType == 2) {
        selectGiftList.forEach((row) => {
          let hasIndex = strategyGiftList.findIndex(obj => {
            return row.id == obj.giftId;
          })
          if (hasIndex == -1){
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.count = ''
            newObj.giftId = row.id
            if (giftType == 3) {
              newObj.type = 3
              newObj.giftName = row.activityName
            } else {
              newObj.type = row.memberType
              newObj.giftName = row.name
            }
            strategyGiftList.push(newObj)
          }
        })

        if (!strategyGiftList[0].giftId) {
          strategyGiftList.splice(0, 1)
        }
        if (strategyGiftList.length > 5) {
          this.$common.warn('每个阶梯最多5个赠品')
          strategyGiftList.splice(5)
        }
      }

    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel

          baseFormModel.beginTime = baseFormModel.validDate && baseFormModel.validDate.length ? baseFormModel.validDate[0] : undefined
          baseFormModel.endTime = baseFormModel.validDate && baseFormModel.validDate.length > 1 ? baseFormModel.validDate[1] : undefined

          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await strategyActivitySave(baseFormModel)
          this.$common.hideLoad()
          if (data) {
            this.id = data.id
            this.$common.n_success('基本信息保存成功')
          } else {
            this.baseFormModel.beginTime = ''
            this.baseFormModel.endTime = ''
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 优惠券类型切换
    typeChange(typeChange) {
      this.$log(typeChange)
      if (typeChange == 3) {
        if (this.useRuleFormModel.conditionBuyerType == 1) {
          this.useRuleFormModel.conditionBuyerType = ''
        }
        if (this.useRuleFormModel.conditionUserType == 1 || this.useRuleFormModel.conditionUserType == 2) {
          this.useRuleFormModel.conditionUserType = ''
        }
        
      }
    },
    // 底部保存按钮点击
    saveClick() {
      if (this.id) {
        this.useRuleFormModel.id = this.id
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }

      this.$refs['useRuleForm'].validate(async (valid) => {
        if (valid) {

          if (this.checkInputData()) {
            let useRuleFormModel = this.useRuleFormModel

            if (useRuleFormModel.conditionBuyerType != 3) {
              useRuleFormModel.conditionEnterpriseType = ''
              useRuleFormModel.conditionEnterpriseTypeValue = []
              useRuleFormModel.conditionUserType = ''
              useRuleFormModel.conditionUserMemberType = []
            } else {
              if (useRuleFormModel.conditionUserType != 4) {
                useRuleFormModel.conditionUserMemberType = []
              }
            }

            let strategyType = this.useRuleFormModel.strategyType
            if (strategyType == 1) {
              if (useRuleFormModel.orderAmountPayType != 2) {
                useRuleFormModel.orderAmountPaymentType = []
              }

              useRuleFormModel.cycleRate = ''
              useRuleFormModel.strategyCycleLadderList = []

              useRuleFormModel.memberRepeat = ''
              useRuleFormModel.memberRepeatDay = []
              useRuleFormModel.memberTimes = ''
              useRuleFormModel.strategyGiftList = []
            }

            if (strategyType == 3) {
              useRuleFormModel.platformSelected = []
              useRuleFormModel.conditionSellerType = ''
              useRuleFormModel.conditionGoodsType = ''
              useRuleFormModel.orderAmountStatusType = ''
              useRuleFormModel.orderAmountPayType = ''
              useRuleFormModel.orderAmountPaymentType = []
              useRuleFormModel.orderAmountLadderType = ''
              useRuleFormModel.strategyAmountLadderList = []

              useRuleFormModel.memberRepeat = ''
              useRuleFormModel.memberRepeatDay = []
              useRuleFormModel.memberTimes = ''
              useRuleFormModel.strategyGiftList = []
            }

            if (strategyType == 4) {
              useRuleFormModel.platformSelected = []
              useRuleFormModel.conditionSellerType = ''
              useRuleFormModel.conditionGoodsType = ''
              useRuleFormModel.orderAmountStatusType = ''
              useRuleFormModel.orderAmountPayType = ''
              useRuleFormModel.orderAmountPaymentType = []
              useRuleFormModel.orderAmountLadderType = ''
              useRuleFormModel.strategyAmountLadderList = []

              useRuleFormModel.cycleRate = ''
              useRuleFormModel.strategyCycleLadderList = []

              useRuleFormModel.memberRepeat = useRuleFormModel.memberRepeat ? 1 : 2
            }

            useRuleFormModel.name = this.baseFormModel.name
            
            this.$common.showLoad()
            let data = await strategyActivitySaveAll(useRuleFormModel)
            this.$common.hideLoad()
            if (typeof data !== 'undefined') {
              this.$common.n_success('保存成功');
              this.$router.go(-1)
            } 
          }
          
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 设置商家 fromType 1-指定商家 2-指定会员推广方
    addProviderClick(fromType) {
      if (this.id) {
        this.addProviderVisible = true
        this.$nextTick( () => {
          this.$refs.addProviderRef.init(this.id, this.running, this.operationType, fromType)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      } 
    },
    
    strategySellerSelectNum(num, fromType) {
      if (fromType == 1) {
        // 策略满赠商家数量
        this.useRuleFormModel.strategySellerLimitCount = num
      } else if (fromType == 2) {
        // 策略满赠推广方会员数量
        this.useRuleFormModel.strategyPromoterMemberLimitCount = num
      }
    },
    // 策略满赠平台SKU数量
    strategyPlatformGoodsSelectNum(num) {
      this.useRuleFormModel.strategyPlatformGoodsLimitCount = num
    },
    // 策略满赠店铺SKU数量
    strategyEnterpriseGoodsSelectNum(num) {
      this.useRuleFormModel.strategyEnterpriseGoodsLimitCount = num
    },
    // 策略满赠客户数量
    strategyBuyerSelectNum(num) {
      this.useRuleFormModel.strategyBuyerLimitCount = num
    },
    // 策略满赠会员方案数量
    strategyMemberSelectNum(num) {
      this.useRuleFormModel.strategyMemberLimitCount = num
    },
    // 策略满赠购买会员数量
    strategyStageMemberEffectSelectNum(num) {
      this.useRuleFormModel.strategyStageMemberEffectCount = num
    },
    // 指定平台SKU
    addGoodsClick() {
      if (this.id) {
        if (!this.useRuleFormModel.conditionSellerType) {
          this.$common.warn('请先选择商家设置');
          return false
        }
        this.addPlatformGoodsVisible = true
        this.$nextTick( () => {
          this.$refs.addPlatformGoodsRef.init(this.id, this.running, this.operationType)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
    },
    // 指定店铺SKU
    addEnterpriseGoodsClick() {
      if (this.id) {
        if (!this.useRuleFormModel.conditionSellerType) {
          this.$common.warn('请先选择商家设置');
          return false
        }
        this.addEnterpriseGoodsVisible = true
        this.$nextTick( () => {
          this.$refs.addEnterpriseGoodsRef.init(this.id, this.running, this.operationType, this.useRuleFormModel.conditionSellerType)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
    },
    // 指定客户
    addClientClick() {
      if (this.id) {
        this.addClientVisible = true
        this.$nextTick( () => {
          this.$refs.addClientRef.init(this.id, this.running, this.operationType)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      } 
    },
    // 指定会员方案-会员定维度
    addMemberDimensionClick() {
      if (this.id) {
        this.addMemberDimensionVisible = true
        this.$nextTick( () => {
          this.$refs.addMemberDimensionRef.init(this.id, this.running, this.operationType)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      } 
    },
    // 购买会员方案-时长维度
    addMemberClick() {
      if (this.id) {
        this.addMemberVisible = true
        this.$nextTick( () => {
          this.$refs.addMemberRef.init(this.id, this.running, this.operationType)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      } 
    },
    // 校验数据
    checkInputData() {

      if (this.operationType == 4) {
        if (!this.baseFormModel.beginTime || !this.baseFormModel.endTime) {
          this.$common.warn('请先保存基本信息')
          return false
        }
      }

      let strategyType = this.useRuleFormModel.strategyType
      if (strategyType == 1) {
        if (this.useRuleFormModel.conditionSellerType == 2 && !this.useRuleFormModel.strategySellerLimitCount) {
          this.$common.warn('设置指定商家')
          return false
        }
        if (this.useRuleFormModel.conditionGoodsType == 2 && !this.useRuleFormModel.strategyPlatformGoodsLimitCount) {
          this.$common.warn('设置指定平台SKU')
          return false
        }
        if (this.useRuleFormModel.conditionGoodsType == 3 && !this.useRuleFormModel.strategyEnterpriseGoodsLimitCount) {
          this.$common.warn('设置指定店铺SKU')
          return false
        }
      } else if (strategyType == 4) {
        if (!this.useRuleFormModel.strategyStageMemberEffectCount) {
          this.$common.warn('设置购买会员方案')
          return false
        }
      }

      if (this.useRuleFormModel.conditionBuyerType == 2 && !this.useRuleFormModel.strategyBuyerLimitCount) {
        this.$common.warn('设置指定客户')
        return false
      }

      if (this.useRuleFormModel.conditionBuyerType == 3 && this.useRuleFormModel.conditionUserType == 4 && this.useRuleFormModel.conditionUserMemberType.indexOf(1) != -1 && !this.useRuleFormModel.strategyMemberLimitCount) {
        this.$common.warn('设置指定会员方案')
        return false
      }

      if (this.useRuleFormModel.conditionBuyerType == 3 && this.useRuleFormModel.conditionUserType == 4 && this.useRuleFormModel.conditionUserMemberType.indexOf(2) != -1 && !this.useRuleFormModel.strategyPromoterMemberLimitCount) {
        this.$common.warn('设置指定会员推广方')
        return false
      }

      // 是否有阶梯
      let hasLadder = false
      // 赠品是否有阶梯
      let hasStrategyGiftList = false
      // 满赠金额条件
      let hasAmountLimit = false
      // 正确的阶梯金额
      let hasLadderAmount = false
      // 赠品数量
      let hasCount = false
      // 是否选择赠品
      let hasGiftId = false

      // 共执行
      let hasTimes = false
      // (1-按周;2-按月)
      let hasConditionValue = false
      // 重复执行日期
      let hasRepeatConditionValue = false

      if (strategyType == 1) {
        let strategyAmountLadderList = this.useRuleFormModel.strategyAmountLadderList
        if (!strategyAmountLadderList || strategyAmountLadderList.length == 0) {
          hasLadder = true
        }
        let lastAmountLimit = 0
        for (let i = 0; i < strategyAmountLadderList.length; i++) {
          let ladderItem = strategyAmountLadderList[i]
          if (!ladderItem.amountLimit || ladderItem.amountLimit == '0') {
            hasAmountLimit = true
          }
          if (Number(ladderItem.amountLimit) < Number(lastAmountLimit)) {
            hasLadderAmount = true
          }
          lastAmountLimit = ladderItem.amountLimit

          let strategyGiftList = ladderItem.strategyGiftList
          if (!strategyGiftList || strategyGiftList.length == 0) {
            hasStrategyGiftList = false
          }
          for (let j = 0; j < strategyGiftList.length; j++) {
            let strategyGiftItem = strategyGiftList[j]
            if (!strategyGiftItem.count || strategyGiftItem.count == '0') {
              hasCount = true
            }
            if (!strategyGiftItem.giftId) {
              hasGiftId = true
            }
          }
        }
      }

      if (strategyType == 3) {
        let strategyCycleLadderList = this.useRuleFormModel.strategyCycleLadderList
        if (!strategyCycleLadderList || strategyCycleLadderList.length == 0) {
          hasLadder = true
        }
        let allConditionValue = []

        for (let i = 0; i < strategyCycleLadderList.length; i++) {
          let ladderItem = strategyCycleLadderList[i]
          if (!ladderItem.times && ladderItem.times != 0) {
            hasTimes = true
          }
          if (!ladderItem.conditionValue || ladderItem.conditionValue.length == 0) {
            hasConditionValue = true
          }

          allConditionValue = allConditionValue.concat(ladderItem.conditionValue)

          let strategyGiftList = ladderItem.strategyGiftList
          if (!strategyGiftList || strategyGiftList.length == 0) {
            hasStrategyGiftList = false
          }
          for (let j = 0; j < strategyGiftList.length; j++) {
            let strategyGiftItem = strategyGiftList[j]
            if (!strategyGiftItem.count || strategyGiftItem.count == '0') {
              hasCount = true
            }
            if (!strategyGiftItem.giftId) {
              hasGiftId = true
            }
          }
        }
        let resultarr = [...new Set(allConditionValue)]
        if (allConditionValue.length != resultarr.length) {
          hasRepeatConditionValue = true
        }
      }

      if (strategyType == 4) {
        let strategyGiftList = this.useRuleFormModel.strategyGiftList
        if (!strategyGiftList || strategyGiftList.length == 0) {
          hasStrategyGiftList = false
        }
        for (let j = 0; j < strategyGiftList.length; j++) {
          let strategyGiftItem = strategyGiftList[j]
          if (!strategyGiftItem.count || strategyGiftItem.count == '0') {
            hasCount = true
          }
          if (!strategyGiftItem.giftId) {
            hasGiftId = true
          }
        }
        if (this.useRuleFormModel.memberRepeat) {
          if (!this.useRuleFormModel.memberTimes && this.useRuleFormModel.memberTimes != 0) {
            hasTimes = true
          }
          if (!this.useRuleFormModel.memberRepeatDay || this.useRuleFormModel.memberRepeatDay.length == 0) {
            hasConditionValue = true
          }
        }

      }

      if (hasLadder) {
        this.$common.warn('最少有1个阶梯')
        return false
      }
      if (hasStrategyGiftList) {
        this.$common.warn('每个阶梯中最少有1个赠品')
        return false
      }
      if (hasAmountLimit) {
        this.$common.warn('满金额需大于0')
        return false
      }
      if (hasLadderAmount) {
        this.$common.warn('后一个阶梯中的金额必须大于前一个阶梯')
        return false
      }

      if (hasCount) {
        this.$common.warn('赠品数量输入数字，必须大于0')
        return false
      }
      if (hasGiftId) {
        this.$common.warn('赠品不允许为空，请重新检查后保存！')
        return false
      }

      if (hasTimes) {
        this.$common.warn('执行次数不允许为空')
        return false
      }
      if (hasConditionValue) {
        this.$common.warn('请选择执行时间周期')
        return false
      }
      if (hasRepeatConditionValue) {
        this.$common.warn('不同阶梯中选中的时间不允许重复')
        return false
      }

      return true

    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit, 5)
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
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
