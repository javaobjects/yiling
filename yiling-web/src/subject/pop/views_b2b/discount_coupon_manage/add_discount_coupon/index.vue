<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <div>
        <el-form :disabled="operationType == 1" :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="100px" class="demo-ruleForm">
          <div class="top-bar">
            <div class="content-box my-form-box">
              <el-form-item label="优惠券类型:" prop="type" style="margin-bottom:0px;">
                <el-radio-group v-model="baseFormModel.type" :disabled="operationType != 3" @change="typeChange">
                  <el-radio :label="1">满减券</el-radio>
                  <el-radio :label="2">满折券</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          </div>
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>基本信息
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="优惠券名称:" prop="name">
                <el-input class="show-word-limit" v-model="baseFormModel.name" maxlength="20" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="预算编号:" prop="budgetCode">
                <el-input v-model="baseFormModel.budgetCode" ></el-input>
              </el-form-item>
              <el-form-item label="活动类型:" prop="threshold">
                <el-radio-group v-model="baseFormModel.threshold">
                  <el-radio v-if="baseFormModel.type == 1" :label="1">金额满减-按支付金额满（金额）减（金额）</el-radio>
                  <el-radio v-if="baseFormModel.type == 2" :label="1">金额满折-按支付金额满（金额）折扣（金额）</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 订单实付金额满 -->
              <el-form-item label="" required>
                <div class="activity-form-item flex-row-left">
                  订单实付金额满
                  <el-form-item label="" prop="thresholdValue">
                    <el-input class="input-box" v-model="baseFormModel.thresholdValue" @keyup.native="baseFormModel.thresholdValue = onInput(baseFormModel.thresholdValue, 2)"></el-input>
                  </el-form-item>
                  元则{{ baseFormModel.type == 1 ? '减' : '打' }}
                  <el-form-item label="" prop="discountValue">
                    <el-input class="input-box" v-model="baseFormModel.discountValue" @keyup.native="baseFormModel.discountValue = onInput(baseFormModel.discountValue, 2)"></el-input>
                  </el-form-item>
                  {{ baseFormModel.type == 1 ? '元' : '%折扣' }}
                  <span class="tip-text"><span class="red-text">*</span>数字项必须填写，支持小数点后两位</span>
                </div>
              </el-form-item>
              <!-- 最高优惠 -->
              <el-form-item label="" v-if="baseFormModel.type == 2">
                <div class="activity-form-item flex-row-left">
                  最高优惠
                  <el-form-item label="">
                    <el-input class="input-box" v-model="baseFormModel.discountMax" @keyup.native="baseFormModel.discountMax = onInput(baseFormModel.discountMax, 2)"></el-input>
                  </el-form-item>
                  <span class="tip-text"><span class="red-text">*</span>不选择时则折扣无上限，选择是必须填写正数，支持小数点后两位</span>
                </div>
              </el-form-item>
              <el-form-item label="有效期:" prop="useDateType" required>
                <el-radio-group v-model="baseFormModel.useDateType">
                  <div class="radio-item">
                    <el-radio :label="1">固定效期</el-radio>
                    <div class="assume-form-item flex-row-left" v-if="baseFormModel.useDateType == 1">
                      <el-form-item label="" prop="validDate">
                        <el-date-picker
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
                      <span class="tip-text"><span class="red-text">*</span>结束时间必须等于或晚于开始时间</span>
                    </div>
                  </div>
                  <div class="radio-item">
                    <el-radio :label="2">按发放/领取时间设定</el-radio>
                    <div v-if="baseFormModel.useDateType == 2" class="assume-form-item">
                      发放/领取日后
                      <el-form-item label="" prop="expiryDays">
                        <el-input class="input-box" v-model="baseFormModel.expiryDays" @keyup.native="baseFormModel.expiryDays = onInput(baseFormModel.expiryDays, 0)"></el-input>
                      </el-form-item>
                      天失效<span class="tip-text"><span class="red-text">*</span>发放/领取日+X天（23:59:59）优惠券失效</span>
                    </div>
                  </div>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="运营备注">
                <el-input class="show-word-limit" v-model="baseFormModel.remark" placeholder="请输入运营备注" maxlength="20" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="">
                <yl-button v-if="operationType != 1" type="primary" @click="topSaveClick">保存</yl-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 使用规则设置 -->
      <div class="font-size-large font-important-color mar-b-16">使用规则设置</div>
      <div >
        <el-form :model="useRuleFormModel" :rules="useRules" ref="useRuleForm" label-position="left" label-width="110px" class="demo-ruleForm">
          <!-- 基本规则设置 -->
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>基本规则设置
            </div>
            <!-- 使用平台 -->
            <div class="content-box my-form-box">
              <el-form-item label="使用平台:">
                <el-radio-group v-model="useRuleFormModel.platformLimit" :disabled="operationType == 1">
                  <el-radio :label="1">全部平台可用</el-radio>
                  <el-radio :label="2">部分平台可用</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.platformLimit == 2">
                <el-checkbox-group v-model="useRuleFormModel.platformSelectedList" :disabled="operationType == 1">
                    <el-checkbox label="1">B2B</el-checkbox>
                    <el-checkbox label="2">销售助手</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <!-- 支付方式 -->
              <!-- b2b-790 计算逻辑优化后，这里就隐藏了 -->
              <!-- <el-form-item label="支付方式:">
                <el-radio-group v-model="useRuleFormModel.payMethodLimit" :disabled="operationType == 1">
                  <el-radio :label="1">全部支付方式可用</el-radio>
                  <el-radio :label="2">部分支付方式可用</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.payMethodLimit == 2">
                <el-checkbox-group v-model="useRuleFormModel.payMethodSelectedList" :disabled="operationType == 1">
                    <el-checkbox label="1">在线支付（已完成）</el-checkbox>
                    <el-checkbox label="2">货到付款</el-checkbox>
                    <el-checkbox label="3">账期</el-checkbox>
                  </el-checkbox-group>
              </el-form-item> -->
              <!-- b2b-553 计算逻辑优化后，这里就隐藏了 -->
              <!-- <el-form-item label="是否与哪些促销可共用:" label-width="180px">
                <el-checkbox-group v-model="useRuleFormModel.coexistPromotionList" :disabled="operationType == 1">
                    <el-checkbox label="1">满赠</el-checkbox>
                  </el-checkbox-group>
              </el-form-item> -->
              <div class="border-1px-t" style="height: 16px;"></div>
              <el-form-item label="设置可用商品:">
                <el-radio-group v-model="useRuleFormModel.goodsLimit">
                  <el-radio :label="1" :disabled="operationType == 1">全部商品可用</el-radio>
                  <el-radio :label="2" :disabled="operationType == 1">部分商品可用</el-radio>
                  <yl-button v-if="useRuleFormModel.goodsLimit == 2" type="text" @click="addGoodsClick">设置更多可用商品</yl-button>
                </el-radio-group>
              </el-form-item>
              <div class="explain-view">添加更多可用商品：满足基本条件、足供应商设置条件和商品设置条件或更多可用商品均可使用此优惠券</div>
            </div>
          </div>
          <!-- 生券规则设置 -->
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>生券规则设置
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="生券数量:" prop="totalCount">
                <div class="input-form-item">
                  <el-input v-model="useRuleFormModel.totalCount" @keyup.native="useRuleFormModel.totalCount = onInput(useRuleFormModel.totalCount, 0)" :disabled="operationType == 1"></el-input>
                  <div class="text-view">生券数量为正整数，将生成对应数量的优惠券</div>
                </div>
              </el-form-item>
              <el-form-item label="优惠券状态:">
                <el-radio-group v-model="useRuleFormModel.status" :disabled="operationType == 1">
                  <el-radio :label="1">启用</el-radio>
                  <el-radio :label="2">停用</el-radio>
                  <el-radio :label="3">废弃</el-radio>
                </el-radio-group>
              </el-form-item>
              <div class="tip-box">
                <i class="el-icon-warning"></i>
                <span> 停用状态时，对应批次优惠券将不可以发放优惠券;作废状态，对应批次券不可发放，直接隐藏</span>
              </div>
            </div>
          </div>
          <!-- 用户领取设置 -->
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>用户领取设置
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="用户领取设置:">
                <el-radio-group v-model="useRuleFormModel.canGet" :disabled="operationType == 1">
                  <el-radio :label="1">用户可领</el-radio>
                  <el-radio :label="2">用户不可领取</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 用户可领设置 -->
              <div v-if="useRuleFormModel.canGet == 1">
                <el-form-item label="活动起止时间:" required="">
                  <div class="activity-form-item flex-row-left">
                    <el-form-item label="" prop="activityTime">
                      <el-date-picker
                        v-model="useRuleFormModel.activityTime"
                        type="datetimerange"
                        format="yyyy/MM/dd HH:mm:ss"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                        :disabled="operationType == 1">
                      </el-date-picker>
                    </el-form-item>
                    <el-form-item prop="giveNum" style="margin-left: 28px;">
                      每企业可领取
                      <el-input v-model="useRuleFormModel.giveNum" @keyup.native="useRuleFormModel.giveNum = onInput(useRuleFormModel.giveNum, 0, 2)" :disabled="operationType == 1"></el-input>
                      张
                    </el-form-item>
                  </div>
                </el-form-item>
                <el-form-item label="企业类型:" prop="conditionEnterpriseType">
                  <el-radio-group v-model="useRuleFormModel.conditionEnterpriseType" :disabled="operationType == 1">
                    <el-radio :label="1">全部企业类型</el-radio>
                    <el-radio :label="2">部分企业类型</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="" v-if="useRuleFormModel.conditionEnterpriseType == 2" prop="conditionEnterpriseTypeValueList">
                  <el-checkbox-group v-model="useRuleFormModel.conditionEnterpriseTypeValueList" :disabled="operationType == 1">
                      <el-checkbox
                        v-for="item in enterpriseType"
                        v-show="item.value != 1 && item.value != 2"
                        :key="item.value"
                        :label="item.value + ''"
                      >{{ item.label }}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="用户类型:" prop="conditionUserType">
                  <el-radio-group v-model="useRuleFormModel.conditionUserType" :disabled="operationType == 1">
                    <el-radio :label="1">全部用户</el-radio>
                    <el-radio :label="5">部分用户</el-radio>
                    <yl-button v-if="useRuleFormModel.conditionUserType == 5" type="text" @click="addProviderClick">设置用户</yl-button>
                  </el-radio-group>
                </el-form-item>
              </div>
              
            </div>
          </div>
        </el-form>
      </div>
      
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 添加商品弹框 -->
    <add-goods-dialog v-if="addGoodsVisible" ref="addGoodsRef" @save="addGoodsSaveClick"></add-goods-dialog>
    <!-- 添加供应商 -->
    <add-provider-dialog v-if="addProviderVisible" ref="addProviderRef" @save="addProviderSaveClick"></add-provider-dialog>
  </div>
