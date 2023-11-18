<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div>
        <el-form :disabled="operationType == 1" :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="110px" class="demo-ruleForm">
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>基本信息
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="优惠券名称" prop="name">
                <el-input :disabled="running" class="show-word-limit" v-model="baseFormModel.name" placeholder="请输入优惠券名称" maxlength="20" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="预算编号" prop="budgetCode">
                <el-input :disabled="running" v-model="baseFormModel.budgetCode" placeholder="请输入预算编号" maxlength="50"></el-input>
              </el-form-item>
              <el-form-item label="活动分类" prop="sponsorType">
                <el-select v-model="baseFormModel.sponsorType" placeholder="请选择" :disabled="running">
                  <el-option v-for="item in sponsorTypeArray" :key="item.value" :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="承担方" prop="bear">
                <el-radio-group v-model="baseFormModel.bear" :disabled="running">
                  <el-radio :label="1">平台承担</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="优惠券数量" prop="totalCount">
                <div class="input-form-item">
                  <el-input v-model="baseFormModel.totalCount" :disabled="running" @keyup.native="baseFormModel.totalCount = onInput(baseFormModel.totalCount, 0)"></el-input>
                </div>
              </el-form-item>
              <el-form-item label="运营备注">
                <el-input :disabled="running" class="show-word-limit" v-model="baseFormModel.remark" placeholder="请输入运营备注" maxlength="20" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="">
                <yl-button v-if="operationType != 1" type="primary" @click="topSaveClick">保存</yl-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 使用设置 -->
      <div class="font-size-large font-important-color mar-b-16">使用设置</div>
      <div >
        <el-form :model="useRuleFormModel" :rules="useRules" ref="useRuleForm" label-position="left" label-width="110px" class="demo-ruleForm">
          <!-- 适用范围 -->
          <div class="top-bar">
            <div class="header-bar header-renative">
              <div class="sign"></div>适用范围
            </div>
            <!-- 使用平台 -->
            <div class="content-box my-form-box">
              <el-form-item label="适用会员方案" prop="memberLimit">
                <el-radio-group v-model="useRuleFormModel.memberLimit">
                  <el-radio :label="1" :disabled="running || operationType == 1">全部会员方案</el-radio>
                  <el-radio :label="2" :disabled="running || operationType == 1">部分会员方案</el-radio>
                  <yl-button v-if="useRuleFormModel.memberLimit == 2" type="text" @click="addMemberClick">设置</yl-button>
                </el-radio-group>
              </el-form-item>
            </div>
            <!-- 使用规则 -->
            <div class="header-bar header-renative mar-t-10">
              <div class="sign"></div>使用规则
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="使用时间" prop="useDateType">
                <el-radio-group v-model="useRuleFormModel.useDateType" :disabled="running || operationType == 1">
                  <el-radio :label="1">固定效期</el-radio>
                  <el-radio :label="2">按发放/领取时间设定</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.useDateType == 1" prop="validDate">
                <div class="assume-form-item flex-row-left" >
                  <el-date-picker
                    v-model="useRuleFormModel.validDate"
                    :disabled="running || operationType == 1"
                    type="datetimerange"
                    format="yyyy/MM/dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']">
                  </el-date-picker>
                  <span class="tip-text"><span class="red-text">*</span>结束时间必须等于或晚于开始时间</span>
                </div>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.useDateType == 2" prop="expiryDays">
                <div class="assume-form-item">
                  发放/领取日后
                  <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.expiryDays" @keyup.native="useRuleFormModel.expiryDays = onInput(useRuleFormModel.expiryDays, 0)"></el-input>
                  天失效<span class="tip-text"><span class="red-text">*</span>发放/领取日+X天（23:59:59）优惠券失效</span>
                </div>
              </el-form-item>
              <!-- 优惠规则 -->
              <el-form-item label="优惠规则" prop="type">
                <el-radio-group v-model="useRuleFormModel.type" :disabled="running || operationType == 1" @change="typeChange">
                  <el-radio :label="1">满减</el-radio>
                  <el-radio :label="2">满折</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- 优惠规则 -->
              <el-form-item label="" v-if="useRuleFormModel.type == 1">
                <div class="activity-form-item flex-row-left">
                  减
                  <el-form-item label="" prop="discountValue">
                    <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.discountValue" @keyup.native="useRuleFormModel.discountValue = onInput(useRuleFormModel.discountValue, 2)"></el-input>
                  </el-form-item>
                  元
                  <span class="tip-text"><span class="red-text">*</span>数字项必须填写，支持小数点后两位</span>
                </div>
              </el-form-item>
              <el-form-item label="" required v-if="useRuleFormModel.type == 2">
                <div class="activity-form-item flex-row-left">
                  打
                  <el-form-item label="" prop="discountValue">
                    <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.discountValue" @keyup.native="useRuleFormModel.discountValue = onInput(useRuleFormModel.discountValue, 2)"></el-input>
                  </el-form-item>
                  %折 
                  最高优惠
                  <el-form-item label="" prop="discountMax">
                    <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.discountMax" @keyup.native="useRuleFormModel.discountMax = onInput(useRuleFormModel.discountMax, 0)"></el-input>
                  </el-form-item>
                  <span class="tip-text"><span class="red-text">*</span>如打8折，填写80 最高优惠0为不限制</span>
                </div>
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
    <!-- 添加会员弹框 -->
    <add-member-dialog v-if="addMemberVisible" ref="addMemberRef"></add-member-dialog>
  </div>
