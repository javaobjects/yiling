<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :disabled="operationType == 1" :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="120px" class="demo-ruleForm">
        <div class="top-bar">
          <div class="content-box my-form-box">
            <el-form-item label="发券活动名称" prop="name">
              <el-input class="show-word-limit" v-model="baseFormModel.name" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="活动起止时间" prop="time">
              <el-date-picker
                v-model="baseFormModel.time"
                type="datetimerange"
                format="yyyy/MM/dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
            </el-form-item>
            <div v-for="(item,index) in baseFormModel.couponActivityList" :key="index">
              <el-form-item label="关联优惠券ID" required>
                <div class="activity-form-item flex-row-left">
                  <el-form-item :prop="'couponActivityList.' + index + '.couponActivityId'" :rules="{ required: true, message: '请输入', trigger: 'blur' }" class="coupon-id-input">
                    <el-input :disabled="running" v-model="item.couponActivityId" class="input-box" @keyup.native="item.couponActivityId = onInput(item.couponActivityId, 0)" @blur="couponActivityIdBlur(item, index)"></el-input>
                  </el-form-item>
                  每满足条件发
                  <el-form-item :prop="'couponActivityList.' + index + '.giveNum'" :rules="{ required: true, message: '请输入', trigger: 'blur' }">
                    <el-input :disabled="running" v-model="item.giveNum" @keyup.native="item.giveNum = onInput(item.giveNum, 0)" class="input-box"></el-input>
                  </el-form-item>
                  张
                  <yl-button v-if="index == 0" class="add-btn" type="text" @click="addGradient">添加</yl-button>
                  <yl-button v-else class="add-btn" type="text" @click="removeGradient(index)">删除</yl-button>
                </div>
              </el-form-item>
              <el-form-item label="">
                <div class="activity-form-item">剩余优惠券数量
                  <el-input class="input-box" v-model="item.residueCount" disabled></el-input>
                  可用供应商
                  <span style="margin-left:10px;" v-if="item.enterpriseLimit == 1" class="font-light-colro">全部供应商</span>
                  <span style="margin-left:10px;" v-if="item.enterpriseLimit == 2" class="font-light-colro">部分供应商</span>
                  <yl-button style="margin-left:10px;" v-if="item.enterpriseLimit == 2" type="text" @click="checkProviderClick(item.couponActivityId)">查看</yl-button>
                  </div>
              </el-form-item>
            </div>
            <el-form-item label="">
              <yl-button type="primary" @click="topSaveClick">保存</yl-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-form :disabled="operationType == 1 || running" :model="useRuleFormModel" :rules="useRules" ref="useRuleForm" label-position="left" label-width="120px" class="demo-ruleForm">
        <div class="top-bar">
          <div class="content-box my-form-box">
            <el-form-item label="发券类型" prop="type">
              <el-radio-group v-model="useRuleFormModel.type" @change="typeChange">
                <el-radio :label="1">订单累积金额</el-radio>
                <el-radio :label="2">会员自动发券</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 订单累积金额 -->
            <div v-if="useRuleFormModel.type == 1">
              <el-form-item label="指定商品" prop="conditionGoods">
                <el-radio-group v-model="useRuleFormModel.conditionGoods">
                  <div class="radio-item">
                    <el-radio :label="1">全平台任意商品</el-radio>
                  </div>
                  <div class="radio-item">
                    <el-radio :label="2">仅限指定商品</el-radio>
                    <yl-button v-if="useRuleFormModel.conditionGoods == 2" class="add-btn" type="text" @click="addGoodsClick">设置商品</yl-button>
                  </div>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="指定支付状态" prop="conditionPaymethod">
                <el-radio-group v-model="useRuleFormModel.conditionPaymethod">
                  <el-radio :label="1">全部支付方式</el-radio>
                  <el-radio :label="2">部分支付方式</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionPaymethod == 2" prop="conditionPaymethodValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionPaymethodValueList">
                    <el-checkbox label="1">在线支付（已完成）</el-checkbox>
                    <el-checkbox label="2">货到付款</el-checkbox>
                    <el-checkbox label="3">账期</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="指定订单状态" prop="conditionOrderStatus">
                <el-radio-group v-model="useRuleFormModel.conditionOrderStatus">
                  <el-radio :label="1">全部订单状态</el-radio>
                  <el-radio :label="2">部分订单状态</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionOrderStatus == 2" prop="conditionOrderStatusValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionOrderStatusValueList">
                    <el-checkbox label="1">已发货</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="下单平台" prop="conditionOrderPlatform">
                <el-radio-group v-model="useRuleFormModel.conditionOrderPlatform">
                  <el-radio :label="1">全部下单平台</el-radio>
                  <el-radio :label="2">部分下单平台</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionOrderPlatform == 2" prop="conditionOrderPlatformValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionOrderPlatformValueList">
                    <el-checkbox label="1">B2B</el-checkbox>
                    <el-checkbox label="2">销售助手</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="企业类型" prop="conditionEnterpriseType">
                <el-radio-group v-model="useRuleFormModel.conditionEnterpriseType">
                  <el-radio :label="1">全部企业类型</el-radio>
                  <el-radio :label="2">部分企业类型</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionEnterpriseType == 2" prop="conditionEnterpriseTypeValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionEnterpriseTypeValueList">
                    <el-checkbox
                      v-for="item in enterpriseType"
                      v-show="item.value != 1 && item.value != 2"
                      :key="item.value"
                      :label="item.value + ''"
                    >{{ item.label }}</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="用户类型" prop="conditionUserType">
                <el-radio-group v-model="useRuleFormModel.conditionUserType">
                  <el-radio :label="1">全部用户</el-radio>
                  <el-radio :label="2">仅普通用户</el-radio>
                  <el-radio :label="3">全部会员用户</el-radio>
                  <el-radio :label="4">部分指定会员</el-radio>
                  <yl-button v-if="useRuleFormModel.conditionUserType == 4" type="text" @click="addProviderClick">添加会员</yl-button>
                </el-radio-group>
              </el-form-item>
              <div class="explain-view mar-b-16">规则说明：以止规则均满足，且累积金额也满足的情况下，才发放优惠券</div>
              <el-form-item label="累积金额满" prop="cumulative">
                <div class="activity-form-item"><el-input class="input-box" v-model="useRuleFormModel.cumulative" @keyup.native="useRuleFormModel.cumulative = onInput(useRuleFormModel.cumulative, 0)"></el-input>则发放</div>
              </el-form-item>
              <el-form-item label="是否重复发放" prop="repeatGive" required>
                <el-radio-group v-model="useRuleFormModel.repeatGive">
                  <div class="radio-item">
                    <el-radio :label="1">仅发一次</el-radio>
                  </div>
                  <div class="radio-item">
                    <el-radio :label="2">可重复发放</el-radio>
                    <div class="assume-form-item" v-if="useRuleFormModel.repeatGive == 2">
                      最多发放
                      <el-form-item prop="maxGiveNum">
                        <el-input class="input-box" v-model="useRuleFormModel.maxGiveNum"></el-input>
                      </el-form-item>
                      次<span class="tip-text">*不限时满足条件即发放一次优惠券</span></div>
                  </div>
                </el-radio-group>
              </el-form-item>
            </div>
            <!-- 会员自动发券 -->
            <div v-if="useRuleFormModel.type == 2">
              <el-form-item label="企业类型" prop="conditionEnterpriseType">
                <el-radio-group v-model="useRuleFormModel.conditionEnterpriseType">
                  <el-radio :label="1">全部企业类型</el-radio>
                  <el-radio :label="2">部分企业类型</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionEnterpriseType == 2" prop="conditionEnterpriseTypeValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionEnterpriseTypeValueList">
                    <el-checkbox
                      v-for="item in enterpriseType"
                      v-show="item.value != 1 && item.value != 2"
                      :key="item.value"
                      :label="item.value + ''"
                    >{{ item.label }}</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="是否有推广码" prop="conditionPromotionCode">
                <el-radio-group v-model="useRuleFormModel.conditionPromotionCode" @change="conditionPromotionCodeChange">
                  <el-radio :label="3">不使用</el-radio>
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="2">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="会员设置" prop="conditionUserType">
                <el-radio-group v-model="useRuleFormModel.conditionUserType">
                  <el-radio :label="3">全部会员</el-radio>
                  <el-radio :label="4">部分会员</el-radio>
                  <yl-button v-if="useRuleFormModel.conditionUserType == 4" type="text" @click="addProviderClick">添加会员</yl-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="发放规则" prop="repeatGive" required>
                <el-radio-group v-model="useRuleFormModel.repeatGive">
                  <div class="radio-item">
                    <el-radio :label="1">一次性发放</el-radio>
                    <el-radio :label="2" v-if="useRuleFormModel.conditionPromotionCode == 3">每月发放</el-radio>
                    <div class="assume-form-item" v-if="useRuleFormModel.repeatGive == 2">
                      共计发放
                      <el-form-item prop="maxGiveNum">
                        <el-input class="input-box" v-model="useRuleFormModel.maxGiveNum"></el-input>
                      </el-form-item>
                      月
                    </div>
                  </div>
                </el-radio-group>
              </el-form-item>
            </div>
          </div>
        </div>
      </el-form>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 添加商品弹框 -->
    <send-add-goods-dialog v-if="addGoodsVisible" ref="addGoodsRef" @save="addGoodsSaveClick"></send-add-goods-dialog>
    <!-- 添加会员 -->
    <send-add-provider-dialog v-if="addProviderVisible" ref="addProviderRef" @save="addProviderSaveClick"></send-add-provider-dialog>
    <!-- 查看部分供应商弹框 -->
    <check-provider-dialog v-if="checkProviderVisible" ref="checkProviderRef"></check-provider-dialog>
  </div>
