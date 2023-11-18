<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 保存基本信息 -->
      <div class="add-form-box">
        <el-form
          class="add-form"
          :model="basicInfoForm"
          :rules="basicInfoFormRules"
          ref="basicInfoFormRef"
          label-position="top"
        >
          <!-- 基本信息 -->
          <div class="common-box info-box">
            <div class="title-box">
              <div class="title-wrap">
                <div class="sign"></div>
                <div class="title">基本信息</div>
              </div>
            </div>
            <div class="form-info-box">
              <el-row class="row-box">
                <el-col :span="6">
                  <el-form-item label="渠道商" prop="channelPartnerEid">
                    <div class="flex-box">
                      <el-select
                        v-model="basicInfoForm.channelPartnerEid"
                        clearable
                        filterable
                        remote
                        :disabled="procRelationId !== ''"
                        :remote-method="getChannelPartnerData"
                        @clear="getChannelPartnerData"
                        @change="handleSelectChannel"
                        :loading="channelLoading"
                        placeholder="请搜索并选择渠道商"
                      >
                        <el-option v-for="item in chennelOption" :key="item.id" :label="item.customerName" :value="item.customerEid">
                        </el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="渠道类型" prop="channelId">
                    <div class="flex-box">
                      <el-select disabled v-model="basicInfoForm.channelId" placeholder="请选择渠道类型">
                        <el-option label="全部" :value="0"></el-option>
                        <el-option
                          v-for="item in popProcChannelType"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        ></el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="配送类型" prop="deliveryType">
                    <div class="flex-box">
                      <el-radio-group v-model="basicInfoForm.deliveryType" @change="handleSelectDeliveryType" :disabled="procRelationId !== ''">
                        <el-radio v-for="item in popProcRelaDeliType" :label="item.value" :key="item.value">{{ item.label }}</el-radio>
                      </el-radio-group>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="工业主体" prop="factoryEid">
                    <div class="flex-box">
                      <el-select v-model="basicInfoForm.factoryEid" @change="handleSelectMainPart" :disabled="procRelationId !== ''" placeholder="请选择工业主体">
                        <el-option
                          v-for="item in mainPartOption"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="row-box">
                <el-col :span="6">
                  <el-form-item label="配送商" prop="deliveryName" :rules="basicInfoForm.deliveryType === 2 ? basicInfoFormRules.deliveryName : {}">
                    <div class="flex-box">
                      <el-select
                        v-model="basicInfoForm.deliveryName"
                        clearable
                        filterable
                        remote
                        :disabled="basicInfoForm.deliveryType === 1 || procRelationId !== ''"
                        :remote-method="getDeliveryPartnerPage"
                        @clear="getDeliveryPartnerPage"
                        @change="handleSelectDelivery"
                        :loading="deliveryLoading"
                        placeholder="请搜索并选择配送商"
                      >
                        <el-option v-for="item in deliveryOption" :key="item.id" :label="item.name" :value="item.name">
                        </el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="履约时间" prop="timeRange">
                    <div class="flex-box">
                      <el-date-picker
                        v-model="basicInfoForm.timeRange"
                        :disabled="procRelationId !== ''"
                        type="daterange"
                        format="yyyy/MM/dd"
                        value-format="yyyy-MM-dd"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :picker-options="pickerOptions"
                      >
                      </el-date-picker>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="row-box">
                <el-col :span="6">
                  <yl-button class="custom-btn" type="primary" :disabled="procRelationId !== ''" @mousedown.native="handleBasicSave">保存</yl-button>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 可采商品 -->
      <div class="add-form-box mar-t-16">
        <!-- 可采商品 -->
        <div class="common-box info-box mar-t-16">
          <div class="title-box">
            <div class="title-wrap">
              <div class="sign"></div>
              <div class="title">可采商品</div>
            </div>
            <div class="btn-wrap">
              <yl-button type="primary" :disabled="!multipleSelection.length" plain @click="handleSetRebate">批量设置折扣</yl-button>
              <yl-button type="primary" plain @click="handleAddGoods">添加可采商品</yl-button>
            </div>
          </div>
          <div class="form-info-box table-box">
            <yl-table class="table-data-box" ref="table" border :list="dataList" :selection-change="handleSelectionChange" show-header :cell-no-pad="true">
              <el-table-column type="selection" width="55"> </el-table-column>
              <el-table-column label="专利类型" min-width="140" align="center" prop="isPatent">
                <template slot-scope="{ row }">
                  <div>{{ row.isPatent | dictLabel(isPatentOption) }}</div>
                </template>
              </el-table-column>
              <el-table-column label="商品名" min-width="80" align="center" prop="goodsName"></el-table-column>
              <el-table-column label="商品规格" min-width="80" align="center" prop="sellSpecifications"></el-table-column>
              <el-table-column label="批准文号" min-width="120" align="center" prop="licenseNo"></el-table-column>
              <el-table-column label="商品优惠折扣%" min-width="100" align="center" prop="rebate" ></el-table-column>
              <el-table-column label="操作" min-width="90" align="center">
                <template slot-scope="{ row, $index }">
                  <div>
                    <yl-button type="text" @click="handleDeleteGoods(row, $index)">移除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <!-- 底部按钮 -->
      <div class="bottom-bar-view flex-row-center">
        <yl-button class="custom-btn" type="primary" plain @click="$router.go(-1)" >返回</yl-button >
        <yl-button class="custom-btn" type="primary" @mousedown.native="handleSaveSubmit">保存</yl-button>
      </div>
      <!-- 选择商品 -->
      <choose-product ref="choose" :id="procRelationId" :choose-list="dataList" @selectItem="selectItem" />
      <!-- 设置折扣比例 -->
      <yl-dialog
        :visible.sync="rebateDialogShow"
        width="480px"
        title="批量设置折扣"
        type="add"
        :show-cancle="false"
        @confirm="confirmSetRebate"
        @close="resetSetRebate"
      >
        <div class="dialog-content">
          <el-form :model="rebateForm" class="rebateForm" ref="rebateFormRef" :rules="rebateFormRules" label-position="left" label-width="120px">
            <el-form-item label="商品优惠比例%" prop="rebate">
              <el-input v-model="rebateForm.rebate" placeholder="请输入商品优惠比例"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <template slot="left-btn">
          <yl-button plain @click="resetSetRebate">取消</yl-button>
        </template>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import ChooseProduct from '../component/ChooseProduct.vue'
