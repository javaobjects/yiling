<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :disabled="operationType == 1" :model="baseFormModel" :rules="baseRules" ref="dataForm" label-position="left" label-width="120px" class="demo-ruleForm">
        <div class="top-bar">
          <div class="content-box my-form-box">
            <el-form-item label="领劵活动名称" prop="name">
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
            <el-form-item label="关联优惠券ID" required>
              <yl-table
                border
                show-header
                :list="baseFormModel.couponActivityAutoGetList"
                :total="0"
              >
                <el-table-column label="类型" min-width="80" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.memberType == 1 ? '商品优惠券' : '会员优惠券' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="优惠券ID" min-width="80" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.id }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="优惠券名称" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base">{{ row.name }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="剩余数量" min-width="80" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base">{{ row.surplusCount }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="最大领取数量" min-width="80" align="center" :render-header="addRedStar">
                  <template slot-scope="{ row, $index }">
                    <div class="operation-view">
                      <div class="option">
                        <el-input v-model="row.giveNum" @input="change($index, 1)"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="每人每天最大领取数量（0为不限制）" min-width="80" align="center">
                  <template slot-scope="{ row, $index }">
                    <div class="operation-view">
                      <div class="option">
                        <el-input v-model="row.giveNumDaily" @input="change($index, 2)"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" min-width="100" align="center">
                  <template slot-scope="{ $index }">
                    <div>
                      <yl-button type="text" @click="selectCouponClick">选择</yl-button>
                      <yl-button type="text" @click="removeClick($index)">删除</yl-button>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
              <yl-button class="mar-t-10" type="text" @click="selectCouponClick">添加优惠券</yl-button>
            </el-form-item>
            <el-form-item label="">
              <yl-button type="primary" @click="topSaveClick">保存</yl-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-form :model="useRuleFormModel" :rules="useRules" ref="useRuleForm" label-position="left" label-width="120px" class="demo-ruleForm">
        <div class="top-bar">
          <div class="content-box my-form-box">
            <el-form-item label="客户范围" prop="conditionEnterpriseRange">
              <el-radio-group v-model="useRuleFormModel.conditionEnterpriseRange">
                <el-radio :label="1" :disabled="running || operationType == 1">全部客户</el-radio>
                <el-radio :label="2" :disabled="running || operationType == 1">指定客户</el-radio>
                <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionEnterpriseRange == 2" type="text" @click="addProviderClick">设置</yl-button>
                <el-radio :label="3" :disabled="running || operationType == 1">指定客户范围</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 指定范围客户 -->
            <div class="section-view" v-if="useRuleFormModel.conditionEnterpriseRange == 3">
              <el-form-item label="企业类型" prop="conditionEnterpriseType">
                <el-radio-group v-model="useRuleFormModel.conditionEnterpriseType" :disabled="operationType == 1 || running">
                  <el-radio :label="1">全部企业类型</el-radio>
                  <el-radio :label="2">部分企业类型</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="useRuleFormModel.conditionEnterpriseType == 2" prop="conditionEnterpriseTypeValueList">
                <el-checkbox-group v-model="useRuleFormModel.conditionEnterpriseTypeValueList" :disabled="operationType == 1 || running" >
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
                  <el-radio :label="1" :disabled="running || operationType == 1">全部用户</el-radio>
                  <el-radio :label="2" :disabled="running || operationType == 1">普通用户</el-radio>
                  <el-radio :label="3" :disabled="running || operationType == 1">全部会员</el-radio>
                  <el-radio :label="7" :disabled="running || operationType == 1">指定会员方案</el-radio>
                  <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionUserType == 7" type="text" @click="addMemberClick">设置</yl-button>
                  <el-radio :label="8" :disabled="running || operationType == 1">指定会员推广方</el-radio>
                  <yl-button v-if="useRuleFormModel.conditionUserType == 8" type="text" @click="addMerchantClick">设置</yl-button>
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
    <!-- 指定客户 -->
    <get-add-provider-dialog v-if="addProviderVisible" ref="addProviderRef"></get-add-provider-dialog>
    <!-- 添加优惠券 -->
    <select-coupon-dialog v-if="selectCouponVisible" ref="selectCouponRef" @save="confirmClick"></select-coupon-dialog>
    <!-- 添加会员维度弹框 -->
    <add-member-dimension-dialog v-if="addMemberDimensionVisible" ref="addMemberDimensionRef"></add-member-dimension-dialog>
    <!-- 选择商家 -->
    <add-merchant-dialog v-if="addMerchantVisible" ref="addMerchantRef"></add-merchant-dialog>
  </div>
</template>

<script>
import { autoGiveGetDetail, autoGetSaveOrUpdateBasic, autoGetSaveOrUpdateRules } from '@/subject/admin/api/b2b_api/discount_coupon'
import { enterpriseType } from '@/subject/admin/utils/busi'
import GetAddProviderDialog from '../components/select_range/get_add_provider_dialog'
import AddMerchantDialog from '../components/select_range/add_merchant_dialog'
import SelectCouponDialog from '../components/select_coupon_dialog'
import AddMemberDimensionDialog from '../components/add_member_dimension_dialog'
import { formatDate } from '@/subject/admin/utils'
import { onInputLimit } from '@/common/utils'

export default {
  components: {
    GetAddProviderDialog,
    SelectCouponDialog,
    AddMemberDimensionDialog,
    AddMerchantDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    }
  },
  data() {
    return {
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      loading: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      baseFormModel: {
        name: '',
        time: [],
        couponActivityAutoGetList: []
      },
      baseRules: {
        name: [{ required: true, message: '请输入领劵活动名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        conditionEnterpriseRange: 1,
        conditionEnterpriseType: 1,
        conditionEnterpriseTypeValueList: [],
        // 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员 6-新客 7-指定方案会员 8-指定推广方）
        conditionUserType: 1
      },
      useRules: {
        conditionEnterpriseRange: [{ required: true, message: '请选择客户范围', trigger: 'change' }],
        conditionEnterpriseType: [{ required: true, message: '请选择指定企业类型', trigger: 'change' }],
        conditionEnterpriseTypeValueList: [{ type: 'array', required: true, message: '请选择企业类型', trigger: 'change' }],
        conditionUserType: [{ required: true, message: '请选择指定用户类型', trigger: 'change' }]
      },
      addProviderVisible: false,
      selectCouponVisible: false,
      addMemberDimensionVisible: false,
      addMerchantVisible: false
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
      let data = await autoGiveGetDetail(this.id)
      if (data) {
        this.running = data.running
        let couponActivityList = data.couponActivityList || []
        couponActivityList.forEach(item => {
          item.couponActivityId = item.id
        })

        let baseFormModel = {
          name: data.name,
          couponActivityAutoGetList: couponActivityList
        }
        if (data.beginTime && data.endTime) {
          baseFormModel.time = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        this.useRuleFormModel = data
      }
    },
    // 给表头加必填符号*
    addRedStar(h, { column }) {
      return [
        h('span', { style: 'color: red' }, '*'),
        h('span', ' ' + column.label)
      ];
    },
    change(index, type) {
      // 重新赋值
      let tmpObj = this.baseFormModel.couponActivityAutoGetList[index];
      if (type == 1) {
        tmpObj.giveNum = this.onInput(tmpObj.giveNum, 0, 2)
      } else if (type == 2) {
        tmpObj.giveNumDaily = this.onInput(tmpObj.giveNumDaily, 0, 2)
      }
      this.$set(this.baseFormModel.couponActivityAutoGetList, index, tmpObj);
    },
    // 删除
    removeClick(index) {
      let couponActivityAutoGetList = this.baseFormModel.couponActivityAutoGetList
      couponActivityAutoGetList.splice(index, 1)
      this.baseFormModel.couponActivityAutoGetList = couponActivityAutoGetList
    },
    // 选择优惠券点击
    selectCouponClick() {
      this.selectCouponVisible = true
      this.$nextTick( () => {
        let selectList = this.baseFormModel.couponActivityAutoGetList
        this.$refs.selectCouponRef.init(selectList)
      })
    },
    // 选择优惠券保存
    confirmClick(selectCouponList) {
      let selectList = this.baseFormModel.couponActivityAutoGetList

      selectCouponList.forEach((row) => {
        let hasIndex = selectList.findIndex(obj => {
          return row.id == obj.id;
        })
        if (hasIndex == -1){
          // eslint-disable-next-line no-new-object
          let newObj = new Object()
          newObj = this.$common.clone(row)
          newObj.couponActivityId = row.id
          newObj.giveNum = ''
          newObj.giveNumDaily = 0
          this.baseFormModel.couponActivityAutoGetList.push(newObj)
        }
      })

    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          if (this.checkInputData()) {
            let baseFormModel = this.baseFormModel

            baseFormModel.beginTime = baseFormModel.time && baseFormModel.time.length ? baseFormModel.time[0] : undefined
            baseFormModel.endTime = baseFormModel.time && baseFormModel.time.length > 1 ? baseFormModel.time[1] : undefined
            if (this.id) {
              baseFormModel.id = this.id
            }
            this.$common.showLoad()
            let data = await autoGetSaveOrUpdateBasic(baseFormModel)
            this.$common.hideLoad()
            if (data) {
              this.id = data
              this.$common.n_success('保存成功')
            } 
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
          if (useRuleFormModel.conditionEnterpriseRange == 3) {
            if (useRuleFormModel.conditionEnterpriseType == 1) {
              useRuleFormModel.conditionEnterpriseTypeValueList = []
            }
          } else {
            useRuleFormModel.conditionEnterpriseType = ''
            useRuleFormModel.conditionEnterpriseTypeValueList = []
            useRuleFormModel.conditionUserType = ''
          }
          this.$common.showLoad()
          let data = await autoGetSaveOrUpdateRules(useRuleFormModel)
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
    // 指定客户
    addProviderClick() {
      if (this.id) {
        this.addProviderVisible = true
        this.$nextTick( () => {
          this.$refs.addProviderRef.init(this.id, this.running, this.operationType)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
      
    },
    // 指定推广方会员
    addMerchantClick() {
      if (this.id) {
        this.addMerchantVisible = true
        this.$nextTick( () => {
          this.$refs.addMerchantRef.init(this.id, this.running, this.operationType)
        })
        
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }
    },
    // 添加会员维度弹框
    addMemberClick() {
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
      let selectList = this.baseFormModel.couponActivityAutoGetList
      let hasGiveNum = false
      if (!selectList.length) {
        this.$common.warn('请选择优惠券')
        return false
      }
      // 每人每天最大领取数据 小于  最大领取数量 
      let hasGiveNumDaily = false
      let couponId = -1
      for (let i = 0; i < selectList.length; i++) {
        let item = selectList[i]
        if (!item.giveNum || item.giveNum == 0) {
          hasGiveNum = true
        }
        if (item.giveNum && item.giveNum != 0 && item.giveNumDaily && item.giveNumDaily != 0 && Number(item.giveNumDaily) > Number(item.giveNum)) {
          hasGiveNumDaily = true
        }
        // 关联的优惠券是固定效期的，在保存时校验领取结束时间 必须小于 优惠券可使用的结束时间
        if (item.useDateType == 1) {
          // 领取结束时间
          let currentEndTime = this.baseFormModel.time && this.baseFormModel.time.length > 1 ? this.baseFormModel.time[1] : undefined
          if (currentEndTime) {
            let currentEndTimeNum = new Date(currentEndTime.replace(/-/g, '/')).getTime()
            // 券结束时间
            let endTime = item.endTime
            if (currentEndTimeNum > endTime) {
              couponId = item.id
              break
            }
          }
          
        }
      }
      if (couponId != -1) {
        this.$common.warn(`优惠券${couponId}的领取结束时间必须小于使用结束时间！`)
        return false
      }
      if (hasGiveNum) {
        this.$common.warn('请填写最大领取数量,不能为0')
        return false
      }
      if (hasGiveNumDaily) {
        this.$common.warn('每人每天最大领取数据 需小于 最大领取数量')
        return false
      }

      return true
    },
    // 校验两位小数
    onInput(value, limit, maxLength) {
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
