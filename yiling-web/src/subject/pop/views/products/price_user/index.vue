<template>
  <div class="app-container">
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content has-bottom-bar">
      <yl-tool-tip class="tool-tip">
        可以对同一个客户分组同一个商品，多次设置定价，系统会覆盖之前的设置，只执行最近的一次定价设置。<br>
        如果您想“批量导入商品”，请先“下载商品模板”，再通过模板批量导入。
      </yl-tool-tip>
      <div class="common-box">
        <div class="header-bar">
          <div class="sign"></div>商品信息
        </div>
        <div class="pro-table">
          <yl-table :show-header="false" :list="product">
            <el-table-column label="商品信息" min-width="900" align="center">
              <template slot-scope="{ row }">
                <div class="flex-row-left">
                  <el-image class="image" :src="row.pic" fit="contain" />
                  <div class="product-desc">
                    <div>商品名称：{{ row.name }}</div>
                    <div>规格：{{ row.sellSpecifications }}</div>
                  </div>
                  <div class="product-other text-l">
                    <div>批准文号：{{ row.licenseNo }}</div>
                    <div>生产厂商：{{ row.manufacturer }}</div>
                  </div>
                  <div class="product-other text-l">
                    <div>商品价格：{{ row.price | toThousand('￥') }}</div>
                    <div v-if="row.goodsLimitPrice && row.goodsLimitPrice.lowerLimitPrice">限价范围：{{ row.goodsLimitPrice.lowerLimitPrice }} - {{ row.goodsLimitPrice.upperLimitPrice }}</div>
                    <div v-else>限价范围：- -</div>
                  </div>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="top-btn font-size-lg">
        <span class="bold">数据列表</span>
        <div class="right-btn">
          <yl-button type="primary" @click="addUserGroup" plain>添加已有客户</yl-button>
        </div>
      </div>
      <div class="my-table">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column label="序号" min-width="71" align="center" type="index">
          </el-table-column>
          <el-table-column label="分组信息" min-width="365" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>{{ row.customerName }}</div>
                <div><span class="mar-r-10">{{ row.contactor }}</span>{{ row.contactorPhone }}</div>
                <div>{{ row.address }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="定价规则" min-width="225" align="center">
            <template slot-scope="{ row }">
              <el-radio-group v-model="row.priceRule" @change="priceRuleChange(row)">
                <div class="mar-b-10">
                  <el-radio :label="1">设置浮动点位</el-radio>
                </div>
                <div>
                  <el-radio :label="2">设置具体价格</el-radio>
                </div>
              </el-radio-group>
            </template>
          </el-table-column>
          <el-table-column label="定价" min-width="353" align="center">
            <template slot-scope="{ row }">
              <div class="text-l" v-if="row.priceRule === 1">
                <el-input-number v-model="row.priceValue" :min="-100" :precision="0" /> % 售价：{{ row.priceValue | floatChange(product) }}元
              </div>
              <div class="text-l" v-else>
                <el-input-number v-model="row.priceValue" :min="0.01" :precision="4"></el-input-number> 元
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="207" align="center">
            <template slot-scope="{ row, $index }">
              <div>
                <yl-button type="text" @click="saveUserSetPrice(row, $index)">保存</yl-button>
              </div>
              <div>
                <yl-button type="text" @click="delUserSetPrice(row, $index)">移除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <yl-dialog title="选择已有客户" :show-footer="false" width="60%" :visible.sync="showDialog">
        <div class="pad-16 dialog-box">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="24">
                <div class="title">企业地址</div>
                <yl-choose-address is-all width="220px" :province.sync="dialogQuery.provinceCode" :city.sync="dialogQuery.cityCode" :area.sync="dialogQuery.regionCode" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16">
            <el-row class="box" :gutter="16">
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="dialogQuery.name" @keyup.enter.native="searchEnter" placeholder="请输入企业名称" />
              </el-col>
              <el-col :span="8">
                <div class="title">企业类型</div>
                <el-select v-model="dialogQuery.type" placeholder="请选择企业类型">
                  <el-option v-for="item in companyType" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-col>
            </el-row>
          </div>
          <div class="mar-t-16 pad-t-16">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="dialogQuery.total" @search="handleSearch" @reset="handleReset" />
              </el-col>
              <el-col :span="8">
                <div class="flex-row-right right">
                  <yl-button type="primary" @click="allChoose" plain>批量添加</yl-button>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="mar-t-16">
            <yl-table :cell-no-pad="true" :list="userList" :total="dialogQuery.total" :page.sync="dialogQuery.page" :limit.sync="dialogQuery.limit" :loading="loading1" @getList="getUserList()">
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <el-row class="table-cell">
                    <el-col :span="22">
                      <div class="font-size-base font-title-color">
                        <div><span class="font-important-color bold mar-r-16">{{ row.customerName }}</span><span>{{ row.customerType }}</span></div>
                        <div>
                          {{ row.address }}
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="2">
                      <div>
                        <yl-button type="text" :disabled="row.select" @click="chooseUser(row, $index)">{{ row.select ? '已选择' : '选择' }}</yl-button>
                      </div>
                    </el-col>
                  </el-row>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getUserPriceList, getInfo, getCustomerList, saveUserPrice, delUserPrice } from '@/subject/pop/api/products'
