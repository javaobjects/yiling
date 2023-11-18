<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 基本信息 -->
      <div class="common-box">
        <div class="title-box">
          <div class="title-wrap">
            <div class="sign"></div>
            <div class="title">基本信息</div>
          </div>
        </div>
        <div class="basic-info-box">
          <el-row>
            <el-col :span="6">
              <span class="info-title">采购关系编号:</span>
              <span class="info-value">{{ basicInfoData.procRelationNumber }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">渠道商:</span>
              <span class="info-value">{{ basicInfoData.channelPartnerName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">渠道商类型:</span>
              <span class="info-value">{{ basicInfoData.channelPartnerChannelId | dictLabel(popProcChannelType) }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">配送类型:</span>
              <span class="info-value">{{ basicInfoData.deliveryType | dictLabel(popProcRelaDeliType) }}</span>
            </el-col>
          </el-row>
          <el-row class="mar-t-8">
            <el-col :span="6">
              <span class="info-title">工业主体:</span>
              <span class="info-value">{{ basicInfoData.factoryName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">配送商:</span>
              <span class="info-value">{{ basicInfoData.deliveryName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="info-title">履约时间:</span>
              <span class="info-value">{{ basicInfoData.startTime | formatDate('yyyy-MM-dd') }} - {{ basicInfoData.endTime | formatDate('yyyy-MM-dd') }}</span>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 可采商品 -->
      <div class="common-box mar-t-16">
        <div class="title-box">
          <div class="title-wrap">
            <div class="sign"></div>
            <div class="title">可采商品</div>
          </div>
        </div>
        <div class="search-btn-wrap">
          <div class="search-wrap"></div>
          <div class="btn-wrap">
            <yl-button type="primary" plain :disabled="!multipleSelection.length" @click="handleSetRebate">批量设置折扣</yl-button>
            <yl-button type="primary" plain @click="handleAddGoods">添加可采商品</yl-button>
          </div>
        </div>
        <div class="form-info-box table-box">
          <yl-table
            class="table-data-box"
            ref="table"
            border
            :list="basicInfoData.goodsList"
            show-header
            :cell-no-pad="true"
            :selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column label="专利类型" min-width="80" align="center" prop="isPatent">
              <template slot-scope="{ row }">
                <div>{{ row.isPatent | dictLabel(isPatentOption) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名" min-width="140" align="center" prop="goodsName"></el-table-column>
            <el-table-column label="商品规格" min-width="120" align="center" prop="sellSpecifications" ></el-table-column>
            <el-table-column label="批准文号" min-width="120" align="center" prop="licenseNo" ></el-table-column>
            <el-table-column label="商品优惠折扣%" min-width="80" align="center" prop="rebate" ></el-table-column>
            <el-table-column label="操作" min-width="90" align="center">
              <template slot-scope="{ row, $index }">
                <div>
                  <yl-button :disabled="row.id && row.id !== ''" type="text" @click="handleDeleteGoods(row, $index)" >移除</yl-button >
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 底部按钮 -->
      <div class="bottom-bar-view flex-row-center">
        <yl-button class="custom-btn" type="primary" plain @click="$router.go(-1)" >返回</yl-button >
        <yl-button class="custom-btn" type="primary" @mousedown.native="handleSaveSubmit">保存</yl-button>
      </div>
      <!-- 选择商品 -->
      <choose-product ref="choose" :id="relationId" :choose-list="basicInfoData.goodsList" @selectItem="selectItem" />
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
import { queryProcRelationDetail, querySaveProcRelationGoods } from '@/subject/pop/api/establish_purchase'
import { popProcRelaStatus, popProcChannelType, popProcRelaDeliType } from '@/subject/pop/busi/pop/establish_purchase'

export default {
  name: 'PurchaseManageEdit',
  components: {
    ChooseProduct
  },
  computed: {
    // 渠道类型
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
    this.relationId = this.$route.params.id
    if (this.relationId) {
      this.getBasicDetailData()
    }
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
          title: '编辑'
        }
      ],
      relationId: '',
      isPatentOption: [
        { label: '非专利', value: 1 },
        { label: '专利', value: 2 }
      ],
      // 建采关系详情数据
      basicInfoData: {
        goodsList: []
      },
      multipleSelection: [],
      // 设置优惠比例
      rebateDialogShow: false,
      rebateForm: {
        // 折扣比例
        rebate: ''
      },
      rebateFormRules: {
        rebate: [{ required: true, validator: isRebateValidator, trigger: ['blur', 'change'] }]
      }
    }
  },
  methods: {
    // 获取基本信息详情
    async getBasicDetailData() {
      this.$common.showLoad()
      let data = await queryProcRelationDetail(this.relationId)
      this.$common.hideLoad()
      if (data) {
        this.basicInfoData = data
      }
    },
    // 添加可采商品
    handleAddGoods() {
      this.$refs.choose.openGoodsDialog()
    },
    // 添加商品到列表
    selectItem(data) {
      this.basicInfoData.goodsList = this.basicInfoData.goodsList.concat(data)
    },
    // 删除商品
    handleDeleteGoods(row, index) {
      this.basicInfoData.goodsList.splice(index, 1)
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
    // 保存采购关系
    async handleSaveSubmit() {
      if (this.basicInfoData.goodsList.length === 0) {
        return this.$common.warn('请添加可采商品！')
      }
      this.$common.showLoad()
      let data = await querySaveProcRelationGoods(
        this.relationId,
        this.basicInfoData.goodsList
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
