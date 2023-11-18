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
              <el-form-item label="活动名称" prop="activityName">
                <el-input class="show-word-limit" v-model="baseFormModel.activityName" maxlength="20" show-word-limit placeholder="请输入活动名称"></el-input>
              </el-form-item>
              <el-form-item label="活动分类" prop="category">
                <el-select v-model="baseFormModel.category" placeholder="请选择" disabled>
                  <el-option label="平台活动" :value="1"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="活动平台" prop="platform">
                <el-select v-model="baseFormModel.platform" placeholder="请选择">
                  <el-option v-for="item in lotteryActivityPlatform" :key="item.value" :label="item.label"
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
              <el-form-item label="运营备注">
                <el-input v-model="baseFormModel.opRemark" placeholder="请输入运营备注" maxlength="20"></el-input>
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
              <div class="sign"></div>参与规则
              <div class="des-label">请通过“营销活动--策略满赠”功能设置客户获取抽奖次数。</div>
              <yl-button class="edit-btn" plain type="primary" :disabled="running || operationType == 1" @click="addRuleClick">添加参与规则</yl-button>
            </div>
            <!-- 参与规则 -->
            <div class="content-box my-form-box">
              <!-- B端规则 -->
              <div class="Gradients-item mar-t-10" v-if="joinRuleList.findIndex((item) => item.id == 1) != -1">
                <div>
                  <div class="header-bar header-renative">
                  参与规则1（B端）<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeRuleId(1)"></yl-button></div>
                  <div>
                    <el-form-item label="" label-width="0">
                      <div class="activity-form-item flex-row-left">
                        <el-form-item label="每人赠送次数" prop="activityGiveScope.giveTimes">
                          <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.activityGiveScope.giveTimes" @keyup.native="useRuleFormModel.activityGiveScope.giveTimes = onInput(useRuleFormModel.activityGiveScope.giveTimes, 0)"></el-input>
                        </el-form-item>
                        <span class="tip-text">请输入大于0的整数</span>
                      </div>
                    </el-form-item>
                    <el-form-item label="重复执行" prop="activityGiveScope.loopGive">
                      <el-radio-group v-model="useRuleFormModel.activityGiveScope.loopGive" :disabled="running || operationType == 1">
                        <el-radio v-for="item in lotteryActivityLoopGive" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="赠送范围" prop="activityGiveScope.giveScope">
                      <el-radio-group v-model="useRuleFormModel.activityGiveScope.giveScope">
                        <el-radio :label="1" :disabled="running || operationType == 1">全部客户</el-radio>
                        <el-radio :label="2" :disabled="running || operationType == 1">指定客户</el-radio>
                        <yl-button class="mar-r-16" v-if="useRuleFormModel.activityGiveScope.giveScope == 2" type="text" @click="addClientClick">设置 {{ useRuleFormModel.activityGiveScope.eidListCount ? `(${useRuleFormModel.activityGiveScope.eidListCount})` : '' }}</yl-button>
                        <el-radio :label="3" :disabled="running || operationType == 1">指定客户范围</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <!-- 指定范围客户 -->
                    <div class="section-view" v-if="useRuleFormModel.activityGiveScope.giveScope == 3">
                      <el-form-item label="企业类型" prop="activityGiveScope.giveEnterpriseType">
                        <el-radio-group v-model="useRuleFormModel.activityGiveScope.giveEnterpriseType" :disabled="running || operationType == 1">
                          <el-radio v-for="item in lotteryActivityGiveEnterpriseType" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="" v-if="useRuleFormModel.activityGiveScope.giveEnterpriseType == 2" prop="activityGiveScope.enterpriseTypeList">
                        <el-checkbox-group v-model="useRuleFormModel.activityGiveScope.enterpriseTypeList" :disabled="running || operationType == 1">
                            <el-checkbox
                              v-for="item in enterpriseType"
                              v-show="item.value != 1 && item.value != 2"
                              :key="item.value"
                              :label="item.value"
                            >{{ item.label }}</el-checkbox>
                          </el-checkbox-group>
                      </el-form-item>
                      <el-form-item label="用户类型" prop="activityGiveScope.giveUserType">
                        <el-radio-group v-model="useRuleFormModel.activityGiveScope.giveUserType">
                          <el-radio :label="1" :disabled="running || operationType == 1">全部用户</el-radio>
                          <el-radio :label="2" :disabled="running || operationType == 1">普通用户</el-radio>
                          <el-radio :label="3" :disabled="running || operationType == 1">全部会员</el-radio>
                          <el-radio :label="4" :disabled="running || operationType == 1">指定会员方案</el-radio>
                          <yl-button class="mar-r-16" v-if="useRuleFormModel.activityGiveScope.giveUserType == 4" type="text" @click="addMemberDimensionClick">设置 {{ useRuleFormModel.activityGiveScope.memberIdListCount ? `(${useRuleFormModel.activityGiveScope.memberIdListCount})` : '' }}</yl-button>
                          <el-radio :label="5" :disabled="running || operationType == 1">指定会员推广方</el-radio>
                          <yl-button v-if="useRuleFormModel.activityGiveScope.giveUserType == 5" type="text" @click="addExtensionClick">设置 {{ useRuleFormModel.activityGiveScope.promoterIdListCount ? `(${useRuleFormModel.activityGiveScope.promoterIdListCount})` : '' }}</yl-button>
                        </el-radio-group>
                      </el-form-item>
                    </div>
                  </div>
                </div>
              </div>
              <!-- C端规则 -->
              <div class="Gradients-item mar-t-10" v-if="joinRuleList.findIndex((item) => item.id == 2) != -1">
                <div>
                  <div class="header-bar header-renative">
                  参与规则2（C端）<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeRuleId(2)"></yl-button></div>
                  <div>
                    <el-form-item label="活动参与门槛" prop="activityJoinRule.joinStage">
                      <el-radio-group v-model="useRuleFormModel.activityJoinRule.joinStage" :disabled="running || operationType == 1">
                        <el-radio :label="1">不限制</el-radio>
                        <el-radio :label="2">关注健康管理中心公众号</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </div>
                </div>
              </div>
              <!-- C端规则 -->
              <div class="Gradients-item mar-t-10" v-if="joinRuleList.findIndex((item) => item.id == 3) != -1">
                <div>
                  <div class="header-bar header-renative">
                  参与规则3（C端）<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeRuleId(3)"></yl-button></div>
                  <div>
                    <el-form-item label="" label-width="0">
                      <div class="activity-form-item flex-row-left">
                        <el-form-item label-width="220px" label="活动期间每天赠送抽奖次数" prop="activityJoinRule.everyGive">
                          <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.activityJoinRule.everyGive" @keyup.native="useRuleFormModel.activityJoinRule.everyGive = onInput(useRuleFormModel.activityJoinRule.everyGive, 0)"></el-input>
                        </el-form-item>
                      </div>
                    </el-form-item>
                  </div>
                </div>
              </div>
              <!-- C端规则 -->
              <div class="Gradients-item mar-t-10" v-if="joinRuleList.findIndex((item) => item.id == 4) != -1">
                <div>
                  <div class="header-bar header-renative">
                  参与规则4（C端）<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeRuleId(4)"></yl-button></div>
                  <div>
                    <el-form-item label="" label-width="0">
                      <div class="activity-form-item flex-row-left">
                        <el-form-item label-width="220px" label="活动签到赠送抽奖次数" prop="activityJoinRule.signGive">
                          <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.activityJoinRule.signGive" @keyup.native="useRuleFormModel.activityJoinRule.signGive = onInput(useRuleFormModel.activityJoinRule.signGive, 0)"></el-input>
                        </el-form-item>
                      </div>
                    </el-form-item>
                  </div>
                </div>
              </div>
              <!-- C端规则 -->
              <div class="Gradients-item mar-t-10" v-if="joinRuleList.findIndex((item) => item.id == 5) != -1">
                <div>
                  <div class="header-bar header-renative">
                  参与规则5（C端）<yl-button class="mar-l-16" icon="el-icon-remove-outline" type="text" :disabled="running || operationType == 1" @click="removeRuleId(5)"></yl-button></div>
                  <div>
                    <el-form-item label="" label-width="0">
                      <div class="activity-form-item flex-row-left">
                        <el-form-item label-width="220px" label="活动邀请新粉丝赠送抽奖次数" prop="activityJoinRule.inviteGive">
                          <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.activityJoinRule.inviteGive" @keyup.native="useRuleFormModel.activityJoinRule.inviteGive = onInput(useRuleFormModel.activityJoinRule.inviteGive, 0)"></el-input>
                        </el-form-item>
                      </div>
                    </el-form-item>
                  </div>
                </div>
              </div>
            </div>
            <div class="header-bar header-renative mar-t-16">
              <div class="sign"></div>奖品设置
              <div class="des-label">奖品最少2个，最多8个</div>
              <yl-button class="edit-btn" plain type="primary" :disabled="running || operationType == 1" @click="selectGiftClick">添加奖品</yl-button>
            </div>
            <div class="content-box my-form-box">
              <yl-table
                border
                show-header
                :list="useRuleFormModel.activityRewardSettingList"
                :total="0"
              >
                <el-table-column label="序号" min-width="50" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base" :class="row.remainNumber < row.rewardNumber ? 'active' : ''">{{ row.level }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="等级" min-width="50" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base" :class="row.remainNumber < row.rewardNumber ? 'active' : ''">{{ row.level | dictLabel(levelArray) }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="类型" min-width="80" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base" :class="row.remainNumber < row.rewardNumber ? 'active' : ''">{{ row.rewardType | dictLabel(lotteryActivityRewardType) }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="奖品名称" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="goods-desc">
                      <div class="font-size-base" :class="row.remainNumber < row.rewardNumber ? 'active' : ''">{{ row.rewardName }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="展示名称" min-width="162" align="center">
                  <template slot-scope="{ row }">
                    <div class="operation-view">
                      <div class="option">
                        <el-input v-model="row.showName" :disabled="running || operationType == 1" maxlength="10"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="奖品剩余数量" min-width="80" align="center">
                  <template slot-scope="{ row }">
                    <div class="font-size-base" v-if="row.rewardType == 5 || row.rewardType == 6">- -</div>
                    <div class="font-size-base" :class="row.remainNumber < row.rewardNumber ? 'active' : ''" v-else>{{ row.remainNumber }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="中奖数量" min-width="80" align="center" :render-header="addRedStarWithRemark">
                  <template slot-scope="{ row, $index }">
                    <div class="operation-view">
                      <div class="font-size-base" v-if="row.rewardType == 5">- -</div>
                      <div class="option" v-else>
                        <el-input v-model="row.rewardNumber" :disabled="running || operationType == 1" @input="change($index, 1)"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="每天最大抽中数量 (0为不限制)" min-width="80" align="center" :render-header="addRedStar">
                  <template slot-scope="{ row, $index }">
                    <div class="operation-view">
                      <div class="font-size-base" v-if="row.rewardType == 5 || row.rewardType == 6">- -</div>
                      <div class="option" v-else>
                        <el-input v-model="row.everyMaxNumber" :disabled="operationType == 1" @input="change($index, 3)"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="中奖概率(%)" min-width="80" align="center" :render-header="addRedStar">
                  <template slot-scope="{ row, $index }">
                    <div class="operation-view">
                      <div class="option">
                        <el-input v-model="row.hitProbability" :disabled="operationType == 1" @input="change($index, 2)"></el-input>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" min-width="100" align="center">
                  <template slot-scope="{ $index }">
                    <div>
                      <yl-button type="text" :disabled="running || operationType == 1" @click="removeGiftClick($index)">删除</yl-button>
                    </div>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
            <!-- 临界控制 -->
            <div class="header-bar header-renative mar-t-16">
              <div class="sign"></div>临界控制
            </div>
            <div class="content-box my-form-box">
              <el-form-item label="" label-width="0">
                <div class="activity-form-item flex-row-left">
                  <el-form-item label="预算金额" prop="budgetAmount">
                    <el-input :disabled="running || operationType == 1" class="input-box" v-model="useRuleFormModel.budgetAmount" @keyup.native="useRuleFormModel.budgetAmount = onInput(useRuleFormModel.budgetAmount, 2)"></el-input>
                  </el-form-item>
                  <span class="tip-text">（0为不控制）</span>
                </div>
              </el-form-item>
            </div>
            <!-- 活动规则说明 -->
            <div class="header-bar header-renative">
              <div class="sign"></div>活动规则说明
            </div>
            <div class="content-box my-form-box">
              <wang-editor ref="editor" :height="height" :content="content" :extral-data="{type: 'richTextEditorFile'}" :handle-content="handleContent" />
            </div>
            <!-- 活动分享（仅适用C端） -->
            <div class="header-bar header-renative mar-t-16" v-if="platformType == 2">
              <div class="sign"></div>活动分享（仅适用C端）
            </div>
            <div class="content-box my-form-box" v-if="platformType == 2">
              <yl-upload
                :default-url="useRuleFormModel.bgPictureUrl"
                :max-size="2048"
                :limit-width="965"
                :limit-height="1751"
                :extral-data="{type: 'lotteryActivityFile'}"
                @onSuccess="onSuccess"
              />
              <div class="explain mar-t-10">不超过2MB，宽965*高1751px</div>
            </div>
          </div>
        </el-form>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 指定客户 -->
    <add-client-dialog v-if="addClientVisible" ref="addClientRef" @selectNumChange="eidListCountSelectNum"></add-client-dialog>
    <!-- 选择赠品 -->
    <select-rule-dialog v-if="selectRuleVisible" ref="selectRuleRef" @save="confirmClick"></select-rule-dialog>
    <!-- 选择奖品 -->
    <select-gift-dialog v-if="selectGiftVisible" ref="selectGiftRef" @save="selectGiftconfirmClick"></select-gift-dialog>
    <!-- 指定会员方案-会员维度 -->
    <add-member-dimension-dialog v-if="addMemberDimensionVisible" ref="addMemberDimensionRef" @selectNumChange="memberIdListCountSelectNum"></add-member-dimension-dialog>
    <!-- 指定会员推广方 -->
    <add-extension-dialog v-if="addExtensionVisible" ref="addExtensionRef" @selectNumChange="promoterIdListCountSelectNum"></add-extension-dialog>
  </div>
</template>

<script>
import { lotteryActivityGet, lotteryActivitySaveActivityBasic, lotteryActivitySaveActivitySetting, lotteryActivityUpdateRewardSetting } from '@/subject/admin/api/view_marketing/lottery_activity'
import { lotteryActivityPlatform, lotteryActivityGiveEnterpriseType, lotteryActivityRewardType, lotteryActivityLoopGive } from '@/subject/admin/busi/marketing/lottery'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { enterpriseType } from '@/subject/admin/utils/busi'
import { wangEditor } from '@/subject/admin/components'
import { ylUpload } from '@/subject/admin/components'

import SelectRuleDialog from '../components/select_rule_dialog'
import SelectGiftDialog from '../components/select_gift_dialog'
import AddMemberDimensionDialog from '../components/add_member_dimension_dialog'
import AddClientDialog from '../components/add_client_dialog'
import AddExtensionDialog from '../components/add_extension_dialog'

export default {
  components: {
    wangEditor,
    ylUpload,
    SelectRuleDialog,
    SelectGiftDialog,
    AddClientDialog,
    AddMemberDimensionDialog,
    AddExtensionDialog
  },
  computed: {
    enterpriseType() {
      return enterpriseType()
    },
    // 活动平台
    lotteryActivityPlatform() {
      let platform = lotteryActivityPlatform()
      let arr = []
      if (this.platformType == 1) {
        platform.forEach((item) => {
          if (item.value == 1) {
            arr.push(item)
          }
        })
      } else {
        platform.forEach((item) => {
          if (item.value != 1) {
            arr.push(item)
          }
        })
      }
      return arr
    },
    // 指定范围客户企业类型
    lotteryActivityGiveEnterpriseType() {
      return lotteryActivityGiveEnterpriseType()
    },
    // 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
    lotteryActivityRewardType() {
      return lotteryActivityRewardType()
    },
    // 重复执行
    lotteryActivityLoopGive() {
      return lotteryActivityLoopGive()
    }
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      if (value == 0) {
        callback(new Error('数量不能为0'));
      } else {
        callback();
      }
    };
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().getTime() - 86400000; 
        }
      },
      levelArray: [
        {
          label: '一等奖',
          value: 1
        },
        {
          label: '二等奖',
          value: 2
        },
        {
          label: '三等奖',
          value: 3
        },
        {
          label: '四等奖',
          value: 4
        },
        {
          label: '五等奖',
          value: 5
        },
        {
          label: '六等奖',
          value: 6
        },
        {
          label: '七等奖',
          value: 7
        },
        {
          label: '八等奖',
          value: 8
        }
      ],
      // 活动是否已开始：true-已开始 false-未开始
      running: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增 4-复制
      operationType: 2,
      // 平台类型：1-B端 2-C端
      platformType: 1,
      id: '',
      // 添加规则
      selectRuleVisible: false,
      joinRuleList: [],
      lastPlatform: '',
      // 选择奖品
      selectGiftVisible: false,
      // 指定客户
      addClientVisible: false,
      addMemberDimensionVisible: false,
      addExtensionVisible: false,
      // 活动规则说明
      content: '',
      height: 500,
      loading: false,
      baseFormModel: {
        activityName: '',
        category: 1,
        platform: '',
        validDate: [],
        opRemark: ''
      },
      baseRules: {
        activityName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        platform: [{ required: true, message: '请选择活动平台', trigger: 'change' }],
        validDate: [{ required: true, message: '请选择生效时间', trigger: 'change' }]
      },
      // 使用规则设置
      useRuleFormModel: {
        // 抽奖活动赠送范围（B端使用规则）
        activityGiveScope: {
          giveTimes: '',
          // 重复执行
          loopGive: '',
          // 赠送范围
          giveScope: '',
          // 指定客户数量
          eidListCount: 0,
          // 企业类型
          giveEnterpriseType: '',
          // 企业类型集合
          enterpriseTypeList: [],
          // 用户类型
          giveUserType: '',
          // 指定方案会员数量
          memberIdListCount: 0,
          // 指定推广方会员数量
          promoterIdListCount: 0
        },
        // C端参与规则
        activityJoinRule: {
          // 活动参与门槛
          joinStage: '',
          // 活动期间每天赠送抽奖次数
          everyGive: '',
          // 活动签到赠送抽奖次数
          signGive: '',
          // 活动邀请新粉丝赠送抽奖次数
          inviteGive: ''
        },
        // 抽奖活动奖品设置集合
        activityRewardSettingList: [],
        // 预算金额
        budgetAmount: 0,
        // 活动规则说明
        content: '',
        // 分享背景图
        bgPicture: '',
        bgPictureUrl: ''
      },
      useRules: {
        'activityGiveScope.giveTimes': [
          { required: true, message: '请输入每人赠送次数', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        'activityGiveScope.loopGive': [{ required: true, message: '请选择重复执行', trigger: 'change' }],
        'activityGiveScope.giveScope': [{ required: true, message: '请选择赠送范围', trigger: 'change' }],
        'activityGiveScope.giveEnterpriseType': [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        'activityGiveScope.enterpriseTypeList': [{ type: 'array', required: true, message: '请选择指定类型', trigger: 'change' }],
        'activityGiveScope.giveUserType': [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        // C端参与规则
        'activityJoinRule.joinStage': [{ required: true, message: '请选择活动参与门槛', trigger: 'change' }],
        'activityJoinRule.everyGive': [
          { required: true, message: '请输入赠送抽奖次数', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        'activityJoinRule.signGive': [
          { required: true, message: '请输入赠送抽奖次数', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        'activityJoinRule.inviteGive': [
          { required: true, message: '请输入赠送抽奖次数', trigger: 'blur' },
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        // 预算金额
        budgetAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    this.platformType = this.$route.params.platformType
    this.$log('params:', this.$route.params)
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let data = await lotteryActivityGet(this.id)
      if (data) {
        this.running = data.lotteryActivityBasic.progress == 2

        let lotteryActivityBasic = data.lotteryActivityBasic
        let baseFormModel = {
          activityName: lotteryActivityBasic.activityName,
          category: 1,
          platform: lotteryActivityBasic.platform,
          validDate: lotteryActivityBasic.validDate,
          opRemark: lotteryActivityBasic.opRemark
        }
        if (lotteryActivityBasic.startTime && lotteryActivityBasic.startTime > 0 && lotteryActivityBasic.endTime && lotteryActivityBasic.endTime > 0) {
          baseFormModel.validDate = [formatDate(lotteryActivityBasic.startTime), formatDate(lotteryActivityBasic.endTime)]
        }

        this.lastPlatform = lotteryActivityBasic.platform
        this.baseFormModel = baseFormModel

        // 获取设置过哪些规则
        let ruleList = []
        if (data.activityGiveScope) {
          this.useRuleFormModel.activityGiveScope = data.activityGiveScope
          if (data.activityGiveScope.giveScope) {
            this.useRuleFormModel.activityGiveScope.loopGive = data.activityGiveScope.loopGive || ''
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = 1
            ruleList.push(newObj)
          }
        }
        if (data.activityJoinRule) {
          this.useRuleFormModel.activityJoinRule = data.activityJoinRule
          if (data.activityJoinRule.joinStage) {
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = 2
            ruleList.push(newObj)
          }
          if (data.activityJoinRule.everyGive) {
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = 3
            ruleList.push(newObj)
          }
          if (data.activityJoinRule.signGive) {
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = 4
            ruleList.push(newObj)
          }
          if (data.activityJoinRule.inviteGive) {
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = 5
            ruleList.push(newObj)
          }
        }
        this.useRuleFormModel.activityRewardSettingList = data.activityRewardSettingList
        this.useRuleFormModel.budgetAmount = data.budgetAmount
        this.useRuleFormModel.content = data.content
        this.useRuleFormModel.bgPicture = data.bgPicture
        this.useRuleFormModel.bgPictureUrl = data.bgPictureUrl

        this.content = data.content
        this.joinRuleList = ruleList

        if (this.operationType == 4) {
          this.running = false
          this.baseFormModel.validDate = []
          this.baseFormModel.startTime = ''
          this.baseFormModel.endTime = ''
        }

        if (this.running || this.operationType == 1) {
          this.$refs.editor.disableMethod()
        }
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
    // 选择客户数量
    eidListCountSelectNum(num) {
      this.useRuleFormModel.activityGiveScope.eidListCount = num
    },
    memberIdListCountSelectNum(num) {
      this.useRuleFormModel.activityGiveScope.memberIdListCount = num
    },
    promoterIdListCountSelectNum(num) {
      this.useRuleFormModel.activityGiveScope.promoterIdListCount = num
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
    addExtensionClick() {
      if (this.id) {
        this.addExtensionVisible = true
        this.$nextTick( () => {
          this.$refs.addExtensionRef.init(this.id, this.running, this.operationType)
        })
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      } 
    },
    // 添加阶梯
    addRuleClick() {
      this.selectRuleVisible = true
      this.$nextTick( () => {
        this.$refs.selectRuleRef.init(this.joinRuleList)
      })
    },
    //删除规则
    removeRuleId(ruleId) {
      let joinRuleList = this.joinRuleList
      let hasIndex = joinRuleList.findIndex(obj => {
        return ruleId == obj.id;
      })
      if (hasIndex != -1) {
        joinRuleList.splice(hasIndex, 1)
      }
    },
    // 选择规则保存
    confirmClick(selectRuleList) {
      let joinRuleList = this.joinRuleList
      let lastJoinRuleListNum = joinRuleList.length || 0
      if (selectRuleList && selectRuleList.length > 0) {
        selectRuleList.forEach((row) => {
          let hasIndex = joinRuleList.findIndex(obj => {
            return row.id == obj.id;
          })
          if (hasIndex == -1 && row.usePlatform == this.platformType){
            // eslint-disable-next-line no-new-object
            let newObj = new Object()
            newObj.id = row.id
            joinRuleList.push(newObj)
          }
        })
        if (selectRuleList.length + lastJoinRuleListNum != joinRuleList.length) {
          this.$common.warn('只能添加当前活动平台规则，每个规则只允许添加一次')
        }

      } 
      
    },
    // 给表头加必填符号*
    addRedStar(h, { column }) {
      this.$log(column)
      return [
        h('span', { style: 'color: red' }, '*'),
        h('span', ' ' + column.label)
      ];
    },
    // 给表头加必填符号*
    addRedStarWithRemark(h, { column }) {
      this.$log(column)
      return [
        h('span', { style: 'color: red' }, '*'),
        h('span', ' ' + column.label),
        h('div', { style: 'color: red' }, '(抽中送几个)')
      ];
    },
    // 选择奖品点击
    selectGiftClick() {
      this.selectGiftVisible = true
      this.$nextTick( () => {
        this.$refs.selectGiftRef.init([], this.platformType)
      })
    },
    // 奖品设置
    change(index, operateType) {
      // 重新赋值
      let tmpObj = this.useRuleFormModel.activityRewardSettingList[index];
      if (operateType == 1) {
        tmpObj.rewardNumber = this.onInput(tmpObj.rewardNumber, 0, 2)
      } else if (operateType == 2) {
        tmpObj.hitProbability = this.onInput(tmpObj.hitProbability, 6)
      } else if (operateType == 3) {
        tmpObj.everyMaxNumber = this.onInput(tmpObj.everyMaxNumber, 0, 4)
      }
      this.$set(this.useRuleFormModel.activityRewardSettingList, index, tmpObj);
    },
    removeGiftClick(index) {
      let activityRewardSettingList = this.useRuleFormModel.activityRewardSettingList
      activityRewardSettingList.splice(index, 1)
      for (let index = 0; index < activityRewardSettingList.length; index++) {
        let item = activityRewardSettingList[index]
        item.level = index + 1
      }
    },
    selectGiftconfirmClick(selectGiftList, type) {
      this.$log(selectGiftList)
      let activityRewardSettingList = this.useRuleFormModel.activityRewardSettingList || []
      let hasRewardNum = activityRewardSettingList.length

      for (let index = 0; index < selectGiftList.length; index++) {
        const item = selectGiftList[index]
        // eslint-disable-next-line no-new-object
        let newObj = new Object()
        newObj.level = hasRewardNum + 1 + index
        // 商品优惠券
        if (type == 1) {
          newObj.rewardId = item.id
          newObj.rewardType = 3
          newObj.rewardName = item.name
          newObj.remainNumber = item.surplusCount
          newObj.rewardNumber = 1
        } 
        // 会员优惠券
        if (type == 2) {
          newObj.rewardId = item.id
          newObj.rewardType = 4
          newObj.rewardName = item.name
          newObj.remainNumber = item.surplusCount
          newObj.rewardNumber = 1
        }
        // 赠品
        if (type == 3) {
          if (item.goodsType == 1) {
            newObj.rewardType = 1
          }
          if (item.goodsType == 2) {
            newObj.rewardType = 2
          }
          newObj.rewardId = item.id
          newObj.rewardName = item.name
          newObj.remainNumber = item.availableQuantity
          newObj.rewardNumber = 1
        }
        // 空奖
        if (type == 4) {
          newObj.rewardType = item.rewardType
          newObj.rewardName = item.rewardName
          newObj.emptyWords = item.emptyWords
        }
        // 抽奖机会
        if (type == 5) {
          newObj.rewardType = item.rewardType
          newObj.rewardName = item.rewardName
          newObj.rewardNumber = item.rewardNumber
        } 
        
        newObj.showName = ''
        newObj.hitProbability = ''
        newObj.everyMaxNumber = 0
        activityRewardSettingList.push(newObj)
      }

      if (activityRewardSettingList.length > 8) {
        this.$common.warn('奖品最少2个，最多8个！')
        activityRewardSettingList.splice(8)
      }

    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.$log(content)
      this.useRuleFormModel.content = content
    },
    // 图片上传成功
    async onSuccess(data) {
      if (data.key) {
        this.useRuleFormModel.bgPicture = data.key
        this.useRuleFormModel.bgPictureUrl = data.url
      }
    },
    // 基本信息保存按钮点击
    topSaveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let baseFormModel = this.baseFormModel

          baseFormModel.startTime = baseFormModel.validDate && baseFormModel.validDate.length ? baseFormModel.validDate[0] : undefined
          baseFormModel.endTime = baseFormModel.validDate && baseFormModel.validDate.length > 1 ? baseFormModel.validDate[1] : undefined

          if (this.id) {
            baseFormModel.id = this.id
          }
          this.$common.showLoad()
          let data = await lotteryActivitySaveActivityBasic(baseFormModel)
          this.$common.hideLoad()
          if (data) {
            this.id = data.id
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
        this.useRuleFormModel.lotteryActivityId = this.id
      } else {
        this.$common.warn('请先保存基本信息');
        return false
      }

      this.$refs['useRuleForm'].validate(async (valid) => {
        if (valid) {

          if (this.checkInputData()) {
            let useRuleFormModel = this.useRuleFormModel
            
            let platform = this.baseFormModel.platform
            if (platform == 1) {
              if (useRuleFormModel.giveScope == 1 || useRuleFormModel.giveScope == 2) {
                useRuleFormModel.giveEnterpriseType = ''
                useRuleFormModel.enterpriseTypeList = []
                useRuleFormModel.giveUserType = ''
              } else {
                if (useRuleFormModel.giveEnterpriseType == 1) {
                  useRuleFormModel.enterpriseTypeList = []
                }
              }
              useRuleFormModel.activityJoinRule = {
                // 活动参与门槛
                joinStage: '',
                // 活动期间每天赠送抽奖次数
                everyGive: '',
                // 活动签到赠送抽奖次数
                signGive: '',
                // 活动邀请新粉丝赠送抽奖次数
                inviteGive: ''
              }
            } else if (platform == 2) {
              useRuleFormModel.activityGiveScope = {
                giveTimes: '',
                // 重复执行
                loopGive: '',
                // 赠送范围
                giveScope: '',
                // 企业类型
                giveEnterpriseType: '',
                // 企业类型集合
                enterpriseTypeList: [],
                // 用户类型
                giveUserType: ''
              }
              let activityJoinRule = {
                // 活动参与门槛
                joinStage: '',
                // 活动期间每天赠送抽奖次数
                everyGive: '',
                // 活动签到赠送抽奖次数
                signGive: '',
                // 活动邀请新粉丝赠送抽奖次数
                inviteGive: ''
              }
              this.joinRuleList.forEach(item => {
                if (item.id == 2) { // 规则2
                  activityJoinRule.joinStage = useRuleFormModel.activityJoinRule.joinStage
                }
                if (item.id == 3) { // 规则3
                  activityJoinRule.everyGive = useRuleFormModel.activityJoinRule.everyGive
                }
                if (item.id == 4) { // 规则4
                  activityJoinRule.signGive = useRuleFormModel.activityJoinRule.signGive
                }
                if (item.id == 5) { // 规则5
                  activityJoinRule.inviteGive = useRuleFormModel.activityJoinRule.inviteGive
                }
              })
              useRuleFormModel.activityJoinRule = activityJoinRule
            }
            
            this.$common.showLoad()
            let data = null
            // 修改奖品设置信息
            if (this.running) {
              data = await lotteryActivityUpdateRewardSetting(useRuleFormModel.activityRewardSettingList)
            } else {
              data = await lotteryActivitySaveActivitySetting(useRuleFormModel)
            }
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
        if (!this.baseFormModel.startTime || !this.baseFormModel.endTime) {
          this.$common.warn('请先保存基本信息')
          return false
        }
      }

      let joinRuleList = this.joinRuleList
      if (!joinRuleList || joinRuleList.length == 0) {
        this.$common.warn('请添加参与规则')
        return false
      }

      if (this.useRuleFormModel.activityGiveScope.giveScope == 2 && !this.useRuleFormModel.activityGiveScope.eidListCount) {
        this.$common.warn('设置指定客户')
        return false
      }

      if (this.useRuleFormModel.activityGiveScope.giveScope == 3 && this.useRuleFormModel.activityGiveScope.giveUserType == 4 && !this.useRuleFormModel.activityGiveScope.memberIdListCount) {
        this.$common.warn('设置指定会员方案')
        return false
      }

      if (this.useRuleFormModel.activityGiveScope.giveScope == 3 && this.useRuleFormModel.activityGiveScope.giveUserType == 5 && !this.useRuleFormModel.activityGiveScope.promoterIdListCount) {
        this.$common.warn('设置指定会员推广方')
        return false
      }

      // 是否有抽奖活动奖品
      let hasRewardSettingList = false
      // 没有空奖
      let hasEmptyReward = false
      // 中奖数量
      let hasRewardNumber = false
      // 每天最大抽中数量
      let hasEveryMaxNumber = false
      // 是否是整数倍
      let hasMultiple = false
      // 中奖概率
      let hashitProbability = false
      // 正确的中奖概率
      let hashitProbabilityAmount = false

      let activityRewardSettingList = this.useRuleFormModel.activityRewardSettingList
      if (!activityRewardSettingList || activityRewardSettingList.length < 2 || activityRewardSettingList.length > 8) {
        hasRewardSettingList = true
      }
      let hashitProbabilityTotal = 0
      for (let i = 0; i < activityRewardSettingList.length; i++) {
        let RewardItem = activityRewardSettingList[i]
        if (RewardItem.rewardType == 5) {
          hasEmptyReward = true
        }
        if (!(RewardItem.rewardNumber + '') && RewardItem.rewardType != 5) {
          hasRewardNumber = true
        }
        // 每天最大抽中数量必须是中奖数量的整数倍！
        if (RewardItem.rewardType != 5 && RewardItem.rewardType != 6) {
          if (RewardItem.rewardNumber && RewardItem.rewardNumber != 0 && RewardItem.everyMaxNumber && RewardItem.everyMaxNumber != 0) {
            if ((Number(RewardItem.everyMaxNumber) % Number(RewardItem.rewardNumber)) != 0) {
              hasMultiple = true
            }
          }
        }
        if (!(RewardItem.everyMaxNumber + '')) {
          hasEveryMaxNumber = true
        }
        if (!(RewardItem.hitProbability + '')) {
          hashitProbability = true
        }
        if (RewardItem.hitProbability) {
          hashitProbabilityTotal = this.$common.add(Number(hashitProbabilityTotal), Number(RewardItem.hitProbability))
        }
      }
      if (hashitProbabilityTotal != 100) {
        hashitProbabilityAmount = true
      }

      if (hasRewardSettingList) {
        this.$common.warn('奖品最少2个，最多8个！')
        return false
      }
      if (!hasEmptyReward) {
        this.$common.warn('奖品必须包含空奖！')
        return false
      }
      if (hasRewardNumber) {
        this.$common.warn('请填写正确的中奖数量')
        return false
      }
      if (hasEveryMaxNumber) {
        this.$common.warn('请填写每天最大抽中数量')
        return false
      }
      if (hasMultiple) {
        this.$common.warn('每天最大抽中数量必须是中奖数量的整数倍！')
        return false
      }
      if (hashitProbability) {
        this.$common.warn('请填写正确的中奖概率')
        return false
      }
      if (hashitProbabilityAmount) {
        this.$common.warn('奖品的概率之和不等于100，请重新检查后保存！')
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
