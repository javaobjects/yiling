<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">上架状态</div>
              <el-select v-model="query.status" placeholder="请选择">
                <el-option label="全部" :value="0"></el-option>
                <el-option v-for="item in goodStatus" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              <div class="title">药品名称</div>
              <el-input v-model="query.name" @keyup.enter.native="searchEnter" placeholder="请输入药品名称" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- table  -->
      <div class="my-table mar-t-16">
        <yl-table :show-header="true" stripe :list="dataList" :total="total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column label-class-name="mar-l-16" label="商品信息" align="left" min-width="180">
            <template slot-scope="{ row }">
              <div class="item text-l mar-l-16">
                <div class="item-text font-size-lg bold clamp-t-1">{{ row.goodsName }}</div>
                <div class="item-text font-size-base font-title-color"><span>{{ row.sellSpecifications }}</span></div>
                <div class="item-text font-size-base font-title-color"><span>批准文号：{{ row.licenseNo }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品ID" align="center" min-width="150">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color">标准库SPU-ID：<span class="font-important-color">{{ row.standardId }}</span></div>
                <div class="item-text font-size-base font-title-color">标准库SKU-ID：<span class="font-important-color">{{ row.skuId }}</span></div>
                <div class="item-text font-size-base font-title-color">企业商品ID：<span class="font-important-color">{{ row.goodsId }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base"><span>{{ row.goodsStatus | dictLabel(goodStatus) }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="标准库分类" align="center">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.standardCategoryName }}</span></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="销售价" align="center" min-width="100">
            <template slot-scope="{ row }">
              {{ row.ihGoodsInfoVO.salePrice ? '￥' + row.ihGoodsInfoVO.salePrice : '' }}
            </template>
          </el-table-column>
          <el-table-column label="成本价" align="center" min-width="100">
            <template slot-scope="{ row }">
              {{ row.ihGoodsInfoVO.marketPrice ? '￥' + row.ihGoodsInfoVO.marketPrice : '' }}
            </template>
          </el-table-column>
          <el-table-column label="库存" align="center" min-width="100">
            <template slot-scope="{ row }">
              {{ row.ihGoodsInfoVO ? row.ihGoodsInfoVO.stock : '' }}
            </template>
          </el-table-column>
          <el-table-column label="安全库存" align="center" min-width="100">
            <template slot-scope="{ row }">
              {{ row.ihGoodsInfoVO ? row.ihGoodsInfoVO.safeStock : '' }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center">
            <template slot-scope="{ row }">
              <div class="item item-center">
                <div class="item-text font-size-base font-title-color"><span>{{ row.createTime | formatDate }}</span></div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" min-width="130">
            <template slot-scope="{ row, $index }">
              <div class="item item-center">
                <yl-button v-role-btn="['1']" type="text" @click="showDetail(row)">药+险库存详情</yl-button>
              </div>
              <div class="item item-center">
                <yl-button type="text" v-role-btn="['2']" :disabled="row.ihGoodsInfoVO.id == 0" @click="stockDetail(row, $index)">销售价格库存信息</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <!-- 库存弹窗 -->
      <yl-dialog title="库存信息" :visible.sync="showDialog" :show-confirm="false">
        <div class="dia-content">
          <div class="dia-content-title">
            <div class="title-text bold">{{ editInfo.goodsName }}</div>
            <div><span>规格：{{ editInfo.sellSpecifications?editInfo.sellSpecifications :'--' }}</span><span class="mar-l-40">批准文号:{{ editInfo.licenseNo?editInfo.licenseNo:'--' }}</span></div>
          </div>
          <div class="mar-t-16">
            <yl-table :show-header="true" stripe :list="stockDetailList">
              <el-table-column label="药+险所需库存数量" align="center" prop="frozenQty"></el-table-column>
              <el-table-column label="当前实际库存" align="center" prop="qty">
                <template slot-scope="scope">
                  <div>
                    <div v-if="editIndex === scope.$index">
                      <el-input 
                        v-model="editNumber" 
                        size="normal" 
                        clearable 
                        width="80px"
                        @input="e => (editNumber = checkInput(e))"
                        >
                      </el-input>
                    </div>
                    <div v-else>
                      <span>{{ scope.row.qty }}</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <div>
                    <div v-if="editIndex === scope.$index">
                      <yl-button type="text" @click="submit(scope.row,scope.$index)">保存</yl-button>
                      <yl-button type="text" @click="cancel(scope.row)">取消</yl-button>
                    </div>
                    <div v-else>
                      <yl-button type="text" @click="edit(scope.row,scope.$index)">修改</yl-button>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </yl-dialog>
      <!-- 销售价格库存信息 -->
      <yl-dialog title="销售价格库存信息" :visible.sync="showDialog2" :show-confirm="false">
        <div class="dia-content">
          <div class="dia-content-title">
            <div class="title-text bold">{{ stock.goodsName }}</div>
            <div><span>规格：{{ stock.sellSpecifications ? stock.sellSpecifications :'--' }}</span><span class="mar-l-40">批准文号:{{ stock.licenseNo ? stock.licenseNo:'--' }}</span></div>
          </div>
          <div class="mar-t-16">
            <yl-table :show-header="true" stripe :list="stockList">
              <el-table-column label="销售价" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="numValue">
                      <el-input 
                        v-model="row.salePrice" 
                        size="normal" 
                        clearable 
                        width="80px"
                        @input="e => (row.salePrice = checkInput2(e))"
                        >
                      </el-input>
                    </div>
                    <div v-else>
                      <span>{{ row.salePrice }}</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="成本价" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="numValue">
                      <el-input 
                        v-model="row.marketPrice" 
                        size="normal" 
                        clearable 
                        width="80px"
                        @input="e => (row.marketPrice = checkInput2(e))"
                        >
                      </el-input>
                    </div>
                    <div v-else>
                      <span>{{ row.marketPrice }}</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="库存" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="numValue">
                      <el-input 
                        v-model="row.stock" 
                        size="normal" 
                        clearable 
                        width="80px"
                        @input="e => (row.stock = checkInput(e))"
                        >
                      </el-input>
                    </div>
                    <div v-else>
                      <span>{{ row.stock }}</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="安全库存" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="numValue">
                      <el-input 
                        v-model="row.safeStock" 
                        size="normal" 
                        clearable 
                        width="80px"
                        @input="e => (row.safeStock = checkInput(e))"
                        >
                      </el-input>
                    </div>
                    <div v-else>
                      <span>{{ row.safeStock }}</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="{ row }">
                  <div>
                    <div v-if="numValue">
                      <yl-button type="text" @click="stockSubmit(row)">保存</yl-button>
                      <yl-button type="text" @click="stockCancel">取消</yl-button>
                    </div>
                    <div v-else>
                      <yl-button type="text" @click="stockEdit">修改</yl-button>
                    </div>
                  </div>
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
import { getCmpGoodsList, editSkuNumber, updateGoodsPriceAndStock } from '@/subject/pop/api/cmp_api/product'
import { goodsStatus } from '@/subject/pop/utils/busi'
export default {
  name: 'CmpProductsIndex',
  components: {
  },
  computed: {
    goodStatus() {
      return goodsStatus()
    }
  },
  data() {
    return {
      // 头部导航
      navList: [
        {
          title: '首页',
          path: ''
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
        limit: 20,
        name: '',
        // 全部，已上架，已下架 ，带设置
        status: 0
      },
      dataList: [],
      loading: false,
      total: 0,
      showDialog: false,
      stockDetailList: [],
      editIndex: -1,
      editNumber: '',
      editInfo: {},
      //销售价格库存信息
      showDialog2: false,
      stock: {
        goodsName: '',
        sellSpecifications: '',
        licenseNo: ''
      },
      stockList: [],
      numValue: 0,
      stockIndex: 0
    }
  },
  activated() {
    this.getList();
  },
  created() {
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
    //  获取商品列表
    async getList() {
      this.loading = true
      let query = this.query
      let data = await getCmpGoodsList(
        query.page,
        query.limit,
        query.status, // 状态
        query.name
      )
      this.loading = false
      console.log(data);
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
        status: ''
      }
    },

    // 展示库存弹框
    showDetail(row) {
      this.editIndex = -1
      this.stockDetailList = []
      this.editInfo = row
      this.showDialog = true
      let obj = {
        frozenQty: row.frozenQty,
        skuId: row.skuId,
        qty: row.qty
      }
      this.stockDetailList.push(obj)
    },
    // 库存详情
    edit(e, index) {
      this.editNumber = e.qty
      this.editIndex = index
      this.editInfo = e;
    },
    async submit(row, index) {
      this.$common.showLoad()
      let data = await editSkuNumber(this.editNumber, row.skuId)
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$set(this.stockDetailList[index], 'qty', this.editNumber)
        this.editNumber = ''
        this.editIndex = -1
        this.getList();
      }

    },
    cancel() {
      this.editNumber = ''
      this.editIndex = -1
    },
    //点击销售价格库存信息
    stockDetail(row, index) {
      this.showDialog2 = true;
      this.stock = row;
      this.stockIndex = index;
      this.stockList = [{ ...row.ihGoodsInfoVO }]
    },
    //点击修改
    stockEdit() {
      this.numValue = 1
    },
    //点击取消
    stockCancel() {
      this.numValue = 0;
      this.stockList = [{ ...this.dataList[this.stockIndex].ihGoodsInfoVO }]
    },
    //点击保存
    async stockSubmit(row) {
      this.$common.showLoad()
      let data = await updateGoodsPriceAndStock(
        row.id,
        row.salePrice,
        row.marketPrice,
        row.stock,
        row.safeStock
      )
      this.$common.hideLoad()
      if (data !== undefined) {
        this.$common.n_success('保存成功')
        this.numValue = 0
        this.getList()
      }
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      return val
    },
    checkInput2(value) {
      //先把非数字的都替换掉，除了数字和.
      value = value.replace(/[^\d.]/g, '')
      //保证只有出现一个.而没有多个.
      value = value.replace(/\.{2,}/g, '.')
      //必须保证第一个为数字而不是.
      value = value.replace(/^\./g, '')
      //保证.只出现一次，而不能出现两次以上
      value = value.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
      //只能输入两个小数
      value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3')
      return value
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
