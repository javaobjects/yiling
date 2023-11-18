<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box" style="margin-top: 0;">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">商品编码</div>
              <el-input v-model="query.standardId" @keyup.enter.native="searchEnter" placeholder="请输入平台商品ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入商品名称" />
            </el-col>
            <el-col :span="6">
              <div class="title">批文许可</div>
              <el-input v-model="query.licenseNo" @keyup.enter.native="searchEnter" placeholder="批准文号/注册证编码/生产许可证" />
            </el-col>
            <el-col :span="6">
              <div class="title">生产厂家</div>
              <el-input v-model="query.manufacturer" @keyup.enter.native="searchEnter" placeholder="请输入生产厂家名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">外部编码</div>
              <el-input v-model="query.inSnOrSn" @keyup.enter.native="searchEnter" placeholder="请输入ERP内码或ERP编码" />
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
      <div class="bottom-table-view common-box mar-t-16 mar-b-8">
        <!-- tab切换 -->
        <div class="tab-box">
          <div class="tab">
            <div v-for="(item,index) in tabList" :key="index" class="tab-item" :class="tabActive === item.id?'tab-active':''" @click="clickTab(item.id)">{{ item.name }}</div>
          </div>
          <div class="btn-box">
            <yl-button v-role-btn="['4']" v-show="tabActive == 1" type="primary" plain @click="upDownChange(2)">批量下架</yl-button>
            <yl-button v-role-btn="['4']" v-show="tabActive == 2" type="primary" plain @click="upDownChange(1)">批量上架</yl-button>
          </div>
        </div>
        <!-- 提示语 -->
        <div class="prompt mar-t-16">列表信息（已为您检索出 <span>{{ total }}</span> 条数据）</div>
        <!-- table  -->
        <div class="my-table mar-t-16">
          <yl-table
            :show-header="true"
            stripe
            :list="dataList"
            :total="total"
            :page.sync="query.page"
            :limit.sync="query.limit"
            :loading="loading"
            :show-select-num="chooseProduct.length > 0"
            :select-num="chooseProduct.length"
            :selection-change="handleSelectionChange"
            @getList="getList"
          >
            <el-table-column key="tab-column" v-if="tabActive == 1 || tabActive == 2" type="selection" width="50"></el-table-column>
            <el-table-column key="goods-info" label="商品信息" prop="id" align="left" min-width="200">
              <template slot-scope="{ row }">
                <div>
                  <div class="products-box">
                    <el-image class="my-image" :src="row.pic" fit="contain" />
                    <div class="products-info">
                      <div class="products-name">{{ row.name }}</div>
                      <div class="basic-info products-id">商品：{{ row.id }}</div>
                      <div class="basic-info">{{ row.licenseNo }}</div>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品规格" align="left" prop="sellSpecifications" min-width="90">
              <template slot-scope="{ row }">
                <div class="specifications-box">
                  <div class="specifications-info">{{ row.sellSpecifications }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="生产厂家" align="left" prop="manufacturer" min-width="200">
              <template slot-scope="{ row }">
                <div class="facture-box">
                  <div class="facture-info">{{ row.manufacturer }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品类目" align="left" min-width="120">
              <template slot-scope="{ row }">
                <div class="category-box">
                  <span class="category" v-if="row.standardCategoryName1 && row.standardCategoryName2">{{ row.standardCategoryName1 }} ＞ {{ row.standardCategoryName2 }}</span>
                  <span class="basic-info" v-else>暂未匹配</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="供货价(元)" min-width="80" align="right">
              <template slot-scope="{ row }">
                <div class="price-box">
                  <span class="price" v-if="row.price">¥ {{ row.price }}</span>
                  <span class="basic-info" v-else>暂无价格</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column key="goods-stock" label="库存信息" min-width="70" align="left">
              <template slot-scope="{ row }">
                <div class="table-content">
                  <yl-button v-role-btn="['5']" type="text" @click="stockDetail(row)">详情</yl-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column key="goods-status-column" v-if="query.status == 0" label="状态" align="left">
              <template slot-scope="{ row }">
                <div class="status-box">
                  <div :class="['status-icon', auditStatus(row.goodsStatus)]"></div>
                  <div class="status-text">{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
                  <!-- <el-switch :value="row.goodsStatus === 1" class="switch" @change="e => switchChange(e, row)"></el-switch> -->
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="120">
              <template slot-scope="{ row }">
                <div class="table-content">
                  <yl-button v-if="query.status == 2" v-role-btn="['6']" type="text" @click="switchChange(true, row)">上架</yl-button>
                  <yl-button v-if="query.status == 1" v-role-btn="['6']" type="text" @click="switchChange(false, row)">下架</yl-button>
                  <yl-button v-role-btn="['1']" type="text" @click="showDetailPage(row)">编辑</yl-button>
                  <yl-button v-role-btn="['2']" type="text" @click="setPrice(row)">限价设置</yl-button>
                  <yl-button v-role-btn="['3']" type="text" @click="controlSale(row)">控销设置</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
      <!-- 库存弹窗 -->
      <yl-dialog title="查看库存信息" :visible.sync="showDialog" @confirm="confirm">
        <div class="pad-16">
          <div>
            <span class="font-title-color">商品名称：</span>
            <span class="font-important-color">{{ form.name }}</span>
            <span class="font-title-color mar-l-32">商品ID：</span>
            <span class="font-important-color">{{ form.id }}</span>
          </div>
          <div class="mar-t-24">
            <yl-table :show-header="true" stripe border :list="stockDetailList">
              <el-table-column label="ERP内码" align="center" prop="inSn"></el-table-column>
              <el-table-column label="ERP编码" align="center" prop="sn"></el-table-column>
              <el-table-column label="包装数量" align="center" prop="packageNumber"></el-table-column>
              <el-table-column label="总库存" align="center" prop="qty"></el-table-column>
              <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
              <el-table-column label="可售库存" align="center">
                <template slot-scope="{ row }">
                  <div>{{ row.qty - row.frozenQty }}</div>
                </template>
              </el-table-column>
              <el-table-column label="编辑库存" align="center" v-if="erpSyncLevel === 0">
                <template slot-scope="{ row,$index}">
                  <el-input v-model="row.editNumber" placeholder="" size="mini" clearable @change="editStock(row,$index)"></el-input>
                </template>
              </el-table-column>
            </yl-table>
            <div class="tool">
              <yl-tool-tip>修改的库存必须大于或等于占有库存</yl-tool-tip>
            </div>
          </div>
        </div>
      </yl-dialog>

    </div>
  </div>
</template>

<script>
import { b2bProductEdit, b2bProAllUpOrDown, getB2BProductList } from '@/subject/pop/api/b2b_api/b2b_products'
import { queryCurrentEnterpriseInfo } from '@/subject/pop/api/zt_api/dashboard'
import { goodsStatus, goodsOutReason } from '@/subject/pop/utils/busi'

export default {
  name: 'B2BProductsList',
  components: {
  },
  computed: {
    goodsReason() {
      return goodsOutReason()
    },
    goodStatus() {
      return goodsStatus()
    },
    // 状态
    auditStatus() {
      return status => {
        let statusStr = ''
        switch (status) {
          case 1:
            statusStr = 'pass'
            break
          case 2:
            statusStr = 'reject'
            break
          case 3:
            statusStr = 'pending'
            break
          default:
            statusStr = ''
            break
        }
        return statusStr
      }
    }
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
        status: 0,
        // 商品编码
        standardId: '',
        // 外部编码
        inSnOrSn: ''
      },
      // 	企业是否开通erp 0：未开通 1：开通
      erpSyncLevel: 0,
      dataList: [],
      stockDetailList: [],
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
    // this.getCate()
    // this.chooseProduct = []
    this.getList()
    this.getCurrentEnterpriseInfo()
  },
  created() {
    // this.getList()
  },
  mounted() {
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
    // 获取当前登录人企业信息(是否已对接ERP)
    async getCurrentEnterpriseInfo() {
      this.$common.showLoad()
      let data = await queryCurrentEnterpriseInfo()
      this.$common.hideLoad()
      if (data && data.enterpriseInfo) {
        this.erpSyncLevel = data.enterpriseInfo.erpSyncLevel
      }
    },
    //  获取控价商品列表
    async getList(type) {
      this.dataList = []
      this.loading = true
      let query = this.query
      let data = await getB2BProductList(
        query.page,
        query.limit,
        query.name,
        query.licenseNo,
        query.manufacturer,
        query.outReason,
        query.status,
        query.standardId,
        query.inSnOrSn
      )
      this.loading = false
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records
        this.total = data.total
        // if (type) {
        //   data.count = data.upShelfCount + data.unShelfCount + data.underReviewCount + data.rejectCount
        //   this.dataCount = data
        // }
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
      console.log(ids);
      this.changeProductAll(ids, type)
    },
    // 批量上下架
    async changeProductAll(ids, status) {
      this.$common.showLoad()
      let data = await b2bProAllUpOrDown(ids, status)
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
      console.log(type);
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
      this.stockDetailList = row.goodsSkuList
      this.stockDetailList.forEach(item => {
        this.$set(item, 'editNumber', item.qty)
      })
      console.log(this.stockDetailList);
      this.showDialog = true
    },
    editStock(row, index) {
      console.log(row);
      console.log(index);
      console.log(row.editNumber);
    },
    // 确定修改库存详情
    async confirm() {
      for (let i = 0; i < this.stockDetailList.length; i++) {
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
      for (let i = 0; i < this.stockDetailList.length; i++) {
        let obj = {}
        obj.frozenQty = this.stockDetailList[i].frozenQty
        obj.id = this.stockDetailList[i].id
        obj.in = this.stockDetailList[i].in
        obj.inSn = this.stockDetailList[i].inSn
        obj.packageNumber = this.stockDetailList[i].packageNumber
        obj.qty = this.stockDetailList[i].editNumber
        obj.remark = this.stockDetailList[i].remark
        arr.push(obj)
      }
      this.$common.showLoad()
      let data = await b2bProductEdit(this.form.id, null, null, arr)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.showDialog = false
        this.$common.n_success('修改库存成功')
        this.getList()
      }
    },

    validateNumber(value, item) {
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
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
        path: `/b2b_products/products_edit/${row.id}`,
        query: {
          type: 'see'
        }
      })
    },
    // 限价
    setPrice(row) {
      this.$router.push({
        path: `/b2b_products/products_set_price/${row.id}`,
        query: {
          type: 'reSubmit'
        }
      })
    },
    controlSale(row) {
      this.$router.push({
        path: `/b2b_products/products_control_sale/${row.id}`,
        query: {
          type: 'reSubmit'
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
