<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 基本信息 -->
      <div class="common-box box-1 top-bar">
        <div class="header-bar mar-b-10">
          <div class="sign"></div>基本信息
        </div>
        <div class="info">
          <div class="info-img">
            <el-image class="my-image" :src="data.picUrl" fit="contain" />
          </div>
          <div class="info-desc">
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">商品名称：</span>{{ data.name||'--' }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">批准文号/注册证编号：</span>{{ data.licenseNo||'--' }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">生产地址：</span>{{ data.manufacturerAddress||'--' }}</div>
              </el-col>

            </el-row>
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">包装规格：</span>{{ data.specifications||'--' }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">生产厂家：</span>{{ data.manufacturer||'--' }}</div>
              </el-col>

              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">基本单位：</span>{{ data.unit||'--' }}</div>
              </el-col>

            </el-row>
            <el-row :gutter="24">
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">商品价格：</span>{{ data.price||'--' }}</div>
              </el-col>
              <el-col :span="8" :offset="0">
                <div class="intro"><span class="font-title-color">售卖规格：</span>{{ data.sellSpecifications||'--' }}</div>
              </el-col>
            </el-row>

          </div>
        </div>
      </div>
      <div class="flex-between mar-t-16 mar-b-8">
        <div class="tab ">
        </div>
        <div class="btn">
          <ylButton type="primary" plain @click="addUserGroup">添加已有客户分组</ylButton>
        </div>
      </div>
      <div class="my-table">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column label="序号" min-width="71" align="center" type="index">
          </el-table-column>
          <el-table-column label="分组信息" min-width="265" align="center">
            <template slot-scope="{ row }">
              <div class="product-desc">
                <div>{{ row.customerGroupName }}</div>
                <div><span class="font-title-color">客户数：</span>{{ row.customerNum }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="定价规则" min-width="325" align="center">
            <template slot-scope="{ row }">
              <el-radio-group v-model="row.priceRule">
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
                <el-input-number :min="0" v-model="row.priceValue" :precision="2"></el-input-number> 元
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
      <!-- 弹窗  -->
      <yl-dialog title="选择已有客户" width="60%" :show-footer="false" :visible.sync="showDialog">
        <div class="common-box  pad-16 dialog-box">
          <div class="search-box mar-t-16">
            <el-row class="box" :gutter="16">
              <el-col :span="8">
                <div class="title">分组名称</div>
                <el-input v-model="dialogQuery.name" @keyup.enter.native="searchEnter" placeholder="请输入分组名称" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box mar-t-16 pad-t-16 pad-b-16 box-line-b">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="dialogQuery.total" @search="handleSearch" @reset="handleReset" />
              </el-col>
              <el-col :span="8">
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <div class="flex-row-right right">
              <yl-button type="primary" @click="allChoose" plain>批量添加</yl-button>
            </div>
          </div>
          <div class="mar-t-16">
            <yl-table :cell-no-pad="true" :list="userList" :total="dialogQuery.total" :page.sync="dialogQuery.page" :limit.sync="dialogQuery.limit" :loading="loading1" @getList="getUserList()">
              <!-- <el-table-column>
                <template slot-scope="{ row, $index }">
                  <el-row class="table-cell">
                    <el-col :span="22">
                      <div class="font-size-base font-title-color">
                        <div><span class="font-important-color bold mar-r-16">{{ row.name }}</span><span>{{ row.customerType }}</span></div>
                        <div>
                          客户数：{{ row.customerNum }} 人
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
              </el-table-column> -->
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <el-row class="table-cell" :class="{ 'table-bg': $index % 2 == 0 }">
                    <el-col :span="22">
                      <div>
                        <div class="table-cell-title "><span class="bold mar-r-8">{{ row.name }}</span><span class="table-cell-type" v-if="row.customerType">{{ row.customerType }}</span></div>
                        <div>
                          客户数：{{ row.customerNum }} 人
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
import { getUserPriceGroupList, b2bProductDetail, saveUserGroupPrice, delUserGroupPrice, getCustomerGroupList } from '@/subject/pop/api/b2b_api/b2b_products'
import { enterpriseType } from '@/subject/pop/utils/busi'

export default {
  components: {
  },
  computed: {
    companyType() {
      let arr = enterpriseType()
      let arrs = arr.filter(function (item) {
        return item.value !== 1 && item.value !== 2
      })
      return arrs;
    }
  },
  filters: {
    floatChange: function (val, value) {
      if (!isNaN(val) && value && value.length) {
        let price = value[0].price
        price = price + price * (val / 100)
        return price.toFixed(2)
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
          path: '/b2b_dashboard'
        },
        {
          title: '商品管理',
          path: ''
        },
        {
          title: '商品定价',
          path: '/b2b_products/set_price_admin'
        },
        {
          title: '分组定价'
        }
      ],
      data: {},
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
    this.id = this.$route.params.id
    this.getList()
    this.getProductInfo()
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
      let data = await getUserPriceGroupList(
        query.page,
        query.limit,
        '',
        this.$route.params.id
      )
      this.loading = false
      if (data !== undefined) {
        this.dataList = data.records
        this.query.total = data.total
      }

    },
    // 获取商品
    async getProductInfo() {
      this.product = []
      this.$common.showLoad()
      let data = await b2bProductDetail(this.id)
      this.$common.hideLoad()
      this.$log(data)
      if (data) {
        if (this.type === 'reSubmit') {
          data.switch = true
        } else {
          data.switch = data.goodsStatus === 1
        }
        this.data = data
        this.product.push(data)
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
      let data = await getCustomerGroupList(
        query.page,
        query.limit,
        query.name,
        query.type,
        query.status,
        this.$route.params.id
      )
      this.loading1 = false
      if (data) {
        this.userList = data.records.map(item => {
          item.customerGroupId = item.id
          item.customerGroupName = item.name
          item.id = ''
          item.priceRule = 2
          let find = this.dataList.find(itx => itx.customerGroupId === item.customerGroupId)
          item.select = item.selectedFlag || !!find
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
    // 保存用户定价
    async saveUserSetPrice(row, index) {
      this.$common.showLoad()
      let data = await saveUserGroupPrice(
        row.customerGroupId,
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
        let data = await delUserGroupPrice(
          row.id
        )
        this.$common.hideLoad()
        if (data !== undefined) {
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
<style lang="scss" scoped>
.top-bar {
  .el-form-item__label {
    font-weight: 400;
    color: $font-title-color;
  }
  .el-input {
    width: 320px;
  }
}
</style>
