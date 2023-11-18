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
      <!-- tab -->
      <div class="flex-between mar-t-16 mar-b-8">
        <div class="tab ">
        </div>
        <div class="btn">
          <yl-button class="width-100" type="primary" plain @click="add">新建</yl-button>
        </div>
      </div>
      <!-- table -->
      <div class="common-box">
        <div class="mar-t-16">
          <yl-table :show-header="true" :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
            <el-table-column label="序号" type="index" align="center"></el-table-column>
            <el-table-column label="客户信息" align="center">
              <template slot-scope="{ row }">
                <div>
                  <div>{{ row.customerName }}</div>
                  <div><span>{{ row.contactor }}</span> <span>{{ row.contactorPhone }}</span> </div>
                  <div>{{ row.address }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="定价规则" align="center" prop="rule">
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
            <el-table-column label="定价" align="center">
              <template slot-scope="{ row }">
                <div class="text-l" v-if="row.priceRule === 1">
                  <el-input-number v-model="row.priceValue" :min="-100" :precision="0" /> % 售价：{{ row.priceValue | floatChange(product) }}元
                </div>
                <div class="text-l" v-else>
                  <el-input-number v-model="row.priceValue" :min="0.01" :precision="2"></el-input-number> 元
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row ,$index }">
                <yl-button type="text" @click="saveUserSetPrice(row,$index)">保存</yl-button>
                <!-- <yl-button v-else type="text" @click="edit(row,$index)">编辑</yl-button> -->
                <yl-button type="text" @click="delUserSetPrice(row,$index)">移除</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <div class="bottom-bar-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 弹窗 -->
      <!-- 批量导入渠道类型弹框 -->
      <yl-dialog title="选择已有客户" :show-footer="false" width="900px" :visible.sync="showDialog">
        <div class="common-box">
          <div class="search-box " style="margin-top:0">
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
          <div class="search-box mar-t-16">
            <el-row class="box">
              <el-col :span="20">
                <div class="title">企业地址</div>
                <yl-choose-address is-all width="220px" :province.sync="dialogQuery.provinceCode" :city.sync="dialogQuery.cityCode" :area.sync="dialogQuery.regionCode" />
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
                <template slot-scope="{ row }">
                  <div class="table-cell">
                    <div>
                      <div class="table-cell-title "><span class="bold mar-r-8">{{ row.customerName }}</span><span class="table-cell-type" v-if="row.customerType">{{ row.customerType }}</span></div>
                      <div>
                        {{ row.address }}
                      </div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column width="100px">
                <template slot-scope="{ row, $index }">
                  <div class="table-cell">
                    <yl-button type="text" :disabled="row.select" @click="chooseUser(row, $index)">{{ row.select ? '已选择' : '选择' }}</yl-button>
                  </div>
                </template>
              </el-table-column> -->
              <el-table-column>
                <template slot-scope="{ row, $index }">
                  <el-row class="table-cell" :class="{ 'table-bg': $index % 2 == 0 }">
                    <el-col :span="22">
                      <div>
                        <div class="table-cell-title "><span class="bold mar-r-8">{{ row.customerName }}</span><span class="table-cell-type" v-if="row.customerType">{{ row.customerType }}</span></div>
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
import { b2bProductDetail, getUserPriceList, getCustomerList, saveUserPrice, delUserPrice } from '@/subject/pop/api/b2b_api/b2b_products'
import { getCurrentUser } from '@/subject/pop/utils/auth'
import { goodsStatus, enterpriseType, goodsOutReason, goodsPatent } from '@/subject/pop/utils/busi'
import { ylChooseAddress } from '@/subject/pop/components'
export default {
  components: {
    ylChooseAddress
  },
  computed: {
    companyType() {
      let arr = enterpriseType()
      let arrs = arr.filter(function (item) {
        return item.value !== 1 && item.value !== 2
      })
      return arrs;
    },
    goodStatus() {
      return goodsStatus()
    },
    goodsReason() {
      return goodsOutReason()
    },
    // 专利非专利
    patent() {
      return goodsPatent()
    },
    isYiLing() {
      let user = getCurrentUser()
      let flag = null;
      if (user.currentEnterpriseInfo) {
        if (user.currentEnterpriseInfo.yilingFlag) {
          flag = !!user.currentEnterpriseInfo.yilingFlag
        }
      }
      return flag
    }
  },
  filters: {
    floatChange: function (val, value) {
      console.log(val)
      console.log(value)

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
          title: '定价管理',
          path: '/b2b_products/set_price_admin'
        },
        {
          title: '设置客户定价'
        }
      ],
      data: {},
      query: {
        page: 1,
        limit: 10,
        name: '',
        enterprise: '',
        createTime: [],
        provinceCode: '',
        cityCode: '',
        regionCode: ''
      },
      dataList: [
      ],
      total: 0,
      loading: false,
      show: false,
      // 表格选择
      dialogQuery: {
        total: 0,
        page: 1,
        limit: 10
      },
      userList: [],
      loading1: false,
      showDialog: false,
      product: []
    }
  },
  mounted() {
    this.id = this.$route.params.id
    if (this.id) {
      this.id = parseFloat(this.id)
      this.getData()
      this.getList()
    }
    this.$log(this.$route)
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getUserList()
      }
    },
    add() {
      this.getUserList(() => {
        this.showDialog = true
      })
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
      if (data !== undefined) {
        this.dataList = data.records
        this.query.total = data.total
      }

      console.log(this.dataList);
    },
    async getData() {
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
    // 弹窗选择商品
    chooseUser(user, index) {
      if (user) {
        this.dataList.unshift(user)
        this.userList[index].select = true
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
