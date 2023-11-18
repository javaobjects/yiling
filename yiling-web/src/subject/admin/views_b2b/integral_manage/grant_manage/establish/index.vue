<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="130px" 
          class="demo-ruleForm">
          <h4>基本信息：</h4>
          <el-form-item label="规则名称：" prop="name">
            <el-input 
              v-model.trim="form.name" 
              maxlength="20" 
              show-word-limit 
              placeholder="请输入规则名称"
              :disabled="type == 2"></el-input>
          </el-form-item>
          <el-form-item label="生效时间：" prop="time">
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :disabled="type == 2"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="规则说明：" prop="description">
            <el-input
              type="textarea"
              maxlength="50"  
              show-word-limit
              style="max-width: 90%"
              :autosize="{ minRows: 2, maxRows: 2}"
              placeholder="请输入规则说明"
              v-model.trim="form.description"
              :disabled="type == 2">
            </el-input>
          </el-form-item>
          <el-form-item label="选择行为：" prop="behaviorId">
            <div>
              <el-tag
                v-model="form.behaviorId"
                :closable="type == 2 ? false : true"
                type="info" 
                class="item-el-tag" 
                v-for="(item, index) in dataList" 
                :key="index" 
                @close="drugClose(index)"
                > 
                {{ item.name }} 
              </el-tag>
            </div>
            <span class="add-span" @click="addClick" v-if="type != 2">{{ dataList && dataList.length > 0 ? '编辑' : '添加' }}</span>
          </el-form-item>
          <div class="basicInfo">
            <yl-button type="primary" v-if="type != 2" @click="basicInfoClick">基本信息保存</yl-button>
          </div>
        </el-form>
        <!-- 签到送积分 -->
        <div v-if="form.id != '' && form.behaviorId == 2">
          <el-form 
            :model="signin" 
            :rules="signinRules" 
            ref="signinForm" 
            label-width="130px" 
            class="demo-ruleForm">
              <h4>签到送积分：</h4>
              <el-form-item label="签到周期：" prop="signPeriod">
                <el-input 
                  v-model.trim="signin.signPeriod" 
                  placeholder="请输入5 ~ 31整数"
                  @input="e => (signin.signPeriod = signPeriodCheckInput(e))"
                  :disabled="type == 2"
                  @blur="blur"></el-input> 天
              </el-form-item>
              <el-form-item label="发放分值配置：">
                <div class="score-to-configure">
                  <el-row v-if="signin.scoreData && signin.scoreData.length > 0">
                    <el-col :span="4" class="col-text col-color">
                      天数
                    </el-col>
                    <el-col :span="10" class="col-text col-color">
                      当天发放积分数
                    </el-col>
                    <el-col :span="10" class="col-text col-color">
                      连签奖励
                    </el-col>
                  </el-row>
                  <el-row class="row-bottom" v-for="(item, index) in signin.scoreData" :key="index">
                    <el-col :span="4" class="col-text col-color">
                      第{{ item.days = (index + 1) }}天
                    </el-col>
                    <el-col :span="10" class="col-text">
                      <el-form-item 
                        label="" 
                        :prop="'scoreData.' + index + '.currentDayIntegral'" 
                        :rules="{ required: true, message: '积分数不能为空', trigger: 'blur'}" 
                        style="display: inline-block">
                          <el-input 
                            v-model.trim="item.currentDayIntegral" 
                            class="input-width" 
                            placeholder="请输入积分数"
                            @input="e => (item.currentDayIntegral = checkInput(e))"
                            :disabled="type == 2">
                          </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="10" class="col-text">
                      <el-form-item 
                        label="" 
                        :prop="'scoreData.' + index + '.continuousReward'" 
                        :rules="{ required: true, message: '连签奖励数不能为空', trigger: 'blur'}" 
                        style="display: inline-block">
                          <el-input 
                            v-model.trim="item.continuousReward" 
                            class="input-width" 
                            placeholder="请输入连签奖励"
                            @input="e => (item.continuousReward = checkInput(e))"
                            :disabled="type == 2">
                          </el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </el-form-item>
          </el-form>
        </div>
        <!-- 订单送积分 -->
        <div v-if="form.id != '' && form.behaviorId == 1">
          <el-form 
            :model="order" 
            :rules="orderRules" 
            ref="orderForm" 
            label-width="130px" 
            class="demo-ruleForm">
            <h4>订单送积分：</h4>
            <el-form-item label="商家范围：" prop="merchantScope">
              <el-radio-group v-model="order.merchantScope" :disabled="type == 2">
                <el-radio :label="1">全部商家</el-radio>
                <el-radio :label="2">指定商家</el-radio>
                <yl-button type="text" v-if="order.merchantScope == 2" @click="setupClick(1)">设置商家<span v-if="order.sellerNum">( {{ order.sellerNum }} )</span></yl-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="商品范围：" prop="goodsScope">
              <el-radio-group v-model="order.goodsScope" :disabled="type == 2">
                <el-radio :label="1">全部商品</el-radio>
                <el-radio :label="2">指定平台SKU</el-radio>
                <yl-button 
                  type="text" 
                  v-if="order.goodsScope == 2" 
                  @click="setupClick(2)"
                  style="margin-right: 10px">
                  设置<span v-if="order.platformGoodsNum">( {{ order.platformGoodsNum }} )</span>
                </yl-button>
                <el-radio :label="3">指定店铺SKU</el-radio>
                <yl-button 
                  type="text" 
                  v-if="order.goodsScope == 3" 
                  @click="setupClick(3)">
                  设置<span v-if="order.enterpriseGoodsNum">( {{ order.enterpriseGoodsNum }} )</span>
                </yl-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="客户范围：" prop="customerScope">
              <el-radio-group v-model="order.customerScope" :disabled="type == 2">
                <el-radio :label="1">全部客户</el-radio>
                <el-radio :label="2">指定客户</el-radio>
                <yl-button 
                  type="text" 
                  v-if="order.customerScope == 2" 
                  @click="setupClick(4)"
                  style="margin-right: 10px">
                  设置<span v-if="order.customerNum">( {{ order.customerNum }} )</span>
                </yl-button>
                <el-radio :label="3">指定客户范围</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 指定范围客户 -->
            <div class="section-view" v-if="order.customerScope == 3">
              <el-form-item label="企业类型" prop="enterpriseType">
                <el-radio-group v-model="order.enterpriseType" :disabled="type == 2">
                  <el-radio :label="1">全部类型</el-radio>
                  <el-radio :label="2">指定类型</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="" v-if="order.enterpriseType == 2" prop="enterpriseTypeList">
                <el-checkbox-group v-model="order.enterpriseTypeList" :disabled="type == 2">
                  <el-checkbox
                    v-for="item in enterpriseData"
                    v-show="item.value != 1 && item.value != 2"
                    :key="item.value"
                    :label="item.value">
                    {{ item.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="用户类型" prop="userType">
                <el-radio-group v-model="order.userType" :disabled="type == 2">
                  <el-radio :label="1">全部用户</el-radio>
                  <el-radio :label="2">普通用户</el-radio>
                  <el-radio :label="3">全部会员</el-radio>
                  <el-radio :label="4">部分会员</el-radio>
                  <yl-button 
                    type="text" 
                    v-if="order.userType == 4" 
                    @click="setupClick(5)"
                    >
                    设置<span v-if="order.memberNum">( {{ order.memberNum }} )</span>
                  </yl-button>
                </el-radio-group>
              </el-form-item>
            </div>
            <el-form-item label="支付方式：" prop="paymentMethodFlag">
              <el-radio-group v-model="order.paymentMethodFlag" :disabled="type == 2">
                <el-radio :label="1">全部支付方式</el-radio>
                <el-radio :label="2">指定支付方式</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="" v-if="order.paymentMethodFlag == 2" prop="paymentMethodList">
              <el-checkbox-group v-model="order.paymentMethodList" :disabled="type == 2">
                <el-checkbox
                  v-for="item in integralPaymentMethodData"
                  :key="item.value"
                  :label="item.value"
                >{{ item.label }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
          <div class="basicInfo">
            <yl-button type="primary" @click="orderClick" v-if="type != 2">订单送积分保存</yl-button>
          </div>
        </div>
        <!-- 积分倍数配置表格 -->
        <div v-if="form.id != '' && form.behaviorId == 1 && orderFormModer.list.length > 0">
          <el-form 
            :model="orderFormModer" 
            :rules="orderFormRules" 
            ref="orderFormRef" 
            label-width="130px" 
            class="demo-ruleForm">
            <h4>积分倍数配置表格：</h4>
            <el-form-item label="">
              <div class="score-to-configure">
                <el-row>
                  <el-col :span="4" class="col-text col-color">
                    用户类型
                  </el-col>
                  <el-col :span="10" class="col-text col-color">
                    支付方式
                  </el-col>
                  <el-col :span="10" class="col-text col-color">
                    积分倍数
                  </el-col>
                </el-row>
                <el-row class="row-bottom" v-for="(item, index) in orderFormModer.list" :key="index">
                  <el-col :span="4" class="col-text col-color">
                    {{ item.userTypeName }}
                  </el-col>
                  <el-col :span="10" class="col-text">
                    <span v-if="item.paymentMethod == 0 ">全部支付方式</span>
                    <span v-else>{{ item.paymentMethod | dictLabel(integralPaymentMethodData) }}</span>
                  </el-col>
                  <el-col :span="10" class="col-text">
                    <el-form-item 
                      label="" 
                      :prop="'list.' + index + '.integralMultiple'" 
                      :rules="{ required: true, message: '积分倍数不能为空', trigger: 'blur'}" 
                      style="display: inline-block">
                        <el-input 
                          v-model.trim="item.integralMultiple" 
                          class="input-width" 
                          placeholder="请输入积分倍数"
                          @keyup.native="item.integralMultiple = onInput(item.integralMultiple, 2)"
                          :disabled="type == 2">
                        </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" v-if="form.id != '' && form.behaviorId == 2 && type != 2" @click="signinClick">签到送积分保存</yl-button>
      <yl-button type="primary" v-if="form.id != '' && form.behaviorId == 1 && type != 2" @click="orderFormClick">积分倍数配置表提交</yl-button>
    </div>
    <!-- 选择行为 -->
    <behavior-pop-up 
      :show="show" 
      :data-list="dataList" 
      @close="close" 
      @addDoctor="addDoctor"
      :data-type="dataType"
      v-if="show">
    </behavior-pop-up>
    <!-- 设置商家 -->
    <add-business 
      v-if="businessShow" 
      @selectNumChange="businessChange" 
      ref="businessRef">
    </add-business>
    <!-- 指定平台sku -->
    <add-specify-platform 
      v-if="specifyPlatformShow" 
      ref="specifyPlatformRef" 
      @specifyPlatformChange="specifyPlatformChange">
    </add-specify-platform>
    <!-- 指定店铺sku -->
    <add-designated-store 
      v-if="designatedStoreShow" 
      ref="designatedStoreRef" 
      @designatedStoreChange="designatedStoreChange">
    </add-designated-store>
    <!-- 指定客户 -->
    <add-customer 
      v-if="customerShow" 
      @customerChange="customerChange" 
      ref="customerRef">
    </add-customer>
    <!-- 部分会员 -->
    <add-member 
      v-if="memberShow" 
      ref="memberRef"
      @memberChange="memberChange">
    </add-member>
   
  </div>
</template>
<script>
import { enterpriseType } from '@/subject/admin/utils/busi'
import behaviorPopUp from '../../components/behaviorPopUp'
import addBusiness from '../../components/addBusiness'
import addSpecifyPlatform from '../../components/addSpecifyPlatform'
import addDesignatedStore from '../../components/addDesignatedStore'
import addCustomer from '../../components/addCustomer'
import addMember from '../../components/addMember'
import { 
  saveBasic, 
  saveSignPeriod, 
  getDistributionRules, 
  saveOrderGiveIntegral, 
  generateMultipleConfig,
  saveMultipleConfig
} from '@/subject/admin/api/b2b_api/integral'
import { formatDate } from '@/subject/admin/utils'
import { integralPaymentMethod } from '@/subject/admin/busi/b2b/integral'
import { onInputLimit } from '@/common/utils'
export default {
  name: 'GrantManageEstablish',
  components: {
    behaviorPopUp,
    addBusiness,
    addSpecifyPlatform,
    addDesignatedStore,
    addCustomer,
    addMember
  },
  computed: {
    enterpriseData() {
      return enterpriseType()
    },
    //支付方式
    integralPaymentMethodData() {
      return integralPaymentMethod()
    }
  },
  data() {
    return {
      form: {
        //基本信息
        id: '',
        name: '',
        time: [],
        description: '',
        behaviorId: ''
      },
      //签到送积分
      signin: {
        signPeriod: '',
        scoreData: []
      },
      //订单送积分
      order: {
        //商家范围：1-全部商家 2-指定商家
        merchantScope: '',
        sellerNum: 0,
        //商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
        goodsScope: '',
        //指定平台sku数量
        platformGoodsNum: 0,
        //指定店铺sku数量
        enterpriseGoodsNum: 0,
        //客户范围的用户类型：1-全部用户 2-指定客户 3-指定客户范围
        customerScope: '',
        //指定客户 的数量
        customerNum: 0,
        //指定客户范围 的企业类型：1-全部类型 2-指定类型
        enterpriseType: '',
        //指定范围企业类型集合（企业类型：3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所，参考企业类型的字典即可）
        enterpriseTypeList: [],
        //指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
        userType: '',
        //用户类型为 4时 部分会员 设置
        memberNum: 0,
        //是否区分支付方式
        paymentMethodFlag: '',
        //支付方式 1-线上支付 2-线下支付 3-账期支付
        paymentMethodList: []
      },
      //订单配置表
      orderFormModer: {
        list: []
      },
      show: false,
      rules: {
        name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择生效时间', trigger: 'change' }],
        description: [{ required: true, message: '请输入规则说明', trigger: 'blur' }],
        behaviorId: [{ required: true, message: '请选择行为', trigger: 'change' }]
      },
      signinRules: {
        signPeriod: [{ required: true, message: '请输入签到周期', trigger: 'blur' }]
      },
      orderRules: {
        merchantScope: [{ required: true, message: '请选择商家范围', trigger: 'change' }],
        goodsScope: [{ required: true, message: '请选择商品范围', trigger: 'change' }],
        customerScope: [{ required: true, message: '请选择客户范围', trigger: 'change' }],
        enterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        userType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        paymentMethodFlag: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
        paymentMethodList: [{ required: true, message: '请选择指定支付方式', trigger: 'change' }],
        enterpriseTypeList: [{ required: true, message: '请选择指定类型下的企业类型', trigger: 'change' }]
      },
      orderFormRules: {},
      //传递给子组件的数据
      dataList: [],
      //选择行为传递给子组件的数据
      dataType: {
        //平台：1-B2B
        platform: 1,
        //行为类型：1-发放 2-消耗
        type: 1
      },
      // 选择当前日期之后的时间
      pickerOptions: {
        disabledDate(time) {
          //如果没有后面的-8.64e7就是不可以选择今天的
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      //1 创建 2查看 3编辑
      type: 1,
      //商家范围弹窗是否显示
      businessShow: false,
      //指定平台设置弹窗是否显示
      specifyPlatformShow: false,
      //指定店铺设置弹窗是否显示
      designatedStoreShow: false,
      //指定客户 设置弹窗是否显示
      customerShow: false,
      //部分会员 设置弹窗是否显示
      memberShow: false,
      //基本信息是否保存成功过
      basicInform: false
    }
  },
  watch: {
    
  },
  mounted() {
    let query = this.$route.params;
    // query.type  //1 创建 2查看 3编辑
    if (query.type && query.type != 1) {
      this.type = query.type
      this.getData(query.id)
      if (query.type == 3) {
        this.basicInform = true
      }
    }
  },
  methods: {
    blur() {
      if (this.signin.signPeriod < 5) {
        this.$common.warn('请输入5～31整数')
        this.signin.signPeriod = ''
      } else if (this.signin.signPeriod > 30) {
        this.$common.warn('请输入5～31整数')
        this.signin.signPeriod = ''
      } else {
        if (this.signin.scoreData && this.signin.scoreData.length > 4) {
          this.$confirm('修改天数会清空下方已配积分值，确认修改?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( () => {
            this.signin.scoreData = []
            for (let i = 0; i < this.signin.signPeriod; i ++) {
              this.signin.scoreData.push({
                //天数
                days: '',
                //当天发放积分数
                currentDayIntegral: '',
                //连签奖励
                continuousReward: ''
              })
            }
          })
          .catch(() => {
            this.signin.signPeriod = this.signin.scoreData.length
          });
        } else {
          this.signin.scoreData = []
          for (let i = 0; i < this.signin.signPeriod; i ++) {
            this.signin.scoreData.push({
              //天数
              days: '',
              //当天发放积分数
              currentDayIntegral: '',
              //连签奖励
              continuousReward: ''
            })
          }
        }
      }
    },
    //设置商家返回的数据量
    businessChange(val) {
      this.$set(this.order, 'sellerNum', val)
    },
    //设置平台sku返回的数据量
    specifyPlatformChange(val) {
      this.$set(this.order, 'platformGoodsNum', val)
    },
    //设置指定店铺sku返回的数据量
    designatedStoreChange(val) {
      this.$set(this.order, 'enterpriseGoodsNum', val)
    },
    //指定客户 返回的数据量
    customerChange(val) {
      this.$set(this.order, 'customerNum', val)
    },
    //部分会员 返回的数据量
    memberChange(val) {
      this.$set(this.order, 'memberNum', val)
    },
    //选择行为操作
    //点击添加
    addClick() {
      this.show = true
    },
    // 关闭弹窗
    close() {
      this.show = false
    },
    addDoctor(row) {
      this.dataList = []
      this.dataList.push({ ...row})
      this.form.behaviorId = row.id
      this.close();
    },
    //删除行为
    drugClose(index) {
      this.form.behaviorId = ''
      this.dataList.splice(index, 1)
    },
    
    //获取发放规则详情信息
    async getData(id) {
      let data = await getDistributionRules(id)
      if (data) {
        this.form = {
          ...data
        }
        this.$nextTick(() => {
          if (data.startTime && data.endTime && data.startTime != -28800000) {
            this.$set(this.form, 'time', [formatDate(data.startTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.endTime, 'yyyy-MM-dd hh:mm:ss')]);
          } else {
            this.$set(this.form, 'time', [])
          }
          this.dataList = [{
            id: data.behaviorId,
            name: data.behaviorName
          }]
        });
        //订单 送积分
        if (data.behaviorId == 1) {
          if (data.orderGiveConfig) {
            this.order = {
              ...data.orderGiveConfig
            }
          }
          //订单配置表格
          this.orderFormModer.list = data.orderGiveMultipleConfigList
        }
        //签到送积分
        if (data.behaviorId == 2) {
          this.signin.signPeriod = data.signPeriod ? data.signPeriod : '';
          this.$nextTick(() => {
            this.signin.scoreData = data.periodConfigList
          });
        }
      }
    },
    //点击基本信息保存
    basicInfoClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveBasic(
            form.name,
            form.time && form.time.length > 0 ? form.time[0] : '',
            form.time && form.time.length > 1 ? form.time[1] : '',
            form.description,
            form.behaviorId,
            form.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            if (this.type == 1) {
              this.$common.n_success('基本信息保存成功')
            }
            this.basicInform = true
            this.form.id = data.id
          }
        }
      })
    },
    //点击签到送积分保存
    signinClick() {
      //先校验基本信息
      if (this.basicInform) {
        this.$refs['signinForm'].validate( async(valid) => {
          if (valid) {
            let signin = this.signin;
            this.$common.showLoad();
            let data = await saveSignPeriod(
              this.form.id,
              signin.scoreData,
              signin.signPeriod
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        })
      } else {
        this.$common.warn('请先保存基本信息')
      } 
    },
    //点击 订单送积分保存
    orderClick() {
      //先校验基本信息
      if (this.basicInform) {
        this.$refs['orderForm'].validate( async(valid) => {
          if (valid) {
            //配置是否可以请求接口
            let show = false;
            let order = this.order;
            if (order.merchantScope == 2) {
              if (order.sellerNum && order.sellerNum != '') {
                show = true
              } else {
                show = false
                this.$common.warn('请设置商家')
              }
            } else {
              show = true
            }
            if (show) {
              if (order.goodsScope == 2) {
                if (order.platformGoodsNum && order.platformGoodsNum != '') {
                  show = true
                } else {
                  show = false
                  this.$common.warn('请设置指定平台SKU')
                }
              }
            }
            if (show) {
              if (order.goodsScope == 3) {
                if (order.enterpriseGoodsNum && order.enterpriseGoodsNum != '') {
                  show = true
                } else {
                  show = false
                  this.$common.warn('请设置指定店铺SKU')
                }
              }
            }
            if (show) {
              if (order.customerScope == 2) {
                if (order.customerNum && order.customerNum != '') {
                  show = true
                } else {
                  show = false
                  this.$common.warn('请设置指定客户')
                }
              }
            }
            if (show) {
              if (order.customerScope == 3) {
                if (order.enterpriseType == 2) {
                 if (order.enterpriseTypeList && order.enterpriseTypeList.length > 0) {
                  show = true
                 } else {
                  show = false
                  this.$common.warn('请设置企业指定类型')
                 }
                }
              }
            }
            if (show) {
              if (order.customerScope == 3) {
                if (order.userType == 4) {
                 if (order.memberNum && order.memberNum != '') {
                  show = true
                 } else {
                  show = false
                  this.$common.warn('请设置部分会员')
                 }
                }
              }
            }
            if (show) {
              this.$common.showLoad();
              let data = await saveOrderGiveIntegral(
                this.form.id,
                order.merchantScope,
                order.goodsScope,
                order.customerScope,
                order.enterpriseType,
                order.enterpriseTypeList,
                order.userType,
                order.paymentMethodFlag,
                order.paymentMethodList
              )
              this.$common.hideLoad();
              if (data !== undefined) {
                this.$common.n_success('订单送积分保存成功！');
                //获取订单配置表格
                this.orderForm();
              }
            }
            
          }
        })
      } else {
        this.$common.warn('请先保存基本信息')
      }
    },
    async orderForm() {
      let data = await generateMultipleConfig(
        this.form.id
      )
      if (data) {
        this.orderFormModer.list = data.list
      }
    },
    //积分倍数配置表保存
    orderFormClick() {
      //先校验基本信息
      this.basicInfoClick();
      if (this.basicInform) {
        this.$refs['orderFormRef'].validate( async(valid) => {
          if (valid) {
            this.$common.showLoad();
            let data = await saveMultipleConfig(
              this.form.id,
              this.orderFormModer.list
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('积分倍数配置表提交成功', r => {
                this.$router.go(-1)
              })
            }
          }
        })
      } else {
        this.$common.warn('请先保存基本信息')
      }
    },
    //点击设置 1 设置商家 2指定平台sku 3指定店铺sku 4指定客户 5 部分会员
    setupClick(val) {
      //参数一：积分id 参数二：弹窗显示 参数三：商家范围
      switch (val) {
        case 1:
          this.businessShow = true;
          this.$nextTick(() => {
            this.$refs.businessRef.init(this.form.id, true, this.type)
          })
          break;
        case 2: 
          this.specifyPlatformShow = true;
          this.$nextTick(() => {
            this.$refs.specifyPlatformRef.init(this.form.id, true, this.type)
          })
          break;
        case 3:
          this.designatedStoreShow = true;
          this.$nextTick(() => {
            this.$refs.designatedStoreRef.init(this.form.id, true, this.type, this.order.merchantScope)
          })
          break;
        case 4:
          this.customerShow = true;
          this.$nextTick(() => {
            this.$refs.customerRef.init(this.form.id, true, this.type)
          }) 
          break;
        case 5:
          this.memberShow = true;
          this.$nextTick(() => {
            this.$refs.memberRef.init(this.form.id, true, this.type)
          }) 
          break;
        default:
          break;
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 0) {
        val = ''
      }
      return val
    },
    //5~ 31的整数
    signPeriodCheckInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val > 31) {
        val = ''
      }
      return val
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
