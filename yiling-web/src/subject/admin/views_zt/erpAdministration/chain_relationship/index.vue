<template>
  <div class="app-container">
    <!-- 内容区 -->
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">总店名称</div>
              <el-select
                filterable
                clearable
                remote
                :remote-method="e => getCompanyData(e, 1)"
                v-model="mainShopEid"
                placeholder="请输入总店名称搜索并选择"
                @clear="e => getCompanyData(e, 1)"
                :loading="searchLoading"
                @change="e => eidChange(e, 1)"
              >
                <el-option 
                  v-for="item in companyOptions" 
                  :key="item.id" 
                  :label="item.name" 
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="title">门店名称</div>
              <el-select
                filterable
                clearable
                remote
                :remote-method="e => getCompanyData(e, 2)"
                v-model="shopEid"
                placeholder="请输入门店名称搜索并选择"
                @clear="e => getCompanyData(e, 2)"
                :loading="searchShopLoading"
                @change="e => eidChange(e, 2)"
              >
                <el-option 
                  v-for="item in companyShopOptions" 
                  :key="item.id" 
                  :label="item.name" 
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="6">
              <div class="title">总店ID</div>
              <el-input v-model="query.mainShopEid" @keyup.enter.native="handleSearch" placeholder="请输入总店ID" />
            </el-col>
            <el-col :span="6">
              <div class="title">门店ID</div>
              <el-input v-model="query.shopEid" @keyup.enter.native="handleSearch" placeholder="请输入门店ID" />
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
      <!-- 导出按钮 -->
      <div class="down-box clearfix">
        <div class="btn">
          <ylButton type="primary" @click="batchSyncStatusClick">批量调整同步状态</ylButton>
          <ylButton type="primary" @click="batchDeletdClick">批量删除</ylButton>
          <ylButton type="primary" @click="importClick">导入关系</ylButton>
        </div>
      </div>
      <!-- 底部列表 -->
      <div class="mar-t-8">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList">
          <el-table-column label="#" min-width="80" align="center">
            <template slot-scope="{ $index }">
              <div class="font-size-base">{{ query.total - ((query.page - 1) * query.limit + $index) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="总店企业ID" prop="mainShopEid">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="总店名称" prop="mainShopName">
          </el-table-column>
          <el-table-column align="center" label="门店ID" prop="shopEid">
          </el-table-column>
          <el-table-column align="center" label="门店编码" prop="shopCode">
          </el-table-column>
          <el-table-column align="center" label="门店名称" prop="shopName">
          </el-table-column>
          <el-table-column align="center" min-width="120" label="对接时间">
            <template slot-scope="{ row }">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="同步状态" min-width="80">
            <template slot-scope="{ row }">
              <div class="table-switch">
                <el-switch 
                  disabled 
                  active-color="#13ce66" 
                  inactive-color="#ff4949"
                  :active-value="1" 
                  :inactive-value="0"
                  v-model="row.syncStatus"
                  @click.native="syncStatusChange(row)">
                </el-switch>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="120">
            <template slot-scope="{ row }">
              <div>
                <yl-button type="text" @click="deleteClick(row)">删除</yl-button>
              </div>
            </template>
          </el-table-column>
        </yl-table>
      </div>
    </div>
    <!-- 导入关系 -->
    <import-send-dialog :visible.sync="importSendVisible" :excel-code="info.excelCode" ref="importSendRef"></import-send-dialog>
  </div>
</template>

<script>
import {
  erpShopMappingPage,
  erpCustomerEnterpriseList,
  erpShopMappingUpdateSyncStatusByQuery,
  erpShopMappingUpdateSyncStatus,
  erpShopMappingDeleteByQuery,
  erpShopMappingDelete
} from '@/subject/admin/api/zt_api/erpAdministration'
import ImportSendDialog from '@/subject/admin/components/ImportSendDialog'

export default {
  name: 'ChainRelationship',
  components: {
    ImportSendDialog
  },
  computed: {
  },
  data() {
    return {
      loading: false,
      query: {
        page: 1,
        limit: 10,
        total: 0,
        // 总店企业id
        mainShopEid: '',
        // 门店企业id
        shopEid: ''
      },
      mainShopEid: '',
      shopEid: '',
      dataList: [],
      searchLoading: false,
      // 搜索总店企业
      companyOptions: [],
      searchShopLoading: false,
      // 搜索总店企业
      companyShopOptions: [],
      // 导入发券
      importSendVisible: false,
      // 导入任务参数 excelCode: sendCoupon-商品优惠券 
      info: {
        excelCode: 'importErpShopMapping'
      }
    };
  },
  activated() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      let query = this.query
      let data = await erpShopMappingPage(
        query.page,
        query.limit,
        query.mainShopEid,
        query.shopEid
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
        mainShopEid: '',
        shopEid: '',
        total: 0
      }
      this.mainShopEid = ''
      this.shopEid = ''
      this.companyOptions = []
      this.companyShopOptions = []
    },
    eidChange(value, type) {
      console.log(value, type)
      if (type == 1) {
        this.query.mainShopEid = value
      } else if (type == 2) {
        this.query.shopEid = value
      }
    },
    // 名字分页模糊查询企业 2、3 总店是商业或连锁总店，4、5 门店是连锁直营或连锁加盟
    async getCompanyData(query = '', type) {
      console.log(query, type)
      if (type == 1) {
        if (query.trim() !== '') {
          this.searchLoading = true
          let inTypeList = [2, 3]
          let data = await erpCustomerEnterpriseList(1, 50, inTypeList, query.trim())
          this.searchLoading = false
          if (data && data.records) {
            this.companyOptions = data.records
          }
        } else {
          this.companyOptions = []
        }
      } else if (type == 2) {
        if (query.trim() !== '') {
          this.searchShopLoading = true
          let inTypeList = [4, 5]
          let data = await erpCustomerEnterpriseList(1, 50, inTypeList, query.trim())
          this.searchShopLoading = false
          if (data && data.records) {
            this.companyShopOptions = data.records
          }
        } else {
          this.companyShopOptions = []
        }
      }
      
    },
    batchSyncStatusClick() {
      this.$confirm(
        '确认批量处理搜索结果同步状态为？', '提示', 
        {
          distinguishCancelAndClose: true,
          confirmButtonText: '开启',
          cancelButtonText: '关闭',
          type: 'warning'
        }
      )
      .then( async() => {
        //确定
        console.log('开启')
        let query = this.query
        this.$common.showLoad();
        let data = await erpShopMappingUpdateSyncStatusByQuery(
          query.mainShopEid,
          query.shopEid,
          1
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('开启成功!')
          this.getList();
        }
      })
      .catch( async(action) => {
        if (action == 'cancel') {
          // 取消
          let query = this.query
          this.$common.showLoad();
          let data = await erpShopMappingUpdateSyncStatusByQuery(
            query.mainShopEid,
            query.shopEid,
            0
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.n_success('关闭成功!')
            this.getList();
          }
        }
      });
    },
    // 修改对应关系同步状态
    syncStatusChange(row) {
      this.$confirm(
        `确认${row.syncStatus == '0' ? '开启' : '关闭'}同步状态！`, '提示', 
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      .then( async() => {
        //确定
        this.$common.showLoad()
        let data = await erpShopMappingUpdateSyncStatus(
          row.id,
          row.syncStatus == 0 ? 1 : 0
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success(`${row.syncStatus == 0 ? '开启' : '停用'} 成功!`);
          this.getList();
        }
      })
      .catch(() => {
        //取消
      });
    },
    batchDeletdClick() {
      this.$confirm(
        '确认删除所选的记录？', '提示', 
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      .then( async() => {
        //确定
        let query = this.query
        this.$common.showLoad();
        let data = await erpShopMappingDeleteByQuery(
          query.mainShopEid,
          query.shopEid
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('删除成功!')
          this.getList();
        }
      })
      .catch(() => {
        // 取消
      });
        
    },
    // 导入关系
    importClick() {
      this.importSendVisible = true
      this.$nextTick( () => {
        this.$refs.importSendRef.init()
      })
    },
    // 删除
    deleteClick(row) {
      this.$confirm('确认删除所选的记录？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(async () => {
        //确定
        this.$common.showLoad()
        let data = await erpShopMappingDelete(row.id)
        this.$common.hideLoad()
        if (typeof data != 'undefined') {
          this.$common.n_success('删除成功')
          this.getList()
        }
      })
      .catch(() => {
        //取消
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
