<template>
  <!-- 自动领券-添加优惠券 -->
  <yl-dialog title="选择赠品" :visible.sync="addGoodsDialog" width="966px" :show-footer="true" @confirm="saveConfirm">
    <div class="dialog-content-box-customer">
      <!-- 已添加商品 -->
      <div class="dialog-content-top">
        <div class="title-view">
          <el-radio-group v-model="type" @change="typeChange">
            <el-radio-button :label="1">商品优惠券</el-radio-button>
            <el-radio-button :label="2">会员优惠券</el-radio-button>
            <el-radio-button :label="3">抽奖次数</el-radio-button>
          </el-radio-group>
        </div>
        <div class="search-box mar-t-10">
          <el-row class="box">
            <el-col :span="6">
              <div class="title" v-if="type == 1 || type == 2">优惠券ID</div>
              <div class="title" v-if="type == 3">活动ID</div>
              <el-input v-model="query.id" placeholder="请输入" />
            </el-col>
            <el-col :span="6">
              <div class="title" v-if="type == 1 || type == 2">优惠券名称</div>
              <div class="title" v-if="type == 3">活动名称</div>
              <el-input v-model="query.name" placeholder="请输入" />
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
              <el-input v-if="type == 2 || type == 3" v-model="query.createUser" placeholder="请输入创建人" />
            </el-col>
            <el-col :span="6">
              <div class="title">运营备注</div>
              <el-input v-model="query.remark" placeholder="请输入运营备注" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
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
          <el-table-column label="活动ID" min-width="80" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.id }}</div>
            </template>
          </el-table-column>
          <el-table-column label="活动名称" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.activityName }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" min-width="162" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.startTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.endTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="160" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动状态" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.status | dictLabel(statusArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="活动进度" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.progress | dictLabel(lotteryActivityProgress) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="分类" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.category | dictLabel(categoryArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="预算金额" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.budgetAmount | toThousand }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="120" align="center">
            <template slot-scope="{ row }">
              <div class="goods-desc">
                <div class="font-size-base">{{ row.createUserName }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="运营备注" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div class="font-size-base">{{ row.opRemark }}</div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryCreatorByNmae, couponActivityQueryListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { memberCouponActivityQueryListPage } from '@/subject/admin/api/b2b_api/marketing_manage'
import { lotteryActivityQueryListPage } from '@/subject/admin/api/view_marketing/lottery_activity'
import { lotteryActivityProgress } from '@/subject/admin/busi/marketing/lottery'

// 选择赠品
export default {
  name: 'SelectGiftDialog',
  components: {},
  computed: {
    // 活动进度：1-未开始 2-进行中 3-已结束
    lotteryActivityProgress() {
      return lotteryActivityProgress()
    }
  },
  data() {
    return {
      // 活动状态
      statusArray: [
        {
          label: '启用',
          value: 1
        },
        {
          label: '停用',
          value: 2
        }
      ],
      // 分类
      categoryArray: [
        {
          label: '平台活动',
          value: 1
        },
        {
          label: '商家活动',
          value: 2
        }
      ],
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
      addGoodsDialog: false,
      loading: false,
      // 1-商品优惠券 2-会员优惠券 3-抽奖
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
      selectList: []
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
      this.$refs.multipleTable.clearSelection()
      this.bottomGoodsHandleReset()
      this.getBottomList()
    },
    init(selectList) {
      this.multipleSelection = []
      this.selectList = selectList
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
      } else {
        data = await lotteryActivityQueryListPage(
          query.page,
          query.limit,
          query.id,
          1,
          query.name,
          1,
          undefined,
          undefined,
          query.createUser,
          undefined,
          undefined,
          undefined,
          undefined,
          query.remark,
          3
        )
      }
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.total = data.total
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
      this.$log(this.multipleSelection)
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.addGoodsDialog = false
        this.$emit('save', this.type, this.multipleSelection)
      } else {
        this.$common.warn('请选择优惠券')
      }
      
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
