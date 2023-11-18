<template>
  <!-- 自动领券-添加优惠券 -->
  <yl-dialog title="选择奖品" :visible.sync="addGoodsDialog" width="966px" :show-footer="true" @confirm="saveConfirm">
    <div class="dialog-content-box-customer" :class="type == 4 || type == 5 ? 'minHeight' : ''">
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <el-radio-group v-model="type" @change="typeChange">
            <el-radio-button :label="1">商品优惠券</el-radio-button>
            <el-radio-button :label="2">会员优惠券</el-radio-button>
            <el-radio-button :label="3">赠品库</el-radio-button>
            <el-radio-button :label="4">空奖</el-radio-button>
            <el-radio-button :label="5">抽奖机会</el-radio-button>
          </el-radio-group>
        </div>
        <div class="search-box mar-t-10" v-if="type == 1 || type == 2 || type == 3">
          <el-row class="box" v-if="type == 1 || type == 2">
            <el-col :span="6">
              <div class="title">优惠券ID</div>
              <el-input v-model="query.id" placeholder="请输入优惠券ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">优惠券名称</div>
              <el-input v-model="query.name" placeholder="请输入优惠券名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">创建人</div>
              <el-autocomplete
                v-if="type == 1"
                v-model="query.createUserName"
                value-key="createrName"
                value="id"
                :fetch-suggestions="querySearchAsync"
                :trigger-on-focus="false"
                placeholder="请选择创建人"
                @input="createUserNameHandle"
                @select="handleSelect"
              ></el-autocomplete>
              <el-input v-if="type == 2" v-model="query.createUser" placeholder="请输入创建人" />
            </el-col>
            <el-col :span="6">
              <div class="title">运营备注</div>
              <el-input v-model="query.remark" placeholder="请输入运营备注" />
            </el-col>
          </el-row>
          <el-row class="box" v-if="type == 3">
            <el-col :span="6">
              <div class="title">商品编号</div>
              <el-input v-model="query.id" placeholder="请输入商品编号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" placeholder="请输入商品名称" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box" v-if="type == 1 || type == 2 || type == 3">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn
                :total="total"
                @search="bottomGoodsHandleSearch"
                @reset="bottomGoodsHandleReset"
              />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;" v-if="type == 1 || type == 2">
        <yl-table
          ref="multipleTable"
          :stripe="true"
          :show-header="true"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :selection-change="handleSelectionChange"
          row-key="id"
          @getList="getBottomList"
        >
          <el-table-column type="selection" :reserve-selection="true" align="center" width="70"></el-table-column>
          <el-table-column label="优惠券ID" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="优惠券名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.name }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="生效时间" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div v-if="row.useDateType == 1" class="font-size-base">{{ row.beginTime | formatDate }} - {{ row.endTime | formatDate }}</div>
              <div v-else-if="row.useDateType == 2" class="font-size-base">{{ row.giveOutEffectiveRules }}</div>
            </template>
          </el-table-column>
          <el-table-column label="总数量" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.totalCount }}</div>
            </template>
          </el-table-column>
          <el-table-column label="剩余数量" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.surplusCount }}</div>
            </template>
          </el-table-column>
          <el-table-column label="活动类型" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.sponsorType | dictLabel(sponsorTypeArray) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="预算编号" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.budgetCode }}</div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.createUserName }}</div>
            </template>
          </el-table-column>
          <el-table-column label="运营备注" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.remark }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;" v-if="type == 3">
        <yl-table
          ref="multipleTable"
          :stripe="true"
          :show-header="true"
          :show-pagin="false"
          :list="dataList"
          :total="total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          :selection-change="handleSelectionChange"
          row-key="id"
          @getList="getBottomList"
        >
          <el-table-column type="selection" :reserve-selection="true" align="center" width="70"></el-table-column>
          <el-table-column label="商品编号" min-width="80" align="center">
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
          <el-table-column label="类别" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.goodsType | dictLabel(goodsTypeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="数量单位" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.unit }}</div>
            </template>
          </el-table-column>
          <el-table-column label="商品价值" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="剩余数量" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.availableQuantity }}</div>
            </template>
          </el-table-column>
          <el-table-column label="总数量" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.quantity }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 空奖、抽奖机会 -->
      <div class="dialog-content-bottom" v-if="type == 4 || type == 5">
        <el-form :rules="rules" ref="dataForm" :model="form" label-position="left" label-width="110px" class="demo-ruleForm">
          <div class="activity-form-item flex-row-left" v-if="type == 4">
            <el-form-item label="空奖提示语" prop="emptyWords">
              <el-input class="input-box" v-model="form.emptyWords" placeholder="请输入空奖提示语"></el-input>
              <span class="tip-text">客户未抽中奖品时，展示空奖提示语</span>
            </el-form-item>
          </div>
          <div class="activity-form-item flex-row-left" v-if="type == 5">
            <el-form-item label="赠送抽奖次数" prop="rewardNumber">
              <el-input class="input-box" v-model="form.rewardNumber" placeholder="请输入赠送抽奖次数" @keyup.native="form.rewardNumber = onInput(form.rewardNumber, 0)"></el-input>
              <span class="tip-text">只支持赠送当前活动的抽奖次数</span>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryCreatorByNmae, couponActivityQueryListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { memberCouponActivityQueryListPage } from '@/subject/admin/api/b2b_api/marketing_manage'
