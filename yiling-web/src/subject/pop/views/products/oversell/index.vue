<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="8">
              <div class="title">批准文号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号/注册证编号" />
            </el-col>
            <el-col :span="8">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
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
      <div class="top-btn font-size-lg">
        <div class="right-btn">
          <yl-button v-role-btn="[1]" type="primary" @click="addProduct" plain>添加商品</yl-button>
        </div>
      </div>
      <!-- 商品列表  -->
      <div class="my-table mar-t-8">
        <yl-table
          :show-header="true"
          stripe
          :list="dataList"
          :total="total"
          :page.sync="query.current"
          :limit.sync="query.size"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="商品信息" prop="id" min-width="200">
            <template slot-scope="{ row }">
              <div class="products-info-box">
                <div class="products-box">
                  <el-image class="my-image" :src="row.pic" fit="contain" />
                  <div class="products-info">
                    <div class="products-info-item goods-id"><span>商品ID:</span>{{ row.id }}</div>
                    <div class="products-info-item">{{ row.name }}</div>
                    <div class="products-info-item">{{ row.sellSpecifications }}</div>
                    <div class="products-info-item">{{ row.licenseNo }}</div>
                    <div class="products-info-item">{{ row.manufacturer }}</div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="94" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">¥{{ row.price }}</div>
            </template>
          </el-table-column>
          <el-table-column label="库存" min-width="151" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button v-role-btn="['2']" type="text" @click="stockDetail(row)">库存详情</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="104" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div :class="row.goodsStatus == 1 ? 'color-green' : ''">
                  {{ row.goodsStatus | dictLabel(goodStatus) }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button v-role-btn="['3']" type="text" @click="delProductItem(row)">移除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 添加商品 -->
      <choose-product ref="choose" @refreshList="getList" />
      <!-- 库存弹窗 -->
      <yl-dialog title="查看库存信息" :visible.sync="showDialog" @confirm="showDialog = false">
        <div class="pad-16">
          <div>
            <span class="font-title-color">商品名称:</span>
            <span class="font-important-color">{{ form.name }}</span>
            <span class="font-title-color mar-l-32">商品ID:</span>
            <span class="font-important-color">{{ form.id }}</span>
          </div>
          <div class="mar-t-24">
            <yl-table :show-header="true" stripe border :list="stockDetailList">
              <el-table-column label="ERP内码" align="center" prop="inSn">
                <template slot-scope="{ row }">
                  <div>{{ row.inSn ? row.inSn : "- -" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="ERP编码" align="center" prop="sn">
                <template slot-scope="{ row }">
                  <div>{{ row.sn ? row.sn : "- -" }}</div>
                </template>
              </el-table-column>
              <el-table-column label="包装数量" align="center" prop="packageNumber"></el-table-column>
              <el-table-column label="总库存" align="center" prop="qty"></el-table-column>
              <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
              <el-table-column label="可售库存" align="center">
                <template slot-scope="{ row }">
                  <div>{{ row.qty - row.frozenQty }}</div>
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
import { getOverSoldList, updateOverSold } from '@/subject/pop/api/products'
import { goodsStatus } from '@/subject/pop/utils/busi'
import ChooseProduct from './component/ChooseProduct'
export default {
  name: 'OversellProducts',
  components: {
    ChooseProduct
  },
  computed: {
    goodStatus() {
      return goodsStatus()
    }
  },
  data() {
    return {
      //  头部导航
      navList: [
        {
          title: '首页',
          path: '/dashboard'
        },
        {
          title: '商品管理'
        },
        {
          title: '超卖商品管理'
        }
      ],
      query: {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        //  是否超卖 0-非超卖 1-超卖
        overSoldType: 1
      },
      dataList: [],
      stockDetailList: [],
      loading: false,
      total: 0,
      form: {},
      showDialog: false
    }
  },
  activated() {
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
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getOverSoldList(
        query.current,
        query.size,
        query.name,
        query.licenseNo,
        query.manufacturer,
        query.overSoldType
      )
      this.loading = false
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
      }
    },
    handleSearch() {
      this.query.current = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        current: 1,
        size: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      }
    },
    //  展示库存弹框
    stockDetail(row) {
      this.form = row
      this.stockDetailList = row.goodsSkuList
      if (this.stockDetailList && this.stockDetailList.length > 0) {
        this.stockDetailList.forEach(item => {
          this.$set(item, 'editNumber', item.qty)
        })
      }
      this.showDialog = true
    },
    validateNumber(value, item) {
      var reg = /^((?!-1)\d{0,10})$/ // 最长9位数字，可修改
      if (!reg.test(value)) {
        return 1
      } else {
        if (value >= item) {
          return 3
        } else {
          return 2
        }
      }
    },
    //  移除商品
    delProductItem(row) {
      this.$confirm('确定移除该商品吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'del-oversell-prd-cfm'
      }).then(async () => {
        //  确认删除 1.删除id数组集合 2.操作类型: 0-移除 1-添加
        let data = await updateOverSold([row.id], 0)
        if (data && data.result == true) {
          this.$common.n_success('移除成功')
          await this.getList()
        }
      })
    },
    //  打开选择商品
    addProduct() {
      this.$refs.choose.chooseProduct()
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
.del-oversell-prd-cfm {
  .el-message-box__header {
    padding-top: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
    .el-message-box__title {
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      text-align: center;
    }
  }
  .el-message-box__container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 31px 0;
    .el-message-box__status {
      position: relative;
      color: #fa8c15;
      font-size: 16px !important;
      transform: translateY(0);
    }
    .el-message-box__message {
      padding-left: 3px;
    }
  }
  .el-message-box__btns {
    border-top: 1px solid #f0f0f0;
  }
}
</style>
