<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :disabled="operationType == 1" :model="form" :rules="rules" ref="dataForm">
        <div class="top-bar">
          <div>
          </div>
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
                <el-col :span="17">
                  <div class="title"><span class="red-text">*</span>促销费用承担方</div>
                  <el-form-item prop="bear">
                    <el-radio-group v-model="form.bear" class="radio-group-view">
                      <div class="radio-item">
                        <el-radio :label="1">平台</el-radio>
                      </div>
                      <div class="radio-item">
                        <el-radio :label="2">商家</el-radio>
                      </div>
                      <div class="radio-item">
                        <el-radio :label="3">分摊</el-radio>
                        <el-form-item label="" required v-if="form.bear == 3">
                          <div class="assume-form-item flex-row-left">平台
                            <el-form-item label="" prop="platformPercent">
                              <el-input v-model="form.platformPercent" @keyup.native="form.platformPercent = onInput(form.platformPercent, 2)" class="input-box"></el-input>
                            </el-form-item>
                            % 商家
                            <el-form-item label="" prop="merchantPercent">
                              <el-input v-model="form.merchantPercent" @keyup.native="form.merchantPercent = onInput(form.merchantPercent, 2)" class="input-box"></el-input>
                            </el-form-item>
                            %
                          </div>
                        </el-form-item>
                      </div>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>{{ type == 2 ? '特价商品' : '秒杀商品' }}</div>
                  <el-form-item prop="merchantType">
                    <el-radio-group v-model="form.merchantType" @change="merchantTypeChange" class="radio-group-view">
                      <el-radio :label="1">以岭商品</el-radio>
                      <el-radio :label="2">非以岭商品</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销预算金额</div>
                  <el-form-item prop="budgetAmount">
                    <el-input v-model="form.budgetAmount" @keyup.native="form.budgetAmount = onInput(form.budgetAmount, 2)"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销编码</div>
                  <el-form-item prop="promotionCode">
                    <el-input v-model="form.promotionCode"></el-input>
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
                      <el-option v-for="item in sponsorTypeArray" :key="item.value" :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title">运营备注</div>
                  <el-form-item>
                    <el-input v-model="form.remark" maxlength="20" show-word-limit class="show-word-limit"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <div class="title"><span class="red-text">*</span>{{ type == 2 ? '设置特价时间' : '设置秒杀时间' }}</div>
                  <el-form-item label="" prop="effectType" required>
                    <el-radio-group v-model="form.effectType" class="time-form-view">
                      <div class="radio-item">
                        <el-radio :label="1">立即生效</el-radio>
                        <div class="assume-form-item flex-row-left" v-if="form.effectType == 1">
                          持续时间
                          <el-form-item label="" prop="lastTime">
                            <el-input class="input-box" v-model="form.lastTime" @keyup.native="form.lastTime = onInput(form.lastTime, 0)"></el-input>
                          </el-form-item>
                          小时
                        </div>
                      </div>
                      <div class="radio-item">
                        <el-radio :label="2">固定时间生效</el-radio>
                        <div v-if="form.effectType == 2" class="assume-form-item">
                          <el-form-item label="" prop="time">
                            <el-date-picker
                              v-model="form.time"
                              type="datetimerange"
                              format="yyyy/MM/dd HH:mm:ss"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              range-separator="至"
                              start-placeholder="开始日期"
                              end-placeholder="结束日期"
                              :default-time="['00:00:00', '23:59:59']">
                            </el-date-picker>
                          </el-form-item>
                        </div>
                      </div>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>{{ type == 2 ? '特价渠道范围' : '秒杀渠道范围' }}
          </div>
          <!-- 使用平台 -->
          <div class="content-box my-form-box">
            <el-form-item label="使用平台：" label-width="100px" prop="platformSelected">
              <el-checkbox-group v-model="form.platformSelected">
                  <el-checkbox :label="1">B2B</el-checkbox>
                  <el-checkbox :label="2">销售助手</el-checkbox>
                </el-checkbox-group>
            </el-form-item>
            <el-form-item label="终端身份：" label-width="100px" prop="terminalType">
              <el-radio-group v-model="form.terminalType">
                <el-radio :label="0">全部</el-radio>
                <el-radio :label="1">会员</el-radio>
                <el-radio :label="2">非会员</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 允许购买区域 -->
            <el-form-item label="允许购买区域：" label-width="120px" prop="permittedAreaType">
              <el-radio-group v-model="form.permittedAreaType">
                <el-radio :label="1">全部区域</el-radio>
                <el-radio :label="2">部分区域</el-radio>
                <yl-button v-if="form.permittedAreaType == 2" type="text" @click="selectClick">{{ selectedData.length > 0 ? selectedDesc : '选择区域' }}</yl-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="企业类型：" label-width="100px" prop="permittedEnterpriseType">
              <el-radio-group v-model="form.permittedEnterpriseType">
                <el-radio :label="1">全部企业</el-radio>
                <el-radio :label="2">部分企业</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="" label-width="100px" v-if="form.permittedEnterpriseType == 2" prop="permittedEnterpriseDetail">
              <el-checkbox-group v-model="form.permittedEnterpriseDetail">
                  <el-checkbox
                    v-for="item in enterpriseType"
                    v-show="item.value != 1 && item.value != 2"
                    :key="item.value"
                    :label="item.value"
                  >{{ item.label }}</el-checkbox>
                </el-checkbox-group>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>{{ type == 2 ? '特价活动企业' : '秒杀活动企业' }}
        </div>
        <div class="content-box">
          <div class="btn-box">
            <yl-button class="mar-r-8 my-buttom" :disabled="operationType == 1" type="primary" @click="addProviderClick">添加</yl-button>
            <yl-button type="danger" v-if="enterpriseLimitList.length" @click="multiDelProviderItem">批量删除</yl-button>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table
              border
              show-header
              :list="enterpriseLimitList"
              :total="0"
            >
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
          <div class="sign"></div>{{ type == 2 ? '特价商品' : '秒杀商品' }}
        </div>
        <div class="content-box">
          <!-- 导出按钮 -->
          <div class="down-box clearfix">
            <div class="left-btn btn-box">
              <yl-button :disabled="operationType == 1" class="mar-r-8 my-buttom" type="primary" @click="addGoodsClick">添加</yl-button>
            </div>
            <div class="btn">
              <el-link :disabled="operationType == 1" class="mar-r-10" type="primary" :underline="false" :href="'NO_7' | template">
                下载模板
              </el-link>
              <ylButton :disabled="operationType == 1" type="primary" plain @click="goImport">导入</ylButton>
              <yl-button type="danger" v-if="goodsLimitList.length" @click="multiDelGoodsItem">批量删除</yl-button>
            </div>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table
              border
              show-header
              :list="goodsLimitList"
              :total="0"
            >
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
              <el-table-column :label="type == 2 ? '特价' : '秒杀价'" min-width="80" align="center">
                <template slot-scope="{ row, $index }">
                  <div class="operation-view">
                    <div class="option">
                      <el-input v-model="row.promotionPrice" @input="change($index, 1)"></el-input>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="type == 2 ? '允许特价商品数量' : '允许秒杀商品数量'" min-width="80" align="center">
                <template slot-scope="{ row, $index }">
                  <div class="operation-view">
                    <div class="option">
                      <el-input v-model="row.allowBuyCount" @input="change($index, 2)"></el-input>
                    </div>
                  </div>
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
                <yl-search-btn
                  :total="providerTotal"
                  @search="providerHandleSearch"
                  @reset="providerHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="multi-action">
          <yl-button plain type="primary" v-if="providerList.length" @click="multiAddProviderItem" :disabled="disableMultiAddProvider">批量添加</yl-button>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="providerList"
            :total="providerTotal"
            :page.sync="providerQuery.page"
            :limit.sync="providerQuery.limit"
            :loading="loading1"
            @getList="getProviderList"
          >
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
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="goodsTotal"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="multi-action">
          <yl-button plain type="primary" v-if="goodsList.length" @click="multiAddGoodsItem" :disabled="disableMultiAddGoods">批量添加</yl-button>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsTotal"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="loading2"
            @getList="getGoodsList"
          >
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
    <!-- 导入商品 -->
    <yl-dialog title="导入" :visible.sync="importDialog" aria-atomic="" :show-footer="false">
      <div class="import-dialog-content flex-row-center">
        <yl-upload-file
          :action="info.action"
          :extral-data="info.extralData"
          @onSuccess="onSuccess"
        />
      </div>
    </yl-dialog>
    <city-select :show.sync="showCityDialog" :init-ids="selectedData" platform="admin" @choose="citySelectConfirm"></city-select>
  </div>
