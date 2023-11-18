<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">批准文号/注册证编号</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="请输入批准文号/注册证编号" />
            </el-col>
            <el-col :span="6">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家" />
            </el-col>
            <el-col :span="6">
              <div class="title">下架原因</div>
              <el-select v-model="query.outReason" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in goodsReason" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
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

      <!-- tab切换 -->
      <div class="flex-between mar-t-16 mar-b-8">
        <div class="tab">
          <div 
            v-for="(item, index) in tabList" 
            :key="index" 
            class="tab-item" 
            :class="tabActive === item.id ? 'tab-active' : ''" 
            @click="clickTab(item.id)">{{ item.name }}</div>
        </div>
        <div>
          <ylButton 
            v-role-btn="['4']" 
            v-show="tabActive == 2" 
            type="text" 
            plain 
            @click="upDownChange(1)">批量上架</ylButton>
          <ylButton 
            v-role-btn="['4']" 
            v-show="tabActive == 1" 
            type="text" 
            plain 
            @click="upDownChange(2)">批量下架</ylButton>
        </div>
      </div>
      <!-- table -->
      <div class="my-table mar-t-8">
        <yl-table 
          :show-header="true" 
          stripe 
          :list="dataList" 
          :total="total" 
          :page.sync="query.page" 
          :limit.sync="query.limit" 
          :loading="loading" 
          :selection-change="handleSelectionChange" 
          @getList="getList">
          <el-table-column able-column v-if="tabActive == 1 || tabActive == 2" type="selection" width="70">
          </el-table-column>
          <el-table-column label="商品信息" prop="id" min-width="200">
            <template slot-scope="{ row }">
              <div class="products-box">
                <div class="products-id"><span class="color-666">商品ID：</span>{{ row.id }}</div>
                <el-image class="my-image" :src="row.pic" fit="contain" />
                <div class="products-info">
                  <div>{{ row.name }}</div>
                  <div>{{ row.sellSpecifications }}</div>
                  <div>{{ row.licenseNo }}</div>
                  <div>{{ row.manufacturer }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="94" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">{{ row.price | toThousand('￥') }}</div>
            </template>
          </el-table-column>
          <el-table-column label="销售组织" min-width="94" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">{{ row.ename }}</div>
            </template>
          </el-table-column>

          <el-table-column label="库存信息" min-width="151" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button v-role-btn="['2']" type="text" @click="stockDetail(row)">库存详情</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="104" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div>
                  <div v-if="row.goodsStatus == 1" class="color-green">{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
                  <div v-else>{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
                  <el-switch :value="row.goodsStatus === 1" class="switch" @change="e => switchChange(e, row)">
                  </el-switch>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button v-role-btn="['1']" type="text" @click="showDetailPage(row)">编辑</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 库存弹窗 -->
      <yl-dialog title="查看库存信息" :visible.sync="showDialog" width="1000px" @confirm="confirm">
        <div class="pad-16">
          <div class="dialog-title">库存汇总</div>
          <div>
            <span class="font-title-color">商品名称：</span>
            <span class="font-important-color">{{ form.name }}</span>
            <span class="font-title-color mar-l-32">售卖规格：</span>
            <span class="font-important-color">{{ form.sellSpecifications }}</span>
            <span class="font-title-color mar-l-32">商品ID：</span>
            <span class="font-important-color">{{ form.id }}</span>
            <span class="font-title-color mar-l-32">销售组织：</span>
            <span class="font-important-color">{{ form.ename }}</span>
          </div>
          <div class="mar-t-24">
            <yl-table 
              class="dia-repertory"
              :show-header="true" 
              border 
              highlight-current-row
              :list="stockDetailList" 
              @row-click="rowClick">
                <el-table-column label="ERP内码" align="center" prop="inSn"></el-table-column>
                <el-table-column label="ERP编码" align="center" prop="sn"></el-table-column>
                <el-table-column label="包装数量" align="center" prop="packageNumber"></el-table-column>
                <el-table-column label="本店库存" align="center" prop="realQty"></el-table-column>
                <el-table-column label="总库存" align="center" prop="qty"></el-table-column>
                <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
                <el-table-column label="可售库存" align="center">
                  <template slot-scope="{ row }">
                    <div>{{ row.qty - row.frozenQty }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="编辑本店库存" align="center" min-width="95">
                  <template slot-scope="{ row, $index }">
                    <el-input v-model="row.editNumber" size="mini" clearable @change="editStock(row, $index)"></el-input>
                  </template>
                </el-table-column>
            </yl-table>
            <div class="tool">
              <yl-tool-tip>修改的库存必须大于或等于占有库存</yl-tool-tip>
            </div>
          </div>
          <div>
              <div class="dialog-title mar-b-0">订阅库存</div>
          </div>
          <div class="mar-t-10" v-if="inventoryDetailList.length > 0">
            <yl-table :show-header="true" stripe border :list="inventoryDetailList">
              <el-table-column label="ERP内码" align="center" prop="sourceInSn"></el-table-column>
              <el-table-column label="库存来源" align="center">
                <template slot-scope="{ row }">
                  <div>{{ row.subscriptionEname || '- -' }}</div>
                </template>
              </el-table-column>
              <el-table-column label="来源方ERP内码" align="center" prop="inSn"></el-table-column>
              <el-table-column label="库存数量" align="center" prop="qty"></el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { getCompanyProductList, ProductEdit, updateStatusAll, getCheckList, getInventoryDetail } from '@/subject/pop/api/products'
import { goodsStatus, goodsOutReason } from '@/subject/pop/utils/busi'
export default {
  name: 'ProductsIndex',
  computed: {
    goodsReason() {
      return goodsOutReason()
    },
    goodStatus() {
      return goodsStatus()
    }
  },
  data() {
    const validateQty = (rule, value, callback) => {
      let form = this.form
      //最长9位数字，可修改
      var reg = /^((?!-1)\d{0,10})$/;
      if (!reg.test(value)) {
        return callback(new Error('请输入正整数'))
      } else {
        if (value >= form.frozenQty) {
          callback()
        } else {
          callback(new Error('库存数量必须大于等于占有库存数量！'))
        }
      }
    }
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
          title: '商品列表'
        }
      ],
      query: {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: '',
        outReason: 0,
        // 全部，已上架，已下架 ，带设置
        status: 0
      },
      dataList: [],
      stockDetailList: [],
      inventoryDetailList: [],
      downTypeList: [
        { id: 1, name: '商家下架' },
        { id: 2, name: '运管紧急下架' },
        { id: 3, name: 'ERP同步下架' }
      ],
      loading: false,
      total: 0,
      form: {},
      showDialog: false,
      rules: {
        qtyNum: [{ required: true, validator: validateQty, trigger: 'blur' }]
      },
      showDetail: false,
      detailData: [],
      detailTotal: 0,
      tabList: [
        { name: '全部商品', id: 0 },
        { name: '已上架', id: 1 },
        { name: '已下架', id: 2 },
        { name: '待设置', id: 3 }
      ],
      tabActive: 0,
      chooseProduct: []
    }
  },
  activated() {
    this.getList()
  },
  created() {
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    clickTab(e) {
      this.tabActive = e;
      this.query.status = e;
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    //  获取商品列表
    async getList(type) {
      this.dataList = []
      this.loading = true
      let query = this.query
      let data = await getCompanyProductList(
        query.page,
        query.limit,
        // 下架原因
        query.outReason,
        // 状态
        query.status, 
        query.licenseNo,
        query.manufacturer,
        query.name
      )
      this.loading = false
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
        licenseNo: '',
        manufacturer: '',
        outReason: 0,
        status: ''
      }
    },
    // 批量上下架
    upDownChange(type) {
      // type  1 批量上架   2 批量下架
      if (this.chooseProduct.length === 0) {
        this.$common.error('暂未选择任何商品')
        return
      }
      let ids = this.chooseProduct.map(item => {
        return item.id
      })
      this.changeProductAll(ids, type)
    },
    // 批量上下架
    async changeProductAll(ids, status) {
      this.$common.showLoad()
      let data = await updateStatusAll(ids, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('上架成功')
        } else {
          this.$common.n_success('下架成功')
        }
        this.getList()
      }
    },
    // 单独 上下架状态
    switchChange(type, row, index) {
      // 上架
      if (type) {
        this.changeProductAll([row.id], 1)
      } else {
        // 下架
        this.changeProductAll([row.id], 2)
      }
    },
    // // 单独上下架
    // async changeProduct(id, status) {
    //   this.$common.showLoad()
    //   let data = await b2bProductEdit(id, status)
    //   this.$common.hideLoad()
    //   if (data !== undefined) {
    //     if (status === 1) {
    //       this.$common.n_success('上架成功')
    //     } else {
    //       this.$common.n_success('下架成功')
    //     }
    //     this.getList()
    //   }
    // },
    // 展示库存弹框
    stockDetail(row) {
      this.form = row
      this.inventoryDetailList = []
      this.stockDetailList = row.goodsSkuList
      this.stockDetailList.forEach(item => {
        this.$set(item, 'editNumber', item.realQty)
      })
      this.showDialog = true
    },
    editStock(row, index) {
      console.log(row);
      console.log(index);
      console.log(row.editNumber);
    },
    // 确定修改库存详情
    async confirm() {
      for (let i = 0; i < this.stockDetailList.length; i ++) {
        let newRealQty = ''
        if ( Number(this.stockDetailList[i].qty) - Number(this.stockDetailList[i].realQty) <= 0 ) {
          newRealQty = Number(this.stockDetailList[i].editNumber)
        } else {
          newRealQty = Number(this.stockDetailList[i].qty) - Number(this.stockDetailList[i].realQty) + Number(this.stockDetailList[i].editNumber)
        }
        let flag = this.validateNumber(newRealQty, this.stockDetailList[i].frozenQty)
        if (flag === 2) {
          return this.$message.error('修改后的总库存必须大于或等于占有库存')
        } else if (flag === 1) {
          return this.$message.error('请输入正整数')
        }
      }
      let arr = []
      for (let i = 0; i < this.stockDetailList.length; i ++) {
        let obj = {}
        obj.frozenQty = this.stockDetailList[i].frozenQty
        obj.id = this.stockDetailList[i].id
        obj.sn = this.stockDetailList[i].sn
        obj.inSn = this.stockDetailList[i].inSn
        obj.packageNumber = this.stockDetailList[i].packageNumber
        obj.qty = this.stockDetailList[i].editNumber
        obj.remark = this.stockDetailList[i].remark
        arr.push(obj)
      }
      this.$common.showLoad()
      let data = await ProductEdit(this.form.id, null, null, arr)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.showDialog = false
        this.$common.n_success('修改库存成功')
        this.getList()
      }
    },
    validateNumber(value, item) {
      //最长9位数字，可修改
      var reg = /^((?!-1)\d{0,10})$/;
      console.log(value);
      if (!reg.test(value)) {
        return 1;
      } else {
        if (value >= item) {
          return 3;
        } else {
          return 2;
        }
      }
    },
    // 获取审核详情
    async getDetailList(callback) {
      this.$common.showLoad()
      let data = await getCheckList(1, 10, this.rowId)
      this.$common.hideLoad()
      if (data) {
        this.detailData = data.records
        this.detailTotal = data.total
        if (callback) callback()
      }
    },
    // 查看编辑
    showDetailPage(row) {
      this.$router.push({
        path: `/products/index_edit/${row.id}`,
        query: {
          type: 'see'
        }
      })
    },
    handleSelectionChange(val) {
      this.chooseProduct = val
    },
    async rowClick(row) {
      this.$common.showLoad()
      let data = await getInventoryDetail(row.id)
      this.$common.hideLoad()
      if (typeof data !== 'undefined'){
        this.inventoryDetailList = data
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
