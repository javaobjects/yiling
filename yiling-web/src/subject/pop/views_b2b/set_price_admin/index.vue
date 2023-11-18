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
          <yl-tool-tip class="mar-t-16">提示：价格优先级为：促销价>客户定价>客户分组定价>基价；<br>
            提示：对同一个客户或同一客户分组同一个商品多次设置定价，系统会覆盖之前的设置，只执行最近的一次定价设置；<br>
            提示：如果您想批量设置商品定价，请先“下载商品定价模板”，再通过“导入商品定价模版”进行批量导入；<br>
          </yl-tool-tip>
        </div>
      </div>
      <div class="down-box">
        <div class="btn">
          <span v-role-btn="['3']">
            <!-- <el-link v-if="isDownFile" class="mar-r-10" type="primary" plain :underline="false" :href="'NO_9' | template">
              下载商品定价模板
            </el-link> -->
            <yl-button v-if="isDownFile" class="mar-r-10" type="primary" plain @click="downUrl(setPriceUrl)">下载商品定价模板</yl-button>
          </span>
          <span v-role-btn="['4']">
            <yl-button v-if="isDownFile" type="primary" plain @click="goImport">导入商品定价模版</yl-button>
          </span>
        </div>
      </div>
      <div class="my-table">
        <yl-table :show-header="true" stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :selection-change="handleSelectionChange" @getList="getList">
          <el-table-column label="商品信息" prop="id" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                <div class="products-box">
                  <el-image class="my-image" :src="row.pic" fit="contain" />
                  <div class="products-info">
                    <div>商品ID：{{ row.id }}</div>
                    <div>{{ row.name }}</div>
                    <div>{{ row.sellSpecifications }}</div>
                    <div>{{ row.licenseNo }}</div>
                    <div>{{ row.manufacturer }}</div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="价格" min-width="94" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">￥{{ row.price.toFixed(2) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="客户定价" min-width="151" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <span class="color-666">已设置：</span>
                <span>{{ row.customerPrice }} </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="分组定价" min-width="104" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <span class="color-666">已设置：</span>
                <span>{{ row.customerGroupPrice }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div>
                  <yl-button v-role-btn="['1']" type="text" @click="setCustomer(row)">设置客户定价</yl-button>
                  <yl-button v-role-btn="['2']" type="text" @click="setCustomerGroup(row)">设置客户分组定价</yl-button>
                </div>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <yl-dialog title="编辑库存信息" :visible.sync="showDialog" @confirm="confirm">
        <div class="pad-16">
          <el-row class="font-important-color font-size-base" style="padding-bottom: 24px;">
            <el-col :span="12">
              <span class="font-title-color">商品名称：</span>
              <span class="font-important-color">{{ form.name }}</span>
            </el-col>
            <el-col :span="12">
              <span class="font-title-color">商品ID：</span>
              <span class="font-important-color">{{ form.id }}</span>
            </el-col>
          </el-row>
        </div>
        <div class="pad-16">
          <yl-table :show-header="true" border :list="stockDetailList">
            <el-table-column label="ERP内码" align="center" prop="erp"></el-table-column>
            <el-table-column label="ERP编码" align="center" prop="auditUser"></el-table-column>
            <el-table-column label="数量" align="center"></el-table-column>
            <el-table-column label="总库存" align="center"></el-table-column>
            <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
            <el-table-column label="可售库存" align="center" prop="rejectMessage"></el-table-column>
            <el-table-column label="编辑库存" align="center" prop="rejectMessage">
              <template slot-scope="{ row,$index}">
                <el-input v-model="row.editNumber" placeholder="" size="normal" clearable @change="editStock(row,$index)"></el-input>
              </template>
            </el-table-column>
          </yl-table>
          <div class="tool">
            <yl-tool-tip>修改的库存必须大于或等于占有库存</yl-tool-tip>
          </div>
        </div>

      </yl-dialog>

    </div>
  </div>
</template>

<script>
import { getPricePageList, getBussinessLevel } from '@/subject/pop/api/b2b_api/b2b_products'
import { goodsStatus } from '@/subject/pop/utils/busi'
import { downloadByUrl } from '@/subject/pop/utils'
export default {
  name: 'ProductsIndex',
  components: {
  },
  computed: {
    goodStatus() {
      return goodsStatus()
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
          title: '定价管理'
        }
      ],
      query: {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      },
      dataList: [

      ],
      stockDetailList: [

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
      detailTotal: 0,
      isDownFile: false,
      setPriceUrl: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_price_templadate_v20211130.xlsx'
    }
  },
  activated() { },
  created() {
    this.getList()
    this.getLevel()
  },
  methods: {
    // Enter
    searchEnter(e) {
      const keyCode = window.event ? e.keyCode : e.which;
      if (keyCode === 13) {
        this.getList()
      }
    },
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/admin/b2b/api/v1/price/import'
        }
      })
    },
    downUrl(url) {
      console.log(url);
      downloadByUrl(url)
    },
    async getList(type) {
      this.loading = true
      let query = this.query
      let data = await getPricePageList(
        query.page,
        query.limit,
        '',
        query.licenseNo,
        query.name,
        query.manufacturer
      )
      this.loading = false
      console.log(data);
      if (data) {
        this.dataList = data.records
        this.total = data.total
        if (type) {
          data.count = data.upShelfCount + data.unShelfCount + data.underReviewCount + data.rejectCount
          this.dataCount = data
        }
      }
    },
    async getLevel() {
      let data = await getBussinessLevel();
      console.log(data);
      if (data !== undefined) {
        // 1 的时候可以下载，导入
        if (data === 1) this.isDownFile = true
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
        manufacturer: ''
      }
    },
    // tab点击
    handleTabClick() {
      this.query.page = 1
      this.query.limit = 10
      this.getList()
    },
    // 获取分类
    async getCate() {
      if (this.category.length === 0) {
        let { list } = await getProductCategory()
        this.category = list
        this.$log(list)
      }
    },
    // 批量上下架
    upDownChange(type) {
      if (this.chooseProduct.length === 0) {
        this.$common.warn('暂未选择任何商品')
        return
      }
      let ids = this.chooseProduct.map(item => {
        return item.id
      })
      this.changeProductAll(ids, type)
    },
    async changeProductAll(ids, status) {
      this.$common.showLoad()
      let data = await updateStatusAll(ids, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('批量上架成功')
        } else {
          this.$common.n_success('批量下架成功')
        }
        this.getList()
      }
    },
    async changeProduct(id, status) {
      this.$common.showLoad()
      let data = await changeProductInfo(id, status)
      this.$common.hideLoad()
      if (data !== undefined) {
        if (status === 1) {
          this.$common.n_success('上架成功')
        } else {
          this.$common.n_success('下架成功')
        }
        this.getList()
        this.getListCount()
      }
    },
    checkSelect(row, index) {
      return row.goodsStatus === 1 || row.goodsStatus === 2
    },
    // 展示库存弹框
    stockDetail(row) {
      this.form = row
      this.showDialog = true
    },
    editStock(row, index) {
      console.log(row);
      console.log(row.editNumber);
    },
    // 确定修改库存
    confirm() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          let form = this.form
          this.$common.showLoad()
          let data = await changeProductInfo(form.id, null, null, null, form.qtyNum)
          this.$common.hideLoad()
          if (data !== undefined) {
            this.showDialog = false
            this.$common.n_success('修改库存成功')
            this.getList()
          }
        } else {
          return false
        }
      })
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
    setCustomer(row) {
      let type = ''
      if (row.goodsStatus === 5) {
        type = 'see'
      }
      this.$router.push({
        path: `/b2b_products/set_customer_price_detail/${row.id}`,
        query: {
          type: type
        }
      })
    },
    // 查看编辑
    setCustomerGroup(row) {
      let type = ''
      if (row.goodsStatus === 5) {
        type = 'see'
      }
      this.$router.push({
        path: `/b2b_products/set_customer_group_price_detail/${row.id}`,
        query: {
          type: type
        }
      })
    },
    reSubmit(row) {
      this.$router.push({
        path: `/products/index_edit/${row.id}`,
        query: {
          type: 'reSubmit'
        }
      })
    },
    handleSelectionChange(val) {
      this.$log(val)
      this.chooseProduct = val
    },
    // 查询审核详情
    showCheck(row) {
      this.rowId = row.id
      this.getDetailList(() => {
        this.showDetail = true
      })
    },
    // 跳转日志页面
    goLogs(row) {
      this.$router.push({
        path: `/products/index_log/${row.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
