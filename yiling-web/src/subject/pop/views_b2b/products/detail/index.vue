<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <el-form ref="data" :model="data" :rules="rules" label-width="auto">
        <div class="common-box box-1 top-bar">
          <div class="header-bar mar-b-10">
            <div class="sign"></div>基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">商品名称：</span>{{ data.name }}</div>
            </el-col>
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo }}</div>
            </el-col>
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">包装规格：</span>{{ data.specifications }}</div>
            </el-col>
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">基本单位：</span>{{ data.unit }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">生产地址：</span>{{ data.manufacturerAddress }}</div>
            </el-col>
            <el-col :span="6" :offset="0">
              <div class="intro"><span class="font-title-color">生产厂家：</span>{{ data.manufacturer }}</div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <el-form-item label="生产日期：" prop="manufacturingDate">
                  <el-date-picker
                    :disabled="erpSyncLevel > 0"
                    v-model="data.manufacturingDate"
                    :picker-options="pickerOptions"
                    type="date"
                    placeholder="选择日期"
                    format="yyyy/MM/dd"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="intro">
                <el-form-item label="有效期至：" prop="expiryDate">
                  <el-date-picker
                    :disabled="erpSyncLevel > 0"
                    v-model="data.expiryDate"
                    type="date"
                    placeholder="选择日期"
                    format="yyyy/MM/dd"
                    value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <div class="header-bar mar-t-8">
            <div class="sign"></div>价格库存
          </div>
          <div class="mar-t-8">
            <!-- .toFixed(2) -->
            <div class="intro">
              <!-- <span class="font-title-color">商品价格：</span> -->
              <el-form-item label="商品价格：" size="normal" prop="price">
                <el-input :disabled="erpSyncLevel > 0" style="width:100px" v-model="data.price" placeholder="" size="normal"></el-input>
                元
              </el-form-item>
            </div>
            <div class="unit-box">
              <div>
                <div class="intro"><span class="font-title-color">销售规格：</span></div>
              </div>
              <div>
                <div class="unit-content mar-b-8" v-for="(item,index) in UnitList" :key="index">
                  <div class="unit-list">
                    <span class="unit-label">数量：</span>
                    <el-input :disabled="erpSyncLevel > 0" style="width:84px" v-model="item.packageNumber" placeholder="" size="normal"></el-input>
                    <span class="mar-l-8">盒</span>
                    <span class="mar-l-35 unit-label">商品库存：</span>
                    <el-input :disabled="erpSyncLevel > 0" style="width:84px" v-model="item.qty" placeholder="" size="normal"></el-input>
                    <span class="mar-l-8">盒</span>
                    <span class="mar-l-35">ERP内码：</span>
                    <el-input :disabled="erpSyncLevel > 0" style="width:84px" v-model="item.inSn" placeholder="" size="normal"></el-input>
                    <span class="mar-l-35">ERP编码：</span>
                    <el-input :disabled="erpSyncLevel > 0" style="width:84px" v-model="item.sn" placeholder="" size="normal"></el-input>
                    <span class="mar-l-35">备注：</span>
                    <el-input :disabled="erpSyncLevel > 0" style="width:165px" v-model="item.remark" placeholder="" size="normal"></el-input>
                  </div>
                  <div class="unit-icon" v-if="index+1 === UnitList.length" @click="addRow(item,index)">
                    <img src="@/subject/pop/assets/b2b-products/add.png" alt="">
                  </div>
                  <!-- <div class="unit-icon">
                    <img src="@/subject/pop/assets/b2b-products/unadd.png" alt="">
                  </div> -->
                  <div class="unit-icon" v-if="index+1 <= UnitList.length && UnitList.length>1 && !item.id">
                    <img src="@/subject/pop/assets/b2b-products/del.png" alt="" @click="delRow(item,index)">
                  </div>
                  <!-- <div class="unit-icon">
                    <img src="@/subject/pop/assets/b2b-products/undel.png" alt="">
                  </div> -->

                </div>
              </div>
            </div>
          </div>

          <div v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.picBasicsInfoList">
            <div class="header-bar mar-b-10">
              <div class="sign"></div>详细信息
            </div>
            <div class="img-box">
              <div class="intro"><span class="font-title-color">商品图片</span></div>
              <div class="flex-wrap imgs">
                <el-image v-for="(img, index) in data.standardGoodsAllInfo.picBasicsInfoList" :key="index" class="img" :src="img.picUrl" />
              </div>
              <div class="intro-box font-title-color font-size-base" v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.goodsInstructionsInfo">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">成分：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.drugDetails || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">性状：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.drugProperties || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">适应症：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.indications || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">用法用量：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.usageDosage || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">不良反应：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.adverseEvents || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">禁忌：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.contraindication || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">注意事项：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.noteEvents || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">药物相互作用：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.interreaction || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">保质期：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.shelfLife || '- -' }}</div>
                  </div>
                  <div class="box-view">
                    <div class="box">执行标准：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.executiveStandard || '- -' }}</div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">贮藏：{{ data.standardGoodsAllInfo.goodsInstructionsInfo.storageConditions || '- -' }}</div>
                  </div>
                  <div style="width: 506px;">
                  </div>
                </div>
              </div>
              <!-- 医疗器械详细信息 -->
              <div class="intro-box font-title-color font-size-base" v-if="data.standardGoodsAllInfo && data.standardGoodsAllInfo.medicalInstrumentInfo">
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      结构组成：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.structure || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      注意事项：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.noteEvents || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      适用范围：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.useScope || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      使用说明：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.usageDosage || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      储藏条件：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.storageConditions || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      包装：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.packingInstructions || '- -' }}
                    </div>
                  </div>
                </div>
                <div class="flex-row-left marb8">
                  <div class="box-view marr150">
                    <div class="box">
                      有效期：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.expirationDate || '- -' }}
                    </div>
                  </div>
                  <div class="box-view">
                    <div class="box">
                      备注：{{ data.standardGoodsAllInfo.medicalInstrumentInfo.remark || '- -' }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="header-bar mar-b-10">
            <div class="sign"></div>特殊信息
          </div>
          <el-form-item label="上下架状态：" prop="isPutOn">
            <el-radio-group v-model="isPutOn">
              <el-radio :label="1">上架</el-radio>
              <el-radio :label="2">下架</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </el-form>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveChange">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { b2bProductDetail, b2bProductEdit } from '@/subject/pop/api/b2b_api/b2b_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'
import { queryCurrentEnterpriseInfo } from '@/subject/pop/api/zt_api/dashboard'

export default {
  components: {},
  computed: {
    goodStatus() {
      return goodsStatus()
    },
    companyType() {
      let arr = enterpriseType()
      let arrs = arr.filter(function (item) {
        return item.value !== 1 && item.value !== 2
      })
      return arrs;
    },
    goodsReason() {
      return goodsOutReason()
    },
    // 专利非专利
    patent() {
      return goodsPatent()
    },
    isYiLing() {
      let user = getCurrentUser()
      let flag = null;
      if (user.currentEnterpriseInfo) {
        if (user.currentEnterpriseInfo.yilingFlag) {
          flag = !!user.currentEnterpriseInfo.yilingFlag
        }
      }
      return flag
    }
  },
  data() {
    var isPriceVlidator = (rule, value, callback) => {
      var pattern = /^\d+.?\d{0,2}$/;
      if (!pattern.test(value)) {
        return callback(new Error('价格请大于0并保留两位小数'))
      } else return callback()
    }
    var isQtyVlidator = (rule, value, callback) => {
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
      if (!reg.test(value)) {
        return callback(new Error('请输入正整数'))
      } else return callback()
    }
    var isPutOnVlidsator = (rule, value, callback) => {
      if (this.isPutOn === 1 || this.isPutOn === 2) {
        return callback()
      } else {
        return callback(new Error('请选择上架或下架商品'))
      }
    }

    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品列表',
          path: '/b2b_products/products_list'
        },
        {
          title: '商品详情'
        }
      ],
      data: {
        standardGoodsAllInfo: {
          baseInfo: {
            specificationInfo: []
          }
        }
      },
      type: '',
      rules: {
        price: [{ required: true, validator: isPriceVlidator, trigger: 'blur' }],
        qty: [{ required: true, validator: isQtyVlidator, trigger: 'blur' }],
        unit: [{ required: false, message: '请输入基本单位', trigger: 'blur' }],
        manufacturingDate: [{ required: true, message: '请选择生产日期', trigger: 'change' }],
        expiryDate: [{ required: true, message: '请选择有效期时间', trigger: 'change' }],
        isPutOn: [{ required: true, validator: isPutOnVlidsator, trigger: 'blur' }]
      },
      UnitList: [],
      isPutOn: '',
      pickerOptions: {
        disabledDate: time => {
          return time.getTime() > new Date().getTime()
        }
      },
      // 	企业是否开通erp 0：未开通 1：开通
      erpSyncLevel: 0
    }
  },
  mounted() {
    this.id = this.$route.params.id
    this.getData()
    this.$nextTick(() => {
      // type: see -只查看 reSubmit -重新提交 为空则为编辑
      this.type = this.$route.query.type
    })
    this.$log(this.$route)
    this.getCurrentEnterpriseInfo()
  },
  methods: {
    addRow(item, index) {
      let obj = {
        'frozenQty': '',
        'id': '',
        'sn': '',
        'inSn': '',
        'packageNumber': '',
        'qty': '',
        'remark': ''
      };
      this.UnitList.push(obj);
    },
    delRow(item, index) {
      // if (index != 0) {
      // }
      this.UnitList.splice(index, 1)
    },
    async getData() {
      this.$common.showLoad()
      let data = await b2bProductDetail(this.id)
      this.$common.hideLoad()
      console.log(data);
      // this.$log(data)
      if (data !== undefined) {
        data.manufacturingDate = data.manufacturingDate ? formatDate(data.manufacturingDate, 'yyyy-MM-dd') : ''
        data.expiryDate = data.expiryDate ? formatDate(data.expiryDate, 'yyyy-MM-dd') : ''
        this.data = data;
        this.UnitList = data.goodsSkuList
        if (data.goodsSkuList && data.goodsSkuList.length === 0) {
          this.addRow()
        }
        // 1.上架 2.下架 编辑保存需勾选上下架状态
        this.isPutOn = (data.goodsStatus === 1 || data.goodsStatus === 2) ? data.goodsStatus : ''
      }
    },
    // 保存
    async saveChange() {
      for (let i = 0; i < this.UnitList.length; i++) {
        if (!(/(^[1-9]\d*$)/.test(this.UnitList[i].packageNumber))) {
          return this.$common.error('数量只能是大于0的正整数');
        }
        if (!(/(^[0-9]\d*$)/.test(this.UnitList[i].qty))) {
          return this.$common.error('库存只能是正整数');
        }
        if (Number(this.UnitList[i].packageNumber) > Number(this.UnitList[i].qty)) {
          return this.$common.error('商品库存必须大于数量');
        }
      }

      this.$refs.data.validate(async valid => {
        if (valid) {
          this.$common.showLoad()
          let data = await b2bProductEdit(
            this.id,
            this.isPutOn,
            this.data.price,
            this.UnitList,
            this.data.manufacturingDate,
            this.data.expiryDate
          )
          this.$common.hideLoad()
          if (data !== undefined) {
            this.$common.n_success('保存成功')
            this.$router.go(-1)
            // this.$router.push('/products/products_index')
          }
        } else {
          return false
        }
      })
    },
    // 获取当前登录人企业信息(是否已对接ERP)
    async getCurrentEnterpriseInfo() {
      this.$common.showLoad()
      let data = await queryCurrentEnterpriseInfo()
      this.$common.hideLoad()
      if (data && data.enterpriseInfo) {
        this.erpSyncLevel = data.enterpriseInfo.erpSyncLevel
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