</template>

<script>
import { autoGiveGetCouponActivity, autoGiveSaveOrUpdateBasic, couponActivityAutoGiveSaveOrUpdateRules, getResidueCount } from '@/subject/admin/api/b2b_api/discount_coupon'
import { enterpriseType } from '@/subject/admin/utils/busi'
import SendAddGoodsDialog from '../components/send_add_goods_dialog';
import SendAddProviderDialog from '../components/send_add_provider_dialog';
import CheckProviderDialog from '../components/check_provider_dialog';
import { formatDate } from '@/subject/admin/utils'
import { onInputLimit } from '@/common/utils'

export default {
  components: {
    SendAddGoodsDialog,
    SendAddProviderDialog,
    CheckProviderDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    }
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      this.$common.log('rule:', rule)
      if (value == 0) {
        callback(new Error('请选择'));
      } else {
        callback();
      }
    };
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      loading: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      baseFormModel: {
        name: '',
        time: [],
        couponActivityList: [
          {
            couponActivityId: '',
            giveNum: '',
            residueCount: '',
            // 供应商限制（1-全部供应商；2-部分供应商）
            enterpriseLimit: 0
          }
        ]
      },
      baseRules: {
        name: [{ required: true, message: '请输入发券活动名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        type: 1,
        conditionGoods: 1,
        conditionPaymethod: 1,
        conditionPaymethodValueList: [],
        // 指定订单状态(1-全部；2-指定)
        conditionOrderStatus: 1,
        conditionOrderStatusValueList: [],
        // 指定下单平台(1-全部；2-指定)
        conditionOrderPlatform: 1,
        conditionOrderPlatformValueList: [],
        // 指定企业类型(1-全部；2-指定)
        conditionEnterpriseType: 1,
        conditionEnterpriseTypeValueList: [],
        // 是否有推广码
        conditionPromotionCode: 3,
        // 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
        conditionUserType: 1,
        // 累计金额/数量
        cumulative: '',
        // 是否重复发放(1-仅发一次；2-重复发放多次)
        repeatGive: 1,
        maxGiveNum: ''
      },
      useRules: {
        type: [
          { required: true, message: '请选择发券类型', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionGoods: [
          { required: true, message: '请选择指定商品', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionPaymethod: [
          { required: true, message: '请选择指定支付状态', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionPaymethodValueList: [{ type: 'array', required: true, message: '请选择支付方式', trigger: 'change' }],
        conditionOrderStatus: [
          { required: true, message: '请选择指定订单状态', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionOrderStatusValueList: [{ type: 'array', required: true, message: '请选择订单状态', trigger: 'change' }],
        conditionOrderPlatform: [
          { required: true, message: '请选择指定下单平台', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionOrderPlatformValueList: [{ type: 'array', required: true, message: '请选择下单平台', trigger: 'change' }],
        conditionEnterpriseType: [
          { required: true, message: '请选择指定企业类型', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionEnterpriseTypeValueList: [{ type: 'array', required: true, message: '请选择企业类型', trigger: 'change' }],
        conditionPromotionCode: [
          { required: true, message: '请选择是否有推广码', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        conditionUserType: [
          { required: true, message: '请选择指定用户类型', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        cumulative: [{ required: true, message: '请输入累计金额', trigger: 'blur' }],
        repeatGive: [
          { required: true, message: '请选择发放规则', trigger: 'change' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        maxGiveNum: [{ required: true, message: '请输入', trigger: 'blur' }]
      },
      addGoodsVisible: false,
      addProviderVisible: false,
      checkProviderVisible: false
    };
  },
  mounted() {
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    if (this.id && this.operationType) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let data = await autoGiveGetCouponActivity(this.id)
      if (data) {
        this.running = data.running
        let couponActivityList = [
          {
            couponActivityId: '',
            giveNum: '',
            enterpriseLimit: 0
          }
        ]
        let currentCouponActivityList = []
        if (data.couponActivityList && data.couponActivityList.length > 0) {
          currentCouponActivityList = data.couponActivityList
        } else {
          currentCouponActivityList = couponActivityList
        }
        let baseFormModel = {
          name: data.name,
          couponActivityList: currentCouponActivityList
        }
        if (data.beginTime && data.endTime) {
          baseFormModel.time = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        this.useRuleFormModel = data
      }
    },
    // 发券类型切换
    typeChange(type) {
      this.$refs['useRuleForm'].clearValidate();
      if (type == 1) {
        this.useRuleFormModel.conditionUserType = 1
      } else {
        this.useRuleFormModel.conditionUserType = 3
      }
      this.useRuleFormModel.conditionEnterpriseType = 1
      this.useRuleFormModel.conditionEnterpriseTypeValueList = []
      this.useRuleFormModel.conditionPromotionCode = 3
      this.useRuleFormModel.repeatGive = 1
      this.useRuleFormModel.maxGiveNum = ''
      
    },
    // 是否有推广码切换
    conditionPromotionCodeChange() {
      this.useRuleFormModel.repeatGive = 1
    },
    // 弹框保存点击
    addGoodsSaveClick(parms) {
      this.$common.log(parms)
    },
    // 添加梯度
    addGradient() {
      let couponActivityList = this.baseFormModel.couponActivityList
      // eslint-disable-next-line no-new-object
      let newObj = new Object()
      newObj.couponActivityId = ''
      newObj.giveNum = ''
      newObj.enterpriseLimit = 0
      this.baseFormModel.couponActivityList = [...couponActivityList, newObj]
    },
    // 删除梯度
    removeGradient(index) {
      let couponActivityList = this.baseFormModel.couponActivityList
      couponActivityList.splice(index, 1)
      this.baseFormModel.couponActivityList = couponActivityList
    },
    // 根据优惠券ID获取剩余数量
    couponActivityIdBlur(item, index) {
      this.$log(item, index)
      this.getResidueCountMethod(item, index)
    },
    async getResidueCountMethod(item, index) {
      let couponActivityList = this.baseFormModel.couponActivityList
      let data = await getResidueCount(Number(item.couponActivityId))
      if (data) {
        let couponActivity = couponActivityList[index]
        couponActivity.residueCount = data.residueCount
        couponActivity.enterpriseLimit = data.enterpriseLimit
        couponActivityList.splice(index, 1, couponActivity)
        this.baseFormModel.couponActivityList = couponActivityList
      } else {
        let couponActivity = couponActivityList[index]
        couponActivity.giveNum = ''
        couponActivity.residueCount = ''
        couponActivity.enterpriseLimit = 0
        couponActivityList.splice(index, 1, couponActivity)
        this.baseFormModel.couponActivityList = couponActivityList
      }
    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel

          baseFormModel.beginTime = baseFormModel.time && baseFormModel.time.length ? baseFormModel.time[0] : undefined
          baseFormModel.endTime = baseFormModel.time && baseFormModel.time.length > 1 ? baseFormModel.time[1] : undefined
          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await autoGiveSaveOrUpdateBasic(baseFormModel)
          this.$common.hideLoad()
          if (data) {
            this.id = data
            this.$common.n_success('保存成功')
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
          
          let useRuleFormModel = this.useRuleFormModel
          this.$common.showLoad()
          let data = await couponActivityAutoGiveSaveOrUpdateRules(useRuleFormModel)
          this.$common.hideLoad()
          if (data) {
            this.$common.n_success('保存成功');
            this.$router.go(-1)
          } 
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 设置商品点击
    addGoodsClick() {
      if (this.id) {
        this.addGoodsVisible = true
        this.$nextTick( () => {
          this.$refs.addGoodsRef.init(this.id)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
    },
    // 设置供应商
    addProviderClick() {
      if (this.id) {
        this.addProviderVisible = true
        this.$nextTick( () => {
          this.$refs.addProviderRef.init(this.id)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
      
    },
    addProviderSaveClick() {
      
    },
    // 
    checkProviderClick(couponActivityId) {
      this.checkProviderVisible = true
      this.$nextTick( () => {
        this.$refs.checkProviderRef.init(couponActivityId)
      })
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
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