</template>

<script>
import { memberCouponSaveOrUpdateBasic, memberCouponGetCouponActivity } from '@/subject/admin/api/b2b_api/discount_coupon'
import AddMemberDialog from '../components/add_member_dialog';
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { isNumLength } from '@/subject/admin/utils/rules'

export default {
  components: {
    AddMemberDialog
  },
  computed: {
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      if (this.useRuleFormModel.type == 2) {
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
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 添加会员弹框
      addMemberVisible: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增 4-复制
      operationType: 2,
      id: '160',
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
        name: '',
        // 预算编号
        budgetCode: '',
        // 活动分类
        sponsorType: '',
        // 费用承担方（1-平台；2-商家；3-共同承担）
        bear: 1,
        // 惠券数量
        totalCount: '',
        remark: ''
      },
      baseRules: {
        name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
        budgetCode: [{ required: true, message: '请输入预算编号', trigger: 'blur' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        bear: [{ required: true, message: '请选择费用承担方', trigger: 'change' }],
        totalCount: [
          { required: true, validator: isNumLength, trigger: 'blur', isNotAllowZero: true, maxLength: 8 }
        ]
      },
      // 使用规则设置
      useRuleFormModel: {
        // 1全部会员方案可用，2部分可用
        memberLimit: 1,
        // 1-固定时间；2-发放领取后生效
        useDateType: 1,
        validDate: [],
        expiryDays: '',
        type: 1,
        discountValue: '',
        discountMax: ''
      },
      useRules: {
        memberLimit: [{ required: true, message: '请选择适用会员方案', trigger: 'change' }],
        useDateType: [{ required: true, message: '请选择使用时间', trigger: 'change' }],
        validDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
        expiryDays: [{ required: true, message: '请输入', trigger: 'blur' }],
        type: [{ required: true, message: '请选择优惠规则', trigger: 'change' }],
        discountValue: [
          { required: true, validator: isNumLength, trigger: 'blur', isNotAllowZero: true, maxLength: 10 },
          { required: true, validator: checkLimitNum, trigger: 'blur' }
        ],
        discountMax: [
          { required: true, validator: isNumLength, trigger: 'blur', isNotAllowZero: false, maxLength: 8 }
        ]
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
      let data = await memberCouponGetCouponActivity(this.id)
      if (data) {
        this.running = data.running
        this.editNameFlag = data.editNameFlag
        this.editLimitFlag = data.editLimitFlag
        let baseFormModel = {
          name: data.name,
          budgetCode: data.budgetCode,
          sponsorType: data.sponsorType,
          bear: data.bear,
          totalCount: data.totalCount,
          remark: data.remark 
        }
        this.baseFormModel = baseFormModel

        let useRuleFormModel = {
          memberLimit: data.memberLimit,
          useDateType: data.useDateType,
          expiryDays: data.expiryDays,
          type: data.type,
          discountValue: data.discountValue,
          discountMax: data.discountMax,
          status: data.status
        }
        if (data.beginTime && data.beginTime > 0 && data.endTime && data.endTime > 0) {
          useRuleFormModel.validDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }

        this.useRuleFormModel = useRuleFormModel
        if (this.operationType == 4) {
          this.running = false
          this.useRuleFormModel.status = 1
        }
      }
    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel
          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await memberCouponSaveOrUpdateBasic(baseFormModel)
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
      this.useRuleFormModel.discountValue = ''
      this.useRuleFormModel.discountMax = ''
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

          // 1-固定时间；2-发放领取后生效
          if (useRuleFormModel.useDateType == 1) {
            useRuleFormModel.beginTime = useRuleFormModel.validDate && useRuleFormModel.validDate.length ? useRuleFormModel.validDate[0] : undefined
            useRuleFormModel.endTime = useRuleFormModel.validDate && useRuleFormModel.validDate.length > 1 ? useRuleFormModel.validDate[1] : undefined
            useRuleFormModel.expiryDays = '' 
          } else {
            useRuleFormModel.beginTime = ''
            useRuleFormModel.endTime = ''
          }
          // 1-满减 2-满折
          if (useRuleFormModel.type == 1) {
            useRuleFormModel.discountMax = ''
          } 
          this.$common.showLoad()
          let data = await memberCouponSaveOrUpdateBasic(useRuleFormModel)
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
    // 添加会员弹框
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
    addProviderSaveClick() {
      
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