</template>

<script>
import { promotionActivityQueryById, goodsB2bList, promotionActivitySubmit } from '@/subject/admin/api/b2b_api/marketing_manage'
import { queryEnterpriseListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { standardGoodsType } from '@/subject/admin/utils/busi'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'
import { ylUploadFile } from '@/subject/admin/components'
import { enterpriseType } from '@/subject/admin/utils/busi'
import citySelect from '@/subject/admin/components/CitySelect'

export default {
  components: {
    ylUploadFile,
    citySelect
  },
  computed: {
    standardGoodsType() {
      return standardGoodsType()
    },
    enterpriseType() {
      return enterpriseType()
    }

  },
  props: {
    activityType: {
      type: Number
    }
  },
  watch: {
    activityType: {
      handler(newVal,oldVal) {
         this.type = newVal
      },
      immediate: true
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
      // 活动类型（1-满赠,2-特价,3-秒杀）
      type: this.activityType,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      // 区域选择
      showCityDialog: false,
      selectedDesc: '',
      selectedData: [],
      nodes: [],
      // 基本信息设置
      form: {
        name: '',
        // 促销预算金额
        budgetAmount: '',
        // 促销编码
        promotionCode: '',
        // 促销费用承担方
        bear: 1,
        merchantType: 1,
        platformPercent: '',
        merchantPercent: '',
        // 置秒杀时间
        effectType: 1,
        lastTime: '',
        time: [],
        sponsorType: '',
        remark: '',
        // 使用平台
        platformSelected: [],
        terminalType: 0,
        // 允许购买区域
        permittedAreaType: 1,
        // 企业类型
        permittedEnterpriseType: 1,
        // 企业类型数组
        permittedEnterpriseDetail: []
      },
      rules: {
        name: [{ required: true, message: '请输入促销名称', trigger: 'blur' }],
        budgetAmount: [{ required: true, message: '请输入促销预算金额', trigger: 'blur' }],
        promotionCode: [{ required: true, message: '请输入促销编码', trigger: 'blur' }],
        bear: [{ required: true, message: '请选择承担方', trigger: 'change' }],
        merchantType: [{ required: true, message: '请选择', trigger: 'change' }],
        platformPercent: [{ required: true, message: '请输入', trigger: 'blur' }],
        merchantPercent: [{ required: true, message: '请输入', trigger: 'blur' }],
        effectType: [{ required: true, message: '请选择', trigger: 'change' }],
        lastTime: [{ required: true, message: '请输入', trigger: 'blur' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }],
        sponsorType: [{ required: true, message: '请选择活动分类', trigger: 'change' }],
        // 使用平台
        platformSelected: [{ type: 'array', required: true, message: '请选择使用平台', trigger: 'change' }],
        terminalType: [{ required: true, message: '请选择终端身份', trigger: 'change' }],
        permittedAreaType: [{ required: true, message: '请选择购买区域', trigger: 'change' }],
        permittedEnterpriseType: [{ required: true, message: '请选择企业类型', trigger: 'change' }],
        permittedEnterpriseDetail: [{ type: 'array', required: true, message: '请选择', trigger: 'change' }]
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
        // 可用库存是否大于零 0全部库存 1可用库存大于0
        isAvailableQty: 1
      },
      goodsList: [],
      goodsTotal: 0,
      // 已选商品列表
      goodsLimitList: [],
      // 导入商品弹窗
      importDialog: false,
      info: {
        action: '',
        extralData: {}
      },
      // 添加活动企业弹窗批量添加按钮禁用状态
      disableMultiAddProvider: false,
      // 添加商品弹窗批量添加按钮禁用状态
      disableMultiAddGoods: false
    };
  },
  mounted() {
    this.$log('seckill-mounted:',this.$route.params)
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    this.form.type = this.$route.params.type
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
        let form = data.promotionActivity
        if (data.promotionActivity.beginTime && data.promotionActivity.endTime) {
          form.time = [formatDate(data.promotionActivity.beginTime), formatDate(data.promotionActivity.endTime)]
        }

        let promotionSecKillSpecial = data.promotionSecKillSpecial
        form.terminalType = promotionSecKillSpecial.terminalType
        form.permittedAreaType = promotionSecKillSpecial.permittedAreaType
        form.permittedEnterpriseType = promotionSecKillSpecial.permittedEnterpriseType
        form.permittedEnterpriseDetail = promotionSecKillSpecial.permittedEnterpriseDetail

        if (promotionSecKillSpecial.permittedAreaType == 2 && promotionSecKillSpecial.permittedAreaDetail) {
          let areaJsonString = promotionSecKillSpecial.permittedAreaDetail
          let nodes = JSON.parse(areaJsonString)
          this.nodes = nodes

          let check = []
          this.getSelectId(nodes, check)
          this.selectedData = check
          this.selectedDesc = '已选' + promotionSecKillSpecial.permittedAreaDetailDescription
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

      }
    },
    // 活动分类
    sponsorTypeChange() {
      this.goodsLimitList = []
    },
    merchantTypeChange() {
      this.goodsLimitList = []
    },
    change(index, type) {
      console.log('type:', type)
      // 重新赋值
      let tmpObj = this.goodsLimitList[index];
      if (type == 1) {
        tmpObj.promotionPrice = this.onInput(tmpObj.promotionPrice, 2)
      }
      if (type == 2) {
        tmpObj.allowBuyCount = this.onInput(tmpObj.allowBuyCount, 0)
      }
      this.$set(this.goodsLimitList, index, tmpObj);
    },
    selectClick() {
      this.showCityDialog = true
    },
    citySelectConfirm(data) {
      this.showCityDialog = false
      this.$log('data:', data)
      this.selectedDesc = data.desc
      this.nodes = data.nodes
      let check = []
      this.getSelectId(data.nodes, check)
      this.selectedData = check
    },
    // 递归获取选中的
    getSelectId(arr, selectArr) {
      arr.forEach(item => {
        if (item.children && item.children.length){
          this.getSelectId(item.children, selectArr)
        } else {
          selectArr.push(item.code)
        }
      });
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
    // 获取商家列表
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
        // 禁用批量添加按钮
        this.disableMultiAddProvider = this.disableMultiAddBtnStatus(this.providerList, this.enterpriseLimitList, 'eid')
      }
    },
    // 弹框每一项企业点击
    providerItemAddClick(row) {
      let currentArr = this.enterpriseLimitList
      currentArr.push(row)

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
      // 禁用批量添加按钮
      this.disableMultiAddProvider = this.disableMultiAddBtnStatus(this.providerList, this.enterpriseLimitList, 'eid')
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
    // 活动企业-批量添加
    multiAddProviderItem() {
      this.providerList.forEach(item => {
        if (item.isSelect == false) {
          this.enterpriseLimitList.push(item)
          item.isSelect = true
        }
      })
      this.disableMultiAddProvider = true
    },
    // 活动企业-批量删除
    multiDelProviderItem () {
      this.enterpriseLimitList = []
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
        isAvailableQty: 1
      }
    },
    async getGoodsList() {
      let eidList = []
      this.enterpriseLimitList.forEach( item => {
        eidList.push(item.eid)
      })

      this.loading2 = true
      let goodsQuery = this.goodsQuery
      let data = await goodsB2bList(
        goodsQuery.page,
        goodsQuery.limit,
        this.type,
        this.form.merchantType,
        this.form.sponsorType,
        1,
        1,
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
        // 禁用批量添加按钮
        this.disableMultiAddGoods = this.disableMultiAddBtnStatus(this.goodsList, this.goodsLimitList, 'goodsId') || this.isAllGoodsItemInOtherActivity(this.goodsList)
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
      // 禁用批量添加按钮
      this.disableMultiAddGoods = this.disableMultiAddBtnStatus(this.goodsList, this.goodsLimitList, 'goodsId') || this.isAllGoodsItemInOtherActivity(this.goodsList)
    },
    goodsRemoveClick(index) {
      let currentArr = this.goodsLimitList
      currentArr.splice(index, 1)
      this.goodsLimitList = currentArr
    },
    // 商品-批量添加
    multiAddGoodsItem() {
      this.goodsList.forEach(item => {
        if (item.goodsDisableVO.isAllowSelect != 1 && item.isSelect == false) {
          this.goodsLimitList.push(item)
          item.isSelect = true
        }
      })
      this.disableMultiAddGoods = true
    },
    // 商品-批量删除
    multiDelGoodsItem () {
      this.goodsLimitList = []
    },
    getCurrentRow(row) {
      this.$log(row)
    },
    // 底部提交
    async saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {

          let activity = this.form
          activity.type = this.type

          if (activity.effectType == 2) {
            activity.beginTime = activity.time && activity.time.length ? activity.time[0] : undefined
            activity.endTime = activity.time && activity.time.length > 1 ? activity.time[1] : undefined
          } else {
            activity.beginTime = ''
            activity.endTime = ''
          }
          let secKillSpecial = {
            terminalType: activity.terminalType,
            permittedAreaType: activity.permittedAreaType,
            permittedEnterpriseType: activity.permittedEnterpriseType,
            permittedEnterpriseDetail: activity.permittedEnterpriseDetail
          }

          if (activity.permittedAreaType == 2) {
            let nodes = this.nodes
            let areaJsonString = JSON.stringify(nodes)
            secKillSpecial.permittedAreaDetail = areaJsonString
          }

          if (this.checkInputData()) {
            let params = {}
            if (this.id) {
              params.id = this.id
            }
            params.activity = activity
            params.enterpriseLimitList = this.enterpriseLimitList
            params.goodsLimitList = this.goodsLimitList
            params.secKillSpecial = secKillSpecial

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
      if (this.form.permittedAreaType == 2 && this.selectedData.length == 0) {
        this.$common.warn('请设置销售区域')
        return false
      }
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('请添加促销企业');
        return false
      }
      if (this.goodsLimitList.length == 0) {
        this.$common.warn('请添加促销商品');
        return false
      }

      let promotionPriceEmpty = false
      let allowBuyCountPriceEmpty = false
      for (let i = 0; i < this.goodsLimitList.length; i++) {
        let item = this.goodsLimitList[i]
        if (!item.promotionPrice) {
          promotionPriceEmpty = true
        }
        if (!item.allowBuyCount) {
          allowBuyCountPriceEmpty = true
        }
      }
      if (promotionPriceEmpty) {
        this.$common.warn('请设置活动价格');
        return false
      }
      if (allowBuyCountPriceEmpty) {
        this.$common.warn('请设置活动商品数量');
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
      this.enterpriseLimitList.forEach( item => {
        eidList.push(item.eid)
      })
      let eidListStr = eidList.join(',')

      let extralData = {
        eidList: eidListStr,
        sponsorType: this.form.sponsorType,
        merchantType: this.form.merchantType,
        type: this.type
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
    },
    // 禁用批量添加按钮状态
    disableMultiAddBtnStatus(checkArr, containerArr, id) {
      let result = checkArr.every(item => {
        return containerArr.some(obj => obj[id] == item[id])
      })
      return result
    },
    // 判断所有商品是否都已经在其他活动中添加
    isAllGoodsItemInOtherActivity(checkArr) {
      let result = checkArr.every(item => item.goodsDisableVO.isAllowSelect == 1)
      return result
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