</template>

<script>

import { couponSaveBasic, couponSaveRules, getCouponActivity } from '@/subject/pop/api/b2b_api/discount_coupon'
import { onInputLimit } from '@/common/utils'
import { enterpriseType } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'
import AddGoodsDialog from '../add_goods_dialog';
import AddProviderDialog from '../add_provider_dialog';

export default {
  components: {
    AddGoodsDialog,
    AddProviderDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    }
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      this.$common.log(rule)
      if (this.baseFormModel.type == 2) {
        if (value == 0 || value >= 100) {
          callback(new Error('折扣需>0且<100'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '优惠券管理'
        },
        {
          title: '优惠券列表'
        },
        {
          title: '编辑'
        }
      ],
      // 供应商
      addProviderVisible: false,
      addGoodsVisible: false,
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
      baseFormModel: {
        type: 1,
        name: '',
        budgetCode: '',
        threshold: 1,
        thresholdValue: '',
        discountValue: '',
        discountMax: '',
        platformRatio: '',
        businessRatio: '',
        useDateType: 1,
        validDate: [],
        expiryDays: '',
        remark: ''
      },
      baseRules: {
        type: [{ required: true, message: '请选择优惠券类型', trigger: 'change' }],
        name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
        budgetCode: [{ required: true, message: '请输入预算编号', trigger: 'blur' }],
        threshold: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
        thresholdValue: [{ required: true, message: '请输入', trigger: 'blur' }],
        discountValue: [
          { required: true, message: '请输入', trigger: 'blur' }, 
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        platformRatio: [{ required: true, message: '请输入', trigger: 'blur' }],
        businessRatio: [{ required: true, message: '请输入', trigger: 'blur' }],
        useDateType: [{ required: true, message: '请选择有效期', trigger: 'change' }],
        validDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
        expiryDays: [{ required: true, message: '请输入', trigger: 'blur' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        platformLimit: 1,
        platformSelectedList: [],
        payMethodLimit: 1,
        payMethodSelectedList: [],
        coexistPromotionList: [],
        goodsLimit: 1,
        totalCount: '',
        status: 1,
        // 用户领取设置
        canGet: 1,
        activityTime: [],
        giveNum: '',
        conditionEnterpriseType: 1,
        // 部分企业
        conditionEnterpriseTypeValueList: [],
        conditionUserType: 1
      },
      useRules: {
        totalCount: [{ required: true, message: '请输入生券数量', trigger: 'blur' }],
        activityTime: [{ required: true, message: '请选择日期', trigger: 'change' }],
        giveNum: [{ required: true, message: '请输入', trigger: 'blur' }],
        conditionEnterpriseTypeValueList: [{ type: 'array', required: true, message: '请选择企业类型', trigger: 'change' }]
      }
    };
  },
  mounted() {
    this.$log(this.$route.params)
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    if (this.id && this.operationType ) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let data = await getCouponActivity(this.id)
      if (data) {
        let baseFormModel = {
          type: data.type,
          name: data.name,
          budgetCode: data.budgetCode,
          threshold: data.threshold,
          thresholdValue: data.thresholdValue,
          discountValue: data.discountValue,
          discountMax: data.discountMax,
          platformRatio: data.platformRatio,
          businessRatio: data.businessRatio,
          useDateType: data.useDateType,
          validDate: data.validDate || [],
          expiryDays: data.expiryDays,
          remark: data.remark
        }
        if (data.beginTime && data.beginTime > 0 && data.endTime && data.endTime > 0) {
          baseFormModel.validDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        let useRuleFormModel = {
          platformLimit: data.platformLimit,
          platformSelectedList: data.platformSelectedList,
          payMethodLimit: data.payMethodLimit,
          payMethodSelectedList: data.payMethodSelectedList,
          coexistPromotionList: data.coexistPromotionList,
          goodsLimit: data.goodsLimit,
          totalCount: data.totalCount,
          status: data.status,
          canGet: data.canGet,
          giveNum: data.giveNum,
          conditionEnterpriseType: data.conditionEnterpriseType,
          conditionEnterpriseTypeValueList: data.conditionEnterpriseTypeValueList,
          conditionUserType: data.conditionUserType,
          activityTime: data.activityTime || []
        }
        if (data.activityBeginTime && data.activityBeginTime > 0 && data.activityEndTime && data.activityEndTime > 0) {
          useRuleFormModel.activityTime = [formatDate(data.activityBeginTime), formatDate(data.activityEndTime)]
        }
        this.useRuleFormModel = useRuleFormModel
        // 复制进入 清空ID
        if (this.operationType == 4) {
          this.useRuleFormModel.status = 1
        }
      }
    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel
          // 让实付金额小于满减金额
          if (baseFormModel.type == 1) {
            if (Number(baseFormModel.thresholdValue) < Number(baseFormModel.discountValue)){
              this.$common.warn('实付金额需大于满减金额')
              return false
            }
          }
          if (baseFormModel.useDateType == 1) {
            baseFormModel.beginTime = baseFormModel.validDate && baseFormModel.validDate.length ? baseFormModel.validDate[0] : undefined
            baseFormModel.endTime = baseFormModel.validDate && baseFormModel.validDate.length > 1 ? baseFormModel.validDate[1] : undefined
          }
          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await couponSaveBasic(baseFormModel)
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
    // 优惠券类型切换
    typeChange() {
      this.baseFormModel.discountValue = ''
    },
    // 底部保存按钮点击
    saveClick() {
      if (this.id) {
        this.useRuleFormModel.id = this.id
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }

      if (this.baseFormModel.useDateType == 1) {
        if (!this.baseFormModel.validDate || this.baseFormModel.validDate.length == 0) {
          this.$common.warn('请先设置固定效期');
          return false
        }
      }

      this.$refs['useRuleForm'].validate(async (valid) => {
        if (valid) {
          
          let useRuleFormModel = this.useRuleFormModel

          if (useRuleFormModel.canGet == 1) {
            useRuleFormModel.activityBeginTime = useRuleFormModel.activityTime && useRuleFormModel.activityTime.length ? useRuleFormModel.activityTime[0] : undefined
            useRuleFormModel.activityEndTime = useRuleFormModel.activityTime && useRuleFormModel.activityTime.length > 1 ? useRuleFormModel.activityTime[1] : undefined

            // 券结束时间
            let currentEndTime = this.baseFormModel.validDate && this.baseFormModel.validDate.length > 1 ? this.baseFormModel.validDate[1] : undefined
            // 领取结束时间
            let activityEndTime = useRuleFormModel.activityEndTime
            if (currentEndTime && activityEndTime) {
              let currentEndTimeNum = new Date(currentEndTime.replace(/-/g, '/')).getTime()
              let activityEndTimeNum = new Date(activityEndTime.replace(/-/g, '/')).getTime()
              
              if (activityEndTimeNum > currentEndTimeNum) {
                this.$common.warn('优惠券的领取结束时间必须小于使用结束时间')
                return false
              }
            }

          }
          
          this.$common.showLoad()
          let data = await couponSaveRules(useRuleFormModel)
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
    // 设置供应商
    addProviderClick() {
      if (this.id) {
        this.addProviderVisible = true
        this.$nextTick( () => {
          this.$refs.addProviderRef.init(this.id, this.operationType)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
      
    },
    addProviderSaveClick() {
      
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
    // 弹框保存点击
    addGoodsSaveClick(parms) {
      this.$common.log(parms)
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
