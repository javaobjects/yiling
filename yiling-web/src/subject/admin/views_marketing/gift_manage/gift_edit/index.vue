<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :model="form" :rules="rules" ref="dataForm" label-position="left" label-width="110px" class="demo-ruleForm">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>商品基本信息
          </div>
          <div class="content-box my-form-box">
            <el-form-item label="商品名称" prop="name">
              <el-input v-model="form.name" maxlength="20" show-word-limit></el-input>
            </el-form-item>
            <el-form-item label="所属业务" prop="businessType">
              <el-radio-group :disabled="isEdit" v-model="form.businessType">
                <el-radio v-for="item in businessTypeArray" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="商品类别" prop="goodsType">
              <el-radio-group :disabled="isEdit" v-model="form.goodsType" @change="goodsTypeChange">
                <el-radio :label="1">真实物品</el-radio>
                <el-radio :label="2">虚拟物品</el-radio>
                <!-- <el-radio :label="3">优惠券</el-radio>
                <el-radio :label="4">会员</el-radio> -->
              </el-radio-group>
            </el-form-item>
            <div class="flex-row-left">
              <el-form-item label="商品数量" prop="quantity" class="my-form-left">
                <el-input v-model="form.quantity" @keyup.native="form.quantity = onInput(form.quantity, 0)"></el-input>
              </el-form-item>
              <el-form-item label="商品规格" class="my-form-left">
                <el-input v-model="form.specifications"></el-input>
              </el-form-item>
            </div>
            <div class="flex-row-left">
              <el-form-item label="安全库存数量" prop="safeQuantity" class="my-form-left">
                <el-input v-model="form.safeQuantity" @keyup.native="form.safeQuantity = onInput(form.safeQuantity, 0)"></el-input>
              </el-form-item>
              <el-form-item label="单位" class="my-form-left">
                <el-input v-model="form.unit"></el-input>
              </el-form-item>
            </div>
            <div class="flex-row-left">
              <el-form-item label="商品品牌" class="my-form-left">
                <el-input v-model="form.brand"></el-input>
              </el-form-item>
              <el-form-item label="商品价值" prop="price" class="my-form-left">
                <el-input v-model="form.price" @keyup.native="form.price = onInput(form.price, 2)"></el-input> 元
              </el-form-item>
            </div>
            <el-form-item label="商品图片" prop="pictureUrl">
              <yl-upload
                :default-url="form.pictureUrl"
                :limit-width="750"
                :limit-height="750"
                :max-size="2048"
                :extral-data="{type: 'marketingGoodsGiftPicture'}"
                @onSuccess="onSuccess"
              />
              <div class="explain">请上传正方形图片，尺寸750*750，最大2MB</div>
            </el-form-item>
            <!-- 真实物品、虚拟物品 -->
            <div v-if="form.goodsType == 2">
              <div class="flex-row-left" v-for="(item,index) in form.cardList" :key="index">
                <el-form-item label="卡号" class="my-form-left">
                  <el-input v-model="item.cardNo"></el-input>
                </el-form-item>
                <el-form-item label="密码" class="my-form-left">
                  <el-input v-model="item.password"></el-input>
                </el-form-item>
                <el-form-item label="">
                  <yl-button v-if="index == 0" type="text" @click="addGradient">继续添加</yl-button>
                  <yl-button v-else type="text" @click="removeGradient(index)">删除</yl-button>
                </el-form-item>
              </div>
            </div>
            <!-- 优惠券 -->
            <div class="mar-t-8 member-table-view" v-if="form.goodsType == 3 && couponDataList.length > 0">
              <div class="font-title-color mar-b-8">优惠券设置</div>
              <yl-table
                border
                :show-header="true"
                :list="couponDataList"
                :total="couponQuery.total"
                :page.sync="couponQuery.page"
                :limit.sync="couponQuery.limit"
                :loading="loading1"
                @getList="queryListPageForGoodsGiftList">
                <el-table-column label="" align="center" width="65">
                  <template slot-scope="{ row }">
                    <el-radio :label="row.id" v-model="form.couponActivityId">{{ }}</el-radio>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="120" label="券批次" prop="id">
                </el-table-column>
                <el-table-column align="center" min-width="120" label="券名称" prop="name">
                </el-table-column>
                <el-table-column align="center" min-width="80" label="发放数量" prop="totalCount">
                </el-table-column>
                <el-table-column align="center" width="120" label="满">
                  <template slot-scope="{ row }">
                    <span>{{ row.thresholdValue }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="120" label="减／折">
                  <template slot-scope="{ row }">
                    <span>{{ row.couponRules }}</span>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
            <!-- 会员 -->
            <div class="mar-t-8 member-table-view" v-if="form.goodsType == 4 && memberDataList.length > 0">
              <div class="font-title-color mar-b-8">设置会员</div>
              <yl-table
                border
                :show-header="true"
                :list="memberDataList"
                :total="memberQuery.total"
                :page.sync="memberQuery.page"
                :limit.sync="memberQuery.limit"
                :loading="loading"
                @getList="getMemberQueryList">
                <el-table-column label="" align="center" width="65">
                  <template slot-scope="{ row }">
                    <el-radio :label="row.id" v-model="form.memberId">{{ }}</el-radio>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="120" label="会员名称" prop="name">
                </el-table-column>
                <el-table-column align="center" min-width="120" label="会员描述" prop="description">
                </el-table-column>
                <el-table-column align="center" min-width="80" label="开通终端数" prop="openNum">
                </el-table-column>
                <el-table-column align="center" width="120" label="权获得条件">
                  <template slot-scope="{ row }">
                    <div>
                      {{ row.getType | dictLabel(getTypeDict) }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" min-width="120" label="创建时间">
                  <template slot-scope="{ row }">
                    <span>{{ row.createTime | formatDate }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="最后维护信息">
                  <template slot-scope="{ row }">
                    <span>{{ row.updateTime | formatDate }}</span>
                  </template>
                </el-table-column>
              </yl-table>
            </div>
            <!-- 商品介绍 -->
            <el-form-item label="商品介绍">
              <wang-editor
                :height="height"
                :content="introduction"
                :extral-data="{type: 'richTextEditorFile'}"
                :handle-content="handleContent"/>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" @click="saveClick">保存</yl-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getGiftDetail, giftSave, giftUpdate, queryListPageForGoodsGift } from '@/subject/admin/api/view_marketing/gift_manage';
import { getMemberListPage } from '@/subject/admin/api/b2b_api/membership'
import { ylUpload } from '@/subject/admin/components'
import { onInputLimit } from '@/common/utils'
import { wangEditor } from '@/subject/admin/components'

export default {
  components: {
    ylUpload,
    wangEditor
  },
  computed: {
  },
  data() {
    var checkLimitNum = (rule, value, callback) => {
      this.$common.log('rule:', rule)
      if (value == 0) {
        callback(new Error('商品价值不能为0'));
      } else {
        callback();
      }
    };
    return {
      isEdit: false,
      // 活动状态
      businessTypeArray: [
        {
          label: 'B2B',
          value: 1
        },
        {
          label: '2C',
          value: 2
        }
      ],
      getTypeDict: [
        {
          value: 1,
          label: '付费'
        },
        {
          value: 2,
          label: '活动赠送'
        }
      ],
      loading: false,
      memberQuery: {
        page: 1,
        limit: 10,
        total: 0
      },
      memberDataList: [],
      loading1: false,
      couponQuery: {
        page: 1,
        limit: 10,
        total: 0
      },
      couponDataList: [],
      form: {
        name: '',
        businessType: '',
        goodsType: '',
        quantity: '',
        specifications: '',
        safeQuantity: '',
        unit: '',
        brand: '',
        price: '',
        pictureUrl: '',
        pictureKey: '',
        introduction: '',
        cardList: [
          {
            cardNo: '',
            password: ''
          }
        ],
        couponActivityId: '',
        memberId: ''
      },
      // 商品介绍
      introduction: '',
      height: 500,
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        businessType: [{ required: true, message: '请选择所属业务', trigger: 'change' }],
        goodsType: [{ required: true, message: '请选择商品类别', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入商品数量', trigger: 'blur' }],
        safeQuantity: [{ required: true, message: '请输入商品安全库存数量', trigger: 'blur' }],
        price: [
          { required: true, message: '请输入商品价值', trigger: 'blur' }, 
          { validator: checkLimitNum, trigger: 'blur' }
        ],
        pictureUrl: [{ required: true, message: '请上传商品图片', trigger: 'blur' }]
      },
      // 使用平台
      payTypeValues: []
      // 奖品列表  
    };
  },
  mounted() {
    this.query = this.$route.params
    this.id = this.query.id
    if (this.query.id) {
      this.isEdit = true
      this.getData()
    }
  },
  methods: {
    async getData() {
      this.$common.showLoad()
      let data = await getGiftDetail(this.query.id)
      this.$common.hideLoad()
      if (data) {
        this.form = data
        this.form.pictureUrl = data.fileInfo && data.fileInfo.fileUrl
        this.form.pictureKey = data.fileInfo && data.fileInfo.fileKey
        this.introduction = data.introduction
        if (data.goodsType == 3) {
          this.queryListPageForGoodsGiftList()
        }
        if (data.goodsType == 4) {
          this.getMemberQueryList()
        }
      }
    },
    async onSuccess(data) {
      if (data.key) {
        this.form.pictureKey = data.key
        this.form.pictureUrl = data.url
      }
    },
    // 商品类别切换
    goodsTypeChange(goodsType) {
      this.form.memberId = ''
      this.form.couponActivityId = ''
      if (goodsType == 3) {
        if (this.couponDataList.length > 0){
          return false
        }
        this.queryListPageForGoodsGiftList()
      }
      if (goodsType == 4) {
        if (this.memberDataList.length > 0){
          return false
        }
        this.getMemberQueryList()
      }
    },
    // 获取优惠券列表
    async queryListPageForGoodsGiftList() {
      this.loading1 = true
      let couponQuery = this.couponQuery
      let data = await queryListPageForGoodsGift(
        couponQuery.page,
        couponQuery.limit
      )
      this.loading1 = false
      if (data) {
        this.couponDataList = data.records
        this.couponQuery.total = data.total
        if (data.records.length == 0) {
          this.$common.warn('暂无优惠券数据')
        }
      }
    },
    // 获取会员列表
    async getMemberQueryList() {
      this.loading = true
      let memberQuery = this.memberQuery
      let data = await getMemberListPage(
        memberQuery.page,
        memberQuery.limit
      )
      this.loading = false
      if (data) {
        this.memberDataList = data.records
        this.memberQuery.total = data.total
        if (data.records.length == 0) {
          this.$common.warn('暂无会员数据')
        }
      }
    },
    // 添加梯度
    addGradient() {
      let cardList = this.form.cardList
      // eslint-disable-next-line no-new-object
      let newObj = new Object()
      newObj.code = ''
      newObj.password = ''
      this.form.cardList = [...cardList, newObj]
    },
    // 删除梯度
    removeGradient(index) {
      let cardList = this.form.cardList
      cardList.splice(index, 1)
      this.form.cardList = cardList
    },
    // 富文本编辑器回调
    handleContent(content, editor) {
      this.form.introduction = content
    },
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let form = this.form
          if (Number(form.quantity) < Number(form.safeQuantity)) {
            this.$common.warn('商品数量不能小于安全库存数量')
            return false
          }
          let addParams = {
            name: form.name,
            businessType: form.businessType,
            goodsType: form.goodsType,
            quantity: form.quantity,
            specifications: form.specifications,
            safeQuantity: form.safeQuantity,
            unit: form.unit,
            brand: form.brand,
            price: form.price,
            pictureUrl: form.pictureUrl,
            pictureKey: form.pictureKey,
            introduction: form.introduction
          }
          // 商品类别：1-真实物品；2-虚拟物品；3-优惠券；4-会员
          if (form.goodsType == 2) {
            addParams.cardList = form.cardList
          } else if (form.goodsType == 3) {
            if (!form.couponActivityId) {
              this.$common.warn('请选择优惠券')
              return false
            }
            addParams.couponActivityId = form.couponActivityId
          } else if (form.goodsType == 4) {
            if (!form.memberId) {
              this.$common.warn('请选择会员')
              return false
            }
            addParams.memberId = form.memberId
          }
          
          let data = null
          this.$common.showLoad()
          if (this.query.id) {
            addParams.id = this.query.id
            data = await giftUpdate(addParams)
          } else {
            data = await giftSave(addParams)
          }
          this.$common.hideLoad()
          if (data) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          } 
        } else {
          console.log('error submit!!');
          return false;
        }
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
