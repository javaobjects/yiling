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
                <el-input class="show-word-limit" v-model="baseFormModel.name" maxlength="20" show-word-limit placeholder="请输入活动名称"></el-input>
              </el-form-item>
              <el-form-item label="活动分类" prop="sponsorType">
                <el-select v-model="baseFormModel.sponsorType" placeholder="请选择">
                  <el-option label="平台活动" :value="1"></el-option>
                  <el-option label="商家活动" :value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="承担方" prop="bear">
                <el-select v-model="baseFormModel.bear" placeholder="请选择">
                  <el-option label="平台承担" :value="1"></el-option>
                  <el-option label="商家承担" :value="2"></el-option>
                  <el-option label="共同承担" :value="3"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="" required v-if="baseFormModel.bear == 3">
                <div class="assume-form-item flex-row-left">平台承担比例 
                  <el-form-item label="" prop="platformRatio">
                    <el-input v-model="baseFormModel.platformRatio" @keyup.native="baseFormModel.platformRatio = onInput(baseFormModel.platformRatio, 2)" class="input-box"></el-input>
                  </el-form-item>
                  % 商家承担比例
                  <el-form-item label="" prop="businessRatio">
                    <el-input v-model="baseFormModel.businessRatio" @keyup.native="baseFormModel.businessRatio = onInput(baseFormModel.businessRatio, 2)" class="input-box"></el-input>
                  </el-form-item>
                  %<span class="tip-text"><span class="red-text">*</span>比例为正数，支持小数点后两位，商家承担比例+平台承担比例=100%</span>
                </div>
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
              <el-form-item label="运营备注">
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
              <div class="sign"></div>适用范围
            </div>
            <!-- 适用范围 -->
            <div class="content-box my-form-box">
              <el-form-item label="适用平台" prop="platformSelected">
                <el-checkbox-group v-model="useRuleFormModel.platformSelected" :disabled="running || operationType == 1">
                    <el-checkbox label="1">B2B</el-checkbox>
                    <el-checkbox label="2" disabled>销售助手</el-checkbox>
                  </el-checkbox-group>
              </el-form-item>
              <el-form-item label="客户范围" prop="conditionBuyerType">
                <el-radio-group v-model="useRuleFormModel.conditionBuyerType">
                  <el-radio :label="1" :disabled="running || operationType == 1">全部客户</el-radio>
                  <el-radio :label="2" :disabled="running || operationType == 1">指定客户</el-radio>
                  <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionBuyerType == 2" type="text" @click="addClientClick">设置 {{ useRuleFormModel.buyerNum ? `(${useRuleFormModel.buyerNum})` : '' }}</yl-button>
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
                        :label="item.value + ''"
                      >{{ item.label }}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="用户类型" prop="conditionUserType">
                  <el-radio-group v-model="useRuleFormModel.conditionUserType">
                    <el-radio :label="1" :disabled="running || operationType == 1">全部用户</el-radio>
                    <el-radio :label="2" :disabled="running || operationType == 1">普通用户</el-radio>
                    <el-radio :label="3" :disabled="running || operationType == 1">全部会员</el-radio>
                    <el-radio :label="4" :disabled="running || operationType == 1">指定会员方案</el-radio>
                    <yl-button class="mar-r-16" v-if="useRuleFormModel.conditionUserType == 4" type="text" @click="addMemberDimensionClick">设置 {{ useRuleFormModel.memberNum ? `(${useRuleFormModel.memberNum})` : '' }}</yl-button>
                    <el-radio :label="5" :disabled="running || operationType == 1">指定会员推广方</el-radio>
                    <yl-button v-if="useRuleFormModel.conditionUserType == 5" type="text" @click="addProviderClick">设置 {{ useRuleFormModel.promoterNnm ? `(${useRuleFormModel.promoterNnm})` : '' }}</yl-button>
                  </el-radio-group>
                </el-form-item>
              </div>
              <el-form-item label="其他">
                <el-checkbox-group v-model="useRuleFormModel.conditionOther" :disabled="running || operationType == 1">
                  <el-checkbox label="1">新客适用</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>
            <div class="header-bar header-renative">
              <div class="sign"></div>预售规则
            </div>
            <div class="content-box my-form-box">
              <div class="explain-view mar-b-16">操作提示：1.商品预售与正常销售同时存在。2.商品预售不受当前库存数量限制。</div>
              <el-form-item label="预售类型" prop="presaleType">
                <el-radio-group v-model="useRuleFormModel.presaleType" :disabled="running || operationType == 1" @change="presaleTypeChange">
                  <el-radio :label="1">定金预售</el-radio>
                  <el-radio :label="2">全款预售</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="支付定金时间" prop="depositDate" v-if="useRuleFormModel.presaleType == 1">
                <el-date-picker
                  disabled
                  :picker-options="pickerOptions"
                  v-model="useRuleFormModel.depositDate"
                  type="datetimerange"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :default-time="['00:00:00', '23:59:59']">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="支付尾款时间" prop="finalPayDate" v-if="useRuleFormModel.presaleType == 1">
                <el-date-picker
                  :picker-options="pickerOptions"
                  v-model="useRuleFormModel.finalPayDate"
                  type="datetimerange"
                  format="yyyy/MM/dd HH:mm:ss"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :default-time="['00:00:00', '23:59:59']">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="预售商品" required class="table-item">
                <yl-table
                  border
                  show-header
                  :list="useRuleFormModel.presaleGoodsLimitForms.slice((query.page - 1) * query.limit, query.page * query.limit)"
                  :total="query.total"
                  :page.sync="query.page"
                  :limit.sync="query.limit"
                  @getList="listPageListMethod"
                >
                  <el-table-column label="序号" min-width="50" align="center">
                    <template slot-scope="{ $index }">
                      <div class="font-size-base">{{ (query.page - 1) * query.limit + $index + 1 }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品编号" min-width="80" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.goodsId }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品名称" min-width="200" align="center">
                    <template slot-scope="{ row }">
                      <div class="font-size-base">{{ row.goodsName }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column label="规格" min-width="162" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.sellSpecifications }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="生产厂家" min-width="200" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.manufacturer }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="商家" min-width="200" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.ename }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="销售价" min-width="60" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.price }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="可用库存" min-width="80" align="center">
                    <template slot-scope="{ row }">
                      <div class="goods-desc">
                        <div class="font-size-base">{{ row.goodsInventory }}</div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="预售价" min-width="120" align="center" :render-header="addRedStar">
                    <template slot-scope="scope">
                      <el-form-item label="" :prop="'presaleGoodsLimitForms.' + scope.$index + '.presaleAmount'" :rules="useRules.presaleAmount">
                        <el-input v-model="scope.row.presaleAmount" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + scope.$index, 1)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="定金比例(%)" min-width="100" align="center" :render-header="addRedStar">
                    <template slot-scope="scope">
                      <el-form-item label="" :prop="'presaleGoodsLimitForms.' + scope.$index + '.depositRatio'" :rules="useRules.depositRatio">
                        <el-input v-model="scope.row.depositRatio" :disabled="running || operationType == 1 || useRuleFormModel.presaleType == 2" @input="change((query.page - 1) * query.limit + scope.$index, 2)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="促销方式" min-width="140" align="center" :render-header="addRedStar">
                    <template slot-scope="scope">
                      <el-form-item label="">
                        <el-select v-model="scope.row.presaleType" placeholder="请选择" :disabled="running || operationType == 1 || useRuleFormModel.presaleType == 2" @change="change((query.page - 1) * query.limit + scope.$index, 3)">
                          <el-option label="无" :value="0"></el-option>
                          <el-option label="定金膨胀" :value="1"></el-option>
                          <el-option label="尾款立减" :value="2"></el-option>
                        </el-select>
                      </el-form-item>
                  </template></el-table-column>
                  <el-table-column label="定金膨胀倍数" min-width="140" align="center" :render-header="addRedStar">
                    <template slot-scope="scope">
                      <el-form-item v-if="useRuleFormModel.presaleType == 1 && scope.row.presaleType == 1" label="" :prop="'presaleGoodsLimitForms.' + scope.$index + '.expansionMultiplier'" :rules="useRules.expansionMultiplier">
                        <el-input v-model="scope.row.expansionMultiplier" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + scope.$index, 4)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="尾款立减金额" min-width="140" align="center" :render-header="addRedStar">
                    <template slot-scope="scope">
                      <el-form-item v-if="useRuleFormModel.presaleType == 1 && scope.row.presaleType == 2" label="" :prop="'presaleGoodsLimitForms.' + scope.$index + '.finalPayDiscountAmount'" :rules="useRules.finalPayDiscountAmount">
                        <el-input v-model="scope.row.finalPayDiscountAmount" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + scope.$index, 5)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="最小预定量 (0为不限制)" min-width="90" align="center">
                    <template slot-scope="{ row, $index }">
                      <el-form-item label="">
                        <el-input v-model="row.minNum" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + $index, 6)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="每人最大预定量 (0为不限制)" min-width="100" align="center">
                    <template slot-scope="{ row, $index }">
                      <el-form-item label="">
                        <el-input v-model="row.maxNum" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + $index, 7)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="合计最大预定量 (0为不限制)" min-width="100" align="center">
                    <template slot-scope="{ row, $index }">
                      <el-form-item label="">
                        <el-input v-model="row.allNum" :disabled="running || operationType == 1" @input="change((query.page - 1) * query.limit + $index, 8)"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" min-width="100" align="center">
                    <template slot-scope="{ $index }">
                      <div>
                        <yl-button type="text" :disabled="running || operationType == 1" @click="removeGiftClick((query.page - 1) * query.limit + $index)">删除</yl-button>
                      </div>
                    </template>
                  </el-table-column>
                </yl-table>
                <yl-button class="mar-t-10" type="text" @click="addGoodsClick">添加商品</yl-button>
              </el-form-item>
            </div>
            <!-- 临界控制 -->
            <div class="header-bar header-renative mar-t-16">
              <div class="sign"></div>临界控制
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="" label-width="0">
                <div class="activity-form-item flex-row-left">
                  <el-form-item label="预算金额">
                    <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.budgetAmount" @keyup.native="useRuleFormModel.budgetAmount = onInput(useRuleFormModel.budgetAmount, 2)"></el-input>
                  </el-form-item>
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
    <!-- 添加预售商品 -->
    <add-enterprise-goods-dialog v-if="addEnterpriseGoodsVisible" ref="addEnterpriseGoodsRef" @addGoodsSave="addGoodsSave"></add-enterprise-goods-dialog>
    <!-- 指定客户 -->
    <add-client-dialog v-if="addClientVisible" ref="addClientRef" @selectNumChange="buyerNumSelectNum"></add-client-dialog>
    <!-- 指定会员方案-会员维度 -->
    <add-member-dimension-dialog v-if="addMemberDimensionVisible" ref="addMemberDimensionRef" @selectNumChange="memberSelectNum"></add-member-dimension-dialog>
    <!-- 指定会员推广方 -->
    <add-provider-dialog v-if="addProviderVisible" ref="addProviderRef" @selectNumChange="promoterNnmSelectNum"></add-provider-dialog>
  </div>
