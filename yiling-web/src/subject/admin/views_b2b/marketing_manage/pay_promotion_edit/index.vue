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
              <el-form-item label="活动名称" prop="name">
                <el-input class="show-word-limit" v-model="baseFormModel.name" maxlength="20" show-word-limit placeholder="请输入动名称"></el-input>
              </el-form-item>
              <el-form-item label="活动分类" prop="sponsorType">
                <el-select v-model="baseFormModel.sponsorType" placeholder="请选择">
                  <el-option v-for="item in sponsorTypeArray" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="费用承担方" prop="bear">
                <el-select v-model="baseFormModel.bear" placeholder="请选择">
                  <el-option v-for="item in bearArray" :key="item.value" :label="item.label"
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
            <!-- 适用范围 -->
            <div class="header-bar header-renative">
              <div class="sign"></div>适用范围
            </div>
            <!-- 使用平台 -->
            <div class="content-box my-form-box">
              <div>
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
                  <el-radio :label="1" :disabled="running || operationType == 1">全部客户</el-radio>
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
                    <el-radio :label="1" :disabled="running || operationType == 1">全部用户</el-radio>
                    <el-radio :label="2" :disabled="running || operationType == 1">普通用户</el-radio>
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
              <el-form-item label="支付方式" prop="payType">
                <el-checkbox-group v-model="useRuleFormModel.payType" :disabled="running || operationType == 1">
                    <el-checkbox :label="4">在线支付</el-checkbox>
                    <el-checkbox :label="1">线下支付</el-checkbox>
                    <el-checkbox :label="2">账期</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
            </div>
          </div>
          <!-- 生效条件&计算规则 -->
          <div class="font-size-large font-important-color mar-b-16">生效条件&计算规则</div>
          <!-- 基本规则设置 -->
          <div class="top-bar">
            <!-- 订单累计金额 -->
            <div class="content-box my-form-box">
              <el-form-item label="生效条件" prop="conditionEffective">
                <el-radio-group v-model="useRuleFormModel.conditionEffective" :disabled="running || operationType == 1">
                  <el-radio :label="1">按金额</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 多阶梯 -->
              <div class="Gradients-item mar-t-10" v-for="(ladderItem, ladderIndex) in useRuleFormModel.calculateRules" :key="ladderIndex">
                <div>
                  <div class="header-bar header-renative">
                  <div class="sign"></div>阶梯{{ ladderIndex + 1 }}<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeLadderClick(ladderIndex)"></yl-button></div>
                  <el-form-item class="" label="满金额" label-width="100px">
                    <el-input v-model="ladderItem.thresholdValue" placeholder="请输入" @keyup.native="ladderItem.thresholdValue = onInput(ladderItem.thresholdValue, 2)" :disabled="running || operationType == 1"></el-input>
                  </el-form-item>
                  <el-form-item class="" label="促销规则" label-width="100px">
                    <el-radio-group v-model="ladderItem.type" @change="(type) => typeChange(type, ladderIndex)">
                      <el-radio :label="1" :disabled="running || operationType == 1">满减</el-radio>
                      <el-radio :label="2" :disabled="running || operationType == 1">满折</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="" label-width="100px" required>
                    <div class="activity-form-item flex-row-left">
                      {{ ladderItem.type == 1 ? '减' : '打' }}
                      <el-form-item label="" class="input-item">
                        <el-input :disabled="running || operationType == 1" class="input-box" v-model="ladderItem.discountValue" @keyup.native="ladderItem.discountValue = onInput(ladderItem.discountValue, 2)"></el-input>
                          <span class="tip-text">{{ ladderItem.type == 1 ? '支持2位小数' : '如打8折，填写80' }}</span>
                        </el-form-item>
                      {{ ladderItem.type == 1 ? '元' : '%折' }}
                      <div class="flex-row-left" v-if="ladderItem.type == 2">
                        <span class="mar-l-32">最高优惠</span>
                        <el-form-item label="" class="input-item">
                          <el-input :disabled="running || operationType == 1" class="input-box" v-model="ladderItem.discountMax" @keyup.native="ladderItem.discountMax = onInput(ladderItem.discountMax, 2)"></el-input>
                          <span class="tip-text">0为不限制</span>
                        </el-form-item>
                        元
                      </div>
                    </div>
                  </el-form-item>
                </div>
              </div>
              <!-- 多阶梯 -->
              <!-- 新增任务返利 -->
              <div style="text-align:center;"><yl-button icon="el-icon-circle-plus-outline" type="text" @click="addLadder" :disabled="running || operationType == 1">增加阶梯</yl-button></div>
            </div>
          </div>
          <div class="top-bar">
            <!-- 适用范围 -->
            <div class="header-bar header-renative">
              <div class="sign"></div>临界控制
            </div>
            <!-- 使用平台 -->
            <div class="content-box my-form-box">
              <el-form-item label="预算金额" prop="budgetAmount" label-width="140px">
                <el-input :disabled="running || operationType == 1" v-model="useRuleFormModel.budgetAmount" placeholder="请输入预算金额" @keyup.native="useRuleFormModel.budgetAmount = onInput(useRuleFormModel.budgetAmount, 2)"></el-input>
                <span class="font-light-color">（0为不控制）</span>
              </el-form-item>
              <el-form-item label="每个客户参与次数" prop="maxGiveNum" label-width="140px">
                <el-input :disabled="running || operationType == 1" v-model="useRuleFormModel.maxGiveNum" placeholder="请输入每个客户参与次数" @keyup.native="useRuleFormModel.maxGiveNum = onInput(useRuleFormModel.maxGiveNum, 0, 8)"></el-input>
                <span class="font-light-color">（0为不控制）</span>
              </el-form-item>
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
  </div>
