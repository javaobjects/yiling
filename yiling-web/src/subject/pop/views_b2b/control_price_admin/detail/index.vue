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

      <div class="down-box mar-b-16">
        <div class="btn">
          <el-link v-if="isDownFile" class="mar-r-10" type="primary" :underline="false" :href="'NO_8' | template">
            下载模板
          </el-link>
          <el-link v-if="isDownFile" class="mar-r-10" type="primary" :underline="false" @click="goImport">批量导入</el-link>
          <ylButton type="primary" plain @click="addGoods(1)">添加商品</ylButton>

        </div>
      </div>
      <div class="my-table mar-t-8">
        <yl-table :show-header="true" :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" :selection-change="handleSelectionChange" @getList="getList">
          <!-- <el-table-column v-if="tabActive == 1 || tabActive == 2" type="selection" width="70">
          </el-table-column> -->
          <el-table-column label="商品ID" prop="id" align="center" width="100">
          </el-table-column>
          <el-table-column label="商品信息" prop="id" min-width="200">
            <template slot-scope="{ row }">
              <div>
                <div class="products-box">
                  <el-image class="my-image" :src="row.picUrl" fit="contain" />
                  <div class="products-info">
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
          <el-table-column label="库存信息" min-width="151" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button type="text" @click="stockDetail(row)">库存详情</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="104" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <div>
                  <!-- <div>已上架</div> -->
                  <div v-if="row.goodsStatus==1 " class="color-green">{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
                  <div v-else>{{ row.goodsStatus | dictLabel(goodStatus) }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="{ row }">
              <div class="table-content">
                <yl-button type="text" @click="delet(row)">移除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 编辑库存信息 -->
      <yl-dialog title="库存信息" :visible.sync="showDialog" @confirm="showDialog = false">
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
            <el-table-column label="ERP内码" align="center" prop="inSn"></el-table-column>
            <el-table-column label="ERP编码" align="center" prop="in"></el-table-column>
            <el-table-column label="包装数量" align="center" prop="packageNumber"></el-table-column>
            <el-table-column label="总库存" align="center" prop="qty"></el-table-column>
            <el-table-column label="占有库存" align="center" prop="frozenQty"></el-table-column>
            <!-- <el-table-column label="编辑库存" align="center" prop="rejectMessage">
              <template slot-scope="{ row,$index}">
                <el-input v-model="row.editNumber" placeholder="" size="normal" clearable @change="editStock(row,$index)"></el-input>
              </template>
            </el-table-column> -->
          </yl-table>
          <!-- <div class="tool">
            <yl-tool-tip>修改的库存必须大于或等于占有库存</yl-tool-tip>
          </div> -->
        </div>

      </yl-dialog>

      <!-- 弹窗 -->
      <yl-dialog title="添加商品" :visible.sync="showAdd" @confirm=" showAdd = false">
        <div class="common-box">
          <div class="search-box">
            <el-form :model="form1" :rules="rules" ref="form1" label-width="72px" label-position="top">
              <el-row :gutter="24">
                <el-col :span="8">
                  <el-form-item label="商品名称">
                    <el-input v-model="form1.goodsName" placeholder="请输入" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="批准文号">
                    <el-input v-model="form1.licenseNo" placeholder="请输入" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="生产厂家">
                    <el-input v-model="form1.manufacturer" placeholder="请输入" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="商品ID">
                    <el-input onkeyup="value=value.replace(/[^\d]/g,'')" v-model="form1.goodsId" placeholder="请输入" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="24">
                  <yl-search-btn :total="total1" @search="handleSearch1" @reset="handleReset1" />
                </el-col>
              </el-row>
            </el-form>
          </div>
          <!-- tab -->
          <div class="flex-between mar-t-16 mar-b-8">
            <div class="tab ">
            </div>
            <div class="btn">
              <ylButton type="primary" plain @click="allAdd">搜索结果批量添加</ylButton>
            </div>
          </div>
          <yl-table stripe :show-header="true" :list="dataList1" :total="total1" :page.sync="form1.page" :limit.sync="form1.limit" :loading="loading1" @getList="getDialogList">
            <!-- <el-table-column type="selection" width="70"></el-table-column> -->
            <el-table-column label="商品id" align="center" prop="id"></el-table-column>
            <el-table-column label="商品名称" align="center" prop="name"></el-table-column>
            <el-table-column label="批准文号" align="center" prop="licenseNo"> </el-table-column>
            <el-table-column label="规格" align="center" prop="sellSpecifications"></el-table-column>
            <el-table-column label="生产厂家" align="center" prop="manufacturer"></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{ row }">
                <yl-button v-if="row.goodsDisableVO.limitDisable" type="text" disabled>已添加</yl-button>
                <yl-button v-else type="text" @click="add(row)">添加</yl-button>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </yl-dialog>

    </div>
  </div>
</template>

<script>
import { ControlPriceListById, ControlPriceGoodsListDialog, ControlPriceListAllAdd, ContorlPriceListDelet, ControlPriceListAdd, getBussinessLevel } from '@/subject/pop/api/b2b_api/b2b_products'
import { goodsStatus } from '@/subject/pop/utils/busi'

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
          title: '控价管理',
          path: '/b2b_products/control_price_admin'
        },
        {
          title: '控价详情'
        }
      ],
      id: '',
      query: {
        page: 1,
        limit: 10,
        name: '',
        licenseNo: '',
        manufacturer: ''
      },
      dataList: [
        // { id: 1, price: 100, goodsStatus: 1 },
        // { id: 2, price: 100, goodsStatus: 2 },
        // { id: 3, price: 100, goodsStatus: 3 },
        // { id: 4, price: 100, goodsStatus: 4 },
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
      total: 0,
      form: {},
      showDialog: false,
      rules: {
        qtyNum: [{ required: true, validator: validateQty, trigger: 'blur' }]
      },
      showDetail: false,
      detailData: [],
      detailTotal: 0,
      // 添加商品
      showAdd: false,
      form1: {
        page: 1,
        limit: 10,
        goodsName: '',
        goodsId: '',
        licenseNo: '',
        manufacturer: ''
      },
      dataList1: [],
      total1: 0,
      loading1: false,
      isDownFile: false
    }
  },
  activated() {
    // this.getList(true)
    // this.getCate()
    // this.chooseProduct = []
  },
  created() {
    this.id = this.$route.params.id
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
    async getList(type) {
      this.loading = true
      let query = this.query
      let data = await ControlPriceListById(
        query.page,
        query.limit,
        query.name,
        this.id,
        query.licenseNo,
        query.manufacturer
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
        outReason: 0
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
    // 弹窗搜索
    handleSearch1() {
      this.form1.page = 1
      this.getDialogList()
    },
    // 弹窗搜索
    handleReset1() {
      this.form1 = {
        page: 1,
        limit: 10,
        goodsName: '',
        goodsId: '',
        licenseNo: '',
        manufacturer: ''
      }
      this.getDialogList()
    },
    // 添加商品
    addGoods() {
      this.showAdd = true;
      this.getDialogList()
    },
    // 获取弹窗添加商品
    async getDialogList() {
      this.loading1 = true
      let data = await ControlPriceGoodsListDialog(
        this.form1.page,
        this.form1.limit,
        this.id,
        this.form1.goodsName,
        this.form1.licenseNo,
        this.form1.manufacturer,
        this.form1.goodsId
      )
      console.log(data);
      this.loading1 = false
      if (data !== undefined) {
        this.dataList1 = data.records
        this.total1 = data.total
      }
    },
    //  批量添加搜索结果
    async allAdd() {
      let data = await ControlPriceListAllAdd(
        this.form1.page,
        this.form1.limit,
        this.form1.goodsId,
        this.id,
        this.form1.goodsName,
        this.form1.licenseNo,
        ''
      )
      if (data !== undefined) {
        this.$common.n_success('添加成功')
        this.getDialogList()
        this.getList()
      }
    },
    // 单独添加
    async add(row) {
      let data = await ControlPriceListAdd(
        this.id,
        [row.goodsId]
      )
      if (data !== undefined) {
        this.getDialogList()
        this.getList()
        this.$common.n_success('添加成功')
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
      for (let i = 0; i < this.stockDetailList.length; i++) {
        let newRealQty = ''
        if ( Number(this.stockDetailList[i].qty) - Number(this.stockDetailList[i].realQty) <= 0 ) {
          newRealQty = Number(this.stockDetailList[i].editNumber)
        } else {
          newRealQty = Number(this.stockDetailList[i].qty) - Number(this.stockDetailList[i].realQty) + Number(this.stockDetailList[i].editNumber)
        }
        let flag = this.validateNumber(newRealQty, this.stockDetailList[i].frozenQty)
        if (flag === 2) {
          return this.$message.warning('修改后的总库存必须大于或等于占有库存')
        } else if (flag === 1) {
          return this.$message.warning('请输入正整数')
        }
      }
      // this.$refs.dataForm.validate(async valid => {
      //   if (valid) {
      //     let form = this.form
      //     this.$common.showLoad()
      //     let data = await changeProductInfo(form.id, null, null, null, form.qtyNum)
      //     this.$common.hideLoad()
      //     if (data !== undefined) {
      //       this.showDialog = false
      //       this.$common.n_success('修改库存成功')
      //       this.getList()
      //     }
      //   } else {
      //     return false
      //   }
      // })
    },

    validateNumber(value, item) {
      var reg = /^((?!-1)\d{0,10})$/;//最长9位数字，可修改
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
    },
    // 删除
    delet(row) {
      this.$confirm('确定删除吗？', '删除条件', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          //确定
          this.removeGroupMethod(row.id);
        })
        .catch(() => {
          //取消
        });
    },
    async removeGroupMethod(id) {
      let data = await ContorlPriceListDelet(
        this.id,
        [id]
      )
      if (data !== undefined) {
        this.$common.n_success('删除成功')
        this.getList()
      }
    },
    goImport() {
      this.$router.push({
        path: '/importFile/importFile_index',
        query: {
          action: '/admin/b2b/api/v1/limit/price/importGoodsLimit?customerEid=' + this.id
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