import { queryChannelPartnerPage, queryProcurementRelationMainPart, queryDeliveryPartnerPage, querySaveProcurementRelation, querySaveProcRelationGoods } from '@/subject/pop/api/establish_purchase'
import { popProcRelaStatus, popProcChannelType, popProcRelaDeliType } from '@/subject/pop/busi/pop/establish_purchase'

export default {
  name: 'PurchaseManageAdd',
  components: {
    ChooseProduct
  },
  computed: {
    popProcChannelType() {
      return popProcChannelType()
    },
    // 配送类型
    popProcRelaDeliType() {
      return popProcRelaDeliType()
    },
    // 状态
    popProcRelaStatus() {
      return popProcRelaStatus()
    }
  },
  mounted() {
    this.getProcurementRelationMainPart()
  },
  data() {
    const isRebateValidator = (rule, value, callback) => {
      var pattern = /^([0-9]{1,2}$)|(^[0-9]{1,2}\.[0-9]{1,2}$)|100$/
      if (value >= 100 || value < 0 || !pattern.test(value)) {
        return callback(new Error('请输入优惠比例并不超过两位小数'))
      } else {
        return callback()
      }
    }
    return {
      // 头部导航
      navList: [
        {
          title: '建采管理'
        },
        {
          title: '建采管理',
          path: '/establish_purchase/purchase_manage'
        },
        {
          title: '新增'
        }
      ],
      // 基本信息表单ID
      procRelationId: '',
      // 工业主体下拉选项
      mainPartOption: [],
      channelLoading: false,
      // 渠道商选项
      chennelOption: [],
      deliveryLoading: false,
      // 配送商选项
      deliveryOption: [],
      pickerOptions: {
        disabledDate: time => {
          return time.getTime() < new Date() - 8.64e7
        }
      },
      // 基本信息表单
      basicInfoForm: {
        // 渠道商eid
        channelPartnerEid: '',
        // 渠道商名称
        channelPartnerName: '',
        // 渠道类型
        channelId: '',
        // 配送类型：1-工业直配 2-三方配送
        deliveryType: '',
        // 工业主体eid
        factoryEid: '',
        // 工业主体名称
        factoryName: '',
        // 配送商eid
        deliveryEid: '',
        // 配送商名称
        deliveryName: '',
        timeRange: []
      },
      // 商业公司基本信息规则
      basicInfoFormRules: {
        channelPartnerEid: [{ required: true, message: '请选择渠道商', trigger: 'blur' }],
        deliveryType: [{ required: true, message: '请选择配送类型', trigger: 'blur' }],
        factoryEid: [{ required: true, message: '请选择工业主体', trigger: 'blur' }],
        deliveryName: [{ required: true, message: '请选择配送商', trigger: 'blur' }],
        timeRange: [{ required: true, message: '请选择履约时间', trigger: 'blur' }]
      },
      isPatentOption: [
        { label: '非专利', value: 1 },
        { label: '专利', value: 2 }
      ],
      // 多选
      multipleSelection: [],
      // 设置优惠比例
      rebateDialogShow: false,
      rebateForm: {
        // 折扣比例
        rebate: ''
      },
      rebateFormRules: {
        rebate: [{ required: true, validator: isRebateValidator, trigger: ['blur', 'change'] }]
      },
      dataList: []
    }
  },
  methods: {
    // 获取工业主体下拉选项数据
    async getProcurementRelationMainPart() {
      let data = await queryProcurementRelationMainPart()
      if (data) {
        this.mainPartOption = data
      }
    },
    // 选择工业主体
    handleSelectMainPart(value) {
      this.basicInfoForm.factoryName = this.mainPartOption.find(item => item.id === value).name
      if (this.basicInfoForm.deliveryType === 1) {
        this.basicInfoForm.deliveryEid = this.basicInfoForm.factoryEid
        this.basicInfoForm.deliveryName = this.basicInfoForm.factoryName
      }
    },
    // 获取渠道商下拉数据
    async getChannelPartnerData(query) {
      if (query) {
        this.channelLoading = true
        let data = await queryChannelPartnerPage(1, 10, query.trim())
        this.channelLoading = false
        if (data) {
          this.chennelOption = data.records
        }
      } else {
        this.chennelOption = []
        this.basicInfoForm.channelPartnerName = ''
        this.basicInfoForm.channelId = ''
      }
    },
    // 选择渠道商
    handleSelectChannel(value) {
      let selectChannel = this.chennelOption.find(item => item.customerEid === value)
      if (selectChannel) {
        this.basicInfoForm.channelPartnerName = selectChannel.customerName
        this.basicInfoForm.channelId = selectChannel.channelId
      }
    },
    // 获取配送商下拉数据
    async getDeliveryPartnerPage(query) {
      if (query) {
        this.deliveryLoading = true
        let data = await queryDeliveryPartnerPage( 1, 10, query.trim())
        this.deliveryLoading = false
        if (data) {
          this.deliveryOption = data.records
        }
      } else {
        this.deliveryOption = []
        this.basicInfoForm.deliveryName = ''
      }
    },
    // 选择配送商
    handleSelectDelivery(value) {
      let selectDelivery = this.deliveryOption.find(item => item.name === value)
      if (selectDelivery) {
        this.basicInfoForm.deliveryEid = selectDelivery.id
      }
    },
    handleSelectDeliveryType(value) {
      if (value === 1) {
        this.basicInfoForm.deliveryEid = this.basicInfoForm.factoryEid
        this.basicInfoForm.deliveryName = this.basicInfoForm.factoryName
        this.deliveryOption = []
      } else {
        this.basicInfoForm.deliveryEid = ''
        this.basicInfoForm.deliveryName = ''
      }
    },
    // 添加可采商品
    handleAddGoods() {
      if (!this.procRelationId) {
        return this.$common.warn('请先保存基本信息')
      }
      this.$refs.choose.openGoodsDialog()
    },
    // 保存基本信息
    handleBasicSave() {
      this.$refs.basicInfoFormRef.validate(async valid => {
        console.log('object', this.basicInfoForm);
        if (valid) {
          this.$common.showLoad()
          let data = await querySaveProcurementRelation(
            this.basicInfoForm.channelPartnerEid,
            this.basicInfoForm.channelPartnerName,
            this.basicInfoForm.deliveryType,
            this.basicInfoForm.factoryEid,
            this.basicInfoForm.factoryName,
            this.basicInfoForm.deliveryEid,
            this.basicInfoForm.deliveryName,
            this.basicInfoForm.timeRange && this.basicInfoForm.timeRange.length > 0 ? this.basicInfoForm.timeRange[0] : '',
            this.basicInfoForm.timeRange && this.basicInfoForm.timeRange.length > 1 ? this.basicInfoForm.timeRange[1] : ''
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.procRelationId = data
            this.$common.n_success('保存基本信息成功')
          }
        }
      })
    },
    // 获取基本信息详情
    async getBasicDetailData() {
      this.$common.showLoad()
      let data = await queryBasicSupplierDetail(this.basicInfoForm.id)
      this.$common.hideLoad()
      if (data) {
        this.basicInfoForm.id = data.id
        this.basicInfoForm.ylCode = data.ylCode
        this.basicInfoForm.licenseNumber = data.licenseNumber
        this.basicInfoForm.name = data.name
        this.basicInfoForm.provinceCode = data.provinceCode
        this.basicInfoForm.cityCode = data.cityCode
        this.basicInfoForm.regionCode = data.regionCode
        this.basicInfoForm.address = data.address
        this.basicInfoForm.distributionLicenseNumber = data.distributionLicenseNumber
        this.basicInfoForm.postalCode = data.postalCode
        this.basicInfoForm.phone = data.phone
        this.basicInfoForm.fax = data.fax
        this.basicInfoForm.businessCode = data.businessCode || ''
        this.basicInfoForm.code = data.code
        this.basicInfoForm.businessScope = data.businessScope
        this.basicInfoForm.businessRemark = data.businessRemark
        this.basicInfoForm.formerName = data.formerName
      }
      this.$nextTick(() => {
        this.$refs.chooseAddressRef.initData()
        this.$refs.basicInfoFormRef.clearValidate('businessCode')
      })
    },
    // 列表勾选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量设置折扣
    handleSetRebate() {
      this.rebateDialogShow = true
    },
    // 设置折扣
    confirmSetRebate() {
      this.$refs.rebateFormRef.validate(async valid => {
        if (valid) {
          this.multipleSelection.forEach(item => {
            item.rebate = this.rebateForm.rebate
          })
          this.rebateDialogShow = false
          this.rebateForm.rebate = ''
        }
      })
    },
    // 重置比例表单
    resetSetRebate() {
      this.rebateDialogShow = false
      this.rebateForm.rebate = ''
    },
    // 添加商品到列表
    selectItem(data) {
      this.dataList = this.dataList.concat(data)
    },
    // 删除商品
    handleDeleteGoods(row, index) {
      this.dataList.splice(index, 1)
    },
    // 保存采购关系
    async handleSaveSubmit() {
      if (!this.procRelationId) {
        return this.$common.warn('请先保存基本信息！')
      }
      if (this.dataList.length === 0) {
        return this.$common.warn('请添加可采商品！')
      }
      this.$common.showLoad()
      let data = await querySaveProcRelationGoods(
        this.procRelationId,
        this.dataList
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('操作成功')
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
<style lang="scss">
.long-custom-select {
  width: 260px;
}
</style>
