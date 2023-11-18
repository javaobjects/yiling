<template>
  <div class="app-container">
    <!-- 面包屑 -->
    <yl-topNav-view :nav-list="navList"></yl-topNav-view>
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">厂家编号</div>
              <el-input v-model="query.eid" placeholder="请输入厂家编号" @keyup.enter.native="handleSearch" />
            </el-col>
            <el-col :span="6">
              <div class="title">厂家名称</div>
              <el-input v-model="query.ename" placeholder="请输入厂家名称" @keyup.enter.native="handleSearch" />
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="16">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- 导出、导入按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton v-role-btn="['1']" type="primary" plain @click="addFactoryClick">新增</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8 pad-b-10 order-table-view">
        <yl-table
          border
          show-header
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column label="ID" min-width="120" align="center" prop="id">
          </el-table-column>
          <el-table-column label="厂家编号" min-width="100" align="center" prop="eid">
          </el-table-column>
          <el-table-column label="厂家名称" min-width="200" align="center" prop="ename">
          </el-table-column>
          <el-table-column label="厂家类型" min-width="100" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.type | dictLabel(typeArray) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="关联商品" min-width="120" align="center" prop="replyCode">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="bindGoodsClick(row)">关联商品</yl-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="200" align="center">
            <template slot-scope="{ row }">
              <div>
                {{ row.createTime | formatDate }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="创建人" min-width="200" align="center" prop="createUserName">
          </el-table-column>
          <el-table-column label="操作" min-width="180" align="center" fixed="right">
            <template slot-scope="{ row }">
              <div class="operation-view">
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 新增厂家 -->
    <yl-dialog title="新增厂家" :visible.sync="addFactoryDialog" width="600px" @confirm="addFactoryConfirm">
      <div class="dialog-content-box">
        <div class="dialog-content-top border-1px-b">
          <el-row class="mar-t-10">
            <el-col :span="10">
              <div class="font-size-base font-title-color flex-row-left">
                <span class="font-important-color mar-r-8 left-label">企业ID：</span>
                <el-input v-model="queryEnterpriseId" placeholder="请输入企业ID" />
              </div>
            </el-col>
            <el-col :span="12">
              <ylButton type="primary" plain @click="searchFactoryClick">查询</ylButton>
            </el-col>
          </el-row>
        </div>
        <div class="mar-t-16 dialog-content-bottom" style="padding-bottom: 10px;background: #FFFFFF;">
          <div class="mar-t-10">
            <span class="font-important-color mar-r-8 left-label">企业ID</span>
            <el-input v-model="addFactory.eid" disabled placeholder="" />
          </div>
          <div class="mar-t-10">
            <span class="font-important-color mar-r-8 left-label">厂家名称</span>
            <el-input v-model="addFactory.ename" disabled placeholder="" />
          </div>
          <div class="mar-t-10">
            <span class="font-important-color mar-r-8 left-label">厂家类型</span>
            <el-select v-model="addFactory.type" placeholder="请选择厂家类型">
              <el-option v-for="item in typeArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
        </div>
      </div>
    </yl-dialog>
    <!-- 已关联商品 -->
    <yl-dialog title="关联商品" :visible.sync="bindGoodsDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品编号</div>
                <el-input v-model="bindQuery.goodsId" placeholder="商品编号" @keyup.enter.native="bindHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="bindQuery.total"
                  @search="bindHandleSearch"
                  @reset="bindHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="down-box clearfix">
          <div class="btn">
            <ylButton type="primary" plain @click="addGoodsClick">添加商品</ylButton>
            <ylButton type="primary" plain @click="batchDeleteClick">批量删除</ylButton>
            <ylButton type="primary" plain >模版导出</ylButton>
            <ylButton type="primary">导入</ylButton>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            :selection-change="handleBindGoodsChange"
            :stripe="true"
            :show-header="true"
            :list="bindList"
            :total="bindQuery.total"
            :page.sync="bindQuery.page"
            :limit.sync="bindQuery.limit"
            :loading="loading1"
            @getList="getBindList"
          >
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="商品编号" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsId }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品规格" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.specifications }}</div>
              </template>
            </el-table-column>
            <el-table-column label="生产厂家" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.manufacturerName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="品牌厂家" min-width="100" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.brandManufacturerName }}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="55" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button v-role-btn="['2']" class="view-btn" type="text" @click="deleteGoodsClick(row)">删除</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
    <!-- 添加商品弹窗 -->
    <yl-dialog title="添加商品" :visible.sync="addGoodsDialog" width="966px" @confirm="addGoodsConfirm">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">商品编号</div>
                <el-input v-model="goodsQuery.goodsId" placeholder="请输入商品编号" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
              <el-col :span="8">
                <div class="title">商品名称</div>
                <el-input v-model="goodsQuery.goodsName" placeholder="请输入商品名称" @keyup.enter.native="goodsHandleSearch" />
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn
                  :total="goodsTotal"
                  @search="goodsHandleSearch"
                  @reset="goodsHandleReset"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table
            ref="multipleTable"
            :selection-change="handleSelectionChange"
            :stripe="true"
            :show-header="true"
            :list="goodsList"
            :total="goodsTotal"
            :page.sync="goodsQuery.page"
            :limit.sync="goodsQuery.limit"
            :loading="loading2"
            @getList="getGoodsList"
          >
            <el-table-column type="selection" align="center" width="70"></el-table-column>
            <el-table-column label="商品编号" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.goodsId }}</div>
              </template>
            </el-table-column>
            <el-table-column label="商品名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.goodsName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品规格" min-width="150" align="center">
              <template slot-scope="{ row }">
                <div class="font-size-base">{{ row.specifications }}</div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import {
  queryManufacturerListPage,
  queryManufacturerGoodsListPage,
  deleteManufacturer,
  getEnterpriseById,
  addManufacturer,
  queryGoodsPageList,
  addManufacturerGoods,
  deleteManufacturerGoods
} from '@/subject/pop/api/agreement';

