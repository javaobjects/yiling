<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :disabled="operationType == 1" :model="form" :rules="rules" ref="dataForm">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>促销规则
          </div>
          <div class="content-box">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销名称</div>
                  <el-form-item prop="name">
                    <el-input v-model="form.name" maxlength="10" show-word-limit></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>活动时间</div>
                  <el-form-item prop="time">
                    <el-date-picker v-model="form.time" type="datetimerange" format="yyyy/MM/dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销预算金额</div>
                  <el-form-item prop="budgetAmount">
                    <el-input v-model="form.budgetAmount" @keyup.native="form.budgetAmount = onInput(form.budgetAmount, 2)"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销费用承担方</div>
                  <el-form-item prop="bear">
                    <el-radio-group v-model="form.bear">
                      <el-radio :label="1">平台</el-radio>
                      <el-radio :label="2">商家</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>活动分类</div>
                  <el-form-item prop="sponsorType">
                    <el-select v-model="form.sponsorType" @change="sponsorTypeChange" placeholder="请选择">
                      <el-option v-for="item in sponsorTypeArray" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title">运营备注</div>
                  <el-form-item>
                    <el-input v-model="form.remark" maxlength="20" show-word-limit class="show-word-limit"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </el-form>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>促销活动企业
        </div>
        <div class="content-box">
          <div>
            <yl-button class="mar-r-8 my-buttom" :disabled="operationType == 1" type="primary" @click="addProviderClick">添加</yl-button>
            <span class="font-size-base font-light-color">只允许添加一个商家</span>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table border show-header :list="enterpriseLimitList" :total="0">
              <el-table-column label="企业ID" align="center" prop="eid">
              </el-table-column>
              <el-table-column label="企业名称" align="center" prop="ename">
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="{ $index }">
                  <div class="operation-view">
                    <yl-button :disabled="operationType == 1" type="text" @click="providerRemoveClick($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>促销商品
        </div>
        <div class="content-box">
          <!-- 导出按钮 -->
          <div class="down-box clearfix">
            <div class="left-btn">
              <yl-button :disabled="operationType == 1" class="mar-r-8 my-buttom" type="primary" @click="addGoodsClick">添加</yl-button>
            </div>
            <div class="btn">
              <el-link :disabled="operationType == 1" class="mar-r-10" type="primary" :underline="false" :href="'NO_7' | template">
                下载模板
              </el-link>
              <ylButton :disabled="operationType == 1" type="primary" plain @click="goImport">导入</ylButton>
              <!-- <ylButton type="danger" plain >批量删除</ylButton> -->
            </div>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table border show-header :list="goodsLimitList" :total="0">
              <el-table-column label="商品编号" align="center" prop="id">
              </el-table-column>
              <el-table-column label="商品名称" align="center" prop="name">
              </el-table-column>
              <el-table-column label="商品类型" align="center">
                <template slot-scope="{ row }">
                  <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
                </template>
              </el-table-column>
              <el-table-column label="包装规格" align="center" prop="sellSpecifications">
              </el-table-column>
              <el-table-column label="企业名称" align="center" prop="ename">
              </el-table-column>
              <el-table-column label="销售价" align="center" prop="price">
              </el-table-column>
              <el-table-column label="库存数量（单位）" align="center" prop="count">
              </el-table-column>
              <el-table-column label="以岭品" min-width="55" align="center">
                <template slot-scope="{ row }">
                  <div class="font-size-base">{{ row.yilingGoodsFlag == 1 ? '以岭品' : '非以岭品' }}</div>
                </template>
              </el-table-column>
              <el-table-column label="状态" min-width="55" align="center">
                <template slot-scope="{ row }">
                  <div v-if="row.goodsStatus == 1" class="font-size-base up">上架</div>
                  <div v-else-if="row.goodsStatus == 2" class="font-size-base">下架</div>
                  <div v-else class="font-size-base">待设置</div>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="{ $index }">
                  <div class="operation-view">
                    <yl-button :disabled="operationType == 1" type="text" @click="goodsRemoveClick($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>促销范围
        </div>
        <div class="content-box">
          <div class="font-size-base mar-b-8 font-title-color"><span class="red-text">*</span>渠道分类</div>
          <div>
            <el-checkbox-group v-model="platformSelected" :disabled="operationType == 1">
              <el-checkbox :label="1">B2B</el-checkbox>
              <el-checkbox :label="2">销售助手</el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
      </div>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>赠品设置
        </div>
        <div class="content-box">
          <div>
            <yl-button class="mar-r-8 my-buttom" :disabled="operationType == 1" type="primary" @click="addGiftClick">{{ goodsGiftLimit.length == 0 ? '添加赠品设置' : '继续添加赠品' }}</yl-button>
          </div>
          <!-- 底部列表 -->
          <div class="mar-t-8 pad-b-10 gift-list-view">
            <div v-for="(item,index) in goodsGiftLimit" :key="index" class="check-item">
              <span class="font-light-color sign-text">按总金额:</span>
              <span class="unit">{{ item.promotionAmount }}</span>
              <span class="add-btn font-light-color sign-text">赠</span>
              <span class="unit">{{ item.giftName }}</span>
              <span class="add-btn font-light-color sign-text">参与活动商品数量:</span>
              <span class="unit">{{ item.promotionStock }}</span>
              <yl-button class="add-btn" :disabled="operationType == 1" type="text" @click="removeGift(index)">删除</yl-button>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 添加商家 -->
    <yl-dialog title="添加" :visible.sync="addProviderDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">企业ID</div>
                <el-input v-model="providerQuery.eid" placeholder="请输入企业ID" @keyup.enter.native="providerHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="providerQuery.ename" placeholder="请输入企业名称" @keyup.enter.native="providerHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="providerTotal" @search="providerHandleSearch" @reset="providerHandleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table :stripe="true" :show-header="true" :list="providerList" :total="providerTotal" :page.sync="providerQuery.page" :limit.sync="providerQuery.limit" :loading="loading1" @getList="getProviderList">
            <el-table-column label="企业ID" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.eid }}</div>
              </template>
            </el-table-column>
            <el-table-column label="企业名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" v-if="!row.isSelect" @click="providerItemAddClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" v-if="row.isSelect" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 添加商品 -->
    <yl-dialog title="添加" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品编号</div>
                <el-input type="number" v-model="goodsQuery.id" placeholder="请输入商品编号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.name" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">以岭品</div>
                <el-select v-model="goodsQuery.yilingGoodsFlag" placeholder="请选择">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option label="以岭品" :value="1"></el-option>
                  <el-option label="非以岭品" :value="2"></el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">上下架状态</div>
                <el-select v-model="goodsQuery.goodsStatus" placeholder="请选择">
                  <el-option label="全部" :value="0"></el-option>
                  <el-option label="上架" :value="1"></el-option>
                  <el-option label="下架" :value="2"></el-option>
                  <el-option label="待设置" :value="3"></el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="goodsTotal" @search="goodsHandleSearch" @reset="goodsHandleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table :stripe="true" :show-header="true" :list="goodsList" :total="goodsTotal" :page.sync="goodsQuery.page" :limit.sync="goodsQuery.limit" :loading="loading2" @getList="getGoodsList">
            <el-table-column label="商品编码" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.id }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.name }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="企业名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.ename }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品类型" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsType | dictLabel(standardGoodsType) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="以岭品" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.yilingGoodsFlag == 1 ? '以岭品' : '非以岭品' }}</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div v-if="row.goodsStatus == 1" class="font-size-base up">上架</div>
                <div v-else-if="row.goodsStatus == 2" class="font-size-base">下架</div>
                <div v-else class="font-size-base">待设置</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="65" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="edit-btn" v-if="row.goodsDisableVO.isAllowSelect == 1" disabled type="text">已被其他活动添加</yl-button>
                  <yl-button class="view-btn" type="text" v-if="!row.isSelect && row.goodsDisableVO.isAllowSelect != 1" @click="goodsItemAddClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" v-if="row.isSelect && row.goodsDisableVO.isAllowSelect != 1" disabled type="text">已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 添加商品 -->
    <yl-dialog title="添加赠品设置" :visible.sync="addGiftDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="12">
                <span class="title mar-r-10">按总金额</span>
                <el-input v-model="promotionAmount" @keyup.native="promotionAmount = onInput(promotionAmount, 2)" placeholder="请输入总金额" />
              </el-col>
              <el-col :span="12">
                <span class="title mar-r-10">参与活动商品数量</span>
                <el-input v-model="promotionStock" @keyup.native="promotionStock = onInput(promotionStock, 0)" placeholder="请输入商品数量" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-16" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table border show-header :list="giftList" :total="0" :loading="loading3">
            <el-table-column label="商品编号" min-width="120" align="center" prop="id">
            </el-table-column>
            <el-table-column label="商品名称" min-width="100" align="center" prop="name">
            </el-table-column>
            <el-table-column label="类别" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.goodsType | dictLabel(goodsTypeArray) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="数量单位" min-width="120" align="center" prop="unit">
            </el-table-column>
            <el-table-column label="品牌" min-width="200" align="center" prop="brand">
            </el-table-column>
            <el-table-column label="商品价值" min-width="200" align="center" prop="price">
            </el-table-column>
            <el-table-column label="安全库存" min-width="200" align="center" prop="safeQuantity">
            </el-table-column>
            <el-table-column label="可用库存" min-width="200" align="center" prop="availableQuantity">
            </el-table-column>
            <el-table-column label="总库存" min-width="200" align="center" prop="quantity">
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="center" fixed="right">
              <template slot-scope="{ row }">
                <div class="operation-view">
                  <div class="option">
                    <yl-button class="view-btn" type="text" @click="giftItemAddClick(row)">添加</yl-button>
                  </div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 导入商品 -->
    <yl-dialog title="导入" :visible.sync="importDialog" aria-atomic="" :show-footer="false">
      <div class="import-dialog-content flex-row-center">
        <yl-upload-file :action="info.action" :extral-data="info.extralData" @onSuccess="onSuccess" />
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { promotionActivityQueryById, goodsB2bList, promotionActivitySubmit } from '@/subject/admin/api/b2b_api/marketing_manage'
import { queryEnterpriseListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { getGiftList } from '@/subject/admin/api/view_marketing/gift_manage'
import { standardGoodsType } from '@/subject/admin/utils/busi'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { ylUploadFile } from '@/subject/admin/components'

export default {
  components: {
    ylUploadFile
  },
  computed: {
    standardGoodsType() {
      return standardGoodsType()
    }
  },
  data() {
    return {
      goodsTypeArray: [
        {
          label: '真实物品',
          value: 1
        },
        {
          label: '虚拟物品',
          value: 2
        },
        {
          label: '优惠券',
          value: 3
        },
        {
          label: '会员',
          value: 4
        }
      ],
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
      loading: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      // 基本信息设置
      form: {
        name: '',
        time: [],
        type: 1,
        budgetAmount: '',
        bear: 1,
        sponsorType: '',
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '请输入促销名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }],
        budgetAmount: [{ required: true, message: '请输入促销预算金额', trigger: 'blur' }],
        bear: [{ required: true, message: '请选择承担方', trigger: 'change' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }]
      },
      // 添加商家弹框
      addProviderDialog: false,
      loading1: false,
      providerQuery: {
        page: 1,
        limit: 10
      },
      providerList: [],
      providerTotal: 0,
      // 已选择企业
      enterpriseLimitList: [],
      // 添加商品弹框
      addGoodsDialog: false,
      loading2: false,
      goodsQuery: {
        page: 1,
        limit: 10,
        yilingGoodsFlag: 0,
        goodsStatus: 0,
        // 可用库存是否大于零 0全部库存 1可用库存大于0
        isAvailableQty: 0
      },
      goodsList: [],
      goodsTotal: 0,
      // 已选商品列表
      goodsLimitList: [],
      // 促销范围
      platformSelected: [],
      // 赠品列表
      loading3: false,
      giftList: [],
      // 赠品设置弹框
      addGiftDialog: false,
      // 赠品设置
      goodsGiftLimit: [],
      // 导入商品弹窗
      importDialog: false,
      // 弹框输入 满赠金额
      promotionAmount: '',
      promotionStock: '',
      info: {
        action: '',
        extralData: {}
      }
    };
  },
  mounted() {
    this.$log(this.$route.params)
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType

    if (this.id && this.operationType) {
      this.getDetail()
    }
  },
  methods: {
    init(couponActivityId) {
    },
    async getDetail() {
      let data = await promotionActivityQueryById(this.id)
      if (data) {
        let form = {
          name: data.promotionActivity.name,
          type: data.promotionActivity.type,
          budgetAmount: data.promotionActivity.budgetAmount,
          bear: data.promotionActivity.bear,
          sponsorType: data.promotionActivity.sponsorType,
          remark: data.promotionActivity.remark
        }
        if (data.promotionActivity.beginTime && data.promotionActivity.endTime) {
          form.time = [formatDate(data.promotionActivity.beginTime), formatDate(data.promotionActivity.endTime)]
        }
        this.form = form
        this.enterpriseLimitList = data.promotionEnterpriseLimitList || []
        // 复制进入
        if (this.operationType == 4) {
          this.id = ''
          this.goodsLimitList = []
        } else {
          this.goodsLimitList = data.promotionGoodsLimitList || []
        }

        this.platformSelected = data.promotionActivity.platformSelected || []
        this.goodsGiftLimit = data.promotionGoodsGiftLimitList || []

      }
    },
    // 活动分类
    sponsorTypeChange() {
      this.goodsLimitList = []
    },
    // 设置商品点击
    addProviderClick() {
      this.addProviderDialog = true
      this.getProviderList()
    },
    // 商品搜索
    providerHandleSearch() {
      this.providerQuery.page = 1
      this.getProviderList()
    },
    providerHandleReset() {
      this.providerQuery = {
        page: 1,
        limit: 10
      }
    },
    // 获取商机列表
    async getProviderList() {
      this.loading1 = true
      let providerQuery = this.providerQuery
      let data = await queryEnterpriseListPage(
        providerQuery.page,
        providerQuery.limit,
        providerQuery.eid,
        providerQuery.ename
      );
      this.loading1 = false
      if (data) {

        data.records.forEach(item => {
          let hasIndex = this.enterpriseLimitList.findIndex(obj => {
            return obj.eid == item.eid;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.providerList = data.records
        this.providerTotal = data.total
      }
    },
    // 弹框每一项企业点击
    providerItemAddClick(row) {
      let currentArr = this.enterpriseLimitList
      currentArr = [row]

      this.enterpriseLimitList = currentArr

      let arr = this.providerList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.eid == item.eid;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.providerList = arr;
      this.goodsLimitList = []
    },
    providerRemoveClick(index) {
      let currentArr = this.enterpriseLimitList
      let deleteItem = currentArr[index]
      currentArr.splice(index, 1)
      this.enterpriseLimitList = currentArr
      if (this.goodsLimitList.length == 0) {
        return
      }
      // 删除改企业下商品
      let goodsLimitList = this.goodsLimitList
      for (let i = 0; i < this.goodsLimitList.length; i++) {
        let item = goodsLimitList[i]
        if (item.eid == deleteItem.eid) {
          goodsLimitList.splice(i, 1)
          i--
        }
      }
      this.goodsLimitList = goodsLimitList
    },
    // 添加商品点击
    addGoodsClick() {
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('请先添加促销企业')
        return false
      }
      if (!this.form.sponsorType) {
        this.$common.warn('请先选择活动分类')
        return false
      }
      this.addGoodsDialog = true
      this.getGoodsList()
    },
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getGoodsList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10,
        yilingGoodsFlag: 0,
        goodsStatus: 0,
        // 可用库存是否大于零 0全部库存 1可用库存大于0
        isAvailableQty: 0
      }
    },
    async getGoodsList() {
      let eidList = []
      this.enterpriseLimitList.forEach(item => {
        eidList.push(item.eid)
      })

      this.loading2 = true
      let goodsQuery = this.goodsQuery
      let data = await goodsB2bList(
        goodsQuery.page,
        goodsQuery.limit,
        1,
        goodsQuery.yilingGoodsFlag,
        this.form.sponsorType,
        1,
        goodsQuery.goodsStatus,
        eidList,
        this.id,
        goodsQuery.id,
        goodsQuery.name,
        goodsQuery.isAvailableQty
      );
      this.loading2 = false
      if (data) {

        data.records.forEach(item => {
          let hasIndex = this.goodsLimitList.findIndex(obj => {
            return obj.goodsId == item.goodsId;
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 弹框每一项企业点击
    goodsItemAddClick(row) {
      let currentArr = this.goodsLimitList
      currentArr.push(row)
      this.goodsLimitList = currentArr

      let arr = this.goodsList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return obj.goodsId == item.goodsId;
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.goodsList = arr;
    },
    goodsRemoveClick(index) {
      let currentArr = this.goodsLimitList
      currentArr.splice(index, 1)
      this.goodsLimitList = currentArr
    },
    // 添加赠品设置点击
    addGiftClick() {
      this.promotionAmount = ''
      this.promotionStock = ''
      this.addGiftDialog = true
      if (this.giftList.length == 0) {
        this.getGiftListMotheds()
      }
    },
    // 获取赠品列表
    async getGiftListMotheds() {
      this.loading3 = true
      let data = await getGiftList(
      );
      this.loading3 = false
      if (data) {
        this.giftList = data.list
      }
    },
    // 点击赠品item点击
    giftItemAddClick(row) {
      if (!this.promotionAmount) {
        this.$common.warn('请设置赠品总金额');
        return false
      }
      if (!this.promotionStock) {
        this.$common.warn('请设置赠品数量');
        return false
      }
      // 判断金额 是否已经设置
      let hasIndex = this.goodsGiftLimit.findIndex((item) => {
        return item.promotionAmount == this.promotionAmount
      })
      if (hasIndex != -1) {
        this.$common.warn('该赠品总金额已经设置过');
        return false
      }
      let GiftItem = {
        goodsGiftId: row.id,
        promotionAmount: this.promotionAmount,
        promotionStock: this.promotionStock,
        giftName: row.name
      }
      let goodsGiftLimit = this.goodsGiftLimit
      this.goodsGiftLimit = [...goodsGiftLimit, GiftItem]
      this.addGiftDialog = false
    },
    removeGift(index) {
      let goodsGiftLimit = this.goodsGiftLimit
      goodsGiftLimit.splice(index, 1)
      this.goodsGiftLimit = goodsGiftLimit
    },
    // 底部提交
    async saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {

          let activity = this.form
          activity.platformSelected = this.platformSelected
          activity.beginTime = activity.time && activity.time.length ? activity.time[0] : undefined
          activity.endTime = activity.time && activity.time.length > 1 ? activity.time[1] : undefined

          if (this.checkInputData()) {
            let params = {}
            if (this.id) {
              params.id = this.id
            }
            params.activity = activity
            params.enterpriseLimitList = this.enterpriseLimitList
            params.goodsLimitList = this.goodsLimitList
            params.goodsGiftLimit = this.goodsGiftLimit

            this.$common.showLoad()
            let data = await promotionActivitySubmit(params)
            this.$common.hideLoad()
            if (data) {
              this.$common.n_success('保存成功')
              this.$router.go(-1)
            }

          }

        } else {
          console.log('error submit!!');
          return false;
        }
      })

    },
    //  校验数据
    checkInputData() {
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('请添加促销企业');
        return false
      }
      if (this.goodsLimitList.length == 0) {
        this.$common.warn('请添加促销商品');
        return false
      }
      if (this.platformSelected.length == 0) {
        this.$common.warn('请选择促销范围');
        return false
      }
      if (this.goodsGiftLimit.length == 0) {
        this.$common.warn('请添加赠品');
        return false
      }
      return true
    },
    // 去导入页面
    goImport() {
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('请先添加促销企业')
        return false
      }
      if (!this.form.sponsorType) {
        this.$common.warn('请先选择活动分类')
        return false
      }

      this.importDialog = true

      let eidList = []
      this.enterpriseLimitList.forEach(item => {
        eidList.push(item.eid)
      })
      let eidListStr = eidList.join(',')

      let extralData = {
        eidList: eidListStr,
        sponsorType: this.form.sponsorType,
        type: this.form.type
      }
      if (this.id) {
        extralData.promotionActivityId = this.id
      }
      let info = {
        action: '/b2b/api/v1/promotion/activity/importPromotionGoods',
        extralData: extralData
      }
      this.info = info
    },
    onSuccess(data) {
      this.$log(data)
      if (data && data.length > 0) {
        this.$common.n_success('成功导入：' + data.length + '条')
        this.importDialog = false
        let currentArr = this.goodsLimitList
        if (currentArr && currentArr.length > 0) {
          data.forEach(item => {
            let hasIndex = currentArr.findIndex(obj => {
              return obj.goodsId == item.goodsId;
            });
            if (hasIndex != -1) { // 已经存在
              item.isSelect = true;
            } else {
              item.isSelect = false;
              currentArr.push(this.$common.clone(item))
            }
          });
          this.goodsLimitList = currentArr
        } else {
          this.goodsLimitList = data
        }

      } else {
        this.$common.warn('暂无匹配该企业商品')
      }
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
};
</script>

<style lang="scss" >
.my-form-box {
  .el-form-item {
    .el-form-item__label {
      color: $font-title-color;
    }
    label {
      font-weight: 400 !important;
    }
  }
  .my-form-item-right {
    label {
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