</template>

<script>
import { presaleActivitySave, presaleActivitySaveAll, presaleActivityQueryDetail } from '@/subject/admin/api/view_marketing/goods_presell'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { enterpriseType } from '@/subject/admin/utils/busi'

// 预售商品
import AddEnterpriseGoodsDialog from '../components/add_enterprise_goods_dialog'
// 指定客户
import AddClientDialog from '../components/add_client_dialog'
// 指定会员方案
import AddMemberDimensionDialog from '../components/add_member_dimension_dialog'
// 指定会员推广方
import AddProviderDialog from '../components/add_provider_dialog'

export default {
  components: {
    AddEnterpriseGoodsDialog,
    AddClientDialog,
    AddMemberDimensionDialog,
    AddProviderDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    }
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      if (value == 0) {
        callback(new Error('大于0'));
      } else {
        callback();
      }
    };
    var checkDepositRatio = (rule, value, callback) => {
      if (value <= 0 || value >= 100) {
        if (this.useRuleFormModel.presaleType == 2) {
          callback();
        } else {
          callback(new Error('大于0小于100'));
        }
      } else {
        callback();
      }
    };
    var checkExpansionMultiplier = (rule, value, callback) => {
      if (value <= 1) {
        callback(new Error('大于1'));
      } else {
        callback();
      }
    };
    // 支付尾款时间 
    var checkFinalPayDate = (rule, value, callback) => {
      this.$log('checkFinalPayDate:', value)
      if (value.length != 2) {
        callback(new Error('请选择正确的时间'));
      } else {
        if (this.baseFormModel.validDate.length != 2) {
          callback();
        } else {
          let finalPayBeginTime = value[0]
          let finalPayEndTime = value[1]
          if (this.baseFormModel.validDate[0] && (new Date(finalPayBeginTime)).getTime() <= (new Date(this.baseFormModel.validDate[0])).getTime()) {
            callback(new Error('支付尾款的开始时间 需大于 活动开始时间'));
          }
          if (this.baseFormModel.validDate[1] && (new Date(finalPayEndTime)).getTime() <= (new Date(this.baseFormModel.validDate[1])).getTime()) {
            callback(new Error('支付尾款的结束时间  需大于  活动结束时间'));
          }
        }
        
        callback();
      }
      
    };
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().getTime() - 86400000; 
        }
      },
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增 4-复制
      operationType: 2,
      id: '',
      // 添加预售商品
      addEnterpriseGoodsVisible: false,
      // 指定客户
      addClientVisible: false,
      addMemberDimensionVisible: false,
      addProviderVisible: false,
      baseFormModel: {
        name: '',
        sponsorType: 1,
        bear: 1,
        platformRatio: '',
        businessRatio: '',
        validDate: [],
        operatingRemark: ''
      },
      baseRules: {
        name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        bear: [{ required: true, message: '请选择承担方', trigger: 'change' }],
        platformRatio: [{ required: true, message: '请输入', trigger: 'blur' }],
        businessRatio: [{ required: true, message: '请输入', trigger: 'blur' }],
        validDate: [{ required: true, message: '请选择生效时间', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        // 选择平台（1-B2B；2-销售助手)
        platformSelected: [],
        // 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
        conditionBuyerType: '',
        // 指定企业类型(1:全部类型 2:指定类型)
        conditionEnterpriseType: '',
        // 指定企业类型值
        conditionEnterpriseTypeValue: [],
        // 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）
        conditionUserType: '',
        // 新客适用
        conditionOther: [],
        // 预售类型（1-定金预售；2-全款预售)
        presaleType: 1,
        // 支付定金时间
        depositDate: [],
        // 支付尾款时间
        finalPayDate: [],
        // 商品信息
        presaleGoodsLimitForms: [],
        // 预算金额
        budgetAmount: '',
        // 指定客户数量
        buyerNum: 0,
        memberNum: 0,
        promoterNnm: 0
      },
      useRules: {
        platformSelected: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        conditionBuyerType: [{ required: true, message: '请选择客户范围', trigger: 'change' }],
        conditionEnterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        conditionEnterpriseTypeValue: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }],
        conditionUserType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        presaleType: [{ required: true, message: '请选择预售类型', trigger: 'change' }],
        depositDate: [{ required: true, message: '请选择支付定金时间', trigger: 'change' }],
        finalPayDate: [
          { required: true, message: '请选择支付尾款时间', trigger: 'change' },
          { validator: checkFinalPayDate, trigger: 'change' }
        ],
        // 预售商品
        presaleAmount: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        // 定金比例
        depositRatio: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkDepositRatio, trigger: 'blur' }
        ],
        // 定金膨胀倍数
        expansionMultiplier: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkExpansionMultiplier, trigger: 'blur' }
        ],
        // 尾款立减金额
        finalPayDiscountAmount: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ]
      },
      // 商品分页
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0
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
      let data = await presaleActivityQueryDetail(this.id)
      if (data) {
        this.running = data.running
        
        let baseFormModel = {
          name: data.name,
          sponsorType: data.sponsorType,
          bear: data.bear,
          platformRatio: data.platformRatio || '',
          businessRatio: data.businessRatio || '',
          operatingRemark: data.operatingRemark
        }
        if (data.beginTime && data.beginTime > 0 && data.endTime && data.endTime > 0) {
          baseFormModel.validDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        this.baseFormModel = baseFormModel

        let useRuleFormModel = {
          platformSelected: data.platformSelected,
          conditionBuyerType: data.conditionBuyerType || '',
          conditionEnterpriseType: data.conditionEnterpriseType || '',
          conditionEnterpriseTypeValue: data.conditionEnterpriseTypeValue,
          conditionUserType: data.conditionUserType || '',
          conditionOther: data.conditionOther,
          presaleType: data.presaleType || 1,
          presaleGoodsLimitForms: data.presaleGoodsLimitForms,
          budgetAmount: data.budgetAmount || '',
          // 指定客户数量
          buyerNum: data.buyerNum,
          memberNum: data.memberNum,
          promoterNnm: data.promoterNnm
        }
        // 支付定金时间
        if (data.depositBeginTime && data.depositBeginTime > 0 && data.depositEndTime && data.depositEndTime > 0) {
          useRuleFormModel.depositDate = [formatDate(data.depositBeginTime), formatDate(data.depositEndTime)]
        } else {
          useRuleFormModel.depositDate = [formatDate(data.beginTime), formatDate(data.endTime)]
        }
        // 支付尾款时间
        if (data.finalPayBeginTime && data.finalPayBeginTime > 0 && data.finalPayEndTime && data.finalPayEndTime > 0) {
          useRuleFormModel.finalPayDate = [formatDate(data.finalPayBeginTime), formatDate(data.finalPayEndTime)]
        }

        this.useRuleFormModel = useRuleFormModel

        if (this.operationType == 4) {
          this.running = false
          this.baseFormModel.validDate = []
          this.baseFormModel.startTime = ''
          this.baseFormModel.endTime = ''

          this.useRuleFormModel.depositDate = []
          this.useRuleFormModel.depositBeginTime = ''
          this.useRuleFormModel.depositEndTime = ''
        }
      }
    },
    // 客户数量
    buyerNumSelectNum(num) {
      this.useRuleFormModel.buyerNum = num
    },
    // 会员方案数量
    memberSelectNum(num) {
      this.useRuleFormModel.memberNum = num
    },
    // 会员推广方数量
    promoterNnmSelectNum(num) {
      this.useRuleFormModel.promoterNnm = num
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
    // 指定会员推广方
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
    // 给表头加必填符号*
    addRedStar(h, { column }) {
      return [
        h('span', { style: 'color: red' }, '*'),
        h('span', ' ' + column.label)
      ];
    },
    // 预售类型切换
    presaleTypeChange(type) {
      let selectGoodsList = this.useRuleFormModel.presaleGoodsLimitForms
      if (type == 1) {
        for (let i = 0; i < selectGoodsList.length; i++) {
          let goodsItem = selectGoodsList[i]
          if (goodsItem.depositRatio) {
            // 定金比例
            goodsItem.depositRatio = ''
            this.$set(this.useRuleFormModel.presaleGoodsLimitForms, i, goodsItem)
          }
        }
      } else if (type == 2) {
        for (let i = 0; i < selectGoodsList.length; i++) {
          let goodsItem = selectGoodsList[i]
          if (goodsItem.depositRatio != '100' || goodsItem.presaleType != 0 || goodsItem.expansionMultiplier || goodsItem.finalPayDiscountAmount) {
            // 定金比例
            goodsItem.depositRatio = '100'
            // 促销方式：0-无 1-定金膨胀 2-尾款立减
            goodsItem.presaleType = 0
            // 定金膨胀倍数
            goodsItem.expansionMultiplier = ''
            // 尾款立减金额
            goodsItem.finalPayDiscountAmount = ''
            this.$set(this.useRuleFormModel.presaleGoodsLimitForms, i, goodsItem)
          }
        }
      }
    },
    // 添加预售商品
    addGoodsClick() {
      this.addEnterpriseGoodsVisible = true
      let selectGoodsList = this.$common.clone(this.useRuleFormModel.presaleGoodsLimitForms)
      this.$nextTick( () => {
        this.$refs.addEnterpriseGoodsRef.init(selectGoodsList, this.running, this.operationType)
      })
    },
    // 删除预售商品
    removeGiftClick(index) {
      let selectGoodsList = this.useRuleFormModel.presaleGoodsLimitForms
      selectGoodsList.splice(index, 1)
      this.query.total = selectGoodsList.length
    },
    // 奖品设置
    change(index, operateType) {
      this.$log(index, operateType)
      // 重新赋值
      let tmpObj = this.useRuleFormModel.presaleGoodsLimitForms[index];
      if (operateType == 1) {
        tmpObj.presaleAmount = this.onInput(tmpObj.presaleAmount, 2, 8)
      } else if (operateType == 2) {
        tmpObj.depositRatio = this.onInput(tmpObj.depositRatio, 2)
      } else if (operateType == 3) {
        // tmpObj.presaleType = tmpObj.presaleType
      } else if (operateType == 4) {
        tmpObj.expansionMultiplier = this.onInput(tmpObj.expansionMultiplier, 2, 8)
      } else if (operateType == 5) {
        tmpObj.finalPayDiscountAmount = this.onInput(tmpObj.finalPayDiscountAmount, 2, 8)
      } else if (operateType == 6) {
        tmpObj.minNum = this.onInput(tmpObj.minNum, 0, 8)
      } else if (operateType == 7) {
        tmpObj.maxNum = this.onInput(tmpObj.maxNum, 0, 8)
      } else if (operateType == 8) {
        tmpObj.allNum = this.onInput(tmpObj.allNum, 0, 8)
      }
      this.$set(this.useRuleFormModel.presaleGoodsLimitForms, index, tmpObj);
    },
    addGoodsSave(goodsList) {
      let selectGoodsList = this.useRuleFormModel.presaleGoodsLimitForms
      for (let i = 0; i < goodsList.length; i++) {
        let goodsItem = goodsList[i]
        // 预售价
        goodsItem.presaleAmount = ''
        // 定金比例
        if (this.useRuleFormModel.presaleType == 1) {
          goodsItem.depositRatio = ''
        } else if (this.useRuleFormModel.presaleType == 2) {
          goodsItem.depositRatio = '100'
        }
        // 促销方式：0-无 1-定金膨胀 2-尾款立减
        goodsItem.presaleType = 0
        // 定金膨胀倍数
        goodsItem.expansionMultiplier = ''
        // 尾款立减金额
        goodsItem.finalPayDiscountAmount = ''
        // 最小预定量（0为不限制)
        goodsItem.minNum = 0
        // 每人最大预定量（0为不限制)
        goodsItem.maxNum = 0
        // 合计最大预定量（0为不限制
        goodsItem.allNum = 0
        
        selectGoodsList.push(goodsItem)
      }
      this.query.total = selectGoodsList.length
    },
    listPageListMethod() {

    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel

          // 处理共同承担100%
        if (baseFormModel.bear == 3) {
          let totalRatio = 0
          totalRatio = this.$common.add(Number(baseFormModel.platformRatio), Number(baseFormModel.businessRatio))
          if (totalRatio != 100){
            this.$common.warn('商家承担比例+平台承担比例=100%')
            return false
          }
        } else {
          baseFormModel.platformRatio = ''
          baseFormModel.businessRatio = ''
        }

          baseFormModel.beginTime = baseFormModel.validDate && baseFormModel.validDate.length ? baseFormModel.validDate[0] : undefined
          baseFormModel.endTime = baseFormModel.validDate && baseFormModel.validDate.length > 1 ? baseFormModel.validDate[1] : undefined

          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await presaleActivitySave(baseFormModel)
          this.$common.hideLoad()
          if (data) {
            this.id = data.id
            this.useRuleFormModel.depositDate = this.baseFormModel.validDate
            this.$common.n_success('基本信息保存成功')
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

            if (useRuleFormModel.conditionBuyerType != 3) {
              useRuleFormModel.conditionEnterpriseType = ''
              useRuleFormModel.conditionEnterpriseTypeValue = []
              useRuleFormModel.conditionUserType = ''
            }
            // 指定企业类型(1:全部类型 2:指定类型)
            if (useRuleFormModel.conditionEnterpriseType == 1) {
              useRuleFormModel.conditionEnterpriseTypeValue = []
            }
            // 预售类型（1-定金预售；2-全款预售）
            if (useRuleFormModel.presaleType == 1) {
              useRuleFormModel.depositBeginTime = useRuleFormModel.depositDate && useRuleFormModel.depositDate.length ? useRuleFormModel.depositDate[0] : undefined
              useRuleFormModel.depositEndTime = useRuleFormModel.depositDate && useRuleFormModel.depositDate.length > 1 ? useRuleFormModel.depositDate[1] : undefined

              useRuleFormModel.finalPayBeginTime = useRuleFormModel.finalPayDate && useRuleFormModel.finalPayDate.length ? useRuleFormModel.finalPayDate[0] : undefined
              useRuleFormModel.finalPayEndTime = useRuleFormModel.finalPayDate && useRuleFormModel.finalPayDate.length > 1 ? useRuleFormModel.finalPayDate[1] : undefined
            }
            
            this.$common.showLoad()
            let data = await presaleActivitySaveAll(useRuleFormModel)
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
    // 校验数据
    checkInputData() {

      if (this.operationType == 4) {
        if (!this.baseFormModel.beginTime || !this.baseFormModel.endTime) {
          this.$common.warn('请先保存基本信息')
          return false
        }
      }

      if (this.useRuleFormModel.conditionBuyerType == 2 && !this.useRuleFormModel.buyerNum) {
        this.$common.warn('设置指定客户')
        return false
      }

      if (this.useRuleFormModel.conditionBuyerType == 3 && this.useRuleFormModel.conditionUserType == 4 && !this.useRuleFormModel.memberNum) {
        this.$common.warn('设置指定会员方案')
        return false
      }

      if (this.useRuleFormModel.conditionBuyerType == 3 && this.useRuleFormModel.conditionUserType == 5 && !this.useRuleFormModel.promoterNnm) {
        this.$common.warn('设置指定会员推广方')
        return false
      }

      // 预售价
      let hasPresaleAmount = false
      // 定金比例
      let hasDepositRatio = false
      // 定金膨胀倍数
      let hasInputExpansionMultiplier = false
      // 尾款立减金额
      let hasInputFinalPayDiscountAmount = false
      // 定金膨胀比例已经超过原价
      let hasExpansionMultiplier = false
      // 尾款立减金额超过尾款金额
      let hasFinalPayDiscountAmount = false
      // 	合计最大预定量应控制大于每人最大预定量
      let hasCorrectAllNum = false

      let selectGoodsList = this.useRuleFormModel.presaleGoodsLimitForms
      if (!selectGoodsList || selectGoodsList.length == 0) {
        this.$common.warn('请添加预售商品')
        return false
      } else {
        
        for (let i = 0; i < this.useRuleFormModel.presaleGoodsLimitForms.length; i++) {
          let goodsItem = this.useRuleFormModel.presaleGoodsLimitForms[i]

          if (!goodsItem.presaleAmount || goodsItem.presaleAmount == 0) {
            hasPresaleAmount = true
          }
          // 定金预售
          if (this.useRuleFormModel.presaleType == 1) {
            if (goodsItem.presaleType == 1 || goodsItem.presaleType == 2) {

              if (!goodsItem.depositRatio || goodsItem.depositRatio == 0 || goodsItem.depositRatio >= 100) {
                hasDepositRatio = true
              }

              // 定金金额
              let itemDepositAmount = this.$common.mul(Number(goodsItem.presaleAmount), Number(goodsItem.depositRatio))
              itemDepositAmount = this.$common.div(Number(itemDepositAmount), 100)
              if (goodsItem.presaleType == 1) {

                if (!goodsItem.expansionMultiplier || goodsItem.expansionMultiplier <= 1) {
                  hasInputExpansionMultiplier = true
                }
                
                let price = this.$common.mul(Number(itemDepositAmount), Number(goodsItem.expansionMultiplier))
                this.$log(price, itemDepositAmount)
                if (Number(price) > Number(goodsItem.presaleAmount)) {
                  hasExpansionMultiplier = true
                }
              }
              if (goodsItem.presaleType == 2) {

                if (!goodsItem.finalPayDiscountAmount || goodsItem.finalPayDiscountAmount == 0) {
                  hasInputFinalPayDiscountAmount = true
                }

                let price = this.$common.add(Number(itemDepositAmount), Number(goodsItem.finalPayDiscountAmount))
                this.$log(price, itemDepositAmount)
                if (Number(price) > Number(goodsItem.presaleAmount)) {
                  hasFinalPayDiscountAmount = true
                }
              }
              
            }
          }
          
          if (goodsItem.maxNum && goodsItem.maxNum != 0 && goodsItem.allNum && goodsItem.allNum != 0) {
            if (Number(goodsItem.maxNum) > Number(goodsItem.allNum)) {
              hasCorrectAllNum = true
            }
          }
        }
      }

      if (hasPresaleAmount) {
        this.$common.warn('请输入预售价，大于0')
        return false
      }

      if (hasDepositRatio) {
        this.$common.warn('请输入定金比例，大于0小于100')
        return false
      }

      if (hasInputExpansionMultiplier) {
        this.$common.warn('请输入定金膨胀倍数，大于1')
        return false
      }

      if (hasInputFinalPayDiscountAmount) {
        this.$common.warn('请输入尾款立减金额，大于0')
        return false
      }

      if (hasExpansionMultiplier) {
        this.$common.warn('定金膨胀比例已经超过原价')
        return false
      }
      if (hasFinalPayDiscountAmount) {
        this.$common.warn('尾款立减金额超过尾款金额')
        return false
      }

      if (hasCorrectAllNum) {
        this.$common.warn('合计最大预定量应控制大于每人最大预定量')
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
