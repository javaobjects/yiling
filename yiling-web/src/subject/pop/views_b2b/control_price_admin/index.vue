<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">企业名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入" />
            </el-col>
            <el-col :span="8">
              <div class="title">企业类型</div>
              <el-select v-model="query.enterprise" @change="selectChange" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in enterpriseType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">标签</div>
              <!-- <el-select v-model="query.tag" @change="selectChange" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in downTypeList" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select> -->
              <el-input v-model="query.tag" @keyup.enter.native="searchEnter" placeholder="请输入" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="15">
              <div class="title">企业地址</div>
              <div class="flex-row-left">
                <yl-choose-address is-all width="238px" :province.sync="query.provinceCode" :city.sync="query.cityCode" :area.sync="query.regionCode" />
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="top-title">
        <span>控价列表</span>
        <span></span>
      </div>
      <div class="my-table">
        <yl-table :show-header="true" stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :selection-change="handleSelectionChange" @getList="getList">
          <!-- <el-table-column type="selection" width="70">
          </el-table-column> -->
          <el-table-column label="企业ID" prop="customerEid" align="center">
          </el-table-column>
          <el-table-column label="企业信息" prop="id" min-width="200">
            <template slot-scope="{ row }">
              <div>
                <div class="products-box">
                  <div class="products-info">
                    <div class="font-blod">{{ row.customerName }}</div>
                    <div>{{ row.customerType }}</div>
                    <div>{{ row.address }}</div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="标签" prop="labelNameList" align="center">
          </el-table-column>
          <el-table-column label="联系人信息" min-width="151">
            <template slot-scope="{ row }">
              <div class="">
                <div class=" no-center">
                  <div>
                    <span class="color-666">联系人：</span>
                    <span>{{ row.contactor }} </span>
                  </div>
                  <div class="mar-t-8">
                    <span class="color-666">联系电话：</span>
                    <span>{{ row.contactorPhone }} </span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="管控" min-width="104">
            <template slot-scope="{ row }">
              <div>
                <div class="no-center">
                  <div class="font-blod">管控</div>
                  <!-- 	是否控制价格：0否 1是	 -->
                  <el-switch :value="row.limitFlag === 1" class="switch" @change="e => switchChange(e, row)">
                  </el-switch>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="是否开启推广会员" min-width="104">
            <template slot-scope="{ row }">
              <div>
                <div class="no-center">
                  <div class="font-blod">开启</div>
                  <!-- 	是否控制价格：0否 1是	 -->
                  <el-switch :value="row.recommendationFlag === 1" class="switch" @change="e => switchChangeFlag(e, row)">
                  </el-switch>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="最后操作信息" min-width="151">
            <template slot-scope="{ row }">
              <div>
                <div class=" no-center">
                  <div>
                    <span class="color-666">操作人：</span>
                    <span class="font-blod">{{ row.operatorName }} </span>
                  </div>
                  <div class="mar-t-8">
                    <span class="color-666">操作时间：</span>
                    <span>{{ row.operatorTime |formatDate }} </span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div>
                  <yl-button v-role-btn="['1']" type="text" @click="showDetailPage(row)">设置控价商品</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
  </div>
</template>

<script>
import { controlPriceList, setControlPriceStatus, setControlPriceRecommendationFlag } from '@/subject/pop/api/b2b_api/b2b_products'
import { enterpriseType, goodsStatus } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'

export default {
  name: 'ProductsIndex',
  components: {
    ylChooseAddress
  },
  computed: {
    enterpriseType() {
      let arr = enterpriseType()
      let arrs = arr.filter(function (item) {
        return item.value !== 1 && item.value !== 2
      })
      return arrs;
    },
    // goodsReason() {
    //   return goodsOutReason()
    // },
    goodStatus() {
      return goodsStatus()
    }
    // auditStatus() {
    //   return goodsAuditStatus()
    // }
  },
  data() {
    const validateQty = (rule, value, callback) => {
      let form = this.form
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
      if (!reg.test(value)) {
        return callback(new Error('请输入正整数'))
      } else {
        if (value >= form.frozenQty) {
          callback()
        } else {
          callback(new Error('修改的库存必须大于或等于占有库存'))
        }
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
          title: '控价管理'
        }
      ],
      query: {
        page: 1,
        limit: 10,
        customerGroupId: '',
        goodsId: '',
        name: '',
        enterprise: '', //
        tag: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      dataList: [
        { id: 1, price: 100, goodsStatus: 1 }
      ],
      stockDetailList: [
        { erp: 1, frozenQty: 20 },
        { erp: 2, frozenQty: 20 },
        { erp: 3, frozenQty: 20 }
      ],
      downTypeList: [
        { id: 1, name: '商家下架' },
        { id: 2, name: '运管紧急下架' },
        { id: 3, name: 'ERP同步下架' }
      ],
      dataCount: {},
      category: [],
      cateChild: [],
      loading: false,
      total: 100,
      form: {},
      showDialog: false,
      rules: {
        qtyNum: [{ required: true, validator: validateQty, trigger: 'blur' }]
      },
      showDetail: false,
      detailData: [],
      detailTotal: 0
    }
  },
  activated() {
    // this.getList(true)
    // this.getCate()
    // this.chooseProduct = []
  },
  created() {
    this.getList()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    async getList(type) {
      this.loading = true
      let query = this.query
      let data = await controlPriceList(
        query.page,
        query.limit,
        query.customerGroupId,
        query.goodsId,
        query.name,
        query.enterprise, // 企业类型
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.tag
      )
      this.loading = false
      console.log('===============' + data);
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },

    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        name: '',
        enterprise: '',
        // tag: '',
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      }
    },
    selectChange(value) {
      this.$log(value)
    },
    //  是否开启管控
    switchChange(type, row, index) {
      // 开启
      console.log(type);
      if (type) {
        this.changeCustomer(row.customerEid, 1)
      } else {
        // 关闭
        this.changeCustomer(row.customerEid, 0)
      }
    },
    //  管控接口调用
    async changeCustomer(id, status) {
      let arr = []
      arr.push(id)
      this.$common.showLoad()
      let data = await setControlPriceStatus(arr, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('开启成功')
        } else {
          this.$common.n_success('关闭成功')
        }
        this.getList()
      }
    },
    // 开启会员推广
    switchChangeFlag(type, row, index) {
      // 开启
      console.log(type);
      if (type) {
        this.changeRecommendationFlag(row.customerEid, 1)
      } else {
        // 关闭
        this.changeRecommendationFlag(row.customerEid, 0)
      }
    },
    //  会员接口调用
    async changeRecommendationFlag(id, status) {
      let arr = []
      arr.push(id)
      this.$common.showLoad()
      let data = await setControlPriceRecommendationFlag(arr, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('开启成功')
        } else {
          this.$common.n_success('关闭成功')
        }
        this.getList()
      }
    },
    // 查看编辑
    showDetailPage(row) {
      this.$router.push({
        path: `/b2b_products/control_price_detail/${row.customerEid}`,
        query: {
          type: 'see'
        }
      })
    },

    handleSelectionChange(val) {
      this.$log(val)
      this.chooseProduct = val
    }

  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