import { enterpriseType } from '@/subject/pop/utils/busi'
import YlChooseAddress from '@/subject/pop/components/ChooseAddress/index';

export default {
  components: {
    YlChooseAddress
  },
  computed: {
    companyType() {
      return enterpriseType()
    }
  },
  filters: {
    floatChange: function (val, value) {
      if (!isNaN(val) && value && value.length) {
        let price = value[0].price
        price = price + price * (val / 100)
        return price.toFixed(4)
      }
      return val
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品定价',
          path: '/products/products_price'
        },
        {
          title: '客户定价'
        }
      ],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      dialogQuery: {
        total: 0,
        page: 1,
        limit: 10
      },
      dataList: [],
      loading: false,
      product: [],
      showDialog: false,
      userList: [],
      loading1: false
    }
  },
  mounted() {
    this.getList()
    this.getProductInfo(this.$route.params.id)
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getUserList()
      }
    },
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getUserPriceList(
        query.page,
        query.limit,
        '',
        this.$route.params.id
      )
      this.loading = false
      this.dataList = data.records
      this.query.total = data.total
      console.log(this.dataList);
    },
    // 获取商品
    async getProductInfo(goodId) {
      if (goodId) {
        this.$common.showLoad()
        let data = await getInfo(parseFloat(goodId))
        this.$common.hideLoad()
        if (data) {
          this.product.push(data)
        }
      }
    },
    // 添加已有客户分组
    addUserGroup() {
      this.getUserList(() => {
        this.showDialog = true
      })
    },
    // 跳转商品详情
    goDetailPage(row) {
      this.$router.push({
        path: `/products/index_edit/${row.id}`,
        query: {
          type: ''
        }
      })
    },
    // 获取用户列表
    async getUserList(callback) {
      this.loading1 = true
      let query = this.dialogQuery
      let data = await getCustomerList(
        query.page,
        query.limit,
        query.provinceCode,
        query.cityCode,
        query.regionCode,
        query.name,
        query.type,
        this.$route.params.id
      )
      this.loading1 = false
      if (data) {
        this.userList = data.records.map(item => {
          let find = this.dataList.find(itx => itx.customerEid === item.customerEid)
          item.select = item.selectedFlag || !!find
          item.priceRule = 2
          return item
        })
        this.dialogQuery.total = data.total
        if (callback) callback()
      }
    },
    handleSearch() {
      this.dialogQuery.page = 1
      this.getUserList()
    },
    handleReset() {
      this.dialogQuery = {
        page: 1,
        limit: 10
      }
    },
    // 批量添加
    allChoose() {
      if (this.userList.length) {
        let newUser = this.userList.filter(item => item.select === false)
        this.dataList.unshift(...newUser)
        this.showDialog = false
      }
    },
    chooseUser(user, index) {
      if (user) {
        this.dataList.unshift(user)
        this.userList[index].select = true
      }
    },
    // 定价规则改变
    priceRuleChange(row) {
      row.priceValue = ''
    },
    // 保存用户定价
    async saveUserSetPrice(row, index) {
      // 添加定价为空校验
      if (!row.priceValue) {
        this.$common.warn('浮动点数/价格不能为空')
        return
      }
      if (this.product[0].goodsLimitPrice && this.product[0].goodsLimitPrice.lowerLimitPrice) {
        let selectPrice = this.product[0].price + this.product[0].price * (row.priceValue / 100)
        // row.priceRule 1 浮动点位； 2 具体价格
        if (row.priceRule === 1 && selectPrice < this.product[0].goodsLimitPrice.lowerLimitPrice || row.priceRule === 1 && selectPrice > this.product[0].goodsLimitPrice.upperLimitPrice ) {
          this.$common.warn('价格需在限价范围内')
          return
        }
        if (row.priceRule === 2 && row.priceValue < this.product[0].goodsLimitPrice.lowerLimitPrice || row.priceRule === 2 && row.priceValue > this.product[0].goodsLimitPrice.upperLimitPrice ) {
          this.$common.warn('价格需在限价范围内')
          return
        }
      }
      this.$common.showLoad()
      let data = await saveUserPrice(
        row.customerEid,
        row.eid,
        parseFloat(this.$route.params.id),
        row.priceRule,
        row.priceValue
      )
      this.$common.hideLoad()
      if (data && data.id) {
        let array = Object.assign([], this.dataList)
        array[index].id = data.id
        this.dataList = array
        this.$common.n_success('保存成功')
      }
    },
    // 删除用户定价
    async delUserSetPrice(row, index) {
      if (row.id) {
        this.$common.showLoad()
        let data = await delUserPrice(
          row.id
        )
        this.$common.hideLoad()
        if (data && data.result) {
          this.dataList.splice(index, 1)
          this.$common.n_success('移除成功')
        }
      } else {
        this.dataList.splice(index, 1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