import { getGiftList } from '@/subject/admin/api/view_marketing/gift_manage'
import { onInputLimit } from '@/common/utils'

// 选择赠品
export default {
  name: 'SelectGiftDialog',
  components: {},
  computed: {
  },
  data() {
    return {
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
      addGoodsDialog: false,
      loading: false,
      // 所属业务 1-b端 2-c端
      businessType: 1,
      type: 1,
      query: {
        page: 1,
        limit: 10,
        // 创建人输入的内容
        createUserName: '',
        // 下来选项点击的id
        createUser: '',
        remark: ''
      },
      dataList: [],
      total: 0,
      // 当前界面勾选的优惠券
      multipleSelection: [],
      // 已经选择的优惠券
      selectList: [],
      form: {
        emptyWords: '',
        rewardNumber: ''
      },
      rules: {
        emptyWords: [{ required: true, message: '请输入', trigger: 'blur' }],
        rewardNumber: [{ required: true, message: '请输入', trigger: 'blur' }]
      }
    };
  },
  mounted() {
    console.log('mounted')
  },
  methods: {
    async querySearchAsync(queryString, cb) {
      let data = await queryCreatorByNmae(queryString)
      if (data && data.list) {
        cb(data.list)
      }
    },
    handleSelect(item) {
      this.query.createUser = item.id
    },
    createUserNameHandle() {
      this.query.createUser = ''
    },
    // 优惠券类型切换
    typeChange(type) {
      this.type = type
      this.dataList = []
      this.total = 0
      if (type == 1 || type == 2 || type == 3) {
        this.$refs.multipleTable.clearSelection()
      }
      this.multipleSelection = []
      this.bottomGoodsHandleReset()
      this.getBottomList()
    },
    init(selectList, businessType) {
      this.multipleSelection = []
      this.selectList = selectList
      this.businessType = businessType
      this.type = 1
      this.dataList = []
      this.total = 0
      this.bottomGoodsHandleReset()
      this.addGoodsDialog = true;
      this.getBottomList()
    },
    async getBottomList() {
      let query = this.query
      let data = null
      this.loading = true
      if (this.type == 1) {
        data = await couponActivityQueryListPage(
          query.page,
          query.limit,
          query.name,
          query.id,
          undefined,
          undefined,
          undefined,
          undefined,
          1,
          query.createUser,
          undefined,
          query.remark,
          2
        )
      } else if (this.type == 2) {
        data = await memberCouponActivityQueryListPage(
          query.page,
          query.limit,
          query.name,
          query.id,
          undefined,
          undefined,
          undefined,
          undefined,
          1,
          query.remark,
          query.createUser,
          2
        )
      } else if (this.type == 3) {
        data = await getGiftList(
          query.name,
          query.id,
          this.businessType,
          [1, 2]
        )
      } 
      this.loading = false
      if (data) {
        this.dataList = this.type == 3 ? data.list : data.records
        this.total = this.type == 3 ? data.list.length : data.total
      }
    },
    // 搜索
    bottomGoodsHandleSearch() {
      this.query.page = 1
      this.getBottomList()
    },
    bottomGoodsHandleReset() {
      this.query = {
        page: 1,
        limit: 10,
        createUserName: '',
        createUser: '',
        remark: ''
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getRowKeys(row) {
      return row.id
    },
    // 保存点击
    saveConfirm() {
      if (this.type == 1 || this.type == 2 || this.type == 3) {
        if (this.multipleSelection && this.multipleSelection.length > 0) {
          this.addGoodsDialog = false
          this.$emit('save', this.multipleSelection, this.type)
        } else {
          this.$common.warn('请选择奖品')
        }
      } else {
        this.$refs['dataForm'].validate(async (valid) => {
          if (valid) {
            let form = this.form
            let arr = []
            if (this.type == 4) {
              let obj = {
                rewardType: 5,
                rewardName: '空奖',
                emptyWords: form.emptyWords
              }
              arr.push(obj)
            }
            if (this.type == 5) {
              let obj = {
                rewardType: 6,
                rewardName: '抽奖机会',
                rewardNumber: form.rewardNumber
              }
              arr.push(obj)
            }
            this.addGoodsDialog = false
            this.$emit('save', arr, this.type)
          } else {
            console.log('error submit!!');
            return false;
          }
        })
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
