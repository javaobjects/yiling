<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
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
                <el-col :span="12">
                  <div class="title"><span class="red-text">*</span>活动时间</div>
                  <el-form-item prop="time">
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
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销类型</div>
                  <el-form-item prop="type">
                    <el-select v-model="form.type" placeholder="请选择">
                      <el-option label="满赠" :value="1"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>促销预算金额</div>
                  <el-form-item prop="budgetAmount">
                    <el-input v-model="form.budgetAmount" @keyup.native="form.budgetAmount = onInput(form.budgetAmount, 2)"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>满足条件</div>
                  <div class="amount-item">
                    <span class="font-light-color font-size-base left-text" style="margin-right:5px;">按总金额</span>
                    <el-form-item prop="promotionAmount">
                      <el-input v-model="form.promotionAmount" @keyup.native="form.promotionAmount = onInput(form.promotionAmount, 2)"></el-input>
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
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
              <el-link class="mar-r-10" type="primary" :underline="false" :href="'NO_7' | template">
                下载模板
              </el-link>
              <ylButton type="primary" plain @click="goImport">导入</ylButton>
              <!-- <ylButton type="danger" plain @click="addClick">批量删除</ylButton> -->
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
                    <yl-button type="text" @click="goodsRemoveClick($index)">删除</yl-button>
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
          <div class="font-size-base mar-b-8 font-title-color">赠送商品名称</div>
          <div>
            <el-input width="200" v-model="goodsGiftName" placeholder="赠送商品名称"></el-input>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog title="添加" :visible.sync="addGoodsDialog" width="966px" :show-footer="false">
        <div class="dialog-content-box-customer">
          <div class="dialog-content-top">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="8">
                  <div class="title">商品编号</div>
                  <el-input v-model="goodsQuery.id" placeholder="请输入商品编号" @keyup.enter.native="goodsHandleSearch" />
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
                  <yl-search-btn
                    :total="goodsTotal"
                    @search="goodsHandleSearch"
                    @reset="goodsHandleReset"
                  />
                </el-col>
              </el-row>
            </div>
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
              <el-table-column label="操作" min-width="55" align="center">
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
    </div>
  </div>
</template>

<script>
import {
  promotionActivityQueryById,
  goodsB2bList,
  promotionActivitySubmit
} from '@/subject/pop/api/b2b_api/marketing_manage';
import { onInputLimit } from '@/common/utils'
import { standardGoodsType } from '@/subject/pop/utils/busi'
import { formatDate } from '@/subject/pop/utils'
import { mapGetters } from 'vuex'
import { ylUploadFile } from '@/subject/pop/components'

export default {
  components: {
    ylUploadFile
  },
  computed: {
    standardGoodsType() {
      return standardGoodsType()
    },
    ...mapGetters(['currentEnterpriseInfo'])
  },
  data() {
    return {
      loading: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/b2b_dashboard'
        },
        {
          title: '营销管理'
        },
        {
          title: '营销活动'
        },
        {
          title: '编辑'
        }
      ],
      // 基本信息设置
      form: {
        name: '',
        time: [],
        type: '',
        budgetAmount: '',
        promotionAmount: '',
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '请输入促销名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }],
        type: [{ required: true, message: '请选择促销类型', trigger: 'change' }],
        budgetAmount: [{ required: true, message: '请输入促销预算金额', trigger: 'blur' }],
        promotionAmount: [{ required: true, message: '请输入总金额', trigger: 'blur' }]
      },
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
      // 赠品名称
      goodsGiftName: '',
      // 导入商品弹窗
      importDialog: false,
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
    let arr = []
    let enterpriseInfo = {
      eid: this.currentEnterpriseInfo.id,
      ename: this.currentEnterpriseInfo.name
    }
    arr.push(enterpriseInfo)
    this.enterpriseLimitList = arr
  },
  methods: {
    async getDetail() {
      let data = await promotionActivityQueryById(this.id)
      if (data) {
        let form = {
          name: data.promotionActivity.name,
          type: data.promotionActivity.type,
          budgetAmount: data.promotionActivity.budgetAmount,
          promotionAmount: data.promotionActivity.promotionAmount,
          remark: data.promotionActivity.remark
        }
        if (data.promotionActivity.beginTime && data.promotionActivity.endTime) {
          form.time = [formatDate(data.promotionActivity.beginTime), formatDate(data.promotionActivity.endTime)]
        }
        this.form = form

        // 复制进入
        if (this.operationType == 4) {
          this.id = ''
          this.goodsLimitList = []
        } else {
          this.goodsLimitList = data.promotionGoodsLimitList || []
        }

        this.platformSelected = data.promotionActivity.platformSelected || []
        this.goodsGiftName = data.goodsGiftName
      }
    },
    // 添加商品点击
    addGoodsClick() {
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
        isAvailableQty: 0
      }
    },
    async getGoodsList() {
      let eidList = [this.currentEnterpriseInfo.id]
      this.loading2 = true
      let goodsQuery = this.goodsQuery
      let data = await goodsB2bList(
        goodsQuery.page,
        goodsQuery.limit,
        1,
        goodsQuery.goodsStatus,
        eidList,
        this.id,
        goodsQuery.id,
        goodsQuery.name,
        goodsQuery.yilingGoodsFlag,
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
          return obj.id == item.id;
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
            params.goodsGiftName = this.goodsGiftName

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

      if (this.goodsLimitList.length == 0) {
        this.$common.warn('请添加促销商品');
        return false
      }
      if (this.platformSelected.length == 0) {
        this.$common.warn('请选择促销范围');
        return false
      }
      if (!this.goodsGiftName) {
        this.$common.warn('请填写赠送商品名称');
        return false
      }
      return true
    },
    // 去导入页面
    goImport() {
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('暂无促销企业信息')
        return false
      }
      if (!this.form.type) {
        this.$common.warn('请选择促销类型');
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
        type: this.form.type
      }
      if (this.id) {
        extralData.promotionActivityId = this.id
      }
      let info = {
        action: '/admin/b2b/api/v1/promotion/activity/importPromotionGoods',
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