export default {
  name: 'FactoryList',
  components: {
  },
  computed: {
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
          title: '协议管理'
        },
        {
          title: '厂家管理'
        }
      ],
      // 厂家类型
      typeArray: [
        {
          label: '生产厂家',
          value: 1
        },
        {
          label: '品牌厂家',
          value: 2
        }
      ],
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        eid: '',
        ename: ''
      },
      dataList: [],
      // 新增厂家
      addFactoryDialog: false,
      // 已关联商品
      bindGoodsDialog: false,
      loading1: false,
      bindQuery: {
        page: 1,
        limit: 10,
        total: 0,
        goodsId: ''
      },
      // 当前选中的eid
      currentEid: '',
      bindList: [],
      // 勾选的商品列表
      bindMultipleSelection: [],
      // 新增厂家 查询企业ID
      queryEnterpriseId: '',
      //
      addFactory: {
        eid: '',
        ename: '',
        type: ''
      },
      // 添加商品弹窗
      addGoodsDialog: false,
      loading2: false,
      goodsQuery: {
        page: 1,
        limit: 10
      },
      goodsList: [],
      goodsTotal: 0,
      // 表格选择
      multipleSelection: []
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await queryManufacturerListPage(
        query.page,
        query.limit,
        query.eid,
        query.ename
      );
      this.loading = false
      if (data) {
        this.dataList = data.records
        this.query.total = data.total
      }
    },
    // 搜索点击
    handleSearch() {
      this.query.page = 1
      this.getList()
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        eid: '',
        ename: ''
      }
    },
    // 新增厂家
    addFactoryClick() {
      this.queryEnterpriseId = ''
      this.addFactory = {
        eid: '',
        ename: '',
        type: ''
      }
      this.addFactoryDialog = true
    },
    // 厂家删除点击
    deleteClick(row) {
      this.$confirm('确认删除 ！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        // //确定
        let data = await deleteManufacturer(row.id)
        if (typeof data !== 'undefined') {
          this.$common.n_success('删除成功!');
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 新增厂家查询-查询企业
    async searchFactoryClick() {
      if (!this.queryEnterpriseId) {
        this.$common.warn('请输入查询企业ID')
        return
      }
      this.$common.showLoad()
      let data = await getEnterpriseById(
        this.queryEnterpriseId
      );
      this.$common.hideLoad()
      if (data) {
        this.addFactory = data
      } else {
        this.addFactory = {
          eid: '',
          ename: '',
          type: ''
        }
      }
    },
    // 新增厂家 确认点击
    async addFactoryConfirm() {
      let addFactory = this.addFactory
      if (!addFactory.eid || !addFactory.ename) {
        this.$common.warn('请选择查询企业')
        return
      }
      if (!addFactory.type) {
        this.$common.warn('请选择厂家类型')
        return
      }
      this.$common.showLoad()
      let data = await addManufacturer(
        addFactory.eid,
        addFactory.ename,
        addFactory.type
      );
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('新增成功')
        this.addFactoryDialog = false
        this.getList()
      }

    },
    //------------------管理商品弹框------------------
    // 关联商品点击
    bindGoodsClick(row) {
      this.bindQuery = {
        page: 1,
        limit: 10,
        goodsId: ''
      }
      this.bindGoodsDialog = true
      this.currentEid = row.id
      this.getBindList()
    },
    bindHandleSearch() {
      this.bindQuery.page = 1
      this.getBindList()
    },
    bindHandleReset() {
      this.bindQuery = {
        page: 1,
        limit: 10,
        goodsId: ''
      }
    },
    // 关联商品列表
    async getBindList() {
      this.loading1 = true
      let bindQuery = this.bindQuery
      let data = await queryManufacturerGoodsListPage(
        bindQuery.page,
        bindQuery.limit,
        this.currentEid,
        parseInt(bindQuery.goodsId)
      );
      this.loading1 = false
      if (data) {
        this.bindList = data.records
        this.bindQuery.total = data.total
      }
    },
    handleBindGoodsChange(val) {
      this.bindMultipleSelection = val;
    },
    async deleteGoodsClick(row) {
      let idList = []
      idList.push(row.id);
      this.$common.showLoad()
      let data = await deleteManufacturerGoods(idList)
      this.$common.hideLoad()
      if (typeof data !== 'undefined') {
        this.$common.n_success('删除成功');
        this.getBindList()
      } 
    },
    // 批量删除
    async batchDeleteClick() {
      if (this.bindMultipleSelection.length > 0) {
        let idList = []
        this.bindMultipleSelection.forEach(item => {
          idList.push(item.id);
        });
        this.$common.showLoad()
        let data = await deleteManufacturerGoods(idList)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('删除成功');
          this.getBindList()
        } 

      } else {
        this.$common.warn('请选择需要删除的商品')
      }
    },
    //------------------管理商品弹框------------------
    // 添加商品
    addGoodsClick() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
      this.addGoodsDialog = true
      this.getGoodsList()
    },
    // 商品搜索
    goodsHandleSearch() {
      this.goodsQuery.page = 1
      this.getGoodsList()
    },
    goodsHandleReset() {
      this.goodsQuery = {
        page: 1,
        limit: 10
      }
    },
    // 获取商品列表
    async getGoodsList() {
      let goodsQuery = this.goodsQuery
      this.loading2 = false
      let data = await queryGoodsPageList(
        goodsQuery.page,
        goodsQuery.limit,
        parseInt(goodsQuery.goodsId),
        goodsQuery.goodsName
      );
      this.loading2 = false
      if (data) {
        this.goodsList = data.records
        this.goodsTotal = data.total
      }
    },
    // 表格全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$log('this.multipleSelection:', val);
    },
    // 添加商品 确认点击
    async addGoodsConfirm() {
      if (this.multipleSelection.length > 0) {
        let manufacturerId = this.currentEid
        this.$common.showLoad()
        let data = await addManufacturerGoods(manufacturerId, this.multipleSelection)
        this.$common.hideLoad()
        if (typeof data !== 'undefined') {
          this.$common.n_success('添加成功');
          this.addGoodsDialog = false
          this.getBindList()
        } 

      } else {
        this.$common.warn('请选择需要添加的商品')
      }
    },
    getCellClass(row) {
      if (row.columnIndex == 4) {
        return 'border-1px-l'
      } 
      return ''
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
<style lang="scss">
  .order-table-view {
    .table-row {
      margin: 0 30px;
      td {
        .el-table__expand-icon{
          visibility: hidden;
        }
      }
    }
  }
</style>
