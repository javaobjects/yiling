<template>
  <!-- 自动领券-添加优惠券 -->
  <yl-dialog 
    title="选择商品" 
    :visible.sync="addGoodsDialog" 
    width="900px" 
    :show-footer="false" 
   >
    <div class="dialog-content-box-customer">
      <!-- 已添加商品 -->
      <div class="title-view">
        <el-radio-group v-model="type" @change="typeChange">
          <el-radio-button :label="3">赠品库</el-radio-button>
          <el-radio-button :label="1">商品优惠券</el-radio-button>
          <el-radio-button :label="2">会员优惠券</el-radio-button>
        </el-radio-group>
      </div>
      <div v-show="type == 1 || type == 2">
        <div class="dialog-content-top">
          <div class="search-box mar-t-10">
            <el-row class="box">
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
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
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
            @getList="getBottomList"
          >
            <!-- <el-table-column type="selection" align="center" width="70"></el-table-column> -->
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
            <el-table-column label="操作" min-width="80" align="center" fixed="right">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="addClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div v-show="type == 3">
        <div class="dialog-content-top">
          <div class="search-box mar-t-10">
            <el-row class="box">
              <el-col :span="6">
                <div class="title">商品编号</div>
                <el-input v-model="giftStore.id" placeholder="请输入商品编号" />
              </el-col>
              <el-col :span="6">
                <div class="title">商品名称</div>
                <el-input v-model="giftStore.name" placeholder="请输入商品名称" />
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
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :show-header="true"
            :stripe="true"
            :list="dataList2"
            :loading="loading2">
            <el-table-column label="商品编号" min-width="80" align="center" prop="id">
            </el-table-column>
            <el-table-column label="商品名称" min-width="120" align="center" prop="name">
            </el-table-column>
            <el-table-column label="类别" min-width="120" align="center">
              <template slot-scope="{ row }">
                <div>{{ row.goodsType | dictLabel(integralGoodsTypeData) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品价值" min-width="100" align="center" prop="price">
            </el-table-column>
            <el-table-column label="可用库存" min-width="100" align="center" prop="availableQuantity">
            </el-table-column>
            <el-table-column label="操作" min-width="80" align="center" fixed="right">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="view-btn" type="text" @click="addClick(row)">添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
import { queryCreatorByNmae, couponActivityQueryListPage, memberCouponActivityQueryListPage } from '@/subject/admin/api/b2b_api/discount_coupon'
import { giftList } from '@/subject/admin/api/b2b_api/integral_record'
import { integralGoodsType } from '@/subject/admin/busi/b2b/integral'
export default {
  components: {},
  computed: {
    integralGoodsTypeData() {
      return integralGoodsType()
    }
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
      addGoodsDialog: false,
      loading: false,
      type: 1,
      query: {
        page: 1,
        limit: 10,
        name: '',
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
      //赠品库
      giftStore: {
        //所属业务（1-2b)
        businessType: 1,
        id: '',
        name: ''
      },
      dataList2: [],
      loading2: false
    };
  },
  mounted() {
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
      if (type == 3) {
        this.giftDataApi()
      } else {
        this.bottomGoodsHandleReset()
        this.getBottomList()
      }
    },
    init(show) {
      this.addGoodsDialog = show;
      this.typeChange(3)
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
          query.remark
        )
      } else {
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
          query.createUser
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
      if (this.type == 1 || this.type == 2) {
        this.query.page = 1;
        this.getBottomList()
      } else {
        this.giftStore.page = 1;
        this.giftDataApi()
      }
      
    },
    bottomGoodsHandleReset() {
      if (this.type == 1 || this.type == 2) {
        this.query = {
          page: 1,
          limit: 10,
          createUserName: '',
          createUser: '',
          remark: ''
        }
      } else {
        this.giftStore = {
          //所属业务（1-2b)
          businessType: 1,
          id: '',
          name: ''
        }
      }
    },
    //赠品库列表
    async giftDataApi() {
      let query = this.giftStore
      this.loading2 = true;
      let data = await giftList(
        query.businessType,
        query.id,
        query.name
      )
      if (data) {
        this.dataList2 = data.list
        this.total = data.list.length
      }
      this.loading2 = false
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 保存点击
    // saveConfirm() {
    //   if (this.multipleSelection && this.multipleSelection.length > 0) {
    //     this.addGoodsDialog = false
    //     this.$emit('save', this.multipleSelection)
    //   } else {
    //     this.$common.warn('请选择优惠券')
    //   }
    // }
    //点击添加
    addClick(row){
      this.addGoodsDialog = false
      let data = {
        conter: row,
        type: this.type
      }
      this.$emit('commodityChange', data)
    }
  }
};
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