</template>

<script>
import { payPromptionActivityQueryDetail, payPromptionActivitySave, payPromptionActivitySaveRule } from '@/subject/admin/api/b2b_api/marketing_manage'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { enterpriseType } from '@/subject/admin/utils/busi'

import AddProviderDialog from '../components/pay_promotion/add_provider_dialog'
import AddPlatformGoodsDialog from '../components/pay_promotion/add_platform_goods_dialog'
import AddEnterpriseGoodsDialog from '../components/pay_promotion/add_enterprise_goods_dialog'
import AddClientDialog from '../components/pay_promotion/add_client_dialog'
import AddMemberDimensionDialog from '../components/pay_promotion/add_member_dimension_dialog'

export default {
  components: {
    AddProviderDialog,
    AddPlatformGoodsDialog,
    AddEnterpriseGoodsDialog,
    AddClientDialog,
    AddMemberDimensionDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().getTime() - 86400000; 
        }
      },
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 供应商
      addProviderVisible: false,
      // 指定平台SKU
      addPlatformGoodsVisible: false,
      addEnterpriseGoodsVisible: false,
      addClientVisible: false,
      addMemberDimensionVisible: false,
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
      // 承担方
      bearArray: [
        {
          label: '平台承担',
          value: 1
        },
        {
          label: '商家承担',
          value: 2
        }
      ],
      baseFormModel: {
        name: '',
        sponsorType: '',
        bear: '',
        validDate: [],
        operatingRemark: ''
      },
      baseRules: {
        name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        bear: [{ required: true, message: '请选择费用承担方', trigger: 'change' }],
        validDate: [{ required: true, message: '请选择生效时间', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
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
        // 支付方式
        payType: [],
        // 生效条件&计算规则
        // 生效条件(1-按金额)
        conditionEffective: '',
        // 阶梯
        calculateRules: [
          {
            // 满赠金额条件
            thresholdValue: '',
            // 促销规则类型（1-满减券；2-满折券）
            type: 1,
            // 优惠内容（金额/折扣比例）
            discountValue: '',
            // 最高优惠多少
            discountMax: 0
          }
        ],
        // 预算金额
        budgetAmount: '',
        // 每个客户参数次数
        maxGiveNum: '',

        // 策略满赠商家数量
        strategySellerLimitCount: 0,
        strategyPlatformGoodsLimitCount: 0,
        strategyEnterpriseGoodsLimitCount: 0,
        strategyBuyerLimitCount: 0,
        strategyMemberLimitCount: 0,
        strategyPromoterMemberLimitCount: 0
      },
      useRules: {
        conditionSellerType: [{ required: true, message: '请选择商家范围', trigger: 'change' }],
        conditionGoodsType: [{ required: true, message: '请选择商品范围', trigger: 'change' }],
        conditionBuyerType: [{ required: true, message: '请选择客户范围', trigger: 'change' }],
        conditionEnterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        conditionEnterpriseTypeValue: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        conditionUserType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        conditionUserMemberType: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        payType: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
        conditionEffective: [{ required: true, message: '请选择生效条件', trigger: 'change' }],
        budgetAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }],
        maxGiveNum: [{ required: true, message: '请输入每个客户参与次数', trigger: 'blur' }]
      }
    };
  },
  mounted() {

    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let data = await payPromptionActivityQueryDetail(this.id)
      if (data) {
        this.running = data.running

        let baseFormModel = {
          name: data.name,
          sponsorType: data.sponsorType,
          bear: data.bear,
          validDate: data.validDate,
          operatingRemark: data.operatingRemark
        }
        if (data.beginTime && data.beginTime > 0 && data.endTime && data.endTime > 0) {
          baseFormModel.validDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        let useRuleFormModel = {
          conditionSellerType: data.conditionSellerType || '',
          conditionGoodsType: data.conditionGoodsType || '',
          conditionBuyerType: data.conditionBuyerType || '',
          conditionEnterpriseType: data.conditionEnterpriseType || '',
          conditionEnterpriseTypeValue: data.conditionEnterpriseTypeValue,
          conditionUserType: data.conditionUserType || '',
          conditionUserMemberType: data.conditionUserMemberType,
          conditionOther: data.conditionOther,
          payType: data.payType,
          conditionEffective: data.conditionEffective || '',
          calculateRules: data.calculateRules,
          budgetAmount: data.budgetAmount,
          maxGiveNum: data.maxGiveNum,

          strategySellerLimitCount: data.strategySellerLimitCount,
          strategyPlatformGoodsLimitCount: data.strategyPlatformGoodsLimitCount,
          strategyEnterpriseGoodsLimitCount: data.strategyEnterpriseGoodsLimitCount,
          strategyBuyerLimitCount: data.strategyBuyerLimitCount,
          strategyMemberLimitCount: data.strategyMemberLimitCount,
          strategyPromoterMemberLimitCount: data.strategyPromoterMemberLimitCount
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
    typeChange(type, ladderIndex) {
      console.log(type, ladderIndex)
      let calculateRules = this.useRuleFormModel.calculateRules
      let item = calculateRules[ladderIndex]
      item.discountValue = ''
      item.discountMax = 0
    },
    // 添加阶梯
    addLadder() {
      let calculateRules = this.useRuleFormModel.calculateRules
      if (calculateRules.length == 3) {
        this.$common.warn('最多3个阶梯')
        return
      }
      let newLadder = {
        // 满赠金额条件
        thresholdValue: '',
        // 促销规则类型（1-满减券；2-满折券）
        type: 1,
        // 优惠内容（金额/折扣比例）
        discountValue: '',
        // 最高优惠多少
        discountMax: 0
      }
      calculateRules.push(newLadder)
      
    },
    // 删除阶梯
    removeLadderClick(ladderIndex) {
      let calculateRules = this.useRuleFormModel.calculateRules
      if (calculateRules.length == 1) {
        this.$common.warn('最少有1个阶梯')
        return
      }
      calculateRules.splice(ladderIndex, 1)
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
          let data = await payPromptionActivitySave(baseFormModel)
          this.$common.hideLoad()
          if (data) {
            this.id = data
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

            useRuleFormModel.name = this.baseFormModel.name
            
            this.$common.showLoad()
            let data = await payPromptionActivitySaveRule(useRuleFormModel)
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
    // 校验数据
    checkInputData() {

      if (this.operationType == 4) {
        if (!this.baseFormModel.beginTime || !this.baseFormModel.endTime) {
          this.$common.warn('请先保存基本信息')
          return false
        }
      }

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
      // 满赠金额条件
      let hasAmountLimit = false
      // 正确的阶梯金额
      let hasLadderAmount = false

      // 满减金额
      let hasDiscountValue = false
      // 满折折扣
      let hasDiscountRatio = false
      // 最高优惠多少
      let hasDiscountMax = false

      let calculateRules = this.useRuleFormModel.calculateRules
      if (!calculateRules || calculateRules.length == 0) {
        hasLadder = true
      }

      let lastAmountLimit = 0
      for (let i = 0; i < calculateRules.length; i++) {
        let ladderItem = calculateRules[i]
        if (!ladderItem.thresholdValue || ladderItem.thresholdValue == '0') {
          hasAmountLimit = true
        }
        if (Number(ladderItem.thresholdValue) < Number(lastAmountLimit)) {
          hasLadderAmount = true
        }
        lastAmountLimit = ladderItem.thresholdValue
        // 满减
        if (ladderItem.type == 1) {
          if (!ladderItem.discountValue || ladderItem.discountValue == '0' || Number(ladderItem.thresholdValue) < Number(ladderItem.discountValue)) {
            hasDiscountValue = true
          }
        }
        if (ladderItem.type == 2) {
          if (!ladderItem.discountValue || ladderItem.discountValue == '0' || Number(ladderItem.discountValue) > 100) {
            hasDiscountRatio = true
          }
          if (Number(ladderItem.thresholdValue) < Number(ladderItem.discountMax)) {
            hasDiscountMax = true
          }
        }
        
      }

      if (hasLadder) {
        this.$common.warn('最少有1个阶梯')
        return false
      }
      if (hasAmountLimit) {
        this.$common.warn('满金额需大于0')
        return false
      }
      if (hasLadderAmount) {
        this.$common.warn('后一个阶梯中的满金额必须大于前一个阶梯')
        return false
      }

      if (hasDiscountValue) {
        this.$common.warn('满减金额需大于0,小于满金额')
        return false
      }
      if (hasDiscountRatio) {
        this.$common.warn('满折折扣需大于0,小于等于100')
        return false
      }
      if (hasDiscountMax) {
        this.$common.warn('最高优惠金额需要小于满金额')
        return false
      }

      return true

    },
    // 校验两位小数
    onInput(value, limit, maxLength = 9) {
      return onInputLimit(value, limit, maxLength)
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
